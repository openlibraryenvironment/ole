package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/1/13
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCode extends PersistableBusinessObjectBase {

    private String codeId;
    private String inputValue;
    private String profileId;
    private String itemType;
    private String itemStatusCode;
    private boolean active;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemStatusCode() {
        return itemStatusCode;
    }

    public void setItemStatusCode(String itemStatusCode) {
        this.itemStatusCode = itemStatusCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
