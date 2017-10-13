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
package org.kuali.rice.kcb.service.impl;

import org.junit.Test;
import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.service.GlobalKCBServiceLocator;
import org.kuali.rice.kcb.service.MessageService;
import org.kuali.rice.kcb.test.BusinessObjectTestCase;
import org.kuali.rice.kcb.test.KCBTestData;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;

import static org.junit.Assert.*;


/**
 * Tests MessageService 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MessageServiceTest extends BusinessObjectTestCase {
    private MessageService messageService;
    private Message MESSAGE;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
    
        messageService = GlobalKCBServiceLocator.getInstance().getMessageService();
        MESSAGE = KCBTestData.getMessage1();
        messageService.saveMessage(MESSAGE);
    }

    @Test
    @Override
    public void testCreate() {
        Message m = new Message();
        m.setContent("test content 2");
        m.setChannel("channel2");
        m.setContentType("test content type 2");
        m.setDeliveryType("test delivery type 2");
        m.setRecipient("test recipient 2");
        m.setTitle("test title 2");

        messageService.saveMessage(m);
        assertNotNull(m.getId());

        Collection<Message> ms = messageService.getAllMessages();
        assertNotNull(ms);
        assertEquals(2, ms.size());
        
        Message m2 = messageService.getMessage(m.getId());
        assertNotNull(m2);

        assertEqualsMD(m, m2);
        
        Message m1 = new Message();
        m1.setContent("a");
        m1.setChannel("a");
        m1.setContentType("a");
        m1.setDeliveryType("a");
        m1.setRecipient("a");
        m1.setTitle("a");
        
        // should allow more than one record with NULL origin id
        messageService.saveMessage(m1);
    }

    @Test
    @Override
    public void testDelete() {
        messageService.deleteMessage(MESSAGE);
        
        Collection<Message> ms = messageService.getAllMessages();
        assertNotNull(ms);
        assertEquals(0, ms.size());
        
        assertNull(messageService.getMessage(MESSAGE.getId()));
    }

    /* since OJB treats creates and updates the same, and we have no constraints,
       this test doesn't really test anything under OJB */
    @Test
    @Override
    public void testDuplicateCreate() {
        Message m = new Message(MESSAGE);
        messageService.saveMessage(m);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Override
    public void testInvalidCreate() {
        final Message m = new Message();
        messageService.saveMessage(m);
    }

    @Test(expected = DataAccessException.class)
    @Override
    public void testInvalidDelete() {
        final Message m = new Message();
        m.setId(new Long(-1));
        // OJB yields an org.springmodules.orm.ojb.OjbOperationException/OptimisticLockException and claims the object
        // may have been deleted by somebody else
        messageService.deleteMessage(m);
    }

    @Test
    @Override
    public void testInvalidRead() {
        Message m = messageService.getMessage(Long.valueOf(-1));
        assertNull(m);
    }

    @Test(expected = DataAccessException.class)
    @Override
    public void testInvalidUpdate() {
        final Message m = messageService.getMessage(MESSAGE.getId());
        m.setChannel(null);
        messageService.saveMessage(m);
    }

    @Test
    @Override
    public void testReadById() {
        Message m = messageService.getMessage(MESSAGE.getId());

        assertEqualsMD(MESSAGE, m);
    }

    @Test
    @Override
    public void testUpdate() {
        Message m = messageService.getMessage(MESSAGE.getId());
        m.setTitle("A better title");
        m.setContent("different content");
        messageService.saveMessage(m);
        
        Message m2 = messageService.getMessage(m.getId());
        assertNotNull(m2);
        
        assertEqualsMD(m, m2);
    }
    
    /**
     * Asserts that an actual Message is equal to an expected Message
     * @param expected the expected Message
     * @param actual the actual Message
     */
    private void assertEqualsMD(Message expected, Message actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCreationDateTime(), actual.getCreationDateTime());
        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getContentType(), actual.getContentType());
        assertEquals(expected.getDeliveryType(), actual.getDeliveryType());
        assertEquals(expected.getRecipient(), actual.getRecipient());
        assertEquals(expected.getTitle(), actual.getTitle());
    }
}
