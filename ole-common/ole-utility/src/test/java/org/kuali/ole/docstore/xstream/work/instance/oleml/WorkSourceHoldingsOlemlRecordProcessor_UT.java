package org.kuali.ole.docstore.xstream.work.instance.oleml;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.SourceHoldings;
import org.kuali.ole.docstore.model.xstream.work.instance.oleml.WorkSourceHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 9/4/12
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkSourceHoldingsOlemlRecordProcessor_UT
        extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(WorkSourceHoldingsOlemlRecordProcessor_UT.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testFromXMLAndToXmlInstanceCollection() throws Exception {
        WorkSourceHoldingOlemlRecordProcessor srHolRecordProcessor = new WorkSourceHoldingOlemlRecordProcessor();
        URL url = getClass().getResource("/org/kuali/ole/SourceHolding-Sample.xml");
        File file = new File(url.toURI());
        SourceHoldings sourceHoldings = srHolRecordProcessor.fromXML(FileUtils.readFileToString(file));
        assertNotNull(sourceHoldings);
        String xml = srHolRecordProcessor.toXML(sourceHoldings);
        LOG.info("Generated XML : " + xml);
        LOG.info("GENERATED XML: ");
        LOG.info(xml);
        assertNotNull(xml);
    }

}
