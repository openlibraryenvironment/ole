package org.kuali.ole.docstore.document.rdbms;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/4/13
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */


public class RdbmsWorkBibDocumentManager_UT extends BaseTestCase {
    @Mock
    private BusinessObjectService businessObjectService;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }


    private BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    @Ignore
    @Test
    public void testIngest() throws Exception {

        File file = new File(getClass().getResource("/org/kuali/ole/repository/request.xml").toURI());
        String input = FileUtils.readFileToString(file);
        RequestHandler rh = new RequestHandler();
        Request request = rh.toObject(input);
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        RequestDocument requestDocument = requestDocuments.get(0);
        DocumentManager documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(
                requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        ResponseDocument responseDocument = new ResponseDocument();
        requestDocument.getAdditionalAttributes().getAttributeMap().put("dateEntered","2010-12-12 00:00:00");
        documentManager.ingest(requestDocument, getBusinessObjectService(), responseDocument);

    }


}
