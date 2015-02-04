/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.service;

import org.kuali.ole.module.purap.businessobject.CreditMemoItem;
import org.kuali.ole.module.purap.businessobject.InvoiceItem;
import org.kuali.ole.module.purap.businessobject.PaymentRequestItem;

import java.util.List;

public interface PurapAccountRevisionService {
    /**
     * This method will identify the changes happened to existing payment request accounting lines and update the account change
     * history table. If new lines are added, then new account history lines are added too.
     *
     * @param paymentRequestItems Items from payment request document
     * @param postingYear         Posting year
     * @param postingPeriodCode   Posting period code
     */
    void savePaymentRequestAccountRevisions(List<PaymentRequestItem> paymentRequestItems, Integer postingYear, String postingPeriodCode);

    /**
     * This method will identify the changes happened to existing payment request accounting lines and update the account change
     * history table. If new lines are added, then new account history lines are added too.
     *
     * @param invoiceItems      Items from payment request document
     * @param postingYear       Posting year
     * @param postingPeriodCode Posting period code
     */
    void saveInvoiceAccountRevisions(List<InvoiceItem> invoiceItems, Integer postingYear, String postingPeriodCode);

    /**
     * This method will identify the changes happened to existing credit memo accounting lines and update the account change history
     * table. If new lines are added, then new account history lines are added too.
     *
     * @param paymentRequestItems Items from payment request document
     * @param postingYear         Posting year
     * @param postingPeriodCode   Posting period code
     */
    void saveCreditMemoAccountRevisions(List<CreditMemoItem> creditMemoItems, Integer postingYear, String postingPeriodCode);

    /**
     * This method will negate all existing payment request account line revisions
     *
     * @param paymentRequestItems Items from payment request document
     * @param postingYear         Posting year
     * @param postingPeriodCode   Posting period code
     */
    void cancelPaymentRequestAccountRevisions(List<PaymentRequestItem> paymentRequestItems, Integer postingYear, String postingPeriodCode);

    /**
     * This method will negate all existing payment request account line revisions
     *
     * @param invoiceItems      Items from payment request document
     * @param postingYear       Posting year
     * @param postingPeriodCode Posting period code
     */
    void cancelInvoiceAccountRevisions(List<InvoiceItem> invoiceItems, Integer postingYear, String postingPeriodCode);


    /**
     * This method will negate all existing credit memo account revision lines
     *
     * @param paymentRequestItems Items from payment request document
     * @param postingYear         Posting year
     * @param postingPeriodCode   Posting period code
     */
    void cancelCreditMemoAccountRevisions(List<CreditMemoItem> creditMemoItems, Integer postingYear, String postingPeriodCode);
}
