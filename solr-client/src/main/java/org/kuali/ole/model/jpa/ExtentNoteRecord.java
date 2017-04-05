package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_ds_ext_ownership_note_t")
@NamedQuery(name="ExtentNoteRecord.findAll", query="SELECT e FROM ExtentNoteRecord e")
public class ExtentNoteRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXT_OWNERSHIP_NOTE_ID")
	private int extOwnershipNoteId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String note;

	private String type;

	//bi-directional many-to-one association to ExtentOfOwnerShipRecord
	@ManyToOne
	@JoinColumn(name="EXT_OWNERSHIP_ID")
	private ExtentOfOwnerShipRecord oleDsExtOwnershipT;

	public ExtentNoteRecord() {
	}

	public int getExtOwnershipNoteId() {
		return this.extOwnershipNoteId;
	}

	public void setExtOwnershipNoteId(int extOwnershipNoteId) {
		this.extOwnershipNoteId = extOwnershipNoteId;
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

	public ExtentOfOwnerShipRecord getOleDsExtOwnershipT() {
		return this.oleDsExtOwnershipT;
	}

	public void setOleDsExtOwnershipT(ExtentOfOwnerShipRecord oleDsExtOwnershipT) {
		this.oleDsExtOwnershipT = oleDsExtOwnershipT;
	}

}