package org.kuali.ole.select.gokb;

import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by premkumarv on 12/8/14.
 */
public class OleGokbReview extends PersistableBusinessObjectBase {

    private Integer gokbReviewId;
    private Timestamp reviewDate;
    private String oleERSIdentifier;
    private String eResourceName;
    private String type;
    private String details;
    private Integer gokbTippId;
    private boolean approve;
    private boolean clear;
    private OLEEResourceRecordDocument oleeResourceRecordDocument;

    public Integer getGokbReviewId() {
        return gokbReviewId;
    }

    public void setGokbReviewId(Integer gokbReviewId) {
        this.gokbReviewId = gokbReviewId;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getGokbTippId() {
        return gokbTippId;
    }

    public void setGokbTippId(Integer gokbTippId) {
        this.gokbTippId = gokbTippId;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public String geteResourceName() {
        return eResourceName;
    }

    public void seteResourceName(String eResourceName) {
        this.eResourceName = eResourceName;
    }

    public OLEEResourceRecordDocument getOleeResourceRecordDocument() {
        return oleeResourceRecordDocument;
    }

    public void setOleeResourceRecordDocument(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        this.oleeResourceRecordDocument = oleeResourceRecordDocument;
    }
}


