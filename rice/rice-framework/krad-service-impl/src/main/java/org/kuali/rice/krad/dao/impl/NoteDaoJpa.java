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
package org.kuali.rice.krad.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.krad.bo.Attachment;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.dao.NoteDao;
import org.springframework.dao.DataAccessException;

/**
 * This class is the JPA implementation of the NoteDao interface.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NoteDaoJpa implements NoteDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Saves a note to the DB using JPA.
	 */
	public void save(Note note) throws DataAccessException {
		if (note != null && note.getNoteIdentifier() == null && note.getAttachment() != null) {
			Attachment attachment = note.getAttachment();
			note.setAttachment(null);
			// store without attachment
			entityManager.merge(note);
			attachment.setNoteIdentifier(note.getNoteIdentifier());
			// put attachment back
			note.setAttachment(attachment);
		}
		entityManager.merge(note);
		
	}

	/**
	 * Deletes a note from the DB using JPA.
	 */
	public void deleteNote(Note note) throws DataAccessException {
		entityManager.remove(note.getAttachment());
		note.setAttachment(null);
		entityManager.remove(note);
	}

	/**
	 * Retrieves document associated with a given object using JPA.
	 */
	public List<Note> findByremoteObjectId(String remoteObjectId) {
		Criteria criteria = new Criteria(Note.class.getName());
		// TODO: Notes - Chris move remoteObjectId string to constants
		criteria.eq("remoteObjectIdentifier", remoteObjectId);
		criteria.orderBy("notePostedTimestamp", true);
		return new ArrayList<Note>(new QueryByCriteria(entityManager, criteria).toQuery().getResultList());
	}
	
	 public Note getNoteByNoteId(Long noteId) {
		 Criteria criteria = new Criteria(Note.class.getName());
		 criteria.eq("noteIdentifier", noteId);
	     return (Note) new QueryByCriteria(entityManager, criteria).toQuery().getSingleResult();
	    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
