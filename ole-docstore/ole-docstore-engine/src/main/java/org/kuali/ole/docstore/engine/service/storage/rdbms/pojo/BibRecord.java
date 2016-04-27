package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.ole.audit.AuditField;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/25/13
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */

@JsonAutoDetect(JsonMethod.FIELD)
public class BibRecord extends PersistableBusinessObjectBase
        implements Serializable {

    @JsonProperty("bibId")
    private String bibId;

    @JsonProperty("formerId")
    private String formerId;

    @AuditField
    @JsonProperty("fassAddFlag")
    private Boolean fassAddFlag;

    @JsonProperty("suppressFromPublic")
    private String suppressFromPublic; //= new Timestamp(new Date().getTime())

    @JsonProperty("dateEntered")
    private Timestamp dateEntered;

    @JsonProperty("dateCreated")
    private Timestamp dateCreated;

    @AuditField
    @JsonProperty("content")
    private String content;
    // private byte[] binaryContent;

    @JsonProperty("status")
    private String status;

    @JsonProperty("statusUpdatedDate")
    private Timestamp statusUpdatedDate;

    @JsonProperty("uniqueIdPrefix")
    private String uniqueIdPrefix;

    @AuditField
    @JsonProperty("staffOnlyFlag")
    private Boolean staffOnlyFlag;

    @JsonProperty("createdBy")
    private String createdBy;

    @AuditField
    @JsonProperty("updatedBy")
    private String updatedBy;

    @AuditField
    @JsonProperty("statusUpdatedBy")
    private String statusUpdatedBy;

    @JsonProperty("holdingsRecords")
    private List<HoldingsRecord> holdingsRecords;

    @JsonProperty("bibInfoRecord")
    private BibInfoRecord bibInfoRecord;

    private String operationType;

  /*  public byte[] getBinaryContent() {
        return binaryContent;
    }

    public void setBinaryContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }
    */
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    public void setStatusUpdatedBy(String statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getFormerId() {
        return formerId;
    }

    public void setFormerId(String formerId) {
        this.formerId = formerId;
    }

    public Boolean getFassAddFlag() {
        return fassAddFlag;
    }

    public void setFassAddFlag(Boolean fassAddFlag) {
        this.fassAddFlag = fassAddFlag;
    }

    public String getSuppressFromPublic() {
        return suppressFromPublic;
    }

    public void setSuppressFromPublic(String suppressFromPublic) {
        this.suppressFromPublic = suppressFromPublic;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Timestamp dateEntered) {
        this.dateEntered = dateEntered;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContent() {
      /*  byte[] binaryContent = this.getBinaryContent();
        try {
             content = new String(binaryContent,"UTF-8");
        }
        catch(Exception e) {
            e.printStackTrace();
        }*/
        return content;
    }

    public void setContent(String content) {
        this.content = content;
       /* try {
             binaryContent = content.getBytes("UTF-8");
        }
        catch(Exception e) {
            e.printStackTrace();
        }*/
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Date getStatusUpdatedDate() {
        return statusUpdatedDate;
    }

    public void setStatusUpdatedDate(Timestamp statusUpdatedDate) {
        this.statusUpdatedDate = statusUpdatedDate;
    }

    public String getUniqueIdPrefix() {
        return uniqueIdPrefix;
    }

    public void setUniqueIdPrefix(String uniqueIdPrefix) {
        this.uniqueIdPrefix = uniqueIdPrefix;
    }

    public Boolean getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(Boolean staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public List<HoldingsRecord> getHoldingsRecords() {
        if(holdingsRecords == null){
            holdingsRecords = new ArrayList<>();
        }
        return holdingsRecords;
    }

    public void setHoldingsRecords(List<HoldingsRecord> holdingsRecords) {
        this.holdingsRecords = holdingsRecords;
    }

    public BibInfoRecord getBibInfoRecord() {
        return bibInfoRecord;
    }

    public void setBibInfoRecord(BibInfoRecord bibInfoRecord) {
        this.bibInfoRecord = bibInfoRecord;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
