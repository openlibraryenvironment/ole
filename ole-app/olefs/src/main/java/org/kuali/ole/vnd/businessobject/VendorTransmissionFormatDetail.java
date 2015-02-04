/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.vnd.businessobject;

import java.util.LinkedHashMap;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public class VendorTransmissionFormatDetail extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Long vendorTransmissionFormatId;
    private Integer vendorHeaderGeneratedIdentifier;
    private Integer vendorDetailAssignedIdentifier;
    private boolean vendorPreferredTransmissionFormat;
    private Integer vendorTransmissionTypeId;
    //private String vendorEDITypeConnection;
    private String vendorEDIConnectionAddress;
    private String vendorEDIConnectionUserName;
    private String vendorEDIConnectionPassword;
    private boolean active;
    private VendorDetail vendorDetail;
    private OleVendorTransmissionFormat vendorTransmissionFormat;
    private OleVendorTransmissionType vendorTransmissionTypes;

    public Integer getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }
    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }
    public Integer getVendorDetailAssignedIdentifier() {
        return vendorDetailAssignedIdentifier;
    }
    public void setVendorDetailAssignedIdentifier(Integer vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
    }

    public String getVendorEDIConnectionAddress() {
        return vendorEDIConnectionAddress;
    }
    public void setVendorEDIConnectionAddress(String vendorEDIConnectionAddress) {
        this.vendorEDIConnectionAddress = vendorEDIConnectionAddress;
    }
    public String getVendorEDIConnectionUserName() {
        return vendorEDIConnectionUserName;
    }
    public void setVendorEDIConnectionUserName(String vendorEDIConnectionUserName) {
        this.vendorEDIConnectionUserName = vendorEDIConnectionUserName;
    }
    public String getVendorEDIConnectionPassword() {
        return vendorEDIConnectionPassword;
    }
    public void setVendorEDIConnectionPassword(String vendorEDIConnectionPassword) {
        this.vendorEDIConnectionPassword = vendorEDIConnectionPassword;
    }
    @Override
    public boolean isActive() {
        return active;
    }
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringMapper()
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap m = new LinkedHashMap();
        if (this.vendorHeaderGeneratedIdentifier != null) {
            m.put("vendorHeaderGeneratedIdentifier", this.vendorHeaderGeneratedIdentifier.toString());
        }
        if (this.vendorDetailAssignedIdentifier != null) {
            m.put("vendorDetailAssignedIdentifier", this.vendorDetailAssignedIdentifier.toString());
        }
        if (this.vendorTransmissionFormatId != null) {
            m.put("vendorTransmissionFormatIdentifier", this.vendorTransmissionFormatId.toString());
        }
        return m;
    }

    public boolean isVendorPreferredTransmissionFormat() {
        return vendorPreferredTransmissionFormat;
    }
    public void setVendorPreferredTransmissionFormat(boolean vendorPreferredTransmissionFormat) {
        this.vendorPreferredTransmissionFormat = vendorPreferredTransmissionFormat;
    }
    public Long getVendorTransmissionFormatId() {
        return vendorTransmissionFormatId;
    }
    public void setVendorTransmissionFormatId(Long vendorTransmissionFormatId) {
        this.vendorTransmissionFormatId = vendorTransmissionFormatId;
    }
    public VendorDetail getVendorDetail() {
        return vendorDetail;
    }
    public void setVendorDetail(VendorDetail vendorDetail) {
        this.vendorDetail = vendorDetail;
    }
    public OleVendorTransmissionFormat getVendorTransmissionFormat() {
        return vendorTransmissionFormat;
    }
    public void setVendorTransmissionFormat(OleVendorTransmissionFormat vendorTransmissionFormat) {
        this.vendorTransmissionFormat = vendorTransmissionFormat;
    }


    public Integer getVendorTransmissionTypeId() {
        return vendorTransmissionTypeId;
    }
    public void setVendorTransmissionTypeId(Integer vendorTransmissionTypeId) {
        this.vendorTransmissionTypeId = vendorTransmissionTypeId;
    }
    public OleVendorTransmissionType getVendorTransmissionTypes() {
        return vendorTransmissionTypes;
    }
    public void setVendorTransmissionTypes(OleVendorTransmissionType vendorTransmissionTypes) {
        this.vendorTransmissionTypes = vendorTransmissionTypes;
    }

}
