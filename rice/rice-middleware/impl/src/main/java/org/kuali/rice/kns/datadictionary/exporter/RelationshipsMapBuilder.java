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

import org.kuali.rice.krad.datadictionary.DataDictionaryEntryBase;
import org.kuali.rice.krad.datadictionary.PrimitiveAttributeDefinition;
import org.kuali.rice.krad.datadictionary.RelationshipDefinition;
import org.kuali.rice.krad.datadictionary.exporter.ExportMap;

/**
 * RelationshipsMapBuilder
 * 
 * 
 */
@Deprecated
public class RelationshipsMapBuilder {

    /**
     * Default constructor
     */
    public RelationshipsMapBuilder() {
    }


    /**
     * @param entry
     * @return ExportMap containing the standard entries for the entry's RelationshipDefinitions
     */
    public ExportMap buildRelationshipsMap(DataDictionaryEntryBase entry) {
        ExportMap relationshipsMap = new ExportMap("relationships");

        for ( RelationshipDefinition relationship : entry.getRelationships() ) {
            relationshipsMap.set(buildRelationshipMap(relationship));
        }

        return relationshipsMap;
    }

    private ExportMap buildRelationshipMap(RelationshipDefinition relationship) {
        ExportMap relationshipMap = new ExportMap(relationship.getObjectAttributeName());

        ExportMap attributesMap = new ExportMap("primitiveAttributes");

        int count = 0;
        for (PrimitiveAttributeDefinition primitiveAttributeDefinition : relationship.getPrimitiveAttributes()) {
            ExportMap attributeMap = new ExportMap(Integer.toString(count++));
            attributeMap.set("sourceName", primitiveAttributeDefinition.getSourceName());
            attributeMap.set("targetName", primitiveAttributeDefinition.getTargetName());

            attributesMap.set(attributeMap);
        }

        relationshipMap.set(attributesMap);

        return relationshipMap;
    }

}
