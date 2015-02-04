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

import org.kuali.ole.fp.businessobject.Check;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * Validation that checks that a check's amount is not zero.
 */
public class CashReceiptCheckAmountNotZeroValidation extends GenericValidation {
    private Check checkForValidation;

    /**
     * Verifies that a check amount is not zero.
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        if (getCheckForValidation().getAmount().isZero()) {
            GlobalVariables.getMessageMap().putError(OLEPropertyConstants.CHECK_AMOUNT, OLEKeyConstants.CashReceipt.ERROR_ZERO_CHECK_AMOUNT, OLEPropertyConstants.CHECKS);
            return false;
        }
        return true;
    }

    /**
     * Gets the checkForValidation attribute. 
     * @return Returns the checkForValidation.
     */
    public Check getCheckForValidation() {
        return checkForValidation;
    }

    /**
     * Sets the checkForValidation attribute value.
     * @param checkForValidation The checkForValidation to set.
     */
    public void setCheckForValidation(Check checkForValidation) {
        this.checkForValidation = checkForValidation;
    }
}
