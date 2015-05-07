package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by hemalathas on 3/23/15.
 */
public class ItemDamagedRecord extends PersistableBusinessObjectBase {

    private String itemDamagedId;
    private String damagedItemNote;
    private Timestamp damagedItemDate;
    private String operatorId;
    private String patronBarcode;
    private String itemId;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemDamagedId() {
        return itemDamagedId;
    }

    public void setItemDamagedId(String itemDamagedId) {
        this.itemDamagedId = itemDamagedId;
    }

    public String getDamagedItemNote() {
        return damagedItemNote;
    }

    public void setDamagedItemNote(String damagedItemNote) {
        this.damagedItemNote = damagedItemNote;
    }

    public Timestamp getDamagedItemDate() {
        return damagedItemDate;
    }

    public void setDamagedItemDate(Timestamp damagedItemDate) {
        this.damagedItemDate = damagedItemDate;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

}
