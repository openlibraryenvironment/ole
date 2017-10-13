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

import java.util.List;

import org.kuali.rice.kew.notes.Note;
import org.kuali.rice.kns.web.struts.form.KualiForm;

/**
 * Struts ActionForm for {@link NoteAction}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NoteForm extends KualiForm {

	private static final long serialVersionUID = 1L;
	private Note note;
    private Note existingNote;
    private String methodToCall = "";
    private String showEdit;
    private Boolean showAdd;
    private String docId;
    private String noteIdNumber;
    private Integer numberOfNotes;
    private String sortOrder = "DESCENDING";
    private Boolean sortNotes;
    private String currentUserName;
    private String currentDate;
    private Boolean authorizedToAdd;
    private List<Note> noteList;
    private String addText;
    private Long idInEdit;
    private Boolean showAttachments;
    private String attachmentTarget;


    private Object file;

    public NoteForm() {
        note = new Note();
    }

    public String getMethodToCall() {
        return methodToCall;
    }

    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Note getExistingNote() {
        return existingNote;
    }

    public void setExistingNote(Note existingNote) {
        this.existingNote = existingNote;
    }

    public String getShowEdit() {
        return showEdit;
    }

    public void setShowEdit(String showEdit) {
        this.showEdit = showEdit;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Integer getNumberOfNotes() {
        return numberOfNotes;
    }

    public void setNumberOfNotes(Integer numberOfNotes) {
        this.numberOfNotes = numberOfNotes;
    }

    public Boolean getShowAdd() {
        return showAdd;
    }

    public void setShowAdd(Boolean showAdd) {
        this.showAdd = showAdd;
    }

    public String getNoteIdNumber() {
        return noteIdNumber;
    }

    public void setNoteIdNumber(String noteIdNumber) {
        this.noteIdNumber = noteIdNumber;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getSortNotes() {
        return sortNotes;
    }

    public void setSortNotes(Boolean sortNotes) {
        this.sortNotes = sortNotes;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public Boolean getAuthorizedToAdd() {
        return authorizedToAdd;
    }

    public void setAuthorizedToAdd(Boolean authorizedToAdd) {
        this.authorizedToAdd = authorizedToAdd;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public String getAddText() {
        return addText;
    }

    public void setAddText(String addText) {
        this.addText = addText;
    }

    public Long getIdInEdit() {
        return idInEdit;
    }

    public void setIdInEdit(Long idInEdit) {
        this.idInEdit = idInEdit;
    }

	public Object getFile() {
		return file;
	}

	public void setFile(Object file) {
		this.file = file;
	}

	public Boolean getShowAttachments() {
		return showAttachments;
	}

	public void setShowAttachments(Boolean showAttachments) {
		this.showAttachments = showAttachments;
	}

	public String getAttachmentTarget() {
		return attachmentTarget;
	}

	public void setAttachmentTarget(String attachmentTarget) {
		this.attachmentTarget = attachmentTarget;
	}
}
