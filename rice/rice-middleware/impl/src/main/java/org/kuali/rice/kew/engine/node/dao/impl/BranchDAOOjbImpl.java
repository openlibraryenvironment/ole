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

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.dao.BranchDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;



public class BranchDAOOjbImpl extends PersistenceBrokerDaoSupport implements BranchDAO {
	
    
    public void save(Branch branch){
    	getPersistenceBrokerTemplate().store(branch);
    }
    
    public void deleteBranchStates(List statesToBeDeleted){
    	for(Iterator stateToBeDeletedIter=statesToBeDeleted.iterator();stateToBeDeletedIter.hasNext();){
    		Long stateId=(Long) stateToBeDeletedIter.next();
    		deleteBranchStatesById(stateId);    		
    	}
    }
    
    public void deleteBranchStatesById(Long stateId){
    	Criteria criteria = new Criteria();
        criteria.addEqualTo("stateId", stateId);
        BranchState branchState=(BranchState)getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(BranchState.class, criteria));
        getPersistenceBrokerTemplate().delete(branchState);
    }
    
}
