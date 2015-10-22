package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by angelind on 10/6/15.
 */
public class OLEAdHocRoutingForAccessActivation extends PersistableBusinessObjectBase {

    private String adHocPrincipalId;

    private String adHocPrincipalName;

    private String adHocRoleId;

    private String adHocRoleName;

    private String adHocGroupId;

    private String adHocGroupName;

    private String adHocActionRequested;

    private String adHocRecipientSelector;

    public String getAdHocPrincipalId() {
        return adHocPrincipalId;
    }

    public void setAdHocPrincipalId(String adHocPrincipalId) {
        this.adHocPrincipalId = adHocPrincipalId;
    }

    public String getAdHocPrincipalName() {
        return adHocPrincipalName;
    }

    public void setAdHocPrincipalName(String adHocPrincipalName) {
        this.adHocPrincipalName = adHocPrincipalName;
    }

    public String getAdHocRoleId() {
        return adHocRoleId;
    }

    public void setAdHocRoleId(String adHocRoleId) {
        this.adHocRoleId = adHocRoleId;
    }

    public String getAdHocRoleName() {
        return adHocRoleName;
    }

    public void setAdHocRoleName(String adHocRoleName) {
        this.adHocRoleName = adHocRoleName;
    }

    public String getAdHocGroupId() {
        return adHocGroupId;
    }

    public void setAdHocGroupId(String adHocGroupId) {
        this.adHocGroupId = adHocGroupId;
    }

    public String getAdHocGroupName() {
        return adHocGroupName;
    }

    public void setAdHocGroupName(String adHocGroupName) {
        this.adHocGroupName = adHocGroupName;
    }

    public String getAdHocActionRequested() {
        return adHocActionRequested;
    }

    public void setAdHocActionRequested(String adHocActionRequested) {
        this.adHocActionRequested = adHocActionRequested;
    }

    public String getAdHocRecipientSelector() {
        return adHocRecipientSelector;
    }

    public void setAdHocRecipientSelector(String adHocRecipientSelector) {
        this.adHocRecipientSelector = adHocRecipientSelector;
    }
}
