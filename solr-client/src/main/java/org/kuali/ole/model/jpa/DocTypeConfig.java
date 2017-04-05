package org.kuali.ole.model.jpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 28/10/16.
 */
@Entity
@Table(name="ole_ds_doc_type_t")
@NamedQuery(name="DocTypeConfig.findAll", query="SELECT d FROM DocTypeConfig d")
public class DocTypeConfig extends ConfigDocument  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_TYPE_ID")
	private Integer docTypeId;

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

	//bi-directional many-to-one association to DocFormatConfig
	@OneToMany(mappedBy="docTypeConfig")
	private List<DocFormatConfig> docFormatConfigs;

	public DocTypeConfig() {
	}

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
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

	public DocFieldConfig addOleDsDocFieldT(DocFieldConfig oleDsDocFieldT) {
		getDocFieldConfigs().add(oleDsDocFieldT);
		oleDsDocFieldT.setDocTypeConfig(this);

		return oleDsDocFieldT;
	}

	public DocFieldConfig removeOleDsDocFieldT(DocFieldConfig oleDsDocFieldT) {
		getDocFieldConfigs().remove(oleDsDocFieldT);
		oleDsDocFieldT.setDocTypeConfig(null);

		return oleDsDocFieldT;
	}

	public List<DocFormatConfig> getDocFormatConfigs() {
		return this.docFormatConfigs;
	}

	public void setDocFormatConfigs(List<DocFormatConfig> docFormatConfigs) {
		this.docFormatConfigs = docFormatConfigs;
	}

	public DocFormatConfig addOleDsDocFormatT(DocFormatConfig docFormatConfig) {
		getDocFormatConfigs().add(docFormatConfig);
		docFormatConfig.setDocTypeConfig(this);

		return docFormatConfig;
	}

	public DocFormatConfig removeOleDsDocFormatT(DocFormatConfig docFormatConfig) {
		getDocFormatConfigs().remove(docFormatConfig);
		docFormatConfig.setDocTypeConfig(null);

		return docFormatConfig;
	}

}