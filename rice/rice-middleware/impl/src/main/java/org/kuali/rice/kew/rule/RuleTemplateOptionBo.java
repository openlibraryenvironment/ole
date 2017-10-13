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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.rule.RuleTemplateOptionContract;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.bo.BusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Defines default values and other preset information for a {@link RuleBaseValues} 
 * which is based off of the associated {@link org.kuali.rice.kew.rule.bo.RuleTemplateBo}.
 * 
 * @see RuleBaseValues
 * @see org.kuali.rice.kew.rule.bo.RuleTemplateBo
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_TMPL_OPTN_T")
//@Sequence(name="KREW_RULE_TMPL_OPTN_S", property="id")
public class RuleTemplateOptionBo extends BusinessObjectBase implements RuleTemplateOptionContract {

	private static final long serialVersionUID = 8913119135197149224L;
	@Id
	@GeneratedValue(generator="KREW_RULE_TMPL_OPTN_S")
	@GenericGenerator(name="KREW_RULE_TMPL_OPTN_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RULE_TMPL_OPTN_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_TMPL_OPTN_ID")
	private String id;
    @Column(name="RULE_TMPL_ID", insertable=false, updatable=false)
	private String ruleTemplateId;
    @Column(name="KEY_CD")
	private String code;
    @Column(name="VAL")
	private String value;
    @Version
	@Column(name="VER_NBR")
	private Long versionNumber;

    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RULE_TMPL_ID")
	private RuleTemplateBo ruleTemplate;
    
    public RuleTemplateOptionBo(){}
    
    public RuleTemplateOptionBo(String key, String value){
        this.code = key;
        this.value = value;
    }

    //@PrePersist
    public void beforeInsert(){
        OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
    }
    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public RuleTemplateBo getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplateBo ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }
    @Override
    public String getRuleTemplateId() {
        return ruleTemplateId;
    }

    public void setRuleTemplateId(String ruleTemplateId) {
        this.ruleTemplateId = ruleTemplateId;
    }
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void refresh() {
        KRADServiceLocator.getPersistenceService().retrieveNonKeyFields(this);
    }
}

