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
@Table(name="ole_cat_stat_srch_cd_t")
@NamedQuery(name="StatisticalSearchRecord.findAll", query="SELECT s FROM StatisticalSearchRecord s")
public class StatisticalSearchRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="STAT_SRCH_CD_ID")
	private Long statSrchCdId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="ROW_ACT_IND")
	private String rowActInd;

	private String src;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SRC_DT")
	private Date srcDt;

	@Column(name="STAT_SRCH_CD")
	private String statSrchCd;

	@Column(name="STAT_SRCH_NM")
	private String statSrchNm;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public StatisticalSearchRecord() {
	}

	public Long getStatSrchCdId() {
		return statSrchCdId;
	}

	public void setStatSrchCdId(Long statSrchCdId) {
		this.statSrchCdId = statSrchCdId;
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

	public String getStatSrchCd() {
		return this.statSrchCd;
	}

	public void setStatSrchCd(String statSrchCd) {
		this.statSrchCd = statSrchCd;
	}

	public String getStatSrchNm() {
		return this.statSrchNm;
	}

	public void setStatSrchNm(String statSrchNm) {
		this.statSrchNm = statSrchNm;
	}

	public BigDecimal getVerNbr() {
		return this.verNbr;
	}

	public void setVerNbr(BigDecimal verNbr) {
		this.verNbr = verNbr;
	}

}