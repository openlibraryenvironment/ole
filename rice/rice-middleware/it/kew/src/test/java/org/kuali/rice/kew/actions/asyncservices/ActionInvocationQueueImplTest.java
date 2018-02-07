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
package org.kuali.rice.kew.actions.asyncservices;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionInvocation;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.impl.action.ActionInvocationQueueImpl;
import org.kuali.rice.kew.rule.TestRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ActionInvocationQueueImplTest extends KEWTestCase {


	@Test public void testActionInvocationQueue_worksWithNoActionItem() throws Exception {

		TestRuleAttribute.setRecipientPrincipalIds("TestRole", "QualRole", getRecipients());

		String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");
		WorkflowDocument doc = WorkflowDocumentFactory.createDocument(rkirkendPrincipalId, "TestDocumentType");
		doc.route("");

		List<ActionRequestValue> requests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(doc.getDocumentId());
		assertFalse(requests.isEmpty());

		ActionRequestValue request = null;
		for (ActionRequestValue tempRequest : requests) {
			if (tempRequest.getPrincipal() != null && tempRequest.getPrincipal().getPrincipalName().equals("user1")) 
			{
				request = tempRequest;
				break;
			}
		}

		assertNotNull(request);

		String user1PrincipalId = getPrincipalIdForName("user1");
		String actionItemID = request.getDocumentId().trim();

		new ActionInvocationQueueImpl().invokeAction(user1PrincipalId, request.getDocumentId(), ActionInvocation.create(
                ActionType.fromCode(request.getActionRequested()), actionItemID));
		//do it again and make sure we don't have a blow up
		new ActionInvocationQueueImpl().invokeAction(user1PrincipalId, request.getDocumentId(), ActionInvocation.create(
                ActionType.fromCode(request.getActionRequested()), actionItemID));

		//verify that user1 doesn't have any AR's
		requests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(doc.getDocumentId());
		assertFalse(requests.isEmpty());

		request = null;
		for (ActionRequestValue tempRequest : requests) {
			if (tempRequest.getPrincipalId() != null && tempRequest.getPrincipalId().equals(getPrincipalIdForName("user1")) && tempRequest.isActive()) {
				request = tempRequest;
				break;
			}
		}

		assertNull(request);

	}

	public List<String> getRecipients()	{
		List<String> recipients = new ArrayList<String>();
		recipients.add(getPrincipalIdForName("user1"));
		recipients.add(getPrincipalIdForName("user2"));
		return recipients;
	}

}
