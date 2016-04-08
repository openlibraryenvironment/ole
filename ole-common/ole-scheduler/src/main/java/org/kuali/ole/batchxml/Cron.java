package org.kuali.ole.batchxml;

/**
 */
public class Cron {
    private String id;
    private String jobId;
    private String cronPattern;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCronPattern() {
        return cronPattern;
    }

    public void setCronPattern(String cronPattern) {
        this.cronPattern = cronPattern;
    }
}
