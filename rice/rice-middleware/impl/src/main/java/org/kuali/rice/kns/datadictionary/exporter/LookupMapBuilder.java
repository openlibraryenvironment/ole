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
package org.kuali.rice.kns.datadictionary.exporter;

import java.util.Iterator;

import org.kuali.rice.kns.datadictionary.FieldDefinition;
import org.kuali.rice.kns.datadictionary.LookupDefinition;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.SortDefinition;
import org.kuali.rice.krad.datadictionary.exporter.ExportMap;

/**
 * LookupMapBuilder
 *
 *
 */
@Deprecated
public class LookupMapBuilder {

    /**
     * Default constructor
     */
    public LookupMapBuilder() {
    }


    /**
     * @param entry
     * @return ExportMap containing the standard entries for the given entry's LookupDefinition, or null if the given entry has no
     *         lookupDefinition
     */
    public ExportMap buildLookupMap(BusinessObjectEntry entry) {
        ExportMap lookupMap = null;

        if (entry.hasLookupDefinition()) {
            LookupDefinition lookupDefinition = entry.getLookupDefinition();
            lookupMap = new ExportMap("lookup");

            // simple properties
            if (lookupDefinition.getLookupableID() != null) {
                lookupMap.set("lookupableID", lookupDefinition.getLookupableID());
            }

            lookupMap.set("title", lookupDefinition.getTitle());

            if (lookupDefinition.hasMenubar()) {
                lookupMap.set("menubar", lookupDefinition.getMenubar());
            }

            if (lookupDefinition.hasResultSetLimit()) {
                lookupMap.set("resultSetLimit", lookupDefinition.getResultSetLimit().toString());
            }
            // complex properties
            lookupMap.setOptional(buildDefaultSortMap(lookupDefinition));
            lookupMap.set(buildLookupFieldsMap(lookupDefinition));
            lookupMap.set(buildResultFieldsMap(lookupDefinition));
        }

        return lookupMap;
    }

    private ExportMap buildDefaultSortMap(LookupDefinition lookupDefinition) {
        ExportMap defaultSortMap = null;

        if (lookupDefinition.hasDefaultSort()) {
            SortDefinition defaultSortDefinition = lookupDefinition.getDefaultSort();
            defaultSortMap = new ExportMap("defaultSort");

            defaultSortMap.set("sortAscending", Boolean.toString(defaultSortDefinition.getSortAscending()));
            defaultSortMap.set(buildSortAttributesMap(defaultSortDefinition));
        }

        return defaultSortMap;
    }

    private ExportMap buildSortAttributesMap(SortDefinition sortDefinition) {
        ExportMap sortAttributesMap = new ExportMap("sortAttributes");

        for (Iterator i = sortDefinition.getAttributeNames().iterator(); i.hasNext();) {
            String attributeName = (String) i.next();

            ExportMap attributeMap = new ExportMap(attributeName);
            attributeMap.set("attributeName", attributeName);

            sortAttributesMap.set(attributeMap);
        }

        return sortAttributesMap;
    }

    private ExportMap buildLookupFieldsMap(LookupDefinition lookupDefinition) {
        ExportMap lookupFieldsMap = new ExportMap("lookupFields");

        for (Iterator i = lookupDefinition.getLookupFields().iterator(); i.hasNext();) {
            FieldDefinition lookupField = (FieldDefinition) i.next();
            lookupFieldsMap.set(buildLookupFieldMap(lookupField));
        }

        return lookupFieldsMap;
    }

    private ExportMap buildLookupFieldMap(FieldDefinition lookupField) {
        ExportMap lookupFieldMap = new ExportMap(lookupField.getAttributeName());

        lookupFieldMap.set("attributeName", lookupField.getAttributeName());
        lookupFieldMap.set("required", Boolean.toString(lookupField.isRequired()));

        return lookupFieldMap;
    }

    private ExportMap buildResultFieldsMap(LookupDefinition lookupDefinition) {
        ExportMap resultFieldsMap = new ExportMap("resultFields");

        for (Iterator i = lookupDefinition.getResultFields().iterator(); i.hasNext();) {
            FieldDefinition resultField = (FieldDefinition) i.next();
            resultFieldsMap.set(MapperUtils.buildFieldMap(resultField));
        }

        return resultFieldsMap;
    }

}
