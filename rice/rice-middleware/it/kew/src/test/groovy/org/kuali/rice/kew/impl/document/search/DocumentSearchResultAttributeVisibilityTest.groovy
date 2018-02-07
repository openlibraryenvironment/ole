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

import java.util.concurrent.Callable
import org.junit.Before
import org.junit.Test
import org.kuali.rice.kew.api.KewApiConstants
import org.kuali.rice.kew.api.WorkflowDocument
import org.kuali.rice.kew.api.WorkflowDocumentFactory
import org.kuali.rice.kew.docsearch.DocumentSearchCriteriaProcessorKEWAdapter
import org.kuali.rice.kew.docsearch.DocumentSearchTestBase
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeDateTime
import org.kuali.rice.kew.docsearch.service.DocumentSearchService
import org.kuali.rice.kew.service.KEWServiceLocator
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.kns.lookup.KualiLookupableImpl
import org.kuali.rice.kns.web.struts.form.LookupForm
import org.kuali.rice.kns.web.ui.ResultRow
import org.kuali.rice.krad.UserSession
import org.kuali.rice.krad.bo.BusinessObject
import org.kuali.rice.krad.util.GlobalVariables
import org.kuali.rice.krad.util.KRADConstants
import static junit.framework.Assert.assertTrue
import static org.junit.Assert.assertFalse

/**
 * 
 */
class DocumentSearchResultAttributeVisibilityTest extends DocumentSearchTestBase {
    private static final String DOCUMENT_TYPE_NAME= "SearchDocType";
    private static final String XML_ATTR_DOCUMENT_TYPE_NAME= "SearchDocType2";
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

    @Test
    void testHiddenSearchableAttributes() {
        def (displayList, resultTable) = searchForDoc(DOCUMENT_TYPE_NAME)
        assertTrue(displayList.size() > 0)
        assertTrue(resultTable.size() > 0)
        def DISPLAYED_COLUMN = "testLongKey"
        // TextXMLSearchAttributeDateTime configured to NOT be shown in results
        def NOT_DISPLAYED_COLUMN = "testDateTimeKey"
        def displayed_columns = resultTable[0].columns.collect { it.propertyName };
        assertTrue(displayed_columns.contains(DISPLAYED_COLUMN))
        assertFalse(displayed_columns.contains(NOT_DISPLAYED_COLUMN))
    }

    @Test
    void testHiddenXMLSearchableAttributes() {
        def (displayList, resultTable) = searchForDoc(XML_ATTR_DOCUMENT_TYPE_NAME)
        assertTrue(displayList.size() > 0)
        assertTrue(resultTable.size() > 0)
        def DISPLAYED_COLUMN = "givenname"
        // SearchableAttributeNotVisible configured to NOT be shown in results
        def NOT_DISPLAYED_COLUMN = "givenname_hidden"
        def displayed_columns = resultTable[0].columns.collect { it.propertyName };
        assertTrue(displayed_columns.contains(DISPLAYED_COLUMN))
        assertFalse(displayed_columns.contains(NOT_DISPLAYED_COLUMN))
    }

    def searchForDoc(docType) {
        def lookupable = new KualiLookupableImpl()
        def lhs = new DocumentSearchCriteriaBoLookupableHelperService();
        lhs.setDocumentSearchCriteriaProcessor(new DocumentSearchCriteriaProcessorKEWAdapter())
        lhs.setDocumentSearchCriteriaTranslator(new DocumentSearchCriteriaTranslatorImpl())
        lookupable.setLookupableHelperService(lhs)
        lookupable.setParameters([:]) // need to set params before calling #setBusinessObjectClass
        lookupable.setBusinessObjectClass(DocumentSearchCriteriaBo.class)

        def resultTable = [] as Collection<ResultRow>

        def displayList = GlobalVariables.doInNewGlobalVariables(new UserSession(USER_NETWORK_ID), new Callable<Collection<? extends BusinessObject>>() {
            public Collection<? extends BusinessObject> call() {
                LookupForm form = new LookupForm()
                def fields = [
                        documentTypeName: docType
                ]
                form.setFields(fields)
                form.setFieldsForLookup(fields)

                routeDoc(docType)

                return lookupable.performLookup(form, resultTable, false)
            }
        })

        return [displayList, resultTable]
    }

    protected void routeDoc(docType) {
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(principalId, docType);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");
    }
    
    protected String getPrincipalId(String networkId){
        return KimApiServiceLocator.getPersonService().getPersonByPrincipalName(networkId).getPrincipalId();
    }
}
