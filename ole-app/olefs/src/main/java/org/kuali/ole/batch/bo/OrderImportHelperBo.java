package org.kuali.ole.batch.bo;

import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.EDIOrder;
import org.kuali.ole.pojo.edi.LineItemOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 8/1/14.
 */
public class OrderImportHelperBo {

    private int orderImportSuccessCount;

    private int orderImportFailureCount;

    private List<String> failureReason;

    private BibMarcRecord bibMarcRecord;

    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    private String ediXMLContent;

    private String agendaName;

    private int updateBibCount;

    private int creatHoldingCount;

    private OleTxRecord oleTxRecord;

    private LineItemOrder lineItemOrder;

    private EDIOrder ediOrder;

    private List reqList;

    public int getOrderImportSuccessCount() {
        return orderImportSuccessCount;
    }

    public void setOrderImportSuccessCount(int orderImportSuccessCount) {
        this.orderImportSuccessCount = orderImportSuccessCount;
    }

    public int getOrderImportFailureCount() {
        return orderImportFailureCount;
    }

    public void setOrderImportFailureCount(int orderImportFailureCount) {
        this.orderImportFailureCount = orderImportFailureCount;
    }

    public List<String> getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(List<String> failureReason) {
        this.failureReason = failureReason;
    }

    public BibMarcRecord getBibMarcRecord() {
        return bibMarcRecord;
    }

    public void setBibMarcRecord(BibMarcRecord bibMarcRecord) {
        this.bibMarcRecord = bibMarcRecord;
    }

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getEdiXMLContent() {
        return ediXMLContent;
    }

    public void setEdiXMLContent(String ediXMLContent) {
        this.ediXMLContent = ediXMLContent;
    }

    public String getAgendaName() {
        return agendaName;
    }

    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }

    public int getUpdateBibCount() {
        return updateBibCount;
    }

    public void setUpdateBibCount(int updateBibCount) {
        this.updateBibCount = updateBibCount;
    }

    public int getCreatHoldingCount() {
        return creatHoldingCount;
    }

    public void setCreatHoldingCount(int creatHoldingCount) {
        this.creatHoldingCount = creatHoldingCount;
    }

    public OleTxRecord getOleTxRecord() {
        return oleTxRecord;
    }

    public void setOleTxRecord(OleTxRecord oleTxRecord) {
        this.oleTxRecord = oleTxRecord;
    }

    public LineItemOrder getLineItemOrder() {
        return lineItemOrder;
    }

    public void setLineItemOrder(LineItemOrder lineItemOrder) {
        this.lineItemOrder = lineItemOrder;
    }

    public EDIOrder getEdiOrder() {
        return ediOrder;
    }

    public void setEdiOrder(EDIOrder ediOrder) {
        this.ediOrder = ediOrder;
    }

    public List getReqList() {
        return reqList;
    }

    public void setReqList(List reqList) {
        this.reqList = reqList;
    }
}
