package org.kuali.ole.ingest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.ole.ingest.pojo.OlePatronGroup;
import org.kuali.ole.ingest.pojo.OlePatronPostalAddress;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.ole.service.OlePatronService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 3/27/12
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronConverterService_IT extends KFSTestCaseBase {
    private OlePatronConverterService olePatronConverterService;

    //@Autowired
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        olePatronConverterService = new OlePatronConverterService();
    }
    @Ignore
    @Test
    public void testCreatePatronRecordDocument() throws Exception {
        URL resource = getClass().getResource("samplePatronRecord.xml");
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        OlePatronIngestSummaryRecord olePatronIngestSummaryRecord =  new OlePatronIngestSummaryRecord();
        OlePatronService olePatronService = (OlePatronService) GlobalResourceLoader.getService("olePatronService");
        olePatronConverterService.setOlePatronService(olePatronService);
        List<OlePatronDocument> olePatronDocuments = olePatronConverterService.persistPatronFromFileContent(fileContent, true,"PatronIngest",olePatronIngestSummaryRecord,"4","tester");
        assertNotNull(olePatronDocuments);
        for (Iterator<OlePatronDocument> iterator = olePatronDocuments.iterator(); iterator.hasNext(); ) {
            OlePatronDocument olePatronDocument = iterator.next();
            assertNotNull(olePatronDocument.getOlePatronId());
        }
    }
    @Ignore
    @Test
    public void testUpdatePatronRecordDocument() throws Exception {
        URL resource = getClass().getResource("samplePatronRecord.xml");
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        OlePatronIngestSummaryRecord olePatronIngestSummaryRecord =  new OlePatronIngestSummaryRecord();
        OlePatronService olePatronService = (OlePatronService) GlobalResourceLoader.getService("olePatronService");
        olePatronConverterService.setOlePatronService(olePatronService);
        List<OlePatronDocument> olePatronDocuments = olePatronConverterService.persistPatronFromFileContent(fileContent, true,"PatronIngest",olePatronIngestSummaryRecord,"4","tester");
        assertNotNull(olePatronDocuments);
        assertTrue(olePatronDocuments.size() == 1);
        for (Iterator<OlePatronDocument> iterator = olePatronDocuments.iterator(); iterator.hasNext(); ) {
            OlePatronDocument olePatronDocument = iterator.next();
            assertNotNull(olePatronDocument.getOlePatronId());
            assertEquals(olePatronDocument.getName().getLastName(), "Sample");
        }

        URL resource1 = getClass().getResource("UpdateSamplePatronRecord.xml");
        File file1 = new File(resource1.toURI());
        String fileContent1 = new FileUtil().readFile(file1);

        List<OlePatronDocument> olePatronDocuments2ndTime =
                olePatronConverterService.persistPatronFromFileContent(fileContent1, true, "PatronIngest",olePatronIngestSummaryRecord,"4","tester");

        assertTrue(olePatronDocuments2ndTime.size() == 1);
        for (Iterator<OlePatronDocument> iterator = olePatronDocuments2ndTime.iterator(); iterator.hasNext(); ) {
            OlePatronDocument olePatronDocument = iterator.next();
            assertNotNull(olePatronDocument.getOlePatronId());
            assertEquals(olePatronDocument.getName().getLastName(), "SampleChanged");
        }

    }

    @Test
    public void testBuildPatronFromFileContent() throws Exception {
        URL resource = getClass().getResource("samplePatronRecord.xml");
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        OlePatronGroup olePatronGroup = olePatronConverterService.buildPatronFromFileContent(fileContent);
        assertNotNull(olePatronGroup);
        assertNotNull(olePatronGroup.getPatronGroup());
        List<OlePatron> patrons = olePatronGroup.getPatronGroup();
        assertTrue(!patrons.isEmpty());
        for (Iterator<OlePatron> iterator = patrons.iterator(); iterator.hasNext(); ) {
            OlePatron patron = iterator.next();
            assertNotNull(patron);
            assertNotNull(patron.getPatronID());
            assertTrue(!patron.getPostalAddresses().isEmpty());
            List<OlePatronPostalAddress> postalAddresses = patron.getPostalAddresses();
            if (null != postalAddresses && !postalAddresses.isEmpty()) {
                for (Iterator<OlePatronPostalAddress> stringIterator = postalAddresses.iterator(); stringIterator.hasNext(); ) {
                    OlePatronPostalAddress postalAddress = stringIterator.next();
                    assertNotNull(postalAddress);
                }
            }
        }
    }
}