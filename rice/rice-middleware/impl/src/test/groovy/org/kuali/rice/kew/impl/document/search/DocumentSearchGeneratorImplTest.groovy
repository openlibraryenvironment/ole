/**
 * Copyright 2005-2014 The Kuali Foundation
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

import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.framework.persistence.platform.MySQLDatabasePlatform
import org.kuali.rice.kew.api.KewApiConstants
import org.kuali.rice.kew.api.doctype.DocumentType
import org.kuali.rice.kew.api.doctype.DocumentTypeService
import org.kuali.rice.kew.api.document.DocumentStatus
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria
import org.kuali.rice.kew.api.document.search.DocumentSearchResult
import org.kuali.rice.kew.api.document.search.DocumentSearchResults

import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp

import static groovy.util.GroovyTestCase.assertEquals

class DocumentSearchGeneratorImplTest {

    private static final int ITERATION_COUNT = 3;
    private static final int FETCH_ITERATION_LIMIT = 10;
    private static final int MAX_RESULT_CAP = KewApiConstants.DOCUMENT_LOOKUP_DEFAULT_RESULT_CAP;
    private static final int FETCH_LIMIT = FETCH_ITERATION_LIMIT * MAX_RESULT_CAP;
    private static final int TOTAL_RESULT_COUNT = MAX_RESULT_CAP * ITERATION_COUNT;

    DocumentSearchGeneratorImpl documentSearchGenerator;

    private StubFor stubStatement;
    private StubFor stubResultSet;
    private int index;

    private MockFor mockDocumentTypeService;

    @Before
    void setup() {
        documentSearchGenerator = new DocumentSearchGeneratorImpl();

        stubStatement = new StubFor(Statement.class);
        stubResultSet = new StubFor(ResultSet.class);
        index = 0;

        mockDocumentTypeService = new MockFor(DocumentTypeService.class);
    }

    // ensures that proper documentrouteheadervalue columns are getting returned from search sql
    @Test void testDocHeaderFields() {
        def generator = new DocumentSearchGeneratorImpl(dbPlatform: new MySQLDatabasePlatform());
        DocumentSearchCriteria.Builder dscb = DocumentSearchCriteria.Builder.create();
        def final EXPECTED = "Select * from ( select DISTINCT(DOC_HDR.DOC_HDR_ID), DOC_HDR.INITR_PRNCPL_ID, DOC_HDR.DOC_HDR_STAT_CD, DOC_HDR.CRTE_DT, DOC_HDR.TTL, DOC_HDR.APP_DOC_STAT, DOC_HDR.STAT_MDFN_DT, DOC_HDR.APRV_DT, DOC_HDR.FNL_DT, DOC_HDR.APP_DOC_ID, DOC_HDR.RTE_PRNCPL_ID, DOC_HDR.APP_DOC_STAT_MDFN_DT, DOC1.DOC_TYP_NM, DOC1.LBL, DOC1.DOC_HDLR_URL, DOC1.ACTV_IND  from KREW_DOC_TYP_T DOC1 , KREW_DOC_HDR_T DOC_HDR   where DOC_HDR.DOC_HDR_STAT_CD != 'I' and  DOC_HDR.DOC_TYP_ID = DOC1.DOC_TYP_ID  ) FINAL_SEARCH order by FINAL_SEARCH.CRTE_DT desc"
        assertEquals(generator.generateSearchSql(dscb.build(), []), EXPECTED)
    }

    // Ensures that multiple calls to processResultSet always generates the same number of records
    @Test void testProcessResultSet_multipleIterations() {
        DocumentSearchCriteria.Builder criteriaBuilder = DocumentSearchCriteria.Builder.create();

        DocumentType.Builder builder = DocumentType.Builder.create("MockDocumentType");
        builder.setId("1");
        DocumentType mockDocumentType = builder.build();

        // Called once per result set
        mockDocumentTypeService.demand.getDocumentTypeByName(1..TOTAL_RESULT_COUNT) {
            return mockDocumentType;
        }

        // Called once per result set per iteration
        stubResultSet.demand.next(1..(TOTAL_RESULT_COUNT * ITERATION_COUNT)) {
            return true;
        }

        // Called nine times per result set
        stubResultSet.demand.getString(1..(TOTAL_RESULT_COUNT * 9)) {
            String columnLabel ->
                if ("DOC_HDR_ID".equals(columnLabel)) {
                    return index++;
                } else if ("INITR_PRNCPL_ID".equals(columnLabel)) {
                    return "admin";
                } else if ("DOC_TYP_NM".equals(columnLabel)) {
                    return "MockDocumentType";
                } else if ("DOC_HDR_STAT_CD".equals(columnLabel)) {
                    DocumentStatus.FINAL.getCode();
                } else {
                    return "";
                }
        }

        // Called five times per result set
        stubResultSet.demand.getTimestamp(1..(TOTAL_RESULT_COUNT * 5)) {
            return new Timestamp(System.currentTimeMillis());
        }

        Statement statement = stubStatement.proxyDelegateInstance();
        ResultSet resultSet = stubResultSet.proxyDelegateInstance();

        documentSearchGenerator.setApiDocumentTypeService(mockDocumentTypeService.proxyDelegateInstance());

        for (i in 0..ITERATION_COUNT - 1) {
            int startAtIndex = MAX_RESULT_CAP * i;
            int endAtIndex = (MAX_RESULT_CAP * (i + 1)) - 1;

            criteriaBuilder.setStartAtIndex(startAtIndex);

            DocumentSearchCriteria criteria = criteriaBuilder.build();

            DocumentSearchResults.Builder resultsBuilder = documentSearchGenerator.processResultSet(
                    criteria, true, statement, resultSet, MAX_RESULT_CAP, FETCH_LIMIT);

            List<DocumentSearchResult.Builder> results = resultsBuilder.getSearchResults();
            Assert.assertEquals("Iteration " + i + " should have " + MAX_RESULT_CAP + " results.",
                    MAX_RESULT_CAP, results.size());

            DocumentSearchResult.Builder firstResultBuilder = results.get(0);
            Assert.assertEquals(String.valueOf(startAtIndex), firstResultBuilder.getDocument().getDocumentId());

            DocumentSearchResult.Builder lastResultBuilder = results.get(results.size() - 1);
            Assert.assertEquals(String.valueOf(endAtIndex), lastResultBuilder.getDocument().getDocumentId());
        }
    }
}
