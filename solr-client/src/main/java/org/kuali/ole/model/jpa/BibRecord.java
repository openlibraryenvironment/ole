package org.kuali.ole.model.jpa;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 27/10/16.
 */
@Entity
@Table(name="ole_ds_bib_t")
@NamedQuery(name="BibRecord.findAll", query="SELECT b FROM BibRecord b")
public class BibRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BIB_ID")
	private Integer bibId;

	@Lob
	private String content;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_CREATED")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="FAST_ADD")
	private String fastAdd;

	@Column(name="FORMER_ID")
	private String formerId;

	@Column(name="STAFF_ONLY")
	private String staffOnly;

	private String status;

	@Column(name="STATUS_UPDATED_BY")
	private String statusUpdatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STATUS_UPDATED_DATE")
	private Date statusUpdatedDate;

	@Column(name="UNIQUE_ID_PREFIX")
	private String uniqueIdPrefix;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	//bi-directional many-to-one association to HoldingsRecord
	@OneToMany(mappedBy="bibRecord")
	private List<HoldingsRecord> holdingsRecords;

	public BibRecord() {
	}

    public Integer getBibId() {
        return bibId;
    }

    public void setBibId(Integer bibId) {
        this.bibId = bibId;
    }

    public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getFastAdd() {
		return this.fastAdd;
	}

	public void setFastAdd(String fastAdd) {
		this.fastAdd = fastAdd;
	}

	public String getFormerId() {
		return this.formerId;
	}

	public void setFormerId(String formerId) {
		this.formerId = formerId;
	}

	public String getStaffOnly() {
		return this.staffOnly;
	}

	public void setStaffOnly(String staffOnly) {
		this.staffOnly = staffOnly;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusUpdatedBy() {
		return this.statusUpdatedBy;
	}

	public void setStatusUpdatedBy(String statusUpdatedBy) {
		this.statusUpdatedBy = statusUpdatedBy;
	}

	public Date getStatusUpdatedDate() {
		return this.statusUpdatedDate;
	}

	public void setStatusUpdatedDate(Date statusUpdatedDate) {
		this.statusUpdatedDate = statusUpdatedDate;
	}

	public String getUniqueIdPrefix() {
		return this.uniqueIdPrefix;
	}

	public void setUniqueIdPrefix(String uniqueIdPrefix) {
		this.uniqueIdPrefix = uniqueIdPrefix;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<HoldingsRecord> getHoldingsRecords() {
		return this.holdingsRecords;
	}

	public void setHoldingsRecords(List<HoldingsRecord> holdingsRecords) {
		this.holdingsRecords = holdingsRecords;
	}

	public HoldingsRecord addHoldingsRecord(HoldingsRecord holdingsRecord) {
		getHoldingsRecords().add(holdingsRecord);
		holdingsRecord.setBibRecord(this);

		return holdingsRecord;
	}

	public HoldingsRecord removeHoldingsRecord(HoldingsRecord holdingsRecord) {
		getHoldingsRecords().remove(holdingsRecord);
		holdingsRecord.setBibRecord(null);

		return holdingsRecord;
	}

}
