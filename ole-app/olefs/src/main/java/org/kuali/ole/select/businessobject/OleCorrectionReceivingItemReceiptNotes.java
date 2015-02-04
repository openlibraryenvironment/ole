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
 * This class is the business object class for OLE Receiving Correction Line Item Receipt Notes.
 */

public class OleCorrectionReceivingItemReceiptNotes extends PersistableBusinessObjectBase {

    private Integer receivingCorrectionLineItemNoteId;
    private Integer receivingItemIdentifier;
    private Integer noteTypeId;
    private String notes;
    private OleNoteType noteType;
    private OleCorrectionReceivingItem correctionReceivingItem;
    private boolean notesAck = false;

    /**
     * Default Constructor
     */
    public OleCorrectionReceivingItemReceiptNotes() {

    }

    /**
     * Constructs a OleCorrectionReceivingItemReceiptNotes with notes populated from Line Item Receiving
     *
     * @param oleReceivingNotes
     */
    public OleCorrectionReceivingItemReceiptNotes(OleLineItemReceivingReceiptNotes oleReceivingNotes) {
        this.setNoteTypeId(oleReceivingNotes.getNoteTypeId());
        this.setNotes(oleReceivingNotes.getNotes());
    }

    /**
     * Gets receiving correction lineitem noteId.
     *
     * @return receivingCorrectionLineItemNoteId.
     */
    public Integer getReceivingCorrectionLineItemNoteId() {
        return receivingCorrectionLineItemNoteId;
    }

    /**
     * Sets correction receiving lineitem noteId.
     *
     * @param receivingCorrectionLineItemNoteId
     *         to set.
     */
    public void setReceivingCorrectionLineItemNoteId(Integer receivingCorrectionLineItemNoteId) {
        this.receivingCorrectionLineItemNoteId = receivingCorrectionLineItemNoteId;
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
     * Gets OleCorrectionReceivingItem.
     *
     * @return correctionReceivingItem.
     */
    public OleCorrectionReceivingItem getCorrectionReceivingItem() {
        return correctionReceivingItem;
    }

    /**
     * Sets OleCorrectionReceivingItem.
     *
     * @param correctionReceivingItem.
     */
    public void setCorrectionReceivingItem(OleCorrectionReceivingItem correctionReceivingItem) {
        this.correctionReceivingItem = correctionReceivingItem;
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
        map.put("receivingItemIdentifier", receivingItemIdentifier);
        map.put("receivingCorrectionLineItemNoteId", receivingCorrectionLineItemNoteId);
        map.put("noteTypeId", noteTypeId);
        map.put("notes", notes);

        return map;
    }
}
