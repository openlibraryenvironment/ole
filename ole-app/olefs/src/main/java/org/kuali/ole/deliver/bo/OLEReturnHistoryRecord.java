package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 12/17/14
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEReturnHistoryRecord extends PersistableBusinessObjectBase {
    private String id;
    private String itemBarcode;
    private String itemUUID;
    private String operator;
    private String homeCirculationDesk;
    private String routeCirculationDesk;
    private Timestamp returnedDateTime;
    private String returnedItemStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public String getHomeCirculationDesk() {
        return homeCirculationDesk;
    }

    public void setHomeCirculationDesk(String homeCirculationDesk) {
        this.homeCirculationDesk = homeCirculationDesk;
    }

    public String getRouteCirculationDesk() {
        return routeCirculationDesk;
    }

    public void setRouteCirculationDesk(String routeCirculationDesk) {
        this.routeCirculationDesk = routeCirculationDesk;
    }

    public Timestamp getReturnedDateTime() {
        return returnedDateTime;
    }

    public void setReturnedDateTime(Timestamp returnedDateTime) {
        this.returnedDateTime = returnedDateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getReturnedItemStatus() {
        return returnedItemStatus;
    }

    public void setReturnedItemStatus(String returnedItemStatus) {
        this.returnedItemStatus = returnedItemStatus;
    }
}
