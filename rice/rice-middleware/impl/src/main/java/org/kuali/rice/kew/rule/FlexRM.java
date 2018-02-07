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
package org.kuali.rice.kew.rule;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.rule.RuleDelegation;
import org.kuali.rice.kew.api.rule.RuleResponsibility;
import org.kuali.rice.kew.api.rule.RuleService;
import org.kuali.rice.kew.api.rule.RuleTemplateAttribute;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.user.RoleRecipient;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kew.util.ResponsibleParty;
import org.kuali.rice.kew.util.Utilities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Generates Action Requests for a Document using the rule system and the specified
 * {@link org.kuali.rice.kew.rule.bo.RuleTemplateBo}.
 *
 * @see ActionRequestValue
 * @see org.kuali.rice.kew.rule.bo.RuleTemplateBo
 * @see RuleBaseValues
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FlexRM {

	private static final Logger LOG = Logger.getLogger(FlexRM.class);

	/**
	 * The default type of rule selector implementation to use if none is explicitly
	 * specified for the node.
	 */
	public static final String DEFAULT_RULE_SELECTOR = "Template";
	/**
	 * Package in which rule selector implementations live
	 */
	private static final String RULE_SELECTOR_PACKAGE = "org.kuali.rice.kew.rule";
	/**
	 * The class name suffix all rule selectors should have; e.g. FooRuleSelector
	 */
	private static final String RULE_SELECTOR_SUFFIX= "RuleSelector";

	private final Timestamp effectiveDate;
	/**
	 * An accumulator that keeps track of the number of rules that have been selected over the lifespan of
	 * this FlexRM instance.
	 */
	private int selectedRules;

	public FlexRM() {
		this.effectiveDate = null;
	}

	public FlexRM(Timestamp effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/*public List<ActionRequestValue> getActionRequests(DocumentRouteHeaderValue routeHeader, String ruleTemplateName) throws KEWUserNotFoundException, WorkflowException {
	return getActionRequests(routeHeader, null, ruleTemplateName);
    }*/

	// loads a RuleSelector implementation
	protected RuleSelector loadRuleSelector(RouteNode routeNodeDef, RouteNodeInstance nodeInstance) {
		// first see if there ruleselector is configured on a nodeinstance basis
		NodeState ns = null;
		if (nodeInstance != null) {
			ns = nodeInstance.getNodeState(KewApiConstants.RULE_SELECTOR_NODE_STATE_KEY);
		}
		String ruleSelectorName = null;
		if (ns != null) {
			ruleSelectorName = ns.getValue();
		} else {
			// otherwise pull it from the RouteNode definition/prototype
			Map<String, String> nodeCfgParams = Utilities.getKeyValueCollectionAsMap(
					routeNodeDef.
					getConfigParams());
			ruleSelectorName = nodeCfgParams.get(RouteNode.RULE_SELECTOR_CFG_KEY);
		}

		if (ruleSelectorName == null) {
			ruleSelectorName = DEFAULT_RULE_SELECTOR;
		}
		ruleSelectorName = StringUtils.capitalize(ruleSelectorName);

		// load up the rule selection implementation
		String className = RULE_SELECTOR_PACKAGE + "." + ruleSelectorName + RULE_SELECTOR_SUFFIX;
		Class<?> ruleSelectorClass;
		try {
			ruleSelectorClass = ClassLoaderUtils.getDefaultClassLoader().loadClass(className);
		} catch (ClassNotFoundException cnfe) {
			throw new IllegalStateException("Rule selector implementation '" + className + "' not found", cnfe);
		}
		if (!RuleSelector.class.isAssignableFrom(ruleSelectorClass)) {
			throw new IllegalStateException("Specified class '" + ruleSelectorClass + "' does not implement RuleSelector interface");
		}
		RuleSelector ruleSelector;
		try {
			ruleSelector = ((Class<RuleSelector>) ruleSelectorClass).newInstance();
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException)e;
			}
			throw new IllegalStateException("Error instantiating rule selector implementation '" + ruleSelectorClass + "'", e);
		}

		return ruleSelector;
	}

	/**
	 * Generates action requests
	 * @param routeHeader the document route header
	 * @param nodeInstance the route node instance; this may NOT be null
	 * @param ruleTemplateName the rule template
	 * @return list of action requests
	 * @throws WorkflowException
	 */
	public List<ActionRequestValue> getActionRequests(DocumentRouteHeaderValue routeHeader, RouteNodeInstance nodeInstance, String ruleTemplateName) {
		return getActionRequests(routeHeader, nodeInstance.getRouteNode(), nodeInstance, ruleTemplateName);
	}

	/**
	 * Generates action requests
	 * @param routeHeader the document route header
	 * @param routeNodeDef the RouteNode definition of the route node instance
	 * @param nodeInstance the route node instance; this may be null!
	 * @param ruleTemplateName the rule template
	 * @return list of action requests
	 * @throws WorkflowException
	 */
	public List<ActionRequestValue> getActionRequests(DocumentRouteHeaderValue routeHeader, RouteNode routeNodeDef, RouteNodeInstance nodeInstance, String ruleTemplateName) {
		RouteContext context = RouteContext.getCurrentRouteContext();
		// TODO really the route context just needs to be able to support nested create and clears
		// (i.e. a Stack model similar to transaction intercepting in Spring) and we wouldn't have to do this
		if (context.getDocument() == null) {
			context.setDocument(routeHeader);
		}
		if (context.getNodeInstance() == null) {
			context.setNodeInstance(nodeInstance);
		}

		LOG.debug("Making action requests for document " + routeHeader.getDocumentId());

		RuleSelector ruleSelector = loadRuleSelector(routeNodeDef, nodeInstance);

		List<Rule> rules = ruleSelector.selectRules(context, routeHeader, nodeInstance, ruleTemplateName, effectiveDate);

		// XXX: FIXME: this is a special case hack to expose info from the default selection implementation
		// this is used in exactly one place, RoutingReportAction, to make a distinction between no rules being
		// selected, and no rules actually matching when evaluated
		// if (numberOfRules == 0) {
		//   errors.add(new WorkflowServiceErrorImpl("There are no rules.", "routereport.noRules"));
		// } else {
		//   errors.add(new WorkflowServiceErrorImpl("There are rules, but no matches.", "routereport.noMatchingRules"));
		// }
		if (ruleSelector instanceof TemplateRuleSelector) {
			selectedRules += ((TemplateRuleSelector) ruleSelector).getNumberOfSelectedRules();
		}

		PerformanceLogger performanceLogger = new PerformanceLogger();

		ActionRequestFactory arFactory = new ActionRequestFactory(routeHeader, context.getNodeInstance());

		List<ActionRequestValue> actionRequests = new ArrayList<ActionRequestValue>();
		if (rules != null) {
			LOG.info("Total number of rules selected by RuleSelector for documentType=" + routeHeader.getDocumentType().getName() + " and ruleTemplate=" + ruleTemplateName + ": " + rules.size());
			for (Rule rule: rules) {
				RuleExpressionResult result = rule.evaluate(rule, context);
				if (result.isSuccess() && result.getResponsibilities() != null) {
					// actionRequests.addAll(makeActionRequests(context, rule, routeHeader, null, null));
                    org.kuali.rice.kew.api.rule.Rule ruleDef = org.kuali.rice.kew.api.rule.Rule.Builder.create(rule.getDefinition()).build();
					makeActionRequests(arFactory, result.getResponsibilities(), context, ruleDef, routeHeader, null, null);
				}
			}
		}
		actionRequests = new ArrayList<ActionRequestValue>(arFactory.getRequestGraphs());
		performanceLogger.log("Time to make action request for template " + ruleTemplateName);

		return actionRequests;
	}

	public ResponsibleParty resolveResponsibilityId(String responsibilityId) {
		if (responsibilityId == null) {
			throw new IllegalArgumentException("A null responsibilityId was passed to resolve responsibility!");
		}
		RuleResponsibility resp = getRuleService().getRuleResponsibility(responsibilityId);
		ResponsibleParty responsibleParty = new ResponsibleParty();
		if (resp!=null && resp.isUsingRole()) {
			responsibleParty.setRoleName(resp.getResolvedRoleName());
		} else if (resp!=null && resp.isUsingPrincipal()) {
			responsibleParty.setPrincipalId(resp.getPrincipalId());
		} else if (resp!=null && resp.isUsingGroup()) {
			responsibleParty.setGroupId(resp.getGroupId());
		} else {
			throw new RiceRuntimeException("Failed to resolve responsibility from responsibility ID " + responsibilityId + ".  Responsibility was an invalid type: " + resp);
		}
		return responsibleParty;
	}

	private void makeActionRequests(ActionRequestFactory arFactory, RouteContext context, org.kuali.rice.kew.api.rule.Rule rule, DocumentRouteHeaderValue routeHeader, ActionRequestValue parentRequest, RuleDelegation ruleDelegation)
			throws WorkflowException {

		List<org.kuali.rice.kew.api.rule.RuleResponsibility> responsibilities = rule.getRuleResponsibilities();
		makeActionRequests(arFactory, responsibilities, context, rule, routeHeader, parentRequest, ruleDelegation);
	}

	public void makeActionRequests(ActionRequestFactory arFactory, List<org.kuali.rice.kew.api.rule.RuleResponsibility> responsibilities, RouteContext context, org.kuali.rice.kew.api.rule.Rule rule, DocumentRouteHeaderValue routeHeader, ActionRequestValue parentRequest, RuleDelegation ruleDelegation) {

		//	Set actionRequests = new HashSet();
        for (org.kuali.rice.kew.api.rule.RuleResponsibility responsibility : responsibilities)
        {
            //	    arFactory = new ActionRequestFactory(routeHeader);

            if (responsibility.isUsingRole())
            {
                makeRoleActionRequests(arFactory, context, rule, responsibility, routeHeader, parentRequest, ruleDelegation);
            } else
            {
                makeActionRequest(arFactory, context, rule, routeHeader, responsibility, parentRequest, ruleDelegation);
            }
            //	    if (arFactory.getRequestGraph() != null) {
            //	    actionRequests.add(arFactory.getRequestGraph());
            //	    }
        }
	}

	private void buildDelegationGraph(ActionRequestFactory arFactory, RouteContext context, 
			org.kuali.rice.kew.api.rule.Rule delegationRule, DocumentRouteHeaderValue routeHeaderValue, ActionRequestValue parentRequest, RuleDelegation ruleDelegation) {
		context.setActionRequest(parentRequest);
        RuleBaseValues delRuleBo = KEWServiceLocator.getRuleService().getRuleByName(delegationRule.getName());
		if (delegationRule.isActive()) {
            for (org.kuali.rice.kew.api.rule.RuleResponsibility delegationResp : delegationRule.getRuleResponsibilities())
            {
                if (delegationResp.isUsingRole())
                {
                    makeRoleActionRequests(arFactory, context, delegationRule, delegationResp, routeHeaderValue, parentRequest, ruleDelegation);
                } else if (delRuleBo.isMatch(context.getDocumentContent()))
                {
                    makeActionRequest(arFactory, context, delegationRule, routeHeaderValue, delegationResp, parentRequest, ruleDelegation);
                }
            }
		}
	}

	/**
	 * Generates action requests for a role responsibility
	 */
	private void makeRoleActionRequests(ActionRequestFactory arFactory, RouteContext context, 
			org.kuali.rice.kew.api.rule.Rule rule, org.kuali.rice.kew.api.rule.RuleResponsibility resp, DocumentRouteHeaderValue routeHeader, ActionRequestValue parentRequest,
			RuleDelegation ruleDelegation)
	{
		String roleName = resp.getResolvedRoleName();
		//RoleAttribute roleAttribute = resp.resolveRoleAttribute();
        RoleAttribute roleAttribute = null;
        if (resp.isUsingRole()) {
            //get correct extension definition
            roleAttribute = (RoleAttribute) GlobalResourceLoader.getResourceLoader().getObject(new ObjectDefinition(
                    resp.getRoleAttributeName()));

            if (roleAttribute instanceof XmlConfiguredAttribute) {
                ExtensionDefinition roleAttributeDefinition = null;
                for (RuleTemplateAttribute ruleTemplateAttribute : rule.getRuleTemplate().getRuleTemplateAttributes()) {
                    if (resp.getRoleAttributeName().equals(ruleTemplateAttribute.getRuleAttribute().getResourceDescriptor())) {
                        roleAttributeDefinition = ruleTemplateAttribute.getRuleAttribute();
                        break;
                    }
                }
                ((XmlConfiguredAttribute)roleAttribute).setExtensionDefinition(roleAttributeDefinition);
            }
        }
		//setRuleAttribute(roleAttribute, rule, resp.getRoleAttributeName());
		List<String> qualifiedRoleNames = new ArrayList<String>();
		if (parentRequest != null && parentRequest.getQualifiedRoleName() != null) {
			qualifiedRoleNames.add(parentRequest.getQualifiedRoleName());
		} else {
	        qualifiedRoleNames.addAll(roleAttribute.getQualifiedRoleNames(roleName, context.getDocumentContent()));
		}
        for (String qualifiedRoleName : qualifiedRoleNames) {
            if (parentRequest == null && isDuplicateActionRequestDetected(routeHeader, context.getNodeInstance(), resp, qualifiedRoleName)) {
                continue;
            }

            ResolvedQualifiedRole resolvedRole = roleAttribute.resolveQualifiedRole(context, roleName, qualifiedRoleName);
            RoleRecipient recipient = new RoleRecipient(roleName, qualifiedRoleName, resolvedRole);
            if (parentRequest == null) {
                ActionRequestValue roleRequest = arFactory.addRoleRequest(recipient, resp.getActionRequestedCd(),
                        resp.getApprovePolicy(), resp.getPriority(), resp.getResponsibilityId(), rule.isForceAction(),
                        rule.getDescription(), rule.getId());

                List<RuleDelegation> ruleDelegations = getRuleService().getRuleDelegationsByResponsibiltityId(resp.getResponsibilityId());
                if (ruleDelegations != null && !ruleDelegations.isEmpty()) {
                    // create delegations for all the children
                    for (ActionRequestValue request : roleRequest.getChildrenRequests()) {
                        for (RuleDelegation childRuleDelegation : ruleDelegations) {
                            buildDelegationGraph(arFactory, context, childRuleDelegation.getDelegationRule(), routeHeader, request, childRuleDelegation);
                        }
                    }
                }

            } else {
                arFactory.addDelegationRoleRequest(parentRequest, resp.getApprovePolicy(), recipient, resp.getResponsibilityId(), rule.isForceAction(), ruleDelegation.getDelegationType(), rule.getDescription(), rule.getId());
            }
        }
	}

	/**
	 * Determines if the attribute has a setRuleAttribute method and then sets the value appropriately if it does.
	 */
	/*private void setRuleAttribute(RoleAttribute roleAttribute, org.kuali.rice.kew.api.rule.Rule rule, String roleAttributeName) {
		// look for a setRuleAttribute method on the RoleAttribute
		Method setRuleAttributeMethod = null;
		try {
			setRuleAttributeMethod = roleAttribute.getClass().getMethod("setExtensionDefinition", RuleAttribute.class);
		} catch (NoSuchMethodException e) {
            LOG.info("method setRuleAttribute not found on " + RuleAttribute.class.getName());
        }
		if (setRuleAttributeMethod == null) {
			return;
		}
		// find the RuleAttribute by looking through the RuleTemplate
		RuleTemplate ruleTemplate = rule.getRuleTemplate();
		if (ruleTemplate != null) {
            for (RuleTemplateAttribute ruleTemplateAttribute : ruleTemplate.getActiveRuleTemplateAttributes())
            {
                RuleAttribute ruleAttribute = ExtensionUtils.loadExtension(ruleTemplateAttribute.getRuleAttribute());
                if (ruleAttribute.getResourceDescriptor().equals(roleAttributeName))
                {
                    // this is our RuleAttribute!
                    try
                    {
                        setRuleAttributeMethod.invoke(roleAttribute, ruleAttribute);
                        break;
                    } catch (Exception e)
                    {
                        throw new WorkflowRuntimeException("Failed to set ExtensionDefinition on our RoleAttribute!", e);
                    }
                }
            }
		}
	}*/

	/**
	 * Generates action requests for a non-role responsibility, either a user or workgroup
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
	private void makeActionRequest(ActionRequestFactory arFactory, RouteContext context, org.kuali.rice.kew.api.rule.Rule rule, DocumentRouteHeaderValue routeHeader, org.kuali.rice.kew.api.rule.RuleResponsibility resp, ActionRequestValue parentRequest,
			RuleDelegation ruleDelegation) {
		if (parentRequest == null && isDuplicateActionRequestDetected(routeHeader, context.getNodeInstance(), resp, null)) {
			return;
		}
		Recipient recipient;
		if (resp.isUsingPrincipal()) {
	        recipient = new KimPrincipalRecipient(resp.getPrincipalId());
        } else if (resp.isUsingGroup()) {
            recipient = new KimGroupRecipient(resp.getGroupId());
        } else {
            throw new RiceRuntimeException("Illegal rule responsibility type encountered");
        }
		ActionRequestValue actionRequest;
		if (parentRequest == null) {
			actionRequest = arFactory.addRootActionRequest(resp.getActionRequestedCd(),
					resp.getPriority(),
					recipient,
					rule.getDescription(),
					resp.getResponsibilityId(),
					rule.isForceAction(),
					resp.getApprovePolicy(),
					rule.getId());

			List<RuleDelegation> ruleDelegations = getRuleService().getRuleDelegationsByResponsibiltityId(
                    resp.getResponsibilityId());
			if (ruleDelegations != null && !ruleDelegations.isEmpty()) {
				for (RuleDelegation childRuleDelegation : ruleDelegations) {
					buildDelegationGraph(arFactory, context, childRuleDelegation.getDelegationRule(), routeHeader, actionRequest, childRuleDelegation);
				}
			}
			
		} else {
			arFactory.addDelegationRequest(parentRequest, recipient, resp.getResponsibilityId(), rule.isForceAction(), ruleDelegation.getDelegationType(), rule.getDescription(), rule.getId());
		}
	}

	private boolean isDuplicateActionRequestDetected(DocumentRouteHeaderValue routeHeader, RouteNodeInstance nodeInstance, org.kuali.rice.kew.api.rule.RuleResponsibility resp, String qualifiedRoleName) {
		List<ActionRequestValue> requests = getActionRequestService().findByStatusAndDocId(ActionRequestStatus.DONE.getCode(), routeHeader.getDocumentId());
        for (ActionRequestValue request : requests)
        {
            if (((nodeInstance != null
                    && request.getNodeInstance() != null
                    && request.getNodeInstance().getRouteNodeInstanceId().equals(nodeInstance.getRouteNodeInstanceId())
                 ) || request.getRouteLevel().equals(routeHeader.getDocRouteLevel())
                )
                    && request.getResponsibilityId().equals(resp.getResponsibilityId())
                        && ObjectUtils.equals(request.getQualifiedRoleName(), qualifiedRoleName)) {
                return true;
            }
        }
		return false;
	}

	public RuleService getRuleService() {
		return KewApiServiceLocator.getRuleService();
	}

	private ActionRequestService getActionRequestService() {
		return KEWServiceLocator.getActionRequestService();
	}

	public int getNumberOfMatchingRules() {
		return selectedRules;
	}

}
