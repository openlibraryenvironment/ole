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
package org.kuali.ole.select.document.web.struts;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.web.struts.VendorCreditMemoForm;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleCreditMemoItem;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.vnd.businessobject.VendorCustomerNumber;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;
import java.util.List;


/**
 * Struts Action Form for Credit Memo document.
 */

public class OleVendorCreditMemoForm extends VendorCreditMemoForm {

    private PurApItem newCreditItemLine;
    private boolean currencyTypeIndicator;

    public OleVendorCreditMemoForm() {
        super();
        this.setNewCreditItemLine(null);
        this.setNewPurchasingItemLine(setupNewPurchasingItemLine());
    }


    public PurApItem getNewCreditItemLine() {
        return newCreditItemLine;
    }


    public void setNewCreditItemLine(PurApItem newCreditItemLine) {
        this.newCreditItemLine = newCreditItemLine;
    }


    @Override
    public PurApItem setupNewPurchasingItemLine() {
        OleCreditMemoItem newItem = new OleCreditMemoItem();
        newItem.setItemTypeCode(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE);
        return newItem;
    }

    @Override
    public PurApItem getAndResetNewPurchasingItemLine() {

        PurApItem aPurchasingItemLine = getNewPurchasingItemLine();
        OleCreditMemoItem crdtItem = (OleCreditMemoItem) aPurchasingItemLine;
        setNewPurchasingItemLine(setupNewPurchasingItemLine());
        OleCreditMemoItem crdtItems = (OleCreditMemoItem) this.getNewPurchasingItemLine();

        OleVendorCreditMemoDocument document = (OleVendorCreditMemoDocument) this.getDocument();
        if (document.getVendorDetail().getCurrencyType()!=null){
            if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        if (document.getVendorDetail() != null) {
            String customerNumber = document.getVendorCustomerNumber();

            List<VendorCustomerNumber> vendorCustomerNumbers = document.getVendorDetail().getVendorCustomerNumbers();
            if (customerNumber != null && vendorCustomerNumbers != null) {
                for (VendorCustomerNumber vendorCustomerNumber : vendorCustomerNumbers) {
                    if (vendorCustomerNumber.getVendorCustomerNumber().equalsIgnoreCase(customerNumber)) {
                        if (!currencyTypeIndicator) {
                            if (customerNumber != null) {
                                crdtItems.setItemForeignDiscount(new KualiDecimal(vendorCustomerNumber.getVendorDiscountPercentage() != null ? vendorCustomerNumber.getVendorDiscountPercentage().bigDecimalValue() : new BigDecimal(0.0)));
                                crdtItems.setItemForeignDiscountType(vendorCustomerNumber.getVendorDiscountType());
                            }

                            crdtItems.setItemExchangeRate(crdtItem.getItemExchangeRate());
                            setNewPurchasingItemLine(crdtItems);
                        }
                    }
                }
            } else {
                crdtItems.setItemForeignDiscount(new KualiDecimal(0.0));
                crdtItems.setItemForeignDiscount(new KualiDecimal(0.0));
            }
        }
        return aPurchasingItemLine;
    }

}
