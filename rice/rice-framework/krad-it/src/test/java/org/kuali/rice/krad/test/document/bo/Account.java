/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.test.document.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

@Entity
@Table(name="TRV_ACCT")
public class Account extends PersistableBusinessObjectBase {
    @Id
    @Column(name="acct_num")
	private String number;
	@Column(name="acct_name")
    private String name;
	@Column(name="acct_fo_id")
    private Long amId;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.REFRESH})
	@JoinColumn(name="acct_fo_id",insertable=false,updatable=false)
    private AccountManager accountManager;    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getAmId() {
        return this.amId;
    }

    public void setAmId(Long id) {
        this.amId = id;
    }

    public AccountManager getAccountManager() {
        return this.accountManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
}
