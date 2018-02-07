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
package org.kuali.rice.krad.workflow.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.exception.UnknownDocumentIdException;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Implementation of the WorkflowDocumentService, which makes use of Workflow
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Transactional
public class WorkflowDocumentServiceImpl implements WorkflowDocumentService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WorkflowDocumentServiceImpl.class);

    @Override
    public boolean workflowDocumentExists(String documentId) {
        boolean exists = false;

        if (StringUtils.isBlank(documentId)) {
            throw new IllegalArgumentException("invalid (blank) documentId");
        }

        exists = KewApiServiceLocator.getWorkflowDocumentService().doesDocumentExist(documentId);

        return exists;
    }

    @Override
    public WorkflowDocument createWorkflowDocument(String documentTypeName, Person person) {
        String watchName = "createWorkflowDocument";
        StopWatch watch = new StopWatch();
        watch.start();
        if (LOG.isDebugEnabled()) {
            LOG.debug(watchName + ": started");
        }
        if (StringUtils.isBlank(documentTypeName)) {
            throw new IllegalArgumentException("invalid (blank) documentTypeName");
        }
        if (person == null) {
            throw new IllegalArgumentException("invalid (null) person");
        }

        if (StringUtils.isBlank(person.getPrincipalName())) {
            throw new IllegalArgumentException("invalid (empty) PrincipalName");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("creating workflowDoc(" + documentTypeName + "," + person.getPrincipalName() + ")");
        }

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(person.getPrincipalId(), documentTypeName);
        watch.stop();
        if (LOG.isDebugEnabled()) {
            LOG.debug(watchName + ": " + watch.toString());	
        }

        return document;
    }

    @Override
    public WorkflowDocument loadWorkflowDocument(String documentId, Person user) {
        if (documentId == null) {
            throw new IllegalArgumentException("invalid (null) documentHeaderId");
        }
        if (user == null) {
            throw new IllegalArgumentException("invalid (null) workflowUser");
        }
        else if (StringUtils.isEmpty(user.getPrincipalName())) {
            throw new IllegalArgumentException("invalid (empty) workflowUser");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("retrieving document(" + documentId + "," + user.getPrincipalName() + ")");
        }
        
        try {
            return WorkflowDocumentFactory.loadDocument(user.getPrincipalId(), documentId);
        } catch (IllegalArgumentException e) {
            // TODO do we really need to do this or just let the IllegalArgument propogate?
            throw new UnknownDocumentIdException("unable to locate document with documentHeaderId '" + documentId + "'");
        }
    }

    @Override
    public void acknowledge(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("acknowleding document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }

        handleAdHocRouteRequests(workflowDocument, annotation, filterAdHocRecipients(adHocRecipients, new String[] { KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_REQUEST_FYI_REQ }));
        workflowDocument.acknowledge(annotation);
    }

    @Override
    public void approve(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("approving document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }

        handleAdHocRouteRequests(workflowDocument, annotation, filterAdHocRecipients(adHocRecipients, new String[] { KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_REQUEST_FYI_REQ, KewApiConstants.ACTION_REQUEST_APPROVE_REQ }));
        workflowDocument.approve(annotation);
    }


    @Override
    public void superUserApprove(WorkflowDocument workflowDocument, String annotation) throws WorkflowException {
    	if ( LOG.isInfoEnabled() ) {
    		LOG.info("super user approve document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
    	}
        workflowDocument.superUserBlanketApprove(annotation);
    }

    @Override
    public void superUserCancel(WorkflowDocument workflowDocument, String annotation) throws WorkflowException {
        LOG.info("super user cancel document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        workflowDocument.superUserCancel(annotation);
    }

    @Override
    public void superUserDisapprove(WorkflowDocument workflowDocument, String annotation) throws WorkflowException {
    	if ( LOG.isInfoEnabled() ) {
    		LOG.info("super user disapprove document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
    	}
        workflowDocument.superUserDisapprove(annotation);
    }

    @Override
    public void blanketApprove(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("blanket approving document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }

        handleAdHocRouteRequests(workflowDocument, annotation, filterAdHocRecipients(adHocRecipients, new String[] { KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_REQUEST_FYI_REQ }));
        workflowDocument.blanketApprove(annotation);
    }

    @Override
    public void cancel(WorkflowDocument workflowDocument, String annotation) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("canceling document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }

        workflowDocument.cancel(annotation);
    }

    @Override
    public void recall(WorkflowDocument workflowDocument, String annotation, boolean cancel) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("recalling document(" + workflowDocument.getDocumentId() + ",'" + annotation + "', '" + cancel + "')");
        }

        workflowDocument.recall(annotation, cancel);
    }

    @Override
    public void clearFyi(WorkflowDocument workflowDocument, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearing FYI for document(" + workflowDocument.getDocumentId() + ")");
        }

        handleAdHocRouteRequests(workflowDocument, "", filterAdHocRecipients(adHocRecipients, new String[] { KewApiConstants.ACTION_REQUEST_FYI_REQ }));
        workflowDocument.fyi();
    }

    @Override
    public void sendWorkflowNotification(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException {
    	sendWorkflowNotification(workflowDocument, annotation, adHocRecipients, null);
    }
    
    @Override
    public void sendWorkflowNotification(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients, String notificationLabel) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sending FYI for document(" + workflowDocument.getDocumentId() + ")");
        }

        handleAdHocRouteRequests(workflowDocument, annotation, adHocRecipients, notificationLabel);
    }

    @Override
    public void disapprove(WorkflowDocument workflowDocument, String annotation) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("disapproving document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }

        workflowDocument.disapprove(annotation);
    }

    @Override
    public void route(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("routing document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }

        handleAdHocRouteRequests(workflowDocument, annotation, filterAdHocRecipients(adHocRecipients, new String[] { KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_REQUEST_FYI_REQ, KewApiConstants.ACTION_REQUEST_APPROVE_REQ, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ }));
        workflowDocument.route(annotation);
    }

    @Override
    public void save(WorkflowDocument workflowDocument, String annotation) throws WorkflowException {
        if (workflowDocument.isValidAction(ActionType.SAVE)) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("saving document(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }

        workflowDocument.saveDocument(annotation);
    }
        else {
            this.saveRoutingData(workflowDocument);
        }
    }

    @Override
    public void saveRoutingData(WorkflowDocument workflowDocument) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("saving document(" + workflowDocument.getDocumentId() + ")");
        }

        workflowDocument.saveDocumentData();
    }

    @Override
    public String getCurrentRouteLevelName(WorkflowDocument workflowDocument) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getting current route level name for document(" + workflowDocument.getDocumentId());
        }
//        return KEWServiceLocator.getRouteHeaderService().getRouteHeader(workflowDocument.getDocumentId()).getCurrentRouteLevelName();
        WorkflowDocument freshCopyWorkflowDoc = loadWorkflowDocument(workflowDocument.getDocumentId(), GlobalVariables.getUserSession().getPerson());
        return getCurrentRouteNodeNames(freshCopyWorkflowDoc);
    }
    
    

    @Override
    public String getCurrentRouteNodeNames(WorkflowDocument workflowDocument) {
        Set<String> nodeNames = workflowDocument.getNodeNames();
        if (nodeNames.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String nodeName : nodeNames) {
            builder.append(nodeName).append(", ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    private void handleAdHocRouteRequests(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException {
    	handleAdHocRouteRequests(workflowDocument, annotation, adHocRecipients, null);
    }
    
    /**
     * Convenience method for generating ad hoc requests for a given document
     *
     * @param flexDoc
     * @param annotation
     * @param adHocRecipients
     * @throws InvalidActionTakenException
     * @throws InvalidRouteTypeException
     * @throws InvalidActionRequestException
     */
    private void handleAdHocRouteRequests(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients, String notificationLabel) throws WorkflowException {

        if (adHocRecipients != null && adHocRecipients.size() > 0) {
            String currentNode = null;
            Set<String> currentNodes = workflowDocument.getNodeNames();
            if (currentNodes.isEmpty()) {
                List<RouteNodeInstance> nodes = KewApiServiceLocator.getWorkflowDocumentService().getTerminalRouteNodeInstances(
                        workflowDocument.getDocumentId());
                currentNodes = new HashSet<String>();
                for (RouteNodeInstance node : nodes) {
                    currentNodes.add(node.getName());
                }
            }
            // for now just pick a node and go with it...
            currentNode = currentNodes.iterator().next();
            
            List<AdHocRoutePerson> adHocRoutePersons = new ArrayList<AdHocRoutePerson>();
            List<AdHocRouteWorkgroup> adHocRouteWorkgroups = new ArrayList<AdHocRouteWorkgroup>();
            
            for (AdHocRouteRecipient recipient : adHocRecipients) {
                if (StringUtils.isNotEmpty(recipient.getId())) {
                	String newAnnotation = annotation;
                	if ( StringUtils.isBlank( annotation ) ) {
                		try {
                			String message = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                                    RiceKeyConstants.MESSAGE_ADHOC_ANNOTATION);
                			newAnnotation = MessageFormat.format(message, GlobalVariables.getUserSession().getPrincipalName() );
                		} catch ( Exception ex ) {
                			LOG.warn("Unable to set annotation", ex );
                		}
                	}
                    if (AdHocRouteRecipient.PERSON_TYPE.equals(recipient.getType())) {
                        // TODO make the 1 a constant
                    	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(recipient.getId());
                		if (principal == null) {
                			throw new RiceRuntimeException("Could not locate principal with name '" + recipient.getId() + "'");
                		}
                        workflowDocument.adHocToPrincipal(ActionRequestType.fromCode(recipient.getActionRequested()), currentNode, newAnnotation, principal.getPrincipalId(), "", true, notificationLabel);
                        AdHocRoutePerson personRecipient  = (AdHocRoutePerson)recipient;
                        adHocRoutePersons.add(personRecipient);
                    }
                    else {
                    	Group group = KimApiServiceLocator.getGroupService().getGroup(recipient.getId());
                		if (group == null) {
                			throw new RiceRuntimeException("Could not locate group with id '" + recipient.getId() + "'");
                		}
                    	workflowDocument.adHocToGroup(ActionRequestType.fromCode(recipient.getActionRequested()), currentNode, newAnnotation, group.getId() , "", true, notificationLabel);
                        AdHocRouteWorkgroup groupRecipient  = (AdHocRouteWorkgroup)recipient;
                        adHocRouteWorkgroups.add(groupRecipient);
                    }
                }
            }
            KRADServiceLocator.getBusinessObjectService().delete(adHocRoutePersons);
            KRADServiceLocator.getBusinessObjectService().delete(adHocRouteWorkgroups);  
        }
    }

    /**
     * Convenience method to filter out any ad hoc recipients that should not be allowed given the action requested of the user that
     * is taking action on the document
     *
     * @param adHocRecipients
     */
    private List<AdHocRouteRecipient> filterAdHocRecipients(List<AdHocRouteRecipient> adHocRecipients, String[] validTypes) {
        // now filter out any but ack or fyi from the ad hoc list
        List<AdHocRouteRecipient> realAdHocRecipients = new ArrayList<AdHocRouteRecipient>();
        if (adHocRecipients != null) {
            for (AdHocRouteRecipient proposedRecipient : adHocRecipients) {
                if (StringUtils.isNotBlank(proposedRecipient.getActionRequested())) {
                    for (int i = 0; i < validTypes.length; i++) {
                        if (validTypes[i].equals(proposedRecipient.getActionRequested())) {
                            realAdHocRecipients.add(proposedRecipient);
                        }
                    }
                }
            }
        }
        return realAdHocRecipients;
    }

    /**
     * Completes workflow document
     * 
     * @see WorkflowDocumentService#complete(org.kuali.rice.kew.api.WorkflowDocument, String, java.util.List)
     */
    public void complete(WorkflowDocument workflowDocument, String annotation, List adHocRecipients) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("routing flexDoc(" + workflowDocument.getDocumentId() + ",'" + annotation + "')");
        }
        handleAdHocRouteRequests(workflowDocument, annotation, filterAdHocRecipients(adHocRecipients, new String[] { KewApiConstants.ACTION_REQUEST_COMPLETE_REQ,KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_REQUEST_FYI_REQ, KewApiConstants.ACTION_REQUEST_APPROVE_REQ }));
        workflowDocument.complete(annotation);
    }
}
