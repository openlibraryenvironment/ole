package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainRecord {

    private String recordPacking;
    private String recordSchema;
    private OleSRUExplainRecordData recordData;

    public String getRecordPacking() {
        return recordPacking;
    }

    public void setRecordPacking(String recordPacking) {
        this.recordPacking = recordPacking;
    }

    public String getRecordSchema() {
        return recordSchema;
    }

    public void setRecordSchema(String recordSchema) {
        this.recordSchema = recordSchema;
    }

    public OleSRUExplainRecordData getRecordData() {
        return recordData;
    }

    public void setRecordData(OleSRUExplainRecordData recordData) {
        this.recordData = recordData;
    }

    @Override
    public String toString() {
        return "Explain Record{" +
                "recordPacking='" + recordPacking + '\'' +
                ", recordSchema='" + recordSchema + '\'' +
                ", recordData=" + recordData +
                '}';
    }
}
