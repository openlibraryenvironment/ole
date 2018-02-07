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
package org.kuali.rice.kns.workflow;

import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.document.SearchAttributeIndexTestDocument;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Assert;

/**
 * tests that a document, which goes through a regular or blanket approval process, is indexed correctly
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SearchAttributeIndexRequestTest extends KRADTestCase {
	static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SearchAttributeIndexRequestTest.class);
	final static String SEARCH_ATTRIBUTE_INDEX_DOCUMENT_TEST_DOC_TYPE = "SearchAttributeIndexTestDocument";
	
	enum DOCUMENT_FIXTURE {
		NORMAL_DOCUMENT("hippo","routing");
		
		private String constantString;
		private String routingString;
		private DOCUMENT_FIXTURE(String constantString, String routingString) {
			this.constantString = constantString;
			this.routingString = routingString;
		}
		
		public SearchAttributeIndexTestDocument getDocument(DocumentService documentService) throws Exception {
			SearchAttributeIndexTestDocument doc = (SearchAttributeIndexTestDocument)documentService.getNewDocument(SearchAttributeIndexRequestTest.SEARCH_ATTRIBUTE_INDEX_DOCUMENT_TEST_DOC_TYPE);
			doc.initialize(constantString, routingString);
			return doc;
		}
	}
	
	/**
	 * Tests that a document, which goes through a regular approval process, is indexed correctly
	 */
	@Test
	public void regularApproveTest() throws Exception {
		LOG.warn("message.delivery state: "+ CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                "message.delivery"));
		
		final DocumentService documentService = KRADServiceLocatorWeb.getDocumentService();
		final String principalName = "quickstart";
        final String principalId = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName).getPrincipalId();
        GlobalVariables.setUserSession(new UserSession(principalName));
        RouteContext.clearCurrentRouteContext();

		SearchAttributeIndexTestDocument document = DOCUMENT_FIXTURE.NORMAL_DOCUMENT.getDocument(documentService);
		document.getDocumentHeader().setDocumentDescription("Routed SAIndexTestDoc");
		final String documentNumber = document.getDocumentNumber();
		final DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(SearchAttributeIndexRequestTest.SEARCH_ATTRIBUTE_INDEX_DOCUMENT_TEST_DOC_TYPE);
		
		documentService.routeDocument(document, "Routed SearchAttributeIndexTestDocument", null);
		
		document = (SearchAttributeIndexTestDocument)documentService.getByDocumentHeaderId(documentNumber);
		DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentNumber);
						
		assertDDSearchableAttributesWork(docType,principalId,"routeLevelCount",
				new String[] {"1","0","2","7"},
				new int[] {1, 0, 0, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"constantString",
				new String[] {"hippo","monkey"},
				new int[] {1, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"routedString",
				new String[] {"routing","hippo"},
				new int[] {1, 0}
		);
		
		GlobalVariables.setUserSession(new UserSession("user1"));
		document = (SearchAttributeIndexTestDocument)documentService.getByDocumentHeaderId(documentNumber);
		documentService.approveDocument(document, "User1 approved document", null);
		
		routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentNumber);
						
		assertDDSearchableAttributesWork(docType,principalId,"routeLevelCount",
				new String[] {"1","0","2","7"},
				new int[] {0, 0, 1, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"constantString",
				new String[] {"hippo","monkey"},
				new int[] {1, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"routedString",
				new String[] {"routing","hippo"},
				new int[] {1, 0}
		);
		
		LOG.info("Read Access Count not at expected value: "+document.getReadAccessCount());
		
		GlobalVariables.setUserSession(new UserSession("user2"));
		document = (SearchAttributeIndexTestDocument)documentService.getByDocumentHeaderId(documentNumber);
		documentService.approveDocument(document, "User1 approved document", null);
		
		routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentNumber);
				
		assertDDSearchableAttributesWork(docType,principalId,"routeLevelCount",
				new String[] {"1","0","2","3","4","7"},
				new int[] {0, 0, 0, 1, 0, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"constantString",
				new String[] {"hippo","monkey"},
				new int[] {1, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"routedString",
				new String[] {"routing","hippo"},
				new int[] {1, 0}
		);
		
		LOG.info("Read Access Count not at expected value: "+document.getReadAccessCount());
		
		GlobalVariables.setUserSession(new UserSession("user3"));
		document = (SearchAttributeIndexTestDocument)documentService.getByDocumentHeaderId(documentNumber);
		documentService.approveDocument(document, "User3 approved document", null);
		
		routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentNumber);
						
		assertDDSearchableAttributesWork(docType,principalId,"routeLevelCount",
				new String[] {"1","0","2","3","4","7"},
				new int[] {0, 0, 0, 1, 0, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"constantString",
				new String[] {"hippo","monkey"},
				new int[] {1, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"routedString",
				new String[] {"routing","hippo"},
				new int[] {1, 0}
		);
		
		LOG.info("Read Access Count not at expected value: "+document.getReadAccessCount());
		
		GlobalVariables.setUserSession(null);
	}
	
	/**
	 * Tests that a blanket approved document is indexed correctly
	 */
	@Test
	public void blanketApproveTest() throws Exception {
		LOG.warn("message.delivery state: "+ CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                "message.delivery"));
		
		final DocumentService documentService = KRADServiceLocatorWeb.getDocumentService();
		final String principalName = "admin";
        final String principalId = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName).getPrincipalId();
        GlobalVariables.setUserSession(new UserSession(principalName));

		SearchAttributeIndexTestDocument document = DOCUMENT_FIXTURE.NORMAL_DOCUMENT.getDocument(documentService);
		document.getDocumentHeader().setDocumentDescription("Blanket Approved SAIndexTestDoc");
		final String documentNumber = document.getDocumentNumber();
		final DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(SearchAttributeIndexRequestTest.SEARCH_ATTRIBUTE_INDEX_DOCUMENT_TEST_DOC_TYPE);
				
		documentService.blanketApproveDocument(document, "Blanket Approved SearchAttributeIndexTestDocument", null);
		
		document = (SearchAttributeIndexTestDocument)documentService.getByDocumentHeaderId(documentNumber);
		DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentNumber);
						
		assertDDSearchableAttributesWork(docType,principalId,"routeLevelCount",
				new String[] {"1","0","2","3","7"},
				new int[] {0, 0, 0, 1, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"constantString",
				new String[] {"hippo","monkey"},
				new int[] {1, 0}
		);
		
		assertDDSearchableAttributesWork(docType,principalId,"routedString",
				new String[] {"routing","hippo"},
				new int[] {1, 0}
		);
		
		LOG.info("Read Access Count not at expected value: "+document.getReadAccessCount());
		
		GlobalVariables.setUserSession(null);
	}
	
	/**
     * A convenience method for testing wildcards on data dictionary searchable attributes.
     *
     * @param docType The document type containing the attributes.
     * @param principalId The ID of the user performing the search.
     * @param fieldName The name of the field on the test document.
     * @param searchValues The search expressions to test. Has to be a String array (for regular fields) or a String[] array (for multi-select fields).
     * @param resultSizes The number of expected documents to be returned by the search; use -1 to indicate that an error should have occurred.
     * @throws Exception
     */
    private void assertDDSearchableAttributesWork(DocumentType docType, String principalId, String fieldName, Object[] searchValues,
    		int[] resultSizes) throws Exception {
    	if (!(searchValues instanceof String[]) && !(searchValues instanceof String[][])) {
    		throw new IllegalArgumentException("'searchValues' parameter has to be either a String[] or a String[][]");
    	}
    	DocumentSearchCriteria.Builder criteria = null;
        DocumentSearchResults results = null;
        DocumentSearchService docSearchService = KEWServiceLocator.getDocumentSearchService();
        for (int i = 0; i < resultSizes.length; i++) {
        	criteria = DocumentSearchCriteria.Builder.create();
        	criteria.setDocumentTypeName(docType.getName());
        	criteria.addDocumentAttributeValue(fieldName, searchValues[i].toString());
        	try {
        		results = docSearchService.lookupDocuments(principalId, criteria.build());
        		if (resultSizes[i] < 0) {
        			Assert.fail(fieldName + "'s search at loop index " + i + " should have thrown an exception");
        		}
        		if(resultSizes[i] != results.getSearchResults().size()){
        			assertEquals(fieldName + "'s search results at loop index " + i + " returned the wrong number of documents.", resultSizes[i], results.getSearchResults().size());
        		}
        	} catch (Exception ex) {
        		if (resultSizes[i] >= 0) {
        			Assert.fail(fieldName + "'s search at loop index " + i + " should not have thrown an exception");
        		}
        	}
        	GlobalVariables.clear();
        }
    }
}
