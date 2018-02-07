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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class handles Actions for the DisbursementVoucher.
 */
public class RuleAction extends KewKualiAction {
    private static final String RULE_TEMPLATE_NAME_PROPERTY = "ruleTemplateName";
    private static final String DOC_TYPE_NAME_PROPERTY = "documentTypeName";

    private static final String RULE_TEMPLATE_ERROR = "rule.template.name.required";
    private static final String DOCUMENT_TYPE_ERROR = "rule.docType.name.required";
    private ConfigurationService kualiConfigurationService;

    public ActionForward createRule(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RuleForm form = (RuleForm) actionForm;
        if (!validateCreateRule(form)) {
            return mapping.findForward(getDefaultMapping());
        }
        return new ActionForward(generateMaintenanceUrl(request, form), true);
    }

    public ActionForward clearInitFields(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RuleForm form = (RuleForm) actionForm;
        form.clearSearchableAttributeProperties();
        return mapping.findForward(getDefaultMapping());
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RuleForm form = (RuleForm) actionForm;
        Object question = request.getParameter(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME);
        // this should probably be moved into a private instance variable
        // logic for cancel question
        if (question == null) {
            // ask question if not already asked
            return this.performQuestionWithoutInput(mapping, form, request, response, KRADConstants.DOCUMENT_CANCEL_QUESTION, getKualiConfigurationService().getPropertyValueAsString(
                    "document.question.cancel.text"), KRADConstants.CONFIRMATION_QUESTION, KRADConstants.MAPPING_CANCEL, "");
        } else {
            Object buttonClicked = request.getParameter(KRADConstants.QUESTION_CLICKED_BUTTON);
            if ((KRADConstants.DOCUMENT_CANCEL_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked)) {
                // if no button clicked just reload the doc
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
            // else go to cancel logic below
        }

        ActionForward dest = null;
        if (StringUtils.isNotBlank(form.getBackLocation())) {
            dest = new ActionForward(form.getBackLocation(), true);
        } else {

            dest = mapping.findForward(KRADConstants.MAPPING_PORTAL);
        }
        return dest;
    }

    protected String generateMaintenanceUrl(HttpServletRequest request, RuleForm form) {
        return getApplicationBaseUrl() + "/kr/" + KRADConstants.MAINTENANCE_ACTION + "?" +
            KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.START_METHOD + "&" +
            KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE + "=" + RuleBaseValues.class.getName() +  "&" +
            WebRuleUtils.DOCUMENT_TYPE_NAME_PARAM + "=" + form.getDocumentTypeName() + "&" +
            WebRuleUtils.RULE_TEMPLATE_NAME_PARAM + "=" + form.getRuleTemplateName();
    }

    protected boolean validateCreateRule(RuleForm form) {
        if (org.apache.commons.lang.StringUtils.isEmpty(form.getRuleTemplateName())) {
            GlobalVariables.getMessageMap().putError(RULE_TEMPLATE_NAME_PROPERTY, RULE_TEMPLATE_ERROR);
        } else {
            RuleTemplateBo ruleTemplate = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(form.getRuleTemplateName().trim());
            if (ruleTemplate == null) {
                GlobalVariables.getMessageMap().putError(RULE_TEMPLATE_NAME_PROPERTY, RULE_TEMPLATE_ERROR);
            }
        }

        if (org.apache.commons.lang.StringUtils.isEmpty(form.getDocumentTypeName())) {
            GlobalVariables.getMessageMap().putError(DOC_TYPE_NAME_PROPERTY, DOCUMENT_TYPE_ERROR);
        } else {
            DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(form.getDocumentTypeName());
            if (docType == null) {
                GlobalVariables.getMessageMap().putError(DOC_TYPE_NAME_PROPERTY, DOCUMENT_TYPE_ERROR);
            }
        }

        return GlobalVariables.getMessageMap().hasNoErrors();
    }
    protected ConfigurationService getKualiConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = CoreApiServiceLocator.getKualiConfigurationService();
        }
        return this.kualiConfigurationService;
    }
}
