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

import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.exporter.ExportMap;

/**
 * BusinessObjectEntryMapper
 */
@Deprecated
public class BusinessObjectEntryMapper {

    /**
     * Default constructor
     */
    public BusinessObjectEntryMapper() {
    }


    /**
     * @param entry
     * @return Map containing a String- and Map-based representation of the given entry
     */
    public ExportMap mapEntry(BusinessObjectEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("invalid (null) entry");
        }

        ExportMap entryMap = new ExportMap(entry.getJstlKey());
        
        // simple properties
        entryMap.set("dataObjectClass", entry.getBusinessObjectClass().getName());
        if (entry.getExporterClass() != null) {
        	entryMap.set("exporterClass", entry.getExporterClass().getName());
        }
        final String objectLabel = entry.getObjectLabel();
        if (objectLabel != null) {
            entryMap.set("objectLabel", objectLabel);
        }
        final String objectDescription = entry.getObjectDescription();
        if (objectDescription != null) {
            entryMap.set("objectDescription", objectDescription);
        }

        // complex properties
        entryMap.setOptional(new InquiryMapBuilder().buildInquiryMap(entry));
        entryMap.setOptional(new LookupMapBuilder().buildLookupMap(entry));
        entryMap.set(new AttributesMapBuilder().buildAttributesMap(entry));
        entryMap.set(new CollectionsMapBuilder().buildCollectionsMap(entry));
        entryMap.set(new RelationshipsMapBuilder().buildRelationshipsMap(entry));

        return entryMap;
    }
}
