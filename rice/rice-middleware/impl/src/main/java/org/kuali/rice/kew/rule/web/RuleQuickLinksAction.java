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
package org.kuali.rice.kew.rule.web;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.engine.node.BranchPrototype;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeConfigParam;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.service.RuleServiceInternal;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;


/**
 * A Struts Action for building and interacting with the Rule Quick Links.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleQuickLinksAction extends KewKualiAction {

	private static final Logger LOG = Logger.getLogger(RuleQuickLinksAction.class);
	
	private MaintenanceDocumentDictionaryService maintenanceDocumentDictionaryService;
	private DocumentHelperService documentHelperService;

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	makeLookupPathParam(mapping, request);
    	establishRequiredState(request, form);
        return mapping.findForward("basic");
    }

    @SuppressWarnings("unchecked")
	public ActionMessages establishRequiredState(HttpServletRequest request, ActionForm form) throws Exception {
        RuleQuickLinksForm qlForm = (RuleQuickLinksForm) form;
        List<DocumentType> documentTypes;
        if (qlForm.getRootDocTypeName() != null) {
        	documentTypes = new ArrayList<DocumentType>();
            DocumentType docType = getDocumentTypeService().findByName(qlForm.getRootDocTypeName());
            documentTypes.add(docType);
            request.setAttribute("renderOpened", Boolean.TRUE);
        } else {
        	documentTypes = getDocumentTypeService().findAllCurrentRootDocuments();
        }
        qlForm.setDocumentTypeQuickLinksStructures(getDocumentTypeDataStructure(documentTypes));
    	int shouldDisplayCount = 0;
        for ( DocumentTypeQuickLinksStructure dt : qlForm.getDocumentTypeQuickLinksStructures() ) {
        	if ( dt.isShouldDisplay() ) {
        		shouldDisplayCount++;
        	}
        }
    	if ( shouldDisplayCount == 1 ) {
            request.setAttribute("renderOpened", Boolean.TRUE);
    	}
        String documentTypeName = getMaintenanceDocumentDictionaryService().getDocumentTypeName(DocumentType.class);
        try {
            if ((documentTypeName != null) && getDocumentHelperService().getDocumentAuthorizer(documentTypeName).canInitiate(documentTypeName, GlobalVariables.getUserSession().getPerson())) {
                qlForm.setCanInitiateDocumentTypeDocument( true );
            }
        } catch (Exception ex) {
			// just skip - and don't display links
        	LOG.error( "Unable to check initiation permission for "+ documentTypeName, ex );
		}

        return null;
    }

    public ActionForward addDelegationRule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward result = null;

        String ruleTemplateId = request.getParameter("delegationRule.ruleTemplate.id");
        String docTypeName = request.getParameter("delegationRule.documentType.name");
        List rules = getRuleService().search(docTypeName, null, ruleTemplateId, "", null, null, Boolean.FALSE, Boolean.TRUE, new HashMap(), null);
        
        if (rules.size() == 1) {
            RuleBaseValues rule = (RuleBaseValues)rules.get(0);
            String url = ConfigContext.getCurrentContextConfig().getKEWBaseURL() +
                "/DelegateRule.do?methodToCall=start" +
            	"&parentRuleId=" + rule.getId();
            result = new ActionForward(url, true);
        } else {
        	makeLookupPathParam(mapping, request);
        	result = new ActionForward(ConfigContext.getCurrentContextConfig().getKRBaseURL() + 
        			"/lookup.do?methodToCall=start&"+ stripMethodToCall(request.getQueryString()), true);
        }
        
        return result;
    }

	private List getDocumentTypeDataStructure(List rootDocuments) {
		List documentTypeQuickLinksStructures = new ArrayList();
		for (Iterator iter = rootDocuments.iterator(); iter.hasNext();) {
			DocumentTypeQuickLinksStructure quickLinkStruct =new DocumentTypeQuickLinksStructure((DocumentType) iter.next());
			if (! quickLinkStruct.getFlattenedNodes().isEmpty() || ! quickLinkStruct.getChildrenDocumentTypes().isEmpty()) {
				documentTypeQuickLinksStructures.add(quickLinkStruct);
			}

		}

		return documentTypeQuickLinksStructures;
	}


	/**
     * A bean to hold a DocumentType with its flattened nodes for rendering purposes
     * on the quick links.
     *
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static class DocumentTypeQuickLinksStructure {
        private DocumentType documentType;
        private List<RouteNode> flattenedNodes = new ArrayList<RouteNode>();
        private List<DocumentTypeQuickLinksStructure> childrenDocumentTypes = new ArrayList<DocumentTypeQuickLinksStructure>();
        private List<Permission> permissions = null;
        
        private DocumentTypeQuickLinksStructure(DocumentType documentType) {
			this.documentType = documentType;
			if ( documentType != null ) {
				List<RouteNode> tempFlattenedNodes = KEWServiceLocator.getRouteNodeService()
						.getFlattenedNodes( documentType, true );
				for ( RouteNode routeNode : tempFlattenedNodes ) {
					if ( routeNode.isFlexRM() || routeNode.isRoleNode() ) {
						flattenedNodes.add( new RouteNodeForDisplay( routeNode ) );
					}
				}
                Collections.sort(flattenedNodes,new Comparator() {
                    public int compare(Object o1, Object o2) {
                        return ( ((RouteNode)o1).getRouteNodeName().compareTo(((RouteNode)o2).getRouteNodeName()));
                    }
                });
				for ( Iterator<DocumentType> iter = documentType.getChildrenDocTypes().iterator(); iter.hasNext(); ) {
					childrenDocumentTypes.add( new DocumentTypeQuickLinksStructure( iter.next() ) );
				}
                Collections.sort(childrenDocumentTypes,new Comparator() {
                    public int compare(Object o1, Object o2) {
                         return ( ((DocumentTypeQuickLinksStructure)o1).documentType.getLabel().compareTo(((DocumentTypeQuickLinksStructure)o2).documentType.getLabel()));
                    }
                });
			}
		}

        public List getChildrenDocumentTypes() {
            return childrenDocumentTypes;
        }
        public void setChildrenDocumentTypes(List<DocumentTypeQuickLinksStructure> childrenDocumentTypes) {
            this.childrenDocumentTypes = childrenDocumentTypes;
        }
        public DocumentType getDocumentType() {
            return documentType;
        }
        public void setDocumentType(DocumentType documentType) {
            this.documentType = documentType;
        }
        public List getFlattenedNodes() {
            return flattenedNodes;
        }
        public void setFlattenedNodes(List<RouteNode> flattenedNodes) {
            this.flattenedNodes = flattenedNodes;
        }
        public boolean isShouldDisplay() {
//			if (flattenedNodes.isEmpty()) {
//				for (DocumentTypeQuickLinksStructure docType : childrenDocumentTypes) {
//					if (docType.isShouldDisplay()) {
//						return true;
//					}
//				}
//				return false;
//			}
			return true;
		}

		public List<Permission> getPermissions() {
			if ( permissions == null ) {
                Predicate p = and(
                        equal("attributeName", "documentTypeName"),
                        equal("active", "Y"),
                        equal("detailCriteria",
						KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME+"="+getDocumentType().getName()));
				permissions = KimApiServiceLocator.getPermissionService().findPermissions( QueryByCriteria.Builder.fromPredicates(p)).getResults();
//				sqlLogger.setLevel( Level.INFO );
			}
			return permissions;
		}
		
		public boolean isHasRelatedPermissions() {
			return !getPermissions().isEmpty();
		}
		
		public int getRelatedPermissionCount() {
			return getPermissions().size();
		}
    }

    public static class RouteNodeForDisplay extends RouteNode {
    	private static final long serialVersionUID = 1L;
		private RouteNode baseNode;
    	
		public RouteNodeForDisplay( RouteNode baseNode ) {
			this.baseNode = baseNode;
		}

		public boolean equals(Object obj) {
			return this.baseNode.equals(obj);
		}

		public String getActivationType() {
			return this.baseNode.getActivationType();
		}

		public BranchPrototype getBranch() {
			return this.baseNode.getBranch();
		}

		public List<RouteNodeConfigParam> getConfigParams() {
			return this.baseNode.getConfigParams();
		}

		public String getContentFragment() {
			return this.baseNode.getContentFragment();
		}

		public DocumentType getDocumentType() {
			return this.baseNode.getDocumentType();
		}

		public String getDocumentTypeId() {
			return this.baseNode.getDocumentTypeId();
		}
		public Group getExceptionWorkgroup() {
			return this.baseNode.getExceptionWorkgroup();
		}
		public String getExceptionWorkgroupId() {
			return this.baseNode.getExceptionWorkgroupId();
		}
		public String getExceptionWorkgroupName() {
			return this.baseNode.getExceptionWorkgroupName();
		}
		public Boolean getFinalApprovalInd() {
			return this.baseNode.getFinalApprovalInd();
		}
		public Integer getLockVerNbr() {
			return this.baseNode.getLockVerNbr();
		}
		public Boolean getMandatoryRouteInd() {
			return this.baseNode.getMandatoryRouteInd();
		}
		public List<RouteNode> getNextNodes() {
			return this.baseNode.getNextNodes();
		}
		public String getNodeType() {
			return this.baseNode.getNodeType();
		}
		public List<RouteNode> getPreviousNodes() {
			return this.baseNode.getPreviousNodes();
		}
		public String getRouteMethodCode() {
			return this.baseNode.getRouteMethodCode();
		}
		public String getRouteMethodName() {
			return this.baseNode.getRouteMethodName();
		}
		public String getRouteNodeId() {
			return this.baseNode.getRouteNodeId();
		}
		public String getRouteNodeName() {
			return this.baseNode.getRouteNodeName();
		}
		public RuleTemplateBo getRuleTemplate() {
			return this.baseNode.getRuleTemplate();
		}
		public int hashCode() {
			return this.baseNode.hashCode();
		}
		public boolean isExceptionGroupDefined() {
			return this.baseNode.isExceptionGroupDefined();
		}
		public boolean isFlexRM() {
			return this.baseNode.isFlexRM();
		}
		public boolean isRoleNode() {
			return this.baseNode.isRoleNode();
		}
		public String toString() {
			return this.baseNode.toString();
		}
		
		private List<Responsibility> responsibilities = null;
		
		public List<Responsibility> getResponsibilities() {
			if ( responsibilities == null ) {
				QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
                Predicate p = and(
                    equal("template.namespaceCode", KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE),
                    equal("template.name", KewApiConstants.DEFAULT_RESPONSIBILITY_TEMPLATE_NAME),
                    equal("active", "Y"),
                    equal("attributes[documentTypeName]", getDocumentType().getName())
                    // KULRICE-8538 -- Check the route node by looping through the results below.  If it is added
                    // into the predicate, no rows are ever returned.
                    // equal("attributes[routeNodeName]", getRouteNodeName())
                );


                builder.setPredicates(p);

                List<Responsibility> possibleResponsibilities =
                    KimApiServiceLocator.getResponsibilityService().findResponsibilities(builder.build()).getResults();

                if ( !possibleResponsibilities.isEmpty() ) {
                    for ( Responsibility resp : possibleResponsibilities ) {
                        String routeNodeName = resp.getAttributes().get( KimConstants.AttributeConstants.ROUTE_NODE_NAME);
                        if (StringUtils.isNotEmpty(routeNodeName) && StringUtils.equals(routeNodeName, getRouteNodeName())){
                             responsibilities.add(resp);
                        }
                    }
                } else {
                    responsibilities = possibleResponsibilities;
                }
			}
			return responsibilities;
		}
		
		public int getResponsibilityCount() {
			return getResponsibilities().size();
		}
		
		public boolean isHasResponsibility() {
			return !getResponsibilities().isEmpty();
		}
    }
    
    private void makeLookupPathParam(ActionMapping mapping, HttpServletRequest request) {
    	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + mapping.getModuleConfig().getPrefix();
    	request.setAttribute("basePath", basePath);
    }

    private String stripMethodToCall(String queryString) {
        return queryString.replaceAll("methodToCall=addDelegationRule&", "");
    }

    private DocumentTypeService getDocumentTypeService() {
        return KEWServiceLocator.getDocumentTypeService();
    }

    private RuleServiceInternal getRuleService() {
    	return KEWServiceLocator.getRuleService();
    }
    
	public DocumentHelperService getDocumentHelperService() {
		if(documentHelperService == null){
			documentHelperService = KNSServiceLocator.getDocumentHelperService();
		}
		return documentHelperService;
	}

	public MaintenanceDocumentDictionaryService getMaintenanceDocumentDictionaryService() {
		if(maintenanceDocumentDictionaryService == null){
			maintenanceDocumentDictionaryService = KNSServiceLocator.getMaintenanceDocumentDictionaryService();
		}
		return maintenanceDocumentDictionaryService;
	}

	@Override
	public ActionForward toggleTab(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		establishRequiredState(request, form);
		return super.toggleTab(mapping, form, request, response);
	}

}
