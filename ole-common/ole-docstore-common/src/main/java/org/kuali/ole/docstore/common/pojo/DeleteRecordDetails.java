package org.kuali.ole.docstore.common.pojo;

import org.marc4j.marc.Record;

/**
 * Created by rajeshbabuk on 4/15/16.
 */
public class DeleteRecordDetails {

    private Record record;
    private String matchPoint;
    private String matchPointValue;
    private String bibId;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public String getMatchPoint() {
        return matchPoint;
    }

    public void setMatchPoint(String matchPoint) {
        this.matchPoint = matchPoint;
    }

    public String getMatchPointValue() {
        return matchPointValue;
    }

    public void setMatchPointValue(String matchPointValue) {
        this.matchPointValue = matchPointValue;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }
}
