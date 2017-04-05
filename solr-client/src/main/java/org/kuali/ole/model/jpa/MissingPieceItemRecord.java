package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_miss_pce_itm_hstry_t")
@NamedQuery(name="MissingPieceItemRecord.findAll", query="SELECT m FROM MissingPieceItemRecord m")
public class MissingPieceItemRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MISSING_PIECE_ID")
	private Integer missingPieceId;

	@Column(name="MISSING_PIECE_COUNT")
	private Integer missingPieceCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MISSING_PIECE_DATE")
	private Date missingPieceDate;

	@Column(name="MISSING_PIECE_NOTE")
	private String missingPieceNote;

	@Column(name="OPERATOR_ID")
	private String operatorId;

	@Column(name="PATRON_BARCODE")
	private String patronBarcode;

	@Column(name="PATRON_ID")
	private String patronId;

	//bi-directional many-to-one association to ItemRecord
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private ItemRecord itemRecord;

	public MissingPieceItemRecord() {
	}

	public Integer getMissingPieceId() {
		return this.missingPieceId;
	}

	public void setMissingPieceId(Integer missingPieceId) {
		this.missingPieceId = missingPieceId;
	}

	public Integer getMissingPieceCount() {
		return this.missingPieceCount;
	}

	public void setMissingPieceCount(Integer missingPieceCount) {
		this.missingPieceCount = missingPieceCount;
	}

	public Date getMissingPieceDate() {
		return this.missingPieceDate;
	}

	public void setMissingPieceDate(Date missingPieceDate) {
		this.missingPieceDate = missingPieceDate;
	}

	public String getMissingPieceNote() {
		return this.missingPieceNote;
	}

	public void setMissingPieceNote(String missingPieceNote) {
		this.missingPieceNote = missingPieceNote;
	}

	public String getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getPatronBarcode() {
		return this.patronBarcode;
	}

	public void setPatronBarcode(String patronBarcode) {
		this.patronBarcode = patronBarcode;
	}

	public String getPatronId() {
		return this.patronId;
	}

	public void setPatronId(String patronId) {
		this.patronId = patronId;
	}

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

}