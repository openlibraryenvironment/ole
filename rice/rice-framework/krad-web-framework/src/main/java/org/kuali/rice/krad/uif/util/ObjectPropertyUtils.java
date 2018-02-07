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
package org.kuali.rice.krad.uif.util;

import org.kuali.rice.krad.datadictionary.parse.StringListConverter;
import org.kuali.rice.krad.datadictionary.parse.StringMapConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.core.convert.support.GenericConversionService;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Utility methods to get/set property values and working with objects
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.springframework.beans.BeanWrapper
 */
public class ObjectPropertyUtils {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ObjectPropertyUtils.class);

	public static void copyPropertiesToObject(Map<String, String> properties, Object object) {
		for (Map.Entry<String, String> property : properties.entrySet()) {
			setPropertyValue(object, property.getKey(), property.getValue());
		}
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Object object) {
		return wrapObject(object).getPropertyDescriptors();
	}

	public static Class<?> getPropertyType(Class<?> object, String propertyPath) {
		return new BeanWrapperImpl(object).getPropertyType(propertyPath);
	}

	public static Class<?> getPropertyType(Object object, String propertyPath) {
		return wrapObject(object).getPropertyType(propertyPath);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getPropertyValue(Object object, String propertyPath) {
	    T result = null;
	    try {
	        result = (T) wrapObject(object).getPropertyValue(propertyPath);
	    } catch (RuntimeException e) {
	        throw new RuntimeException("Error getting property '" + propertyPath + "' from " + object, e);
	    }
	    return result;
	}

	public static void initializeProperty(Object object, String propertyPath) {
		Class<?> propertyType = getPropertyType(object, propertyPath);
        try {
            setPropertyValue(object, propertyPath, propertyType.newInstance());
        } catch (InstantiationException e) {
            // just set the value to null
            setPropertyValue(object, propertyPath, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to set new instance for property: " + propertyPath, e);
        }
    }

	public static void setPropertyValue(Object object, String propertyPath, Object propertyValue) {
		wrapObject(object).setPropertyValue(propertyPath, propertyValue);
	}

	public static void setPropertyValue(Object object, String propertyPath, Object propertyValue, boolean ignoreUnknown) {
		try {
			wrapObject(object).setPropertyValue(propertyPath, propertyValue);
		}
		catch (BeansException e) {
			// only throw exception if they have indicated to not ignore unknown
			if (!ignoreUnknown) {
				throw new RuntimeException(e);
			}
			if (LOG.isTraceEnabled()) {
				LOG.trace("Ignoring exception thrown during setting of property '" + propertyPath + "': "
						+ e.getLocalizedMessage());
			}
		}
	}

    public static boolean isReadableProperty(Object object, String propertyPath) {
        return wrapObject(object).isReadableProperty(propertyPath);
    }

    public static boolean isWritableProperty(Object object, String propertyPath) {
        return wrapObject(object).isWritableProperty(propertyPath);
    }

	public static BeanWrapper wrapObject(Object object) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(object);
		beanWrapper.setAutoGrowNestedPaths(true);

        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new StringMapConverter());
        conversionService.addConverter(new StringListConverter());
        beanWrapper.setConversionService(conversionService);

		return beanWrapper;
	}

}
