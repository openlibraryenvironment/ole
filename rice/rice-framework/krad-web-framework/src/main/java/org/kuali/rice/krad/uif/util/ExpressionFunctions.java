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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Defines functions that can be used in el expressions within
 * the UIF dictionary files
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExpressionFunctions {

    /**
     * Checks whether the given class parameter is assignable from the given object class
     * parameter
     *
     * @param assignableClass class to use for assignable to
     * @param objectClass class to use for assignable from
     * @return true if the object class is of type assignable class, false if not
     */
    public static boolean isAssignableFrom(Class<?> assignableClass, Class<?> objectClass) {
        return assignableClass.isAssignableFrom(objectClass);
    }

    /**
     * Checks whether the given value is null or blank string
     *
     * @param value property value to check
     * @return true if value is null or blank, false if not
     */
    public static boolean empty(Object value) {
        return (value == null) || (StringUtils.isBlank(value.toString()));
    }

    /**
     * Checks to see if the list is empty.  Throws a RuntimeException if list is not a List.
     *
     * @param value the list
     * @return true if the list is null or empty, false otherwise
     */
    public static boolean emptyList(List<?> list) {
        return (list == null) || list.isEmpty();
    }

    /**
     * Check to see if the list contains the values passed in.
     *
     * <p>In the SpringEL call values can be single item or array due to the way the EL converts values.
     * The values can be string or numeric and should match
     * the content type being stored in the list.  If the list is String and the values passed in are not string,
     * toString() conversion will be used.  Returns true if the values are in the list and both lists are non-empty,
     * false otherwise.
     * </p>
     *
     * @param list the list to be evaluated
     * @param values the values to be to check for in the list
     * @return true if all values exist in the list and both values and list are non-null/not-empty, false otherwise
     */
    public static boolean listContains(List<?> list, Object[] values) {
        if (list != null && values != null && values.length > 0 && !list.isEmpty()) {
            //conversion for if the values are non-string but the list is string (special case)
            if (list.get(0) instanceof String && !(values[0] instanceof String)) {
                String[] stringValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    stringValues[i] = values[i].toString();
                }
                return list.containsAll(Arrays.asList(stringValues));
            } else if (list.get(0) instanceof Date && values[0] instanceof String) {
                //TODO date conversion
                return false;
            } else if (!(list.get(0) instanceof String) && values[0] instanceof String) {
                //values passed in are string but the list is of objects, use object's toString method
                List<String> stringList = new ArrayList<String>();
                for (Object value : list) {
                    stringList.add(value.toString());
                }
                return stringList.containsAll(Arrays.asList(values));
            } else {
                //no conversion for if neither list is String, assume matching types (numeric case)
                return list.containsAll(Arrays.asList(values));
            }
        }

        //no cases satisfied, return false
        return false;

    }

    /**
     * Returns the name for the given class
     *
     * @param clazz class object to return name for
     * @return class name or empty string if class is null
     */
    public static String getName(Class<?> clazz) {
        if (clazz == null) {
            return "";
        } else {
            return clazz.getName();
        }
    }

    /**
     * Retrieves the value of the parameter identified with the given namespace, component, and name
     *
     * @param namespaceCode namespace code for the parameter to retrieve
     * @param componentCode component code for the parameter to retrieve
     * @param parameterName name of the parameter to retrieve
     * @return String value of parameter as a string or null if parameter does not exist
     */
    public static String getParm(String namespaceCode, String componentCode, String parameterName) {
        return CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(namespaceCode, componentCode,
                parameterName);
    }

    /**
     * Retrieves the value of the parameter identified with the given namespace, component, and name and converts
     * to a Boolean
     *
     * @param namespaceCode namespace code for the parameter to retrieve
     * @param componentCode component code for the parameter to retrieve
     * @param parameterName name of the parameter to retrieve
     * @return Boolean value of parameter as a boolean or null if parameter does not exist
     */
    public static Boolean getParmInd(String namespaceCode, String componentCode, String parameterName) {
        return CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(namespaceCode,
                componentCode, parameterName);
    }

    /**
     * Indicates whether the current user has the permission identified by the given namespace and permission name
     *
     * @param namespaceCode namespace code for the permission to check
     * @param permissionName name of the permission to check
     * @return true if the current user has the permission, false if not or the permission does not exist
     */
    public static boolean hasPerm(String namespaceCode, String permissionName) {
        Person user = GlobalVariables.getUserSession().getPerson();

        return KimApiServiceLocator.getPermissionService().hasPermission(user.getPrincipalId(), namespaceCode,
                permissionName);
    }

    /**
     * Indicates whether the current user has the permission identified by the given namespace and permission name
     * and with the given details and role qualification
     *
     * @param namespaceCode namespace code for the permission to check
     * @param permissionName name of the permission to check
     * @param permissionDetails details for the permission check
     * @param roleQualifiers qualification for assigned roles
     * @return true if the current user has the permission, false if not or the permission does not exist
     */
    public static boolean hasPermDtls(String namespaceCode, String permissionName,
            Map<String, String> permissionDetails, Map<String, String> roleQualifiers) {
        Person user = GlobalVariables.getUserSession().getPerson();

        return KimApiServiceLocator.getPermissionService().isAuthorized(user.getPrincipalId(), namespaceCode,
                permissionName, roleQualifiers);
    }

    /**
     * Indicates whether the current user has the permission identified by the given namespace and template name
     * and with the given details and role qualification
     *
     * @param namespaceCode namespace code for the permission to check
     * @param templateName name of the permission template to find permissions for
     * @param permissionDetails details for the permission check
     * @param roleQualifiers qualification for assigned roles
     * @return true if the current user has a permission with the given template, false if not or
     *         the permission does not exist
     */
    public static boolean hasPermTmpl(String namespaceCode, String templateName, Map<String, String> permissionDetails,
            Map<String, String> roleQualifiers) {
        Person user = GlobalVariables.getUserSession().getPerson();

        return KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(user.getPrincipalId(), namespaceCode,
                templateName, permissionDetails, roleQualifiers);
    }

    /**
     * Gets the next available number from a sequence
     *
     * @param sequenceName name of the sequence to retrieve from
     * @return next sequence value
     */
    public static Long sequence(String sequenceName) {
        return KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber(sequenceName);
    }

    /**
     * Get the a primary key (valid for inquiry/maintenance view retrieval) for the dataObject by class name passed in
     *
     * @param dataObjectClassName the class name to get the key for
     * @return a key valid for use as a request parameter for retrieving an inquiry or maintenance doc
     */
    public static String getDataObjectKey(String dataObjectClassName) {

        if (StringUtils.isBlank(dataObjectClassName)) {
            throw new RuntimeException("getDataObjectKey SpringEL function failed because the class name was blank");
        }

        Class dataObjectClass = null;

        try {
            dataObjectClass = Class.forName(dataObjectClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "getDataObjectKey SpringEL function failed when trying to find class " + dataObjectClassName, e);
        }

        DataObjectMetaDataService dataObjectMetaDataService = KRADServiceLocatorWeb.getDataObjectMetaDataService();

        // build list of key values from the map parameters
        List<String> pkPropertyNames = dataObjectMetaDataService.listPrimaryKeyFieldNames(dataObjectClass);

        //return first primary key found
        if (pkPropertyNames != null && !pkPropertyNames.isEmpty()) {
            return pkPropertyNames.get(0);
        }

        //this likely won't be reached, as most should have a primary key (assumption)
        KualiModuleService kualiModuleService = KRADServiceLocatorWeb.getKualiModuleService();
        ModuleService moduleService = kualiModuleService.getResponsibleModuleService(dataObjectClass);

        // some classes might have alternate keys defined for retrieving
        List<List<String>> altKeys = null;
        if (moduleService != null) {
            altKeys = moduleService.listAlternatePrimaryKeyFieldNames(dataObjectClass);
        }

        if (altKeys != null && !altKeys.isEmpty()) {
            for (List<String> list : altKeys) {
                if (list != null && !list.isEmpty()) {
                    //return any key first found
                    return list.get(0);
                }
            }
        }

        return null;
    }
}
