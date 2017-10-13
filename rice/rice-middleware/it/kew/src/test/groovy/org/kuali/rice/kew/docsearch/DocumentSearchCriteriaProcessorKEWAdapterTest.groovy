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
package org.kuali.rice.kew.docsearch

import org.junit.Test
import org.kuali.rice.kew.impl.document.search.DocumentSearchCriteriaBo
import org.kuali.rice.kew.service.KEWServiceLocator
import org.kuali.rice.kew.test.KEWTestCase
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl
import org.kuali.rice.kns.lookup.LookupableHelperService
import org.kuali.rice.kns.web.ui.Row
import static org.junit.Assert.assertEquals

import static org.junit.Assert.fail

/**
 * Tests the DocumentSearchCriteriaProcessorKEWAdapter
 */
class DocumentSearchCriteriaProcessorKEWAdapterTest extends KEWTestCase {
    // the default rows for a DocumentSearchCriteria lookup, returned by AbstractLookupableHelperServiceImpl
    private static def DEFAULT_ROW_VALUES = [
        [ propertyName: "documentTypeName", fieldLabel: "Document Type", fieldType: "text" ],
        [ propertyName: "initiatorPrincipalName", fieldLabel: "Initiator", fieldType: "text" ],
        [ propertyName: "approverPrincipalName", fieldLabel: "Approver", fieldType: "text" ],
        [ propertyName: "viewerPrincipalName", fieldLabel: "Viewer", fieldType: "text" ],
        [ propertyName: "groupViewerId", fieldLabel: "Group Viewer Id", fieldType: "lookupreadonly" ],
        [ propertyName: "documentId", fieldLabel: "Document Id", fieldType: "text" ],
        [ propertyName: "applicationDocumentId", fieldLabel: "Application Document Id", fieldType: "text" ],
        [ propertyName: "statusCode", fieldLabel: "Document Status", fieldType: "multiselect" ],
        [ propertyName: "applicationDocumentStatus", fieldLabel: "Application Document Status", fieldType: "text" ],
        [ propertyName: "rangeLowerBoundKeyPrefix_dateApplicationDocumentStatusChanged", fieldLabel: "Date App Doc Status Changed, From", fieldType: "text" ],
        [ propertyName: "dateApplicationDocumentStatusChanged", fieldLabel: "Date App Doc Status Changed, To", fieldType: "text" ],
        [ propertyName: "routeNodeName", fieldLabel: "Route Node", fieldType: "dropdown" ],
        [ propertyName: "routeNodeLogic", fieldLabel: "Route Node Logic", fieldType: "dropdown" ],
        [ propertyName: "rangeLowerBoundKeyPrefix_dateCreated", fieldLabel: "Date Created From", fieldType: "text" ],
        [ propertyName: "dateCreated", fieldLabel: "Date Created To", fieldType: "text" ],
        [ propertyName: "rangeLowerBoundKeyPrefix_dateApproved", fieldLabel: "Date Approved From", fieldType: "text" ],
        [ propertyName: "dateApproved", fieldLabel: "Date Approved To", fieldType: "text" ],
        [ propertyName: "rangeLowerBoundKeyPrefix_dateLastModified", fieldLabel: "Date Last Modified From", fieldType: "text" ],
        [ propertyName: "dateLastModified", fieldLabel: "Date Last Modified To", fieldType: "text" ],
        [ propertyName: "rangeLowerBoundKeyPrefix_dateFinalized", fieldLabel: "Date Finalized From", fieldType: "text" ],
        [ propertyName: "dateFinalized", fieldLabel: "Date Finalized To", fieldType: "text" ],
        [ propertyName: "title", fieldLabel: "Title", fieldType: "text" ],
        [ propertyName: "saveName", fieldLabel: "Name this search (optional)", fieldType: "text" ],
        [ propertyName: "groupViewerName", fieldLabel: "Group Viewer", fieldType: "hidden" ]
    ]

    // the list of *default* fields the DSCPKEWAdapter will return for a basic search
    private static def BASIC_FIELD_NAMES = [
        "documentTypeName",
        "initiatorPrincipalName",
        "documentId",
        "applicationDocumentStatus",
        "dateCreated",
        "rangeLowerBoundKeyPrefix_dateCreated",
        "saveName"
    ]

    // the list of *default* fields the DSCPKEWAdapter will return for a detailed search
    private static def ADVANCED_FIELD_NAMES = [
        "documentTypeName",
        "initiatorPrincipalName",
        "approverPrincipalName",
        "viewerPrincipalName",
        "groupViewerName",
        "groupViewerId",
        "documentId",
        "applicationDocumentId",
        "statusCode",
        "applicationDocumentStatus",
        "rangeLowerBoundKeyPrefix_dateApplicationDocumentStatusChanged",
        "dateApplicationDocumentStatusChanged",
        "routeNodeName",
        "routeNodeLogic",
        "dateCreated",
        "rangeLowerBoundKeyPrefix_dateCreated",
        "dateApproved",
        "rangeLowerBoundKeyPrefix_dateApproved",
        "dateLastModified",
        "rangeLowerBoundKeyPrefix_dateLastModified",
        "dateFinalized",
        "rangeLowerBoundKeyPrefix_dateFinalized",
        "title",
        "saveName"
    ]

    // the list of additional fields added by DSCPKEWAdapter, including the searchable attribute fields for the "SearchDocType"
    private static def SEARCHABLE_FIELD_NAMES = [
        "documentAttribute.givenname", "documentAttribute.testLongKey","documentAttribute.testFloatKey",
        "documentAttribute.testDateTimeKey", "documentAttribute.testDateTimeKey",
        "documentAttribute.rangeLowerBoundKeyPrefix_testDateTimeKey",
        "isAdvancedSearch",
        "resetSavedSearch",
        "superUserSearch"
    ]

    @Override
    protected void loadTestData() throws Exception {
        loadXmlFile("SearchAttributeConfig.xml");
    }


    /* this test should probably go in a KualiLookupableHelperService(Impl) integration test */
    @Test
    void testDocumentSearchCriteriaDefaultRows() {
        // simulate helper service initialization from lookup form
        def rows = getDefaultRows()
        displayRows(rows)
        assertRowValues(rows, DEFAULT_ROW_VALUES)
    }

    /**
     * Test basic search rows
     */
    @Test
    void testBasicRows() {
        assertRows(BASIC_FIELD_NAMES + SEARCHABLE_FIELD_NAMES)
    }

    /**
     * Test basic search rows
     */
    @Test
    void testBasicSuperUserRows() {
        assertRows(BASIC_FIELD_NAMES + SEARCHABLE_FIELD_NAMES, false, true)
    }

    /**
     * Test advanced/detailed search rows
     */
    @Test
    void testAdvancedRows() {
        assertRows(ADVANCED_FIELD_NAMES + SEARCHABLE_FIELD_NAMES, true)
    }

    /**
     * Test advanced/detailed search rows
     */
    @Test
    void testAdvancedSuperUserRows() {
        assertRows(ADVANCED_FIELD_NAMES + SEARCHABLE_FIELD_NAMES, true, true)
    }

    /**
     * Calls a vanilla KualiLookupableHelperServiceImpl to get the default lookup rows for a DocumentSearchCriteriaBo which
     * are sent to the DSCPKEWAdapter to simulate a real lookup rendering
     */
    protected List<Row> getDefaultRows() {
        LookupableHelperService lhs = new KualiLookupableHelperServiceImpl()
        lhs.setBusinessObjectClass(DocumentSearchCriteriaBo.class)
        lhs.getRows()
    }

    /**
     * Obtains rows and asserts that the expected list of fields is present
     */
    protected List<Row> assertRows(expectedFieldNames, advanced=false, superUser=false) {
        DocumentSearchCriteriaProcessor dscp = new DocumentSearchCriteriaProcessorKEWAdapter()
        def defaultRows = getDefaultRows()
        def dscpRows = dscp.getRows(KEWServiceLocator.getDocumentTypeService().findByName("SearchDocType"), defaultRows, advanced, superUser)
        assertFieldValue(dscpRows, "isAdvancedSearch", advanced ? "YES" : "NO")
        assertFieldValue(dscpRows, "superUserSearch", superUser ? "YES" : "NO")
        assertFieldPresence(dscpRows, expectedFieldNames)
    }

    /**
     * Assert that the set of rows represents the set of field names
     */
    protected assertFieldPresence(List<Row> rows, List<String> names) {
        def row_fields = rows.collect { it.fields }.flatten().collect { it.propertyName }
        assertEquals(names.toSet(), row_fields.toSet())
    }

    /**
     * Assert that a field with a specific value exists
     */
    protected assertFieldValue(List<Row> rows, fieldName, fieldValue) {
        for (row in rows) {
            for (field in row.fields) {
                if (field.propertyName == fieldName && field.propertyValue == fieldValue) {
                    return
                }
            }
        }
        fail("Field with name '$fieldName' and value '$fieldValue' not found.")
    }

    /**
     * Assert row list matches expected field data, in exact order
     */
    protected assertRowValues(List<Row> rows, test_values) {
        rows.eachWithIndex {
            it, i ->
                assertEquals(test_values[i]["propertyName"], it.fields[0].propertyName)
                assertEquals(test_values[i]["fieldLabel"], it.fields[0].fieldLabel)
                assertEquals(test_values[i]["fieldType"], it.fields[0].fieldType)
        }
    }

    protected displayRows(List<Row> rows) {
        rows.each {
            it.fields.each {
                println it.propertyName
                println it.fieldLabel
                println it.fieldType
            }
        }
    }
}
