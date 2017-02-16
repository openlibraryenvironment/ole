package org.kuali.ole.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.service.MultiPartBagRequestClient;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/17/12
 * Time: 12:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class MultiPartBagRequestClientServlet_UT extends BaseTestCase {
    private MockHttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    MultiPartBagRequestClientServlet bagRequestClientServlet = new MultiPartBagRequestClientServlet();

    @Override
          @Before
          public void setUp() throws Exception {
              super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
          }

          @After
          public void tearDown() throws Exception {
          }
    @Ignore
    @Test
    public void testMultiPartBagItIngest() throws Exception {
        File inputDir = new File(this.getClass().getResource("license/ingest").toURI());
        mockRequest.setParameter("restUrl","http://dev.docstore.oleproject.org/rest/documents" );
        mockRequest.setParameter("requestFolderPath", inputDir.getPath());
        bagRequestClientServlet.doGet(mockRequest, mockResponse);

        /*MultiPartBagRequestClient multiPartBagRequestClient = new MultiPartBagRequestClient();
        List<Response> responseList = multiPartBagRequestClient.runMultiPartRequestsAtLocation(inputDir.getPath(), null);
        Response response = responseList.get(0);
        String responseString = new org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler().toXML(response);
        System.out.println("bagIt Ingest Response" + responseString);
*/
    }
    @Ignore
    @Test
    public void testMultiPartBagItCheckout() throws Exception {
        File inputDir = new File(this.getClass().getResource("license/checkout").toURI());
        MultiPartBagRequestClient multiPartBagRequestClient = new MultiPartBagRequestClient();
        List<Response> responseList = multiPartBagRequestClient.runMultiPartRequestsAtLocation(inputDir.getPath(), null);
        Response response = responseList.get(0);
        String responseString = new org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler().toXML(response);
        System.out.println("bagIt Checkout Response" + responseString);

    }
    @Ignore
    @Test
    public void testMultiPartBagItCheckin() throws Exception {
        File inputDir = new File(this.getClass().getResource("license/checkin").toURI());
        MultiPartBagRequestClient multiPartBagRequestClient = new MultiPartBagRequestClient();
        List<Response> responseList = multiPartBagRequestClient.runMultiPartRequestsAtLocation(inputDir.getPath(), null);
        Response response = responseList.get(0);
        String responseString = new org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler().toXML(response);
        System.out.println("bagIt Checkin Response" + responseString);

    }
    @Ignore
    @Test
    public void testMultiPartBagItDelete() throws Exception {
        File inputDir = new File(this.getClass().getResource("license/delete").toURI());
        MultiPartBagRequestClient multiPartBagRequestClient = new MultiPartBagRequestClient();
        List<Response> responseList = multiPartBagRequestClient.runMultiPartRequestsAtLocation(inputDir.getPath(), null);
        Response response = responseList.get(0);
        String responseString = new org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler().toXML(response);
        System.out.println("bagIt Delete Response" + responseString);

    }
}
