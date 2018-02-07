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

import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.bo.NotificationRecipientListBo;
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;


/**
 * This class test basic persistence for the NotificationRecipientList business object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationRecipientListDaoTest extends BusinessObjectPersistenceTestCaseBase {
    NotificationChannelBo channel1 = MockObjectsUtil.getTestChannel1();
    NotificationChannelBo channel2 = MockObjectsUtil.getTestChannel2();
    
    NotificationRecipientListBo recipientList1 = new NotificationRecipientListBo();
    NotificationRecipientListBo recipientList2 = new NotificationRecipientListBo();
    
    private String[] recipientTypes = {"Type 1", "Type 2"};
    private String[] recipientIds = {"ag266", "jaf30"};
    private String[] updatedRecipientIds = {"bh79", "arh14"};
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#setup()
     */
    @Override
    protected void setup() {
	businessObjectDao.save(channel1);
	businessObjectDao.save(channel2);
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Override
    protected boolean delete() {
	try {
	    businessObjectDao.delete(recipientList1);
	    businessObjectDao.delete(recipientList2);
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
	recipientList1 = new NotificationRecipientListBo();
	recipientList2 = new NotificationRecipientListBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel1.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_TYPE, recipientTypes[0]);
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_ID, recipientIds[0]);
	recipientList1 = (NotificationRecipientListBo) (businessObjectDao.findMatching(NotificationRecipientListBo.class, criteria)).iterator().next();
	
	criteria.clear();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel2.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_TYPE, recipientTypes[1]);
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_ID, recipientIds[1]);
	recipientList2 = (NotificationRecipientListBo) (businessObjectDao.findMatching(NotificationRecipientListBo.class, criteria)).iterator().next();
	
	boolean success = true;
	
	success &= recipientList1 != null;
	success &= recipientList1.getRecipientId().equals(recipientIds[0]);
	
	success &= recipientList2 != null;
	success &= recipientList2.getRecipientId().equals(recipientIds[1]);
	
	return success;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
	recipientList1.setChannel(channel1);
	recipientList1.setRecipientType(recipientTypes[0]);
	recipientList1.setRecipientId(recipientIds[0]);
	
	recipientList2.setChannel(channel2);
	recipientList2.setRecipientType(recipientTypes[1]);
	recipientList2.setRecipientId(recipientIds[1]);
	
	try {
	    businessObjectDao.save(recipientList1);
	    businessObjectDao.save(recipientList2);
	} catch(Exception e) {
        LOG.error("Error saving recipients", e);
	    return false;
	}
	return true;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#update()
     */
    @Override
    protected boolean update() {
	recipientList1.setRecipientId(updatedRecipientIds[0]);
	
	recipientList2.setRecipientId(updatedRecipientIds[1]);
	
	try {
	    businessObjectDao.save(recipientList1);
	    businessObjectDao.save(recipientList2);
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
	recipientList1 = new NotificationRecipientListBo();
	recipientList2 = new NotificationRecipientListBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel1.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_TYPE, recipientTypes[0]);
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_ID, updatedRecipientIds[0]);
	recipientList1 = (NotificationRecipientListBo) (businessObjectDao.findMatching(NotificationRecipientListBo.class, criteria)).iterator().next();
	
	criteria.clear();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel2.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_TYPE, recipientTypes[1]);
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENT_ID, updatedRecipientIds[1]);
	recipientList2 = (NotificationRecipientListBo) (businessObjectDao.findMatching(NotificationRecipientListBo.class, criteria)).iterator().next();
	
	boolean success = true;
	
	success &= recipientList1.getRecipientId().equals(updatedRecipientIds[0]);
	success &= recipientList2.getRecipientId().equals(updatedRecipientIds[1]);
	    
	return success;
    }
}
