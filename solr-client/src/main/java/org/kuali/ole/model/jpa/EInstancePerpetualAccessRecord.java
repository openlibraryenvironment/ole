package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_perpetual_access_t")
@NamedQuery(name="EInstancePerpetualAccessRecord.findAll", query="SELECT o FROM EInstancePerpetualAccessRecord o")
public class EInstancePerpetualAccessRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HOLDINGS_PERPETUAL_ACCESS_ID")
	private int holdingsPerpetualAccessId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="PERPETUAL_ACCESS_END_DATE")
	private String perpetualAccessEndDate;

	@Column(name="PERPETUAL_ACCESS_END_ISSUE")
	private String perpetualAccessEndIssue;

	@Column(name="PERPETUAL_ACCESS_END_VOLUME")
	private String perpetualAccessEndVolume;

	@Column(name="PERPETUAL_ACCESS_START_DATE")
	private String perpetualAccessStartDate;

	@Column(name="PERPETUAL_ACCESS_START_ISSUE")
	private String perpetualAccessStartIssue;

	@Column(name="PERPETUAL_ACCESS_START_VOLUME")
	private String perpetualAccessStartVolume;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	public EInstancePerpetualAccessRecord() {
	}

	public int getHoldingsPerpetualAccessId() {
		return this.holdingsPerpetualAccessId;
	}

	public void setHoldingsPerpetualAccessId(int holdingsPerpetualAccessId) {
		this.holdingsPerpetualAccessId = holdingsPerpetualAccessId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getPerpetualAccessEndDate() {
		return this.perpetualAccessEndDate;
	}

	public void setPerpetualAccessEndDate(String perpetualAccessEndDate) {
		this.perpetualAccessEndDate = perpetualAccessEndDate;
	}

	public String getPerpetualAccessEndIssue() {
		return this.perpetualAccessEndIssue;
	}

	public void setPerpetualAccessEndIssue(String perpetualAccessEndIssue) {
		this.perpetualAccessEndIssue = perpetualAccessEndIssue;
	}

	public String getPerpetualAccessEndVolume() {
		return this.perpetualAccessEndVolume;
	}

	public void setPerpetualAccessEndVolume(String perpetualAccessEndVolume) {
		this.perpetualAccessEndVolume = perpetualAccessEndVolume;
	}

	public String getPerpetualAccessStartDate() {
		return this.perpetualAccessStartDate;
	}

	public void setPerpetualAccessStartDate(String perpetualAccessStartDate) {
		this.perpetualAccessStartDate = perpetualAccessStartDate;
	}

	public String getPerpetualAccessStartIssue() {
		return this.perpetualAccessStartIssue;
	}

	public void setPerpetualAccessStartIssue(String perpetualAccessStartIssue) {
		this.perpetualAccessStartIssue = perpetualAccessStartIssue;
	}

	public String getPerpetualAccessStartVolume() {
		return this.perpetualAccessStartVolume;
	}

	public void setPerpetualAccessStartVolume(String perpetualAccessStartVolume) {
		this.perpetualAccessStartVolume = perpetualAccessStartVolume;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

}