package org.kuali.ole.service;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.circulation.XmlContentHandler;
import org.kuali.ole.docstore.discovery.circulation.json.CircInstance;
import org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection;
import org.kuali.ole.docstore.discovery.service.OleDocstoreDataRetrieveService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.repository.CheckoutManager;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/7/13
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDocstoreDataRetrieveService_UT {

    private static final Logger LOG = LoggerFactory.getLogger(OleDocstoreDataRetrieveService_UT.class);
    private OleDocstoreDataRetrieveService oleDocstoreDataRetrieveService;
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator.getIngestNIndexHandlerService();

    @Mock
    private org.apache.solr.client.solrj.SolrServer mockSolrServer;
    @Mock
    private QueryResponse mockQueryResponse;
    @Mock
    private CheckoutManager mockCheckoutManager;

    private SolrDocumentList mockSolrDocumentList;

    private String mockResponse;


    @Before
    public void setUp() throws Exception {
        mockResponse = FileUtils.readFileToString(new File(getClass().getResource("mock-instance-response.xml").toURI()));
        System.out.println(mockResponse);
        oleDocstoreDataRetrieveService = new OleDocstoreDataRetrieveService();
        MockitoAnnotations.initMocks(this);
        SolrDocument mockSolrDocument = new SolrDocument();
        ArrayList<String> instanceIdentifierList = new ArrayList<String>();
        instanceIdentifierList.add("instanceUUID1");
        mockSolrDocument.put("instanceIdentifier", instanceIdentifierList);

        mockSolrDocumentList = new SolrDocumentList();
        mockSolrDocumentList.add(mockSolrDocument);

        Mockito.when(mockSolrServer.query(Mockito.any(SolrQuery.class))).thenReturn(mockQueryResponse);
        Mockito.when(mockQueryResponse.getResults()).thenReturn(mockSolrDocumentList);
        Mockito.when(mockCheckoutManager.checkOut("instanceUUID1", null, "checkout")).thenReturn(mockResponse);
    }

    @Test
    public void testGetJSONForCircInstance() throws Exception {
        oleDocstoreDataRetrieveService.setCheckoutManager(mockCheckoutManager);
        oleDocstoreDataRetrieveService.setSolrServer(mockSolrServer);

        ArrayList<String> bibUUIDs = new ArrayList<String>();
        bibUUIDs.add("uuid1");
        String jsonFormat = oleDocstoreDataRetrieveService.getInstanceDetails(bibUUIDs, "json");
        assertNotNull(jsonFormat);
        System.out.println(jsonFormat);
    }

    @Test
    public void testGetXMLResponse() throws Exception {
        oleDocstoreDataRetrieveService.setCheckoutManager(mockCheckoutManager);
        oleDocstoreDataRetrieveService.setSolrServer(mockSolrServer);

        ArrayList<String> bibUUIDs = new ArrayList<String>();
        bibUUIDs.add("uuid1");
        bibUUIDs.add("uuid2");
        String xmlFormat = oleDocstoreDataRetrieveService.getInstanceDetails(bibUUIDs, "xml");
        assertNotNull(xmlFormat);
        System.out.println(xmlFormat);
    }
}
