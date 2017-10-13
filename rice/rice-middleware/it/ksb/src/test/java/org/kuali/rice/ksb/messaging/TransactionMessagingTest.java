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
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.messaging.serviceproxies.MessageSendingTransactionSynchronization;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronization;


/**
 * Verify that messaging works in the context of a transaction and message invokation is done via the
 * {@link TransactionSynchronization} messagei
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class TransactionMessagingTest extends KSBTestCase {

    @Override
    public boolean startClient1() {
	return true;
    }

    @Override
    public void setUp() throws Exception {
	super.setUp();
	    MessageSendingTransactionSynchronization.CALLED_TRANS_COMMITTED.set(false);
	    MessageSendingTransactionSynchronization.CALLED_TRANS_ROLLEDBACKED.set(false);
    }

    @Test
    public void testMessageSentOnCommittedTransaction() throws Exception {
	KSBTestUtils.setMessagingToAsync();

	KSBServiceLocator.getTransactionTemplate().execute(new TransactionCallback<Object>() {
	    public Object doInTransaction(TransactionStatus status) {

		QName serviceName = new QName("testAppsSharedQueue", "sharedQueue");
		KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper()
			.getServiceAsynchronously(serviceName);
		testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));

		// this is a sanity check that we haven't sent the message before the trans is committed. dont remove this
                // line.
		assertFalse(MessageSendingTransactionSynchronization.CALLED_TRANS_COMMITTED.get());
		return null;
	    }
	});

	assertTrue("Message not sent transactionallY", MessageSendingTransactionSynchronization.CALLED_TRANS_COMMITTED.get());

    }

    @Test
    public void testMessageNotSentOnRolledBackTransaction() throws Exception {
	KSBTestUtils.setMessagingToAsync();

	KSBServiceLocator.getTransactionTemplate().execute(new TransactionCallback<Object>() {
	    public Object doInTransaction(TransactionStatus status) {

		QName serviceName = new QName("testAppsSharedQueue", "sharedQueue");
		KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper()
			.getServiceAsynchronously(serviceName);
		testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));

		status.setRollbackOnly();
		// this is a sanity check that we haven't sent the message before the trans is committed. dont remove this
                // line.
		assertFalse(MessageSendingTransactionSynchronization.CALLED_TRANS_ROLLEDBACKED.get());
		return null;
	    }
	});

	assertFalse("Message not sent transactionallY", MessageSendingTransactionSynchronization.CALLED_TRANS_COMMITTED.get());
	assertTrue("Message not sent transactionallY", MessageSendingTransactionSynchronization.CALLED_TRANS_ROLLEDBACKED.get());

    }

}
