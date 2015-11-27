package org.kuali.ole.audit;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;


/**
 * Created by pvsubrah on 11/5/15.
 */
public class Audit extends PersistableBusinessObjectBase {
    private Integer auditId;
    private String foreignKeyRef;
    private String actor;
    private Timestamp updateDate;
    private String columnUpdated;
    private byte[] columnValue;

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public String getForeignKeyRef() {
        return foreignKeyRef;
    }

    public void setForeignKeyRef(String foreignKeyRef) {
        this.foreignKeyRef = foreignKeyRef;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getColumnUpdated() {
        return columnUpdated;
    }

    public void setColumnUpdated(String columnUpdated) {
        this.columnUpdated = columnUpdated;
    }

    public byte[] getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(byte[] columnValue) {
        this.columnValue = columnValue;
    }
}
