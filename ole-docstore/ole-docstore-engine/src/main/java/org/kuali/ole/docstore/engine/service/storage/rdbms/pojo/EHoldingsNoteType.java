package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 7/20/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class EHoldingsNoteType extends PersistableBusinessObjectBase implements Serializable {

    private String eHoldingsNoteTypeId;
    private String eHoldingsNoteTypeCode;
    private String eHoldingsNoteTypeName;
    private boolean active;

    public String geteHoldingsNoteTypeId() {
        return eHoldingsNoteTypeId;
    }

    public void seteHoldingsNoteTypeId(String eHoldingsNoteTypeId) {
        this.eHoldingsNoteTypeId = eHoldingsNoteTypeId;
    }

    public String geteHoldingsNoteTypeCode() {
        return eHoldingsNoteTypeCode;
    }

    public void seteHoldingsNoteTypeCode(String eHoldingsNoteTypeCode) {
        this.eHoldingsNoteTypeCode = eHoldingsNoteTypeCode;
    }

    public String geteHoldingsNoteTypeName() {
        return eHoldingsNoteTypeName;
    }

    public void seteHoldingsNoteTypeName(String eHoldingsNoteTypeName) {
        this.eHoldingsNoteTypeName = eHoldingsNoteTypeName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
