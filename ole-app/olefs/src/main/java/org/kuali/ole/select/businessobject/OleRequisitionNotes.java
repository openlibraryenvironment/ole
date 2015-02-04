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

/**
 * OLE RequisitionNotes Base Business Object.
 */
public class OleRequisitionNotes extends PersistableBusinessObjectBase implements OleNotes {

    private Integer itemNoteId;
    private Integer itemIdentifier;
    private Integer noteTypeId;
    private String note;

    private OleNoteType noteType;
    private PurApItem purapItem;

    /**
     * Constructs a OleRequisitionNotesBase.java.
     */
    public OleRequisitionNotes() {

    }

    /**
     * get the RequisitionItem Note Id
     *
     * @return itemNoteId
     * @see org.kuali.ole.select.businessobject.OleNotes#getItemNoteId()
     */

    @Override
    public Integer getItemNoteId() {
        return itemNoteId;
    }

    /**
     * set the Requisition Item Note Id
     *
     * @param itemNoteId
     * @see org.kuali.ole.select.businessobject.OleNotes#setItemNoteId(java.math.BigDecimal)
     */
    @Override
    public void setItemNoteId(Integer itemNoteId) {
        this.itemNoteId = itemNoteId;

    }

    /**
     * get Item Identifier
     *
     * @return itemIdentifier
     * @see org.kuali.ole.select.businessobject.OleNotes#getItemIdentifier()
     */
    @Override
    public Integer getItemIdentifier() {
        return itemIdentifier;
    }

    /**
     * set Item Identifier
     *
     * @param itemIdentifier
     * @see org.kuali.ole.select.businessobject.OleNotes#setItemIdentifier(java.lang.Integer)
     */
    @Override
    public void setItemIdentifier(Integer itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    /**
     * get NoteType Bussiness Object
     *
     * @return noteType
     * @see org.kuali.ole.select.businessobject.OleNotes#getNoteType()
     */
    @Override
    public OleNoteType getNoteType() {
        return noteType;
    }

    /**
     * set NoteType Bussiness Object
     *
     * @param noteType
     * @see org.kuali.ole.select.businessobject.OleNotes#setNoteType(org.kuali.ole.select.businessobject.OleNoteType)
     */
    @Override
    public void setNoteType(OleNoteType noteType) {
        this.noteType = noteType;
    }

    /**
     * get Requisition NoteType Id
     *
     * @return noteTypeId
     * @see org.kuali.ole.select.businessobject.OleNotes#getNoteTypeId()
     */
    @Override
    public Integer getNoteTypeId() {
        return noteTypeId;
    }

    /**
     * set Requisition NoteTypeId
     *
     * @param NoteTypeId
     * @see org.kuali.ole.select.businessobject.OleNotes#setNoteTypeId(java.math.BigDecimal)
     */
    @Override
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    /**
     * get Requisition Note
     *
     * @return note
     * @see org.kuali.ole.select.businessobject.OleNotes#getNote()
     */
    @Override
    public String getNote() {
        return note;
    }

    /**
     * set Requisition Note
     *
     * @param Note
     * @see org.kuali.ole.select.businessobject.OleNotes#setNote(java.lang.String)
     */
    @Override
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * get the OleRequistionItem
     *
     * @return purapItem
     * @see org.kuali.ole.select.businessobject.OleNotes#getPurapItem()
     */
    @Override
    public PurApItem getPurapItem() {
        return purapItem;
    }

    /**
     * set the OleRequistionItem
     *
     * @param purapItem
     * @see org.kuali.ole.select.businessobject.OleNotes#setReqItem(org.kuali.ole.module.purap.businessobject.PurApItem)
     */
    @Override
    public void setPurapItem(PurApItem purapItem) {
        this.purapItem = purapItem;
    }
}
