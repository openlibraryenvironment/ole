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
import org.kuali.rice.kew.rule.RuleTemplateOptionBo;
import org.kuali.rice.kew.rule.dao.RuleTemplateOptionDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;


public class RuleTemplateOptionDAOOjbImpl extends PersistenceBrokerDaoSupport implements RuleTemplateOptionDAO {

  
  /*
   * (non-Javadoc)
   * @see org.kuali.rice.kew.rule.dao.RuleTemplateOptionDAO#delete(java.lang.Long)
   */
  public void delete(String ruleTemplateOptionId) {
	  this.getPersistenceBrokerTemplate().delete(findByRuleTemplateOptionId(ruleTemplateOptionId));
  }

  /*
   * (non-Javadoc)
   * @see org.kuali.rice.kew.rule.dao.RuleTemplateOptionDAO#findByRuleTemplateOptionId(java.lang.Long)
   */
  public RuleTemplateOptionBo findByRuleTemplateOptionId(String ruleTemplateOptionId) {
  	RuleTemplateOptionBo ruleTemplateOption = new RuleTemplateOptionBo();
    ruleTemplateOption.setId(ruleTemplateOptionId);
    return (RuleTemplateOptionBo) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ruleTemplateOption));
  }
  
  public void save (RuleTemplateOptionBo ruleTemplateOption){
	  this.getPersistenceBrokerTemplate().store(ruleTemplateOption);
  }
}
