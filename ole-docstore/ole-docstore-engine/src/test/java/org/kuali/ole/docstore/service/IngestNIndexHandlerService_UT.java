/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.docstore.service;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.SourceHoldings;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.process.BulkIngestTimeManager;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.repository.CheckoutManager;
import org.kuali.ole.repository.NodeHandler;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Class to test IngestNIndexHandlerService.
 *
 * @author Rajesh Chowdary K
 * @created Feb 20, 2012
 */

public class IngestNIndexHandlerService_UT
        extends BaseTestCase {

    private IngestNIndexHandlerService ingestNIndexHandlerService;
    private static final Logger LOG = LoggerFactory.getLogger(IngestNIndexHandlerService_UT.class);

    public IngestNIndexHandlerService_UT() {
        ingestNIndexHandlerService = new IngestNIndexHandlerService();
        ingestNIndexHandlerService.setRequestHandler(new RequestHandler());
        ingestNIndexHandlerService.setDocumentIndexer(new DocumentIndexer());
        ingestNIndexHandlerService.setDocumentIngester(new DocumentIngester());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kuali.ole.BaseTestCase#setUp()
     */

    @Mock
    private RepositoryManager mockRepositoryManager;
    @Mock
    private Session mockSession;
    @Mock
    private Node mockRootNode;
    @Mock
    private NodeIterator mockNodeIterator;
    @Mock
    private Node mockChildNode;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        System.getProperties().put("app.environment", "local");
        Mockito.when(mockRepositoryManager.getSession(Mockito.anyString(), Mockito.anyString())).thenReturn(mockSession);
        Mockito.when(mockSession.getRootNode()).thenReturn(mockRootNode);
        Mockito.when(mockRootNode.hasProperty("nodeType")).thenReturn(true);
        Mockito.when(mockRootNode.getNodes()).thenReturn(mockNodeIterator);
        Mockito.when(mockNodeIterator.hasNext()).thenReturn(true);
        Mockito.when(mockNodeIterator.next()).thenReturn(mockChildNode);
        Mockito.when(mockChildNode.getName()).thenReturn("MockFile");
    }

    /**
     * Method to tearDown
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.service.IngestNIndexHandlerService#ingestNIndexRequestDocuments(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testDocumentIndexer() throws Exception {
        DocumentIndexer documentIndexer = new DocumentIndexer();
        documentIndexer.optimizeSolr();
        documentIndexer.optimizeSolr(true, false);
        URL resource = getClass().getResource("/org/kuali/ole/repository/request.xml");
        File file = new File(resource.toURI());
        String fileContent = FileUtils.readFileToString(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(fileContent);
        ingestNIndexHandlerService.setRepositoryManager(mockRepositoryManager);
        Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        RequestDocument requestDocument = request.getRequestDocuments().get(0);
        documentIndexer.indexDocument(requestDocument);
        List list = new ArrayList();
        list.add(requestDocument);
        documentIndexer.indexDocuments(list);
        documentIndexer.indexDocumentsForBulk(list, true);
        documentIndexer.rollbackIndexedData(list);
    }
    @Ignore
    @Test
    public final void testMarcDocumentIngest() {
        try {
            URL resource = getClass().getResource("/org/kuali/ole/repository/request.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            LOG.info("xmlResponse=" + xmlResponse);
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                LOG.info("Doc UUID: " + requestDocument.getUuid());
                if (requestDocument.getUuid() == null) {
                    fail("Instance Document Not Ingested.");
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage() , e );
            fail("Failed due to: " + e);
        }

    }

    @Ignore
    @Test
    public final void testInstanceDocumentIngest() {
        try {
            URL resource = getClass().getResource("/org/kuali/ole/repository/requestInstance.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            LOG.info("xmlResponse=" + xmlResponse);
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                LOG.info("Doc UUID: " + requestDocument.getUuid());
                if (requestDocument.getUuid() == null) {
                    fail("Instance Document Not Ingested.");
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed due to: " + e);
        }
    }
    @Ignore
    @Test
    public final void testInstanceSourceHoldingDocumentIngest() {
        try {
            URL resource = getClass().getResource("/org/kuali/ole/repository/requestInstanceSourceHolding.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            LOG.info("xmlResponse=" + xmlResponse);
            RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
            //               System.out.println("Repo Dump from JCR \n"+repositoryBrowser.getRepositoryDump());
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                LOG.info("Doc UUID: " + requestDocument.getUuid());

                InstanceCollection instanceCollection = (InstanceCollection) requestDocument.getLinkedRequestDocuments()
                        .get(0).getContent()
                        .getContentObject();

                SourceHoldings sourceHoldings = instanceCollection.getInstance().get(0).getSourceHoldings();
                String sourceHoldingId = sourceHoldings.getHoldingsIdentifier();
                LOG.info(sourceHoldingId);
                CheckoutManager checkoutManager = new CheckoutManager();
                String sourceHoldingContent = checkoutManager.checkOut(sourceHoldingId, "mockUser", "checkout");
                System.out.println("sourceHoldingContent \n" + sourceHoldingContent);
                if (requestDocument.getUuid() == null) {
                    fail("Instance Document Not Ingested.");
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed due to: " + e);
        }

    }

    @Ignore
    @Test
    public void testDublinCoreDocumentIngest() throws Exception {

        URL resource = getClass().getResource("/org/kuali/ole/repository/Bib-Bib-DublinQ-Request.xml");
        File file = new File(resource.toURI());
        String fileContent = FileUtils.readFileToString(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(fileContent);
        Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        LOG.info("xmlResponse=" + xmlResponse);
        for (RequestDocument requestDocument : request.getRequestDocuments()) {
            LOG.info("Doc UUID: " + requestDocument.getUuid());
            if (requestDocument.getUuid() == null) {
                fail("Instance Document Not Ingested.");
            }
        }
    }
    @Ignore
    @Test
    public void testDublinUnqualifiedDocumentIngest() throws Exception {

        URL resource = getClass().getResource("/org/kuali/ole/repository/Bib-Bib-Dublin-Unqualified-Request.xml");
        File file = new File(resource.toURI());
        String fileContent = FileUtils.readFileToString(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(fileContent);
        Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        LOG.info("xmlResponse=" + xmlResponse);
        for (RequestDocument requestDocument : request.getRequestDocuments()) {
            LOG.info("Doc UUID: " + requestDocument.getUuid());
            if (requestDocument.getUuid() == null) {
                fail("Instance Document Not Ingested.");
            }
        }


    }

    /**
     * Test method for {@link org.kuali.ole.docstore.service.IngestNIndexHandlerService#ingestNIndexRequestDocuments(java.lang.String)}.
     */
    @Ignore
    @Test
    public final void testMarcIngestForBulk() {
        try {
            Session session = null;
            URL resource = getClass().getResource("/org/kuali/ole/repository/request.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            request.getRequestDocuments().get(0).getLinkedRequestDocuments().clear();
            request.getRequestDocuments().get(1).getLinkedRequestDocuments().clear();
            if (session == null) {
                session = RepositoryManager.getRepositoryManager()
                        .getSession(request.getUser(), request.getOperation());
            }
            List<String> ingestedIds = ingestNIndexHandlerService.bulkIngestNIndex(request, session);
            LOG.info("Ingested Ids : " + ingestedIds);
            if (ingestedIds == null || ingestedIds.size() == 0) {
                fail("Ingest And Index Failed, Because No Documents Ingested.");
            }
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                LOG.info("Doc UUID: " + requestDocument.getUuid());
                if (requestDocument.getUuid() == null) {
                    fail("Instance Document Not Ingested.");
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed due to: " + e);
        }
    }
    @Ignore
    @Test
    public final void testPatronDocumentIngest() {
        try {
            URL resource = getClass().getResource("/org/kuali/ole/repository/request-patron.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            String reqContent = null;
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                reqContent = requestDocument.getContent().getContent();
            }
            Response resp = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            checkIndexForPatronRecord(resp);
            checkIngestForPatronRecord(resp, reqContent);
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed due to: " + e);
        }
    }

    private void checkIngestForPatronRecord(Response resp, String reqContent) throws OleException, RepositoryException {
        for (ResponseDocument responseDocument : resp.getDocuments()) {
            String uuid = responseDocument.getUuid();
            Session session = null;
            session = RepositoryManager.getRepositoryManager().getSession("chuntley", "bulkIngest");
            Node patronFileNode = new NodeHandler().getNodeByUUID(session, uuid);
            Node patronFormatNode = patronFileNode.getParent();
            String formatName = patronFormatNode.getName();
            assertEquals("oleml", formatName);
            Node patronTypeNode = patronFormatNode.getParent();
            String typeName = patronTypeNode.getName();
            assertEquals("patron", typeName);
            Node patronCatNode = patronTypeNode.getParent();
            String catName = patronCatNode.getName();
            assertEquals("security", catName);
            Node contentNode = patronFileNode.getNode("jcr:content");
            String content = contentNode.getProperty("jcr:data").getValue().getString();
            assertEquals(content, reqContent);
            RepositoryManager.getRepositoryManager().logout(session);
        }

    }

    private void checkIndexForPatronRecord(Response resp) {
        for (ResponseDocument responseDocument : resp.getDocuments()) {
            String uuid = responseDocument.getUuid();
            QueryResponse queryResponse = ServiceLocator.getIndexerService()
                    .searchBibRecord(responseDocument.getCategory(),
                            responseDocument.getType(),
                            responseDocument.getFormat(), "id", uuid,
                            "RecordNumber_search");
            if (System.getProperty("OLE_DOCSTORE_USE_DISCOVERY").equalsIgnoreCase(Boolean.TRUE.toString())) {
                long numFound = queryResponse.getResults().getNumFound();
                assertEquals(1, numFound);
                List solrInstIdList = new ArrayList();
                solrInstIdList = (List) queryResponse.getResults().get(0).getFieldValue("RecordNumber_search");
                assertNotNull(solrInstIdList.get(0));
                Assert.assertEquals(solrInstIdList.get(0), "00100055U");
            }
        }
    }
    @Ignore
    @Test
    public final void testMarcIngestWithSpecialChars() {
        List<String> bibIds = new ArrayList<String>();
        List<String> instanceIds = new ArrayList<String>();
        try {

            URL resource = getClass().getResource("/org/kuali/ole/repository/request.xml");
            File file = new File(resource.toURI());
            String input = FileUtils.readFileToString(file);
            StringBuffer stringBuffer = new StringBuffer();
            String regex = "Sandburg, Carl";
            String replace = "San~X1< 9+&!5#%^,&*(2)>{6}[8]!?H";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                matcher.appendReplacement(stringBuffer, replace);
            }
            matcher.appendTail(stringBuffer);
            String inputFile = stringBuffer.toString();
            RequestHandler rh = new RequestHandler();
            Request request = rh.toObject(inputFile);
            Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            for (ResponseDocument resDoc : response.getDocuments()) {
                bibIds.add(resDoc.getUuid());
                for (ResponseDocument linkedDoc : resDoc.getLinkedDocuments()) {
                    instanceIds.add(linkedDoc.getUuid());
                }
            }
            LOG.info("Bib Ids:" + bibIds);
            LOG.info("Instance ids:" + instanceIds);
            CheckoutManager checkoutManager = new CheckoutManager();
            for (String bibId : bibIds) {
                String checkedOutContent = checkoutManager.checkOut(bibId, "mockUser", "checkout");
                LOG.info("Bib Id:" + bibId);
                LOG.info("checkedOutContent for Bibliographic " + checkedOutContent);
            }
            for (String instanceId : instanceIds) {
                String checkedOutContent = checkoutManager.checkOut(instanceId, "mockUser", "checkout");
                LOG.info("Instance Id:" + instanceId);
                LOG.info("checkedOutContent fro Instance " + checkedOutContent);
            }

        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
        }
    }
    @Ignore
    @Test
    public final void testLicenseOnixPLDocumentIngest() {
        try {
            URL resource = getClass().getResource("/org/kuali/ole/repository/request-license-onixpl.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            String reqContent = null;
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                reqContent = requestDocument.getContent().getContent();
            }
            Response resp = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            LOG.info("License Onix PL response Ststus  " + resp.getStatus());
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                LOG.info("License Onix uuid: " + requestDocument.getUuid());
                if (requestDocument.getUuid() == null) {
                    fail("Instance Document Not Ingested.");
                }
                CheckoutManager checkoutManager = new CheckoutManager();
                String checkedOutContent = checkoutManager.checkOut(requestDocument.getUuid(), "mockUser", "checkout");
                Assert.assertEquals(reqContent, checkedOutContent);
            }
            //            RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
            //           LOG.info(repositoryBrowser.getRepositoryDump());
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed due to: " + e);
        }
    }
    @Ignore
    @Test
    public final void testLicensePDFDocumentIngest() {
        try {
            URL resource = getClass().getResource("/org/kuali/ole/repository/request-license-pdf.xml");
            File file = new File(resource.toURI());
            String fileContent = FileUtils.readFileToString(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(fileContent);
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                String reqContent = requestDocument.getContent().getContent();
                LOG.info("reqContent" + reqContent);
            }
            Response resp = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            LOG.info("License PDF response Ststus " + resp);
            for (RequestDocument requestDocument : request.getRequestDocuments()) {
                LOG.info("License PDF  UUID: " + requestDocument.getUuid());
                if (requestDocument.getUuid() == null) {
                    fail("Instance Document Not Ingested.");
                }
                CheckoutManager checkoutManager = new CheckoutManager();
                String checkedOutContent = checkoutManager.checkOut(requestDocument.getUuid(), "mockUser", "checkout");
                Assert.assertNotNull(checkedOutContent);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed due to: " + e);
        }
    }
    @Ignore
    @Test
    public void testBulkIngestTimeManager() {
        BulkIngestTimeManager bulkIngestTimeManager = new BulkIngestTimeManager();
        bulkIngestTimeManager.reset();
        bulkIngestTimeManager.toString();
    }
}
