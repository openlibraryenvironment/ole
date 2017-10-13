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
package org.kuali.rice.krad.bo;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Represents a user note in the system.
 */
@Entity
@Table(name="KRNS_NTE_T")
public class Note extends PersistableBusinessObjectBase {
    private static final long serialVersionUID = -7647166354016356770L;

    @Id
    @GeneratedValue(generator="KRNS_NTE_S")
	@GenericGenerator(name="KRNS_NTE_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRNS_NTE_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="NTE_ID")
	private Long noteIdentifier;
    @Column(name="RMT_OBJ_ID")
	private String remoteObjectIdentifier;
    @Column(name="AUTH_PRNCPL_ID")
	private String authorUniversalIdentifier;
	@Column(name="POST_TS")
	private Timestamp notePostedTimestamp;
    @Column(name="NTE_TYP_CD")
	private String noteTypeCode;
    @Column(name="TXT")
	private String noteText;
    @Column(name="TPC_TXT")
	private String noteTopicText;
    @Column(name="PRG_CD")
	private String notePurgeCode;
    @Transient
    private String attachmentIdentifier;

    @OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="NTE_TYP_CD", insertable=false, updatable=false)
	private NoteType noteType;
    @Transient
    private transient Person authorUniversal;
    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "NTE_ID", insertable = false, updatable = false)
	private Attachment attachment;
    @Transient
    private AdHocRouteRecipient adHocRouteRecipient;

    /**
     * Default constructor.
     */
    public Note() {
        super();

        //this.setNotePostedTimestampToCurrent();
        this.setNoteText(KRADConstants.EMPTY_STRING);
        // for now just do this
        this.setNoteTypeCode("DH");

        this.setAdHocRouteRecipient(new AdHocRoutePerson());
    }
    
    /**
     * Sets the {@link #setNotePostedTimestamp(Timestamp)} to the current time.
     */
    public void setNotePostedTimestampToCurrent() {
    	final Timestamp now = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
    	this.setNotePostedTimestamp(now);
    }

    /**
     * Gets the noteIdentifier attribute.
     * 
     * @return Returns the noteIdentifier.
     */
    public Long getNoteIdentifier() {
        return noteIdentifier;
    }

    /**
     * Sets the noteIdentifier attribute value.
     * 
     * @param noteIdentifier The noteIdentifier to set.
     */
    public void setNoteIdentifier(Long noteIdentifier) {
        this.noteIdentifier = noteIdentifier;
    }

    /**
     * Gets the remoteObjectIdentifier attribute.
     * 
     * @return Returns the remoteObjectIdentifier
     */
    public String getRemoteObjectIdentifier() {
        return remoteObjectIdentifier;
    }

    /**
     * Sets the remoteObjectIdentifier attribute.
     * 
     * @param remoteObjectIdentifier The remoteObjectIdentifier to set.
     */
    public void setRemoteObjectIdentifier(String remoteObjectIdentifier) {
        this.remoteObjectIdentifier = remoteObjectIdentifier;
    }


    /**
     * Gets the authorUniversalIdentifier attribute.
     * 
     * @return Returns the authorUniversalIdentifier
     */
    public String getAuthorUniversalIdentifier() {
        return authorUniversalIdentifier;
    }

    /**
     * Sets the authorUniversalIdentifier attribute.
     * 
     * @param noteAuthorIdentifier The author ID to be set as the AuthorUniversalIdentifier
     */
    public void setAuthorUniversalIdentifier(String noteAuthorIdentifier) {
        this.authorUniversalIdentifier = noteAuthorIdentifier;
    }


    /**
     * Gets the notePostedTimestamp attribute.
     * 
     * @return Returns the notePostedTimestamp
     */
    public Timestamp getNotePostedTimestamp() {
        return notePostedTimestamp;
    }

    /**
     * Sets the notePostedTimestamp attribute.
     * 
     * @param notePostedTimestamp The notePostedTimestamp to set.
     */
    public void setNotePostedTimestamp(Timestamp notePostedTimestamp) {
        this.notePostedTimestamp = notePostedTimestamp;
    }


    /**
     * Gets the noteTypeCode attribute.
     * 
     * @return Returns the noteTypeCode
     */
    public String getNoteTypeCode() {
        return noteTypeCode;
    }

    /**
     * Sets the noteTypeCode attribute.
     * 
     * @param noteTypeCode The noteTypeCode to set.
     */
    public void setNoteTypeCode(String noteTypeCode) {
        this.noteTypeCode = noteTypeCode;
    }


    /**
     * Gets the noteText attribute.
     * 
     * @return Returns the noteText
     */
    public String getNoteText() {
        return noteText;
    }

    /**
     * Sets the noteText attribute.
     * 
     * @param noteText The noteText to set.
     */
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }


    /**
     * Gets the noteTopicText attribute.
     * 
     * @return Returns the noteTopicText.
     */
    public String getNoteTopicText() {
        return noteTopicText;
    }

    /**
     * Sets the noteTopicText attribute value.
     * 
     * @param noteTopicText The noteTopicText to set.
     */
    public void setNoteTopicText(String noteTopicText) {
        this.noteTopicText = noteTopicText;
    }

    /**
     * Gets the notePurgeCode attribute.
     * 
     * @return Returns the notePurgeCode
     */
    public String getNotePurgeCode() {
        return notePurgeCode;
    }

    /**
     * Sets the notePurgeCode attribute.
     * 
     * @param notePurgeCode The notePurgeCode to set.
     */
    public void setNotePurgeCode(String notePurgeCode) {
        this.notePurgeCode = notePurgeCode;
    }

    /**
     * Gets the noteType attribute.
     * 
     * @return Returns the noteType.
     */
    public NoteType getNoteType() {
        return noteType;
    }

    /**
     * Sets the noteType attribute value.
     * 
     * @param noteType The noteType to set.
     * @deprecated
     */
    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    /**
     * Gets the authorUniversal attribute.
     * 
     * @return Returns the authorUniversal.
     */
    public Person getAuthorUniversal() {
        authorUniversal = KimApiServiceLocator.getPersonService().updatePersonIfNecessary(authorUniversalIdentifier, authorUniversal);
        return authorUniversal;
    }

    /**
     * Sets the authorUniversal attribute value.
     * 
     * @param authorUniversal The authorUniversal to set.
     * @deprecated
     */
    public void setAuthorUniversal(Person authorUniversal) {
        this.authorUniversal = authorUniversal;
    }

    /**
     * Gets the attachment attribute.
     * 
     * @return Returns the attachment.
     */
    public Attachment getAttachment() {
        return attachment;
    }

    /**
     * Sets the attachment attribute value.
     * 
     * @param attachment The attachment to set.
     */
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    /**
     * Gets the attachmentIdentifier attribute.
     * 
     * @return Returns the attachmentIdentifier.
     */
    public String getAttachmentIdentifier() {
        return attachmentIdentifier;
    }

    /**
     * Sets the attachmentIdentifier attribute value.
     * 
     * @param attachmentIdentifier The attachmentIdentifier to set.
     */
    public void setAttachmentIdentifier(String attachmentIdentifier) {
        this.attachmentIdentifier = attachmentIdentifier;
    }

    /**
     * Adds the given attachment to this note. More specifically, sets both the attachmentIdentifier and the attachment reference,
     * since they both need to be done separately now that we aren't using anonymous keys.
     * 
     * @param attachment
     */
    public void addAttachment(Attachment attachment) {
        setAttachmentIdentifier(attachment.getAttachmentIdentifier());
        setAttachment(attachment);

        // copy foreign key and redundant values into attachment
        attachment.setNoteIdentifier(noteIdentifier);
        // we'll need this note reference if the attachment is deleted
        // before the note is saved
        attachment.setNote(this);
    }

    /**
     * Removes the current attachment, if any. More specifically, clears both the attachmentIdentifier and the attachment reference,
     * since they both need to be done separately now that we aren't using anonymous keys.
     */
    public void removeAttachment() {
        setAttachment(null);
        setAttachmentIdentifier(null);
    }

    /**
     * @return the adHocRouteRecipient
     */
    public AdHocRouteRecipient getAdHocRouteRecipient() {
        return adHocRouteRecipient;
    }

    /**
     * @param adHocRouteRecipient the adHocRouteRecipient to set
     */
    public void setAdHocRouteRecipient(AdHocRouteRecipient adHocRouteRecipient) {
        this.adHocRouteRecipient = adHocRouteRecipient;
    }
    /**
     * @return the attachmentLink
     */
    public String getAttachmentLink() {
        //getAttachment() is always return null.
        if(KRADServiceLocator.getAttachmentService().getAttachmentByNoteId(this.getNoteIdentifier()) == null){
            return "";
        }else{
            Properties params = new Properties();
            params.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.DOWNLOAD_BO_ATTACHMENT_METHOD);
            params.put(KRADConstants.DOC_FORM_KEY, "88888888");
            params.put(KRADConstants.NOTE_IDENTIFIER, this.getNoteIdentifier().toString());
            return UrlFactory.parameterizeUrl(KRADConstants.INQUIRY_ACTION, params);
        }
    }
}


