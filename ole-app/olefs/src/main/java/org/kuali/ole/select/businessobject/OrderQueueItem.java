/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.BusinessObjectBase;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

public class OrderQueueItem extends BusinessObjectBase {


    private Integer itemLineNumber;

    private String orderQueueDocNumber;

    private String statusCode;

    private boolean lineIndicator;

    private String requesitionDocumentNumber;

    private String objId;

    private Integer versionNumber;

    private Timestamp creationDate;

    private Timestamp modifiedDate;

    public Integer getItemLineNumber() {
        return itemLineNumber;
    }

    public void setItemLineNumber(Integer itemLineNumber) {
        this.itemLineNumber = itemLineNumber;
    }

    public String getOrderQueueDocNumber() {
        return orderQueueDocNumber;
    }

    public void setOrderQueueDocNumber(String orderQueueDocNumber) {
        this.orderQueueDocNumber = orderQueueDocNumber;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isLineIndicator() {
        return lineIndicator;
    }

    public void setLineIndicator(boolean lineIndicator) {
        this.lineIndicator = lineIndicator;
    }

    public String getRequesitionDocumentNumber() {
        return requesitionDocumentNumber;
    }

    public void setRequesitionDocumentNumber(String requesitionDocumentNumber) {
        this.requesitionDocumentNumber = requesitionDocumentNumber;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void refresh() {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringMapper()
     */
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        return null;
    }

}
