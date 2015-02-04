package org.kuali.ole.docstore.model.xmlpojo.ingest;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/8/12
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class LinkInformation {
    private String id;
    private String bibUUID;
    private String instanceUUID;
    private String holdingsUUID;
    private List<String> itemUUIDs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public String getInstanceUUID() {
        return instanceUUID;
    }

    public void setInstanceUUID(String instanceUUID) {
        this.instanceUUID = instanceUUID;
    }

    public String getHoldingsUUID() {
        return holdingsUUID;
    }

    public void setHoldingsUUID(String holdingsUUID) {
        this.holdingsUUID = holdingsUUID;
    }

    public List<String> getItemUUIDs() {
        return itemUUIDs;
    }

    public void setItemUUIDs(List<String> itemUUIDs) {
        this.itemUUIDs = itemUUIDs;
    }
}
