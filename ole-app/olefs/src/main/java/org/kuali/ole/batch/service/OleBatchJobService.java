package org.kuali.ole.batch.service;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OleBatchJobBo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.quartz.JobDetail;
import org.quartz.impl.StdScheduler;
import org.springframework.scheduling.quartz.CronTriggerBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/5/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleBatchJobService {

    private static final Logger LOG = Logger.getLogger(OleBatchJobService.class);
    public OleBatchJobService(){
        addAvailableJobsToScheduler();
    }

    /*
     This method is used to add the active job  to the scheduler.
     */
    public void addAvailableJobsToScheduler(){

        Map<String,String> batchMap = new HashMap<String, String>();
        batchMap.put("jobEnableStatus","true");
        List<OleBatchJobBo> oleDeliverBatchJobBoList =(List<OleBatchJobBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleBatchJobBo.class,batchMap);
        StdScheduler scheduler = (StdScheduler) GlobalResourceLoader.getService("scheduler");

      if(oleDeliverBatchJobBoList.size()>0){
          for(OleBatchJobBo oleDeliverBatchJobBo:oleDeliverBatchJobBoList){

              try {
                  JobDetail jobDetail = (JobDetail) GlobalResourceLoader.getService(oleDeliverBatchJobBo.getJobTriggerName());
                  CronTriggerBean cronTriggerBean = new CronTriggerBean();
                  cronTriggerBean.setName(oleDeliverBatchJobBo.getJobTriggerName()+"Trigger");
                  cronTriggerBean.setCronExpression(oleDeliverBatchJobBo.getJobCronExpression());
                  cronTriggerBean.setJobName(jobDetail.getName());
                  cronTriggerBean.setJobGroup(jobDetail.getGroup());
                  cronTriggerBean.setJobDetail(jobDetail);
                  try{
                      scheduler.scheduleJob(jobDetail, cronTriggerBean);
                      LOG.info(jobDetail.getName()+" job is scheduled");
                  }
                  catch (Exception e){
                      scheduler.rescheduleJob(oleDeliverBatchJobBo.getJobTriggerName(),jobDetail.getName(),cronTriggerBean);
                  }   } catch (Exception e) {
                  LOG.info(e.getMessage()+" : Unable to schedule the job");
                    //To change body of catch statement use File | Settings | File Templates.
              }
          }
      }

    }
}
