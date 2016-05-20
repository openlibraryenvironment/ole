package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 1/20/14
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEHoldingsDonorRecord extends PersistableBusinessObjectBase
        implements Serializable {


    private String holdingsId;
    private String donorId;
    private String donorCode;
    private String donorPublicDisplay;
    private String donorNote;
    private HoldingsRecord holdingsRecord;
    private Timestamp updatedDate;

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getDonorPublicDisplay() {
        return donorPublicDisplay;
    }

    public void setDonorPublicDisplay(String donorPublicDisplay) {
        this.donorPublicDisplay = donorPublicDisplay;
    }

    public String getDonorNote() {
        return donorNote;
    }

    public void setDonorNote(String donorNote) {
        this.donorNote = donorNote;
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
