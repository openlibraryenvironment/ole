/*
 * Copyright 2005 The Kuali Foundation
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

package org.kuali.ole.coa.businessobject;

import org.apache.log4j.Logger;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;


/**
 *
 */
public class OleFundCodeAccountingLine extends PersistableBusinessObjectBase {

    private static final Logger LOG = Logger.getLogger(OleFundCodeAccountingLine.class);

    private String fundCodeAccountingLineId;

    private String fundCodeId;

    private String chartCode;

    private String accountNumber;

    private String subAccount;

    private String objectCode;

    private String subObject;

    private String project;

    private String orgRefId;

    private BigDecimal percentage;

    public static Logger getLog() {
        return LOG;
    }

    public String getFundCodeAccountingLineId() {
        return fundCodeAccountingLineId;
    }

    public void setFundCodeAccountingLineId(String fundCodeAccountingLineId) {
        this.fundCodeAccountingLineId = fundCodeAccountingLineId;
    }

    public String getFundCodeId() {
        return fundCodeId;
    }

    public void setFundCodeId(String fundCodeId) {
        this.fundCodeId = fundCodeId;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSubAccount() {
        return subAccount;
    }

    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getSubObject() {
        return subObject;
    }

    public void setSubObject(String subObject) {
        this.subObject = subObject;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getOrgRefId() {
        return orgRefId;
    }

    public void setOrgRefId(String orgRefId) {
        this.orgRefId = orgRefId;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
