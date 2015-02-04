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

import java.util.LinkedHashMap;


public class OlePersonRequestor extends PersistableBusinessObjectBase {


    private String id;
    private String firstName;
    private String lastName;
    private String name;
    private String internalRequestorId;
    private String externalRequestorId;
    private String email;
    private String phoneNumber;
    private String requestorTypeId;
    private String refKrimId;
    private String requestorTypeName;

    public String getRequestorTypeName() {
        return requestorTypeName;
    }

    public void setRequestorTypeName(String requestorTypeName) {
        this.requestorTypeName = requestorTypeName;
    }

    public String getRefKrimId() {
        return refKrimId;
    }

    public void setRefKrimId(String refKrimId) {
        this.refKrimId = refKrimId;
    }

    public String getRequestorTypeId() {
        return requestorTypeId;
    }


    public void setRequestorTypeId(String requestorTypeId) {
        this.requestorTypeId = requestorTypeId;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getInternalRequestorId() {
        return internalRequestorId;
    }


    public void setInternalRequestorId(String internalRequestorId) {
        this.internalRequestorId = internalRequestorId;
    }


    public String getEmail() {
        return email;
    }


    public String getExternalRequestorId() {
        return externalRequestorId;
    }


    public void setExternalRequestorId(String externalRequestorId) {
        this.externalRequestorId = externalRequestorId;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        return null;
    }


}
