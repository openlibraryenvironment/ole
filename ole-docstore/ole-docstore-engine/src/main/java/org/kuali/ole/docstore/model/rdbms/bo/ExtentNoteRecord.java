package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

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
    private String extOfOwnerShipID;


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

    public String getExtOfOwnerShipID() {
        return extOfOwnerShipID;
    }

    public void setExtOfOwnerShipID(String extOfOwnerShipID) {
        this.extOfOwnerShipID = extOfOwnerShipID;
    }
}
