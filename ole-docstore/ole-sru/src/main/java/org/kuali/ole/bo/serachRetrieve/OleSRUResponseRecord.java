package org.kuali.ole.bo.serachRetrieve;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 5/27/13
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUResponseRecord {

    private String recordSchema;
    private String recordPacking;
    private OleSRUResponseDocument oleSRUResponseDocument;
    private int recordPosition;
    public String getRecordSchema() {
        return recordSchema;
    }

    public void setRecordSchema(String recordSchema) {
        this.recordSchema = recordSchema;
    }

    public String getRecordPacking() {
        return recordPacking;
    }

    public void setRecordPacking(String recordPacking) {
        this.recordPacking = recordPacking;
    }

    public int getRecordPosition() {
        return recordPosition;
    }

    public void setRecordPosition(int recordPosition) {
        this.recordPosition = recordPosition;
    }

    public OleSRUResponseDocument getOleSRUResponseDocument() {
        return oleSRUResponseDocument;
    }

    public void setOleSRUResponseDocument(OleSRUResponseDocument oleSRUResponseDocument) {
        this.oleSRUResponseDocument = oleSRUResponseDocument;
    }

    @Override
    public String toString() {
        return "OleSRUResponseRecord{" +
                "recordSchema='" + recordSchema + '\'' +
                ", recordPacking='" + recordPacking + '\'' +
                ", oleSRUResponseDocument=" + oleSRUResponseDocument +
                ", recordPosition=" + recordPosition +
                '}';
    }
}
