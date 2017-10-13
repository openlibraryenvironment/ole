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
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.bo.MessageDeliveryStatus;
import org.kuali.rice.kcb.service.GlobalKCBServiceLocator;
import org.kuali.rice.kcb.service.MessageDeliveryService;
import org.kuali.rice.kcb.service.MessageService;
import org.kuali.rice.kcb.test.BusinessObjectTestCase;
import org.kuali.rice.kcb.test.KCBTestData;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;

import static org.junit.Assert.*;


/**
 * Tests MessageDeliveryService
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MessageDeliveryServiceTest extends BusinessObjectTestCase {
    private MessageService messageService;
    private MessageDeliveryService messageDeliveryService;
    private Message MESSAGE;
    private MessageDelivery MESSAGE_DELIV;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
    
        messageService = GlobalKCBServiceLocator.getInstance().getMessageService();
        messageDeliveryService = GlobalKCBServiceLocator.getInstance().getMessageDeliveryService();

        MESSAGE = KCBTestData.getMessage1();
        messageService.saveMessage(MESSAGE);
        
        MESSAGE_DELIV = KCBTestData.getMessageDelivery1();
        MESSAGE_DELIV.setMessage(MESSAGE);
        
        messageDeliveryService.saveMessageDelivery(MESSAGE_DELIV);
    }

    @Test
    @Override
    public void testCreate() {
        MessageDelivery md = new MessageDelivery();
        md.setDelivererTypeName("pigeon");
        md.setMessage(MESSAGE);
        
        messageDeliveryService.saveMessageDelivery(md);

        assertNotNull(md.getId());
        Collection<MessageDelivery> ms = messageDeliveryService.getAllMessageDeliveries();
        assertNotNull(ms);
        assertEquals(2, ms.size());
        
        MessageDelivery md2 = messageDeliveryService.getMessageDelivery(md.getId());
        assertNotNull(md2);
        
        assertEqualsMD(md, md2);
    }

    @Test
    @Override
    public void testDelete() {
        messageDeliveryService.deleteMessageDelivery(MESSAGE_DELIV);

        Collection<MessageDelivery> ms = messageDeliveryService.getAllMessageDeliveries();
        assertNotNull(ms);
        assertEquals(0, ms.size());
        
        assertNull(messageDeliveryService.getMessageDelivery(MESSAGE_DELIV.getId()));
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Override
    public void testDuplicateCreate() {
        // violates messageid-deliverer constraint
        final MessageDelivery md = new MessageDelivery();
        md.setId(KCBTestData.FAKE_ID);
        md.setDelivererSystemId(MESSAGE_DELIV.getDelivererSystemId());
        md.setDelivererTypeName(MESSAGE_DELIV.getDelivererTypeName());
        md.setDeliveryStatus(MESSAGE_DELIV.getDeliveryStatus());
        md.setLockVerNbr(MESSAGE_DELIV.getLockVerNbr());
        md.setMessage(MESSAGE);
        messageDeliveryService.saveMessageDelivery(md);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Override
    public void testInvalidCreate() {
        final MessageDelivery m = new MessageDelivery();
        messageDeliveryService.saveMessageDelivery(m);
    }

    @Test(expected = DataAccessException.class)
    @Override
    public void testInvalidDelete() {
        final MessageDelivery m = new MessageDelivery();
        m.setId(new Long(-1));
        // OJB yields an org.springmodules.orm.ojb.OjbOperationException/OptimisticLockException and claims the object
        // may have been deleted by somebody else
        messageDeliveryService.deleteMessageDelivery(m);
    }

    @Test
    @Override
    public void testInvalidRead() {
        MessageDelivery m = messageDeliveryService.getMessageDelivery(Long.valueOf(-1));
        assertNull(m);
    }

    @Test(expected = DataAccessException.class)
    @Override
    public void testInvalidUpdate() {
        final MessageDelivery m = messageDeliveryService.getMessageDelivery(MESSAGE_DELIV.getId());
        m.setDelivererTypeName(null);
        messageDeliveryService.saveMessageDelivery(m);
    }

    @Test
    @Override
    public void testReadById() {
        MessageDelivery m = messageDeliveryService.getMessageDelivery(MESSAGE_DELIV.getId());

        assertEqualsMD(MESSAGE_DELIV, m);
    }

    @Test
    @Override
    public void testUpdate() {
        MessageDelivery m = messageDeliveryService.getMessageDelivery(MESSAGE_DELIV.getId());
        m.setDelivererTypeName("eagle");
        m.setDeliveryStatus(MessageDeliveryStatus.UNDELIVERED);
        m.setDelivererSystemId("1234");
        messageDeliveryService.saveMessageDelivery(m);
        
        MessageDelivery m2 = messageDeliveryService.getMessageDelivery(m.getId());
        assertNotNull(m2);
        
        assertEqualsMD(m, m2);
    }
    
    private void assertEqualsMD(MessageDelivery expected, MessageDelivery actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDelivererSystemId(), actual.getDelivererSystemId());
        assertEquals(expected.getDelivererTypeName(), actual.getDelivererTypeName());
        assertEquals(expected.getDeliveryStatus(), actual.getDeliveryStatus());
        assertEquals(expected.getMessage().getId(), actual.getMessage().getId());
    }
}
