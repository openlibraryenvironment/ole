package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_ds_item_stat_search_t")
@NamedQuery(name="ItemStatisticalSearchRecord.findAll", query="SELECT i FROM ItemStatisticalSearchRecord i")
public class ItemStatisticalSearchRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_STAT_SEARCH_ID")
	private Integer itemStatSearchId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="STAT_SEARCH_CODE_ID")
	private Integer statSearchCodeId;

	//bi-directional many-to-one association to ItemRecord
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private ItemRecord itemRecord;

	public ItemStatisticalSearchRecord() {
	}

	public Integer getItemStatSearchId() {
		return this.itemStatSearchId;
	}

	public void setItemStatSearchId(Integer itemStatSearchId) {
		this.itemStatSearchId = itemStatSearchId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Integer getStatSearchCodeId() {
		return this.statSearchCodeId;
	}

	public void setStatSearchCodeId(Integer statSearchCodeId) {
		this.statSearchCodeId = statSearchCodeId;
	}

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

}