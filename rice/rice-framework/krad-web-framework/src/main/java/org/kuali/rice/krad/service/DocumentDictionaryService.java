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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.document.DocumentAuthorizer;
import org.kuali.rice.krad.document.DocumentPresentationController;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.rules.rule.BusinessRule;

import java.util.Collection;
import java.util.List;

/**
 * Defines methods that a <code>DocumentEntry</code> Service must provide, and the API for the interacting
 * with Document-related entries in the data dictionary
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentDictionaryService {

    /**
     * Retrieves the label for the document as described in its data dictionary entry
     *
     * @param documentTypeName - document type name for the document entry to retrieve label for
     * @return String document label
     */
    public String getLabel(String documentTypeName);

    /**
     * Retrieves the configured document type name for the maintenance document
     * entry associated with the given data object class
     *
     * @param dataObjectClass - data object class for maintenance entry to retrieve
     * @return String document type name for maintenance document
     */
    public String getMaintenanceDocumentTypeName(Class dataObjectClass);

    /**
     * Retrieves the full description of the document as described in its data dictionary entry
     *
     * @param documentTypeName - document type name for the document entry to retrieve description for
     * @return String documents full description
     */
    public String getDescription(String documentTypeName);

    /**
     * Retrieves the collection of ReferenceDefinition objects defined as DefaultExistenceChecks
     * for the MaintenanceDocument associated with the given data object class
     *
     * @param dataObjectClass - data object class for maintenance document
     * @return Collection reference definitions for default existence checks
     */
    public Collection getDefaultExistenceChecks(Class dataObjectClass);

    /**
     * Retrieves the collection of ReferenceDefinition objects defined as DefaultExistenceChecks
     * for the document instance
     *
     * @param document - document instance to pull document type for associated document entry
     * @return Collection reference definitions for default existence checks
     */
    public Collection getDefaultExistenceChecks(Document document);

    /**
     * Retrieves the collection of ReferenceDefinition objects defined as DefaultExistenceChecks
     * for the document entry associated with the given document type name
     *
     * @param docTypeName - document type name for document entry to pull existence checks for
     * @return Collection reference definitions for default existence checks
     */
    public Collection getDefaultExistenceChecks(String docTypeName);

    /**
     * Retrieves the data object class configured for the maintenance entry
     * associated with the given document type name
     *
     * @param docTypeName - document type name associated with maintenance document entry
     * @return Class<?> data object class associated with maintenance document entry
     */
    public Class<?> getMaintenanceDataObjectClass(String docTypeName);

    /**
     * Retrieves the maintainable class instance that is configured in the maintenance document
     * entry associated with the given document type name
     *
     * @param docTypeName - document type name to retrieve maintainable for
     * @return Class<? extends Maintainable> maintainable class for document type name
     */
    public Class<? extends Maintainable> getMaintainableClass(String docTypeName);

    /**
     * Retrieves the configured business rule class configured for the document entry
     * that is associated with the document type of the given document instance
     *
     * @param document - document instance to retrieve rule class for
     * @return Class<? extends BusinessRule> businessRulesClass associated with the given document type
     */
    public Class<? extends BusinessRule> getBusinessRulesClass(Document document);

    /**
     * Returns whether or not this document's data dictionary file has flagged it to allow document copies
     *
     * @param document - document instance to check copy flag for
     * @return boolean true if copies are allowed, false otherwise
     */
    public Boolean getAllowsCopy(Document document);

    /**
     * Returns whether or not this document's data dictionary file has flagged it to allow maintenance new
     * or copy actions
     *
     * @param docTypeName - document type name to retrieve maintenance document entry for
     * @return boolean true if new or copy maintenance actions are allowed
     */
    public Boolean getAllowsNewOrCopy(String docTypeName);

    /**
     * Retrieves the document entry that is associated with the given document type name
     *
     * @param docTypeName - document type name to retrieve document entry for
     * @return DocumentEntry instance associated with document type
     */
    public DocumentEntry getDocumentEntry(String docTypeName);

    /**
     * Retrieves the document entry that is associated with the given document class
     *
     * @param documentClass - document class to retrieve document entry for
     * @return DocumentEntry instance associated with document class
     */
    public DocumentEntry getDocumentEntryByClass(Class<? extends Document> documentClass);

    /**
     * Retrieves the maintenance document entry that is associated with the given document type name
     *
     * @param docTypeName - document type name to retrieve maintenance document entry for
     * @return MaintenanceDocumentEntry instance associated with document type
     */
    public MaintenanceDocumentEntry getMaintenanceDocumentEntry(String docTypeName);

    /**
     * Retrieves the document class configured on the document entry associated with the
     * given document type name
     *
     * @param documentTypeName - document type name to retrieve class for
     * @return Class<?> document class associated with document type name
     */
    public Class<?> getDocumentClassByName(String documentTypeName);

    /**
     * Retrieves the document type configured on the document entry associated with the
     * given document class
     *
     * @param documentClass - class for document to retrieve the document type for
     * @return String document type associated with document type name
     */
    public String getDocumentTypeByClass(Class<? extends Document> documentClass);

    /**
     * Indicates whether the given data object class is configured to allow record deletions
     *
     * @param dataObjectClass - class for the data object to check
     * @return Boolean true if record deletion is allowed, false if not allowed, null if not configured
     */
    public Boolean getAllowsRecordDeletion(Class dataObjectClass);

    /**
     * Indicates whether the given maintenance document is configured to allow record deletions
     *
     * @param document - maintenance document instance to check
     * @return Boolean true if record deletion is allowed, false if not allowed, null if not configured
     */
    public Boolean getAllowsRecordDeletion(MaintenanceDocument document);

    /**
     * Retrieves the list of property names that are configured as locking keys for the maintenance
     * document entry associated with the given document type name
     *
     * @param docTypeName - document type name to retrieve maintenance document entry for
     * @return List<String> list of locking key property names
     */
    public List<String> getLockingKeys(String docTypeName);

    /**
     * Indicates whether the configured locking keys for a class should be cleared on a maintenance
     * copy action or values carried forward
     *
     * @param dataObjectClass - class for the data object to check
     * @return boolean true if locking keys should be copied, false if they should be cleared
     */
    public boolean getPreserveLockingKeysOnCopy(Class dataObjectClass);

    /**
     * Retrieves the {@link DocumentAuthorizer} configured on the document entry with the given document type
     * name
     *
     * @param documentType - document type name to retrieve document entry and associated authorizer for
     * @return DocumentAuthorizer authorizer instance
     */
    public DocumentAuthorizer getDocumentAuthorizer(String documentType);

    /**
     * Retrieves the {@link DocumentAuthorizer} configured on the document entry for the document type associated
     * with the document instance
     *
     * @param document - document instance to retrieve document entry and associated authorizer for
     * @return DocumentAuthorizer authorizer instance
     */
    public DocumentAuthorizer getDocumentAuthorizer(Document document);

    /**
     * Retrieves the {@link DocumentPresentationController} configured on the document entry with the given document
     * type name
     *
     * @param documentType - document type name to retrieve document entry and associated presentation controller for
     * @return DocumentPresentationController instance
     */
    public DocumentPresentationController getDocumentPresentationController(String documentType);

    /**
     * Retrieves the {@link DocumentPresentationController} configured on the document entry for the document type
     * associated with the document instance
     *
     * @param document - document instance to retrieve document entry and associated presentation controller for
     * @return DocumentPresentationController instance
     */
    public DocumentPresentationController getDocumentPresentationController(Document document);
}
