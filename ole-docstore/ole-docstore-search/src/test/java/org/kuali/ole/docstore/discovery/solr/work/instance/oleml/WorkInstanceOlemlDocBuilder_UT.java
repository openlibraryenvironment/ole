/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.docstore.discovery.solr.work.instance.oleml;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to test WorkInstanceOlemlDocBuilder.
 *
 * @author Rajesh Chowdary K
 * @created Jun 28, 2012
 */
@Ignore
@Deprecated
public class WorkInstanceOlemlDocBuilder_UT extends BaseTestCase {

    private Instance instance = null;

    /**
     * Method to setUp
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        InstanceOlemlRecordProcessor xmlProcessor = new InstanceOlemlRecordProcessor();
        instance = xmlProcessor.fromXML(
                IOUtils.toString(getClass().getResourceAsStream("/work/instance/oleml/instance9.1.1.xml"))).getInstance().get(0);
    }

    /**
     * Method to tearDown
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        instance = null;
    }

    @Test
    public void testBuildSolrInputDocumentsForInstanceByJXPath() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();
        docBuilder.buildSolrInputDocumentsForInstanceByJXPath(instance, inputDocuments);
        Assert.assertEquals(1, inputDocuments.size());
        System.out.println("" + inputDocuments);

    }

    @Test
    public void testBuildSolrInputDocumentsForHoldingByJXPath() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();
        docBuilder.buildSolrInputDocumentsForHoldingByJXPath(instance, inputDocuments);
        Assert.assertEquals(1, inputDocuments.size());
        System.out.println("" + inputDocuments);

    }

    @Test
    public void testBuildSolrInputDocumentsForItemsByJXPath() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();
        docBuilder.buildSolrInputDocumentsForItemsByJXPath(instance, inputDocuments);
        Assert.assertEquals(1, inputDocuments.size());
        System.out.println("" + inputDocuments);

    }

    @Test
    public void testBuildSolrInputDocumentsForInstance() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();
        docBuilder.buildSolrInputDocumentsForinstance(instance, inputDocuments);
        Assert.assertEquals(1, inputDocuments.size());
        System.out.println("" + inputDocuments);

    }

    @Test
    public void testBuildSolrInputDocumentsForHolding() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();
        RequestDocument requestDocument=new RequestDocument();
        docBuilder.buildSolrInputDocumentsForHolding(instance, inputDocuments, requestDocument);
        Assert.assertEquals(1, inputDocuments.size());
        System.out.println("" + inputDocuments);

    }

    @Test
    public void testBuildSolrInputDocumentsForItems() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();
        RequestDocument requestDocument=new RequestDocument();
        docBuilder.buildSolrInputDocumentsForItems(instance, inputDocuments, requestDocument);
        Assert.assertEquals(1, inputDocuments.size());
        System.out.println("" + inputDocuments);

    }

    @Test
    public void testPerformanceFromCustomToJxPath() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> jxSolrDocs = new ArrayList<SolrInputDocument>();
        List<SolrInputDocument> custSolrDocs = new ArrayList<SolrInputDocument>();
        RequestDocument rd = new RequestDocument();
        rd.setCategory(DocCategory.WORK.getCode());
        rd.setType(DocType.INSTANCE.getCode());
        // rd.

        rd.getContent().setContentObject(instance);
        Long timeCustom = 0L;
        Long timeJxPath = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            docBuilder.buildSolrInputDocumentsForInstanceByJXPath(instance, jxSolrDocs);
            docBuilder.buildSolrInputDocumentsForHoldingByJXPath(instance, jxSolrDocs);
            docBuilder.buildSolrInputDocumentsForItemsByJXPath(instance, jxSolrDocs);
            if (i == 0)
                System.out.println("JX SOLR DOCS: \n" + jxSolrDocs);
        }
        timeJxPath = System.currentTimeMillis() - timeJxPath;

        timeCustom = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            docBuilder.buildSolrInputDocumentsForinstance(instance, custSolrDocs);
            RequestDocument requestDocument=new RequestDocument();
            docBuilder.buildSolrInputDocumentsForHolding(instance, custSolrDocs, requestDocument);
            docBuilder.buildSolrInputDocumentsForItems(instance, custSolrDocs, requestDocument);
            if (i == 0)
                System.out.println("CUSTOM SOLR DOCS: \n" + custSolrDocs);
        }
        timeCustom = System.currentTimeMillis() - timeCustom;

        System.out.println("Time taken for(10kdocs) Jxpath Builder:(ms) " + timeJxPath);
        System.out.println("Time taken for(10kdocs) Custom Builder:(ms) " + timeCustom);
        System.out.println("Time diff. for(10kdocs) (JxPath - Custom) Builder:(ms) " + (timeJxPath - timeCustom));

    }

    @Test
    public void testPerformance() {
        testPerformanceWithoutJxPath();
        testPerformanceFromJxPathExisting();
        testPerformanceFromJxPathModified();
    }

    @Test
    public void testPerformanceFromJxPathModified() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> jxSolrDocs = new ArrayList<SolrInputDocument>();
        List<SolrInputDocument> custSolrDocs = new ArrayList<SolrInputDocument>();
        RequestDocument rd = new RequestDocument();
        rd.setCategory(DocCategory.WORK.getCode());
        rd.setType(DocType.INSTANCE.getCode());
        rd.getContent().setContentObject(instance);
        Long timeJxPath = System.currentTimeMillis();
        JXPathContext instance1 = JXPathContext.newContext(instance);
        for (int i = 0; i < 1; i++) {
            docBuilder.buildSolrInputDocumentsForInstanceByJXPathTest(instance1, instance, jxSolrDocs);
            docBuilder.buildSolrInputDocumentsForHoldingByJXPathTest(instance1, instance, jxSolrDocs);
            docBuilder.buildSolrInputDocumentsForItemsByJXPathTest(instance1, instance, jxSolrDocs);
            //                        if (i == 0)
            //                            System.out.println("Modified JS SOLR DOCS: \n" + jxSolrDocs);
        }
        timeJxPath = System.currentTimeMillis() - timeJxPath;
        System.out.println("testPerformanceFromJxPathModified " + timeJxPath);
    }

    @Test
    public void testPerformanceFromJxPathExisting() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> jxSolrDocs = new ArrayList<SolrInputDocument>();
        List<SolrInputDocument> custSolrDocs = new ArrayList<SolrInputDocument>();
        RequestDocument rd = new RequestDocument();
        rd.setCategory(DocCategory.WORK.getCode());
        rd.setType(DocType.INSTANCE.getCode());
        rd.getContent().setContentObject(instance);
        Long timeJxPath = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            docBuilder.buildSolrInputDocumentsForInstanceByJXPath(instance, jxSolrDocs);
            docBuilder.buildSolrInputDocumentsForHoldingByJXPath(instance, jxSolrDocs);
            docBuilder.buildSolrInputDocumentsForItemsByJXPath(instance, jxSolrDocs);
            if (i == 0) {
                System.out.println("CUSTOM SOLR DOCS: \n" + custSolrDocs);
            }
        }
        timeJxPath = System.currentTimeMillis() - timeJxPath;
        System.out.println("testPerformanceFromJxPathExisting timeJxPath " + timeJxPath);
    }

    @Test
    public void testPerformanceWithoutJxPath() {
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        List<SolrInputDocument> jxSolrDocs = new ArrayList<SolrInputDocument>();
        List<SolrInputDocument> custSolrDocs = new ArrayList<SolrInputDocument>();
        RequestDocument rd = new RequestDocument();
        rd.setCategory(DocCategory.WORK.getCode());
        rd.setType(DocType.INSTANCE.getCode());
        rd.getContent().setContentObject(instance);
        Long timeJxPath = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            docBuilder.buildSolrInputDocumentsForinstance(instance, custSolrDocs);
            RequestDocument requestDocument=new RequestDocument();
            docBuilder.buildSolrInputDocumentsForHolding(instance, custSolrDocs, requestDocument);
            docBuilder.buildSolrInputDocumentsForItems(instance, custSolrDocs, requestDocument);
            //            if (i == 0)
            //                System.out.println("CUSTOM SOLR DOCS: \n" + custSolrDocs);
        }
        timeJxPath = System.currentTimeMillis() - timeJxPath;
        System.out.println("testPerformanceWithoutJxPath timeJxPath " + timeJxPath);
    }

    @Test
    public void testUpdateSolrDocumentsWithLinkedDocs() throws Exception {
        //IndexerService indexerService = IndexerServiceImpl.getInstance();
        WorkInstanceOlemlDocBuilder docBuilder = new WorkInstanceOlemlDocBuilder();
        String instanceRequestFile = "/work/instance/oleml/instanceOneRec-Test1.xml";
        Request request = getRequestObject(instanceRequestFile);
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        List<SolrInputDocument> solrInputDocuments = null;
        List<SolrDocument> solrDocuments = null;
        for (RequestDocument requestDocument : requestDocumentList) {
            requestDocument.setUuid("UUID" + (long) (Math.random() * 100000000));
            getIndexerService(requestDocument).indexDocument(requestDocument);
            solrDocuments = getIndexerService(requestDocument).getSolrDocumentBySolrId(requestDocument.getId());
            for (SolrDocument solrDocument : solrDocuments) {
                docBuilder.updateSolrDocument(requestDocument, solrInputDocuments, solrDocument);
            }
        }
    }

    private Request getRequestObject(String resourceLocation) throws Exception {
        URL resource = getClass().getResource(resourceLocation);
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(FileUtils.readFileToString(file));
        return request;
    }

    private IndexerService getIndexerService(RequestDocument requestDocument) {
        IndexerService indexerService = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        return indexerService;
    }

}
