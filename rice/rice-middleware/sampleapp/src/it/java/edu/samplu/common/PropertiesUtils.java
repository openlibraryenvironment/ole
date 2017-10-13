/**
 * Copyright 2005-2013 The Kuali Foundation
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
package edu.samplu.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class PropertiesUtils {


    public static Properties loadPropertiesWithSystemOverrides(InputStream inputStream) throws IOException {
        Properties props = PropertiesUtils.loadProperties(inputStream);
        PropertiesUtils.systemPropertiesOverride(props);
        return props;
    }

    public static Properties loadPropertiesWithSystemOverridesAndNumberedPropertiesToList(InputStream inputStream) throws IOException {
        Properties props = PropertiesUtils.loadProperties(inputStream);
        PropertiesUtils.systemPropertiesOverride(props);
        PropertiesUtils.transformNumberedPropertiesToList(props);
        return props;
    }

    public static Properties loadProperties(InputStream inputStream) throws IOException {
        Properties props = new Properties();

        if(inputStream != null) {
            props.load(inputStream);
        }

        return props;
    }

    public static String removeNumber(final String numberedKey) {
        String unnumberedKey = numberedKey;
        int firstNumberIndex = unnumberedKey.length() - 1;
        while (Character.isDigit(unnumberedKey.charAt(firstNumberIndex))) {
            firstNumberIndex--;
        }
        unnumberedKey = unnumberedKey.substring(0, firstNumberIndex + 1);

        return unnumberedKey;
    }

    public static void systemPropertiesOverride(Properties props) {
        PropertiesUtils.systemPropertiesOverride(props, null);
    }

    /**
     * -Dkey.propertyname= to override the property value for propertyname.
     * @param props properties to update with System.getProperty overrides.
     * @param key optional value that the property names will be appended to.
     */
    public static void systemPropertiesOverride(Properties props, String key) {
        Enumeration<?> names = props.propertyNames();
        Object nameObject;
        String name;
        while (names.hasMoreElements()) {

            nameObject = names.nextElement();
            if (nameObject instanceof String) {

                name = (String)nameObject;
                if (key == null || key.isEmpty()) {
                    props.setProperty(name, System.getProperty(name, props.getProperty(name)));
                } else {
                    props.setProperty(name, System.getProperty(key + "." + name, props.getProperty(name)));
                }
            }
        }
    }

    public static void transformNumberedPropertiesToList(Properties props) {
        String key = null;
        String unnumberedKey = null;
        List<String> keyList = null;
        List<String> removeKeys = new LinkedList<String>();

        // unnumber keys and place their values in a list
        Iterator keys = props.keySet().iterator();
        Map<String, List<String>> keysLists = new HashMap<String, List<String>>();
        while (keys.hasNext()) {
            key = (String)keys.next();
            if (Character.isDigit(key.charAt(key.length()-1))) {
                unnumberedKey = removeNumber(key);
                if (keysLists.get(unnumberedKey) == null) {
                    keyList = new ArrayList<String>();
                    keyList.add(props.getProperty(key));
                    keysLists.put(unnumberedKey, keyList);
                    removeKeys.add(key);
                } else {
                    keyList = keysLists.get(unnumberedKey);
                    keyList.add(props.getProperty(key));
                    keysLists.put(unnumberedKey, keyList);
                    removeKeys.add(key);
                }
            }
        }

        // remove keys that where unnumbered
        Iterator removeKey = removeKeys.iterator();
        while (removeKey.hasNext()) {
            key = (String)removeKey.next();
            props.remove(key);
        }

        // put new unnumbered key values mapped by unnumber key with an s appended to it.
        Iterator newKeys = keysLists.keySet().iterator();
        String newKey = null;
        while (newKeys.hasNext()) {
            newKey = (String)newKeys.next();
            props.put(newKey + "s", keysLists.get(newKey));
        }
    }
}
