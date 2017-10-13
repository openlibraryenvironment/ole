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
package org.kuali.rice.core.api.config.property;

import java.util.Map;

/**
 * ConfigurationService is an abstraction of system configuration for KNS clients which provides
 * methods for getting string resources, including Kuali message resource strings
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ConfigurationService {

    /**
     * Given a property name (key), returns the value associated with that key, or null if none is available.
     *
     * @param key - key for the message in application resources
     * @return String associated with the given key
     * @throws IllegalArgumentException if the key is null
     */
    public String getPropertyValueAsString(String key);

    /**
     * Given a property name (key), returns the "booleanized" value associated with that key.
     *
     * <p>
     * true, yes, on, or 1 are translated into <b>true</b> - all other values result in <b>false</b>
     * </p>
     *
     * @param key - key for the message in application resources
     * @return String associated with the given key
     * @throws IllegalArgumentException if the key is null
     */
    public boolean getPropertyValueAsBoolean(String key);

    /**
     * Returns all properties configured for the Rice instance
     *
     * @return Properties instance containing all (key,value) pairs known to the service
     */
    public Map<String, String> getAllProperties();
}
