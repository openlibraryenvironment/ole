/*
 * Copyright 2006 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.web.struts;

import org.kuali.ole.module.purap.PurapAuthorizationConstants;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.vnd.businessobject.VendorCustomerNumber;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kns.web.ui.ExtraButton;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Struts Action Form for Purchase Order document.
 */
public class OlePurchaseOrderForm extends PurchaseOrderForm {

    private static final OLEConstants result = null;
    private boolean currencyTypeIndicator = true;

    public OlePurchaseOrderForm() {
    }

    @Override
    public PurApItem setupNewPurchasingItemLine() {
        return new OlePurchaseOrderItem();
    }

    @Override
    public PurApItem getAndResetNewPurchasingItemLine() {

        PurApItem aPurchasingItemLine = getNewPurchasingItemLine();
        OlePurchaseOrderItem reqItem = (OlePurchaseOrderItem) aPurchasingItemLine;
        setNewPurchasingItemLine(setupNewPurchasingItemLine());
        OlePurchaseOrderItem reqItems = (OlePurchaseOrderItem) this.getNewPurchasingItemLine();
        PurchaseOrderDocument document = (PurchaseOrderDocument) this.getDocument();

        if (document.getVendorDetail() != null) {
            if (document.getVendorDetail().getCurrencyType()!=null){
                if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
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

    @Override
    protected Map<String, ExtraButton> createButtonsMap() {

        HashMap<String, ExtraButton> result = new HashMap<String, ExtraButton>();
        result = (HashMap) super.createButtonsMap();
        ExtraButton noPrintButton = new ExtraButton();
        noPrintButton.setExtraButtonProperty("methodToCall.printPo");
        noPrintButton.setExtraButtonSource("${" + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + "}buttonsmall_print.gif");
        noPrintButton.setExtraButtonAltText("Print");
        result.put(noPrintButton.getExtraButtonProperty(), noPrintButton);
        return result;
    }

    @Override
    public List<ExtraButton> getExtraButtons() {
        super.getExtraButtons();
        Map buttonsMap = createButtonsMap();
        if (!getEditingMode().containsKey(PurapAuthorizationConstants.PurchaseOrderEditMode.PRINT_PURCHASE_ORDER)) {
            extraButtons.add((ExtraButton) buttonsMap.get("methodToCall.printPo"));
        }
        return extraButtons;

    }
}

