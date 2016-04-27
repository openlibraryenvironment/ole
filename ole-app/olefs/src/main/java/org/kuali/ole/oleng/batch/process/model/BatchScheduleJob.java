package org.kuali.ole.oleng.batch.process.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by rajeshbabuk on 2/10/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchScheduleJob extends PersistableBusinessObjectBase {

    @JsonProperty(OleNGConstants.SCHEDULE_OPTION)
    private String scheduleOption;

    @JsonProperty(OleNGConstants.SCHEDULE_TYPE)
    private String scheduleType;

    @JsonProperty(OleNGConstants.SCHEDULE_DATE)
    private Timestamp scheduleDate;

    @JsonProperty(OleNGConstants.SCHEDULE_TIME)
    private String scheduleTime;

    @JsonProperty(OleNGConstants.WEEK_DAY)
    private String weekDay;

    @JsonProperty(OleNGConstants.MONTH_DAY)
    private String monthDay;

    @JsonProperty(OleNGConstants.MONTH_FREQUENCY)
    private String monthFrequency;

    @JsonProperty(OleNGConstants.CRON_EXPRESSION)
    private String cronExpression;

    public String getScheduleOption() {
        return scheduleOption;
    }

    public void setScheduleOption(String scheduleOption) {
        this.scheduleOption = scheduleOption;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Timestamp getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Timestamp scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(String monthDay) {
        this.monthDay = monthDay;
    }

    public String getMonthFrequency() {
        return monthFrequency;
    }

    public void setMonthFrequency(String monthFrequency) {
        this.monthFrequency = monthFrequency;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
