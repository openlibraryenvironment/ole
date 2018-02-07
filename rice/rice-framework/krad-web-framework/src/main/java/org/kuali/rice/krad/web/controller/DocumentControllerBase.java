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
package org.kuali.rice.krad.web.controller;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSessionUtils;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.bo.Attachment;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.document.DocumentAuthorizer;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.exception.UnknownDocumentIdException;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.rules.rule.event.AddNoteEvent;
import org.kuali.rice.krad.service.AttachmentService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.NoteService;
import org.kuali.rice.krad.uif.UifConstants.WorkflowAction;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.NoteType;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Base controller class for all KRAD <code>DocumentView</code> screens working
 * with <code>Document</code> models
 *
 * <p>
 * Provides default controller implementations for the standard document actions including: doc handler
 * (retrieve from doc search and action list), save, route (and other KEW actions)
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class DocumentControllerBase extends UifControllerBase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentControllerBase.class);

    // COMMAND constants which cause docHandler to load an existing document
    // instead of creating a new one
    protected static final String[] DOCUMENT_LOAD_COMMANDS =
            {KewApiConstants.ACTIONLIST_COMMAND, KewApiConstants.DOCSEARCH_COMMAND, KewApiConstants.SUPERUSER_COMMAND,
                    KewApiConstants.HELPDESK_ACTIONLIST_COMMAND};

    private BusinessObjectService businessObjectService;
    private DataDictionaryService dataDictionaryService;
    private DocumentService documentService;
    private DocumentDictionaryService documentDictionaryService;
    private AttachmentService attachmentService;
    private NoteService noteService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected abstract DocumentFormBase createInitialForm(HttpServletRequest request);

    /**
     * Used to funnel all document handling through, we could do useful things
     * like log and record various openings and status Additionally it may be
     * nice to have a single dispatcher that can know how to dispatch to a
     * redirect url for document specific handling but we may not need that as
     * all we should need is the document to be able to load itself based on
     * document id and then which action forward or redirect is pertinent for
     * the document type.
     */
    @RequestMapping(params = "methodToCall=docHandler")
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String command = form.getCommand();

        // in all of the following cases we want to load the document
        if (ArrayUtils.contains(DOCUMENT_LOAD_COMMANDS, command) && form.getDocId() != null) {
            loadDocument(form);
        } else if (KewApiConstants.INITIATE_COMMAND.equals(command)) {
            createDocument(form);
        } else {
            LOG.error("docHandler called with invalid parameters");
            throw new IllegalArgumentException("docHandler called with invalid parameters");
        }

        // TODO: authorization on document actions
        // if (KEWConstants.SUPERUSER_COMMAND.equalsIgnoreCase(command)) {
        // form.setSuppressAllButtons(true);
        // }

        return getUIFModelAndView(form);
    }

    /**
     * Loads the document by its provided document header id. This has been abstracted out so that
     * it can be overridden in children if the need arises
     *
     * @param form - form instance that contains the doc id parameter and where
     * the retrieved document instance should be set
     */
    protected void loadDocument(DocumentFormBase form) throws WorkflowException {
        String docId = form.getDocId();

        LOG.debug("Loading document" + docId);

        Document doc = null;
        doc = getDocumentService().getByDocumentHeaderId(docId);
        if (doc == null) {
            throw new UnknownDocumentIdException(
                    "Document no longer exists.  It may have been cancelled before being saved.");
        }

        WorkflowDocument workflowDocument = doc.getDocumentHeader().getWorkflowDocument();
        if (!getDocumentDictionaryService().getDocumentAuthorizer(doc).canOpen(doc,
                GlobalVariables.getUserSession().getPerson())) {
            throw buildAuthorizationException("open", doc);
        }

        // re-retrieve the document using the current user's session - remove
        // the system user from the WorkflowDcument object
        if (workflowDocument != doc.getDocumentHeader().getWorkflowDocument()) {
            LOG.warn("Workflow document changed via canOpen check");
            doc.getDocumentHeader().setWorkflowDocument(workflowDocument);
        }

        form.setDocument(doc);
        WorkflowDocument workflowDoc = doc.getDocumentHeader().getWorkflowDocument();
        form.setDocTypeName(workflowDoc.getDocumentTypeName());

        UserSessionUtils.addWorkflowDocument(GlobalVariables.getUserSession(), workflowDoc);
    }

    /**
     * Creates a new document of the type specified by the docTypeName property of the given form.
     * This has been abstracted out so that it can be overridden in children if the need arises.
     *
     * @param form - form instance that contains the doc type parameter and where
     * the new document instance should be set
     */
    protected void createDocument(DocumentFormBase form) throws WorkflowException {
        LOG.debug("Creating new document instance for doc type: " + form.getDocTypeName());
        Document doc = getDocumentService().getNewDocument(form.getDocTypeName());

        form.setDocument(doc);
        form.setDocTypeName(doc.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
    }

    /**
     * Reloads the document contained on the form from the database
     *
     * @param form - document form base containing the document instance from which the document number will
     * be retrieved and used to fetch the document from the database
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=reload")
    public ModelAndView reload(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document document = form.getDocument();

        // prepare for the reload action - set doc id and command
        form.setDocId(document.getDocumentNumber());
        form.setCommand(DOCUMENT_LOAD_COMMANDS[1]);

        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_RELOADED);

        // forward off to the doc handler
        return docHandler(form, result, request, response);
    }

    /**
     * Prompts user to confirm the cancel action then if confirmed cancels the document instance
     * contained on the form
     *
     * @param form - document form base containing the document instance that will be cancelled
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=cancel")
    @Override
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        DocumentFormBase documentForm = (DocumentFormBase) form;

        // TODO: prompt user to confirm the cancel, need question framework

        performWorkflowAction(documentForm, WorkflowAction.CANCEL, false);

        return returnToPrevious(form);
    }

    /**
     * Saves the document instance contained on the form
     *
     * @param form - document form base containing the document instance that will be saved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        performWorkflowAction(form, WorkflowAction.SAVE, true);

        return getUIFModelAndView(form);
    }

    /**
     * Completes the document instance contained on the form
     *
     * @param form - document form base containing the document instance that will be completed
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=complete")
    public ModelAndView complete(@ModelAttribute("KualiForm")
    DocumentFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        performWorkflowAction(form, WorkflowAction.COMPLETE, true);

        return getUIFModelAndView(form);
    }

    /**
     * Routes the document instance contained on the form
     *
     * @param form - document form base containing the document instance that will be routed
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        performWorkflowAction(form, WorkflowAction.ROUTE, true);

        return getUIFModelAndView(form);
    }

    /**
     * Performs the blanket approve workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be blanket approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        performWorkflowAction(form, WorkflowAction.BLANKETAPPROVE, true);

        return getUIFModelAndView(form);
    }

    /**
     * Performs the approve workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=approve")
    public ModelAndView approve(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        performWorkflowAction(form, WorkflowAction.APPROVE, true);

        return returnToPrevious(form);
    }

    /**
     * Performs the disapprove workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be disapproved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=disapprove")
    public ModelAndView disapprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO: need to prompt for disapproval note text
        performWorkflowAction(form, WorkflowAction.DISAPPROVE, true);

        return returnToPrevious(form);
    }

    /**
     * Performs the fyi workflow action on the form document instance
     *
     * @param form - document form base containing the document instance the fyi will be taken on
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=fyi")
    public ModelAndView fyi(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        performWorkflowAction(form, WorkflowAction.FYI, false);

        return returnToPrevious(form);
    }

    /**
     * Performs the acknowledge workflow action on the form document instance
     *
     * @param form - document form base containing the document instance the acknowledge will be taken on
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=acknowledge")
    public ModelAndView acknowledge(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        performWorkflowAction(form, WorkflowAction.ACKNOWLEDGE, false);

        return returnToPrevious(form);
    }

    /**
     * Sends a AdHoc Request of the document instance contained on the form to the AdHoc Recipients
     *
     * @param form - document form base containing the document instance that will be sent
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=sendAdHocRequests")
    public ModelAndView sendAdHocRequests(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        performWorkflowAction(form, WorkflowAction.SENDADHOCREQUESTS, true);

        return getUIFModelAndView(form);
    }

    /**
     * Invokes the {@link DocumentService} to carry out a request workflow action and adds a success message, if
     * requested a check for sensitive data is also performed
     *
     * @param form - document form instance containing the document for which the action will be taken on
     * @param action - {@link WorkflowAction} enum indicating what workflow action to take
     * @param checkSensitiveData - boolean indicating whether a check for sensitive data should occur
     */
    protected void performWorkflowAction(DocumentFormBase form, WorkflowAction action, boolean checkSensitiveData) {
        Document document = form.getDocument();

        LOG.debug("Performing workflow action " + action.name() + "for document: " + document.getDocumentNumber());

        // TODO: need question and prompt framework
        if (checkSensitiveData) {
            //        String viewName = checkAndWarnAboutSensitiveData(form, request, response,
            //                KRADPropertyConstants.DOCUMENT_EXPLANATION, document.getDocumentHeader().getExplanation(), "route", "");
            //        if (viewName != null) {
            //            return new ModelAndView(viewName);
            //        }
        }

        try {
            String successMessageKey = null;
            switch (action) {
                case SAVE:
                    getDocumentService().saveDocument(document);
                    successMessageKey = RiceKeyConstants.MESSAGE_SAVED;
                    break;
                case ROUTE:
                    getDocumentService().routeDocument(document, form.getAnnotation(), combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL;
                    break;
                case BLANKETAPPROVE:
                    getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                            form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                    break;
                case APPROVE:
                    getDocumentService().approveDocument(document, form.getAnnotation(), combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                    break;
                case DISAPPROVE:
                    // TODO: need to get disapprove note from user
                    String disapprovalNoteText = generateDisapprovalNote(form, checkSensitiveData);
                    getDocumentService().disapproveDocument(document, disapprovalNoteText);
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_DISAPPROVED;
                    break;
                case FYI:
                    getDocumentService().clearDocumentFyi(document, combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_FYIED;
                    break;
                case ACKNOWLEDGE:
                    getDocumentService().acknowledgeDocument(document, form.getAnnotation(), combineAdHocRecipients(
                            form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_ACKNOWLEDGED;
                    break;
                case CANCEL:
                    if (getDocumentService().documentExists(document.getDocumentNumber())) {
                        getDocumentService().cancelDocument(document, form.getAnnotation());
                        successMessageKey = RiceKeyConstants.MESSAGE_CANCELLED;
                    }
                    break;
                case COMPLETE:
                    if (getDocumentService().documentExists(document.getDocumentNumber())) {
                        getDocumentService().completeDocument(document, form.getAnnotation(), combineAdHocRecipients(form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL;
                    }
                    break;
                case SENDADHOCREQUESTS:
                    getDocumentService().sendAdHocRequests(document, form.getAnnotation(), combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL;
                    break;
            }

            if (successMessageKey != null) {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, successMessageKey);
            }
        } catch (ValidationException e) {
            // if errors in map, swallow exception so screen will draw with errors
            // if not then throw runtime because something bad happened
            if (GlobalVariables.getMessageMap().hasNoErrors()) {
                throw new RiceRuntimeException("Validation Exception with no error message.", e);
            }
        } catch (Exception e) {
            throw new RiceRuntimeException(
                    "Exception trying to invoke action " + action.name() + "for document: " + document
                            .getDocumentNumber(), e);
        }

        form.setAnnotation("");
    }

    /**
     * Redirects to the supervisor functions page
     *
     * @return ModelAndView - model and view configured for the redirect URL
     */
    @RequestMapping(params = "methodToCall=supervisorFunctions")
    public ModelAndView supervisorFunctions(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String workflowSuperUserUrl = getConfigurationService().getPropertyValueAsString(KRADConstants.WORKFLOW_URL_KEY)
                + "/" + KRADConstants.SUPERUSER_ACTION;

        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, "displaySuperUserDocument");
        props.put(UifPropertyPaths.DOCUMENT_ID, form.getDocument().getDocumentNumber());

        return performRedirect(form, workflowSuperUserUrl, props);
    }

    /**
     * Called by the add note action for adding a note. Method validates, saves attachment and adds the
     * time stamp and author. Calls the UifControllerBase.addLine method to handle generic actions.
     *
     * @param uifForm - document form base containing the note instance that will be inserted into the document
     * @return ModelAndView
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertNote")
    public ModelAndView insertNote(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        // Get the note add line
        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = uifForm.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object addLine = ObjectPropertyUtils.getPropertyValue(uifForm, addLinePath);
        Note newNote = (Note) addLine;
        newNote.setNotePostedTimestampToCurrent();

        Document document = ((DocumentFormBase) uifForm).getDocument();

        newNote.setRemoteObjectIdentifier(document.getNoteTarget().getObjectId());

        // Get the attachment file
        String attachmentTypeCode = null;
        MultipartFile attachmentFile = uifForm.getAttachmentFile();
        Attachment attachment = null;
        if (attachmentFile != null && !StringUtils.isBlank(attachmentFile.getOriginalFilename())) {
            if (attachmentFile.getSize() == 0) {
                GlobalVariables.getMessageMap().putError(String.format("%s.%s",
                        KRADConstants.NEW_DOCUMENT_NOTE_PROPERTY_NAME,
                        KRADConstants.NOTE_ATTACHMENT_FILE_PROPERTY_NAME), RiceKeyConstants.ERROR_UPLOADFILE_EMPTY,
                        attachmentFile.getOriginalFilename());
            } else {
                if (newNote.getAttachment() != null) {
                    attachmentTypeCode = newNote.getAttachment().getAttachmentTypeCode();
                }

                DocumentAuthorizer documentAuthorizer = getDocumentDictionaryService().getDocumentAuthorizer(document);
                if (!documentAuthorizer.canAddNoteAttachment(document, attachmentTypeCode,
                        GlobalVariables.getUserSession().getPerson())) {
                    throw buildAuthorizationException("annotate", document);
                }

                try {
                    String attachmentType = null;
                    Attachment newAttachment = newNote.getAttachment();
                    if (newAttachment != null) {
                        attachmentType = newAttachment.getAttachmentTypeCode();
                    }

                    attachment = getAttachmentService().createAttachment(document.getNoteTarget(),
                            attachmentFile.getOriginalFilename(), attachmentFile.getContentType(),
                            (int) attachmentFile.getSize(), attachmentFile.getInputStream(), attachmentType);
                } catch (IOException e) {
                    throw new RiceRuntimeException("Unable to store attachment", e);
                }
            }
        }

        Person kualiUser = GlobalVariables.getUserSession().getPerson();
        if (kualiUser == null) {
            throw new IllegalStateException("Current UserSession has a null Person.");
        }

        newNote.setAuthorUniversalIdentifier(kualiUser.getPrincipalId());

        // validate the note
        boolean rulePassed = KRADServiceLocatorWeb.getKualiRuleService().applyRules(new AddNoteEvent(document,
                newNote));

        // if the rule evaluation passed, let's add the note; otherwise, return with an error
        if (rulePassed) {
            newNote.refresh();

            DocumentHeader documentHeader = document.getDocumentHeader();

            // adding the attachment after refresh gets called, since the attachment record doesn't get persisted
            // until the note does (and therefore refresh doesn't have any attachment to autoload based on the id, nor does it
            // autopopulate the id since the note hasn't been persisted yet)
            if (attachment != null) {
                newNote.addAttachment(attachment);
            }
            // Save the note if the document is already saved
            if (!documentHeader.getWorkflowDocument().isInitiated() && StringUtils.isNotEmpty(
                    document.getNoteTarget().getObjectId()) && !(document instanceof MaintenanceDocument && NoteType
                    .BUSINESS_OBJECT.getCode().equals(newNote.getNoteTypeCode()))) {

                getNoteService().save(newNote);
            }

            return addLine(uifForm, result, request, response);
        } else {
            return getUIFModelAndView(uifForm);
        }
    }

    /**
     * Called by the delete note action for deleting a note.
     * Calls the UifControllerBase.deleteLine method to handle
     * generic actions.
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=deleteNote")
    public ModelAndView deleteNote(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        String selectedLineIndex = uifForm.getActionParamaterValue("selectedLineIndex");
        Document document = ((DocumentFormBase) uifForm).getDocument();
        Note note = document.getNote(Integer.parseInt(selectedLineIndex));

        Attachment attachment = note.getAttachment();
        String attachmentTypeCode = null;
        if (attachment != null) {
            attachmentTypeCode = attachment.getAttachmentTypeCode();
        }

        // check delete note authorization
        Person user = GlobalVariables.getUserSession().getPerson();
        String authorUniversalIdentifier = note.getAuthorUniversalIdentifier();
        if (!getDocumentDictionaryService().getDocumentAuthorizer(document).canDeleteNoteAttachment(document,
                attachmentTypeCode, authorUniversalIdentifier, user)) {
            throw buildAuthorizationException("annotate", document);
        }

        if (attachment != null && attachment.isComplete()) { // only do this if the note has been persisted
            //KFSMI-798 - refresh() changed to refreshNonUpdateableReferences()
            //All references for the business object Attachment are auto-update="none",
            //so refreshNonUpdateableReferences() should work the same as refresh()
            if (note.getNoteIdentifier()
                    != null) { // KULRICE-2343 don't blow away note reference if the note wasn't persisted
                attachment.refreshNonUpdateableReferences();
            }
            getAttachmentService().deleteAttachmentContents(attachment);
        }
        // delete the note if the document is already saved
        if (!document.getDocumentHeader().getWorkflowDocument().isInitiated()) {
            getNoteService().deleteNote(note);
        }

        return deleteLine(uifForm, result, request, response);
    }

    /**
     * Called by the download attachment action on a note. Method
     * gets the attachment input stream from the AttachmentService
     * and writes it to the request output stream.
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=downloadAttachment")
    public ModelAndView downloadAttachment(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response) throws ServletRequestBindingException, FileNotFoundException, IOException {
        // Get the attachment input stream
        String selectedLineIndex = uifForm.getActionParamaterValue("selectedLineIndex");
        Note note = ((DocumentFormBase) uifForm).getDocument().getNote(Integer.parseInt(selectedLineIndex));
        Attachment attachment = note.getAttachment();
        InputStream is = getAttachmentService().retrieveAttachmentContents(attachment);

        // Set the response headers
        response.setContentType(attachment.getAttachmentMimeTypeCode());
        response.setContentLength(attachment.getAttachmentFileSize().intValue());
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + attachment.getAttachmentFileName() + "\"");

        // Copy the input stream to the response
        FileCopyUtils.copy(is, response.getOutputStream());
        return null;
    }

    /**
     * Called by the cancel attachment action on a note. Method
     * removes the attachment file from the form.
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancelAttachment")
    public ModelAndView cancelAttachment(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        // Remove the attached file
        uifForm.setAttachmentFile(null);
        return getUIFModelAndView(uifForm);
    }

    /**
     * Checks if the given value matches patterns that indicate sensitive data
     * and if configured to give a warning for sensitive data will prompt the
     * user to continue.
     *
     * @param form
     * @param request
     * @param response
     * @param fieldName - name of field with value being checked
     * @param fieldValue - value to check for sensitive data
     * @param caller - method that should be called back from question
     * @param context - additional context that needs to be passed back with the
     * question response
     * @return - view for spring to forward to, or null if processing should
     *         continue
     * @throws Exception
     */
    protected String checkAndWarnAboutSensitiveData(DocumentFormBase form, HttpServletRequest request,
            HttpServletResponse response, String fieldName, String fieldValue, String caller,
            String context) throws Exception {

        String viewName = null;
        Document document = form.getDocument();

        // TODO: need to move containsSensitiveDataPatternMatch to util class in krad
//        boolean containsSensitiveData = false;
//        //boolean containsSensitiveData = WebUtils.containsSensitiveDataPatternMatch(fieldValue);
//
//        // check if warning is configured in which case we will prompt, or if
//        // not business rules will thrown an error
//        boolean warnForSensitiveData = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(
//                KRADConstants.KRAD_NAMESPACE, ParameterConstants.ALL_COMPONENT,
//                KRADConstants.SystemGroupParameterNames.SENSITIVE_DATA_PATTERNS_WARNING_IND);
//
//        // determine if the question has been asked yet
//        Map<String, String> ticketContext = new HashMap<String, String>();
//        ticketContext.put(KRADPropertyConstants.DOCUMENT_NUMBER, document.getDocumentNumber());
//        ticketContext.put(KRADConstants.CALLING_METHOD, caller);
//        ticketContext.put(KRADPropertyConstants.NAME, fieldName);
//
//        boolean questionAsked = GlobalVariables.getUserSession().hasMatchingSessionTicket(
//                KRADConstants.SENSITIVE_DATA_QUESTION_SESSION_TICKET, ticketContext);
//
//        // start in logic for confirming the sensitive data
//        if (containsSensitiveData && warnForSensitiveData && !questionAsked) {
//            Object question = request.getParameter(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME);
//            if (question == null || !KRADConstants.DOCUMENT_SENSITIVE_DATA_QUESTION.equals(question)) {
//
//                // TODO not ready for question framework yet
//                /*
//                     * // question hasn't been asked, prompt to continue return
//                     * this.performQuestionWithoutInput(mapping, form, request,
//                     * response, KRADConstants.DOCUMENT_SENSITIVE_DATA_QUESTION,
//                     * getKualiConfigurationService()
//                     * .getPropertyValueAsString(RiceKeyConstants
//                     * .QUESTION_SENSITIVE_DATA_DOCUMENT),
//                     * KRADConstants.CONFIRMATION_QUESTION, caller, context);
//                     */
//                viewName = "ask_user_questions";
//            } else {
//                Object buttonClicked = request.getParameter(KRADConstants.QUESTION_CLICKED_BUTTON);
//
//                // if no button clicked just reload the doc
//                if (ConfirmationQuestion.NO.equals(buttonClicked)) {
//                    // TODO figure out what to return
//                    viewName = "user_says_no";
//                }
//
//                // answered yes, create session ticket so we not to ask question
//                // again if there are further question requests
//                SessionTicket ticket = new SessionTicket(KRADConstants.SENSITIVE_DATA_QUESTION_SESSION_TICKET);
//                ticket.setTicketContext(ticketContext);
//                GlobalVariables.getUserSession().putSessionTicket(ticket);
//            }
//        }

        // returning null will indicate processing should continue (no redirect)
        return viewName;
    }

    /**
     * Convenience method to combine the two lists of ad hoc recipients into one which should be done before
     * calling any of the document service methods that expect a list of ad hoc recipients
     *
     * @param form - document form instance containing the ad hod lists
     * @return List<AdHocRouteRecipient> combined ad hoc recipients
     */
    protected List<AdHocRouteRecipient> combineAdHocRecipients(DocumentFormBase form) {
        Document document = form.getDocument();

        List<AdHocRouteRecipient> adHocRecipients = new ArrayList<AdHocRouteRecipient>();
        adHocRecipients.addAll(document.getAdHocRoutePersons());
        adHocRecipients.addAll(document.getAdHocRouteWorkgroups());

        return adHocRecipients;
    }

    /**
     * Convenience method for building authorization exceptions
     *
     * @param action - the action that was requested
     * @param document - document instance the action was requested for
     */
    protected DocumentAuthorizationException buildAuthorizationException(String action, Document document) {
        return new DocumentAuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(),
                action, document.getDocumentNumber());
    }

    public BusinessObjectService getBusinessObjectService() {
        if (this.businessObjectService == null) {
            this.businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return this.businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public DataDictionaryService getDataDictionaryService() {
        if (this.dataDictionaryService == null) {
            this.dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();
        }
        return this.dataDictionaryService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    public DocumentService getDocumentService() {
        if (this.documentService == null) {
            this.documentService = KRADServiceLocatorWeb.getDocumentService();
        }
        return this.documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public DocumentDictionaryService getDocumentDictionaryService() {
        if (this.documentDictionaryService == null) {
            this.documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }
        return this.documentDictionaryService;
    }

    public void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        this.documentDictionaryService = documentDictionaryService;
    }

    public AttachmentService getAttachmentService() {
        if (attachmentService == null) {
            attachmentService = KRADServiceLocator.getAttachmentService();
        }
        return this.attachmentService;
    }

    public NoteService getNoteService() {
        if (noteService == null) {
            noteService = KRADServiceLocator.getNoteService();
        }

        return this.noteService;
    }

    public ConfigurationService getConfigurationService() {
        return CoreApiServiceLocator.getKualiConfigurationService();
    }

    protected String generateDisapprovalNote(DocumentFormBase form, boolean checkSensitiveData) {
        return "";
    }

}
