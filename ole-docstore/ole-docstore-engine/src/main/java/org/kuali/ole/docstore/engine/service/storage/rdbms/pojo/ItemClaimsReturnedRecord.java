package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by angelind on 3/24/15.
 */
public class ItemClaimsReturnedRecord extends PersistableBusinessObjectBase implements Serializable {

    private Integer claimsReturnedId;

    private Timestamp claimsReturnedFlagCreateDate;

    private String claimsReturnedNote;

    private String claimsReturnedPatronBarcode;

    private String claimsReturnedOperatorId;

    private String itemId;

    public Integer getClaimsReturnedId() {
        return claimsReturnedId;
    }

    public void setClaimsReturnedId(Integer claimsReturnedId) {
        this.claimsReturnedId = claimsReturnedId;
    }

    public Timestamp getClaimsReturnedFlagCreateDate() {
        return claimsReturnedFlagCreateDate;
    }

    public void setClaimsReturnedFlagCreateDate(Timestamp claimsReturnedFlagCreateDate) {
        this.claimsReturnedFlagCreateDate = claimsReturnedFlagCreateDate;
    }

    public String getClaimsReturnedNote() {
        return claimsReturnedNote;
    }

    public void setClaimsReturnedNote(String claimsReturnedNote) {
        this.claimsReturnedNote = claimsReturnedNote;
    }

    public String getClaimsReturnedPatronBarcode() {
        return claimsReturnedPatronBarcode;
    }

    public void setClaimsReturnedPatronBarcode(String claimsReturnedPatronBarcode) {
        this.claimsReturnedPatronBarcode = claimsReturnedPatronBarcode;
    }

    public String getClaimsReturnedOperatorId() {
        return claimsReturnedOperatorId;
    }

    public void setClaimsReturnedOperatorId(String claimsReturnedOperatorId) {
        this.claimsReturnedOperatorId = claimsReturnedOperatorId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
