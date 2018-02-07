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
package edu.sampleu.travel.bo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.kuali.rice.krad.bo.PersistableAttachment;
import org.kuali.rice.krad.bo.PersistableAttachmentList;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public class AttachmentSample extends PersistableBusinessObjectBase implements PersistableAttachment, PersistableAttachmentList<MultiAttachmentSample> {
    
    private String id;
    private String description;
    
    private String fileName;
    private String contentType;
    private byte[] attachmentContent;
    private transient FormFile attachmentFile;
    
    private List<MultiAttachmentSample> attachments;
    
    public AttachmentSample() {
        this.attachments = new ArrayList<MultiAttachmentSample>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getAttachmentContent() {
        return this.attachmentContent;
    }

    public void setAttachmentContent(byte[] attachmentContent) {
        this.attachmentContent = attachmentContent;
    }

    public FormFile getAttachmentFile() {
        return this.attachmentFile;
    }

    public void setAttachmentFile(FormFile attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    @Override
    public List<MultiAttachmentSample> getAttachments() {
        return this.attachments;
    }

    @Override
    public void setAttachments(List<MultiAttachmentSample> attachments) {
        this.attachments = attachments;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap<String, Object> toStringMap = new LinkedHashMap<String, Object>();
        toStringMap.put("id", id);
        return toStringMap;
    }

    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(this.attachments);
        return managedLists;
    }
}
