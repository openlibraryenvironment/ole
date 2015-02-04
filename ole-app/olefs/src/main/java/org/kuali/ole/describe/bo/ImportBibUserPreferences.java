package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 30/11/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBibUserPreferences
        extends PersistableBusinessObjectBase {
    private Integer prefId;
    private String prefId1;
    private String userId;
    private String userRole;
    private List<String> prefNameList = new ArrayList<String>();
    private String prefName;
    private String importType;
    private String importStatus;
    private String permLocation;
    private String tempLocation;
    private String adminProtectedTags;
    private String adminRemovalTags;
    private String removalTags;
    private String protectedTags;
    private List<String> shelvingSchemeList = new ArrayList<String>();
    private String shelvingScheme;
    private String callNumberSource1;
    private String callNumberSource2;
    private String callNumberSource3;
    private String message;

    public List<String> getPrefNameList() {
        return prefNameList;
    }

    public void setPrefNameList(List<String> prefNameList) {
        this.prefNameList = prefNameList;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public String getRemovalTags() {
        return removalTags;
    }

    public void setRemovalTags(String removalTags) {
        this.removalTags = removalTags;
    }

    public String getProtectedTags() {
        return protectedTags;
    }

    public void setProtectedTags(String protectedTags) {
        this.protectedTags = protectedTags;
    }


    public String getPermLocation() {
        return permLocation;
    }

    public void setPermLocation(String permLocation) {
        this.permLocation = permLocation;
    }

    public String getTempLocation() {
        return tempLocation;
    }

    public void setTempLocation(String tempLocation) {
        this.tempLocation = tempLocation;
    }

    public String getAdminProtectedTags() {
        return adminProtectedTags;
    }

    public void setAdminProtectedTags(String adminProtectedTags) {
        this.adminProtectedTags = adminProtectedTags;
    }

    public String getAdminRemovalTags() {
        return adminRemovalTags;
    }

    public void setAdminRemovalTags(String adminRemovalTags) {
        this.adminRemovalTags = adminRemovalTags;
    }

    public List<String> getShelvingSchemeList() {
        return shelvingSchemeList;
    }

    public void setShelvingSchemeList(List<String> shelvingSchemeList) {
        this.shelvingSchemeList = shelvingSchemeList;
    }

    public String getShelvingScheme() {
        return shelvingScheme;
    }

    public void setShelvingScheme(String shelvingScheme) {
        this.shelvingScheme = shelvingScheme;
    }

    public void setCallNumberSource1(String callNumberSource1) {
        this.callNumberSource1 = callNumberSource1;
    }

    public String getCallNumberSource1() {
        return callNumberSource1;
    }

    public String getCallNumberSource2() {
        return callNumberSource2;
    }

    public void setCallNumberSource2(String callNumberSource2) {
        this.callNumberSource2 = callNumberSource2;
    }

    public String getCallNumberSource3() {
        return callNumberSource3;
    }

    public void setCallNumberSource3(String callNumberSource3) {
        this.callNumberSource3 = callNumberSource3;
    }

    public String getPrefName() {
        return prefName;
    }

    public void setPrefName(String prefName) {
        this.prefName = prefName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPrefId() {
        return prefId;
    }

    public void setPrefId(Integer prefId) {
        this.prefId = prefId;
    }

    public String getUserId() {
        userId = GlobalVariables.getUserSession().getPrincipalId();
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        userRole = "admin";
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getPrefId1() {
        return prefId1;
    }

    public void setPrefId1(String prefId1) {
        this.prefId1 = prefId1;
    }
}
