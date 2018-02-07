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
package org.kuali.rice.kns.service.impl;

import java.util.Collection;

import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kns.service.TransactionalDocumentDictionaryService;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.datadictionary.TransactionalDocumentEntry;
import org.kuali.rice.krad.document.TransactionalDocument;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.service.DataDictionaryService;

/**
 * This class is the service implementation for the TransactionalDocumentDictionary structure. Defines the API for the interacting
 * with Document-related entries in the data dictionary. This is the default implementation that gets delivered with Kuali.
 */
@Deprecated
public class TransactionalDocumentDictionaryServiceImpl implements TransactionalDocumentDictionaryService {
    private DataDictionaryService dataDictionaryService;

    /**
     * @see org.kuali.rice.kns.service.TransactionalDocumentDictionaryService#getAllowsCopy(org.kuali.bo.TransactionalDocument)
     */
    public Boolean getAllowsCopy(TransactionalDocument document) {
        Boolean allowsCopy = null;

        TransactionalDocumentEntry entry = getTransactionalDocumentEntry(document);
        if (entry != null) {
            allowsCopy = Boolean.valueOf(entry.getAllowsCopy());
        }

        return allowsCopy;
    }

    /**
     * @see org.kuali.rice.kns.service.TransactionalDocumentDictionaryService#getDocumentClassByName(java.lang.String)
     */
    public Class getDocumentClassByName(String documentTypeName) {
        Class documentClass = null;

        TransactionalDocumentEntry entry = getTransactionalDocumentEntryBydocumentTypeName(documentTypeName);
        if (entry != null) {
            documentClass = entry.getDocumentClass();
        }

        return documentClass;
    }

    /**
     * @see org.kuali.rice.kns.service.TransactionalDocumentDictionaryService#getDescription(org.kuali.bo.TransactionalDocument)
     */
    public String getDescription(String transactionalDocumentTypeName) {
        String description = null;

        DocumentType docType = getDocumentType(transactionalDocumentTypeName);
        if (docType != null) {
            description = docType.getDescription();
        }

        return description;
    }

    /**
     * @see org.kuali.rice.kns.service.TransactionalDocumentDictionaryService#getDescription(org.kuali.bo.TransactionalDocument)
     */
    public String getLabel(String transactionalDocumentTypeName) {
        String label = null;

        DocumentType docType = getDocumentType(transactionalDocumentTypeName);
        if (docType != null) {
            label = docType.getLabel();
        }

        return label;
    }


    /**
     * Sets the data dictionary instance.
     * 
     * @param dataDictionaryService
     */
    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    /**
     * Retrieves the data dictionary instance.
     * 
     * @return
     */
    public DataDictionary getDataDictionary() {
        return this.dataDictionaryService.getDataDictionary();
    }

    /**
     * This method gets the workflow document type for the given documentTypeName
     * 
     * @param documentTypeName
     * @return
     */
    protected DocumentType getDocumentType(String documentTypeName) {
        return KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
    }

    /**
     * Retrieves the document entry by transactional document class instance.
     * 
     * @param document
     * @return TransactionalDocumentEntry
     */
    private TransactionalDocumentEntry getTransactionalDocumentEntry(TransactionalDocument document) {
        if (document == null) {
            throw new IllegalArgumentException("invalid (null) document");
        }

        TransactionalDocumentEntry entry = (TransactionalDocumentEntry)getDataDictionary().getDocumentEntry(document.getClass().getName());

        return entry;
    }

    /**
     * Retrieves the document entry by transactional document type name.
     * 
     * @param documentTypeName
     * @return
     */
    private TransactionalDocumentEntry getTransactionalDocumentEntryBydocumentTypeName(String documentTypeName) {
        if (documentTypeName == null) {
            throw new IllegalArgumentException("invalid (null) document type name");
        }

        TransactionalDocumentEntry entry = (TransactionalDocumentEntry) getDataDictionary().getDocumentEntry(documentTypeName);

        return entry;
    }

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kns.service.TransactionalDocumentDictionaryService#getDefaultExistenceChecks(java.lang.String)
	 */
	public Collection getDefaultExistenceChecks(String docTypeName) {
        Collection defaultExistenceChecks = null;

        TransactionalDocumentEntry entry = getTransactionalDocumentEntryBydocumentTypeName(docTypeName);
        if (entry != null) {
            defaultExistenceChecks = entry.getDefaultExistenceChecks();
        }

        return defaultExistenceChecks;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kns.service.TransactionalDocumentDictionaryService#getDefaultExistenceChecks(org.kuali.rice.krad.document.TransactionalDocument)
	 */
	public Collection getDefaultExistenceChecks(TransactionalDocument document) {
		return getDefaultExistenceChecks(getTransactionalDocumentEntry(document).getDocumentTypeName());
	}
}
