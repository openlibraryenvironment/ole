package org.kuali.ole.model.jpa;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by sheiks on 14/02/17.
 */
@Entity
@Table(name = "REPORT_DATA_T")
public class ReportDataEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REPORT_DATA_ID")
    private Integer reportDataId;

    @Column(name = "HEADER_NAME")
    private String headerName;

    @Column(name = "HEADER_VALUE")
    private String headerValue;

    @Column(name = "RECORD_NUM")
    private String recordNum;

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public Integer getReportDataId() {
        return reportDataId;
    }

    public void setReportDataId(Integer reportDataId) {
        this.reportDataId = reportDataId;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }
}
