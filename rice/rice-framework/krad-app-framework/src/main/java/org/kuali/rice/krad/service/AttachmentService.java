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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.bo.Attachment;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Defines the methods common to all AttachmentService implementations
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface AttachmentService {
    /**
     * Stores the given fileContents and returns referring Attachment object which acts as a momento to the archived object.
     *
     * @param parent - the document to which the attachment belongs
     * @param uploadedFileName - the name of the uploaded file
     * @param mimeType - the uploaded file's mime type
     * @param  fileSize - the size of the uploaded file in bytes
     * @param  fileContents - an input stream used to read the file's contents
     * @param attachmentType -the attachment type code
     *
     * @return Attachment - the attachment, having been written to the file system
     * @throws IOException
     */
    public Attachment createAttachment(PersistableBusinessObject parent, String uploadedFileName, String mimeType, int fileSize, InputStream fileContents, String attachmentType) throws IOException;

    /**
     * Retrieves a given Attachments contents from the corresponding Attachment object
     *
     * @param attachment - the attachment whose contents are to be retrieved
     *
     * @return OutputStream
     * @throws IOException
     */
    public InputStream retrieveAttachmentContents(Attachment attachment) throws IOException;

    /**
     * Deletes a given DocumentAttachment contents from the corresponding Attachment object
     *
     * @param attachment - the attachment whose contents are to be deleted
     */
    public void deleteAttachmentContents(Attachment attachment);
    
    /**
     * 
     * Moves attachments on notes from the pending directory to the real one
     * @param note the Note from which to move attachments.  If this Note does not
     * have an attachment then this method does nothing.
     * 
     * @throws IllegalArgumentException if the given Note is null
     * @throws IllegalArgumentException if the Note does not have a valid object id
     */
    public void moveAttachmentWherePending(Note note);
    
    /**
     * Deletes pending attachments that were last modified before the given time.  Java does not have easy access to a file's creation
     * time, so we use modification time instead.
     * 
     * @param modificationTime the number of milliseconds since "the epoch" (i.e.January 1, 1970, 00:00:00 GMT).  java.util.Date and java.util.Calendar's
     *  methods return time in this format.  If a pending attachment was modified before this time, then it will be deleted (unless an error occurs)
     */
    public void deletePendingAttachmentsModifiedBefore(long modificationTime);
    
    public Attachment getAttachmentByNoteId(Long noteId);
}
