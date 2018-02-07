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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.api.rule.RuleTemplateAttributeContract;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.RuleValidationAttribute;
import org.kuali.rice.kew.rule.WorkflowRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A model bean which services as the link between a {@link RuleTemplateBo} and
 * a {@link RuleAttribute}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_TMPL_ATTR_T")
//@Sequence(name="KREW_RTE_TMPL_S", property="id")
public class RuleTemplateAttributeBo extends PersistableBusinessObjectBase
        implements Comparable<RuleTemplateAttributeBo>, MutableInactivatable, RuleTemplateAttributeContract {

    private static final long serialVersionUID = -3580049225424553828L;
    @Id
    @GeneratedValue(generator="KREW_RTE_TMPL_S")
	@GenericGenerator(name="KREW_RTE_TMPL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_TMPL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_TMPL_ATTR_ID")
	private String id;
    @Column(name="RULE_TMPL_ID", insertable=false, updatable=false)
	private String ruleTemplateId;
    @Column(name="RULE_ATTR_ID", insertable=false, updatable=false)
	private String ruleAttributeId;
    @Column(name="REQ_IND")
	private Boolean required;
    @Column(name="ACTV_IND")
	private Boolean active;
    @Column(name="DSPL_ORD")
	private Integer displayOrder;
    @Column(name="DFLT_VAL")
	private String defaultValue;

    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RULE_TMPL_ID")
	private RuleTemplateBo ruleTemplate;
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RULE_ATTR_ID")
	private RuleAttribute ruleAttribute;
    @OneToMany(fetch=FetchType.LAZY,mappedBy="ruleTemplateAttribute")
	private List<RuleExtensionBo> ruleExtensions;
    
    
    public RuleTemplateAttributeBo() {
        this.required = Boolean.FALSE;
        this.active = Boolean.TRUE;
    }
   
    public int compareTo(RuleTemplateAttributeBo ruleTemplateAttribute) {
        if ((this.getDisplayOrder() != null) && (ruleTemplateAttribute.getDisplayOrder() != null)) {
            return this.getDisplayOrder().compareTo(ruleTemplateAttribute.getDisplayOrder());
        }
        return 0;
    }

    public Object getAttribute() {
        try {
            //ObjectDefinition objectDefinition = new ObjectDefinition(getRuleAttribute().getResourceDescriptor(), getRuleAttribute().getApplicationId());
            Object attribute = ExtensionUtils.loadExtension(RuleAttribute.to(getRuleAttribute()), getRuleAttribute().getApplicationId());
            if (attribute == null) {
                throw new WorkflowRuntimeException("Could not find attribute " + getRuleAttribute().getName());
            }
            if (attribute instanceof WorkflowRuleAttribute) {
                ((WorkflowRuleAttribute) attribute).setRequired(required.booleanValue());
            }
            return attribute;
        } catch (Exception e) {
            throw new RuntimeException("Caught error attempting to load attribute class: " + getRuleAttribute().getResourceDescriptor(), e);
        }
    }

    public boolean isWorkflowAttribute() {
        return getRuleAttribute().isWorkflowAttribute();
    }

    public boolean isRuleValidationAttribute() {
        // just check the type here to avoid having to load the class from the class loader if it's not actually there
        return KewApiConstants.RULE_VALIDATION_ATTRIBUTE_TYPE.equals(getRuleAttribute().getType());
    }

    /**
     * Instantiates and returns a new instance of the WorkflowAttribute class configured on this template.
     * The calling code should be sure to call isWorkflowAttribute first to verify the type of this attribute
     * is that of a WorkflowAttribute.  Otherwise a RuntimeException will be thrown.
     */
    public WorkflowRuleAttribute getWorkflowAttribute() {
        try {
            Object tempAttr = ExtensionUtils.loadExtension(RuleAttribute.to(getRuleAttribute()), getRuleAttribute().getApplicationId());

            if (tempAttr == null
                    || !WorkflowRuleAttribute.class.isAssignableFrom(tempAttr.getClass())) {
                throw new WorkflowRuntimeException("Could not find workflow attribute " + getRuleAttribute().getName());
            }
            WorkflowRuleAttribute workflowAttribute = (WorkflowRuleAttribute)tempAttr;
            workflowAttribute.setRequired(required.booleanValue());
            return workflowAttribute;
        } catch (Exception e) {
            throw new RuntimeException("Caught exception instantiating new " + getRuleAttribute().getResourceDescriptor(), e);
        }
    }

    /**
     * Instantiates and returns a new instance of the RuleValidationAttribute class configured on this template.
     * The calling code should be sure to call isRuleValidationAttribute first to verify the type of this attribute
     * is that of a RuleValidationAttribute.  Otherwise a RuntimeException will be thrown.
     */
    public RuleValidationAttribute getRuleValidationAttribute() {
        try {
            RuleAttribute attrib = getRuleAttribute();
            return KEWServiceLocator.getRuleValidationAttributeResolver().resolveRuleValidationAttribute(attrib.getName(), attrib.getApplicationId());
        } catch (Exception e) {
            throw new RuntimeException("Caught exception instantiating new " + getRuleAttribute().getResourceDescriptor(), e);
        }
    }

    public List<RuleExtensionBo> getRuleExtensions() {
        return ruleExtensions;
    }

    public Map<String, String> getRuleExtensionMap() {
        Map<String, String> extensions = new HashMap<String, String>();
        if (this.getRuleExtensions() != null) {
            for (RuleExtensionBo ext : this.getRuleExtensions()) {
                for (RuleExtensionValue value : ext.getExtensionValues()) {
                    extensions.put(value.getKey(), value.getValue());
                }
            }
        }
        return extensions;
    }

    public void setRuleExtensions(List<RuleExtensionBo> ruleExtensions) {
        this.ruleExtensions = ruleExtensions;
    }

    public RuleAttribute getRuleAttribute() {
        if (ruleAttribute == null && ruleAttributeId != null) {
            ruleAttribute = RuleAttribute.from(KewApiServiceLocator.getExtensionRepositoryService().getExtensionById(ruleAttributeId));
        }
        return ruleAttribute;
    }

    public void setRuleAttribute(org.kuali.rice.kew.rule.bo.RuleAttribute ruleAttribute) {
        this.ruleAttribute = ruleAttribute;
    }

    public RuleTemplateBo getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplateBo ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isRequired() {
        return (getRequired() == null) || (getRequired().booleanValue());
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public boolean isActive() {
        return (getActive() == null) || (getActive().booleanValue());
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public void setActive(boolean active) {
    	this.active = active;
    }

    public String getRuleAttributeId() {
    	return ruleAttributeId;
    }

    public void setRuleAttributeId(String ruleAttributeId) {
    	this.ruleAttributeId = ruleAttributeId;
    }

    public String getId() {
    	return id;
    }

    public void setId(String id) {
    	this.id = id;
    }

    public String getRuleTemplateId() {
    	return ruleTemplateId;
    }

    public void setRuleTemplateId(String ruleTemplateId) {
    	this.ruleTemplateId = ruleTemplateId;
    }

}
