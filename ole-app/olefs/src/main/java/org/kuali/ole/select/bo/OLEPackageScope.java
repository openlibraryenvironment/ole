package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/22/13
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */

public class OLEPackageScope extends PersistableBusinessObjectBase {

    private String olePackageScopeId;
    private String olePackageScopeName;
    private String olePackageScopeDesc;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOlePackageScopeDesc() {
        return olePackageScopeDesc;
    }

    public void setOlePackageScopeDesc(String olePackageScopeDesc) {
        this.olePackageScopeDesc = olePackageScopeDesc;
    }

    public String getOlePackageScopeId() {
        return olePackageScopeId;
    }

    public void setOlePackageScopeId(String olePackageScopeId) {
        this.olePackageScopeId = olePackageScopeId;
    }

    public String getOlePackageScopeName() {
        return olePackageScopeName;
    }

    public void setOlePackageScopeName(String olePackageScopeName) {
        this.olePackageScopeName = olePackageScopeName;
    }
}
