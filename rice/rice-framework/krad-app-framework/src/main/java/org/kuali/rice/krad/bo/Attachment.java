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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KRNS_ATT_T")
public class Attachment extends PersistableBusinessObjectBase {
	private static final long serialVersionUID = 402432724949441326L;
	
    @Id
	@Column(name="NTE_ID")
	private Long noteIdentifier;
	@Column(name="MIME_TYP")
	private String attachmentMimeTypeCode;
	@Column(name="FILE_NM")
	private String attachmentFileName;
	@Column(name="ATT_ID")
	private String attachmentIdentifier;
	@Column(name="FILE_SZ")
	private Long attachmentFileSize;
	@Column(name="ATT_TYP_CD")
	private String attachmentTypeCode;

    @OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="NTE_ID")
	private Note note;

	/**
	 * Default constructor.
	 */
	public Attachment() {

	}

	/**
	 * Gets the noteIdentifier attribute.
	 *
	 * @return Returns the noteIdentifier
	 *
	 */
	public Long getNoteIdentifier() {
		return noteIdentifier;
	}

	/**
	 * Sets the noteIdentifier attribute.
	 *
	 * @param noteIdentifier The noteIdentifier to set.
	 *
	 */
	public void setNoteIdentifier(Long noteIdentifier) {
		this.noteIdentifier = noteIdentifier;
	}


	/**
	 * Gets the attachmentMimeTypeCode attribute.
	 *
	 * @return Returns the attachmentMimeTypeCode
	 *
	 */
	public String getAttachmentMimeTypeCode() {
		return attachmentMimeTypeCode;
	}

	/**
	 * Sets the attachmentMimeTypeCode attribute.
	 *
	 * @param attachmentMimeTypeCode The attachmentMimeTypeCode to set.
	 *
	 */
	public void setAttachmentMimeTypeCode(String attachmentMimeTypeCode) {
		this.attachmentMimeTypeCode = attachmentMimeTypeCode;
	}


	/**
	 * Gets the attachmentFileName attribute.
	 *
	 * @return Returns the attachmentFileName
	 *
	 */
	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	/**
	 * Sets the attachmentFileName attribute.
	 *
	 * @param attachmentFileName The attachmentFileName to set.
	 *
	 */
	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}


	/**
	 * Gets the attachmentIdentifier attribute.
	 *
	 * @return Returns the attachmentIdentifier
	 *
	 */
	public String getAttachmentIdentifier() {
		return attachmentIdentifier;
	}

	/**
	 * Sets the attachmentIdentifier attribute.
	 *
	 * @param attachmentIdentifier The attachmentIdentifier to set.
	 *
	 */
	public void setAttachmentIdentifier(String attachmentIdentifier) {
		this.attachmentIdentifier = attachmentIdentifier;
	}


	/**
	 * Gets the attachmentFileSize attribute.
	 *
	 * @return Returns the attachmentFileSize
	 *
	 */
	public Long getAttachmentFileSize() {
		return attachmentFileSize;
	}

	/**
	 * Sets the attachmentFileSize attribute.
	 *
	 * @param attachmentFileSize The attachmentFileSize to set.
	 *
	 */
	public void setAttachmentFileSize(Long attachmentFileSize) {
		this.attachmentFileSize = attachmentFileSize;
	}
	
    /**
     * Returns the size of the attachment with units (byte, kilobyte, ...)
     * 
     * @return String attachment file size
     */
    public String getAttachmentFileSizeWithUnits() {
        if (attachmentFileSize != null) {
            return RiceUtilities.getFileSizeUnits(attachmentFileSize);
        }

        return "";
    }

	/**
	 * Gets the attachmentTypeCode attribute.
	 *
	 * @return Returns the attachmentTypeCode
	 *
	 */
	public String getAttachmentTypeCode() {
		return attachmentTypeCode;
	}

	/**
	 * Sets the attachmentTypeCode attribute.
	 *
	 * @param attachmentTypeCode The attachmentTypeCode to set.
	 *
	 */
	public void setAttachmentTypeCode(String attachmentTypeCode) {
		this.attachmentTypeCode = attachmentTypeCode;
	}

    /**
     * Gets the note attribute.
     * @return Returns the note.
     */
    public Note getNote() {
        return note;
    }

    /**
     * Sets the note attribute value.
     * @param note The note to set.
     */
    public void setNote(Note note) {
        this.note = note;
    }
    /**
     * @return false if any of the required fields (attachmentId, fileName, fileSize, and mimeType) are blank
     */
    public boolean isComplete() {
        return (StringUtils.isNotBlank(attachmentIdentifier) && StringUtils.isNotBlank(attachmentFileName) && (attachmentFileSize != null) && StringUtils.isNotBlank(attachmentMimeTypeCode));
    }

    /**
     *
     *
     */
    public InputStream getAttachmentContents() throws IOException {
        return KRADServiceLocator.getAttachmentService().retrieveAttachmentContents(this);
    }
}

