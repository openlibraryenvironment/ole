package org.kuali.ole.batch.rule;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.helper.OLESchedulerHelper;
import org.kuali.ole.deliver.bo.OleBatchJobBo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.springframework.scheduling.quartz.CronTriggerBean;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/1/13
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleBatchJobRule extends MaintenanceDocumentRuleBase {

    private static final Logger LOG = Logger.getLogger(OleBatchJobRule.class);
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleBatchJobBo oleBatchJobBo = (OleBatchJobBo) document.getNewMaintainableObject().getDataObject();
        if (oleBatchJobBo.getRunNowOrSchedule().equalsIgnoreCase("RunNow")) {
            OLEBatchProcessScheduleBo oleBatchProcessScheduleBo = oleBatchJobBo.getOleBatchProcessScheduleBo();
            oleBatchProcessScheduleBo.setCreateTime(new Timestamp(new Date().getTime()));
            oleBatchProcessScheduleBo.setOneTimeOrRecurring("onetime");
            oleBatchProcessScheduleBo.setOneTimeStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            oleBatchProcessScheduleBo.setOneTimeStartTime(new SimpleDateFormat("HH:mm").format(DateUtils.addMinutes(new Date(), 1)));
            oleBatchJobBo.setJobCronExpression(OLESchedulerHelper.getInstance().getCronExpression(oleBatchProcessScheduleBo));
        }
        if (StringUtils.isNotBlank(oleBatchJobBo.getCronOrSchedule()) && oleBatchJobBo.getCronOrSchedule().equalsIgnoreCase("Scheduled")) {
            OLEBatchProcessScheduleBo oleBatchProcessScheduleBo = oleBatchJobBo.getOleBatchProcessScheduleBo();
            oleBatchProcessScheduleBo.setScheduleType(oleBatchJobBo.getScheduleType());
            oleBatchProcessScheduleBo.setOneTimeOrRecurring(oleBatchJobBo.getOneTimeOrRecurring());
            oleBatchJobBo.setJobCronExpression(OLESchedulerHelper.getInstance().getCronExpression(oleBatchJobBo.getOleBatchProcessScheduleBo()));
        }
        isValid &= validateCronExpression(oleBatchJobBo);
        return isValid;
    }

    /**
     *  This method  validates duplicate completeness Id and return boolean value.
     * @param oleBatchJobBo
     * @return  boolean
     */
    private boolean validateCronExpression(OleBatchJobBo oleBatchJobBo) {

        boolean isJobModified=true;
        StdScheduler scheduler = (StdScheduler) GlobalResourceLoader.getService("scheduler");

        if(!oleBatchJobBo.isJobEnableStatus()){
            JobDetail jobDetail = (JobDetail) GlobalResourceLoader.getService(oleBatchJobBo.getJobTriggerName());
            try {
                scheduler.deleteJob(jobDetail.getName(),jobDetail.getGroup()) ;
            } catch (SchedulerException e) {
                isJobModified=false;
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        else {
            try {

                JobDetail jobDetail = (JobDetail) GlobalResourceLoader.getService(oleBatchJobBo.getJobTriggerName());
                CronTriggerBean cronTriggerBean = new CronTriggerBean();
                boolean valid=   org.quartz.CronExpression.isValidExpression(oleBatchJobBo.getJobCronExpression());
                if(valid){
                    cronTriggerBean.setName(oleBatchJobBo.getJobTriggerName()+"Trigger");
                    cronTriggerBean.setCronExpression(oleBatchJobBo.getJobCronExpression());
                    cronTriggerBean.setJobName(jobDetail.getName());
                    cronTriggerBean.setJobGroup(jobDetail.getGroup());
                    cronTriggerBean.setJobDetail(jobDetail);
                    String [] jobNames=scheduler.getJobNames(jobDetail.getGroup());
                    for (String jobName:jobNames){
                        if(jobName.equals(jobDetail.getName())) {
                            scheduler.deleteJob(jobDetail.getName(),jobDetail.getGroup());
                        }
                    }

                    try{
                        scheduler.scheduleJob(jobDetail, cronTriggerBean);
                    }
                    catch (Exception e){
                        isJobModified=false;
                        LOG.error("Batch Job Exception :: "+e);
                    }


                }else {
                    isJobModified=false;
                    this.putFieldError(OLEConstants.INVALID_CRON, "error.invalid.cron");
                }

            }
            catch (Exception e) {
                isJobModified=false;
                LOG.error("Batch Job Exception :: "+e);
            }



        }

        return isJobModified;
    }
}
