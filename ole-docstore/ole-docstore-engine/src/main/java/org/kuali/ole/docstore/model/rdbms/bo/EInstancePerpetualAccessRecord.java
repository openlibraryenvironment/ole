package org.kuali.ole.docstore.model.rdbms.bo;

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
    private String eHoldingsIdentifier;
    private Date perpetualAccessStartDate;
    private String perpetualAccessStartVolume;
    private String perpetualAccessStartIssue;
    private Date perpetualAccessEndDate;
    private String perpetualAccessEndVolume;
    private String perpetualAccessEndIssue;
    private EHoldingsRecord eHoldingsRecord;

    public String getEInstancePerpetualAccessId() {
        return eInstancePerpetualAccessId;
    }

    public void setEInstancePerpetualAccessId(String eInstancePerpetualAccessId) {
        this.eInstancePerpetualAccessId = eInstancePerpetualAccessId;
    }

    public Date getPerpetualAccessStartDate() {
        return perpetualAccessStartDate;
    }

    public void setPerpetualAccessStartDate(Date perpetualAccessStartDate) {
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

    public Date getPerpetualAccessEndDate() {
        return perpetualAccessEndDate;
    }

    public void setPerpetualAccessEndDate(Date perpetualAccessEndDate) {
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

    public EHoldingsRecord getEHoldingsRecord() {
        return eHoldingsRecord;
    }

    public void setEHoldingsRecord(EHoldingsRecord eHoldingsRecord) {
        this.eHoldingsRecord = eHoldingsRecord;
    }

    public String getEHoldingsIdentifier() {
        return eHoldingsIdentifier;
    }

    public void setEHoldingsIdentifier(String eHoldingsIdentifier) {
        this.eHoldingsIdentifier = eHoldingsIdentifier;
    }
}
