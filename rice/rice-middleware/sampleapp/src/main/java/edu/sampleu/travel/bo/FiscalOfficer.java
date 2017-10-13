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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.kuali.rice.core.framework.persistence.jpa.annotations.Sequence;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * FiscalOfficer
 */
@Entity
@Table(name="TRV_ACCT_FO")
@Sequence(name="seq_acct_fo_id", property="id")
public class FiscalOfficer extends PersistableBusinessObjectBase {
	private static final long serialVersionUID = -4645124696676896963L;

	@Id
	@Column(name="acct_fo_id")
	private Long id;
	
	@Column(name="acct_fo_user_name")
	private String userName;
	
	@Column(name="acct_fo_user_name")
	private String firstName;

	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER, mappedBy="fiscalOfficer")
	private List<TravelAccount> accounts = new ArrayList<TravelAccount>();
	
	public FiscalOfficer() {
		accounts = new ArrayList<TravelAccount>();
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setUserName(String userId) {
		userName = userId;
	}

	public String getUserName() {
		return userName;
	}

	public final boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof FiscalOfficer)) return false;
        FiscalOfficer f = (FiscalOfficer) o;
        return StringUtils.equals(userName, f.getUserName()) &&
               ObjectUtils.equals(id, f.getId());
	}

	public int hashCode() {
    	return new HashCodeBuilder().append(id).append(userName).toHashCode();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public List<TravelAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<TravelAccount> accounts) {
        this.accounts = accounts;
    }
}
