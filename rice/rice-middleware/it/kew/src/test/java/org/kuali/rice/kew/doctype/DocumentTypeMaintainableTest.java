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
package org.kuali.rice.kew.doctype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionlist.ActionListFilter;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.document.DocumentTypeMaintainable;
import org.kuali.rice.kew.preferences.service.impl.PreferencesServiceImpl;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kew.api.KewApiConstants;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * This class is used to test the {@link DocumentTypeMaintainable} 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentTypeMaintainableTest extends KEWTestCase {

    private class TemporaryDocumentType {
        public static final String NAME = "DocumentTypeMaintainTest";
        public static final String DESCRIPTION = "Document Type Maintainable Test";
        public static final String LABEL = "Document Type Maintainable Test";
        public static final String POST_PROCESSOR_CLASS_NAME = "org.kuali.rice.kew.postprocessor.DefaultPostProcessor";
        public static final String DOC_HANDLER_URL = "http://localhost/dochandler";
        public static final String HELP_DEFINITION_URL = "http://localhost/helpdefinition";

        public static final String START_NODE_NAME = "AdHoc";
        public static final String FIRST_NODE_NAME = "First Node";
        public static final String FIRST_NODE_APPROVER_1 = "temay";
        public static final String FIRST_NODE_APPROVER_2 = "delyea";
    }

    private void saveDocumentTypeUsingMaintainable(DocumentType documentType) throws Exception {
        DocumentTypeMaintainable docMaintainable = new DocumentTypeMaintainable();
        docMaintainable.setBoClass(DocumentType.class);
        docMaintainable.setBusinessObject(documentType);
        docMaintainable.saveBusinessObject();
    }

    private void turnOnOutboxForUser(final String principalId) {
        new TransactionTemplate(KEWServiceLocator.getPlatformTransactionManager()).execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                KEWServiceLocator.getUserOptionsService().save(principalId, Preferences.KEYS.USE_OUT_BOX, KewApiConstants.PREFERENCES_YES_VAL);
                return null;
            }
        });
    }

    /**
     * This method tests to make sure that the {@link DocumentType#getApplyRetroactively()} method
     * does not throw a {@link NullPointerException}
     */
    @Test public void testMaintainableSave_PreserveRouteNodes() throws Exception {
        loadXmlFile("DocTypeMaintainableConfig.xml");
        DocumentTypeService docTypeService = KEWServiceLocator.getDocumentTypeService();
        DocumentType documentType = docTypeService.findByName(TemporaryDocumentType.NAME);
        assertEquals("Wrong number of route nodes", 2, KEWServiceLocator.getRouteNodeService().getFlattenedNodes(documentType, false).size());
        documentType.setApplyRetroactively(null);
        saveDocumentTypeUsingMaintainable(documentType);
        documentType = docTypeService.findByName(TemporaryDocumentType.NAME);
        assertEquals("Document type should be current", Boolean.TRUE, documentType.getCurrentInd());
        List flattenedNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(documentType, false);
        assertEquals("Wrong number of route nodes", 2, flattenedNodes.size());
    }

    /**
     * This method tests to make sure that the {@link DocumentType#getApplyRetroactively()} method
     * does not throw a {@link NullPointerException}
     */
    @Test public void testApplyRetroactiveNull() throws Exception {
        loadXmlFile("DocTypeMaintainableConfig.xml");
        DocumentTypeService docTypeService = KEWServiceLocator.getDocumentTypeService();
        DocumentType documentType = docTypeService.findByName(TemporaryDocumentType.NAME);
        documentType.setApplyRetroactively(null);
        saveDocumentTypeUsingMaintainable(documentType);
    }

    /**
     * This method tests the functionality of the 'apply retroactively' option on a document type
     * which should save certain fields to all former versions of a document type, action items, and
     * outbox items.
     */
    @Test public void testApplyRetroactiveSave() throws Exception {
        DocumentTypeService docTypeService = KEWServiceLocator.getDocumentTypeService();
        // load the file multiple times to create multiple previous versions in the doc type table
        loadXmlFile("DocTypeMaintainableConfig.xml");
        loadXmlFile("DocTypeMaintainableConfig_Rule.xml"); // ingests just the rule
        loadXmlFile("DocTypeMaintainableConfig.xml");
        loadXmlFile("DocTypeMaintainableConfig.xml");
        assertEquals("Incorrect number of total document type instances for name " + TemporaryDocumentType.NAME, 2, docTypeService.findPreviousInstances(TemporaryDocumentType.NAME).size());

        // create a document and route it so we have at least one action item and one outbox item
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), TemporaryDocumentType.NAME);
        document.route("Test Doc");
        String documentHeaderId = document.getDocumentId();
        assertTrue("Document should be ENROUTE", document.isEnroute());
        // verify routing worked properly
        // for the first user we should approve and verify that an outbox item has been created
        String userPrincipalName = TemporaryDocumentType.FIRST_NODE_APPROVER_1;
        turnOnOutboxForUser(getPrincipalIdForName(userPrincipalName));
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(userPrincipalName), documentHeaderId);
        TestUtilities.assertAtNode("Document should be at the first 'requests' node", document, TemporaryDocumentType.FIRST_NODE_NAME);
        assertTrue("User " + userPrincipalName + " should have approval requested.", document.isApprovalRequested());
        document.approve("Test Approval");
        assertEquals("There should be an outbox item", 1, KEWServiceLocator.getActionListService().getOutbox(getPrincipalIdForName(userPrincipalName), new ActionListFilter()).size());
        TestUtilities.assertNotInActionList(getPrincipalIdForName(userPrincipalName), documentHeaderId);
        // for the second user we should not approve but verify an action item exists in the user's action list
        userPrincipalName = TemporaryDocumentType.FIRST_NODE_APPROVER_2;
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(userPrincipalName), documentHeaderId);
        assertTrue("User " + userPrincipalName + " should have approval requested.", document.isApprovalRequested());
        TestUtilities.assertInActionList(getPrincipalIdForName(userPrincipalName), documentHeaderId);

        // load the document type and change the fields that can be retroactively applied
        DocumentType documentType = docTypeService.findByName(TemporaryDocumentType.NAME);
        documentType.setApplyRetroactively(Boolean.TRUE);
        String new_description = "Maintainable Test Document Type Description";
        documentType.setDescription(new_description);
        String new_label = "Maintainable Test Document Label";
        documentType.setLabel(new_label);
        String new_help_def_url = "http://test.kuali.org/helpdefinition";
        documentType.setUnresolvedHelpDefinitionUrl(new_help_def_url);

        saveDocumentTypeUsingMaintainable(documentType);
        // verify all previous version instances of the doc type have the updated fields
        for (DocumentType docTypeInstance : docTypeService.findPreviousInstances(TemporaryDocumentType.NAME)) {
            assertEquals("Label should have been applied retroactively to former document type versions", new_label, docTypeInstance.getLabel());
            assertEquals("Description should have been applied retroactively to former document type versions", new_description, docTypeInstance.getDescription());
            assertEquals("Unresolved Help Definition URL should have been applied retroactively to former document type versions", new_help_def_url, docTypeInstance.getUnresolvedHelpDefinitionUrl());
        }

        // verify that the outbox item was updated properly
        userPrincipalName = TemporaryDocumentType.FIRST_NODE_APPROVER_1;
        Collection<OutboxItemActionListExtension> outboxItems = KEWServiceLocator.getActionListService().getOutbox(getPrincipalIdForName(userPrincipalName), new ActionListFilter());
        assertEquals("There should be one outbox item", 1, outboxItems.size());
        ActionItem outboxItem = outboxItems.iterator().next();
        assertEquals("The label on the outbox item should have been changed", new_label, outboxItem.getDocLabel());

        // verify that the action item was updated properly
        userPrincipalName = TemporaryDocumentType.FIRST_NODE_APPROVER_2;
        Collection<ActionItem> actionList = KEWServiceLocator.getActionListService().findByPrincipalId(getPrincipalIdForName(userPrincipalName));
        assertEquals("There should be one action item", 1, actionList.size());
        ActionItem actionItem = actionList.iterator().next();
        assertEquals("The label on the action item should have been changed", new_label, actionItem.getDocLabel());
    }

    // write test to verify parent document edits via the UI save properly
    @Test public void testParentDocumentChanges() throws Exception {
        loadXmlFile("DocTypeMaintainableConfig.xml");
        loadXmlFile("DocTypeMaintainableConfig_Rule.xml"); // ingests just the rule
        
    }
}
