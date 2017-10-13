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
package org.kuali.rice.krad.maintenance;

import org.kuali.rice.krad.document.DocumentPresentationControllerBase;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintenanceDocumentPresentationControllerBase extends DocumentPresentationControllerBase
        implements MaintenanceDocumentPresentationController {
    private static final long serialVersionUID = 2849921477944820474L;

    private transient DocumentDictionaryService documentDictionaryService;

    public boolean canCreate(Class boClass) {
        return getDocumentDictionaryService().getAllowsNewOrCopy(
                getDocumentDictionaryService().getMaintenanceDocumentTypeName(boClass));
    }

    public boolean canMaintain(Object dataObject) {
        return true;
    }

    protected DocumentDictionaryService getDocumentDictionaryService() {
        if (documentDictionaryService == null) {
            documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }
        return documentDictionaryService;
    }

    public void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        this.documentDictionaryService = documentDictionaryService;
    }
}
