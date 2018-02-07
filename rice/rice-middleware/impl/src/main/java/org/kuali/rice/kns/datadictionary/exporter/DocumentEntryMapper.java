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

import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kns.datadictionary.KNSDocumentEntry;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntryBase;
import org.kuali.rice.krad.datadictionary.exporter.ExportMap;

/**
 * DocumentEntryMapper
 * 
 * 
 */
@Deprecated
public abstract class DocumentEntryMapper {
    
    protected DocumentType getDocumentType(String documentTypeName) {
        return KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
    }

    /**
     * @param entry
     * @return Map containing entries for properties common to all DocumentEntry subclasses
     */
    @SuppressWarnings("unchecked")
	protected ExportMap mapEntry(KNSDocumentEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("invalid (null) entry");
        }

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
        
        entryMap.set(new AttributesMapBuilder().buildAttributesMap((DataDictionaryEntryBase) entry));
        entryMap.set(new CollectionsMapBuilder().buildCollectionsMap((DataDictionaryEntryBase) entry));

        return entryMap;
    }

}
