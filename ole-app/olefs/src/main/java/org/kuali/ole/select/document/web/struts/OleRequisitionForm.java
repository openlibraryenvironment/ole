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

import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.web.struts.RequisitionForm;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleRequisitionCopies;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.businessobject.OleRequisitionPaymentHistory;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.vnd.businessobject.VendorCustomerNumber;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;
import java.util.List;

public class OleRequisitionForm extends RequisitionForm {

    private OleRequisitionCopies newOleCopies = new OleRequisitionCopies();
    private OleRequisitionPaymentHistory newOleRequisitionPaymentHistory = new OleRequisitionPaymentHistory();
    private boolean currencyTypeIndicator = true;
    /**
     * @see org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase#setupNewPurchasingItemLine()
     */
    @Override
    public PurApItem setupNewPurchasingItemLine() {
        return new OleRequisitionItem();
    }

    @Override
    public PurApItem getAndResetNewPurchasingItemLine() {

        PurApItem aPurchasingItemLine = getNewPurchasingItemLine();
        OleRequisitionItem reqItem = (OleRequisitionItem) aPurchasingItemLine;
        setNewPurchasingItemLine(setupNewPurchasingItemLine());
        OleRequisitionItem reqItems = (OleRequisitionItem) this.getNewPurchasingItemLine();

        OleRequisitionDocument document = (OleRequisitionDocument) this.getDocument();
        if (document.getVendorDetail() != null) {

            if (document.getVendorDetail().getCurrencyType()!=null){
                if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME )){
                    currencyTypeIndicator=true;
                }
                else{
                    currencyTypeIndicator=false;
                }
            }
            String customerNumber = document.getVendorCustomerNumber();
            List<VendorCustomerNumber> vendorCustomerNumbers = document.getVendorDetail().getVendorCustomerNumbers();
            if (customerNumber != null && vendorCustomerNumbers != null) {
                for (VendorCustomerNumber vendorCustomerNumber : vendorCustomerNumbers) {
                    if (vendorCustomerNumber.getVendorCustomerNumber().equalsIgnoreCase(customerNumber)) {
                        if (!currencyTypeIndicator) {
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

    /**
     * Gets the newOleCopies attribute.
     *
     * @return Returns the newOleCopies.
     */
    public OleRequisitionCopies getNewOleCopies() {
        return newOleCopies;
    }

    /**
     * Sets the newOleCopies attribute value.
     *
     * @param newOleCopies The newOleCopies to set.
     */
    public void setNewOleCopies(OleRequisitionCopies newOleCopies) {
        this.newOleCopies = newOleCopies;
    }

    /**
     * Gets the newOleRequisitionPaymentHistory attribute.
     *
     * @return Returns the newOleRequisitionPaymentHistory.
     */
    public OleRequisitionPaymentHistory getNewOleRequisitionPaymentHistory() {
        return newOleRequisitionPaymentHistory;
    }

    /**
     * Sets the newOleRequisitionPaymentHistory attribute value.
     *
     * @param newOleRequisitionPaymentHistory
     *         The newOleRequisitionPaymentHistory to set.
     */
    public void setNewOleRequisitionPaymentHistory(OleRequisitionPaymentHistory newOleRequisitionPaymentHistory) {
        this.newOleRequisitionPaymentHistory = newOleRequisitionPaymentHistory;
    }

}