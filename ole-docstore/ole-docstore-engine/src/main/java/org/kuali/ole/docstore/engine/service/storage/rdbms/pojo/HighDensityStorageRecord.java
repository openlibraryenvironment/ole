package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/28/13
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class HighDensityStorageRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String highDensityStorageId;
    private String row;
    private String module;
    private String shelf;
    private String tray;
    private Timestamp updatedDate;

    public String getHighDensityStorageId() {
        return highDensityStorageId;
    }

    public void setHighDensityStorageId(String highDensityStorageId) {
        this.highDensityStorageId = highDensityStorageId;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getTray() {
        return tray;
    }

    public void setTray(String tray) {
        this.tray = tray;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
