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
package org.kuali.rice.krad.document;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.io.Serializable;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentPresentationControllerBase implements DocumentPresentationController, Serializable {
    private static final long serialVersionUID = -9181864754090276024L;

    private static transient ParameterService parameterService;

    public boolean canInitiate(String documentTypeName) {
        return true;
    }

    public boolean canEdit(Document document) {
        boolean canEdit = false;
        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        if (workflowDocument.isInitiated()
                || workflowDocument.isSaved()
                || workflowDocument.isEnroute()
                || workflowDocument.isException()) {
            canEdit = true;
        }

        return canEdit;
    }

    public boolean canAnnotate(Document document) {
        return canEdit(document);
    }

    public boolean canReload(Document document) {
        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        return (canEdit(document) && !workflowDocument.isInitiated());

    }

    public boolean canClose(Document document) {
        return true;
    }

    public boolean canSave(Document document) {
        return canEdit(document);
    }

    public boolean canRoute(Document document) {
        boolean canRoute = false;
        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        if (workflowDocument.isInitiated() || workflowDocument.isSaved()) {
            canRoute = true;
        }
        return canRoute;
    }

    public boolean canCancel(Document document) {
        return canEdit(document);
    }

    public boolean canRecall(Document document) {
        // Enroute - the most liberal approximation of recallability
        // DocumentAuthorizer will perform finer-grained authorization
        return document.getDocumentHeader().getWorkflowDocument().isEnroute();
    }

    public boolean canCopy(Document document) {
        boolean canCopy = false;
        if (document.getAllowsCopy()) {
            canCopy = true;
        }
        return canCopy;
    }

    @Override
    public boolean canPerformRouteReport(Document document) {
        return getParameterService().getParameterValueAsBoolean(KRADConstants.KNS_NAMESPACE,
                KRADConstants.DetailTypes.DOCUMENT_DETAIL_TYPE,
                KRADConstants.SystemGroupParameterNames.DEFAULT_CAN_PERFORM_ROUTE_REPORT_IND);
    }

    public boolean canAddAdhocRequests(Document document) {
        return true;
    }

    public boolean canBlanketApprove(Document document) {
        // check system parameter - if Y, use default workflow behavior: allow a user with the permission
        // to perform the blanket approve action at any time
        Boolean allowBlanketApproveNoRequest = getParameterService().getParameterValueAsBoolean(
                KRADConstants.KNS_NAMESPACE, KRADConstants.DetailTypes.DOCUMENT_DETAIL_TYPE,
                KRADConstants.SystemGroupParameterNames.ALLOW_ENROUTE_BLANKET_APPROVE_WITHOUT_APPROVAL_REQUEST_IND);
        if (allowBlanketApproveNoRequest != null && allowBlanketApproveNoRequest.booleanValue()) {
            return canEdit(document);
        }

        // otherwise, limit the display of the blanket approve button to only the initiator of the document
        // (prior to routing)
        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        if (canRoute(document) && StringUtils.equals(workflowDocument.getInitiatorPrincipalId(),
                GlobalVariables.getUserSession().getPrincipalId())) {
            return true;
        }

        // or to a user with an approval action request
        if (workflowDocument.isApprovalRequested()) {
            return true;
        }

        return false;
    }

    public boolean canApprove(Document document) {
        return !canComplete(document);
    }

    public boolean canDisapprove(Document document) {
        // most of the time, a person who can approve can disapprove
        return canApprove(document);
    }

    public boolean canSendAdhocRequests(Document document) {
        WorkflowDocument kualiWorkflowDocument = document.getDocumentHeader().getWorkflowDocument();
        return !(kualiWorkflowDocument.isInitiated() || kualiWorkflowDocument.isSaved());
    }

    public boolean canSendNoteFyi(Document document) {
        return true;
    }

    public boolean canEditDocumentOverview(Document document) {
        WorkflowDocument kualiWorkflowDocument = document.getDocumentHeader().getWorkflowDocument();
        return (kualiWorkflowDocument.isInitiated() || kualiWorkflowDocument.isSaved());
    }

    public boolean canFyi(Document document) {
        return true;
    }

    public boolean canAcknowledge(Document document) {
        return true;
    }

    public boolean canComplete(Document document) {
        boolean docInInit = document.getDocumentHeader().getWorkflowDocument().isInitiated() || document.getDocumentHeader().getWorkflowDocument().isSaved();
        boolean completionRequested = document.getDocumentHeader().getWorkflowDocument().isCompletionRequested();
        if (completionRequested && !docInInit) {
            return true;
        }
        return false;
    }

    protected ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = CoreFrameworkServiceLocator.getParameterService();
        }
        return parameterService;
    }
}
