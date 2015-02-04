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
 * This class is the business object class for OLE payment method
 */
public class OleInvoiceType extends PersistableBusinessObjectBase implements MutableInactivatable {
    private Integer invoiceTypeId;
    private String invoiceType;
    private String invoiceTypeDescription;
    private boolean active;


    /**
     * Gets invoice Type id.
     *
     * @return invoiceTypeId.
     */

    public Integer getInvoiceTypeId() {
        return invoiceTypeId;
    }

    /**
     * Sets invoice Type id.
     *
     * @param invoiceTypeId to set.
     */
    public void setInvoiceTypeId(Integer invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    /**
     * Gets invoice  Type .
     *
     * @return invoiceType.
     */

    public String getInvoiceType() {
        return invoiceType;
    }

    /**
     * Sets invoice Type .
     *
     * @param invoiceType to set.
     */
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }


    /**
     * Gets invoice  Type Description.
     *
     * @return invoiceTypeDescription.
     */
    public String getInvoiceTypeDescription() {
        return invoiceTypeDescription;
    }

    /**
     * Sets invoice  Type description .
     *
     * @param invoiceTypeDescription to set.
     */
    public void setInvoiceTypeDescription(String invoiceTypeDescription) {
        this.invoiceTypeDescription = invoiceTypeDescription;
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
        m.put("invoiceTypeId", this.invoiceTypeId);
        return m;
    }

}
