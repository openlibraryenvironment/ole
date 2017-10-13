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
package org.kuali.rice.krad.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.bo.Attachment;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.AttachmentDao;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.AttachmentService;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Attachment service implementation
 */
@Transactional
public class AttachmentServiceImpl implements AttachmentService {
	private static final int MAX_DIR_LEVELS = 6;
    private static Logger LOG = Logger.getLogger(AttachmentServiceImpl.class);

    private ConfigurationService kualiConfigurationService;
    private AttachmentDao attachmentDao;
    /**
     * Retrieves an Attachment by note identifier.
     * 
     * @see org.kuali.rice.krad.service.AttachmentService#getAttachmentByNoteId(java.lang.Long)
     */
    public Attachment getAttachmentByNoteId(Long noteId) {
		return attachmentDao.getAttachmentByNoteId(noteId);
	}

    /**
     * @see org.kuali.rice.krad.service.AttachmentService#createAttachment(org.kuali.rice.krad.bo.PersistableBusinessObject,
     * String, String, int, java.io.InputStream, String)
     */
    public Attachment createAttachment(PersistableBusinessObject parent, String uploadedFileName, String mimeType, int fileSize, InputStream fileContents, String attachmentTypeCode) throws IOException {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug("starting to create attachment for document: " + parent.getObjectId());
        }
        if (parent == null) {
            throw new IllegalArgumentException("invalid (null or uninitialized) document");
        }
        if (StringUtils.isBlank(uploadedFileName)) {
            throw new IllegalArgumentException("invalid (blank) fileName");
        }
        if (StringUtils.isBlank(mimeType)) {
            throw new IllegalArgumentException("invalid (blank) mimeType");
        }
        if (fileSize <= 0) {
            throw new IllegalArgumentException("invalid (non-positive) fileSize");
        }
        if (fileContents == null) {
            throw new IllegalArgumentException("invalid (null) inputStream");
        }

        String uniqueFileNameGuid = UUID.randomUUID().toString();
        String fullPathUniqueFileName = getDocumentDirectory(parent.getObjectId()) + File.separator + uniqueFileNameGuid;

        writeInputStreamToFileStorage(fileContents, fullPathUniqueFileName);

        // create DocumentAttachment
        Attachment attachment = new Attachment();
        attachment.setAttachmentIdentifier(uniqueFileNameGuid);
        attachment.setAttachmentFileName(uploadedFileName);
        attachment.setAttachmentFileSize(new Long(fileSize));
        attachment.setAttachmentMimeTypeCode(mimeType);
        attachment.setAttachmentTypeCode(attachmentTypeCode);
        
        LOG.debug("finished creating attachment for document: " + parent.getObjectId());
        return attachment;
    }

    private void writeInputStreamToFileStorage(InputStream fileContents, String fullPathUniqueFileName) throws IOException {
        File fileOut = new File(fullPathUniqueFileName);
        FileOutputStream streamOut = null;
        BufferedOutputStream bufferedStreamOut = null;
        try {
            streamOut = new FileOutputStream(fileOut);
            bufferedStreamOut = new BufferedOutputStream(streamOut);
            int c;
            while ((c = fileContents.read()) != -1) {
                bufferedStreamOut.write(c);
            }
        }
        finally {
            bufferedStreamOut.close();
            streamOut.close();
        }
    }
    
    public void moveAttachmentWherePending(Note note) {
    	if (note == null) {
    		throw new IllegalArgumentException("Note must be non-null");
    	}
    	if (StringUtils.isBlank(note.getObjectId())) {
    		throw new IllegalArgumentException("Note does not have a valid object id, object id was null or empty");
    	}
    	Attachment attachment = note.getAttachment();
    	if(attachment!=null){
    		try {
    			moveAttachmentFromPending(attachment, note.getRemoteObjectIdentifier());
    		}
    		catch (IOException e) {
    			throw new RuntimeException("Problem moving pending attachment to final directory");    
    		}
    	}
    }
    
    private void moveAttachmentFromPending(Attachment attachment, String objectId) throws IOException {
        //This method would probably be more efficient if attachments had a pending flag
        String fullPendingFileName = getPendingDirectory() + File.separator + attachment.getAttachmentIdentifier();
        File pendingFile = new File(fullPendingFileName);
        
        if(pendingFile.exists()) {
            BufferedInputStream bufferedStream = null;
            FileInputStream oldFileStream = null;
            String fullPathNewFile = getDocumentDirectory(objectId) + File.separator + attachment.getAttachmentIdentifier();
            try {
                oldFileStream = new FileInputStream(pendingFile);
                bufferedStream = new BufferedInputStream(oldFileStream);
                writeInputStreamToFileStorage(bufferedStream,fullPathNewFile);
            }
            finally {

                bufferedStream.close();
                oldFileStream.close();
                //this has to come after the close
                pendingFile.delete();
                
            }
        }
        
    }

    public void deleteAttachmentContents(Attachment attachment) {
    	if (attachment.getNote() == null) throw new RuntimeException("Attachment.note must be set in order to delete the attachment");
        String fullPathUniqueFileName = getDocumentDirectory(attachment.getNote().getRemoteObjectIdentifier()) + File.separator + attachment.getAttachmentIdentifier();
        File attachmentFile = new File(fullPathUniqueFileName);
        attachmentFile.delete();
    }
    private String getPendingDirectory() {
        return this.getDocumentDirectory("");
    }

    private String getDocumentDirectory(String objectId) {
        // Create a directory; all ancestor directories must exist
        File documentDirectory = new File(getDocumentFileStorageLocation(objectId));
        if (!documentDirectory.exists()) {
            boolean success = documentDirectory.mkdirs();
            if (!success) {
                throw new RuntimeException("Could not generate directory for File at: " + documentDirectory.getAbsolutePath());
            }
        }
        return documentDirectory.getAbsolutePath();
    }

    /**
     * /* (non-Javadoc)
     *
     * @see org.kuali.rice.krad.service.AttachmentService#retrieveAttachmentContents(org.kuali.rice.krad.bo.Attachment)
     */
    public InputStream retrieveAttachmentContents(Attachment attachment) throws IOException {
        //refresh to get Note object in case it's not there
        if(attachment.getNoteIdentifier()!=null) {
            attachment.refreshNonUpdateableReferences();
        }
        
        String parentDirectory = "";
        if(attachment.getNote()!=null && attachment.getNote().getRemoteObjectIdentifier() != null) {
            parentDirectory = attachment.getNote().getRemoteObjectIdentifier();
        }
         
        return new BufferedInputStream(new FileInputStream(getDocumentDirectory(parentDirectory) + File.separator + attachment.getAttachmentIdentifier()));
    }

    private String getDocumentFileStorageLocation(String objectId) {
        String location = null;
        if(StringUtils.isEmpty(objectId)) {
            location = kualiConfigurationService.getPropertyValueAsString(
                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY);
        } else {    
        	/* 
        	 * We need to create a hierarchical directory structure to store
        	 * attachment directories, as most file systems max out at 16k
        	 * or 32k entries.  If we use 6 levels of hierarchy, it allows
        	 * hundreds of billions of attachment directories.
        	 */
            char[] chars = objectId.toUpperCase().replace(" ", "").toCharArray();            
            int count = chars.length < MAX_DIR_LEVELS ? chars.length : MAX_DIR_LEVELS;

            StringBuffer prefix = new StringBuffer();            
            for ( int i = 0; i < count; i++ )
                prefix.append(File.separator + chars[i]);
            
            location = kualiConfigurationService.getPropertyValueAsString(KRADConstants.ATTACHMENTS_DIRECTORY_KEY) + prefix + File.separator + objectId;
        }
        return  location;
    }

    /**
     * @see org.kuali.rice.krad.service.AttachmentService#deletePendingAttachmentsModifiedBefore(long)
     */
    public void deletePendingAttachmentsModifiedBefore(long modificationTime) {
        String pendingAttachmentDirName = getPendingDirectory();
        if (StringUtils.isBlank(pendingAttachmentDirName)) {
            throw new RuntimeException("Blank pending attachment directory name");
        }
        File pendingAttachmentDir = new File(pendingAttachmentDirName);
        if (!pendingAttachmentDir.exists()) {
            throw new RuntimeException("Pending attachment directory does not exist");
        }
        if (!pendingAttachmentDir.isDirectory()) {
            throw new RuntimeException("Pending attachment directory is not a directory! " + pendingAttachmentDir.getAbsolutePath());
        }
        
        File[] files = pendingAttachmentDir.listFiles();
        for (File file : files) {
            if (!file.getName().equals("placeholder.txt")) {
                if (file.lastModified() < modificationTime) {
                    file.delete();
                }
            }
        }
        
    }
    
    // needed for Spring injection
    /**
     * Sets the data access object
     * 
     * @param d
     */
    public void setAttachmentDao(AttachmentDao d) {
        this.attachmentDao = d;
    }

    /**
     * Retrieves a data access object
     */
    public AttachmentDao getAttachmentDao() {
        return attachmentDao;
    }

    /**
     * Gets the configService attribute. 
     * @return Returns the configService.
     */
    public ConfigurationService getKualiConfigurationService() {
        return kualiConfigurationService;
    }

    /**
     * Sets the configService attribute value.
     * @param configService The configService to set.
     */
    public void setKualiConfigurationService(ConfigurationService configService) {
        this.kualiConfigurationService = configService;
    }
}
