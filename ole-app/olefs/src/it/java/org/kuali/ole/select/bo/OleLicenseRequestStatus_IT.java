package org.kuali.ole.select.bo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: meenau
 * Date: 6/4/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleLicenseRequestStatus_IT extends SpringBaseTestCase{

    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleLicenseRequestStatus oleLicenseRequestStatus = new OleLicenseRequestStatus();
        oleLicenseRequestStatus.setCode("Mock Code1");
        oleLicenseRequestStatus.setName("Mock Name1");
        oleLicenseRequestStatus.setDescription("Mock Description1");
        oleLicenseRequestStatus.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleLicenseRequestStatus);
        OleLicenseRequestStatus savedLicenseStatus = boService.findBySinglePrimaryKey(OleLicenseRequestStatus.class,oleLicenseRequestStatus.getCode());
        assertEquals("Mock Name1",savedLicenseStatus.getName());
        assertEquals("Mock Description1",savedLicenseStatus.getDescription());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleLicenseRequestStatus oleLicenseRequestStatus = new OleLicenseRequestStatus();
        oleLicenseRequestStatus.setCode("Mock Code1");
        oleLicenseRequestStatus.setName("Mock Name1");
        oleLicenseRequestStatus.setDescription("Mock Description1");
        oleLicenseRequestStatus.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleLicenseRequestStatus);
        OleLicenseRequestStatus savedLicenseStatus = boService.findBySinglePrimaryKey(OleLicenseRequestStatus.class,oleLicenseRequestStatus.getCode());
        assertEquals("Mock Name1",savedLicenseStatus.getName());
        assertEquals("Mock Description1",savedLicenseStatus.getDescription());
    }
}
