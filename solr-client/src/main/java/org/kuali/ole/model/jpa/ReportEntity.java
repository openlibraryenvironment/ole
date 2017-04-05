package org.kuali.ole.model.jpa;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 14/02/17.
 */
@Entity
@Table(name = "REPORT_T")
public class ReportEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RECORD_NUM")
    private Integer recordNumber;

    @Column(name = "TYPE")
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "RECORD_NUM")
    private List<ReportDataEntity> reportDataEntities = new ArrayList<>();

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ReportDataEntity> getReportDataEntities() {
        return reportDataEntities;
    }

    public void setReportDataEntities(List<ReportDataEntity> reportDataEntities) {
        this.reportDataEntities = reportDataEntities;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void addAll(List<ReportDataEntity> reportDataEntities) {
        if(null == getReportDataEntities()){
            reportDataEntities = new ArrayList<>();
        }
        this.reportDataEntities.addAll(reportDataEntities);
    }
}
