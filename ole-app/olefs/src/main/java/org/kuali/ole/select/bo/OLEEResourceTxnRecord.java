package org.kuali.ole.select.bo;

import org.kuali.ole.coa.businessobject.OLECretePOAccountingLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 12/4/14.
 */
public class OLEEResourceTxnRecord {

    private String orderType;
    private String requisitionSource;
    private String listPrice;
    private String itemType;
    private String defaultLocation;
    private String purposeId;
    private String costSource;
    private String methodOfPOTransmission;
    private String vendorNumber;
    private String deliveryBuildingRoomNumber;
    private String deliveryCampusCode;
    private String buildingCode;
    private String chartCode;
    private String orgCode;
    private String fundingSource;
    private String quantity;
    private String itemNoOfParts;
    private String vendorItemIdentifier;
    private boolean receivingRequired;
    private boolean payReqPositiveApprovalReq;
    private String vendorInfoCustomer;
    private List<OLECretePOAccountingLine> cretePOAccountingLines = new ArrayList<>();

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRequisitionSource() {
        return requisitionSource;
    }

    public void setRequisitionSource(String requisitionSource) {
        this.requisitionSource = requisitionSource;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(String defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public String getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }

    public String getCostSource() {
        return costSource;
    }

    public void setCostSource(String costSource) {
        this.costSource = costSource;
    }

    public String getMethodOfPOTransmission() {
        return methodOfPOTransmission;
    }

    public void setMethodOfPOTransmission(String methodOfPOTransmission) {
        this.methodOfPOTransmission = methodOfPOTransmission;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getDeliveryBuildingRoomNumber() {
        return deliveryBuildingRoomNumber;
    }

    public void setDeliveryBuildingRoomNumber(String deliveryBuildingRoomNumber) {
        this.deliveryBuildingRoomNumber = deliveryBuildingRoomNumber;
    }

    public String getDeliveryCampusCode() {
        return deliveryCampusCode;
    }

    public void setDeliveryCampusCode(String deliveryCampusCode) {
        this.deliveryCampusCode = deliveryCampusCode;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemNoOfParts() {
        return itemNoOfParts;
    }

    public void setItemNoOfParts(String itemNoOfParts) {
        this.itemNoOfParts = itemNoOfParts;
    }

    public String getVendorItemIdentifier() {
        return vendorItemIdentifier;
    }

    public void setVendorItemIdentifier(String vendorItemIdentifier) {
        this.vendorItemIdentifier = vendorItemIdentifier;
    }

    public boolean isReceivingRequired() {
        return receivingRequired;
    }

    public void setReceivingRequired(boolean receivingRequired) {
        this.receivingRequired = receivingRequired;
    }

    public boolean isPayReqPositiveApprovalReq() {
        return payReqPositiveApprovalReq;
    }

    public void setPayReqPositiveApprovalReq(boolean payReqPositiveApprovalReq) {
        this.payReqPositiveApprovalReq = payReqPositiveApprovalReq;
    }

    public String getVendorInfoCustomer() {
        return vendorInfoCustomer;
    }

    public void setVendorInfoCustomer(String vendorInfoCustomer) {
        this.vendorInfoCustomer = vendorInfoCustomer;
    }

    public List<OLECretePOAccountingLine> getCretePOAccountingLines() {
        return cretePOAccountingLines;
    }

    public void setCretePOAccountingLines(List<OLECretePOAccountingLine> cretePOAccountingLines) {
        this.cretePOAccountingLines = cretePOAccountingLines;
    }
}
