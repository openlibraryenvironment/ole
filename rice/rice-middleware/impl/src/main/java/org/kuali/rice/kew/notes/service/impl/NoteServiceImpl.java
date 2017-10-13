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
package org.kuali.rice.kew.notes.service.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.kuali.rice.kew.notes.Attachment;
import org.kuali.rice.kew.notes.Note;
import org.kuali.rice.kew.notes.dao.NoteDAO;
import org.kuali.rice.kew.notes.service.AttachmentService;
import org.kuali.rice.kew.notes.service.NoteService;


public class NoteServiceImpl implements NoteService {

	private NoteDAO noteDAO;

	private AttachmentService attachmentService;

	public Note getNoteByNoteId(String noteId) {
		return getNoteDAO().getNoteByNoteId(noteId);
	}

	public List<Note> getNotesByDocumentId(String documentId) {
		return getNoteDAO().getNotesByDocumentId(documentId);
	}

	public void saveNote(Note note) {
		try {
			if (! note.getAttachments().isEmpty()){
				for (Iterator iter = note.getAttachments().iterator(); iter.hasNext();) {
					Attachment attachment = (Attachment) iter.next();
					if (attachment.getAttachedObject()!= null){
						attachmentService.persistAttachedFileAndSetAttachmentBusinessObjectValue(attachment);
					}
				}
			}
			getNoteDAO().saveNote(note);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteNote(Note note) {
		try {
           if (note != null && !note.getAttachments().isEmpty()){
               for (Iterator iter = note.getAttachments().iterator(); iter.hasNext();) {
                   Attachment attachment = (Attachment) iter.next();
                   attachmentService.deleteAttachedFile(attachment);
               }
           }
           if (note != null) {
               getNoteDAO().deleteNote(note);
           }
		} catch (Exception e) {
			throw new RuntimeException("caught exception deleting attachment", e);
		}
	}

	public NoteDAO getNoteDAO() {
		return noteDAO;
	}

	public void setNoteDAO(NoteDAO noteDAO) {
		this.noteDAO = noteDAO;
	}

	public void deleteAttachment(Attachment attachment) {
		this.noteDAO.deleteAttachment(attachment);
		try {
			attachmentService.deleteAttachedFile(attachment);
		} catch (Exception e) {
			throw new RuntimeException("caught exception deleting attachment", e);
		}
	}

	public File findAttachmentFile(Attachment attachment) {
		try {
			return attachmentService.findAttachedFile(attachment);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public Attachment findAttachment(String attachmentId) {
		return noteDAO.findAttachment(attachmentId);
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}
