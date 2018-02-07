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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for generating JavaScript
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ScriptUtils {

    /**
     * Translates an Object to a String for representing the given Object as
     * a JavaScript value
     *
     * <p>
     * Handles null, List, Map, and Set collections, along with non quoting for numeric and
     * boolean types. Complex types are treated as a String value using toString
     * </p>
     *
     * @param value Object instance to translate
     * @return JS value
     */
    public static String translateValue(Object value) {
        String jsValue = "";

        if (value == null) {
            jsValue = "null";
            return jsValue;
        }

        if (value instanceof List) {
            jsValue = "[";

            List<Object> list = (List<Object>) value;
            for (Object listItem : list) {
                jsValue += translateValue(listItem);
                jsValue += ",";
            }
            jsValue = StringUtils.removeEnd(jsValue, ",");

            jsValue += "]";
        } else if (value instanceof Set) {
            jsValue = "[";

            Set<Object> set = (Set<Object>) value;
            for (Object setItem : set) {
                jsValue += translateValue(setItem);
                jsValue += ",";
            }
            jsValue = StringUtils.removeEnd(jsValue, ",");

            jsValue += "]";
        } else if (value instanceof Map) {
            jsValue = "{";

            Map<Object, Object> map = (Map<Object, Object>) value;
            for (Map.Entry<Object, Object> mapEntry : map.entrySet()) {
                jsValue += "\"" + mapEntry.getKey().toString() + "\":";
                jsValue += translateValue(mapEntry.getValue());
                jsValue += ",";
            }
            jsValue = StringUtils.removeEnd(jsValue, ",");

            jsValue += "}";
        } else {
            Class<?> valueClass = value.getClass();
            if (TypeUtils.isSimpleType(valueClass) || TypeUtils.isClassClass(valueClass)) {
                boolean quoteValue = true;

                if (TypeUtils.isBooleanClass(valueClass) ||
                        TypeUtils.isDecimalClass(valueClass) ||
                        TypeUtils.isIntegralClass(valueClass)) {
                    quoteValue = false;
                }

                if (quoteValue) {
                    jsValue = "\"";
                }

                // TODO: should this go through property editors?
                jsValue += value.toString();

                if (quoteValue) {
                    jsValue += "\"";
                }
            } else {
                // treat as data object
                jsValue = "{";

                PropertyDescriptor[] propertyDescriptors = ObjectPropertyUtils.getPropertyDescriptors(value);
                for (int i = 0; i < propertyDescriptors.length; i++) {
                    PropertyDescriptor descriptor = propertyDescriptors[i];
                    if ((descriptor.getReadMethod() != null) && !"class".equals(descriptor.getName())) {
                        Object propertyValue = ObjectPropertyUtils.getPropertyValue(value, descriptor.getName());

                        jsValue += descriptor.getName() + ":";
                        jsValue += translateValue(propertyValue);
                        jsValue += ",";
                    }
                }
                jsValue = StringUtils.removeEnd(jsValue, ",");

                jsValue += "}";
            }
        }

        return jsValue;
    }

    /**
     * Builds a JSON string form the given map
     *
     * @param map map to translate
     * @return String in JSON format
     */
    public static String toJSON(Map<String, String> map) {
        StringBuffer sb = new StringBuffer("{");

        for (String key : map.keySet()) {
            String optionValue = map.get(key);
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append("\"" + key + "\"");

            sb.append(":");
            sb.append("\"" + escapeJSONString(optionValue) + "\"");
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Escapes double quotes present in the given string
     *
     * @param jsonString string to escape
     * @return escaped string
     */
    public static String escapeJSONString(String jsonString) {
        if (jsonString != null) {
            jsonString = jsonString.replace("\"", "&quot;");
        }

        return jsonString;
    }

    /**
     * Converts a map of string values to a json equivalent by converting the string values through the
     * convertToJsValue(String) method; this will output a string representation of the map
     * in json format
     *
     * @param stringMap the map of String values to convert to a simple json object representation
     * @return the json object as a string (for use in js)
     */
    public static String convertToJsValue(Map<String,String> stringMap){
        if (stringMap == null || stringMap.isEmpty()){
            return "{}";
        }

        String convertedValue = "{";
        for (String key: stringMap.keySet()){
            convertedValue = convertedValue + "\"" + key + "\":" + convertToJsValue(stringMap.get(key)) + ",";
        }

        convertedValue = StringUtils.removeEnd(convertedValue, ",");
        convertedValue = convertedValue + "}";

        return convertedValue;
    }

    /**
     * Convert a string to a javascript value - especially for use for options used to initialize widgets such as the
     * tree and rich table
     *
     * @param value the string to be converted
     * @return the converted value
     */
    public static String convertToJsValue(String value) {

        // save input value to preserve any whitespace formatting
        String originalValue = value;

        // remove whitespace for correct string matching
        value = StringUtils.strip(value);

        // If an option value starts with { or [, it would be a nested value
        // and it should not use quotes around it
        if (StringUtils.startsWith(value, "{") || StringUtils.startsWith(value, "[")) {
            return originalValue;
        }
        // need to be the base boolean value "false" is true in js - a non
        // empty string
        else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) {
            return originalValue;
        }
        // if it is a call back function, do not add the quotes
        else if (StringUtils.startsWith(value, "function") && StringUtils.endsWith(value, "}")) {
            return originalValue;
        }
        // for numerics
        else if (NumberUtils.isNumber(value)) {
            return originalValue;
        } else {
            // use single quotes since hidden scripts are placed in
            // the value attribute which surrounds the script with double quotes
            return "'" + originalValue + "'";
        }
    }

    /**
     * Escapes the ' character present in collection names so it can be properly used in js without causing
     * javascript errors due to an early completion of a ' string.
     *
     * @param name
     * @return
     */
    public static String escapeName(String name) {
        name = name.replace("'", "\\'");
        return name;
    }

    /**
     * Converts a list of string to a valid js string array
     *
     * @param list list of Strings to be converted
     * @return String representing the js array
     */
    public static String convertStringListToJsArray(List<String> list) {
        String array = "[";

        if (list != null) {
            for (String s : list) {
                array = array + "'" + s + "',";
            }
            array = StringUtils.removeEnd(array, ",");
        }
        array = array + "]";

        return array;
    }

    /**
     * escapes a string using {@link org.apache.commons.lang.StringEscapeUtils#escapeHtml(String)}
     *
     * <p>The apostrophe character is included as <code>StringEscapeUtils#escapeHtml(String)</code>
     * does not consider it a legal entity. </p>
     *
     * @param string the string to be escaped
     * @return the escaped string - useful for embedding in server side generated JS scripts
     */
    public static String escapeHtml(String string) {
        if (string == null) {
            return null;
        } else {
            return StringEscapeUtils.escapeHtml(string).replace("'", "&apos;").replace("&quot;", "\\u0022");
        }
    }

    /**
     * escape an array of strings
     *
     * @param strings an array of strings to escape
     * @return the array, with the strings escaped
     */
    public static List<String> escapeHtml(List<String> strings) {
        if (strings == null) {
            return null;
        } else if (strings.isEmpty()) {
            return strings;
        } else {
            List<String> result = new ArrayList<String>(strings.size());
            for (String string : strings) {
                result.add(escapeHtml(string));
            }
            return result;
        }
    }

    /**
     * Will append the second script parameter to the first if the first is not empty, also checks to see if the
     * first script needs an end semi-colon added
     *
     * @param script script that will be added to (null is allowed and converted to empty string)
     * @param appendScript script to append
     * @return String result of appending the two script parameters
     */
    public static String appendScript(String script, String appendScript) {
        String appendedScript = script;
        if (appendedScript == null) {
            appendedScript = "";
        } else if (StringUtils.isNotBlank(appendedScript) && !appendedScript.trim().endsWith(";")) {
            appendedScript += "; ";
        }

        appendedScript += appendScript;

        return appendedScript;
    }

    /**
     * Helper method to build a JS string that will invoke the given function with the given arguments
     * 
     * @param functionName name of the JS function to invoke
     * @param arguments zero or more arguments to pass, each will be converted to the corresponding JS type
     * @return JS String for invoking the function
     */
    public static String buildFunctionCall(String functionName, Object... arguments) {
        StringBuffer sb = new StringBuffer(functionName).append("(");

        if (arguments != null) {
            List<String> jsArguments = new ArrayList<String>();
            for (Object argument : arguments) {
                jsArguments.add(translateValue(argument));
            }

            sb.append(StringUtils.join(jsArguments, ","));
        }

        sb.append(");");

        return sb.toString();
    }

    /**
     * Builds the JavaScript string for binding the given script to the component with the given id
     * for the given event name (using jQuery)
     *
     * @param id id of the element to handle the event for
     * @param eventName name of the event the script will handle
     * @param eventScript script to be executed when the event is thrown, if blank an empty string will
     * be returned
     * @return JS event handler script
     */
    public static String buildEventHandlerScript(String id, String eventName, String eventScript) {
        if (StringUtils.isBlank(eventScript)) {
            return "";
        }

        Assert.hasLength(id, "Id is required for building event handler script");
        Assert.hasLength(eventName, "Event name is required for building event handler script");

        StringBuffer sb = new StringBuffer();

        sb.append("jQuery('#" + id + "').on('");
        sb.append(eventName);
        sb.append("', function(e) {");
        sb.append(eventScript);
        sb.append("}); ");

        return sb.toString();
    }
}
