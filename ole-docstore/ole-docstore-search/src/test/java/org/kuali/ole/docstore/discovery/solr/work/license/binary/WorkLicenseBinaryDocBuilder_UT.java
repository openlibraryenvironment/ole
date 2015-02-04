package org.kuali.ole.docstore.discovery.solr.work.license.binary;

import junit.framework.TestCase;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 6/1/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLicenseBinaryDocBuilder_UT extends BaseTestCase {

    protected final Logger LOG = LoggerFactory.getLogger(WorkLicenseBinaryDocBuilder_UT.class);

    WorkLicenseBinaryDocBuilder workLicenseBinaryDocBuilder = new WorkLicenseBinaryDocBuilder();
    List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Test
    public void testIndexBinaryDocuments() throws Exception {
        try {
            String resFile = "/work/license/binary/OLE-License-Binary.xml";
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                workLicenseBinaryDocBuilder.buildSolrInputDocument(requestDocument, solrInputDocuments);
                Assert.assertNotNull(solrInputDocuments);
                LOG.info("uuid-->" + requestDocument.getUuid());
                LOG.info("type-->" + requestDocument.getAdditionalAttributes().getAttribute("type"));
            }
            String fileName = (String) solrInputDocuments.get(0).getField("FileName_search").getValue();
            LOG.info("File Name-->" + fileName);
            Assert.assertEquals("test.xslt", fileName);
            String dateuploaded = (String) solrInputDocuments.get(0).getField("DateUploaded_search").getValue();
            LOG.info("Date Uploaded-->" + dateuploaded);
            Assert.assertEquals("01/06/2012", dateuploaded);
            String owner = (String) solrInputDocuments.get(0).getField("Owner_search").getValue();
            LOG.info("Owner-->" + owner);
            Assert.assertEquals("HTC", owner);
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    @Test
    public void testBuildSolrInputDocumentByJxp() {
        String resFile = "/work/license/binary/OLE-License-Binary.xml";
        URL resource = getClass().getResource(resFile);
        File file;
        try {
            file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                solrInputDocuments.add(workLicenseBinaryDocBuilder.buildSolrInputDocumentByJxp(requestDocument.getAdditionalAttributes(),
                        requestDocument.getFormat()));
                Assert.assertNotNull(solrInputDocuments);
                LOG.info("uuid-->" + requestDocument.getUuid());
                LOG.info("type-->" + requestDocument.getAdditionalAttributes().getAttribute("type"));
            }
            System.out.println(solrInputDocuments);
            String fileName = (String) solrInputDocuments.get(0).getField("FileName_search").getValue();
            LOG.info("File Name-->" + fileName);
            Assert.assertEquals("test.xslt", fileName);
            String dateuploaded = (String) solrInputDocuments.get(0).getField("DateUploaded_search").getValue();
            LOG.info("Date Uploaded-->" + dateuploaded);
            Assert.assertEquals("01/06/2012", dateuploaded);
            String owner = (String) solrInputDocuments.get(0).getField("Owner_search").getValue();
            LOG.info("Owner-->" + owner);
            Assert.assertEquals("HTC", owner);

        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            TestCase.fail("");
        }
    }

    @Test
    public void testBuildSolrInputDocumentByJxpForPdf() {
        String resFile = "/work/license/binary/OLE-License-Binary.xml";
        URL resource = getClass().getResource(resFile);
        File file;
        try {
            file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                requestDocument.setFormat(DocFormat.PDF.getCode());
                solrInputDocuments.add(workLicenseBinaryDocBuilder.buildSolrInputDocumentByJxp(requestDocument.getAdditionalAttributes(),
                        requestDocument.getFormat()));
                Assert.assertNotNull(solrInputDocuments);
                LOG.info("uuid-->" + requestDocument.getUuid());
                LOG.info("type-->" + requestDocument.getAdditionalAttributes().getAttribute("type"));
            }
            System.out.println(solrInputDocuments);
            String fileName = (String) solrInputDocuments.get(0).getField("FileName_search").getValue();
            LOG.info("File Name-->" + fileName);
            Assert.assertEquals("test.xslt", fileName);
            String dateuploaded = (String) solrInputDocuments.get(0).getField("DateUploaded_search").getValue();
            LOG.info("Date Uploaded-->" + dateuploaded);
            Assert.assertEquals("01/06/2012", dateuploaded);
            String owner = (String) solrInputDocuments.get(0).getField("Owner_search").getValue();
            LOG.info("Owner-->" + owner);
            Assert.assertEquals("HTC", owner);

        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            TestCase.fail("");
        }
    }

    @Test
    public void testBuildSolrInputDocumentByJxpForDoc() {
        String resFile = "/work/license/binary/OLE-License-Binary.xml";
        URL resource = getClass().getResource(resFile);
        File file;
        try {
            file = new File(resource.toURI());
            String requestContent = readFile(file);
            RequestHandler requestHandler = new RequestHandler();
            Request request = requestHandler.toObject(requestContent);
            List<RequestDocument> requestDocumentList = request.getRequestDocuments();
            for (RequestDocument requestDocument : requestDocumentList) {
                requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
                requestDocument.setFormat(DocFormat.DOC.getCode());
                solrInputDocuments.add(workLicenseBinaryDocBuilder.buildSolrInputDocumentByJxp(requestDocument.getAdditionalAttributes(),
                        requestDocument.getFormat()));
                Assert.assertNotNull(solrInputDocuments);
                LOG.info("uuid-->" + requestDocument.getUuid());
                LOG.info("type-->" + requestDocument.getAdditionalAttributes().getAttribute("type"));
            }
            System.out.println(solrInputDocuments);
            String fileName = (String) solrInputDocuments.get(0).getField("FileName_search").getValue();
            LOG.info("File Name-->" + fileName);
            Assert.assertEquals("test.xslt", fileName);
            String dateuploaded = (String) solrInputDocuments.get(0).getField("DateUploaded_search").getValue();
            LOG.info("Date Uploaded-->" + dateuploaded);
            Assert.assertEquals("01/06/2012", dateuploaded);
            String owner = (String) solrInputDocuments.get(0).getField("Owner_search").getValue();
            LOG.info("Owner-->" + owner);
            Assert.assertEquals("HTC", owner);

        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            TestCase.fail("");
        }
    }
}
