package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 3/4/14
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsItemRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String itemHoldingsId;
    private String holdingsId;
    private String itemId;

    private List<ItemRecord> itemRecords;
    private List<HoldingsRecord> holdingsRecords;
    private Timestamp updatedDate;

    public String getItemHoldingsId() {
        return itemHoldingsId;
    }

    public void setItemHoldingsId(String itemHoldingsId) {
        this.itemHoldingsId = itemHoldingsId;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public List<ItemRecord> getItemRecords() {
        return itemRecords;
    }

    public void setItemRecords(List<ItemRecord> itemRecords) {
        this.itemRecords = itemRecords;
    }

    public List<HoldingsRecord> getHoldingsRecords() {
        return holdingsRecords;
    }

    public void setHoldingsRecords(List<HoldingsRecord> holdingsRecords) {
        this.holdingsRecords = holdingsRecords;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
