package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_ds_item_donor_t")
@NamedQuery(name="OLEItemDonorRecord.findAll", query="SELECT o FROM OLEItemDonorRecord o")
public class OLEItemDonorRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_DONOR_ID")
	private Integer itemDonorId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="DONOR_CODE")
	private String donorCode;

	@Column(name="DONOR_DISPLAY_NOTE")
	private String donorDisplayNote;

	@Column(name="DONOR_NOTE")
	private String donorNote;

	//bi-directional many-to-one association to ItemRecord
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private ItemRecord itemRecord;

	public OLEItemDonorRecord() {
	}

	public Integer getItemDonorId() {
		return this.itemDonorId;
	}

	public void setItemDonorId(Integer itemDonorId) {
		this.itemDonorId = itemDonorId;
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

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

}