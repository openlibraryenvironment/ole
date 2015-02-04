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
 * This class is the business object class for OLE Receiving Correction Line Item Exception Notes.
 */

public class OleCorrectionReceivingItemExceptionNotes extends PersistableBusinessObjectBase {

    private Integer receivingCorrectionItemExceptionId;
    private Integer receivingItemIdentifier;
    private Integer exceptionTypeId;
    private String exceptionNotes;
    private OleExceptionType exceptionType;
    private OleLineItemReceivingItem receivingLineItem;

    /**
     * Default Constructor
     */
    public OleCorrectionReceivingItemExceptionNotes() {

    }

    /**
     * Constructs a OleCorrectionReceivingItemReceiptNotes with notes populated from Line Item Receiving
     *
     * @param oleReceivingNotes
     */
    public OleCorrectionReceivingItemExceptionNotes(OleReceivingLineExceptionNotes oleExceptionNotes) {
        this.setExceptionTypeId(oleExceptionNotes.getExceptionTypeId());
        this.setExceptionNotes(oleExceptionNotes.getExceptionNotes());
    }

    /**
     * Gets receiving correction lineitem exceptionId.
     *
     * @return receivingCorrectionItemExceptionId.
     */
    public Integer getReceivingCorrectionItemExceptionId() {
        return receivingCorrectionItemExceptionId;
    }

    /**
     * Sets receiving correction lineitem exceptionId.
     *
     * @param receivingCorrectionItemExceptionId
     *         to set.
     */
    public void setReceivingCorrectionItemExceptionId(Integer receivingCorrectionItemExceptionId) {
        this.receivingCorrectionItemExceptionId = receivingCorrectionItemExceptionId;
    }

    /**
     * Gets receiving correction lineitem identifier.
     *
     * @return receivingCorrectionItemIdentifier.
     */
    public Integer getReceivingItemIdentifier() {
        return receivingItemIdentifier;
    }

    /**
     * Sets receiving correction lineitem identifier.
     *
     * @param receivingCorrectionItemIdentifier
     *         to set.
     */
    public void setReceivingItemIdentifier(Integer receivingItemIdentifier) {
        this.receivingItemIdentifier = receivingItemIdentifier;
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
        map.put("receivingCorrectionItemExceptionId", receivingCorrectionItemExceptionId);
        map.put("receivingItemIdentifier", receivingItemIdentifier);
        map.put("exceptionTypeId", exceptionTypeId);
        map.put("exceptionNotes", exceptionNotes);

        return map;
    }
}
