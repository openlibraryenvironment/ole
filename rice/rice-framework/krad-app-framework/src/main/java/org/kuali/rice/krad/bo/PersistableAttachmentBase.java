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

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

/**
 * This is a description of what this class does - chitra07 don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
public class PersistableAttachmentBase extends PersistableBusinessObjectBase implements PersistableAttachment {
	@Lob
	@Column(name="ATT_CNTNT")
    private byte[] attachmentContent;
	@Column(name="FILE_NM")
    private String fileName;
	@Column(name="CNTNT_TYP")
    private String contentType;

    /**
     * This overridden method ...
     * 
     * @see PersistableAttachment#getAttachmentContent()
     */
    public byte[] getAttachmentContent() {
        return this.attachmentContent;
    }

    /**
     * This overridden method ...
     * 
     * @see PersistableAttachment#setAttachmentContent(byte[])
     */
    public void setAttachmentContent(byte[] attachmentContent) {
        this.attachmentContent = attachmentContent;
    }

    /**
     * This overridden method ...
     * 
     * @see PersistableAttachment#getFileName()
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This overridden method ...
     * 
     * @see PersistableAttachment#setFileName(java.lang.String)
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * This overridden method ...
     * 
     * @see PersistableAttachment#getContentType()
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * This overridden method ...
     * 
     * @see PersistableAttachment#setContentType(java.lang.String)
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
