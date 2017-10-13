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
package org.kuali.rice.kew.quicklinks;

import org.junit.Test;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.quicklinks.service.QuickLinksService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.SQLDataLoader;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the QuickLinks Service
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class QuickLinksServiceTest extends KEWTestCase {
    private static String principalId = "admin";
    private static String badPrincipalId = "joeshmoe";

    private QuickLinksService service;

    @Override
    protected void loadTestData() throws Exception {
        new SQLDataLoader("classpath:org/kuali/rice/kew/quicklinks/actionItem.sql", ";").runSql();
        new SQLDataLoader("classpath:org/kuali/rice/kew/quicklinks/documentRoute.sql", ";").runSql();
        new SQLDataLoader("classpath:org/kuali/rice/kew/quicklinks/documentType.sql", ";").runSql();
        new SQLDataLoader("classpath:org/kuali/rice/kew/quicklinks/userOption.sql", ";").runSql();
    }

    @Override
    protected void setUpAfterDataLoad() throws Exception {
        service = (QuickLinksService) KEWServiceLocator.getService(KEWServiceLocator.QUICK_LINKS_SERVICE);
    }

    @Test
    public void testGetActionListStats() {
        List<ActionListStats> actionListStats = service.getActionListStats(principalId);
        assertNotNull("No collection returned", actionListStats);
        assertTrue("No test data", actionListStats.size() > 0);
        assertEquals("Wrong number of Action List Stats", 3, actionListStats.size());
        ActionListStats als = actionListStats.get(0);
        assertEquals("Wrong count", 1, als.getCount());
        assertEquals("Wrong Type Label", "Add/modify EDEN workgroup", als.getDocumentTypeLabelText());
        assertEquals("Wrong Type name", "EDENSERVICE-DOCS.WKGRPREQ", als.getDocumentTypeName());
        als = actionListStats.get(2);
        assertEquals("Wrong count", 4, als.getCount());
        assertEquals("Wrong Type Label", "Travel Request", als.getDocumentTypeLabelText());
        assertEquals("Wrong Type name", "TravelRequest", als.getDocumentTypeName());

        actionListStats = service.getActionListStats(badPrincipalId);
        assertNotNull("No collection returned", actionListStats);
        assertFalse("Found test data", actionListStats.size() > 0);
    }

    @Test
    public void testGetWatchedDocuments() {
        List<WatchedDocument> watchedDocuments = service.getWatchedDocuments(principalId);
        assertNotNull("No collection returned", watchedDocuments);
        assertTrue("No test data", watchedDocuments.size() > 0);
        assertEquals("Wrong number of Watched Documents", 28, watchedDocuments.size());

        WatchedDocument wd = watchedDocuments.get(0);
        assertEquals("Wrong header id", "2694", wd.getDocumentHeaderId());
        assertEquals("Wrong status code", "ENROUTE", wd.getDocumentStatusCode());
        assertEquals("Wrong document title", "Travel Doc 2 - esdf", wd.getDocumentTitle());

        wd = watchedDocuments.get(27);
        assertEquals("Wrong header id", "2120", wd.getDocumentHeaderId());
        assertEquals("Wrong status code", "ENROUTE", wd.getDocumentStatusCode());
        assertEquals("Wrong document title", "Routing workgroup CreatinAGroup123", wd.getDocumentTitle());

        watchedDocuments = service.getWatchedDocuments(badPrincipalId);
        assertNotNull("No collection returned", watchedDocuments);
        assertFalse("Found test data", watchedDocuments.size() > 0);
    }

    @Test
    public void testGetRecentSearches() {
		DateFormat df = new SimpleDateFormat("'Created='MM/dd/yyyy'..;'");
        List<KeyValue> recentSearches = service.getRecentSearches(principalId);

        assertNotNull("No collection returned", recentSearches);
        assertTrue("No test data", recentSearches.size() > 0);
        assertEquals("Wrong number of Recent Searches", 5, recentSearches.size());

        KeyValue kv = recentSearches.get(0);
        assertEquals("Wrong key", "DocSearch.LastSearch.Holding4", kv.getKey());
        assertEquals("Wrong value", df.format(new Date( 1229925600000L)), kv.getValue().trim());

        kv = recentSearches.get(4);
        assertEquals("Wrong key", "DocSearch.LastSearch.Holding0", kv.getKey());
        assertEquals("Wrong value", df.format(new Date( 1225778400000L)), kv.getValue().trim());

        recentSearches = service.getRecentSearches(badPrincipalId);
        assertNotNull("No collection returned", recentSearches);
        assertFalse("Found test data", recentSearches.size() > 0);
    }

    @Test
    public void testGetNamedSearches() {
        List<KeyValue> namedSearches = service.getNamedSearches(principalId);
        assertNotNull("No collection returned", namedSearches);
        assertTrue("No test data", namedSearches.size() > 0);
        assertEquals("Wrong number of Named Searches", 3, namedSearches.size());

        KeyValue kv = namedSearches.get(0);
        assertEquals("Wrong key", "DocSearch.NamedSearch.FindAlumni", kv.getKey());
        assertEquals("Wrong value", "FindAlumni", kv.getValue());

        kv = namedSearches.get(2);
        assertEquals("Wrong key", "DocSearch.NamedSearch.FindStudent", kv.getKey());
        assertEquals("Wrong value", "FindStudent", kv.getValue());

        namedSearches = service.getRecentSearches(badPrincipalId);
        assertNotNull("No collection returned", namedSearches);
        assertFalse("Found test data", namedSearches.size() > 0);
    }

    @Test
    public void testGetInitiatedDocumentTypesList() {
        List<InitiatedDocumentType> initiatedDocumentTypesList = service.getInitiatedDocumentTypesList(principalId);
        assertNotNull("No collection returned", initiatedDocumentTypesList);
        assertTrue("No test data", initiatedDocumentTypesList.size() > 0);
        assertEquals("Wrong number of Document Types List", 8, initiatedDocumentTypesList.size());

        InitiatedDocumentType idt = initiatedDocumentTypesList.get(0);
        assertEquals("Wrong Type Label Text", "Add/modify EDEN workgroup", idt.getDocumentTypeLabelText());
        assertEquals("Wrong Type Name", "EDENSERVICE-DOCS.WKGRPREQ", idt.getDocumentTypeName());

        idt = initiatedDocumentTypesList.get(7);
        assertEquals("Wrong Type Label Text", "Travel Request", idt.getDocumentTypeLabelText());
        assertEquals("Wrong Type Name", "TravelRequest", idt.getDocumentTypeName());


        initiatedDocumentTypesList = service.getInitiatedDocumentTypesList(badPrincipalId);
        assertNotNull("No collection returned", initiatedDocumentTypesList);
        assertFalse("Found test data", initiatedDocumentTypesList.size() > 0);
    }
}
