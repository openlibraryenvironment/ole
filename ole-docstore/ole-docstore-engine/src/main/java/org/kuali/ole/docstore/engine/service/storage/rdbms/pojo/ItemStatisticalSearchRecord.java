package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 1/28/14
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemStatisticalSearchRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String itemStatisticalSearchId;
    private String itemId;
    private String statisticalSearchId;

    @JsonIgnore
    private ItemRecord itemRecord;
    private StatisticalSearchRecord statisticalSearchRecord;

    public String getItemStatisticalSearchId() {
        return itemStatisticalSearchId;
    }

    public void setItemStatisticalSearchId(String itemStatisticalSearchId) {
        this.itemStatisticalSearchId = itemStatisticalSearchId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStatisticalSearchId() {
        return statisticalSearchId;
    }

    public void setStatisticalSearchId(String statisticalSearchId) {
        this.statisticalSearchId = statisticalSearchId;
    }

    public ItemRecord getItemRecord() {
        return itemRecord;
    }

    public void setItemRecord(ItemRecord itemRecord) {
        this.itemRecord = itemRecord;
    }

    public StatisticalSearchRecord getStatisticalSearchRecord() {
        return statisticalSearchRecord;
    }

    public void setStatisticalSearchRecord(StatisticalSearchRecord statisticalSearchRecord) {
        this.statisticalSearchRecord = statisticalSearchRecord;
    }
}
