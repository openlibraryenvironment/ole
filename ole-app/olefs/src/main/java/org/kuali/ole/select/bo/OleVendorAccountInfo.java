package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/5/12
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleVendorAccountInfo extends PersistableBusinessObjectBase {

    private String vendorAccountInfoId;
    private String vendorRefNumber;
    private String accountNumber;
    private String objectCode;
    private boolean active;

    public String getVendorAccountInfoId() {
        return vendorAccountInfoId;
    }

    public void setVendorAccountInfoId(String vendorAccountInfoId) {
        this.vendorAccountInfoId = vendorAccountInfoId;
    }

    public String getVendorRefNumber() {
        return vendorRefNumber;
    }

    public void setVendorRefNumber(String vendorRefNumber) {
        this.vendorRefNumber = vendorRefNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("vendorAccountInfoId", vendorAccountInfoId);
        toStringMap.put("vendorRefNumber", vendorRefNumber);
        toStringMap.put("accountNumber", accountNumber);
        toStringMap.put("objectCode", objectCode);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
