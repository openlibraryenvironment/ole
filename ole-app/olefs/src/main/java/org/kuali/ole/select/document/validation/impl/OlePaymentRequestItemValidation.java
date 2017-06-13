/*
 * Copyright 2011 The Kuali Foundation.
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
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OleSufficientFundCheck;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.service.OlePaymentRequestFundCheckService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OlePaymentRequestItemValidation extends GenericValidation {

    private BusinessObjectService businessObjectService;

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        boolean isUnordered = false;
        boolean orderedWithQty = true;
        int count = 0;
        OlePaymentRequestDocument document = (OlePaymentRequestDocument) event.getDocument();
        if ((document.getPaymentMethod() != null) && (document.getPaymentMethod().getPaymentMethodId() != null)) {
            document.setPaymentMethodId(document.getPaymentMethod().getPaymentMethodId());
        }
        List<OlePaymentRequestItem> items = document.getItems();
        for (OlePaymentRequestItem item : items) {
            count += 1;
            if (item.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                isUnordered = true;
                if (StringUtils.equalsIgnoreCase(item.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                    if (item.getItemQuantity() == null || item.getItemQuantity().isLessEqual(KualiDecimal.ZERO)) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_ITEM_QUANTITY_REQUIRED, "Item " + count);
                        valid = false;
                    }
                }
            }
            if (item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                if (StringUtils.equalsIgnoreCase(item.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE) || StringUtils.equalsIgnoreCase(item.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                    if (item.getItemDescription() == null || item.getItemDescription().trim().length() <= 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, "Item " + count);
                        valid = false;
                    }
                }
                if (item.getItemQuantity() != null && item.getItemQuantity().isGreaterThan(KualiDecimal.ZERO)) {
                    if (item.getItemNoOfParts() == null || (item.getItemNoOfParts()).isLessEqual(KualiInteger.ZERO)) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_NO_OF_PARTS_REQUIRED, "Item " + count);
                        valid = false;
                    }
                }
            }
        }
        if (!isUnordered) {
            for (OlePaymentRequestItem item : items) {
                if (item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                    if (item.getItemQuantity() == null || item.getItemQuantity().isLessEqual(KualiDecimal.ZERO)) {
                        orderedWithQty = false;
                    } else {
                        orderedWithQty = true;
                    }
                }
                if (orderedWithQty) {
                    break;
                }
            }
        }
        if (!orderedWithQty) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_ATLEAST_ONE_ITEM_QTY_REQUIRED);
            valid = false;
        }

        if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            List<SourceAccountingLine> sourceAccountingLineList = document.getSourceAccountingLines();
            for (SourceAccountingLine accLine : sourceAccountingLineList) {
                Map searchMap = new HashMap();
                Map<String, Object> key = new HashMap<String, Object>();
                String chartCode = accLine.getChartOfAccountsCode();
                String accNo = accLine.getAccountNumber();
                String objectCd = accLine.getFinancialObjectCode();
                key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
                String option = null;
                OleSufficientFundCheck account = getBusinessObjectService().findByPrimaryKey(OleSufficientFundCheck.class,
                        key);
                if (account != null) {
                    option = account.getNotificationOption();
                    if (option != null) {
                        if (option.equals(OLEPropertyConstants.BLOCK_USE)) {
                            boolean fundCheck = SpringContext.getBean(OlePaymentRequestFundCheckService.class)
                                    .hasSufficientFundCheckRequired(accLine);
                            if (fundCheck) {
                                GlobalVariables.getMessageMap().putError(OLEConstants.ERROR_MSG_FOR_INSUFF_FUND,
                                        RiceKeyConstants.ERROR_CUSTOM,
                                        OLEConstants.INSUFF_FUND + accLine.getAccountNumber());
                                valid = false;
                            }
                        } else if (option.equals(OLEPropertyConstants.WARNING_MSG)) {
                            boolean fundcheckRequired = SpringContext.getBean(OlePaymentRequestFundCheckService.class)
                                    .hasSufficientFundCheckRequired(accLine);
                            if (fundcheckRequired) {
                                GlobalVariables.getMessageMap().putWarning(PurapConstants.DETAIL_TAB_ERRORS,
                                        OleSelectPropertyConstants.INSUFF_FUND,
                                        new String[]{accLine.getAccountNumber()});
                            }
                        }
                    }
                }
            }
        }
        return valid;

    }


    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

}
