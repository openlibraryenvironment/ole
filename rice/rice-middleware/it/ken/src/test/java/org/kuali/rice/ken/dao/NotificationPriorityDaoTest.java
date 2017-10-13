/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.ken.dao;

import java.util.HashMap;

import org.kuali.rice.ken.bo.NotificationPriorityBo;
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;


/**
 * This class test basic persistence for the NotificationPriority business object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationPriorityDaoTest extends BusinessObjectPersistenceTestCaseBase {
    NotificationPriorityBo mockPriority1 = MockObjectsUtil.getTestPriority1();
    NotificationPriorityBo priority1 = MockObjectsUtil.getTestPriority1();
    
    private String[] updatedDescriptions = {"Test 1 - updated description", "Test 2 - updated description"};
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Override
    protected boolean delete() {
	try {
	    businessObjectDao.delete(priority1);
	} catch(Exception e) {
	    return false;
	}
	return true;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#retrieve()
     */
    @Override
    protected boolean retrieve() {
	priority1 = new NotificationPriorityBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockPriority1.getName());
	priority1 = (NotificationPriorityBo) (businessObjectDao.findMatching(NotificationPriorityBo.class, criteria)).iterator().next();
	
	boolean success = true;
	
	success &= priority1 != null;
	success &= priority1.getDescription().equals(mockPriority1.getDescription());
	
	return success;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
	try {
	    businessObjectDao.save(priority1);
	} catch(Exception e) {
	    return false;
	}
	return true;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#update()
     */
    @Override
    protected boolean update() {
	priority1.setDescription(updatedDescriptions[0]);
	
	try {
	    businessObjectDao.save(priority1);
	} catch(Exception e) {
	    return false;
	}
	return true;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#validateChanges()
     */
    @Override
    protected boolean validateChanges() {
	retrieve();  //retrieve fresh again
	
	boolean success = true;
	
	success &= priority1.getDescription().equals(updatedDescriptions[0]);
	    
	return success;
    }
}
