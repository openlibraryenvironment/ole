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
package org.kuali.ole.select.document.validation.impl;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleDisbursementVoucherAccountingLine;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.util.List;

public class OleDVAccountingLinesPercentValidation extends GenericValidation {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDVAccountingLinesPercentValidation.class);

    private AccountingDocument accountingDocumentForValidation;

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        //super.validate(event);
        if (valid) {
            // validate that the percents total 100 for each account
            BigDecimal sourceTotalPercent = BigDecimal.ZERO;
            BigDecimal desiredPercent = new BigDecimal("100");
            List sourceAccounts = getAccountingDocumentForValidation().getSourceAccountingLines();
            for (Object account : sourceAccounts) {
                if (((OleDisbursementVoucherAccountingLine) account).getAccountLinePercent() != null) {
                    sourceTotalPercent = sourceTotalPercent.add(((OleDisbursementVoucherAccountingLine) account).getAccountLinePercent());
                } else {
                    sourceTotalPercent = sourceTotalPercent.add(BigDecimal.ZERO);
                }
            }
            if ((sourceAccounts.size() > 0 && desiredPercent.compareTo(sourceTotalPercent) != 0)) {
                GlobalVariables.getMessageMap().putError(OLEConstants.ACCOUNTING_LINE_ERRORS, OleSelectConstant.ERROR_DI_ACCOUNTING_TOTAL, "");
                valid = false;
            }
        }
        return valid;
    }


    /**
     * Gets the accountingDocumentForValdation attribute.
     *
     * @return Returns the accountingDocumentForValdation.
     */
    public AccountingDocument getAccountingDocumentForValidation() {
        return accountingDocumentForValidation;
    }

    /**
     * Sets the accountingDocumentForValdation attribute value.
     *
     * @param accountingDocumentForValdation The accountingDocumentForValdation to set.
     */
    public void setAccountingDocumentForValidation(AccountingDocument accountingDocumentForValidation) {
        this.accountingDocumentForValidation = accountingDocumentForValidation;
    }
}

