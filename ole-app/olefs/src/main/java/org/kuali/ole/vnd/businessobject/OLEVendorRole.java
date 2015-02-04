package org.kuali.ole.vnd.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by angelind on 12/22/14.
 */
public class OLEVendorRole extends PersistableBusinessObjectBase {

    private String vendorRoleId;

    private String roleName;

    public String getVendorRoleId() {
        return vendorRoleId;
    }

    public void setVendorRoleId(String vendorRoleId) {
        this.vendorRoleId = vendorRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
