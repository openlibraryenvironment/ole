package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.LocationsCheckinCountRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 6/5/15.
 */
public class OleItemRecordForCirc {
    private String itemLocation;
    private String itemLibraryLocation;
    private String institutionLocation;
    private String campusLocation;
    private String collectionLocation;
    private String itemStatusToBeUpdatedTo;
    private OleCirculationDesk checkinLocation;
    private String routeToLocation;
    private OleCirculationDesk pickupLocation;
    private OleCirculationDesk operatorCircLocation;
    private OleDeliverRequestBo oleDeliverRequestBo;
    private LocationsCheckinCountRecord locationsCheckinCountRecordToBeUpdated;
    private ItemRecord itemRecord;

    private boolean isCheckinLocationSameAsHomeLocation;
    private boolean isLocationMappedToCircDesk;
    private boolean isPickupLocationSameAsOperatorCircLocation;

    public String getItemType() {
        if (null != itemRecord) {
            ItemTypeRecord itemTempTypeRecord = itemRecord.getItemTempTypeRecord();
            String itemType = getItemTypeFromItemTypeRecord(itemTempTypeRecord);
            if (StringUtils.isBlank(itemType)) {
                return getItemTypeFromItemTypeRecord(itemRecord.getItemTypeRecord());
            }
            return itemType;
        }
        return null;
    }

    private String getItemTypeFromItemTypeRecord(ItemTypeRecord itemTypeRecord) {
        if (null != itemTypeRecord && StringUtils.isNotBlank(itemTypeRecord.getCode())) {
            return itemTypeRecord.getCode();
        }
        return null;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemLibraryLocation() {
        return itemLibraryLocation;
    }

    public void setItemLibraryLocation(String itemLibraryLocation) {
        this.itemLibraryLocation = itemLibraryLocation;
    }

    public ItemStatusRecord getItemStatusRecord() {
        if (null != itemRecord) {
            return itemRecord.getItemStatusRecord();
        }
        return null;
    }


    public String getItemUUID() {
        if (null != itemRecord) {
            return itemRecord.getItemId();
        }
        return null;
    }

    public void setInstitutionLocation(String institutionLocation) {
        this.institutionLocation = institutionLocation;
    }

    public void setCampusLocation(String campusLocation) {
        this.campusLocation = campusLocation;
    }

    public void setCollectionLocation(String collectionLocation) {
        this.collectionLocation = collectionLocation;
    }

    public String getInstitutionLocation() {
        return institutionLocation;
    }

    public String getCampusLocation() {
        return campusLocation;
    }

    public String getCollectionLocation() {
        return collectionLocation;
    }

    public String getItemStatusToBeUpdatedTo() {
        return itemStatusToBeUpdatedTo;
    }

    public void setItemStatusToBeUpdatedTo(String itemStatusToBeUpdatedTo) {
        this.itemStatusToBeUpdatedTo = itemStatusToBeUpdatedTo;
    }

    public OleCirculationDesk getCheckinLocation() {
        return checkinLocation;
    }

    public void setCheckinLocation(OleCirculationDesk checkinLocation) {
        this.checkinLocation = checkinLocation;
    }

    public OleCirculationDesk getPickupLocation() {
        if (null != getOleDeliverRequestBo()) {
            pickupLocation = getOleDeliverRequestBo().getOlePickUpLocation();
        }
        return pickupLocation;
    }

    public void setPickupLocation(OleCirculationDesk pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setOperatorCircLocation(OleCirculationDesk operatorCircLocation) {
        this.operatorCircLocation = operatorCircLocation;
    }

    public Boolean isItemPickupLocationSameAsOperatorCircLoc() {
       return isPickupLocationSameAsOperatorCircLocation;
    }

    public void setIsPickupLocationSameAsOperatorCircLocation(boolean isPickupLocationSameAsOperatorCircLocation) {
        this.isPickupLocationSameAsOperatorCircLocation = isPickupLocationSameAsOperatorCircLocation;
    }

    public Boolean isCheckinLocationSameAsHomeLocation() {
      return isCheckinLocationSameAsHomeLocation;

    }

    public void setIsCheckinLocationSameAsHomeLocation(boolean isCheckinLocationSameAsHomeLocation) {
        this.isCheckinLocationSameAsHomeLocation = isCheckinLocationSameAsHomeLocation;
    }

    public boolean isLocationMappedToCirculationDesk() {
        return isLocationMappedToCircDesk;
    }

    public void setIsLocationMappedToCircDesk(boolean isLocationMappedToCircDesk) {
        this.isLocationMappedToCircDesk = isLocationMappedToCircDesk;
    }

    public String getItemFullPathLocation() {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(institutionLocation)) {
            stringBuilder.append(institutionLocation);
        }
        if (StringUtils.isNotEmpty(campusLocation)) {
            appendLocationToStrinBuilder(stringBuilder, campusLocation);
        }
        if (StringUtils.isNotEmpty(itemLibraryLocation)) {
            appendLocationToStrinBuilder(stringBuilder, itemLibraryLocation);
        }
        if (StringUtils.isNotEmpty(collectionLocation)) {
            appendLocationToStrinBuilder(stringBuilder, collectionLocation);
        }
        if (StringUtils.isNotEmpty(itemLocation)) {
            appendLocationToStrinBuilder(stringBuilder, itemLocation);
        }

        return stringBuilder.toString();
    }

    private void appendLocationToStrinBuilder(StringBuilder stringBuilder, String location) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("/").append(location);
        } else {
            stringBuilder.append(location);
        }
    }

    public OleCirculationDesk getOperatorCircLocation() {
        return operatorCircLocation;
    }

    public OleDeliverRequestBo getOleDeliverRequestBo() {
        return oleDeliverRequestBo;
    }

    public void setOleDeliverRequestBo(OleDeliverRequestBo oleDeliverRequestBo) {
        this.oleDeliverRequestBo = oleDeliverRequestBo;
    }

    public List<LocationsCheckinCountRecord> getLocationsCheckinCountRecords() {
        if (null != itemRecord) {
            return itemRecord.getLocationsCheckinCountRecords();
        }
        return new ArrayList<>();
    }

    public void updateCheckinCount() {
        List<LocationsCheckinCountRecord> locationsCheckinCountRecords = getLocationsCheckinCountRecords();
        boolean isLocationPresent = false;
        if (!locationsCheckinCountRecords.isEmpty()) {
            for (Iterator<LocationsCheckinCountRecord> iterator = locationsCheckinCountRecords.iterator(); iterator.hasNext(); ) {
                LocationsCheckinCountRecord checkinCountRecord = iterator.next();
                if(checkinCountRecord.getLocationName() != null){
                    if (checkinCountRecord.getLocationName().equals(getItemFullPathLocation())) {
                        Integer locationCount = checkinCountRecord.getLocationCount();
                        checkinCountRecord.setLocationCount(null == locationCount ? 1 : locationCount + 1);
                        locationsCheckinCountRecordToBeUpdated = checkinCountRecord;
                        isLocationPresent = true;
                    }
                }
            }
        }
        if(!isLocationPresent) {
            locationsCheckinCountRecordToBeUpdated = new LocationsCheckinCountRecord();
            locationsCheckinCountRecordToBeUpdated.setLocationCount(1);
            locationsCheckinCountRecordToBeUpdated.setLocationName(getItemFullPathLocation());
            locationsCheckinCountRecordToBeUpdated.setLocationInhouseCount(0);
        }
    }

    public void updateInHouseCheckinCount() {
        List<LocationsCheckinCountRecord> locationsCheckinCountRecords = getLocationsCheckinCountRecords();
        boolean isLocationPresent = false;
        if (!locationsCheckinCountRecords.isEmpty()) {
            for (Iterator<LocationsCheckinCountRecord> iterator = locationsCheckinCountRecords.iterator(); iterator.hasNext(); ) {
                LocationsCheckinCountRecord checkinCountRecord = iterator.next();
                if (checkinCountRecord.getLocationName().equals(getItemFullPathLocation())) {
                    Integer locationInhouseCount = checkinCountRecord.getLocationInhouseCount();
                    checkinCountRecord.setLocationInhouseCount(null == locationInhouseCount ? 1 : locationInhouseCount + 1);
                    locationsCheckinCountRecordToBeUpdated = checkinCountRecord;
                    isLocationPresent = true;
                }
            }
        }
        if(!isLocationPresent) {
            locationsCheckinCountRecordToBeUpdated = new LocationsCheckinCountRecord();
            locationsCheckinCountRecordToBeUpdated.setLocationInhouseCount(1);
            locationsCheckinCountRecordToBeUpdated.setLocationName(getItemFullPathLocation());
            locationsCheckinCountRecordToBeUpdated.setLocationCount(0);
        }
    }

    public LocationsCheckinCountRecord getLocationsCheckinCountRecordToBeUpdated() {
        return locationsCheckinCountRecordToBeUpdated;
    }

    public void setLocationsCheckinCountRecordToBeUpdated(LocationsCheckinCountRecord locationsCheckinCountRecordToBeUpdated) {
        this.locationsCheckinCountRecordToBeUpdated = locationsCheckinCountRecordToBeUpdated;
    }

    public ItemRecord getItemRecord() {
        return itemRecord;
    }

    public void setItemRecord(ItemRecord itemRecord) {
        this.itemRecord = itemRecord;
    }

    public String getRouteToLocation() {
        return routeToLocation;
    }

    public void setRouteToLocation(String routeToLocation) {
        this.routeToLocation = routeToLocation;
    }
}
