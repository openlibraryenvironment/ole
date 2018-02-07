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
package org.kuali.rice.krad.datadictionary;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;

/**
 * Base class for dictionary bean processors that provides utility methods
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class DictionaryBeanProcessorBase implements DictionaryBeanProcessor {

    /**
     * Retrieves the class for the object that will be created from the bean definition. Since the class might not
     * be configured on the bean definition, but by a parent, each parent bean definition is recursively checked for
     * a class until one is found
     *
     * @param beanDefinition bean definition to get class for
     * @param beanFactory bean factory that contains the bean definition
     * @return Class<?> class configured for the bean definition, or null
     */
    protected Class<?> getBeanClass(BeanDefinition beanDefinition, ConfigurableListableBeanFactory beanFactory) {
        if (StringUtils.isNotBlank(beanDefinition.getBeanClassName())) {
            try {
                return Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                // swallow exception and return null so bean is not processed
                return null;
            }
        } else if (StringUtils.isNotBlank(beanDefinition.getParentName())) {
            BeanDefinition parentBeanDefinition = beanFactory.getBeanDefinition(beanDefinition.getParentName());
            if (parentBeanDefinition != null) {
                return getBeanClass(parentBeanDefinition, beanFactory);
            }
        }

        return null;
    }

    /**
     * Determines whether the given value is of String type and if so returns the string value
     *
     * @param value object value to check
     * @return String string value for object or null if object is not a string type
     */
    protected String getStringValue(Object value) {
        if (value instanceof TypedStringValue) {
            TypedStringValue typedStringValue = (TypedStringValue) value;
            return typedStringValue.getValue();
        } else if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    /**
     * Applies the given property name and value to the bean definition
     *
     * @param propertyPath name of the property to add value for
     * @param propertyValue value for the property
     * @param beanDefinition bean definition to add property value to
     */
    protected void applyPropertyValueToBean(String propertyPath, String propertyValue, BeanDefinition beanDefinition) {
        applyPropertyValueToBean(propertyPath, propertyValue, beanDefinition.getPropertyValues());
    }

    /**
     * Applies the given property name and value to given property values
     *
     * @param propertyPath name of the property to add value for
     * @param propertyValue value for the property
     * @param pvs property values to add property to
     */
    protected void applyPropertyValueToBean(String propertyPath, String propertyValue, MutablePropertyValues pvs) {
        pvs.addPropertyValue(propertyPath, propertyValue);
    }

    /**
     * Determines if the given property value is a bean definition or bean definition holder and if
     * so returns the value as a bean definintion
     *
     * @param propertyValue property value to get bean definition from
     * @return property value as a bean definition or null if value does not contain a bean definition
     */
    protected BeanDefinition getPropertyValueBeanDefinition(PropertyValue propertyValue) {
        BeanDefinition beanDefinition = null;

        Object value = propertyValue.getValue();
        if ((value instanceof BeanDefinition) || (value instanceof BeanDefinitionHolder)) {
            if (propertyValue instanceof BeanDefinition) {
                beanDefinition = (BeanDefinition) propertyValue;
            } else {
                beanDefinition = ((BeanDefinitionHolder) value).getBeanDefinition();
            }
        }

        return beanDefinition;
    }

    /**
     * Indicates whether the given bean name was generated by spring
     *
     * @param beanName bean name to check
     * @return boolean true if bean name is generated, false if not
     */
    protected boolean isGeneratedBeanName(String beanName) {
        return StringUtils.isNotBlank(beanName) && (StringUtils.contains(beanName, "$") || StringUtils.contains(
                beanName, "#"));
    }

    /**
     * Returns an instance of the data dictionary service
     *
     * @return DataDictionaryService instance
     */
    protected DataDictionaryService getDataDictionaryService() {
        return KRADServiceLocatorWeb.getDataDictionaryService();
    }
}
