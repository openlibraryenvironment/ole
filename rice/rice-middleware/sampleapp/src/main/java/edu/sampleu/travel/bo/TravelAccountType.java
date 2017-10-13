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
package edu.sampleu.travel.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TRV_ACCT_TYPE")
public class TravelAccountType extends PersistableBusinessObjectBase {
    private static final long serialVersionUID = 413236253897119667L;
    
	@Id
	@Column(name="acct_type")
	private String accountTypeCode;
    @Column(name="acct_type_name")
	private String name;
    

    public String getAccountTypeCode() {
		return accountTypeCode;
	}


	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getCodeAndDescription() {
        if (accountTypeCode != null) {
            return accountTypeCode + " - " + name;
        }

        return "";
	}
}
