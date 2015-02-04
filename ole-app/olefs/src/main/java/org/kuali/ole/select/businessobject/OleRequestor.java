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

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OleRequestor extends PersistableBusinessObjectBase implements MutableInactivatable {

    private String requestorId;
    private String requestorTypeId;
    private String requestorFirstName;
    private String requestorLastName;
    private String requestorAddress1;
    private String requestorAddress2;
    private String requestorCityName;
    private String requestorStateCode;
    private String requestorPostalCode;
    private String requestorCountryCode;
    private String requestorPhoneNumber;
    private String requestorEmail;
    private String requestorSms;
    private boolean active;
    private String requestorName;
    private String refKrimId;
    private String requestorProvince;

    public String getRefKrimId() {
        return refKrimId;
    }

    public void setRefKrimId(String refKrimId) {
        this.refKrimId = refKrimId;
    }

    public String getRequestorId() {
        return requestorId;
    }


    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public String getRequestorTypeId() {
        return requestorTypeId;
    }


    public void setRequestorTypeId(String requestorTypeId) {
        this.requestorTypeId = requestorTypeId;
    }

    public String getRequestorFirstName() {
        return requestorFirstName;
    }


    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }


    public String getRequestorLastName() {
        return requestorLastName;
    }


    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
    }


    public String getRequestorAddress1() {
        return requestorAddress1;
    }


    public void setRequestorAddress1(String requestorAddress1) {
        this.requestorAddress1 = requestorAddress1;
    }


    public String getRequestorAddress2() {
        return requestorAddress2;
    }


    public void setRequestorAddress2(String requestorAddress2) {
        this.requestorAddress2 = requestorAddress2;
    }


    public String getRequestorCityName() {
        return requestorCityName;
    }


    public void setRequestorCityName(String requestorCityName) {
        this.requestorCityName = requestorCityName;
    }


    public String getRequestorStateCode() {
        return requestorStateCode;
    }


    public void setRequestorStateCode(String requestorStateCode) {
        this.requestorStateCode = requestorStateCode;
    }


    public String getRequestorPostalCode() {
        return requestorPostalCode;
    }


    public void setRequestorPostalCode(String requestorPostalCode) {
        this.requestorPostalCode = requestorPostalCode;
    }


    public String getRequestorCountryCode() {
        return requestorCountryCode;
    }


    public void setRequestorCountryCode(String requestorCountryCode) {
        this.requestorCountryCode = requestorCountryCode;
    }


    public String getRequestorPhoneNumber() {
        return requestorPhoneNumber;
    }


    public void setRequestorPhoneNumber(String requestorPhoneNumber) {
        this.requestorPhoneNumber = requestorPhoneNumber;
    }


    public String getRequestorEmail() {
        return requestorEmail;
    }


    public void setRequestorEmail(String requestorEmail) {
        this.requestorEmail = requestorEmail;
    }


    public String getRequestorSms() {
        return requestorSms;
    }


    public void setRequestorSms(String requestorSms) {
        this.requestorSms = requestorSms;
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(requestorLastName).append(", ");
        buffer.append(requestorFirstName);
        return buffer.toString();
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */
    @SuppressWarnings("rawtypes")
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("requestorFirstName", requestorFirstName);
        toStringMap.put("requestorLastName", requestorLastName);
        toStringMap.put("requestorAddress1", requestorAddress1);
        toStringMap.put("requestorAddress2", requestorAddress2);
        toStringMap.put("requestorCityName", requestorCityName);
        toStringMap.put("requestorStateCode", requestorStateCode);
        toStringMap.put("requestorPostalCode", requestorPostalCode);
        toStringMap.put("requestorCountryCode", requestorCountryCode);
        toStringMap.put("requestorPhoneNumber", requestorPhoneNumber);
        toStringMap.put("requestorEmail", requestorEmail);
        toStringMap.put("requestorSms", requestorSms);
        toStringMap.put("requestorProvince", requestorProvince);
        return toStringMap;
    }

    public String getRequestorName() {
        if (this.requestorFirstName != null && this.requestorLastName != null)
            this.requestorName = this.requestorLastName.concat(", ").concat(this.requestorFirstName);
        else if (this.requestorLastName == null && this.requestorFirstName != null)
            this.requestorName = this.requestorFirstName;
        else if (this.requestorFirstName == null && this.requestorLastName != null)
            this.requestorName = this.requestorLastName;
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getRequestorProvince() {
        return requestorProvince;
    }

    public void setRequestorProvince(String requestorProvince) {
        this.requestorProvince = requestorProvince;
    }


}
