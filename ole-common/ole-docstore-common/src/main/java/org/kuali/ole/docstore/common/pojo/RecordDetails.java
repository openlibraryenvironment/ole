package org.kuali.ole.docstore.common.pojo;

import org.marc4j.marc.Record;

/**
 * Created by SheikS on 3/9/2016.
 */
public class RecordDetails {
    private Record record;
    private String bibUUID;
    private Integer index;
    private String message;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
