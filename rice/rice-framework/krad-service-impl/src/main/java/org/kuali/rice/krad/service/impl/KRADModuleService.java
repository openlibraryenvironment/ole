/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.service.impl;

import java.util.List;
import java.util.Map;

import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.util.ExternalizableBusinessObjectUtils;

/**
 * Module service implementation for the Rice KRAD module
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRADModuleService extends ModuleServiceBase {
    protected List<String> businessObjects;

    @Override
    public boolean isResponsibleFor(Class businessObjectClass) {
        if (businessObjects != null) {
            if (businessObjects.contains(businessObjectClass.getName())) {
                return true;
            }
        }

        if (ExternalizableBusinessObject.class.isAssignableFrom(businessObjectClass)) {
            Class externalizableBusinessObjectInterface =
                    ExternalizableBusinessObjectUtils.determineExternalizableBusinessObjectSubInterface(
                            businessObjectClass);
            if (externalizableBusinessObjectInterface != null) {
                Map<Class, Class> validEBOs = getModuleConfiguration().getExternalizableBusinessObjectImplementations();
                if (validEBOs != null) {
                    if (validEBOs.get(externalizableBusinessObjectInterface) != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public List<String> getBusinessObjects() {
        return this.businessObjects;
    }

    public void setBusinessObjects(List<String> businessObjects) {
        this.businessObjects = businessObjects;
    }
}
