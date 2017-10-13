/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.demo.travel.account;

import org.kuali.rice.krad.bo.PersistableBusinessObjectExtensionBase;

import javax.persistence.*;

@Entity
@Table(name="TRV_ACCT_EXT")
public class TravelAccountExtension extends PersistableBusinessObjectExtensionBase {
    private static final long serialVersionUID = -520222959801026298L;

	@Id
	@Column(name="acct_num")
	private String number;
    
    @Column(name="acct_type")
	private String accountTypeCode;
    
    @OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="acct_type", insertable=false, updatable=false)
	private TravelAccountType accountType; 
    
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

	public TravelAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(TravelAccountType accountType) {
		this.accountType = accountType;
	}

 
}
