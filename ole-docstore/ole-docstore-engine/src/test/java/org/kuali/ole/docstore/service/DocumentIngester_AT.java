package org.kuali.ole.docstore.service;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.discovery.util.HttpPostUtil;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 3/8/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class DocumentIngester_AT
        extends BaseTestCase {

    private IndexerService indexerService = null;
    public static final String FAILURE = "failure";
    private static final Logger LOG = LoggerFactory
            .getLogger(DocumentIngester_AT.class);
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator
            .getIngestNIndexHandlerService();

    @Before
    public void setUp() throws Exception {
        indexerService = ServiceLocator.getIndexerService();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBibBatchIngest() throws Exception {

        try {
            cleanUpData();
            URL resource = getClass().getResource("/org/kuali/ole/repository/request.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            ResponseHandler responseHandler = new ResponseHandler();
            String result = responseHandler.toXML(response);
            LOG.info("response=" + result);
            ResponseDocument responseDocument = new ResponseDocument();
            List<ResponseDocument> responseDocumentList = new ArrayList<ResponseDocument>();
            responseDocumentList = response.getDocuments();
            responseDocument = responseDocumentList.get(0);
            String bibUuid = responseDocument.getUuid();
            LOG.info("uuid of Bib record-->" + bibUuid);
            assertEquals(bibUuid, responseDocumentList.get(0).getUuid());
            String instanceUuid = responseDocumentList.get(0).getLinkedDocuments().get(0).getUuid();
            LOG.info("instance Uuid-->" + instanceUuid);
            QueryResponse queryResponse = indexerService
                    .searchBibRecord("work", "bibliographic", "marc", "instanceIdentifier", instanceUuid, "");
            String id = (String) queryResponse.getResults().get(0).getFieldValue("id");
            LOG.info("id-->" + id);
            assertEquals(bibUuid, id);
            LOG.info("queryResponse->" + queryResponse);
            QueryResponse queryResponse1 = indexerService
                    .searchBibRecord("work", "instance", "oleml", "id", instanceUuid, "");
            LOG.info("queryResponse1-->" + queryResponse1);

        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            //fail("Failed due to: " + e);
        }
        //cleanUpData();

    }

    private void cleanUpData() throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(SolrServerManager.getInstance().getSolrCoreURL());
        buffer.append("/update/");
        String params = "stream.body=<delete><query>*:*</query></delete>&commit=true";
        LOG.info("delete URL-->" + buffer.toString() + params);
        HttpPostUtil.postData(buffer.toString(), params);
    }

    protected String buildFailureMsg() {
        return FAILURE + "-ErrorID:" + getErrorID();
    }

    protected String getErrorID() {
        return String.valueOf(new Date().getTime());
    }


}
