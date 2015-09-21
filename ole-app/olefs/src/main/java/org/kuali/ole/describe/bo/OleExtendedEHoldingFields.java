package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.select.document.OLEEResourceLicense;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: venkatasrinathy
 * Date: 12/27/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleExtendedEHoldingFields {

    private String relatedHoldingIdentifier;
    private String paymentStatus;
    private String purchaseOrderId;
    private String purchaseOrderNo;
    private String vendorName;
    private String orderType;
    private String currentFYCost;
    private String orderFormat;
    private String fundCode;
    private List<String> accessLocation = new ArrayList<String>();
    private List<OLEEResourceLicense> license = new ArrayList<>();

    public String getRelatedHoldingIdentifier() {
        return relatedHoldingIdentifier;
    }

    public void setRelatedHoldingIdentifier(String relatedHoldingIdentifier) {
        this.relatedHoldingIdentifier = relatedHoldingIdentifier;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCurrentFYCost() {
        return currentFYCost;
    }

    public void setCurrentFYCost(String currentFYCost) {
        this.currentFYCost = currentFYCost;
    }

    public String getOrderFormat() {
        return orderFormat;
    }

    public void setOrderFormat(String orderFormat) {
        this.orderFormat = orderFormat;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public List<String> getAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(List<String> accessLocation) {
        this.accessLocation = accessLocation;
    }

    public List<OLEEResourceLicense> getLicense() {
        return license;
    }

    public void setLicense(List<OLEEResourceLicense> license) {
        this.license = license;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }
}
