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

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.core.api.util.Truth;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.web.format.BooleanFormatter;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.messages.Message;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Image;
import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.uif.field.ActionField;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.field.ImageField;
import org.kuali.rice.krad.uif.field.LinkField;
import org.kuali.rice.krad.uif.field.MessageField;
import org.kuali.rice.krad.uif.field.SpaceField;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Miscellaneous Utility Methods
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KRADUtils {
    private static KualiModuleService kualiModuleService;

    private static final KualiDecimal ONE_HUNDRED = new KualiDecimal("100.00");

    /**
     * Prevent instantiation of the class.
     */
    private KRADUtils() {
        throw new UnsupportedOperationException("do not call");
    }

    /**
     * Retrieve the title for a business object class
     *
     * <p>
     * The title is a nicely formatted version of the simple class name.
     * </p>
     *
     * @param clazz business object class
     * @return title of the business object class
     */
    public final static String getBusinessTitleForClass(Class<? extends Object> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException(
                    "The getBusinessTitleForClass method of KRADUtils requires a non-null class");
        }
        String className = clazz.getSimpleName();

        StringBuffer label = new StringBuffer(className.substring(0, 1));
        for (int i = 1; i < className.length(); i++) {
            if (Character.isLowerCase(className.charAt(i))) {
                label.append(className.charAt(i));
            } else {
                label.append(" ").append(className.charAt(i));
            }
        }
        return label.toString().trim();
    }

    /**
     * Picks off the filename from the full path
     *
     * <p>
     * The different OS path separators are being taken into consideration.
     * </p>
     *
     * @param fullFileNames file name with path
     * @return file name
     */
    public final static List<String> getFileNameFromPath(List<String> fullFileNames) {
        List<String> fileNameList = new ArrayList<String>();

        for (String fullFileName : fullFileNames) {
            if (StringUtils.contains(fullFileName, "/")) {
                fileNameList.add(StringUtils.substringAfterLast(fullFileName, "/"));
            } else {
                fileNameList.add(StringUtils.substringAfterLast(fullFileName, "\\"));
            }
        }

        return fileNameList;
    }

    /**
     * Convert the given money amount into an integer string.
     *
     * <p>
     * Since the return string cannot have decimal point, multiplies the amount by 100 so the decimal places
     * are not lost, for example, 320.15 is converted into 32015.
     * </p>
     *
     * @param decimalNumber decimal number to be converted
     * @return an integer string of the given money amount through multiplying by 100 and removing the fraction
     *         portion.
     */
    public final static String convertDecimalIntoInteger(KualiDecimal decimalNumber) {
        KualiDecimal decimalAmount = decimalNumber.multiply(ONE_HUNDRED);
        NumberFormat formatter = NumberFormat.getIntegerInstance();
        String formattedAmount = formatter.format(decimalAmount);

        return StringUtils.replace(formattedAmount, ",", "");
    }

    /**
     * Return the integer value of a string
     *
     * <p>
     * If the string contains a decimal value everything after the decimal point is dropped.
     * </p>
     *
     * @param numberStr string
     * @return integer representation of the given string
     */
    public static Integer getIntegerValue(String numberStr) {
        Integer numberInt = null;
        try {
            numberInt = new Integer(numberStr);
        } catch (NumberFormatException nfe) {
            Double numberDbl = new Double(numberStr);
            numberInt = new Integer(numberDbl.intValue());
        }
        return numberInt;
    }

    /**
     * Attempt to coerce a String attribute value to the given propertyType.  If the transformation can't be made,
     * either because the propertyType is null or because the transformation required exceeds this method's very small
     * bag of tricks, then null is returned.
     *
     * @param propertyType the Class to coerce the attributeValue to
     * @param attributeValue the String value to coerce
     * @return an instance of the propertyType class, or null the transformation can't be made.
     */
    public static Object hydrateAttributeValue(Class<?> propertyType, String attributeValue) {
        Object attributeValueObject = null;
        if (propertyType != null && attributeValue != null) {
            if (String.class.equals(propertyType)) {
                // it's already a String
                attributeValueObject = attributeValue;
            } // KULRICE-6808: Kim Role Maintenance - Custom boolean role qualifier values are not being converted properly
            else if (Boolean.class.equals(propertyType) || Boolean.TYPE.equals(propertyType)) {
                attributeValueObject = Truth.strToBooleanIgnoreCase(attributeValue);
            } else {
                // try to create one with KRADUtils for other misc data types
                attributeValueObject = KRADUtils.createObject(propertyType, new Class[]{String.class},
                        new Object[]{attributeValue});
                // if that didn't work, we'll get a null back
            }
        }
        return attributeValueObject;
    }

    public static Object createObject(Class<?> clazz, Class<?>[] argumentClasses, Object[] argumentValues) {
        if (clazz == null) {
            return null;
        }
        if (argumentClasses.length == 1 && argumentClasses[0] == String.class) {
            if (argumentValues.length == 1 && argumentValues[0] != null) {
                if (clazz == String.class) {
                    // this means we're trying to create a String from a String
                    // don't new up Strings, it's a bad idea
                    return argumentValues[0];
                } else {
                    // maybe it's a type that supports valueOf?
                    Method valueOfMethod = null;
                    try {
                        valueOfMethod = clazz.getMethod("valueOf", String.class);
                    } catch (NoSuchMethodException e) {
                        // ignored
                    }
                    if (valueOfMethod != null) {
                        try {
                            return valueOfMethod.invoke(null, argumentValues[0]);
                        } catch (Exception e) {
                            // ignored
                        }
                    }
                }
            }
        }
        try {
            Constructor<?> constructor = clazz.getConstructor(argumentClasses);
            return constructor.newInstance(argumentValues);
        } catch (Exception e) {
            // ignored
        }
        return null;
    }

    /**
     * Creates a comma separated String representation of the given list.
     *
     * <p>
     * For example 'a','b',c'.
     * </p>
     *
     * @param list
     * @return the joined String, empty if the list is null or has no elements
     */
    public static String joinWithQuotes(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }

        return KRADConstants.SINGLE_QUOTE +
                StringUtils.join(list.iterator(), KRADConstants.SINGLE_QUOTE + "," + KRADConstants.SINGLE_QUOTE) +
                KRADConstants.SINGLE_QUOTE;
    }

    private static KualiModuleService getKualiModuleService() {
        if (kualiModuleService == null) {
            kualiModuleService = KRADServiceLocatorWeb.getKualiModuleService();
        }
        return kualiModuleService;
    }

    /**
     * TODO this method will probably need to be exposed in a public KRADUtils class as it is used
     * by several different modules.  That will have to wait until ModuleService and KualiModuleService are moved
     * to core though.
     */
    public static String getNamespaceCode(Class<? extends Object> clazz) {
        ModuleService moduleService = getKualiModuleService().getResponsibleModuleService(clazz);
        if (moduleService == null) {
            return KRADConstants.DEFAULT_NAMESPACE;
        }
        return moduleService.getModuleConfiguration().getNamespaceCode();
    }

    public static Map<String, String> getNamespaceAndComponentSimpleName(Class<? extends Object> clazz) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(KRADConstants.NAMESPACE_CODE, getNamespaceCode(clazz));
        map.put(KRADConstants.COMPONENT_NAME, getComponentSimpleName(clazz));
        return map;
    }

    public static Map<String, String> getNamespaceAndComponentFullName(Class<? extends Object> clazz) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(KRADConstants.NAMESPACE_CODE, getNamespaceCode(clazz));
        map.put(KRADConstants.COMPONENT_NAME, getComponentFullName(clazz));
        return map;
    }

    public static Map<String, String> getNamespaceAndActionClass(Class<? extends Object> clazz) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(KRADConstants.NAMESPACE_CODE, getNamespaceCode(clazz));
        map.put(KRADConstants.ACTION_CLASS, clazz.getName());
        return map;
    }

    private static String getComponentSimpleName(Class<? extends Object> clazz) {
        return clazz.getSimpleName();
    }

    private static String getComponentFullName(Class<? extends Object> clazz) {
        return clazz.getName();
    }

    /**
     * Parses a string that is in map format (commas separating map entries, colon separates
     * map key/value) to a new map instance
     *
     * @param parameter - string parameter to parse
     * @return Map<String, String> instance populated from string parameter
     */
    public static Map<String, String> convertStringParameterToMap(String parameter) {
        Map<String, String> map = new HashMap<String, String>();

        if (StringUtils.isNotBlank(parameter)) {
            if (StringUtils.contains(parameter, ",")) {
                String[] fieldConversions = StringUtils.split(parameter, ",");

                for (int i = 0; i < fieldConversions.length; i++) {
                    String fieldConversionStr = fieldConversions[i];
                    if (StringUtils.isNotBlank(fieldConversionStr)) {
                        if (StringUtils.contains(fieldConversionStr, ":")) {
                            String[] fieldConversion = StringUtils.split(fieldConversionStr, ":");
                            map.put(fieldConversion[0], fieldConversion[1]);
                        } else {
                            map.put(fieldConversionStr, fieldConversionStr);
                        }
                    }
                }
            } else if (StringUtils.contains(parameter, ":")) {
                String[] fieldConversion = StringUtils.split(parameter, ":");
                map.put(fieldConversion[0], fieldConversion[1]);
            } else {
                map.put(parameter, parameter);
            }
        }

        return map;
    }

    /**
     * Parses a string that is in list format (commas separating list entries) to a new List instance
     *
     * @param parameter - string parameter to parse
     * @return List<String> instance populated from string parameter
     */
    public static List<String> convertStringParameterToList(String parameter) {
        List<String> list = new ArrayList<String>();

        if (StringUtils.isNotBlank(parameter)) {
            if (StringUtils.contains(parameter, ",")) {
                String[] parameters = StringUtils.split(parameter, ",");
                List arraysList = Arrays.asList(parameters);
                list.addAll(arraysList);
            } else {
                list.add(parameter);
            }
        }

        return list;
    }

    /**
     * Translates characters in the given string like brackets that will cause
     * problems with binding to characters that do not affect the binding
     *
     * @param key - string to translate
     * @return String translated string
     */
    public static String translateToMapSafeKey(String key) {
        String safeKey = key;

        safeKey = StringUtils.replace(safeKey, "[", "_");
        safeKey = StringUtils.replace(safeKey, "]", "_");

        return safeKey;
    }

    /**
     * Builds a string from the given map by joining each entry with a comma and
     * each key/value pair with a colon
     *
     * @param map - map instance to build string for
     * @return String of map entries
     */
    public static String buildMapParameterString(Map<String, String> map) {
        String parameterString = "";

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isNotBlank(parameterString)) {
                parameterString += ",";
            }

            parameterString += entry.getKey() + ":" + entry.getValue();
        }

        return parameterString;
    }

    /**
     * Parses the given string into a Map by splitting on the comma to get the
     * map entries and within each entry splitting by colon to get the key/value
     * pairs
     *
     * @param parameterString - string to parse into map
     * @return Map<String, String> map from string
     */
    public static Map<String, String> getMapFromParameterString(String parameterString) {
        Map<String, String> map = new HashMap<String, String>();

        String[] entries = parameterString.split(",");
        for (int i = 0; i < entries.length; i++) {
            String[] keyValue = entries[i].split(":");
            if (keyValue.length != 2) {
                throw new RuntimeException("malformed field conversion pair: " + Arrays.toString(keyValue));
            }

            map.put(keyValue[0], keyValue[1]);
        }

        return map;
    }

    /**
     * Retrieves value for the given parameter name in the request and attempts to convert to a Boolean using
     * the <code>BooleanFormatter</code>
     *
     * @param request - servlet request containing parameters
     * @param parameterName - name of parameter to retrieve value for
     * @return Boolean set to value of parameter, or null if parameter was not found in request
     */
    public static Boolean getRequestParameterAsBoolean(ServletRequest request, String parameterName) {
        Boolean parameterValue = null;

        String parameterValueStr = request.getParameter(parameterName);
        if (StringUtils.isNotBlank(parameterValueStr)) {
            parameterValue = (Boolean) new BooleanFormatter().convertFromPresentationFormat(parameterValueStr);
        }

        return parameterValue;
    }

    /**
     * Translates the given Map of String keys and String array values to a Map
     * of String key and values. If the String array contains more than one
     * value, the single string is built by joining the values with the vertical
     * bar character
     *
     * @param requestParameters - Map of request parameters to translate
     * @return Map<String, String> translated Map
     */
    public static Map<String, String> translateRequestParameterMap(Map<String, String[]> requestParameters) {
        Map<String, String> parameters = new HashMap<String, String>();

        for (Map.Entry<String, String[]> parameter : requestParameters.entrySet()) {
            String parameterValue = "";
            if (parameter.getValue().length > 1) {
                parameterValue = StringUtils.join(parameter.getValue(), "|");
            } else {
                parameterValue = parameter.getValue()[0];
            }
            parameters.put(parameter.getKey(), parameterValue);
        }

        return parameters;
    }

    /**
     * Retrieves parameter values from the request that match the requested
     * names. In addition, based on the object class an authorization check is
     * performed to determine if the values are secure and should be decrypted.
     * If true, the value is decrypted before returning
     *
     * @param parameterNames - names of the parameters whose values should be retrieved
     * from the request
     * @param parentObjectClass - object class that contains the parameter names as properties
     * and should be consulted for security checks
     * @param requestParameters - all request parameters to pull from
     * @return Map<String, String> populated with parameter name/value pairs
     *         pulled from the request
     */
    public static Map<String, String> getParametersFromRequest(List<String> parameterNames, Class<?> parentObjectClass,
            Map<String, String> requestParameters) {
        Map<String, String> parameterValues = new HashMap<String, String>();

        for (Iterator<String> iter = parameterNames.iterator(); iter.hasNext(); ) {
            String keyPropertyName = iter.next();

            if (requestParameters.get(keyPropertyName) != null) {
                String keyValue = requestParameters.get(keyPropertyName);

                // Check if this element was encrypted, if it was decrypt it
                if (KRADServiceLocatorWeb.getDataObjectAuthorizationService()
                        .attributeValueNeedsToBeEncryptedOnFormsAndLinks(parentObjectClass, keyPropertyName)) {
                    try {
                        keyValue = StringUtils.removeEnd(keyValue, EncryptionService.ENCRYPTION_POST_PREFIX);
                        keyValue = CoreApiServiceLocator.getEncryptionService().decrypt(keyValue);
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    }
                }

                parameterValues.put(keyPropertyName, keyValue);
            }
        }

        return parameterValues;
    }

    /**
     * Builds a Map containing a key/value pair for each property given in the property names list, general
     * security is checked to determine if the value needs to be encrypted along with applying formatting to
     * the value
     *
     * @param propertyNames - list of property names to get key/value pairs for
     * @param dataObject - object instance containing the properties for which the values will be pulled
     * @return Map<String, String> containing entry for each property name with the property name as the map key
     *         and the property value as the value
     */
    public static Map<String, String> getPropertyKeyValuesFromDataObject(List<String> propertyNames,
            Object dataObject) {
        return getPropertyKeyValuesFromDataObject(propertyNames, Collections.<String>emptyList(), dataObject);
    }

    /**
     * Builds a Map containing a key/value pair for each property given in the property names list, general
     * security is checked to determine if the value needs to be encrypted along with applying formatting to
     * the value
     *
     * @param propertyNames - list of property names to get key/value pairs for
     * @param securePropertyNames - list of secure property names to match for encryption
     * @param dataObject - object instance containing the properties for which the values will be pulled
     * @return Map<String, String> containing entry for each property name with the property name as the map key
     * and the property value as the value
     */
    public static Map<String, String> getPropertyKeyValuesFromDataObject(List<String> propertyNames,
            List<String> securePropertyNames, Object dataObject) {
        Map<String, String> propertyKeyValues = new HashMap<String, String>();

        if (dataObject == null) {
            return propertyKeyValues;
        }

        // iterate through properties and add a map entry for each
        for (String propertyName : propertyNames) {
            Object propertyValue = ObjectPropertyUtils.getPropertyValue(dataObject, propertyName);
            if (propertyValue == null) {
                propertyValue = StringUtils.EMPTY;
            }

            // secure values are not returned
            if (!isSecure(propertyName, securePropertyNames, dataObject, propertyValue)) {
                propertyKeyValues.put(propertyName, propertyValue.toString());
            }

        }

        return propertyKeyValues;
    }

    /**
     * Determines whether a property name should be secured, either based on installed sensitive data patterns, a list
     * of secure property name patterns, or attributes in the Data Dictionary.
     *
     * @param propertyName The property name to check for security
     * @param securePropertyNames The secure property name patterns to check
     * @param dataObject The object containing this property
     * @param propertyValue The value of the property
     * @return true if the property needs to be secure, false otherwise
     */
    private static boolean isSecure(String propertyName, List<String> securePropertyNames, Object dataObject, Object propertyValue) {
        if (propertyValue instanceof String && containsSensitiveDataPatternMatch((String) propertyValue)) {
            return true;
        }

        if (containsSecurePropertyName(propertyName, securePropertyNames)) {
            return true;
        }

        return KRADServiceLocatorWeb.getDataObjectAuthorizationService()
                .attributeValueNeedsToBeEncryptedOnFormsAndLinks(dataObject.getClass(), propertyName);
    }

    /**
     * Helper method to identify if propertyName contains a secure property name element.
     * Check handles simple or compound names and ignores partial matches.
     *
     * @param propertyName property name as a single term or compound term (i.e. items[0].propertyName)
     * @param securePropertyNames list of secure property names to match
     * @return true if any of the secure property names are found in the property name, false otherwise
     */
    private static boolean containsSecurePropertyName(String propertyName, List<String> securePropertyNames) {
        if (securePropertyNames == null) {
            return false;
        }

        for (String securePropertyName : securePropertyNames) {
            // pattern prefix and suffix used to handle compound names and ignore partial name matches
            if (Pattern.compile("(?:\\.|^)" + Pattern.quote(securePropertyName) + "(?:\\.|\\[|$)").matcher(propertyName)
                    .find()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Utility method to convert a Map to a Properties object
     *
     * @param parameters - map to convert
     * @return Properties object containing all the map entries
     */
    public static Properties convertMapToProperties(Map<String, String> parameters) {
        Properties properties = new Properties();

        if (parameters != null) {
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                properties.put(parameter.getKey(), parameter.getValue());
            }
        }

        return properties;
    }

    /**
     * Utility method to convert a Request Parameters Map to a Properties object
     *
     * <p>
     * Multiple values for a parameter are joined together with comma delimiter
     * </p>
     *
     * @param requestParameters - map to convert
     * @return Properties object containing all the map entries
     */
    public static Properties convertRequestMapToProperties(Map<String, String[]> requestParameters) {
        Properties properties = new Properties();

        if (requestParameters != null) {
            for (Map.Entry<String, String[]> parameter : requestParameters.entrySet()) {
                String[] parameterValue = parameter.getValue();
                String parameterValueString = StringUtils.join(parameterValue, ",");

                properties.put(parameter.getKey(), parameterValueString);
            }
        }

        return properties;
    }

    /**
     * Check if data might be sensitive
     *
     * <p>
     * The sensitivity of the data is checked by matching it against the sensitive data patterns that are specified
     * in the system parameter table.
     * </p>
     *
     * @param fieldValue data to be checked for sensitivity
     * @return true if the data matches the sensitive data pattern, false otherwise
     */
    public static boolean containsSensitiveDataPatternMatch(String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return false;
        }

        ParameterService parameterService = CoreFrameworkServiceLocator.getParameterService();
        Collection<String> sensitiveDataPatterns = parameterService.getParameterValuesAsString(
                KRADConstants.KNS_NAMESPACE, ParameterConstants.ALL_COMPONENT,
                KRADConstants.SystemGroupParameterNames.SENSITIVE_DATA_PATTERNS);

        for (String pattern : sensitiveDataPatterns) {
            if (Pattern.compile(pattern).matcher(fieldValue).find()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Strips out common patterns used in cross side scripting
     *
     * @param value string to strip patterns from
     * @return cleaned string
     */
    public static String stripXSSPatterns(String value) {
        if (value == null) {
            return null;
        }

        // Avoid null characters
        value = value.replaceAll("", "");

        // Avoid anything between script tags
        Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        // Avoid anything in a src='...' type of expression
        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        // Remove any lonesome </script> tag
        scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        // Remove any lonesome <script ...> tag
        scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        // Avoid eval(...) expressions
        scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        // Avoid expression(...) expressions
        scriptPattern = Pattern.compile("expression\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        // Avoid javascript:... expressions
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        // Avoid vbscript:... expressions
        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        // Avoid onload= expressions
        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        return value;
    }

    /**
     * Gets the UserSession object from the HttpServletRequest object's
     * associated session.
     *
     * <p>
     * In some cases (different threads) the UserSession cannot be retrieved
     * from GlobalVariables but can still be accessed via the session object
     * </p>
     */
    public static final UserSession getUserSessionFromRequest(HttpServletRequest request) {
        return (UserSession) request.getSession().getAttribute(KRADConstants.USER_SESSION_KEY);
    }

    /**
     * Check if current deployment is the production environment
     *
     * @return true if the deploy environment is production, false otherwise
     */
    public static boolean isProductionEnvironment() {
        return CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.PROD_ENVIRONMENT_CODE_KEY).equalsIgnoreCase(
                CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                        KRADConstants.ENVIRONMENT_KEY));
    }

    /**
     * Gets the message associated with ErrorMessage object passed in, using message service.
     * The prefix and suffix will be appended to the retrieved message if processPrefixSuffix is true and if those
     * settings are set on the ErrorMessage passed in.
     *
     * @param errorMessage the ErrorMessage object containing the message key(s)
     * @param processPrefixSuffix if true appends the prefix and suffix to the message if they exist on ErrorMessage
     * @return the converted/retrieved message
     */
    public static String getMessageText(ErrorMessage errorMessage, boolean processPrefixSuffix) {
        String message = "";
        if (errorMessage != null && errorMessage.getErrorKey() != null) {
            MessageService messageService = KRADServiceLocatorWeb.getMessageService();

            // find message by key
            message = messageService.getMessageText(errorMessage.getNamespaceCode(), errorMessage.getComponentCode(),
                    errorMessage.getErrorKey());
            if (message == null) {
                message = "Intended message with key: " + errorMessage.getErrorKey() + " not found.";
            }

            if (errorMessage.getMessageParameters() != null && StringUtils.isNotBlank(message)) {
                message = message.replace("'", "''");
                message = MessageFormat.format(message, (Object[]) errorMessage.getMessageParameters());
            }

            // add prefix
            if (StringUtils.isNotBlank(errorMessage.getMessagePrefixKey()) && processPrefixSuffix) {
                String prefix = messageService.getMessageText(errorMessage.getNamespaceCode(),
                        errorMessage.getComponentCode(), errorMessage.getMessagePrefixKey());

                if (errorMessage.getMessagePrefixParameters() != null && StringUtils.isNotBlank(prefix)) {
                    prefix = prefix.replace("'", "''");
                    prefix = MessageFormat.format(prefix, (Object[]) errorMessage.getMessagePrefixParameters());
                }

                if (StringUtils.isNotBlank(prefix)) {
                    message = prefix + " " + message;
                }
            }

            // add suffix
            if (StringUtils.isNotBlank(errorMessage.getMessageSuffixKey()) && processPrefixSuffix) {
                String suffix = messageService.getMessageText(errorMessage.getNamespaceCode(),
                        errorMessage.getComponentCode(), errorMessage.getMessageSuffixKey());

                if (errorMessage.getMessageSuffixParameters() != null && StringUtils.isNotBlank(suffix)) {
                    suffix = suffix.replace("'", "''");
                    suffix = MessageFormat.format(suffix, (Object[]) errorMessage.getMessageSuffixParameters());
                }

                if (StringUtils.isNotBlank(suffix)) {
                    message = message + " " + suffix;
                }
            }
        }

        return message;
    }

    /**
     * Generate the request parameter portion of the url based on the map of key value pairs passed in
     *
     * @param requestParameters the request parameters to use in the string
     * @return a request parameter string starting with "?" with "&" separators, or blank if the mapped passed in is
     *         blank
     */
    public static String getRequestStringFromMap(Map<String, String> requestParameters) {
        String requestString = "";

        if (requestParameters.isEmpty()) {
            return requestString;
        }

        URLCodec urlCodec = new URLCodec(KRADConstants.DEFAULT_ENCODING);

        for (String key : requestParameters.keySet()) {
            String value = null;
            try {
                value = urlCodec.encode(requestParameters.get(key));
            } catch (EncoderException e) {
                throw new RuntimeException("Unable to encode parameter name or value: " + key + "=" + value, e);
            }

            if (StringUtils.isNotBlank(requestString)) {
                requestString = requestString + "&";
            }

            requestString = requestString + key + "=" + value;
        }

        return "?" + requestString;
    }

    /**
     * Helper method for building a URL that will invoke the given controller and render the given
     * KRAD view
     *
     * @param baseUrl base url (domain, port)
     * @param controllerMapping mapping for the controller that should be invoked
     * @param viewId id for the view that should be rendered
     * @return url for invoking the view
     */
    public static String buildViewUrl(String baseUrl, String controllerMapping, String viewId) {
        Assert.hasLength(baseUrl, "base url is null or empty");
        Assert.hasLength(controllerMapping, "controller mapping is null or empty");
        Assert.hasLength(viewId, "view id is null or empty");

        StringBuffer url = new StringBuffer();

        url.append(baseUrl);

        if (!baseUrl.endsWith("/")) {
            url.append("/");
        }

        url.append(controllerMapping);

        url.append("?");
        url.append(UifParameters.VIEW_ID);
        url.append("=");
        url.append(viewId);

        return url.toString();
    }

    /**
     * Removes parameters from the given properties object that are request specific (useful when manupulating the
     * current URL to invoke something else)
     *
     * @param requestParameters properties instance containing the parameters to clean
     */
    public static void cleanRequestParameters(Properties requestParameters) {
        requestParameters.remove(UifParameters.SESSION_ID);
        requestParameters.remove(UifParameters.AJAX_REQUEST);
        requestParameters.remove(UifParameters.AJAX_RETURN_TYPE);
        requestParameters.remove(UifParameters.FORM_KEY);
        requestParameters.remove(UifParameters.JUMP_TO_ID);
        requestParameters.remove(UifParameters.FOCUS_ID);
    }

    /**
     * Get the full url for a request (requestURL + queryString)
     *
     * @param request the request
     * @return the fullUrl
     */
    public static String getFullURL(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    /**
     * Attempts to generate a unique view title by combining the View's headerText with the title attribute for the
     * dataObjectClass found through the DataObjectMetaDataService.  If the title attribute cannot be found, just the
     * headerText is returned.
     *
     * @param form the form
     * @param view the view
     * @return the headerText with the title attribute in parenthesis or just the headerText if it title attribute
     *         cannot be determined
     */
    public static String generateUniqueViewTitle(UifFormBase form, View view) {
        String title = view.getHeader().getHeaderText();

        String viewLabelPropertyName = "";

        Class<?> dataObjectClass;
        if (StringUtils.isNotBlank(view.getDefaultBindingObjectPath())) {
            dataObjectClass = ObjectPropertyUtils.getPropertyType(form, view.getDefaultBindingObjectPath());
        } else {
            dataObjectClass = view.getFormClass();
        }

        DataObjectMetaDataService mds = KRADServiceLocatorWeb.getDataObjectMetaDataService();
        if (dataObjectClass != null) {
            viewLabelPropertyName = mds.getTitleAttribute(dataObjectClass);
        }

        String viewLabelPropertyPath = "";
        if (StringUtils.isNotBlank(viewLabelPropertyName)) {
            // adjust binding prefix
            if (!viewLabelPropertyName.startsWith(UifConstants.NO_BIND_ADJUST_PREFIX)) {
                if (StringUtils.isNotBlank(view.getDefaultBindingObjectPath())) {
                    viewLabelPropertyPath = view.getDefaultBindingObjectPath() + "." + viewLabelPropertyName;
                }
            } else {
                viewLabelPropertyPath = StringUtils.removeStart(viewLabelPropertyName,
                        UifConstants.NO_BIND_ADJUST_PREFIX);
            }
        } else {
            // attempt to get title attribute
            if (StringUtils.isNotBlank(view.getDefaultBindingObjectPath())) {
                dataObjectClass = ViewModelUtils.getObjectClassForMetadata(view, form,
                        view.getDefaultBindingObjectPath());
            } else {
                dataObjectClass = view.getFormClass();
            }

            if (dataObjectClass != null) {
                String titleAttribute = mds.getTitleAttribute(dataObjectClass);
                if (StringUtils.isNotBlank(titleAttribute)) {
                    viewLabelPropertyPath = view.getDefaultBindingObjectPath() + "." + titleAttribute;
                }
            }
        }

        Object viewLabelPropertyValue = null;
        if (StringUtils.isNotBlank(viewLabelPropertyPath) && ObjectPropertyUtils.isReadableProperty(form,
                viewLabelPropertyPath)) {
            viewLabelPropertyValue = ObjectPropertyUtils.getPropertyValue(form, viewLabelPropertyPath);
        }

        if (viewLabelPropertyValue != null && StringUtils.isNotBlank(viewLabelPropertyValue.toString()) && StringUtils
                .isNotBlank(title)) {
            return title + " (" + viewLabelPropertyValue.toString() + ")";
        } else {
            return title;
        }
    }

    /**
     * Attempts to extract a string value out of the field passed in, varies depending on field type
     *
     * <p>If the field is a dataField, it will use its propertyName to retrieve a value, otherwise it will try to
     * retrieve textual content out of various component types.  If the field is a FieldGroup, only the first
     * component's determined value will be used.  This function is used for sorting.</p>
     *
     * @param model the current model
     * @param field the field to get a value from
     * @return the field's String value, false if it cant be determined
     */
    public static String getSimpleFieldValue(Object model, Field field) {
        if (field == null) {
            return null;
        }

        String value = null;
        // check for what type of field this is
        if (field instanceof DataField) {
            String propertyPath = ((DataField) field).getBindingInfo().getBindingPath();
            Object valueObject = null;

            if (field.isHidden()) {
                return "";
            }

            // check if readable
            if (ObjectPropertyUtils.isReadableProperty(model, propertyPath)) {
                valueObject = ObjectPropertyUtils.getPropertyValue(model, propertyPath);
            }

            // use object's string value
            if (valueObject != null && !((DataField) field).isApplyMask()) {
                value = valueObject.toString();
            } else if (valueObject != null && ((DataField) field).isApplyMask()) {
                value = ((DataField) field).getMaskFormatter().maskValue(valueObject);
            }
        } else if (field instanceof ActionField) {
            value = ((ActionField) field).getActionLabel();

            // use image alt text if any
            if (StringUtils.isBlank(value) && ((ActionField) field).getActionImage() != null) {
                value = ((ActionField) field).getActionImage().getAltText();
            }
        } else if (field instanceof LinkField) {
            value = ((LinkField) field).getLinkText();
        } else if (field instanceof ImageField) {
            value = ((ImageField) field).getAltText();
        } else if (field instanceof MessageField && ((MessageField) field).getMessage() != null) {
            value = ((MessageField) field).getMessage().getMessageText();
        } else if (field instanceof SpaceField) {
            value = "";
        } else if (field instanceof FieldGroup
                && ((FieldGroup) field).getGroup() != null
                && ((FieldGroup) field).getGroup().getItems() != null
                && !((FieldGroup) field).getGroup().getItems().isEmpty()) {
            // using first components type for assumed value
            Component firstComponent = ((FieldGroup) field).getGroup().getItems().get(0);

            // check first component type to extract value
            if (firstComponent != null && firstComponent instanceof Field) {
                value = getSimpleFieldValue(model, field);
            } else if (firstComponent instanceof Action && StringUtils.isNotBlank(
                    ((Action) firstComponent).getActionLabel())) {
                value = ((Action) firstComponent).getActionLabel();
            } else if (firstComponent instanceof Action && ((Action) firstComponent).getActionImage() != null) {
                value = ((Action) firstComponent).getActionImage().getAltText();
            } else if (firstComponent instanceof Link) {
                value = ((Link) firstComponent).getLinkText();
            } else if (firstComponent instanceof Image) {
                value = ((Image) firstComponent).getAltText();
            } else if (firstComponent instanceof org.kuali.rice.krad.uif.element.Message) {
                value = ((org.kuali.rice.krad.uif.element.Message) firstComponent).getMessageText();
            } else {
                value = null;
            }
        }

        return value;
    }

    /**
     * Helper method to change common characters into HTML attribute safe characters
     *
     * @param message the string to convert
     * @return the converted string with quotes, sing quotes, and slash replaced
     */
    public static String convertToHTMLAttributeSafeString(String message) {
        if (StringUtils.isBlank(message)) {
            return message;
        }

        if (message.contains("\"")) {
            message = message.replace("\"", "&quot;");
        }
        if (message.contains("'")) {
            message = message.replace("'", "&#39;");
        }
        if (message.contains("\\")) {
            message = message.replace("\\", "&#92;");
        }

        return message;
    }

    /**
     * Get the rowCss for the line specified, by evaluating the conditionalRowCssClasses map for that row
     *
     * @param conditionalRowCssClasses the conditionalRowCssClass map, where key is the condition and value is
     * the class(es)
     * @param lineIndex the line/row index
     * @param isOdd true if the row is considered odd
     * @param lineContext the lineContext for expressions, pass null if not applicable
     * @param expressionEvaluator the expressionEvaluator, pass null if not applicable
     * @return row csss class String for the class attribute of this row
     */
    public static String generateRowCssClassString(Map<String, String> conditionalRowCssClasses, int lineIndex,
            boolean isOdd, Map<String, Object> lineContext, ExpressionEvaluator expressionEvaluator) {
        String rowCss = "";
        if (conditionalRowCssClasses == null || conditionalRowCssClasses.isEmpty()) {
            return rowCss;
        }

        for (String cssRule : conditionalRowCssClasses.keySet()) {
            if (cssRule.startsWith(UifConstants.EL_PLACEHOLDER_PREFIX) && lineContext != null &&
                    expressionEvaluator != null) {
                String outcome = expressionEvaluator.evaluateExpressionTemplate(lineContext, cssRule);
                if (outcome != null && Boolean.parseBoolean(outcome)) {
                    rowCss = rowCss + " " + conditionalRowCssClasses.get(cssRule);
                }
            } else if (cssRule.equals(UifConstants.RowSelection.ALL)) {
                rowCss = rowCss + " " + conditionalRowCssClasses.get(cssRule);
            } else if (cssRule.equals(UifConstants.RowSelection.EVEN) && !isOdd) {
                rowCss = rowCss + " " + conditionalRowCssClasses.get(cssRule);
            } else if (cssRule.equals(UifConstants.RowSelection.ODD) && isOdd) {
                rowCss = rowCss + " " + conditionalRowCssClasses.get(cssRule);
            } else if (StringUtils.isNumeric(cssRule) && (lineIndex + 1) == Integer.parseInt(cssRule)) {
                rowCss = rowCss + " " + conditionalRowCssClasses.get(cssRule);
            }
        }

        rowCss = StringUtils.removeStart(rowCss, " ");

        return rowCss;
    }
}
