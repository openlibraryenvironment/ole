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
package org.kuali.rice.kew.notes.web;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.notes.Attachment;
import org.kuali.rice.kew.notes.CustomNoteAttribute;
import org.kuali.rice.kew.notes.Note;
import org.kuali.rice.kew.notes.service.NoteService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * Struts action for interfacing with the Notes system.
 *
 * @see NoteService
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NoteAction extends KewKualiAction {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(NoteAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        initForm(request, form);
        return super.execute(mapping, form, request, response);
    }
    
    //public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    //    return mapping.findForward("allNotesReport");
    //}
    
   
    @Override
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	NoteForm noteForm = (NoteForm) form;
    	if(StringUtils.isBlank(noteForm.getShowEdit())) {
    		noteForm.setShowEdit("no");
    	}
    	return super.start(mapping, noteForm, request, response);
    }

    
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NoteForm noteForm = (NoteForm) form;
        noteForm.setShowEdit("no");
        noteForm.setNoteIdNumber(null);
        retrieveNoteList(request, noteForm);
        noteForm.setShowAdd(Boolean.TRUE);
        return start(mapping, form, request, response);
    }

    public ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	NoteForm noteForm = (NoteForm) form;
    	NoteService noteService = KEWServiceLocator.getNoteService();
    	Note note = noteService.getNoteByNoteId(noteForm.getNote().getNoteId());
    	noteService.deleteAttachment(note.getAttachments().remove(0));
    	noteForm.setDocId(note.getDocumentId());
    	noteForm.setNoteIdNumber(note.getNoteId());
    	edit(mapping, form, request, response);
    	return start(mapping, form, request, response);
    	//return mapping.findForward("allNotesReport");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NoteForm noteForm = (NoteForm) form;
        if ("yes".equalsIgnoreCase(noteForm.getShowEdit())) {
            noteForm.setNoteIdNumber(noteForm.getNote().getNoteId());
        } else {
            noteForm.setShowEdit("yes");
            Note noteToEdit = getNoteService().getNoteByNoteId(noteForm.getNoteIdNumber());
            noteForm.setNote(noteToEdit);
            noteForm.getNote().setNoteCreateLongDate(new Long(noteForm.getNote().getNoteCreateDate().getTime()));
        }
        retrieveNoteList(request, noteForm);
        return start(mapping, form, request, response);
    }

//    public ActionForward attachFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//
//    	return start(mapping, form, request, response);
//    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NoteForm noteForm = (NoteForm) form;
        Note noteToSave = null;
        if (noteForm.getShowEdit().equals("yes")) {
            noteToSave = noteForm.getNote();
            noteToSave.setNoteCreateDate(new Timestamp(noteToSave.getNoteCreateLongDate().longValue()));
        } else {
            noteToSave = new Note();
            noteToSave.setNoteId(null);
            noteToSave.setDocumentId(noteForm.getDocId());
            noteToSave.setNoteCreateDate(new Timestamp((new Date()).getTime()));
            noteToSave.setNoteAuthorWorkflowId(getUserSession().getPrincipalId());
            noteToSave.setNoteText(noteForm.getAddText());
        }
        CustomNoteAttribute customNoteAttribute = null;
        DocumentRouteHeaderValue routeHeader = getRouteHeaderService().getRouteHeader(noteToSave.getDocumentId());
        boolean canEditNote = false;
        boolean canAddNotes = false;
        if (routeHeader != null) {
            customNoteAttribute = routeHeader.getCustomNoteAttribute();
            if (customNoteAttribute != null) {
                customNoteAttribute.setUserSession(GlobalVariables.getUserSession());
                canAddNotes = customNoteAttribute.isAuthorizedToAddNotes();
                canEditNote = customNoteAttribute.isAuthorizedToEditNote(noteToSave);
            }
        }

        if ((noteForm.getShowEdit().equals("yes") && canEditNote) ||
                (!noteForm.getShowEdit().equals("yes") && canAddNotes)) {
        	FormFile uploadedFile = (FormFile)noteForm.getFile();
        	if (uploadedFile != null && StringUtils.isNotBlank(uploadedFile.getFileName())) {
        		Attachment attachment = new Attachment();
        		attachment.setAttachedObject(uploadedFile.getInputStream());
        		attachment.setFileName(uploadedFile.getFileName());
        		attachment.setMimeType(uploadedFile.getContentType());
        		attachment.setNote(noteToSave);
        		noteToSave.getAttachments().add(attachment);
            } else {
                if(noteToSave.getNoteId() != null) { //note is not new and we need to ensure the attachments are preserved
                    noteToSave.setAttachments(getNoteService().getNoteByNoteId(noteToSave.getNoteId()).getAttachments());
                }
            }
            getNoteService().saveNote(noteToSave);
        }
        if (noteForm.getShowEdit().equals("yes")) {
            noteForm.setNote(new Note());
        } else {
            noteForm.setAddText(null);
        }
        noteForm.setShowEdit("no");
        noteForm.setNoteIdNumber(null);
        retrieveNoteList(request, noteForm);
        return start(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NoteForm noteForm = (NoteForm) form;
        Note existingNote = getNoteService().getNoteByNoteId(noteForm.getNoteIdNumber());
        getNoteService().deleteNote(existingNote);
        noteForm.setShowEdit("no");
        noteForm.setNoteIdNumber(null);
        retrieveNoteList(request, noteForm);
        return start(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NoteForm noteForm = (NoteForm) form;
        noteForm.setShowEdit("no");
        noteForm.setNote(new Note());
        noteForm.setNoteIdNumber(null);
        retrieveNoteList(request, noteForm);
        return start(mapping, form, request, response);
    }

    public ActionForward sort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return start(mapping, form, request, response);
    }



    public ActionMessages initForm(HttpServletRequest request, ActionForm form) throws Exception {
        NoteForm noteForm = (NoteForm) form;
        noteForm.setCurrentUserName(getUserSession().getPerson().getName());
        noteForm.setCurrentDate(getCurrentDate());
        if (! "workflowReport".equalsIgnoreCase(noteForm.getMethodToCall()) && ! "add".equalsIgnoreCase(noteForm.getMethodToCall()) && ! "cancel".equalsIgnoreCase(noteForm.getMethodToCall()) && ! "edit".equalsIgnoreCase(noteForm.getMethodToCall()) && ! "delete".equalsIgnoreCase(noteForm.getMethodToCall()) && ! "save".equalsIgnoreCase(noteForm.getMethodToCall())) {
            retrieveNoteList(request, noteForm);
        }
        boolean showAttachments = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.SHOW_ATTACHMENTS_IND);
        noteForm.setShowAttachments(new Boolean(showAttachments));
        return null;
    }

    private void retrieveNoteList(HttpServletRequest request, NoteForm noteForm) throws Exception {
        if (noteForm.getDocId() != null) {
//            List allNotes = getNoteService().getNotesByDocumentId(noteForm.getDocId());

            CustomNoteAttribute customNoteAttribute = null;
            DocumentRouteHeaderValue routeHeader = getRouteHeaderService().getRouteHeader(noteForm.getDocId());

            List<Note> allNotes = routeHeader.getNotes();
            boolean canAddNotes = false;
            if (routeHeader != null) {
                customNoteAttribute = routeHeader.getCustomNoteAttribute();
                if (customNoteAttribute != null) {
                    customNoteAttribute.setUserSession(GlobalVariables.getUserSession());
                    canAddNotes = customNoteAttribute.isAuthorizedToAddNotes();
                }
            }
            Iterator<Note> notesIter = allNotes.iterator();
            while (notesIter.hasNext()) {
                Note singleNote = notesIter.next();
                singleNote.setNoteCreateLongDate(new Long(singleNote.getNoteCreateDate().getTime()));
                getAuthorData(singleNote);
                boolean canEditNote = false;
                if (customNoteAttribute != null) {
                	canEditNote = customNoteAttribute.isAuthorizedToEditNote(singleNote);
                }
                singleNote.setAuthorizedToEdit(new Boolean(canEditNote));
                // KULRICE-5201 - added StringUtils.equals to check equally since this is no longer a numeric (int) comparison
                if (StringUtils.equals(noteForm.getNoteIdNumber(), singleNote.getNoteId())) {
                    singleNote.setEditingNote(Boolean.TRUE);
                }
            }
            if (noteForm.getSortNotes() != null && noteForm.getSortNotes().booleanValue()) {
                if (KewApiConstants.Sorting.SORT_SEQUENCE_DSC.equalsIgnoreCase(noteForm.getSortOrder())) {
                    noteForm.setSortOrder(KewApiConstants.Sorting.SORT_SEQUENCE_ASC);
                    noteForm.setSortNotes(Boolean.FALSE);
                } else {
                    noteForm.setSortOrder(KewApiConstants.Sorting.SORT_SEQUENCE_DSC);
                    noteForm.setSortNotes(Boolean.FALSE);
                }
            } else {
                noteForm.setSortOrder(noteForm.getSortOrder());
            }
            noteForm.setNoteList(sortNotes(allNotes, noteForm.getSortOrder()));
            noteForm.setNumberOfNotes(new Integer(allNotes.size()));
            noteForm.setAuthorizedToAdd(new Boolean(canAddNotes));
            noteForm.setShowAdd(Boolean.TRUE);
            if (! canAddNotes) {
                noteForm.setShowAdd(Boolean.FALSE);
            } else if (noteForm.getNoteList().size() == 0) {
                noteForm.setShowAdd(Boolean.FALSE);
            }
        }
    }

    private void getAuthorData(Note note) throws Exception {
        Person user = null;
        String id = "";
        if (note != null && note.getNoteAuthorWorkflowId() != null && ! "".equalsIgnoreCase(note.getNoteAuthorWorkflowId())) {
        	user = KimApiServiceLocator.getPersonService().getPerson(note.getNoteAuthorWorkflowId());
            id = note.getNoteAuthorWorkflowId();
        }
        if (user != null) {
            note.setNoteAuthorFullName(user.getName());
            note.setNoteAuthorEmailAddress(user.getEmailAddressUnmasked());
            note.setNoteAuthorNetworkId(user.getPrincipalId());
        } else {
            note.setNoteAuthorFullName(id + " (Name not Available)");
            note.setNoteAuthorEmailAddress("Not Available");
            note.setNoteAuthorNetworkId("Not Available");
        }
    }

    public String getCurrentDate() {
        Date currentDate = new Date();
        DateFormat dateFormat = RiceConstants.getDefaultDateFormat();
        return dateFormat.format(currentDate);
    }

    private List<Note> sortNotes(List<Note> allNotes, String sortOrder) {
        final int returnCode = KewApiConstants.Sorting.SORT_SEQUENCE_DSC.equalsIgnoreCase(sortOrder) ? -1 : 1;

        try {
          Collections.sort(allNotes,
          new Comparator<Note>() {
            @Override
			public int compare(Note o1, Note o2) {
  			Timestamp date1 = o1.getNoteCreateDate();
  			Timestamp date2 = o2.getNoteCreateDate();

                if (date1.before(date2)) {
                  return returnCode * -1;
                } else if (date1.after(date2)) {
                  return returnCode;
                } else {
                  return 0;
                }
            }
          });
      } catch (Throwable e) {
        LOG.error(e.getMessage(), e);
      }
      return allNotes;
    }

    private NoteService getNoteService() {
        return (NoteService) KEWServiceLocator.getService(KEWServiceLocator.NOTE_SERVICE);
    }

    private RouteHeaderService getRouteHeaderService() {
        return (RouteHeaderService) KEWServiceLocator.getService(KEWServiceLocator.DOC_ROUTE_HEADER_SRV);
    }
    private static UserSession getUserSession() {
        return GlobalVariables.getUserSession();
    }
}
