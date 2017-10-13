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

import com.google.common.collect.Maps
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.framework.persistence.jdbc.sql.SQLUtils
import org.kuali.rice.kew.api.KEWPropertyConstants
import org.kuali.rice.kew.api.KewApiConstants
import org.kuali.rice.kew.api.WorkflowDocument
import org.kuali.rice.kew.api.WorkflowDocumentFactory
import org.kuali.rice.kew.api.document.DocumentStatus
import org.kuali.rice.kew.api.document.DocumentStatusCategory
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria
import org.kuali.rice.kew.api.document.search.DocumentSearchResults
import org.kuali.rice.kew.api.document.search.RouteNodeLookupLogic
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils
import org.kuali.rice.kew.docsearch.DocumentSearchTestBase
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeDateTime
import org.kuali.rice.kew.docsearch.service.DocumentSearchService
import org.kuali.rice.kew.service.KEWServiceLocator
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.krad.util.KRADConstants

import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertTrue
import static org.junit.Assert.assertNotNull

/**
 * 
 */
class DocumentSearchCriteriaTranslatorTest extends DocumentSearchTestBase {
    private static final String DOCUMENT_TYPE_NAME= "SearchDocType";
    private static final String USER_NETWORK_ID = "rkirkend";

    private static final LOWER_FIELD_NAME = KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY;
    private static final UPPER_FIELD_NAME = KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY;

    private DocumentSearchService docSearchService;
    private String principalId;
    def documentSearchCriteriaTranslator = new DocumentSearchCriteriaTranslatorImpl()

    protected void loadTestData() throws Exception {
        loadXmlFile("org/kuali/rice/kew/docsearch/SearchAttributeConfig.xml");
    }

    @Before
    void init() {
        principalId = getPrincipalId(USER_NETWORK_ID);
        docSearchService = (DocumentSearchService) KEWServiceLocator.getDocumentSearchService();
    }

    /**
     * Tests that the lookup form fields are properly parsed into a DocumentSearchCriteria object
     */
    @Test
    void testTranslateFieldsToCriteria() {
        // form fields
        def fields = new HashMap<String, String>()
        fields.put("documentTypeName", "SearchDocType")
        fields.put(KEWPropertyConstants.DOC_SEARCH_RESULT_PROPERTY_NAME_STATUS_CODE,
                   [ DocumentStatus.INITIATED.code,
                     DocumentStatus.PROCESSED.code,
                     DocumentStatus.FINAL.code,
                     "category:" + DocumentStatusCategory.SUCCESSFUL.getCode(),
                     "category:" + DocumentStatusCategory.UNSUCCESSFUL.getCode()].join(','))
        // org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl.performLookup calls LookupUtils.preProcessRangeFields(lookupFormFields);
        // to pre-process range fields, resulting in values that have already been converted to expressions by the time the DSCTranslator sees them
        fields.put("dateCreated", ">=01/01/2010")
        fields.put("routeNodeLookupLogic", RouteNodeLookupLogic.EXACTLY.toString())

        // document attrs
        def docattrs = [ attr1: [ "val1" ], attr2: [ "val2" ] ] // attr3: [ "mult0", "mult1" ] ] Note: translator does not support multiple values
        docattrs.each { k, v ->
            fields.put(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + k, v.join(','))
        }

        def crit = documentSearchCriteriaTranslator.translateFieldsToCriteria(fields)
        assertNotNull(crit)

        assertEquals("SearchDocType", crit.documentTypeName)
        assertEquals([ DocumentStatus.INITIATED, DocumentStatus.PROCESSED, DocumentStatus.FINAL ] as Set, crit.getDocumentStatuses() as Set)
        assertEquals([ DocumentStatusCategory.SUCCESSFUL, DocumentStatusCategory.UNSUCCESSFUL ] as Set, crit.getDocumentStatusCategories() as Set)
        assertEquals(new DateTime(2010, 1, 1, 0, 0), crit.dateCreatedFrom)
        assertEquals(RouteNodeLookupLogic.EXACTLY, crit.routeNodeLookupLogic)

        assertEquals(docattrs.size(), crit.documentAttributeValues.size())
        docattrs.each { k, v ->
            assertEquals(v as Set, crit.documentAttributeValues[k] as Set)
        }


        // KULRICE-7786: support for groups (categories) of application document statuses
        fields.put("applicationDocumentStatus", "category:TestCategory");
        // TestCategory matches up with an app doc status category defined in the doc type xml that
        // contains a couple of statuses
        crit = documentSearchCriteriaTranslator.translateFieldsToCriteria(fields);

        assertNotNull("documentSearchCriteriaTranslator.getApplicationDocumentStatuses is NULL! expecting Approval In Progress and Submitted", crit.getApplicationDocumentStatuses())
        assertTrue( "https://jira.kuali.org/browse/KULRICE-8555", crit.getApplicationDocumentStatuses().contains("Approval In Progress"));
        assertTrue(crit.getApplicationDocumentStatuses().contains("Submitted"));
    }

    /**
     * Tests that the saved DocumumentLookupCriteria is properly loaded into form field values
     */
    @Test
    void testTranslateCriteriaToFields() {
        def builder = DocumentSearchCriteria.Builder.create()
        builder.documentTypeName = "whatever"
        builder.documentStatuses = [ DocumentStatus.INITIATED, DocumentStatus.PROCESSED, DocumentStatus.FINAL ]
        builder.documentStatusCategories = [ DocumentStatusCategory.UNSUCCESSFUL, DocumentStatusCategory.SUCCESSFUL ]
        builder.dateApprovedFrom = new DateTime(2010, 1, 1, 0, 0)
        builder.dateApprovedTo = new DateTime(2011, 1, 1, 0, 0)
        builder.routeNodeLookupLogic = RouteNodeLookupLogic.BEFORE

        builder.documentAttributeValues = [ attr1: [ "val1" ], attr2: [ "val2" ], attr3: [ "mult0", "mult1" ] ]

        def fields = documentSearchCriteriaTranslator.translateCriteriaToFields(builder.build())

        assertEquals("whatever", fields["documentTypeName"][0])
        assertEquals([ DocumentStatus.INITIATED.code,
                     DocumentStatus.PROCESSED.code,
                     DocumentStatus.FINAL.code,
                     "category:" + DocumentStatusCategory.SUCCESSFUL.getCode(),
                     "category:" + DocumentStatusCategory.UNSUCCESSFUL.getCode()] as Set, fields[KEWPropertyConstants.DOC_SEARCH_RESULT_PROPERTY_NAME_STATUS_CODE] as Set)
        assertEquals(new DateTime(2010, 1, 1, 0, 0).toString(), fields[KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + "dateApproved"][0])
        assertEquals(new DateTime(2011, 1, 1, 0, 0).toString(), fields["dateApproved"][0])
        assertEquals(RouteNodeLookupLogic.BEFORE.toString(), fields["routeNodeLookupLogic"][0])

        builder.documentAttributeValues.each { k, v ->
            assertEquals(v as Set, fields[KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + k] as Set)
        }
    }

    protected void routeDoc() {
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(principalId, DOCUMENT_TYPE_NAME);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");
    }
    
    protected String getPrincipalId(String networkId){
        return KimApiServiceLocator.getPersonService().getPersonByPrincipalName(networkId).getPrincipalId();
    }

    protected def searchValues() {
        Calendar searchAttributeDateTimeValue = TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE.toGregorianCalendar()

        def from = (Calendar) searchAttributeDateTimeValue.clone()
        from.add(Calendar.DATE, -1)
        def to = (Calendar) searchAttributeDateTimeValue.clone()
        to.add(Calendar.DATE, 1)

        return [ DocumentSearchInternalUtils.getDisplayValueWithDateOnly(SQLUtils.convertCalendar(from)),
                 DocumentSearchInternalUtils.getDisplayValueWithDateOnly(SQLUtils.convertCalendar(to)) ]
    }

    @Test void testTranslateSearchableAttributeFieldsToCriteria() {
        routeDoc();
        
        // form fields
        def fields = new HashMap<String, String>()
        fields.put("documentTypeName", DOCUMENT_TYPE_NAME)

        def (from, to) = searchValues()
        fields.put(LOWER_FIELD_NAME, from)
        fields.put(UPPER_FIELD_NAME, to)

        def crit = documentSearchCriteriaTranslator.translateFieldsToCriteria(fields)
        assertNotNull(crit)

        assertEquals(DOCUMENT_TYPE_NAME, crit.documentTypeName)
        assertEquals(1, crit.documentAttributeValues[TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY].size())
        // TestXMLSearchableAttributeDateTime defines exclusive bounds in lookup settings
        assertEquals("${from}>..<${to}", crit.documentAttributeValues[TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY][0])

        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, crit);
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());
    }

    @Test void testTranslateSearchableAttributeCriteriaToFields() {
        routeDoc();

        def builder = DocumentSearchCriteria.Builder.create()
        builder.documentTypeName = DOCUMENT_TYPE_NAME
        builder.saveName = "savedSearch"

        def (from, to) = searchValues()
        builder.addDocumentAttributeValue(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, "${from}..${to}" as String)

        def fields = documentSearchCriteriaTranslator.translateCriteriaToFields(builder.build())

        assertNotNull(fields[LOWER_FIELD_NAME])
        assertNotNull(fields[UPPER_FIELD_NAME])
        assertEquals(1, fields[LOWER_FIELD_NAME].size())
        assertEquals(1, fields[UPPER_FIELD_NAME].size())
        assertEquals(from, fields[LOWER_FIELD_NAME][0])
        assertEquals(to, fields[UPPER_FIELD_NAME][0])
    }

    @Test void testRoundTrip() {
        routeDoc();
        
        def builder = DocumentSearchCriteria.Builder.create()
        builder.documentTypeName = DOCUMENT_TYPE_NAME
        builder.saveName = "savedSearch"
        def (from, to) = searchValues()
        builder.addDocumentAttributeValue(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, "${from}>..<${to}" as String)

        def original = builder.build()

        DocumentSearchResults results = docSearchService.lookupDocuments(principalId, original);
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        def fields = documentSearchCriteriaTranslator.translateCriteriaToFields(original)
        // API is not symmetric...
        def field_strings = convertToMapStringString(fields)

        def translated_crit = new DocumentSearchCriteriaTranslatorImpl().translateFieldsToCriteria(field_strings)

        assertEquals(original, translated_crit)

        results = docSearchService.lookupDocuments(principalId, translated_crit);
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        DocumentSearchCriteria reloaded_crit = docSearchService.getNamedSearchCriteria(principalId, "savedSearch");

        assertEquals(original, reloaded_crit)

        results = docSearchService.lookupDocuments(principalId, reloaded_crit);
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());
    }

    private Map<String, String> convertToMapStringString(Map<String, String[]> values) {
        return Maps.transformEntries(values, new Maps.EntryTransformer<String, String[], String>() {
            public String transformEntry(String key, String[] value) {
                if (value != null && value.length > 0) {
                    return value[0];
                } else {
                    return null;
                }
            }
        });
    }
}
