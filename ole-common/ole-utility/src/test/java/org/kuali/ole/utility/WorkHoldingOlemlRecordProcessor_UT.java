package org.kuali.ole.utility;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.Item;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.OleHoldings;
import org.kuali.ole.docstore.model.xstream.work.instance.oleml.WorkHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: SR8455
 * Date: 12/27/12
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkHoldingOlemlRecordProcessor_UT {
    public static final Logger LOG = LoggerFactory.getLogger(WorkItemOlemlRecordProcessor_UT.class);

    @Test
    public void testWorkItemOlemlRecordProcessor() {
        try {
            WorkHoldingOlemlRecordProcessor workHoldingOlemlRecordProcessor = new WorkHoldingOlemlRecordProcessor();
            String resFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test2.xml";
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String fileContent;
            fileContent = new FileUtil().readFile(file);
            OleHoldings oleHoldings = workHoldingOlemlRecordProcessor.fromXML(fileContent);
            if (oleHoldings != null) {
                LOG.info(oleHoldings.toString());
            }
            String stringXml = workHoldingOlemlRecordProcessor.toXML(oleHoldings);
            if (stringXml != null) {
                LOG.info(stringXml);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

    }
}
