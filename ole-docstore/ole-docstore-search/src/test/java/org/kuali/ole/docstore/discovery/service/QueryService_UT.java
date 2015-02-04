package org.kuali.ole.docstore.discovery.service;

import junit.framework.Assert;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.discovery.model.CallNumberBrowseParams;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.bo.*;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.net.URL;
import java.util.*;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * User: tirumalesh.b
 * Date: 16/1/12 Time: 2:58 PM
 */
@Ignore
@Deprecated
public class QueryService_UT extends BaseTestCase {

    private QueryService queryService = null;
    //private IndexerService indexerService = null;
//    protected Server server = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();
//        System.setProperty(SolrServerManager.SOLR_HOME, "../OLE-SOLR");
        //System.setProperty(IndexerServiceImpl.SOLR_SERVER_EMBEDDED, "true");
        queryService = ServiceLocator.getQueryService();
        //indexerService = ServiceLocator.getIndexerService();
        MockitoAnnotations.initMocks(this);
        // TODO: Make sure discovery webapp is running.
        //startJetty();
        // http://ptrthomas.wordpress.com/2009/01/24/how-to-start-and-stop-jetty-revisited/
        // http://docs.codehaus.org/display/JETTY/Embedding+Jetty
        // http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
    }

    @After
    public void tearDown() throws Exception {
        //stopJetty();
    }


    public void testQueryField() throws Exception {
        String inputFile = "/bib/bib/marc/MarcRecord.xml";
        indexFile(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(), inputFile);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("Author_search", "Test-Author");
        SearchParams searchParams = buildSearchParams(queryParams);

        String value = queryService.queryField(searchParams, "Author_display");
        System.out.println("value " + value);
        assertEquals(value, "Test-Author");
        cleanUpData();
    }

    public void testBuildQueryForDoc() {
        OleDocument od = new OleDocument();
        queryService.buildQueryForDoc(od);
    }

    public void testGetUUIDList() {
        queryService.getUUIDList(null, null);

    }

    @Test
    public void testGetTitleValues() {
        queryService.getTitleValues(null);
    }

    @Test
    public void testQueryForDocs() throws Exception {
        //Index sample file
        cleanUpData();
        String inputFile = "/bib/bib/marc/MarcRecord.xml";
        indexFile(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(), inputFile);
        testForSingleRecord();
        testForMultipleRecords();
        //Clean up Indexed data
        cleanUpData();
    }

    public void searchForBibByInstance() throws Exception {
        cleanUpData();
        QueryResponse response;
        String instanceRequestFile = "/work/instance/oleml/Instance-Request.xml";
        URL resource = getClass().getResource(instanceRequestFile);
        File file = new File(resource.toURI());
        String instanceRequestContent = readFile(file);
        RequestHandler requestHandler = new RequestHandler();
        org.kuali.ole.docstore.model.xmlpojo.ingest.Request request = requestHandler.toObject(instanceRequestContent);
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        for (RequestDocument requestDocument : requestDocumentList) {
            modifyRequestDocument(requestDocument);
            getIndexerService(requestDocument).indexDocument(requestDocument);
            response = searchBibRecord(DocCategory.WORK.getCode(), DocType.BIB.getDescription(),
                    DocFormat.MARC.getCode(), "id", requestDocument.getUuid());
            SolrDocumentList solrDocumentList = response.getResults();
            assertEquals(1, solrDocumentList.getNumFound());
            Object title = solrDocumentList.get(0).getFieldValue("Title_display");
            Object author = solrDocumentList.get(0).getFieldValue("Author_display");
            List<String> instanceIdentifier = (List<String>) solrDocumentList.get(0)
                    .getFieldValue("instanceIdentifier");
            WorkBibDocument workBibDocument = new WorkBibDocument();
            WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
            workInstanceDocument.setInstanceIdentifier(instanceIdentifier.get(0));
            workBibDocument.setInstanceDocument(workInstanceDocument);
            queryService.queryForDocs(workBibDocument);
            List<OleDocument> oleDocumentsList = queryService.queryForDocs(workBibDocument);
            for (OleDocument oleDocument : oleDocumentsList) {
                workBibDocument = (WorkBibDocument) oleDocument;
                assertEquals(workBibDocument.getTitles(), title);
                assertEquals(workBibDocument.getAuthors(), author);
            }
        }
    }

    public SearchParams buildSearchParams(Map<String, String> queryParams) {
        SearchParams searchParams = new SearchParams();
        searchParams.setDocCategory(DocCategory.WORK.getCode());
        searchParams.setDocType(DocType.BIB.getDescription());
        searchParams.setDocFormat(DocFormat.MARC.getCode());
        searchParams.setResultPageSize("10");
        searchParams.setResultFromIndex("1");
        searchParams.setResultPageIndex("1");


        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        searchParams.setSearchFieldsList(searchConditionList);
        if (null != queryParams) {
            for (Iterator<String> keys = queryParams.keySet().iterator(); keys.hasNext(); ) {
                String key = keys.next();
                String value = queryParams.get(key);
                SearchCondition searchCondition = new SearchCondition();
                searchCondition.setDocField(key);
                searchCondition.setSearchScope("AND");
                searchCondition.setSearchText(value);
                if (searchConditionList.size() > 0) {
                    searchCondition.setOperator("AND");
                }
                searchConditionList.add(searchCondition);
            }
        }
        return searchParams;
    }

    protected void indexFile(String docCategory, String docType, String docFormat, String inputRecordsFile) throws Exception {
        URL resource = getClass().getResource(inputRecordsFile);
        File file = new File(resource.toURI());
        String filePath = file.getAbsolutePath();
        String output = getIndexerService(docCategory, docType, docFormat).indexDocumentsFromFileBySolrDoc(docCategory, docType, docFormat, filePath);
        System.out.println("indexFile output " + output);
    }

    private List<Object> getSolrRecordsForQueryField(String fieldName, String fieldValue) throws Exception {
        SolrDocument doc;
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

    private void testForSingleRecord() throws Exception {
        //Get Solr Response
        List<Object> solrResponse = getSolrRecordsForQueryField("Title_display", "Test-Title");
        List<String> title = (List<String>) ((SolrDocument) solrResponse.get(0))
                .getFieldValue("Title_display");

        //Set values to OleDocument
        OleDocument oleDocument = new WorkBibDocument();
        ((WorkBibDocument) oleDocument).setTitles(title);
        List<OleDocument> docs = queryService.queryForDocs(oleDocument);
        assertEquals(1, docs.size());
        System.out.println("Title original" + ((WorkBibDocument) docs.get(0)).getTitles() + "expected " + title);
        assert (((WorkBibDocument) docs.get(0)).getTitles().equals(title));
    }

    private void testForMultipleRecords() throws Exception {
        //Get Solr Response
        List<Object> solrResponse = getSolrRecordsForQueryField("Author_display", "Test-Author");
        List<String> author = (List<String>) ((SolrDocument) solrResponse
                .get(0)).getFieldValue("Author_display");

        //Set values to OleDocument
        OleDocument oleDocument = new WorkBibDocument();
        ((WorkBibDocument) oleDocument).setAuthors(author);
        List<OleDocument> docs = queryService.queryForDocs(oleDocument);
        assertEquals(2, docs.size());
        System.out.println("Author original" + ((WorkBibDocument) docs.get(0)).getAuthors() + "expected " + author);
        assert (((WorkBibDocument) docs.get(0)).getAuthors().equals(author));
    }

    private static int count = 0;

    public void modifyRequestDocument(RequestDocument requestDocument) {
        count++;
        int oleItemCount = 0;
        String marcID = String.valueOf(Math.round(Math.random())+1);
        String oleHoldingID = String.valueOf(Math.round(Math.random())+1);
        String oleItemID = "";
        String instanceID = String.valueOf(Math.round(Math.random())+1);
        requestDocument.setUuid(marcID);
        InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        List<RequestDocument> linkedRequestDocumentsModified = new ArrayList<RequestDocument>();
        Content content = null;
        for (RequestDocument req : requestDocument.getLinkedRequestDocuments()) {
            InstanceCollection ic = instanceOlemlRecordProcessor.fromXML(req.getContent().getContent());
            List<Instance> oleInstances = ic.getInstance();
            req.getContent().setContentObject(ic);
            req.setUuid(instanceID);
            for (Instance oleInstance : oleInstances) {
                oleInstance.setInstanceIdentifier(instanceID);
                OleHoldings holding = oleInstance.getOleHoldings();
                holding.setHoldingsIdentifier(oleHoldingID);
                for (Item oleItem : oleInstance.getItems().getItem()) {
                    oleItemCount++;
                    oleItemID = String.valueOf(Math.round(Math.random()));
                    oleItem.setItemIdentifier(oleItemID);
                }
            }
        }

    }

    public QueryResponse searchBibRecord(String docCat, String docType, String docFormat, String fieldNameIdentifier,
                                         String fieldValueIdentifier) throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);
    }

    public QueryResponse searchRecord(String docCat, String docType, String docFormat, String fieldName,
                                      String fieldValue) throws Exception {
        LOG.info("fieldNameIdentifier " + fieldName);
        String identifier_args = "(" + fieldName + ":" + fieldValue + ")";
        String docCategory_args = "(DocCategory" + ":" + docCat + ")";
        String docType_args = "(DocType" + ":" + docType + ")";
        String docFormat_args = "(DocFormat" + ":" + docFormat + ")";
        String args = identifier_args + "AND" + docCategory_args + "AND" + docType_args + "AND" + docFormat_args;
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        SolrQuery query = new SolrQuery();
        LOG.info("args " + args);
        query.setQuery(args);
        QueryResponse response = solr.query(query);
        LOG.info("response " + response);
        return response;
    }

    @Test
    public void testGetBibDocuments() throws Exception {
        SearchParams searchParams = new SearchParams();
        searchParams.setDocCategory("work");
        searchParams.setDocType("bibliographic");
        searchParams.setDocFormat("marc");
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchText("Carl");
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
        List<WorkBibDocument> workBibDocumentList = queryService.getBibDocuments(searchParams);
        System.out.println("docs-->" + workBibDocumentList.size());
    }

    @Test
    public void testGetHoldingsDocuments() throws Exception {
        SearchParams searchParams = new SearchParams();
        searchParams.setDocCategory("work");
        searchParams.setDocType("holdings");
        searchParams.setDocFormat("oleml");
        List<WorkHoldingsDocument> workHoldingsDocuments = queryService.getHoldingDocuments(searchParams);
        System.out.println("docs-->" + workHoldingsDocuments.size());
    }

    @Test
    public void testGetItemDocuments() throws Exception {
        SearchParams searchParams = new SearchParams();
        searchParams.setDocCategory("work");
        searchParams.setDocType("item");
        searchParams.setDocFormat("oleml");
        List<WorkItemDocument> workItemDocuments = queryService.getItemDocuments(searchParams);
        System.out.println("docs-->" + workItemDocuments.size());
    }

    @Ignore
    @Test
    public void testCallNumberBrowseMatchIndex() throws Exception {
        cleanUpData();
        String inputFile = "/sample-request.xml";
        indexDocument(inputFile);
        LOG.info("Record count:" + getRecordCount());
        CallNumberBrowseParams callNumberBrowseParams = new CallNumberBrowseParams("", "LCC", "HD", 0, 0, 0, 0, 0, "item","");
        queryService.initCallNumberBrowse(callNumberBrowseParams);
        Assert.assertEquals(23, callNumberBrowseParams.getMatchIndex());
    }

    @Ignore
    @Test
    public void testBrowseCallNumbers() throws Exception {
        cleanUpData();
        String inputFile = "/sample-request.xml";
        indexDocument(inputFile);
        CallNumberBrowseParams callNumberBrowseParams = new CallNumberBrowseParams("", "LCC", "", 10, 18, 0, 0, 0, "item","");
        List<WorkBibDocument> workBibDocuments = queryService.browseCallNumbers(callNumberBrowseParams);
        String itemCallNumber1 = workBibDocuments.get(0).getWorkInstanceDocumentList().get(0).getItemDocumentList().get(0).getCallNumber();
        String itemCallNumber10 = workBibDocuments.get(9).getWorkInstanceDocumentList().get(0).getItemDocumentList().get(0).getCallNumber();
        assertNotNull(workBibDocuments);
        assertEquals("E175.5.A15 A3 2007 v.1 1996", itemCallNumber1.trim());
        assertEquals("HQ1815.5 .A176 v.1 2006 no.2", itemCallNumber10.trim());
    }


    public void indexDocument(String inputFile) throws Exception {
        cleanUpData();
        QueryResponse response;
        URL resource = getClass().getResource(inputFile);
        File file = new File(resource.toURI());
        String instanceRequestContent = readFile(file);
        RequestHandler requestHandler = new RequestHandler();
        org.kuali.ole.docstore.model.xmlpojo.ingest.Request request = requestHandler.toObject(instanceRequestContent);
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        LOG.info("requestDocuments size " + requestDocumentList.size());
        for (RequestDocument requestDocument : requestDocumentList) {
            modifyRequestDocument(requestDocument);
            getIndexerService(requestDocument).indexDocument(requestDocument);
            SolrDocumentList solrDocumentList = null;
            response = searchBibRecord(DocCategory.WORK.getCode(), DocType.BIB.getDescription(),
                    DocFormat.MARC.getCode(), "id", requestDocument.getUuid());
            // LOG.info("searchBibRecord : response " + response);
            solrDocumentList = response.getResults();

           // assertEquals(1, solrDocumentList.getNumFound());
            LOG.info("getNumFound marc " + solrDocumentList.getNumFound());
            for (RequestDocument requestDocumentLinks : requestDocument.getLinkedRequestDocuments()) {
                InstanceCollection ic = (InstanceCollection) requestDocumentLinks.getContent().getContentObject();
                for (Instance oleInstance : ic.getInstance()) {
                    for (Item oleItem : oleInstance.getItems().getItem()) {
                        response = searchItemsRecord(DocCategory.WORK.getCode(), "item",
                                DocFormat.OLEML.getCode(), "id", oleItem.getItemIdentifier());
                        solrDocumentList = response.getResults();
                        LOG.info("getNumFound oleitem " + solrDocumentList.getNumFound());
                        assertEquals(1, solrDocumentList.getNumFound());
                    }
                }
            }
        }
    }

    public QueryResponse searchInstanceRecord(String docCat, String docType, String docFormat, String fieldNameIdentifier,
                                              String fieldValueIdentifier) throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);

    }

    public QueryResponse searchHoldingRecord(String docCat, String docType, String docFormat, String fieldNameIdentifier,
                                             String fieldValueIdentifier) throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);

    }

    public QueryResponse searchItemsRecord(String docCat, String docType, String docFormat, String fieldNameIdentifier,
                                           String fieldValueIdentifier) throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);

    }

    @Test
    public void testIsFieldValueExists() {
        try {
            cleanUpData();
            String barcode = null;
            String itemId = null;
            String itemId2 = null;
            String barCodeField = "ItemBarcode_display";
            String instanceRequestFile = "/work/instance/oleml/Instance-Request.xml";
            URL resource = getClass().getResource(instanceRequestFile);
            File file = new File(resource.toURI());
            String instanceRequestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            org.kuali.ole.docstore.model.xmlpojo.ingest.Request request = requestHandler.toObject(instanceRequestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            RequestDocument requestDocument = requestDocumentList.get(0);
            modifyRequestDocument(requestDocument);

            // Verifying for item barcode before ingesting a new item record
            boolean isFieldExists = queryService.isFieldValueExists(DocType.INSTANCE.getDescription(), barCodeField, "1111", null);
            Assert.assertEquals("item barcode do not exists in solr", false, isFieldExists);

            indexRequestDocument(requestDocument, barcode, itemId);
            assertEquals("1111", barcode);

            // Verifying for same item barcode after ingesting a item record
            isFieldExists = queryService.isFieldValueExists(DocType.INSTANCE.getDescription(), barCodeField, barcode, null);
            Assert.assertEquals("item barcode exists in solr", true, isFieldExists);

            // verifying for item barcode to update item with same barcode
            isFieldExists = queryService.isFieldValueExists(DocType.INSTANCE.getDescription(), barCodeField, barcode, itemId);
            Assert.assertEquals("updating an item with the same barcode(with out updating barcode value)", false, isFieldExists);

            requestDocument = requestDocumentList.get(1);
            modifyRequestDocument(requestDocument);

            // Verifying for item barcode before ingesting a new item record
            isFieldExists = queryService.isFieldValueExists(DocType.INSTANCE.getDescription(), barCodeField, "2222", null);
            Assert.assertEquals("item barcode do not exists in solr", false, isFieldExists);

            indexRequestDocument(requestDocument, barcode, itemId2);
            assertEquals("2222", barcode);

            // Verifying for item barcode after ingesting a item record (trying to ingest item record with existing barcode)
            isFieldExists = queryService.isFieldValueExists(DocType.INSTANCE.getDescription(), barCodeField, "2222", null);
            Assert.assertEquals("item barcode exists in solr", true, isFieldExists);

            // update item barcode to 1111 for the second item record and verify its updated value in solr
            isFieldExists = queryService.isFieldValueExists(DocType.INSTANCE.getDescription(), barCodeField, "1111", itemId2);
            Assert.assertEquals("updating an item with the other barcode value that exists with other record in solr", true, isFieldExists);


        } catch (Exception e) {
            LOG.debug("exception " + e);
        }
    }

    private void indexRequestDocument(RequestDocument requestDocument, String barcode, String itemId) throws Exception {
        QueryResponse response;
        getIndexerService(requestDocument).indexDocument(requestDocument);
        response = searchBibRecord(DocCategory.WORK.getCode(), DocType.INSTANCE.getDescription(),
                DocFormat.OLEML.getCode(), "id", requestDocument.getLinkedRequestDocuments().get(0).getUuid());
        SolrDocumentList solrDocumentList = response.getResults();
        //assertEquals(1, solrDocumentList.getNumFound());
        List<String> itemIdentifier = (List<String>) solrDocumentList.get(0).getFieldValue("itemIdentifier");
        itemId = itemIdentifier.get(0);
        response = searchBibRecord(DocCategory.WORK.getCode(), DocType.ITEM.getDescription(), DocFormat.OLEML.getCode(), "id", itemId);
        solrDocumentList = response.getResults();
        barcode = (String) solrDocumentList.get(0).getFieldValue("ItemBarcode_display");
    }

    private IndexerService getIndexerService(RequestDocument requestDocument) {
        IndexerService indexerService = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        return indexerService;
    }

    private IndexerService getIndexerService(String category, String type, String format) {
        IndexerService indexerService = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(category, type, format);
        return indexerService;
    }

}
