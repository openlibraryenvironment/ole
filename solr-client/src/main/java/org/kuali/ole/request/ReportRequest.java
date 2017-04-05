package org.kuali.ole.request;

import java.util.Date;

/**
 * Created by sheiks on 15/02/17.
 */
public class ReportRequest {
    public String reportType;
    public Date createdDate;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
