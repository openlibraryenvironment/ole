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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public class OleLicensingRequirement extends PersistableBusinessObjectBase implements MutableInactivatable {

    private String licensingRequirementCode;
    private String licensingRequirementDesc;
    private boolean active;

    public String getLicensingRequirementCode() {
        return licensingRequirementCode;
    }

    public void setLicensingRequirementCode(String licensingRequirementCode) {
        this.licensingRequirementCode = licensingRequirementCode;
    }

    public String getLicensingRequirementDesc() {
        return licensingRequirementDesc;
    }

    public void setLicensingRequirementDesc(String licensingRequirementDesc) {
        this.licensingRequirementDesc = licensingRequirementDesc;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}