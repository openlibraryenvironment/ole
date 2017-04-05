package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 12/07/16.
 * 
 */
@Entity
@Table(name="ole_ds_itm_former_identifier_t")
@NamedQuery(name="FormerIdentifierRecord.findAll", query="SELECT f FROM FormerIdentifierRecord f")
public class FormerIdentifierRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_FORMER_IDENTIFIER_ID")
	private Integer itemFormerIdentifierId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String type;

	private String value;

	//bi-directional many-to-one association to ItemRecord
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private ItemRecord itemRecord;

	public FormerIdentifierRecord() {
	}

	public Integer getItemFormerIdentifierId() {
		return this.itemFormerIdentifierId;
	}

	public void setItemFormerIdentifierId(Integer itemFormerIdentifierId) {
		this.itemFormerIdentifierId = itemFormerIdentifierId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

}