package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_ds_high_density_storage_t")
@NamedQuery(name="HighDensityStorageRecord.findAll", query="SELECT h FROM HighDensityStorageRecord h")
public class HighDensityStorageRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HIGH_DENSITY_STORAGE_ID")
	private Integer highDensityStorageId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="HIGH_DENSITY_MODULE")
	private String highDensityModule;

	@Column(name="HIGH_DENSITY_ROW")
	private String highDensityRow;

	@Column(name="HIGH_DENSITY_SHELF")
	private String highDensityShelf;

	@Column(name="HIGH_DENSITY_TRAY")
	private String highDensityTray;

	//bi-directional many-to-one association to ItemRecord
	@OneToMany(mappedBy="highDensityStorageRecord")
	private List<ItemRecord> itemRecords;

	public HighDensityStorageRecord() {
	}

	public Integer getHighDensityStorageId() {
		return this.highDensityStorageId;
	}

	public void setHighDensityStorageId(Integer highDensityStorageId) {
		this.highDensityStorageId = highDensityStorageId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getHighDensityModule() {
		return this.highDensityModule;
	}

	public void setHighDensityModule(String highDensityModule) {
		this.highDensityModule = highDensityModule;
	}

	public String getHighDensityRow() {
		return this.highDensityRow;
	}

	public void setHighDensityRow(String highDensityRow) {
		this.highDensityRow = highDensityRow;
	}

	public String getHighDensityShelf() {
		return this.highDensityShelf;
	}

	public void setHighDensityShelf(String highDensityShelf) {
		this.highDensityShelf = highDensityShelf;
	}

	public String getHighDensityTray() {
		return this.highDensityTray;
	}

	public void setHighDensityTray(String highDensityTray) {
		this.highDensityTray = highDensityTray;
	}

	public List<ItemRecord> getItemRecords() {
		return this.itemRecords;
	}

	public void setItemRecords(List<ItemRecord> itemRecords) {
		this.itemRecords = itemRecords;
	}

	public ItemRecord addOleDsItemT(ItemRecord oleDsItemT) {
		getItemRecords().add(oleDsItemT);
		oleDsItemT.setHighDensityStorageRecord(this);

		return oleDsItemT;
	}

	public ItemRecord removeOleDsItemT(ItemRecord oleDsItemT) {
		getItemRecords().remove(oleDsItemT);
		oleDsItemT.setHighDensityStorageRecord(null);

		return oleDsItemT;
	}

}