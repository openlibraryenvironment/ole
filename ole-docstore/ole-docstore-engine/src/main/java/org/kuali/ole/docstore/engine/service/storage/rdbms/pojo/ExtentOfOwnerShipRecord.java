package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/28/13
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtentOfOwnerShipRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String extOfOwnerShipId;
    private String extOfOwnerShipTypeId;
    private String text;
    private String holdingsId;
    private int ord;
    private List<ExtentNoteRecord> extentNoteRecords;
    private ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord;
    private Timestamp updatedDate;

    public String getExtOfOwnerShipId() {
        return extOfOwnerShipId;
    }

    public void setExtOfOwnerShipId(String extOfOwnerShipId) {
        this.extOfOwnerShipId = extOfOwnerShipId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public List<ExtentNoteRecord> getExtentNoteRecords() {
        return extentNoteRecords;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public void setExtentNoteRecords(List<ExtentNoteRecord> extentNoteRecords) {
        this.extentNoteRecords = extentNoteRecords;
    }

    public String getExtOfOwnerShipTypeId() {
        return extOfOwnerShipTypeId;
    }

    public void setExtOfOwnerShipTypeId(String extOfOwnerShipTypeId) {
        this.extOfOwnerShipTypeId = extOfOwnerShipTypeId;
    }

    public ExtentOfOwnerShipTypeRecord getExtentOfOwnerShipTypeRecord() {
        return extentOfOwnerShipTypeRecord;
    }

    public void setExtentOfOwnerShipTypeRecord(ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord) {
        this.extentOfOwnerShipTypeRecord = extentOfOwnerShipTypeRecord;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
