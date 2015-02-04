package org.kuali.ole.describe.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.ingest.ISBNUtil;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/7/12
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveryHelperService_UT {
    @Mock
    private SolrRequestReponseHandler mockSolrRequestResponseHandler;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetResponseFromSOLR() throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("author_display", "mock_author");
        map.put("title_display", "mock_title");
        map.put("uuid", "mock_uuid");
        Mockito.when(mockSolrRequestResponseHandler.retriveResults("020a:9091405183689")).thenReturn(Arrays.asList(map));
        
        DiscoveryHelperService discoveryHelperService = new DiscoveryHelperService();
        discoveryHelperService.setSolrRequestReponseHandler(mockSolrRequestResponseHandler);
        ISBNUtil isbnUtil = new ISBNUtil();
        String normalizedISBN = isbnUtil.normalizeISBN("9091405183689");
        List responseFromSOLR = discoveryHelperService.getResponseFromSOLR("020a", normalizedISBN);
        assertNotNull(responseFromSOLR);
    }


    /*@Test
    //Test to go against a dev or a test server depending on -Dapp.environment value
    public void testGetResponseFromSOLR_AT() throws Exception {
        System.setProperty("app.environment", "dev");
        DiscoveryHelperService discoveryHelperService = new DiscoveryHelperService();
        ISBNUtil isbnUtil = new ISBNUtil();
        String normalizedISBN = isbnUtil.normalizeISBN("0152038655");
        List responseFromSOLR = discoveryHelperService.getResponseFromSOLR("020a", normalizedISBN);
        assertNotNull(responseFromSOLR);
    }*/

    /*
    @Test
    //Test to go against a dev or a test server depending on -Dapp.environment value
    public void testGetBibInfoFromSolr_AT() throws Exception {
        System.setProperty("app.environment", "dev");
        DiscoveryHelperService discoveryHelperService = new DiscoveryHelperService();
        List responseFromSOLR = discoveryHelperService.getBibInformationFromInsatnceId("14080f4a-d05b-473b-8806-03a5bfe72ef0");
        assertNotNull(responseFromSOLR);
    }*/
}
