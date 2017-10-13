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

import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;


/**
 * This class test basic persistence for the NotificationContentType business object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationContentTypeDaoTest extends BusinessObjectPersistenceTestCaseBase {
    NotificationContentTypeBo mockType1 = MockObjectsUtil.getTestContentType1();
    NotificationContentTypeBo type1 = MockObjectsUtil.getTestContentType1();
    
    private String[] updatedDescriptions = {"Test 1 - updated description", "Test 2 - updated description"};
    private String[] namespaces = {"https://forge.cornell.edu/notif_sys/test1", "https://forge.cornell.edu/notif_sys/test1"};
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Override
    protected boolean delete() {
	try {
	    businessObjectDao.delete(type1);
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
	type1 = new NotificationContentTypeBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockType1.getName());
	type1 = (NotificationContentTypeBo) (businessObjectDao.findMatching(NotificationContentTypeBo.class, criteria)).iterator().next();
	
	criteria.clear();
	
	boolean success = true;
	
	success &= type1 != null;
	success &= type1.getDescription().equals(mockType1.getDescription());
	success &= type1.getXsd().equals(mockType1.getXsd());
	success &= type1.getXsl().equals(mockType1.getXsl());
	
	return success;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
	try {
	    businessObjectDao.save(type1);
	} catch(Exception e) {
        LOG.error("Error saving NotificationContentType", e);
	    return false;
	}
	return true;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#update()
     */
    @Override
    protected boolean update() {
	type1.setDescription(updatedDescriptions[0]);
	
	try {
	    businessObjectDao.save(type1);
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
	
	success &= type1.getDescription().equals(updatedDescriptions[0]);
	    
	return success;
    }
}
