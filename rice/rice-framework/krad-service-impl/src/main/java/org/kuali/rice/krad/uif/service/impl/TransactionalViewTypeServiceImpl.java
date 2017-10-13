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
package org.kuali.rice.krad.uif.service.impl;

import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.service.ViewTypeService;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.springframework.beans.PropertyValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Type service implementation for transactional views.
 *
 * <p>
 * Indexes views on document class and view name. Can retrieve views by document type,
 * document type and view name, document class, document class and view name, or document id.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TransactionalViewTypeServiceImpl implements ViewTypeService {
    private DocumentService documentService;
    private DocumentDictionaryService documentDictionaryService;

    /**
    * Determines the view type name.
    *
    * <p>
    * The view type name is specific for each type of transactional
    * document and manually set.
    * </p>
    *
    * @return String
    */
    public UifConstants.ViewType getViewTypeName() {
        return UifConstants.ViewType.TRANSACTIONAL;
    }

    /**
    * Get the document class and view name.
    *
    * <p>
    * Extracts the view name and document class name from
    * the PropertyValues.
    * </p>
    *
    * @param propertyValues - collection including document class and view name
    * @return Map<String, String>
    */
    public Map<String, String> getParametersFromViewConfiguration(PropertyValues propertyValues) {
        Map<String, String> parameters = new HashMap<String, String>();

        String viewName = ViewModelUtils.getStringValFromPVs(propertyValues, UifParameters.VIEW_NAME);
        String documentClass = ViewModelUtils.getStringValFromPVs(propertyValues,
                UifParameters.DOCUMENT_CLASS);

        parameters.put(UifParameters.VIEW_NAME, viewName);
        parameters.put(UifParameters.DOCUMENT_CLASS, documentClass);

        return parameters;
    }

    /**
    * Determines the view type name.
    *
    * <p>
    * Check for document id in request parameters, if given retrieve document
    * instance to get the document class. Otherwise check for document class or
    * document type parameters for creating a new document view.
    # </p>
    *
    * @param requestParameters - collection including document class and view name
    * @return Map<String, String> - document class name, view name
    */
    @Override
    public Map<String, String> getParametersFromRequest(Map<String, String> requestParameters) {
        Map<String, String> parameters = new HashMap<String, String>();

        if (requestParameters.containsKey(KRADPropertyConstants.DOC_ID)) {
            String documentNumber = requestParameters.get(KRADPropertyConstants.DOC_ID);

            Class<?> documentClass = null;
            try {
                // determine object class based on the document type
                Document document = documentService.getByDocumentHeaderId(documentNumber);

                if (!documentService.documentExists(documentNumber)) {
                    parameters = new HashMap<String, String>();
                    parameters.put(UifParameters.VIEW_ID, KRADConstants.KRAD_INITIATED_DOCUMENT_VIEW_NAME);
                    return parameters;
                }

                if (document != null) {
                    String docTypeName = document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName();
                    documentClass = getDocumentDictionaryService().getDocumentClassByName(docTypeName);
                    if (documentClass != null) {
                        parameters.put(UifParameters.DOCUMENT_CLASS, documentClass.getName());
                    }
                }

                if (documentClass == null) {
                    throw new RuntimeException(
                            "Could not determine document class for document with id: " + documentNumber);
                }

            } catch (WorkflowException e) {
                throw new RuntimeException(
                        "Encountered workflow exception while retrieving document with id: " + documentNumber, e);
            }
        }
        else {

            if (requestParameters.containsKey(UifParameters.DOCUMENT_CLASS)) {
                parameters.put(UifParameters.DOCUMENT_CLASS, requestParameters.get(UifParameters.DOCUMENT_CLASS));
            }
            else if (requestParameters.containsKey(UifParameters.DOC_TYPE_NAME)) {
                String docTypeName = requestParameters.get(UifParameters.DOC_TYPE_NAME);
                Class<?> documentClass = getDocumentDictionaryService().getDocumentClassByName(docTypeName);

                if (documentClass != null) {
                    parameters.put(UifParameters.DOCUMENT_CLASS, documentClass.getName());
                }
            }

        }

        if (requestParameters.containsKey(UifParameters.VIEW_NAME)) {
            parameters.put(UifParameters.VIEW_NAME, requestParameters.get(UifParameters.VIEW_NAME));
        }
        else {
            parameters.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
        }

        return parameters;
    }

    /**
    * Returns the document service.
    *
    * <p>
    * Gets the document service.
    * </p>
    *
    * @return DocumentService - document service
    */
    protected DocumentService getDocumentService() {

        if (documentService == null) {
            this.documentService = KRADServiceLocatorWeb.getDocumentService();
        }

        return this.documentService;
    }

    /**
    * Initializes the document service .
    *
    * <p>
    * Sets the document service.
    * </p>
    *
    * @param documentService - document service
    */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
    * Returns the document dictionary service.
    *
    * <p>
    * Gets the document dictionary service.
    * </p>
    *
    * @return DocumentDictionaryService - document dictionary service
    */
    public DocumentDictionaryService getDocumentDictionaryService() {

        if (documentDictionaryService == null) {
            this.documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }

        return documentDictionaryService;
    }

    /**
     * Initializes the document dictionary service.
     *
     * <p>
     * Sets the document dictionary service.
     * </p>
     *
     * @param documentDictionaryService - document dictionary service
     */
    public void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        this.documentDictionaryService = documentDictionaryService;
    }
}
