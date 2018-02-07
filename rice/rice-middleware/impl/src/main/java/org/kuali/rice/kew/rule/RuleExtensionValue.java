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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.service.KEWServiceLocator;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The value of an extension to a rule.  Essentially contains a
 * key-value pair containing the key of the extension data and
 * it's value.
 * 
 * @see RuleBaseValues
 * @see RuleExtensionBo
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_EXT_VAL_T")
//@Sequence(name="KREW_RTE_TMPL_S", property="ruleExtensionValueId")
public class RuleExtensionValue implements Serializable {

	private static final long serialVersionUID = 8909789087052290261L;
	@Id
	@GeneratedValue(generator="KREW_RTE_TMPL_S")
	@GenericGenerator(name="KREW_RTE_TMPL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_TMPL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_EXT_VAL_ID")
	private String ruleExtensionValueId;
    @Column(name="RULE_EXT_ID", insertable=false, updatable=false)
	private String ruleExtensionId;
    @Column(name="VAL")
	private String value;
    @Column(name="KEY_CD")
	private String key;
    @Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;
    
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="RULE_EXT_ID")
	private RuleExtensionBo extension;
    
    public RuleExtensionValue() {
    }
    
    public RuleExtensionValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    //@PrePersist
    public void beforeInsert(){
        OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
    }
    
    public RuleExtensionBo getExtension() {
        return extension;
    }
    public void setExtension(RuleExtensionBo extension) {
        this.extension = extension;
    }
    public Integer getLockVerNbr() {
        return lockVerNbr;
    }
    public void setLockVerNbr(Integer lockVerNbr) {
        this.lockVerNbr = lockVerNbr;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getRuleExtensionId() {
        return ruleExtensionId;
    }
    public void setRuleExtensionId(String ruleExtensionId) {
        this.ruleExtensionId = ruleExtensionId;
    }
    public String getRuleExtensionValueId() {
        return ruleExtensionValueId;
    }
    public void setRuleExtensionValueId(String ruleExtensionValueId) {
        this.ruleExtensionValueId = ruleExtensionValueId;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof RuleExtensionValue)) return false;
        RuleExtensionValue pred = (RuleExtensionValue) o;
        return ObjectUtils.equals(key, pred.key) && ObjectUtils.equals(value, pred.value);
    }

    public String toString() {
        return "[RuleExtensionValue:"
               +  " ruleExtensionValueId=" + ruleExtensionValueId
               + ", ruleExtensionId=" + ruleExtensionId
               + ", value=" + value
               + ", key=" + key
               + ", lockVerNbr=" + lockVerNbr
               + "]";
            
    }
}
