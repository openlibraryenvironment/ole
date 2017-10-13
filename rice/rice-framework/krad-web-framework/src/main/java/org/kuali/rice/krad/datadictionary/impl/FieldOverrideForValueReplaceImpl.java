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

import org.apache.commons.beanutils.BeanUtils;
import org.kuali.rice.krad.datadictionary.FieldOverride;

import java.lang.reflect.InvocationTargetException;

/**
 * A Field Override used to replace value elements from a Data Dictionary bean.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FieldOverrideForValueReplaceImpl implements FieldOverride {

    private String propertyName;
    private Object value;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object performFieldOverride(Object bean, Object property) {
        try {
            BeanUtils.setProperty(bean, this.getPropertyName(), this.getValue());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return getValue();
    }
}
