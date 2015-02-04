package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 8/13/12
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLicenseRequestItemTitle extends PersistableBusinessObjectBase {
    private String oleLicenseRequestItemTitleId;
    private String oleLicenseRequestId;
    private String itemUUID;
    private OleLicenseRequestBo oleLicenseRequestBo;

    public String getOleLicenseRequestId() {
        return oleLicenseRequestId;
    }

    public void setOleLicenseRequestId(String oleLicenseRequestId) {
        this.oleLicenseRequestId = oleLicenseRequestId;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public OleLicenseRequestBo getOleLicenseRequestBo() {
        return oleLicenseRequestBo;
    }

    public void setOleLicenseRequestBo(OleLicenseRequestBo oleLicenseRequestBo) {
        this.oleLicenseRequestBo = oleLicenseRequestBo;
    }

    public String getOleLicenseRequestItemTitleId() {
        return oleLicenseRequestItemTitleId;
    }

    public void setOleLicenseRequestItemTitleId(String oleLicenseRequestItemTitleId) {
        this.oleLicenseRequestItemTitleId = oleLicenseRequestItemTitleId;
    }
}
