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

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.dao.RuleAttributeDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;


public class RuleAttributeDAOOjbImpl extends PersistenceBrokerDaoSupport implements RuleAttributeDAO {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleAttributeDAOOjbImpl.class);

	public void save(RuleAttribute ruleAttribute) {
		this.getPersistenceBrokerTemplate().store(ruleAttribute);
	}

	public void delete(String ruleAttributeId) {
		this.getPersistenceBrokerTemplate().delete(findByRuleAttributeId(ruleAttributeId));
	}

	public RuleAttribute findByRuleAttributeId(String ruleAttributeId) {
		RuleAttribute ruleAttribute = new RuleAttribute();
		ruleAttribute.setId(ruleAttributeId);
		return (RuleAttribute) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ruleAttribute));
	}

	public List<RuleAttribute> findByRuleAttribute(RuleAttribute ruleAttribute) {
		Criteria crit = new Criteria();
		if (ruleAttribute.getName() != null) {
			crit.addSql("UPPER(RULE_ATTRIB_NM) like '" + ruleAttribute.getName().toUpperCase() + "'");
		}
		if (ruleAttribute.getResourceDescriptor() != null) {
			crit.addSql("UPPER(RULE_ATTRIB_CLS_NM) like '" + ruleAttribute.getResourceDescriptor().toUpperCase() + "'");
		}
		if (ruleAttribute.getType() != null) {
			crit.addSql("UPPER(RULE_ATTRIB_TYP) like '" + ruleAttribute.getType().toUpperCase() + "'");
		}
		return (List<RuleAttribute>) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleAttribute.class, crit));

	}

	public List<RuleAttribute> getAllRuleAttributes() {
		return (List<RuleAttribute>) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleAttribute.class));
	}

	public RuleAttribute findByName(String name) {
		LOG.debug("findByName name=" + name);
		Criteria crit = new Criteria();
		crit.addEqualTo("name", name);
		return (RuleAttribute) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(RuleAttribute.class, crit));
	}

	public List<RuleAttribute> findByClassName(String classname) {
		Criteria crit = new Criteria();
		crit.addEqualTo("resourceDescriptor", classname);
		return (List<RuleAttribute>) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleAttribute.class, crit));
	}

}
