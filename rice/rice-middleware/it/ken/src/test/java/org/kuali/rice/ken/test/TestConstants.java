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
package org.kuali.rice.ken.test;

import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.util.NotificationConstants;

/**
 * Constants reflecting test data that is used in tests.
 * If tests or test data is updated, this file need to be updated
 * to be kept in sync.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class TestConstants {
    /**
     * A primary key that won't exist in the database
     */
    public static final Long NON_EXISTENT_ID = new Long(-1);

    /**
     * Producers
     */
    public static final NotificationProducerBo PRODUCER_1 = new NotificationProducerBo();
    static {
        PRODUCER_1.setId(new Long(101));
        PRODUCER_1.setDescription("First Producer for Unit Tests");
        PRODUCER_1.setName("Test Producer #1");
        PRODUCER_1.setContactInfo("producer_1_and_2@127.0.0.1");
    }
    public static final NotificationProducerBo PRODUCER_2 = new NotificationProducerBo();
    static {
        PRODUCER_2.setId(new Long(102));
        PRODUCER_2.setName("Test Producer #2");
        PRODUCER_2.setDescription("Second Producer for Unit Tests");
        PRODUCER_2.setContactInfo("producer_1_and_2@127.0.0.1");
    }
    public static final NotificationProducerBo PRODUCER_3 = new NotificationProducerBo();
    static {
        PRODUCER_3.setId(new Long(103));
        PRODUCER_3.setName("Test Producer #3");
        PRODUCER_3.setDescription("Third Producer for Unit Tests");
        PRODUCER_3.setContactInfo("producer_3@127.0.0.1");
    }
    public static final NotificationProducerBo PRODUCER_4 = new NotificationProducerBo();
    static {
        PRODUCER_4.setId(new Long(104));
        PRODUCER_4.setName("Test Producer #4");
        PRODUCER_4.setDescription("Fourth Producer for Unit Tests");
        PRODUCER_4.setContactInfo("producer_4@127.0.0.1");
    }
    public static final NotificationProducerBo PRODUCER_5 = new NotificationProducerBo();
    static {
        PRODUCER_5.setId(new Long(105));
        PRODUCER_5.setName("Notification System");
        PRODUCER_5.setDescription("This producer represents messages sent from the general message sending form.");
        PRODUCER_5.setContactInfo("admins-notsys@127.0.0.1");
    }

    public static final int NUM_TEST_PRODUCERS = 5;
    public static final String CONTACT_INFO_2_PRODUCERS = "producer_1_and_2@127.0.0.1";
    public static final String CONTACT_INFO_1_PRODUCER = "producer_3@127.0.0.1";

    /**
     * Some valid channels
     */
    public static final String VALID_CHANNEL_ONE = "Test Channel #1";
    public static final Long VALID_CHANNEL_ONE_ID = new Long(101);
    public static final String VALID_CHANNEL_TWO = "Test Channel #2";
    public static final Long VALID_CHANNEL_TWO_ID = new Long(102);

    /**
     * Channel in test data:
     * PRODUCER_3 MUST be a valid producer for this channel
     * PRODUCER_4 MUST NOT be a valid producer for this channel
     */
    public static final Long CHANNEL_ID_1 = new Long(101);

    /**
     * A valid message delivery id in the test data
     */
    public static final Long VALID_MESSAGE_DELIVERY_ID = new Long(1);

    /**
     * Number of existing message deliveries in test data
     */
    public static final int NUM_OF_MSG_DELIVS_IN_TEST_DATA = 5;

    /**
     * A valid 'Email' message delivery id in the test data
     */
    public static final Long VALID_EMAIL_MESSAGE_DELIVERY_ID = new Long(8);

    /**
     * Id of message delivery which has been intentionally misconfigured in
     * test data to fail delivery (e.g. invalid delivery type)
     */
    public static final Long BAD_MESSAGE_DELIVERY_ID = new Long(5);

    /**
     * A valid deliverer name
     */
    public static final String VALID_DELIVERER_NAME = NotificationConstants.MESSAGE_DELIVERY_TYPES.KEW_ACTION_LIST_MESSAGE_DELIVERY_TYPE;

    /**
     * An invalid deliverer name
     */
    public static final String NON_EXISTENT_DELIVERER_NAME = "BOGUS_DELIVERER";

    /**
     * Some user names
     */
    public static final String TEST_USER_ONE = "testuser1";
    public static final String TEST_USER_ONE_DISPLAYNAME = "Test User 1";

    public static final String TEST_USER_TWO = "testuser2";
    public static final String TEST_USER_THREE = "testuser3";
    public static final String TEST_USER_FOUR = "testuser4";
    public static final String TEST_USER_FIVE = "testuser5";
    public static final String TEST_USER_SIX = "testuser6";

    public static final String ADMIN_USER_1 = "testadmin1";
    public static final String NON_ADMIN_USER_1 = TEST_USER_ONE;
    public static final String INVALID_USER_1 = "InvalidUser";

    /**
     * A user who is the target of a notification
     */
    public static final String NOTIFICATION_RECIPIENT_ID = "testuser5";
    /**
     * The content type of a notification for which the NOTIFICATION_RECIPIENT_ID user is a target
     * (can by any content type as long as the user is the target of at least one notification with that content type)
     */
    public static final String NOTIFICATION_RECIPIENT_CONTENT_TYPE = NotificationConstants.CONTENT_TYPES.SIMPLE_CONTENT_TYPE;

    /**
     * A valid group
     */
    public static final String VALID_GROUP_NAME_1 = "RiceTeam";

    /**
     * A valid KIM group name
     */
    public static final String VALID_KIM_GROUP_NAME_1 = "KR-WKFLW:WorkflowAdmin";

    /**
     * A valid KIM group name
     */
    public static final String VALID_KIM_GROUP_ID_1 = "1000464";

    /**
     * A valid KIM principal name
     */
    //nb: the corresponding id should be TEST_USER_5 (ie "testuser5")
    public static final String VALID_KIM_PRINCIPAL_NAME = "testuser5";

    /**
     * Number of members in this group
     */
    public static final int GROUP_1_MEMBERS = 6;

    /**
     *
     */
    public static final int KIM_GROUP_1_MEMBERS = 4;

    /**
     * An invalid content type
     */
    public static final String INVALID_CONTENT_TYPE = "Bad Type";

    /**
     * A valid notification id
     */
    public static final Long NOTIFICATION_1 = new Long(1);

    /**
     * Delivery type of the NOTIFICATION_1 notification
     */
    public static final String NOTIFICATION_1_DELIVERY_TYPE = NotificationConstants.DELIVERY_TYPES.FYI;
    /**
     * Number of message deliveries in the test data that are associated with notification #2
     */
    public static final int NUM_OF_MSG_DELIVS_FOR_NOTIF_1_TEST_USER_5 = 1;

    /**
     * Id of the message delivery in the test data for notification 1, test user 5, kew action list deliverer
     */
    public static final Long NOT_MSG_DELIV_NOTIF_1_TEST_USER_5_ACT_LST = new Long(1);

    /**
     * Id of the message delivery in the test data for notification 1, test user 5, email deliverer
     */
    public static final Long NOT_MSG_DELIV_NOTIF_1_TEST_USER_5 = new Long(1);

    /**
     * Email deliverer property
     */
    public static final String EMAIL_DELIVERER_PROPERTY_VALUE = "kuali-ken-testing@cornell.edu";
    
	private TestConstants() {
		throw new UnsupportedOperationException("do not call");
	}

}
