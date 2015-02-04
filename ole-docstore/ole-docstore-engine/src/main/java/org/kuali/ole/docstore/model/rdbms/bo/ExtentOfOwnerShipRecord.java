package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
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

    private String extOfOwnerShipID;
    private String extOfOwnerShipTypeId;
    private String text;
    private String holdingsId;
    private List<ExtentNoteRecord> extentNoteRecords;
    private ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord;

    public String getExtOfOwnerShipID() {
        return extOfOwnerShipID;
    }

    public void setExtOfOwnerShipID(String extOfOwnerShipID) {
        this.extOfOwnerShipID = extOfOwnerShipID;
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
}
