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
package org.kuali.rice.krad.datadictionary.validation.result;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntryValidationResult {

    private String entryName;
    private Map<String, AttributeValidationResult> attributeValidationResultMap;

    public EntryValidationResult(String entryName) {
        this.entryName = entryName;
        this.attributeValidationResultMap = new LinkedHashMap<String, AttributeValidationResult>();
    }

    public Iterator<AttributeValidationResult> iterator() {
        return attributeValidationResultMap.values().iterator();
    }

    protected AttributeValidationResult getAttributeValidationResult(String attributeName) {
        AttributeValidationResult attributeValidationResult = attributeValidationResultMap.get(attributeName);
        if (attributeValidationResult == null) {
            attributeValidationResult = new AttributeValidationResult(attributeName);
            attributeValidationResultMap.put(attributeName, attributeValidationResult);
        }
        return attributeValidationResult;
    }

}
