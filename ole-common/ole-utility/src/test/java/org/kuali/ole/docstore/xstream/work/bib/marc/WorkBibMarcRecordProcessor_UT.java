package org.kuali.ole.docstore.xstream.work.bib.marc;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecords;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 12/12/11
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibMarcRecordProcessor_UT extends BaseTestCase {
    //    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WorkBibMarcRecordProcessor_UT.class);
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibMarcRecordProcessor_UT.class);


    @Test
    public void testFromToXml() throws Exception {
        WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        String resFile = "/bib/bib/marc/marc-one-record-xstream.xml";
        URL resource = getClass().getResource(resFile);
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        WorkBibMarcRecords workBibMarcRecords = workBibMarcRecordProcessor.fromXML(fileContent);
        assertNotNull(workBibMarcRecords);
        String xml = workBibMarcRecordProcessor.toXml(workBibMarcRecords);
        // TODO: Need to compare the input xml and output xml.
        LOG.info(xml);
    }

}
