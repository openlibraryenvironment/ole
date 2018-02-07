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
package org.kuali.rice.kew.rule.dao.impl;

import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.rule.dao.RuleTemplateAttributeDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;


public class RuleTemplateAttributeDAOOjbImpl extends PersistenceBrokerDaoSupport implements RuleTemplateAttributeDAO {

 
  /*
   * (non-Javadoc)
   * @see org.kuali.rice.kew.rule.dao.RuleTemplateAttributeDAO#delete(java.lang.Long)
   */
//  public void delete(Long ruleTemplateAttributeId) {
//        this.getPersistenceBrokerTemplate().delete(findByRuleTemplateAttributeId(ruleTemplateAttributeId));
//  }

  /*
   * (non-Javadoc)
   * @see org.kuali.rice.kew.rule.dao.RuleTemplateAttributeDAO#findByRuleTemplateAttributeId(java.lang.Long)
   */
  public RuleTemplateAttributeBo findByRuleTemplateAttributeId(String ruleTemplateAttributeId) {
    RuleTemplateAttributeBo ruleTemplateAttribute = new RuleTemplateAttributeBo();
    ruleTemplateAttribute.setId(ruleTemplateAttributeId);
    ruleTemplateAttribute.setRequired(null);
    return (RuleTemplateAttributeBo) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ruleTemplateAttribute));
  }
  
  public void save (RuleTemplateAttributeBo ruleTemplateAttribute){
      this.getPersistenceBrokerTemplate().store(ruleTemplateAttribute);
  }
}
