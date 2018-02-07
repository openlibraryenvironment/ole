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

import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.document.DocumentViewPresentationControllerBase;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

/**
 * Implementation of {@link org.kuali.rice.krad.uif.view.ViewPresentationController} for
 * {@link org.kuali.rice.krad.uif.view.MaintenanceDocumentView} instances
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintenanceViewPresentationControllerBase extends DocumentViewPresentationControllerBase {

    public boolean canCreate(Class<?> dataObjectClass) {
        return KRADServiceLocatorWeb.getDocumentDictionaryService().getAllowsNewOrCopy(
                KRADServiceLocatorWeb.getDocumentDictionaryService().getMaintenanceDocumentTypeName(dataObjectClass))
                .booleanValue();
    }

    @Override
    public boolean canSave(Document document) {
        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();

        return (!workflowDocument.isEnroute() && super.canSave(document));
    }

    @Override
    public boolean canBlanketApprove(Document document) {
        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();

        return (!workflowDocument.isEnroute() && super.canBlanketApprove(document));
    }
}
