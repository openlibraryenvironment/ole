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
package org.kuali.ole.select.businessobject;

import org.kuali.ole.sys.businessobject.SourceAccountingLine;

import java.math.BigDecimal;

public class OleDisbursementVoucherAccountingLine extends SourceAccountingLine {

    /**
     * Constructs a OleDisbursementVoucherAccountingLine.java.
     */
    public OleDisbursementVoucherAccountingLine() {
        super();
        accountLinePercent = new BigDecimal(100);
    }

    private BigDecimal accountLinePercent;

    /**
     * Gets the value of the accountLinePercent property
     *
     * @return accountLinePercent
     */
    public BigDecimal getAccountLinePercent() {
        if (accountLinePercent != null) {
            accountLinePercent = accountLinePercent.setScale(2, BigDecimal.ROUND_HALF_UP);
            return accountLinePercent;
        } else {
            return new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * Sets the accountLinePercent value
     *
     * @param accountLinePercent
     */
    public void setAccountLinePercent(BigDecimal accountLinePercent) {
        this.accountLinePercent = accountLinePercent;
    }
}
    


