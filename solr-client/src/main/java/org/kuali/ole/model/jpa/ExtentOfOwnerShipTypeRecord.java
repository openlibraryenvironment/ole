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
@Table(name="ole_cat_type_ownership_t")
@NamedQuery(name="ExtentOfOwnerShipTypeRecord.findAll", query="SELECT e FROM ExtentOfOwnerShipTypeRecord e")
public class ExtentOfOwnerShipTypeRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TYPE_OWNERSHIP_ID")
	private Long typeOwnershipId;

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

	@Column(name="TYPE_OWNERSHIP_CD")
	private String typeOwnershipCd;

	@Column(name="TYPE_OWNERSHIP_NM")
	private String typeOwnershipNm;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public ExtentOfOwnerShipTypeRecord() {
	}

	public Long getTypeOwnershipId() {
		return typeOwnershipId;
	}

	public void setTypeOwnershipId(Long typeOwnershipId) {
		this.typeOwnershipId = typeOwnershipId;
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

	public String getTypeOwnershipCd() {
		return this.typeOwnershipCd;
	}

	public void setTypeOwnershipCd(String typeOwnershipCd) {
		this.typeOwnershipCd = typeOwnershipCd;
	}

	public String getTypeOwnershipNm() {
		return this.typeOwnershipNm;
	}

	public void setTypeOwnershipNm(String typeOwnershipNm) {
		this.typeOwnershipNm = typeOwnershipNm;
	}

	public BigDecimal getVerNbr() {
		return this.verNbr;
	}

	public void setVerNbr(BigDecimal verNbr) {
		this.verNbr = verNbr;
	}

}