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
 * This class is the business object class for OLE Note type
 */

public class OleNoteType extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Integer noteTypeId;
    private String noteType;
    private boolean active;

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
     * Gets note type.
     *
     * @return noteType.
     */
    public String getNoteType() {
        return noteType;
    }

    /**
     * Sets note type.
     *
     * @param noteType to set.
     */
    public void setNoteType(String noteType) {
        this.noteType = noteType;
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
        m.put("noteTypeId", this.noteTypeId);
        return m;
    }

}
