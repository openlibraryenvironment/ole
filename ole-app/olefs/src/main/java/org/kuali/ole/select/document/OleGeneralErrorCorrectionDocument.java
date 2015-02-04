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

import org.kuali.ole.fp.businessobject.GECSourceAccountingLine;
import org.kuali.ole.fp.businessobject.GECTargetAccountingLine;
import org.kuali.ole.fp.document.GeneralErrorCorrectionDocument;
import org.kuali.ole.sys.OLEConstants;

import java.util.List;

public class OleGeneralErrorCorrectionDocument extends GeneralErrorCorrectionDocument {

    /**
     * Provides answers to the following splits: HasVendorDepositAccount
     *
     * @see org.kuali.ole.sys.document.FinancialSystemTransactionalDocumentBase#answerSplitNodeQuestion(java.lang.String)
     */
    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(OLEConstants.HAS_VENDOR_DEPOSIT_ACCOUNT))
            return hasVendorDepositAccount();
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
    }

    private boolean hasVendorDepositAccount() {
        List<GECSourceAccountingLine> sourceAccounts = this.getSourceAccountingLines();
        List<GECTargetAccountingLine> targetAccounts = this.getTargetAccountingLines();
        for (GECSourceAccountingLine sourceAccount : sourceAccounts) {
            if (sourceAccount.getAccount().getSubFundGroupCode().equalsIgnoreCase(OLEConstants.CLEARING_ACCOUNT_CODE)) {
                return true;
            }
        }
        for (GECTargetAccountingLine targetAccount : targetAccounts) {
            if (targetAccount.getAccount().getSubFundGroupCode().equalsIgnoreCase(OLEConstants.CLEARING_ACCOUNT_CODE)) {
                return true;
            }
        }
        return false;
    }

}
