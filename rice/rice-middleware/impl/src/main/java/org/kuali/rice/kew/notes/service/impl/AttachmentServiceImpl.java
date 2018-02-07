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
package org.kuali.rice.kew.notes.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Logger;

import org.kuali.rice.kew.notes.Attachment;
import org.kuali.rice.kew.notes.service.AttachmentService;
import org.kuali.rice.kew.service.KEWServiceLocator;


/**
 * Implementation of the {@link AttachmentService}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AttachmentServiceImpl implements AttachmentService {
	
	protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AttachmentServiceImpl.class);
	
	private static final String ATTACHMENT_PREPEND = "wf_att_";
	
	private String attachmentDir;

	public void persistAttachedFileAndSetAttachmentBusinessObjectValue(Attachment attachment) throws Exception {
		createStorageDirIfNecessary();
		String uniqueId = KEWServiceLocator.getResponsibilityIdService().getNewResponsibilityId();
		String internalFileIndicator = attachment.getFileName().replace('.', '_');
		String fileName = ATTACHMENT_PREPEND + attachment.getNote().getDocumentId() + "_" + internalFileIndicator + "_" + uniqueId;
		File file = File.createTempFile(fileName, null, new File(attachmentDir));
		LOG.info("Persisting attachment at: " + file.getAbsolutePath());
		if (!file.canWrite()) {
			throw new RuntimeException("Do not have permissions to write to attachment file at: " + file.getAbsolutePath());
		}
		FileOutputStream streamOut = null;
        BufferedOutputStream bufferedStreamOut = null;
        try {
            streamOut = new FileOutputStream(file);
            bufferedStreamOut = new BufferedOutputStream(streamOut);
            int c;
            while ((c = attachment.getAttachedObject().read()) != -1) 
                {
                    bufferedStreamOut.write(c);
                }
        } finally {
        	if (bufferedStreamOut != null) {
        		bufferedStreamOut.close();
        	}
            if (streamOut != null) {
            	streamOut.close();
            }
        }
        attachment.setFileLoc(file.getAbsolutePath());
	}

	public File findAttachedFile(Attachment attachment) throws Exception {
		return new File(attachment.getFileLoc());
	}
	
	public void deleteAttachedFile(Attachment attachment) throws Exception {
		File file = new File(attachment.getFileLoc());
		if (! file.delete()) {
			LOG.error("failed to delete file " + attachment.getFileLoc());
		}
	}
	
	private void createStorageDirIfNecessary() {
		if (attachmentDir == null) {
			throw new RuntimeException("Attachment Directory was not set when configuring workflow");
		}
		File attachDir = new File(attachmentDir);
		if (! attachDir.exists()) {
			LOG.warn("No attachment directory found.  Attempting to create directory " + attachmentDir);
			boolean directoriesCreated = attachDir.mkdirs();
			if (!directoriesCreated) {
				throw new RuntimeException("Failed to create directory for attachments at: " + attachDir.getAbsolutePath());
			}
		}
		if (!attachDir.canWrite()) {
			throw new RuntimeException("Do not have permission to write to: " + attachDir.getAbsolutePath());
		}
	}

	public String getAttachmentDir() {
		return attachmentDir;
	}

	public void setAttachmentDir(String attachmentDir) {
		this.attachmentDir = attachmentDir;
	}
	
}
