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
package org.kuali.rice.kew.engine.node.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.dao.BranchDAO;

/**
 * This is a description of what this class does - Garey don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class BranchDAOJpaImpl implements BranchDAO {

	 @PersistenceContext(unitName="kew-unit")
	 private EntityManager entityManager;
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.engine.node.dao.BranchDAO#deleteBranchStates(java.util.List)
	 */
	public void deleteBranchStates(List<Long> statesToBeDeleted) {
		for(Long stateId : statesToBeDeleted){
			this.deleteBranchStatesById(stateId);
		}

	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.engine.node.dao.BranchDAO#save(org.kuali.rice.kew.engine.node.Branch)
	 */
	public void save(Branch branch) {
		  if (branch.getBranchId() == null) {
	            entityManager.persist(branch);
	        } else {
	            OrmUtils.merge(entityManager, branch);
	        }

	}
	
	 protected void deleteBranchStatesById(Long stateId){
	    	Criteria criteria = new Criteria("BranchState", "branchState");
	    	criteria.eq("stateId", stateId);
	        BranchState branchState = (BranchState)new QueryByCriteria(entityManager, criteria).toQuery().getSingleResult();
	        entityManager.remove(branchState);
	    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
