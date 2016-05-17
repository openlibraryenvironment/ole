package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/27/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsAccessLocation extends PersistableBusinessObjectBase implements Serializable {

    private String holdingsAccessLocationId;
    private String holdingsId;
    private String accessLocationId;

    private HoldingsRecord holdingsRecord;
    private AccessLocation accessLocation;
    private Timestamp updatedDate;


    public String getHoldingsAccessLocationId() {
        return holdingsAccessLocationId;
    }

    public void setHoldingsAccessLocationId(String holdingsAccessLocationId) {
        this.holdingsAccessLocationId = holdingsAccessLocationId;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getAccessLocationId() {
        return accessLocationId;
    }

    public void setAccessLocationId(String accessLocationId) {
        this.accessLocationId = accessLocationId;
    }

    public HoldingsRecord getHoldingsRecord() {
        return holdingsRecord;
    }

    public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
        this.holdingsRecord = holdingsRecord;
    }

    public AccessLocation getAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(AccessLocation accessLocation) {
        this.accessLocation = accessLocation;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
