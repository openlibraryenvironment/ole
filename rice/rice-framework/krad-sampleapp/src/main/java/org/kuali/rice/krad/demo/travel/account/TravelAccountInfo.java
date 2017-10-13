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
import org.kuali.rice.krad.demo.travel.fiscalofficer.FiscalOfficerInfo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TravelAccountInfo implements Serializable {
    
    private String number;
    private String subAccount;
    private String name;
    private String subAccountName;
    private KualiPercent subsidizedPercent;
    private Date createDate;
    private Long foId;
    private String objectId;
    private Long versionNumber;

    // this is just for testing relationshipDefinitions until a nested object
    // is no longer required
    private FiscalOfficerInfo fiscalOfficerInfo;

     public FiscalOfficerInfo getFiscalOfficerInfo() {
        return fiscalOfficerInfo;
    }

    public void setFiscalOfficerInfo(FiscalOfficerInfo fiscalOfficerInfo) {
        this.fiscalOfficerInfo = fiscalOfficerInfo;
    }
    
    
    public String getNumber() {
        return this.number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public String getSubAccount() {
        return this.subAccount;
    }
    
    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public Long getFoId() {
        return this.foId;
    }
    
    public void setFoId(Long foId) {
        this.foId = foId;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getVersionNumber() {
        return this.versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }
    
}
