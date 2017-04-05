package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_holdings_uri_t")
@NamedQuery(name="HoldingsUriRecord.findAll", query="SELECT o FROM HoldingsUriRecord o")
public class HoldingsUriRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HOLDINGS_URI_ID")
	private int holdingsUriId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String text;

	private String uri;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	public HoldingsUriRecord() {
	}

	public int getHoldingsUriId() {
		return this.holdingsUriId;
	}

	public void setHoldingsUriId(int holdingsUriId) {
		this.holdingsUriId = holdingsUriId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

}