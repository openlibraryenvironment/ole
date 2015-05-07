package org.kuali.ole.batch.form;

import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: krishnamohanv
 * Date: 7/12/13
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessJobDetailsForm extends  TransactionalDocumentFormBase {

    private String jobId;
    private List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList = new ArrayList<OLEBatchProcessScheduleBo>();
    private String scheduleId;
    private List<OLEBatchProcessDefinitionDocument>  oleBatchProcessDefinitionDocument;
    private OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo;
    private List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList = new ArrayList<OLEBatchProcessJobDetailsBo>();
    private Date jobFromDate;
    private Date jobToDate;
    private boolean singleJobView;

    public OLEBatchProcessJobDetailsBo getOleBatchProcessJobDetailsBo() {
        return oleBatchProcessJobDetailsBo;
    }

    public void setOleBatchProcessJobDetailsBo(OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo) {
        this.oleBatchProcessJobDetailsBo = oleBatchProcessJobDetailsBo;
    }

    public List<OLEBatchProcessDefinitionDocument> getOleBatchProcessDefinitionDocument() {
        return oleBatchProcessDefinitionDocument;
    }

    public void setOleBatchProcessDefinitionDocument(List<OLEBatchProcessDefinitionDocument> oleBatchProcessDefinitionDocument) {
        this.oleBatchProcessDefinitionDocument = oleBatchProcessDefinitionDocument;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<OLEBatchProcessJobDetailsBo> getOleBatchProcessJobDetailsBoList() {
        return oleBatchProcessJobDetailsBoList;
    }

    public void setOleBatchProcessJobDetailsBoList(List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList) {
        this.oleBatchProcessJobDetailsBoList = oleBatchProcessJobDetailsBoList;
    }

    public OLEBatchProcessJobDetailsForm() {
        super();
        oleBatchProcessJobDetailsBoList.add(new OLEBatchProcessJobDetailsBo());
    }

    private List<OLEBatchProcessDefinitionDocument> oleBatchProcessDefinitionDocumentList;

    public List<OLEBatchProcessDefinitionDocument> getOleBatchProcessDefinitionDocumentList() {
        return oleBatchProcessDefinitionDocumentList;
    }

    public void setOleBatchProcessDefinitionDocumentList(List<OLEBatchProcessDefinitionDocument> oleBatchProcessDefinitionDocumentList) {
        this.oleBatchProcessDefinitionDocumentList = oleBatchProcessDefinitionDocumentList;
    }




    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_BTCH_PRCS_JOB";
    }



    public List<OLEBatchProcessScheduleBo> getOleBatchProcessScheduleBoList() {
        return oleBatchProcessScheduleBoList;
    }

    public void setOleBatchProcessScheduleBoList(List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList) {
        this.oleBatchProcessScheduleBoList = oleBatchProcessScheduleBoList;
    }

    public Date getJobFromDate() {
        return jobFromDate;
    }

    public void setJobFromDate(Date jobFromDate) {
        this.jobFromDate = jobFromDate;
    }

    public Date getJobToDate() {
        return jobToDate;
    }

    public void setJobToDate(Date jobToDate) {
        this.jobToDate = jobToDate;
    }

    public boolean isSingleJobView() {
        return singleJobView;
    }

    public void setSingleJobView(boolean singleJobView) {
        this.singleJobView = singleJobView;
    }
}