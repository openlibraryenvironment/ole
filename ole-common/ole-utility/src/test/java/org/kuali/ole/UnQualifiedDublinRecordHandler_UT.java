package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.*;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.dublin.unqualified.UnQualifiedDublinRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: poornima
 * Date: 4/17/12
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnQualifiedDublinRecordHandler_UT {

    public static final Logger LOG = LoggerFactory.getLogger(UnQualifiedDublinRecordHandler_UT.class);

    @Test
    public void testExtractUnQualifiedDublinRecordsFromXML() throws Exception {
        UnQualifiedDublinRecordHandler unQualifiedDublinRecordHandler =
                new UnQualifiedDublinRecordHandler();

        URL resource = getClass().getResource("sample-dublin-unqualified.xml");
        String xmlContent = new FileUtil().readFile(new File(resource.toURI()));

        UnQualifiedDublinRecord unQualifiedDublinRecord = unQualifiedDublinRecordHandler.fromXML(xmlContent);
        assertNotNull(unQualifiedDublinRecord);
    }

    @Test
    public void testGenerateXML() throws Exception {
        UnQualifiedDublinRecordHandler unQualifiedDublinRecordHandler =
                new UnQualifiedDublinRecordHandler();
        UnQualifiedDublinRecord unQualifiedDublinRecord = generateMockUnqDublin();
        String xml = unQualifiedDublinRecordHandler.toXml(unQualifiedDublinRecord);
        LOG.info(xml);
    }

    private UnQualifiedDublinRecord generateMockUnqDublin() {
        UnQualifiedDublinRecord unQualifiedDublinRecord = new UnQualifiedDublinRecord();
        unQualifiedDublinRecord.setRequest("http://quod.lib.umich.edu/cgi/o/oai/oai");
        unQualifiedDublinRecord.setResponseDate("2010-07-25T08:14:49Z");

        ListRecords listRecords = new ListRecords();
        Header header = new Header();
        Tag tag = new Tag("dc:title");
        Record record = new Record();
        MetaData metaData = new MetaData();
        OaiDcDoc oaiDcDoc = new OaiDcDoc();
        List<OaiDcDoc> oaiDcDocList = new ArrayList<OaiDcDoc>();
        header.put("dc:title", "U.S. Marines in Vietnam");
        if (header.get("dc:title") != null) {
            LOG.info(header.get("dc:title").toString());
        }
        oaiDcDoc.put("dc:title", "U.S. Marines in Vietnam");
        oaiDcDoc.put("dc:creator", "Shulimson, Jack.");
        oaiDcDoc.put("dc:subject", "Vietnam War, 1961-1975--Campaigns");
        oaiDcDoc.put("dc:description", "xiii, 261 p. :");
        oaiDcDoc.put("dc:identifier", "(LCCN)78600120");
        if (oaiDcDoc.get("dc:title") != null) {
            LOG.info("dc:title");
        }
        if (oaiDcDoc.get("dc:creator") != null) {
            LOG.info("dc:creator");
        }
        oaiDcDocList.add(oaiDcDoc);
        metaData.setOaiDcDocs(oaiDcDocList);
        metaData.addOaiDcDoc(oaiDcDoc);
        if (metaData.getOaiDcDocs() != null) {
            LOG.info(metaData.getOaiDcDocs().toString());
        }
        record.setHeader(header);
        record.setMetadata(metaData);
        if (record.getMetadata() != null) {
            LOG.info(record.getMetadata().toString());
        }
        listRecords.addRecord(record);

        unQualifiedDublinRecord.setListRecords(listRecords);

        listRecords = unQualifiedDublinRecord.getListRecords();
        List<Record> recordList = listRecords.getRecords();
        listRecords.setRecords(recordList);
        for (Record record1 : recordList) {
            LOG.info("" + record1.getHeader());
        }
        LOG.info(unQualifiedDublinRecord.toString());
        LOG.info("Response Date:" + unQualifiedDublinRecord.getResponseDate());
        LOG.info("Request:" + unQualifiedDublinRecord.getRequest());

        tag.setName("dc:title");
        tag.setValue("U.S. Marines in Vietnam");
        tag.equals(oaiDcDoc);

        WorkBibDublinUnQualifiedRecord workBibDublinUnQualifiedRecord = new WorkBibDublinUnQualifiedRecord();
        workBibDublinUnQualifiedRecord.setListRecords(listRecords);
        if (workBibDublinUnQualifiedRecord.getListRecords() != null) {
            LOG.info(workBibDublinUnQualifiedRecord.getListRecords().toString());
        }
        workBibDublinUnQualifiedRecord.setResponseDate("12-12-2012");
        if (workBibDublinUnQualifiedRecord.getResponseDate() != null) {
            LOG.info(workBibDublinUnQualifiedRecord.getResponseDate());
        }
        workBibDublinUnQualifiedRecord.setRequest("request");
        if (workBibDublinUnQualifiedRecord.getRequest() != null) {
            LOG.info(workBibDublinUnQualifiedRecord.getRequest());
        }
        return unQualifiedDublinRecord;
    }
}
