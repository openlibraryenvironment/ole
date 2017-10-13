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
package org.kuali.rice.krad.dao.proxy;

import java.util.List;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.dao.NoteDao;
import org.springframework.dao.DataAccessException;

public class NoteDaoProxy implements NoteDao {

    private NoteDao noteDaoJpa;
    private NoteDao noteDaoOjb;
	
    private NoteDao getDao(Class clazz) {
    	final String TMP_NM = clazz.getName();
		final int START_INDEX = TMP_NM.indexOf('.', TMP_NM.indexOf('.') + 1) + 1;
    	return (OrmUtils.isJpaAnnotated(clazz) && (OrmUtils.isJpaEnabled() || OrmUtils.isJpaEnabled("rice.krad"))) ?
					noteDaoJpa : noteDaoOjb; 
    }
    
    public void save(Note note) throws DataAccessException {
		getDao(Note.class).save(note);
	}

	public void deleteNote(Note note) throws DataAccessException {
		getDao(Note.class).deleteNote(note);
	}
	
    public Note getNoteByNoteId(Long noteId){
    	return getDao(Note.class).getNoteByNoteId(noteId);
    }

	public List<Note> findByremoteObjectId(String remoteObjectId) {
		return getDao(Note.class).findByremoteObjectId(remoteObjectId);
	}

	public void setNoteDaoJpa(NoteDao noteDaoJpa) {
		this.noteDaoJpa = noteDaoJpa;
	}

	public void setNoteDaoOjb(NoteDao noteDaoOjb) {
		this.noteDaoOjb = noteDaoOjb;
	}

}
