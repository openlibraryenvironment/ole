/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.test.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.struts.upload.FormFile;
import org.springframework.util.FileCopyUtils;

/**
 * A mock FormFile which is constructed directly from a File on disk 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MockFormFile implements FormFile {
    private File file;
    private String fileName;
    private int fileSize;
    private String contentType = "application/octet-stream";

    public MockFormFile(File file) {
        this.file = file;
        this.fileName = file.getName();
        this.fileSize = (int) file.length();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getFileData() throws FileNotFoundException, IOException {
        return FileCopyUtils.copyToByteArray(file);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public InputStream getInputStream() throws FileNotFoundException, IOException {
        return new FileInputStream(file);
    }

    public void destroy() {
    }

    public String toString() {
        return "[MockFormFile: " + file + "]";
    }
}
