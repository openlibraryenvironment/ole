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

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderVendorQuote;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * A validation that checks whether the given accounting line is accessible to the given user or not
 */
public class PurchaseOrderAddQuoteToVendorValidation extends GenericValidation {

    private PurchaseOrderDocument accountingDocumentForValidation;
    private PurchaseOrderVendorQuote vendorQuote;
    private VendorService vendorService;

    /**
     * Applies rules for validation of the Split of PO and PO child documents
     *
     * @param document A PurchaseOrderDocument (or one of its children)
     * @return True if all relevant validation rules are passed.
     */
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        valid &= isVendorQuoteActiveNotDebarredVendor(vendorQuote.getVendorHeaderGeneratedIdentifier(), vendorQuote.getVendorDetailAssignedIdentifier());
        valid &= vendorQuoteHasRequiredFields(vendorQuote);
        return valid;
    }

    protected boolean isVendorQuoteActiveNotDebarredVendor(Integer vendorHeaderGeneratedIdentifier, Integer vendorDetailAssignedIdentifer) {
        VendorDetail vendorDetail = vendorService.getVendorDetail(vendorHeaderGeneratedIdentifier, vendorDetailAssignedIdentifer);
        if (vendorDetail != null) {
            if (!vendorDetail.isActiveIndicator()) {
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.NEW_PURCHASE_ORDER_VENDOR_QUOTE_VENDOR_NAME, PurapKeyConstants.ERROR_PURCHASE_ORDER_QUOTE_INACTIVE_VENDOR);
                return false;
            } else if (vendorDetail.isVendorDebarred()) {
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.NEW_PURCHASE_ORDER_VENDOR_QUOTE_VENDOR_NAME, PurapKeyConstants.ERROR_PURCHASE_ORDER_QUOTE_DEBARRED_VENDOR);
                return false;
            }
        }
        return true;
    }

    protected boolean vendorQuoteHasRequiredFields(PurchaseOrderVendorQuote vendorQuote) {
        boolean valid = true;
        if (StringUtils.isBlank(vendorQuote.getVendorName())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.NEW_PURCHASE_ORDER_VENDOR_QUOTE_VENDOR_NAME, OLEKeyConstants.ERROR_REQUIRED, "Vendor Name");
            valid = false;
        }
        if (StringUtils.isBlank(vendorQuote.getVendorLine1Address())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.NEW_PURCHASE_ORDER_VENDOR_QUOTE_VENDOR_LINE_1_ADDR, OLEKeyConstants.ERROR_REQUIRED, "Vendor Line 1 Address");
            valid = false;
        }
        if (StringUtils.isBlank(vendorQuote.getVendorCityName())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.NEW_PURCHASE_ORDER_VENDOR_QUOTE_VENDOR_CITY_NAME, OLEKeyConstants.ERROR_REQUIRED, "Vendor City Name");
            valid = false;
        }
        return valid;
    }

    public PurchaseOrderDocument getAccountingDocumentForValidation() {
        return accountingDocumentForValidation;
    }

    public void setAccountingDocumentForValidation(PurchaseOrderDocument accountingDocumentForValidation) {
        this.accountingDocumentForValidation = accountingDocumentForValidation;
    }

    public PurchaseOrderVendorQuote getVendorQuote() {
        return vendorQuote;
    }

    public void setVendorQuote(PurchaseOrderVendorQuote vendorQuote) {
        this.vendorQuote = vendorQuote;
    }

    public VendorService getVendorService() {
        return vendorService;
    }

    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

}

