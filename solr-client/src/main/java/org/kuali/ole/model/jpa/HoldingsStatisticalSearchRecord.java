package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_holdings_stat_search_t")
@NamedQuery(name="HoldingsStatisticalSearchRecord.findAll", query="SELECT o FROM HoldingsStatisticalSearchRecord o")
public class HoldingsStatisticalSearchRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HOLDINGS_STAT_SEARCH_ID")
	private Integer holdingsStatSearchId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="STAT_SEARCH_CODE_ID")
	private Integer statSearchCodeId;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	public HoldingsStatisticalSearchRecord() {
	}

	public Integer getHoldingsStatSearchId() {
		return this.holdingsStatSearchId;
	}

	public void setHoldingsStatSearchId(Integer holdingsStatSearchId) {
		this.holdingsStatSearchId = holdingsStatSearchId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Integer getStatSearchCodeId() {
		return this.statSearchCodeId;
	}

	public void setStatSearchCodeId(Integer statSearchCodeId) {
		this.statSearchCodeId = statSearchCodeId;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

}