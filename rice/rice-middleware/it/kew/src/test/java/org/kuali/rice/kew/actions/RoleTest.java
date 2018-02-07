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
package org.kuali.rice.kew.actions;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actions.BlanketApproveTest.NotifySetup;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

public class RoleTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Test public void testRoleRequestGeneration() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        document.route("");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");//ewestfal had force action rule
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");
        
        //this be the role delegate of jitrue
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("natjohns"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        document.approve("");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("xqi"), document.getDocumentId());
        document.acknowledge("");
        
        assertTrue("Document should be final", document.isFinal());

        List requests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
        List rootRequests = KEWServiceLocator.getActionRequestService().getRootRequests(requests);
        
        //verify our requests have been made correctly
        for (Iterator iter = rootRequests.iterator(); iter.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iter.next();
			if (request.isRoleRequest()) {
				//direct children should not be role requests
				iterateChildrenRequests(request.getChildrenRequests(), new String[] {"U", "W"}, request);
			}
		}
    }
	
    private void iterateChildrenRequests(Collection childrenRequests, String[] requestTypes, ActionRequestValue parentRequest) {
    	for (Iterator iter = childrenRequests.iterator(); iter.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iter.next();
			boolean matched = false;
			for (int i = 0; i < requestTypes.length; i++) {
				if (request.getRecipientTypeCd().equals(requestTypes[i])) {
					matched = true;
				}
			}
			if (!matched) {
				fail("Didn't find request of types expected Recipient Type: " + parentRequest.getRecipientTypeCd() + " RoleName: " + parentRequest.getRoleName() + " Qualified Role Name:" + parentRequest.getQualifiedRoleName() + " RuleId: " + parentRequest.getRuleBaseValuesId());
			}
			//if this is a role then it can't have a child role
			if (request.isRoleRequest()) {
				//direct children should not be role requests
				iterateChildrenRequests(request.getChildrenRequests(), new String[] {"U", "W"}, request);
			}
		}
    }
}
