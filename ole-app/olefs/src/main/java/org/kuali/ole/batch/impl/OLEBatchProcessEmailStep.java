package org.kuali.ole.batch.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.ole.sys.batch.service.SchedulerService;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailToList;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.StringWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/25/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessEmailStep extends AbstractStep {
    private static final Logger LOG = Logger.getLogger(OLEBatchProcessEmailStep.class);
    private static final String FROMEMAIL = "BATCHJOBADMIN@KUALI.ORG";

    private OleMailer oleMailer;

    private OleMailer getOleMailer() {
        if (oleMailer == null) {
            oleMailer = GlobalResourceLoader.getService("oleMailer");
        }
        return oleMailer;
    }

    private OLEBatchProcessDataHelper oleBatchProcessDataHelper;

    private OLEBatchProcessDataHelper getOLEBatchProcessDataHelper() {

        if (oleBatchProcessDataHelper == null) {
            oleBatchProcessDataHelper = OLEBatchProcessDataHelper.getInstance();
        }
        return oleBatchProcessDataHelper;
    }

    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        buildJobReport(jobName);
        return true;
    }

    private void buildJobReport(String jobName) {
        try {
            String jobId = StringUtils.substringAfter(jobName, SchedulerService.SCHEDULED_GROUP + "." + OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB);
            String scheduleId = StringUtils.substringAfter(jobName, SchedulerService.SCHEDULED_GROUP + "." + OLEConstants.OLEBatchProcess.BATCH_JOB);
            if (StringUtils.isNotEmpty(jobId)) {
                OLEBatchProcessJobDetailsBo jobDetailsBo = readJobRecord(jobId);
                OLEBatchProcessDefinitionDocument processDefDoc = readOLEBatchProcessDefinitionDocument(jobDetailsBo.getBatchProcessId());
                sendJobReportMail(processDefDoc, jobDetailsBo);
            } else if (StringUtils.isNotEmpty(scheduleId)) {
                OLEBatchProcessScheduleBo scheduleBo = readScheduleRecord(scheduleId);
                OLEBatchProcessJobDetailsBo jobDetailsBo = readJobRecordForSch(scheduleBo.getBatchProcessId());
                if(jobDetailsBo==null){
                    LOG.error("Error while Running step OLEBatchProcessEmailStep:: Unable to get job details for process id:: "+scheduleBo.getBatchProcessId());
                }else{
                    OLEBatchProcessDefinitionDocument processDefDoc = readOLEBatchProcessDefinitionDocument(jobDetailsBo.getBatchProcessId());
                    sendJobReportMail(processDefDoc, jobDetailsBo);
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while Running step OLEBatchProcessEmailStep:: ", ex);
        }
    }

    /**
     * reads the job record which is being run
     *
     * @param processId
     * @return
     */
    private OLEBatchProcessJobDetailsBo readJobRecordForSch(String processId) {
        Map<String, String> jobMap = new HashMap<String, String>();
        jobMap.put("bat_prcs_id", processId);
        List<OLEBatchProcessJobDetailsBo> jobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEBatchProcessJobDetailsBo.class, jobMap, "JOB_ID", false);
        if (jobDetailsBoList.isEmpty()) {
            return null;
        } else {
            return jobDetailsBoList.get(0);
        }
    }

    /**
     * reads the job record which is being run
     *
     * @param jobId
     * @return
     */
    private OLEBatchProcessJobDetailsBo readJobRecord(String jobId) {
        Map<String, String> jobMap = new HashMap<String, String>();
        jobMap.put("job_id", jobId);
        OLEBatchProcessJobDetailsBo jobDetailsBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessJobDetailsBo.class, jobMap);
        return jobDetailsBo;
    }

    /**
     * reads the schedule record which is being run
     *
     * @param scheduleId
     * @return
     */
    private OLEBatchProcessScheduleBo readScheduleRecord(String scheduleId) {
        Map<String, String> schMap = new HashMap<String, String>();
        schMap.put("ole_bat_prcs_schdule_id", scheduleId);
        OLEBatchProcessScheduleBo scheduleBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessScheduleBo.class, schMap);
        return scheduleBo;
    }

    /**
     * Read the OLEBatchProcessDefinitionDocument record by using batchProcessId.
     *
     * @param batchProcessId
     * @return
     */
    private OLEBatchProcessDefinitionDocument readOLEBatchProcessDefinitionDocument(String batchProcessId) {
        Map<String, String> jobMap = new HashMap<String, String>();
        jobMap.put("bat_prcs_id", batchProcessId);
        OLEBatchProcessDefinitionDocument processDefDoc = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessDefinitionDocument.class, jobMap);
        return processDefDoc;
    }

    /**
     * Sends mail to the given recipients
     *
     * @param processDefDoc, job
     * @return
     */
    private void sendJobReportMail(OLEBatchProcessDefinitionDocument processDefDoc, OLEBatchProcessJobDetailsBo job) throws Exception {
        if (StringUtils.isNotBlank(processDefDoc.getEmailIds())) {
            String[] emailsIdsArray = processDefDoc.getEmailIds().split(",");
            if (Integer.parseInt(job.getNoOfSuccessRecords()) <= 0) {
                if (processDefDoc.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)){
                    getOleMailer().sendEmail(new EmailFrom(FROMEMAIL), new EmailToList(Arrays.asList(emailsIdsArray)),
                            new EmailSubject(OLEConstants.OLEBatchProcess.BATCH_DELETION_JOB + (StringUtils.isEmpty(processDefDoc.getBatchProcessName()) ? "" : processDefDoc.getBatchProcessName()) + OLEConstants.OLEBatchProcess.FAILED),
                            new EmailBody(getReportByVM(job,processDefDoc)), null, null, true);
                }
                else {
                    getOleMailer().sendEmail(new EmailFrom(FROMEMAIL), new EmailToList(Arrays.asList(emailsIdsArray)),
                            new EmailSubject((StringUtils.isEmpty(processDefDoc.getBatchProcessName()) ? "" : processDefDoc.getBatchProcessName() + "-") + processDefDoc.getBatchProcessType() + OLEConstants.OLEBatchProcess.FAILED),
                            new EmailBody(getReportByVM(job,processDefDoc)), null, null, true);
                }
            } else {
                if (processDefDoc.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)){
                    getOleMailer().sendEmail(new EmailFrom(FROMEMAIL), new EmailToList(Arrays.asList(emailsIdsArray)),
                            new EmailSubject(OLEConstants.OLEBatchProcess.BATCH_DELETION_JOB + (StringUtils.isEmpty(processDefDoc.getBatchProcessName()) ? "" : processDefDoc.getBatchProcessName()) + OLEConstants.OLEBatchProcess.COMPLETE),
                            new EmailBody(getReportByVM(job,processDefDoc)), null, null, true);
                }
                else {
                    getOleMailer().sendEmail(new EmailFrom(FROMEMAIL), new EmailToList(Arrays.asList(emailsIdsArray)),
                            new EmailSubject((StringUtils.isEmpty(processDefDoc.getBatchProcessName()) ? "" : processDefDoc.getBatchProcessName() + "-") + processDefDoc.getBatchProcessType() + OLEConstants.OLEBatchProcess.COMPLETE),
                            new EmailBody(getReportByVM(job,processDefDoc)), null, null, true);
                }
            }
        }
    }
    /**
     *
     * @param job
     * @return
     * @throws Exception
     */
    private String getReportByVM(OLEBatchProcessJobDetailsBo job,OLEBatchProcessDefinitionDocument processDefDoc) throws Exception {
        VelocityEngine ve = GlobalResourceLoader.getService("velocityEngine");
        ve.init();
        VelocityContext context = new VelocityContext();
        context.put("job", job);
        context.put("processDefDoc", processDefDoc);
        context.put("dataHelper", OLEBatchProcessDataHelper.getInstance());
        context.put("Integer", Integer.class);
        context.put("StringUtils", StringUtils.class);
        Template t = ve.getTemplate("jobreport.vm");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }


}
