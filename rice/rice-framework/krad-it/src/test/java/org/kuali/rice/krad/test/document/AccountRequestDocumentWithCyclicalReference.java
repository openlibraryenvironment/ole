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
package org.kuali.rice.krad.test.document;

import org.kuali.rice.krad.document.SessionDocument;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.test.document.bo.Account;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
* This is a test copy of AccountRequestDocument that has been modified to allow for custom lock descriptors.
* 
*/

@Entity
@Table(name="TRV_DOC_2")
public class AccountRequestDocumentWithCyclicalReference extends TransactionalDocumentBase implements SessionDocument {

	@Column(name="traveler")
    private String requester;
	@Column(name= "org")
    private String reason1;
	@Column(name="dest")
    private String reason2;
	@Column(name="request_trav")
    private String requestType;
	@Transient
    private String accountTypeCode;

	@OneToOne(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	@JoinColumn(name="fdoc_nbr",insertable=false,updatable=false)
    private AccountRequestDocumentWithCyclicalReference child;
	@OneToOne(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	@JoinColumn(name="fdoc_nbr",insertable=false,updatable=false)
    private AccountRequestDocumentWithCyclicalReference parent;
    
    @ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST})
	@JoinTable(
			name="TRAV_DOC_2_ACCOUNTS",
			joinColumns=@JoinColumn(name="DOC_HDR_ID", referencedColumnName="DOC_HDR_ID",insertable=false,updatable=false),
			inverseJoinColumns=@JoinColumn(name="ACCT_NUM", referencedColumnName="ACCT_NUM",insertable=false,updatable=false)
				)
    private List<Account> accounts;

    public AccountRequestDocumentWithCyclicalReference() {
        accounts = new ArrayList<Account>();
    }

    public String getReason2() {
        return reason2;
    }

    public void setReason2(String reason2) {
        this.reason2 = reason2;
    }

    public String getReason1() {
        return reason1;
    }

    public void setReason1(String reason1) {
        this.reason1 = reason1;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getAccount(int index) {
        while(accounts.size() - 1 < index) {
            accounts.add(new Account());
        }
        return accounts.get(index);
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setAccountTypeCode(String accountType) {
        this.accountTypeCode = accountType;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

	public AccountRequestDocumentWithCyclicalReference getChild() {
		return this.child;
	}

	public void setChild(AccountRequestDocumentWithCyclicalReference child) {
		this.child = child;
	}

	public AccountRequestDocumentWithCyclicalReference getParent() {
		return this.parent;
	}

	public void setParent(AccountRequestDocumentWithCyclicalReference parent) {
		this.parent = parent;
	}

}
