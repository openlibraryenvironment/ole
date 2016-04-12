package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by vivekb on 9/15/14.
 */
public class OLEDeliverNotice extends PersistableBusinessObjectBase {
    private String id;
    private String loanId;
    private String noticeType;
    private Timestamp noticeToBeSendDate;
    private BigDecimal replacementFeeAmount;
    private BigDecimal lostItemProcessingFeeAmount;
    private String noticeSendType;
    private String patronId;
    private OleLoanDocument oleLoanDocument;
    private String requestId;
    private String itemBarcode;
    private String noticeContentConfigName;
    private OleDeliverRequestBo oleDeliverRequestBo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public Timestamp getNoticeToBeSendDate() {
        return noticeToBeSendDate;
    }

    public void setNoticeToBeSendDate(Timestamp noticeToBeSendDate) {
        this.noticeToBeSendDate = noticeToBeSendDate;
    }

    public BigDecimal getReplacementFeeAmount() {
        return replacementFeeAmount;
    }

    public void setReplacementFeeAmount(BigDecimal replacementFeeAmount) {
        this.replacementFeeAmount = replacementFeeAmount;
    }

    public BigDecimal getLostItemProcessingFeeAmount() {
        return lostItemProcessingFeeAmount;
    }

    public void setLostItemProcessingFeeAmount(BigDecimal lostItemProcessingFeeAmount) {
        this.lostItemProcessingFeeAmount = lostItemProcessingFeeAmount;
    }

    public String getNoticeSendType() {
        return noticeSendType;
    }

    public void setNoticeSendType(String noticeSendType) {
        this.noticeSendType = noticeSendType;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public OleLoanDocument getOleLoanDocument() {
        return oleLoanDocument;
    }

    public void setOleLoanDocument(OleLoanDocument oleLoanDocument) {
        this.oleLoanDocument = oleLoanDocument;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public OleDeliverRequestBo getOleDeliverRequestBo() {
        return oleDeliverRequestBo;
    }

    public void setOleDeliverRequestBo(OleDeliverRequestBo oleDeliverRequestBo) {
        this.oleDeliverRequestBo = oleDeliverRequestBo;
    }

    public String getNoticeContentConfigName() {
        return noticeContentConfigName;
    }

    public void setNoticeContentConfigName(String noticeContentConfigName) {
        this.noticeContentConfigName = noticeContentConfigName;
    }
}
