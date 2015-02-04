package org.kuali.ole.web;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/17/12
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class GetUUIDServlet_UT
        extends BaseTestCase {

    private RepositoryBrowser repositoryBrowser;
    private MockHttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    private MockServletContext mockServletContext = new MockServletContext();
    GetUUIDServlet getUUIDServlet = new GetUUIDServlet();
    private static final String RESULTS_JSP =
               "/getUUIDResults.jsp";
    @Mock
    private IndexerService mockIndexerService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetUUIDs() throws Exception {
        List<String> uuidsList = new ArrayList<String>();
        String category = DocCategory.WORK.getDescription();
        String type = DocType.BIB.getDescription();
        String format = DocFormat.MARC.getDescription();
        String numUUIDs = "4";

//        //Servlet code : Having problem with servlet context
//        mockRequest.setParameter("category", category );
//        mockRequest.setParameter("type", type );
//        mockRequest.setParameter("format", format );
//        mockRequest.setParameter("numUUIDs", numUUIDs );
//        getUUIDServlet.service(mockRequest, mockResponse);
//        getUUIDServlet.doPost(mockRequest, mockResponse);
//        System.out.println(mockRequest.getAttribute("result"));
//


       try {
            if (null != category && null != format && null != numUUIDs) {
                uuidsList = getRepositoryBrowser()
                        .getUUIDs(category.toLowerCase(), type.toLowerCase(), format.toLowerCase(),
                                  new Integer(numUUIDs));
            }
        }
        catch (Exception e) {
            uuidsList.add(e.getMessage());
        }

        if(uuidsList.size() > 0 ){
            System.out.println("UUIDs");
            for(String uuid : uuidsList){
            System.out.println(uuid);
        }
        }

    }

    public RepositoryBrowser getRepositoryBrowser() {
        if (null == repositoryBrowser) {
            repositoryBrowser = new RepositoryBrowser();
        }
        return repositoryBrowser;
    }
}
