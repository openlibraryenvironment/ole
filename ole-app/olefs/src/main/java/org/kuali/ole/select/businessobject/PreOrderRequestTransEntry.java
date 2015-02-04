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

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;
import java.util.LinkedHashMap;

public class PreOrderRequestTransEntry extends PersistableBusinessObjectBase {
    private Long preRequestOrderId;
    private Long titleId;
    private Boolean routeToRequestor;
    private Long requestorContactInfo;
    private String noteFromPatron;
    private String typeOfContactInformation;
    private Long numberOfCopies;
    private String requestSource;
    private Date dateTimeStamp;


    public Long getPreRequestOrderId() {
        return preRequestOrderId;
    }


    public void setPreRequestOrderId(Long preRequestOrderId) {
        this.preRequestOrderId = preRequestOrderId;
    }


    public Long getTitleId() {
        return titleId;
    }


    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }


    public Boolean getRouteToRequestor() {
        return routeToRequestor;
    }


    public void setRouteToRequestor(Boolean routeToRequestor) {
        this.routeToRequestor = routeToRequestor;
    }


    public Long getRequestorContactInfo() {
        return requestorContactInfo;
    }


    public void setRequestorContactInfo(Long requestorContactInfo) {
        this.requestorContactInfo = requestorContactInfo;
    }


    public String getNoteFromPatron() {
        return noteFromPatron;
    }


    public void setNoteFromPatron(String noteFromPatron) {
        this.noteFromPatron = noteFromPatron;
    }


    public String getTypeOfContactInformation() {
        return typeOfContactInformation;
    }


    public void setTypeOfContactInformation(String typeOfContactInformation) {
        this.typeOfContactInformation = typeOfContactInformation;
    }


    public Long getNumberOfCopies() {
        return numberOfCopies;
    }


    public void setNumberOfCopies(Long numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }


    public String getRequestSource() {
        return requestSource;
    }


    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }


    public Date getDateTimeStamp() {
        return dateTimeStamp;
    }


    public void setDateTimeStamp(Date dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }


    @SuppressWarnings("rawtypes")

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("routeToRequestor", routeToRequestor);
        map.put("noteFromPatron", noteFromPatron);
        map.put("requestorContactInfo", requestorContactInfo);
        map.put("typeOfContactInformation", typeOfContactInformation);
        map.put("numberOfCopies", numberOfCopies);
        map.put("requestSource", requestorContactInfo);
        map.put("dateTimeStamp", dateTimeStamp);
        return map;
    }

}
