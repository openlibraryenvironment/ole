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
package org.kuali.rice.kew.rule;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.util.collect.CollectionUtils;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.api.rule.RuleExtensionContract;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.service.KEWServiceLocator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An extension of a {@link RuleBaseValues}.  Provides attribute-specific data
 * extensions to the rule for a particular {@link org.kuali.rice.kew.rule.bo.RuleAttribute}.  Contains
 * a List of {@link RuleExtensionValue}s.
 * 
 * @see RuleBaseValues
 * @see org.kuali.rice.kew.rule.bo.RuleAttribute
 * @see RuleExtensionValue
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_EXT_T")
//@Sequence(name="KREW_RTE_TMPL_S", property="ruleExtensionId")
public class RuleExtensionBo implements RuleExtensionContract, Serializable {

	private static final long serialVersionUID = 8178135296413950516L;

	@Id
	@GeneratedValue(generator="KREW_RTE_TMPL_S")
	@GenericGenerator(name="KREW_RTE_TMPL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_TMPL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_EXT_ID")
	private String ruleExtensionId;

	@Column(name="RULE_TMPL_ATTR_ID", insertable=false, updatable=false)
	private String ruleTemplateAttributeId;

	@Column(name="RULE_ID", insertable=false, updatable=false)
	private String ruleBaseValuesId;

	@Version
	@Column(name="VER_NBR")
	private Long versionNumber;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RULE_ID")
	private RuleBaseValues ruleBaseValues;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RULE_TMPL_ATTR_ID")
	private RuleTemplateAttributeBo ruleTemplateAttribute;

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, mappedBy="extension")
	@Fetch(value = FetchMode.SELECT)
	private List<RuleExtensionValue> extensionValues;

	public RuleExtensionBo() {
		extensionValues = new ArrayList<RuleExtensionValue>();
	}

	//@PrePersist
    public void beforeInsert(){
        OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
    }
	
	public List<RuleExtensionValue> getExtensionValues() {
		return extensionValues;
	}

	public void setExtensionValues(List<RuleExtensionValue> extensionValues) {
		this.extensionValues = extensionValues;
	}

    @Override
	public RuleTemplateAttributeBo getRuleTemplateAttribute() {
		return ruleTemplateAttribute;
	}

    @Override
    public Map<String, String> getExtensionValuesMap() {
        Map<String, String> extensions = new HashMap<String, String>();
        for (RuleExtensionValue value : getExtensionValues()) {
            extensions.put(value.getKey(), value.getValue());
        }
        return extensions;
    }

    public void setRuleTemplateAttribute(RuleTemplateAttributeBo ruleTemplateAttribute) {
		this.ruleTemplateAttribute = ruleTemplateAttribute;
	}

	public RuleExtensionValue getRuleExtensionValue(int index) {
		while (getExtensionValues().size() <= index) {
			getExtensionValues().add(new RuleExtensionValue());
		}
		return (RuleExtensionValue) getExtensionValues().get(index);
	}

	public RuleBaseValues getRuleBaseValues() {
		return ruleBaseValues;
	}

	public void setRuleBaseValues(RuleBaseValues ruleBaseValues) {
		this.ruleBaseValues = ruleBaseValues;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getRuleBaseValuesId() {
		return ruleBaseValuesId;
	}

	public void setRuleBaseValuesId(String ruleBaseValuesId) {
		this.ruleBaseValuesId = ruleBaseValuesId;
	}

	public String getRuleExtensionId() {
		return ruleExtensionId;
	}

	public void setRuleExtensionId(String ruleExtensionId) {
		this.ruleExtensionId = ruleExtensionId;
	}

	public String getRuleTemplateAttributeId() {
		return ruleTemplateAttributeId;
	}

	public void setRuleTemplateAttributeId(String ruleTemplateAttributeId) {
		this.ruleTemplateAttributeId = ruleTemplateAttributeId;
	}

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof RuleExtensionBo)) return false;
        RuleExtensionBo pred = (RuleExtensionBo) o;
        return ObjectUtils.equals(ruleBaseValues.getRuleTemplate(), pred.getRuleBaseValues().getRuleTemplate()) &&
               ObjectUtils.equals(ruleTemplateAttribute, pred.getRuleTemplateAttribute()) &&
               CollectionUtils.collectionsEquivalent(extensionValues, pred.getExtensionValues());
    }

    public String toString() {
        return "[RuleExtension:"
               +  " ruleExtensionId=" + ruleExtensionId
               + ", ruleTemplateAttributeId=" + ruleTemplateAttributeId
               + ", ruleBaseValuesId=" + ruleBaseValuesId
               + ", ruleBaseValues=" + ruleBaseValues
               + ", ruleTemplateAttribute=" + ruleTemplateAttribute
               + ", extensionValues=" + extensionValues
               + ", versionNumber=" + versionNumber
               + "]";
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static RuleExtension to(RuleExtensionBo bo) {
        if (bo == null) {
            return null;
        }
        return RuleExtension.Builder.create(bo).build();
    }

}
