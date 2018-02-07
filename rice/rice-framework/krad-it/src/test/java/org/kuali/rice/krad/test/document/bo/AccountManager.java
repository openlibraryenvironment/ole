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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;
import java.util.List;


/**
 * FiscalOfficer
 */
@Entity
@Table(name="TRV_ACCT_FO")
public class AccountManager extends PersistableBusinessObjectBase {
	
	@Column(name="acct_fo_user_name")
	private String userName;
	@Id
	@GeneratedValue(generator="TRV_FO_ID_S")
	@GenericGenerator(name="TRV_FO_ID_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="TRV_FO_ID_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="acct_fo_id")
	private Long amId;
	@Transient
	private String defaultType;
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	@JoinColumn(name="acct_fo_id",insertable=false,updatable=false)
	private List<Account> accounts;

	public void setUserName(String userId) {
		userName = userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getDefaultType() {
        return this.defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public final boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof AccountManager)) return false;
        AccountManager am = (AccountManager) o;
        return StringUtils.equals(userName, am.getUserName()) &&
               ObjectUtils.equals(amId, am.getAmId());
	}

	/**
	 * Returns the hashcode of the docHeaderId, which is supposed to be the
	 * primary key for the document
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/*public int hashCode() {
		return (accountNum == null) ? 0 : accountNum.hashCode();
	}*/

	public Long getAmId() {
		return amId;
	}

	public void setAmId(Long id) {
		this.amId = id;
	}

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
