/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.bo.ui;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * This class is the base class for KIM documents sub-business objects that store attribute/qualifier data
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
public class KimDocumentAttributeDataBusinessObjectBase extends KimDocumentBoActivatableEditableBase {

	private static final long serialVersionUID = -1512640359333185819L;
	@Id
	@GeneratedValue(generator="KRIM_ATTR_DATA_ID_S")
	@GenericGenerator(name="KRIM_ATTR_DATA_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRIM_ATTR_DATA_ID_S"),
			@Parameter(name="value_column",value="id")
		})
	@Column(name = "ATTR_DATA_ID")
	private String attrDataId;
	@Column(name = "KIM_TYP_ID")
	private String kimTypId;
	@Column(name = "KIM_ATTR_DEFN_ID")
	private String kimAttrDefnId;
	@Column(name = "ATTR_VAL")
	private String attrVal = "";
	@OneToOne(targetEntity=KimAttributeBo.class, fetch=FetchType.EAGER, cascade={})
    @JoinColumn(name="KIM_ATTR_DEFN_ID",insertable=false,updatable=false)
	private KimAttributeBo kimAttribute;
	@Transient
	private String qualifierKey;
	@Transient
	private Boolean unique;
	
	/**
	 * This constructs a ...
	 * 
	 */
	public KimDocumentAttributeDataBusinessObjectBase() {
		super();
	}

	public String getAttrDataId() {
		return attrDataId;
	}

	public void setAttrDataId(String attrDataId) {
		this.attrDataId = attrDataId;
	}

	public String getKimTypId() {
		return kimTypId;
	}

	public void setKimTypId(String kimTypId) {
		this.kimTypId = kimTypId;
	}

	public String getKimAttrDefnId() {
		return kimAttrDefnId;
	}

	public void setKimAttrDefnId(String kimAttrDefnId) {
		this.kimAttrDefnId = kimAttrDefnId;
	}

	public String getAttrVal() {
		return attrVal;
	}

	public void setAttrVal(String attrVal) {
		this.attrVal = attrVal;
	}

	public String getQualifierKey() {
		return this.qualifierKey;
	}

	public void setQualifierKey(String qualifierKey) {
		this.qualifierKey = qualifierKey;
	}

	/**
	 * @return the kimAttribute
	 */
	public KimAttributeBo getKimAttribute() {
		return this.kimAttribute;
	}

	/**
	 * @param kimAttribute the kimAttribute to set
	 */
	public void setKimAttribute(KimAttributeBo kimAttribute) {
		this.kimAttribute = kimAttribute;
	}

	/**
	 * @return the uniqueAndReadOnly
	 */
	public Boolean isUnique() {
		return this.unique;
	}

	/**
	 * @param uniqueAndReadOnly the uniqueAndReadOnly to set
	 */
	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

}
