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
 * This class is the business object class for OLE Line Item Receiving Receipt Notes.
 */

public class OleLineItemReceivingReceiptNotes extends PersistableBusinessObjectBase {

    private Integer receivingLineItemNoteId;
    private Integer receivingLineItemIdentifier;
    private Integer noteTypeId;
    private String notes;
    private OleNoteType noteType;
    private OleLineItemReceivingItem receivingLineItem;
    private boolean notesAck = false;

    /**
     * Default Constructor
     */
    public OleLineItemReceivingReceiptNotes() {

    }

    /**
     * Constructs a OleLineItemReceivingReceiptNotes with notes populated from Ole PurchaseOrder
     *
     * @param olePoNotes
     */
    public OleLineItemReceivingReceiptNotes(OleNotes olePoNotes) {
        this.setNoteTypeId(olePoNotes.getNoteTypeId().intValue());
        this.setNotes(olePoNotes.getNote());
    }

    /**
     * Gets receiving lineitem noteId.
     *
     * @return receivingLineItemNoteId.
     */
    public Integer getReceivingLineItemNoteId() {
        return receivingLineItemNoteId;
    }

    /**
     * Sets receiving lineitem noteId.
     *
     * @param receivingLineItemNoteId to set.
     */
    public void setReceivingLineItemNoteId(Integer receivingLineItemNoteId) {
        this.receivingLineItemNoteId = receivingLineItemNoteId;
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
     * Gets note type id.
     *
     * @return noteTypeId.
     */
    public Integer getNoteTypeId() {
        return noteTypeId;
    }

    /**
     * Sets note type id.
     *
     * @param noteTypeId to set.
     */
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    /**
     * Gets Notes.
     *
     * @return notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets Notes.
     *
     * @param notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets OleNoteType.
     *
     * @return noteType.
     */
    public OleNoteType getNoteType() {
        return noteType;
    }

    /**
     * Sets OleNoteType.
     *
     * @param noteType to set.
     */
    public void setNoteType(OleNoteType noteType) {
        this.noteType = noteType;
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
     * Gets acknowledgement flag for special handling notes
     *
     * @return notesAck
     */
    public boolean isNotesAck() {
        return notesAck;
    }

    /**
     * Sets acknowledgement flag for special handling notes
     *
     * @param notesAck
     */
    public void setNotesAck(boolean notesAck) {
        this.notesAck = notesAck;
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("receivingLineItemIdentifier", receivingLineItemIdentifier);
        map.put("receivingLineItemNoteId", receivingLineItemNoteId);
        map.put("noteTypeId", noteTypeId);
        map.put("notes", notes);

        return map;
    }
}
