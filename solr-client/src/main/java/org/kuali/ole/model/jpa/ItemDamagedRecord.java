package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_ds_dmgd_itm_hstry_t")
@NamedQuery(name="ItemDamagedRecord.findAll", query="SELECT i FROM ItemDamagedRecord i")
public class ItemDamagedRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITM_DMGD_ID")
	private Integer itmDmgdId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DMGD_ITM_DATE")
	private Date dmgdItmDate;

	@Column(name="DMGD_ITM_NOTE")
	private String dmgdItmNote;

	@Column(name="DMGD_PATRON_ID")
	private String dmgdPatronId;

	@Column(name="OPERATOR_ID")
	private String operatorId;

	@Column(name="PATRON_BARCODE")
	private String patronBarcode;

	//bi-directional many-to-one association to ItemRecord
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private ItemRecord itemRecord;

	public ItemDamagedRecord() {
	}

	public Integer getItmDmgdId() {
		return this.itmDmgdId;
	}

	public void setItmDmgdId(Integer itmDmgdId) {
		this.itmDmgdId = itmDmgdId;
	}

	public Date getDmgdItmDate() {
		return this.dmgdItmDate;
	}

	public void setDmgdItmDate(Date dmgdItmDate) {
		this.dmgdItmDate = dmgdItmDate;
	}

	public String getDmgdItmNote() {
		return this.dmgdItmNote;
	}

	public void setDmgdItmNote(String dmgdItmNote) {
		this.dmgdItmNote = dmgdItmNote;
	}

	public String getDmgdPatronId() {
		return this.dmgdPatronId;
	}

	public void setDmgdPatronId(String dmgdPatronId) {
		this.dmgdPatronId = dmgdPatronId;
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

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

}