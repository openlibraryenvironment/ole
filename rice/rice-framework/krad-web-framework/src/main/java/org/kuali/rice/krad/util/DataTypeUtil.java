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
package org.kuali.rice.krad.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.kuali.rice.krad.bo.BusinessObject;

import java.beans.PropertyDescriptor;

/**
 * Pulled this logic out of the org.kuali.rice.krad.workflow.service.impl.WorkflowAttributePropertyResolutionServiceImpl
 * since it wasn't really service logic at all, just util logic.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataTypeUtil {

	public static String determineFieldDataType(Class<? extends BusinessObject> businessObjectClass, String attributeName) {
		final Class<?> attributeClass = thieveAttributeClassFromBusinessObjectClass(businessObjectClass, attributeName);
		return determineDataType(attributeClass);
	}
	 /**
     * Determines the datatype of the given class.
     *
     * @param attributeClass the class whose datatype is to be determined.
     * @return String representation of the datatype. Defaults to string.
     */
	public static String determineDataType(Class<?> attributeClass) {
        if (isStringy(attributeClass)) return KRADConstants.DATA_TYPE_STRING; // our most common case should go first
        if (isDecimaltastic(attributeClass)) return KRADConstants.DATA_TYPE_FLOAT;
        if (isDateLike(attributeClass)) return KRADConstants.DATA_TYPE_DATE;
        if (isIntsy(attributeClass)) return KRADConstants.DATA_TYPE_LONG;
        if (isBooleanable(attributeClass)) return KRADConstants.DATA_TYPE_BOOLEAN;
        return KRADConstants.DATA_TYPE_STRING; // default to String
    }

    /**
     * Determines if the given Class is a String
     * @param clazz the class to check for Stringiness
     * @return true if the Class is a String, false otherwise
     */
	public static boolean isStringy(Class clazz) {
        return java.lang.String.class.isAssignableFrom(clazz);
    }

    /**
     * Determines if the given class is enough like a date to store values of it as a SearchableAttributeDateTimeValue
     * @param clazz the class to determine the type of
     * @return true if it is like a date, false otherwise
     */
	public static boolean isDateLike(Class clazz) {
        return java.util.Date.class.isAssignableFrom(clazz);
    }

    /**
     * Determines if the given class is enough like a Float to store values of it as a SearchableAttributeFloatValue
     * @param clazz the class to determine of the type of
     * @return true if it is like a "float", false otherwise
     */
	public static boolean isDecimaltastic(Class clazz) {
        return java.lang.Double.class.isAssignableFrom(clazz) || java.lang.Float.class.isAssignableFrom(clazz) || clazz.equals(Double.TYPE) || clazz.equals(Float.TYPE) || java.math.BigDecimal.class.isAssignableFrom(clazz) || org.kuali.rice.core.api.util.type.KualiDecimal.class.isAssignableFrom(clazz);
    }

    /**
     * Determines if the given class is enough like a "long" to store values of it as a SearchableAttributeLongValue
     * @param clazz the class to determine the type of
     * @return true if it is like a "long", false otherwise
     */
	public static boolean isIntsy(Class clazz) {
        return java.lang.Integer.class.isAssignableFrom(clazz) || java.lang.Long.class.isAssignableFrom(clazz) || java.lang.Short.class.isAssignableFrom(clazz) || java.lang.Byte.class.isAssignableFrom(clazz) || java.math.BigInteger.class.isAssignableFrom(clazz) || clazz.equals(Integer.TYPE) || clazz.equals(Long.TYPE) || clazz.equals(Short.TYPE) || clazz.equals(Byte.TYPE);
    }

    /**
     * Determines if the given class is enough like a boolean, to index it as a String "Y" or "N"
     * @param clazz the class to determine the type of
     * @return true if it is like a boolean, false otherwise
     */
	public static boolean isBooleanable(Class clazz) {
        return java.lang.Boolean.class.isAssignableFrom(clazz) || clazz.equals(Boolean.TYPE);
    }

    /**
     * Given a BusinessObject class and an attribute name, determines the class of that attribute on the BusinessObject class
     * @param boClass a class extending BusinessObject
     * @param attributeKey the name of a field on that class
     * @return the Class of the given attribute
     */
    private static Class thieveAttributeClassFromBusinessObjectClass(Class<? extends BusinessObject> boClass, String attributeKey) {
        for (PropertyDescriptor prop : PropertyUtils.getPropertyDescriptors(boClass)) {
            if (prop.getName().equals(attributeKey)) {
                return prop.getPropertyType();
            }
        }
        return null;
    }

}
