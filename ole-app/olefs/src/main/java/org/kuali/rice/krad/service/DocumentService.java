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
package org.kuali.rice.krad.service;

import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.rules.rule.event.SaveEvent;

import java.util.List;

/**
 * Defines various operations that support the Document framework.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentService {

    /**
     * @param documentHeaderId
     * @return true if a document with the given documentHeaderId exists
     */
    public boolean documentExists(String documentHeaderId);

    /**
     * get a new blank document instance based on the document type name
     *
     * @param documentTypeName
     * @return
     */
    public Document getNewDocument(String documentTypeName) throws WorkflowException;

    /**
     * get a new blank document instance having the given Document class
     *
     * @param documentClass
     * @return
     */
    public Document getNewDocument(Class<? extends Document> documentClass) throws WorkflowException;

    /**
     * get a new blank document instance based on the document type name. The principal name
     * passed in will be used as the document initiator.
     *
     * @param documentTypeName
     * @param initiatorPrincipalNm
     * @return
     */
    public Document getNewDocument(String documentTypeName, String initiatorPrincipalNm) throws WorkflowException;

    /**
     * get a document based on the document header id which is the primary key for all document types
     *
     * @param documentHeaderId
     * @return
     */
    public Document getByDocumentHeaderId(String documentHeaderId) throws WorkflowException;

    public Document getBySimpleDocumentHeaderId(String documentHeaderId,String documentTypeName) throws WorkflowException;


    /**
     * get a document based on the document header id which is the primary key for all document types.  Using this method
     * does not require that GlobalVariables.getUserSession() be populated.  Therefore, this method can be used when a HTTP request
     * is not being processed (e.g. during workflow indexing/post-processing).
     *
     * @param documentHeaderId
     * @return
     */
    public Document getByDocumentHeaderIdSessionless(String documentHeaderId) throws WorkflowException;

    /**
     * This method retrieves a list of fully-populated documents given a list of document header id values.
     *
     * @param clazz
     * @param documentHeaderIds
     * @return List of fully-populated documents
     * @throws WorkflowException
     */
    public List<Document> getDocumentsByListOfDocumentHeaderIds(Class<? extends Document> documentClass, List<String> documentHeaderIds) throws WorkflowException;

    /**
     *
     * This method is to allow for documents to be updated which is currently used to update the document status as well as to allow
     * for locked docs to be unlocked
     *
     * @param document
     */
    public Document updateDocument(Document document);

    /**
     * This is a helper method that performs the same as the {@link #saveDocument(Document, Class)} method.  The convenience
     * of this method is that the event being used is the standard SaveDocumentEvent.
     *
     * @see DocumentService#saveDocument(Document, Class)
     */
    public Document saveDocument(Document document) throws WorkflowException;

    /**
     * Saves the passed-in document. This will persist it both to the Kuali database, and also initiate it (if necessary) within
     * workflow, so its available in the initiator's action list.  This method uses the passed in KualiDocumentEvent class when saving
     * the document.  The KualiDocumentEvent class must implement the {@link SaveEvent} interface.
     *
     * Note that the system does not support passing in Workflow Annotations or AdHoc Route Recipients on a SaveDocument call. These
     * are sent to workflow on a routeDocument action, or any of the others which actually causes a routing action to happen in
     * workflow.
     *
     * NOTE: This method will not check the document action flags to check if a save is valid
     *
     * @param document The document to be saved
     * @param kualiDocumentEventClass The event class to use when saving (class must implement the SaveEvent interface)
     * @return the document that was passed in
     * @throws WorkflowException
     */
    public Document saveDocument(Document document, Class<? extends KualiDocumentEvent> kualiDocumentEventClass) throws WorkflowException;

    /**
     * start the route the document for approval, optionally providing a list of ad hoc recipients, and additionally provideing a
     * annotation to show up in the route log for the document
     *
     * @param document
     * @param annotation
     * @param adHocRoutingRecipients
     * @return
     * @throws ValidationErrorList
     */
    public Document routeDocument(Document document, String annotation, List<AdHocRouteRecipient> adHocRoutingRecipients) throws WorkflowException;

    /**
     * approve this document, optionally providing an annotation which will show up in the route log for this document for this
     * action taken, and optionally providing a list of ad hoc recipients for the document
     *
     * @param document
     * @param annotation
     * @param adHocRoutingRecipients
     * @return
     * @throws ValidationErrorList
     */
    public Document approveDocument(Document document, String annotation, List<AdHocRouteRecipient> adHocRoutingRecipients) throws WorkflowException;

    /**
     * approve this document as super user, optionally providing an annotation which will show up in the route log for this document
     * for this action taken
     *
     * @param document
     * @param annotation
     * @return
     * @throws ValidationErrorList
     */
    public Document superUserApproveDocument(Document document, String annotation) throws WorkflowException;

    /**
     * cancel this document as super user, optionally providing an annotation which will show up in the route log for this document
     * for this action taken
     *
     * @param document
     * @param annotation
     * @return
     * @throws WorkflowException
     */
    public Document superUserCancelDocument(Document document, String annotation) throws WorkflowException;

    /**
     * disapprove this document as super user, optionally providing an annotation which will show up in the route log for this document
     * for this action taken
     *
     * @param document
     * @param annotation
     * @return
     * @throws WorkflowException
     */
    public Document superUserDisapproveDocument(Document document, String annotation) throws WorkflowException;

    /**
     * disapprove this document as super user, without saving, optionally providing an annotation which will show up in the route log for this document
     * for this action taken
     *
     * @param document
     * @param annotation
     * @return
     * @throws WorkflowException
     */
    public Document superUserDisapproveDocumentWithoutSaving(Document document, String annotation) throws WorkflowException;


    /**
     * disapprove this document, optionally providing an annotation for the disapproval which will show up in the route log for the
     * document for this action taken
     *
     * @param document
     * @param annotation
     * @return Document
     * @throws Exception
     */
    public Document disapproveDocument(Document document, String annotation) throws Exception;

    /**
     * cancel this document, optionally providing an annotation for the disapproval which will show up in the route log for the
     * document for this action taken
     *
     * @param document
     * @param annotation
     * @return
     */
    public Document cancelDocument(Document document, String annotation) throws WorkflowException;

    /**
     * acknowledge this document, optionally providing an annotation for the acknowledgement which will show up in the route log for
     * the document for this acknowledgement, additionally optionally provide a list of ad hoc recipients that should recieve this
     * document. The list of ad hoc recipients for this document should have an action requested of acknowledge or fyi as all other
     * actions requested will be discarded as invalid based on the action being taken being an acknowledgement.
     *
     * @param document
     * @param annotation
     * @param adHocRecipients
     * @return
     */
    public Document acknowledgeDocument(Document document, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException;

    /**
     * blanket approve this document which will approve the document and stand in for an approve for all typically generated
     * approval actions requested for this document. The user must have blanket approval authority for this document by being
     * registered as a user in the blanket approval workgroup that is associated with this document type. Optionally an annotation
     * can be provided which will show up for this action taken on the document in the route log. Additionally optionally provide a
     * list of ad hoc recipients for this document, which should be restricted to actions requested of acknowledge and fyi as all
     * other actions requested will be discarded
     *
     * @param document
     * @param annotation
     * @param adHocRecipients
     * @return
     * @throws ValidationErrorList
     */
    public Document blanketApproveDocument(Document document, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException;

    /**
     * clear the fyi request for this document, optionally providing a list of ad hoc recipients for this document, which should be
     * restricted to action requested of fyi as all other actions requested will be discarded
     *
     * @param document
     * @param adHocRecipients
     * @return
     */
    public Document clearDocumentFyi(Document document, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException;

    /**
     * Sets the title and app document id in the workflow document
     *
     * @param document
     * @throws WorkflowException
     */
    public void prepareWorkflowDocument(Document document) throws WorkflowException;


    /**
     * This method creates a note from a given document and note text.  The resulting Note will
     * have it's note type set to the value of {@link Document#getNoteType()}.  Additionally, it's
     * remoteObjectId will be set to the object id of the document's note target.
     *
     * @param document the document from which to use the note type and note target when creating the note
     * @param text the text value to include in the resulting note
     * @return the note that was created
     */
    public Note createNoteFromDocument(Document document, String text);

    /**
     * Saves the notes associated with the given document if they are in a state where they can be
     * saved.  In certain cases they may not be ready to be saved.  For example, in maintenance documents
     * where the notes are associated with the business object instead of the document header, the notes
     * cannot be saved until the business object itself has been persisted.
     *
     * @param document the document for which to save notes
     * @return true if the notes were saved, false if they were not
     */
    public boolean saveDocumentNotes(Document document);

    public void sendAdHocRequests(Document document, String annotation, List<AdHocRouteRecipient> adHocRecipients) throws WorkflowException;

    /**
     * Builds an workflow notification request for the note and sends it to note recipient.
     *
     * @param document - document that contains the note
     * @param note - note to notify
     * @param sender - user who is sending the notification
     * @throws WorkflowException
     */
    public void sendNoteRouteNotification(Document document, Note note, Person sender) throws WorkflowException;

    /**
     * recall this document, optionally providing an annotation for the recall which will show up in the route log for the
     * document for this action taken
     *
     * @since 2.1
     * @param document
     * @param annotation
     * @return
     */
    public Document recallDocument(Document document, String annotation, boolean cancel) throws WorkflowException;

    /**
     * Complete action for a document
     *
     * @param document Document
     * @param annotation Annotation text
     * @param adHocRecipients list of adhoc recipients
     */
    public Document completeDocument(Document document, String annotation, List adHocRecipients) throws WorkflowException;

    /**
     * Helper method used to save and validate a document
     *
     * @param document Document
     * @param event KualiDocumentEvent
     */
    public Document validateAndPersistDocument(Document document, KualiDocumentEvent event) throws ValidationException;
}
