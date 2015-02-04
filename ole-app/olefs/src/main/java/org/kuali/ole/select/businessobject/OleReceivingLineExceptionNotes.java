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

/**
 * This class is the business object class for OLE Receiving Line Item Receipt Notes.
 */

public class OleReceivingLineExceptionNotes extends PersistableBusinessObjectBase {

    private Integer receivingItemExceptionId;
    private Integer receivingLineItemIdentifier;
    private Integer exceptionTypeId;
    private String exceptionNotes;
    private OleExceptionType exceptionType;
    private OleLineItemReceivingItem receivingLineItem;

    /**
     * Gets receiving lineitem exceptionId.
     *
     * @return receivingItemExceptionId.
     */
    public Integer getReceivingItemExceptionId() {
        return receivingItemExceptionId;
    }

    /**
     * Sets receiving lineitem exceptionId.
     *
     * @param receivingItemExceptionId to set.
     */
    public void setReceivingItemExceptionId(Integer receivingItemExceptionId) {
        this.receivingItemExceptionId = receivingItemExceptionId;
    }

    /**
     * Gets receiving lineitem identifier.
     *
     * @return receivingLineItemIdentifier.
     */
    public Integer getReceivingLineItemIdentifier() {
        return receivingLineItemIdentifier;
    }

    /**
     * Sets receiving lineitem identifier.
     *
     * @param receivingLineItemIdentifier to set.
     */
    public void setReceivingLineItemIdentifier(Integer receivingLineItemIdentifier) {
        this.receivingLineItemIdentifier = receivingLineItemIdentifier;
    }

    /**
     * Gets exception type id.
     *
     * @return exceptionTypeId.
     */
    public Integer getExceptionTypeId() {
        return exceptionTypeId;
    }

    /**
     * Sets exception type id.
     *
     * @param exceptionTypeId to set.
     */
    public void setExceptionTypeId(Integer exceptionTypeId) {
        this.exceptionTypeId = exceptionTypeId;
    }

    /**
     * Gets exception notes.
     *
     * @return exceptionNotes.
     */
    public String getExceptionNotes() {
        return exceptionNotes;
    }

    /**
     * Sets exception notes.
     *
     * @param exceptionNotes to set.
     */
    public void setExceptionNotes(String exceptionNotes) {
        this.exceptionNotes = exceptionNotes;
    }

    /**
     * Gets OleExceptionType.
     *
     * @return exceptionType.
     */
    public OleExceptionType getExceptionType() {
        return exceptionType;
    }

    /**
     * Sets OleExceptionType.
     *
     * @param exceptionType to set.
     */
    public void setExceptionType(OleExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * Gets OleLineItemReceivingItem.
     *
     * @return receivingLineItem.
     */
    public OleLineItemReceivingItem getReceivingLineItem() {
        return receivingLineItem;
    }

    /**
     * Sets OleLineItemReceivingItem.
     *
     * @param receivingLineItem.
     */
    public void setReceivingLineItem(OleLineItemReceivingItem receivingLineItem) {
        this.receivingLineItem = receivingLineItem;
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("receivingLineItemIdentifier", receivingLineItemIdentifier);
        map.put("receivingItemExceptionId", receivingItemExceptionId);
        map.put("exceptionTypeId", exceptionTypeId);
        map.put("exceptionNotes", exceptionNotes);

        return map;
    }
}
