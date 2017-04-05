package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 * 
 */
@Entity
@Table(name="ole_ds_access_location_code_t")
@NamedQuery(name="AccessLocation.findAll", query="SELECT a FROM AccessLocation a")
public class AccessLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESS_LOCATION_CODE_ID")
	private Integer accessLocationCodeId;

	private String code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String name;

	public AccessLocation() {
	}

	public Integer getAccessLocationCodeId() {
		return this.accessLocationCodeId;
	}

	public void setAccessLocationCodeId(Integer accessLocationCodeId) {
		this.accessLocationCodeId = accessLocationCodeId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}