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
package org.kuali.rice.kew.notes;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.note.NoteContract;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A note attached to a document.  May also contain a List of attachments.
 * 
 * @see Attachment
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity(name="org.kuali.rice.kew.notes.Note")
@Table(name="KREW_DOC_NTE_T")
//@Sequence(name="KREW_DOC_NTE_S",property="noteId")
@NamedQueries({
	@NamedQuery(name="KewNote.FindNoteByNoteId",query="select n from org.kuali.rice.kew.notes.Note as n where n.noteId = :noteId"),
	@NamedQuery(name="KewNote.FindNoteByDocumentId", query="select n from org.kuali.rice.kew.notes.Note as n where n.documentId = :documentId order by n.noteId")
})
public class Note implements Serializable, NoteContract {

	private static final long serialVersionUID = -6136544551121011531L;
	@Id
	@GeneratedValue(generator="KREW_DOC_NTE_S")
	@GenericGenerator(name="KREW_DOC_NTE_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_DOC_NTE_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="DOC_NTE_ID")
	private String noteId;
    @Column(name="DOC_HDR_ID")
	private String documentId;
    @Column(name="AUTH_PRNCPL_ID")
	private String noteAuthorWorkflowId;
	@Column(name="CRT_DT")
	private Timestamp noteCreateDate;
    @Column(name="TXT")
	private String noteText;
    @Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;
    
    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST},
    	targetEntity=org.kuali.rice.kew.notes.Attachment.class, mappedBy="note")
    @Fetch(value = FetchMode.SELECT)
    private List<Attachment> attachments = new ArrayList<Attachment>();

    //additional data not in database
    @Transient
    private String noteAuthorEmailAddress;
    @Transient
    private String noteAuthorNetworkId;
    @Transient
    private String noteAuthorFullName;
    @Transient
    private Long noteCreateLongDate;
    @Transient
    private Boolean authorizedToEdit; 
    @Transient
    private Boolean editingNote;
    
    public Integer getLockVerNbr() {
        return lockVerNbr;
    }

    public void setLockVerNbr(Integer lockVerNbr) {
        this.lockVerNbr = lockVerNbr;
    }

    public String getNoteAuthorWorkflowId() {
        return noteAuthorWorkflowId;
    }

    public void setNoteAuthorWorkflowId(String noteAuthorWorkflowId) {
        this.noteAuthorWorkflowId = noteAuthorWorkflowId;
    }

    public Timestamp getNoteCreateDate() {
        return noteCreateDate;
    }

    public void setNoteCreateDate(Timestamp noteCreateDate) {
        this.noteCreateDate = noteCreateDate;
    }

    public String getNoteId() {
        return noteId;
    }
 
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
 
    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getNoteAuthorEmailAddress() {
        return noteAuthorEmailAddress;
    }
 
    public void setNoteAuthorEmailAddress(String noteAuthorEmailAddress) {
        this.noteAuthorEmailAddress = noteAuthorEmailAddress;
    }

    public String getNoteAuthorFullName() {
        return noteAuthorFullName;
    }

    public void setNoteAuthorFullName(String noteAuthorFullName) {
        this.noteAuthorFullName = noteAuthorFullName;
    }

    public String getNoteAuthorNetworkId() {
        return noteAuthorNetworkId;
    }

    public void setNoteAuthorNetworkId(String noteAuthorNetworkId) {
        this.noteAuthorNetworkId = noteAuthorNetworkId;
    }

    public Long getNoteCreateLongDate() {
        return noteCreateLongDate;
    }

    public void setNoteCreateLongDate(Long noteCreateLongDate) {
        this.noteCreateLongDate = noteCreateLongDate;
    }

    public Boolean getAuthorizedToEdit() {
        return authorizedToEdit;
    }

    public void setAuthorizedToEdit(Boolean authorizedToEdit) {
        this.authorizedToEdit = authorizedToEdit;
    }

    public Boolean getEditingNote() {
        return editingNote;
    }

    public void setEditingNote(Boolean editingNote) {
        this.editingNote = editingNote;
    }

    public String getFormattedCreateDateTime() {
        long time = getNoteCreateDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat(KewApiConstants.TIMESTAMP_DATE_FORMAT_PATTERN2);
        return dateFormat.format(date);
    }

    public String getFormattedCreateDate() {
        long time = getNoteCreateDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        DateFormat dateFormat = RiceConstants.getDefaultDateFormat();
        return dateFormat.format(date);
    }
    
    public String getFormattedCreateTime() {
        long time = getNoteCreateDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        DateFormat dateFormat = RiceConstants.getDefaultTimeFormat();
        return dateFormat.format(date);
    }

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	//@PrePersist
	public void beforeInsert(){
		OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
	}
	
	// new methods from NoteContract in 2.0

	@Override
	public String getId() {
		if (getNoteId() == null) {
			return null;
		}
		return getNoteId().toString();
	}

	@Override
	public Long getVersionNumber() {
		if (getLockVerNbr() == null) {
			return null;
		}
		return new Long(getLockVerNbr().longValue());
	}

	@Override
	public String getAuthorPrincipalId() {
		return getNoteAuthorWorkflowId();
	}

	@Override
	public DateTime getCreateDate() {
		if (getNoteCreateDate() == null) {
			return null;
		}
		return new DateTime(getNoteCreateDate().getTime());
	}

	@Override
	public String getText() {
		return getNoteText();
	}
	
	public static org.kuali.rice.kew.api.note.Note to(Note note) {
		if (note == null) {
			return null;
		}
		return org.kuali.rice.kew.api.note.Note.Builder.create(note).build();
	}
	
	public static Note from(org.kuali.rice.kew.api.note.Note note) {
		if (note == null) {
			return null;
		}
		Note noteBo = new Note();
		if (note.getId() != null) {
			noteBo.setNoteId(note.getId());
		}
		noteBo.setDocumentId(note.getDocumentId());
		noteBo.setNoteAuthorWorkflowId(note.getAuthorPrincipalId());
		if (note.getCreateDate() != null) {
			noteBo.setNoteCreateDate(new Timestamp(note.getCreateDate().getMillis()));
		}
		noteBo.setNoteText(note.getText());
		if (note.getVersionNumber() != null) {
			noteBo.setLockVerNbr(Integer.valueOf(note.getVersionNumber().intValue()));
		}
		return noteBo;
	}
	
}

