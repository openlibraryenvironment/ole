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
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.datadictionary.BeanOverride;
import org.kuali.rice.krad.datadictionary.FieldOverride;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * The base implementation of the BeanOverride interface.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BeanOverrideImpl implements BeanOverride {
    private static final Logger LOG = Logger.getLogger(BeanOverrideImpl.class);
    private String beanName;
    private List<FieldOverride> fieldOverrides;

    /**
     * @see org.kuali.rice.krad.datadictionary.BeanOverride#getFieldOverrides()
     */
    public List<FieldOverride> getFieldOverrides() {
        return this.fieldOverrides;
    }

    public void setFieldOverrides(List<FieldOverride> fieldOverirdes) {
        this.fieldOverrides = fieldOverirdes;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.BeanOverride#getBeanName()
     */
    public String getBeanName() {
        return this.beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.BeanOverride#performOverride(java.lang.Object)
     */
    public void performOverride(Object bean) {
        try {
            for (FieldOverride fieldOverride : fieldOverrides) {
                Object property = PropertyUtils.getProperty(bean, fieldOverride.getPropertyName());
                Object newProperty = fieldOverride.performFieldOverride(bean, property);
                BeanUtils.setProperty(bean, fieldOverride.getPropertyName(), newProperty);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
