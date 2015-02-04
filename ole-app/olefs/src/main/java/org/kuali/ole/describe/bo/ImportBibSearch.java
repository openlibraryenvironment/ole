package org.kuali.ole.describe.bo;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 14/12/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBibSearch {

    private String source;
    private int recordsInFile;
    private int recordsImported = 0;
    private String selectedFileName;
    private List<BibDocumentSearchResult> localBibDocumentSearchResults;
    private List<BibDocumentSearchResult> externalBibDocumentSearchResults;
    private boolean returnCheck;
    private BibDocumentSearchResult selectedMarc;
    private MultipartFile locationFile;
    private String message;
    private int selectedRecordIndex = 0;
    private int recordsInUnicode;
    private int recordsInNonUnicode;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getRecordsInFile() {
        return recordsInFile;
    }

    public void setRecordsInFile(int recordsInFile) {
        this.recordsInFile = recordsInFile;
    }

    public int getRecordsImported() {
        return recordsImported;
    }

    public void setRecordsImported(int recordsImported) {
        this.recordsImported = recordsImported;
    }

    public String getSelectedFileName() {
        return selectedFileName;
    }

    public void setSelectedFileName(String selectedFileName) {
        this.selectedFileName = selectedFileName;
    }

    public List<BibDocumentSearchResult> getLocalBibDocumentSearchResults() {
        return localBibDocumentSearchResults;
    }

    public void setLocalBibDocumentSearchResults(List<BibDocumentSearchResult> localBibDocumentSearchResults) {
        this.localBibDocumentSearchResults = localBibDocumentSearchResults;
    }

    public List<BibDocumentSearchResult> getExternalBibDocumentSearchResults() {
        return externalBibDocumentSearchResults;
    }

    public void setExternalBibDocumentSearchResults(
            List<BibDocumentSearchResult> externalBibDocumentSearchResults) {
        this.externalBibDocumentSearchResults = externalBibDocumentSearchResults;
    }

    public boolean isReturnCheck() {
        return returnCheck;
    }

    public void setReturnCheck(boolean returnCheck) {
        this.returnCheck = returnCheck;
    }


    public BibDocumentSearchResult getSelectedMarc() {
        return selectedMarc;
    }

    public void setSelectedMarc(BibDocumentSearchResult selectedMarc) {
        this.selectedMarc = selectedMarc;
    }

    public MultipartFile getLocationFile() {
        return locationFile;
    }

    public void setLocationFile(MultipartFile locationFile) {
        this.locationFile = locationFile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSelectedRecordIndex() {
        return selectedRecordIndex;
    }

    public void setSelectedRecordIndex(int selectedRecordIndex) {
        this.selectedRecordIndex = selectedRecordIndex;
    }

    public int getRecordsInUnicode() {
        return recordsInUnicode;
    }

    public void setRecordsInUnicode(int recordsInUnicode) {
        this.recordsInUnicode = recordsInUnicode;
    }

    public int getRecordsInNonUnicode() {
        return recordsInNonUnicode;
    }

    public void setRecordsInNonUnicode(int recordsInNonUnicode) {
        this.recordsInNonUnicode = recordsInNonUnicode;
    }
}
