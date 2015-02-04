package org.kuali.ole.docstore.discovery.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcFields;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Acceptance Test to test the Indexed Records of various document formats [Marc/Dublin/Dublin_Unqualified].
 *
 * @author Rajesh Chowdary K
 * @created Jan 20, 2012
 */
public class DiscoveryIndexer_AT extends BaseTestCase implements WorkBibMarcFields {

    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryIndexer_AT.class);
    //private IndexerService indexerService = null;
    public Map<String, Object> fieldToExpectedValueMap = new HashMap<String, Object>();
    private List<Map<String, Object>> expRecs = null;
    private Map<String, List<List<String>>> expectedFacetsDetailsMap = new HashMap<String, List<List<String>>>();
    private List<List<String>> facetValuesList = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        //indexerService = IndexerServiceImpl.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        //cleanUpData();
    }
    @Ignore
    @Test
    public void testDiscoveryOutcomeforMarc() throws Throwable {
        LOG.info("Test: Marc Verifying.....");
        String inputRecordsFile = "/bib/bib/marc/marc_test.xml";
        String expectedResultsFile = "/bib/bib/marc/marc_test.properties";
        setEnvironment(expectedResultsFile);
        indexFile(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(), inputRecordsFile);
        boolean succeeded1 = testSolrOutputDocsForInputXML();
        boolean succeeded2 = testFacetsForInputXML();
        assertTrue(succeeded1 && succeeded2);
    }
    @Ignore
    @Test
    public void testDiscoveryOutcomeforDublin() throws Throwable {
        LOG.info("Test: Dublin Core Verifying.....");
        String inputRecordsFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test1.xml";
        String expectedResultsFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test1.properties";
        setEnvironment(expectedResultsFile);
        indexFile(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.DUBLIN_CORE.getCode(), inputRecordsFile);
        boolean succeeded1 = testSolrOutputDocsForInputXML();
        boolean succeeded2 = testFacetsForInputXML();
        assertTrue(succeeded1 && succeeded2);
    }
    @Ignore
    @Test
    public void testDiscoveryOutcomeforDublinUnQ() throws Throwable {
        LOG.info("Test: Dublin Core Unqualified Verifying.....");
        String inputRecordsFile = "/bib/bib/dublin/unqualified/Bib-Bib-DublinUnQ-Test1.xml";
        String expectedResultsFile = "/bib/bib/dublin/unqualified/Bib-Bib-DublinUnQ-Test1.properties";
        setEnvironment(expectedResultsFile);
        indexFile(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.DUBLIN_UNQUALIFIED.getCode(), inputRecordsFile);
        boolean succeeded1 = testSolrOutputDocsForInputXML();
        boolean succeeded2 = testFacetsForInputXML();
        assertTrue(succeeded1 && succeeded2);
    }

    public void setEnvironment(String expectedResultsFile) throws Exception {
        SolrFieldsFileReader expectedValues = new SolrFieldsFileReader(expectedResultsFile);
        expRecs = expectedValues.getRecords();
        getExpectedFacetsDetailsMap(expRecs);
        cleanUpData();
    }

    private void getExpectedFacetsDetailsMap(List<Map<String, Object>> expRecs) {
        Map<String, Object> map = null;
        Set<String> set = null;
        Iterator<String> ite = null;
        String next = null;
        String temp = "";
        List<String> facetValueList = null;
        for (int k = 0; k < expRecs.size(); k++) {
            map = expRecs.get(k);
            set = map.keySet();
            ite = set.iterator();
            while (ite.hasNext()) {
                next = ite.next();
                temp = next.substring(next.indexOf("_"));
                if (temp.equalsIgnoreCase("_facet")) {
                    facetValueList = (List<String>) map.get(next);
                    if (expectedFacetsDetailsMap.containsKey(next)) {
                        expectedFacetsDetailsMap.get(next).add(facetValueList);
                    } else {
                        facetValuesList = new ArrayList<List<String>>();
                        facetValuesList.add(facetValueList);
                        expectedFacetsDetailsMap.put(next, facetValuesList);
                    }
                }

            }
        }
    }

    public boolean testFacetsForInputXML() throws Throwable {
        //Subject,  Author, Format, Language, Publication Date, Genre
        Map<String, Integer> facetCount = new HashMap<String, Integer>();
        String facetFieldName = "";
        boolean hasNoErrors = true;
        FacetField facetField = null;
        Set<String> facetsFieldsSet = expectedFacetsDetailsMap.keySet();
        Iterator<String> facetsFieldsIterator = facetsFieldsSet.iterator();
        QueryResponse response = executeFacetQuery("*:*", facetsFieldsSet);
        while (facetsFieldsIterator.hasNext()) {
            facetFieldName = facetsFieldsIterator.next();
            LOG.info("Verifying Facet: " + facetFieldName);
            facetField = response.getFacetField(facetFieldName);
            List<List<String>> expectedFacetValuesList = expectedFacetsDetailsMap.get(facetFieldName);
            List<String> convertedFacetsList = convertMultipleFacetsListToSingleFacetList(expectedFacetValuesList);
            Map<String, Integer> facetValueCountMap = getFacetCount(convertedFacetsList);
            try {
                verifyFacetFieldValues(facetValueCountMap, facetFieldName, facetField);
            } catch (AssertionError e) {
                hasNoErrors = false;
                LOG.error(e.getMessage());
            }
        }
        return hasNoErrors;
    }

    private void verifyFacetFieldValues(Map<String, Integer> facetValueCountMap, String facetFieldName,
                                        FacetField facetField) {
        FacetField.Count fc = null;
        for (int i = 0; i < facetField.getValues().size(); i++) {
            fc = facetField.getValues().get(i);
            if (facetValueCountMap.containsKey(fc.getName())) {

                assertEquals(fc.getCount(), facetValueCountMap.get(fc.getName()).intValue());
            } else {
                String msg = "could not test facetFieldName " + facetFieldName + " for " + fc.getName()
                        + " because actual value " + fc.getName() + " is not defined in the properties file";
                assertNotNull(msg, facetValueCountMap.get(fc.getName()));
            }
        }
    }

    private void verifyFacetFieldValue(FacetField facetField, String facetFieldValue, int facetFieldCount) {
        FacetField.Count fc = null;
        int count = 0;
        boolean matched = false;
        LOG.info("Facet field name=" + facetField.getName() + "; Expected facetFieldValue=" + facetFieldValue
                + "; facetFieldCount=" + facetFieldCount);

        List<FacetField.Count> facetFieldValues = facetField.getValues();
        if (null != facetFieldValues) {
            for (int i = 0; i < facetFieldValues.size(); i++) {
                fc = facetFieldValues.get(i);
                //LOG.info("fc.getName()=" + fc.getName() + "; fc.getCount()=" + fc.getCount());
                if (fc.getName().equals(facetFieldValue)) {
                    LOG.info("Expected facetFieldValue found.");
                    assertEquals(facetFieldCount, fc.getCount());
                    matched = true;
                    break;
                }
            }
        }
        if (!matched) {
            LOG.info("Expected facetFieldValue not found.");
        }
        assertEquals(matched, true);
    }

    private List<String> convertMultipleFacetsListToSingleFacetList(List<List<String>> list) {
        List<String> convertedList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            for (int k = 0; k < list.get(i).size(); k++) {
                convertedList.add(list.get(i).get(k));
            }
        }
        return convertedList;

    }

    private QueryResponse executeFacetQuery(String args, Set<String> facetFields) {
        QueryResponse response = null;
        try {
            String docSearchUrl="";
            if(ConfigContext.getCurrentContextConfig()!=null){
                docSearchUrl=ConfigContext.getCurrentContextConfig().getProperty("docSearchURL");
            }
            else{
                docSearchUrl="http://localhost:8080/oledocstore";
            }

            SolrServer solr = new HttpSolrServer(docSearchUrl + "bib");
            SolrQuery query = new SolrQuery();
            query.setQuery(args);
            Iterator<String> facetFieldsIterator = facetFields.iterator();
            while (facetFieldsIterator.hasNext()) {
                query.addFacetField(facetFieldsIterator.next());

            }
            //query.addFacetField(facetField);
            query.setFacet(true);
            response = solr.query(query);
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
        return response;
    }

    private Map<String, Integer> getFacetCount(List<String> list) {
        Map<String, Integer> facetCount = new HashMap<String, Integer>();
        for (String word : list) {
            Integer count = facetCount.get(word);
            facetCount.put(word, (count == null) ? 1 : count + 1);
        }
        return facetCount;
    }

    public boolean testSolrOutputDocsForInputXML() throws Throwable {
        // Getting one marc record ingested in solr.
        List<Object> actRecs = getSolrRecordsForQueryField("*", "*");
        String fieldNameTemp = "";
        boolean hasNoErrors = true;
        // validate no. of records
        assertEquals(expRecs.size(), actRecs.size());

        // Validation process for Record fetched from solr.
        for (int i = 0; i < expRecs.size(); i++) {
            for (String fieldName : expRecs.get(i).keySet()) {
                fieldNameTemp = fieldName.substring(fieldName.indexOf("_"));
                Object exceptedValue = expRecs.get(i).get(fieldName);
                Object actualValue = ((SolrDocument) actRecs.get(i)).getFieldValue(fieldName);
                if (!fieldNameTemp.equalsIgnoreCase("_facet")) {
                    LOG.info("Verifying Field: " + fieldName + "\t\t:\t\t" + actualValue);
                    try {
                        assertEquals(exceptedValue == null ? "" : exceptedValue, actualValue == null ? "" : actualValue);
                    } catch (AssertionError e) {
                        hasNoErrors = false;
                        LOG.error(e.getMessage());
                    }
                }
            }
        }

        return hasNoErrors;
    }

    protected void indexFile(String docCategory, String docType, String docFormat, String inputRecordsFile) throws Exception {
        URL resource = getClass().getResource(inputRecordsFile);
        File file = new File(resource.toURI());
        String docContent = readFile(file);
        String result = null;
        // TODO: Use indexerService.indexDocumentsFromFileBySolrDoc()
        result = getIndexerService(docCategory, docType, docFormat).indexDocumentsFromStringBySolrDoc(docCategory, docType, docFormat, docContent);
        assertNotNull(result);
        assertTrue(result.contains(IndexerService.SUCCESS));
        String countStr = result.substring(result.indexOf("-") + 1);
        int count = Integer.parseInt(countStr);
        assertTrue(count > 0);
    }

    private List<Object> getSolrRecordsForQueryField(String fieldName, String fieldValue) throws Exception {
        SolrDocument doc = null;
        List<Object> records = new ArrayList<Object>();
        String args = fieldName + ":" + fieldValue;
        QueryResponse response = executeQuery(args);
        SolrDocumentList solrDocumentList = response.getResults();
        for (int i = 0; i < solrDocumentList.size(); i++) {
            doc = solrDocumentList.get(i);
            records.add(doc);
        }
        return records;
    }

    private IndexerService getIndexerService(String category, String type, String format) {
        IndexerService indexerService = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(category, type, format);
        return indexerService;
    }

}
