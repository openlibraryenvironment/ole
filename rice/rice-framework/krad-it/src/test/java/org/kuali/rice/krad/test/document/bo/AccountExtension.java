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

import org.kuali.rice.krad.bo.PersistableBusinessObjectExtensionBase;

import javax.persistence.*;

@Entity
@Table(name="TRV_ACCT_EXT")
public class AccountExtension extends PersistableBusinessObjectExtensionBase {
    
	@Id
	@Column(name="acct_num")
    private String number;
	@Column(name="acct_type")
    private String accountTypeCode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="acct_type",insertable=false,updatable=false)
    private AccountType accountType; 
    
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

 
}
