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
package org.kuali.rice.kew.rule.bo;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionDefinitionContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Model bean defining a rule attribute.  Includes the classname of the attribute
 * class, as well as it's name and other information.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_ATTR_T")
//@Sequence(name="KREW_RTE_TMPL_S", property="id")
@NamedQueries({
  @NamedQuery(name="RuleAttribute.FindById",  query="select ra from RuleAttribute ra where ra.ruleAttributeId = :ruleAttributeId"),
  @NamedQuery(name="RuleAttribute.FindByName",  query="select ra from RuleAttribute ra where ra.name = :name"),
  @NamedQuery(name="RuleAttribute.FindByClassName",  query="select ra from RuleAttribute ra where ra.className = :className"),
  @NamedQuery(name="RuleAttribute.GetAllRuleAttributes",  query="select ra from RuleAttribute ra")
})
public class RuleAttribute extends PersistableBusinessObjectBase implements ExtensionDefinitionContract {

	private static final long serialVersionUID = 1027673603158346349L;

	@Id
	@GeneratedValue(generator="KREW_RTE_TMPL_S")
	@GenericGenerator(name="KREW_RTE_TMPL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_TMPL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_ATTR_ID")
	private String id;
    @Column(name="NM")
	private String name;
    @Column(name="LBL")
	private String label;
    @Column(name="RULE_ATTR_TYP_CD")
	private String type;
    @Column(name="CLS_NM")
	private String resourceDescriptor;
    @Column(name="DESC_TXT")
	private String description;
    @Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="XML")
	private String xmlConfigData;

    @Column(name="APPL_ID")
	private String applicationId;
    
    @OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
           targetEntity=RuleTemplateAttributeBo.class, mappedBy="ruleAttribute")
    @Fetch(value=FetchMode.SELECT)
	private List ruleTemplateAttributes;
    @Transient
    private List validValues;
    
    // required to be lookupable
    @Transient
    private String returnUrl;

    public RuleAttribute() {
        ruleTemplateAttributes = new ArrayList();
        validValues = new ArrayList();
    }

    public List getValidValues() {
        return validValues;
    }
    public void setValidValues(List ruleAttributeValidValues) {
        this.validValues = ruleAttributeValidValues;
    }
    public List getRuleTemplateAttributes() {
        return ruleTemplateAttributes;
    }
    public void setRuleTemplateAttributes(List ruleTemplateAttributes) {
        this.ruleTemplateAttributes = ruleTemplateAttributes;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the resourceDescriptor.
     */
    public String getResourceDescriptor() {
      return resourceDescriptor;
    }
    /**
     * @param resourceDescriptor The className to set.
     */
    public void setResourceDescriptor(String resourceDescriptor) {
      this.resourceDescriptor = resourceDescriptor;
    }
    
    public String getRuleAttributeActionsUrl() {
        return "<a href=\"RuleAttributeReport.do?id="+ id +"\" >report</a>";
    }
    
    public String getReturnUrl() {
        return returnUrl;
    }
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

	public String getXmlConfigData() {
		return xmlConfigData;
	}

    @Override
    public Map<String, String> getConfiguration() {
        Map<String, String> config = new HashMap<String, String>();
        if (StringUtils.isNotBlank(getXmlConfigData())) {
            config.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, getXmlConfigData());
        }
        return config;
    }

	public void setXmlConfigData(String xmlConfigData) {
		this.xmlConfigData = xmlConfigData;
	}

    @Override
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

    public boolean isWorkflowAttribute() {
        return isWorkflowAttribute(getType());
    }

    public static boolean isWorkflowAttribute(String type) {
        return KewApiConstants.RULE_ATTRIBUTE_TYPE.equals(type) ||
            KewApiConstants.RULE_XML_ATTRIBUTE_TYPE.equals(type);
    }

    public static ExtensionDefinition to(RuleAttribute ruleAttribute) {
        if (ruleAttribute == null) {
            return null;
        }
        return ExtensionDefinition.Builder.create(ruleAttribute).build();
    }

    public static RuleAttribute from(ExtensionDefinition im) {
        if (im == null) {
            return null;
        }
        RuleAttribute bo = new RuleAttribute();
        bo.setApplicationId(im.getApplicationId());
        bo.setDescription(im.getDescription());
        bo.setResourceDescriptor(im.getResourceDescriptor());
        bo.setId(im.getId());
        bo.setLabel(im.getLabel());
        bo.setType(im.getType());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setXmlConfigData(im.getConfiguration().get(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA));

        return bo;
    }
}
