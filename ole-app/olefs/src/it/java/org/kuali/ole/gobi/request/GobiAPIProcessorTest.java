package org.kuali.ole.gobi.request;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.response.Response;
import org.kuali.ole.oleng.gobi.processor.OleNGGobiApiProcessor;
import org.kuali.ole.oleng.gobi.processor.OleNGListedPrintSerialRecordProcessor;
import org.kuali.ole.oleng.gobi.processor.OleNGUnlistedPrintMonographRecordProcessor;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.net.URL;

import static junit.framework.TestCase.assertNotNull;

public class GobiAPIProcessorTest { //} extends OLETestCaseBase {


    @Test
    public void processForUnListedPrintMonograph() throws Exception {
        GobiRequest gobiRequest;

        MockitoAnnotations.initMocks(this);
        GobiRequestHandler gobiRequestHandler = new GobiRequestHandler();
        URL resource = getClass().getResource("UnlistedPrintMonograph.xml");
        assertNotNull(resource);
        File inputFile = new File(resource.toURI());
        assertNotNull(inputFile);
        String gobiRequestXML = FileUtils.readFileToString(inputFile);
        assertNotNull(gobiRequestXML);
        gobiRequest = gobiRequestHandler.unmarshal(gobiRequestXML);
        PurchaseOrder purchaseOrder = gobiRequest.getPurchaseOrder();
        assertNotNull(purchaseOrder);
        assertNotNull(gobiRequest);



        gobiRequest.setProfileIdForDefaultMapping("cp-ybp-gobi");
        OleNGGobiApiProcessor gobiAPIProcessor = new OleNGUnlistedPrintMonographRecordProcessor();
        Response gobiResponse = gobiAPIProcessor.process(gobiRequest);
        assertNotNull(gobiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.defaultPrettyPrintingWriter().writeValueAsString(gobiResponse));
    }


    @Test
    public void processForListedPrintSerial() throws Exception {
        GobiRequest gobiRequest;

        MockitoAnnotations.initMocks(this);
        GobiRequestHandler gobiRequestHandler = new GobiRequestHandler();
        URL resource = getClass().getResource("ListedPrintSerial.xml");
        assertNotNull(resource);
        File inputFile = new File(resource.toURI());
        assertNotNull(inputFile);
        String gobiRequestXML = FileUtils.readFileToString(inputFile);
        assertNotNull(gobiRequestXML);
        gobiRequest = gobiRequestHandler.unmarshal(gobiRequestXML);
        PurchaseOrder purchaseOrder = gobiRequest.getPurchaseOrder();
        assertNotNull(purchaseOrder);
        assertNotNull(gobiRequest);



        gobiRequest.setProfileIdForDefaultMapping("cp-ybp-gobi");
        OleNGGobiApiProcessor gobiAPIProcessor = new OleNGListedPrintSerialRecordProcessor();
        Response gobiResponse = gobiAPIProcessor.process(gobiRequest);
        assertNotNull(gobiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.defaultPrettyPrintingWriter().writeValueAsString(gobiResponse));
    }

}