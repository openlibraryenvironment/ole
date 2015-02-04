package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by hemalathas on 12/17/14.
 */
public class OLEAccessActivationWorkFlow extends PersistableBusinessObjectBase {

    private String accessId;
    private Integer orderNo;
    private String status;
    private String roleName;
    private String roleId;
    private String accessActivationName;
    private String accessActivationConfigurationId;
    private OLEAccessActivationConfiguration oleAccessActivationConfiguration;

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAccessActivationName() {
        return accessActivationName;
    }

    public void setAccessActivationName(String accessActivationName) {
        this.accessActivationName = accessActivationName;
    }


    public OLEAccessActivationConfiguration getOleAccessActivationConfiguration() {
        return oleAccessActivationConfiguration;
    }

    public void setOleAccessActivationConfiguration(OLEAccessActivationConfiguration oleAccessActivationConfiguration) {
        this.oleAccessActivationConfiguration = oleAccessActivationConfiguration;
    }

    public String getAccessActivationConfigurationId() {
        return accessActivationConfigurationId;
    }

    public void setAccessActivationConfigurationId(String accessActivationConfigurationId) {
        this.accessActivationConfigurationId = accessActivationConfigurationId;
    }

}


