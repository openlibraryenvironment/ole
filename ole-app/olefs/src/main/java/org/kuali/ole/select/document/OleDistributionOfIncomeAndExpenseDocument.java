/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.select.document;

import org.kuali.ole.fp.document.DistributionOfIncomeAndExpenseDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.TargetAccountingLine;

import java.util.List;

public class OleDistributionOfIncomeAndExpenseDocument extends DistributionOfIncomeAndExpenseDocument {

    /**
     * Provides answers to the following splits: HasVendorDepositAccount
     *
     * @see org.kuali.ole.sys.document.FinancialSystemTransactionalDocumentBase#answerSplitNodeQuestion(java.lang.String)
     */
    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(OLEConstants.HAS_VENDOR_DEPOSIT_ACCOUNT)) {
            return hasVendorDepositAccount();
        }
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
    }

    private boolean hasVendorDepositAccount() {
        List<SourceAccountingLine> sourceAccounts = this.getSourceAccountingLines();
        List<TargetAccountingLine> targetAccounts = this.getTargetAccountingLines();
        for (SourceAccountingLine sourceAccount : sourceAccounts) {
            if (sourceAccount.getAccount().getSubFundGroupCode().equalsIgnoreCase(OLEConstants.CLEARING_ACCOUNT_CODE)) {
                return true;
            }
        }
        for (TargetAccountingLine targetAccount : targetAccounts) {
            if (targetAccount.getAccount().getSubFundGroupCode().equalsIgnoreCase(OLEConstants.CLEARING_ACCOUNT_CODE)) {
                return true;
            }
        }
        return false;
    }
}
