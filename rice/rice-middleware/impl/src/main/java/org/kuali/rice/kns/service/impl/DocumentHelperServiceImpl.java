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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kns.datadictionary.KNSDocumentEntry;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.datadictionary.TransactionalDocumentEntry;
import org.kuali.rice.kns.document.authorization.DocumentAuthorizer;
import org.kuali.rice.kns.document.authorization.DocumentAuthorizerBase;
import org.kuali.rice.kns.document.authorization.DocumentPresentationController;
import org.kuali.rice.kns.document.authorization.DocumentPresentationControllerBase;
import org.kuali.rice.kns.document.authorization.MaintenanceDocumentPresentationControllerBase;
import org.kuali.rice.kns.document.authorization.TransactionalDocumentAuthorizerBase;
import org.kuali.rice.kns.document.authorization.TransactionalDocumentPresentationControllerBase;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.kns.document.authorization.MaintenanceDocumentAuthorizerBase;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

/**
 * This class is a utility service intended to help retrieve objects related to particular documents.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentHelperServiceImpl implements DocumentHelperService {
    
    private DataDictionaryService dataDictionaryService;

    /**
     * @see org.kuali.rice.kns.service.DocumentHelperService#getDocumentAuthorizer(java.lang.String)
     * // TODO: in krad documents could have multiple views and multiple authorizer classes
     */
    public DocumentAuthorizer getDocumentAuthorizer(String documentType) {
        DataDictionary dataDictionary = getDataDictionaryService().getDataDictionary();

        if (StringUtils.isBlank(documentType)) {
            throw new IllegalArgumentException("invalid (blank) documentType");
        }

        KNSDocumentEntry documentEntry = (KNSDocumentEntry) dataDictionary.getDocumentEntry(documentType);
        if (documentEntry == null) {
            throw new IllegalArgumentException("unknown documentType '" + documentType + "'");
        }

        Class<? extends DocumentAuthorizer> documentAuthorizerClass = documentEntry.getDocumentAuthorizerClass();

        DocumentAuthorizer documentAuthorizer = null;
        try {
            if (documentAuthorizerClass != null) {
                documentAuthorizer = documentAuthorizerClass.newInstance();
            }
            else if (documentEntry instanceof MaintenanceDocumentEntry) {
                documentAuthorizer = new MaintenanceDocumentAuthorizerBase();
            }
            else if (documentEntry instanceof TransactionalDocumentEntry) {
                documentAuthorizer = new TransactionalDocumentAuthorizerBase();
            }
            else {
                documentAuthorizer = new DocumentAuthorizerBase();
            }
        }
        catch (Exception e) {
            throw new RuntimeException("unable to instantiate documentAuthorizer '" + documentAuthorizerClass.getName() + "' for doctype '" + documentType + "'", e);
        }

        return documentAuthorizer;
    }

    /**
     * @see org.kuali.rice.kns.service.DocumentHelperService#getDocumentAuthorizer(org.kuali.rice.krad.document.Document)
     */
    public DocumentAuthorizer getDocumentAuthorizer(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("invalid (null) document");
        } else if (document.getDocumentHeader() == null) {
            throw new IllegalArgumentException(
                    "invalid (null) document.documentHeader");
        } else if (!document.getDocumentHeader().hasWorkflowDocument()) {
            throw new IllegalArgumentException(
                    "invalid (null) document.documentHeader.workflowDocument");
        }

        String documentType = document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName();

        DocumentAuthorizer documentAuthorizer = getDocumentAuthorizer(documentType);
        return documentAuthorizer;
    }

    /**
     * @see org.kuali.rice.kns.service.DocumentHelperService#getDocumentPresentationController(java.lang.String)
     * // TODO: in krad documents could have multiple views and multiple presentation controller
     */
    public DocumentPresentationController getDocumentPresentationController(String documentType) {
        DataDictionary dataDictionary = getDataDictionaryService().getDataDictionary();
        DocumentPresentationController documentPresentationController = null;
        
        if (StringUtils.isBlank(documentType)) {
            throw new IllegalArgumentException("invalid (blank) documentType");
        }

        KNSDocumentEntry documentEntry = (KNSDocumentEntry) dataDictionary.getDocumentEntry(documentType);
        if (documentEntry == null) {
            throw new IllegalArgumentException("unknown documentType '" + documentType + "'");
        }
        Class<? extends DocumentPresentationController> documentPresentationControllerClass = null;
        try{
            documentPresentationControllerClass = documentEntry.getDocumentPresentationControllerClass();
            if(documentPresentationControllerClass != null){
                documentPresentationController = documentPresentationControllerClass.newInstance();
            } else {
                KNSDocumentEntry doc = (KNSDocumentEntry) dataDictionary.getDocumentEntry(documentType);
                if ( doc instanceof TransactionalDocumentEntry) {
                    documentPresentationController = new TransactionalDocumentPresentationControllerBase();
                } else if(doc instanceof MaintenanceDocumentEntry) {
                    documentPresentationController = new MaintenanceDocumentPresentationControllerBase();
                } else {
                    documentPresentationController = new DocumentPresentationControllerBase();
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("unable to instantiate documentPresentationController '" + documentPresentationControllerClass.getName() + "' for doctype '" + documentType + "'", e);
        }

        return documentPresentationController;
    }

    /**
     * @see org.kuali.rice.kns.service.DocumentHelperService#getDocumentPresentationController(org.kuali.rice.krad.document.Document)
     */
    public DocumentPresentationController getDocumentPresentationController(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("invalid (null) document");
        }
        else if (document.getDocumentHeader() == null) {
            throw new IllegalArgumentException("invalid (null) document.documentHeader");
        }
        else if (!document.getDocumentHeader().hasWorkflowDocument()) {
            throw new IllegalArgumentException("invalid (null) document.documentHeader.workflowDocument");
        }

        String documentType = document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName();

        DocumentPresentationController documentPresentationController = getDocumentPresentationController(documentType);
        return documentPresentationController;
    }

    /**
     * @return the dataDictionaryService
     */
    public DataDictionaryService getDataDictionaryService() {
        if (dataDictionaryService == null) {
            this.dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();
        }
        return this.dataDictionaryService;
    }

    /**
     * @param dataDictionaryService the dataDictionaryService to set
     */
    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

}
