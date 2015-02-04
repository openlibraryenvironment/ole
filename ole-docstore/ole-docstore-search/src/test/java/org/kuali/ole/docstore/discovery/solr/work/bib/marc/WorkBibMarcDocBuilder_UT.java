package org.kuali.ole.docstore.discovery.solr.work.bib.marc;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecords;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JUnit Test Case to test WorkBibMarcDocBuilder.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibMarcDocBuilder_UT extends junit.framework.TestCase {

    private static final Logger LOG = LoggerFactory.getLogger(WorkBibMarcDocBuilder_UT.class);

    private WorkBibMarcRecord record = null;
    WorkBibMarcDocBuilder workBibMarcDocBuilder = new WorkBibMarcDocBuilder();
    List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
    StopWatch buildSolrInputDocTimer = new StopWatch();
    StopWatch xmlToPojoTimer = new StopWatch();


    @Override
    public void setUp() {
        WorkBibMarcRecords recs = null;
        try {
            recs = new WorkBibMarcRecordProcessor().fromXML(IOUtils.toString(getClass().getResourceAsStream("/bib/bib/marc/OneMarcRecord.xml")));
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
        record = recs.getRecords().get(0);

    }

    @Override
    public void tearDown() {
        record = null;
    }

    @Test
    public void testBuildSolrInputDocument() {
        try {
            SolrInputDocument solrInputDocument = new WorkBibMarcDocBuilder().buildSolrInputDocument(record);
            LOG.info("Output: " + solrInputDocument);
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testBuildSolrInputDocuments() {
        try {
            List<WorkBibMarcRecord> records = new ArrayList<WorkBibMarcRecord>();
            records.add(record);
            List<SolrInputDocument> solrInputDocuments = new WorkBibMarcDocBuilder().buildSolrInputDocuments(records);
            LOG.info("Output: " + solrInputDocuments);
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testWholeXMLtoSolrConverter() {
        WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        String resFile = "/bib/bib/marc/marc_test.xml";

        try {
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String docContent = FileUtils.readFileToString(file);
            WorkBibMarcRecords records = workBibMarcRecordProcessor.fromXML(docContent);
            List<SolrInputDocument> solrInputDocuments = new WorkBibMarcDocBuilder().buildSolrInputDocuments(records.getRecords());
            Assert.assertEquals(records.getRecords().size(), solrInputDocuments.size());
            LOG.info("Output: " + solrInputDocuments);
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            fail("Failed due to: " + e);
        }

    }

    @Test
    public void testSecondIndicatorForMarc() throws Exception {
        String resFile = "/bib/bib/marc/request-Marc2ndIndicatorTest.xml";
        URL resource = getClass().getResource(resFile);
        File file = new File(resource.toURI());
        String requestContent = FileUtils.readFileToString(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(requestContent);
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        buildSolrInputDocTimer.start();
        buildSolrInputDocTimer.suspend();
        xmlToPojoTimer.start();
        xmlToPojoTimer.suspend();
        for (RequestDocument requestDocument : requestDocumentList) {
            requestDocument.setUuid("1");
            workBibMarcDocBuilder.buildSolrInputDocument(requestDocument, solrInputDocuments, buildSolrInputDocTimer, xmlToPojoTimer);
            assertNotNull(solrInputDocuments);
        }

        Object titleSort = solrInputDocuments.get(0).getField("Title_sort").getValue();
        LOG.info("Title after applying 2nd indicator-->" + titleSort.toString());
        Assert.assertEquals(titleSort, "April Shower");
        Object authorSort = solrInputDocuments.get(0).getField("Author_sort").getValue();
        LOG.info("Author after applying 2nd indicator-->" + authorSort.toString());
        Assert.assertEquals(authorSort, "Philip");


    }

    @Test
    public void testSearchFieldsXMLtoSolrConverter() {
        WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        String resFile = "/bib/bib/marc/marc_subject_test.xml";
        try {
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String docContent = FileUtils.readFileToString(file);
            WorkBibMarcRecords records = workBibMarcRecordProcessor.fromXML(docContent);
            List<SolrInputDocument> solrInputDocuments = new WorkBibMarcDocBuilder().buildSolrInputDocuments(records.getRecords());
            Assert.assertEquals(records.getRecords().size(), solrInputDocuments.size());
            LOG.info("Output: " + solrInputDocuments);
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            fail("Failed due to: " + e);
        }

    }
}