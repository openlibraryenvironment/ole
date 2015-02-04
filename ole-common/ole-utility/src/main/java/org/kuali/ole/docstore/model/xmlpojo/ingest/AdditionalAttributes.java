package org.kuali.ole.docstore.model.xmlpojo.ingest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/8/12
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdditionalAttributes {

    public static final String DATE_ENTERED = "dateEntered";
    public static final String CREATED_BY = "createdBy";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String UPDATED_BY = "updatedBy";
    public static final String STATUS_UPDATED_BY = "statusUpdatedBy";
    public static final String STATUS_UPDATED_ON = "statusUpdatedOn";
    public static final String STATUS = "status";
    public static final String FAST_ADD_FLAG = "fastAddFlag";
    public static final String SUPRESS_FROM_PUBLIC = "supressFromPublic";
    public static final String STAFFONLYFLAG = "staffOnlyFlag";
    public static final String HOLDINGS_CREATED_BY = "holdingsCreatedBy";
    public static final String HOLDINGS_UPDATED_BY = "holdingsUpdatedBy";
    public static final String HOLDINGS_DATE_ENTERED = "holdingsDateEntered";
    public static final String HOLDINGS_LAST_UPDATED = "holdingsLastUpdated";


    private String dateEntered;
    private String lastUpdated;
    private String fastAddFlag;
    private String supressFromPublic;
    private String harvestable;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String statusUpdatedOn;
    private String statusUpdatedBy;
    private String staffOnlyFlag;
    private String holdingsCreatedBy;
    private String holdingsUpdatedBy;
    private String holdingsDateEntered;
    private String holdingsLastUpdated;

    public String getHoldingsCreatedBy() {
        return holdingsCreatedBy;
    }

    public void setHoldingsCreatedBy(String holdingsCreatedBy) {
        this.holdingsCreatedBy = holdingsCreatedBy;
    }

    public String getHoldingsUpdatedBy() {
        return holdingsUpdatedBy;
    }

    public void setHoldingsUpdatedBy(String holdingsUpdatedBy) {
        this.holdingsUpdatedBy = holdingsUpdatedBy;
    }

    public String getHoldingsDateEntered() {
        return holdingsDateEntered;
    }

    public void setHoldingsDateEntered(String holdingsDateEntered) {
        this.holdingsDateEntered = holdingsDateEntered;
    }

    public String getHoldingsLastUpdated() {
        return holdingsLastUpdated;
    }

    public void setHoldingsLastUpdated(String holdingsLastUpdated) {
        this.holdingsLastUpdated = holdingsLastUpdated;
    }

    public String getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(String staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    private Map<String, String> attributeMap = new LinkedHashMap<String, String>();


    public String getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getFastAddFlag() {
        return fastAddFlag;
    }

    public void setFastAddFlag(String fastAddFlag) {
        this.fastAddFlag = fastAddFlag;
    }

    public String getSupressFromPublic() {
        return supressFromPublic;
    }

    public void setSupressFromPublic(String supressFromPublic) {
        this.supressFromPublic = supressFromPublic;
    }

    public String getHarvestable() {
        return harvestable;
    }

    public void setHarvestable(String harvestable) {
        this.harvestable = harvestable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAttribute(String key, String value) {
        attributeMap.put(key, value);
    }

    public String getAttribute(String value) {

        return attributeMap.get(value);
    }

    public Collection<String> getAttributeNames() {
        Collection<String> collection = null;
        if (attributeMap != null && attributeMap.size() > 0) {
            collection = attributeMap.keySet();
        }

        return collection;
    }

    public Map<String, String> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<String, String> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public String getStatusUpdatedOn() {
        return statusUpdatedOn;
    }

    public void setStatusUpdatedOn(String statusUpdatedOn) {
        this.statusUpdatedOn = statusUpdatedOn;
    }

    public String getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    public void setStatusUpdatedBy(String statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }

    public Collection<String> getAdditionalAttributeKeyCollection() {
        Collection<String> keyCollection = new ArrayList<String>();
        keyCollection.add("dateEntered");
        keyCollection.add("lastUpdated");
        keyCollection.add("fastAddFlag");
        keyCollection.add("supressFromPublic");
        keyCollection.add("harvestable");
        keyCollection.add("status");
        keyCollection.add("createdBy");
        keyCollection.add("updatedBy");
        keyCollection.add("statusUpdatedOn");
        keyCollection.add("statusUpdatedBy");
        keyCollection.add("staffOnlyFlag");
        return keyCollection;
    }
}
