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
 * This class is the business object class for OLE Exception type
 */
public class OleExceptionType extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Integer exceptionTypeId;
    private String exceptionType;
    private boolean active;

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
     * Gets exception type.
     *
     * @return exceptionType.
     */
    public String getExceptionType() {
        return exceptionType;
    }

    /**
     * Sets exception type.
     *
     * @param exceptionType.
     */
    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
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
        m.put("exceptionTypeId", this.exceptionTypeId);
        return m;
    }

}
