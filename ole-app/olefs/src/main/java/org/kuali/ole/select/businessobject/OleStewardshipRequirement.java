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
import org.kuali.ole.coa.businessobject.OleStewardship;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;


public class OleStewardshipRequirement extends PersistableBusinessObjectBase implements Inactivatable {


    private Integer stewardshipId;
    private Integer stewardshipTypeCode;
    private String stewardshipNote;
    private String chartOfAccountsCode;
    private String accountNumber;

    private OleStewardship oleStewardship;

    private Account account;

    private boolean active;

    public Integer getStewardshipId() {
        return stewardshipId;
    }

    public void setStewardshipId(Integer stewardshipId) {
        this.stewardshipId = stewardshipId;
    }


    public Integer getStewardshipTypeCode() {
        return stewardshipTypeCode;
    }

    public void setStewardshipTypeCode(Integer stewardshipTypeCode) {
        this.stewardshipTypeCode = stewardshipTypeCode;
    }

    public String getStewardshipNote() {
        return stewardshipNote;
    }

    public void setStewardshipNote(String stewardshipNote) {
        this.stewardshipNote = stewardshipNote;
    }

    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public OleStewardship getOleStewardship() {
        return oleStewardship;
    }

    public void setOleStewardship(OleStewardship oleStewardship) {
        this.oleStewardship = oleStewardship;
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
        m.put("stewardshipId", this.stewardshipId.toString());
        return m;
    }

}
