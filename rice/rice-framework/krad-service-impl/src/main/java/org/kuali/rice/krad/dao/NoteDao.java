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
package org.kuali.rice.krad.dao;

import java.util.List;

import org.kuali.rice.krad.bo.Note;


/**
 * The data access interface for NOte objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NoteDao {
    /**
     * Saves a note to the DB.
     * 
     * @param line
     */
    void save(Note note);

    /**
     * Deletes a note from the DB.
     * 
     * @param line
     */
    void deleteNote(Note note);

    /**
     * Retrieves a list of notes (by class type) associated with a given object.
     * 
     * @param clazz
     * @param id
     * @return
     */
    public List<Note> findByremoteObjectId(String id);
    
    /**
     * Retrieve note by a given noteIdentifier
     * 
     * @param noteId
     * @return
     */
    public Note getNoteByNoteId(Long noteId);
}
