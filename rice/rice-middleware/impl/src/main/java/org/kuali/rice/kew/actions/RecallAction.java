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
package org.kuali.rice.kew.actions;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.input.DOMBuilder;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.impl.CoreServiceImplServiceLocator;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.api.doctype.ProcessDefinition;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.role.KimRoleRecipient;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.xml.CommonXmlParser;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @since 2.1
 */
public class RecallAction extends ReturnToPreviousNodeAction {
    private static final Logger LOG = Logger.getLogger(RecallAction.class);

    protected final boolean cancel;
    protected final Collection<Recipient> notificationRecipients;

    /**
     * Constructor required for ActionRegistry validation
     */
    public RecallAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        this(routeHeader, principal, null, true, true, true);
    }

    public RecallAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean cancel) {
        this(routeHeader, principal, annotation, cancel, true, true);
    }

    public RecallAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean cancel, boolean sendNotifications) {
        this(routeHeader, principal, annotation, cancel, sendNotifications, true);
    }

    public RecallAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean cancel, boolean sendNotifications, boolean runPostProcessorLogic) {
        super(ActionType.RECALL.getCode(), routeHeader, principal,
              principal.getPrincipalName() + " recalled document" + (StringUtils.isBlank(annotation) ? "" : ": " + annotation),
              INITIAL_NODE_NAME,
              sendNotifications, runPostProcessorLogic);
        this.cancel = cancel;
        this.notificationRecipients = Collections.unmodifiableCollection(parseNotificationRecipients(routeHeader));
    }

    /**
     * Parses notification recipients from the RECALL_NOTIFICATION document type policy, if present
     * @param routeHeader this document
     * @return notification recipient RuleResponsibilityBos
     */
    protected static Collection<Recipient> parseNotificationRecipients(DocumentRouteHeaderValue routeHeader) {
        Collection<Recipient> toNotify = new ArrayList<Recipient>();
        org.kuali.rice.kew.doctype.DocumentTypePolicy recallNotification = routeHeader.getDocumentType().getRecallNotification();
        if (recallNotification != null) {
            String config = recallNotification.getPolicyStringValue();
            if (!StringUtils.isBlank(config)) {
                Document notificationConfig;
                try {
                    notificationConfig = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(config)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                DOMBuilder jdom = new DOMBuilder();
                // ok, so using getElementsByTagName is less strict than it could be because it inspects all descendants
                // not just immediate children.  but the w3c dom API stinks and this is less painful than manually searching immediate children
                NodeList recipients = notificationConfig.getDocumentElement().getElementsByTagName("recipients");
                for (int i = 0; i < recipients.getLength(); i++) {
                    NodeList children = recipients.item(i).getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node n = children.item(j);
                        if (n instanceof Element) {
                            Element e = (Element) n;
                            if ("role".equals(e.getNodeName())) {
                                String ns = e.getAttribute("namespace");
                                String name = e.getAttribute("name");
                                Role role = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName(ns, name);
                                if (role == null) {
                                    LOG.error("No role round: " + ns + ":" + name);
                                } else {
                                    toNotify.add(new KimRoleRecipient(role));
                                }
                            } else {
                                // parseResponsibilityNameAndType parses a single responsibility choice from a parent element
                                // wrap the element with a parent so we can use this method
                                org.jdom.Element parent = new org.jdom.Element("parent");
                                parent.addContent(jdom.build((Element) e.cloneNode(true)).detach());
                                toNotify.add(CommonXmlParser.parseResponsibilityNameAndType(parent).getRecipient());
                            }
                        }
                    }
                }
            }
        }
        return toNotify;
    }

    @Override
    public String validateActionRules(List<ActionRequestValue> actionRequests) {
        if (!getRouteHeader().isValidActionToTake(getActionPerformedCode())) {
            return "Document of status '" + getRouteHeader().getDocRouteStatus() + "' cannot taken action '" + ActionType.fromCode(this.getActionTakenCode()).getLabel() + "' to node name " + this.nodeName;
        }

        // validate that recall action can be taken given prior actions taken
        String errMsg = validateActionsTaken(getRouteHeader());
        if (errMsg != null) {
            return errMsg;
        }

        // validate that the doc will actually route to anybody
        errMsg = validateRouting(getRouteHeader());
        if (errMsg != null) {
            return errMsg;
        }


        if (!KEWServiceLocator.getDocumentTypePermissionService().canRecall(getPrincipal().getPrincipalId(), getRouteHeader())) {
            return "User is not authorized to Recall document";
        }
        return "";
    }

    /**
     * Determines whether prior actions taken are compatible with recall action by checking the RECALL_VALID_ACTIONSTAKEN
     * document type policy.
     * @param rh the DocumentRouteHeaderValue
     * @return null if valid (policy not specified, no actions taken, or all actions taken are in valid actions taken list), or error message if invalid
     */
    protected String validateActionsTaken(DocumentRouteHeaderValue rh) {
        String validActionsTaken = rh.getDocumentType().getPolicyByName(DocumentTypePolicy.RECALL_VALID_ACTIONSTAKEN.getCode(), (String) null).getPolicyStringValue();
        if (StringUtils.isNotBlank(validActionsTaken)) {
            // interpret as comma-separated list of codes OR labels
            String[] validActionsTakenStrings = validActionsTaken.split("\\s*,\\s*");
            Set<ActionType> validActionTypes = new HashSet<ActionType>(validActionsTakenStrings.length);
            for (String s: validActionsTakenStrings) {
                ActionType at = ActionType.fromCodeOrLabel(s);
                if (at == null) {
                    throw new IllegalArgumentException("Failed to locate the ActionType with the given code or label: " + s);
                }
                validActionTypes.add(at);
            }

            Collection<ActionTakenValue> actionsTaken = KEWServiceLocator.getActionTakenService().getActionsTaken(getRouteHeader().getDocumentId());

            for (ActionTakenValue actionTaken: actionsTaken) {
                ActionType at = ActionType.fromCode(actionTaken.getActionTaken());
                if (!validActionTypes.contains(at)) {
                    return "Invalid prior action taken: '" + at.getLabel() + "'. Cannot Recall.";
                }
            }
        }
        return null;
    }

    /**
     * Determines whether the doc's type appears to statically define any routing.  If not, then Recall action
     * doesn't make much sense, and should not be available.  Checks whether any document type processes are defined,
     * and if so, whether are are any non-"adhoc" nodes (based on literal node name check).
     * @param rh the DocumentRouteHeaderValue
     * @return error message if it looks like it's this doc will not route to a person based on static route definition, null (valid) otherwise
     */
    protected String validateRouting(DocumentRouteHeaderValue rh) {
        List<ProcessDefinitionBo> processes = rh.getDocumentType().getProcesses();

        String errMsg = null;
        if (processes.size() == 0) {
            // if no processes are present then this doc isn't going to route to anyone
            errMsg = "No processes are defined for document type. Recall is not applicable.";
        } else {
            // if there is not at least one route node not named "ADHOC", then assume this doc will not route to anybody
            RouteNode initialRouteNode = rh.getDocumentType().getPrimaryProcess().getInitialRouteNode();
            if (initialRouteNode.getName().equalsIgnoreCase("ADHOC") && initialRouteNode.getNextNodeIds().isEmpty()) {
                errMsg = "No non-adhoc route nodes defined for document type. Recall is not applicable";
            }
        }
        return errMsg;
    }

    @Override // overridden to simply pass through all actionrequests
    protected List<ActionRequestValue> findApplicableActionRequests(List<ActionRequestValue> actionRequests) {
        return actionRequests;
    }

    @Override // overridden to implement Recall action validation
    public boolean isActionCompatibleRequest(List<ActionRequestValue> requests) {
        return true;
    }

    @Override // When invoked, RECALL TO ACTION LIST action will return the document back to their action list with the route status of SAVED and requested action of COMPLETE.
    protected ActionRequestType getReturnToInitiatorActionRequestType() {
        return ActionRequestType.COMPLETE;
    }

    /**
     * Override the default return to previous behavior so that the document is returned to the recaller, not initiator
     */
    @Override
    protected PrincipalContract determineInitialNodePrincipal(DocumentRouteHeaderValue routeHeader) {
        return getPrincipal();
    }

    @Override
    protected void sendAdditionalNotifications() {
        super.sendAdditionalNotifications();
        // NOTE: do we need construct w/ nodeInstance here?
        ActionRequestFactory arFactory = new ActionRequestFactory(routeHeader);
        for (Recipient recipient: this.notificationRecipients) {
            if (!(recipient instanceof KimRoleRecipient)) {
                arFactory.addRootActionRequest(ActionRequestType.FYI.getCode(), 0, recipient, "Document was recalled", KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID, null, null, null);
            } else {
                KimRoleRecipient kimRoleRecipient = (KimRoleRecipient) recipient;
                // no qualifications
                Map<String, String> qualifications = Collections.emptyMap();
                LOG.info("Adding KIM Role Request for " + kimRoleRecipient.getRole());
                arFactory.addKimRoleRequest(ActionRequestType.FYI.getCode(), 0, kimRoleRecipient.getRole(),
                                            KimApiServiceLocator.getRoleService().getRoleMembers(Collections.singletonList(kimRoleRecipient.getRole().getId()), qualifications),
                                            "Document was recalled", KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID,
                                            // force action, so the member is notified even if they have already participated in the workflow
                                            // since this is a notification about an exceptional case they should receive
                                            true, ActionRequestPolicy.FIRST.getCode(), "Document was recalled");
            }
        }
        getActionRequestService().activateRequests(arFactory.getRequestGraphs());
    }

    @Override
    public void recordAction() throws InvalidActionTakenException {
        if (this.cancel) {
            // perform action validity check here as well as WorkflowDocumentServiceImpl calls recordAction immediate after action construction
            String errorMessage = validateActionRules(getActionRequestService().findAllPendingRequests(getDocumentId()));
            if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
                throw new InvalidActionTakenException(errorMessage);
            }
            // we are in cancel mode, execute a cancel action with RECALL code
            // NOTE: this performs CancelAction validation, including getDocumentTypePermissionService().canCancel
            // stub out isActionCompatibleRequest since we are programmatically forcing this action
            new CancelAction(ActionType.RECALL, this.routeHeader, this.getPrincipal(), this.annotation) {
                @Override
                public boolean isActionCompatibleRequest(List<ActionRequestValue> requests) {
                    return true;
                }
                @Override
                protected void markDocumentStatus() throws InvalidActionTakenException {
                    getRouteHeader().markDocumentRecalled();
                }
            }.recordAction();
        } else {
            super.recordAction();
            // When invoked, RECALL TO ACTION LIST action will return the document back to their action list with the route status of SAVED and requested action of COMPLETE.
            String oldStatus = getRouteHeader().getDocRouteStatus();
            getRouteHeader().markDocumentSaved();
            String newStatus = getRouteHeader().getDocRouteStatus();
            notifyStatusChange(newStatus, oldStatus);
            KEWServiceLocator.getRouteHeaderService().saveRouteHeader(routeHeader);
        }
        // we don't have an established mechanism for exposing the action taken that is saved as the result of a (nested) action
        // so use the last action take saved
        ActionTakenValue last = getLastActionTaken(getDocumentId());
        if (last != null) {
            notifyAfterActionTaken(last);
        }
    }

    /**
     * Returns the last action taken on a document
     * @param docId the doc id
     * @return last action taken on a document
     */
    protected static ActionTakenValue getLastActionTaken(String docId) {
        ActionTakenValue last = null;
        Collection<ActionTakenValue> actionsTaken = (Collection<ActionTakenValue>) KEWServiceLocator.getActionTakenService().getActionsTaken(docId);
        for (ActionTakenValue at: actionsTaken) {
            if (last == null || at.getActionDate().after(last.getActionDate())) {
                last = at;
            }
        }
        return last;
    }
}