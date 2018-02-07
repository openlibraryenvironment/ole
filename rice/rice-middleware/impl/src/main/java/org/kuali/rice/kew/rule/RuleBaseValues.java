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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.api.rule.RuleContract;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.lookupable.MyColumns;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.service.RuleServiceInternal;
import org.kuali.rice.kew.rule.xmlrouting.GenericXMLRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*import org.kuali.rice.kim.api.group.Group;*/


/**
 * A model bean for a Rule within the KEW rules engine.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_T")
//@Sequence(name="KREW_RTE_TMPL_S", property="id")
public class RuleBaseValues extends PersistableBusinessObjectBase implements RuleContract {

    private static final long serialVersionUID = 6137765574728530156L;
    @Id
    @GeneratedValue(generator="KREW_RTE_TMPL_S")
	@GenericGenerator(name="KREW_RTE_TMPL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_TMPL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_ID")
    private String id;
    /**
     * Unique Rule name
     */
    @Column(name="NM")
	private String name;
    @Column(name="RULE_TMPL_ID", insertable=false, updatable=false)
	private String ruleTemplateId;
    @Column(name="PREV_VER_RULE_ID")
	private String previousRuleId;
    @Column(name="ACTV_IND")
	private boolean active = true;
    @Column(name="RULE_BASE_VAL_DESC")
	private String description;
    @Column(name="DOC_TYP_NM")
	private String docTypeName;
    @Column(name="DOC_HDR_ID")
	private String documentId;
	@Column(name="FRM_DT")
	private Timestamp fromDateValue;
	@Column(name="TO_DT")
	private Timestamp toDateValue;
	@Column(name="DACTVN_DT")
	private Timestamp deactivationDate;
    @Column(name="CUR_IND")
	private Boolean currentInd = Boolean.TRUE;
    @Column(name="RULE_VER_NBR")
	private Integer versionNbr = new Integer(0);
    @Column(name="FRC_ACTN")
	private boolean forceAction;
    @Fetch(value = FetchMode.SELECT)
    @OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},mappedBy="ruleBaseValues")
	private List<RuleResponsibilityBo> ruleResponsibilities;
    @Fetch(value = FetchMode.SELECT)
    @OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},mappedBy="ruleBaseValues")
	private List<RuleExtensionBo> ruleExtensions;
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RULE_TMPL_ID")
	private RuleTemplateBo ruleTemplate;
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumn(name="RULE_EXPR_ID")
	private RuleExpressionDef ruleExpressionDef;
    @Transient
    private RuleBaseValues previousVersion;
    @Column(name="ACTVN_DT")
	private Timestamp activationDate;
    @Column(name="DLGN_IND")
    private Boolean delegateRule = Boolean.FALSE;
    /**
     * Indicator that signifies that this rule is a defaults/template rule which contains
     * template-defined rule defaults for other rules which use the associated template
     */
    @Column(name="TMPL_RULE_IND")
    private Boolean templateRuleInd = Boolean.FALSE;

    // required to be lookupable
    @Transient
    private String returnUrl;
    @Transient
    private String destinationUrl;
    @Transient
    private MyColumns myColumns;
    @Transient
    private List<PersonRuleResponsibility> personResponsibilities = new ArrayList<PersonRuleResponsibility>();
    @Transient
    private List<GroupRuleResponsibility> groupResponsibilities = new ArrayList<GroupRuleResponsibility>();
    @Transient
    private List<RoleRuleResponsibility> roleResponsibilities = new ArrayList<RoleRuleResponsibility>();
    @Transient
    private Map<String, String> fieldValues;
    @Transient
    private String groupReviewerName;
    @Transient
    private String groupReviewerNamespace;
    @Transient
    private String personReviewer;
    @Transient
    private String personReviewerType;

    public RuleBaseValues() {
        ruleResponsibilities = new ArrayList<RuleResponsibilityBo>();
        ruleExtensions = new ArrayList<RuleExtensionBo>();
        /*personResponsibilities = new AutoPopulatingList<PersonRuleResponsibility>(PersonRuleResponsibility.class);
        groupResponsibilities = new AutoPopulatingList<GroupRuleResponsibility>(GroupRuleResponsibility.class);
        roleResponsibilities = new AutoPopulatingList<RoleRuleResponsibility>(RoleRuleResponsibility.class);*/
        fieldValues = new HashMap<String, String>();
    }

    /**
     * @return the rule expression definition for this rule, if defined
     */
    public RuleExpressionDef getRuleExpressionDef() {
        return ruleExpressionDef;
    }

    /**
     * @param ruleExpressionDef the rule expression definition to set for this rule
     */
    public void setRuleExpressionDef(RuleExpressionDef ruleExpressionDef) {
        this.ruleExpressionDef = ruleExpressionDef;
    }

    public String getRuleTemplateName() {
        if (ruleTemplate != null) {
            return ruleTemplate.getName();
        }
        return null;
    }

    public RuleBaseValues getPreviousVersion() {
        if (previousVersion == null && previousRuleId != null) {
            RuleServiceInternal ruleService = (RuleServiceInternal) KEWServiceLocator.getService(KEWServiceLocator.RULE_SERVICE);
            return ruleService.findRuleBaseValuesById(previousRuleId);
        }
        return previousVersion;
    }

    public void setPreviousVersion(RuleBaseValues previousVersion) {
        this.previousVersion = previousVersion;
    }

    public RuleResponsibilityBo getResponsibility(int index) {
        while (getRuleResponsibilities().size() <= index) {
            RuleResponsibilityBo ruleResponsibility = new RuleResponsibilityBo();
            ruleResponsibility.setRuleBaseValues(this);
            getRuleResponsibilities().add(ruleResponsibility);
        }
        return (RuleResponsibilityBo) getRuleResponsibilities().get(index);
    }

    public RuleExtensionBo getRuleExtension(int index) {
        while (getRuleExtensions().size() <= index) {
            getRuleExtensions().add(new RuleExtensionBo());
        }
        return (RuleExtensionBo) getRuleExtensions().get(index);
    }

    public RuleExtensionValue getRuleExtensionValue(String key) {
        for (Iterator iter = getRuleExtensions().iterator(); iter.hasNext();) {
            RuleExtensionBo ruleExtension = (RuleExtensionBo) iter.next();
            for (Iterator iterator = ruleExtension.getExtensionValues().iterator(); iterator.hasNext();) {
                RuleExtensionValue ruleExtensionValue = (RuleExtensionValue) iterator.next();
                if (ruleExtensionValue.getKey().equals(key)) {
                    return ruleExtensionValue;
                }
            }
        }
        return null;
    }

    public RuleExtensionValue getRuleExtensionValue(String ruleTemplateAttributeId, String key) {
        for (Iterator iter = getRuleExtensions().iterator(); iter.hasNext();) {
            RuleExtensionBo ruleExtension = (RuleExtensionBo) iter.next();
            if (ruleExtension.getRuleTemplateAttributeId().equals(ruleTemplateAttributeId)) {
                for (Iterator iterator = ruleExtension.getExtensionValues().iterator(); iterator.hasNext();) {
                    RuleExtensionValue ruleExtensionValue = (RuleExtensionValue) iterator.next();
                    if (ruleExtensionValue.getKey().equals(key)) {
                        return ruleExtensionValue;
                    }
                }
            }
        }
        return null;
    }

    public String getPreviousRuleId() {
        return previousRuleId;
    }

    public void setPreviousRuleId(String previousVersion) {
        this.previousRuleId = previousVersion;
    }

    public void addRuleResponsibility(RuleResponsibilityBo ruleResponsibility) {
        addRuleResponsibility(ruleResponsibility, new Integer(getRuleResponsibilities().size()));
    }

    public void addRuleResponsibility(RuleResponsibilityBo ruleResponsibility, Integer counter) {
        boolean alreadyAdded = false;
        int location = 0;
        if (counter != null) {
            for (RuleResponsibilityBo ruleResponsibilityRow : getRuleResponsibilities()) {
                if (counter.intValue() == location) {
                    ruleResponsibilityRow.setPriority(ruleResponsibility.getPriority());
                    ruleResponsibilityRow.setActionRequestedCd(ruleResponsibility.getActionRequestedCd());
                    ruleResponsibilityRow.setVersionNumber(ruleResponsibility.getVersionNumber());
                    ruleResponsibilityRow.setRuleBaseValuesId(ruleResponsibility.getRuleBaseValuesId());
                    ruleResponsibilityRow.setRuleResponsibilityName(ruleResponsibility.getRuleResponsibilityName());
                    ruleResponsibilityRow.setRuleResponsibilityType(ruleResponsibility.getRuleResponsibilityType());
                    //ruleResponsibilityRow.setDelegationRules(ruleResponsibility.getDelegationRules());
                    ruleResponsibilityRow.setApprovePolicy(ruleResponsibility.getApprovePolicy());
                    alreadyAdded = true;
                }
                location++;
            }
        }
        if (!alreadyAdded) {
            getRuleResponsibilities().add(ruleResponsibility);
        }
    }

    public RuleTemplateBo getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplateBo ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    public String getRuleTemplateId() {
        return ruleTemplateId;
    }

    public void setRuleTemplateId(String ruleTemplateId) {
        this.ruleTemplateId = ruleTemplateId;
    }

    public DocumentType getDocumentType() {
    	return KEWServiceLocator.getDocumentTypeService().findByName(getDocTypeName());
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public List<RuleExtensionBo> getRuleExtensions() {
        return ruleExtensions;
    }

    public Map<String, String> getRuleExtensionMap() {
        Map<String, String> extensions = new HashMap<String, String>();
        for (RuleExtensionBo ext : this.getRuleExtensions()) {
            for (RuleExtensionValue value : ext.getExtensionValues()) {
                extensions.put(value.getKey(), value.getValue());
            }
        }
        return extensions;
    }

    public void setRuleExtensions(List<RuleExtensionBo> ruleExtensions) {
        this.ruleExtensions = ruleExtensions;
    }

    public List<RuleResponsibilityBo> getRuleResponsibilities() {
        return this.ruleResponsibilities;
    }

    public void setRuleResponsibilities(List<RuleResponsibilityBo> ruleResponsibilities) {
        this.ruleResponsibilities = ruleResponsibilities;
    }

    public RuleResponsibilityBo getResponsibility(Long ruleResponsibilityKey) {
        for (Iterator iterator = getRuleResponsibilities().iterator(); iterator.hasNext();) {
            RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iterator.next();
            if (responsibility.getId() != null
                    && responsibility.getId().equals(ruleResponsibilityKey)) {
                return responsibility;
            }
        }
        return null;
    }

    public void removeResponsibility(int index) {
        getRuleResponsibilities().remove(index);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActiveIndDisplay() {
        return CodeTranslator.getActiveIndicatorLabel(isActive());
    }

    public Boolean getCurrentInd() {
        return currentInd;
    }

    public void setCurrentInd(Boolean currentInd) {
        this.currentInd = currentInd;
    }

    public Timestamp getFromDateValue() {
        return fromDateValue;
    }
    
    @Override
    public DateTime getFromDate() {
        if (this.fromDateValue == null) {
            return null;
        }
        return new DateTime(this.fromDateValue.getTime());
    }

    public void setFromDateValue(Timestamp fromDateValue) {
        this.fromDateValue = fromDateValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getToDateValue() {
        return toDateValue;
    }
    
    @Override
    public DateTime getToDate() {
        if (this.toDateValue == null) {
            return null;
        }
        return new DateTime(this.toDateValue.getTime());
    }

    public void setToDateValue(Timestamp toDateValue) {
        this.toDateValue = toDateValue;
    }

    public Integer getVersionNbr() {
        return versionNbr;
    }

    public void setVersionNbr(Integer versionNbr) {
        this.versionNbr = versionNbr;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getFromDateString() {
        if (this.fromDateValue != null) {
            return RiceConstants.getDefaultDateFormat().format(this.fromDateValue);
        }
        return null;
    }

    public String getToDateString() {
        if (this.toDateValue != null) {
            return RiceConstants.getDefaultDateFormat().format(this.toDateValue);
        }
        return null;
    }

    @Override
    public boolean isForceAction() {
        return forceAction;
    }

    public void setForceAction(boolean forceAction) {
        this.forceAction = forceAction;
    }

    public boolean isActive(Date date) {
    	boolean isAfterFromDate = getFromDateValue() == null || date.after(getFromDateValue());
    	boolean isBeforeToDate = getToDateValue() == null || date.before(getToDateValue());
    	return isActive() && isAfterFromDate && isBeforeToDate;
    }

    public boolean isMatch(DocumentContent docContent) {
        for (RuleTemplateAttributeBo ruleTemplateAttribute : getRuleTemplate().getActiveRuleTemplateAttributes()) {
            if (!ruleTemplateAttribute.isWorkflowAttribute()) {
                continue;
            }
            WorkflowRuleAttribute routingAttribute = (WorkflowRuleAttribute) ruleTemplateAttribute.getWorkflowAttribute();

            RuleAttribute ruleAttribute = ruleTemplateAttribute.getRuleAttribute();
            if (ruleAttribute.getType().equals(KewApiConstants.RULE_XML_ATTRIBUTE_TYPE)) {
                ((GenericXMLRuleAttribute) routingAttribute).setExtensionDefinition(RuleAttribute.to(ruleAttribute));
            }
            String className = ruleAttribute.getResourceDescriptor();
            List<RuleExtension> editedRuleExtensions = new ArrayList<RuleExtension>();
            for (RuleExtensionBo extension : getRuleExtensions()) {
                if (extension.getRuleTemplateAttribute().getRuleAttribute().getResourceDescriptor().equals(className)) {
                    editedRuleExtensions.add(RuleExtensionBo.to(extension));
                }
            }
            if (!routingAttribute.isMatch(docContent, editedRuleExtensions)) {
                return false;
            }
        }
        return true;
    }

    public RuleResponsibilityBo findResponsibility(String roleName) {
        for (Iterator iter = getRuleResponsibilities().iterator(); iter.hasNext();) {
            RuleResponsibilityBo resp = (RuleResponsibilityBo) iter.next();
            if (KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID.equals(resp.getRuleResponsibilityType())
                    && roleName.equals(resp.getRuleResponsibilityName())) {
                return resp;
            }
        }
        return null;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Boolean getDelegateRule() {
        return delegateRule;
    }

    public void setDelegateRule(Boolean isDelegateRule) {
        this.delegateRule = isDelegateRule;
    }

    public Timestamp getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Timestamp activationDate) {
        this.activationDate = activationDate;
    }

    public MyColumns getMyColumns() {
        return myColumns;
    }

    public void setMyColumns(MyColumns additionalColumns) {
        this.myColumns = additionalColumns;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public Timestamp getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Timestamp deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    /**
     * @return whether this is a defaults/template rule
     */
    public Boolean getTemplateRuleInd() {
        return templateRuleInd;
    }

    /**
     * @param templateRuleInd whether this is a defaults/template rule
     */
    public void setTemplateRuleInd(Boolean templateRuleInd) {
        this.templateRuleInd = templateRuleInd;
    }

    /**
     * Get the rule name
     * @return the rule name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the rule name
     * @param name the rule name
     */
    public void setName(String name) {
        this.name = name;
    }

	public List<PersonRuleResponsibility> getPersonResponsibilities() {
		return this.personResponsibilities;
	}

	public void setPersonResponsibilities(List<PersonRuleResponsibility> personResponsibilities) {
		this.personResponsibilities = personResponsibilities;
	}

	public List<GroupRuleResponsibility> getGroupResponsibilities() {
		return this.groupResponsibilities;
	}

	public void setGroupResponsibilities(List<GroupRuleResponsibility> groupResponsibilities) {
		this.groupResponsibilities = groupResponsibilities;
	}

	public List<RoleRuleResponsibility> getRoleResponsibilities() {
		return this.roleResponsibilities;
	}

	public void setRoleResponsibilities(List<RoleRuleResponsibility> roleResponsibilities) {
		this.roleResponsibilities = roleResponsibilities;
	}

	/**
	 * @return the fieldValues
	 */
	public Map<String, String> getFieldValues() {
		return this.fieldValues;
	}

	/**
	 * @param fieldValues the fieldValues to set
	 */
	public void setFieldValues(Map<String, String> fieldValues) {
		this.fieldValues = fieldValues;
	}

    public String getGroupReviewerName() {
        return this.groupReviewerName;
    }

    public String getGroupReviewerNamespace() {
        return this.groupReviewerNamespace;
    }

    public String getPersonReviewer() {
        return this.personReviewer;
    }

    public void setGroupReviewerName(String groupReviewerName) {
        this.groupReviewerName = groupReviewerName;
    }

    public void setGroupReviewerNamespace(String groupReviewerNamespace) {
        this.groupReviewerNamespace = groupReviewerNamespace;
    }

    public void setPersonReviewer(String personReviewer) {
        this.personReviewer = personReviewer;
    }

    public GroupBo getGroupBo() {
        GroupBo groupBo = null;
        if (StringUtils.isNotBlank(getGroupReviewerName())) {
            if ( groupBo == null ) {
                groupBo = GroupBo.from(KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                        getGroupReviewerNamespace(), getGroupReviewerName()));
            }
        }
        return groupBo;
    }

    public PersonImpl getPersonImpl() {
        return new PersonImpl();
    }

    public String getPersonReviewerType() {
        return this.personReviewerType;
    }

    public void setPersonReviewerType(String personReviewerType) {
        this.personReviewerType = personReviewerType;
    }

        /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static org.kuali.rice.kew.api.rule.Rule to(RuleBaseValues bo) {
        if (bo == null) {
            return null;
        }
        return org.kuali.rice.kew.api.rule.Rule.Builder.create(bo).build();
    }
}
