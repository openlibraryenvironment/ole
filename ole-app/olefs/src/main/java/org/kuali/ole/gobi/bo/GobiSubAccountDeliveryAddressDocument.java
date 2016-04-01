package org.kuali.ole.gobi.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public class GobiSubAccountDeliveryAddressDocument extends PersistableBusinessObjectBase {
    private Integer id;
    private String subAccount;
    private String buildingCode;
    private String roomNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubAccount() {
        return subAccount;
    }

    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
