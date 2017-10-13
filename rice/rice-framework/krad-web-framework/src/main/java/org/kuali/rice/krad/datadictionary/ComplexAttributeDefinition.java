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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;

/**
 * A complex attribute definition in the DataDictictionary. This can be be used to define
 * an attribute for a DataObjectEntry's attribute list which is represented by another
 * object entry definition. It will
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComplexAttributeDefinition extends AttributeDefinitionBase {

    protected DataDictionaryEntry dataObjectEntry;

    /**
     * @return the dataObjectEntry
     */
    public DataDictionaryEntry getDataObjectEntry() {
        return this.dataObjectEntry;
    }

    /**
     * @param dataObjectEntry the dataObjectEntry to set
     */
    public void setDataObjectEntry(DataDictionaryEntry dataObjectEntry) {
        this.dataObjectEntry = dataObjectEntry;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(java.lang.Class,
     *      java.lang.Class)
     */
    @Override
    public void completeValidation(Class<?> rootObjectClass, Class<?> otherObjectClass) {
        if (getDataObjectEntry() == null) {
            throw new AttributeValidationException("complex property '"
                    + getName()
                    + "' in class '"
                    + rootObjectClass.getName()
                    + " does not have a dataObjectClass defined");

        }
    }

}
