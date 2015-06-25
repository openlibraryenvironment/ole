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

public class OleLicenseRequestType_IT extends KFSTestCaseBase{
    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleLicenseRequestType oleLicenseRequestType = new OleLicenseRequestType();
        oleLicenseRequestType.setName("Mock Type Name");
        oleLicenseRequestType.setDescription("Mock Type Description");
        oleLicenseRequestType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleLicenseRequestType savedLicenseRequestType = (OleLicenseRequestType)boService.save(oleLicenseRequestType);
        assertNotNull(savedLicenseRequestType);
        assertNotNull(savedLicenseRequestType.getId());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleLicenseRequestType oleLicenseRequestType = new OleLicenseRequestType();
        oleLicenseRequestType.setName("Mock Type Name");
        oleLicenseRequestType.setDescription("Mock Type Description");
        oleLicenseRequestType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleLicenseRequestType savedLicenseRequestLocation = (OleLicenseRequestType)boService.save(oleLicenseRequestType);
        assertNotNull(savedLicenseRequestLocation);
        assertNotNull(savedLicenseRequestLocation.getId());
        OleLicenseRequestType savedLicenseRequestType = boService.findBySinglePrimaryKey(OleLicenseRequestType.class,oleLicenseRequestType.getId());
        assertEquals("Mock Type Name",savedLicenseRequestType.getName());
        assertEquals("Mock Type Description",savedLicenseRequestType.getDescription());
    }
}
