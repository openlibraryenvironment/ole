package org.kuali.ole.deliver.bo;

import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
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
    private String pickupLocation;

    private boolean scheduleFlag;
    private boolean rescheduleFlag = true;
    private String runNowOrSchedule;
    private String cronOrSchedule;
    private String enteredCronExp;
    private String oneTimeOrRecurring;
    private String scheduleType;
    private boolean runNowFlag = false;
    private OLEBatchProcessScheduleBo oleBatchProcessScheduleBo;

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

    public boolean isScheduleFlag() {
        return scheduleFlag;
    }

    public void setScheduleFlag(boolean scheduleFlag) {
        this.scheduleFlag = scheduleFlag;
    }

    public boolean isRescheduleFlag() {
        return rescheduleFlag;
    }

    public void setRescheduleFlag(boolean rescheduleFlag) {
        this.rescheduleFlag = rescheduleFlag;
    }

    public String getRunNowOrSchedule() {
        return runNowOrSchedule;
    }

    public void setRunNowOrSchedule(String runNowOrSchedule) {
        this.runNowOrSchedule = runNowOrSchedule;
    }

    public String getCronOrSchedule() {
        return cronOrSchedule;
    }

    public void setCronOrSchedule(String cronOrSchedule) {
        this.cronOrSchedule = cronOrSchedule;
    }

    public String getEnteredCronExp() {
        return enteredCronExp;
    }

    public void setEnteredCronExp(String enteredCronExp) {
        this.enteredCronExp = enteredCronExp;
    }

    public String getOneTimeOrRecurring() {
        return oneTimeOrRecurring;
    }

    public void setOneTimeOrRecurring(String oneTimeOrRecurring) {
        this.oneTimeOrRecurring = oneTimeOrRecurring;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public boolean isRunNowFlag() {
        return runNowFlag;
    }

    public void setRunNowFlag(boolean runNowFlag) {
        this.runNowFlag = runNowFlag;
    }

    public OLEBatchProcessScheduleBo getOleBatchProcessScheduleBo() {
        return oleBatchProcessScheduleBo;
    }

    public void setOleBatchProcessScheduleBo(OLEBatchProcessScheduleBo oleBatchProcessScheduleBo) {
        this.oleBatchProcessScheduleBo = oleBatchProcessScheduleBo;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
}
