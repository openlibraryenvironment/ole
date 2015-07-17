package org.kuali.ole.batch.form;

import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/12/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessDefinitionForm extends UifFormBase {

    private OLEBatchProcessDefinitionDocument document;

    private String batchProcessId;
    private String batchProcessType;
    private boolean permissionFlag =true;
    private boolean marcOnly;
    private String navigationBatchProcessId;

    public OLEBatchProcessDefinitionDocument getDocument() {
        return document;
    }

    public void setDocument(OLEBatchProcessDefinitionDocument document) {
        this.document = document;
    }

    public String getBatchProcessId() {
        return batchProcessId;
    }

    public void setBatchProcessId(String batchProcessId) {
        this.batchProcessId = batchProcessId;
    }

    public String getBatchProcessType() {
        return batchProcessType;
    }

    public void setBatchProcessType(String batchProcessType) {
        this.batchProcessType = batchProcessType;
    }

    public boolean isPermissionFlag() {
        return permissionFlag;
    }

    public void setPermissionFlag(boolean permissionFlag) {
        this.permissionFlag = permissionFlag;
    }

    public boolean isMarcOnly() {
        OLEBatchProcessDefinitionDocument batchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument)getDocument();
        if(batchProcessDefinitionDocument.getMarcOnly() != null){
            return batchProcessDefinitionDocument.getMarcOnly();
        }
        return false;
    }

    public void setMarcOnly(boolean marcOnly) {
        this.marcOnly = marcOnly;
    }

    //private String cronOrSchedule;
    //private String scheduleType;
    //private String oneTimeOrRecurring;

    /*public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }*/

   /* public String getOneTimeOrRecurring() {
        return oneTimeOrRecurring;
    }

    public void setOneTimeOrRecurring(String oneTimeOrRecurring) {
        this.oneTimeOrRecurring = oneTimeOrRecurring;
    }*/

 /*   public String getCronOrSchedule() {
        return cronOrSchedule;
    }

    public void setCronOrSchedule(String cronOrSchedule) {
        this.cronOrSchedule = cronOrSchedule;
    }*/

    public String getNavigationBatchProcessId() {
        return navigationBatchProcessId;
    }

    public void setNavigationBatchProcessId(String navigationBatchProcessId) {
        this.navigationBatchProcessId = navigationBatchProcessId;
    }
}
