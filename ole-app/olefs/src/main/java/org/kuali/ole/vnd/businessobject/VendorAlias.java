/*
 * Copyright 2007 The Kuali Foundation
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

package org.kuali.ole.vnd.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Alternate name for a Vendor.
 */
public class VendorAlias extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Integer vendorAliasId;
    private String vendorAliasName;
    private boolean gokbVendorAliasInd;
    private Integer vendorDetailAssignedIdentifier;
    private Integer vendorHeaderGeneratedIdentifier;
    private Integer vendorAliasTypeId;
    private AliasType vendorAliasType;
    private boolean active;

    VendorDetail vendorDetail;

    /**
     * Default constructor.
     */
    public VendorAlias() {

    }

    public String getVendorAliasName() {

        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public Integer getVendorDetailAssignedIdentifier() {

        return vendorDetailAssignedIdentifier;
    }

    public void setVendorDetailAssignedIdentifier(Integer vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
    }

    public Integer getVendorHeaderGeneratedIdentifier() {

        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public VendorDetail getVendorDetail() {

        return vendorDetail;
    }

    /**
     * Sets the vendorDetail attribute value.
     * 
     * @param vendorDetail The vendorDetail to set.
     * @deprecated
     */
    public void setVendorDetail(VendorDetail vendorDetail) {
        this.vendorDetail = vendorDetail;
    }

    

    public Integer getVendorAliasTypeId() {
        return vendorAliasTypeId;
    }

    public void setVendorAliasTypeId(Integer vendorAliasTypeId) {
        this.vendorAliasTypeId = vendorAliasTypeId;
    }

    public AliasType getVendorAliasType() {
        return vendorAliasType;
    }

    public void setVendorAliasType(AliasType vendorAliasType) {
        this.vendorAliasType = vendorAliasType;
    }

    /**
     * Gets the vendorAliasId attribute. 
     * @return Returns the vendorAliasId.
     */
    public Integer getVendorAliasId() {
        return vendorAliasId;
    }

    /**
     * Sets the vendorAliasId attribute value.
     * @param vendorAliasId The vendorAliasId to set.
     */
    public void setVendorAliasId(Integer vendorAliasId) {
        this.vendorAliasId = vendorAliasId;
    }

    public boolean isGokbVendorAliasInd() {
        return gokbVendorAliasInd;
    }

    public void setGokbVendorAliasInd(boolean gokbVendorAliasInd) {
        this.gokbVendorAliasInd = gokbVendorAliasInd;
    }
}
