package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 * 
 */
@Entity
@Table(name="ole_ds_authentication_type_t")
@NamedQuery(name="AuthenticationTypeRecord.findAll", query="SELECT a FROM AuthenticationTypeRecord a")
public class AuthenticationTypeRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AUTHENTICATION_TYPE_ID")
	private Integer authenticationTypeId;

	private String code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String name;

	public AuthenticationTypeRecord() {
	}

	public Integer getAuthenticationTypeId() {
		return this.authenticationTypeId;
	}

	public void setAuthenticationTypeId(Integer authenticationTypeId) {
		this.authenticationTypeId = authenticationTypeId;
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