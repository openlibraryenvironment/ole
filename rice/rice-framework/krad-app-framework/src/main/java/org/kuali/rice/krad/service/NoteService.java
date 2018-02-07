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

import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

import java.util.List;

/**
 * This service provides various operations related to {@link Note} objects.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NoteService {
	
    /**
     * Retrieves a list of notes that are associated with the given object id.
     * This object id will generally be the object id of the {@link org.kuali.rice.krad.bo.PersistableBusinessObject}
     * that the note was attached to when it was created.
     *
     * @param remoteObjectId the object id that the notes being searched for are associated with
     * @return the list of notes which are associated with the given object id.  If no such notes are found, an empty list will be returned.
     */
    public List<Note> getByRemoteObjectId(String remoteObjectId);

    /**
     * Retrieves the note with the given id.
     *
     * @param noteId the note id to search by
     * @return the note with the given note id, or null if no note is found
     * @throws IllegalArgumentException if the specified id is null
     */
    public Note getNoteByNoteId(Long noteId);
    
    /**
     * Saves the given lists of notes.  If the given list is null or empty,
     * this method will do nothing.
     * 
     * @param notes the list of notes to save
     * @throws IllegalStateException if any of the notes in the list have an invalid remoteObjectId
     */
    public void saveNoteList(List<Note> notes);

    /**
     * Saves the specified note.  This method returns a reference to the note that was
     * saved.  Callers of this method should reassign their reference to the note
     * passed in with the one that is returned.
     *
     * @param note the note to save
     * @return the saved note
     * @throws IllegalArgumentException if the specified note is null
     * @throws IllegalStateException if the given note's remoteObjectId is not valid
     */
    public Note save(Note note);

    /**
     * Deletes the specified note.
     *
     * @param note the note to delete
     * @throws IllegalArgumentException if the given note is null
     */
    public void deleteNote(Note note);

    /**
     * Creates a new note which is a copy of the given note and is associated with
     * the specified PersistableBusinessObject and Person.
     * 
     * @param noteToCopy the note to copy
     * @param bo the business object to associate the Note with
     * @return a copy of the given note which
     */
    public Note createNote(Note noteToCopy, PersistableBusinessObject bo, String authorPrincipalId);

}
