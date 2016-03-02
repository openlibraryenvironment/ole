package org.kuali.ole.deliver.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.deliver.calendar.bo.OleCalendarGroup;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleCirculationDesk is business object class for Circulation Desk Maintenance Document
 */
public class OleCirculationDesk extends PersistableBusinessObjectBase {

    private String circulationDeskId;
    private String circulationDeskCode;
    private String circulationDeskPublicName;
    private String circulationDeskStaffName;
    private boolean pickUpLocation;
    private boolean asrPickupLocation;
    private boolean active;
    private String onHoldDays;
    private String requestExpirationDays;
    private String shelvingLagTime;
    private List<OleCirculationDeskLocation> oleCirculationDeskLocations = new ArrayList<OleCirculationDeskLocation>();
    private List<OleCirculationDeskLocation> oleCirculationDeskLocationList = new ArrayList<OleCirculationDeskLocation>();
    private List<OleCirculationDeskLocation> olePickupCirculationDeskLocations = new ArrayList<OleCirculationDeskLocation>();
    private List<OleCirculationDeskFeeType> oleCirculationDeskFeeTypeList = new ArrayList<OleCirculationDeskFeeType>();
    private List<OleCirculationDeskLocation> deleteoleCirculationDeskLocations = new ArrayList<OleCirculationDeskLocation>();
    private List<OleCirculationDeskLocation> deleteOlePickupCirculationDeskLocations = new ArrayList<OleCirculationDeskLocation>();
    private String locationId;
    private boolean printSlip;
    private String errorMessage;
    private OleCalendarGroup oleCalendarGroup;
    private String calendarGroupId;
    private String holdFormat;
    private boolean holdQueue;
    private boolean staffed;
    private String replyToEmail;
    private boolean renewLostItem;
    private String showItemOnHold;
    private String defaultRequestTypeId;
    private String defaultRequestTypeCode;
    private OleDeliverRequestType defaultRequestType;
    private String defaultPickupLocationId;
    private String defaultPickupLocationCode;
    private OleCirculationDesk defaultPickupLocation;
    private String fromEmailAddress;


    public List<OleCirculationDeskFeeType> getOleCirculationDeskFeeTypeList() {
        return oleCirculationDeskFeeTypeList;
    }

    public void setOleCirculationDeskFeeTypeList(List<OleCirculationDeskFeeType> oleCirculationDeskFeeTypeList) {
        this.oleCirculationDeskFeeTypeList = oleCirculationDeskFeeTypeList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isPrintSlip() {
        return printSlip;
    }

    public void setPrintSlip(boolean printSlip) {
        this.printSlip = printSlip;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * Gets the circulationDeskId attribute.
     *
     * @return Returns the circulationDeskId
     */
    public String getCirculationDeskId() {
        return circulationDeskId;
    }

    /**
     * Sets the circulationDeskId attribute value.
     *
     * @param circulationDeskId The accessMethodId to set.
     */
    public void setCirculationDeskId(String circulationDeskId) {
        this.circulationDeskId = circulationDeskId;
    }

    /**
     * Gets the circulationDeskCode attribute.
     *
     * @return Returns the circulationDeskCode
     */
    public String getCirculationDeskCode() {
        return circulationDeskCode;
    }

    /**
     * Sets the circulationDeskCode attribute value.
     *
     * @param circulationDeskCode The circulationDeskCode to set.
     */
    public void setCirculationDeskCode(String circulationDeskCode) {
        this.circulationDeskCode = circulationDeskCode;
    }

    /**
     * Gets the circulationDeskPublicName attribute.
     *
     * @return Returns the circulationDeskPublicName
     */
    public String getCirculationDeskPublicName() {
        return circulationDeskPublicName;
    }

    /**
     * Sets the circulationDeskPublicName attribute value.
     *
     * @param circulationDeskPublicName The circulationDeskPublicName to set.
     */
    public void setCirculationDeskPublicName(String circulationDeskPublicName) {
        this.circulationDeskPublicName = circulationDeskPublicName;
    }

    /**
     * Gets the circulationDeskStaffName attribute.
     *
     * @return Returns the circulationDeskStaffName
     */
    public String getCirculationDeskStaffName() {
        return circulationDeskStaffName;
    }

    /**
     * Sets the circulationDeskStaffName attribute value.
     *
     * @param circulationDeskStaffName The circulationDeskStaffName to set.
     */
    public void setCirculationDeskStaffName(String circulationDeskStaffName) {
        this.circulationDeskStaffName = circulationDeskStaffName;
    }

    /**
     * Gets the active attribute.
     *
     * @return Returns the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active attribute value.
     *
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the locationId attribute.
     *
     * @return Returns the locationId
     */

    public boolean isPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(boolean pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getOnHoldDays() {
        if(onHoldDays!=null && onHoldDays.equalsIgnoreCase("")){
            return null;
        }else{
            return onHoldDays;
        }
    }

    public void setOnHoldDays(String onHoldDays) {
        this.onHoldDays = onHoldDays;
    }

    public List<OleCirculationDeskLocation> getOleCirculationDeskLocations() {
        return oleCirculationDeskLocations;
    }

    public void setOleCirculationDeskLocations(List<OleCirculationDeskLocation> oleCirculationDeskLocations) {
        this.oleCirculationDeskLocations = oleCirculationDeskLocations;
    }

    public String getShelvingLagTime() {
        return shelvingLagTime;
    }

    public int getShelvingLagTimeInt() {
        return Integer.valueOf(shelvingLagTime);
    }

    public void setShelvingLagTime(String shelvingLagTime) {
        this.shelvingLagTime = shelvingLagTime;
    }

    public OleCalendarGroup getOleCalendarGroup() {
        return oleCalendarGroup;
    }

    public void setOleCalendarGroup(OleCalendarGroup oleCalendarGroup) {
        this.oleCalendarGroup = oleCalendarGroup;
    }

    public String getCalendarGroupId() {
        return calendarGroupId;
    }

    public void setCalendarGroupId(String calendarGroupId) {
        this.calendarGroupId = calendarGroupId;
    }

    public List<OleCirculationDeskLocation> getDeleteoleCirculationDeskLocations() {
        return deleteoleCirculationDeskLocations;
    }

    public void setDeleteoleCirculationDeskLocations(List<OleCirculationDeskLocation> deleteoleCirculationDeskLocations) {
        this.deleteoleCirculationDeskLocations = deleteoleCirculationDeskLocations;
    }

    public boolean isAsrPickupLocation() {
        return asrPickupLocation;
    }

    public void setAsrPickupLocation(boolean asrPickupLocation) {
        this.asrPickupLocation = asrPickupLocation;
    }

    public String getHoldFormat() {
        return holdFormat;
    }

    public void setHoldFormat(String holdFormat) {
        this.holdFormat = holdFormat;
    }

    public boolean isHoldQueue() {
        return holdQueue;
    }

    public void setHoldQueue(boolean holdQueue) {
        this.holdQueue = holdQueue;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public void setReplyToEmail(String replyToEmail) {
        this.replyToEmail = replyToEmail;
    }

    public List<OleCirculationDeskLocation> getOlePickupCirculationDeskLocations() {
        return olePickupCirculationDeskLocations;
    }

    public void setOlePickupCirculationDeskLocations(List<OleCirculationDeskLocation> olePickupCirculationDeskLocations) {
        this.olePickupCirculationDeskLocations = olePickupCirculationDeskLocations;
    }

    public List<OleCirculationDeskLocation> getDeleteOlePickupCirculationDeskLocations() {
        return deleteOlePickupCirculationDeskLocations;
    }

    public void setDeleteOlePickupCirculationDeskLocations(List<OleCirculationDeskLocation> deleteOlePickupCirculationDeskLocations) {
        this.deleteOlePickupCirculationDeskLocations = deleteOlePickupCirculationDeskLocations;
    }

    public List<OleCirculationDeskLocation> getOleCirculationDeskLocationList() {
        return oleCirculationDeskLocationList;
    }

    public void setOleCirculationDeskLocationList(List<OleCirculationDeskLocation> oleCirculationDeskLocationList) {
        this.oleCirculationDeskLocationList = oleCirculationDeskLocationList;
    }

    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        List<Collection<PersistableBusinessObject>> collectionList = new ArrayList<>();
        collectionList.add((Collection)getOleCirculationDeskLocations());
        collectionList.add((Collection)getDeleteOlePickupCirculationDeskLocations());
        collectionList.add((Collection)getOleCirculationDeskFeeTypeList());
        return collectionList;
    }

    public String getRequestExpirationDays() {
        return StringUtils.isBlank(requestExpirationDays)?"0":requestExpirationDays;
    }

    public void setRequestExpirationDays(String requestExpirationDays) {
        this.requestExpirationDays = requestExpirationDays;
    }	

    public boolean isStaffed() {
        return staffed;
    }

    public void setStaffed(boolean staffed) {
        this.staffed = staffed;
    }

    public boolean isRenewLostItem() {
        return renewLostItem;
    }

    public void setRenewLostItem(boolean renewLostItem) {
        this.renewLostItem = renewLostItem;
    }

    public String getShowItemOnHold() {
        return showItemOnHold;
    }

    public void setShowItemOnHold(String showItemOnHold) {
        this.showItemOnHold = showItemOnHold;
    }

    public String getDefaultRequestTypeId() {
        return defaultRequestTypeId;
    }

    public void setDefaultRequestTypeId(String defaultRequestTypeId) {
        this.defaultRequestTypeId = defaultRequestTypeId;
    }

    public String getDefaultRequestTypeCode() {
        if(defaultRequestTypeCode==null && defaultRequestType!=null && defaultRequestType.getRequestTypeCode()!=null){
            defaultRequestTypeCode = defaultRequestType.getRequestTypeCode();
        }
        return defaultRequestTypeCode;
    }

    public void setDefaultRequestTypeCode(String defaultRequestTypeCode) {
        this.defaultRequestTypeCode = defaultRequestTypeCode;
    }

    public OleDeliverRequestType getDefaultRequestType() {
        return defaultRequestType;
    }

    public void setDefaultRequestType(OleDeliverRequestType defaultRequestType) {
        this.defaultRequestType = defaultRequestType;
    }

    public String getDefaultPickupLocationId() {
        return defaultPickupLocationId;
    }

    public void setDefaultPickupLocationId(String defaultPickupLocationId) {
        this.defaultPickupLocationId = defaultPickupLocationId;
    }

    public String getDefaultPickupLocationCode() {
        if (defaultPickupLocation != null && defaultPickupLocation.getCirculationDeskCode() != null) {
            defaultPickupLocationCode = defaultPickupLocation.getCirculationDeskCode();
        }
        return defaultPickupLocationCode;
    }

    public void setDefaultPickupLocationCode(String defaultPickupLocationCode) {
        this.defaultPickupLocationCode = defaultPickupLocationCode;
    }

    public OleCirculationDesk getDefaultPickupLocation() {
        return defaultPickupLocation;
    }

    public void setDefaultPickupLocation(OleCirculationDesk defaultPickupLocation) {
        this.defaultPickupLocation = defaultPickupLocation;
    }

    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }
}