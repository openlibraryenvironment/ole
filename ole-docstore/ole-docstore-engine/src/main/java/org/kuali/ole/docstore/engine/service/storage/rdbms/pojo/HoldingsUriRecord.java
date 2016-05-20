package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/8/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsUriRecord extends PersistableBusinessObjectBase
        implements Serializable {
    private String accessUriId;
    private String text;
    private String uri;
    private String holdingsId;
    private Timestamp updatedDate;

    private HoldingsRecord holdingsRecord;

    public String getAccessUriId() {
        return accessUriId;
    }

    public void setAccessUriId(String accessUriId) {
        this.accessUriId = accessUriId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public HoldingsRecord getHoldingsRecord() {
        return holdingsRecord;
    }

    public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
        this.holdingsRecord = holdingsRecord;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
