package org.kuali.ole.gobi.dao;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kew.api.document.DocumentStatus;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GobiDAOTest extends OLETestCaseBase {

    protected GobiDAO gobiDAO;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void getDocStatus() throws Exception {

        gobiDAO = (GobiDAO) SpringContext.getService("gobiDAO");
        String documentStatus = gobiDAO.getDocumentStatus("1003");
        assertNotNull(documentStatus);
        assertTrue(DocumentStatus.FINAL.getCode().equalsIgnoreCase(documentStatus));
        System.out.println(documentStatus);

    }

    @Test
    public void getPOId() throws Exception {
        gobiDAO = (GobiDAO) SpringContext.getService("gobiDAO");
        String poId = gobiDAO.getPOId("1003");
        assertNotNull(poId);
        System.out.println(poId);
    }
}