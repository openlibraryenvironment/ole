package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_itm_clm_rtnd_hstry_t")
@NamedQuery(name="ItemClaimsReturnedRecord.findAll", query="SELECT i FROM ItemClaimsReturnedRecord i")
public class ItemClaimsReturnedRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CLAIMS_RETURNED_ID")
	private Integer claimsReturnedId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CLAIMS_RETURNED_DATE_CREATED")
	private Date claimsReturnedDateCreated;

	@Column(name="CLAIMS_RETURNED_NOTE")
	private String claimsReturnedNote;

	@Column(name="CLAIMS_RETURNED_OPERATOR_ID")
	private String claimsReturnedOperatorId;

	@Column(name="CLAIMS_RETURNED_PATRON_BARCODE")
	private String claimsReturnedPatronBarcode;

	@Column(name="CLAIMS_RETURNED_PATRON_ID")
	private String claimsReturnedPatronId;

	//bi-directional many-to-one association to ItemRecord
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private ItemRecord itemRecord;

	public ItemClaimsReturnedRecord() {
	}

	public Integer getClaimsReturnedId() {
		return this.claimsReturnedId;
	}

	public void setClaimsReturnedId(Integer claimsReturnedId) {
		this.claimsReturnedId = claimsReturnedId;
	}

	public Date getClaimsReturnedDateCreated() {
		return this.claimsReturnedDateCreated;
	}

	public void setClaimsReturnedDateCreated(Date claimsReturnedDateCreated) {
		this.claimsReturnedDateCreated = claimsReturnedDateCreated;
	}

	public String getClaimsReturnedNote() {
		return this.claimsReturnedNote;
	}

	public void setClaimsReturnedNote(String claimsReturnedNote) {
		this.claimsReturnedNote = claimsReturnedNote;
	}

	public String getClaimsReturnedOperatorId() {
		return this.claimsReturnedOperatorId;
	}

	public void setClaimsReturnedOperatorId(String claimsReturnedOperatorId) {
		this.claimsReturnedOperatorId = claimsReturnedOperatorId;
	}

	public String getClaimsReturnedPatronBarcode() {
		return this.claimsReturnedPatronBarcode;
	}

	public void setClaimsReturnedPatronBarcode(String claimsReturnedPatronBarcode) {
		this.claimsReturnedPatronBarcode = claimsReturnedPatronBarcode;
	}

	public String getClaimsReturnedPatronId() {
		return this.claimsReturnedPatronId;
	}

	public void setClaimsReturnedPatronId(String claimsReturnedPatronId) {
		this.claimsReturnedPatronId = claimsReturnedPatronId;
	}

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

}