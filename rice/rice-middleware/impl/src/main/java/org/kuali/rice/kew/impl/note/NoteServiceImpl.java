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
package org.kuali.rice.kew.impl.note;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.note.Note;
import org.kuali.rice.kew.api.note.NoteService;
import org.kuali.rice.kew.notes.dao.NoteDAO;

/**
 * TODO 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NoteServiceImpl implements NoteService {

	private NoteDAO noteDao;
	
	@Override
	public List<Note> getNotes(String documentId) {
		if (StringUtils.isBlank(documentId)) {
			throw new RiceIllegalArgumentException("documentId was null or blank");
		}
		List<org.kuali.rice.kew.notes.Note> noteBos = getNoteDao().getNotesByDocumentId(documentId);
		if (noteBos == null) {
			return Collections.emptyList();
		}
		List<Note> notes = new ArrayList<Note>();
		for (org.kuali.rice.kew.notes.Note noteBo : noteBos) {
			notes.add(org.kuali.rice.kew.notes.Note.to(noteBo));
		}
		return Collections.unmodifiableList(notes);
	}

	@Override
	public Note getNote(String noteId) {
		if (StringUtils.isBlank(noteId)) {
			throw new RiceIllegalArgumentException("noteId was null or blank");
		}
		org.kuali.rice.kew.notes.Note noteBo = getNoteDao().getNoteByNoteId(noteId);
		return org.kuali.rice.kew.notes.Note.to(noteBo);
	}

	@Override
	public Note createNote(Note note) {
		if (note == null) {
			throw new RiceIllegalArgumentException("note was null");
		}
		if (note.getId() != null) {
			throw new RiceIllegalArgumentException("Attempted to create a note that already has an id assigned, id must be null upon creation");
		}
		if (note.getVersionNumber() != null) {
			throw new RiceIllegalArgumentException("Attempted to create a note that already has a version number assigned, version number must be null upon creation");
		}
		org.kuali.rice.kew.notes.Note noteBo = org.kuali.rice.kew.notes.Note.from(note);
		if (noteBo.getNoteCreateDate() == null) {
			noteBo.setNoteCreateDate(new Timestamp(System.currentTimeMillis()));
		}
		getNoteDao().saveNote(noteBo);
		return org.kuali.rice.kew.notes.Note.to(noteBo);
	}

	@Override
	public Note updateNote(Note note) {
		if (note == null) {
			throw new RiceIllegalArgumentException("note was null");
		}
		if (note.getId() == null) {
			throw new RiceIllegalArgumentException("Attempted to update a note without an id, id must be present when updating");
		}
		if (note.getVersionNumber() == null) {
			throw new RiceIllegalArgumentException("Attempted to update a note without a version number, version number must be present when updating");
		}
		if (note.getCreateDate() == null) {
			throw new RiceIllegalArgumentException("Attempted to update a note without a create date, note must have a create date");
		}
		Note existingNoteBo = getNote(note.getId());
		if (existingNoteBo == null) {
			throw new RiceIllegalArgumentException("Attempted to udpate a note with an id for a not that does not exist: " + note.getId());
		}
		org.kuali.rice.kew.notes.Note noteBo = org.kuali.rice.kew.notes.Note.from(note);
		getNoteDao().saveNote(noteBo);
		return org.kuali.rice.kew.notes.Note.to(noteBo);
	}

	@Override
	public Note deleteNote(String noteId) {
		if (StringUtils.isBlank(noteId)) {
			throw new RiceIllegalArgumentException("noteId was null or blank");
		}
		org.kuali.rice.kew.notes.Note noteBo = getNoteDao().getNoteByNoteId(noteId);
		if (noteBo == null) {
			throw new RiceIllegalArgumentException("A note does not exist for the given note id: " + noteId);
		}
		Note deletedNote = org.kuali.rice.kew.notes.Note.to(noteBo);
		getNoteDao().deleteNote(noteBo);
		return deletedNote;
	}
	
	public void setNoteDao(NoteDAO noteDao) {
		this.noteDao = noteDao;
	}
	
	public NoteDAO getNoteDao() {
		return noteDao;
	}

}
