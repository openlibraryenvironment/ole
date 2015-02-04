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

import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * This class... OLE PurchaseOrderNotes Business class Base
 */
public class OlePurchaseOrderNotes extends PersistableBusinessObjectBase implements OleNotes {

    /**
     * Constructs a OlePurchaseOrderNotesBase.java.
     */
    public OlePurchaseOrderNotes() {

    }

    private Integer itemNoteId;
    private String documentNumber;
    private Integer itemIdentifier;
    private String note;
    private Integer noteTypeId;

    private OleNoteType noteType;
    private PurApItem purapItem;

    /**
     * get the PO Item Note Id
     *
     * @return itemNoteId
     * @see org.kuali.ole.select.businessobject.OleNotes#getItemNoteId()
     */
    public Integer getItemNoteId() {
        return itemNoteId;
    }

    /**
     * set PO Item Note Id
     *
     * @param itemNoteId
     * @see org.kuali.ole.select.businessobject.OleNotes#setItemNoteId(java.math.BigDecimal)
     */
    public void setItemNoteId(Integer itemNoteId) {
        this.itemNoteId = itemNoteId;
    }

    /**
     * get the Document Number
     *
     * @return documentNumber
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * set Document Number
     *
     * @param documentNumber
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * get Item Identifier
     *
     * @return item Identifier
     * @see org.kuali.ole.select.businessobject.OleNotes#getItemIdentifier()
     */
    public Integer getItemIdentifier() {
        return itemIdentifier;
    }

    /**
     * set Item Identifier
     *
     * @return itemIdentifier
     * @see org.kuali.ole.select.businessobject.OleNotes#setItemIdentifier(java.lang.Integer)
     */
    public void setItemIdentifier(Integer itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    /**
     * get PO Note
     *
     * @return note
     * @see org.kuali.ole.select.businessobject.OleNotes#getNote()
     */
    public String getNote() {
        return note;
    }

    /**
     * set PO Note
     *
     * @param note
     * @see org.kuali.ole.select.businessobject.OleNotes#setNote(java.lang.String)
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * get PO NoteType Id
     *
     * @return noteTypeId
     * @see org.kuali.ole.select.businessobject.OleNotes#getNoteTypeId()
     */
    public Integer getNoteTypeId() {
        return noteTypeId;
    }

    /**
     * set PO NoteType Id
     *
     * @param noteTypeId
     * @see org.kuali.ole.select.businessobject.OleNotes#setNoteTypeId(java.math.BigDecimal)
     */
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    /**
     * get Note Type
     *
     * @return note
     * @see org.kuali.ole.select.businessobject.OleNotes#getNoteType()
     */
    public OleNoteType getNoteType() {
        return noteType;
    }

    /**
     * set Note Type
     *
     * @param note
     * @see org.kuali.ole.select.businessobject.OleNotes#setNoteType(org.kuali.ole.select.businessobject.OleNoteType)
     */
    public void setNoteType(OleNoteType noteType) {
        this.noteType = noteType;
    }

    /**
     * Constructs a OlePurchaseOrderNotesBase.java.
     *
     * @param OleRequisitionNotes notes
     */
    public OlePurchaseOrderNotes(OleNotes notes) {
        this.setNote(notes.getNote());
        this.setNoteTypeId(notes.getNoteTypeId());
    }

    /**
     * This method... for getting OlePurchaseOrderItem from PurapItem
     *
     * @return purapItem
     */
    public OlePurchaseOrderItem getOlePurchaseOrderItem() {
        return this.getPurapItem();
    }

    /**
     * This method... for setting the OlePurchaseOrderItem to PurApItem
     *
     * @param item
     */
    public void setOlePurchaseOrderItem(OlePurchaseOrderItem item) {
        this.setPurapItem(item);
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("documentNumber", documentNumber);
        map.put("itemIdentifier", itemIdentifier);
        map.put("poItemNoteId", itemNoteId);
        map.put("noteTypeId", noteTypeId);
        map.put("note", note);
        return map;
    }

    /**
     * @see org.kuali.ole.select.businessobject.OleNotes#getPurapItem()
     */
    @Override
    public <T extends PurApItem> T getPurapItem() {
        return (T) purapItem;
    }

    /**
     * @see org.kuali.ole.select.businessobject.OleNotes#setPurapItem(org.kuali.ole.module.purap.businessobject.PurApItem)
     */
    @Override
    public void setPurapItem(PurApItem item) {
        purapItem = item;

    }

}
