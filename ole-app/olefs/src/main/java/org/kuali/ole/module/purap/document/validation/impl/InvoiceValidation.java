/*
 * Copyright 2008-2009 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.validation.impl;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PRQSDocumentsStrings;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceValidation extends GenericValidation {

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        OleInvoiceDocument document = (OleInvoiceDocument) event.getDocument();
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);
        
       /* if (ObjectUtils.isNull(document.getPurchaseOrderDocuments()) || document.getPurchaseOrderDocuments().size() < 1) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.PURCHASE_ORDER_ID);
            valid &= false;
        }*/
        if (ObjectUtils.isNull(document.getInvoiceDate())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.INVOICE_DATE, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.INVOICE_DATE);
            valid &= false;
        }
        /* if (StringUtils.isBlank(document.getInvoiceNumber())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.INVOICE_NUMBER, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.INVOICE_NUMBER);
            valid &= false;
        } */
        if (ObjectUtils.isNull(document.getVendorInvoiceAmount())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.VENDOR_INVOICE_AMOUNT, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.VENDOR_INVOICE_AMOUNT);
            valid &= false;
        }
        
        if(document.getPaymentMethodIdentifier() != null) {
            String paymentType = SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(document.getPaymentMethodIdentifier());
            if (paymentType.equals(OLEConstants.DEPOSIT)) {
                for (OleInvoiceItem item : (List<OleInvoiceItem>) document.getItems()) {
                    if (item.getItemTypeCode().equals("ITEM")) {
                        if (item.getSourceAccountingLineList().size() <= 0) {
                            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.OFFSET_ACCT_LINE, OLEKeyConstants.ERROR_REQUIRED, PurapConstants.PRQSDocumentsStrings.OFFSET_ACCT_LINE);
                            valid &= false;
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
                                        valid &= false;
                                    }

                                }

                            }


                        }

                    }
                }
            }
        }
        
        GlobalVariables.getMessageMap().clearErrorPath();
        return valid;
    }

}
