package org.kuali.ole;

import org.junit.Test;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;

/**
 * User: peris
 * Date: 2/7/13
 * Time: 2:57 PM
 */
public class OCLCResponseHandler_UT extends BaseTestCase {

    @Test
    public void handleOCLCReponse() throws Exception {
        OCLCResponseHandler oclcResponseHandler = new OCLCResponseHandler();
        assertNotNull(oclcResponseHandler);

        URL resource = getClass().getResource("oclcresponse.txt");
        String oclcResponse = readFile(new File(resource.toURI()));
        System.out.println(oclcResponse);
        String marcxmlFromOCLCResponse = oclcResponseHandler.getMARCXMLFromOCLCResponse(oclcResponse);
        assertNotNull(marcxmlFromOCLCResponse);
        System.out.println(marcxmlFromOCLCResponse);
    }
}
