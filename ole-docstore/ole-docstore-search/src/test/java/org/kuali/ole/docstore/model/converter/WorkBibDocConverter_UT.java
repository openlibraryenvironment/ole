package org.kuali.ole.docstore.model.converter;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecords;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 28/11/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibDocConverter_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibDocConverter_UT.class);

    @Test
    public void testConvert() {
        String resFile = "/bib/bib/marc/OneMarcRecord.xml";
        WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        WorkBibDocConverter workBibDocConverter = new WorkBibDocConverter();
        try {
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String docContent = FileUtils.readFileToString(file);
            WorkBibMarcRecords records = workBibMarcRecordProcessor.fromXML(docContent);
            for (WorkBibMarcRecord marcRecord : records.getRecords()) {
                WorkBibDocument bibDoc = workBibDocConverter.convert(marcRecord);
                System.out.println("Author is..." + bibDoc.getAuthor());
                System.out.println("Title is..." + bibDoc.getTitle());
                System.out.println("Publication Date is..." + bibDoc.getPublicationDate());
                System.out.println("Publisher is..." + bibDoc.getPublisher());
                System.out.println("Publisher is..." + bibDoc.getEdition());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            fail("Failed due to: " + e);
        }
    }

    @Test
    public void testConvertRecords() {
        String resFile = "/bib/bib/marc/OneMarcRecord.xml";
        WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        WorkBibDocConverter workBibDocConverter = new WorkBibDocConverter();
        try {
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String docContent = FileUtils.readFileToString(file);
            WorkBibMarcRecords records = workBibMarcRecordProcessor.fromXML(docContent);
            List workBibDocumentList = new ArrayList<WorkBibDocument>();
            workBibDocConverter.convert(records);
            LOG.info("WorkBibDocumentRecords :" + workBibDocumentList);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            fail("Failed due to: " + e);
        }
    }


}



