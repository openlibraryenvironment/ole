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

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.coa.businessobject.IndirectCostRecoveryAccount;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * This class...
 */
public class IndirectCostAdjustmentAccountValidation extends GenericValidation {
    protected AccountingLine accountingLineForValidation;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(IndirectCostAdjustmentAccountValidation.class);
    
    /**
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        AccountingLine accountingLine = getAccountingLineForValidation();
        boolean isValid = true;
        if (accountingLine.isSourceAccountingLine()) {
            accountingLine.refreshReferenceObject(OLEPropertyConstants.ACCOUNT);
            if (ObjectUtils.isNotNull(accountingLine.getAccount())) {
                for (IndirectCostRecoveryAccount icrAccount : accountingLine.getAccount().getActiveIndirectCostRecoveryAccounts()){
                    isValid &= StringUtils.isNotBlank(icrAccount.getIndirectCostRecoveryAccountNumber());
                }
                //not valid if ICR collection is empty or any of the account number is blank
                if (!isValid) {
                    GlobalVariables.getMessageMap().putError(OLEPropertyConstants.ACCOUNT_NUMBER, OLEKeyConstants.IndirectCostAdjustment.ERROR_DOCUMENT_ICA_GRANT_INVALID_ACCOUNT, accountingLine.getAccountNumber());
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("rule failure: " + OLEKeyConstants.IndirectCostAdjustment.ERROR_DOCUMENT_ICA_GRANT_INVALID_ACCOUNT + " / " + accountingLine.getAccountNumber() );
                    }
                }
            }
        }
        return isValid;
    }

    /**
     * Gets the accountingLineForValidation attribute. 
     * @return Returns the accountingLineForValidation.
     */
    public AccountingLine getAccountingLineForValidation() {
        return accountingLineForValidation;
    }

    /**
     * Sets the accountingLineForValidation attribute value.
     * @param accountingLineForValidation The accountingLineForValidation to set.
     */
    public void setAccountingLineForValidation(AccountingLine accountingLineForValidation) {
        this.accountingLineForValidation = accountingLineForValidation;
    }
    
}
