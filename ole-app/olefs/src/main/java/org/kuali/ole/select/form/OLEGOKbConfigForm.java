package org.kuali.ole.select.form;

import org.kuali.ole.select.document.OLEEResourceSynchronizationGokbLog;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srirams on 24/9/14.
 */
public class OLEGOKbConfigForm extends UifFormBase {

    private String gokbconfig;

    private String updatedDate;

    private List<OLEEResourceSynchronizationGokbLog> gokbLogs = new ArrayList<>();

    public String getGokbconfig() {
        return gokbconfig;
    }

    public void setGokbconfig(String gokbconfig) {
        this.gokbconfig = gokbconfig;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<OLEEResourceSynchronizationGokbLog> getGokbLogs() {
        return gokbLogs;
    }

    public void setGokbLogs(List<OLEEResourceSynchronizationGokbLog> gokbLogs) {
        this.gokbLogs = gokbLogs;
    }
}
