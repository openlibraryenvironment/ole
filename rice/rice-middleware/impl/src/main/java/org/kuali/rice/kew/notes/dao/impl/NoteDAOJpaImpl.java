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
package org.kuali.rice.kew.notes.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.notes.Attachment;
import org.kuali.rice.kew.notes.Note;
import org.kuali.rice.kew.notes.dao.NoteDAO;


public class NoteDAOJpaImpl implements NoteDAO {

	@PersistenceContext(unitName="kew-unit")
	EntityManager entityManager;
	
    public Note getNoteByNoteId(String noteId) {
    	Query query = entityManager.createNamedQuery("KewNote.FindNoteByNoteId");
    	query.setParameter("noteId", noteId);
        return (Note) query.getSingleResult();          
    }

    public List getNotesByDocumentId(String documentId) {
    	Query query = entityManager.createNamedQuery("KewNote.FindNoteByDocumentId");
    	query.setParameter("documentId", documentId);
        return (List) query.getResultList();        
    }
    
    public void saveNote(Note note) {
    	if (note.getNoteId() == null){
    		entityManager.persist(note);
    	} else {
    		entityManager.merge(note);
    	}
    }

    public void deleteNote(Note note) {
    	Note n = getNoteByNoteId(note.getNoteId());
    	OrmUtils.merge(entityManager, n);
    	entityManager.remove(n);
    }
   
    public void deleteAttachment(Attachment attachment) {
    	Attachment a = findAttachment(attachment.getAttachmentId());
    	entityManager.remove(a);
    }

	
    public Attachment findAttachment(String attachmentId) {
    	Query query = entityManager.createNamedQuery("Attachment.FindAttachmentById");
    	query.setParameter("attachmentId", attachmentId);
    	return (Attachment)query.getSingleResult();
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
