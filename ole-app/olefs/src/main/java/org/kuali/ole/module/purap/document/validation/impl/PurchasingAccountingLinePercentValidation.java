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
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

public class PurchasingAccountingLinePercentValidation extends GenericValidation {

    private AccountingLine updatedAccountingLine;

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        //   PurApAccountingLine purapAccountingLine = (PurApAccountingLine)updatedAccountingLine;

        // make sure it's a whole number
        //  if (purapAccountingLine.getAccountLinePercent().stripTrailingZeros().scale() > 0) {
        //      GlobalVariables.getMessageMap().putError(PurapPropertyConstants.ACCOUNTS, PurapKeyConstants.ERROR_PURCHASING_PERCENT_NOT_WHOLE, purapAccountingLine.getAccountLinePercent().toPlainString());
//
        //     valid &= false;
        //  }

        return valid;
    }

    public AccountingLine getUpdatedAccountingLine() {
        return updatedAccountingLine;
    }

    public void setUpdatedAccountingLine(AccountingLine updatedAccountingLine) {
        this.updatedAccountingLine = updatedAccountingLine;
    }
    // added for jira OLE-2112.

    /**
     * Method to validate accountingLinePercent field
     *
     * @return boolean.
     */
    public boolean validatePercent() {
        boolean valid = true;
        PurApAccountingLine purapAccountingLine = (PurApAccountingLine) updatedAccountingLine;
        // accounting line percent should not be greater than 100.
        if (purapAccountingLine.getAccountLinePercent().intValue() > OleSelectConstant.ACCOUNTINGLINE_PERCENT_HUNDRED) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_ACCOUNTINGLINE_PERCENT_GT_HUNDRED);
            valid = false;
        }
        return valid;

    }

}
