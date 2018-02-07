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
package org.kuali.rice.kew.impl.document.search

import org.kuali.rice.kew.docsearch.DocumentSearchTestBase
import org.kuali.rice.kew.api.WorkflowDocument
import org.kuali.rice.kew.api.WorkflowDocumentFactory

import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.junit.Before
import org.junit.Test
import org.kuali.rice.kns.web.struts.form.LookupForm
import org.apache.struts.mock.MockHttpServletRequest
import org.kuali.rice.krad.util.KRADConstants

import static org.junit.Assert.assertNull

import org.kuali.rice.kew.docsearch.DocumentSearchCriteriaProcessorKEWAdapter
import org.kuali.rice.kew.api.KewApiConstants

import static org.junit.Assert.assertFalse

import static org.junit.Assert.assertTrue

import org.kuali.rice.krad.util.GlobalVariables
import java.util.concurrent.Callable

import org.joda.time.DateTime
import java.text.SimpleDateFormat

import static org.junit.Assert.assertEquals
import org.kuali.rice.krad.UserSession
import org.kuali.rice.kns.lookup.KualiLookupableImpl
import org.kuali.rice.kew.docsearch.service.impl.DocumentSearchServiceImpl
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria
import org.kuali.rice.kns.lookup.LookupableHelperService
import org.kuali.rice.kns.lookup.Lookupable
import org.kuali.rice.kew.service.KEWServiceLocator
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeString
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeLong
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeFloat
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeDateTime
import org.kuali.rice.kew.api.document.search.DocumentSearchResults
import static org.junit.Assert.assertNotNull
import org.kuali.rice.kew.docsearch.service.DocumentSearchService
import org.kuali.rice.kns.web.ui.Row
import org.kuali.rice.kns.web.ui.ResultRow
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import org.apache.commons.lang.StringUtils

/**
 */
class DocumentSearchURLParametersTest extends DocumentSearchTestBase {
    def DOCUMENT_TYPE_NAME= "SearchDocType";
    def PRINCIPAL_NAME = "rkirkend";
    def PRINCIPAL_ID = null;

    class TestLookupForm extends LookupForm {
        Lookupable testLookupable;
        public TestLookupForm() {
            def lhs = new DocumentSearchCriteriaBoLookupableHelperService();
            lhs.setDocumentSearchCriteriaProcessor(new DocumentSearchCriteriaProcessorKEWAdapter())
            lhs.setDocumentSearchCriteriaTranslator(new DocumentSearchCriteriaTranslatorImpl())
            lhs.setDocumentSearchService(KEWServiceLocator.getDocumentSearchService())
            this.testLookupable = new KualiLookupableImpl()
            testLookupable.setLookupableHelperService(lhs)
        }
        
        @Override
        protected Lookupable getLookupable(String beanName) {
            return testLookupable
        }
    }
    
    protected void loadTestData() throws Exception {
        loadXmlFile("org/kuali/rice/kew/docsearch/SearchAttributeConfig.xml");
    }

    @Before
    void initPerson() {
        PRINCIPAL_ID = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(PRINCIPAL_NAME).getPrincipalId();
    }

    @Test
    void testURLParametersDefaults() {
        def form = new TestLookupForm();
        def req = new MockHttpServletRequest();

        req.addParameter(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, DocumentSearchCriteriaBo.class.getName())
        form.populate(req)
        DocumentSearchCriteriaBoLookupableHelperService lhs = (DocumentSearchCriteriaBoLookupableHelperService) form.lookupable.lookupableHelperService

        assertTrue(form.headerBarEnabled)
        assertTrue(form.lookupCriteriaEnabled)
        assertFalse(lhs.advancedSearch)
        assertFalse(lhs.superUserSearch)
        assertFalse(lhs.clearSavedSearch)
        assertNull(lhs.savedSearchName)

        GlobalVariables.doInNewGlobalVariables(new UserSession(PRINCIPAL_NAME), new Callable() {
            Object call() {
            def crit = lhs.loadCriteria(form.fieldsForLookup)
                assertEquals("Document type was not parsed from request params", null, crit.documentTypeName)
                assertEquals(0, crit.documentAttributeValues.size())
            }
        })
    }

    protected populateForm(params) {
        def form = new TestLookupForm();
        def req = new MockHttpServletRequest();

        req.addParameter(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, DocumentSearchCriteriaBo.class.getName())
        req.addParameter("documentTypeName", DOCUMENT_TYPE_NAME)
        req.addParameter(LookupForm.HEADER_BAR_ENABLED_PARAM, "false")
        req.addParameter(LookupForm.SEARCH_CRITERIA_ENABLED_PARAM, "false")
        req.addParameter(KRADConstants.ADVANCED_SEARCH_FIELD, "YES")
        req.addParameter(DocumentSearchCriteriaProcessorKEWAdapter.SUPERUSER_SEARCH_FIELD, "YES")
        req.addParameter(DocumentSearchCriteriaProcessorKEWAdapter.CLEARSAVED_SEARCH_FIELD, "YES")
        req.addParameter("dateCreated", "11/11/11..12/12/15")
        req.addParameter(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, "string searchable attr")
        req.addParameter(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, "1")
        req.addParameter(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, "2.0")
        params.each { k, v ->
            req.addParameter(k, v)
        }

        form.populate(req)
        return [form, (DocumentSearchCriteriaBoLookupableHelperService) form.lookupable.lookupableHelperService]
    }

    @Test
    void testURLParams() {
        def (form, lhs) = populateForm([
            (KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY): "09/09/09",
            (KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY): "12/12/13"
        ])

        assertFalse(form.headerBarEnabled)
        assertFalse(form.lookupCriteriaEnabled)
        assertTrue(lhs.advancedSearch)
        assertTrue(lhs.superUserSearch)
        assertTrue(lhs.clearSavedSearch)
        assertEquals(null, lhs.savedSearchName)

        DocumentSearchCriteria crit = null;
        GlobalVariables.doInNewGlobalVariables(new UserSession(PRINCIPAL_NAME), new Callable() {
            Object call() {
                crit = lhs.loadCriteria(form.fieldsForLookup)
            }
        })

        println(crit)
        println(crit.documentAttributeValues)

        assertEquals("Document type was not parsed from request params", DOCUMENT_TYPE_NAME, crit.documentTypeName)
        // dateCreated gets mapped directly into criteria object
        assertEquals(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("11/11/11")).withMillisOfDay(0), crit.dateCreatedFrom)
        assertEquals(new DateTime(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("12/13/15")).toDateMidnight()).minusMillis(1), crit.dateCreatedTo)
        assertEquals(["string searchable attr"], crit.documentAttributeValues[TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY])
        assertEquals(["1"], crit.documentAttributeValues[TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY])
        assertEquals(["2.0"], crit.documentAttributeValues[TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY])
        assertEquals(["09/09/09>..<12/12/13"], crit.documentAttributeValues[TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY])
    }

    /**
     * Confirms that entering range expressions directly as request parameters is not supported,
     * only individual bounds values are.
     */
    @Test
    void testURLParamsDateExpressionNotSupported() {
        def (form, lhs) = populateForm([
            (KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY): "09/09/09..12/12/13"
        ])

        assertFalse(form.headerBarEnabled)
        assertFalse(form.lookupCriteriaEnabled)
        assertTrue(lhs.advancedSearch)
        assertTrue(lhs.superUserSearch)
        assertTrue(lhs.clearSavedSearch)
        assertEquals(null, lhs.savedSearchName)

        DocumentSearchCriteria crit = null;
        GlobalVariables.doInNewGlobalVariables(new UserSession(PRINCIPAL_NAME), new Callable() {
            Object call() {
                crit = lhs.loadCriteria(form.fieldsForLookup)
            }
        })

        println(crit)
        println(crit.documentAttributeValues)

        assertEquals("Document type was not parsed from request params", DOCUMENT_TYPE_NAME, crit.documentTypeName)
        // dateCreated gets mapped directly into criteria object
        assertEquals(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("11/11/11")).withMillisOfDay(0), crit.dateCreatedFrom)
        assertEquals(new DateTime(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("12/13/15")).toDateMidnight()).minusMillis(1), crit.dateCreatedTo)
        assertEquals(["string searchable attr"], crit.documentAttributeValues[TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY])
        assertEquals(["1"], crit.documentAttributeValues[TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY])
        assertEquals(["2.0"], crit.documentAttributeValues[TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY])
        assertFalse(["09/09/09..12/12/13"].equals(crit.documentAttributeValues[TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY]))
    }

    @Test
    void testURLParamsSavedSearch() {
        routeDoc()

        def SAVED_SEARCH_NAME = "testURLSavedDocSearch"
        def docSearchService = (DocumentSearchService)KEWServiceLocator.getDocumentSearchService();

        DocumentSearchCriteria.Builder builder = DocumentSearchCriteria.Builder.create();
        builder.setDocumentTypeName(DOCUMENT_TYPE_NAME);
        builder.setSaveName(SAVED_SEARCH_NAME);
        builder.setDateCreatedFrom(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("01/01/01")))
        builder.setDateCreatedTo(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("01/01/2020")))
        Map<String, List<String>> docAttrs = new HashMap<String, List<String>>();
        docAttrs.put(TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, [ TestXMLSearchableAttributeString.SEARCH_STORAGE_VALUE ]);
        docAttrs.put(TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, [ TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString() ]);
        docAttrs.put(TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, [ TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString() ]);
        docAttrs.put(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, ["09/09/05..12/12/13"]);

        builder.setDocumentAttributeValues(docAttrs);

        DocumentSearchResults results = docSearchService.lookupDocuments(PRINCIPAL_ID, builder.build());
        assertEquals(1, results.searchResults.size())
        
        DocumentSearchCriteria saved = docSearchService.getNamedSearchCriteria(PRINCIPAL_ID, SAVED_SEARCH_NAME)
        assertNotNull(saved);

        def form = new TestLookupForm();
        def req = new MockHttpServletRequest();

        req.addParameter(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, DocumentSearchCriteriaBo.class.getName())
        req.addParameter(DocumentSearchCriteriaBoLookupableHelperService.SAVED_SEARCH_NAME_PARAM, SAVED_SEARCH_NAME)

        form.populate(req)
        def lhs = (DocumentSearchCriteriaBoLookupableHelperService) form.lookupable.lookupableHelperService

        // test that the saved search name made it into the LHS
        assertEquals(SAVED_SEARCH_NAME, lhs.savedSearchName)

        // test that the criteria is loaded correctly from the form fields (i.e. from the loaded saved search)
        DocumentSearchCriteria crit = null;
        GlobalVariables.doInNewGlobalVariables(new UserSession(PRINCIPAL_NAME), new Callable() {
            Object call() {
                crit = lhs.loadCriteria(form.fieldsForLookup)
            }
        })

        assertEquals("Document type was not parsed from request params", DOCUMENT_TYPE_NAME, crit.documentTypeName)
        // dateCreated gets mapped directly into criteria object
        assertEquals(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("01/01/01")).withMillisOfDay(0), crit.dateCreatedFrom)
        // NOTE: we are invoking doc search service to perform doc lookup required to save the doc search, therefore upper date range is not update to last millis of day
        assertEquals(new DateTime(new SimpleDateFormat("MM/dd/yy").parse("01/01/2020")), crit.dateCreatedTo)
        assertEquals([TestXMLSearchableAttributeString.SEARCH_STORAGE_VALUE], crit.documentAttributeValues[TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY])
        assertEquals([TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString()], crit.documentAttributeValues[TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY]);
        assertEquals([TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString()], crit.documentAttributeValues[TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY]);
        assertEquals(["09/09/05..12/12/13"], crit.documentAttributeValues[TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY]);

        // test that we can perfom the lookup supplying a form with only the saved search name
        def rows = new ArrayList<ResultRow>()
        def bos;
        GlobalVariables.doInNewGlobalVariables(new UserSession(PRINCIPAL_NAME), new Callable() {
            Object call() {
                bos = form.lookupable.performLookup(form, rows, false)
            }
        })
        // the saved search should be (re) executed and we should get 1 result
        assertEquals(1, bos.size())
    }

    @Test
    void testCurrentUserReplacement() {
        final UserSession userSession = new UserSession(PRINCIPAL_NAME)

        def TESTS = [
            // all id types
            CURRENT_USER: userSession.getPerson().getPrincipalName(),
            ("CURRENT_USER.authenticationId"): userSession.getPerson().getPrincipalName(),
            ("CURRENT_USER.a"): userSession.getPerson().getPrincipalName(),
            ("CURRENT_USER.principalName"): userSession.getPerson().getPrincipalName(),
            ("CURRENT_USER.workflowId"): userSession.getPerson().getPrincipalId(),
            ("CURRENT_USER.w"): userSession.getPerson().getPrincipalId(),
            ("CURRENT_USER.principalId"): userSession.getPerson().getPrincipalId(),
            ("CURRENT_USER.emplId"): userSession.getPerson().getEmployeeId(),
            ("CURRENT_USER.e"): userSession.getPerson().getEmployeeId(),

            // whitespace/terminals
            (" CURRENT_USER "): " ${userSession.getPerson().getPrincipalName()} ",
            (" !CURRENT_USER.authenticationId "): " !${userSession.getPerson().getPrincipalName()} ",

            // non-matching
            ("abcdCURRENT_USER"): null,
            ("CURRENT_USERdef"): null,
            ("abcdCURRENT_USER "): null,
            ("!CURRENT_USERdef"): null,

            // empty entries
            (null): null,
            (""): null,

            // more sophisticated expressions
            (" CURRENT_USER > 'abcd' && !(CURRENT_USER.emplId == 'defg') "): " ${userSession.getPerson().getPrincipalName()} > 'abcd' && !(${userSession.getPerson().getEmployeeId()} == 'defg') ",
        ]

        TESTS.each { value, expected ->
            // test replaceCurrentUserToken impl
            if (StringUtils.isNotEmpty(value)) {
                String actual = DocumentSearchCriteriaBoLookupableHelperService.replaceCurrentUserToken(value, userSession.getPerson())
                assertEquals("pattern: '" + value + "' expected '" + expected + "' got '" + actual + "'", expected?.toString(), actual)
            }

            // test cleanupFieldValues is overriding values correctly
            GlobalVariables.doInNewGlobalVariables(userSession, new Callable() {
                Object call() {
                    Map<String, String> values = DocumentSearchCriteriaBoLookupableHelperService.cleanupFieldValues([f: value], [p: [value] as String[]])

                    assertEquals("pattern: '" + value + "' expected field value '" + (expected == null ? value : expected.toString()) + "' got '" + values['f'] + "'", expected == null ? value : expected.toString(), values['f'])
                    // parameters are not all included by cleanupFieldValues
                    //assertEquals("pattern: '" + value + "' expected param value '" + expected + "' got '" + values['p'] + "'", expected?.toString(), values['p'])
                }
            })
        }
    }

    protected void routeDoc() {
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(PRINCIPAL_ID, DOCUMENT_TYPE_NAME);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");
    }
}
