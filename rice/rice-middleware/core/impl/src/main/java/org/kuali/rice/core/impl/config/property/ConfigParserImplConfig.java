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
package org.kuali.rice.core.impl.config.property;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.config.property.SimpleConfig;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Old config implementation still used by PluginConfig to support old non-JAXB-compliant
 * Plugin config xml
 * @deprecated We need to retrofit PluginConfig to JAXBConfig, or eliminate along with old KEW plugin architecture
 */
@Deprecated
public class ConfigParserImplConfig extends SimpleConfig {
    private static final Logger LOG = Logger.getLogger(ConfigParserImplConfig.class);

    public ConfigParserImplConfig(String fileLoc) {
        super(fileLoc);
    }

    public ConfigParserImplConfig(Properties properties) {
        super(properties);
    }

    protected void parseWithConfigParserImpl() throws IOException {
        ConfigParserImpl cp = new ConfigParserImpl();
        Map p = new Properties();
        p.putAll(getProperties());
        cp.parse(p, fileLocs.toArray(new String[fileLocs.size()]));
        putPropertiesInPropsUsed(p, StringUtils.join(fileLocs, ", "));
    }

    protected void putPropertiesInPropsUsed(Map properties, String fileName) {
        // Properties configProperties = (Properties)config.getValue();
        Map<String, String> safeConfig = ConfigLogger.getDisplaySafeConfig(properties);
        if ( LOG.isInfoEnabled() ) {
            LOG.info("Loading properties for config " + fileName);
        }
        for (Iterator iterator2 = properties.entrySet().iterator(); iterator2.hasNext();) {
            Map.Entry configProp = (Map.Entry) iterator2.next();
            String key = (String) configProp.getKey();
            String value = (String) configProp.getValue();
            String safeValue = safeConfig.get(key);
            if ( LOG.isDebugEnabled() ) {
                LOG.debug("---->Putting config Prop " + key + "=[" + safeValue + "]");
            }
            this.propertiesUsed.put(key, value);
        }
    }

    public void parseConfig() throws IOException {
        if ( LOG.isInfoEnabled() ) {
            LOG.info("Loading Rice configs: " + StringUtils.join(fileLocs, ", "));
        }
        Map<String, Object> baseObjects = getBaseObjects();
        if (baseObjects != null) {
            this.getObjects().putAll(baseObjects);
        }
        configureBuiltIns(getProperties());
        Properties baseProperties = getBaseProperties();
        if (baseProperties != null) {
            this.getProperties().putAll(baseProperties);
        }

        parseWithConfigParserImpl();
        //parseWithHierarchicalConfigParser();

        //if (!fileLocs.isEmpty()) {
        if ( LOG.isInfoEnabled() ) {
            LOG.info("");
            LOG.info("####################################");
            LOG.info("#");
            LOG.info("# Properties used after config override/replacement");
            LOG.info("# " + StringUtils.join(fileLocs, ", "));
            LOG.info("#");
            LOG.info("####################################");
            LOG.info("");
        }
        Map<String, String> safePropsUsed = ConfigLogger.getDisplaySafeConfig(this.propertiesUsed);
        Set<Map.Entry<String,String>> entrySet = safePropsUsed.entrySet();
        // sort it for display
        SortedSet<Map.Entry<String,String>>
                sorted = new TreeSet<Map.Entry<String,String>>(new Comparator<Map.Entry<String,String>>() {
            public int compare(Map.Entry<String,String> a, Map.Entry<String,String> b) {
                return a.getKey().compareTo(b.getKey());
            }
        });
        sorted.addAll(entrySet);
        //}
        if ( LOG.isInfoEnabled() ) {
            for (Map.Entry<String, String> propUsed: sorted) {
                LOG.info("Using config Prop " + propUsed.getKey() + "=[" + propUsed.getValue() + "]");
            }
        }
    }
}