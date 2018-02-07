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
package org.kuali.rice.krad.datadictionary.exporter;

import java.util.Collections;
import java.util.Map;

/*
 * An ExportMap represents an entry or definition from the dataDictionary as a Map of the contents of that entry or definintion, and
 * the key by which that entry or definition will be stored in the parent Map.
 * 
 * 
 */
@Deprecated
public class ExportMap {
    private final String exportKey;
    private final StringMap exportData;

    public ExportMap(String exportKey) {
        this.exportKey = exportKey;
        this.exportData = new StringMap();
    }

    /**
     * @return exportKey associated with this instance
     */
    public String getExportKey() {
        return this.exportKey;
    }

    /**
     * @return unmodifiable copy of the exportData associated with this Map
     */
    public Map<String, Object> getExportData() {
        return Collections.unmodifiableMap(this.exportData);
    }

    /**
     * Adds the ExportMap's exportKey and exportData as a key,value pair to this Map
     */
    public void set(ExportMap map) {
        if (map == null) {
            throw new IllegalArgumentException("invalid (null) map");
        }

        exportData.set(map.getExportKey(), map.getExportData());
    }

    /**
     * If the given map is not null, adds the ExportMap's exportKey and exportData as a key,value pair to this Map.
     */
    public void setOptional(ExportMap map) {
        if (map != null) {
            set(map);
        }
    }

    /**
     * Adds the given key,value pair to this Map
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("invalid (null) key");
        }
        if (value == null) {
            throw new IllegalArgumentException("invalid (null) value - key=" + key);
        }

        exportData.set(key, value);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.exportKey + "(" + this.exportData.size() + " children)";
    }
}
