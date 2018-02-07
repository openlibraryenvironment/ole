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
package org.kuali.rice.core.framework.config.property;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.util.RiceUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Base config implementation that does not support file parsing.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimpleConfig extends AbstractBaseConfig {

    private static final Logger LOG = Logger.getLogger(SimpleConfig.class);

    protected Map<String, Object> configs = new LinkedHashMap<String, Object>();

    protected final List<String> fileLocs;

    protected Properties propertiesUsed = new Properties();

    private Map<String, Object> objects = new LinkedHashMap<String, Object>();

    private Properties baseProperties = new Properties();

    private Map<String, Object> baseObjects = new HashMap<String, Object>();

    public SimpleConfig() {
        this.fileLocs = new ArrayList<String>();
    }

    public SimpleConfig(String fileLoc) {
       this(Collections.singletonList(fileLoc));
    }

    public SimpleConfig(List<String> fileLocs) {
        this.fileLocs = fileLocs;
    }

    public SimpleConfig(Properties properties) {
        this(new ArrayList<String>(), properties);
    }

    public SimpleConfig(String fileLoc, Properties baseProperties) {
        this(Collections.singletonList(fileLoc), baseProperties);
    }

    public SimpleConfig(List<String> fileLocs, Properties baseProperties) {
        this(fileLocs);
        this.baseProperties = baseProperties;
    }


    public void parseConfig() throws IOException {
        throw new UnsupportedOperationException("Parsing is no longer supported by BaseConfig, please see JAXBConfigImpl instead.");
    }

    /**
     * Configures built-in properties.
     */
    protected void configureBuiltIns(Properties properties) {
        properties.put("host.ip", RiceUtilities.getIpNumber());
        properties.put("host.name", RiceUtilities.getHostName());
    }

    public Map<String, Object> getBaseObjects() {
        return this.baseObjects;
    }

    public Properties getBaseProperties() {
        return this.baseProperties;
    }

    public Properties getProperties() {
        return this.propertiesUsed;
    }

    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public Map<String, Object> getObjects() {
        return this.objects;
    }

    public Object getObject(String key) {
        return getObjects().get(key);
    }

    public void putProperties(Properties properties) {
        if (properties != null) {
            getProperties().putAll(properties);
        }
    }

    public void putProperty(String key, String value) {
        this.getProperties().put(key, value);
    }

    public void putObject(String key, Object value) {
        this.getObjects().put(key, value);
    }

    public void putObjects(Map<String, Object> objects) {
        if(objects != null){
            this.getObjects().putAll(objects);
        }
    }

    public void removeObject(String key){
        this.getObjects().remove(key);
    }

    public void removeProperty(String key){
        this.getProperties().remove(key);
    }

    public void putConfig(Config config) {
        putProperties(config.getProperties());
        putObjects(config.getObjects());
    }

    public String toString() {
        return new ToStringBuilder(this).append("fileLocs", fileLocs).toString();
    }
}