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

public class OleRequestSourceType extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Integer requestSourceTypeId;
    private String requestSourceType;
    private boolean active;


    public Integer getRequestSourceTypeId() {
        return requestSourceTypeId;
    }

    public void setRequestSourceTypeId(Integer requestSourceTypeId) {
        this.requestSourceTypeId = requestSourceTypeId;
    }

    public String getRequestSourceType() {
        return requestSourceType;
    }

    public void setRequestSourceType(String requestSourceType) {
        this.requestSourceType = requestSourceType;
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
        m.put("requestSourceTypeId", this.requestSourceTypeId);
        m.put("requestSourceType", this.requestSourceType);
        return m;
    }

}
