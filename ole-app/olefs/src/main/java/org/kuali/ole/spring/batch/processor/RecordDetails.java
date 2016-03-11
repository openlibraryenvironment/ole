package org.kuali.ole.spring.batch.processor;

import org.marc4j.marc.Record;

/**
 * Created by SheikS on 3/9/2016.
 */
public class RecordDetails {
    private Record record;
    private String bibUUID;

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
}
