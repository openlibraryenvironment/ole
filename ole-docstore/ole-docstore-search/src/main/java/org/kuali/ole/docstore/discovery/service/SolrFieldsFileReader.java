/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.docstore.discovery.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

public class SolrFieldsFileReader {

    private String filePath = null;
    private List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();

    /**
     * Creates SolrFieldsFileReader for a given Properties file.
     *
     * @param filePath - relative classpath for the input properties file.
     */
    public SolrFieldsFileReader(String filePath) {
        if (filePath != null) {
            this.filePath = filePath;
            Properties properties = new Properties();
            try {
                properties.load(getClass().getResourceAsStream(filePath));
                parseRecords(properties);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot Load From File: " + filePath, e);
            }
        } else {
            throw new RuntimeException("filePath Cannnot be null");
        }
    }

    /**
     * Method to parseRecords
     *
     * @param properties
     */
    private void parseRecords(Properties properties) {
        TreeSet<Object> sortedKeys = new TreeSet<Object>();
        sortedKeys.addAll(properties.keySet());
        int currentRecIndex = getCurrentRecordIndex((String) sortedKeys.first());
        int previousRecIndex = currentRecIndex;
        Map<String, Object> record = new HashMap<String, Object>();
        while (!sortedKeys.isEmpty()) {
            String key = (String) sortedKeys.pollFirst();
            currentRecIndex = getCurrentRecordIndex(key);

            if (currentRecIndex != previousRecIndex) {
                records.add(record);
                record = new HashMap<String, Object>();
            }

            String subKey = key.substring(key.indexOf('.') + 1, key.length());
            int ind2 = subKey.indexOf('.');
            if (ind2 == -1) {
                record.put(subKey, properties.getProperty(key));
            } else {
                String fieldName = subKey.substring(0, subKey.indexOf('.'));
                if (record.get(fieldName) == null) {
                    List<Object> values = new ArrayList<Object>();
                    values.add(properties.get(key));
                    record.put(fieldName, values);
                } else {
                    ((List<Object>) record.get(fieldName)).add(properties.get(key));
                }
            }
            previousRecIndex = currentRecIndex;
        }
        if (record != null && record.size() != 0) {
            records.add(record);
        }
    }

    /**
     * Method to getRecords.
     *
     * @return
     */
    public List<Map<String, Object>> getRecords() {
        return Collections.unmodifiableList(records);
    }

    private int getCurrentRecordIndex(String key) {
        return Integer.parseInt(key.substring(3, key.indexOf('.')));
    }

}
