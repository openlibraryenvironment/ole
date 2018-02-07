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
package org.kuali.rice.krad.datadictionary.impl;

import org.kuali.rice.krad.datadictionary.FieldOverride;

import java.util.List;

/**
 * A Field Override used to delete elements from a Data Dictionary bean.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FieldOverrideForListElementDeleteImpl extends FieldOverrideForListElementBase implements FieldOverride {

    public Object performFieldOverride(Object bean, Object property) {
        List oldList = (List) property;

        int deletePos = getElementPositionInList(this.getElement(), oldList);

        if (deletePos < 0) {
            throw new RuntimeException(
                    "Element to be deleted could not be found:bean=" + bean + ", property=" + property);
        } else {
            oldList.remove(deletePos);
        }
        return oldList;
    }
}
