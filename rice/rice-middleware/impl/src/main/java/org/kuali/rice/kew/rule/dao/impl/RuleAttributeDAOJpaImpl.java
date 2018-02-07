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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.dao.RuleAttributeDAO;


public class RuleAttributeDAOJpaImpl implements RuleAttributeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleAttributeDAOJpaImpl.class);
   
    /**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(RuleAttribute ruleAttribute) {
        if (ruleAttribute.getId() == null) {
            entityManager.persist(ruleAttribute);
        } else {
            OrmUtils.merge(entityManager, ruleAttribute);
        }
    }

    public void delete(String ruleAttributeId) {
        entityManager.remove(findByRuleAttributeId(ruleAttributeId));
    }

    public RuleAttribute findByRuleAttributeId(String ruleAttributeId) {
        return (RuleAttribute) entityManager.createNamedQuery("RuleAttribute.FindById").setParameter("ruleAttributeId", ruleAttributeId).getSingleResult();
    }

    public List<RuleAttribute> findByRuleAttribute(RuleAttribute ruleAttribute) {
        Criteria crit = new Criteria("RuleAttribute", "ra");

        if (ruleAttribute.getName() != null) {
            crit.rawJpql("UPPER(RULE_ATTRIB_NM) like '" + ruleAttribute.getName().toUpperCase() + "'");
        }

        if (ruleAttribute.getResourceDescriptor() != null) {
            crit.rawJpql("UPPER(RULE_ATTRIB_CLS_NM) like '" + ruleAttribute.getResourceDescriptor().toUpperCase() + "'");
        }
        if (ruleAttribute.getType() != null) {
            crit.rawJpql("UPPER(RULE_ATTRIB_TYP) like '" + ruleAttribute.getType().toUpperCase() + "'");
        }
        return new QueryByCriteria(entityManager, crit).toQuery().getResultList();

    }

    public List<RuleAttribute> getAllRuleAttributes() {
        return  entityManager.createNamedQuery("RuleAttribute.GetAllRuleAttributes").getResultList();
    }

    public RuleAttribute findByName(String name) {
        LOG.debug("findByName name=" + name);
        return (RuleAttribute) entityManager.createNamedQuery("RuleAttribute.FindByName").setParameter("name", name).getSingleResult();
    }

    public List<RuleAttribute> findByClassName(String classname) {
        LOG.debug("findByClassName classname=" + classname);

        List<RuleAttribute> ruleAttributes = entityManager.createNamedQuery("RuleAttribute.FindByClassName").setParameter("resourceDescriptor", classname).getResultList();

        return ruleAttributes;
    }

}
