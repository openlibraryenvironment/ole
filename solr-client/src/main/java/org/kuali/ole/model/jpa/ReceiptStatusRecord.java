package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 * 
 */
@Entity
@Table(name="ole_cat_rcpt_stat_t")
@NamedQuery(name="ReceiptStatusRecord.findAll", query="SELECT r FROM ReceiptStatusRecord r")
public class ReceiptStatusRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RCPT_STAT_ID")
	private String rcptStatId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="RCPT_STAT_CD")
	private String rcptStatCd;

	@Column(name="RCPT_STAT_NM")
	private String rcptStatNm;

	@Column(name="ROW_ACT_IND")
	private String rowActInd;

	private String src;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SRC_DT")
	private Date srcDt;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public ReceiptStatusRecord() {
	}

	public String getRcptStatId() {
		return this.rcptStatId;
	}

	public void setRcptStatId(String rcptStatId) {
		this.rcptStatId = rcptStatId;
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

	public String getRcptStatCd() {
		return this.rcptStatCd;
	}

	public void setRcptStatCd(String rcptStatCd) {
		this.rcptStatCd = rcptStatCd;
	}

	public String getRcptStatNm() {
		return this.rcptStatNm;
	}

	public void setRcptStatNm(String rcptStatNm) {
		this.rcptStatNm = rcptStatNm;
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