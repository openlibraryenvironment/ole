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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;


public class ValidActionsTest extends KEWTestCase {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ValidActionsTest.class);

    public static final String DOCUMENT_TYPE_NAME = "BlanketApproveSequentialTest";

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Test public void testValidActions() throws Exception {
        WorkflowDocument document = null;
        String networkId = null;
        document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), DOCUMENT_TYPE_NAME);
        String documentId = document.getDocumentId();

        networkId = "rkirkend";
        document = this.checkActions(networkId, documentId, 
                new String[]{KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD},
                new String[]{KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_ROUTED_CD,KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_CANCELED_CD,ActionType.RECALL.getCode()});
        // check for super user action "c", "a"
        // check for blanket approve "B"
        // check for no route "O"
        // check for no save "S"

        networkId = "pmckown";
        document = this.checkActions(networkId, documentId, 
                new String[]{}, 
                new String[]{KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD,KewApiConstants.ACTION_TAKEN_ROUTED_CD,KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_CANCELED_CD,ActionType.RECALL.getCode()});
        // check for no super user action "c", "a"
        // check for blanket approve "B"
        // check for no route "O"
        // check for no save "S"

        networkId = "user1";
        document = this.checkActions(networkId, documentId, 
                new String[]{KewApiConstants.ACTION_TAKEN_ROUTED_CD,KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_CANCELED_CD},
                new String[]{KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD,ActionType.RECALL.getCode()});
        // check for no blanket approve "B"
        // check for no super user actions "c", "a"
        // check for routable "O"
        // check for savable "S"
        document.saveDocument("");

        networkId = "rkirkend";
        document = this.checkActions(networkId, documentId, 
                new String[]{KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD},
                new String[]{KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_ROUTED_CD,KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_CANCELED_CD,ActionType.RECALL.getCode()});
        // check for super user action "c", "a"
        // check for blanket approve "B"
        // check for no route "O"
        // check for no save "S"

        networkId = "pmckown";
        document = this.checkActions(networkId, documentId, 
                new String[]{}, 
                new String[]{KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD,KewApiConstants.ACTION_TAKEN_ROUTED_CD,KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_CANCELED_CD,ActionType.RECALL.getCode()});
        // check for no super user action "c", "a"
        // check for blanket approve "B"
        // check for no route "O"
        // check for no save "S"

        networkId = "user1";
        document = this.checkActions(networkId, documentId, 
                new String[]{KewApiConstants.ACTION_TAKEN_ROUTED_CD,KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_CANCELED_CD},
                new String[]{KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD,ActionType.RECALL.getCode()});
        // check for no blanket approve "B"
        // check for no super user actions "c", "a"
        // check for routable "O"
        // check for savable "S"
        document.route("");
        assertEquals("Document should be ENROUTE", DocumentStatus.ENROUTE, document.getStatus());

        networkId = "user1";
        document = this.checkActions(networkId, documentId, 
                new String[]{ActionType.RECALL.getCode()},
                new String[]{KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_ROUTED_CD,KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD,KewApiConstants.ACTION_TAKEN_CANCELED_CD});
        // check for no blanket approve "B"
        // check for no super user actions "c", "a"
        // check for no routable "O"
        // check for no savable "S"

        networkId = "rkirkend";
        document = this.checkActions(networkId, documentId, 
                new String[]{KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD,KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD,KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD,KewApiConstants.ACTION_TAKEN_APPROVED_CD},
                new String[]{KewApiConstants.ACTION_TAKEN_SAVED_CD,KewApiConstants.ACTION_TAKEN_ROUTED_CD,ActionType.RECALL.getCode()});
        // check for super user action "c", "a"
        // check for blanket approve "B"
        // check for approve "A"
        // check for no route "O"
        // check for no save "S"

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        document.approve("");

        // an action has been taken, initiator can no longer recall!
        this.checkActions("user1", documentId, new String[]{}, new String[]{ActionType.RECALL.getCode()});

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        document.approve("");

        // an action has been taken, initiator can no longer recall
        this.checkActions("user1", documentId, new String[]{}, new String[]{ActionType.RECALL.getCode()});

        // SHOULD NOW BE ONLY ACKNOWLEDGED

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        // test for Processed Status on document
        document.acknowledge("");

        // an action has been taken, initiator can no longer recall
        this.checkActions("user1", documentId, new String[]{}, new String[]{ActionType.RECALL.getCode()});

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        document.acknowledge("");

        // an action has been taken, initiator can no longer recall
        this.checkActions("user1", documentId, new String[]{}, new String[]{ActionType.RECALL.getCode()});

    }

    private WorkflowDocument checkActions(String networkId,String documentId,String[] validActionsAllowed,String[] invalidActionsNotAllowed) throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(networkId), documentId);
        org.kuali.rice.kew.api.action.ValidActions validActions = document.getValidActions();
        Set<ActionType> validActionsSet = validActions.getValidActions();

        for (int i = 0; i < validActionsAllowed.length; i++) {
            String actionAllowed = validActionsAllowed[i];
            if (!validActionsSet.contains(ActionType.fromCode(actionAllowed))) {
                fail("Action '" + KewApiConstants.ACTION_TAKEN_CD.get(actionAllowed) + "' should be allowed for user " + networkId);
            }
        }

        for (int j = 0; j < invalidActionsNotAllowed.length; j++) {
            String actionDisallowed = invalidActionsNotAllowed[j];
            if (validActionsSet.contains(actionDisallowed)) {
                fail("Action '" + KewApiConstants.ACTION_TAKEN_CD.get(actionDisallowed) + "' should not be allowed for user " + networkId);
            }
        }

        return document;
    }
}
