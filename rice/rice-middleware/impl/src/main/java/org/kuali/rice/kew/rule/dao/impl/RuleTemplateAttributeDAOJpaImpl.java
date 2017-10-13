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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.rule.dao.RuleTemplateAttributeDAO;

public class RuleTemplateAttributeDAOJpaImpl implements RuleTemplateAttributeDAO {

	@PersistenceContext(unitName = "kew-unit")
	private EntityManager entityManager;

	public RuleTemplateAttributeBo findByRuleTemplateAttributeId(
			String ruleTemplateAttributeId) {
		return entityManager.find(RuleTemplateAttributeBo.class,
				ruleTemplateAttributeId);
	}

	public void save(RuleTemplateAttributeBo ruleTemplateAttribute) {
		if(ruleTemplateAttribute.getId()==null){
			entityManager.persist(ruleTemplateAttribute);
		}else{
			OrmUtils.merge(entityManager, ruleTemplateAttribute);
		}
	}

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
