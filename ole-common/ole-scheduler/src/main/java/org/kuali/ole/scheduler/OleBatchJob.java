package org.kuali.ole.scheduler;

import org.springframework.batch.core.Job;

/**
 * Created by SBRitter on 07.03.2016.
 */
public class OleBatchJob {

    private Job job;
    private String cronExpression;
    private String name;

    public OleBatchJob(Job job, String cronExpression, String name) {
        this.job = job;
        this.cronExpression = cronExpression;
        this.name = name;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
