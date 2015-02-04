package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/27/12
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleBibliographicRecordStatus extends PersistableBusinessObjectBase {

    private String bibliographicRecordStatusId;
    private String bibliographicRecordStatusCode;
    private String bibliographicRecordStatusName;
    private String source;
    private Date sourceDate;
    private boolean active;

    public String getBibliographicRecordStatusId() {
        return bibliographicRecordStatusId;
    }

    public void setBibliographicRecordStatusId(String bibliographicRecordStatusId) {
        this.bibliographicRecordStatusId = bibliographicRecordStatusId;
    }

    public String getBibliographicRecordStatusCode() {
        return bibliographicRecordStatusCode;
    }

    public void setBibliographicRecordStatusCode(String bibliographicRecordStatusCode) {
        this.bibliographicRecordStatusCode = bibliographicRecordStatusCode;
    }

    public String getBibliographicRecordStatusName() {
        return bibliographicRecordStatusName;
    }

    public void setBibliographicRecordStatusName(String bibliographicRecordStatusName) {
        this.bibliographicRecordStatusName = bibliographicRecordStatusName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(Date sourceDate) {
        this.sourceDate = sourceDate;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("bibliographicRecordStatusId", bibliographicRecordStatusId);
        toStringMap.put("bibliographicRecordStatusCode", bibliographicRecordStatusCode);
        toStringMap.put("bibliographicRecordStatusName", bibliographicRecordStatusName);
        toStringMap.put("source", source);
        toStringMap.put("sourceDate", sourceDate);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
