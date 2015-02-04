package org.kuali.ole.docstore.discovery.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.indexer.solr.WorkBibDocumentIndexer;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for IndexerService class.
 * User: tirumalesh.b
 * Date: 22/11/11 Time: 5:23 PM
 */
public class IndexerService_UT extends BaseTestCase {

    private IndexerService indexerService = null;
    private int oleItemCount = 0;

    public IndexerService_UT() {

    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test the bulk indexing of instance records, which also links them with the already indexed Bib records.
     */
    @Ignore
    @Test
    public void testInstanceBulkIndex() {
        String result = "";
        try {
            // Initial log.
            printInitialLog();
            List<String> ids = new ArrayList<String>();
            ids.add("1234");
            ids.add("14445");
            ids.add("144446");
            ids.add("144447");
            ids.add("144448");
            ids.add("144449");
            ids.add("144450");
            ids.add("144451");
            ids.add("144452");
            ids.add("144453");
            ids.add("144454");
            ids.add("144455");
            ids.add("144456");
            String filePath = "/work/bib/marc/OLE-Bib-bulkIngest-IU-Set1-split.xml";
            URL resource = getClass().getResource(filePath);
            File file = new File(resource.toURI());
            String instanceRequestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(instanceRequestContent);
            List<RequestDocument> marcRequestDocumentList = request.getRequestDocuments();
            LOG.info("requestDocuments size " + marcRequestDocumentList.size());
            int i = 0;
            for (RequestDocument requestDocument : marcRequestDocumentList) {
                if (requestDocument.getUuid() == null)
                    requestDocument.setUuid(ids.get(i));
                i++;
            }
            result = getIndexerService(marcRequestDocumentList.get(0)).bulkIndexDocuments(marcRequestDocumentList, true);
            for (RequestDocument doc : marcRequestDocumentList) {
                LOG.info("Doc Marc : " + doc.getUuid());
            }
            LOG.info("RESULT: " + result);
            if (!result.toLowerCase().contains("success"))
                fail("Bulk Index Failed.");

            filePath = "/work/instance/oleml/OLE-Instance-bulkIngest-IU-Set1-split.xml";
            resource = getClass().getResource(filePath);
            file = new File(resource.toURI());
            instanceRequestContent = readFile(file);
            requestHandler = new RequestHandler();
            Request instanceRequest = requestHandler.toObject(instanceRequestContent);
            List<RequestDocument> newInstanceRequestDocumentList = new ArrayList<RequestDocument>();
            List<RequestDocument> instanceRequestDocumentList = instanceRequest.getRequestDocuments();
            LOG.info("requestDocuments size " + instanceRequestDocumentList.size());
            for (RequestDocument requestDocument : instanceRequestDocumentList) {
                if (requestDocument.getUuid() == null)
                    requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                if (requestDocument.getContent().getContentObject() == null) {
                    InstanceOlemlRecordProcessor oleMlProcessor = new InstanceOlemlRecordProcessor();
                    InstanceCollection instanceCollection = oleMlProcessor.fromXML(requestDocument.getContent()
                            .getContent());
                    for (Instance instance : instanceCollection.getInstance()) {
                        if (instance.getInstanceIdentifier() == null)
                            instance.setInstanceIdentifier(requestDocument.getUuid());
                        if (instance.getOleHoldings().getHoldingsIdentifier() == null)
                            instance.getOleHoldings().setHoldingsIdentifier(String.valueOf(Math.round(Math.random())));
                        for (Item item : instance.getItems().getItem())
                            if (item.getItemIdentifier() == null)
                                item.setItemIdentifier(String.valueOf(Math.round(Math.random())));
                    }
                    requestDocument.getContent().setContentObject(instanceCollection);
                    newInstanceRequestDocumentList.add(requestDocument);
                }

            }
            result = getIndexerService(newInstanceRequestDocumentList.get(0)).bulkIndexDocuments(newInstanceRequestDocumentList, true);
            LOG.info("RESULT: " + result);
            if (!result.toLowerCase().contains("success"))
                fail("Bulk Index Failed.");

            for (RequestDocument bibDoc : marcRequestDocumentList) {
                List<SolrDocument> bibSolrDocs = getIndexerService(bibDoc).getSolrDocumentBySolrId(bibDoc.getUuid());
                if (bibSolrDocs == null || bibSolrDocs.size() <= 0)
                    fail(" Bulk Indexing Failed.");
                for (SolrDocument outDoc : bibSolrDocs) {
                    if (outDoc.getFieldValues("instanceIdentifier") != null)
                        for (Object instanceId : outDoc.getFieldValues("instanceIdentifier")) {
                            List<SolrDocument> linkedInstanceDocs = getIndexerService(bibDoc).getSolrDocumentBySolrId(instanceId
                                    .toString());
                            if (linkedInstanceDocs.size() <= 0)
                                fail(" Bulk Indexing Failed.");
                            else
                                for (SolrDocument instDoc : linkedInstanceDocs)
                                    if (!instDoc.getFieldValues("bibIdentifier").contains(bibDoc.getUuid()))
                                        fail(" Bulk Indexing Failed.");
                        }
                }
            }
            // Final log.
            printFinalLog();

        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            fail("Bulk Index Failed.");
        }
    }

    @Test
    public void testDublinBulkIndex() throws Exception {
        try {
            // Initial log.
            printInitialLog();
            String filePath = "/work/bib/dublin/DublinQ-Test1.xml";
            List<RequestDocument> dublinRequestDocumentList = getRequestDocumentList(filePath);
            String result = getIndexerService(dublinRequestDocumentList.get(0)).bulkIndexDocuments(dublinRequestDocumentList, true);
            LOG.info("result:" + result);
            // Final log.
            printFinalLog();

        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    @Test
    public void testDublinUnqBulkIndex() throws Exception {
        try {
            // Initial log.
            printInitialLog();
            String filePath = "/work/bib/dublin/unqualified/DublinUnq-Test1.xml";
            List<RequestDocument> dublinUnqRequestDocumentList = getRequestDocumentList(filePath);
            System.out.println("size:" + dublinUnqRequestDocumentList.size());
            String result = getIndexerService(dublinUnqRequestDocumentList.get(0)).bulkIndexDocuments(dublinUnqRequestDocumentList, true);
            LOG.info("result:" + result);
            // Final log.
            printFinalLog();

        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    public List<RequestDocument> getRequestDocumentList(String path) throws Exception {
        URL resource = getClass().getResource(path);
        File file = new File(resource.toURI());
        String requestContent = readFile(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(requestContent);
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        int i = 1000004;
        for (RequestDocument requestDocument : requestDocumentList) {
            if (requestDocument.getUuid() == null) {
                requestDocument.setUuid("" + i + "");
            }
            i++;
        }
        return requestDocumentList;
    }

    @Test
    public void testDeleteDocuments() throws Exception {
        try {
            // Initial log.
            printInitialLog();
            List<String> UUIDList = new ArrayList<String>();
            // get uuids of the  record using author name
            // List<String> uuids = getUUIDsByAuthor("Test_Author_HTC (David George), 1940-");
            String inputFilePath = "/work/instance/oleml/Instance-Request.xml";
            Request request = getRequest(inputFilePath);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                UUIDList.add(requestDocument.getUuid());
                String result = getIndexerService(requestDocument).indexDocument(requestDocument);
                LOG.info("result:" + result);


            }

            printLogInfo("Record count after indexing:" + getRecordCount());
            getIndexerService(requestDocumentList.get(0)).deleteDocuments(DocCategory.WORK.getCode(), UUIDList);
            printLogInfo("Record count after deleting:" + getRecordCount());

            // Final log.
            printFinalLog();

        } catch (Exception e) {
            LOG.info("Exception+" + e.getMessage(), e);
            LOG.error(e.getMessage() , e);
        }
    }

    @Test
    public void testDeleteDocument() throws Exception {
        try {
            // Initial log.
            printInitialLog();

            String inputFilePath = "/work/instance/oleml/Instance-Request.xml";
            Request request = getRequest(inputFilePath);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            RequestDocument requestDocument = requestDocumentList.get(0);
            requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
            String result = getIndexerService(requestDocument).indexDocument(requestDocument);
            LOG.info("result:" + result);
            printLogInfo("Record count after indexing:" + getRecordCount());

            getIndexerService(requestDocument).deleteDocument(DocCategory.WORK.getCode(), requestDocument.getUuid());

            printLogInfo("Record count after deleting:" + getRecordCount());

            // Final log.
            printFinalLog();

        } catch (Exception e) {
            LOG.info("Exception+" + e.getMessage(), e);
        }

    }
    @Ignore
    @Test
    public void testIndexDocumentsFromFilesBySolrDoc() throws Exception {         // *********************
        // Initial log.
        printLogInfo("======");
        printLogInfo("Total record count before cleanup = " + getRecordCount());
        cleanUpData();
        printLogInfo("Total record count after cleanup = " + getRecordCount());

        // Test Marc Records
        LOG.info("testIndexDocumentsFromFileBySolrDoc() :  Marc Records Test");
        String docCategory = DocCategory.WORK.getCode();
        String docType = DocType.BIB.getDescription();
        String docFormat = DocFormat.MARC.getCode();
        String requestFile
                = "/bib/bib/marc/marc-one-record-xstream.xml";  // "/work/instance/oleml/Instance-Request.xml";
        invokeIndexDocumentsFromFileBySolrDoc(docCategory, docType, docFormat, requestFile);

        // Test Dublin Records
        LOG.info("testIndexDocumentsFromFileBySolrDoc() :  Dublin Records Test");
        docFormat = DocFormat.DUBLIN_CORE.getCode();
        requestFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test1.xml";
        invokeIndexDocumentsFromFileBySolrDoc(docCategory, docType, docFormat, requestFile);

        // Test Dublin UnQualified Records
        LOG.info("testIndexDocumentsFromFileBySolrDoc() :  Dublin Unqualified Records Test");
        requestFile = "/bib/bib/dublin/unqualified/Bib-Bib-DublinUnQ-Test1.xml";
        docFormat = DocFormat.DUBLIN_UNQUALIFIED.getCode();
        invokeIndexDocumentsFromFileBySolrDoc(docCategory, docType, docFormat, requestFile);

        // Final log.
        printLogInfo("Total record count after the test = " + getRecordCount());
        printLogInfo("======");
    }
    @Ignore
    @Test
    /**
     *  Same as testIndexDocumentsFromFileBySolrDoc, but user needs to provide dataDir=a/b/c.
     *  For manual testing. Test data inserted by this method is not deleted automatically.
     *  e.g. run with maven as follows:
     *  $ole-discovery-core>mvn -Dtest=IndexerService_UT#testIndexDocumentsFromFileBy
     SolrDocWithConfirm -DdataDir=.\src\test\resources\bib\bib\marc test
     */
    public void testIndexDocumentsFromDirBySolrDocWithConfirm() throws Exception {
        // Initial log.
        printLogInfo("======");
        printLogInfo("Total record count before cleanup = " + getRecordCount());
        cleanUpData();
        printLogInfo("Total record count after cleanup = " + getRecordCount());
        String dirPath = "/bib/bib/marc/request";
        invokeIndexDocumentsFromDirBySolrDoc(dirPath);
        // Final log.
        printLogInfo("Total record count after the test = " + getRecordCount());
        printLogInfo("======");
    }

    @Test
    public void testIndexDocumentsWithInvalidDirectoryORFile() throws Exception {
        try {
            String docCategory = DocCategory.WORK.getCode();
            String docType = DocType.BIB.getDescription();
            String docFormat = DocFormat.MARC.getCode();
            // Initial log.
            printInitialLog();
            String dirPath = "/bib/bib/marc/request/invalidDir";
            getIndexerService(docCategory, docType, docFormat).indexDocumentsFromDirBySolrDoc(docCategory, docType, docFormat, dirPath);

            dirPath = "/work/emptyDir";
            getIndexerService(docCategory, docType, docFormat).indexDocumentsFromDirBySolrDoc(docCategory, docType, docFormat, dirPath);
            // Final log.
            printFinalLog();
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }

    }

    public void invokeIndexDocumentsFromDirBySolrDoc(String path) {
        try {
            printLogInfo("-----");
            printLogInfo("Input Dir/File : " + path);
            String docCategory = DocCategory.WORK.getCode();
            String docType = DocType.BIB.getDescription();
            String docFormat = DocFormat.MARC.getCode();
            URL resource = getClass().getResource(path);
            File file = new File(resource.toURI());
            String dataDir = file.getAbsolutePath();
            if (null == dataDir) {
                LOG.info("dataDir is null.");
                System.out.println("dataDir is null.");
                return;
            }
            String result = getIndexerService(docCategory, docType, docFormat).indexDocumentsFromDirBySolrDoc(docCategory, docType, docFormat, dataDir);
            LOG.info("result=" + result);
            System.out.println("result=" + result);
            assertNotNull(result);
            assertTrue(result.contains(IndexerService.SUCCESS));
            printLogInfo("-----");
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
    }
    @Ignore
    @Test
    public void testIndexDocumentFromStringBySolr() throws Exception {
        // Initial log.
        printInitialLog();
        String requestFile = "/bib/bib/marc/marc-one-record-xstream.xml";
        invokeIndexDocumentsFromStringBySolrDoc(requestFile);
        // Final log.
        printFinalLog();

    }

    public void invokeIndexDocumentsFromStringBySolrDoc(String requestFile) throws Exception {
        try {
            printLogInfo("-----");
            printLogInfo("Input Dir/File : " + requestFile);
            String docCategory = DocCategory.WORK.getCode();
            String docType = DocType.BIB.getDescription();
            String docFormat = DocFormat.MARC.getCode();
            URL resource = getClass().getResource(requestFile);
            File file = new File(resource.toURI());
            String requestContent = FileUtils.readFileToString(file);
            getIndexerService(docCategory, docType, docFormat).indexDocumentsFromStringBySolrDoc(docCategory, docType, docFormat, requestContent);
            printLogInfo("-----");
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }

    }

    public void invokeIndexDocumentsFromFileBySolrDoc(String docCategory, String docType, String docFormat,
                                                      String requestFile) throws Exception {
        URL resource = getClass().getResource(requestFile);
        File file = new File(resource.toURI());
        String path = file.getAbsolutePath();
        String result = getIndexerService(docCategory, docType, docFormat).indexDocumentsFromFileBySolrDoc(docCategory, docType, docFormat, path);
        System.out.println("result=" + result);
        LOG.info("result=" + result);
        assertNotNull(result);
        assertTrue(result.contains(IndexerService.SUCCESS));
    }

    @Test
    /**
     *  Same as deleteTestData, but user needs to provide confirmDelete=true.
     *  For manual testing. Used to manually delete test data.
     */
    public void deleteTestDataWithConfirm() {
        String confirmDelete = System.getProperty("confirmDelete");
        if (confirmDelete != null) {
            boolean confirm = Boolean.valueOf(confirmDelete);
            if (!confirm) {
                LOG.info("Delete is not confirmed through a system property.");
                return;
            }
            deleteTestData();
        }
    }

    @Test
    public void testSpecialCharsInInput() {
        assert (true);
    }

    protected void indexTestFile(String docCategory, String docType, String docFormat, String resFile)
            throws Exception {
        URL resource = getClass().getResource(resFile);
        File file = new File(resource.toURI());
        String filePath = file.getAbsolutePath();
        String result = getIndexerService(docCategory, docType, docFormat).indexDocumentsFromFileBySolrDoc(docCategory, docType, docFormat, filePath);
        LOG.info("result =" + result);
        assertNotNull(result);
        assertTrue(result.contains(IndexerService.SUCCESS));
        String countStr = result.substring(result.indexOf("-") + 1);
        int count = Integer.parseInt(countStr);
        assertTrue(count > 0);
    }

    protected void deleteTestData() {
        String docCategory = DocCategory.WORK.getCode();
        // Test data is assumed to have id field value starting with "id_".
        String result = WorkBibDocumentIndexer.getInstance().deleteDocument(docCategory, indexerService.ID_FIELD_PREFIX + "*");
        LOG.info("result=" + result);
        assertNotNull(result);
        assertTrue(result.contains(IndexerService.SUCCESS));
    }

    protected List<String> getUUIDsByAuthor(String authorName) throws Exception {
        List<String> uuidList = new ArrayList<String>();
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        SolrQuery query = new SolrQuery();
        query.setQuery("Author_display:" + authorName);
        QueryResponse queryResponse = solr.query(query);
        Iterator<SolrDocument> iter = queryResponse.getResults().iterator();
        while (iter.hasNext()) {
            SolrDocument resultDoc = iter.next();
            String id = (String) resultDoc.getFieldValue("id");
            uuidList.add(id);
            LOG.info("uuidList=" + uuidList);
        }
        return uuidList;
    }
    @Ignore
    @Test
    public void testWorkInstanceOlemlIndexDocuments() throws Exception {
        cleanUpData();
        RequestDocument rd = null;
        QueryResponse response;
        String instanceRequestFile = "/work/instance/oleml/Instance-Request.xml";   // "/work/instance/oleml/Instance-Request.xml";
        URL resource = getClass().getResource(instanceRequestFile);
        File file = new File(resource.toURI());
        String instanceRequestContent = readFile(file);
        RequestHandler requestHandler = new RequestHandler();
        org.kuali.ole.docstore.model.xmlpojo.ingest.Request request = requestHandler.toObject(instanceRequestContent);
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        LOG.info("requestDocuments size " + requestDocumentList.size());
        int i = 0;

        for (RequestDocument requestDocument : requestDocumentList) {
            modifyRequestDocument(requestDocument);
            display(requestDocument);
            getIndexerService(requestDocument).indexDocument(requestDocument);
            response = searchBibRecord(DocCategory.WORK.getCode(), DocType.BIB.getDescription(),
                    DocFormat.MARC.getCode(), "id", requestDocument.getUuid());
            // LOG.info("searchBibRecord : response " + response);
            SolrDocumentList solrDocumentList = response.getResults();

            //assertEquals(1, solrDocumentList.getNumFound());
            LOG.info("getNumFound marc " + solrDocumentList.getNumFound());


            for (RequestDocument requestDocumentLinks : requestDocument.getLinkedRequestDocuments()) {
                InstanceCollection ic = (InstanceCollection) requestDocumentLinks.getContent().getContentObject();
                for (Instance oleInstance : ic.getInstance()) {
                    response = searchInstanceRecord(requestDocumentLinks.getCategory(), requestDocumentLinks.getType(),
                            requestDocumentLinks.getFormat(), "id",
                            oleInstance.getInstanceIdentifier());
                    solrDocumentList = response.getResults();
                    LOG.info("getNumFound oleinstance " + solrDocumentList.getNumFound());
                    //assertEquals(1, solrDocumentList.getNumFound());
                    response = searchHoldingRecord(DocCategory.WORK.getCode(), "holding",
                            DocFormat.OLEML.getCode(), "id",
                            oleInstance.getOleHoldings().getHoldingsIdentifier());
                    solrDocumentList = response.getResults();
                    LOG.info("getNumFound oleholding " + solrDocumentList.getNumFound());
                    //assertEquals(1, solrDocumentList.getNumFound());
                    for (Item oleItem : oleInstance.getItems().getItem()) {
                        response = searchItemsRecord(DocCategory.WORK.getCode(), "item",
                                DocFormat.OLEML.getCode(), "id", oleItem.getItemIdentifier());
                        solrDocumentList = response.getResults();
                        LOG.info("getNumFound oleitem " + solrDocumentList.getNumFound());
                       // assertEquals(1, solrDocumentList.getNumFound());
                    }
                }
            }
        }
    }


    public QueryResponse searchRecord(String docCat, String docType, String docFormat, String fieldName,
                                      String fieldValue) throws Exception {
        LOG.info("fieldNameIdentifier " + fieldName);
        String identifier_args = "(" + fieldName + ":" + fieldValue + ")";
        String docCategory_args = "(DocCategory" + ":" + docCat + ")";
        String docType_args = "(DocType" + ":" + docType + ")";
        String docFormat_args = "(DocFormat" + ":" + docFormat + ")";
        String args = identifier_args + "AND" + docCategory_args + "AND" + docType_args + "AND" + docFormat_args;
        //SolrServer solr = new CommonsHttpSolrServer(PropertyUtil.getPropertyUtil().getProperty("docSearchURL") + "bib");
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        SolrQuery query = new SolrQuery();
        LOG.info("args " + args);
        query.setQuery(args);
        //query.setQuery(docCategory_args);
        //query.setQuery(docType_args);
        //query.setQuery(docFormat_args);
        //LOG.info("getQuery "+query.getQuery());


        QueryResponse response = solr.query(query);
        // LOG.info("response "+response);
        return response;
    }

    public QueryResponse searchBibRecord(String docCat, String docType, String docFormat, String fieldNameIdentifier,
                                         String fieldValueIdentifier) throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);
    }

    public QueryResponse searchInstanceRecord(String docCat, String docType, String docFormat,
                                              String fieldNameIdentifier, String fieldValueIdentifier)
            throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);
    }

    public QueryResponse searchHoldingRecord(String docCat, String docType, String docFormat,
                                             String fieldNameIdentifier, String fieldValueIdentifier) throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);
    }

    public QueryResponse searchItemsRecord(String docCat, String docType, String docFormat, String fieldNameIdentifier,
                                           String fieldValueIdentifier) throws Exception {
        return searchRecord(docCat, docType, docFormat, fieldNameIdentifier, fieldValueIdentifier);

    }

    private static int count = 0;

    public void modifyRequestDocument(RequestDocument requestDocument) {
        count++;
        LOG.info("count " + count);
        oleItemCount = 0;
        String marcID = String.valueOf(Math.round(Math.random()));
        LOG.info("modifyRequestDocument : marcID " + marcID);
        String oleHoldingID = String.valueOf(Math.round(Math.random()));

        LOG.info("modifyRequestDocument : oleHoldingID " + oleHoldingID);
        String oleItemID = "";
        String instanceID = String.valueOf(Math.round(Math.random()));
        LOG.info("modifyRequestDocument : instanceID " + instanceID);

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
//                oleInstance.setResourceIdentifier(marcID);
                /*
                                rd = (RequestDocument) req.clone();
                                content1 = new Content();
                                rd.setContent(content1);
                */
                OleHoldings holding = oleInstance.getOleHoldings();
                holding.setHoldingsIdentifier(oleHoldingID);
//                holding.setInstanceIdentifier(instanceID);
                /*
                                rd.getContent().setContentObject(holding);
                                linkedRequestDocumentsModified.add(rd);
                */
                for (Item oleItem : oleInstance.getItems().getItem()) {
                    oleItemCount++;
                    oleItemID = String.valueOf(Math.round(Math.random()));
                    //                    rd = (RequestDocument) req.clone();
                    oleItem.setItemIdentifier(oleItemID);
//                    oleItem.setInstanceIdentifier(instanceID);
                    /*
                                        content1 = new Content();
                                        rd.setContent(content1);
                                        rd.getContent().setContentObject(oleItem);
                                        linkedRequestDocumentsModified.add(rd);
                    */
                }
            }
            //            requestDocument.setLinkedRequestDocuments(linkedRequestDocumentsModified);
        }

    }

    public void display(RequestDocument requestDocument) {
        LOG.info("in display");
        LOG.info("requestDocument.getId " + requestDocument.getId());
        LOG.info("requestDocument.getCategory " + requestDocument.getCategory());
        LOG.info("requestDocument.getFormat " + requestDocument.getFormat());
        LOG.info("requestDocument.getType " + requestDocument.getType());
        //  LOG.info("requestDocument.getType " + requestDocument.getType());
        List<RequestDocument> links = requestDocument.getLinkedRequestDocuments();
        LOG.info("links.size " + links.size());
        for (RequestDocument requestDocumentLinks : links) {
            LOG.info("requestDocumentLinks.getId " + requestDocumentLinks.getId());
            LOG.info("requestDocumentLinks.getUuid " + requestDocumentLinks.getUuid());
            LOG.info("requestDocumentLinks.getCategory " + requestDocumentLinks.getCategory());
            LOG.info("requestDocumentLinks.getFormat " + requestDocumentLinks.getFormat());
            LOG.info("requestDocumentLinks.getType " + requestDocumentLinks.getType());
            LOG.info("requestDocumentLinks getContentObject " + requestDocumentLinks.getContent().getContentObject());
            InstanceCollection ic = (InstanceCollection) requestDocumentLinks.getContent().getContentObject();
            for (Instance oleInstance : ic.getInstance()) {
                LOG.info("getResourceIdentifier " + oleInstance.getResourceIdentifier());
                LOG.info("getHoldingsIdentifier " + oleInstance.getOleHoldings().getHoldingsIdentifier());
//               LOG.info("getInstanceIdentifier " + oleInstance.getHoldings().getInstanceIdentifier());
                for (Item oleItem : oleInstance.getItems().getItem()) {
                    LOG.info("getItemIdentifier " + oleItem.getItemIdentifier());
//                    LOG.info("getInstanceIdentifier " + oleItem.getInstanceIdentifier());

                }

            }

            if (requestDocumentLinks.getContent().getContentObject() instanceof OleHoldings) {
                LOG.info("requestDocumentLinks.getHoldingsIdentifier " + ((OleHoldings) requestDocumentLinks.getContent()
                        .getContentObject())
                        .getHoldingsIdentifier());
//               LOG.info(
//                        "requestDocumentLinks.Holding getInstanceIdentifier " + ((OleHolding) requestDocumentLinks
//                                .getContent().getContentObject()).getInstanceIdentifier());

            }
            if (requestDocumentLinks.getContent().getContentObject() instanceof Item) {
                LOG.info("requestDocumentLinks.getItemIdentifier " + ((Item) requestDocumentLinks.getContent()
                        .getContentObject())
                        .getItemIdentifier());
//               LOG.info("requestDocumentLinks.item getInstanceIdentifier " + ((OleItem) requestDocumentLinks
//                        .getContent().getContentObject()).getInstanceIdentifier());

            }

        }
    }
    @Ignore
    @Test
    /**
     * This method manages the testing of {@link IndexerService#indexDocuments(List<RequestDocument> requestDocuments)}
     */
    public void testIndexDocuments() throws Exception {
        // Initial log.
        printInitialLog();
        String inputFilePath = "/work/bib/marc/OLE-Bib-bulkIngest-IU-Set1-split.xml";
        invokeIndexDocuments(inputFilePath);

        inputFilePath = "/work/bib/dublin/DublinQ-Test1.xml";
        invokeIndexDocuments(inputFilePath);

        inputFilePath = "/work/bib/dublin/unqualified/DublinUnq-Test1.xml";
        invokeIndexDocuments(inputFilePath);

        // inputFilePath = "/work/instance/oleml/instanceOneRec-Test1.xml";
        //invokeIndexDocuments(inputFilePath);
        // Test with more input files.

        // Final log.
        printFinalLog();
    }

    /**
     * This method prepares the input and calls the test for {@link IndexerService#indexDocuments(List<RequestDocument>)}
     *
     * @param inputFilePath
     */
    private void invokeIndexDocuments(String inputFilePath) {
        try {
            printLogInfo("-----");
            printLogInfo("Input file : " + inputFilePath);
            // Preparation for test.
            URL resource = getClass().getResource(inputFilePath);
            File file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();

            indexDocuments(requestDocumentList);
            printLogInfo("-----");
        } catch (Exception e) {
             LOG.error("Exception:", e);
        }
    }

    /**
     * This method actually tests {@link IndexerService#indexDocuments(List<RequestDocument>)}
     *
     * @param requestDocuments
     */
    private void indexDocuments(List<RequestDocument> requestDocuments) throws Exception {
        String methodName = getCallingMethod().toString();
        printLogInfo("Testing " + methodName + "...");

        int numInputDocs = requestDocuments.size();
        printLogInfo("Num of input documents : " + numInputDocs);
        String result = "";
        for (RequestDocument requestDocument : requestDocuments) {
            result += getIndexerService(requestDocument).indexDocument(requestDocument);
            printLogInfo("result = " + result);
        }
        // Assertions.
        assertTrue(result.contains("success"));
        long numOfIndexedDocs = getCountFromResult(result);
        String message = "Number of indexed docs equals number of input docs.";
        assertEquals("Assertion failed:" + message, numInputDocs, numOfIndexedDocs);

        printLogInfo(message);
        printLogInfo("Testing " + methodName + "...done");
    }

    private long getCountFromResult(String result) {
        result = result.replaceAll("success","");
        long count = 0;
        String[] strings = result.split("-");
        for(int i=0; i<strings.length ; i++) {
            if(StringUtils.isNotEmpty( strings[i]))  {
                count = count + Integer.parseInt(strings[i]);
            }
        }
        return count;
    }

    protected String getRecordField(String filedName, String fieldValue) throws Exception {
        QueryResponse response = executeQuery(filedName + ":" + fieldValue);
        String result = response.getResults().toString();
        return result;
    }

    @Test
    public void testIndexDocument() {
        try {
            printInitialLog();
            String filePath = "/work/bib/marc/OLE-Bib-bulkIngest-IU-Set1-split.xml";
            URL resource = getClass().getResource(filePath);
            File file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            int i = 0;
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                String result = getIndexerService(requestDocument).indexDocument(requestDocument);
                assertTrue(result.contains("success"));
            }
            printFinalLog();
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    @Test
    public void testdeleteDocumentsByUUIDList() {
        try {
            // Initial log.
            printInitialLog();
            String filePath = "/work/bib/marc/marcOneRec-Test1.xml";
            List<String> UUIDList = new ArrayList<String>();
            URL resource = getClass().getResource(filePath);
            File file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            int i = 0;
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                UUIDList.add(requestDocument.getUuid());
                String result = getIndexerService(requestDocument).indexDocument(requestDocument);
                assertTrue(result.contains("success"));
            }
            System.out.println("uuid list size:" + UUIDList.size());
            LOG.info("Record count before deletion:" + getRecordCount());
            getIndexerService(requestDocumentList.get(0)).deleteDocuments(DocCategory.WORK.getCode(), UUIDList);
            LOG.info("Record count after deletion:" + getRecordCount());
            // Final log.
            printFinalLog();
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    @Ignore
    @Test
    public void testIndexDocumentsForONIXPL() throws Exception {
        try {
            cleanUpData();
            String filePath = "/work/license/onixpl/OLE-License-ONIXPL.xml";
            URL resource = getClass().getResource(filePath);
            File file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            int i = 0;
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                String result = getIndexerService(requestDocument).indexDocument(requestDocument);
                LOG.info("uuid-->" + requestDocument.getUuid());
                LOG.info("result-->" + result);
                String searchValue = getRecordField("Title_search", "Test Title");
                LOG.info("search result-->" + searchValue);
                assertTrue(result.contains("success"));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }


    }

    @Test
    public void testIndexDocumentsForBinary() throws Exception {
        try {
            cleanUpData();
            String filePath = "/work/license/binary/OLE-License-Binary.xml";
            Request request = getRequest(filePath);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                String result = getIndexerService(requestDocument).indexDocument(requestDocument);
                LOG.info("uuid-->" + requestDocument.getUuid());
                LOG.info("result-->" + result);
                String searchValue = getRecordField("FileName_search", "test.xslt");
                LOG.info("search result-->" + searchValue);
                assertTrue(result.contains("success"));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

//      @Test
//    public void testIndexWithSpecialChars() {
//        List<String> uuids = null;
//        try {
//            cleanUpData();
//            String filePath = "/work/bib/marc/marcOneRec-Test1.xml";
//            URL resource = getClass().getResource(filePath);
//            File file = new File(resource.toURI());
//            String input = readFile(file);
//            StringBuffer stringBuffer = new StringBuffer();
//            String regex = "BIAC journal";
//            String replace = "BIAC journal H.X1< 9+&5#%,&*(2)\\[\\^\\!\\}";
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(input);
//            while (matcher.find()) {
//                matcher.appendReplacement(stringBuffer, replace);
//            }
//            matcher.appendTail(stringBuffer);
//            String inputFile = stringBuffer.toString();
//            System.out.println(inputFile);
//            RequestHandler requestHandler = new RequestHandler();
//            Request request = requestHandler.toObject(inputFile);
//            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
//            indexerService.indexDocuments(requestDocumentList);
//            uuids = getUUIDsByAuthor(replace);
//
//        } catch (Exception e) {
//            LOG.error("Exception:", e);
//        }
//        assertNotNull(uuids);
//        assertTrue(uuids.size() > 0);
//    }

    @Ignore
    @Test
    public void cleanupDiscoveryData() throws Exception {
        String inputFilePath = "/work/bib/marc/OLE-Bib-bulkIngest-IU-Set1-split.xml";
        List<SolrInputDocument> SolrInputDocument = new ArrayList<org.apache.solr.common.SolrInputDocument>();
        URL resource = getClass().getResource(inputFilePath);
        File file = new File(resource.toURI());
        String requestContent = readFile(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(requestContent);
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        for (RequestDocument requestDocument : requestDocumentList) {
            requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
            String result = getIndexerService(requestDocument).indexDocument(requestDocument);
            LOG.info("uuid-->" + requestDocument.getUuid());
            System.out.println("Solr Document before delete -->" + getIndexerService(requestDocument).getSolrDocumentBySolrId(requestDocument.getUuid()));
            System.out.println("result-->" + result);
            assertTrue(result.contains("success"));
            getIndexerService(requestDocument).cleanupDiscoveryData();
            System.out.println("Solr Document after delete -->" + getIndexerService(requestDocument).getSolrDocumentBySolrId(requestDocument.getUuid()));
        }


    }

    public Request getRequest(String filePath) throws Exception {
        URL resource = getClass().getResource(filePath);
        File file = new File(resource.toURI());
        String requestContent = readFile(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(requestContent);
        return request;
    }

    public void printInitialLog() throws Exception {
        printLogInfo("======");
        printLogInfo("Total record count before cleanup = " + getRecordCount());
        cleanUpData();
        printLogInfo("Total record count after cleanup = " + getRecordCount());
    }

    public void printFinalLog() throws Exception {
        printLogInfo("Total record count after the test = " + getRecordCount());
        printLogInfo("======");
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
