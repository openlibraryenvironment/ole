package org.kuali.ole.docstore.common.response;

import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by rajeshbabuk on 4/26/16.
 */
public class BatchExportFileResponse {

    private List<Record> marcRecords;
    private SortedSet<String> deletedBibIds;
    private String fileType;
    private String fileName;
    private int fileNumber;
    private String profileName;
    private String directoryName;

    public List<Record> getMarcRecords() {
        if (null == marcRecords) {
            marcRecords = new ArrayList<>();
        }
        return marcRecords;
    }

    public void setMarcRecords(List<Record> marcRecords) {
        this.marcRecords = marcRecords;
    }

    public SortedSet<String> getDeletedBibIds() {
        return deletedBibIds;
    }

    public void setDeletedBibIds(SortedSet<String> deletedBibIds) {
        this.deletedBibIds = deletedBibIds;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
}
