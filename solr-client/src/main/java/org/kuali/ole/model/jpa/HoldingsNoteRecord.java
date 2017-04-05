package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_holdings_note_t")
@NamedQuery(name="HoldingsNoteRecord.findAll", query="SELECT o FROM HoldingsNoteRecord o")
public class HoldingsNoteRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HOLDINGS_NOTE_ID")
	private int holdingsNoteId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String note;

	private String type;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	public HoldingsNoteRecord() {
	}

	public int getHoldingsNoteId() {
		return this.holdingsNoteId;
	}

	public void setHoldingsNoteId(int holdingsNoteId) {
		this.holdingsNoteId = holdingsNoteId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

}