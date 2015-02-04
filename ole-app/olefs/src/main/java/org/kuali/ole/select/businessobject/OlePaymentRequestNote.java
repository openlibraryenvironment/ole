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
 * This Bussiness Object Base class is for Ole Payment Request Invoice Note
 */
public class OlePaymentRequestNote extends PersistableBusinessObjectBase {

    private Integer itemNoteIdentifier;
    private Integer itemIdentifier;
    private String note;

    private PurApItem purapItem;

    /**
     * Gets the Identifier of Item Note
     *
     * @return itemNoteIdentifier
     */
    public Integer getItemNoteIdentifier() {
        return itemNoteIdentifier;
    }

    /**
     * Sets that Identifier value of Item Note
     *
     * @param itemNoteIdentifier
     */
    public void setItemNoteIdentifier(Integer itemNoteIdentifier) {
        this.itemNoteIdentifier = itemNoteIdentifier;
    }

    /**
     * Gets the Identifier Value of Item
     *
     * @return itemIdentifier
     */
    public Integer getItemIdentifier() {
        return itemIdentifier;
    }

    /**
     * Sets the Identifier Value of Item
     *
     * @param itemIdentifier
     */
    public void setItemIdentifier(Integer itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    /**
     * Gets the Invoice Note description
     *
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * Gets the Invoice Note Description
     *
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Gets the PurapItem
     *
     * @return purapItem
     */
    public PurApItem getPurapItem() {
        return purapItem;
    }

    /**
     * Sets the PurapItem
     *
     * @param purapItem
     */
    public void setPurapItem(PurApItem purapItem) {
        this.purapItem = purapItem;
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("itemNoteIdentfier", itemNoteIdentifier);
        map.put("itemIdentifier", itemIdentifier);
        map.put("note", note);
        return null;
    }
}
