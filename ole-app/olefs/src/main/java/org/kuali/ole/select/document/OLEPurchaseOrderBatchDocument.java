package org.kuali.ole.select.document;

import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 5/15/15
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchaseOrderBatchDocument extends TransactionalDocumentBase  {

    private MultipartFile ingestedFile;
    private MultipartFile docIdIngestFile;
    private String uploadFileName;
    private String type;
    private Date batchStartDate;
    private String time;
    private Integer documentIdentifier;
    private String message;
    private String principalName;

    public void initiateDocument() throws WorkflowException {
        this.getDocumentHeader().setDocumentDescription("Test");
    }

    public MultipartFile getIngestedFile() {
        return ingestedFile;
    }

    public void setIngestedFile(MultipartFile ingestedFile) {
        this.ingestedFile = ingestedFile;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getBatchStartDate() {
        return batchStartDate;
    }

    public void setBatchStartDate(Date batchStartDate) {
        this.batchStartDate = batchStartDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MultipartFile getDocIdIngestFile() {
        return docIdIngestFile;
    }

    public void setDocIdIngestFile(MultipartFile docIdIngestFile) {
        this.docIdIngestFile = docIdIngestFile;
    }

    public Integer getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(Integer documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }
}
