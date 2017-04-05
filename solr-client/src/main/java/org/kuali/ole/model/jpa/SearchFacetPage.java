package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_search_facet_size_t")
@NamedQuery(name="SearchFacetPage.findAll", query="SELECT s FROM SearchFacetPage s")
public class SearchFacetPage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_SEARCH_FACET_SIZE_ID")
	private Integer docSearchFacetSizeId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="LONG_SIZE")
	private int longSize;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="SHORT_SIZE")
	private int shortSize;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public SearchFacetPage() {
	}

	public Integer getDocSearchFacetSizeId() {
		return docSearchFacetSizeId;
	}

	public void setDocSearchFacetSizeId(Integer docSearchFacetSizeId) {
		this.docSearchFacetSizeId = docSearchFacetSizeId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public int getLongSize() {
		return this.longSize;
	}

	public void setLongSize(int longSize) {
		this.longSize = longSize;
	}

	public String getObjId() {
		return this.objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public int getShortSize() {
		return this.shortSize;
	}

	public void setShortSize(int shortSize) {
		this.shortSize = shortSize;
	}

	public BigDecimal getVerNbr() {
		return this.verNbr;
	}

	public void setVerNbr(BigDecimal verNbr) {
		this.verNbr = verNbr;
	}

}