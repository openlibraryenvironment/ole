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
package org.kuali.rice.krad.workflow.service;

import java.util.List;

import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;

/**
 * This interface defines the contract that must be implemented by the workflow engine.
 *
 *
 */
public interface WorkflowDocumentService {
    /**
     * @param documentHeaderId
     * @return true if a workflowDocument exists for the given documentHeaderId
     */
    public boolean workflowDocumentExists(String documentHeaderId);

    /**
     * Given a documentTypeName and workflowUser, returns a new workflowDocument from the workflow
     * server.
     *
     * @param documentTypeName
     * @param workflowUser
     * @return newly-created workflowDocument instance
     * @throws IllegalArgumentException if the given documentTypeName is blank
     * @throws IllegalArgumentException if the given workflowUser is null or contains no id
     * @throws ResourceUnavailableException
     */
    public WorkflowDocument createWorkflowDocument(String documentTypeName, Person workflowUser)
            throws WorkflowException;

    /**
     * Given a documentHeaderId and workflowUser, retrieves the workflowDocument associated with
     * that documentHeaderId from the workflow server.
     *
     * @param documentHeaderId
     * @param workflowUser
     * @return existing workflowDoc
     * @throws IllegalArgumentException if the given documentHeaderId is null
     * @throws IllegalArgumentException if the given workflowUser is null or contains no id
     */
    public WorkflowDocument loadWorkflowDocument(String documentHeaderId, Person workflowUser) throws WorkflowException;

    /**
     * This method will first determine if the {@link WorkflowDocument#saveDocument(String)} method
     * is valid to be called. If so the method will save the document to workflows action list
     * optionally providing an annotation which will show up in the route log for this document
     * corresponding to this action taken. If the WorkflowDocument.saveDocument() method is not
     * valid to be called the system will instead call the method
     * {@link WorkflowDocumentService#saveRoutingData(WorkflowDocument)}
     *
     * @param workflowDocument
     * @param annotation
     * @throws WorkflowException
     */
    public void save(WorkflowDocument workflowDocument, String annotation) throws WorkflowException;

    /**
     * save the routing data of the document to workflow
     *
     * @param workflowDocument
     * @throws WorkflowException
     */
    public void saveRoutingData(WorkflowDocument workflowDocument) throws WorkflowException;

    /**
     * route this workflowDocument optionally providing an annotation for this action taken which
     * will show up in the route log for this document corresponding to this action taken, and
     * additionally optionally providing a list of ad hoc recipients for the document
     *
     * @param workflowDocument
     * @param annotation
     * @param adHocRecipients
     */
    public void route(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients)
            throws WorkflowException;

    /**
     * approve this workflowDocument optionally providing an annotation for this action taken which
     * will show up in the route log for this document corresponding to this action taken, and
     * additionally optionally providing a list of ad hoc recipients for the document
     *
     * @param workflowDocument
     * @param annotation
     * @param adHocRecipients
     */
    public void approve(WorkflowDocument workflowDocument, String annotation, List<AdHocRouteRecipient> adHocRecipients)
            throws WorkflowException;

    /**
     * super user approve this workflowDocument optionally providing an annotation for this action
     * taken which will show up in the route log for this document corresponding to this action
     * taken
     *
     * @param workflowDocument
     * @param annotation
     */
    public void superUserApprove(WorkflowDocument workflowDocument, String annotation) throws WorkflowException;

    /**
     * super user cancel this workflowDocument optionally providing an annotation for this action
     * taken which will show up in the route log for this document corresponding to this action
     * taken
     *
     * @param workflowDocument
     * @param annotation
     * @throws WorkflowException
     */
    public void superUserCancel(WorkflowDocument workflowDocument, String annotation) throws WorkflowException;

    /**
     * super user disapprove this workflowDocument optionally providing an annotation for this
     * action taken which will show up in the route log for this document corresponding to this
     * action taken
     *
     * @param workflowDocument
     * @param annotation
     * @throws WorkflowException
     */
    public void superUserDisapprove(WorkflowDocument workflowDocument, String annotation) throws WorkflowException;

    /**
     * disapprove this workflowDocument optionally providing an annotation for this action taken
     * which will show up in the route log for this document corresponding to this action taken
     *
     * @param workflowDocument
     * @param annotation
     */
    public void disapprove(WorkflowDocument workflowDocument, String annotation) throws WorkflowException;

    /**
     * cancel this workflowDocument optionally providing an annotation for this action taken which
     * will show up in the route log for this document corresponding to this action taken
     *
     * @param workflowDocument
     * @param annotation
     */
    public void cancel(WorkflowDocument workflowDocument, String annotation) throws WorkflowException;

    /**
     * acknowledge this workflowDocument optionally providing an annotation for this action taken
     * which will show up in the route log for this document corresponding to this action taken,
     * additionally optionally providing a list of ad hoc recipients for this document which should
     * be restricted to actions requested of acknowledge or fyi as all other action request types
     * will be discarded
     *
     * @param workflowDocument
     * @param annotation
     * @param adHocRecipients
     */
    public void acknowledge(WorkflowDocument workflowDocument, String annotation,
            List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException;

    /**
     * blanket approve this document optionally providing an annotation for this action taken which
     * will show up in the route log for this document corresponding to this action taken, and
     * additionally optionally providing a list of ad hoc recipients for this document which should
     * be restricted to actions requested of acknowledge or fyi as all other action request types
     * will be discarded.
     *
     * @param workflowDocument
     * @param annotation
     * @param adHocRecipients
     */
    public void blanketApprove(WorkflowDocument workflowDocument, String annotation,
            List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException;

    /**
     * clear the fyi request for this document, optinoally providing a list of ad hoc recipients for
     * this document which should be restricted to actions requested of fyi as all other action
     * request types will be discarded
     *
     * @param workflowDocument
     * @param adHocRecipients
     */
    public void clearFyi(WorkflowDocument workflowDocument, List<AdHocRouteRecipient> adHocRecipients)
            throws WorkflowException;

    /**
     * Gets the current route level name of the workflow document even if document has no active
     * node names. Allows for getting the node name of a document already in a final status.
     *
     * @param workflowDocument
     * @return node name of the current node if only one or list of node names separated by string
     *         ", " if more than one current node name
     * @throws WorkflowException
     */
    public String getCurrentRouteLevelName(WorkflowDocument workflowDocument) throws WorkflowException;

    /**
     * Sends workflow notification to the list of ad hoc recipients. This method is usually used to
     * notify users of a note that has been added to a document. The notificationLabel parameter is
     * used to give the request a custom label in the user's Action List
     *
     * @param workflowDocument
     * @param annotation
     * @param adHocRecipients
     * @param notificationLabel
     * @throws WorkflowException
     */
    public void sendWorkflowNotification(WorkflowDocument workflowDocument, String annotation,
            List<AdHocRouteRecipient> adHocRecipients, String notificationLabel) throws WorkflowException;

    /**
     * Sends workflow notification to the list of ad hoc recipients. This method is usually used to
     * notify users of a note that has been added to a document
     *
     * @param workflowDocument
     * @param annotation
     * @param adHocRecipients
     * @throws WorkflowException
     */
    public void sendWorkflowNotification(WorkflowDocument workflowDocument, String annotation,
            List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException;

    /**
     * Returns the current node names of the document delimited by {@code ", "} if there is more
     * than one.
     */
    public String getCurrentRouteNodeNames(WorkflowDocument workflowDocument);

    /**
     * Completes document
     *
     * @param workflowDocument
     * @param annotation
     * @param adHocRecipients
     */
    public void complete(WorkflowDocument workflowDocument, String annotation, List adHocRecipients) throws WorkflowException;

    /**
     * recall this workflowDocument optionally providing an annotation for this action taken which
     * will show up in the route log for this document corresponding to this action taken
     *
     * @param workflowDocument
     * @param annotation
     */
    public void recall(WorkflowDocument workflowDocument, String annotation, boolean cancel) throws WorkflowException;
}
