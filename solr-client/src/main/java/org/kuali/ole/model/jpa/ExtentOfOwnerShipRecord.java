package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_ext_ownership_t")
@NamedQuery(name="ExtentOfOwnerShipRecord.findAll", query="SELECT o FROM ExtentOfOwnerShipRecord o")
public class ExtentOfOwnerShipRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXT_OWNERSHIP_ID")
	private Integer extOwnershipId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="EXT_OWNERSHIP_TYPE_ID")
	private Integer extOwnershipTypeId;

	private Integer ord;

	private String text;

	//bi-directional many-to-one association to ExtentNoteRecord
	@OneToMany(mappedBy="oleDsExtOwnershipT")
	private List<ExtentNoteRecord> extentNoteRecord;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	public ExtentOfOwnerShipRecord() {
	}

	public Integer getExtOwnershipId() {
		return this.extOwnershipId;
	}

	public void setExtOwnershipId(Integer extOwnershipId) {
		this.extOwnershipId = extOwnershipId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Integer getExtOwnershipTypeId() {
		return this.extOwnershipTypeId;
	}

	public void setExtOwnershipTypeId(Integer extOwnershipTypeId) {
		this.extOwnershipTypeId = extOwnershipTypeId;
	}

	public Integer getOrd() {
		return this.ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<ExtentNoteRecord> getExtentNoteRecords() {
		return this.extentNoteRecord;
	}

	public void setExtentNoteRecords(List<ExtentNoteRecord> extentNoteRecord) {
		this.extentNoteRecord = extentNoteRecord;
	}

	public ExtentNoteRecord addExtentNoteRecord(ExtentNoteRecord oleDsExtOwnershipNoteT) {
		getExtentNoteRecords().add(oleDsExtOwnershipNoteT);
		oleDsExtOwnershipNoteT.setOleDsExtOwnershipT(this);

		return oleDsExtOwnershipNoteT;
	}

	public ExtentNoteRecord removeExtentNoteRecord(ExtentNoteRecord oleDsExtOwnershipNoteT) {
		getExtentNoteRecords().remove(oleDsExtOwnershipNoteT);
		oleDsExtOwnershipNoteT.setOleDsExtOwnershipT(null);

		return oleDsExtOwnershipNoteT;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

}