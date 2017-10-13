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
package org.kuali.rice.kew.docsearch;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import static org.junit.Assert.*;

/**
 * This is a description of what this class does - jjhanso don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentSearchSecurityTest extends KEWTestCase {
	private static final String WORKFLOW_ADMIN_USER_NETWORK_ID = "bmcgough";
    private static final String APPROVER_USER_NETWORK_ID = "user2";
    private static final String STANDARD_USER_NETWORK_ID = "user1";
	DocumentSearchService docSearchService;
	
	@Override
	protected void setUpAfterDataLoad() throws Exception {
        docSearchService = (DocumentSearchService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);
    }
	
	@Override
	protected void loadTestData() throws Exception {
    	loadXmlFile("SearchSecurityConfig.xml");
        
    }
	
    /**
     * Test for https://test.kuali.org/jira/browse/KULRICE-1968 - Document search fails when users are missing
     * Tests that we can safely search on docs whose initiator no longer exists in the identity management system
     * This test searches by doc type name criteria.
     * @throws Exception
     */
    @Test public void testDocSearchSecurityPermissionDocType() throws Exception {
        String documentTypeName = "SecurityDoc_PermissionOnly";
        String userNetworkId = "arh14";
        // route a document to enroute and route one to final
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("testDocSearch_PermissionSecurity");
        workflowDocument.route("routing this document.");

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("edna");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals(0, results.getNumberOfSecurityFilteredResults());
        assertEquals("Search returned invalid number of documents", 1, results.getSearchResults().size());
    }
    
    @Test public void testDocSearchBadPermission() throws Exception {
        String documentTypeName = "SecurityDoc_InvalidPermissionOnly";
        String userNetworkId = "arh14";
        // route a document to enroute and route one to final
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("testDocSearch_PermissionSecurity");
        workflowDocument.route("routing this document.");

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("edna");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search returned invalid number of documents", 0, results.getSearchResults().size());
    }
	
    @Test public void testFilteringInitiator() throws Exception {    	
        String documentType = "SecurityDoc_InitiatorOnly";
        String initiator = getPrincipalId(STANDARD_USER_NETWORK_ID);
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(initiator, documentType);
        document.route("");
        assertFalse("Document should not be in init status after routing", document.isInitiated());

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        DocumentSearchResults results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(initiator, criteria.build());
        assertEquals("Should retrive one record from search", 1, results.getSearchResults().size());
        assertEquals("No rows should have been filtered due to security", 0, results.getNumberOfSecurityFilteredResults());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId("user3"), criteria.build());
        assertEquals("Should retrive no records from search", 0, results.getSearchResults().size());
        assertEquals("One row should have been filtered due to security", 1, results.getNumberOfSecurityFilteredResults());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId(WORKFLOW_ADMIN_USER_NETWORK_ID), criteria.build());
        assertEquals("Should retrive one record from search", 1, results.getSearchResults().size());
        assertEquals("No rows should have been filtered due to security", 0, results.getNumberOfSecurityFilteredResults());
    }
    
    @Test public void testFiltering_Workgroup() throws Exception {
        String documentType = "SecurityDoc_WorkgroupOnly";
        String initiator = getPrincipalId(STANDARD_USER_NETWORK_ID);
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(initiator, documentType);
        document.route("");
        assertFalse("Document should not be in init status after routing", document.isInitiated());

        // verify that initiator cannot see the document
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        DocumentSearchResults results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(initiator, criteria.build());
        assertEquals("Should retrive no records from search", 0, results.getSearchResults().size());
        assertEquals("One row should have been filtered due to security", 1, results.getNumberOfSecurityFilteredResults());

        // verify that workgroup can see the document
        String workgroupName = "Test_Security_Group";
        Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, workgroupName);
        assertNotNull("Workgroup '" + workgroupName + "' should be valid", group);
        for (String workgroupUserId : KimApiServiceLocator.getGroupService().getMemberPrincipalIds(group.getId())) {
            Person workgroupUser = KimApiServiceLocator.getPersonService().getPerson(workgroupUserId);
            criteria = DocumentSearchCriteria.Builder.create();
            criteria.setDocumentId(document.getDocumentId());
            results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(workgroupUser.getPrincipalId(), criteria.build());
            assertEquals("Should retrive one record from search for user " + workgroupUser, 1, results.getSearchResults().size());
            assertEquals("No rows should have been filtered due to security for user " + workgroupUser, 0, results.getNumberOfSecurityFilteredResults());
        }

        // verify that user3 cannot see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId("user3"), criteria.build());
        assertEquals("Should retrive no records from search", 0, results.getSearchResults().size());
        assertEquals("One row should have been filtered due to security", 1, results.getNumberOfSecurityFilteredResults());

        // verify that WorkflowAdmin can see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId(WORKFLOW_ADMIN_USER_NETWORK_ID), criteria.build());
        assertEquals("Should retrive one record from search", 1, results.getSearchResults().size());
        assertEquals("No rows should have been filtered due to security", 0, results.getNumberOfSecurityFilteredResults());
    }

    @Test public void testFiltering_SearchAttribute() throws Exception {

        String searchAttributeName = "UserEmployeeId";
        String searchAttributeFieldName = "employeeId";
        String documentTypeName = "SecurityDoc_SearchAttributeOnly";
        String initiatorNetworkId = STANDARD_USER_NETWORK_ID;
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalId(initiatorNetworkId), documentTypeName);
        WorkflowAttributeDefinition.Builder definition = WorkflowAttributeDefinition.Builder.create(searchAttributeName);
        definition.addPropertyDefinition(searchAttributeFieldName, "user3");
        document.addSearchableDefinition(definition.build());
        document.route("");
        assertFalse("Document should not be in init status after routing", document.isInitiated());

        // verify that initiator cannot see the document
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        DocumentSearchResults results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId(initiatorNetworkId), criteria.build());
        assertEquals("Should retrive no records from search", 0, results.getSearchResults().size());
        assertEquals("One row should have been filtered due to security", 1, results.getNumberOfSecurityFilteredResults());

        // verify that user3 can see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId("user3"), criteria.build());
        assertEquals("Should retrive one record from search", 1, results.getSearchResults().size());
        assertEquals("No rows should have been filtered due to security", 0, results.getNumberOfSecurityFilteredResults());

        // verify that user2 cannot see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId("user2"), criteria.build());
        assertEquals("Should retrive no records from search", 0, results.getSearchResults().size());
        assertEquals("One row should have been filtered due to security", 1, results.getNumberOfSecurityFilteredResults());

        // verify that WorkflowAdmin can see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId(WORKFLOW_ADMIN_USER_NETWORK_ID), criteria.build());
        assertEquals("Should retrive one record from search", 1, results.getSearchResults().size());
        assertEquals("No rows should have been filtered due to security", 0, results.getNumberOfSecurityFilteredResults());

        RouteContext.clearCurrentRouteContext();
        document = WorkflowDocumentFactory.loadDocument(getPrincipalId(APPROVER_USER_NETWORK_ID), document.getDocumentId());
        document.clearSearchableContent();
        definition = WorkflowAttributeDefinition.Builder.create(searchAttributeName);
        definition.addPropertyDefinition(searchAttributeFieldName, "user2");
        document.addSearchableDefinition(definition.build());
        document.saveDocumentData();

        // verify that user2 can see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId("user2"), criteria.build());
        assertEquals("Should retrive one record from search", 1, results.getSearchResults().size());
        assertEquals("No rows should have been filtered due to security", 0, results.getNumberOfSecurityFilteredResults());

        // verify that user3 cannot see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId("user3"), criteria.build());
        assertEquals("Should retrive no records from search", 0, results.getSearchResults().size());
        assertEquals("One row should have been filtered due to security", 1, results.getNumberOfSecurityFilteredResults());

        // verify that initiator cannot see the document
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentId(document.getDocumentId());
        results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(getPrincipalId(initiatorNetworkId), criteria.build());
        assertEquals("Should retrive no records from search", 0, results.getSearchResults().size());
        assertEquals("One row should have been filtered due to security", 1, results.getNumberOfSecurityFilteredResults());
    }

    private String getPrincipalId(String principalName) {
    	return KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName).getPrincipalId();
    }
	
}
