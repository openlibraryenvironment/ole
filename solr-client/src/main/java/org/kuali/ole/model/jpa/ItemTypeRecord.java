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
@Table(name="ole_cat_itm_typ_t")
@NamedQuery(name="ItemTypeRecord.findAll", query="SELECT i FROM ItemTypeRecord i")
public class ItemTypeRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITM_TYP_CD_ID")
	private String itmTypCdId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="ITM_TYP_CD")
	private String itmTypCd;

	@Column(name="ITM_TYP_DESC")
	private String itmTypDesc;

	@Column(name="ITM_TYP_NM")
	private String itmTypNm;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="ROW_ACT_IND")
	private String rowActInd;

	private String src;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SRC_DT")
	private Date srcDt;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public ItemTypeRecord() {
	}

	public String getItmTypCdId() {
		return this.itmTypCdId;
	}

	public void setItmTypCdId(String itmTypCdId) {
		this.itmTypCdId = itmTypCdId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getItmTypCd() {
		return this.itmTypCd;
	}

	public void setItmTypCd(String itmTypCd) {
		this.itmTypCd = itmTypCd;
	}

	public String getItmTypDesc() {
		return this.itmTypDesc;
	}

	public void setItmTypDesc(String itmTypDesc) {
		this.itmTypDesc = itmTypDesc;
	}

	public String getItmTypNm() {
		return this.itmTypNm;
	}

	public void setItmTypNm(String itmTypNm) {
		this.itmTypNm = itmTypNm;
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

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public Date getSrcDt() {
		return this.srcDt;
	}

	public void setSrcDt(Date srcDt) {
		this.srcDt = srcDt;
	}

	public BigDecimal getVerNbr() {
		return this.verNbr;
	}

	public void setVerNbr(BigDecimal verNbr) {
		this.verNbr = verNbr;
	}

}