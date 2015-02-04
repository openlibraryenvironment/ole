package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/28/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String instanceId;
    private String source;
    private String bibId;
    private List<HoldingsRecord> holdingsRecords;
    private List<ItemRecord> itemRecords;
    private List<BibRecord> bibRecords;
    private String uniqueIdPrefix;


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public List<HoldingsRecord> getHoldingsRecords() {
        return holdingsRecords;
    }

    public void setHoldingsRecords(List<HoldingsRecord> holdingsRecords) {
        this.holdingsRecords = holdingsRecords;
    }

    public List<ItemRecord> getItemRecords() {
        return itemRecords;
    }

    public void setItemRecords(List<ItemRecord> itemRecords) {
        this.itemRecords = itemRecords;
    }

    public List<BibRecord> getBibRecords() {
        return bibRecords;
    }

    public void setBibRecords(List<BibRecord> bibRecords) {
        this.bibRecords = bibRecords;
    }

    public String getUniqueIdPrefix() {
        return uniqueIdPrefix;
    }

    public void setUniqueIdPrefix(String uniqueIdPrefix) {
        this.uniqueIdPrefix = uniqueIdPrefix;
    }
}
