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

import org.kuali.ole.fp.businessobject.DisbursementVoucherPayeeDetail;

public class OleDisbursementVoucherPayeeDetail extends DisbursementVoucherPayeeDetail {

    private Boolean disbVchrDepositAccount;
    private String vendorAliasName;

    /**
     * Default no-arg constructor.
     */
    public OleDisbursementVoucherPayeeDetail() {
        super();
    }

    /**
     * Gets the disbVchrDepositAccount attribute.
     *
     * @return Returns the disbVchrDepositAccount
     */
    public boolean isDisbVchrDepositAccount() {
        return disbVchrDepositAccount;
    }

    /**
     * Sets the disbVchrDepositAccount attribute.
     *
     * @param disbVchrDepositAccount The disbVchrDepositAccount to set.
     */
    public void setDisbVchrDepositAccount(boolean disbVchrDepositAccount) {
        this.disbVchrDepositAccount = disbVchrDepositAccount;
    }


    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }


}
