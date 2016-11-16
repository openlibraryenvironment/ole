/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants.PRQSDocumentsStrings;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OleInvoiceValidation extends GenericValidation {

    private boolean currencyTypeIndicator = true;

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        OleInvoiceDocument document = (OleInvoiceDocument) event.getDocument();
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);
        PurchaseOrderDocument po = null;
        if (ObjectUtils.isNull(document.getPurchaseOrderIdentifier())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.PURCHASE_ORDER_ID);
            valid &= false;
        } else {
            po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(document.getPurchaseOrderIdentifier());
        }
        if (ObjectUtils.isNull(document.getInvoiceDate())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.INVOICE_DATE, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.INVOICE_DATE);
            valid &= false;
        }
  /*      if (StringUtils.isBlank(document.getInvoiceNumber())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.INVOICE_NUMBER, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.INVOICE_NUMBER);
            valid &= false;
        }*/
        if (document.getInvoiceCurrencyType()!=null){
            String currencyType = SpringContext.getBean(OleInvoiceService.class).getCurrencyType(document.getInvoiceCurrencyType());
            if (StringUtils.isNotBlank(currencyType)) {
                if(currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                    currencyTypeIndicator=true;
                }
                else{
                    currencyTypeIndicator=false;
                }
            }
        }

        if (!currencyTypeIndicator) {
            if (ObjectUtils.isNull(document.getForeignVendorInvoiceAmount())) {
                if (ObjectUtils.isNull(document.getVendorInvoiceAmount())) {
                    GlobalVariables.getMessageMap().putError(OleSelectConstant.FOREIGN_VENDOR_INVOICE_AMOUNT, OLEKeyConstants.ERROR_REQUIRED, OleSelectConstant.FOREIGN_VENDOR_INVOICE_AMOUNT_STRING);
                    valid &= false;
                }
            } else {
                Long currencyTypeId = po.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                Map documentNumberMap = new HashMap();
                documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
                org.kuali.rice.krad.service.BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                Iterator iterator = exchangeRateList.iterator();
                if(document.getForeignVendorInvoiceAmount().equals(new BigDecimal("0.00"))) {
                    document.setVendorInvoiceAmount(document.getGrandTotal());
                }
                else {
                    if (iterator.hasNext()) {
                        OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                        document.setVendorInvoiceAmount(new KualiDecimal(document.getForeignVendorInvoiceAmount().divide(tempOleExchangeRate.getExchangeRate(), 4, RoundingMode.HALF_UP)));
                    }
                }

            }
        }
        if (ObjectUtils.isNull(document.getVendorInvoiceAmount())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.VENDOR_INVOICE_AMOUNT, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.VENDOR_INVOICE_AMOUNT);
            valid &= false;
        }


            GlobalVariables.getMessageMap().clearErrorPath();
        return valid;
    }

}
