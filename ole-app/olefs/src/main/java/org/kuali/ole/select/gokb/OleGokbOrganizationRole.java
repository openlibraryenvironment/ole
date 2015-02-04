package org.kuali.ole.select.gokb;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by premkumarv on 12/8/14.
 */
public class OleGokbOrganizationRole extends PersistableBusinessObjectBase {

    private Integer gokbOrgRoleId;
    private Integer gokbOrganizationId;
    private String role;

    public Integer getGokbOrgRoleId() {
        return gokbOrgRoleId;
    }

    public void setGokbOrgRoleId(Integer gokbOrgRoleId) {
        this.gokbOrgRoleId = gokbOrgRoleId;
    }

    public Integer getGokbOrganizationId() {
        return gokbOrganizationId;
    }

    public void setGokbOrganizationId(Integer gokbOrganizationId) {
        this.gokbOrganizationId = gokbOrganizationId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
