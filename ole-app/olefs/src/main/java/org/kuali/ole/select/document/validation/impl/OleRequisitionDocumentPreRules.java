/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.validation.impl.RequisitionDocumentPreRules;
import org.kuali.ole.select.businessobject.OleSufficientFundCheck;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleRequisitionDocumentService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleRequisitionDocumentPreRules extends RequisitionDocumentPreRules {
    /**
     * Default Constructor
     */
    public OleRequisitionDocumentPreRules() {
        super();
    }

    /**
     * Main hook point to perform rules check.
     *
     * @see org.kuali.rice.kns.rules.PromptBeforeValidationBase#doRules(org.kuali.rice.krad.document.Document)
     */
    @Override
    public boolean doPrompts(Document document) {
        boolean preRulesOK = true;
        OleRequisitionDocument reqDoc = null;
        if (document instanceof RequisitionDocument) {
            reqDoc = (OleRequisitionDocument) document;
        }
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        StringBuilder accountNumbers = new StringBuilder();
        if (oleRequisitionDocumentService != null && reqDoc != null) {
            List<SourceAccountingLine> sourceAccountingLineList = reqDoc.getSourceAccountingLines();
            for (SourceAccountingLine accLine : sourceAccountingLineList) {
                String notificationOption = null;
                boolean sufficientFundCheck;
                Map<String, Object> key = new HashMap<String, Object>();
                String chartCode = accLine.getChartOfAccountsCode();
                String accNo = accLine.getAccountNumber();
                String objectCd = accLine.getFinancialObjectCode();
                key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
                OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                        OleSufficientFundCheck.class, key);
                if (account != null) {
                    notificationOption = account.getNotificationOption();
                }
                if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.WARNING_MSG)
                        && oleRequisitionDocumentService.hasSufficientFundsOnRequisition(accLine, notificationOption, SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear())) {
                    accountNumbers.append(accLine.getAccountNumber());
                }
            }
        }
        if (accountNumbers.length() > 0) {
            preRulesOK = askForConfirmation(OLEConstants.SufficientFundCheck.REQUISITION_SFC_CHECKING_STRING,
                    OLEConstants.SufficientFundCheck.REQUISITION_SFC_CHECKING, accountNumbers);
        }
        preRulesOK &= super.doPrompts(document);
        return preRulesOK;
    }

    /**
     * Prompts user to confirm with a Yes or No to a question being asked.
     *
     * @param questionType    - type of question
     * @param messageConstant - key to retrieve message
     * @return - true if overriding, false otherwise
     */
    protected boolean askForConfirmation(String questionType, String messageConstant, StringBuilder accountNumbers) {

        String questionText = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(
                messageConstant);
        if (questionText.contains("{")) {
            questionText = prepareQuestionText(questionType, questionText, accountNumbers);
        }

        boolean confirmOverride = super.askOrAnalyzeYesNoQuestion(questionType, questionText);

        if (!confirmOverride) {
            event.setActionForwardName(OLEConstants.MAPPING_BASIC);
            return false;
        }
        return true;
    }

    /**
     * Creates the actual text of the question, replacing place holders like pay date threshold with an actual constant value.
     *
     * @param questionType - type of question
     * @param questionText - actual text of question pulled from resource file
     * @return - question text with place holders replaced
     */
    protected String prepareQuestionText(String questionType, String questionText, StringBuilder accountNumbers) {
        if (StringUtils.equals(questionType, OLEConstants.SufficientFundCheck.REQUISITION_SFC_CHECKING_STRING)) {
            questionText = StringUtils.replace(questionText, "{0}", accountNumbers.toString());
        }
        return questionText;
    }

}
