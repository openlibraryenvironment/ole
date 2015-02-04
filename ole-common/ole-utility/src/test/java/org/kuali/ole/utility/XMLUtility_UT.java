package org.kuali.ole.utility;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.IngestDocumentHandler;
import org.kuali.ole.docstore.utility.XMLUtility;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class XMLUtility_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(XMLUtility_UT.class);

    @Test
    public void testXMLUtility() throws Exception {
        XMLUtility xmlUtility = new XMLUtility();
        URL resource = getClass().getResource("/org/kuali/ole/docstore/model/xstream/ingest/single-record-ingest.xml");
        File file = new File(resource.toURI());
        String fileContnet = new FileUtil().readFile(file);
        IngestDocumentHandler ingestDocumentHandler = new IngestDocumentHandler();
        RequestDocument request = ingestDocumentHandler.toObject(fileContnet);
        StringBuffer sb = xmlUtility.getAllContentText(request.getContent().getContent());
        LOG.info(sb.toString());
    }

}
