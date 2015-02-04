package org.kuali.ole.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.servlet.DocumentConfigServlet;
import org.kuali.ole.docstore.discovery.web.struts.action.DiscoveryAction;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.*;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/17/12
 * Time: 1:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentConfigServlet_UT {
    private MockHttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    DocumentConfigServlet documentConfigServlet = new DocumentConfigServlet();

    /**
     * Method to setUp
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Method to tearDown
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDocumentConfig() throws Exception {
        URL resource = getClass().getResource("/DocumentConfig.xml");
        File file = new File(resource.toURI());
        System.setProperty("document.config.file", file.getPath());
        documentConfigServlet.doGet(mockRequest, mockResponse);
    }
}
