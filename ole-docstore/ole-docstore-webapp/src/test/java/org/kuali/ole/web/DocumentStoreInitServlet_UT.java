package org.kuali.ole.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/16/12
 * Time: 11:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class DocumentStoreInitServlet_UT  extends BaseTestCase {
    private MockHttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    DocumentStoreInitServlet documentStoreInitServlet = new DocumentStoreInitServlet();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDocumentStoreInitServlet() throws Exception {
        documentStoreInitServlet.init(documentStoreInitServlet.getServletConfig());
    }
}
