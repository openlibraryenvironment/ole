package org.kuali.ole.select.bo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
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
 * User: JuliyaMonica.S
 * Date: 11/20/12
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleLicenseRequestLocation_IT extends KFSTestCaseBase{

    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleLicenseRequestLocation oleLicenseRequestLocation = new OleLicenseRequestLocation();
        oleLicenseRequestLocation.setName("Mock Location Name");
        oleLicenseRequestLocation.setDescription("Mock Location Description");
        oleLicenseRequestLocation.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleLicenseRequestLocation savedLicenseRequestLocation = (OleLicenseRequestLocation)boService.save(oleLicenseRequestLocation);
        assertNotNull(savedLicenseRequestLocation);
        assertNotNull(savedLicenseRequestLocation.getId());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleLicenseRequestLocation oleLicenseRequestLocation = new OleLicenseRequestLocation();
        oleLicenseRequestLocation.setName("Mock Location Name");
        oleLicenseRequestLocation.setDescription("Mock Location Description");
        oleLicenseRequestLocation.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleLicenseRequestLocation savedLicenseRequestLocation = (OleLicenseRequestLocation)boService.save(oleLicenseRequestLocation);
        assertNotNull(savedLicenseRequestLocation);
        assertNotNull(savedLicenseRequestLocation.getId());
        OleLicenseRequestLocation savedLicenseLocation = boService.findBySinglePrimaryKey(OleLicenseRequestLocation.class,oleLicenseRequestLocation.getId());
        assertEquals("Mock Location Name",savedLicenseLocation.getName());
        assertEquals("Mock Location Description",savedLicenseLocation.getDescription());
    }
}
