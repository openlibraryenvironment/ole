/**
 * Copyright 2005-2013 The Kuali Foundation
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.note.Note;
import org.kuali.rice.kew.api.note.NoteService;
import org.kuali.rice.kew.test.KEWTestCase;

public class NoteServiceTest extends KEWTestCase {
	
	@Test
	public void testIllegalNoteOperations() throws Exception {
		NoteService noteService = KewApiServiceLocator.getNoteService();
		
		try {
			noteService.createNote(null);
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}
		
		Note.Builder testNote = Note.Builder.create("1234", "ewestfal");
		// shouldn't be able to create with an id already on it
		testNote.setId("4321");
		try {
			noteService.createNote(testNote.build());
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}
		
		try {
			noteService.updateNote(null);
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}
		
		// shouldn't be able to update a note that doesn't have an id
		testNote = Note.Builder.create("1234", "ewestfal");
		try {
			noteService.updateNote(testNote.build());
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}
		
		// shouldn't be able to update a note if it doesn't have a version number
		testNote.setId("4321");
		try {
			noteService.updateNote(testNote.build());
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}
		
		// add version number, but now shouldn't be able to update a note if it doesn't have a valid create date
		testNote.setVersionNumber(new Long(1));
		try {
			noteService.updateNote(testNote.build());
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}
		
		// add create date, but now shouldn't be able to update a note with an id of a note that doesn't exist
		testNote.setCreateDate(new DateTime());
		try {
			noteService.updateNote(testNote.build());
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}

		// shouldn't be able to delete a note with a note id that doesn't exist
		try {
			noteService.deleteNote("1234");
			fail("RiceIllegalArgumentException should have been thrown");
		} catch (RiceIllegalArgumentException e) {}
				
	}
	
	@Test public void testNotesClient() throws Exception {
		NoteService noteService = KewApiServiceLocator.getNoteService();
		
		WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "TestDocumentType");

		assertTrue(noteService.getNotes(doc.getDocumentId()).isEmpty());
		
		// Test add notes
		Note.Builder testNote = Note.Builder.create(doc.getDocumentId(), "andlee");
		testNote.setText("first added note");
		
		Note createdNote = noteService.createNote(testNote.build());
		assertNotNull(createdNote);
		assertNotNull(createdNote.getId());
		assertNotNull(createdNote.getVersionNumber());
		assertEquals(doc.getDocumentId(), createdNote.getDocumentId());
		assertEquals("andlee", createdNote.getAuthorPrincipalId());
		assertEquals("first added note", createdNote.getText());
		
		testNote = Note.Builder.create(doc.getDocumentId(), "rou");
		testNote.setText("second added note");
		Note createdNote2 = noteService.createNote(testNote.build());
		
		List<Note> notesList = noteService.getNotes(doc.getDocumentId());
		
        assertEquals ("Two notes are added.", 2, notesList.size());
                
        assertEquals("Note List size changed",2,notesList.size());
        for (Iterator<Note> iter = notesList.iterator(); iter.hasNext();) {
			Note note = iter.next();
			assertNotNull("Note saved", note.getId());
			if (note.getId().equals(createdNote.getId())) {
				assertEquals("text altered during save", "first added note", note.getText());
				assertEquals("note user associated with saved note", "andlee", note.getAuthorPrincipalId());
			}
			if (note.getId().equals(createdNote2.getId())) {
				assertEquals("text altered during save", "second added note", note.getText());
				assertEquals("note user associated with saved note", "rou", note.getAuthorPrincipalId());
			}
			
		}
                
        notesList = noteService.getNotes(doc.getDocumentId());
        Note note1 = null;
        Note note2 = null;
        for (Note note : notesList) {
        	if (note.getId().equals(createdNote.getId())) {
        		note1 = note;
        	} else if(note.getId().equals(createdNote2.getId())) {
        		note2 = note;
        	} else {
        		fail("encountered unexpected note!: " + note.getId());
        	}
        }
        noteService.deleteNote(note1.getId());

        Note.Builder note2Builder = Note.Builder.create(note2);
        note2Builder.setText("Update second note text");
        noteService.updateNote(note2Builder.build());
        
        notesList = noteService.getNotes(doc.getDocumentId());
        assertEquals("Note List size changed",1,notesList.size());
        Note remainingNote = notesList.get(0);
        assertNotNull("Note saved", remainingNote.getId());
        assertEquals("text altered during save", "Update second note text", remainingNote.getText());
        assertEquals("note user associated with saved note", "rou", remainingNote.getAuthorPrincipalId());
	}

}
