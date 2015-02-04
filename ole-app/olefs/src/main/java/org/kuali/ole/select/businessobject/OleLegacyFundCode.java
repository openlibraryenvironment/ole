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
package org.kuali.ole.select.businessobject;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OleLegacyFundCode extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Integer legacyFundCodeIdentifier;
    private String accountNumber;
    private String chartOfAccountsCode;
    private String legacyFundCode;
    private String legacyFundCodeNote;

    private Account account;

    private boolean active;


    public Integer getLegacyFundCodeIdentifier() {
        return legacyFundCodeIdentifier;
    }

    public void setLegacyFundCodeIdentifier(Integer legacyFundCodeIdentifier) {
        this.legacyFundCodeIdentifier = legacyFundCodeIdentifier;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public String getLegacyFundCode() {
        return legacyFundCode;
    }

    public void setLegacyFundCode(String legacyFundCode) {
        this.legacyFundCode = legacyFundCode;
    }

    public String getLegacyFundCodeNote() {
        return legacyFundCodeNote;
    }

    public void setLegacyFundCodeNote(String legacyFundCodeNote) {
        this.legacyFundCodeNote = legacyFundCodeNote;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("legacyFundCodeIdentifier", legacyFundCodeIdentifier);
        return m;
    }

}
