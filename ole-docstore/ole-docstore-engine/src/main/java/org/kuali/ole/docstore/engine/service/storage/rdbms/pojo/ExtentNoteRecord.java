package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/8/13
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtentNoteRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String extentNoteId;
    private String type;
    private String note;
    private String extOfOwnerShipId;
    private Timestamp updatedDate;

    public static final String EXTENT_OF_OWNERSHIP_ID = "extOfOwnerShipId";

    private ExtentOfOwnerShipRecord extentOfOwnerShipRecord;

    public String getExtentNoteId() {
        return extentNoteId;
    }

    public void setExtentNoteId(String extentNoteId) {
        this.extentNoteId = extentNoteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExtOfOwnerShipId() {
        return extOfOwnerShipId;
    }

    public void setExtOfOwnerShipId(String extOfOwnerShipId) {
        this.extOfOwnerShipId = extOfOwnerShipId;
    }

    public ExtentOfOwnerShipRecord getExtentOfOwnerShipRecord() {
        return extentOfOwnerShipRecord;
    }

    public void setExtentOfOwnerShipRecord(ExtentOfOwnerShipRecord extentOfOwnerShipRecord) {
        this.extentOfOwnerShipRecord = extentOfOwnerShipRecord;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
