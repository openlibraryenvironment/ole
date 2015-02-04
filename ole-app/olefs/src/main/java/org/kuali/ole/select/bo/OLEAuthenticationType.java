package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/20/13
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAuthenticationType extends PersistableBusinessObjectBase {
    private String oleAuthenticationTypeId;
    private String oleAuthenticationTypeName;
    private String oleAuthenticationTypeDesc;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOleAuthenticationTypeDesc() {
        return oleAuthenticationTypeDesc;
    }

    public void setOleAuthenticationTypeDesc(String oleAuthenticationTypeDesc) {
        this.oleAuthenticationTypeDesc = oleAuthenticationTypeDesc;
    }

    public String getOleAuthenticationTypeId() {
        return oleAuthenticationTypeId;
    }

    public void setOleAuthenticationTypeId(String oleAuthenticationTypeId) {
        this.oleAuthenticationTypeId = oleAuthenticationTypeId;
    }

    public String getOleAuthenticationTypeName() {
        return oleAuthenticationTypeName;
    }

    public void setOleAuthenticationTypeName(String oleAuthenticationTypeName) {
        this.oleAuthenticationTypeName = oleAuthenticationTypeName;
    }
}
