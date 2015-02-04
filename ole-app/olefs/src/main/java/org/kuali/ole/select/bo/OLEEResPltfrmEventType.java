package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEEResPltfrmEventType extends PersistableBusinessObjectBase {

    private String eResPltfrmEventTypeId;

    private String eResPltfrmEventTypeName;

    private String eResPltfrmEventTypeDesc;

    private boolean active;

    public String geteResPltfrmEventTypeId() {
        return eResPltfrmEventTypeId;
    }

    public void seteResPltfrmEventTypeId(String eResPltfrmEventTypeId) {
        this.eResPltfrmEventTypeId = eResPltfrmEventTypeId;
    }

    public String geteResPltfrmEventTypeName() {
        return eResPltfrmEventTypeName;
    }

    public void seteResPltfrmEventTypeName(String eResPltfrmEventTypeName) {
        this.eResPltfrmEventTypeName = eResPltfrmEventTypeName;
    }

    public String geteResPltfrmEventTypeDesc() {
        return eResPltfrmEventTypeDesc;
    }

    public void seteResPltfrmEventTypeDesc(String eResPltfrmEventTypeDesc) {
        this.eResPltfrmEventTypeDesc = eResPltfrmEventTypeDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
