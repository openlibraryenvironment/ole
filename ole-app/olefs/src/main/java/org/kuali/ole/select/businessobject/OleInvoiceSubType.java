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

/**
 * This class is the business object class for OLE invoice subType
 */
public class OleInvoiceSubType extends PersistableBusinessObjectBase implements MutableInactivatable {
    private Integer invoiceSubTypeId;
    private String invoiceSubType;
    private String invoiceSubTypeDescription;
    private boolean active;

    /**
     * Gets invoice SubType id.
     *
     * @return invoiceSubTypeId.
     */

    public Integer getInvoiceSubTypeId() {
        return invoiceSubTypeId;
    }

    /**
     * Sets invoice SubType id.
     *
     * @param invoiceSubTypeId to set.
     */
    public void setInvoiceSubTypeId(Integer invoiceSubTypeId) {
        this.invoiceSubTypeId = invoiceSubTypeId;
    }

    /**
     * Gets invoice SubType .
     *
     * @return invoiceSubType.
     */

    public String getInvoiceSubType() {
        return invoiceSubType;
    }

    /**
     * Sets invoice subType .
     *
     * @param invoiceSubType to set.
     */
    public void setInvoiceSubType(String invoiceSubType) {
        this.invoiceSubType = invoiceSubType;
    }


    /**
     * Gets invoice SubType Description.
     *
     * @return invoiceSubTypeDescription.
     */
    public String getInvoiceSubTypeDescription() {
        return invoiceSubTypeDescription;
    }

    /**
     * Sets invoice subType description .
     *
     * @param invoiceSubTypeDescription to set.
     */
    public void setInvoiceSubTypeDescription(String invoiceSubTypeDescription) {
        this.invoiceSubTypeDescription = invoiceSubTypeDescription;
    }

    /**
     * Gets active indicator.
     *
     * @return active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active indicator.
     *
     * @param active.
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("invoiceSubTypeId", this.invoiceSubTypeId);
        return m;
    }

}
