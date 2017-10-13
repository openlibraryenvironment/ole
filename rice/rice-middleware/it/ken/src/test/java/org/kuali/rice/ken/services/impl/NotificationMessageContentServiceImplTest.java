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
package org.kuali.rice.ken.services.impl;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationRecipientBo;
import org.kuali.rice.ken.bo.NotificationResponseBo;
import org.kuali.rice.ken.bo.NotificationSenderBo;
import org.kuali.rice.ken.service.NotificationMessageContentService;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.ken.test.TestConstants;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests NotificationMessageContentService
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.CLEAR_DB)
public class NotificationMessageContentServiceImplTest extends KENTestCase {
    private static final String SAMPLE_EVENT_MESSAGE = "sample_message_event_type.xml";
    private static final String SAMPLE_SIMPLE_MESSAGE = "sample_message_simple_type.xml";
    private static final String SAMPLE_MALFORMED_EVENT_MESSAGE = "sample_malformed_message_event_type.xml";
    private static final String SAMPLE_MALFORMED_SIMPLE_MESSAGE = "sample_malformed_message_simple_type.xml";
    private static final String SAMPLE_BADNAMESPACE_EVENT_MESSAGE = "badnamespace_message_event_type.xml";
    private static final String SAMPLE_CHANNEL = TestConstants.VALID_CHANNEL_ONE;
    private static final String VALID_CHANNEL = TestConstants.VALID_CHANNEL_TWO;
    private static final String VALID_TYPE = NotificationConstants.DELIVERY_TYPES.FYI;
    private static final String VALID_CONTENT = "<content xmlns=\"ns:notification/ContentTypeSimple\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                                                " xsi:schemaLocation=\"ns:notification/ContentTypeSimple resource:notification/ContentTypeSimple\">\n" +
                                                "    <message>Holiday-Ho-Out Starts Next Week - 11/20/2006!</message>\n" +
                                                "</content>";
    
    private static final String sampleEdlFile = "NotificationDocumentContent.xml";

    public NotificationMessageContentServiceImplTest() {
        //setDefaultRollback(false);
    }

    private void testParseNotificationRequestMessage(String samplePath) throws Exception {
        NotificationMessageContentService impl = services.getNotificationMessageContentService();
        InputStream is = this.getClass().getResourceAsStream(samplePath);
        System.out.println(is);
        NotificationBo notification = impl.parseNotificationRequestMessage(is);
        assertEquals(SAMPLE_CHANNEL, notification.getChannel().getName());
        System.out.println(notification.getSenders());
        System.out.println("notification id: " + notification.getId());
        List<NotificationSenderBo> sl = notification.getSenders();
        assertTrue(sl.size() > 0);
        for (NotificationSenderBo s :sl) {
            assertNotNull(s);
            assertNotNull(s.getSenderName());
        }
        List<NotificationRecipientBo> rl = notification.getRecipients();
        assertTrue(rl.size() > 0);
        for (NotificationRecipientBo r : rl) {
            assertNotNull(r);
            assertNotNull(r.getRecipientId());
        }
        //fail("Not yet implemented");

        notification.setCreationDateTimeValue(new Timestamp(System.currentTimeMillis()));
        services.getGenericDao().save(notification);
        //setComplete();
    }

    @Test
    public void testParseEventNotificationRequestMessage() throws Exception {
        testParseNotificationRequestMessage(SAMPLE_EVENT_MESSAGE);
    }

    @Test
    public void testParseSimpleNotificationRequestMessage() throws Exception {
        testParseNotificationRequestMessage(SAMPLE_SIMPLE_MESSAGE);
    }
    @Test
    public void testParseMalformedEventNotificationRequestMessage() throws Exception {
        try {
            testParseNotificationRequestMessage(SAMPLE_MALFORMED_EVENT_MESSAGE);
            fail("malformed event message passed validation");
        } catch (XmlException ixe) {
            // expected
            return;
        }
    }
    @Test
    public void testParseBadNamespaceEventNotificationRequestMessage() throws Exception {
        try {
            testParseNotificationRequestMessage(SAMPLE_BADNAMESPACE_EVENT_MESSAGE);
            fail("malformed event message passed validation");
        } catch (XmlException ixe) {
            // expected
            return;
        }
    }
    @Test
    public void testParseMalformedSimpleNotificationRequestMessage() throws Exception {
        try {
            testParseNotificationRequestMessage(SAMPLE_MALFORMED_SIMPLE_MESSAGE);
            fail("malformed simple message passed validation");
        } catch (XmlException ixe) {
            // expected
        }
    }

    @Test
    public void testGenerateNotificationResponseMessage() throws Exception {
	NotificationResponseBo response = new NotificationResponseBo();
	response.setStatus("PASS");
	response.setMessage("Here is your response");
	NotificationMessageContentService impl = services.getNotificationMessageContentService();
	String xml = impl.generateNotificationResponseMessage(response);
	assertTrue(xml.length() == 89);
    }

    @Test
    public void testGenerateNotificationMessage() throws Exception {
	NotificationMessageContentService impl = services.getNotificationMessageContentService();
        InputStream is = this.getClass().getResourceAsStream(SAMPLE_SIMPLE_MESSAGE);
        System.out.println(is);
        NotificationBo notification = impl.parseNotificationRequestMessage(is);
        String XML = impl.generateNotificationMessage(notification);
        assertTrue(XML.length()>0);
    }

    @Test
    public void testParseSerializedNotificationXml() throws Exception {
	InputStream is = this.getClass().getResourceAsStream(sampleEdlFile);
	
	byte[] bytes = IOUtils.toByteArray(is);
	
	NotificationMessageContentService impl = services.getNotificationMessageContentService();
	
        NotificationBo notification = impl.parseSerializedNotificationXml(bytes);
        
        assertNotNull(notification);
        assertEquals(VALID_CHANNEL, notification.getChannel().getName());
        
        assertEquals(VALID_TYPE, notification.getDeliveryType());
        
        assertEquals(VALID_CONTENT.replaceAll("\\s+", " "), notification.getContent().replaceAll("\\s+", " "));
    }
}
