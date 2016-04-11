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
package org.kuali.rice.kew.impl.action;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.*;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException;
import org.kuali.rice.kew.api.document.*;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeIndexingQueue;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.definition.AttributeDefinition;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.dto.DTOConverter;
import org.kuali.rice.kew.engine.ActivationContext;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.simulation.SimulationCriteria;
import org.kuali.rice.kew.engine.simulation.SimulationResults;
import org.kuali.rice.kew.engine.simulation.SimulationWorkflowEngine;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.WorkflowAttributeXmlValidator;
import org.kuali.rice.kew.rule.WorkflowRuleAttribute;
import org.kuali.rice.kew.rule.xmlrouting.GenericXMLRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.*;

/**
 * Reference implementation of the {@link org.kuali.rice.kew.api.action.WorkflowDocumentActionsService} api.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class WorkflowDocumentActionsServiceImpl implements WorkflowDocumentActionsService {

    private static final Logger LOG = Logger.getLogger(WorkflowDocumentActionsServiceImpl.class);

    private DocumentTypeService documentTypeService;

    private static final DocumentActionCallback ACKNOWLEDGE_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().acknowledgeDocument(principalId, documentBo,
                    annotation);
        }

        public String getActionName() {
            return ActionType.ACKNOWLEDGE.getLabel();
        }
    };

    private static final DocumentActionCallback APPROVE_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().approveDocument(principalId, documentBo, annotation);
        }

        public String getActionName() {
            return ActionType.APPROVE.getLabel();
        }
    };

    private static final DocumentActionCallback CANCEL_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().cancelDocument(principalId, documentBo, annotation);
        }

        public String getActionName() {
            return ActionType.CANCEL.getLabel();
        }
    };

    private static final DocumentActionCallback FYI_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().clearFYIDocument(principalId, documentBo, annotation);
        }

        public String getActionName() {
            return ActionType.FYI.getLabel();
        }
    };

    private static final DocumentActionCallback COMPLETE_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().completeDocument(principalId, documentBo, annotation);
        }

        public String getActionName() {
            return ActionType.COMPLETE.getLabel();
        }
    };

    private static final DocumentActionCallback DISAPPROVE_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().disapproveDocument(principalId, documentBo,
                    annotation);
        }

        public String getActionName() {
            return ActionType.DISAPPROVE.getLabel();
        }
    };

    private static final DocumentActionCallback ROUTE_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().routeDocument(principalId, documentBo, annotation);
        }

        public String getActionName() {
            return ActionType.ROUTE.getLabel();
        }
    };

    private static final DocumentActionCallback BLANKET_APPROVE_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().blanketApproval(principalId, documentBo, annotation,
                    new HashSet<String>());
        }

        public String getActionName() {
            return ActionType.BLANKET_APPROVE.getLabel();
        }
    };

    private static final DocumentActionCallback SAVE_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().saveDocument(principalId, documentBo, annotation);
        }

        public String getActionName() {
            return ActionType.SAVE.getLabel();
        }
    };

    private static final DocumentActionCallback PLACE_IN_EXCEPTION_CALLBACK = new StandardDocumentActionCallback() {
        public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                String annotation) throws WorkflowException {
            return KEWServiceLocator.getWorkflowDocumentService().placeInExceptionRouting(principalId, documentBo,
                    annotation);
        }

        public String getActionName() {
            return "Place In Exception";
        }
    };

    protected DocumentRouteHeaderValue init(DocumentActionParameters parameters) {
        String documentId = parameters.getDocumentId();
        String principalId = parameters.getPrincipalId();
        DocumentUpdate documentUpdate = parameters.getDocumentUpdate();
        DocumentContentUpdate documentContentUpdate = parameters.getDocumentContentUpdate();
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing Document from incoming documentId: " + documentId);
        }
        KEWServiceLocator.getRouteHeaderService().lockRouteHeader(documentId, true);

        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        if (document == null) {
            throw new RiceIllegalArgumentException("Failed to locate a document for document id: " + documentId);
        }
        boolean modified = false;
        if (documentUpdate != null) {
            document.applyDocumentUpdate(documentUpdate);
            modified = true;
        }
/*        if (documentContentUpdate != null) {
            String newDocumentContent = DTOConverter.buildUpdatedDocumentContent(document.getDocContent(),
                    documentContentUpdate, document.getDocumentTypeName());
            document.setDocContent(newDocumentContent);
            modified = true;
        }*/

        if (modified) {
            KEWServiceLocator.getRouteHeaderService().saveRouteHeader(document);

            /*
             * Branch data is not persisted when we call saveRouteHeader so we must Explicitly
             * save the branch.  Noticed issue in: KULRICE-4074 when the future action request info,
             * which is stored in the branch, was not being persisted.
             *
             * The call to setRouteHeaderData will ensure that the variable data is in the branch, but we have
             * to persist the route header before we can save the branch info.
             *
             * Placing here to minimize system impact.  We should investigate placing this logic into
             * saveRouteHeader... but at that point we should just turn auto-update = true on the branch relationship
             *
             */
            this.saveRouteNodeInstances(document);

        }

        return document;
    }

    /**
     * This method explicitly saves the branch data if it exists in the routeHeaderValue
     *
     * @param routeHeader
     */
    private void saveRouteNodeInstances(DocumentRouteHeaderValue routeHeader) {

        List<RouteNodeInstance> routeNodes = routeHeader.getInitialRouteNodeInstances();
        if (routeNodes != null && !routeNodes.isEmpty()) {
            for (RouteNodeInstance rni : routeNodes) {
                KEWServiceLocator.getRouteNodeService().save(rni);
            }
        }

    }

    @Override
    public Document create(String documentTypeName,
            String initiatorPrincipalId, DocumentUpdate documentUpdate,
            DocumentContentUpdate documentContentUpdate)
            throws RiceIllegalArgumentException, IllegalDocumentTypeException, InvalidActionTakenException {

        incomingParamCheck(documentTypeName, "documentTypeName");
        incomingParamCheck(initiatorPrincipalId, "initiatorPrincipalId");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Create Document [documentTypeName=" + documentTypeName + ", initiatorPrincipalId="
                    + initiatorPrincipalId + "]");
        }

        String documentTypeId = documentTypeService.getIdByName(documentTypeName);
        if (documentTypeId == null) {
            throw new RiceIllegalArgumentException("Failed to locate a document type with the given name: "
                    + documentTypeName);
        }

        DocumentRouteHeaderValue documentBo = new DocumentRouteHeaderValue();
        documentBo.setDocumentTypeId(documentTypeId);
        documentBo.setInitiatorWorkflowId(initiatorPrincipalId);
        if (documentUpdate != null) {
            documentBo.setDocTitle(documentUpdate.getTitle());
            documentBo.setAppDocId(documentUpdate.getApplicationDocumentId());
        }
        if (documentContentUpdate != null) {
            String newDocumentContent = DTOConverter.buildUpdatedDocumentContent(null, documentContentUpdate,
                    documentTypeName);
            documentBo.setDocContent(newDocumentContent);
        }

        try {
            documentBo = KEWServiceLocator.getWorkflowDocumentService()
                    .createDocument(initiatorPrincipalId, documentBo);
        } catch (WorkflowException e) {
            // TODO remove this once we stop throwing WorkflowException everywhere!
            translateException(e);
        }
        return DocumentRouteHeaderValue.to(documentBo);
    }

    @Override
    public ValidActions determineValidActions(String documentId, String principalId) {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        if (documentBo == null) {
            throw new RiceIllegalArgumentException("Failed to locate a document for document id: " + documentId);
        }
        return determineValidActionsInternal(documentBo, principalId);
    }

    protected ValidActions determineValidActionsInternal(DocumentRouteHeaderValue documentBo, String principalId) {
        Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipal(principalId);
        return KEWServiceLocator.getActionRegistry().getValidActions(principal, documentBo);
    }

    @Override
    public RequestedActions determineRequestedActions(String documentId, String principalId) {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        if (documentBo == null) {
            throw new RiceIllegalArgumentException("Failed to locate a document for document id: " + documentId);
        }
        KEWServiceLocator.getIdentityHelperService().validatePrincipalId(principalId);
        return determineRequestedActionsInternal(documentBo, principalId);
    }

    protected RequestedActions determineRequestedActionsInternal(DocumentRouteHeaderValue documentBo, String principalId) {
        Map<String, String> actionsRequested = KEWServiceLocator.getActionRequestService().getActionsRequested(documentBo,
                principalId, true);
        boolean completeRequested = false;
        boolean approveRequested = false;
        boolean acknowledgeRequested = false;
        boolean fyiRequested = false;
        for (String actionRequestCode : actionsRequested.keySet()) {
            if (ActionRequestType.FYI.getCode().equals(actionRequestCode)) {
                fyiRequested = Boolean.parseBoolean(actionsRequested.get(actionRequestCode));
            } else if (ActionRequestType.ACKNOWLEDGE.getCode().equals(actionRequestCode)) {
                acknowledgeRequested = Boolean.parseBoolean(actionsRequested.get(actionRequestCode));
            } else if (ActionRequestType.APPROVE.getCode().equals(actionRequestCode)) {
                approveRequested = Boolean.parseBoolean(actionsRequested.get(actionRequestCode));
            } else if (ActionRequestType.COMPLETE.getCode().equals(actionRequestCode)) {
                completeRequested = Boolean.parseBoolean(actionsRequested.get(actionRequestCode));
            }
        }
        return RequestedActions.create(completeRequested, approveRequested, acknowledgeRequested, fyiRequested);
    }

    @Override
    public DocumentDetail executeSimulation(RoutingReportCriteria reportCriteria) {
        incomingParamCheck(reportCriteria, "reportCriteria");
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Executing routing report [docId=" + reportCriteria.getDocumentId() + ", docTypeName=" + reportCriteria.getDocumentTypeName() + "]");
        }
        SimulationCriteria criteria = SimulationCriteria.from(reportCriteria);

        return DTOConverter.convertDocumentDetailNew(KEWServiceLocator.getRoutingReportService().report(criteria));
    }

    protected DocumentActionResult constructDocumentActionResult(DocumentRouteHeaderValue documentBo, String principalId) {
        Document document = DocumentRouteHeaderValue.to(documentBo);
        ValidActions validActions = determineValidActionsInternal(documentBo, principalId);
        RequestedActions requestedActions = determineRequestedActionsInternal(documentBo, principalId);
        return DocumentActionResult.create(document, validActions, requestedActions);
    }

    @Override
    public DocumentActionResult acknowledge(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, ACKNOWLEDGE_CALLBACK);
    }

    @Override
    public DocumentActionResult approve(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, APPROVE_CALLBACK);
    }

    @Override
    public DocumentActionResult adHocToPrincipal(DocumentActionParameters parameters,
            final AdHocToPrincipal adHocToPrincipal) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(adHocToPrincipal, "adHocToPrincipal");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    @Override
                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "AdHoc Route To Principal [principalId=" + principalId +
                                ", docId=" + documentId +
                                ", actionRequest=" + adHocToPrincipal.getActionRequested() +
                                ", nodeName=" + adHocToPrincipal.getNodeName() +
                                ", targetPrincipalId=" + adHocToPrincipal.getTargetPrincipalId() +
                                ", forceAction=" + adHocToPrincipal.isForceAction() +
                                ", annotation=" + annotation +
                                ", requestLabel=" + adHocToPrincipal.getRequestLabel() + "]";
                    }

                    @Override
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().adHocRouteDocumentToPrincipal(
                                principalId,
                                    documentBo,
                                    adHocToPrincipal.getActionRequested().getCode(),
                                    adHocToPrincipal.getNodeName(),
                                    adHocToPrincipal.getPriority(),
                                    annotation,
                                    adHocToPrincipal.getTargetPrincipalId(),
                                    adHocToPrincipal.getResponsibilityDescription(),
                                    adHocToPrincipal.isForceAction(),
                                    adHocToPrincipal.getRequestLabel());
                    }
                });
    }

    @Override
    public DocumentActionResult adHocToPrincipal(DocumentActionParameters parameters, AdHocToPrincipal_v2_1_2 adHocToPrincipal) {
        return adHocToPrincipal(parameters, AdHocToPrincipal_v2_1_2.to(adHocToPrincipal));
    }

    @Override
    public DocumentActionResult adHocToGroup(DocumentActionParameters parameters,
            final AdHocToGroup adHocToGroup) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(adHocToGroup, "adHocToGroup");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    @Override
                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "AdHoc Route To Group [principalId=" + principalId +
                                ", docId=" + documentId +
                                ", actionRequest=" + adHocToGroup.getActionRequested() +
                                ", nodeName=" + adHocToGroup.getNodeName() +
                                ", targetGroupId=" + adHocToGroup.getTargetGroupId() +
                                ", forceAction=" + adHocToGroup.isForceAction() +
                                ", annotation=" + annotation +
                                ", requestLabel=" + adHocToGroup.getRequestLabel() + "]";
                    }

                    @Override
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().adHocRouteDocumentToGroup(principalId,
                                    documentBo,
                                    adHocToGroup.getActionRequested().getCode(),
                                    adHocToGroup.getNodeName(),
                                    adHocToGroup.getPriority(),
                                    annotation,
                                    adHocToGroup.getTargetGroupId(),
                                    adHocToGroup.getResponsibilityDescription(),
                                    adHocToGroup.isForceAction(),
                                    adHocToGroup.getRequestLabel());
                    }
                });
    }

    @Override
    public DocumentActionResult adHocToGroup(DocumentActionParameters parameters, AdHocToGroup_v2_1_2 adHocToGroup) {
        return adHocToGroup(parameters, AdHocToGroup_v2_1_2.to(adHocToGroup));
    }

    @Override
    public DocumentActionResult revokeAdHocRequestById(DocumentActionParameters parameters,
            final String actionRequestId) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(actionRequestId, "actionRequestId");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    @Override
                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "Revoke AdHoc from Principal [principalId=" + principalId +
                                ", documentId=" + documentId +
                                ", annotation=" + annotation +
                                ", actionRequestId=" + actionRequestId + "]";
                    }

                    @Override
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().revokeAdHocRequests(principalId,
                                documentBo, actionRequestId, annotation);
                    }
                });
    }

    @Override
    public DocumentActionResult revokeAdHocRequests(DocumentActionParameters parameters,
            final AdHocRevoke revoke) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(revoke, "revoke");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    @Override
                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "Revoke AdHoc Requests [principalId=" + principalId +
                                ", docId=" + documentId +
                                ", annotation=" + annotation +
                                ", revoke=" + revoke.toString() + "]";
                    }

                    @Override
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().revokeAdHocRequests(principalId,
                                documentBo, revoke, annotation);
                    }
                });
    }

    @Override
    public DocumentActionResult revokeAllAdHocRequests(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    @Override
                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "Revoke All AdHoc Requests [principalId=" + principalId +
                                ", docId=" + documentId +
                                ", annotation=" + annotation + "]";
                    }

                    @Override
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().revokeAdHocRequests(principalId,
                                documentBo, (AdHocRevoke) null, annotation);
                    }
                });
    }

    @Override
    public DocumentActionResult cancel(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, CANCEL_CALLBACK);
    }

    @Override
    public DocumentActionResult recall(DocumentActionParameters parameters, final boolean cancel) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(cancel, "cancel");
        return executeActionInternal(parameters, new StandardDocumentActionCallback() {
            public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                    String annotation) throws WorkflowException {
                return KEWServiceLocator.getWorkflowDocumentService().recallDocument(principalId, documentBo, annotation, cancel);
            }
            public String getActionName() {
                return ActionType.RECALL.getLabel();
            }
        });
    }

    @Override
    public DocumentActionResult clearFyi(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, FYI_CALLBACK);
    }

    @Override
    public DocumentActionResult complete(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, COMPLETE_CALLBACK);
    }

    @Override
    public DocumentActionResult disapprove(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, DISAPPROVE_CALLBACK);
    }

    @Override
    public DocumentActionResult route(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, ROUTE_CALLBACK);
    }

    @Override
    public DocumentActionResult blanketApprove(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, BLANKET_APPROVE_CALLBACK);
    }

    @Override
    public DocumentActionResult blanketApproveToNodes(DocumentActionParameters parameters,
            final Set<String> nodeNames) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(nodeNames, "nodeNames");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().blanketApproval(principalId, documentBo,
                                annotation, nodeNames);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "Blanket Approve [principalId=" + principalId + ", documentId=" + documentId
                                + ", annotation=" + annotation + ", nodeNames=" + nodeNames + "]";
                    }
                });
    }

    @Override
    public DocumentActionResult returnToPreviousNode(DocumentActionParameters parameters,
            final ReturnPoint returnPoint) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(returnPoint, "returnPoint");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().returnDocumentToPreviousNode(principalId,
                                documentBo, returnPoint.getNodeName(), annotation);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "Return to Previous [principalId=" + principalId + ", documentId=" + documentId
                                + ", annotation=" + annotation + ", destNodeName=" + returnPoint.getNodeName() + "]";
                    }
                });
    }

    @Override
    public DocumentActionResult move(DocumentActionParameters parameters,
            final MovePoint movePoint) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(movePoint, "movePoint");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().moveDocument(principalId, documentBo,
                                movePoint, annotation);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "Move Document [principalId=" + principalId + ", documentId=" + documentId
                                + ", annotation=" + annotation + ", movePoint=" + movePoint + "]";
                    }
                });
    }

    @Override
    public DocumentActionResult takeGroupAuthority(DocumentActionParameters parameters,
            final String groupId) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(groupId, "groupId");
        return executeActionInternal(parameters,
                new StandardDocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().takeGroupAuthority(principalId,
                                documentBo, groupId, annotation);
                    }

                    public String getActionName() {
                        return ActionType.TAKE_GROUP_AUTHORITY.getLabel();
                    }
                });
    }

    @Override
    public DocumentActionResult releaseGroupAuthority(DocumentActionParameters parameters,
            final String groupId) {
        incomingParamCheck(parameters, "parameters");
        incomingParamCheck(groupId, "groupId");
        return executeActionInternal(parameters,
                new StandardDocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().releaseGroupAuthority(principalId,
                                documentBo, groupId, annotation);
                    }

                    public String getActionName() {
                        return ActionType.RELEASE_GROUP_AUTHORITY.getLabel();
                    }
                });

    }

    @Override
    public DocumentActionResult save(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, SAVE_CALLBACK);
    }

    @Override
    public DocumentActionResult saveDocumentData(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, new DocumentActionCallback() {

            @Override
            public String getLogMessage(String documentId, String principalId, String annotation) {
                return "Saving Routing Data [principalId=" + principalId + ", docId=" + documentId + "]";
            }

            @Override
            public DocumentRouteHeaderValue doInDocumentBo(
                    DocumentRouteHeaderValue documentBo, String principalId,
                    String annotation) throws WorkflowException {
                return KEWServiceLocator.getWorkflowDocumentService().saveRoutingData(principalId, documentBo);
            }
        });
    }

    @Override
    public Document delete(String documentId, String principalId) {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        DocumentRouteHeaderValue documentBo = init(DocumentActionParameters.create(documentId, principalId, null));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Delete [principalId=" + principalId + ", documentId=" + documentId + "]");
        }
        Document document = null;
        try {
            document = DocumentRouteHeaderValue.to(documentBo);
            KEWServiceLocator.getWorkflowDocumentService().deleteDocument(principalId, documentBo);

        } catch (WorkflowException e) {
            translateException(e);
        }
        return document;
    }

    @Override
    public void logAnnotation(String documentId, String principalId, String annotation) {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(annotation, "annotation");
        DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        try {
            KEWServiceLocator.getWorkflowDocumentService().logDocumentAction(principalId, documentBo, annotation);
        } catch (WorkflowException e) {
            translateException(e);
        }
    }

    @Override
    public void initiateIndexing(String documentId) {
        incomingParamCheck(documentId, "documentId");
        DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        if (documentBo.getDocumentType().hasSearchableAttributes()) {
            DocumentAttributeIndexingQueue queue = KewApiServiceLocator.getDocumentAttributeIndexingQueue(documentBo.getDocumentType().getApplicationId());
            queue.indexDocument(documentId);
        }
    }

    @Override
    public DocumentActionResult superUserBlanketApprove(DocumentActionParameters parameters,
            final boolean executePostProcessor) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().superUserApprove(principalId, documentBo,
                                annotation, executePostProcessor);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "SU Blanket Approve [principalId=" + principalId + ", documentId=" + documentId
                                + ", annotation=" + annotation + "]";
                    }
                });
    }

    @Override
    public DocumentActionResult superUserNodeApprove(DocumentActionParameters parameters,
            final boolean executePostProcessor, final String nodeName) {
                incomingParamCheck(parameters, "parameters");
                incomingParamCheck(nodeName, "nodeName");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().superUserNodeApproveAction(principalId,
                                documentBo, nodeName, annotation, executePostProcessor);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "SU Node Approve Action [principalId=" + principalId + ", documentId=" + documentId
                                + ", nodeName=" + nodeName + ", annotation=" + annotation + "]";
                    }
                });

    }

    @Override
    public DocumentActionResult superUserTakeRequestedAction(DocumentActionParameters parameters,
            final boolean executePostProcessor, final String actionRequestId) {
                incomingParamCheck(parameters, "parameters");
                incomingParamCheck(actionRequestId, "actionRequestId");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().superUserActionRequestApproveAction(
                                principalId, documentBo, actionRequestId, annotation,
                                executePostProcessor);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "SU Take Requested Action [principalId=" + principalId + ", docume tId=" + documentId
                                + ", actionRequestId=" + actionRequestId + ", annotation=" + annotation + "]";
                    }
                });
    }

    @Override
    public DocumentActionResult superUserDisapprove(DocumentActionParameters parameters,
            final boolean executePostProcessor) {
                        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().superUserDisapproveAction(principalId,
                                documentBo, annotation, executePostProcessor);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "SU Disapprove [principalId=" + principalId + ", documentId=" + documentId
                                + ", annotation=" + annotation + "]";
                    }
                });
    }

    @Override
    public DocumentActionResult superUserCancel(DocumentActionParameters parameters, final boolean executePostProcessor) {
                        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().superUserCancelAction(principalId,
                                documentBo, annotation, executePostProcessor);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "SU Cancel [principalId=" + principalId + ", documentId=" + documentId + ", annotation="
                                + annotation + "]";
                    }
                });
    }

    @Override
    public DocumentActionResult superUserReturnToPreviousNode(DocumentActionParameters parameters,
            final boolean executePostProcessor, final ReturnPoint returnPoint) {
            incomingParamCheck(parameters, "parameters");
            incomingParamCheck(returnPoint, "returnPoint");
        return executeActionInternal(parameters,
                new DocumentActionCallback() {
                    public DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo,
                            String principalId, String annotation) throws WorkflowException {
                        return KEWServiceLocator.getWorkflowDocumentService().superUserReturnDocumentToPreviousNode(
                                principalId, documentBo, returnPoint.getNodeName(), annotation, executePostProcessor);
                    }

                    public String getLogMessage(String documentId, String principalId, String annotation) {
                        return "SU Return to Previous Node [principalId=" + principalId + ", documentId=" + documentId
                                + ", annotation=" + annotation + ", returnPoint=" + returnPoint + "]";
                    }
                });

    }

    @Override
    public DocumentActionResult placeInExceptionRouting(DocumentActionParameters parameters) {
        incomingParamCheck(parameters, "parameters");
        return executeActionInternal(parameters, PLACE_IN_EXCEPTION_CALLBACK);
    }

    @Override
    public boolean documentWillHaveAtLeastOneActionRequest(RoutingReportCriteria reportCriteria, List<String> actionRequestedCodes, boolean ignoreCurrentActionRequests) {
        incomingParamCheck(reportCriteria, "reportCriteria");
        incomingParamCheck(actionRequestedCodes, "actionRequestedCodes");
        try {
	        SimulationWorkflowEngine simulationEngine = KEWServiceLocator.getSimulationEngine();
	        SimulationCriteria criteria = SimulationCriteria.from(reportCriteria);
	        // set activate requests to true by default so force action works correctly
	        criteria.setActivateRequests(Boolean.TRUE);
	        SimulationResults results = simulationEngine.runSimulation(criteria);
            List<ActionRequestValue> actionRequestsToProcess = results.getSimulatedActionRequests();
            if (!ignoreCurrentActionRequests) {
                actionRequestsToProcess.addAll(results.getDocument().getActionRequests());
            }
            for (ActionRequestValue actionRequest : actionRequestsToProcess) {
                if (actionRequest.isDone()) {
                    // an action taken has eliminated this request from being active
                    continue;
                }
				// if no action request codes are passed in.... assume any request found is
		    	if (CollectionUtils.isEmpty(actionRequestedCodes) ) {
		    		// we found an action request
		    		return true;
		    	}
		    	// check the action requested codes passed in
		    	for (String requestedActionRequestCode : actionRequestedCodes) {
					if (requestedActionRequestCode.equals(actionRequest.getActionRequested())) {
					    boolean satisfiesDestinationUserCriteria = (criteria.getDestinationRecipients().isEmpty()) || (isRecipientRoutedRequest(actionRequest,criteria.getDestinationRecipients()));
					    if (satisfiesDestinationUserCriteria) {
					        if (StringUtils.isBlank(criteria.getDestinationNodeName())) {
					            return true;
					        } else if (StringUtils.equals(criteria.getDestinationNodeName(),actionRequest.getNodeInstance().getName())) {
					            return true;
					        }
					    }
					}
				}
			}
	        return false;
        } catch (Exception ex) {
        	String error = "Problems evaluating documentWillHaveAtLeastOneActionRequest: " + ex.getMessage();
            LOG.error(error,ex);
            if (ex instanceof RuntimeException) {
            	throw (RuntimeException)ex;
            }
            throw new RuntimeException(error, ex);
        }
    }

    private boolean isRecipientRoutedRequest(ActionRequestValue actionRequest, List<Recipient> recipients) throws WorkflowException {
        for (Recipient recipient : recipients) {
            if (actionRequest.isRecipientRoutedRequest(recipient)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reResolveRoleByDocTypeName(String documentTypeName, String roleName, String qualifiedRoleNameLabel) {
        incomingParamCheck(documentTypeName, "documentTypeName");
        incomingParamCheck(roleName, "roleName");
        incomingParamCheck(qualifiedRoleNameLabel, "qualifiedRoleNameLabel");
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Re-resolving Role [docTypeName=" + documentTypeName + ", roleName=" + roleName + ", qualifiedRoleNameLabel=" + qualifiedRoleNameLabel + "]");
        }
    	DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
    	if (StringUtils.isEmpty(qualifiedRoleNameLabel)) {
    		KEWServiceLocator.getRoleService().reResolveRole(documentType, roleName);
    	} else {
    		KEWServiceLocator.getRoleService().reResolveQualifiedRole(documentType, roleName, qualifiedRoleNameLabel);
    	}
    }

    public void reResolveRoleByDocumentId(String documentId, String roleName, String qualifiedRoleNameLabel) {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(roleName, "roleName");
        incomingParamCheck(qualifiedRoleNameLabel, "qualifiedRoleNameLabel");
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Re-resolving Role [documentId=" + documentId + ", roleName=" + roleName + ", qualifiedRoleNameLabel=" + qualifiedRoleNameLabel + "]");
        }
        DocumentRouteHeaderValue routeHeader = loadDocument(documentId);
    	if (StringUtils.isEmpty(qualifiedRoleNameLabel)) {
    		KEWServiceLocator.getRoleService().reResolveRole(routeHeader, roleName);
    	} else {
    		KEWServiceLocator.getRoleService().reResolveQualifiedRole(routeHeader, roleName, qualifiedRoleNameLabel);
    	}
    }

    @Override
    public List<RemotableAttributeError> validateWorkflowAttributeDefinition(
            WorkflowAttributeDefinition definition) {
        if (definition == null) {
            throw new RiceIllegalArgumentException("definition was null");
        }
        if ( LOG.isDebugEnabled() ) {
            LOG.debug("Validating WorkflowAttributeDefinition [attributeName="+definition.getAttributeName()+"]");
        }
        AttributeDefinition attributeDefinition = DTOConverter.convertWorkflowAttributeDefinition(definition);
        WorkflowRuleAttribute attribute = null;
        if (attributeDefinition != null) {
            attribute = (WorkflowRuleAttribute) GlobalResourceLoader.getObject(attributeDefinition.getObjectDefinition());
        }
        if (attribute instanceof GenericXMLRuleAttribute) {
            Map<String, String> attributePropMap = new HashMap<String, String>();
            GenericXMLRuleAttribute xmlAttribute = (GenericXMLRuleAttribute)attribute;
            xmlAttribute.setExtensionDefinition(attributeDefinition.getExtensionDefinition());
            for (PropertyDefinition propertyDefinition : definition.getPropertyDefinitions()) {
                attributePropMap.put(propertyDefinition.getName(), propertyDefinition.getValue());
            }
            xmlAttribute.setParamMap(attributePropMap);
    }
        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
        //validate inputs from client application if the attribute is capable
        if (attribute instanceof WorkflowAttributeXmlValidator) {
            List<? extends RemotableAttributeErrorContract> validationErrors = ((WorkflowAttributeXmlValidator)attribute).validateClientRoutingData();
            if (validationErrors != null) {
                for (RemotableAttributeErrorContract validationError : validationErrors) {
                    errors.add(RemotableAttributeError.Builder.create(validationError).build());
                }
            }
        }
        return errors;
    }

    @Override
    public boolean isFinalApprover(String documentId, String principalId) {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Evaluating isFinalApprover [docId=" + documentId + ", principalId=" + principalId + "]");
        }
        DocumentRouteHeaderValue routeHeader = loadDocument(documentId);
        List<ActionRequestValue> requests = KEWServiceLocator.getActionRequestService().findPendingByDoc(documentId);
        List<RouteNode> finalApproverNodes = KEWServiceLocator.getRouteNodeService().findFinalApprovalRouteNodes(routeHeader.getDocumentType().getDocumentTypeId());
        if (finalApproverNodes.isEmpty()) {
        	if ( LOG.isDebugEnabled() ) {
        		LOG.debug("Could not locate final approval nodes for document " + documentId);
        	}
            return false;
        }
        Set<String> finalApproverNodeNames = new HashSet<String>();
        for (RouteNode node : finalApproverNodes) {
            finalApproverNodeNames.add(node.getRouteNodeName());
        }

        int approveRequest = 0;
        for (ActionRequestValue request : requests) {
            RouteNodeInstance nodeInstance = request.getNodeInstance();
            if (nodeInstance == null) {
            	if ( LOG.isDebugEnabled() ) {
            		LOG.debug("Found an action request on the document with a null node instance, indicating EXCEPTION routing.");
            	}
                return false;
            }
            if (finalApproverNodeNames.contains(nodeInstance.getRouteNode().getRouteNodeName())) {
                if (request.isApproveOrCompleteRequest()) {
                    approveRequest++;
                    if ( LOG.isDebugEnabled() ) {
                    	LOG.debug("Found request is approver " + request.getActionRequestId());
                    }
                    if (! request.isRecipientRoutedRequest(principalId)) {
                    	if ( LOG.isDebugEnabled() ) {
                    		LOG.debug("Action Request not for user " + principalId);
                    	}
                        return false;
                    }
                }
            }
        }

        if (approveRequest == 0) {
            return false;
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Principal "+principalId+" is final approver for document " + documentId);
        }
        return true;
    }

    @Override
    public boolean routeNodeHasApproverActionRequest(String documentTypeName, String docContent, String nodeName) {
        incomingParamCheck(documentTypeName, "documentTypeName");
        incomingParamCheck(docContent, "docContent");
        incomingParamCheck(nodeName, "nodeName");
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Evaluating routeNodeHasApproverActionRequest [docTypeName=" + documentTypeName + ", nodeName=" + nodeName + "]");
        }
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        RouteNode routeNode = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(documentType.getDocumentTypeId(), nodeName);
        return routeNodeHasApproverActionRequest(documentType, docContent, routeNode, new Integer(KewApiConstants.INVALID_ROUTE_LEVEL));
    }

    /**
     * Really this method needs to be implemented using the executeSimulation functionality (the SimulationEngine).
     * This would get rid of the needs for us to call to FlexRM directly.
     */
    private boolean routeNodeHasApproverActionRequest(DocumentType documentType, String docContent, RouteNode node, Integer routeLevel) {
        incomingParamCheck(documentType, "documentType");
        incomingParamCheck(docContent, "docContent");
        incomingParamCheck(node, "node");
        incomingParamCheck(routeLevel, "routeLevel");

/*        DocumentRouteHeaderValue routeHeader = new DocumentRouteHeaderValue();
        routeHeader.setDocumentId("");
        routeHeader.setDocumentTypeId(documentType.getDocumentTypeId());
        routeHeader.setDocRouteLevel(routeLevel);
        routeHeader.setDocVersion(new Integer(KewApiConstants.DocumentContentVersions.CURRENT));*/

        RoutingReportCriteria.Builder builder = RoutingReportCriteria.Builder.createByDocumentTypeName(documentType.getName());
        builder.setNodeNames(Collections.singletonList(node.getName()));
        builder.setXmlContent(docContent);
        DocumentDetail docDetail = executeSimulation(builder.build());
        if (docDetail != null) {
            for (ActionRequest actionRequest : docDetail.getActionRequests()) {
                if (actionRequest.isApprovalRequest()) {
                    return true;
                }
            }
        }
        /*if (node.getRuleTemplate() != null && node.isFlexRM()) {
            String ruleTemplateName = node.getRuleTemplate().getName();
            builder.setXmlContent(docContent);
            routeHeader.setDocRouteStatus(KewApiConstants.ROUTE_HEADER_INITIATED_CD);
            FlexRM flexRM = new FlexRM();
    		RouteContext context = RouteContext.getCurrentRouteContext();
    		context.setDocument(routeHeader);
    		try {
    			List actionRequests = flexRM.getActionRequests(routeHeader, node, null, ruleTemplateName);
    			for (Iterator iter = actionRequests.iterator(); iter.hasNext();) {
    				ActionRequestValue actionRequest = (ActionRequestValue) iter.next();
    				if (actionRequest.isApproveOrCompleteRequest()) {
    					return true;
    				}
    			}
    		} finally {
    			RouteContext.clearCurrentRouteContext();
    		}
        }*/
        return false;
    }

    @Override
    public boolean isLastApproverAtNode(String documentId, String principalId, String nodeName)  {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(nodeName, "nodeName");
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Evaluating isLastApproverAtNode [docId=" + documentId + ", principalId=" + principalId + ", nodeName=" + nodeName + "]");
        }
        loadDocument(documentId);
        // If this app constant is set to true, then we will attempt to simulate activation of non-active requests before
        // attempting to deactivate them, this is in order to address the force action issue reported by EPIC in issue
        // http://fms.dfa.cornell.edu:8080/browse/KULWF-366
        Boolean activateFirst = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(
                KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.FEATURE_DETAIL_TYPE, KewApiConstants.IS_LAST_APPROVER_ACTIVATE_FIRST_IND);
        if (activateFirst == null) {
            activateFirst = Boolean.FALSE;
        }

        List<ActionRequestValue> requests = KEWServiceLocator.getActionRequestService().findPendingByDocRequestCdNodeName(documentId, KewApiConstants.ACTION_REQUEST_APPROVE_REQ, nodeName);
        if (requests == null || requests.isEmpty()) {
            return false;
        }

        // Deep-copy the action requests for the simulation.
        List<ActionRequestValue> copiedRequests = new ArrayList<ActionRequestValue>();
        for (ActionRequestValue request : requests) {
        	ActionRequestValue actionRequest = (ActionRequestValue) ObjectUtils.deepCopy(
                    (ActionRequestValue) request);
        	// Deep-copy the action items as well, since they are indirectly retrieved from the action request via service calls.
        	for (ActionItem actionItem : actionRequest.getActionItems()) {
        		actionRequest.getSimulatedActionItems().add((ActionItem) ObjectUtils.deepCopy(actionItem));
        	}
        	copiedRequests.add(actionRequest);
        }

        ActivationContext activationContext = new ActivationContext(ActivationContext.CONTEXT_IS_SIMULATION);
        for (ActionRequestValue request : copiedRequests) {
            if (activateFirst.booleanValue() && !request.isActive()) {
                KEWServiceLocator.getActionRequestService().activateRequest(request, activationContext);
            }
            if (request.isUserRequest() && request.getPrincipalId().equals(principalId)) {
                KEWServiceLocator.getActionRequestService().deactivateRequest(null, request, activationContext);
            } else if (request.isGroupRequest() && KimApiServiceLocator.getGroupService().isMemberOfGroup(principalId, request.getGroup().getId())) {
                KEWServiceLocator.getActionRequestService().deactivateRequest(null, request, activationContext);
            }
        }
        boolean allDeactivated = true;
        for (ActionRequestValue actionRequest: copiedRequests) {
            allDeactivated = allDeactivated && actionRequest.isDeactivated();
        }
        return allDeactivated;
    }

    @Override
    public boolean isUserInRouteLog(String documentId, String principalId, boolean lookFuture) {
    	incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        return isUserInRouteLogWithOptionalFlattening(documentId, principalId, lookFuture, false);
    }

    @Override
    public boolean isUserInRouteLogWithOptionalFlattening(String documentId, String principalId, boolean lookFuture, boolean flattenNodes) {
        incomingParamCheck(documentId, "documentId");
        incomingParamCheck(principalId, "principalId");
        boolean authorized = false;
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Evaluating isUserInRouteLog [docId=" + documentId + ", principalId=" + principalId + ", lookFuture=" + lookFuture + "]");
        }
        DocumentRouteHeaderValue routeHeader = loadDocument(documentId);
        if (routeHeader == null) {
            throw new IllegalArgumentException("Document for documentId: " + documentId + " does not exist");
        }
        Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipal(principalId);
        if (principal == null) {
            throw new IllegalArgumentException("Principal for principalId: " + principalId + " does not exist");
        }
        List<ActionTakenValue> actionsTaken = KEWServiceLocator.getActionTakenService().findByDocumentIdWorkflowId(documentId, principal.getPrincipalId());

        if(routeHeader.getInitiatorWorkflowId().equals(principal.getPrincipalId())){
        	return true;
        }

        if (!actionsTaken.isEmpty()) {
        	LOG.debug("found action taken by user");
        	authorized = true;
        }

        List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(documentId);
        if (actionRequestListHasPrincipal(principal, actionRequests)) {
        	authorized = true;
        }

        if (!lookFuture || authorized) {
        	return authorized;
        }


        SimulationWorkflowEngine simulationEngine = KEWServiceLocator.getSimulationEngine();
        SimulationCriteria criteria = SimulationCriteria.createSimulationCritUsingDocumentId(documentId);
        criteria.setDestinationNodeName(null); // process entire document to conclusion
        criteria.getDestinationRecipients().add(new KimPrincipalRecipient(principal));
        criteria.setFlattenNodes(flattenNodes);

        try {
        	SimulationResults results = simulationEngine.runSimulation(criteria);
        	if (actionRequestListHasPrincipal(principal, results.getSimulatedActionRequests())) {
        		authorized = true;
        	}
        } catch (Exception e) {
        	throw new RiceRuntimeException(e);
        }

        return authorized;
    }

    private boolean actionRequestListHasPrincipal(Principal principal, List<ActionRequestValue> actionRequests) {
        for (ActionRequestValue actionRequest : actionRequests) {
            if (actionRequest.isRecipientRoutedRequest(new KimPrincipalRecipient(principal))) {
                return true;
            }
        }
        return false;
    }

    public List<String> getPrincipalIdsInRouteLog(String documentId, boolean lookFuture) {
        if (StringUtils.isEmpty(documentId)) {
            throw new IllegalArgumentException("documentId passed in is null or blank");
        }
    	Set<String> principalIds = new HashSet<String>();
        try {
        	if ( LOG.isDebugEnabled() ) {
        		LOG.debug("Evaluating isUserInRouteLog [docId=" + documentId + ", lookFuture=" + lookFuture + "]");
        	}
            DocumentRouteHeaderValue routeHeader = loadDocument(documentId);
            List<ActionTakenValue> actionsTakens =
            	(List<ActionTakenValue>)KEWServiceLocator.getActionTakenService().findByDocumentId(documentId);
            //TODO: confirm that the initiator is not already there in the actionstaken
            principalIds.add(routeHeader.getInitiatorWorkflowId());
            for(ActionTakenValue actionTaken: actionsTakens){
            	principalIds.add(actionTaken.getPrincipalId());
            }
            List<ActionRequestValue> actionRequests =
            	KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(documentId);
            for(ActionRequestValue actionRequest: actionRequests){
            	principalIds.addAll(getPrincipalIdsForActionRequest(actionRequest));
            }
            if (!lookFuture) {
            	return new ArrayList<String>(principalIds);
            }
            SimulationWorkflowEngine simulationEngine = KEWServiceLocator.getSimulationEngine();
            SimulationCriteria criteria = SimulationCriteria.createSimulationCritUsingDocumentId(documentId);
            criteria.setDestinationNodeName(null); // process entire document to conclusion
            SimulationResults results = simulationEngine.runSimulation(criteria);
            actionRequests = (List<ActionRequestValue>)results.getSimulatedActionRequests();
            for(ActionRequestValue actionRequest: actionRequests){
            	principalIds.addAll(getPrincipalIdsForActionRequest(actionRequest));
            }
        } catch (Exception ex) {
            LOG.warn("Problems getting principalIds in Route Log for documentId: "+documentId+". Exception:"+ex.getMessage(),ex);
        }
    	return new ArrayList<String>(principalIds);
    }

    private DocumentRouteHeaderValue loadDocument(String documentId) {
        return KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
    }

    /**
	 * This method gets all of the principalIds for the given ActionRequestValue.  It drills down into
	 * groups if need be.
	 *
	 * @param actionRequest
	 */
	private List<String> getPrincipalIdsForActionRequest(ActionRequestValue actionRequest) {
		List<String> results = Collections.emptyList();
		if (actionRequest.getPrincipalId() != null) {
			results = Collections.singletonList(actionRequest.getPrincipalId());
		} else if (actionRequest.getGroupId() != null) {
			List<String> principalIdsForGroup =
				KimApiServiceLocator.getGroupService().getMemberPrincipalIds(actionRequest.getGroupId());
			if (principalIdsForGroup != null) {
				results = principalIdsForGroup;
			}
		}
		return results;
	}

    private void incomingParamCheck(Object object, String name) {
        if (object == null) {
            throw new RiceIllegalArgumentException(name + " was null");
        } else if (object instanceof String
                && StringUtils.isBlank((String) object)) {
            throw new RiceIllegalArgumentException(name + " was blank");
        }
    }

    public void setDocumentTypeService(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    /**
     * TODO - this code is temporary until we get rid of all the crazy throwing of
     * "WorkflowException"
     */
    private void translateException(WorkflowException e) {
        if (e instanceof org.kuali.rice.kew.api.exception.InvalidActionTakenException) {
            throw new InvalidActionTakenException(e.getMessage(), e);
        }
        throw new WorkflowRuntimeException(e.getMessage(), e);
    }

    protected DocumentActionResult executeActionInternal(DocumentActionParameters parameters,
            DocumentActionCallback callback) {
        if (parameters == null) {
            throw new RiceIllegalArgumentException("Document action parameters was null.");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(callback.getLogMessage(parameters.getDocumentId(), parameters.getPrincipalId(),
                    parameters.getAnnotation()));
        }
        DocumentRouteHeaderValue documentBo = init(parameters);
        try {
            documentBo = callback.doInDocumentBo(documentBo, parameters.getPrincipalId(), parameters.getAnnotation());
        } catch (WorkflowException e) {
            // TODO fix this up once the checked exception goes away
            translateException(e);
        }
        return constructDocumentActionResult(documentBo, parameters.getPrincipalId());
    }

    protected static interface DocumentActionCallback {

        DocumentRouteHeaderValue doInDocumentBo(DocumentRouteHeaderValue documentBo, String principalId,
                                                String annotation) throws WorkflowException;

        String getLogMessage(String documentId, String principalId, String annotation);

    }

    protected static abstract class StandardDocumentActionCallback implements DocumentActionCallback {

        public final String getLogMessage(String documentId, String principalId, String annotation) {
            return getActionName() + " [principalId=" + principalId + ", documentId=" + documentId + ", annotation="
                    + annotation + "]";
        }

        protected abstract String getActionName();

    }

}
