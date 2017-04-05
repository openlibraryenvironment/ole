package org.kuali.ole.model.jpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sheiks on 28/10/16.
 */
@Entity
@Table(name="ole_ds_doc_field_t")
@NamedQuery(name="DocFieldConfig.findAll", query="SELECT d FROM DocFieldConfig d")
public class DocFieldConfig extends ConfigDocument  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FIELD_ID")
	private Integer docFieldId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String description;

	@Column(name="DISPLAY_LABEL")
	private String displayLabel;

	@Column(name="EXCLUDE_PATH")
	private String excludePath;

	@Column(name="INCLUDE_PATH")
	private String includePath;

	@Column(name="IS_DISPLAY")
	private String isDisplay;

	@Column(name="IS_EXPORT")
	private String isExport;

	@Column(name="IS_FACET")
	private String isFacet;

	@Column(name="IS_GLOBAL_EDIT")
	private String isGlobalEdit;

	@Column(name="IS_SEARCH")
	private String isSearch;

	private String name;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	//bi-directional many-to-one association to DocFormatConfig
	@ManyToOne
	@JoinColumn(name="DOC_FORMAT_ID")
	private DocFormatConfig docFormatConfig;

	//bi-directional many-to-one association to DocTypeConfig
	@ManyToOne
	@JoinColumn(name="DOC_TYPE_ID")
	private DocTypeConfig docTypeConfig;

	public DocFieldConfig() {
	}

	public Integer getDocFieldId() {
		return docFieldId;
	}

	public void setDocFieldId(Integer docFieldId) {
		this.docFieldId = docFieldId;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayLabel() {
		return this.displayLabel;
	}

	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	public String getExcludePath() {
		return this.excludePath;
	}

	public void setExcludePath(String excludePath) {
		this.excludePath = excludePath;
	}

	public String getIncludePath() {
		return this.includePath;
	}

	public void setIncludePath(String includePath) {
		this.includePath = includePath;
	}

	public String getIsDisplay() {
		return this.isDisplay;
	}

	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getIsExport() {
		return this.isExport;
	}

	public void setIsExport(String isExport) {
		this.isExport = isExport;
	}

	public String getIsFacet() {
		return this.isFacet;
	}

	public void setIsFacet(String isFacet) {
		this.isFacet = isFacet;
	}

	public String getIsGlobalEdit() {
		return this.isGlobalEdit;
	}

	public void setIsGlobalEdit(String isGlobalEdit) {
		this.isGlobalEdit = isGlobalEdit;
	}

	public String getIsSearch() {
		return this.isSearch;
	}

	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjId() {
		return this.objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public BigDecimal getVerNbr() {
		return this.verNbr;
	}

	public void setVerNbr(BigDecimal verNbr) {
		this.verNbr = verNbr;
	}

	public DocFormatConfig getDocFormatConfig() {
		return this.docFormatConfig;
	}

	public void setDocFormatConfig(DocFormatConfig docFormatConfig) {
		this.docFormatConfig = docFormatConfig;
	}

	public DocTypeConfig getDocTypeConfig() {
		return this.docTypeConfig;
	}

	public void setDocTypeConfig(DocTypeConfig docTypeConfig) {
		this.docTypeConfig = docTypeConfig;
	}

}