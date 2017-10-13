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

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.kew.notes.Attachment;
import org.kuali.rice.kew.notes.Note;
import org.kuali.rice.kew.notes.dao.NoteDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;


public class NoteDAOOjbImpl extends PersistenceBrokerDaoSupport implements NoteDAO {

    public Note getNoteByNoteId(String noteId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("noteId", noteId);
        return (Note) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(Note.class, crit));          
    }

    public List getNotesByDocumentId(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        QueryByCriteria query = new QueryByCriteria(Note.class, crit);
        query.addOrderByAscending("noteId");
        return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(query);        
    }
    
    public void saveNote(Note note) {
    	this.getPersistenceBrokerTemplate().store(note);    
    }

    public void deleteNote(Note note) {
        Criteria crit = new Criteria();
        crit.addEqualTo("noteId", note.getNoteId());
        this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(Note.class, crit));
    }
   
    public void deleteAttachment(Attachment attachment) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("attachmentId", attachment.getAttachmentId());
    	this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(Attachment.class, crit));
    }

	
    public Attachment findAttachment(String attachmentId) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("attachmentId", attachmentId);
    	return (Attachment)this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(Attachment.class, crit));
    }

}
