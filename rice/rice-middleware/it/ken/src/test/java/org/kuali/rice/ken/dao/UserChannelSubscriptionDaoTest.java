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
import org.kuali.rice.ken.bo.UserChannelSubscriptionBo;
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;


/**
 * This class test basic persistence for the UserChannelSubscription business object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UserChannelSubscriptionDaoTest extends BusinessObjectPersistenceTestCaseBase {
    NotificationChannelBo channel1 = MockObjectsUtil.getTestChannel1();
    NotificationChannelBo channel2 = MockObjectsUtil.getTestChannel2();
    
    UserChannelSubscriptionBo subscription1 = new UserChannelSubscriptionBo();
    UserChannelSubscriptionBo subscription2 = new UserChannelSubscriptionBo();
    
    private String[] userIds = {"ag266", "jaf30"};
    private String[] updatedUserIds = {"bh79", "arh14"};
    
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
	    businessObjectDao.delete(subscription1);
	    businessObjectDao.delete(subscription2);
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
	subscription1 = null;
	subscription2 = null;
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel1.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.USER_ID, userIds[0]);
	subscription1 = (UserChannelSubscriptionBo) businessObjectDao.findByUniqueKey(UserChannelSubscriptionBo.class, criteria);
	
	criteria.clear();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel2.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.USER_ID, userIds[1]);
	subscription2 = (UserChannelSubscriptionBo) businessObjectDao.findByUniqueKey(UserChannelSubscriptionBo.class, criteria);
	
	boolean success = true;
	
	success &= subscription1 != null;
	success &= subscription1.getId()>0;
	success &= subscription1.getChannel().getId().equals(channel1.getId());
	
	success &= subscription2 != null;
	success &= subscription2.getId()>0;
	success &= subscription2.getChannel().getId().equals(channel2.getId());
	
	return success;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
	subscription1.setChannel(channel1);
	subscription1.setUserId(userIds[0]);
	
	subscription2.setChannel(channel2);
	subscription2.setUserId(userIds[1]);
	
	try {
	    businessObjectDao.save(subscription1);
	    businessObjectDao.save(subscription2);
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
	subscription1.setUserId(updatedUserIds[0]);
	
	subscription2.setUserId(updatedUserIds[1]);
	
	try {
	    businessObjectDao.save(subscription1);
	    businessObjectDao.save(subscription2);
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
	subscription1 = new UserChannelSubscriptionBo();
	subscription2 = new UserChannelSubscriptionBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel1.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.USER_ID, updatedUserIds[0]);
	subscription1 = (UserChannelSubscriptionBo) businessObjectDao.findByUniqueKey(UserChannelSubscriptionBo.class, criteria);
	
	criteria.clear();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channel2.getId());
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.USER_ID, updatedUserIds[1]);
	subscription2 = (UserChannelSubscriptionBo) businessObjectDao.findByUniqueKey(UserChannelSubscriptionBo.class, criteria);
	
	boolean success = true;
	
	success &= subscription1 != null;
	success &= subscription1.getId()>0;
	success &= subscription1.getChannel().getId().equals(channel1.getId());
	success &= subscription1.getUserId().equals(updatedUserIds[0]);
	
	success &= subscription2 != null;
	success &= subscription2.getId()>0;
	success &= subscription2.getChannel().getId().equals(channel2.getId());
	success &= subscription2.getUserId().equals(updatedUserIds[1]);
	
	return success;
    }
}
