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

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class OleItemPriceSource extends PersistableBusinessObjectBase implements MutableInactivatable {

    private BigDecimal itemPriceSourceId;
    private String itemPriceSource;
    private boolean active;

    public BigDecimal getItemPriceSourceId() {
        return itemPriceSourceId;
    }

    public void setItemPriceSourceId(BigDecimal itemPriceSourceId) {
        this.itemPriceSourceId = itemPriceSourceId;
    }

    public String getItemPriceSource() {
        return itemPriceSource;
    }

    public void setItemPriceSource(String itemPriceSource) {
        this.itemPriceSource = itemPriceSource;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("itemPriceSourceId", this.itemPriceSourceId);
        return m;
    }
}
