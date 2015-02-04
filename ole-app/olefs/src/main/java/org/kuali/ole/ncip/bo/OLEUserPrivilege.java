package org.kuali.ole.ncip.bo;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/21/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEUserPrivilege {
    private String userPrivilegeType;
    private String userPrivilegeStatus;
    private String userPrivilegeDescription;

    public String getUserPrivilegeType() {
        return userPrivilegeType;
    }

    public void setUserPrivilegeType(String userPrivilegeType) {
        this.userPrivilegeType = userPrivilegeType;
    }

    public String getUserPrivilegeStatus() {
        return userPrivilegeStatus;
    }

    public void setUserPrivilegeStatus(String userPrivilegeStatus) {
        this.userPrivilegeStatus = userPrivilegeStatus;
    }

    public String getUserPrivilegeDescription() {
        return userPrivilegeDescription;
    }

    public void setUserPrivilegeDescription(String userPrivilegeDescription) {
        this.userPrivilegeDescription = userPrivilegeDescription;
    }
}
