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
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.businessobject.OleSufficientFundCheck;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.service.OleInvoiceFundCheckService;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleInvoiceItemValidation extends GenericValidation {

    private BusinessObjectService businessObjectService;

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        boolean isUnordered = false;
        boolean orderedWithQty = false;
        int count = 0;
        OleInvoiceDocument document = (OleInvoiceDocument) event.getDocument();
        if ((document.getPaymentMethod() != null) && (document.getPaymentMethod().getPaymentMethodId() != null)) {
            document.setPaymentMethodId(document.getPaymentMethod().getPaymentMethodId());
        }

        if(document.getPaymentMethodIdentifier() != null) {
            String paymentType = SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(document.getPaymentMethodIdentifier());
            if (paymentType.equals(OLEConstants.DEPOSIT)) {
                for (OleInvoiceItem item : (List<OleInvoiceItem>) document.getItems()) {
                    if (item.getItemTypeCode().equals("ITEM")) {
                        if (item.getSourceAccountingLineList().size() <= 0) {
                            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.OFFSET_ACCT_LINE, OLEKeyConstants.ERROR_REQUIRED, PurapConstants.PRQSDocumentsStrings.OFFSET_ACCT_LINE);
                            valid = false;
                        }
                        if (item.getSourceAccountingLineList().size() > 0) {
                            for (SourceAccountingLine accountingLine : item.getSourceAccountingLineList()) {
                                String accNo = accountingLine.getAccountNumber();
                                Integer vendorIdentifier = document.getVendorHeaderGeneratedIdentifier();
                                if ((accNo != null && accNo != "") && (vendorIdentifier != null)) {
                                    Map accMap = new HashMap();
                                    accMap.put("accountNumber", accNo);
                                    accMap.put("subFundGroupCode", "CLRREV");
                                    accMap.put("vendorHeaderGeneratedIdentifier", vendorIdentifier);
                                    List<Account> account = (List<Account>) KRADServiceLocator.getBusinessObjectService().findMatching(Account.class, accMap);
                                    if (account.size() <= 0) {
                                        GlobalVariables.getMessageMap().putError(PurapPropertyConstants.OFFSET_ACCT_LINE, OleSelectConstant.ERROR_INVALID_ACC_NO, accNo);
                                        valid = false;
                                    }

                                }

                            }


                        }

                    }
                }
            }
        }

        List<OleInvoiceItem> items = document.getItems();
        for (OleInvoiceItem item : items) {
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
            for (OleInvoiceItem item : items) {
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


/*        List<SourceAccountingLine> sourceAccountingLineList = document.getSourceAccountingLines();
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
                        boolean fundCheck = SpringContext.getBean(OleInvoiceFundCheckService.class)
                                .hasSufficientFundCheckRequired(accLine);
                        if (fundCheck) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.ERROR_MSG_FOR_INSUFF_FUND,
                                    RiceKeyConstants.ERROR_CUSTOM,
                                    OLEConstants.INSUFF_FUND + accLine.getAccountNumber());
                            valid = false;
                        }
                    } else if (option.equals(OLEPropertyConstants.WARNING_MSG)) {
                        boolean fundcheckRequired = SpringContext.getBean(OleInvoiceFundCheckService.class)
                                .hasSufficientFundCheckRequired(accLine);
                        if (fundcheckRequired) {
                            GlobalVariables.getMessageMap().putWarning(PurapConstants.DETAIL_TAB_ERRORS,
                                    OleSelectPropertyConstants.INSUFF_FUND,
                                    new String[]{accLine.getAccountNumber()});
                        }
                    }
                }
            }
        }*/
        /*if(!(document.getInvoiceNumber()!=null && document.getInvoiceNumber().equalsIgnoreCase(""))){
            boolean isInvoiceNoDuplicateExist = true;
            String combinationUserString = "";
            Map<String, Object> map = new HashMap<String, Object>();
            if (!(document.getInvoiceNumber() != null && document.getInvoiceNumber().equalsIgnoreCase(""))) {
                map.put(OLEConstants.InvoiceDocument.INVOICE_NUMBER, document.getInvoiceNumber().toString());
                combinationUserString += document.getInvoiceNumber().toString();
            }
            if (!(document.getInvoiceDate() != null && document.getInvoiceDate().toString().equalsIgnoreCase(""))) {
                map.put(OLEConstants.InvoiceDocument.INVOICE_DATE, document.getInvoiceDate());
                combinationUserString += document.getInvoiceDate();
            }
            if (!(document.getVendorHeaderGeneratedIdentifier() != null && document.getVendorHeaderGeneratedIdentifier().toString().equalsIgnoreCase(""))) {
                map.put(OLEConstants.InvoiceDocument.VENDOR_GENERATED_IDENTIFIER, document.getVendorHeaderGeneratedIdentifier().toString());
                combinationUserString += document.getVendorHeaderGeneratedIdentifier().toString();
            }
            if (!(document.getVendorDetailAssignedIdentifier() != null && document.getVendorDetailAssignedIdentifier().toString().equalsIgnoreCase(""))) {
                map.put(OLEConstants.InvoiceDocument.VENDOR_DETAIL_ASSIGNED_GENERATED_IDENTIFIER, document.getVendorDetailAssignedIdentifier().toString());
                combinationUserString += document.getVendorDetailAssignedIdentifier().toString();
            }
            List<OleInvoiceDocument> documents = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, map);
            //List<String> combinationList = new ArrayList<String>();
            Map<String, String> combinationMap = new HashMap<String, String>();
            if (documents.size() > 0) {
                String combinationString = "";
                for (OleInvoiceDocument invoiceDocument : documents) {

                    if (!(invoiceDocument.getInvoiceNumber() != null && invoiceDocument.getInvoiceNumber().equalsIgnoreCase(""))) {
                        combinationString += invoiceDocument.getInvoiceNumber().toString();
                    }
                    if (!(invoiceDocument.getInvoiceDate() != null && invoiceDocument.getInvoiceDate().toString().equalsIgnoreCase(""))) {
                        combinationString += invoiceDocument.getInvoiceDate().toString();
                    }
                    if (!(invoiceDocument.getVendorHeaderGeneratedIdentifier() != null && invoiceDocument.getVendorHeaderGeneratedIdentifier().toString().equalsIgnoreCase(""))) {
                        combinationString += invoiceDocument.getVendorHeaderGeneratedIdentifier();
                    }
                    if (!(invoiceDocument.getVendorDetailAssignedIdentifier() != null && invoiceDocument.getVendorDetailAssignedIdentifier().toString().equalsIgnoreCase(""))) {
                        combinationString += invoiceDocument.getVendorDetailAssignedIdentifier();
                    }
                    // combinationList.add(combinationString.toString());
                    combinationMap.put(combinationString.toString(), invoiceDocument.getApplicationDocumentStatus());

                }
            } else {
                isInvoiceNoDuplicateExist = false;
            }
            boolean isDocumentSaved = false;
            if (document.getDocumentHeader() != null && document.getDocumentHeader().getWorkflowDocument() != null) {
                if (document.getDocumentHeader().getWorkflowDocument().isSaved()) {
                    isDocumentSaved = true;
                }
            }
            if (!isDocumentSaved) {
                if (combinationMap.containsKey(combinationUserString)) {
                    isInvoiceNoDuplicateExist = true;
                } else {
                    isInvoiceNoDuplicateExist = false;
                }
                if (isInvoiceNoDuplicateExist) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.InvoiceDocument.ERROR_DUPLICATE_INVOICE_DATE_NUMBER_VND);
                    valid &= false;
                }
            }
        }*/


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
