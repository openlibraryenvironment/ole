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

import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.document.authorization.PessimisticLock;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.util.NoteType;
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator;

import java.util.List;


/**
 * This is the Document interface. All entities that are regarded as "eDocs" in the system, including Maintenance documents and
 * Transaction Processing documents should implement this interface as it defines methods that are necessary to interact with the
 * underlying frameworks and components (i.e. notes, attachments, workflow, etc).
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Document extends PersistableBusinessObject {

    /**
     * This retrieves the standard {@code DocumentHeader} object, which contains standard meta-data about a document.
     *
     * @return document header since all docs will have a document header
     */
    DocumentHeader getDocumentHeader();

    /**
     * Sets the associated {@code DocumentHeader} for this document.
     *
     * @param documentHeader
     */
    void setDocumentHeader(DocumentHeader documentHeader);

    /**
     * All documents have a document header id. This is the quick accessor to that unique identifier and should return the same
     * value as documentHeader.getDocumentHeaderId().
     *
     * @return doc header id
     */
    String getDocumentNumber();

    /**
     * setter for document header id
     *
     * @param documentHeaderId
     */
    void setDocumentNumber(String documentHeaderId);

    /**
     * This is the method to integrate with workflow, where we will actually populate the workflow defined data structure(s) so that
     * workflow can routed based on this data. This method is responsible for passing over the proper Kuali (client system) data
     * that will be used by workflow to determine how the document is actually routed.
     */
    void populateDocumentForRouting();

    /**
     * This is a method where we can get the xml of a document that the workflow system will use to base it's routing and search
     * attributes on.
     *
     * @return the document serialized to an xml string
     */
    String serializeDocumentToXml();

    /**
     * This method is used to get the xml that should be used in a Route Report.  In it's default implementation this will call the
     * methods prepareForSave() and populateDocumentForRouting().
     */
    String getXmlForRouteReport();

    /**
     * method to integrate with workflow, where we will actually handle the transitions of levels for documents
     */
    void doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent);

    /**
     * method to integrate with workflow where we will be able to perform logic for an action taken being performed on a document
     */
    void doActionTaken(ActionTakenEvent event);

    /**
     * method to integrate with workflow where we will be able to perform logic after an action taken being performed on a document
     * @since 2.1
     */
    public void afterActionTaken(ActionType performed, ActionTakenEvent event);

    /**
     * This method will be called after the Workflow engine has completely finished processing a document.
     *
     * @param successfullyProcessed - true if the document was processed successfully, false otherwise
     */
    void afterWorkflowEngineProcess(boolean successfullyProcessed);

    /**
     * This method will be called before the Workflow engine has begun processing a document.
     */
    void beforeWorkflowEngineProcess();

    /**
     * This method will be called before the Workflow engine has begun processing a document.
     */
    List<String> getWorkflowEngineDocumentIdsToLock();

    /**
     * Getter method to get the document title as it will appear in and be searchable in workflow.
     */
    String getDocumentTitle();

    /**
     * getter method to get the list of ad hoc route persons associated with a document at a point in time, this list is only valid
     * for a given users version of a document as this state is only persisted in workflow itself when someone takes an action on a
     * document
     */
    List<AdHocRoutePerson> getAdHocRoutePersons();

    /**
     * getter method to get the list of ad hoc route workgroups associated with a document at a point in time, this list is only
     * valid for a given users version of a document as this state is only persisted in workflow itself when someone takes an action
     * on a document
     */
    List<AdHocRouteWorkgroup> getAdHocRouteWorkgroups();

    /**
     * setter method to set the list of ad hoc route persons associated with a document at a point in time, this list is only valid
     * for a given users version of a document as this state is only persisted in workflow itself when someone takes an action on a
     * document
     *
     * @param adHocRoutePersons
     */
    void setAdHocRoutePersons(List<AdHocRoutePerson> adHocRoutePersons);

    /**
     * setter method to set the list of ad hoc route workgroups associated with a document at a point in time, this list is only
     * valid for a given users version of a document as this state is only persisted in workflow itself when someone takes an action
     * on a document
     *
     * @param adHocRouteWorkgroups
     */
    void setAdHocRouteWorkgroups(List<AdHocRouteWorkgroup> adHocRouteWorkgroups);

    /**
     * This method provides a hook that will be called before the document is saved. This method is useful for applying document
     * level data to children. For example, if someone changes data at the document level, and that data needs to be propagated to
     * child objects or child lists of objects, you can use this method to update the child object or iterate through the list of
     * child objects and apply the document level data to them. Any document that follows this paradigm will need to make use of
     * this method to apply all of those changes.
     */
    void prepareForSave();

    /**
     * Sends document off to the rules engine to verify business rules.
     *
     * @param event - indicates which document event was requested
     * @throws org.kuali.rice.krad.exception.ValidationException - containing the MessageMap from the validation session.
     */
    void validateBusinessRules(KualiDocumentEvent event);

    /**
     * Do any work on the document that requires the KualiDocumentEvent before the save.
     *
     * @param event - indicates which document event was requested
     */
    void prepareForSave(KualiDocumentEvent event);

    /**
     * Do any work on the document after the save.
     *
     * @param event - indicates which document event was requested
     */
    void postProcessSave(KualiDocumentEvent event);

    /**
     * This method provides a hook that will be called after a document is retrieved, but before it is returned from the
     * DocumentService.
     */
    void processAfterRetrieve();

    /**
     * This method returns whether or not this document can be copied.
     *
     * @return True if it can be copied, false if not.
     */
    boolean getAllowsCopy();

    /**
     * Generate any necessary events required during the save event generation
     *
     * @return a list of document events that were triggered by the save event
     */
    List<KualiDocumentEvent> generateSaveEvents();

   /**
     * Handle the doRouteStatusChange event from the post processor
     *
     */
   void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent);

    /**
     * Returns the note type which should be used for notes associated with this document.
     * This method should never return null.
     *
     * @return the note type supported by this document, this value should never be null
     */
    NoteType getNoteType();

    /**
     * Return the target PersistableBusinessObject that notes associated with this document should be attached to.
     * In general, this method should never return null.  However, it is permissible that it will return a 
     * business object which has not been persisted yet (and therefore does not have it's unique object id
     * established).  This is only valid in cases where the note type is {@link NoteType#BUSINESS_OBJECT}.
     *
     * In these cases it's the responsibility for implementers of the Document interface to handle storing transient
     * copies of the document notes (in XML or otherwise) until the underlying note target has been persisted and can be attached
     * to the document's notes via it's object id.
     *
     * @return the PersistableBusinessObject with which notes on this document should be associated
     */
    PersistableBusinessObject getNoteTarget();

    /**
     * Adds the given Note to the document's list of Notes.
     *
     * @param note the Note to add, must be non-null
     */
    void addNote(Note note);

    /**
     * Returns a mutable list of all notes on the document.
     *
     * @return the list of notes associated with this document, if this document has no notes then an empty list will be returned
     */
    List<Note> getNotes();

    /**
	 * Sets the document's list of notes to the given list.
	 *
	 * @param notes the list of notes to set on the document, must be non-null
	 */
    void setNotes(List<Note> notes);

    /**
     * Retrieves the note at the given index.
     *
     * @param index the zero-based index of the note to retrieve
     * @return the note located at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    Note getNote(int index);

    /**
     * Removes the given note from the document's list of notes.
     *
     * @param note the note to remove from the document's list of notes, must be non-null
     * @return true if the note was successfully removed, false if the list did not contain the given note
     */
    boolean removeNote(Note note);

    /**
     * This method gets a list of the {@link PessimisticLock} objects associated with this document
     *
     */
    List<PessimisticLock> getPessimisticLocks();

    /**
     * This method updates the list of {@link PessimisticLock} objects on the document if changes could
     * have been made
     */
    void refreshPessimisticLocks();

    /**
     * This method adds a new {@link PessimisticLock} to the document
     *
     * NOTE: LOCKS ADDED VIA THIS METHOD WILL NOT BE SAVED WITH THE DOCUMENT
     *
     * @param lock - the lock to add to the document
     */
    void addPessimisticLock(PessimisticLock lock);

    /**
     * This is a method that is used by Kuali Pessimistic Locking to get the names (method to call values)
     * of the {@link KualiDocumentActionBase} methods that should release locks
     *
     * @return the list of method names of an action that should clear locks for the current user
     */
    List<String> getLockClearningMethodNames();
    /**
     * Returns an evaluator object that determines whether a given property relative to the root object ({@link #wrapDocumentWithMetadataForXmlSerialization()}
     * is serializable during the document serialization process.
     *
     * @return a fully initialized evaluator object, ready to be used for workflow routing
     *
     * @see org.kuali.rice.krad.service.DocumentSerializerService
     * @see #wrapDocumentWithMetadataForXmlSerialization()
     */

    String getBasePathToDocumentDuringSerialization();

    /**
     * Returns an evaluator object that determines whether a given property relative to the root object ({@link #wrapDocumentWithMetadataForXmlSerialization()}
     * is serializable during the document serialization process.
     *
     * @return a fully initialized evaluator object, ready to be used for workflow routing
     *
     * @see org.kuali.rice.krad.service.DocumentSerializerService
     * @see #wrapDocumentWithMetadataForXmlSerialization()
     */
    PropertySerializabilityEvaluator getDocumentPropertySerizabilityEvaluator();

    /**
     * This method will return the root object to be serialized for workflow routing.  If necessary, this method will wrap this document object with a wrapper (i.e. contains a reference back to this document).  This
     * wrapper may also contain references to additional objects that provide metadata useful to the workflow engine.
     *
     * If no wrappers are necessary, then this object may return "this"
     *
     * @return a wrapper object (most likely containing a reference to "this"), or "this" itself.
     * @see org.kuali.rice.krad.workflow.KualiDocumentXmlMaterializer
     */
    Object wrapDocumentWithMetadataForXmlSerialization();

    /**
     * This method returns whether or not this document supports custom lock descriptors for pessimistic locking.
     *
     * @return True if the document can generate custom lock descriptors, false otherwise.
     * @see #getCustomLockDescriptor(Person)
     */
    boolean useCustomLockDescriptors();

    /**
     * Generates a custom lock descriptor for pessimistic locking. This method should not be called unless {@link #useCustomLockDescriptors()} returns true.
     *
     * @param user The user trying to establish the lock.
     * @return A String representing the lock descriptor.
     * @see #useCustomLockDescriptors()
     * @see org.kuali.rice.krad.service.PessimisticLockService
     */
    String getCustomLockDescriptor(Person user);
}
