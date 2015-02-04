package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleElectronicLocationAndAccessRelationship;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: maheswarang
 * Date: 5/29/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleElectronicLocationAndAccessRelationship_IT extends SpringBaseTestCase {

    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleElectronicLocationAndAccessRelationship oleElaRelationship
                = new OleElectronicLocationAndAccessRelationship();
        oleElaRelationship.setElaRelationshipCode("testCode");
        oleElaRelationship.setElaRelationshipName("testName");
        oleElaRelationship.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleElaRelationship.setSourceDate(new java.sql.Date(st.getTime()));
        oleElaRelationship.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleElectronicLocationAndAccessRelationship> elaRelationshipCollection = boService
                .findAll(OleElectronicLocationAndAccessRelationship.class);
        for (OleElectronicLocationAndAccessRelationship elaRelationship : elaRelationshipCollection) {
            if (elaRelationship.getElaRelationshipCode()
                               .equalsIgnoreCase(oleElaRelationship.getElaRelationshipCode())) {
                exists = true;
            }
        }

        if (!exists && oleElaRelationship.getElaRelationshipCode() != null) {
            boService.save(oleElaRelationship);
            OleElectronicLocationAndAccessRelationship oleElaRelationshipService = boService
                    .findBySinglePrimaryKey(OleElectronicLocationAndAccessRelationship.class,
                                            oleElaRelationship.getElaRelationshipId());
            assertEquals("testCode", oleElaRelationshipService.getElaRelationshipCode());
            assertEquals("testName", oleElaRelationshipService.getElaRelationshipName());
        }
    }

    @Test
    @Transactional
    public void testSearch() {
        OleElectronicLocationAndAccessRelationship oleElaRelationship
                = new OleElectronicLocationAndAccessRelationship();
        oleElaRelationship.setElaRelationshipCode("testCode");
        oleElaRelationship.setElaRelationshipName("testName");
        oleElaRelationship.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleElaRelationship.setSourceDate(new java.sql.Date(st.getTime()));
        oleElaRelationship.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleElectronicLocationAndAccessRelationship> elaRelationshipCollection = boService
                .findAll(OleElectronicLocationAndAccessRelationship.class);
        for (OleElectronicLocationAndAccessRelationship elaRelationship : elaRelationshipCollection) {
            if (elaRelationship.getElaRelationshipCode()
                               .equalsIgnoreCase(oleElaRelationship.getElaRelationshipCode())) {
                exists = true;
            }
        }

        if (!exists && oleElaRelationship.getElaRelationshipCode() != null) {
            boService.save(oleElaRelationship);
            OleElectronicLocationAndAccessRelationship oleElaRelationshipService = boService
                    .findBySinglePrimaryKey(OleElectronicLocationAndAccessRelationship.class,
                                            oleElaRelationship.getElaRelationshipId());
            assertEquals("testCode", oleElaRelationshipService.getElaRelationshipCode());
            assertEquals("testName", oleElaRelationshipService.getElaRelationshipName());
        }
    }
}
