package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_search_result_page_t")
@NamedQuery(name="SearchResultPage.findAll", query="SELECT s FROM SearchResultPage s")
public class SearchResultPage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_SEARCH_PAGE_SIZE_ID")
	private Integer docSearchPageSizeId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="PAGE_SIZE")
	private int pageSize;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	public SearchResultPage() {
	}

	public Integer getDocSearchPageSizeId() {
		return docSearchPageSizeId;
	}

	public void setDocSearchPageSizeId(Integer docSearchPageSizeId) {
		this.docSearchPageSizeId = docSearchPageSizeId;
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

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public BigDecimal getVerNbr() {
		return this.verNbr;
	}

	public void setVerNbr(BigDecimal verNbr) {
		this.verNbr = verNbr;
	}

}