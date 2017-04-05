package org.kuali.ole.model.jpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 28/10/16.
 */
@Entity
@Table(name="ole_ds_doc_format_t")
@NamedQuery(name="DocFormatConfig.findAll", query="SELECT d FROM DocFormatConfig d")
public class DocFormatConfig extends ConfigDocument  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FORMAT_ID")
	private Integer docFormatId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	private String description;

	@Column(name="DISPLAY_LABEL")
	private String displayLabel;

	private String name;

	@Column(name="OBJ_ID")
	private String objId;

	@Column(name="VER_NBR")
	private BigDecimal verNbr;

	//bi-directional many-to-one association to DocFieldConfig
	@OneToMany(mappedBy="docFormatConfig")
	private List<DocFieldConfig> docFieldConfigs;

	//bi-directional many-to-one association to DocTypeConfig
	@ManyToOne
	@JoinColumn(name="DOC_TYPE_ID")
	private DocTypeConfig docTypeConfig;

	public DocFormatConfig() {
	}

	public Integer getDocFormatId() {
		return docFormatId;
	}

	public void setDocFormatId(Integer docFormatId) {
		this.docFormatId = docFormatId;
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

	public List<DocFieldConfig> getDocFieldConfigs() {
		return this.docFieldConfigs;
	}

	public void setDocFieldConfigs(List<DocFieldConfig> docFieldConfigs) {
		this.docFieldConfigs = docFieldConfigs;
	}

	public DocFieldConfig addOleDsDocFieldT(DocFieldConfig docFieldConfig) {
		getDocFieldConfigs().add(docFieldConfig);
		docFieldConfig.setDocFormatConfig(this);

		return docFieldConfig;
	}

	public DocFieldConfig removeOleDsDocFieldT(DocFieldConfig docFieldConfig) {
		getDocFieldConfigs().remove(docFieldConfig);
		docFieldConfig.setDocFormatConfig(null);

		return docFieldConfig;
	}

	public DocTypeConfig getDocTypeConfig() {
		return this.docTypeConfig;
	}

	public void setDocTypeConfig(DocTypeConfig docTypeConfig) {
		this.docTypeConfig = docTypeConfig;
	}

}