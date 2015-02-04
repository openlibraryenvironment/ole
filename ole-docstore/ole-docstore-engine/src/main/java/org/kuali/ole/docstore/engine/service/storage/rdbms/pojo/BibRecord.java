package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/25/13
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String bibId;
    private String formerId;
    private Boolean fassAddFlag;
    private String suppressFromPublic; //= new Timestamp(new Date().getTime())
    private Timestamp dateEntered;
    private Timestamp dateCreated;
    private String content;
   // private byte[] binaryContent;
    private String status;
    private Timestamp statusUpdatedDate;
    private String uniqueIdPrefix;
    private Boolean staffOnlyFlag;
    private String createdBy;
    private String updatedBy;
    private String statusUpdatedBy;
    private List<HoldingsRecord> holdingsRecords;

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
        return holdingsRecords;
    }

    public void setHoldingsRecords(List<HoldingsRecord> holdingsRecords) {
        this.holdingsRecords = holdingsRecords;
    }
}
