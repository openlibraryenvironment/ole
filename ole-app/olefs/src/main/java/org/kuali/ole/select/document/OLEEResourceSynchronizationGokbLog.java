package org.kuali.ole.select.document;



import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import java.sql.Timestamp;

/**
 * Created by chenchulakshmig on 12/28/14.
 */
public class OLEEResourceSynchronizationGokbLog extends PersistableBusinessObjectBase{

    private String eResSynchronizationGokbLogId;
    private String oleERSIdentifier;
    private Integer eResUpdatedCount;
    private Integer eHoldingsAddedCount;
    private Integer eHoldingsUpdatedCount;
    private Integer eHoldingsRetiredCount;
    private Integer eHoldingsDeletedCount;
    private Integer bibAddedCount;
    private Integer vendorsAddedCount;
    private Integer vendorsUpdatedCount;
    private Integer platformsAddedCount;
    private Integer platformsUpdatedCount;
    private String userName;
    private String status;
    private String gokbconfig;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp updatedDate;

    public String geteResSynchronizationGokbLogId() {
        return eResSynchronizationGokbLogId;
    }

    public void seteResSynchronizationGokbLogId(String eResSynchronizationGokbLogId) {
        this.eResSynchronizationGokbLogId = eResSynchronizationGokbLogId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public Integer geteResUpdatedCount() {
        return eResUpdatedCount;
    }

    public void seteResUpdatedCount(Integer eResUpdatedCount) {
        this.eResUpdatedCount = eResUpdatedCount;
    }

    public Integer geteHoldingsAddedCount() {
        return eHoldingsAddedCount;
    }

    public void seteHoldingsAddedCount(Integer eHoldingsAddedCount) {
        this.eHoldingsAddedCount = eHoldingsAddedCount;
    }

    public Integer geteHoldingsUpdatedCount() {
        return eHoldingsUpdatedCount;
    }

    public void seteHoldingsUpdatedCount(Integer eHoldingsUpdatedCount) {
        this.eHoldingsUpdatedCount = eHoldingsUpdatedCount;
    }

    public Integer geteHoldingsRetiredCount() {
        return eHoldingsRetiredCount;
    }

    public void seteHoldingsRetiredCount(Integer eHoldingsRetiredCount) {
        this.eHoldingsRetiredCount = eHoldingsRetiredCount;
    }

    public Integer geteHoldingsDeletedCount() {
        return eHoldingsDeletedCount;
    }

    public void seteHoldingsDeletedCount(Integer eHoldingsDeletedCount) {
        this.eHoldingsDeletedCount = eHoldingsDeletedCount;
    }

    public Integer getBibAddedCount() {
        return bibAddedCount;
    }

    public void setBibAddedCount(Integer bibAddedCount) {
        this.bibAddedCount = bibAddedCount;
    }

    public Integer getVendorsAddedCount() {
        return vendorsAddedCount;
    }

    public void setVendorsAddedCount(Integer vendorsAddedCount) {
        this.vendorsAddedCount = vendorsAddedCount;
    }

    public Integer getVendorsUpdatedCount() {
        return vendorsUpdatedCount;
    }

    public void setVendorsUpdatedCount(Integer vendorsUpdatedCount) {
        this.vendorsUpdatedCount = vendorsUpdatedCount;
    }

    public Integer getPlatformsAddedCount() {
        return platformsAddedCount;
    }

    public void setPlatformsAddedCount(Integer platformsAddedCount) {
        this.platformsAddedCount = platformsAddedCount;
    }

    public Integer getPlatformsUpdatedCount() {
        return platformsUpdatedCount;
    }

    public void setPlatformsUpdatedCount(Integer platformsUpdatedCount) {
        this.platformsUpdatedCount = platformsUpdatedCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGokbconfig() {
        return gokbconfig;
    }

    public void setGokbconfig(String gokbconfig) {
        this.gokbconfig = gokbconfig;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
