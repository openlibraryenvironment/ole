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

import org.kuali.ole.select.businessobject.*;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;

public interface OleValidationRule extends BusinessRule {

    public boolean processCustomAddDiscountRequisitionBusinessRules(Document document, OleRequisitionItem reqItem);

    public boolean processCustomAddDiscountPurchaseOrderBusinessRules(Document document, OlePurchaseOrderItem purItem);

    public boolean processCustomAddDiscountPaymentRequestBusinessRules(Document document, OlePaymentRequestItem payItem);

    public boolean processCustomAddDiscountInvoiceBusinessRules(Document document, OleInvoiceItem payItem);

    public boolean processCustomForeignCurrencyRequisitionBusinessRules(Document document, OleRequisitionItem reqItem);

    public boolean processCustomForeignCurrencyPurchaseOrderBusinessRules(Document document, OlePurchaseOrderItem purItem);

    public boolean processCustomForeignCurrencyPaymentRequestBusinessRules(Document document, OlePaymentRequestItem payItem);

    public boolean processCustomForeignCurrencyInvoiceBusinessRules(Document document, OleInvoiceItem payItem);

    public boolean processCustomForeignCurrencyCreditMemoBusinessRules(Document document, OleCreditMemoItem creditMemoItem);

    public boolean processCustomPaymentRequestDescriptionBusinessRules(Document document, OlePaymentRequestItem payItem);

    public boolean processCustomInvoiceDescriptionBusinessRules(Document document, OleInvoiceItem payItem);

    public boolean processCustomPurchaseOrderDescriptionBusinessRules(Document document, OlePurchaseOrderItem purItem);

    public boolean processCustomCreditMemoDescriptionBusinessRules(Document document, OleCreditMemoItem creditMemoItem);

    public boolean processCustomAddCopiesRequisitionBusinessRules(Document document, OleRequisitionItem reqItem);

    public boolean processCustomAddCopiesPurchaseOrderBusinessRules(Document document, OlePurchaseOrderItem purItem);

    public boolean processInvoiceSubscriptionOverlayBusinessRules(Document document, OleInvoiceItem invoiceItem);


}