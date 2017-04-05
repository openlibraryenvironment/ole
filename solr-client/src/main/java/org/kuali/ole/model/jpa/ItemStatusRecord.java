package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_dlvr_item_avail_stat_t")
@NamedQuery(name="ItemStatusRecord.findAll", query="SELECT i FROM ItemStatusRecord i")
public class ItemStatusRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_AVAIL_STAT_ID")
	private String itemAvailStatId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="ITEM_AVAIL_STAT_CD")
	private String itemAvailStatCd;

	@Column(name="ITEM_AVAIL_STAT_NM")
	private String itemAvailStatNm;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="ROW_ACT_IND")
	private String rowActInd;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public ItemStatusRecord() {
	}

	public String getItemAvailStatId() {
		return this.itemAvailStatId;
	}

	public void setItemAvailStatId(String itemAvailStatId) {
		this.itemAvailStatId = itemAvailStatId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getItemAvailStatCd() {
		return this.itemAvailStatCd;
	}

	public void setItemAvailStatCd(String itemAvailStatCd) {
		this.itemAvailStatCd = itemAvailStatCd;
	}

	public String getItemAvailStatNm() {
		return this.itemAvailStatNm;
	}

	public void setItemAvailStatNm(String itemAvailStatNm) {
		this.itemAvailStatNm = itemAvailStatNm;
	}

	public String getObjId() {
		return this.objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getRowActInd() {
		return this.rowActInd;
	}

	public void setRowActInd(String rowActInd) {
		this.rowActInd = rowActInd;
	}

	public BigDecimal getVerNbr() {
		return this.verNbr;
	}

	public void setVerNbr(BigDecimal verNbr) {
		this.verNbr = verNbr;
	}

}