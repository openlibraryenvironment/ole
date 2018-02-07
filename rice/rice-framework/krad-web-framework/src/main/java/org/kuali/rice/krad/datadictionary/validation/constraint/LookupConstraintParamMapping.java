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
package org.kuali.rice.krad.datadictionary.validation.constraint;

import java.util.List;

/**
 * This class is a direct copy of one that was in Kuali Student. Look up constraints are currently not implemented.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
public class LookupConstraintParamMapping {
    protected String fieldPath;
    protected String paramKey;
    protected List<String> defaultValueList;
    protected String defaultValueString;

    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public List<String> getDefaultValueList() {
        return defaultValueList;
    }

    public void setDefaultValueList(List<String> defaultValueList) {
        this.defaultValueList = defaultValueList;
    }

    public String getDefaultValueString() {
        return defaultValueString;
    }

    public void setDefaultValueString(String defaultValueString) {
        this.defaultValueString = defaultValueString;
    }
}