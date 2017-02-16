package org.kuali.ole.docstore.discovery.service;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for DiscoveryService class.
 * User: tirumalesh.b
 * Date: 22/11/11 Time: 5:23 PM
 */
public class DiscoveryService_UT
        extends BaseTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuildQuery() {
        assertTrue(true);
        SearchParams searchParams = getSearchParams();
        searchParams.setSearchType("moreFacets");
        DiscoveryService discoveryService = ServiceLocator.getDiscoveryService();
        Map<String, String> facetTermsMap = new HashMap<String, String>();
        facetTermsMap.put("Author_display", "Author");
        facetTermsMap.put("Title_display", "Title");
        searchParams.setFacetTermsMap(facetTermsMap);
        searchParams.setResultPageSize("50");
        searchParams.setResultFromIndex("0");
        String query = discoveryService.buildQuery(searchParams);
        assertNotNull(query);
        LOG.info("query:" + query);

    }

    @Test
    public void testSearch() throws Exception {
        SearchParams searchParams = getSearchParams();
        //System.setProperty("docSearchURL", "http://dev.docstore.oleproject.org/");
       // System.out.println("url:" + ConfigContext.getCurrentContextConfig().getProperty("docSearchURL"));
        String query = "";

        searchParams.setSearchType("moreFacets");
        DiscoveryService discoveryService = DiscoveryServiceImpl.getInstance();
        query = discoveryService.search(searchParams);
        assertNotNull(query);
        LOG.info("search Type:" + searchParams.getSearchType());
        LOG.info("query:" + query);


        searchParams = getSearchParams();
        searchParams.setSearchType("linksearch");
        query = discoveryService.search(searchParams);
        LOG.info("search Type:" + searchParams.getSearchType());
        LOG.info("query:" + query);


        searchParams = getSearchParams();
        searchParams.setSearchType("advancedSearch");
        searchParams.setResultFromIndex("0");
        searchParams.setResultPageSize("50");
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchText("Carl San");
        searchCondition.setOperator("NOT");
        searchCondition.setDocField("all");
        searchCondition.setSearchScope("OR");
        searchConditionList.add(searchCondition);
        searchParams.setSearchFieldsList(searchConditionList);

        List<String> fieldList = new ArrayList<String>();
        fieldList.add("Author");
        fieldList.add("Subject");
        searchParams.setFieldList(fieldList);
        searchParams.setSortField("Author");
        query = discoveryService.search(searchParams);
        LOG.info("search Type:" + searchParams.getSearchType());
        LOG.info("query:" + query);


    }

    public SearchParams getSearchParams() {
        SearchParams searchParams = new SearchParams();
        searchParams.setDocCategory("work");
        searchParams.setDocType("bibliographic");
        searchParams.setDocFormat("marc");
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchText("Carl San");
        searchCondition.setOperator("NOT");
        searchCondition.setDocField("Author_search");
        searchCondition.setSearchScope("OR");
        searchConditionList.add(searchCondition);
        searchCondition = new SearchCondition();
        searchCondition.setSearchText("Sandburg");
        searchCondition.setOperator("NOT");
        searchCondition.setDocField("Author_search");
        searchCondition.setSearchScope("OR");
        searchConditionList.add(searchCondition);
        searchParams.setSearchFieldsList(searchConditionList);
        //        searchParams.setSearchType("moreFacets");
        return searchParams;
    }

}
