/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
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
import java.util.LinkedHashMap;

public class OleConstraintType extends PersistableBusinessObjectBase implements MutableInactivatable {

    private BigDecimal constraintTypeId;
    private String constraintType;
    private boolean active;


    /**
     * Gets the constraintTypeId attribute.
     *
     * @return Returns the constraintTypeId.
     */
    public BigDecimal getConstraintTypeId() {
        return constraintTypeId;
    }

    /**
     * Sets the constraintTypeId attribute value.
     *
     * @param constraintTypeId The constraintTypeId to set.
     */
    public void setConstraintTypeId(BigDecimal constraintTypeId) {
        this.constraintTypeId = constraintTypeId;
    }

    /**
     * Gets the constraintType attribute.
     *
     * @return Returns the constraintType.
     */
    public String getConstraintType() {
        return constraintType;
    }

    /**
     * Sets the constraintType attribute value.
     *
     * @param constraintType The constraintType to set.
     */
    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        LinkedHashMap m = new LinkedHashMap();
        m.put("constraintTypeId", constraintTypeId);
        /*m.put("constraintType",constraintType);
        m.put("active", active);*/
        return m;
    }

}
