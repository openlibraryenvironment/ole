package org.kuali.ole.select.bo;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 11/26/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEDonor extends PersistableBusinessObjectBase {
    private String donorId;
    private String donorCode;
    private String donorName;
    private String donorNote;
    private String donorPublicDisplay;
    private KualiDecimal donorAmount;
    private KualiDecimal encumberedAmount;
    private KualiDecimal expensedAmount;
    private String bookPlateUrl;
    private String donorPublicUrl;
    private String poNumber;
    private boolean active;
    private String poLinkUrl;

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getDonorNote() {
        return donorNote;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorPublicDisplay() {
        return donorPublicDisplay;
    }

    public void setDonorPublicDisplay(String donorPublicDisplay) {
        this.donorPublicDisplay = donorPublicDisplay;
    }

    public void setDonorNote(String donorNote) {
        this.donorNote = donorNote;
    }

    public KualiDecimal getDonorAmount() {
        return donorAmount;
    }

    public void setDonorAmount(KualiDecimal donorAmount) {
        this.donorAmount = donorAmount;
    }

    public KualiDecimal getEncumberedAmount() {
        return encumberedAmount;
    }

    public void setEncumberedAmount(KualiDecimal encumberedAmount) {
        this.encumberedAmount = encumberedAmount;
    }

    public KualiDecimal getExpensedAmount() {
        return expensedAmount;
    }

    public void setExpensedAmount(KualiDecimal expensedAmount) {
        this.expensedAmount = expensedAmount;
    }

    public String getBookPlateUrl() {
        return bookPlateUrl;
    }

    public void setBookPlateUrl(String bookPlateUrl) {
        this.bookPlateUrl = bookPlateUrl;
    }

    public String getDonorPublicUrl() {
        return donorPublicUrl;
    }

    public void setDonorPublicUrl(String donorPublicUrl) {
        this.donorPublicUrl = donorPublicUrl;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPoLinkUrl() {
        return poLinkUrl;
    }

    public void setPoLinkUrl(String poLinkUrl) {
        this.poLinkUrl = poLinkUrl;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("donorCode", donorCode);
        toStringMap.put("donorName", donorName);
        toStringMap.put("donorNote", donorNote);
        toStringMap.put("donorPublicDisplay", donorPublicDisplay);
        toStringMap.put("donorAmount", donorAmount);
        toStringMap.put("encumberedAmount", encumberedAmount);
        toStringMap.put("expensedAmount", expensedAmount);
        toStringMap.put("poNumber", poNumber);
        toStringMap.put("active", active);
        toStringMap.put("poLinkUrl", poLinkUrl);
        return toStringMap;
    }
}
