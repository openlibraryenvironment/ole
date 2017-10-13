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

import java.util.HashMap;
import java.util.Map;

import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.service.ViewTypeService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.springframework.beans.PropertyValues;

/**
 * Type service implementation for maintenance views
 * 
 * <p>
 * Indexes views on object class and name. Can retrieve views by object class,
 * object class and name, or document id
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintenanceViewTypeServiceImpl implements ViewTypeService {
	private DocumentService documentService;
    private DocumentDictionaryService documentDictionaryService;

	/**
	 * @see org.kuali.rice.krad.uif.service.ViewTypeService#getViewTypeName()
	 */
	public ViewType getViewTypeName() {
		return ViewType.MAINTENANCE;
	}

    /**
     * @see org.kuali.rice.krad.uif.service.ViewTypeService#getParametersFromViewConfiguration(org.springframework.beans.PropertyValues)
     */
    public Map<String, String> getParametersFromViewConfiguration(PropertyValues propertyValues) {
        Map<String, String> parameters = new HashMap<String, String>();

        String viewName = ViewModelUtils.getStringValFromPVs(propertyValues, UifParameters.VIEW_NAME);
        String dataObjectClassName = ViewModelUtils.getStringValFromPVs(propertyValues,
                UifParameters.DATA_OBJECT_CLASS_NAME);

        parameters.put(UifParameters.VIEW_NAME, viewName);
        parameters.put(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClassName);

        return parameters;
    }

	/**
	 * Check for document id in request parameters, if given retrieve document
	 * instance to get the data object class. Otherwise check for data object class parameter for
     * creating a new maintenance view
	 * 
	 * @see org.kuali.rice.krad.uif.service.ViewTypeService#getParametersFromRequest(java.util.Map)
	 */
	@Override
    public Map<String, String> getParametersFromRequest(Map<String, String> requestParameters) {
        Map<String, String> parameters = new HashMap<String, String>();

        if (requestParameters.containsKey(KRADPropertyConstants.DOC_ID)) {
            String documentNumber = requestParameters.get(KRADPropertyConstants.DOC_ID);

            Class<?> objectClassName = null;
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
                    objectClassName = getDocumentDictionaryService().getMaintenanceDataObjectClass(docTypeName);
                    if (objectClassName != null) {
                        parameters.put(UifParameters.DATA_OBJECT_CLASS_NAME, objectClassName.getName());
                    }
                }

                if (objectClassName == null) {
                    throw new RuntimeException(
                            "Could not determine object class for maintenance document with id: " + documentNumber);
                }
            } catch (WorkflowException e) {
                throw new RuntimeException(
                        "Encountered workflow exception while retrieving document with id: " + documentNumber, e);
            }
        } else if (requestParameters.containsKey(UifParameters.DATA_OBJECT_CLASS_NAME)) {
            parameters.put(UifParameters.DATA_OBJECT_CLASS_NAME, requestParameters.get(
                    UifParameters.DATA_OBJECT_CLASS_NAME));
        }

        if (requestParameters.containsKey(UifParameters.VIEW_NAME)) {
            parameters.put(UifParameters.VIEW_NAME, requestParameters.get(UifParameters.VIEW_NAME));
        } else {
            parameters.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
        }

        return parameters;
    }

	protected DocumentService getDocumentService() {
        if (documentService == null) {
            this.documentService = KRADServiceLocatorWeb.getDocumentService();
        }
		return this.documentService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

    public DocumentDictionaryService getDocumentDictionaryService() {
        if (documentDictionaryService == null) {
            this.documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }
        return documentDictionaryService;
    }

    public void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        this.documentDictionaryService = documentDictionaryService;
    }
}
