package org.kuali.ole.select.form;

import org.kuali.ole.select.bo.OLEClaimingByVendor;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/18/14
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimingForm extends UifFormBase {
    private List<OLEClaimingByVendor> oleClaimingByVendors;
    private String successMsg;
    private boolean cancelBox;

    public boolean isCancelBox() {
        return cancelBox;
    }

    public void setCancelBox(boolean cancelBox) {
        this.cancelBox = cancelBox;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public List<OLEClaimingByVendor> getOleClaimingByVendors() {
        return oleClaimingByVendors;
    }

    public void setOleClaimingByVendors(List<OLEClaimingByVendor> oleClaimingByVendors) {
        this.oleClaimingByVendors = oleClaimingByVendors;
    }


}
