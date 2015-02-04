/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.module.purap.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class DiscountType extends PersistableBusinessObjectBase implements MutableInactivatable {
    private String itemDiscountType;
    private String itemDiscountTypeDescription;
    private boolean active;


    public String getItemDiscountType() {
        return itemDiscountType;
    }

    public void setItemDiscountType(String itemDiscountType) {
        this.itemDiscountType = itemDiscountType;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringMapper()
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("itemDiscountType", this.itemDiscountType);
        return m;
    }

    public String getItemDiscountTypeDescription() {
        return itemDiscountTypeDescription;
    }

    public void setItemDiscountTypeDescription(String itemDiscountTypeDescription) {
        this.itemDiscountTypeDescription = itemDiscountTypeDescription;
    }
}
