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

import org.kuali.rice.krad.datadictionary.CollectionDefinition;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntryBase;
import org.kuali.rice.krad.datadictionary.exporter.ExportMap;

/**
 * CollectionsMapBuilder
 * 
 * 
 */
@Deprecated
public class CollectionsMapBuilder {

    /**
     * Default constructor
     */
    public CollectionsMapBuilder() {
    }


    /**
     * @param entry
     * @return ExportMap containing the standard entries for the entry's CollectionsDefinition
     */
    public ExportMap buildCollectionsMap(DataDictionaryEntryBase entry) {
        ExportMap collectionsMap = new ExportMap("collections");

        for ( CollectionDefinition collection : entry.getCollections() ) {
            collectionsMap.set(buildCollectionMap(collection));
        }

        return collectionsMap;
    }

    private ExportMap buildCollectionMap(CollectionDefinition collection) {
        ExportMap collectionMap = new ExportMap(collection.getName());

        collectionMap.set("name", collection.getName());
        collectionMap.set("label", collection.getLabel());
        collectionMap.set("shortLabel", collection.getShortLabel());
        if (collection.getSummary() != null) {
            collectionMap.set("summary", collection.getSummary());
        }
        if (collection.getDescription() != null) {
            collectionMap.set("description", collection.getDescription());
        }

        return collectionMap;
    }

}
