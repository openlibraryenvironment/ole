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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.dao.RuleDelegationDAO;


public class RuleDelegationDAOJpaImpl implements RuleDelegationDAO {

	@PersistenceContext(unitName="kew-unit")
	private EntityManager entityManager;

    public List<RuleDelegationBo> findByDelegateRuleId(String ruleId) {
        Criteria crit = new Criteria(RuleDelegationBo.class.getName());
        crit.eq("delegateRuleId", ruleId);
        return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    public void save(RuleDelegationBo ruleDelegation) {
    	if(ruleDelegation.getRuleDelegationId()==null){
    		entityManager.persist(ruleDelegation);
    	}else{
    		OrmUtils.merge(entityManager, ruleDelegation);
    	}
    }
    public List<RuleDelegationBo> findAllCurrentRuleDelegations(){
        Criteria crit = new Criteria(RuleDelegationBo.class.getName());
        crit.eq("delegationRuleBaseValues.currentInd", true);
        return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    public RuleDelegationBo findByRuleDelegationId(String ruleDelegationId){
        return entityManager.find(RuleDelegationBo.class, ruleDelegationId);

    }
    public void delete(String ruleDelegationId){
    	entityManager.remove(findByRuleDelegationId(ruleDelegationId));
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<RuleDelegationBo> findByResponsibilityIdWithCurrentRule(String responsibilityId) {
    	Criteria crit = new Criteria(RuleDelegationBo.class.getName());
    	crit.eq("responsibilityId", responsibilityId);
    	crit.eq("delegationRuleBaseValues.currentInd", true);
    	Collection delegations = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    	return new ArrayList<RuleDelegationBo>(delegations);
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.kew.rule.dao.RuleDelegationDAO#search(java.lang.String, java.lang.Long, java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.util.Map, java.lang.String)
     */
    public List<RuleDelegationBo> search(String parentRuleBaseVaueId, String parentResponsibilityId, String docTypeName, String ruleId,
            String ruleTemplateId, String ruleDescription, String workgroupId,
            String workflowId, String delegationType, Boolean activeInd,
            Map extensionValues, String workflowIdDirective) {
        // TODO jjhanso - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.kew.rule.dao.RuleDelegationDAO#search(java.lang.String, java.lang.Long, java.lang.String, java.util.Collection, java.lang.String, java.lang.String, java.lang.Boolean, java.util.Map, java.util.Collection)
     */
    public List<RuleDelegationBo> search(String parentRuleBaseVaueId, String parentResponsibilityId, String docTypeName, String ruleTemplateId,
            String ruleDescription, Collection<String> workgroupIds,
            String workflowId, String delegationType, Boolean activeInd,
            Map extensionValues, Collection actionRequestCodes) {
        // TODO jjhanso - THIS METHOD NEEDS JAVADOCS
        return null;
    }

}
