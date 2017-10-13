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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.service.RuleServiceInternal;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;


/**
 * A decorator around a {@link org.kuali.rice.kew.rule.RuleResponsibilityBo} object which provides some
 * convienance functions for interacting with the bean from the web-tier.
 * This helps to alleviate some of the weaknesses of JSTL.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WebRuleResponsibility extends RuleResponsibilityBo {

	private static final long serialVersionUID = -8422695726158274189L;

	private static final String DISPLAY_INLINE = "display:inline";

	private static final String DISPLAY_NONE = "display:none";

	private String reviewer;

	private String reviewerStyle = "";

	private String personLookupStyle = "";

	private String workgroupLookupStyle = "";

	private String roleReviewer;

	private String roleAreaStyle = "";

	private boolean delegationRulesMaterialized = false;

	private boolean showDelegations = false;

	private int numberOfDelegations;

	private int index = 0;

	private boolean hasDelegateRuleTemplate = false;

	/**
	 * "reviewerId added to support links to workgroup report or user report
	 */

	private String reviewerId;

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public WebRuleResponsibility() {
		setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID);
		setApprovePolicy(ActionRequestPolicy.FIRST.getCode());
	}

	public void initialize() throws Exception {
		if (getDelegationRules().size() <= Integer.parseInt(CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.RULE_DETAIL_TYPE, KewApiConstants.RULE_DELEGATE_LIMIT))) {
			showDelegations = true;
		}
		setNumberOfDelegations(getDelegationRules().size());
		if (delegationRulesMaterialized) {
			for (Iterator iterator = getDelegationRules().iterator(); iterator.hasNext();) {
				RuleDelegationBo ruleDelegation = (RuleDelegationBo) iterator.next();
				WebRuleBaseValues webRule = (WebRuleBaseValues) ruleDelegation.getDelegationRule();
				webRule.initialize();
			}
		}
		establishRequiredState();
	}

	private void loadWebValues() throws Exception {
		if (!org.apache.commons.lang.StringUtils.isEmpty(getRuleResponsibilityName())) {
			if (KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID.equals(getRuleResponsibilityType())) {
				// setReviewer(getUserService().getWorkflowUser(new
				// WorkflowUserId(getRuleResponsibilityName())).getPrincipalName().getAuthenticationId());
				Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipal(getRuleResponsibilityName());
				setReviewer(principal.getPrincipalName());
				setReviewerId(principal.getPrincipalId());
			} else if (KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID.equals(getRuleResponsibilityType())) {
				// setReviewer(getWorkgroupService().getWorkgroup(new
				// WorkflowGroupId(new
				// Long(getRuleResponsibilityName()))).getGroupNameId().getNameId());
				Group group = KimApiServiceLocator.getGroupService().
	                  getGroup(getRuleResponsibilityName());
				setReviewer(group.getName());
				setReviewerId(group.getId());
			} else if (KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID.equals(getRuleResponsibilityType())) {
				setRoleReviewer(getRuleResponsibilityName());
				setReviewer(getResolvedRoleName());
			}
		}
	}

	private void injectWebMembers() throws Exception {
        DelegationRulesProxy delegationRulesProxy = new DelegationRulesProxy(getDelegationRules());
        Class delegationRulesClass = getDelegationRules().getClass();
        //System.err.println("delegation rules class: "+ delegationRulesClass);
        Class[] delegationRulesInterfaces = new Class[0]; // = delegationRulesClass.getInterfaces();
        List<Class> delegationRulesInterfaceList = (List<Class>) ClassUtils.getAllInterfaces(delegationRulesClass);
        delegationRulesInterfaces = delegationRulesInterfaceList.toArray(delegationRulesInterfaces);
        ClassLoader delegationRulesClassLoader = getDelegationRules().getClass().getClassLoader();
        Object o = Proxy.newProxyInstance(delegationRulesClassLoader, delegationRulesInterfaces, delegationRulesProxy);
        //setDelegationRules((List) o);

		if (Integer.parseInt(CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.RULE_DETAIL_TYPE, KewApiConstants.RULE_DELEGATE_LIMIT)) > getDelegationRules().size() || showDelegations) {
			for (Iterator iterator = getDelegationRules().iterator(); iterator.hasNext();) {
				RuleDelegationBo ruleDelegation = (RuleDelegationBo) iterator.next();
				WebRuleBaseValues webRule = new WebRuleBaseValues();
				webRule.load(ruleDelegation.getDelegationRule());
				webRule.edit(ruleDelegation.getDelegationRule());
				ruleDelegation.setDelegationRule(webRule);
			}
		}
	}

	public RuleDelegationBo addNewDelegation() {
		RuleDelegationBo ruleDelegation = new RuleDelegationBo();
		ruleDelegation.setDelegationRule(new WebRuleBaseValues());
		ruleDelegation.setDelegationType(DelegationType.PRIMARY);
		ruleDelegation.getDelegationRule().setDelegateRule(Boolean.TRUE);
		ruleDelegation.getDelegationRule().setDocTypeName(getRuleBaseValues().getDocTypeName());
		getDelegationRules().add(ruleDelegation);
		showDelegations = true;
		return ruleDelegation;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public void setWorkgroupId(String workgroupId) {
	    Group workgroup = KimApiServiceLocator.getGroupService().getGroup(workgroupId);
		//Workgroup workgroup = getWorkgroupService().getWorkgroup(new WorkflowGroupId(workgroupId));
		if (workgroup != null) {
			setReviewer(workgroup.getName());
		} else {
			setReviewer("");
		}
	}

	public String getPersonLookupStyle() {
		return personLookupStyle;
	}

	public void setPersonLookupStyle(String personLookupStyle) {
		this.personLookupStyle = personLookupStyle;
	}

	public String getReviewerStyle() {
		return reviewerStyle;
	}

	public void setReviewerStyle(String reviewerStyle) {
		this.reviewerStyle = reviewerStyle;
	}

	public String getRoleAreaStyle() {
		return roleAreaStyle;
	}

	public void setRoleAreaStyle(String roleAreaLookupStyle) {
		this.roleAreaStyle = roleAreaLookupStyle;
	}

	public String getWorkgroupLookupStyle() {
		return workgroupLookupStyle;
	}

	public void setWorkgroupLookupStyle(String workgroupLookupStyle) {
		this.workgroupLookupStyle = workgroupLookupStyle;
	}

	public RuleDelegationBo getDelegationRule(int index) {
		while (getDelegationRules().size() <= index) {
			addNewDelegation();
		}
		return (RuleDelegationBo) getDelegationRules().get(index);
	}

	public int getNumberOfDelegations() {
		return numberOfDelegations;
	}

	public void setNumberOfDelegations(int numberOfDelegations) {
		this.numberOfDelegations = numberOfDelegations;
	}

	public boolean isDelegationRulesMaterialized() {
		return delegationRulesMaterialized;
	}

	public void setDelegationRulesMaterialized(boolean isDelegationRulesMaterialized) {
		this.delegationRulesMaterialized = isDelegationRulesMaterialized;
	}

	public String getRoleReviewer() {
		return roleReviewer;
	}

	public void setRoleReviewer(String roleReviewer) {
		this.roleReviewer = roleReviewer;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isShowDelegations() {
		return showDelegations;
	}

	public void setShowDelegations(boolean showDelegations) {
		this.showDelegations = showDelegations;
	}

	public void establishRequiredState() throws Exception {
		if (KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID.equals(getRuleResponsibilityType())) {
			reviewerStyle = DISPLAY_INLINE;
			personLookupStyle = DISPLAY_INLINE;
			workgroupLookupStyle = DISPLAY_NONE;
			roleAreaStyle = DISPLAY_NONE;
		}
		if (KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID.equals(getRuleResponsibilityType())) {
			reviewerStyle = DISPLAY_INLINE;
			personLookupStyle = DISPLAY_NONE;
			workgroupLookupStyle = DISPLAY_INLINE;
			roleAreaStyle = DISPLAY_NONE;
		}
		if (KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID.equals(getRuleResponsibilityType())) {
			reviewerStyle = DISPLAY_NONE;
			personLookupStyle = DISPLAY_NONE;
			workgroupLookupStyle = DISPLAY_NONE;
			roleAreaStyle = DISPLAY_INLINE;
		}
		loadWebValues();
		if (delegationRulesMaterialized) {
			for (Iterator iterator = getDelegationRules().iterator(); iterator.hasNext();) {
				RuleDelegationBo delegation = (RuleDelegationBo) iterator.next();
				((WebRuleBaseValues) delegation.getDelegationRule()).establishRequiredState();
			}
		}
	}

	public void edit(RuleResponsibilityBo ruleResponsibility) throws Exception {
		load(ruleResponsibility);
		initialize();
	}

	public void load(RuleResponsibilityBo ruleResponsibility) throws Exception {
		PropertyUtils.copyProperties(this, ruleResponsibility);
		injectWebMembers();
	}

	public void loadDelegations() throws Exception {
		fetchDelegations();

		for (Iterator iterator = getDelegationRules().iterator(); iterator.hasNext();) {
			RuleDelegationBo ruleDelegation = (RuleDelegationBo) iterator.next();
			WebRuleBaseValues webRule = new WebRuleBaseValues();
			webRule.edit(ruleDelegation.getDelegationRule());
			ruleDelegation.setDelegationRule(webRule);
		}
		delegationRulesMaterialized = true;
		populatePreviousRuleIds();
	}

	public void populatePreviousRuleIds() {
		if (delegationRulesMaterialized) {
			for (Iterator iterator = getDelegationRules().iterator(); iterator.hasNext();) {
				RuleDelegationBo delegation = (RuleDelegationBo) iterator.next();
				((WebRuleBaseValues) delegation.getDelegationRule()).populatePreviousRuleIds();
			}
		}
	}

	private void fetchDelegations() {
		if (getId() != null) {
			RuleResponsibilityBo responsibility = getRuleService().findByRuleResponsibilityId(getId());
			if (responsibility == null) {
				return;
			}
			getDelegationRules().addAll(responsibility.getDelegationRules());
		}
	}

	public void prepareHiddenDelegationsForRoute() {
		if (showDelegations) {
			return;
		}

		fetchDelegations();

		for (Iterator iter = getDelegationRules().iterator(); iter.hasNext();) {
			RuleDelegationBo delegation = (RuleDelegationBo) iter.next();
			delegation.setDelegateRuleId(null);
			delegation.setVersionNumber(null);
			delegation.setRuleDelegationId(null);
			//delegation.setRuleResponsibility(this);
			delegation.setResponsibilityId(null);

			RuleBaseValues rule = delegation.getDelegationRule();
			rule.setVersionNumber(null);
			rule.setPreviousRuleId(rule.getId());
			rule.setDocumentId(null);
			rule.setId(null);

			for (Iterator iterator = rule.getRuleResponsibilities().iterator(); iterator.hasNext();) {
				RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iterator.next();
				responsibility.setVersionNumber(null);
				responsibility.setRuleBaseValuesId(null);
				responsibility.setRuleBaseValues(rule);
				responsibility.setId(null);
			}

			for (Iterator iterator = rule.getRuleExtensions().iterator(); iterator.hasNext();) {
				RuleExtensionBo extension = (RuleExtensionBo) iterator.next();
				extension.setVersionNumber(null);
				extension.setRuleBaseValues(rule);
				extension.setRuleBaseValuesId(null);
				extension.setRuleExtensionId(null);

				for (Iterator iter2 = extension.getExtensionValues().iterator(); iter2.hasNext();) {
					RuleExtensionValue value = (RuleExtensionValue) iter2.next();
					value.setExtension(extension);
					value.setLockVerNbr(null);
					value.setRuleExtensionId(null);
					value.setRuleExtensionValueId(null);
				}
			}
		}
	}

	public boolean isHasDelegateRuleTemplate() {
		return hasDelegateRuleTemplate;
	}

	public void setHasDelegateRuleTemplate(boolean hasDelegateRuleTemplate) {
		this.hasDelegateRuleTemplate = hasDelegateRuleTemplate;
	}

	private RuleServiceInternal getRuleService() {
		return (RuleServiceInternal) KEWServiceLocator.getService(KEWServiceLocator.RULE_SERVICE);
	}

	/**
	 * Just a little dynamic proxy to keep us from establishing required state
	 * on the delegation rules if they haven't been materialized from the
	 * database yet (they are currenty proxied by OJB)
	 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
	 */
	private class DelegationRulesProxy implements InvocationHandler, java.io.Serializable {

		private static final long serialVersionUID = 7046323200221509473L;

		private List delegationRules;

		public DelegationRulesProxy(List delegationRules) {
			this.delegationRules = delegationRules;
		}

		public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
			if (!delegationRulesMaterialized && !m.getName().equals("isEmpty") && !m.getName().equals("size")) {
				for (Iterator iterator = delegationRules.iterator(); iterator.hasNext();) {
					RuleDelegationBo ruleDelegation = (RuleDelegationBo) iterator.next();
					WebRuleBaseValues webRule = new WebRuleBaseValues();
					webRule.load(ruleDelegation.getDelegationRule());
					webRule.establishRequiredState();
					ruleDelegation.setDelegationRule(webRule);
				}
				delegationRulesMaterialized = true;

			}
			return m.invoke(delegationRules, args);
		}

	}

}
