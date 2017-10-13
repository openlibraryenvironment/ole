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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Years;
import org.junit.Test;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.document.search.RouteNodeLookupLogic;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.useroptions.UserOptions;
import org.kuali.rice.kew.useroptions.UserOptionsService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.TestHarnessServiceLocator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class DocumentSearchTest extends KEWTestCase {
    private static final String KREW_DOC_HDR_T = "KREW_DOC_HDR_T";
    private static final String INITIATOR_COL = "INITR_PRNCPL_ID";

    DocumentSearchService docSearchService;
    UserOptionsService userOptionsService;

    @Override
    protected void loadTestData() throws Exception {
        loadXmlFile("SearchAttributeConfig.xml");
    }

    @Override
    protected void setUpAfterDataLoad() throws Exception {
        docSearchService = (DocumentSearchService)KEWServiceLocator.getDocumentSearchService();
        userOptionsService = (UserOptionsService)KEWServiceLocator.getUserOptionsService();
    }


    @Test public void testDocSearch() throws Exception {
        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("bmcgough");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        DocumentSearchResults results = null;
        criteria.setTitle("*IN");
        criteria.setSaveName("bytitle");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setTitle("*IN-CFSG");
        criteria.setSaveName("for in accounts");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDateApprovedFrom(new DateTime(2004, 9, 16, 0, 0));
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        criteria = DocumentSearchCriteria.Builder.create();
        user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("bmcgough");
        DocumentSearchCriteria savedCriteria = docSearchService.getNamedSearchCriteria(user.getPrincipalId(), "bytitle");
        assertNotNull(savedCriteria);
        assertEquals("bytitle", savedCriteria.getSaveName());
        savedCriteria = docSearchService.getNamedSearchCriteria(user.getPrincipalId(), "for in accounts");
        assertNotNull(savedCriteria);
        assertEquals("for in accounts", savedCriteria.getSaveName());
    }

    // KULRICE-5755 tests that the Document in the DocumentResult is properly populated
    @Test public void testDocSearchDocumentResult() throws Exception {
        String[] docIds = routeTestDocs();
        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("bmcgough");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals(3, results.getSearchResults().size());
        DocumentSearchResult result = results.getSearchResults().get(0);
        Document doc = result.getDocument();

        // check all the DocumentContract properties
        assertNotNull(doc.getApplicationDocumentStatus());
        assertNotNull(doc.getApplicationDocumentStatusDate());
        assertNotNull(doc.getDateApproved());
        assertNotNull(doc.getDateCreated());
        assertNotNull(doc.getDateFinalized());
        assertNotNull(doc.getDocumentId());
        assertNotNull(doc.getDocumentTypeName());
        assertNotNull(doc.getApplicationDocumentId());
        assertNotNull(doc.getDateLastModified());
        assertNotNull(doc.getDocumentHandlerUrl());
        assertNotNull(doc.getDocumentTypeId());
        assertNotNull(doc.getInitiatorPrincipalId());
        assertNotNull(doc.getRoutedByPrincipalId());
        assertNotNull(doc.getStatus());
        assertNotNull(doc.getTitle());
        // route variables are currently excluded
        assertTrue(doc.getVariables().isEmpty());
    }

    @Test public void testDocSearch_appDocStatuses() throws Exception {
        String[] docIds = routeTestDocs();

        DateTime now = DateTime.now();
        DateTime before = now.minusDays(2);
        DateTime after = now.plusDays(2);

        String principalId = getPrincipalId("bmcgough");

        // first test basic multi-appDocStatus search without dates.  search for 2 out of 3 existing statuses

        List<String> appDocStatusSearch = Arrays.asList("Submitted", "Pending");
        DocumentSearchResults results = doAppStatusDocSearch(principalId, appDocStatusSearch, null, null);

        assertEquals("should have matched one doc for each status", 2, results.getSearchResults().size());

        for (DocumentSearchResult result : results.getSearchResults()) {
            assertTrue("app doc status should be in " + StringUtils.join(appDocStatusSearch, ", "),
                    appDocStatusSearch.contains(result.getDocument().getApplicationDocumentStatus()));
        }

        // use times to verify app doc status change date functionality

        // first try to bring all 3 docs back with a range that includes all the test docs

        appDocStatusSearch = Arrays.asList("Submitted", "Pending", "Completed");
        results = doAppStatusDocSearch(principalId, appDocStatusSearch, before, after);

        assertEquals("all docs are in the date range, should have matched them all", 3, results.getSearchResults().size());

        for (DocumentSearchResult result : results.getSearchResults()) {
            assertTrue("app doc status should be in " + StringUtils.join(appDocStatusSearch, ", "),
                    appDocStatusSearch.contains(result.getDocument().getApplicationDocumentStatus()));
        }

        // test that the app doc status list still limits the results when a date range is set

        appDocStatusSearch = Arrays.asList("Submitted", "Pending");

        results = doAppStatusDocSearch(principalId, appDocStatusSearch, before, after);
        assertEquals("should have matched one doc for each status", 2, results.getSearchResults().size());

        for (DocumentSearchResult result : results.getSearchResults()) {
            assertTrue("app doc status should be in " + StringUtils.join(appDocStatusSearch, ", "),
                    appDocStatusSearch.contains(result.getDocument().getApplicationDocumentStatus()));
        }

        // test that the date range limits the results too.  No docs will be in this range.

        appDocStatusSearch = Arrays.asList("Submitted", "Pending", "Completed");

        results = doAppStatusDocSearch(principalId, appDocStatusSearch, after, after.plusDays(1));
        assertEquals("none of the docs should be in the date range", 0, results.getSearchResults().size());

        // finally, test a legacy form of the search to make sure that the criteria is still respected

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setApplicationDocumentStatus("Submitted");

        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals("legacy style app doc status search should have matched one document",
                1, results.getSearchResults().size());
        assertTrue("app doc status should match the search criteria",
                "Submitted".equals(results.getSearchResults().get(0).getDocument().getApplicationDocumentStatus()));
    }

    private DocumentSearchResults doAppStatusDocSearch(String principalId, List<String> appDocStatuses,
            DateTime appStatusChangedFrom, DateTime appStatusChangedTo) {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setApplicationDocumentStatuses(appDocStatuses);
        criteria.setDateApplicationDocumentStatusChangedFrom(appStatusChangedFrom);
        criteria.setDateApplicationDocumentStatusChangedTo(appStatusChangedTo);
        return docSearchService.lookupDocuments(principalId, criteria.build());
    }

    @Test public void testDocSearch_maxResults() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setMaxResults(5);
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
        criteria.setMaxResults(2);
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(2, results.getSearchResults().size());

        // test search result document population
        // break out into separate test if/when we have more fields to test
        assertEquals("_blank", results.getSearchResults().get(0).getDocument().getDocumentHandlerUrl());
    }

    @Test public void testDocSearch_maxResultsIsNull() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setMaxResults(5);
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
        criteria.setMaxResults(null);
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
    }

    @Test public void testDocSearch_maxResultsIsZero() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setMaxResults(5);
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
        criteria.setMaxResults(0);
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(0, results.getSearchResults().size());
    }

    @Test public void testDocSearch_startAtIndex() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setMaxResults(5);
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
        criteria.setStartAtIndex(1);
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(2, results.getSearchResults().size());
    }

    @Test public void testDocSearch_startAtIndexMoreThanResuls() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setMaxResults(5);
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
        criteria.setStartAtIndex(5);
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(0, results.getSearchResults().size());

    }

    @Test public void testDocSearch_startAtIndexNegative() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setMaxResults(5);
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
        criteria.setStartAtIndex(-1);
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(0, results.getSearchResults().size());

    }

    @Test public void testDocSearch_startAtIndexZero() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        criteria.setMaxResults(5);
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());
        criteria.setStartAtIndex(0);
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());

    }

    /**
     * Tests that performing a search automatically saves the last search criteria
     */
    @Test public void testUnnamedDocSearchPersistence() throws Exception {
        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("bmcgough");
        Collection<UserOptions> allUserOptions_before = userOptionsService.findByWorkflowUser(user.getPrincipalId());
        List<UserOptions> namedSearches_before = userOptionsService.findByUserQualified(user.getPrincipalId(), "DocSearch.NamedSearch.%");

        assertEquals(0, namedSearches_before.size());
        assertEquals(0, allUserOptions_before.size());

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setTitle("*IN");
        criteria.setDateCreatedFrom(DateTime.now().minus(Years.ONE)); // otherwise one is set for us
        DocumentSearchCriteria c1 = criteria.build();
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), c1);

        Collection<UserOptions> allUserOptions_after = userOptionsService.findByWorkflowUser(user.getPrincipalId());
        List<UserOptions> namedSearches_after = userOptionsService.findByUserQualified(user.getPrincipalId(), "DocSearch.NamedSearch.%");

        // saves the "last doc search criteria"
        // and a pointer to the "last doc search criteria"
        assertEquals(allUserOptions_before.size() + 2, allUserOptions_after.size());
        assertEquals(namedSearches_before.size(), namedSearches_after.size());

        assertEquals("DocSearch.LastSearch.Holding0", userOptionsService.findByOptionId("DocSearch.LastSearch.Order".toString(), user.getPrincipalId()).getOptionVal());
        assertEquals(marshall(c1), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding0", user.getPrincipalId()).getOptionVal());

        // 2nd search

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setTitle("*IN-CFSG*");
        criteria.setDateCreatedFrom(DateTime.now().minus(Years.ONE)); // otherwise one is set for us
        DocumentSearchCriteria c2 = criteria.build();
        results = docSearchService.lookupDocuments(user.getPrincipalId(), c2);

        // still only 2 more user options
        assertEquals(allUserOptions_before.size() + 2, allUserOptions_after.size());
        assertEquals(namedSearches_before.size(), namedSearches_after.size());

        assertEquals("DocSearch.LastSearch.Holding1,DocSearch.LastSearch.Holding0", userOptionsService.findByOptionId("DocSearch.LastSearch.Order", user.getPrincipalId()).getOptionVal());
        assertEquals(marshall(c1), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding0", user.getPrincipalId()).getOptionVal());
        assertEquals(marshall(c2), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding1", user.getPrincipalId()).getOptionVal());
    }

     /**
     * Tests that performing a named search automatically saves the last search criteria as well as named search
     */
    @Test public void testNamedDocSearchPersistence() throws Exception {
        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("bmcgough");
        Collection<UserOptions> allUserOptions_before = userOptionsService.findByWorkflowUser(user.getPrincipalId());
        List<UserOptions> namedSearches_before = userOptionsService.findByUserQualified(user.getPrincipalId(), "DocSearch.NamedSearch.%");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setTitle("*IN");
        criteria.setSaveName("bytitle");
        criteria.setDateCreatedFrom(DateTime.now().minus(Years.ONE)); // otherwise one is set for us
        DocumentSearchCriteria c1 = criteria.build();
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), c1);

        Collection<UserOptions> allUserOptions_after = userOptionsService.findByWorkflowUser(user.getPrincipalId());
        List<UserOptions> namedSearches_after = userOptionsService.findByUserQualified(user.getPrincipalId(), "DocSearch.NamedSearch.%");

        assertEquals(allUserOptions_before.size() + 1, allUserOptions_after.size());
        assertEquals(namedSearches_before.size() + 1, namedSearches_after.size());

        assertEquals(marshall(c1), userOptionsService.findByOptionId("DocSearch.NamedSearch." + criteria.getSaveName(), user.getPrincipalId()).getOptionVal());

        // second search
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setTitle("*IN");
        criteria.setSaveName("bytitle2");
        criteria.setDateCreatedFrom(DateTime.now().minus(Years.ONE)); // otherwise one is set for us
        DocumentSearchCriteria c2 = criteria.build();
        results = docSearchService.lookupDocuments(user.getPrincipalId(), c2);

        allUserOptions_after = userOptionsService.findByWorkflowUser(user.getPrincipalId());
        namedSearches_after = userOptionsService.findByUserQualified(user.getPrincipalId(), "DocSearch.NamedSearch.%");

        // saves a second named search
        assertEquals(allUserOptions_before.size() + 2, allUserOptions_after.size());
        assertEquals(namedSearches_before.size() + 2, namedSearches_after.size());

        assertEquals(marshall(c2), userOptionsService.findByOptionId("DocSearch.NamedSearch." + criteria.getSaveName(), user.getPrincipalId()).getOptionVal());

    }

    protected static String marshall(DocumentSearchCriteria criteria) throws Exception {
        return DocumentSearchInternalUtils.marshalDocumentSearchCriteria(criteria);
    }

    @Test
    public void testDocSearch_criteriaModified() throws Exception {
        String principalId = getPrincipalId("ewestfal");

        // if no criteria is specified, the dateCreatedFrom is defaulted to today
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertTrue("criteria should have been modified", results.isCriteriaModified());
        assertNull("original date created from should have been null", criteria.getDateCreatedFrom());
        assertNotNull("modified date created from should be non-null", results.getCriteria().getDateCreatedFrom());
        assertEquals("Criteria date minus today's date should equal the constant value",
                KewApiConstants.DOCUMENT_SEARCH_NO_CRITERIA_CREATE_DATE_DAYS_AGO.intValue(),
                getDifferenceInDays(results.getCriteria().getDateCreatedFrom()));

        // now set some attributes which should still result in modified criteria since they don't count toward
        // determining if the criteria is empty or not
        criteria.setMaxResults(new Integer(50));
        criteria.setSaveName("myRadSearch");
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertTrue("criteria should have been modified", results.isCriteriaModified());
        assertNotNull("modified date created from should be non-null", results.getCriteria().getDateCreatedFrom());

        // now set the title, when only title is specified, date created from is defaulted
        criteria.setTitle("My rad title search!");
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertTrue("criteria should have been modified", results.isCriteriaModified());
        assertNotNull("modified date created from should be non-null", results.getCriteria().getDateCreatedFrom());
        assertEquals("Criteria date minus today's date should equal the constant value",
                Math.abs(KewApiConstants.DOCUMENT_SEARCH_DOC_TITLE_CREATE_DATE_DAYS_AGO.intValue()),
                getDifferenceInDays(results.getCriteria().getDateCreatedFrom()));

        // now set another field on the criteria, modification should *not* occur
        criteria.setApplicationDocumentId("12345");
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertFalse("criteria should *not* have been modified", results.isCriteriaModified());
        assertNull("modified date created from should still be null", results.getCriteria().getDateCreatedFrom());
        assertEquals("both criterias should be equal", criteria.build(), results.getCriteria());
    }

    /**
     * Test for https://test.kuali.org/jira/browse/KULRICE-1968 - Document search fails when users are missing
     * Tests that we can safely search on docs whose initiator no longer exists in the identity management system
     * This test searches by doc type name criteria.
     * @throws Exception
     */
    @Test public void testDocSearch_MissingInitiator() throws Exception {
        String documentTypeName = "SearchDocType";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        String userNetworkId = "arh14";
        // route a document to enroute and route one to final
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("testDocSearch_MissingInitiator");
        workflowDocument.route("routing this document.");

        // verify the document is enroute for jhopf
        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId("jhopf"),workflowDocument.getDocumentId());
        assertTrue(workflowDocument.isEnroute());
        assertTrue(workflowDocument.isApprovalRequested());

        // now nuke the initiator...
        new JdbcTemplate(TestHarnessServiceLocator.getDataSource()).execute("update " + KREW_DOC_HDR_T + " set " + INITIATOR_COL + " = 'bogus user' where DOC_HDR_ID = " + workflowDocument.getDocumentId());


        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("jhopf");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search returned invalid number of documents", 1, results.getSearchResults().size());
    }

    /**
     * Test for https://test.kuali.org/jira/browse/KULRICE-1968 - Tests that we get an error if we try and search on an initiator that doesn't exist in the IDM system
     * @throws Exception
     */
    @Test public void testDocSearch_SearchOnMissingInitiator() throws Exception {
        String documentTypeName = "SearchDocType";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        String userNetworkId = "arh14";
        // route a document to enroute and route one to final
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("testDocSearch_MissingInitiator");
        workflowDocument.route("routing this document.");

        // verify the document is enroute for jhopf
        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId("jhopf"),workflowDocument.getDocumentId());
        assertTrue(workflowDocument.isEnroute());
        assertTrue(workflowDocument.isApprovalRequested());

        // now nuke the initiator...
        new JdbcTemplate(TestHarnessServiceLocator.getDataSource()).execute("update " + KREW_DOC_HDR_T + " set " + INITIATOR_COL + " = 'bogus user' where DOC_HDR_ID = " + workflowDocument.getDocumentId());


        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("jhopf");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setInitiatorPrincipalName("bogus user");

        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(),
                criteria.build());
        int size = results.getSearchResults().size();
        assertTrue("Searching by an invalid initiator should return nothing", size == 0);

    }

    @Test public void testDocSearch_RouteNodeName() throws Exception {
        loadXmlFile("DocSearchTest_RouteNode.xml");
        String documentTypeName = "SearchDocType_RouteNodeTest";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        String userNetworkId = "rkirkend";

        // route a document to enroute and route one to final
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");
        // verify the document is enroute for jhopf
        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId("jhopf"),workflowDocument.getDocumentId());
        assertTrue(workflowDocument.isEnroute());
        assertTrue(workflowDocument.isApprovalRequested());
        workflowDocument.approve("");
        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId("jhopf"),workflowDocument.getDocumentId());
        assertTrue(workflowDocument.isFinal());
        workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");
        // verify the document is enroute for jhopf
        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId("jhopf"),workflowDocument.getDocumentId());
        assertTrue(workflowDocument.isEnroute());
        assertTrue(workflowDocument.isApprovalRequested());


        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(userNetworkId);
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(),
                criteria.build());
        assertEquals("Search returned invalid number of documents", 2, results.getSearchResults().size());

        criteria.setRouteNodeName(getRouteNodeForSearch(documentTypeName,workflowDocument.getNodeNames()));
        criteria.setRouteNodeLookupLogic(RouteNodeLookupLogic.EXACTLY);
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search returned invalid number of documents", 1, results.getSearchResults().size());

        // load the document type again to change the route node ids
        loadXmlFile("DocSearchTest_RouteNode.xml");

        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId("jhopf"),workflowDocument.getDocumentId());
        assertTrue(workflowDocument.isEnroute());
        assertTrue(workflowDocument.isApprovalRequested());
        criteria.setRouteNodeName(getRouteNodeForSearch(documentTypeName, workflowDocument.getNodeNames()));
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search returned invalid number of documents", 1, results.getSearchResults().size());

    }

    private String getRouteNodeForSearch(String documentTypeName, Set<String> nodeNames) {
        assertEquals(1,	nodeNames.size());
    String expectedNodeName = nodeNames.iterator().next();
        List routeNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName), true);
        for (Iterator iterator = routeNodes.iterator(); iterator.hasNext();) {
        RouteNode node = (RouteNode) iterator.next();
        if (expectedNodeName.equals(node.getRouteNodeName())) {
        return node.getRouteNodeName();
        }
    }
        return null;
    }

    @Test public void testGetNamedDocSearches() throws Exception {
        List namedSearches = docSearchService.getNamedSearches(getPrincipalId("bmcgough"));
        assertNotNull(namedSearches);
    }

    private static int getDifferenceInDays(DateTime compareDate) {
        return Days.daysBetween(compareDate, new DateTime()).getDays();
    }

    /**
     * Tests searching against document search attrs
     * @throws Exception
     */
    @Test public void testDocSearchWithAttributes() throws Exception {
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");
        DocumentSearchCriteria.Builder builder = DocumentSearchCriteria.Builder.create();
        builder.setDocumentTypeName("SearchDocType");
        builder.setSaveName("testDocSearchWithAttributes");
        Map<String, List<String>> docAttrs = new HashMap<String, List<String>>();
        docAttrs.put(TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, Arrays.asList(new String[]{TestXMLSearchableAttributeString.SEARCH_STORAGE_VALUE}));
        builder.setDocumentAttributeValues(docAttrs);

        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, builder.build());
        assertEquals(docIds.length, results.getSearchResults().size());

        DocumentSearchCriteria loaded = docSearchService.getNamedSearchCriteria(principalId, builder.getSaveName());
        assertNotNull(loaded);
        assertEquals(docAttrs, loaded.getDocumentAttributeValues());

        // re-run saved search
        results = docSearchService.lookupDocuments(principalId, loaded);
        assertEquals(docIds.length, results.getSearchResults().size());
    }

    /**
     * Tests the usage of wildcards on the regular document search attributes.
     * @throws Exception
     */
    @Test public void testDocSearch_WildcardsOnRegularAttributes() throws Exception {
        // TODO: Add some wildcard testing for the document type attribute once wildcards are usable with it.

        // Route some test documents.
        String[] docIds = routeTestDocs();

        String principalId = getPrincipalId("bmcgough");
        DocumentSearchCriteria.Builder criteria = null;
        DocumentSearchResults results = null;

        /**
         * BEGIN - commenting out until we can resolve issues with person service not returning proper persons based on wildcards and various things
         */
        // Test the wildcards on the initiator attribute.
        String[] searchStrings = {"!quickstart", "!quickstart&&!rkirkend", "!admin", "user1", "quickstart|bmcgough",
        		"admin|rkirkend", ">bmcgough", ">=rkirkend", "<bmcgough", "<=quickstart", ">bmcgough&&<=rkirkend", "<rkirkend&&!bmcgough",
        		"?mc?oug?", "*t", "*i?k*", "*", "!quick*", "!*g*&&!*k*", "quickstart..rkirkend"};
        int[] expectedResults = {2, 1, 3, 0, 2, 1, 2, 1, 0, 2, 2, 1, 1, 1, 2, 3, 2, 0, 2/*1*/};
        for (int i = 0; i < searchStrings.length; i++) {
        	criteria = DocumentSearchCriteria.Builder.create();
        	criteria.setInitiatorPrincipalName(searchStrings[i]);
        	results = docSearchService.lookupDocuments(principalId, criteria.build());
        	assertEquals("Initiator search at index " + i + " retrieved the wrong number of documents.", expectedResults[i], results.getSearchResults().size());
        }

        // Test the wildcards on the approver attribute.
        searchStrings = new String[] {"jhopf","!jhopf", ">jhopf", "<jjopf", ">=quickstart", "<=jhopf", "jhope..jhopg", "?hopf", "*i*", "!*f", "j*"};
        expectedResults = new int[] {1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1};
        for (int i = 0; i < searchStrings.length; i++) {
        	criteria = DocumentSearchCriteria.Builder.create();
        	criteria.setApproverPrincipalName(searchStrings[i]);
        	results = docSearchService.lookupDocuments(principalId, criteria.build());
        	assertEquals("Approver search at index " + i + " retrieved the wrong number of documents.", expectedResults[i], results.getSearchResults().size());
        }

        // Test the wildcards on the viewer attribute.
        searchStrings = new String[] {"jhopf","!jhopf", ">jhopf", "<jjopf", ">=quickstart", "<=jhopf", "jhope..jhopg", "?hopf", "*i*", "!*f", "j*"};
        expectedResults = new int[] {3, 0, 0, 3, 0, 3, 3, 3, 0, 0, 3};
        for (int i = 0; i < searchStrings.length; i++) {
        	criteria = DocumentSearchCriteria.Builder.create();
        	criteria.setViewerPrincipalName(searchStrings[i]);
        	results = docSearchService.lookupDocuments(principalId, criteria.build());
        	if(expectedResults[i] !=  results.getSearchResults().size()){
        		assertEquals("Viewer search at index " + i + " retrieved the wrong number of documents.", expectedResults[i], results.getSearchResults().size());
        	}
        }

        /**
         * END
         */

        // Test the wildcards on the document/notification ID attribute. The string wildcards should work, since the doc ID is not a string.
        searchStrings = new String[] {"!"+docIds[0], docIds[1]+"|"+docIds[2], "<="+docIds[1], ">="+docIds[2], "<"+docIds[0]+"&&>"+docIds[2],
                ">"+docIds[1], "<"+docIds[2]+"&&!"+docIds[0], docIds[0]+".."+docIds[2], "?"+docIds[1]+"*", "?"+docIds[1].substring(1)+"*", "?9*7"};
        expectedResults = new int[] {2, 2, 2, 1, 0, 1, 1, 3, 0, 1, 0};
        for (int i = 0; i < searchStrings.length; i++) {
            criteria = DocumentSearchCriteria.Builder.create();
            criteria.setDocumentId(searchStrings[i]);
            results = docSearchService.lookupDocuments(principalId, criteria.build());
            assertEquals("Doc ID search at index " + i + " retrieved the wrong number of documents.", expectedResults[i], results.getSearchResults().size());
        }

        // Test the wildcards on the application document/notification ID attribute. The string wildcards should work, since the app doc ID is a string.
        searchStrings = new String[] {"6543", "5432|4321", ">4321", "<=5432", ">=6543", "<3210", "!3210", "!5432", "!4321!5432", ">4321&&!6543",
                "*5?3*", "*", "?3?1", "!*43*", "!???2", ">43*1", "<=5432&&!?32?", "5432..6543"};
        expectedResults = new int[] {1, 2, 2, 2, 1, 0, 3, 2, 1, 1, 2, 3, 1, 0, 2, 3, 1, 2/*1*/};
        for (int i = 0; i < searchStrings.length; i++) {
            criteria = DocumentSearchCriteria.Builder.create();
            criteria.setApplicationDocumentId(searchStrings[i]);
            results = docSearchService.lookupDocuments(principalId, criteria.build());
            if(expectedResults[i] !=  results.getSearchResults().size()){
                assertEquals("App doc ID search at index " + i + " retrieved the wrong number of documents.", expectedResults[i], results.getSearchResults().size());
            }
        }

        // Test the wildcards on the title attribute.
        searchStrings = new String[] {"Some New Document", "Document Number 2|The New Doc", "!The New Doc", "!Some New Document!Document Number 2",
                "!The New Doc&&!Some New Document", ">Document Number 2", "<=Some New Document", ">=The New Doc&&<Some New Document", ">A New Doc",
                "<Some New Document|The New Doc", ">=Document Number 2&&!Some New Document", "*Docu??nt*", "*New*", "The ??? Doc", "*Doc*", "*Number*",
                "Some New Document..The New Doc", "Document..The", "*New*&&!*Some*", "!The ??? Doc|!*New*"};
        expectedResults = new int[] {1, 2, 2, 1, 1, 2, 2, 0, 3, 2, 2, 2, 2, 1, 3, 1, 2/*1*/, 2, 1, 2};
        for (int i = 0; i < searchStrings.length; i++) {
            criteria = DocumentSearchCriteria.Builder.create();
            criteria.setTitle(searchStrings[i]);
            results = docSearchService.lookupDocuments(principalId, criteria.build());
            if(expectedResults[i] !=  results.getSearchResults().size()){
                assertEquals("Doc title search at index " + i + " retrieved the wrong number of documents.", expectedResults[i], results.getSearchResults().size());
            }
        }

    }

    @Test public void testAdditionalDocumentTypesCriteria() throws Exception {
        String[] docIds = routeTestDocs();
        String docId2 = routeTestDoc2();

        String principalId = getPrincipalId("bmcgough");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");

        // TODO finish this test
        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(3, results.getSearchResults().size());

        criteria.setDocumentTypeName("SearchDocType2");
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(1, results.getSearchResults().size());

        criteria.getAdditionalDocumentTypeNames().add("SearchDocType");
        results = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals(4, results.getSearchResults().size());
    }

    /**
     * Tests searching on document status and document status category
     */
    @Test public void testDocumentStatusSearching() {
        String dt = "SearchDocType";
        String pid = getPrincipalIdForName("quickstart");
        WorkflowDocument initiated = WorkflowDocumentFactory.createDocument(pid, dt);
        WorkflowDocument saved = WorkflowDocumentFactory.createDocument(pid, dt);
        saved.saveDocument("saved");
        assertEquals(DocumentStatus.SAVED, saved.getStatus());

        WorkflowDocument enroute = WorkflowDocumentFactory.createDocument(pid, dt);
        enroute.route("routed");
        assertEquals(DocumentStatus.ENROUTE, enroute.getStatus());

        WorkflowDocument exception = WorkflowDocumentFactory.createDocument(pid, dt);
        exception.route("routed");
        exception.placeInExceptionRouting("placed in exception routing");
        assertEquals(DocumentStatus.EXCEPTION, exception.getStatus());

        // no acks on this doc, can't test?
        //WorkflowDocument processed = WorkflowDocumentFactory.createDocument(pid, dt);
        //processed.route("routed");

        WorkflowDocument finl = WorkflowDocumentFactory.createDocument(pid, dt);
        finl.route("routed");
        finl.switchPrincipal(getPrincipalId("jhopf"));
        finl.approve("approved");
        assertEquals(DocumentStatus.FINAL, finl.getStatus());

        WorkflowDocument canceled = WorkflowDocumentFactory.createDocument(pid, dt);
        canceled.cancel("canceled");
        assertEquals(DocumentStatus.CANCELED, canceled.getStatus());

        WorkflowDocument disapproved = WorkflowDocumentFactory.createDocument(pid, dt);
        disapproved.route("routed");
        disapproved.switchPrincipal(getPrincipalId("jhopf"));
        RequestedActions ra = disapproved.getRequestedActions();
        disapproved.disapprove("disapproved");
        assertEquals(DocumentStatus.DISAPPROVED, disapproved.getStatus());

        assertDocumentStatuses(dt, pid, 1, 1, 1, 1, 0, 1, 1, 1);
    }

    /**
     * Asserts that documents are present in the given statuses, including document status categories (this requires that
     * no docs are in the system prior to routing of the test docs)
     */
    protected void assertDocumentStatuses(String documentType, String principalId, int initiated, int saved, int enroute, int exception,
                                                          int processed, int finl, int canceled, int disapproved) {
        assertDocumentStatus(documentType, principalId, DocumentStatus.INITIATED, initiated);
        assertDocumentStatus(documentType, principalId, DocumentStatus.SAVED, saved);
        assertDocumentStatus(documentType, principalId, DocumentStatus.ENROUTE, enroute);
        assertDocumentStatus(documentType, principalId, DocumentStatus.EXCEPTION, exception);

        assertDocumentStatusCategory(documentType, principalId, DocumentStatusCategory.PENDING,
                initiated + saved + enroute + exception);

        assertDocumentStatus(documentType, principalId, DocumentStatus.PROCESSED, processed);
        assertDocumentStatus(documentType, principalId, DocumentStatus.FINAL, finl);

        assertDocumentStatusCategory(documentType, principalId, DocumentStatusCategory.SUCCESSFUL, processed + finl);

        assertDocumentStatus(documentType, principalId, DocumentStatus.CANCELED, canceled);
        assertDocumentStatus(documentType, principalId, DocumentStatus.DISAPPROVED, finl);

        assertDocumentStatusCategory(documentType, principalId, DocumentStatusCategory.UNSUCCESSFUL,
                canceled + disapproved);
    }

    /**
     * Asserts that there are a certain number of docs in the given status
     */
    protected void assertDocumentStatus(String documentType, String principalId, DocumentStatus status, int num) {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentType);
        criteria.setDocumentStatuses(Arrays.asList(new DocumentStatus[] { status }));
        DocumentSearchResults result = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals("Expected " + num + " documents in status " + status, num, result.getSearchResults().size());
    }

    /**
     * Asserts that there are a certain number of docs in the given document status category
     */
    protected void assertDocumentStatusCategory(String documentType, String principalId, DocumentStatusCategory status, int num) {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentType);
        criteria.setDocumentStatusCategories(Arrays.asList(new DocumentStatusCategory[]{status}));
        DocumentSearchResults result = docSearchService.lookupDocuments(principalId, criteria.build());
        assertEquals("Expected " + num + " documents in status category " + status, num,
                result.getSearchResults().size());
    }

    private static final class TestDocData {
        private TestDocData() { throw new IllegalStateException("leave me alone"); }

        static String docTypeName = "SearchDocType";
        static String[] principalNames = {"bmcgough", "quickstart", "rkirkend"};
        static String[] titles = {"The New Doc", "Document Number 2", "Some New Document"};
        static String[] appDocIds = {"6543", "5432", "4321"};
        static String[] appDocStatuses = {"Submitted", "Pending", "Completed"};
        static String[] approverNames = {null, "jhopf", null};
    }

    /**
     * Routes some test docs for searching
     * @return String[] of doc ids
     */
    protected String[] routeTestDocs() {
        // Route some test documents.
        String[] docIds = new String[TestDocData.titles.length];

        for (int i = 0; i < TestDocData.titles.length; i++) {
            WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(
                    getPrincipalId(TestDocData.principalNames[i]), TestDocData.docTypeName);
            workflowDocument.setTitle(TestDocData.titles[i]);
            workflowDocument.setApplicationDocumentId(TestDocData.appDocIds[i]);
            workflowDocument.route("routing this document.");

            docIds[i] = workflowDocument.getDocumentId();

            if (TestDocData.approverNames[i] != null) {
                workflowDocument.switchPrincipal(getPrincipalId(TestDocData.approverNames[i]));
                workflowDocument.approve("approving this document.");
            }

            workflowDocument.setApplicationDocumentStatus(TestDocData.appDocStatuses[i]);
            workflowDocument.saveDocumentData();
        }

        return docIds;
    }

    /**
     * "Saves" a single instance of a "SearchDocType2" document and returns it's id.
     */
    protected String routeTestDoc2() {
        // Route some test documents.
        String docTypeName = "SearchDocType2";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId("ewestfal"), docTypeName);
        workflowDocument.setTitle("Search Doc Type 2!");
        workflowDocument.saveDocument("saving the document");
        return workflowDocument.getDocumentId();
    }

    private String getPrincipalId(String principalName) {
        return KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName).getPrincipalId();
    }

    @Test
    public void testDocSearch_maxResultsCap() throws Exception {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        int maxResultsCap = docSearchService.getMaxResultCap(criteria.build());
        assertEquals(500, maxResultsCap);

        criteria.setMaxResults(5);
        int maxResultsCap1 = docSearchService.getMaxResultCap(criteria.build());
        assertEquals(5, maxResultsCap1);

        criteria.setMaxResults(2);
        int maxResultsCap2 = docSearchService.getMaxResultCap(criteria.build());
        assertEquals(2, maxResultsCap2);
    }

    @Test
    public void testDocSearch_fetchMoreIterationLimit() throws Exception {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName("SearchDocType");
        int fetchIterationLimit = docSearchService.getFetchMoreIterationLimit();
        assertEquals(10, fetchIterationLimit);

    }
}
