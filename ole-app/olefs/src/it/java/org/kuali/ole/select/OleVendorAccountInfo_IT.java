package org.kuali.ole.select;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.select.bo.OleVendorAccountInfo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/23/12
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */


@Ignore
public class OleVendorAccountInfo_IT extends SpringBaseTestCase {

            private BusinessObjectService boService ;
            @Ignore
            @Test
            @Transactional
            public void testSave() {
                OleVendorAccountInfo oleVendorAccountInfo = new OleVendorAccountInfo();
                oleVendorAccountInfo.setVendorRefNumber("Mock Vendor Identifier");
                oleVendorAccountInfo.setAccountNumber("Mock Account Number");
                oleVendorAccountInfo.setObjectCode("Mock Object Code");
                oleVendorAccountInfo.setActive(true);
                boService = KRADServiceLocator.getBusinessObjectService();
                OleVendorAccountInfo savedOleVendorAccountInfo = boService.save(oleVendorAccountInfo);
                assertNotNull(savedOleVendorAccountInfo);
                assertNotNull(savedOleVendorAccountInfo.getVendorAccountInfoId());
            }

            @Ignore
            @Test
            @Transactional
            public void testSearch() {
                OleVendorAccountInfo oleVendorAccountInfo = new OleVendorAccountInfo();
                oleVendorAccountInfo.setVendorRefNumber("Mock Vendor Identifier");
                oleVendorAccountInfo.setAccountNumber("Mock Account Number");
                oleVendorAccountInfo.setObjectCode("Mock Object Code");
                oleVendorAccountInfo.setActive(true);
                boService = KRADServiceLocator.getBusinessObjectService();
                OleVendorAccountInfo savedOleVendorAccountInfo = boService.save(oleVendorAccountInfo);
                assertNotNull(savedOleVendorAccountInfo);
                OleVendorAccountInfo vendorAccountInfo = boService.findBySinglePrimaryKey(OleVendorAccountInfo.class,savedOleVendorAccountInfo.getVendorAccountInfoId());
                assertEquals("Mock Vendor Identifier", vendorAccountInfo.getVendorRefNumber());
                assertEquals("Mock Account Number",vendorAccountInfo.getAccountNumber());
                assertEquals("Mock Object Code", vendorAccountInfo.getObjectCode());
                assertEquals(true, vendorAccountInfo.isActive());
            }


}

