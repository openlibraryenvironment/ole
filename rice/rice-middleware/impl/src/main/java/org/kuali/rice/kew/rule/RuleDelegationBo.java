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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.api.rule.RuleDelegationContract;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * A model bean representing the delegation of a rule from a responsibility to
 * another rule.  Specifies the delegation type which can be either
 * {@link {@link DelegationType#PRIMARY} or {@link DelegationType#SECONDARY}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_DLGN_RSP_T")
//@Sequence(name="KREW_RTE_TMPL_S", property="ruleDelegationId")
public class RuleDelegationBo extends PersistableBusinessObjectBase implements RuleDelegationContract {

	private static final long serialVersionUID = 7989203310473741293L;
	@Id
	@GeneratedValue(generator="KREW_RTE_TMPL_S")
	@GenericGenerator(name="KREW_RTE_TMPL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_TMPL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="DLGN_RULE_ID")
	private String ruleDelegationId;
    @Column(name="RSP_ID")
	private String responsibilityId;
    @Column(name="DLGN_RULE_BASE_VAL_ID", insertable=false, updatable=false)
	private String delegateRuleId;
    @Column(name="DLGN_TYP")
    private String delegationTypeCode = DelegationType.PRIMARY.getCode();

    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="DLGN_RULE_BASE_VAL_ID")
	private RuleBaseValues delegationRule;
//    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
//	@JoinColumn(name="RULE_RSP_ID")
//	private RuleResponsibility ruleResponsibility;

    @Transient
    private String groupReviewerName;
    @Transient
    private String groupReviewerNamespace;
    @Transient
    private String personReviewer;
    @Transient
    private String personReviewerType;

    public RuleDelegationBo() {
    }

    public Object copy(boolean preserveKeys) {
        RuleDelegationBo clone = new RuleDelegationBo();
        if (ruleDelegationId != null && preserveKeys) {
            clone.setRuleDelegationId(ruleDelegationId);
        }
        clone.setDelegationRule(delegationRule);
        clone.setDelegateRuleId(delegationRule.getId());
        if (delegationTypeCode != null) {
            clone.setDelegationType(DelegationType.fromCode(delegationTypeCode));
        }
        return clone;
    }

    public String getDelegateRuleId() {
        return delegateRuleId;
    }
    public void setDelegateRuleId(String delegateRuleId) {
        this.delegateRuleId = delegateRuleId;
    }

    @Override
    public RuleBaseValues getDelegationRule() {
        return delegationRule;
    }

    public RuleBaseValues getDelegationRuleBaseValues() {
        return delegationRule;
    }

    public void setDelegationRuleBaseValues(RuleBaseValues delegationRuleBaseValues) {
        this.delegationRule = delegationRuleBaseValues;
    }

    public void setDelegationRule(RuleBaseValues delegationRule) {
        this.delegationRule = delegationRule;
    }

    /**
     * Setter for type code preserved for DD
     * @param delegationTypeCode the DelegationType code
     */
    public void setDelegationTypeCode(String delegationTypeCode) {
        DelegationType.fromCode(delegationTypeCode);
        this.delegationTypeCode = delegationTypeCode;
    }

    /**
     * Getter for type code preserved for DD
     * @return the DelegationType code
     */
    public String getDelegationTypeCode() {
        return delegationTypeCode;
    }

    @Override
    public DelegationType getDelegationType() {
        return DelegationType.fromCode(delegationTypeCode);
    }
    public void setDelegationType(DelegationType delegationType) {
        this.delegationTypeCode = delegationType.getCode();
    }
    public String getRuleDelegationId() {
        return ruleDelegationId;
    }
    public void setRuleDelegationId(String ruleDelegationId) {
        this.ruleDelegationId = ruleDelegationId;
    }

    /**
     * Returns the most recent RuleResponsibility for the responsibility
     * id on this RuleDelegation.
     */
    public RuleResponsibilityBo getRuleResponsibility() {
    	if ( getResponsibilityId() == null ) {
    		return null;
    	}
    	return KEWServiceLocator.getRuleService().findRuleResponsibility(getResponsibilityId());
    }

    public DocumentType getDocumentType() {
        return this.getDelegationRule().getDocumentType();
    }

    public String getResponsibilityId() {
        return responsibilityId;
    }
    public void setResponsibilityId(String ruleResponsibilityId) {
        this.responsibilityId = ruleResponsibilityId;
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

    public String getPersonReviewerType() {
        return this.personReviewerType;
    }

    public void setPersonReviewerType(String personReviewerType) {
        this.personReviewerType = personReviewerType;
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

        /**
       * An override of the refresh() method that properly preserves the RuleBaseValues instance. If the delegationRuleBaseValues property
       * becomes null as a result of the refresh() method on the PersistableBusinessObjectBase superclass, an attempt is made to retrieve
       * it by calling refreshReferenceObject() for the property. If that also fails, then the RuleBaseValues instance that was in-place
       * prior to the refresh() superclass call will be used as the delegationRuleBaseValues property's value. This override is necessary
       * in order to prevent certain exceptions during the cancellation of a rule delegation maintenance document.
       *
       * @see org.kuali.rice.krad.bo.PersistableBusinessObjectBase#refresh()
       * @see org.kuali.rice.krad.bo.PersistableBusinessObjectBase#refreshReferenceObject(java.lang.String)
       */
	@Override
	public void refresh() {
		RuleBaseValues oldRuleBaseValues = this.getDelegationRule();
		super.refresh();
		if (this.getDelegationRule() == null) {
			this.refreshReferenceObject("delegationRuleBaseValues");
			if (this.getDelegationRule() == null) {
				this.setDelegationRule(oldRuleBaseValues);
			}
		}
	}

    public static org.kuali.rice.kew.api.rule.RuleDelegation to(RuleDelegationBo bo) {
        if (bo == null) {
            return null;
        }
        return org.kuali.rice.kew.api.rule.RuleDelegation.Builder.create(bo).build();
        /*org.kuali.rice.kew.api.rule.RuleDelegation.Builder builder = org.kuali.rice.kew.api.rule.RuleDelegation.Builder.create();
        builder.setDelegationType(bo.getDelegationType());
        builder.setDelegationRule(org.kuali.rice.kew.api.rule.Rule.Builder.create(RuleBaseValues.to(
                bo.getDelegationRule())));
        return builder.build();*/
    }
}

