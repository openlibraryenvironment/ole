package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 6/27/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCopy extends PersistableBusinessObjectBase {
    private Integer copyId;
    private Integer reqDocNum;
    private Integer reqItemId;
    private String poDocNum;
    private Integer poItemId;
    private String itemUUID;
    private String instanceId;
    private String oleERSIdentifier;
    private String bibId;
    private Integer receivingItemId;
    private Integer correctionItemId;
    private OleRequisitionItem oleRequisitionItem;
    private OlePurchaseOrderItem olePurchaseOrderItem;
    private OleLineItemReceivingItem oleLineItemReceivingItem;
    private OleCorrectionReceivingItem oleCorrectionReceivingItem;
    private String enumeration;
    private String location;
    private String copyNumber;
    private String receiptStatus;
    private String partNumber;
    private String serialReceivingIdentifier;

    private List<OLEPaidCopy> olePaidCopies = new ArrayList<OLEPaidCopy>();


    public String getSerialReceivingIdentifier() {
        return serialReceivingIdentifier;
    }

    public void setSerialReceivingIdentifier(String serialReceivingIdentifier) {
        this.serialReceivingIdentifier = serialReceivingIdentifier;
    }

    public Integer getReqDocNum() {
        return reqDocNum;
    }

    public void setReqDocNum(Integer reqDocNum) {
        this.reqDocNum = reqDocNum;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public Integer getReceivingItemId() {
        return receivingItemId;
    }

    public void setReceivingItemId(Integer receivingItemId) {
        this.receivingItemId = receivingItemId;
    }

    public Integer getCorrectionItemId() {
        return correctionItemId;
    }

    public void setCorrectionItemId(Integer correctionItemId) {
        this.correctionItemId = correctionItemId;
    }

    public OleLineItemReceivingItem getOleLineItemReceivingItem() {
        return oleLineItemReceivingItem;
    }

    public void setOleLineItemReceivingItem(OleLineItemReceivingItem oleLineItemReceivingItem) {
        this.oleLineItemReceivingItem = oleLineItemReceivingItem;
    }

    public OleCorrectionReceivingItem getOleCorrectionReceivingItem() {
        return oleCorrectionReceivingItem;
    }

    public void setOleCorrectionReceivingItem(OleCorrectionReceivingItem oleCorrectionReceivingItem) {
        this.oleCorrectionReceivingItem = oleCorrectionReceivingItem;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public OleRequisitionItem getOleRequisitionItem() {
        return oleRequisitionItem;
    }

    public void setOleRequisitionItem(OleRequisitionItem oleRequisitionItem) {
        this.oleRequisitionItem = oleRequisitionItem;
    }

    public OlePurchaseOrderItem getOlePurchaseOrderItem() {
        return olePurchaseOrderItem;
    }

    public void setOlePurchaseOrderItem(OlePurchaseOrderItem olePurchaseOrderItem) {
        this.olePurchaseOrderItem = olePurchaseOrderItem;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public Integer getCopyId() {
        return copyId;
    }

    public void setCopyId(Integer copyId) {
        this.copyId = copyId;
    }

    public Integer getReqItemId() {
        return reqItemId;
    }

    public void setReqItemId(Integer reqItemId) {
        this.reqItemId = reqItemId;
    }

    public String getPoDocNum() {
        return poDocNum;
    }

    public void setPoDocNum(String poDocNum) {
        this.poDocNum = poDocNum;
    }

    public Integer getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(Integer poItemId) {
        this.poItemId = poItemId;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public List<OLEPaidCopy> getOlePaidCopies() {
        return olePaidCopies;
    }

    public void setOlePaidCopies(List<OLEPaidCopy> olePaidCopies) {
        this.olePaidCopies = olePaidCopies;
    }
}
