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

import org.kuali.rice.core.api.util.type.KualiPercent;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.demo.travel.fiscalofficer.FiscalOfficer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="TRV_ACCT")
public class TravelAccount extends PersistableBusinessObjectBase {
    
	private static final long serialVersionUID = -7739303391609093875L;
	
	@Id
	@Column(name="acct_num")
	private String number;
	
	private String subAccount;
    
	@Column(name="acct_name")
	private String name;
	
	private String subAccountName;
	
	private KualiPercent subsidizedPercent;
	
	private Date createDate;
    
	public Date getCreateDate() {
        return this.createDate;
    }

    @Column(name="acct_fo_id")
	private Long foId;
    
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="acct_fo_id", insertable=false, updatable=false)
	private FiscalOfficer fiscalOfficer;  
    
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

    public FiscalOfficer getFiscalOfficer() {
        return fiscalOfficer;
    }

    public void setFiscalOfficer(FiscalOfficer fiscalOfficer) {
        this.fiscalOfficer = fiscalOfficer;
    }

    public Long getFoId() {
        return foId;
    }

    public void setFoId(Long foId) {
        this.foId = foId;
    }

    public String getSubAccount() {
        return this.subAccount;
    }

    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    public String getSubAccountName() {
        return this.subAccountName;
    }

    public void setSubAccountName(String subAccountName) {
        this.subAccountName = subAccountName;
    }

    public KualiPercent getSubsidizedPercent() {
        return this.subsidizedPercent;
    }

    public void setSubsidizedPercent(KualiPercent subsidizedPercent) {
        this.subsidizedPercent = subsidizedPercent;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
}
