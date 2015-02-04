package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/8/13
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsNoteRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String noteId;
    private String type;
    private String note;
    private String holdingsId;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
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

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }
}
