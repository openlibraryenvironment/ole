package org.kuali.ole.gobi.request;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;

import java.io.File;
import java.net.URL;

import static junit.framework.TestCase.assertNotNull;

public class GobiRequestProcessorTest {

    @Test
    public void unmarshal() throws Exception {
        GobiRequestHandler gobiRequestHandler = new GobiRequestHandler();
        URL resource = getClass().getResource("UnlistedPrintMonograph.xml");
        assertNotNull(resource);
        File inputFile = new File(resource.toURI());
        assertNotNull(inputFile);
        String gobiRequestXML = FileUtils.readFileToString(inputFile);
        assertNotNull(gobiRequestXML);
        GobiRequest gobiRequest = gobiRequestHandler.unmarshal(gobiRequestXML);
        PurchaseOrder purchaseOrder = gobiRequest.getPurchaseOrder();
        assertNotNull(purchaseOrder);
        assertNotNull(gobiRequest);
    }
}
