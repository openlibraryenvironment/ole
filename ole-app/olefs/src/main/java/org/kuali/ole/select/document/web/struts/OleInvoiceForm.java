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
package org.kuali.ole.select.document.web.struts;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.web.struts.InvoiceForm;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.vnd.businessobject.VendorCustomerNumber;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class is the KualiForm class for Ole Invoice
 */
public class OleInvoiceForm extends InvoiceForm {


    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleInvoiceForm.class);

    private PurApItem newPaymentItemLine;

    /**
     * setting Item Type to Unordered Item
     *
     * @see org.kuali.ole.module.purap.document.web.struts.InvoiceForm#setupNewPurchasingItemLine()
     */
    @Override
    public PurApItem setupNewPurchasingItemLine() {
        OleInvoiceItem newItem = new OleInvoiceItem();
        newItem.setItemTypeCode(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE);
        return newItem;
    }

    @Override
    public PurApItem getAndResetNewPurchasingItemLine() {

        PurApItem aPurchasingItemLine = getNewPurchasingItemLine();
        OleInvoiceItem reqItem = (OleInvoiceItem) aPurchasingItemLine;
        setNewPurchasingItemLine(setupNewPurchasingItemLine());
        OleInvoiceItem reqItems = (OleInvoiceItem) this.getNewPurchasingItemLine();

        OleInvoiceDocument document = (OleInvoiceDocument) this.getDocument();
        if (document.getVendorDetail() != null) {
            String customerNumber = document.getVendorCustomerNumber();

            List<VendorCustomerNumber> vendorCustomerNumbers = document.getVendorDetail().getVendorCustomerNumbers();
            if (customerNumber != null && vendorCustomerNumbers != null) {
                for (VendorCustomerNumber vendorCustomerNumber : vendorCustomerNumbers) {
                    if (vendorCustomerNumber.getVendorCustomerNumber().equalsIgnoreCase(customerNumber)) {
                        if (document.getVendorDetail().getVendorHeader().getVendorForeignIndicator()) {
                            if (customerNumber != null) {
                                reqItems.setItemForeignDiscount(new KualiDecimal(vendorCustomerNumber.getVendorDiscountPercentage() != null ? vendorCustomerNumber.getVendorDiscountPercentage().bigDecimalValue() : new BigDecimal(0.0)));
                                reqItems.setItemForeignDiscountType(vendorCustomerNumber.getVendorDiscountType());
                            }

                            reqItems.setItemExchangeRate(reqItem.getItemExchangeRate());
                            setNewPurchasingItemLine(reqItems);
                        } else {
                            if (customerNumber != null) {
                                reqItems.setItemDiscount(new KualiDecimal(vendorCustomerNumber.getVendorDiscountPercentage() != null ? vendorCustomerNumber.getVendorDiscountPercentage().bigDecimalValue() : new BigDecimal(0.0)));
                                reqItems.setItemDiscountType(vendorCustomerNumber.getVendorDiscountType());
                            }
                            setNewPurchasingItemLine(reqItems);
                        }
                    }
                }
            } else {
                reqItems.setItemForeignDiscount(new KualiDecimal(0.0));
                reqItems.setItemDiscount(new KualiDecimal(0));
            }
        }
        return aPurchasingItemLine;

    }

    public PurApItem getNewPaymentItemLine() {
        return newPaymentItemLine;
    }

    public void setNewPaymentItemLine(PurApItem newPaymentItemLine) {
        this.newPaymentItemLine = newPaymentItemLine;
    }

    public OleInvoiceForm() {
        super();
        this.setNewPaymentItemLine(null);
        this.setNewPurchasingItemLine(setupNewPurchasingItemLine());
    }

}
