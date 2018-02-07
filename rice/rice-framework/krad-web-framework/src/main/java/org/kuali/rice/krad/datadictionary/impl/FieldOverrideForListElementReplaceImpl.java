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
 * A Field Override used to replace list elements in a Data Dictionary bean.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FieldOverrideForListElementReplaceImpl extends FieldOverrideForListElementBase implements FieldOverride {
    private Object replaceWith;

    public Object getReplaceWith() {
        return replaceWith;
    }

    public void setReplaceWith(Object replaceAt) {
        this.replaceWith = replaceAt;
    }

    protected void varifyConfig() {
        if (replaceWith == null) {
            throw new RuntimeException("Configuration Error, Missing required replaceWith parameter....");
        }
    }

    public Object performFieldOverride(Object bean, Object property) {
        varifyConfig();
        List oldList = (List) property;

        int replacePos = getElementPositionInList(getElement(), oldList);

        if (replacePos == -1) {
            throw new RuntimeException("Configuration Error, replace element could not be located.");
        }
        if (replacePos >= 0 && replacePos < oldList.size()) {
            oldList.remove(replacePos);
            oldList.add(replacePos, getReplaceWith());
        }

        return oldList;
    }
}
