package org.kuali.ole.batch.bo;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/19/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessScheduleBo extends PersistableBusinessObjectBase {
    private String scheduleId;
    private String batchProcessId;
    private String batchProcessType;
    private String userName;
    private String uploadFileName;
    private Timestamp createTime = new Timestamp(new Date().getTime());
    private Date oneTimeDate;
    private String oneTimeStartTime;
    private String scheduleType;
    private String startTime;
    private List<String> weekDaysList;
    private String weekDays;
    private String dayNumber;
    private String monthNumber;
    private String oneTimeOrRecurring;
    private String cronExpression;
    private String documentNumber;
    private Timestamp nextRunTime;
    private String uploadOPFileName;
    private String batchProfileName;

    private OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo;
    private OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument;
    private List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList;


    public Timestamp getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(Timestamp nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public String getUploadOPFileName() {
        return uploadOPFileName;
    }

    public void setUploadOPFileName(String uploadOPFileName) {
        this.uploadOPFileName = uploadOPFileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getOneTimeDate() {
        return oneTimeDate;
    }

    public void setOneTimeDate(Date oneTimeDate) {
        this.oneTimeDate = oneTimeDate;
    }

    public String getOneTimeStartTime() {
        return oneTimeStartTime;
    }

    public void setOneTimeStartTime(String oneTimeStartTime) {
        this.oneTimeStartTime = oneTimeStartTime;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<String> getWeekDaysList() {
        return weekDaysList;
    }

    public void setWeekDaysList(List<String> weekDaysList) {
        this.weekDaysList = weekDaysList;
    }

    public String getWeekDays() {
        if(!CollectionUtils.isEmpty(weekDaysList)){
            StringBuilder weekdaysBuffer = new StringBuilder(500);
            for(int i=0;i<weekDaysList.size();i++){
                weekdaysBuffer.append(weekDaysList.get(i));
                if(i!=weekDaysList.size()-1){
                    weekdaysBuffer.append(",");
                }
            }
            return weekdaysBuffer.toString();
        }
        else{
            return null;
        }
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getOneTimeOrRecurring() {
        return oneTimeOrRecurring;
    }

    public void setOneTimeOrRecurring(String oneTimeOrRecurring) {
        this.oneTimeOrRecurring = oneTimeOrRecurring;
    }

    public String getBatchProcessType() {
        return batchProcessType;
    }

    public void setBatchProcessType(String batchProcessType) {
        this.batchProcessType = batchProcessType;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getBatchProcessId() {
        return batchProcessId;
    }

    public void setBatchProcessId(String batchProcessId) {
        this.batchProcessId = batchProcessId;
    }

    public OLEBatchProcessDefinitionDocument getOleBatchProcessDefinitionDocument() {
        return oleBatchProcessDefinitionDocument;
    }

    public void setOleBatchProcessDefinitionDocument(OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument) {
        this.oleBatchProcessDefinitionDocument = oleBatchProcessDefinitionDocument;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public List<OLEBatchProcessJobDetailsBo> getOleBatchProcessJobDetailsBoList() {
        return oleBatchProcessJobDetailsBoList;
    }

    public void setOleBatchProcessJobDetailsBoList(List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList) {
        this.oleBatchProcessJobDetailsBoList = oleBatchProcessJobDetailsBoList;
    }

   public OLEBatchProcessJobDetailsBo getOleBatchProcessJobDetailsBo() {
        return oleBatchProcessJobDetailsBo;
    }

    public void setOleBatchProcessJobDetailsBo(OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo) {
        this.oleBatchProcessJobDetailsBo = oleBatchProcessJobDetailsBo;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getBatchProfileName() {
        return batchProfileName;
    }

    public void setBatchProfileName(String batchProfileName) {
        this.batchProfileName = batchProfileName;
    }
}
