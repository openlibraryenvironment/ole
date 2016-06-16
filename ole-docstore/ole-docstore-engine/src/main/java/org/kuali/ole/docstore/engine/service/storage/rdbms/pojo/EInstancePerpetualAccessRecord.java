package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 7/19/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class EInstancePerpetualAccessRecord extends PersistableBusinessObjectBase implements Serializable {

    private String eInstancePerpetualAccessId;
    private String holdingsId;
    private String perpetualAccessStartDate;
    private String perpetualAccessStartVolume;
    private String perpetualAccessStartIssue;
    private String perpetualAccessEndDate;
    private String perpetualAccessEndVolume;
    private String perpetualAccessEndIssue;
    private HoldingsRecord holdingsRecord;
    private Timestamp updatedDate;

    public String getEInstancePerpetualAccessId() {
        return eInstancePerpetualAccessId;
    }

    public void setEInstancePerpetualAccessId(String eInstancePerpetualAccessId) {
        this.eInstancePerpetualAccessId = eInstancePerpetualAccessId;
    }

    public String getPerpetualAccessStartDate() {
        return perpetualAccessStartDate;
    }

    public void setPerpetualAccessStartDate(String perpetualAccessStartDate) {
        this.perpetualAccessStartDate = perpetualAccessStartDate;
    }

    public String getPerpetualAccessStartVolume() {
        return perpetualAccessStartVolume;
    }

    public void setPerpetualAccessStartVolume(String perpetualAccessStartVolume) {
        this.perpetualAccessStartVolume = perpetualAccessStartVolume;
    }

    public String getPerpetualAccessStartIssue() {
        return perpetualAccessStartIssue;
    }

    public void setPerpetualAccessStartIssue(String perpetualAccessStartIssue) {
        this.perpetualAccessStartIssue = perpetualAccessStartIssue;
    }

    public String getPerpetualAccessEndDate() {
        return perpetualAccessEndDate;
    }

    public void setPerpetualAccessEndDate(String perpetualAccessEndDate) {
        this.perpetualAccessEndDate = perpetualAccessEndDate;
    }

    public String getPerpetualAccessEndVolume() {
        return perpetualAccessEndVolume;
    }

    public void setPerpetualAccessEndVolume(String perpetualAccessEndVolume) {
        this.perpetualAccessEndVolume = perpetualAccessEndVolume;
    }

    public String getPerpetualAccessEndIssue() {
        return perpetualAccessEndIssue;
    }

    public void setPerpetualAccessEndIssue(String perpetualAccessEndIssue) {
        this.perpetualAccessEndIssue = perpetualAccessEndIssue;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public HoldingsRecord getHoldingsRecord() {
        return holdingsRecord;
    }

    public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
        this.holdingsRecord = holdingsRecord;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
