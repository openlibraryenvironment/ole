package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/21/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPackageType extends PersistableBusinessObjectBase {

    private String olePackageTypeId;
    private String olePackageTypeName;
    private String olePackageTypeDesc;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOlePackageTypeDesc() {
        return olePackageTypeDesc;
    }

    public void setOlePackageTypeDesc(String olePackageTypeDesc) {
        this.olePackageTypeDesc = olePackageTypeDesc;
    }

    public String getOlePackageTypeId() {
        return olePackageTypeId;
    }

    public void setOlePackageTypeId(String olePackageTypeId) {
        this.olePackageTypeId = olePackageTypeId;
    }

    public String getOlePackageTypeName() {
        return olePackageTypeName;
    }

    public void setOlePackageTypeName(String olePackageTypeName) {
        this.olePackageTypeName = olePackageTypeName;
    }
}
