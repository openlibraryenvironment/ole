package org.kuali.ole.utility;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.Item;
import org.kuali.ole.docstore.model.xstream.work.instance.oleml.WorkItemOlemlRecordProcessor;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: SR8455
 * Date: 12/27/12
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkItemOlemlRecordProcessor_UT {
    public static final Logger LOG = LoggerFactory.getLogger(WorkItemOlemlRecordProcessor_UT.class);

    @Test
    public void testWorkItemOlemlRecordProcessor() {
        try {
            WorkItemOlemlRecordProcessor workItemOlemlRecordProcessor = new WorkItemOlemlRecordProcessor();
            String resFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test2.xml";
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String fileContent;
            fileContent = new FileUtil().readFile(file);
            Item item = workItemOlemlRecordProcessor.fromXML(fileContent);
            if (item != null) {
                LOG.info(item.toString());
            }
            String stringXml = workItemOlemlRecordProcessor.toXML(item);
            if (stringXml != null) {
                LOG.info(stringXml);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

    }
}
