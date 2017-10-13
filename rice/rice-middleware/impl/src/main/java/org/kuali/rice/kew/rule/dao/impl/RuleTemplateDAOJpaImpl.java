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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.dao.RuleTemplateDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;



public class RuleTemplateDAOJpaImpl implements RuleTemplateDAO {

	@PersistenceContext(unitName="kew-unit")
	private EntityManager entityManager;
	
    public List<RuleTemplateBo> findAll() {
        return entityManager.createNamedQuery("findAllOrderedByName").getResultList();
    }

    public RuleTemplateBo findByRuleTemplateName(String ruleTemplateName) {
        if (StringUtils.isBlank(ruleTemplateName)) {
        	return null;
        }
    	
    	Criteria crit = new Criteria(RuleTemplateBo.class.getName());
        crit.eq("name", ruleTemplateName);
        crit.orderBy("ruleTemplateId", false);
        
        List ruleTemplates =  new QueryByCriteria(entityManager, crit).toQuery().getResultList();
        
        if(ruleTemplates==null||ruleTemplates.size()==0){
        	return null;
        }
        return (RuleTemplateBo) ruleTemplates.get(0);
    }

    public List<RuleTemplateBo> findByRuleTemplate(RuleTemplateBo ruleTemplate) {
        Criteria crit = new Criteria(RuleTemplateBo.class.getName());
        if (ruleTemplate.getName() != null) {
          crit.rawJpql("UPPER(RULE_TMPL_NM) like '"+ ruleTemplate.getName().toUpperCase() +"'");
        }
        if (ruleTemplate.getDescription() != null) {
          crit.rawJpql("UPPER(RULE_TMPL_DESC) like '"+ ruleTemplate.getDescription().toUpperCase()+"'");
        }
        return new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    public void delete(String ruleTemplateId) {
    	entityManager.remove(findByRuleTemplateId(ruleTemplateId));
    }

    public RuleTemplateBo findByRuleTemplateId(String ruleTemplateId) {
        return entityManager.find(RuleTemplateBo.class, ruleTemplateId);
     }

    public void save(RuleTemplateBo ruleTemplate) {
    	if(ruleTemplate.getId()==null){
    		entityManager.persist(ruleTemplate);
    	}else{
    		OrmUtils.merge(entityManager, ruleTemplate);
    	}
    }

    public String getNextRuleTemplateId() {
       return String.valueOf(getPlatform().getNextValSQL("KREW_RTE_TMPL_S", entityManager));
    }

    protected DatabasePlatform getPlatform() {
    	return (DatabasePlatform)GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
