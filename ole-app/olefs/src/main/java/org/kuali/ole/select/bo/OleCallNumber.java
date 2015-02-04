package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/21/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCallNumber extends PersistableBusinessObjectBase {

    private String inputValue;
    private String profileId;
    private String callNumberPreferenceOne;
    private String callNumberPreferenceTwo;
    private String callNumberPreferenceThree;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getCallNumberPreferenceOne() {
        return callNumberPreferenceOne;
    }

    public void setCallNumberPreferenceOne(String callNumberPreferenceOne) {
        this.callNumberPreferenceOne = callNumberPreferenceOne;
    }

    public String getCallNumberPreferenceTwo() {
        return callNumberPreferenceTwo;
    }

    public void setCallNumberPreferenceTwo(String callNumberPreferenceTwo) {
        this.callNumberPreferenceTwo = callNumberPreferenceTwo;
    }

    public String getCallNumberPreferenceThree() {
        return callNumberPreferenceThree;
    }

    public void setCallNumberPreferenceThree(String callNumberPreferenceThree) {
        this.callNumberPreferenceThree = callNumberPreferenceThree;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("inputValue", inputValue);
        toStringMap.put("profileId", profileId);
        toStringMap.put("callNumberPreferenceOne", callNumberPreferenceOne);
        toStringMap.put("callNumberPreferenceTwo", callNumberPreferenceTwo);
        toStringMap.put("callNumberPreferenceThree", callNumberPreferenceThree);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
