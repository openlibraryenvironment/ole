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
package org.kuali.rice.kew.notes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;
import org.kuali.rice.kew.notes.service.NoteService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;

public class NoteServiceTest extends KEWTestCase {
		
    @Test public void testAttachmentSave() throws Exception {
		Note note = new Note();
		note.setNoteAuthorWorkflowId("fakeyUser");
		note.setDocumentId("2");
		note.setNoteCreateDate(new Timestamp(new Date().getTime()));
		note.setNoteText("i like notes");
		
		Attachment attachment = new Attachment();
		attachment.setNote(note);
		attachment.setMimeType("mimeType");
		attachment.setFileName("attachedFile.txt");
		attachment.setAttachedObject(TestUtilities.loadResource(this.getClass(), "attachedFile.txt"));
		
		note.getAttachments().add(attachment);
		
		NoteService noteService = KEWServiceLocator.getNoteService();
		noteService.saveNote(note);
		assertNotNull("Note should have a id", note.getNoteId());
		assertNotNull("Note should have a version number", note.getLockVerNbr());
		
		assertNotNull("Attachment should have a id", attachment.getAttachmentId());
		assertNotNull("Attachment should have version number", attachment.getLockVerNbr());
		assertNotNull("Attachment file loc should reflect file system location", attachment.getFileLoc());
		
		FileReader fileReader = new FileReader(noteService.findAttachmentFile(attachment));
		StringWriter stringWriter = new StringWriter();
		int c;
        while ((c = fileReader.read()) != -1) {
        	stringWriter.write(c);
        }
        //i'm being lazy and knowing what's in the source file
        assertEquals("Attached file content should equal source file content", "I'm an attached file", stringWriter.getBuffer().toString());
	}

}
