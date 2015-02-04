package org.kuali.ole.ingest.controller;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/11/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class StaffUploadController_UT {

    public StaffUploadController staffUploadController;

    @Before
    public void setUp() throws Exception {
        staffUploadController = new StaffUploadController();
    }

    @Test
    public void testWhenRawMarcAndRawEDI() throws Exception {
        assertTrue(staffUploadController.validateFile("iu.mrc", "iu.edi"));
    }


    @Test
    public void testRawMarcAndEdiXML() throws Exception {
        assertFalse(staffUploadController.validateFile("iu.mrc", "sample-marc.xml"));
    }


    @Test
    public void testMarcXMLAndRawEdi() throws Exception {
        assertFalse(staffUploadController.validateFile("sample-marc.xml", "iu.edi"));
    }


    @Test
    public void testMarcXMLAndEdiXML() throws Exception {
        assertTrue(staffUploadController.validateFile("sample-marc.xml", "ybp-sample-profile.xml"));
    }


}
