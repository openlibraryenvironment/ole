package org.kuali.ole.select.gokb;

import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by srirams on 27/12/14.
 */
public class OleGokbChangeLog extends PersistableBusinessObjectBase{

    private Integer gokbChangeLogId;
    private Timestamp changeLogDate;
    private String oleERSIdentifier;
    private String type;
    private String details;
    private String origin;
    private Integer gokbTippId;
    private Timestamp archivedDate;
    private OLEEResourceRecordDocument oleeResourceRecordDocument;

    public Integer getGokbChangeLogId() {
        return gokbChangeLogId;
    }

    public void setGokbChangeLogId(Integer gokbChangeLogId) {
        this.gokbChangeLogId = gokbChangeLogId;
    }

    public Timestamp getChangeLogDate() {
        return changeLogDate;
    }

    public void setChangeLogDate(Timestamp changeLogDate) {
        this.changeLogDate = changeLogDate;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getGokbTippId() {
        return gokbTippId;
    }

    public void setGokbTippId(Integer gokbTippId) {
        this.gokbTippId = gokbTippId;
    }

    public Timestamp getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Timestamp archivedDate) {
        this.archivedDate = archivedDate;
    }

    public OLEEResourceRecordDocument getOleeResourceRecordDocument() {
        return oleeResourceRecordDocument;
    }

    public void setOleeResourceRecordDocument(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        this.oleeResourceRecordDocument = oleeResourceRecordDocument;
    }
}
