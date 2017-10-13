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
import org.kuali.rice.ken.test.util.MockObjectsUtil;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.kim.api.KimConstants.KimGroupMemberTypes;

import java.util.HashMap;

/**
 * This class tests basic persistence for the {@link org.kuali.rice.ken.bo.NotificationChannelReviewerBo} business object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationChannelReviewerDaoTest extends BusinessObjectPersistenceTestCaseBase {
    private NotificationChannelBo mockChannel1 = MockObjectsUtil.getTestChannel1();
    private NotificationChannelReviewerBo mockReviewer = MockObjectsUtil.buildTestNotificationChannelReviewer(KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE, "aReviewer");

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#setup()
     */
    @Override
    protected void setup() {
        super.setup();
        businessObjectDao.save(mockChannel1);
    }

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Override
    protected boolean delete() {
        HashMap criteria = new HashMap();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.REVIEWER_ID, mockReviewer.getReviewerId());
        NotificationChannelReviewerBo
                reviewer = (NotificationChannelReviewerBo) businessObjectDao.findByUniqueKey(NotificationChannelReviewerBo.class, criteria);

        try {
            businessObjectDao.delete(reviewer);
        } catch(Exception e) {
            return false;
        }
        return true;
    }


    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Override
    protected boolean insert() {
        mockReviewer.setChannel(mockChannel1);

        try {
            businessObjectDao.save(mockReviewer);
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
        HashMap criteria = new HashMap();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.REVIEWER_ID, mockReviewer.getReviewerId());
        NotificationChannelReviewerBo
                reviewer = (NotificationChannelReviewerBo) businessObjectDao.findByUniqueKey(NotificationChannelReviewerBo.class, criteria);

        boolean success = true;

        success &= reviewer != null;
        success &= reviewer.getReviewerId().equals(mockReviewer.getReviewerId());
        success &= reviewer.getReviewerType().equals(mockReviewer.getReviewerType());
        success &= reviewer.getChannel() != null;
        success &= reviewer.getChannel().getId().equals(mockChannel1.getId());

        return success;
    }

    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#update()
     */
    @Override
    protected boolean update() {
        HashMap criteria = new HashMap();

        criteria.put(NotificationConstants.BO_PROPERTY_NAMES.REVIEWER_ID, mockReviewer.getReviewerId());
        NotificationChannelReviewerBo
                reviewer = (NotificationChannelReviewerBo) businessObjectDao.findByUniqueKey(NotificationChannelReviewerBo.class, criteria);

        reviewer.setReviewerId("updatedReviewerId");
        reviewer.setReviewerType(KimGroupMemberTypes.GROUP_MEMBER_TYPE.getCode());

        try {
            businessObjectDao.save(reviewer);
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
        NotificationChannelReviewerBo
                reviewer = (NotificationChannelReviewerBo) businessObjectDao.findById(NotificationChannelReviewerBo.class, mockReviewer.getId());

        boolean success = reviewer != null;
        success &= reviewer.getReviewerId().equals("updatedReviewerId");
        success &= reviewer.getReviewerType().equals(KimGroupMemberTypes.GROUP_MEMBER_TYPE.getCode());

        return success;
    }
}
