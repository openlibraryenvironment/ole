package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/27/13
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsStatisticalSearchRecord extends PersistableBusinessObjectBase implements Serializable {

    private String holdingsStatisticalSearchId;
    private String holdingsId;
    private String statisticalSearchId;

    private HoldingsRecord holdingsRecord;
    private StatisticalSearchRecord statisticalSearchRecord;
    private Timestamp updatedDate;

    public String getHoldingsStatisticalSearchId() {
        return holdingsStatisticalSearchId;
    }

    public void setHoldingsStatisticalSearchId(String holdingsStatisticalSearchId) {
        this.holdingsStatisticalSearchId = holdingsStatisticalSearchId;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getStatisticalSearchId() {
        return statisticalSearchId;
    }

    public void setStatisticalSearchId(String statisticalSearchId) {
        this.statisticalSearchId = statisticalSearchId;
    }

    public HoldingsRecord getHoldingsRecord() {
        return holdingsRecord;
    }

    public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
        this.holdingsRecord = holdingsRecord;
    }

    public StatisticalSearchRecord getStatisticalSearchRecord() {
        return statisticalSearchRecord;
    }

    public void setStatisticalSearchRecord(StatisticalSearchRecord statisticalSearchRecord) {
        this.statisticalSearchRecord = statisticalSearchRecord;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}

