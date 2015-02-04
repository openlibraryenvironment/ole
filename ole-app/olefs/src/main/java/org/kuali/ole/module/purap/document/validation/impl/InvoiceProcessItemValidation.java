/*
 * Copyright 2009 The Kuali Foundation
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

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.ItemFields;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.businessobject.InvoiceItem;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.Validation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.ole.sys.document.validation.impl.*;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.ole.vnd.businessobject.VendorCustomerNumber;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceProcessItemValidation extends GenericValidation {

    private PurapService purapService;
    private PurApItem itemForValidation;
    private AttributedDocumentEvent event;
    private CompositeValidation reviewAccountingLineValidation;
    private InvoiceDocument invoiceDocument;
    private PurApAccountingLine invoiceAccountingLine;

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        this.event = event;

        InvoiceDocument invoiceDocument = (InvoiceDocument) event.getDocument();
        InvoiceItem invoiceItem = (InvoiceItem) itemForValidation;

        valid &= validateEachItem(invoiceDocument, invoiceItem);

        return valid;

    }

    /**
     * Calls another validate item method and passes an identifier string from the item.
     *
     * @param invoiceDocument - payment request document.
     * @param item
     * @return
     */
    protected boolean validateEachItem(InvoiceDocument invoiceDocument, InvoiceItem item) {
        boolean valid = true;
        String identifierString = item.getItemIdentifierString();
        valid &= validateItem(invoiceDocument, item, identifierString);
        return valid;
    }

    /**
     * Performs validation if full document entry not completed and peforms varying item validation.
     * Such as, above the line, items without accounts, items with accounts.
     *
     * @param invoiceDocument  - payment request document
     * @param item             - payment request item
     * @param identifierString - identifier string used to mark in an error map
     * @return
     */
    public boolean validateItem(InvoiceDocument invoiceDocument, InvoiceItem item, String identifierString) {
        boolean valid = true;
        // only run item validations if before full entry
        if (!purapService.isFullDocumentEntryCompleted(invoiceDocument)) {
            if (item.getItemType().isLineItemIndicator()) {
                valid &= validateAboveTheLineItems(item, identifierString, invoiceDocument.isReceivingDocumentRequiredIndicator());
                valid &= validatePOItem((OleInvoiceItem)item);
            }
            valid &= validateItemWithoutAccounts(item, identifierString);
        }
        // always run account validations
        valid &= validateProrationType(invoiceDocument);
        valid &= validateItemAccounts(invoiceDocument, item, identifierString);
        return valid;
    }

    private boolean validatePOItem (OleInvoiceItem invoiceItem) {
        if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
            if(invoiceItem.getPoItemIdentifier() == null ||
                    invoiceItem.getPoItemIdentifier().compareTo(new Integer(0)) == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITEM_WITHOUT_PO);
                return false;
            }
        }
        return true;

    }

    protected boolean validateProrationType(InvoiceDocument invoiceDocument) {
        boolean isValid = true;
        OleInvoiceDocument document = (OleInvoiceDocument) invoiceDocument;
        List<OleInvoiceItem> items = document.getItems();
        boolean additionalItemPresent = false;
        boolean canProrate = false;
        KualiDecimal additionalCharge = KualiDecimal.ZERO;
        KualiDecimal totalAmt = document.getInvoicedItemTotal() != null ?
                new KualiDecimal(document.getInvoicedItemTotal()) : KualiDecimal.ZERO;
        for (OleInvoiceItem invoiceItem : items) {
            if (invoiceItem.getItemType().isAdditionalChargeIndicator() && invoiceItem.getExtendedPrice() != null &&
                    !invoiceItem.getExtendedPrice().isZero()) {
                additionalCharge = additionalCharge.add(invoiceItem.getExtendedPrice());
                additionalItemPresent  = true;
            }
            if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && invoiceItem.getItemUnitPrice().compareTo(BigDecimal.ZERO) != 0 ) {
                canProrate = true;
            }
        }
        if (additionalItemPresent && ((document.getProrateBy() == null) ||
                (!document.isProrateDollar() && !document.isProrateManual() && !document.isProrateQty() && !document.isNoProrate()))) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OleSelectConstant.INVOICE_ADDITIONAL_ITEM_SECTION_ID,
                    OLEKeyConstants.ERROR_REQUIRED, PurapConstants.PRQSDocumentsStrings.PRORATION_TYPE);
            isValid &= false;

        }
        if ((totalAmt.isZero() || !canProrate) && document.isProrateDollar() ) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ADDITIONAL_CHARGE_SECTION_ID,
                    OLEKeyConstants.ERROR_PRORATE_DOLLAR_ZERO_ITEM_TOTAL);
        }
        if (document.getVendorCustomerNumber() != null && !document.getVendorCustomerNumber().equalsIgnoreCase("")) {
            Map<String, String> map = new HashMap<String, String>();
            if (document.getVendorCustomerNumber() != null && !document.getVendorCustomerNumber().equalsIgnoreCase("")) {
                map.put(OLEConstants.VENDOR_CUSTOMER_NUMBER, document.getVendorCustomerNumber());
            }
            if (document.getVendorHeaderGeneratedIdentifier() != null && !document.getVendorHeaderGeneratedIdentifier().toString().equalsIgnoreCase("")) {
                map.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, document.getVendorHeaderGeneratedIdentifier().toString());
            }
            if (document.getVendorDetailAssignedIdentifier() != null && !document.getVendorDetailAssignedIdentifier().toString().equalsIgnoreCase("")) {
                map.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, document.getVendorDetailAssignedIdentifier().toString());
            }
            List<VendorCustomerNumber> vendorCustomerNumbers = (List<VendorCustomerNumber>) KRADServiceLocator.getBusinessObjectService().findMatching(VendorCustomerNumber.class, map);
            if (!(vendorCustomerNumbers != null && vendorCustomerNumbers.size() > 0)) {
                isValid &= false;
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.INVALID_ACQUISITION_NUMBER);
            }
        }
       /* if (totalAmt != null && totalAmt.isZero() && !additionalCharge.isZero() && document.isProrateDollar() ) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ADDITIONAL_CHARGE_SECTION_ID,
                    OLEKeyConstants.ERROR_PRORATE_DOLLAR_ZERO_ITEM_TOTAL);
            isValid &= false;
        }*/
        return isValid;
    }
    /**
     * Validates above the line items.
     *
     * @param item             - payment request item
     * @param identifierString - identifier string used to mark in an error map
     * @return
     */
    protected boolean validateAboveTheLineItems(InvoiceItem item, String identifierString, boolean isReceivingDocumentRequiredIndicator) {
        boolean valid = true;
        // Currently Quantity is allowed to be NULL on screen;
        // must be either a positive number or NULL for DB
        if (ObjectUtils.isNotNull(item.getItemQuantity())) {
            /*if (item.getItemQuantity().isNegative()) {
                // if quantity is negative give an error
                valid = false;
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_AMOUNT_BELOW_ZERO, ItemFields.INVOICE_QUANTITY, identifierString);
            }*/
            if (!isReceivingDocumentRequiredIndicator) {
                /*if (item.getPoOutstandingQuantity().isLessThan(item.getItemQuantity())) {
                    valid = false;
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_QUANTITY_TOO_MANY, ItemFields.INVOICE_QUANTITY, identifierString, ItemFields.OPEN_QUANTITY);
                } */
            }
        }
        // if (ObjectUtils.isNotNull(item.getExtendedPrice()) && item.getExtendedPrice().isPositive() && ObjectUtils.isNotNull(item.getPoOutstandingQuantity()) && item.getPoOutstandingQuantity().isPositive()) {
        if (ObjectUtils.isNotNull(item.getExtendedPrice()) && item.getExtendedPrice().isPositive()) {

            // here we must require the user to enter some value for quantity if they want a credit amount associated
            if (ObjectUtils.isNull(item.getItemQuantity()) || item.getItemQuantity().isZero()) {
                // here we have a user not entering a quantity with an extended amount but the PO has a quantity...require user to
                // enter a quantity
                valid = false;
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_QUANTITY_REQUIRED, ItemFields.INVOICE_QUANTITY, identifierString, ItemFields.OPEN_QUANTITY);
            }
        }

        // check that non-quantity based items are not trying to pay on a zero encumbrance amount (check only prior to ap approval)
        if ((ObjectUtils.isNull(item.getInvoice().getPurapDocumentIdentifier())) || (PurapConstants.InvoiceStatuses.APPDOC_IN_PROCESS.equals(item.getInvoice().getApplicationDocumentStatus()))) {
// RICE20 : needed? :  !purapService.isFullDocumentEntryCompleted(item.getInvoice())) {
            if ((item.getItemType().isAmountBasedGeneralLedgerIndicator()) && ((item.getExtendedPrice() != null) && item.getExtendedPrice().isNonZero())) {
                if (item.getPoOutstandingAmount() == null || item.getPoOutstandingAmount().isZero()) {
                    valid = false;
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_AMOUNT_ALREADY_PAID, identifierString);
                }
            }
        }

        return valid;
    }

    /**
     * Validates that the item must contain at least one account
     *
     * @param item - payment request item
     * @return
     */
    public boolean validateItemWithoutAccounts(InvoiceItem item, String identifierString) {
        boolean valid = true;
        if (ObjectUtils.isNotNull(item.getItemUnitPrice()) && (new KualiDecimal(item.getItemUnitPrice())).isNonZero() && item.isAccountListEmpty()) {
            valid = false;
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_ACCOUNTING_INCOMPLETE, identifierString);
        }
        return valid;
    }

    /**
     * Validates the totals for the item by account, that the total by each accounting line for the item, matches
     * the extended price on the item.
     *
     * @param invoiceDocument  - payment request document
     * @param item             - payment request item to validate
     * @param identifierString - identifier string used to mark in an error map
     * @return
     */
    public boolean validateItemAccounts(InvoiceDocument invoiceDocument, InvoiceItem item, String identifierString) {
        boolean valid = true;
        List<PurApAccountingLine> accountingLines = item.getSourceAccountingLines();
        KualiDecimal itemTotal = item.getTotalAmount();
        KualiDecimal accountTotal = KualiDecimal.ZERO;
        KualiDecimal prorateSurcharge = KualiDecimal.ZERO;
        OleInvoiceItem invoiceItem = (OleInvoiceItem) item;
        if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && invoiceItem.getExtendedPrice() != null && invoiceItem.getExtendedPrice().compareTo(KualiDecimal.ZERO) != 0) {
            if (invoiceItem.getItemSurcharge() != null && invoiceItem.getItemTypeCode().equals("ITEM")) {
                prorateSurcharge = new KualiDecimal(invoiceItem.getItemSurcharge()).multiply(invoiceItem.getItemQuantity());
            }
            itemTotal = itemTotal.subtract(prorateSurcharge);
        }
        for (PurApAccountingLine accountingLine : accountingLines) {
            if (accountingLine.getAmount().isZero()) {
                if (!canApproveAccountingLinesWithZeroAmount()) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_ACCOUNTING_AMOUNT_INVALID, itemForValidation.getItemIdentifierString());
                    valid &= false;
                }
            }
            valid &= reviewAccountingLineValidation(invoiceDocument, accountingLine);
            accountTotal = accountTotal.add(accountingLine.getAmount());
        }
        if (purapService.isFullDocumentEntryCompleted(invoiceDocument)) {
            // check amounts not percent after full entry
            if (accountTotal.compareTo(itemTotal) != 0) {
                valid = false;
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_ACCOUNTING_AMOUNT_TOTAL, identifierString);
            }
        }
        return valid;
    }

    public CompositeValidation getReviewAccountingLineValidation() {
        return reviewAccountingLineValidation;
    }

    public void setReviewAccountingLineValidation(CompositeValidation reviewAccountingLineValidation) {
        this.reviewAccountingLineValidation = reviewAccountingLineValidation;
    }

    public PurapService getPurapService() {
        return purapService;
    }

    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    public PurApItem getItemForValidation() {
        return itemForValidation;
    }

    public void setItemForValidation(PurApItem itemForValidation) {
        this.itemForValidation = itemForValidation;
    }

    protected boolean reviewAccountingLineValidation(InvoiceDocument document, PurApAccountingLine accountingLine) {
        boolean valid = true;
        List<Validation> gauntlet = new ArrayList<Validation>();
        this.invoiceDocument = document;
        this.invoiceAccountingLine = accountingLine;

        createGauntlet(reviewAccountingLineValidation);

        for (Validation validation : gauntlet) {
            valid &= validation.validate(event);
        }

        return valid;
    }

    protected void createGauntlet(CompositeValidation validation) {
        for (Validation val : validation.getValidations()) {
            if (val instanceof CompositeValidation) {
                createGauntlet((CompositeValidation) val);
            } else if (val instanceof BusinessObjectDataDictionaryValidation) {
                addParametersToValidation((BusinessObjectDataDictionaryValidation) val);
            } else if (val instanceof AccountingLineAmountPositiveValidation) {
                //addParametersToValidation((AccountingLineAmountPositiveValidation) val);
            } else if (val instanceof AccountingLineDataDictionaryValidation) {
                addParametersToValidation((AccountingLineDataDictionaryValidation) val);
            } else if (val instanceof AccountingLineValuesAllowedValidationHutch) {
                addParametersToValidation((AccountingLineValuesAllowedValidationHutch) val);
            } else {
                throw new IllegalStateException("Validations in the InvoiceProcessItemValidation must contain specific instances of validation");
            }
        }
    }

    /**
     * checks if an accounting line with zero dollar amount can be approved.  This will check
     * the system parameter APPROVE_ACCOUNTING_LINES_WITH_ZERO_DOLLAR_AMOUNT_IND and determines if the
     * line can be approved or not.
     *
     * @return true if the system parameter value is Y else returns N.
     */
    public boolean canApproveAccountingLinesWithZeroAmount() {
        boolean canApproveLine = false;

        // get parameter to see if accounting line with zero dollar amount can be approved.
        String approveZeroAmountLine = SpringContext.getBean(ParameterService.class).getParameterValueAsString(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapParameterConstants.APPROVE_ACCOUNTING_LINES_WITH_ZERO_DOLLAR_AMOUNT_IND);

        if ("Y".equalsIgnoreCase(approveZeroAmountLine)) {
            return true;
        }

        return canApproveLine;
    }

    protected void addParametersToValidation(BusinessObjectDataDictionaryValidation validation) {
        validation.setBusinessObjectForValidation(this.invoiceAccountingLine);
    }

    protected void addParametersToValidation(AccountingLineAmountPositiveValidation validation) {
        validation.setAccountingDocumentForValidation(this.invoiceDocument);
        validation.setAccountingLineForValidation(this.invoiceAccountingLine);
    }

    protected void addParametersToValidation(AccountingLineDataDictionaryValidation validation) {
        validation.setAccountingLineForValidation(this.invoiceAccountingLine);
    }

    protected void addParametersToValidation(AccountingLineValuesAllowedValidationHutch validation) {
        validation.setAccountingDocumentForValidation(this.invoiceDocument);
        validation.setAccountingLineForValidation(this.invoiceAccountingLine);
    }

    /**
     * Gets the event attribute.
     *
     * @return Returns the event.
     */
    protected AttributedDocumentEvent getEvent() {
        return event;
    }

    /**
     * Sets the event attribute value.
     *
     * @param event The event to set.
     */
    protected void setEvent(AttributedDocumentEvent event) {
        this.event = event;
    }

    /**
     * Gets the invoiceDocument attribute.
     *
     * @return Returns the invoiceDocument.
     */
    protected InvoiceDocument getPreqDocument() {
        return invoiceDocument;
    }

    /**
     * Sets the invoiceDocument attribute value.
     *
     * @param invoiceDocument The invoiceDocument to set.
     */
    protected void setPreqDocument(InvoiceDocument invoiceDocument) {
        this.invoiceDocument = invoiceDocument;
    }

    /**
     * Gets the invoiceAccountingLine attribute.
     *
     * @return Returns the invoiceAccountingLine.
     */
    protected PurApAccountingLine getPreqAccountingLine() {
        return invoiceAccountingLine;
    }

    /**
     * Sets the invoiceAccountingLine attribute value.
     *
     * @param invoiceAccountingLine The invoiceAccountingLine to set.
     */
    protected void setPreqAccountingLine(PurApAccountingLine invoiceAccountingLine) {
        this.invoiceAccountingLine = invoiceAccountingLine;
    }

}
