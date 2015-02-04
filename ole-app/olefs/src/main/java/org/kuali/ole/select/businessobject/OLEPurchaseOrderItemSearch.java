package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchaseOrderItemSearch extends PersistableBusinessObjectBase {
    private String vendorName;
    private String vendorAliasName;
    private String itemDescription;

    private Integer purchaseOrderItemID;
    private String vendorDetailAssignedIdentifier;
    private String vendorHeaderGeneratedIdentifier;
    private String instanceId;
    private String callNumber;
    private String copyNumber;
    private String publisher;
    private String localId;
    private String purapDocumentIdentifier;
    private Integer accountsPayableLinkIdentifier;
    private String poItemPrice;
    private String docFormat;
    private Integer tempItemIdentifier;
    private Integer itemLineNumber;

    public Integer getTempItemIdentifier() {
        return tempItemIdentifier;
    }

    public void setTempItemIdentifier(Integer tempItemIdentifier) {
        this.tempItemIdentifier = tempItemIdentifier;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public String getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    public void setPurapDocumentIdentifier(String purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public Integer getPurchaseOrderItemID() {
        return purchaseOrderItemID;
    }

    public void setPurchaseOrderItemID(Integer purchaseOrderItemID) {
        this.purchaseOrderItemID = purchaseOrderItemID;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getVendorDetailAssignedIdentifier() {
        return vendorDetailAssignedIdentifier;
    }

    public void setVendorDetailAssignedIdentifier(String vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
    }

    public String getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(String vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public Integer getAccountsPayableLinkIdentifier() {
        return accountsPayableLinkIdentifier;
    }

    public void setAccountsPayableLinkIdentifier(Integer accountsPayableLinkIdentifier) {
        this.accountsPayableLinkIdentifier = accountsPayableLinkIdentifier;
    }

    public String getPoItemPrice() {
        return poItemPrice;
    }

    public void setPoItemPrice(String poItemPrice) {
        this.poItemPrice = poItemPrice;
    }

    public Integer getItemLineNumber() {
        return itemLineNumber;
    }

    public void setItemLineNumber(Integer itemLineNumber) {
        this.itemLineNumber = itemLineNumber;
    }
}
