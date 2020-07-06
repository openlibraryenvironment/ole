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
import org.apache.log4j.Logger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.WorkflowAttributes;
import org.kuali.rice.krad.datadictionary.WorkflowProperties;
import org.kuali.rice.krad.document.authorization.PessimisticLock;
import org.kuali.rice.krad.exception.PessimisticLockingException;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.AttachmentService;
import org.kuali.rice.krad.service.DocumentSerializerService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.NoteService;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.NoteType;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.documentserializer.AlwaysFalsePropertySerializabilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.AlwaysTruePropertySerializibilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.BusinessObjectPropertySerializibilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator;
import org.kuali.rice.krad.workflow.DocumentInitiator;
import org.kuali.rice.krad.workflow.KualiDocumentXmlMaterializer;
import org.kuali.rice.krad.workflow.KualiTransactionalDocumentInformation;
import org.springframework.util.AutoPopulatingList;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @see Document
 */
@MappedSuperclass
public abstract class DocumentBase extends PersistableBusinessObjectBase implements Document {
    private static final Logger LOG = Logger.getLogger(DocumentBase.class);

    @Id @Column(name = "DOC_HDR_ID")
    protected String documentNumber;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @JoinColumn(
            name = "DOC_HDR_ID", insertable = false, updatable = false)
    protected DocumentHeader documentHeader;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @JoinColumn(
            name = "DOC_HDR_ID", insertable = false, updatable = false)
    private List<PessimisticLock> pessimisticLocks;

    @Transient
    private List<AdHocRoutePerson> adHocRoutePersons;
    @Transient
    private List<AdHocRouteWorkgroup> adHocRouteWorkgroups;
    @Transient
    private List<Note> notes;

    private transient NoteService noteService;
    private transient AttachmentService attachmentService;

    /**
     * Constructs a DocumentBase.java.
     */
    public DocumentBase() {
        try {
            // create a new document header object
            Class<? extends DocumentHeader> documentHeaderClass =
                    KRADServiceLocatorWeb.getDocumentHeaderService().getDocumentHeaderBaseClass();
            setDocumentHeader(documentHeaderClass.newInstance());
            pessimisticLocks = new ArrayList<PessimisticLock>();
            adHocRoutePersons = new ArrayList<AdHocRoutePerson>();
            adHocRouteWorkgroups = new ArrayList<AdHocRouteWorkgroup>();
            notes = new ArrayList<Note>();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error instantiating DocumentHeader", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Error instantiating DocumentHeader", e);
        }
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getAllowsCopy()
     */
    public boolean getAllowsCopy() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Retrieves the title of the document
     *
     * <p>
     * This is the default document title implementation. It concatenates the document's data dictionary file label
     * attribute and
     * the document's document header description together. This title is used to populate workflow and will show up in
     * document
     * search results and user action lists.
     * </p>
     *
     * return String representing the title of the document
     *
     * @see org.kuali.rice.krad.document.Document#getDocumentTitle()
     */
    public String getDocumentTitle() {
        String documentTypeLabel = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(
                this.getDocumentHeader().getWorkflowDocument().getDocumentTypeName()).getLabel();
        if (null == documentTypeLabel) {
            documentTypeLabel = "";
        }

        String description = this.getDocumentHeader().getDocumentDescription();
        if (null == description) {
            description = "";
        }

        return documentTypeLabel + " - " + description;
    }

    /**
     * Uses the persistence service's implementation of OJB's retrieveNonKey() fields method.
     *
     * @see org.kuali.rice.krad.bo.BusinessObject#refresh()
     */
    @Override
    public void refresh() {
        KRADServiceLocator.getPersistenceService().retrieveNonKeyFields(this);
    }

    /**
     * Checks to see if the objectId value is empty. If so, it will try to refresh the object from the DB.
     *
     * @see org.kuali.rice.krad.document.Document#refreshIfEmpty()
     */
    public void refreshIfEmpty() {
        if (null == this.getDocumentHeader()) {
            this.refresh();
        } else if (StringUtils.isEmpty(this.getDocumentHeader().getObjectId())) {
            this.refresh();
        }
    }

    /**
     * Uses the persistence service to retrieve a reference object of a parent.
     *
     * @see org.kuali.rice.krad.document.Document#refreshReferenceObject(java.lang.String)
     */
    @Override
    public void refreshReferenceObject(String referenceObjectName) {
        KRADServiceLocator.getPersistenceService().retrieveReferenceObject(this, referenceObjectName);
    }

    /**
     * @see org.kuali.rice.krad.document.Document#prepareForSave()
     */
    public void prepareForSave() {
        // do nothing
    }

    /**
     * @see org.kuali.rice.krad.document.Document#processAfterRetrieve()
     */
    public void processAfterRetrieve() {
        // do nothing
    }

    /**
     * The the default implementation for RouteLevelChange does nothing, but is meant to provide a hook for documents to
     * implement
     * for other needs.
     *
     * @see org.kuali.rice.krad.document.Document#doRouteLevelChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange)
     */
    public void doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) {
        // do nothing
    }

    /**
     * @see org.kuali.rice.krad.document.Document#doActionTaken(org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    public void doActionTaken(ActionTakenEvent event) {
        if ((KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(
                this.getClass().getName()).getUseWorkflowPessimisticLocking()) && (!getNonLockingActionTakenCodes()
                .contains(event.getActionTaken().getActionTaken().getCode()))) {
            KRADServiceLocatorWeb.getPessimisticLockService().establishWorkflowPessimisticLocking(this);
        }
    }

    /**
     * @see org.kuali.rice.krad.document.Document#afterActionTaken(ActionType, org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    public void afterActionTaken(ActionType performed, ActionTakenEvent event) {
        // do nothing
    }

    protected List<String> getNonLockingActionTakenCodes() {
        List<String> actionTakenStatusCodes = new ArrayList<String>();
        actionTakenStatusCodes.add(KewApiConstants.ACTION_TAKEN_SAVED_CD);
        actionTakenStatusCodes.add(KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD);
        actionTakenStatusCodes.add(KewApiConstants.ACTION_TAKEN_FYI_CD);
        actionTakenStatusCodes.add(KewApiConstants.ACTION_TAKEN_DENIED_CD);
        actionTakenStatusCodes.add(KewApiConstants.ACTION_TAKEN_CANCELED_CD);
        actionTakenStatusCodes.add(KewApiConstants.ACTION_TAKEN_LOG_DOCUMENT_ACTION_CD);
        return actionTakenStatusCodes;
    }

    /**
     * The the default implementation for afterWorkflowEngineProcess does nothing, but is meant to provide a hook for
     * documents to implement for other needs.
     *
     * @see org.kuali.rice.krad.document.Document#afterWorkflowEngineProcess(boolean)
     */
    public void afterWorkflowEngineProcess(boolean successfullyProcessed) {
        if (KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(
                this.getClass().getName()).getUseWorkflowPessimisticLocking()) {
            if (successfullyProcessed) {
                KRADServiceLocatorWeb.getPessimisticLockService().releaseWorkflowPessimisticLocking(this);
            }
        }
    }

    /**
     * The the default implementation for beforeWorkflowEngineProcess does nothing, but is meant to provide a hook for
     * documents to implement for other needs.
     *
     * @see org.kuali.rice.krad.document.Document#beforeWorkflowEngineProcess()
     */
    public void beforeWorkflowEngineProcess() {
        // do nothing
    }

    /**
     * The default implementation returns no additional ids for the workflow engine to lock prior to processing.
     *
     * @see org.kuali.rice.krad.document.Document#getWorkflowEngineDocumentIdsToLock()
     */
    public List<String> getWorkflowEngineDocumentIdsToLock() {
        return null;
    }

    /**
     * @see org.kuali.rice.krad.document.Copyable#toCopy()
     */
    public void toCopy() throws WorkflowException, IllegalStateException {
        if (!this.getAllowsCopy()) {
            throw new IllegalStateException(this.getClass().getName() + " does not support document-level copying");
        }
        String sourceDocumentHeaderId = getDocumentNumber();
        setNewDocumentHeader();

        getDocumentHeader().setDocumentTemplateNumber(sourceDocumentHeaderId);

        //clear out notes from previous bo
        this.notes.clear();
        addCopyErrorDocumentNote("copied from document " + sourceDocumentHeaderId);
    }

    /**
     * Gets a new document header for this documents type and sets in the document instance.
     *
     * @throws WorkflowException
     */
    protected void setNewDocumentHeader() throws WorkflowException {
        TransactionalDocument newDoc =
                (TransactionalDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument(
                        getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        newDoc.getDocumentHeader().setDocumentDescription(getDocumentHeader().getDocumentDescription());
        newDoc.getDocumentHeader().setOrganizationDocumentNumber(getDocumentHeader().getOrganizationDocumentNumber());

        try {
            ObjectUtils.setObjectPropertyDeep(this, KRADPropertyConstants.DOCUMENT_NUMBER, documentNumber.getClass(),
                    newDoc.getDocumentNumber());
        } catch (Exception e) {
            LOG.error("Unable to set document number property in copied document " + e.getMessage(), e);
            throw new RuntimeException("Unable to set document number property in copied document " + e.getMessage(),
                    e);
        }

        // replace current documentHeader with new documentHeader
        setDocumentHeader(newDoc.getDocumentHeader());
    }

    /**
     * Adds a note to the document indicating it was created by a copy or error correction.
     *
     * @param noteText - text for note
     */
    protected void addCopyErrorDocumentNote(String noteText) {
        Note note = null;
        try {
            note = KRADServiceLocatorWeb.getDocumentService().createNoteFromDocument(this, noteText);
        } catch (Exception e) {
            logErrors();
            throw new RuntimeException("Couldn't create note on copy or error", e);
        }
        addNote(note);
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getXmlForRouteReport()
     */
    public String getXmlForRouteReport() {
        prepareForSave();
        populateDocumentForRouting();
        return getDocumentHeader().getWorkflowDocument().getApplicationContent();
    }

    /**
     * @see org.kuali.rice.krad.document.Document#populateDocumentForRouting()
     */
    public void populateDocumentForRouting() {
        getDocumentHeader().getWorkflowDocument().setApplicationContent(serializeDocumentToXml());
    }

    /**
     * @see org.kuali.rice.krad.document.Document#serializeDocumentToXml()
     */
    public String serializeDocumentToXml() {
        DocumentSerializerService documentSerializerService = KRADServiceLocatorWeb.getDocumentSerializerService();
        String xml = documentSerializerService.serializeDocumentToXmlForRouting(this);
        return xml;
    }

    /**
     * Wraps a document in an instance of KualiDocumentXmlMaterializer, that provides additional metadata for
     * serialization
     *
     * @see org.kuali.rice.krad.document.Document#wrapDocumentWithMetadataForXmlSerialization()
     */
    public KualiDocumentXmlMaterializer wrapDocumentWithMetadataForXmlSerialization() {
        KualiTransactionalDocumentInformation transInfo = new KualiTransactionalDocumentInformation();
        DocumentInitiator initiator = new DocumentInitiator();
        String initiatorPrincipalId = getDocumentHeader().getWorkflowDocument().getDocument().getInitiatorPrincipalId();
        Person initiatorUser = KimApiServiceLocator.getPersonService().getPerson(initiatorPrincipalId);
        initiator.setPerson(initiatorUser);
        transInfo.setDocumentInitiator(initiator);
        KualiDocumentXmlMaterializer xmlWrapper = new KualiDocumentXmlMaterializer();
        xmlWrapper.setDocument(this);
        xmlWrapper.setKualiTransactionalDocumentInformation(transInfo);
        return xmlWrapper;
    }

    /**
     * If workflowProperties have been defined within the data dictionary for this document, then it returns an instance
     * of
     * {@link BusinessObjectPropertySerializibilityEvaluator} initialized with the properties.  If none have been
     * defined, then returns
     * {@link AlwaysTruePropertySerializibilityEvaluator}.
     *
     * @see org.kuali.rice.krad.document.Document#getDocumentPropertySerizabilityEvaluator()
     */
    public PropertySerializabilityEvaluator getDocumentPropertySerizabilityEvaluator() {
        String docTypeName = getDocumentHeader().getWorkflowDocument().getDocumentTypeName();
        DocumentEntry documentEntry =
                KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(docTypeName);
        WorkflowProperties workflowProperties = documentEntry.getWorkflowProperties();
        WorkflowAttributes workflowAttributes = documentEntry.getWorkflowAttributes();
        return createPropertySerializabilityEvaluator(workflowProperties, workflowAttributes);
    }

    protected PropertySerializabilityEvaluator createPropertySerializabilityEvaluator(
            WorkflowProperties workflowProperties, WorkflowAttributes workflowAttributes) {
        if (workflowAttributes != null) {
            return new AlwaysFalsePropertySerializabilityEvaluator();
        }
        if (workflowProperties == null) {
            return new AlwaysTruePropertySerializibilityEvaluator();
        }
        PropertySerializabilityEvaluator evaluator = new BusinessObjectPropertySerializibilityEvaluator();
        evaluator.initializeEvaluatorForDocument(this);
        return evaluator;
    }

    /**
     * Returns the POJO property name of "this" document in the object returned by {@link
     * #wrapDocumentWithMetadataForXmlSerialization()}
     *
     * @see org.kuali.rice.krad.document.Document#getBasePathToDocumentDuringSerialization()
     */
    public String getBasePathToDocumentDuringSerialization() {
        return "document";
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getDocumentHeader()
     */
    public DocumentHeader getDocumentHeader() {
        return this.documentHeader;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#setDocumentHeader(org.kuali.rice.krad.document.DocumentHeader)
     */
    public void setDocumentHeader(DocumentHeader documentHeader) {
        this.documentHeader = documentHeader;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getDocumentNumber()
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#setDocumentNumber(java.lang.String)
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getAdHocRoutePersons()
     */
    public List<AdHocRoutePerson> getAdHocRoutePersons() {
        return adHocRoutePersons;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#setAdHocRoutePersons(java.util.List)
     */
    public void setAdHocRoutePersons(List<AdHocRoutePerson> adHocRoutePersons) {
        this.adHocRoutePersons = adHocRoutePersons;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getAdHocRouteWorkgroups()
     */
    public List<AdHocRouteWorkgroup> getAdHocRouteWorkgroups() {
        return adHocRouteWorkgroups;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#setAdHocRouteWorkgroups(java.util.List)
     */
    public void setAdHocRouteWorkgroups(List<AdHocRouteWorkgroup> adHocRouteWorkgroups) {
        this.adHocRouteWorkgroups = adHocRouteWorkgroups;
    }

    public void postProcessSave(KualiDocumentEvent event) {
        // TODO Auto-generated method stub

    }

    /**
     * Override this method with implementation specific prepareForSave logic
     *
     * @see org.kuali.rice.krad.document.Document#prepareForSave(org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent)
     */
    public void prepareForSave(KualiDocumentEvent event) {
        // do nothing by default
    }

    public void validateBusinessRules(KualiDocumentEvent event) {
       if (GlobalVariables.getMessageMap().hasErrors()) {
            logErrors();
           GlobalVariables.getMessageMap().clearErrorMessages();;
           GlobalVariables.getMessageMap().clearErrorPath();
           throw new ValidationException("errors occured before business rule");
        }

        // perform validation against rules engine
        LOG.info("invoking rules engine on document " + getDocumentNumber());
        boolean isValid = true;
        isValid = KRADServiceLocatorWeb.getKualiRuleService().applyRules(event);

        // check to see if the br eval passed or failed
        if (!isValid) {
            logErrors();
            // TODO: better error handling at the lower level and a better error message are
            // needed here
            throw new ValidationException("business rule evaluation failed");
        } else if (GlobalVariables.getMessageMap().hasErrors()) {
            logErrors();
            throw new ValidationException(
                    "Unreported errors occured during business rule evaluation (rule developer needs to put meaningful error messages into global ErrorMap)");
        }
        LOG.debug("validation completed");

    }

    /**
     * This method logs errors.
     */
    protected void logErrors() {
        if (LOG.isInfoEnabled()) {
            if (GlobalVariables.getMessageMap().hasErrors()) {

                for (Iterator<Map.Entry<String, AutoPopulatingList<ErrorMessage>>> i =
                             GlobalVariables.getMessageMap().getAllPropertiesAndErrors().iterator(); i.hasNext(); ) {
                    Map.Entry<String, AutoPopulatingList<ErrorMessage>> e = i.next();

                    StringBuffer logMessage = new StringBuffer();
                    logMessage.append("[" + e.getKey() + "] ");
                    boolean first = true;

                    AutoPopulatingList<ErrorMessage> errorList = e.getValue();
                    for (Iterator<ErrorMessage> j = errorList.iterator(); j.hasNext(); ) {
                        ErrorMessage em = j.next();

                        if (first) {
                            first = false;
                        } else {
                            logMessage.append(";");
                        }
                        logMessage.append(em);
                    }

                    LOG.info(logMessage);
                }
            }
        }
    }

    /**
     * Hook for override
     *
     * @see org.kuali.rice.krad.document.Document#generateSaveEvents()
     */
    public List<KualiDocumentEvent> generateSaveEvents() {
        return new ArrayList<KualiDocumentEvent>();
    }

    /**
     * @see org.kuali.rice.krad.document.Document#doRouteStatusChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange)
     */
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        // do nothing
    }

    /**
     * Returns the business object with which notes related to this document should be associated.
     * By default, the {@link DocumentHeader} of this document will be returned as the note target.
     *
     * <p>Sub classes can override this method if they want notes to be associated with something
     * other than the document header.  If this method is overridden, the {@link #getNoteType()}
     * method should be overridden to return {@link NoteType#BUSINESS_OBJECT}
     *
     * @return Returns the documentBusinessObject.
     */
    @Override
    public PersistableBusinessObject getNoteTarget() {
        return getDocumentHeader();
    }

    /**
     * Returns the {@link NoteType} to use for notes associated with this document.
     * By default this returns {@link NoteType#DOCUMENT_HEADER} since notes are
     * associated with the {@link DocumentHeader} record by default.
     *
     * <p>The case in which this should be overridden is if {@link #getNoteTarget()} is
     * overridden to return an object other than the DocumentHeader.
     *
     * @return the note type to use for notes associated with this document
     * @see org.kuali.rice.krad.document.Document#getNoteType()
     */
    @Override
    public NoteType getNoteType() {
        return NoteType.DOCUMENT_HEADER;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#addNote(org.kuali.rice.krad.bo.Note)
     */
    @Override
    public void addNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null.");
        }
        notes.add(note);
    }

    /**
     * @see org.kuali.rice.krad.document.Document#removeNote(org.kuali.rice.krad.bo.Note)
     */
    @Override
    public boolean removeNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null.");
        }
        return notes.remove(note);
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getNote(int)
     */
    @Override
    public Note getNote(int index) {
        return notes.get(index);
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getNotes()
     */
    @Override
    public List<Note> getNotes() {
        if (CollectionUtils.isEmpty(notes) && getNoteType().equals(NoteType.BUSINESS_OBJECT) && StringUtils.isNotBlank(
                getNoteTarget().getObjectId())) {
            notes = getNoteService().getByRemoteObjectId(getNoteTarget().getObjectId());
        }

        return notes;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#setNotes(java.util.List)
     */
    @Override
    public void setNotes(List<Note> notes) {
        if (notes == null) {
            throw new IllegalArgumentException("List of notes must be non-null.");
        }
        this.notes = notes;
    }

    @Override
    protected void postLoad() {
        super.postLoad();
        refreshPessimisticLocks();
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getPessimisticLocks()
     */
    public List<PessimisticLock> getPessimisticLocks() {
        return this.pessimisticLocks;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#refreshPessimisticLocks()
     * @deprecated This is not needed with the relationship set up with JPA annotations
     */
    @Deprecated
    public void refreshPessimisticLocks() {
        this.pessimisticLocks.clear();
        this.pessimisticLocks = KRADServiceLocatorWeb.getPessimisticLockService().getPessimisticLocksForDocument(
                this.documentNumber);
    }

    /**
     * @param pessimisticLocks the PessimisticLock objects to set
     */
    public void setPessimisticLocks(List<PessimisticLock> pessimisticLocks) {
        this.pessimisticLocks = pessimisticLocks;
    }

    /**
     * @see org.kuali.rice.krad.document.Document#addPessimisticLock(org.kuali.rice.krad.document.authorization.PessimisticLock)
     */
    public void addPessimisticLock(PessimisticLock lock) {
        this.pessimisticLocks.add(lock);
    }

    /**
     * @see org.kuali.rice.krad.document.Document#getLockClearningMethodNames()
     */
    public List<String> getLockClearningMethodNames() {
        List<String> methodToCalls = new ArrayList<String>();
        methodToCalls.add(KRADConstants.CLOSE_METHOD);
        methodToCalls.add(KRADConstants.CANCEL_METHOD);
        //        methodToCalls.add(RiceConstants.BLANKET_APPROVE_METHOD);
        methodToCalls.add(KRADConstants.ROUTE_METHOD);
        methodToCalls.add(KRADConstants.APPROVE_METHOD);
        methodToCalls.add(KRADConstants.DISAPPROVE_METHOD);
        methodToCalls.add(KRADConstants.ACKNOWLEDGE_METHOD);
        return methodToCalls;
    }

    /**
     * This default implementation simply returns false to indicate that custom lock descriptors are not supported by
     * DocumentBase. If custom lock
     * descriptors are needed, the appropriate subclasses should override this method.
     *
     * @see org.kuali.rice.krad.document.Document#useCustomLockDescriptors()
     */
    public boolean useCustomLockDescriptors() {
        return false;
    }

    /**
     * This default implementation just throws a PessimisticLockingException. Subclasses of DocumentBase that need
     * support for custom lock descriptors
     * should override this method.
     *
     * @see org.kuali.rice.krad.document.Document#getCustomLockDescriptor(org.kuali.rice.kim.api.identity.Person)
     */
    public String getCustomLockDescriptor(Person user) {
        throw new PessimisticLockingException("Document " + getDocumentNumber() +
                " is using pessimistic locking with custom lock descriptors, but the document class has not overriden the getCustomLockDescriptor method");
    }

    protected AttachmentService getAttachmentService() {
        if (attachmentService == null) {
            attachmentService = KRADServiceLocator.getAttachmentService();
        }
        return attachmentService;
    }

    protected NoteService getNoteService() {
        if (noteService == null) {
            noteService = KRADServiceLocator.getNoteService();
        }
        return noteService;
    }
}
