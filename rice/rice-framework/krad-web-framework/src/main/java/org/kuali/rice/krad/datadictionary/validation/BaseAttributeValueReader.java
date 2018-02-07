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
package org.kuali.rice.krad.datadictionary.validation;

import org.kuali.rice.core.framework.persistence.jdbc.sql.SQLUtils;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.util.DataTypeUtil;

import java.util.List;

/**
 * A class that implements the required accessors and legacy processing for an attribute value reader. This provides a
 * convenient base class
 * from which other attribute value readers can be derived.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BaseAttributeValueReader implements AttributeValueReader {

    protected String entryName;
    protected String attributeName;

    @Override
    public List<String> getCleanSearchableValues(String attributeKey) throws AttributeValidationException {
        Class<?> attributeType = getType(attributeKey);
        Object rawValue = getValue(attributeKey);

        String attributeInValue = rawValue != null ? rawValue.toString() : "";
        String attributeDataType = DataTypeUtil.determineDataType(attributeType);
        return SQLUtils.getCleanedSearchableValues(attributeInValue, attributeDataType);
    }

    /**
     * @return the currentName
     */
    @Override
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * @param currentName the currentName to set
     */
    @Override
    public void setAttributeName(String currentName) {
        this.attributeName = currentName;
    }

    /**
     * @return the entryName
     */
    @Override
    public String getEntryName() {
        return this.entryName;
    }

    @Override
    public abstract AttributeValueReader clone();

}
