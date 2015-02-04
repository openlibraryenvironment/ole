package org.kuali.ole.docstore.xstream.ingest;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.IngestDocumentHandler;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.xstream.FileUtil;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/28/12
 * Time: 8:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class IngestDocumentHandler_UT extends BaseTestCase {
    @Test
    public void testConvertIngestXML() throws Exception {
        URL resource = getClass().getResource("/org/kuali/ole/docstore/model/xstream/ingest/single-record-ingest.xml");
        File file = new File(resource.toURI());
        String fileContnet = new FileUtil().readFile(file);

        IngestDocumentHandler ingestDocumentHandler = new IngestDocumentHandler();
        RequestDocument request = ingestDocumentHandler.toObject(fileContnet);
        assertNotNull(ingestDocumentHandler.toXML(new Request()));
        assertNotNull(request);
        assertNotNull(request.getCategory());
        assertNotNull(request.getType());
        assertNotNull(request.getFormat());
        assertNotNull(request.getContent());
    }
}
