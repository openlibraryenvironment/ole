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
package org.kuali.rice.kew.docsearch.xml;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils;
import org.kuali.rice.kew.docsearch.DocumentSearchTestBase;
import org.kuali.rice.kew.docsearch.SearchableAttributeLongValue;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeDateTime;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeFloat;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeLong;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeString;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.test.BaselineTestCase;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests the StandardGenericXMLSearchableAttribute.
 *
 * KULWF-654: Tests the resolution to this issue by configuring a CustomActionListAttribute as well as a
 * searchable attribute.
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class StandardGenericXMLSearchableAttributeTest extends DocumentSearchTestBase {

    protected void loadTestData() throws Exception {
        loadXmlFile("XmlConfig.xml");
    }

    @Test public void testXMLStandardSearchableAttributeWithInvalidValue() throws Exception {
        String documentTypeName = "SearchDocTypeStandardSearchDataType";
        String userNetworkId = "rkirkend";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);

        /*
         *   Below we are using the keys and values from the custom searchable attribute classes' static constants but
         *   this is only for convenience as those should always be valid values to test for.
         */
        // adding string value in what should be a long searchable attribute
        WorkflowAttributeDefinition.Builder longXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeStdLong");
        longXMLDef.addPropertyDefinition(TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, "123x23");
        workflowDocument.addSearchableDefinition(longXMLDef.build());

        workflowDocument.setTitle("Routing style");
        try {
            workflowDocument.route("routing this document.");
            fail("Document should be unroutable with invalid searchable attribute value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * The call to TestUtilities below is needed because when exception routing spawns a new thread (see
         * TestExceptionRoutingServiceImpl class) the next test will begin before the exception thread is complete and
         * cause errors. This was originally discovered because the test method
         * testXMLStandardSearchableAttributesWithDataType() would run and get errors loading xml data for workgroups
         * perhaps because the exception thread was keeping the cache around and now allowing it to be cleared?
         */
        TestUtilities.waitForExceptionRouting();
    }

    @Test public void testXMLStandardSearchableAttributesWithDataType() throws Exception {
        String documentTypeName = "SearchDocTypeStandardSearchDataType";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        String userNetworkId = "rkirkend";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);

        /*
         *   Below we are using the keys and values from the custom searchable attribute classes' static constants but
         *   this is only for convenience as those should always be valid values to test for.
         */
        int i = 0;
        // adding string searchable attribute
        i++;
        WorkflowAttributeDefinition.Builder stringXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttribute");
        stringXMLDef.addPropertyDefinition(TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeString.SEARCH_STORAGE_VALUE);
        workflowDocument.addSearchableDefinition(stringXMLDef.build());
        // adding long searchable attribute
        i++;
        WorkflowAttributeDefinition.Builder longXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeStdLong");
        longXMLDef.addPropertyDefinition(TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString());
        workflowDocument.addSearchableDefinition(longXMLDef.build());
        // adding float searchable attribute
        i++;
        WorkflowAttributeDefinition.Builder floatXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeStdFloat");
        floatXMLDef.addPropertyDefinition(TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString());
        workflowDocument.addSearchableDefinition(floatXMLDef.build());
        // adding string searchable attribute
        i++;
        WorkflowAttributeDefinition.Builder dateXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeStdDateTime");
        dateXMLDef.addPropertyDefinition(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, DocumentSearchInternalUtils
                .getDisplayValueWithDateOnly(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE));
        workflowDocument.addSearchableDefinition(dateXMLDef.build());

        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(userNetworkId), workflowDocument.getDocumentId());
        DocumentRouteHeaderValue doc = KEWServiceLocator.getRouteHeaderService().getRouteHeader(workflowDocument.getDocumentId());

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);
        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(userNetworkId);

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY,
                TestXMLSearchableAttributeString.SEARCH_STORAGE_VALUE);
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        DocumentSearchCriteria.Builder criteria2 = DocumentSearchCriteria.Builder.create();
        criteria2.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria2, TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, "fred");
        DocumentSearchResults results2 = docSearchService.lookupDocuments(user.getPrincipalId(), criteria2.build());

        assertEquals("Search results should be empty.", 0, results2.getSearchResults().size());

        DocumentSearchCriteria.Builder criteria3 = DocumentSearchCriteria.Builder.create();
        criteria3.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria3, "fakeproperty", "doesntexist");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria3.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("LookupException"));
        }

        criteria = null;
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString());
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria2 = null;
        criteria2 = DocumentSearchCriteria.Builder.create();
        criteria2.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria2, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, "1111111");
        results2 = docSearchService.lookupDocuments(user.getPrincipalId(), criteria2.build());
        assertEquals("Search results should be empty.", 0, results2.getSearchResults().size());

        criteria3 = null;
        criteria3 = DocumentSearchCriteria.Builder.create();
        criteria3.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria3, "fakeymcfakefake", "99999999");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria3.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("LookupException"));
        }

        criteria = null;
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY,
                TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString());
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria2 = null;
        criteria2 = DocumentSearchCriteria.Builder.create();
        criteria2.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria2, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, "215.3548");
        results2 = docSearchService.lookupDocuments(user.getPrincipalId(), criteria2.build());
        assertEquals("Search results should be empty.", 0, results2.getSearchResults().size());

        criteria3 = null;
        criteria3 = DocumentSearchCriteria.Builder.create();
        criteria3.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria3, "fakeylostington", "9999.9999");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria3.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException wsee) {
            assertTrue(wsee.getMessage().contains("LookupException"));
        }

        criteria = null;
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY,
                DocumentSearchInternalUtils.getDisplayValueWithDateOnly(new Timestamp(
                        TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE_IN_MILLS)));
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria2 = null;
        criteria2 = DocumentSearchCriteria.Builder.create();
        criteria2.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria2, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, "07/06/1979");
        results2 = docSearchService.lookupDocuments(user.getPrincipalId(), criteria2.build());
        assertEquals("Search results should be empty.", 0, results2.getSearchResults().size());

        criteria3 = null;
        criteria3 = DocumentSearchCriteria.Builder.create();
        criteria3.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria3, "lastingsfakerson", "07/06/2007");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria3.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException wsee) {
            assertTrue(wsee.getMessage().contains("LookupException"));
        }
    }

    @Test public void testRouteDocumentWithSearchableAttribute() throws Exception {
        String documentTypeName = "SearchDocType";
        String key = "givenname";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), documentTypeName);
        WorkflowAttributeDefinition.Builder givennameXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttribute");

        workflowDocument.setApplicationContent("<test></test>");

        givennameXMLDef.addPropertyDefinition(key, "jack");
        workflowDocument.addSearchableDefinition(givennameXMLDef.build());

        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = null;
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "fred");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = null;
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeproperty", "doesntexist");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException wsee) {
            assertTrue(wsee.getMessage().contains("LookupException"));
        }
    }

    @Test public void testDocumentSearchAttributeWildcarding() throws Exception {
        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        String documentTypeName = "SearchDocType";
        String key = "givenname";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(
                documentTypeName);
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"),
                documentTypeName);
        WorkflowAttributeDefinition.Builder givennameXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttribute");

        workflowDocument.setApplicationContent("<test></test>");

        givennameXMLDef.addPropertyDefinition(key, "jack");
        workflowDocument.addSearchableDefinition(givennameXMLDef.build());

        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "ja*");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "ja");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should have one document.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "*ack");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());
    }

    @Test public void testDocumentSearchAttributeWildcardingDisallow() throws Exception {
        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        String documentTypeName = "SearchDocTypeStandardSearchDataType";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        String userNetworkId = "rkirkend";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);

        /*
         *   Below we are using the keys and values from the custom searchable attribute classes' static constants but
         *   this is only for convenience as those should always be valid values to test for.
         */
        WorkflowAttributeDefinition.Builder longXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeStdLong");
        longXMLDef.addPropertyDefinition(TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString());
        workflowDocument.addSearchableDefinition(longXMLDef.build());
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(userNetworkId);

        String validSearchValue = TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString();
        DocumentSearchCriteria.Builder criteria = null;
        DocumentSearchResults results = null;
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, validSearchValue);
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY,
                "*" + validSearchValue.substring(2));

        if ((new SearchableAttributeLongValue()).allowsWildcards()) {
            results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            assertEquals("Search results should be empty using wildcard '*' value.", 0, results.getSearchResults().size());
        } else {
            try {
                docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
                fail("Search results should be throwing a validation exception for use of the character '*' without allowing wildcards");
            } catch (WorkflowServiceErrorException wsee) {}
        }

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, validSearchValue.substring(
                0, (validSearchValue.length() - 2)));
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should be empty trying to use assumed ending wildcard.", 0, results.getSearchResults().size());
    }

    @Test public void testDocumentSearchAttributeCaseSensitivity() throws Exception {
        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);
        String documentTypeName = "SearchDocTypeCaseSensitivity";
        String networkId = "rkirkend";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);

        String key = "givenname";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(networkId), documentTypeName);
        WorkflowAttributeDefinition.Builder givennameXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttribute");
        givennameXMLDef.addPropertyDefinition(key, "jack");
        workflowDocument.addSearchableDefinition(givennameXMLDef.build());
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(networkId);
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "JACK");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jAck");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jacK");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        key = "givenname_nocase";
        workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(networkId), documentTypeName);
        WorkflowAttributeDefinition.Builder givenname_nocaseXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttribute_CaseInsensitive");
        givenname_nocaseXMLDef.addPropertyDefinition(key, "jaCk");
        workflowDocument.addSearchableDefinition(givenname_nocaseXMLDef.build());
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "JACK");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jaCk");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jacK");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jAc");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jA*");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "*aCk");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());
    }

    /**
     * Tests searching with client-generated documentContent which is just malformed XML.
     * @throws WorkflowException
     */
    @Test public void testRouteDocumentWithMalformedSearchableAttributeContent() throws WorkflowException {
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "SearchDocType");

        workflowDocument.setApplicationContent("hey, <I'm Not ] Even & XML");

        workflowDocument.setTitle("Routing style");
        try {
            workflowDocument.route("routing this document.");
            fail("routeDocument succeeded with malformed XML");
        } catch (Exception we) {
            // An exception is thrown in DTOConverter/XmlUtils.appendXml at the time of this writing
            // so I will just assume that is the expected behavior
        }
        TestUtilities.waitForExceptionRouting();
    }

    /**
     * Tests searching with client-generated documentContent which will not match what the SearchableAttributeOld
     * is configured to look for.  This should pass with zero search results, but should not throw an exception.
     * @throws Exception
     */
    @Test public void testRouteDocumentWithInvalidSearchableAttributeContent() throws Exception {
        String documentTypeName = "SearchDocType";
        String key = "givenname";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), documentTypeName);

        workflowDocument.setApplicationContent("<documentContent><searchableContent><garbage>" +
                                               "<blah>not going to match anything</blah>" +
                                               "</garbage></searchableContent></documentContent>");

        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        DocumentSearchResults results =  docSearchService.lookupDocuments(user.getPrincipalId(),
                criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "fred");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeproperty", "doesntexist");
        try {
            results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException wsee) {
            assertTrue(wsee.getMessage().contains("LookupException"));
        }
    }

    /**
     * Tests searching with client-generated documentContent which will not match what the SearchableAttributeOld
     * is configured to look for.  This should pass with zero search results, but should not throw an exception.
     * @throws Exception
     */
    @Test public void testRouteDocumentWithMoreInvalidSearchableAttributeContent() throws Exception {
        String documentTypeName = "SearchDocType";
        String key = "givenname";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), documentTypeName);

        workflowDocument.setApplicationContent("<documentContent><NOTsearchableContent><garbage>" +
                                               "<blah>not going to match anything</blah>" +
                                               "</garbage></NOTsearchableContent></documentContent>");

        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        DocumentSearchResults results =  docSearchService.lookupDocuments(user.getPrincipalId(),
                criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "fred");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeproperty", "doesntexist");
        try {
            results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException wsee) {
            assertTrue(wsee.getMessage().contains("LookupException"));
        }
    }

    /*
     * Test method for 'org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute.getSearchContent()'
     */
    @Test public void testGetSearchContent() throws Exception {
        //Filling in a random document type name... Revisit
        String documentTypeName = "SearchDocType";

        assertGeneratedSearchContent(documentTypeName, "XMLSearchableAttribute", "givenname", "value", "//putWhateverWordsIwantInsideThisTag/givenname/value");
        assertGeneratedSearchContent(documentTypeName, "XMLSearchableAttributeStdLong", "testLongKey", "123458", "//putWhateverWordsIwantInsideThisTag/testLongKey/value");
        assertGeneratedSearchContent(documentTypeName, "XMLSearchableAttributeStdFloat", "testFloatKey", "2568.204", "//putWhateverWordsIwantInsideThisTag/testFloatKey/value");
        assertGeneratedSearchContent(documentTypeName, "XMLSearchableAttributeStdCurrency", "testCurrencyKey", "2248.20", "//putWhateverWordsIwantInsideThisTag/testCurrencyKey/value");
        String value = DocumentSearchInternalUtils.getDisplayValueWithDateOnly(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE);
        assertGeneratedSearchContent(documentTypeName, "XMLSearchableAttributeStdDateTime", "testDateTimeKey", value, "//putWhateverWordsIwantInsideThisTag/testDateTimeKey/value");
    }

    /**
     * Helper for asserting generated search content
     */
    protected void assertGeneratedSearchContent(String docType, String attrName, String key, String value, String expr) throws Exception {
        StandardGenericXMLSearchableAttribute attribute = getAttribute(attrName);
        ExtensionDefinition ed = createExtensionDefinition(attrName);
        WorkflowAttributeDefinition.Builder wad = WorkflowAttributeDefinition.Builder.create(attrName);
        wad.addPropertyDefinition(key, value);
        String searchContent = attribute.generateSearchContent(ed, docType, wad.build());
        assertTrue("searchContent was not found.", searchContent != null && searchContent.length() > 0);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Element foundDocContent = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(searchContent)))).getDocumentElement();
        assertTrue("Search content does not contain correct value for field '" + key + "'.", value.equals(xpath.evaluate(expr, foundDocContent, XPathConstants.STRING)));
    }

    /*
     * Test method for 'org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute.getSearchStorageValues(String)'
     */
    @Test public void testExtractDocumentAttributes() throws ParseException {
        //Filling in a random document type name... Revisit
        String documentTypeName = "SearchDocType";

        String key = "givenname";
        String value = "jack";
        assertExtractDocumentAttributes(documentTypeName, "XMLSearchableAttribute", key, value, "<putWhateverWordsIwantInsideThisTag>" + "<" + key + ">" + "<value>" + value + "</value>" + "</" + key+ ">" + "</putWhateverWordsIwantInsideThisTag>");

        // test general operation
        key = "testLongKey";
        value = "123458";
        assertExtractDocumentAttributes(documentTypeName, "XMLSearchableAttributeStdLong", key, new BigInteger(value), "<putWhateverWordsIwantInsideThisTag>" + "<" + key + ">" + "<value>" + value + "</value>" + "</" + key+ ">" + "</putWhateverWordsIwantInsideThisTag>");
        // test operation with leading and trailing spaces in xml doc content
        assertExtractDocumentAttributes(documentTypeName, "XMLSearchableAttributeStdLong", key, new BigInteger(value), "<putWhateverWordsIwantInsideThisTag>" + "<" + key + ">" + "<value>" + " " + value + " " + "</value>" + "</" + key + ">" + "</putWhateverWordsIwantInsideThisTag>");

        key = "testFloatKey";
        value = "2568.204154796";
        assertExtractDocumentAttributes(documentTypeName, "XMLSearchableAttributeStdFloat", key, new BigDecimal(value), "<putWhateverWordsIwantInsideThisTag>" + "<" + key+ ">" + "<value>" + value + "</value>" + "</" + key + ">" + "</putWhateverWordsIwantInsideThisTag>");

        key = "testDateTimeKey";
        value = DocumentSearchInternalUtils.getDisplayValueWithDateOnly(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE);
        // value is coerced to Date without time
        DateMidnight expected = TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE.toDateMidnight();
        assertExtractDocumentAttributes(documentTypeName, "XMLSearchableAttributeStdDateTime", key, expected, "<putWhateverWordsIwantInsideThisTag>" + "<" + key + ">" + "<value>" + value + "</value>" + "</" + key + ">" + "</putWhateverWordsIwantInsideThisTag>");

        // test for kuali xstream formatted dates
        value = "02/20/2007";
        String returnValue = "02/20/2007";
        // value is coerced to Date without time
        Date d = RiceConstants.getDefaultDateFormat().parse(returnValue);
        expected = new DateTime(d.getTime()).toDateMidnight();
        assertExtractDocumentAttributes(documentTypeName, "XMLSearchableAttributeStdDateTime", key, expected, "<putWhateverWordsIwantInsideThisTag>" + "<" + key + ">" + "<value>" + value + "</value>" + "</" + key + ">" + "</putWhateverWordsIwantInsideThisTag>");
    }

    /**
     * Helper to create documentwithcontent with searchable content
     */
    protected DocumentWithContent createDocumentWithSearchableContent(String docType, String content) {
        Document doc = Document.Builder.create("fakeDocId123", "fake initiator", docType, "fake doc type id").build();
        DocumentContent.Builder c = DocumentContent.Builder.create("fakeDocId123");
        c.setSearchableContent(content);
        return DocumentWithContent.create(doc, c.build());
    }

    /**
     * Help for asserting extracted document attributes
     */
    protected void assertExtractDocumentAttributes(String docType, String attrName, String key, Object value, String searchableContent) {
        StandardGenericXMLSearchableAttribute attribute = getAttribute(attrName);
        ExtensionDefinition ed = createExtensionDefinition(attrName);
        List<DocumentAttribute> values = attribute.extractDocumentAttributes(ed, createDocumentWithSearchableContent(docType, searchableContent));
        assertEquals("Number of search attribute values is wrong",1,values.size());
        for (DocumentAttribute attrib: values) {
            assertEquals("Key of attribute is wrong",key, attrib.getName());
            assertEquals("Value of attribute is wrong",value, attrib.getValue());
        }
    }
    
    private String insertCommasIfNeeded(String value, int interval) {
        int indexOfDecimal = value.indexOf(".");
        String decimalPointOn = value.substring(indexOfDecimal);
        String temp = value.substring(0, indexOfDecimal);
        StringBuffer builtValue = new StringBuffer();
        if (temp.length() <= interval) {
            builtValue.append(temp);
        } else {
            int counter = 0;
            for (int i = temp.length() - 1; (i >= 0); i--) {
                if (counter == interval) {
                    builtValue.insert(0, ",");
                    counter = 0;
                }
                counter++;
                builtValue.insert(0, temp.substring(i, i+1));
            }
        }
        return (builtValue.append(decimalPointOn)).toString();
    }

    /*
     * Test method for 'org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute.getSearchingRows()'
     */
    @Test public void testGetSearchFields() {
        //Filling in a random document type name... Revisit
        String documentTypeName = "SearchDocType";
        StandardGenericXMLSearchableAttribute searchAttribute = getAttribute(null);

        ExtensionDefinition ed = createExtensionDefinition("XMLSearchableAttribute");
        List<RemotableAttributeField> remotableAttributeFields = searchAttribute.getSearchFields(ed, documentTypeName);
        List<Row> rows = FieldUtils.convertRemotableAttributeFields(remotableAttributeFields);
        assertTrue("Invalid number of search rows", rows.size() == 1);

        //we really just want this to load without exploding
        searchAttribute = getAttribute("BlankDropDownSearchAttribute");
        ed = createExtensionDefinition("BlankDropDownSearchAttribute");
        remotableAttributeFields = searchAttribute.getSearchFields(ed, documentTypeName);
        rows = FieldUtils.convertRemotableAttributeFields(remotableAttributeFields);
        assertEquals("Invalid number of search rows", 1, rows.size());
        Row row = (Row) rows.get(0);
        Field field = row.getField(0);
        assertEquals("Should be 5 valid values", 5, field.getFieldValidValues().size());

        assertEquals("Default value is not correct", "AMST", field.getPropertyValue());
    }

    /*
     * Test method for 'org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute.validateUserSearchInputs(Map)'
     */
    @Test  public void testValidateUserSearchInputs() {
        String documentTypeName = "SearchDocType";

        StandardGenericXMLSearchableAttribute searchAttribute = getAttribute("XMLSearchableAttribute");
        ExtensionDefinition ed = createExtensionDefinition("XMLSearchableAttribute");
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, "jack", false);
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, "jack.jack", true);
        // NOTE: the original intent of this particular test is unclear. it currently passes since the wildcard character is stripped
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY,  "jack*jack", false);

        searchAttribute = getAttribute("XMLSearchableAttributeStdLong");
        ed = createExtensionDefinition("XMLSearchableAttributeStdLong");
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString(), false);
        RemotableAttributeError error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString() + ".33", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));
        error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString() + "jack*jack", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));

        searchAttribute = getAttribute("XMLSearchableAttributeStdFloat");
        ed = createExtensionDefinition("XMLSearchableAttributeStdFloat");
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString(), false);
        error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString() + "a", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));
        error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString() + "*", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));

        searchAttribute = getAttribute("XMLSearchableAttributeStdCurrency");
        ed = createExtensionDefinition("XMLSearchableAttributeStdCurrency");
        String key = "testCurrencyKey";
        Float value = Float.valueOf("5486.25");
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, key, value.toString(), false);
        error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, key, value.toString() + "a", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));
        error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, key, value.toString() + "*", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));

        searchAttribute = getAttribute("XMLSearchableAttributeStdDateTime");
        ed = createExtensionDefinition("XMLSearchableAttributeStdDateTime");
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, DocumentSearchInternalUtils.getDisplayValueWithDateOnly(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE), false);
        assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, "001/5/08", false);
        error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, "41/5/08", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));
        error = assertDocumentSearchCriteriaValidation(searchAttribute, ed, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, "01/02/20*", true);
        assertTrue("Validation error is incorrect", error.getMessage().endsWith("does not conform to standard validation for field type."));
    }

    /**
     * Helper to assert document search criteria validation
     */
    protected RemotableAttributeError assertDocumentSearchCriteriaValidation(StandardGenericXMLSearchableAttribute attribute, ExtensionDefinition ed, String attrkey, String attrvalue, boolean expectError) {
        DocumentSearchCriteria.Builder dscb = DocumentSearchCriteria.Builder.create();
        dscb.addDocumentAttributeValue(attrkey, attrvalue);

        List<RemotableAttributeError> errors = attribute.validateDocumentAttributeCriteria(ed, dscb.build());

        if (expectError) {
            assertEquals("Validation should return a single error message.", 1, errors.size());
            return errors.get(0);
        } else {
            assertEquals("Validation should not have returned an error.", 0, errors.size());
            return null;
        }
    }

    /**
     * Tests the XStreamSafeEvaluator against searchable attributes to resolve EN-63 and KULWF-723.
     *
     * This test is pretty much just a copy of testRouteDocumentWithSearchableAttribute using a
     * different document type which defines the same xpath expression, only with embedded
     * XStream "reference" attributes in the XML.
     */
    @Test public void testRouteDocumentWithXStreamSearchableAttribute() throws Exception {
        String documentTypeName = "SearchDocType";
        String key = "givenname";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);

        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "SearchDocTypeXStream");
        WorkflowAttributeDefinition.Builder givennameXMLDef = WorkflowAttributeDefinition.Builder.create("XMLXStreamSearchableAttribute");

        workflowDocument.setApplicationContent("<test></test>");

        givennameXMLDef.addPropertyDefinition("givenname", "jack");
        workflowDocument.addSearchableDefinition(givennameXMLDef.build());

        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        DocumentSearchResults results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "fred");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeproperty", "doesntexist");
        try {
            results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException wsee) {
            assertTrue(wsee.getMessage().contains("LookupException"));
        }
    }


    /**
     * Tests the resolution to issues EN-95, KULWF-757, KULOWF-52 whereby the use of a quickfinder is causing
     * NullPointers when searching for documents.
     */
    @Test public void testSearchableAttributeWithQuickfinder() throws Exception {
        String documentTypeName = "AttributeWithQuickfinderDocType";
        String key = "chart";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
         WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), documentTypeName);

         // define the chart for the searchable attribute
         WorkflowAttributeDefinition.Builder chartDef = WorkflowAttributeDefinition.Builder.create("SearchableAttributeWithQuickfinder");
         chartDef.addPropertyDefinition(key, "BL");
         document.addSearchableDefinition(chartDef.build());

         // save the document
         document.setTitle("Routin' with style");
         document.saveDocument("Savin' this document.");

         // prepare to search
         DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);
         Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");

         // execute the search by our chart, we should see one result
         DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
         criteria.setDocumentTypeName(documentTypeName);
         addSearchableAttribute(criteria, key, "BL");
         DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(),
                 criteria.build());
         assertEquals("Search results should have one document.", 1, results.getSearchResults().size());
         DocumentSearchResult result = results.getSearchResults().get(0);
         String documentId = result.getDocument().getDocumentId();
         assertEquals("Wrong document in search results.", document.getDocumentId(), documentId);

         // search with no searchable attribute criteria, should return our document as well
         criteria = DocumentSearchCriteria.Builder.create();
         criteria.setDocumentTypeName(documentTypeName);
         results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
         assertEquals("Search results should have one document.", 1, results.getSearchResults().size());
         result = results.getSearchResults().get(0);
         assertEquals("Wrong document in search results.", document.getDocumentId(), result.getDocument().getDocumentId());

    }

    /**
     * Tests that the hidding of fields and columns works properly to resolve EN-53.
     *
     * TODO this is currently commented out because we can't test this properly through the unit
     * test since the filtering of the column actually happens in the web-tier.  Shoudl this be
     * the case?  Maybe we need to re-examine when we refactor document search.
     */
    @Ignore
    @Test public void testSearchableAttributeWithHiddens() throws Exception {
        // for the following document, the chart field should not show up in the result set and the org field
        // should not show up in the criteriaw
        String docType = "AttributeWithHiddensDocType";
        DocumentType documentType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(docType);

        String attributeName = "SearchableAttributeWithHiddens";
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), docType);

        // define the chart for the searchable attribute
        WorkflowAttributeDefinition.Builder chartDef = WorkflowAttributeDefinition.Builder.create(attributeName);
        chartDef.addPropertyDefinition("chart", "BL");
        chartDef.addPropertyDefinition("org", "ARSC");
        chartDef.addPropertyDefinition("dollar", "24");
        document.addSearchableDefinition(chartDef.build());

        // save the document
        document.setTitle("Routin' with style");
        document.saveDocument("Savin' this document.");

        // prepare to search
        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);
        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");

        // execute the search by our chart, we should see one result
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(docType);
        addSearchableAttribute(criteria, "chart", "BL");
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());
        DocumentSearchResult result = results.getSearchResults().get(0);
        assertEquals("Wrong document in search results.", document.getDocumentId(), result.getDocument().getDocumentId());
        // also check that the chart field is not in the result set and the org field is
        DocumentAttribute documentAttribute = result.getSingleDocumentAttributeByName("chart");
        assertNull("The chart column should not be in the result set!", documentAttribute);
        documentAttribute = result.getSingleDocumentAttributeByName("org");
        assertNotNull("The org column should be in the result set", documentAttribute);
        assertEquals("Wrong org code.", "ARSC", documentAttribute.getValue());
        documentAttribute = result.getSingleDocumentAttributeByName("dollar");
        assertNotNull("The dollar column should be in the result set", documentAttribute);
        assertEquals("Wrong dollar code.", "24", documentAttribute.getValue().toString());
    }

    @Test public void testSetApplicationContentXMLRoutedDocument() throws Exception {
        String documentTypeName = "SearchDocType";
        String key = "givenname";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), documentTypeName);
        workflowDocument.setApplicationContent("<documentContent><searchableContent><putWhateverWordsIwantInsideThisTag>" +
                                               "<givenname><value>jack</value></givenname>" +
                                               "</putWhateverWordsIwantInsideThisTag></searchableContent></documentContent>");

        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("rkirkend");
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "jack");
        DocumentSearchResults results =  docSearchService.lookupDocuments(user.getPrincipalId(),
                criteria.build());

        assertEquals("Search results should be empty.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, key, "fred");
        results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeproperty", "doesntexist");
        try {
            results =  docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException wsee) {
            assertTrue(wsee.getMessage().contains("LookupException"));
        }
    }

    /**
     * Tests that Field objects use correct KeyValue instances when checks for blank valid values are performed
     * (such as when JSP renders drop-downs), to verify that KULRICE-3587 has been fixed.
     * 
     * @throws Exception
     */
    @Test public void testBlankValidValuesOnKeyValues() throws Exception {
        boolean[] shouldHaveBlank = {true, false};
        String[] attributesToTest = {"XMLSearchableAttributeWithBlank", "XMLSearchableAttributeWithoutBlank"};

        // Verify that the getHasBlankValidValue() method on each field returns the correct result and does not cause unexpected exceptions.
        for (int i = 0; i < shouldHaveBlank.length; i++) {
            ExtensionDefinition ed = createExtensionDefinition(attributesToTest[i]);
            List<RemotableAttributeField> remotableAttributeFields = getAttribute(attributesToTest[i]).getSearchFields(ed, "BlankValidValuesDocType");
            List<Row> rowList = FieldUtils.convertRemotableAttributeFields(remotableAttributeFields);
            assertEquals("The searching fields for " + attributesToTest[i] + " should have exactly one element", 1, rowList.size());
            assertEquals("Searching row for " + attributesToTest[i] + " should have exactly one field", 1, rowList.get(0).getFields().size());

            Field testField = rowList.get(0).getFields().get(0);
            try {
                assertEquals("The field for " + attributesToTest[i] + " does not have the expected getHasBlankValidValue() result",
                        shouldHaveBlank[i], testField.getHasBlankValidValue());
            } catch (Exception ex) {
                fail("An exception occurred while running getHasBlankValidValue() on " + attributesToTest[i] + ": " + ex.getMessage());
            }
        }
    }
}
