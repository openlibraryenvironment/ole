package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_holdings_donor_t")
@NamedQuery(name="OLEHoldingsDonorRecord.findAll", query="SELECT o FROM OLEHoldingsDonorRecord o")
public class OLEHoldingsDonorRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HOLDINGS_DONOR_ID")
	private int holdingsDonorId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="DONOR_CODE")
	private String donorCode;

	@Column(name="DONOR_DISPLAY_NOTE")
	private String donorDisplayNote;

	@Column(name="DONOR_NOTE")
	private String donorNote;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	public OLEHoldingsDonorRecord() {
	}

	public int getHoldingsDonorId() {
		return this.holdingsDonorId;
	}

	public void setHoldingsDonorId(int holdingsDonorId) {
		this.holdingsDonorId = holdingsDonorId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getDonorCode() {
		return this.donorCode;
	}

	public void setDonorCode(String donorCode) {
		this.donorCode = donorCode;
	}

	public String getDonorDisplayNote() {
		return this.donorDisplayNote;
	}

	public void setDonorDisplayNote(String donorDisplayNote) {
		this.donorDisplayNote = donorDisplayNote;
	}

	public String getDonorNote() {
		return this.donorNote;
	}

	public void setDonorNote(String donorNote) {
		this.donorNote = donorNote;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

}