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


import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.sys.document.validation.impl.AccountingRuleEngineRuleBase;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.GlobalVariables;

public class OleRequisitionRuleBase extends AccountingRuleEngineRuleBase implements OleRequisitionRule {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleRequisitionRuleBase.class);

    /**
     * Constructs a OleRequisitionRule.java
     */
    public OleRequisitionRuleBase() {
        super();
    }


    public boolean processCustomAddDiscountBusinessRules(Document document, OleRequisitionItem reqItem) {
        boolean result = true;
        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules OleRequisitionItem---------->");
        if (reqItem.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
            LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules '%' for  OleRequisitionItem---------->");
            int inDecimalPoint = String.valueOf(reqItem.getItemDiscount()).indexOf(".");
            if (inDecimalPoint != -1) {
                if (String.valueOf(reqItem.getItemDiscount()).substring(0, inDecimalPoint).length() > 2) {

                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleRequisitionItem ---------->");
                    result = false;
                } else if (reqItem.getItemDiscount().bigDecimalValue().scale() > 2) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT);
                    LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DECIMAL_LIMIT for OleRequisitionItem ---------->");
                    result = false;
                }

            } else {
                if (String.valueOf(reqItem.getItemDiscount()).length() > 2) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleRequisitionItem----2------>");
                    result = false;
                }
            }
        } else {
            LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules '#' for  OleRequisitionItem---------->");
            int i = reqItem.getItemListPrice().compareTo(reqItem.getItemDiscount());
            if (reqItem.getItemListPrice().compareTo(reqItem.getItemDiscount()) < 0) {

                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleRequisitionItem---------->");
                result = false;
            }
        }
        return result;
    }

    public boolean processCustomForeignCurrencyRequisitionBusinessRules(Document document, OleRequisitionItem item) {
        boolean result = true;
        if (item.getItemForeignListPrice() == null) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_FOREIGN_LIST_PRICE, new String[]{"Foreign List Price"});
            LOG.debug("***Inside ForeignCurrencyRequisitionBusinessRules ItemForeignListPrice is null***");
            result = false;
        }
        if (item.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
            LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '%' For requisition---------->");
            int inDecimalPoint = String.valueOf(item.getItemForeignDiscount()).indexOf(".");
            if (inDecimalPoint != -1) {
                if (String.valueOf(item.getItemForeignDiscount()).substring(0, inDecimalPoint).length() > 2) {

                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For requisition ---------->");
                    result = false;
                } else if (item.getItemForeignDiscount().bigDecimalValue().scale() > 2) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT);
                    LOG.debug("<------------------Inside ForeignCurrencyBusinessRules MAX_DECIMAL_LIMIT For requisition ---------->");
                    result = false;
                }

            } else {
                if (String.valueOf(item.getItemForeignDiscount()).length() > 2) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For requisition ----2------>");
                    result = false;
                }
            }
        } else {
            LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '#' For requisition ---------->");
            int inDecimalPoint = String.valueOf(item.getItemForeignDiscount()).indexOf(".");
            if (inDecimalPoint != -1) {
                if (String.valueOf(item.getItemForeignDiscount()).substring(0, inDecimalPoint).length() > 2) {

                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For requisition ---------->");
                    result = false;
                } else if (item.getItemForeignDiscount().bigDecimalValue().scale() > 2) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT);
                    LOG.debug("<------------------Inside ForeignCurrencyBusinessRules MAX_DECIMAL_LIMIT For requisition ---------->");
                    result = false;
                }

            } else if (String.valueOf(item.getItemForeignDiscount()).length() > 2) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For requisition ----2------>");
                result = false;
            }
            if (item.getItemForeignListPrice().compareTo(item.getItemForeignDiscount()) < 0) {

                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleRequisitionItem---------->");
                result = false;
            }
        }

        return result;
    }


}
