package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_ds_loc_checkin_count_t")
@NamedQuery(name="LocationsCheckinCountRecord.findAll", query="SELECT l FROM LocationsCheckinCountRecord l")
public class LocationsCheckinCountRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CHECK_IN_LOCATION_ID")
	private Integer checkInLocationId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="LOCATION_COUNT")
	private Integer locationCount;

	@Column(name="LOCATION_IN_HOUSE_COUNT")
	private Integer locationInHouseCount;

	@Column(name="LOCATION_NAME")
	private String locationName;

	//bi-directional many-to-one association to ItemRecord
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private ItemRecord itemRecord;

	public LocationsCheckinCountRecord() {
	}

	public Integer getCheckInLocationId() {
		return this.checkInLocationId;
	}

	public void setCheckInLocationId(Integer checkInLocationId) {
		this.checkInLocationId = checkInLocationId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Integer getLocationCount() {
		return this.locationCount;
	}

	public void setLocationCount(Integer locationCount) {
		this.locationCount = locationCount;
	}

	public Integer getLocationInHouseCount() {
		return this.locationInHouseCount;
	}

	public void setLocationInHouseCount(Integer locationInHouseCount) {
		this.locationInHouseCount = locationInHouseCount;
	}

	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

}