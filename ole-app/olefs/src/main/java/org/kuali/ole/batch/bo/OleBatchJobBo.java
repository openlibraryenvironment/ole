package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/1/13
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleBatchJobBo extends PersistableBusinessObjectBase {
    private String jobId;
    private String jobTriggerName;
    private String jobCronExpression;
    private boolean jobEnableStatus;


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobCronExpression() {
        return jobCronExpression;
    }

    public void setJobCronExpression(String jobCronExpression) {
        this.jobCronExpression = jobCronExpression;
    }

    public boolean isJobEnableStatus() {
        return jobEnableStatus;
    }

    public void setJobEnableStatus(boolean jobEnableStatus) {
        this.jobEnableStatus = jobEnableStatus;
    }


    public String getJobTriggerName() {
        return jobTriggerName;
    }

    public void setJobTriggerName(String jobTriggerName) {
        this.jobTriggerName = jobTriggerName;
    }
}
