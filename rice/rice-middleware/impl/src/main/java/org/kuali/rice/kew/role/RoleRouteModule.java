/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.role;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNodeUtils;
import org.kuali.rice.kew.routemodule.RouteModule;
import org.kuali.rice.kew.rule.XmlConfiguredAttribute;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.ResponsibleParty;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.responsibility.ResponsibilityAction;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RoleRouteModule is responsible for interfacing with the KIM
 * Role system to provide Role-based routing to KEW. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoleRouteModule implements RouteModule {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RoleRouteModule.class);
	
	protected static final String QUALIFIER_RESOLVER_ELEMENT = KewApiConstants.ROLEROUTE_QUALIFIER_RESOLVER_ELEMENT;
	protected static final String QUALIFIER_RESOLVER_CLASS_ELEMENT = KewApiConstants.ROLEROUTE_QUALIFIER_RESOLVER_CLASS_ELEMENT;
	protected static final String RESPONSIBILITY_TEMPLATE_NAME_ELEMENT = KewApiConstants.ROLEROUTE_RESPONSIBILITY_TEMPLATE_NAME_ELEMENT;
	protected static final String NAMESPACE_ELEMENT = KewApiConstants.ROLEROUTE_NAMESPACE_ELEMENT;
	
	private static ResponsibilityService responsibilityService;
	
	private String qualifierResolverName;
	private String qualifierResolverClassName;
	private String responsibilityTemplateName;
	private String namespace;

    @Override
    public boolean isMoreRequestsAvailable(RouteContext context) {
        return false;
    }

	@SuppressWarnings("unchecked")
	public List<ActionRequestValue> findActionRequests(RouteContext context)
			throws Exception {
		
		ActionRequestFactory arFactory = new ActionRequestFactory(context.getDocument(), context.getNodeInstance());

		QualifierResolver qualifierResolver = loadQualifierResolver(context);
		List<Map<String, String>> qualifiers = qualifierResolver.resolve(context);
		String responsibilityTemplateName = loadResponsibilityTemplateName(context);
		String namespaceCode = loadNamespace(context);
		Map<String, String> responsibilityDetails = loadResponsibilityDetails(context);
		if (LOG.isDebugEnabled()) {
			logQualifierCheck(namespaceCode, responsibilityTemplateName, responsibilityDetails, qualifiers);
		}
		if ( qualifiers != null ) {
			for (Map<String, String> qualifier : qualifiers) {
				if ( qualifier.containsKey( KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER ) ) {
					responsibilityDetails.put(KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER, qualifier.get(KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER));
				} else {
					responsibilityDetails.remove( KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER );
				}
				List<ResponsibilityAction> responsibilities = getResponsibilityService().getResponsibilityActionsByTemplate(
                        namespaceCode, responsibilityTemplateName, qualifier, responsibilityDetails);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Found " + responsibilities.size() + " responsibilities from ResponsibilityService");
				}
				// split the responsibility list defining characteristics (per the ResponsibilitySet.matches() method)
				List<ResponsibilitySet> responsibilitySets = partitionResponsibilities(responsibilities);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Found " + responsibilitySets.size() + " responsibility sets from ResponsibilityActionInfo list");
				}
				for (ResponsibilitySet responsibilitySet : responsibilitySets) {
					String approvePolicy = responsibilitySet.getApprovePolicy();
					// if all must approve, add the responsibilities individually so that the each get their own approval graph
					if (ActionRequestPolicy.ALL.getCode().equals(approvePolicy)) {
						for (ResponsibilityAction responsibility : responsibilitySet.getResponsibilities()) {
							arFactory.addRoleResponsibilityRequest(Collections.singletonList(responsibility), approvePolicy);
						}
					} else {
						// first-approve policy, pass as groups to the ActionRequestFactory so that a single approval per set will clear the action request
						arFactory.addRoleResponsibilityRequest(responsibilitySet.getResponsibilities(), approvePolicy);
					}
				}
			}		
		}
		List<ActionRequestValue> actionRequests = new ArrayList<ActionRequestValue>(arFactory.getRequestGraphs());
		disableResolveResponsibility(actionRequests);
		return actionRequests;
	}
	
    protected void logQualifierCheck(String namespaceCode, String responsibilityName, Map<String, String> responsibilityDetails, List<Map<String, String>> qualifiers ) {
		StringBuilder sb = new StringBuilder();
		sb.append(  '\n' );
		sb.append( "Get Resp Actions: " ).append( namespaceCode ).append( "/" ).append( responsibilityName ).append( '\n' );
		sb.append( "             Details:\n" );
		if ( responsibilityDetails != null ) {
			sb.append( responsibilityDetails );
		} else {
			sb.append( "                         [null]\n" );
		}
		sb.append( "             Qualifiers:\n" );
		for (Map<String, String> qualification : qualifiers) {
			if ( qualification != null ) {
				sb.append( qualification );
			} else {
				sb.append( "                         [null]\n" );
			}
		}
		if (LOG.isTraceEnabled()) { 
			LOG.trace( sb.append(ExceptionUtils.getStackTrace(new Throwable())));
		} else {
			LOG.debug(sb.toString());
		}
    }

    /**
	 * Walks the ActionRequest graph and disables responsibility resolution on those ActionRequests.
	 * Because of the fact that it's not possible to tell if an ActionRequest was generated by
	 * KIM once it's been saved in the database, we want to disable responsibilityId
	 * resolution on the RouteModule because we will end up geting a reference to FlexRM and
	 * a call to resolveResponsibilityId will fail.
	 * 
	 * @param actionRequests
	 */
	protected void disableResolveResponsibility(List<ActionRequestValue> actionRequests) {
		for (ActionRequestValue actionRequest : actionRequests) {
			actionRequest.setResolveResponsibility(false);
			disableResolveResponsibility(actionRequest.getChildrenRequests());
		}
	}

	protected QualifierResolver loadQualifierResolver(RouteContext context) {
		if (StringUtils.isBlank(qualifierResolverName)) {
			this.qualifierResolverName = RouteNodeUtils.getValueOfCustomProperty(context.getNodeInstance().getRouteNode(), QUALIFIER_RESOLVER_ELEMENT);
		}
		if (StringUtils.isBlank(qualifierResolverClassName)) {			
			this.qualifierResolverClassName = RouteNodeUtils.getValueOfCustomProperty(context.getNodeInstance().getRouteNode(), QUALIFIER_RESOLVER_CLASS_ELEMENT);
		}
		QualifierResolver resolver = null;
		if (!StringUtils.isBlank(qualifierResolverName)) {
			//RuleAttribute ruleAttribute = KEWServiceLocator.getRuleAttributeService().findByName(qualifierResolverName);
            ExtensionDefinition extDef = KewApiServiceLocator.getExtensionRepositoryService().getExtensionByName(qualifierResolverName);
			if (extDef == null) {
				throw new RiceRuntimeException("Failed to locate QualifierResolver for name: " + qualifierResolverName);
			}
            resolver = ExtensionUtils.loadExtension(extDef, extDef.getApplicationId());
			if (resolver instanceof XmlConfiguredAttribute) {
				((XmlConfiguredAttribute)resolver).setExtensionDefinition(extDef);
			}
		}
		if (resolver == null && !StringUtils.isBlank(qualifierResolverClassName)) {
			resolver = (QualifierResolver)GlobalResourceLoader.getObject(new ObjectDefinition(qualifierResolverClassName));
		}
		if (resolver == null) {
			resolver = new NullQualifierResolver();
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Resolver class being returned: " + resolver.getClass().getName());
		}
		return resolver;
	}
	
	protected Map<String, String> loadResponsibilityDetails(RouteContext context) {
		String documentTypeName = context.getDocument().getDocumentType().getName();
		String nodeName = context.getNodeInstance().getName();
		Map<String, String> responsibilityDetails = new HashMap<String, String>();
		responsibilityDetails.put(KewApiConstants.DOCUMENT_TYPE_NAME_DETAIL, documentTypeName);
		responsibilityDetails.put(KewApiConstants.ROUTE_NODE_NAME_DETAIL, nodeName);
		return responsibilityDetails;
	}
	
	protected String loadResponsibilityTemplateName(RouteContext context) {
		if (StringUtils.isBlank(responsibilityTemplateName)) {
			this.responsibilityTemplateName = RouteNodeUtils.getValueOfCustomProperty(context.getNodeInstance().getRouteNode(), RESPONSIBILITY_TEMPLATE_NAME_ELEMENT);
		}
		if (StringUtils.isBlank(responsibilityTemplateName)) {
			this.responsibilityTemplateName = KewApiConstants.DEFAULT_RESPONSIBILITY_TEMPLATE_NAME;
		}
		return responsibilityTemplateName;
	}
	
	protected String loadNamespace(RouteContext context) {
		if (StringUtils.isBlank(namespace)) {
			this.namespace = RouteNodeUtils.getValueOfCustomProperty(context.getNodeInstance().getRouteNode(), NAMESPACE_ELEMENT);
		}
		if (StringUtils.isBlank(namespace)) {
			this.namespace = KewApiConstants.KEW_NAMESPACE;
		}
		return namespace;
	}
	
    protected ObjectDefinition getAttributeObjectDefinition(RuleAttribute ruleAttribute) {
    	return new ObjectDefinition(ruleAttribute.getResourceDescriptor(), ruleAttribute.getApplicationId());
    }
    
    protected List<ResponsibilitySet> partitionResponsibilities(List<ResponsibilityAction> responsibilities) {
    	List<ResponsibilitySet> responsibilitySets = new ArrayList<ResponsibilitySet>();
    	for (ResponsibilityAction responsibility : responsibilities) {
    		ResponsibilitySet targetResponsibilitySet = null;
    		for (ResponsibilitySet responsibiliySet : responsibilitySets) {
    			if (responsibiliySet.matches(responsibility)) {
    				targetResponsibilitySet = responsibiliySet;
    			}
    		}
    		if (targetResponsibilitySet == null) {
    			targetResponsibilitySet = new ResponsibilitySet(responsibility);
    			responsibilitySets.add(targetResponsibilitySet);
    		}
    		targetResponsibilitySet.getResponsibilities().add(responsibility);
    	}
    	return responsibilitySets;
    }
	
	/**
	 * Return null so that the responsibility ID will remain the same.
	 *
	 * @see org.kuali.rice.kew.routemodule.RouteModule#resolveResponsibilityId(String)
	 */
	public ResponsibleParty resolveResponsibilityId(String responsibilityId)
			throws WorkflowException {
		return null;
	}
	
	
	
	private static class ResponsibilitySet {
		private String actionRequestCode;
		private String approvePolicy;
		private Integer priorityNumber;
		private String parallelRoutingGroupingCode;
		private String roleResponsibilityActionId;
		private List<ResponsibilityAction> responsibilities = new ArrayList<ResponsibilityAction>();

		public ResponsibilitySet(ResponsibilityAction responsibility) {
			this.actionRequestCode = responsibility.getActionTypeCode();
			this.approvePolicy = responsibility.getActionPolicyCode();
			this.priorityNumber = responsibility.getPriorityNumber();
			this.parallelRoutingGroupingCode = responsibility.getParallelRoutingGroupingCode();
			this.roleResponsibilityActionId = responsibility.getRoleResponsibilityActionId();
		}
		
		public boolean matches(ResponsibilityAction responsibility) {
			return responsibility.getActionTypeCode().equals(actionRequestCode) &&
				responsibility.getActionPolicyCode().equals(approvePolicy) && 
				responsibility.getPriorityNumber().equals( priorityNumber ) &&
				responsibility.getParallelRoutingGroupingCode().equals( parallelRoutingGroupingCode ) &&
				responsibility.getRoleResponsibilityActionId().equals( roleResponsibilityActionId );
		}

		public String getActionRequestCode() {
			return this.actionRequestCode;
		}

		public String getApprovePolicy() {
			return this.approvePolicy;
		}
		
		public Integer getPriorityNumber() {
			return priorityNumber;
		}

		public List<ResponsibilityAction> getResponsibilities() {
			return this.responsibilities;
		}

		public String getParallelRoutingGroupingCode() {
			return this.parallelRoutingGroupingCode;
		}

		public String getRoleResponsibilityActionId() {
			return this.roleResponsibilityActionId;
		}		
		
	}



	/**
	 * @param qualifierResolverName the qualifierResolverName to set
	 */
	public void setQualifierResolverName(String qualifierResolverName) {
		this.qualifierResolverName = qualifierResolverName;
	}

	/**
	 * @param qualifierResolverClassName the qualifierResolverClassName to set
	 */
	public void setQualifierResolverClassName(String qualifierResolverClassName) {
		this.qualifierResolverClassName = qualifierResolverClassName;
	}

	/**
	 * @param responsibilityTemplateName the responsibilityTemplateName to set
	 */
	public void setResponsibilityTemplateName(String responsibilityTemplateName) {
		this.responsibilityTemplateName = responsibilityTemplateName;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	protected ResponsibilityService getResponsibilityService() {
		if ( responsibilityService == null ) {
			responsibilityService = KimApiServiceLocator.getResponsibilityService();
		}
		return responsibilityService;
	}

}
