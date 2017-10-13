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

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.dao.RuleTemplateDAO;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RuleTemplateDAOOjbImpl extends PersistenceBrokerDaoSupport implements RuleTemplateDAO {


    public List<RuleTemplateBo> findAll() {
        QueryByCriteria query = new QueryByCriteria(RuleTemplateBo.class);
        query.addOrderByAscending("name");
        return (List)this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    public RuleTemplateBo findByRuleTemplateName(String ruleTemplateName) {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", ruleTemplateName);
        QueryByCriteria query = new QueryByCriteria(RuleTemplateBo.class, crit);
        query.addOrderByDescending("id");

        Iterator ruleTemplates = this.getPersistenceBrokerTemplate().getCollectionByQuery(query).iterator();
        while(ruleTemplates.hasNext()) {
            return (RuleTemplateBo) ruleTemplates.next();
        }
        return null;
    }

    public List<RuleTemplateBo> findByRuleTemplate(RuleTemplateBo ruleTemplate) {
        Criteria crit = new Criteria();
        if (ruleTemplate.getName() != null) {
          crit.addSql("UPPER(RULE_TMPL_NM) like '"+ ruleTemplate.getName().toUpperCase() +"'");
        }
        if (ruleTemplate.getDescription() != null) {
          crit.addSql("UPPER(RULE_TMPL_DESC) like '"+ ruleTemplate.getDescription().toUpperCase()+"'");
        }
        return new ArrayList<RuleTemplateBo>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleTemplateBo.class, crit)));
    }

    public void delete(String ruleTemplateId) {
    	this.getPersistenceBrokerTemplate().delete(findByRuleTemplateId(ruleTemplateId));
    }

    public RuleTemplateBo findByRuleTemplateId(String ruleTemplateId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("id", ruleTemplateId);
        return (RuleTemplateBo) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(RuleTemplateBo.class, crit));
    }

    public void save(RuleTemplateBo ruleTemplate) {
    	this.getPersistenceBrokerTemplate().store(ruleTemplate);
    }

    public String getNextRuleTemplateId() {
        return (String)this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
            	  return String.valueOf(getPlatform().getNextValSQL("KREW_RTE_TMPL_S", broker));
            }
        });
    }

    protected DatabasePlatform getPlatform() {
    	return (DatabasePlatform)GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
    }


}
