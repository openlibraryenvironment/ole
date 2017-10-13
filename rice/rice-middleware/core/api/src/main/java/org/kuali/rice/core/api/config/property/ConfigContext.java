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

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton that holds references to global engine objects.
 *
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ConfigContext {
    private static final ConcurrentHashMap<ClassLoader, Config> CONFIGS = new ConcurrentHashMap<ClassLoader, Config>();

    private ConfigContext() {
        // nothing to do here
    }

    /**
     * Perform a one-time initialization of the Config system.  This should only be performed by the applicable LifeCycle
     * implementation.
     * @param rootCfg the root config
     */
    public static void init(Config rootCfg) {
    	init(Thread.currentThread().getContextClassLoader(), rootCfg);
    }

    /**
     * Initializes the ConfigContext with the given Config and binds it to the given ClassLoader.
     */
    public static void init(ClassLoader classLoader, Config config) {
    	CONFIGS.put(classLoader, config);
    }

    public static boolean isInitialized() {
    	return !CONFIGS.isEmpty();
    }
    
    /**
     * Destroy method (mostly to aid testing, as core needs to be torn down appropriately).
     */
    public static void destroy() {
        CONFIGS.clear();
    }

    /**
     * Utility method that all code should call to obtain its appropriate Config object.
     * The Config object which is associated with the caller's context classloader will be
     * returned, being created first if it does not yet exist.
     * @return the Config object which is associated with the caller's context classloader
     */
    public static Config getCurrentContextConfig() {
    	return getConfig(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Returns the Config which is bound to the given class loader.  If no configuration is bound to the given class
     * loader this method will return null;
     *
     * @param cl the classloader whose Config to return
     * @return the Config of a particular class loader, or null if no config is bound to the given class loader
     */
    public static Config getConfig(ClassLoader cl) {
        return CONFIGS.get(cl);
    }

    /**
     * @return an immutable view of the Configs entry set
     */
    public static Set<Map.Entry<ClassLoader, Config>> getConfigs() {
        return Collections.unmodifiableSet(CONFIGS.entrySet());
    }

    /**
     * Overrides any existing Config for the classloader
     * @param cl the classloader whose Config should be overridden
     * @param config the config
     */
    public static void overrideConfig(ClassLoader cl, Config config) {
        CONFIGS.put(cl, config);
    }
}
