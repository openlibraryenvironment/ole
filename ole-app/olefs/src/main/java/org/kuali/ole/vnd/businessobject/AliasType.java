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
package org.kuali.ole.vnd.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public class AliasType extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Long aliasTypeId;

    private String aliasType;

    private String definition;

    private boolean active;


    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active=active;

    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Long getAliasTypeId() {
        return aliasTypeId;
    }

    public void setAliasTypeId(Long aliasTypeId) {
        this.aliasTypeId = aliasTypeId;
    }

    public String getAliasType() {
        return aliasType;
    }

    public void setAliasType(String aliasType) {
        this.aliasType = aliasType;
    }



}
