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
package org.kuali.ole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/8/11
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */

public class OleDocStoreData {
    private String category;
    private List<String> doctypes = new ArrayList<String>();
    private List<String> formats = new ArrayList<String>();
    private Map<String, List<String>> typeFormatMap = new HashMap<String, List<String>>();
    private Map<String, Integer> formatLevelsMap = new HashMap<String, Integer>();
    private Map<String, Map<String, Long>> typeFormatMapWithNodeCount = new HashMap<String, Map<String, Long>>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, List<String>> getTypeFormatMap() {
        return typeFormatMap;
    }

    public List<String> getDoctypes() {
        return doctypes;
    }

    public void setDoctypes(List<String> doctypes) {
        this.doctypes = doctypes;
    }

    public void addDocType(String docType) {
        if (!this.doctypes.contains(docType)) {
            this.doctypes.add(docType);
        }
    }

    public void addFormat(String docType, String format) {
        List<String> formatsList = null;
        if (!this.typeFormatMap.containsKey(docType)) {
            this.typeFormatMap.put(docType, new ArrayList<String>());
        }

        formatsList = this.typeFormatMap.get(docType);

        if (!formatsList.contains(format)) {
            formatsList.add(format);
            formats.add(format);
        }
        this.typeFormatMap.put(docType, formatsList);
    }

    public void addLevelInfoForFormat(String format, Integer levels) {
        formatLevelsMap.put(format, levels);
    }

    public Map<String, Map<String, Long>> getTypeFormatMapWithNodeCount() {
        return typeFormatMapWithNodeCount;
    }

    public void setTypeFormatMapWithNodeCount(Map<String, Map<String, Long>> typeFormatMapWithNodeCount) {
        this.typeFormatMapWithNodeCount = typeFormatMapWithNodeCount;
    }

    public Map<String, Integer> getFormatLevelsMap() {
        return formatLevelsMap;
    }

    public void setFormatLevelsMap(Map<String, Integer> formatLevelsMap) {
        this.formatLevelsMap = formatLevelsMap;
    }

    public List<String> getFormats() {
        return formats;
    }
}
