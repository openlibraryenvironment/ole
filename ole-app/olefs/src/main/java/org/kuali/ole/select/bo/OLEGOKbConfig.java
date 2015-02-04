package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by srirams on 23/12/14.
 */
public class OLEGOKbConfig extends PersistableBusinessObjectBase {

    private String gokbconfigId;

    private String gokbconfig;

    private Timestamp updatedDate;

    public String getGokbconfigId() {
        return gokbconfigId;
    }

    public void setGokbconfigId(String gokbconfigId) {
        this.gokbconfigId = gokbconfigId;
    }

    public String getGokbconfig() {
        return gokbconfig;
    }

    public void setGokbconfig(String gokbconfig) {
        this.gokbconfig = gokbconfig;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
