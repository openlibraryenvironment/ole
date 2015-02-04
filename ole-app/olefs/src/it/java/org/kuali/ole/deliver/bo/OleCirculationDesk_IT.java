package org.kuali.ole.deliver.bo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/7/12
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleCirculationDesk_IT extends SpringBaseTestCase{

    private BusinessObjectService boService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        boService = KRADServiceLocator.getBusinessObjectService();

    }

    @Test
    @Transactional
    public void testSave() {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setLocationId("1010");
        oleCirculationDesk.setCirculationDeskCode("code");
        oleCirculationDesk.setCirculationDeskPublicName("publicName");
        oleCirculationDesk.setCirculationDeskStaffName("staffName");
        oleCirculationDesk.setOnHoldDays("2");
        oleCirculationDesk.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleCirculationDesk> oleCirculationDeskCollection = boService.findAll(OleCirculationDesk.class);
        for (OleCirculationDesk circulationDesk : oleCirculationDeskCollection) {
            if (circulationDesk.getCirculationDeskCode()
                               .equalsIgnoreCase(oleCirculationDesk.getCirculationDeskCode())) {
                exists = true;
            }
        }

        if (!exists && oleCirculationDesk.getOnHoldDays() != null) {
            boService.save(oleCirculationDesk);
            OleCirculationDesk oleCirculationDeskService = boService
                    .findBySinglePrimaryKey(OleCirculationDesk.class, oleCirculationDesk.getCirculationDeskId());
            assertEquals("code", oleCirculationDeskService.getCirculationDeskCode());
            assertEquals("publicName", oleCirculationDeskService.getCirculationDeskPublicName());
        }

    }

    @Test
    @Transactional
    public void testSearch() {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setLocationId("1011");
        oleCirculationDesk.setCirculationDeskCode("code");
        oleCirculationDesk.setCirculationDeskPublicName("publicName");
        oleCirculationDesk.setCirculationDeskStaffName("staffName");
        oleCirculationDesk.setOnHoldDays("2");
        oleCirculationDesk.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleCirculationDesk> oleCirculationDeskCollection = boService.findAll(OleCirculationDesk.class);
        for (OleCirculationDesk circulationDesk : oleCirculationDeskCollection) {
            if (circulationDesk.getCirculationDeskCode()
                               .equalsIgnoreCase(oleCirculationDesk.getCirculationDeskCode())) {
                exists = true;
            }
        }

        if (!exists && oleCirculationDesk.getOnHoldDays() != null) {
            boService.save(oleCirculationDesk);
            OleCirculationDesk oleCirculationDeskService = boService
                    .findBySinglePrimaryKey(OleCirculationDesk.class, oleCirculationDesk.getCirculationDeskId());
            assertEquals("code", oleCirculationDeskService.getCirculationDeskCode());
            assertEquals("publicName", oleCirculationDeskService.getCirculationDeskPublicName());
        }
    }

}



