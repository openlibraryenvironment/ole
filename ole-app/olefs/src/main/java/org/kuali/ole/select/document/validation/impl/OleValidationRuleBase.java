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
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.OlePurchaseOrderAmendmentDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.service.OleForiegnVendorPhoneNumberService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.impl.AccountingRuleEngineRuleBase;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleValidationRuleBase extends AccountingRuleEngineRuleBase implements OleValidationRule {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleValidationRuleBase.class);

    protected static ParameterService parameterService;

    /**
     * Constructs a OleRequisitionRule.java
     */
    public OleValidationRuleBase() {
        super();
    }

    @Override
    public boolean processCustomAddDiscountRequisitionBusinessRules(Document document, OleRequisitionItem reqItem) {
        boolean result = true;
        /*if (reqItem.getItemListPrice() == null) {
            reqItem.setItemUnitPrice(BigDecimal.ZERO);
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_LIST_PRICE, new String[]{"List Price"});
            LOG.debug("***Inside DiscountRequisitionBusinessRules ItemListPrice is null***");
            result = false;
        } else if ((reqItem.getItemListPrice()).isLessEqual(KualiDecimal.ZERO)) {
            reqItem.setItemUnitPrice(BigDecimal.ZERO);
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_LIST_PRICE_NON_ZERO, new String[]{"List Price"});
            LOG.debug("***Inside DiscountRequisitionBusinessRules ItemListPrice is non-zero and positive***");
            result = false;
        }*/
        if (reqItem.getItemDiscount() != null && reqItem.getItemDiscountType() != null) {
            if (reqItem.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules '%' for  OleRequisitionItem---------->");
                int inDecimalPoint = String.valueOf(reqItem.getItemDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(reqItem.getItemDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleRequisitionItem ---------->");
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
                if (reqItem.getItemListPrice() != null) {
                    if (reqItem.getItemListPrice().compareTo(reqItem.getItemDiscount()) < 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleRequisitionItem---------->");
                        result = false;
                    }
                }
            }
            if (reqItem.getItemDiscount().bigDecimalValue().scale() > 4) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DECIMAL_LIMIT for OleRequisitionItem ---------->");
                result = false;
            }
        }
        if (reqItem.getSourceAccountingLines().size() > 0) {
            String accountNumber = reqItem.getSourceAccountingLine(0).getAccountNumber();
            String chartOfAccountsCode = reqItem.getSourceAccountingLine(0).getChartOfAccountsCode();
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
            criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
            Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class, criteria);
            String subFundGroupParameter = getParameterService().getParameterValueAsString(Account.class,
                    OleSelectConstant.SUB_FUND_GRP_CD);
            if (account != null && account.getSubFundGroupCode().equalsIgnoreCase(subFundGroupParameter)) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ACCOUNT_NUMBER, new String[]{"Requisition"});
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean processCustomAddDiscountPurchaseOrderBusinessRules(Document document, OlePurchaseOrderItem purItem) {
        boolean result = true;
       /* if (purItem.getItemListPrice() == null) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_LIST_PRICE, new String[]{"List Price"});
            LOG.debug("***Inside DiscountPurchaseOrderBusinessRules ItemListPrice is null***");
            result = false;
        }*/
        if (purItem.getItemDiscount() != null && purItem.getItemDiscountType() != null) {
            if (purItem.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules '%' for  OleRequisitionItem---------->");
                int inDecimalPoint = String.valueOf(purItem.getItemDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(purItem.getItemDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleRequisitionItem ---------->");
                        result = false;
                    }
                } else {

                    if (String.valueOf(purItem.getItemDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleRequisitionItem----2------>");
                        result = false;
                    }
                }
            } else {

                if (purItem.getItemListPrice() != null) {
                    if (purItem.getItemListPrice().compareTo(purItem.getItemDiscount()) < 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleRequisitionItem---------->");
                        result = false;
                    }
                }
            }
            if (purItem.getItemDiscount().bigDecimalValue().scale() > 4) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DECIMAL_LIMIT for OleRequisitionItem ---------->");
                result = false;
            }
        }
        if (purItem.getSourceAccountingLines().size() > 0) {
            String accountNumber = purItem.getSourceAccountingLine(0).getAccountNumber();
            String chartOfAccountsCode = purItem.getSourceAccountingLine(0).getChartOfAccountsCode();
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
            criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
            Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class, criteria);
            String subFundGroupParameter = getParameterService().getParameterValueAsString(Account.class,
                    OleSelectConstant.SUB_FUND_GRP_CD);
            if (account != null && account.getSubFundGroupCode().equalsIgnoreCase(subFundGroupParameter)) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ACCOUNT_NUMBER, new String[]{"Purchase Order"});
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean processCustomAddDiscountPaymentRequestBusinessRules(Document document, OlePaymentRequestItem payItem) {
        boolean result = true;
        /*if (payItem.getItemListPrice() == null || payItem.getItemListPrice().equals(BigDecimal.ZERO)) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_LIST_PRICE, new String[]{"List Price"});
            LOG.debug("***Inside DiscountPaymentRequestBusinessRules ItemListPrice is null***");
            result = false;
        }*/

        if (payItem.getItemDiscount() != null && payItem.getItemDiscountType() != null) {
            if (payItem.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules '%' for  OlePaymentRequestItem---------->");
                int inDecimalPoint = String.valueOf(payItem.getItemDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(payItem.getItemDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OlePaymentRequestItem ---------->");
                        result = false;
                    } else if (payItem.getItemDiscount().bigDecimalValue().scale() > 4) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DECIMAL_LIMIT for OlePaymentRequestItem ---------->");
                        result = false;
                    }
                } else {
                    if (String.valueOf(payItem.getItemDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OlePaymentRequestItem----2------>");
                        result = false;
                    }

                }
            } else {
                if (payItem.getItemListPrice().compareTo(payItem.getItemDiscount()) < 0) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OlePaymentRequestItem---------->");
                    result = false;
                }
            }
        }
        if (payItem.getSourceAccountingLines().size() > 0) {
            String accountNumber = payItem.getSourceAccountingLine(0).getAccountNumber();
            String chartOfAccountsCode = payItem.getSourceAccountingLine(0).getChartOfAccountsCode();
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
            criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
            Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class, criteria);
            String subFundGroupParameter = getParameterService().getParameterValueAsString(Account.class,
                    OleSelectConstant.SUB_FUND_GRP_CD);
            if (account != null && account.getSubFundGroupCode().equalsIgnoreCase(subFundGroupParameter)) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ACCOUNT_NUMBER, new String[]{"Payment Request"});
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean processCustomAddDiscountInvoiceBusinessRules(Document document, OleInvoiceItem payItem) {
        boolean result = true;
        if (payItem.getItemListPrice() == null ) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_LIST_PRICE, new String[]{"List Price"});
            LOG.debug("***Inside processCustomAddDiscountInvoiceBusinessRules ItemListPrice is null***");
            result = false;
        }
       /* if (payItem.getExtendedPrice().equals(BigDecimal.ZERO) && payItem.getItemType().isAdditionalChargeIndicator()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_LIST_PRICE, new String[]{"List Price"});
            LOG.debug("***Inside processCustomAddDiscountInvoiceBusinessRules ItemListPrice is null***");
            result = false;
        }*/

        if (payItem.getItemDiscount() != null && payItem.getItemDiscountType() != null) {
            if (payItem.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside processCustomAddDiscountInvoiceBusinessRules '%' for  OleInvoiceItem---------->");
                int inDecimalPoint = String.valueOf(payItem.getItemDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(payItem.getItemDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountInvoiceBusinessRules PERCENTAGE_MAX_LIMIT for  OleInvoiceItem ---------->");
                        result = false;
                    } else if (payItem.getItemDiscount().bigDecimalValue().scale() > 4) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountInvoiceBusinessRules MAX_DECIMAL_LIMIT for OleInvoiceItem ---------->");
                        result = false;
                    }
                } else {
                    if (String.valueOf(payItem.getItemDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountInvoiceBusinessRules PERCENTAGE_MAX_LIMIT for  OleInvoiceItem----2------>");
                        result = false;
                    }

                }
            } else {
                if (payItem.getItemListPrice().compareTo(payItem.getItemDiscount()) < 0) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside processCustomAddDiscountInvoiceBusinessRules MAX_DISCOUNT_LIMIT for  OleInvoiceItem---------->");
                    result = false;
                }
            }
        }
        if (payItem.getSourceAccountingLines().size() > 0) {
            String accountNumber = payItem.getSourceAccountingLine(0).getAccountNumber();
            String chartOfAccountsCode = payItem.getSourceAccountingLine(0).getChartOfAccountsCode();
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
            criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
            Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class, criteria);
            String subFundGroupParameter = getParameterService().getParameterValueAsString(Account.class,
                    OleSelectConstant.SUB_FUND_GRP_CD);
            if (LOG.isDebugEnabled()){
                LOG.debug("subFundGroupParameter value in processCustomAddDiscountInvoiceBusinessRules >>>>>>>" + subFundGroupParameter);
            }
           /* if (account != null && account.getSubFundGroupCode().equalsIgnoreCase(subFundGroupParameter)) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ACCOUNT_NUMBER, new String[]{"Payment Request"});
                result = false;
            }*/
        }
        return result;
    }

    @Override
    public boolean processCustomForeignCurrencyRequisitionBusinessRules(Document document, OleRequisitionItem item) {
        boolean result = true;
        if (item.getItemForeignListPrice() == null) {
            item.setItemUnitPrice(BigDecimal.ZERO);
            item.setItemForeignListPrice(KualiDecimal.ZERO);
            LOG.debug("***Inside ForeignCurrencyRequisitionBusinessRules ItemForeignListPrice is null***");
            result = true;
        }
        if (item.getItemForeignDiscount() != null && item.getItemForeignDiscountType() != null) {
            if (item.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '%' For requisition---------->");
                int inDecimalPoint = String.valueOf(item.getItemForeignDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(item.getItemForeignDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For requisition ---------->");
                        result = false;
                    }
                } else {
                    if (String.valueOf(item.getItemForeignDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleRequisitionItem----2------>");
                        result = false;
                    }
                }
            } else {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '#' For requisition ---------->");
                if (item.getItemForeignListPrice() != null) {
                    if (item.getItemForeignListPrice().compareTo(item.getItemForeignDiscount()) < 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleRequisitionItem---------->");
                        result = false;
                    }
                }
                if (item.getItemForeignDiscount().bigDecimalValue().scale() > 4) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                    LOG.debug("<------------------Inside ForeignCurrencyBusinessRules MAX_DECIMAL_LIMIT For requisition ---------->");
                    result = false;
                }
            }
        }
        processCustomAddCopiesRequisitionBusinessRules(document, item);
        return result;

    }

    @Override
    public boolean processCustomForeignCurrencyPurchaseOrderBusinessRules(Document document, OlePurchaseOrderItem purItem) {
        boolean result = true;
        if (purItem.getItemForeignListPrice() == null) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_FOREIGN_LIST_PRICE, new String[]{"Foreign List Price"});
            LOG.debug("***Inside ForeignCurrencyRequisitionBusinessRules ItemForeignListPrice is null***");
            result = false;
        }

        if (purItem.getItemForeignDiscount() != null && purItem.getItemForeignDiscountType() != null) {
            if (purItem.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '%' For requisition---------->");
                int inDecimalPoint = String.valueOf(purItem.getItemForeignDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(purItem.getItemForeignDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For requisition ---------->");
                        result = false;
                    }
                } else {
                    if (String.valueOf(purItem.getItemForeignDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleRequisitionItem----2------>");
                        result = false;
                    }
                }
            } else {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '#' For requisition ---------->");
                if (purItem.getItemForeignListPrice() != null) {
                    if (purItem.getItemForeignListPrice().compareTo(purItem.getItemForeignDiscount()) < 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleRequisitionItem---------->");
                        result = false;
                    }
                }
            }
            if (purItem.getItemForeignDiscount().bigDecimalValue().scale() > 4) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules MAX_DECIMAL_LIMIT For requisition ---------->");
                result = false;
            }
        }
        return result;
    }


    @Override
    public boolean processCustomForeignCurrencyPaymentRequestBusinessRules(Document document, OlePaymentRequestItem payItem) {
        boolean result = true;
        if (payItem.getItemForeignListPrice() == null) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_FOREIGN_LIST_PRICE, new String[]{"Foreign List Price"});
            LOG.debug("***Inside ForeignCurrencyPaymentRequestBusinessRules ItemForeignListPrice is null***");
            result = false;
        }

        if (payItem.getItemForeignDiscount() != null && payItem.getItemForeignDiscountType() != null) {
            if (payItem.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '%' For PaymentRequest---------->");
                int inDecimalPoint = String.valueOf(payItem.getItemForeignDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(payItem.getItemForeignDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For PaymentRequest ---------->");
                        result = false;
                    }
                } else {
                    if (String.valueOf(payItem.getItemForeignDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OlePaymentRequestItem----2------>");
                        result = false;
                    }
                }
            } else {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '#' For PaymentRequest ---------->");
                if (payItem.getItemForeignListPrice() != null) {
                    if (payItem.getItemForeignListPrice().compareTo(payItem.getItemForeignDiscount()) < 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OlePaymentRequestItem---------->");
                        result = false;
                    }
                }
            }
            if (payItem.getItemForeignDiscount().bigDecimalValue().scale() > 4) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules MAX_DECIMAL_LIMIT For PaymentRequest ---------->");
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean processCustomForeignCurrencyInvoiceBusinessRules(Document document, OleInvoiceItem payItem) {
        boolean result = true;
        if (payItem.getItemForeignListPrice() == null) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_FOREIGN_LIST_PRICE, new String[]{"Foreign List Price"});
            LOG.debug("***Inside ForeignCurrencyInvoiceBusinessRules ItemForeignListPrice is null***");
            result = false;
        }

        if (payItem.getItemForeignDiscount() != null && payItem.getItemForeignDiscountType() != null) {
            if (payItem.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '%' For Invoice---------->");
                int inDecimalPoint = String.valueOf(payItem.getItemForeignDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(payItem.getItemForeignDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT For Invoice ---------->");
                        result = false;
                    }
                } else {
                    if (String.valueOf(payItem.getItemForeignDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleInvoiceItem----2------>");
                        result = false;
                    }
                }
            } else {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '#' For Invoice ---------->");
                if (payItem.getItemForeignListPrice() != null) {
                    if (payItem.getItemForeignListPrice().compareTo(payItem.getItemForeignDiscount()) < 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleInvoiceItem---------->");
                        result = false;
                    }
                }
            }
            if (payItem.getItemForeignDiscount().bigDecimalValue().scale() > 4) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules MAX_DECIMAL_LIMIT For Invoice ---------->");
                result = false;
            }
        }
        return result;
    }


    /**
     * @see org.kuali.rice.krad.rules.DocumentRuleBase#processCustomSaveDocumentBusinessRules(org.kuali.rice.krad.document.Document)
     */
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        if (document instanceof OleRequisitionDocument) {
            OleRequisitionDocument oleRequisitionDocument = (OleRequisitionDocument) document;
            selectVendorDetailsForRequisitionDocument(oleRequisitionDocument);
            validateItemAccount(oleRequisitionDocument);
            validateDeliverAddress(oleRequisitionDocument);
        } else if (document instanceof OlePaymentRequestDocument) {
            OlePaymentRequestDocument olePaymentRequestDocument = (OlePaymentRequestDocument) document;
            if (olePaymentRequestDocument.getInvoiceIdentifier() == null) {
                selectVendorDetailsForPaymentRequestDocumet(olePaymentRequestDocument);
            }

        } else if (document instanceof OleInvoiceDocument) {
            OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) document;
            oleInvoiceDocument.setPaymentMethodId(oleInvoiceDocument.getPaymentMethodId());
            oleInvoiceDocument.setInvoiceCurrencyTypeId(oleInvoiceDocument.getInvoiceCurrencyTypeId());
        } else if (document instanceof OlePurchaseOrderAmendmentDocument) {
            OlePurchaseOrderAmendmentDocument olePurchaseOrderAmendmentDocument = (OlePurchaseOrderAmendmentDocument) document;
            selectVendorDetailsForPurchaseOrderAmendmentDocument(olePurchaseOrderAmendmentDocument);
        }
        boolean result = super.processCustomSaveDocumentBusinessRules(document);
        /*result &= isRequestorPhoneNumberValid(document);*/
        return result;
    }

    public void validateItemAccount(OleRequisitionDocument oleRequisitionDocument){
        List<OleRequisitionItem> oleRequisitionItemList = oleRequisitionDocument.getItems();
        for(OleRequisitionItem item : oleRequisitionItemList){
            if(item.getItemTypeCode().equals("ITEM") && item.getSourceAccountingLines().size() <=0){
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEConstants.NO_ACC_LINE);
            }else{
                if(item.getExtendedPrice().isGreaterThan(KualiDecimal.ZERO) && item.getSourceAccountingLines().size()<=0){
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEConstants.NO_ADD_ACC_LINE,item.getItemTypeCode());
                }
            }
        }
    }

    public void validateAdditionalCharges(OleRequisitionDocument oleRequisitionDocument){


    }

    public void validateDeliverAddress(OleRequisitionDocument oleRequisitionDocument){
        boolean receivingAddress = oleRequisitionDocument.getAddressToVendorIndicator();
        if(receivingAddress){
            if(oleRequisitionDocument.getReceivingName() == null){
                GlobalVariables.getMessageMap().putError(PurapConstants.DELIVERY_TAB_ERRORS, OLEConstants.NO_RECEIVING_ADDR);
            }
        }else{
            if(oleRequisitionDocument.getDeliveryBuildingLine1Address() == null){
                GlobalVariables.getMessageMap().putError(PurapConstants.DELIVERY_TAB_ERRORS, OLEConstants.NO_DELIVERY_ADDR);
            }
        }
    }

    public void selectVendorDetailsForRequisitionDocument(OleRequisitionDocument oleRequisitionDocument) {

        if(oleRequisitionDocument.getVendorName()== null || oleRequisitionDocument.getVendorName().isEmpty()){
            GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.NO_VENDOR);
        }
        if (oleRequisitionDocument.getVendorAliasName() != null && oleRequisitionDocument.getVendorAliasName().length() > 0) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, oleRequisitionDocument.getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {  /* if there matching vendor found for the given vendor alias name */
                if (oleRequisitionDocument.getVendorHeaderGeneratedIdentifier() == null && oleRequisitionDocument.getVendorDetailAssignedIdentifier() == null) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_SELECT);
                } else {
                    String vendorDetailAssignedIdentifier = vendorAliasList.get(0).getVendorDetailAssignedIdentifier().toString();
                    String VendorHeaderGeneratedIdentifier = vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier().toString();
                    if (oleRequisitionDocument.getVendorHeaderGeneratedIdentifier().toString().equals(VendorHeaderGeneratedIdentifier) &&
                            oleRequisitionDocument.getVendorDetailAssignedIdentifier().toString().equals(vendorDetailAssignedIdentifier)) {
                        LOG.debug("###########vendors are allowed to save###########");
                    } else {
                        GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_SAME);
                    }
                }
            } else {     /* If there is no matching vendor found*/
                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
            }
        }
    }

    public void selectVendorDetailsForPaymentRequestDocumet(OlePaymentRequestDocument olePaymentRequestDocument) {
        olePaymentRequestDocument.setPaymentMethodId(olePaymentRequestDocument.getPaymentMethod().getPaymentMethodId());
        if (((olePaymentRequestDocument.getVendorAliasName() != null && olePaymentRequestDocument.getVendorAliasName().length() > 0))) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, olePaymentRequestDocument.getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {  /* if there matching vendor found for the given vendor alias name */
                if (olePaymentRequestDocument.getVendorHeaderGeneratedIdentifier() == null && olePaymentRequestDocument.getVendorDetailAssignedIdentifier() == null) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_SELECT);
                } else {
                    String vendorDetailAssignedIdentifier = vendorAliasList.get(0).getVendorDetailAssignedIdentifier().toString();
                    String VendorHeaderGeneratedIdentifier = vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier().toString();
                    if (olePaymentRequestDocument.getVendorHeaderGeneratedIdentifier().toString().equals(VendorHeaderGeneratedIdentifier) &&
                            olePaymentRequestDocument.getVendorDetailAssignedIdentifier().toString().equals(vendorDetailAssignedIdentifier)) {
                        LOG.debug("###########vendors are allowed to save###########");
                    } else {
                        GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_SAME);
                    }
                }
            } else {     /* If there is no matching vendor found*/
                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
            }
        }
    }

    public void selectVendorDetailsForPurchaseOrderAmendmentDocument(OlePurchaseOrderAmendmentDocument olePurchaseOrderAmendmentDocument) {
        if (((olePurchaseOrderAmendmentDocument.getVendorAliasName() != null && olePurchaseOrderAmendmentDocument.getVendorAliasName().length() > 0))) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, olePurchaseOrderAmendmentDocument.getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {  /* if there matching vendor found for the given vendor alias name */
                if (olePurchaseOrderAmendmentDocument.getVendorHeaderGeneratedIdentifier() == null && olePurchaseOrderAmendmentDocument.getVendorDetailAssignedIdentifier() == null) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_SELECT);
                } else {
                    String vendorDetailAssignedIdentifier = vendorAliasList.get(0).getVendorDetailAssignedIdentifier().toString();
                    String VendorHeaderGeneratedIdentifier = vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier().toString();
                    if (olePurchaseOrderAmendmentDocument.getVendorHeaderGeneratedIdentifier().toString().equals(VendorHeaderGeneratedIdentifier) &&
                            olePurchaseOrderAmendmentDocument.getVendorDetailAssignedIdentifier().toString().equals(vendorDetailAssignedIdentifier)) {
                        LOG.debug("###########vendors are allowed to save###########");
                    } else {
                        GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_SAME);
                    }
                }
            } else {     /* If there is no matching vendor found*/
                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
            }
        }
    }

    public boolean isRequestorPhoneNumberValid(Document document) {
        boolean valid = true;
        MessageMap errorMap = GlobalVariables.getMessageMap();
        if (document instanceof RequisitionDocument) {
            RequisitionDocument req = (RequisitionDocument) document;
            String phNumber = req.getRequestorPersonPhoneNumber();
            if (StringUtils.isNotEmpty(phNumber) && !SpringContext.getBean(OleForiegnVendorPhoneNumberService.class).isValidForiegnVendorPhoneNumber(phNumber)) {
                errorMap.putError(OleSelectConstant.REQUESTOR_PERSON_PHONE_NUMBER, OleSelectConstant.ERROR_REQUESTOR_PHONE_NUMBER);
                valid &= false;
            }
        } else if (document instanceof PurchaseOrderDocument) {
            PurchaseOrderDocument req = (PurchaseOrderDocument) document;
            String phNumber = req.getRequestorPersonPhoneNumber();
            if (StringUtils.isNotEmpty(phNumber) && !SpringContext.getBean(OleForiegnVendorPhoneNumberService.class).isValidForiegnVendorPhoneNumber(phNumber)) {
                errorMap.putError(OleSelectConstant.PURCHASE_ORDER_PERSON_PHONE_NUMBER, OleSelectConstant.ERROR_REQUESTOR_PHONE_NUMBER);
                valid &= false;
            }
        }

        return valid;
    }

    @Override
    public boolean processCustomPaymentRequestDescriptionBusinessRules(Document document, OlePaymentRequestItem payItem) {
        boolean validate = true;
        if (payItem.getItemDescription() == null || payItem.getItemDescription().isEmpty()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, new String[]{"Line Item"});
            validate = false;
        }
        return validate;
    }

    @Override
    public boolean processCustomInvoiceDescriptionBusinessRules(Document document, OleInvoiceItem payItem) {
        boolean validate = true;
        if (payItem.getItemDescription() == null || payItem.getItemDescription().isEmpty()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, new String[]{"Line Item"});
            validate = false;
        }
        return validate;
    }

    @Override
    public boolean processCustomPurchaseOrderDescriptionBusinessRules(Document document, OlePurchaseOrderItem purItem) {
        boolean validate = true;
        if (purItem.getItemDescription() == null || purItem.getItemDescription().isEmpty()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, new String[]{"Line Item"});
            validate = false;
        }
        return validate;
    }

    @Override
    public boolean processCustomForeignCurrencyCreditMemoBusinessRules(Document document, OleCreditMemoItem creditMemoItem) {
        boolean result = true;
        if (creditMemoItem.getItemForeignListPrice() == null) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ITEM_FOREIGN_LIST_PRICE, new String[]{"Foreign List Price"});
            LOG.debug("***Inside ForeignCurrencyCreditMemoBusinessRules ItemForeignListPrice is null***");
            result = false;
        }

        if (creditMemoItem.getItemForeignDiscount() != null && creditMemoItem.getItemForeignDiscountType() != null) {
            if (creditMemoItem.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '%' For CreditMemo---------->");
                int inDecimalPoint = String.valueOf(creditMemoItem.getItemForeignDiscount()).indexOf(".");
                if (inDecimalPoint != -1) {
                    if (String.valueOf(creditMemoItem.getItemForeignDiscount()).substring(0, inDecimalPoint).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside ForeignCurrencyBusinessRules PERCENTAGE_MAX_LIMIT CreditMemo ---------->");
                        result = false;
                    }
                } else {
                    if (String.valueOf(creditMemoItem.getItemForeignDiscount()).length() > 2) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.PERCENTAGE_MAX_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules PERCENTAGE_MAX_LIMIT for  OleCreditMemoItem----2------>");
                        result = false;
                    }
                }
            } else {
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules '#' For CreditMemoItem ---------->");
                if (creditMemoItem.getItemForeignListPrice() != null) {
                    if (creditMemoItem.getItemForeignListPrice().compareTo(creditMemoItem.getItemForeignDiscount()) < 0) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DISCOUNT_LIMIT, new String[]{"Discount"});
                        LOG.debug("<------------------Inside processCustomAddDiscountBusinessRules MAX_DISCOUNT_LIMIT for  OleCreditMemoItem---------->");
                        result = false;
                    }
                }
            }
            if (creditMemoItem.getItemForeignDiscount().bigDecimalValue().scale() > 4) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.MAX_DECIMAL_LIMIT, new String[]{"Discount"});
                LOG.debug("<------------------Inside ForeignCurrencyBusinessRules MAX_DECIMAL_LIMIT For CreditMemoItem ---------->");
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean processCustomCreditMemoDescriptionBusinessRules(Document document, OleCreditMemoItem creditMemoItem) {
        boolean validate = true;
        if (creditMemoItem.getItemDescription() == null || creditMemoItem.getItemDescription().isEmpty()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, new String[]{"Line Item"});
            validate = false;
        }
        return validate;
    }

    @Override
    public boolean processCustomAddCopiesRequisitionBusinessRules(Document document, OleRequisitionItem reqItem) {
        boolean isValid = true;
        if (reqItem.getItemType() != null && reqItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
            if (reqItem.getItemQuantity() != null && reqItem.getItemNoOfParts() != null && (reqItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                    || reqItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1)))) {
                if (reqItem.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || reqItem.getLinkToOrderOption().equals(OLEConstants.EB_PRINT)){
                    OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                    isValid = oleCopyHelperService.checkForTotalCopiesGreaterThanQuantityAtSubmit(reqItem.getCopies(), reqItem.getItemQuantity());
                    for (OleCopies copies : reqItem.getCopies()) {
                        List<String> volChar = new ArrayList<>();
                        String[] volNumbers = copies.getVolumeNumber() != null ? copies.getVolumeNumber().split(",") : new String[0];
                        for (String volStr : volNumbers) {
                            volChar.add(volStr);
                        }
                        Integer itemCount = volChar.size();
                        isValid &= oleCopyHelperService.checkCopyEntry(
                                copies.getItemCopies(), copies.getLocationCopies(), itemCount, reqItem.getItemQuantity(),
                                reqItem.getItemNoOfParts(), reqItem.getCopies(), reqItem.getVolumeNumber(), true);
                        if (isValid)
                            reqItem.setItemLocation(OLEConstants.MULTIPLE_ITEM_LOC);
                    }
                }
                /*else if (reqItem.getLinkToOrderOption().equals(OLEConstants.NB_ELECTRONIC) || reqItem.getLinkToOrderOption().equals(OLEConstants.EB_ELECTRONIC)){
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                            OLEConstants.ITEM_COPIESANDPARTS_SHOULDNOT_BE_GREATERTHAN_ONE_EINSTANCE, new String[]{});
                }*/
            } else {
                if (reqItem.getItemLocation() == null || reqItem.getItemLocation().isEmpty()) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                            OLEConstants.ITEM_LOCATION_REQUIRED, new String[]{});
                }
            }
        }
        return isValid;
    }

    public boolean processCustomAddCopiesPurchaseOrderBusinessRules(Document document, OlePurchaseOrderItem purItem) {
        boolean isValid = true;
        if (purItem.getItemType() != null && purItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
            if (purItem.getItemQuantity() != null && purItem.getItemNoOfParts() != null && (purItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                    || purItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1)))) {
                if (purItem.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || purItem.getLinkToOrderOption().equals(OLEConstants.EB_PRINT)){
                    OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                    isValid = oleCopyHelperService.checkForTotalCopiesGreaterThanQuantityAtSubmit(purItem.getCopies(), purItem.getItemQuantity());
                    for (OleCopies copies : purItem.getCopies()) {
                        List<String> volChar = new ArrayList<>();
                        String[] volNumbers = copies.getVolumeNumber() != null ? copies.getVolumeNumber().split(",") : new String[0];
                        for (String volStr : volNumbers) {
                            volChar.add(volStr);
                        }
                        Integer itemCount = volChar.size();
                        isValid &= oleCopyHelperService.checkCopyEntry(
                                copies.getItemCopies(), copies.getLocationCopies(), itemCount, purItem.getItemQuantity(),
                                purItem.getItemNoOfParts(), purItem.getCopies(), purItem.getVolumeNumber(), true);
                        if (isValid)
                            purItem.setItemLocation(OLEConstants.MULTIPLE_ITEM_LOC);
                    }
                }
                /*else if (purItem.getLinkToOrderOption().equals(OLEConstants.NB_ELECTRONIC) || purItem.getLinkToOrderOption().equals(OLEConstants.EB_ELECTRONIC)){
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                            OLEConstants.ITEM_COPIESANDPARTS_SHOULDNOT_BE_GREATERTHAN_ONE_EINSTANCE, new String[]{});
                }*/
                else {
                    OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                    isValid = oleCopyHelperService.checkForTotalCopiesGreaterThanQuantityAtSubmit(purItem.getCopies(), purItem.getItemQuantity());
                    for (OleCopies copies : purItem.getCopies()) {
                        List<String> volChar = new ArrayList<>();
                        String[] volNumbers = copies.getVolumeNumber() != null ? copies.getVolumeNumber().split(",") : new String[0];
                        for (String volStr : volNumbers) {
                            volChar.add(volStr);
                        }
                        Integer itemCount = volChar.size();
                        isValid &= oleCopyHelperService.checkCopyEntry(
                                copies.getItemCopies(), copies.getLocationCopies(), itemCount, purItem.getItemQuantity(),
                                purItem.getItemNoOfParts(), purItem.getCopies(), purItem.getVolumeNumber(), true);
                        if (isValid)
                            purItem.setItemLocation(OLEConstants.MULTIPLE_ITEM_LOC);
                    }
                }

            } else {
                if (purItem.getItemLocation() == null || purItem.getItemLocation().isEmpty()) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                            OLEConstants.ITEM_LOCATION_REQUIRED, new String[]{});
                }
            }
        }
        return isValid;
    }

    public boolean processInvoiceSubscriptionOverlayBusinessRules(Document document, OleInvoiceItem invoiceItem) {
        boolean isValid = true;
        if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && invoiceItem.isSubscriptionOverlap()) {
            if (invoiceItem.getSubscriptionFromDate() == null) {
                isValid &= false;
                GlobalVariables.getMessageMap().putErrorForSectionId(OleSelectConstant.INVOICE_ITEM_SECTION_ID,
                        OleSelectConstant.ERROR_SUBSCIPTION_FROM_DATE_REQUIRED);
            }
            if (invoiceItem.getSubscriptionToDate() == null) {
                isValid &= false;
                GlobalVariables.getMessageMap().putErrorForSectionId(OleSelectConstant.INVOICE_ITEM_SECTION_ID,
                        OleSelectConstant.ERROR_SUBSCIPTION_TO_DATE_REQUIRED);
            }
        }
        if (invoiceItem.getSubscriptionFromDate() != null && invoiceItem.getSubscriptionToDate() != null &&
            invoiceItem.getSubscriptionFromDate().compareTo(invoiceItem.getSubscriptionToDate()) > 0)  {
                isValid &= false;
                GlobalVariables.getMessageMap().putErrorForSectionId(OleSelectConstant.INVOICE_ITEM_SECTION_ID,
                            OleSelectConstant.ERROR_SUBSCIPTION_FROM_DATE_GREATER_THAN_TO_DATE);
        }
        return isValid;
    }

    public ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = SpringContext.getBean(ParameterService.class);
        }
        return parameterService;
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
}
