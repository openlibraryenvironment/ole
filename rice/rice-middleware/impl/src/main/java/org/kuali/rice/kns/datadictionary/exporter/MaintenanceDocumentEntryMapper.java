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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kns.datadictionary.MaintainableCollectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableFieldDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableItemDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableSectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableSubSectionHeaderDefinition;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.datadictionary.exporter.ExportMap;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

/**
 * MaintenanceDocumentEntryMapper
 * 
 * 
 */
@Deprecated
public class MaintenanceDocumentEntryMapper extends DocumentEntryMapper {

    /**
     * Default constructor
     */
    public MaintenanceDocumentEntryMapper() {
    }


    /**
     * @param entry
     * @return Map containing a String- and Map-based representation of the given entry
     */
    public ExportMap mapEntry(MaintenanceDocumentEntry entry) {
         // simple properties
        ExportMap entryMap = new ExportMap(entry.getJstlKey());

        Class businessRulesClass = entry.getBusinessRulesClass();
        if (businessRulesClass != null) {
            entryMap.set("businessRulesClass", businessRulesClass.getName());
        }

        entryMap.set("documentTypeName", entry.getDocumentTypeName());

        DocumentType docType = getDocumentType(entry.getDocumentTypeName());
        entryMap.set("label", docType.getLabel());

        if (docType.getDescription() != null) {
            entryMap.set("description", docType.getDescription());
        }

        DocumentHelperService documentHelperService = KNSServiceLocator.getDocumentHelperService();
        entryMap.set("documentAuthorizerClass", documentHelperService.getDocumentAuthorizer(entry.getDocumentTypeName()).getClass().getName());
        entryMap.set("documentPresentationControllerClass", documentHelperService.getDocumentPresentationController(entry.getDocumentTypeName()).getClass().getName());

        entryMap.set("allowsNoteAttachments", Boolean.toString(entry.getAllowsNoteAttachments()));

        entryMap.set("allowsNoteFYI", Boolean.toString(entry.getAllowsNoteFYI()));

        if (entry.getAttachmentTypesValuesFinderClass() != null) {
            entryMap.set("attachmentTypesValuesFinderClass", entry.getAttachmentTypesValuesFinderClass().getName());
        }

        entryMap.set("displayTopicFieldInNotes", Boolean.toString(entry.getDisplayTopicFieldInNotes()));

        entryMap.set("usePessimisticLocking", Boolean.toString(entry.getUsePessimisticLocking()));
        entryMap.set("useWorkflowPessimisticLocking", Boolean.toString(entry.getUseWorkflowPessimisticLocking()));
        entryMap.set("sessionDocument", Boolean.toString(entry.isSessionDocument()));

        entryMap.set(new AttributesMapBuilder().buildAttributesMap(entry));
        entryMap.set(new CollectionsMapBuilder().buildCollectionsMap(entry));

        // simple properties
        entryMap.set("maintenanceDocument", "true");
        entryMap.set("dataObjectClass", entry.getBusinessObjectClass().getName());
        entryMap.set("maintainableClass", entry.getMaintainableClass().getName());

        // complex properties
        entryMap.set(buildMaintainableSectionsMap(entry));

        return entryMap;
    }

    private ExportMap buildMaintainableSectionsMap(MaintenanceDocumentEntry entry) {
        ExportMap maintainableSectionsMap = new ExportMap("maintainableSections");

        int index = 0;
        for (Iterator i = entry.getMaintainableSections().iterator(); i.hasNext();) {
            MaintainableSectionDefinition section = (MaintainableSectionDefinition) i.next();

            maintainableSectionsMap.set(buildMaintainableSectionMap(section, index++));
        }

        return maintainableSectionsMap;
    }

    private ExportMap buildMaintainableSectionMap(MaintainableSectionDefinition section, int index) {
        ExportMap sectionMap = new ExportMap(Integer.toString(index));

        sectionMap.set("index", Integer.toString(index));
        sectionMap.set("title", section.getTitle());

        sectionMap.set(buildMaintainableItemsMap(section));

        return sectionMap;
    }

    private ExportMap buildMaintainableItemsMap(MaintainableSectionDefinition section) {
        ExportMap itemsMap = new ExportMap("maintainableItems");

        for (Iterator i = section.getMaintainableItems().iterator(); i.hasNext();) {
            MaintainableItemDefinition item = (MaintainableItemDefinition) i.next();
            itemsMap.set(buildMaintainableItemMap(item));
        }

        return itemsMap;
    }

    private ExportMap buildMaintainableItemMap(MaintainableItemDefinition item) {
        ExportMap itemMap = new ExportMap(item.getName());

        if (item instanceof MaintainableFieldDefinition) {
            MaintainableFieldDefinition field = (MaintainableFieldDefinition) item;

            itemMap.set("field", "true");
            itemMap.set("name", field.getName());
            itemMap.set("required", Boolean.toString(field.isRequired()));
			if (StringUtils.isNotBlank(field.getAlternateDisplayAttributeName())) {
				itemMap.set("alternateDisplayAttributeName", field.getAlternateDisplayAttributeName());
			}
			if (StringUtils.isNotBlank(field.getAdditionalDisplayAttributeName())) {
				itemMap.set("additionalDisplayAttributeName", field.getAdditionalDisplayAttributeName());
			}
        }
        else if (item instanceof MaintainableCollectionDefinition) {
            MaintainableCollectionDefinition collection = (MaintainableCollectionDefinition) item;

            itemMap.set("collection", "true");
            itemMap.set("name", collection.getName());
            itemMap.set("dataObjectClass", collection.getBusinessObjectClass().getName());
        }
        else if (item instanceof MaintainableSubSectionHeaderDefinition) {
            MaintainableSubSectionHeaderDefinition subSectionHeader = (MaintainableSubSectionHeaderDefinition) item;
            itemMap.set("name", subSectionHeader.getName());
        }
        else {
            throw new IllegalStateException("unable to create itemMap for unknown MaintainableItem subclass '" + item.getClass().getName() + "'");
        }

        return itemMap;
    }
}
