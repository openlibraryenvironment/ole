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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * Struts action for handling the initial Delegate Rule screen for selecting
 * the parent rule and responsibility. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DelegateRuleAction extends KewKualiAction {

	private static final String PARENT_RULE_PROPERTY = "parentRuleId";
	private static final String PARENT_RESPONSIBILITY_PROPERTY = "parentResponsibilityId";
	
	private static final String PARENT_RULE_ERROR = "delegateRule.parentRule.required";
	private static final String PARENT_RESPONSIBILITY_ERROR = "delegateRule.parentResponsibility.required";
	private static final String DELEGATE_RULE_INVALID_ERROR = "delegateRule.delegateRuleTemplate.invalid";
	
	public ActionForward createDelegateRule(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DelegateRuleForm form = (DelegateRuleForm) actionForm;
		if (!validateCreateDelegateRule(form)) {
			return mapping.findForward(getDefaultMapping());
		}
		return new ActionForward(generateMaintenanceUrl(request, form), true);
	}
	
	protected boolean validateCreateDelegateRule(DelegateRuleForm form) {
		if (form.getParentRule() == null) {
			GlobalVariables.getMessageMap().putError(PARENT_RULE_PROPERTY, PARENT_RULE_ERROR);
		} else {
			RuleTemplateBo ruleTemplate = form.getParentRule().getRuleTemplate();
			if (ruleTemplate == null
			        || ruleTemplate.getDelegationTemplate() == null) {
				GlobalVariables.getMessageMap().putError(PARENT_RULE_PROPERTY, DELEGATE_RULE_INVALID_ERROR);
			}
		}
		if (form.getParentResponsibility() == null) {
			GlobalVariables.getMessageMap().putError(PARENT_RESPONSIBILITY_PROPERTY, PARENT_RESPONSIBILITY_ERROR);
		}
		
		return GlobalVariables.getMessageMap().hasNoErrors();
	}
	
	protected String generateMaintenanceUrl(HttpServletRequest request, DelegateRuleForm form) {
		return getApplicationBaseUrl() + "/kr/" + KRADConstants.MAINTENANCE_ACTION + "?" +
			KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.START_METHOD + "&" +
			KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE + "=" + RuleDelegationBo.class.getName() +  "&" +
			WebRuleUtils.RESPONSIBILITY_ID_PARAM + "=" + form.getParentResponsibilityId() + "&" +
			WebRuleUtils.RULE_TEMPLATE_ID_PARAM + "=" + form.getParentRule().getRuleTemplate().getDelegationTemplateId() + "&" +
			WebRuleUtils.DOCUMENT_TYPE_NAME_PARAM + "=" + form.getParentRule().getDocTypeName();
	}
	
}
