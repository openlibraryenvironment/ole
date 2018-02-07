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

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.api.rule.RuleTemplate;
import org.kuali.rice.kew.api.rule.RuleTemplateContract;
import org.kuali.rice.kew.rule.RuleTemplateOptionBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * A model bean which represents a template upon which a rule is created.
 * The RuleTemplate is essentially a collection of {@link RuleAttribute}s
 * (associated vai the {@link RuleTemplateAttributeBo} bean).
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_TMPL_T")
//@Sequence(name="KREW_RTE_TMPL_S", property="id")
@NamedQueries({@NamedQuery(name="findAllOrderedByName", query="SELECT rt FROM RuleTemplate rt ORDER BY rt.name ASC")})
public class RuleTemplateBo extends PersistableBusinessObjectBase implements RuleTemplateContract {

    private static final long serialVersionUID = -3387940485523951302L;

    /**
     * A list of default rule template option keys.
     */
    public static final String[] DEFAULT_OPTION_KEYS = {
        //KewApiConstants.RULE_INSTRUCTIONS_CD,
        KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ,
        KewApiConstants.ACTION_REQUEST_APPROVE_REQ,
        KewApiConstants.ACTION_REQUEST_COMPLETE_REQ,
        KewApiConstants.ACTION_REQUEST_FYI_REQ,
        KewApiConstants.ACTION_REQUEST_DEFAULT_CD
    };
    
    @Id
    @GeneratedValue(generator="KREW_RTE_TMPL_S")
	@GenericGenerator(name="KREW_RTE_TMPL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_TMPL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_TMPL_ID")
	private String id;
    @Column(name="NM")
	private String name;
    @Column(name="RULE_TMPL_DESC")
	private String description;

    @Column(name="DLGN_RULE_TMPL_ID", insertable=false, updatable=false)
	private String delegationTemplateId;
    @OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DLGN_RULE_TMPL_ID")
	private RuleTemplateBo delegationTemplate;
    @Fetch(value = FetchMode.SELECT)
    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
           mappedBy="ruleTemplate")
	private List<RuleTemplateAttributeBo> ruleTemplateAttributes;
    @Fetch(value = FetchMode.SELECT)
    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
           mappedBy="ruleTemplate", orphanRemoval=true)
	private List<RuleTemplateOptionBo> ruleTemplateOptions;

    // required to be lookupable
    @Transient
    private String returnUrl;

    public RuleTemplateBo() {
        ruleTemplateAttributes = new ArrayList<RuleTemplateAttributeBo>();
        ruleTemplateOptions = new ArrayList<RuleTemplateOptionBo>();
    }
    
 
    /**
     * Removes any non-default rule template options on the template
     */
    public void removeNonDefaultOptions() {
        Iterator<RuleTemplateOptionBo> it = ruleTemplateOptions.iterator();
        while (it.hasNext()) {
            RuleTemplateOptionBo option = it.next();
            // if it's not one of the default options, remove it
            if (!ArrayUtils.contains(DEFAULT_OPTION_KEYS, option.getCode())) {
                it.remove();
            }
        }
    }

    public String getDelegateTemplateName() {
        if (delegationTemplate != null) {
            return delegationTemplate.getName();
        }        
        return "";
    }

    public String getRuleTemplateActionsUrl() {
        return "<a href=\"RuleTemplate.do?methodToCall=report&currentRuleTemplateId=" + id + "\" >report</a>" /*+ "&nbsp;&nbsp;|&nbsp;&nbsp;<a href=\"RuleTemplate.do?methodToCall=edit&ruleTemplate.id=" + id + "\" >edit</a>"*/;
    }

    /**
     * Returns the rule template attribute on this instance whose name matches the name of the rule template attribute
     * passed as a parameter, qualified by it's active state, or null if a match was not found.
     */
    private RuleTemplateAttributeBo getRuleTemplateAttribute(RuleTemplateAttributeBo ruleTemplateAttribute, Boolean active) {
        for (RuleTemplateAttributeBo currentRuleTemplateAttribute: getRuleTemplateAttributes()) {
            if (currentRuleTemplateAttribute.getRuleAttribute().getName().equals(ruleTemplateAttribute.getRuleAttribute().getName())) {
                if (active == null) {
                    return currentRuleTemplateAttribute;
                }
                else if (active.compareTo(currentRuleTemplateAttribute.getActive()) == 0) {
                    return currentRuleTemplateAttribute;
                }
            }
        }
        return null;
    }
    
    public RuleTemplateAttributeBo getRuleTemplateAttribute(RuleTemplateAttributeBo ruleTemplateAttribute) {
        return getRuleTemplateAttribute(ruleTemplateAttribute, null);
    }
    
    public boolean containsActiveRuleTemplateAttribute(RuleTemplateAttributeBo templateAttribute) {
        return (getRuleTemplateAttribute(templateAttribute, Boolean.TRUE) != null);
    }

    public boolean containsRuleTemplateAttribute(RuleTemplateAttributeBo templateAttribute) {
        return (getRuleTemplateAttribute(templateAttribute, null) != null);
    }

    public RuleTemplateAttributeBo getRuleTemplateAttribute(int index) {
        while (getRuleTemplateAttributes().size() <= index) {
            getRuleTemplateAttributes().add(new RuleTemplateAttributeBo());
        }
        return (RuleTemplateAttributeBo) getRuleTemplateAttributes().get(index);
    }

    public List<RuleTemplateAttributeBo> getRuleTemplateAttributes() {
    	Collections.sort(ruleTemplateAttributes);
        return ruleTemplateAttributes;
    }

    /**
     * Returns a List of only the active RuleTemplateAttributes on the RuleTemplate
     * sorted according to display order (ascending).
     * @return
     */
    public List<RuleTemplateAttributeBo> getActiveRuleTemplateAttributes() {
        List<RuleTemplateAttributeBo> activeAttributes = new ArrayList<RuleTemplateAttributeBo>();
        for (RuleTemplateAttributeBo templateAttribute : getRuleTemplateAttributes())
        {
            if (templateAttribute.isActive())
            {
                activeAttributes.add(templateAttribute);
            }
        }
        Collections.sort(activeAttributes);
        return activeAttributes;
    }
    
    /**
     * This is implemented to allow us to use this collection on the inquiry for RuleTemplate.  In the
     * KNS code it does an explicit check that the property is writable.
     */
    public void setActiveRuleTemplateAttributes(List<RuleTemplateAttributeBo> ruleTemplateAttributes) {
    	throw new UnsupportedOperationException("setActiveRuleTemplateAttributes is not implemented");
    }

    public void setRuleTemplateAttributes(List<RuleTemplateAttributeBo> ruleTemplateAttributes) {
        this.ruleTemplateAttributes = ruleTemplateAttributes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDelegationTemplateId() {
        return delegationTemplateId;
    }

    public void setDelegationTemplateId(String delegationTemplateId) {
        this.delegationTemplateId = delegationTemplateId;
    }

    public RuleTemplateBo getDelegationTemplate() {
        return delegationTemplate;
    }

    public void setDelegationTemplate(RuleTemplateBo delegationTemplate) {
        this.delegationTemplate = delegationTemplate;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    /**
     * Used from the rule quicklinks when doing the focus channel.
     */
    public String getEncodedName() {
        return URLEncoder.encode(getName());
    }

    public List<RuleTemplateOptionBo> getRuleTemplateOptions() {
        return ruleTemplateOptions;
    }

    public void setRuleTemplateOptions(List<RuleTemplateOptionBo> ruleTemplateOptions) {
        this.ruleTemplateOptions = ruleTemplateOptions;
    }

    public RuleTemplateOptionBo getRuleTemplateOption(String key) {
        for (RuleTemplateOptionBo option: ruleTemplateOptions) {
            if (option.getCode().equals(key)) {
                return option;
            }
        }
        return null;
    }

    public void setAcknowledge(RuleTemplateOptionBo acknowledge) {
        RuleTemplateOptionBo option = getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ);
        option.setValue(acknowledge.getValue());
        option.setId(acknowledge.getId());
        option.setVersionNumber(acknowledge.getVersionNumber());
    }

    public void setComplete(RuleTemplateOptionBo complete) {
        RuleTemplateOptionBo option = getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
        option.setValue(complete.getValue());
        option.setId(complete.getId());
        option.setVersionNumber(complete.getVersionNumber());
    }

    public void setApprove(RuleTemplateOptionBo approve) {
        RuleTemplateOptionBo option = getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_APPROVE_REQ);
        option.setValue(approve.getValue());
        option.setId(approve.getId());
        option.setVersionNumber(approve.getVersionNumber());
    }

    public void setFyi(RuleTemplateOptionBo fyi) {
        RuleTemplateOptionBo option = getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_FYI_REQ);
        option.setValue(fyi.getValue());
        option.setId(fyi.getId());
        option.setVersionNumber(fyi.getVersionNumber());
    }

    public void setDefaultActionRequestValue(RuleTemplateOptionBo defaultActionRequestValue) {
        RuleTemplateOptionBo option = getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_DEFAULT_CD);
        option.setValue(defaultActionRequestValue.getValue());
        option.setId(defaultActionRequestValue.getId());
        option.setVersionNumber(defaultActionRequestValue.getVersionNumber());
    }

    public RuleTemplateOptionBo getAcknowledge() {
        return getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ);
    }

    public RuleTemplateOptionBo getComplete() {
        return getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
    }

    public RuleTemplateOptionBo getApprove() {
        return getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_APPROVE_REQ);
    }

    public RuleTemplateOptionBo getFyi() {
        return getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_FYI_REQ);
    }

    public RuleTemplateOptionBo getDefaultActionRequestValue() {
        return getRuleTemplateOption(KewApiConstants.ACTION_REQUEST_DEFAULT_CD);
    }

    /**
     * Returns a List of Roles from all RoleAttributes attached to this template.
     *
     * @return list of roles
     */
    public List<RoleName> getRoles() {
        List<RoleName> roleNames = new ArrayList<RoleName>();
        List<RuleTemplateAttributeBo> templateAttributes = getRuleTemplateAttributes();
        for (RuleTemplateAttributeBo templateAttribute : templateAttributes) {
            if (!templateAttribute.isWorkflowAttribute())
            {
				continue;
			}
            roleNames.addAll(KEWServiceLocator.getWorkflowRuleAttributeMediator().getRoleNames(templateAttribute));
        }
        return roleNames;
    }

    public static RuleTemplate to(RuleTemplateBo bo) {
        if (bo == null) {
            return null;
        }
        return RuleTemplate.Builder.create(bo).build();
    }
}
