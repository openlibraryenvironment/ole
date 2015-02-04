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

public class OleSufficientFundsCheckType extends PersistableBusinessObjectBase implements MutableInactivatable {


    private BigDecimal sufficientFundsCheckTypeId;
    private String sufficientFundsCheckType;
    private boolean active;


    public BigDecimal getSufficientFundsCheckTypeId() {
        return sufficientFundsCheckTypeId;
    }

    public void setSufficientFundsCheckTypeId(BigDecimal sufficientFundsCheckTypeId) {
        this.sufficientFundsCheckTypeId = sufficientFundsCheckTypeId;
    }

    public String getSufficientFundsCheckType() {
        return sufficientFundsCheckType;
    }

    public void setSufficientFundsCheckType(String sufficientFundsCheckType) {
        this.sufficientFundsCheckType = sufficientFundsCheckType;
    }


    public boolean isActive() {
        // TODO Auto-generated method stub
        return active;
    }


    public void setActive(boolean active) {
        // TODO Auto-generated method stub
        this.active = active;
    }

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        LinkedHashMap m = new LinkedHashMap();
        m.put("sufficientFundsCheckTypeId", sufficientFundsCheckTypeId);
        m.put("sufficientFundsCheckType", sufficientFundsCheckType);
        m.put("active", active);
        return m;


    }

}
