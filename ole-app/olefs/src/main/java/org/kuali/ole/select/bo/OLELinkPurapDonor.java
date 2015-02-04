package org.kuali.ole.select.bo;

import org.kuali.ole.select.businessobject.*;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 1/10/14
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLELinkPurapDonor extends PersistableBusinessObjectBase {
    private Integer linkPurapDonorId;
    private Integer reqItemId;
    private String poDocNum;
    private Integer poItemId;
    private Integer receivingItemId;
    private Integer correctionItemId;
    private String donorId;
    private String donorCode;
    private OleRequisitionItem oleRequisitionItem;
    private OlePurchaseOrderItem olePurchaseOrderItem;
    private OleLineItemReceivingItem oleLineItemReceivingItem;
    private OleCorrectionReceivingItem oleCorrectionReceivingItem;
    private OleInvoiceItem oleInvoiceItem;
    private OlePaymentRequestItem olePaymentRequestItem;

    public Integer getLinkPurapDonorId() {
        return linkPurapDonorId;
    }

    public void setLinkPurapDonorId(Integer linkPurapDonorId) {
        this.linkPurapDonorId = linkPurapDonorId;
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

    public OleInvoiceItem getOleInvoiceItem() {
        return oleInvoiceItem;
    }

    public void setOleInvoiceItem(OleInvoiceItem oleInvoiceItem) {
        this.oleInvoiceItem = oleInvoiceItem;
    }

    public OlePaymentRequestItem getOlePaymentRequestItem() {
        return olePaymentRequestItem;
    }

    public void setOlePaymentRequestItem(OlePaymentRequestItem olePaymentRequestItem) {
        this.olePaymentRequestItem = olePaymentRequestItem;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("linkPurapDonorId", linkPurapDonorId);
        toStringMap.put("reqItemId", reqItemId);
        toStringMap.put("poItemId", poItemId);
        toStringMap.put("receivingItemId", receivingItemId);
        toStringMap.put("correctionItemId", correctionItemId);
        toStringMap.put("donorCode", donorCode);
        return toStringMap;
    }
}
