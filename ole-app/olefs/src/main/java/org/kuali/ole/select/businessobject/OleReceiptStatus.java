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

import java.math.BigDecimal;

public class OleReceiptStatus extends PersistableBusinessObjectBase implements MutableInactivatable {

    private BigDecimal receiptStatusId;
    private String receiptStatusDocType;
    private String receiptStatusCd;
    private String receiptStatus;
    private boolean active;

    /**
     * Gets the value of the receiptStatusId property
     *
     * @return receiptStatusId
     */
    public BigDecimal getReceiptStatusId() {
        return receiptStatusId;
    }

    /**
     * Sets the receiptStatusId value
     *
     * @param receiptStatusId
     */
    public void setReceiptStatusId(BigDecimal receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    /**
     * Gets the value of the receiptStatus property
     *
     * @return receiptStatus
     */
    public String getReceiptStatus() {
        return receiptStatus;
    }

    /**
     * Sets the receiptStatus value
     *
     * @param receiptStatus
     */
    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    /**
     * Gets the active attribute.
     *
     * @return Returns the active.
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active attribute value.
     *
     * @param active The active to set.
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the receiptStatusCd attribute.
     *
     * @return Returns the receiptStatusCd.
     */
    public String getReceiptStatusCd() {
        return receiptStatusCd;
    }

    /**
     * Sets the receiptStatusCd attribute value.
     *
     * @param receiptStatusCd The receiptStatusCd to set.
     */
    public void setReceiptStatusCd(String receiptStatusCd) {
        this.receiptStatusCd = receiptStatusCd;
    }

    /**
     * Gets the receiptStatusDocType attribute.
     *
     * @return Returns the receiptStatusDocType.
     */
    public String getReceiptStatusDocType() {
        return receiptStatusDocType;
    }

    /**
     * Sets the receiptStatusDocType attribute value.
     *
     * @param receiptStatusDocType The receiptStatusDocType to set.
     */
    public void setReceiptStatusDocType(String receiptStatusDocType) {
        this.receiptStatusDocType = receiptStatusDocType;
    }

}
