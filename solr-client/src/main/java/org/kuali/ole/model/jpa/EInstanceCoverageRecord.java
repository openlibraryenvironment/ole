package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 *
 */
@Entity
@Table(name="ole_ds_holdings_coverage_t")
@NamedQuery(name="EInstanceCoverageRecord.findAll", query="SELECT o FROM EInstanceCoverageRecord o")
public class EInstanceCoverageRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HOLDINGS_COVERAGE_ID")
	private int holdingsCoverageId;

	@Column(name="COVERAGE_END_DATE")
	private String coverageEndDate;

	@Column(name="COVERAGE_END_ISSUE")
	private String coverageEndIssue;

	@Column(name="COVERAGE_END_VOLUME")
	private String coverageEndVolume;

	@Column(name="COVERAGE_START_DATE")
	private String coverageStartDate;

	@Column(name="COVERAGE_START_ISSUE")
	private String coverageStartIssue;

	@Column(name="COVERAGE_START_VOLUME")
	private String coverageStartVolume;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	public EInstanceCoverageRecord() {
	}

	public int getHoldingsCoverageId() {
		return this.holdingsCoverageId;
	}

	public void setHoldingsCoverageId(int holdingsCoverageId) {
		this.holdingsCoverageId = holdingsCoverageId;
	}

	public String getCoverageEndDate() {
		return this.coverageEndDate;
	}

	public void setCoverageEndDate(String coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	public String getCoverageEndIssue() {
		return this.coverageEndIssue;
	}

	public void setCoverageEndIssue(String coverageEndIssue) {
		this.coverageEndIssue = coverageEndIssue;
	}

	public String getCoverageEndVolume() {
		return this.coverageEndVolume;
	}

	public void setCoverageEndVolume(String coverageEndVolume) {
		this.coverageEndVolume = coverageEndVolume;
	}

	public String getCoverageStartDate() {
		return this.coverageStartDate;
	}

	public void setCoverageStartDate(String coverageStartDate) {
		this.coverageStartDate = coverageStartDate;
	}

	public String getCoverageStartIssue() {
		return this.coverageStartIssue;
	}

	public void setCoverageStartIssue(String coverageStartIssue) {
		this.coverageStartIssue = coverageStartIssue;
	}

	public String getCoverageStartVolume() {
		return this.coverageStartVolume;
	}

	public void setCoverageStartVolume(String coverageStartVolume) {
		this.coverageStartVolume = coverageStartVolume;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

}