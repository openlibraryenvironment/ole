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
package org.kuali.rice.ksb.messaging;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.api.messaging.ResourceFacade;
import org.kuali.rice.ksb.messaging.remotedservices.BaseballCard;
import org.kuali.rice.ksb.messaging.remotedservices.BaseballCardCollectionService;
import org.kuali.rice.ksb.messaging.remotedservices.Inbox;
import org.kuali.rice.ksb.messaging.remotedservices.InboxResource;
import org.kuali.rice.ksb.messaging.remotedservices.Message;
import org.kuali.rice.ksb.messaging.remotedservices.MessageResource;
import org.kuali.rice.ksb.test.KSBTestCase;


/**
 * Test that RESTful services work over the KSB
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RESTServiceTest extends KSBTestCase {

    private static final String BBCARD_SERVICE = "baseballCardCollectionService";
    private static final String KMS_SERVICE = "kms";
    private static final String NAMESPACE = "test";


    public boolean startClient1() {
        return true;
    }

    @Test
    public void testMessagingService() throws Exception {
    	ResourceFacade kmsService =
            (ResourceFacade) GlobalResourceLoader.getService(
                new QName(NAMESPACE, KMS_SERVICE));

    	// Get service by resource name
    	InboxResource inboxResource = kmsService.getResource("inbox");
//    	kmsService.getRe...
    	// Get service by resource class
    	MessageResource messageResource = kmsService.getResource(MessageResource.class);

    	exerciseMessagingService(inboxResource, messageResource);
    }

    /**
	 * This method ...
	 *
	 * @param inboxResource
	 * @param messageResource
	 */
	private void exerciseMessagingService(InboxResource inboxResource,
			MessageResource messageResource) {
		Inbox inbox = new Inbox();
    	inbox.setOwner("Joe Q. Tester");

    	inbox = inboxResource.createInbox(inbox);

    	Message message = new Message();
    	message.setRecipient("Joe Q. Tester");
    	message.setSubject("Hello new world!");
    	message.setText("This is a test message.");

    	Message createdMessage = messageResource.createMessage(message);

    	List<String> messages = inbox.getMessages();
    	messages.add(createdMessage.getId());

    	inboxResource.updateInbox(inbox);

    	inbox = inboxResource.retrieveInbox(inbox.getId());

    	List<String> updatedMessages = inbox.getMessages();

    	String updatedMessageId = updatedMessages.get(0);

    	Message retrievedMessage = messageResource.retrieve(updatedMessageId);

    	assertTrue(retrievedMessage.getSubject().equals("Hello new world!"));
	}

    /**
     * Exercise our RESTful {@link BaseballCardCollectionService} over the KSB
     */
    @Test
    public void testBaseballCardCollectionService() {
        BaseballCard cardA = new BaseballCard("Mickey Mantle", "Topps", 1952);
        BaseballCard cardB = new BaseballCard("Ted Williams", "Bowman", 1954);
        BaseballCard cardC = new BaseballCard("Willie Mays", "Bowman", 1951);
        BaseballCard cardD = new BaseballCard("Willie Mays Hayes", "Bogus", 1989);

        BaseballCardCollectionService service = 
            (BaseballCardCollectionService) GlobalResourceLoader.getService(
                    new QName(NAMESPACE, BBCARD_SERVICE)
            );

        // test @POST
        service.add(cardA);
        service.add(cardB);
        Integer willieMaysId = service.add(cardC);

        // test @GET
        List<BaseballCard> allCards = service.getAll();
        assertNotNull(allCards);
        assertTrue(allCards.size() == 3);
        assertTrue(allCards.contains(cardA));
        assertTrue(allCards.contains(cardB));
        assertTrue(allCards.contains(cardC));

        // test @PUT
        service.update(willieMaysId, cardD); // replace Willie Mays w/ Willie Mays Hayes
        allCards = service.getAll();
        assertNotNull(allCards);
        assertTrue(allCards.size() == 3);
        assertFalse(allCards.contains(cardC)); // this one was replaced
        assertTrue(allCards.contains(cardD));  // this was the replacement

        // test @DELETE
        service.delete(willieMaysId); // delete Willie
        allCards = service.getAll();
        assertNotNull(allCards);
        assertTrue(allCards.size() == 2);
        assertFalse(allCards.contains(cardD)); // should be gone

        // test that adding a card already in the collection in fact adds to the collection. 
        service.add(cardA);
        service.add(cardA);
        service.add(cardA);
        allCards = service.getAll();
        assertTrue(allCards.size() == 5);
        
        try {
            service.unannotatedMethod();
            fail("Magic?  You can't remotely invoke a method that doesn't have JAX-RS annotations in the resource class!");
        } catch (Exception e) {
            // I expect this to throw an exception
            e.printStackTrace();
        }
    }


    /**
     * By defualt, CXF has a special Service List page that you can see by 
     * appending /services to a RESTful service URL.  We've purposefully disabled it,
     * so let's verify that it isn't there.
     * 
     * @throws Exception
     */
   /* @Test
    public void testCXFServiceListIsDisabled() throws Exception {
        // if CXF's service list is enabled, this URL will return the service list
        URL url = new URL(getEndpointUrl()+"/services");

        try {
            InputStream contentStream = (InputStream)url.getContent();
            fail("the service list shouldn't be available!");
        } catch (IOException e) {
            // this is what should happen
        }
    }*/

}
