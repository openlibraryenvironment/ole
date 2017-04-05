package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_cat_shvlg_schm_t")
@NamedQuery(name="CallNumberTypeRecord.findAll", query="SELECT c FROM CallNumberTypeRecord c")
public class CallNumberTypeRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SHVLG_SCHM_ID")
	private Long shvlgSchmId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="ROW_ACT_IND")
	private String rowActInd;

	@Column(name="SHVLG_SCHM_CD")
	private String shvlgSchmCd;

	@Column(name="SHVLG_SCHM_NM")
	private String shvlgSchmNm;

	private String src;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SRC_DT")
	private Date srcDt;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public CallNumberTypeRecord() {
	}

	public Long getShvlgSchmId() {
		return shvlgSchmId;
	}

	public void setShvlgSchmId(Long shvlgSchmId) {
		this.shvlgSchmId = shvlgSchmId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
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

	public String getShvlgSchmCd() {
		return this.shvlgSchmCd;
	}

	public void setShvlgSchmCd(String shvlgSchmCd) {
		this.shvlgSchmCd = shvlgSchmCd;
	}

	public String getShvlgSchmNm() {
		return this.shvlgSchmNm;
	}

	public void setShvlgSchmNm(String shvlgSchmNm) {
		this.shvlgSchmNm = shvlgSchmNm;
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