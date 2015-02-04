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
package org.kuali.ole.fp.document.validation.impl;

import static org.kuali.ole.sys.OLEConstants.SOURCE_ACCOUNTING_LINE_ERRORS;
import static org.kuali.ole.sys.OLEConstants.TARGET_ACCOUNTING_LINE_ERRORS;
import static org.kuali.ole.sys.OLEKeyConstants.ERROR_DOCUMENT_ACCOUNTING_LINE_TOTAL_CHANGED;

import org.kuali.ole.fp.document.BudgetAdjustmentDocument;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.ole.sys.document.validation.impl.AccountingLineGroupTotalsUnchangedValidation;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * The Budget Adjustment's variation on whether accounting lines have been unchanged or not
 */
public class BudgetAdjustmentAccountingLineTotalsUnchangedValidation extends AccountingLineGroupTotalsUnchangedValidation {

    /**
     * Returns true if account line totals remains unchanged from what was entered and what was persisted before; takes into account all adjustment totals
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        boolean isUnchanged = true;

        BudgetAdjustmentDocument persistedDocument = (BudgetAdjustmentDocument) retrievePersistedDocument(getAccountingDocumentForValidation());
        BudgetAdjustmentDocument currentDocument = (BudgetAdjustmentDocument)getAccountingDocumentForValidation();

        if (persistedDocument == null) {
            handleNonExistentDocumentWhenApproving(getAccountingDocumentForValidation());
        }
        else {
            // retrieve the persisted totals
            KualiDecimal persistedSourceCurrentBudgetTotal = persistedDocument.getSourceCurrentBudgetTotal();
            KualiDecimal persistedSourceBaseBudgetTotal = persistedDocument.getSourceBaseBudgetTotal();
            KualiDecimal persistedTargetCurrentBudgetTotal = persistedDocument.getTargetCurrentBudgetTotal();
            KualiDecimal persistedTargetBaseBudgetTotal = persistedDocument.getTargetBaseBudgetTotal();

            // retrieve the updated totals
            KualiDecimal currentSourceCurrentBudgetTotal = currentDocument.getSourceCurrentBudgetTotal();
            KualiDecimal currentSourceBaseBudgetTotal = currentDocument.getSourceBaseBudgetTotal();
            KualiDecimal currentTargetCurrentBudgetTotal = currentDocument.getTargetCurrentBudgetTotal();
            KualiDecimal currentTargetBaseBudgetTotal = currentDocument.getTargetBaseBudgetTotal();

            // make sure that totals have remained unchanged, if not, recognize that, and
            // generate appropriate error messages
            if (persistedSourceCurrentBudgetTotal.compareTo(currentSourceCurrentBudgetTotal) != 0) {
                isUnchanged = false;
                buildTotalChangeErrorMessage(SOURCE_ACCOUNTING_LINE_ERRORS, "source current budget", persistedSourceCurrentBudgetTotal, currentSourceCurrentBudgetTotal);
            }
            if (persistedSourceBaseBudgetTotal.compareTo(currentSourceBaseBudgetTotal) != 0) {
                isUnchanged = false;
                buildTotalChangeErrorMessage(SOURCE_ACCOUNTING_LINE_ERRORS, "source base budget", persistedSourceBaseBudgetTotal, currentSourceBaseBudgetTotal);
            }
            if (persistedTargetCurrentBudgetTotal.compareTo(currentTargetCurrentBudgetTotal) != 0) {
                isUnchanged = false;
                buildTotalChangeErrorMessage(TARGET_ACCOUNTING_LINE_ERRORS, "target current budget", persistedTargetCurrentBudgetTotal, currentTargetCurrentBudgetTotal);
            }
            if (persistedTargetBaseBudgetTotal.compareTo(currentTargetBaseBudgetTotal) != 0) {
                isUnchanged = false;
                buildTotalChangeErrorMessage(TARGET_ACCOUNTING_LINE_ERRORS, "target base budget", persistedTargetBaseBudgetTotal, currentTargetBaseBudgetTotal);
            }
        }

        return isUnchanged;
    }
    
    /**
     * Builds the error message for when totals have changed.
     * 
     * @param propertyName name of property
     * @param sectionTitle title of section
     * @param persistedSourceLineTotal previously persisted source line total
     * @param currentSourceLineTotal current entered source line total
     */
    protected void buildTotalChangeErrorMessage(String propertyName, String sectionTitle, KualiDecimal persistedSourceLineTotal, KualiDecimal currentSourceLineTotal) {
        String persistedTotal = (String) new CurrencyFormatter().format(persistedSourceLineTotal);
        String currentTotal = (String) new CurrencyFormatter().format(currentSourceLineTotal);

        GlobalVariables.getMessageMap().putError(propertyName, ERROR_DOCUMENT_ACCOUNTING_LINE_TOTAL_CHANGED, new String[] { sectionTitle, persistedTotal, currentTotal });
    }
}
