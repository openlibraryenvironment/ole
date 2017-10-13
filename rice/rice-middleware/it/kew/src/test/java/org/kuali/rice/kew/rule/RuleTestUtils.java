/**
 * Copyright 2005-2013 The Kuali Foundation
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

import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.workgroup.GroupNameId;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * This is a description of what this class does - gilesp don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class RuleTestUtils {
	
	private RuleTestUtils() {
		throw new UnsupportedOperationException("do not call");
	}
	
	/**
	 * <p>This method will create a delegate rule for the rule (assumed to be cardinality of 1) specified by the given
	 * docType and ruleTemplate. 
	 * 
	 * <p>As a side effect, active documents of this type will be requeued for workflow processing.
	 * 
	 * @param delegateUser the user who will be the delegate
	 */
	public static RuleDelegationBo createDelegationToUser(String docType, String ruleTemplate, String delegateUser) {
		// create and save a rule delegation 
    	RuleBaseValues originalRule = getRule(docType, ruleTemplate);
    	List<RuleResponsibilityBo> responsibilities = originalRule.getRuleResponsibilities();
    	assertTrue("assuming there is 1 responsibility", responsibilities != null && responsibilities.size() == 1);
    	
    	RuleResponsibilityBo originalResp = responsibilities.get(0);

    	Principal delegatePrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(delegateUser);

		// save the new rule delegation
		// this *SHOULD* requeue
		return createRuleDelegationToUser(originalRule, originalResp, delegatePrincipal);
	}

	/**
	 * <p>This method will create a delegate rule for the rule (assumed to be cardinality of 1) specified by the given
	 * docType and ruleTemplate. 
	 * 
	 * <p>As a side effect, active documents of this type will be requeued for workflow processing.
	 * 
	 * @param delegateUser the user who will be the delegate
	 */
	public static RuleDelegationBo createDelegationToGroup(String docType, String ruleTemplate, String delegateGroupId) {
		// create and save a rule delegation 
    	RuleBaseValues originalRule = getRule(docType, ruleTemplate);
    	List<RuleResponsibilityBo> responsibilities = originalRule.getRuleResponsibilities();
    	assertTrue("assuming there is 1 responsibility", responsibilities != null && responsibilities.size() == 1);
    	
    	RuleResponsibilityBo originalResp = responsibilities.get(0);
    	Group delegateGroup = KEWServiceLocator.getIdentityHelperService().getGroup(new GroupNameId(delegateGroupId));
    	
		// save the new rule delegation
		// this *SHOULD* requeue
		return createRuleDelegationToGroup(originalRule, originalResp, delegateGroup);
	}
	/**
	 * This method gets a rule from a docType / ruleTemplate combo
	 */
	public static RuleBaseValues getRule(String docType, String ruleTemplate) {
		List rules = KEWServiceLocator.getRuleService().fetchAllCurrentRulesForTemplateDocCombination(ruleTemplate, docType);
    	assertTrue("assuming there is 1 rule", rules != null && rules.size() == 1);
    	
    	RuleBaseValues originalRule = (RuleBaseValues)rules.get(0);
		return originalRule;
	}

	/**
	 * <p>This method creates and saves a rule delegation
	 * 
	 * <p>As a side effect, active documents of this type will be requeued for workflow processing.
	 * 
	 * @param parentRule
	 * @param parentResponsibility
	 * @param delegatePrincipal
	 */
	public static RuleDelegationBo createRuleDelegationToUser(RuleBaseValues parentRule, RuleResponsibilityBo parentResponsibility, PrincipalContract delegatePrincipal) {
		return createRuleDelegation(parentRule, parentResponsibility, delegatePrincipal.getPrincipalId(), KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID);
	}
	
	/**
	 * <p>This method creates and saves a rule delegation
	 * 
	 * <p>As a side effect, active documents of this type will be requeued for workflow processing.
	 * 
	 * @param parentRule
	 * @param parentResponsibility
	 * @param delegateGroup
	 */
	public static RuleDelegationBo createRuleDelegationToGroup(RuleBaseValues parentRule, RuleResponsibilityBo parentResponsibility, Group delegateGroup) {
		return createRuleDelegation(parentRule, parentResponsibility, delegateGroup.getId(), KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
	}
	
	/**
	 * <p>This method creates and saves a rule delegation
	 * 
	 * <p>As a side effect, active documents of this type will be requeued for workflow processing.
	 */
	private static RuleDelegationBo createRuleDelegation(RuleBaseValues parentRule, RuleResponsibilityBo parentResponsibility, String delegateId, String groupTypeCode) {
    	RuleTemplateBo delegationTemplate = parentRule.getRuleTemplate();
		RuleDelegationBo ruleDelegation = new RuleDelegationBo();
		ruleDelegation.setResponsibilityId(parentResponsibility.getResponsibilityId());
		ruleDelegation.setDelegationType(DelegationType.PRIMARY);
		RuleBaseValues rule = new RuleBaseValues();
		ruleDelegation.setDelegationRule(rule);
		rule.setDelegateRule(true);
		rule.setActive(true);
		rule.setCurrentInd(true);
		rule.setDocTypeName(parentRule.getDocTypeName());
		rule.setRuleTemplateId(delegationTemplate.getDelegationTemplateId());
		rule.setRuleTemplate(delegationTemplate);
		rule.setDescription("Description of this delegate rule");
		rule.setForceAction(true);
		RuleResponsibilityBo delegationResponsibility = new RuleResponsibilityBo();
		rule.getRuleResponsibilities().add(delegationResponsibility);
		delegationResponsibility.setRuleBaseValues(rule);
		delegationResponsibility.setRuleResponsibilityName(delegateId);
		delegationResponsibility.setRuleResponsibilityType(groupTypeCode);
		KEWServiceLocator.getRuleService().saveRuleDelegation(ruleDelegation, true);
		return ruleDelegation;
	}
}
