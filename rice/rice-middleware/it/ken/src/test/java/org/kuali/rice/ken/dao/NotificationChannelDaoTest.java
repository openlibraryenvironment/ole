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
import org.kuali.rice.ken.bo.NotificationChannelReviewerBo;
import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.kim.api.KimConstants.KimGroupMemberTypes;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class test basic persistence for the NotificationChannel business object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.CLEAR_DB) // this test can't run in a transaction because of how it is using ojb
public class NotificationChannelDaoTest extends BusinessObjectPersistenceTestCaseBase {
    NotificationChannelBo channel1 = MockObjectsUtil.getTestChannel1();
    NotificationChannelBo channel2 = MockObjectsUtil.getTestChannel2();

    NotificationProducerBo mockProducer1 = MockObjectsUtil.getTestProducer1();

    private String[] updatedDescriptions = {"Test 1 - updated description", "Test 2 - updated description"};
    private boolean[] updatedSubscribables = {false, true};

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#setup()
     */
    @Override
    protected void setup() {
        super.setup();
        businessObjectDao.save(mockProducer1);
    }

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Override
    protected boolean delete() {
        NotificationChannelBo mockChannel1 = MockObjectsUtil.getTestChannel1();
        NotificationChannelBo mockChannel2 = MockObjectsUtil.getTestChannel2();

        channel1 = new NotificationChannelBo();
        channel2 = new NotificationChannelBo();

        HashMap criteria = new HashMap();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockChannel1.getName());
        channel1 = (NotificationChannelBo) (businessObjectDao.findMatching(NotificationChannelBo.class, criteria)).iterator().next();

        criteria.clear();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockChannel2.getName());
        channel2 = (NotificationChannelBo) (businessObjectDao.findMatching(NotificationChannelBo.class, criteria)).iterator().next();

        try {
            businessObjectDao.delete(channel1);
            businessObjectDao.delete(channel2);
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
        NotificationChannelBo mockChannel1 = MockObjectsUtil.getTestChannel1();
        NotificationChannelBo mockChannel2 = MockObjectsUtil.getTestChannel2();

        channel1 = new NotificationChannelBo();
        channel2 = new NotificationChannelBo();

        HashMap criteria = new HashMap();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockChannel1.getName());
        channel1 = (NotificationChannelBo) (businessObjectDao.findMatching(NotificationChannelBo.class, criteria)).iterator().next();

        criteria.clear();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockChannel2.getName());
        channel2 = (NotificationChannelBo) (businessObjectDao.findMatching(NotificationChannelBo.class, criteria)).iterator().next();

        boolean success = true;

        success &= channel1 != null;
        success &= channel1.getDescription().equals(mockChannel1.getDescription());
        success &= (channel1.isSubscribable()==mockChannel1.isSubscribable());
        success &= channel1.getProducers().size() == 1;

        success &= channel2 != null;
        success &= channel2.getDescription().equals(mockChannel2.getDescription());
        success &= (channel2.isSubscribable()==mockChannel2.isSubscribable());
        success &= channel2.getProducers().size() == 1;

        return success;
    }

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
        // add in a notification channel producer join object
        try {
            channel1.getProducers().add(mockProducer1);
            businessObjectDao.save(channel1);

            // reload for collections
            mockProducer1 = (NotificationProducerBo) businessObjectDao.findById(NotificationProducerBo.class, mockProducer1.getId());

            channel2.getProducers().add(mockProducer1);
            businessObjectDao.save(channel2);
            assertEquals(1, channel2.getProducers().size());

            mockProducer1 = (NotificationProducerBo) businessObjectDao.findById(NotificationProducerBo.class, mockProducer1.getId());
            assertEquals(2, mockProducer1.getChannels().size());

            channel2 = (NotificationChannelBo) businessObjectDao.findById(NotificationChannelBo.class, channel2.getId());
            assertEquals(1, channel2.getProducers().size());
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
        try {

            channel2 = (NotificationChannelBo) businessObjectDao.findById(NotificationChannelBo.class, channel2.getId());
            assertEquals(1, channel2.getProducers().size());

            channel1.setDescription(updatedDescriptions[0]);
            channel1.setSubscribable(updatedSubscribables[0]);
            channel1.getProducers().clear();

            businessObjectDao.save(channel1);

            mockProducer1 = (NotificationProducerBo) businessObjectDao.findById(NotificationProducerBo.class, mockProducer1.getId());
            assertNotNull(mockProducer1);
            assertEquals(1, mockProducer1.getChannels().size());

            channel2 = (NotificationChannelBo) businessObjectDao.findById(NotificationChannelBo.class, channel2.getId());
            assertEquals(1, channel2.getProducers().size());

            channel2.setDescription(updatedDescriptions[1]);
            channel2.setSubscribable(updatedSubscribables[1]);
            NotificationChannelReviewerBo reviewer = MockObjectsUtil.buildTestNotificationChannelReviewer(KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE, "aReviewer");
            reviewer.setChannel(channel2);
            channel2.getReviewers().add(reviewer);

            businessObjectDao.save(channel2);

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
        //retrieve fresh again
        NotificationChannelBo mockChannel1 = MockObjectsUtil.getTestChannel1();
        NotificationChannelBo mockChannel2 = MockObjectsUtil.getTestChannel2();

        channel1 = new NotificationChannelBo();
        channel2 = new NotificationChannelBo();

        HashMap criteria = new HashMap();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockChannel1.getName());
        channel1 = (NotificationChannelBo) (businessObjectDao.findMatching(NotificationChannelBo.class, criteria)).iterator().next();

        criteria.clear();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.NAME, mockChannel2.getName());
        channel2 = (NotificationChannelBo) (businessObjectDao.findMatching(NotificationChannelBo.class, criteria)).iterator().next();

        boolean success = true;

        success &= channel1.getDescription().equals(updatedDescriptions[0]);
        success &= (channel1.isSubscribable()==updatedSubscribables[0]);
        success &= channel1.getProducers().size() == 0;

        success &= channel2.getDescription().equals(updatedDescriptions[1]);
        success &= (channel2.isSubscribable()==updatedSubscribables[1]);
        success &= channel2.getProducers().size() == 1;
        success &= channel2.getReviewers().size() == 1;

        return success;
    }
}
