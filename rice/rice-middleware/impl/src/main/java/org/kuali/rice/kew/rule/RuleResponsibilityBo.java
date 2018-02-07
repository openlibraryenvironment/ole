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
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.api.rule.RuleResponsibilityContract;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.user.RoleRecipient;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;


/**
 * A model bean representing the responsibility of a user, workgroup, or role
 * to perform some action on a document.  Used by the rule system to
 * identify the appropriate responsibile parties to generate
 * {@link ActionRequestValue}s to.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_RSP_T")
//@Sequence(name="KREW_RSP_S", property="id")
public class RuleResponsibilityBo extends PersistableBusinessObjectBase implements RuleResponsibilityContract {

	private static final long serialVersionUID = -1565688857123316797L;
	@Id
	@GeneratedValue(generator="KREW_RSP_S")
	@GenericGenerator(name="KREW_RSP_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RSP_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_RSP_ID")
	private String id;
    @Column(name="RSP_ID")
	private String responsibilityId;
    @Column(name="RULE_ID", insertable=false, updatable=false)
    private String ruleBaseValuesId;
    @Column(name="ACTN_RQST_CD")
	private String actionRequestedCd;
    @Column(name="NM")
	private String ruleResponsibilityName;
    @Column(name="TYP")
	private String ruleResponsibilityType;
    @Column(name="PRIO")
	private Integer priority;
    @Column(name="APPR_PLCY")
	private String approvePolicy;

    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RULE_ID")
	private RuleBaseValues ruleBaseValues;
    //@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
    //        mappedBy="ruleResponsibility")
    //private List<RuleDelegation> delegationRules = new ArrayList<RuleDelegation>();

    public Principal getPrincipal()
    {
    	if (isUsingPrincipal()) {
    		return KEWServiceLocator.getIdentityHelperService().getPrincipal(ruleResponsibilityName);
    	}
    	return null;
    }

    public Group getGroup() {
        if (isUsingGroup()) {
        	return KimApiServiceLocator.getGroupService().getGroup(ruleResponsibilityName);
        }
        return null;
    }

    public String getRole() {
        if (isUsingRole()) {
            return ruleResponsibilityName;
        }
        return null;
    }

    public String getResolvedRoleName() {
        if (isUsingRole()) {
            return getRole().substring(getRole().indexOf("!") + 1, getRole().length());
        }
        return null;
    }

    public String getRoleAttributeName() {
	    return getRole().substring(0, getRole().indexOf("!"));
    }
    
    public RoleAttribute resolveRoleAttribute() {
        if (isUsingRole()) {
            String attributeName = getRoleAttributeName();
            return (RoleAttribute) GlobalResourceLoader.getResourceLoader().getObject(new ObjectDefinition(attributeName));
        }
        return null;
    }

    @Override
    public boolean isUsingRole() {
    	return (ruleResponsibilityName != null && ruleResponsibilityType != null && ruleResponsibilityType.equals(KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID));
    }

    @Override
    public boolean isUsingPrincipal() {
    	return (ruleResponsibilityName != null && !ruleResponsibilityName.trim().equals("") && ruleResponsibilityType != null && ruleResponsibilityType.equals(KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID));
    }

    @Override
    public boolean isUsingGroup() {
    	return (ruleResponsibilityName != null && !ruleResponsibilityName.trim().equals("") && ruleResponsibilityType != null && ruleResponsibilityType.equals(KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID));
    }

    public String getRuleBaseValuesId() {
        return ruleBaseValuesId;
    }

    public void setRuleBaseValuesId(String ruleBaseValuesId) {
        this.ruleBaseValuesId = ruleBaseValuesId;
    }

    public RuleBaseValues getRuleBaseValues() {
        return ruleBaseValues;
    }

    public void setRuleBaseValues(RuleBaseValues ruleBaseValues) {
        this.ruleBaseValues = ruleBaseValues;
    }

    public String getActionRequestedCd() {
        return actionRequestedCd;
    }

    public void setActionRequestedCd(String actionRequestedCd) {
        this.actionRequestedCd = actionRequestedCd;
    }

    public String getId() {
        return id;
    }

    public void setId(String ruleResponsibilityId) {
        this.id = ruleResponsibilityId;
    }
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getApprovePolicy() {
        return approvePolicy;
    }

    public void setApprovePolicy(String approvePolicy) {
        this.approvePolicy = approvePolicy;
    }

    public Object copy(boolean preserveKeys) {
        RuleResponsibilityBo ruleResponsibilityClone = new RuleResponsibilityBo();
        ruleResponsibilityClone.setApprovePolicy(getApprovePolicy());
        if (actionRequestedCd != null) {
            ruleResponsibilityClone.setActionRequestedCd(actionRequestedCd);
        }
        if (id != null && preserveKeys) {
            ruleResponsibilityClone.setId(id);
        }

        if (responsibilityId != null) {
            ruleResponsibilityClone.setResponsibilityId(responsibilityId);
        }

        if (ruleResponsibilityName != null) {
            ruleResponsibilityClone.setRuleResponsibilityName(ruleResponsibilityName);
        }
        if (ruleResponsibilityType != null) {
            ruleResponsibilityClone.setRuleResponsibilityType(ruleResponsibilityType);
        }
        if (priority != null) {
            ruleResponsibilityClone.setPriority(priority);
        }
//        if (delegationRules != null) {
//            for (Iterator iter = delegationRules.iterator(); iter.hasNext();) {
//                RuleDelegation delegation = (RuleDelegation) iter.next();
//                RuleDelegation delegationClone = (RuleDelegation)delegation.copy(preserveKeys);
//                delegationClone.setRuleResponsibility(ruleResponsibilityClone);
//                ruleResponsibilityClone.getDelegationRules().add(delegationClone);
//
//            }
//        }
        return ruleResponsibilityClone;
    }

    public String getRuleResponsibilityName() {
        return ruleResponsibilityName;
    }

    public void setRuleResponsibilityName(String ruleResponsibilityName) {
        this.ruleResponsibilityName = ruleResponsibilityName;
    }

    public String getRuleResponsibilityType() {
        return ruleResponsibilityType;
    }

    public void setRuleResponsibilityType(String ruleResponsibilityType) {
        this.ruleResponsibilityType = ruleResponsibilityType;
    }

    public String getResponsibilityId() {
        return responsibilityId;
    }
    public void setResponsibilityId(String responsibilityId) {
        this.responsibilityId = responsibilityId;
    }
    
    public List<RuleDelegationBo> getDelegationRules() {
    	return KEWServiceLocator.getRuleDelegationService().findByResponsibilityId(getResponsibilityId());
    }
    
    public RuleDelegationBo getDelegationRule(int index) {
    	return getDelegationRules().get(index);
    }
    
//    public boolean isDelegating() {
//        return !getDelegationRules().isEmpty();
//    }
//
//    public List getDelegationRules() {
//        return delegationRules;
//    }
//    public void setDelegationRules(List delegationRules) {
//        this.delegationRules = delegationRules;
//    }
//
//    public RuleDelegation getDelegationRule(int index) {
//        while (getDelegationRules().size() <= index) {
//            RuleDelegation ruleDelegation = new RuleDelegation();
//            ruleDelegation.setRuleResponsibility(this);
//            ruleDelegation.setDelegationRuleBaseValues(new RuleBaseValues());
//            getDelegationRules().add(ruleDelegation);
//        }
//        return (RuleDelegation) getDelegationRules().get(index);
//    }
    
    // convenience methods for the web-tier
    
    public String getActionRequestedDisplayValue() {
    	return KewApiConstants.ACTION_REQUEST_CODES.get(getActionRequestedCd());
    }
    
    public String getRuleResponsibilityTypeDisplayValue() {
    	return KewApiConstants.RULE_RESPONSIBILITY_TYPES.get(getRuleResponsibilityType());
    }
    
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof RuleResponsibilityBo)) return false;
        RuleResponsibilityBo pred = (RuleResponsibilityBo) o;
        return ObjectUtils.equals(ruleResponsibilityName, pred.getRuleResponsibilityName()) &&
               ObjectUtils.equals(actionRequestedCd, pred.getActionRequestedCd()) &&
               ObjectUtils.equals(priority, pred.getPriority()) &&
               ObjectUtils.equals(approvePolicy, pred.getApprovePolicy());
    }
    
    /**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		.append(this.actionRequestedCd)
		.append(this.approvePolicy)
		.append(this.priority)
		.append(this.ruleResponsibilityName).toHashCode();
	}

    @Override
    public String getGroupId() {
        if (!isUsingGroup()) {
            return null;
        }
        return getGroup().getId();
    }

    @Override
    public String getPrincipalId() {
        if (getPrincipal() == null) {
            return null;
        }
        return getPrincipal().getPrincipalId();
    }

    @Override
    public String getRoleName() {
        return getRole();
    }

    /**
     * Convenience method to return the Recipient for this RuleResponsibility
     * @return the Recipient for this RuleResponsibility
     */
    public Recipient getRecipient() {
        if (isUsingPrincipal()) {
            return new KimPrincipalRecipient(getPrincipal());
        } else if (isUsingGroup()) {
            return new KimGroupRecipient(getGroup());
        } else if (isUsingRole()) {
            return new RoleRecipient(getRole());
        } else {
            return null;
        }
    }

    public static org.kuali.rice.kew.api.rule.RuleResponsibility to(RuleResponsibilityBo bo) {
        if (bo == null) {
            return null;
        }
        return org.kuali.rice.kew.api.rule.RuleResponsibility.Builder.create(bo).build();
        /*org.kuali.rice.kew.api.rule.RuleResponsibility.Builder builder = org.kuali.rice.kew.api.rule.RuleResponsibility.Builder.create();
        builder.setPriority(bo.getPriority());
        builder.setResponsibilityId(bo.getResponsibilityId());
        builder.setActionRequestedCd(bo.getActionRequestedCd());
        builder.setApprovePolicy(bo.getApprovePolicy());
        builder.setPrincipalId(bo.getPrincipal() == null ? null : bo.getPrincipal().getPrincipalId());
        builder.setGroupId(bo.getGroup() == null ? null : bo.getGroup().getId());
        builder.setRoleName(bo.getResolvedRoleName());
        if (CollectionUtils.isNotEmpty(bo.getDelegationRules())) {
            List<org.kuali.rice.kew.api.rule.RuleDelegation.Builder> delegationRuleBuilders =
                    new ArrayList<org.kuali.rice.kew.api.rule.RuleDelegation.Builder>();
            for (RuleDelegation delegationRule : bo.getDelegationRules()) {
                delegationRuleBuilders.add(
                        org.kuali.rice.kew.api.rule.RuleDelegation.Builder.create(RuleDelegation.to(delegationRule)));
            }
            builder.setDelegationRules(delegationRuleBuilders);
        } else {
            builder.setDelegationRules(Collections.<org.kuali.rice.kew.api.rule.RuleDelegation.Builder>emptyList());
        }
        return builder.build();*/
    }
}
