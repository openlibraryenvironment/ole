package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 4/5/13
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleRecentlyReturned extends PersistableBusinessObjectBase {
    private String id;
    private String circulationDeskId;
    private String itemUuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCirculationDeskId() {
        return circulationDeskId;
    }

    public void setCirculationDeskId(String circulationDeskId) {
        this.circulationDeskId = circulationDeskId;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }
}
