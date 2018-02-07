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

import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;


/**
 * This class tests basic persistence for the NotificationProducer business object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationProducerDaoTest extends BusinessObjectPersistenceTestCaseBase {
    NotificationChannelBo mockChannel1 = MockObjectsUtil.getTestChannel1();
    NotificationChannelBo mockChannel2 = MockObjectsUtil.getTestChannel2();
    
    NotificationProducerBo mockProducer1 = MockObjectsUtil.getTestProducer1();
    
    private String[] updatedDescriptions = {"Test 1 - updated description", "Test 2 - updated description"};
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#setup()
     */
    @Override
    protected void setup() {
	super.setup();
	businessObjectDao.save(mockChannel1);
	businessObjectDao.save(mockChannel2);
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Override
    protected boolean delete() {
	HashMap criteria = new HashMap();
	
	NotificationProducerBo producer4 = new NotificationProducerBo();
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockProducer1.getName());
	producer4 = (NotificationProducerBo) businessObjectDao.findByUniqueKey(NotificationProducerBo.class, criteria);
	
	assertEquals(1, producer4.getChannels().size());
	
	criteria.clear();
	NotificationProducerBo producer5 = new NotificationProducerBo();
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockProducer1.getName());
	producer5 = (NotificationProducerBo) businessObjectDao.findByUniqueKey(NotificationProducerBo.class, criteria);
		
	try {
	    businessObjectDao.delete(producer5);
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
	NotificationProducerBo producer2 = new NotificationProducerBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockProducer1.getName());
	producer2 = (NotificationProducerBo) businessObjectDao.findByUniqueKey(NotificationProducerBo.class, criteria);
	
	boolean success = true;
	
	success &= producer2 != null;
	success &= producer2.getDescription().equals(mockProducer1.getDescription());
	success &= producer2.getChannels().size()==2;
	
	return success;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
	NotificationProducerBo producer1 = MockObjectsUtil.getTestProducer1();
	
	//set up the channels
	producer1.getChannels().add(mockChannel1);
	producer1.getChannels().add(mockChannel2);
	
	try {
	    businessObjectDao.save(producer1);
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
	NotificationProducerBo producer2 = new NotificationProducerBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockProducer1.getName());
	producer2 = (NotificationProducerBo) businessObjectDao.findByUniqueKey(NotificationProducerBo.class, criteria);
	
	producer2.setDescription(updatedDescriptions[0]);
	producer2.getChannels().remove(0);
	
	try {
	    businessObjectDao.save(producer2);
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
	NotificationProducerBo producer2 = new NotificationProducerBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockProducer1.getName());
	producer2 = (NotificationProducerBo) businessObjectDao.findByUniqueKey(NotificationProducerBo.class, criteria);
	
	boolean success = true;
	
	success &= producer2.getDescription().equals(updatedDescriptions[0]);
	success &= producer2.getChannels().size()==1;
	
	return success;
    }
}
