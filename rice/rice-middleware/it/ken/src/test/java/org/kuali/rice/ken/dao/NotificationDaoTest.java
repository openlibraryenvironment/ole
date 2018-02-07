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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.bo.NotificationPriorityBo;
import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.bo.NotificationRecipientBo;
import org.kuali.rice.ken.bo.NotificationSenderBo;
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;

/**
 * This class test basic persistence for the Notification business object.  In addition, 
 * it also tests basic persistence for the NotificationSender and NotificationRecipient bos 
 * since those bos are mostly persisted and retrieved through the parent Notification 
 * instance.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationDaoTest extends BusinessObjectPersistenceTestCaseBase {
    Long id = new Long(-1);
    NotificationPriorityBo mockPriority = MockObjectsUtil.getTestPriority1();
    NotificationContentTypeBo mockContentType = MockObjectsUtil.getTestContentType1();
    NotificationChannelBo mockChannel = MockObjectsUtil.getTestChannel1();
    NotificationProducerBo mockProducer = MockObjectsUtil.getTestProducer1();
    
    NotificationBo notification = new NotificationBo();

    private String deliveryType = NotificationConstants.DELIVERY_TYPES.ACK;
    private Timestamp sendDateTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
    private Timestamp autoRemoveDateTime = null;
    private String content = "Notification Content!";

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#setup()
     */
    @Override
    protected void setup() {
	super.setup();
	businessObjectDao.save(mockPriority);
	businessObjectDao.save(mockContentType);
	businessObjectDao.save(mockChannel);
	businessObjectDao.save(mockProducer);
    }

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Override
    protected boolean delete() {
	try {
	    businessObjectDao.delete(notification);
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
	notification = new NotificationBo();
	
	HashMap criteria = new HashMap();
	
	criteria.put(NotificationConstants.BO_PROPERTY_NAMES.ID, id);
	notification = (NotificationBo) businessObjectDao.findByPrimaryKey(NotificationBo.class, criteria);
	
	boolean success = true;
	
	success &= notification != null;
	success &= notification.getContentType().getName().equals(MockObjectsUtil.getTestContentType1().getName());
	success &= notification.getRecipients().size()==2;
	success &= notification.getSenders().size()==2;
        success &= notification.getCreationDateTime()!=null;
	success &= notification.getAutoRemoveDateTime()==null;
	success &= notification.getDeliveryType().equals(NotificationConstants.DELIVERY_TYPES.ACK);
	success &= notification.getProcessingFlag().equals(NotificationConstants.PROCESSING_FLAGS.UNRESOLVED);
	
	return success;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
	List<NotificationRecipientBo> recipients = new ArrayList();
	recipients.add(MockObjectsUtil.getTestRecipient1());
	recipients.add(MockObjectsUtil.getTestRecipient2());
	
	List<NotificationSenderBo> senders = new ArrayList();
	senders.add(MockObjectsUtil.getTestSender1());
	senders.add(MockObjectsUtil.getTestSender2());
	
	notification = MockObjectsUtil.buildTestNotification(deliveryType, sendDateTime, autoRemoveDateTime, mockContentType, 
		content, mockPriority, mockProducer, mockChannel, recipients, senders);
	try {
	    businessObjectDao.save(notification);
	    id = new Long(notification.getId());
	} catch(Exception e) {
        LOG.error("Error saving notification", e);
	    return false;
	}
	return true;
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#update()
     */
    @Override
    protected boolean update() {
	notification.setDeliveryType(NotificationConstants.DELIVERY_TYPES.FYI);
	
	try {
	    businessObjectDao.save(notification);
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
	
	success &= notification.getDeliveryType().equals(NotificationConstants.DELIVERY_TYPES.FYI);
	    
	return success;
    }
}
