package org.kuali.ole.batch.bo;

/**
 * Created by sheiksalahudeenm on 6/13/14.
 */
public class OLEListFileLocation{
    private String fileName;
    private String fileLocation;
    private String fileSize;
    private String fileLastModified;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileLastModified() {
        return fileLastModified;
    }

    public void setFileLastModified(String fileLastModified) {
        this.fileLastModified = fileLastModified;
    }
}
