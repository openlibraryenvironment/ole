package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleTypeOfOwnership;
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
 * User: meenau
 * Date: 6/4/12
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleTypeOfOwnership_IT extends SpringBaseTestCase {

    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleTypeOfOwnership oleTypeOfOwnership = new OleTypeOfOwnership();
        oleTypeOfOwnership.setTypeOfOwnershipCode("mockCd");
        oleTypeOfOwnership.setTypeOfOwnershipName("mockTypeOfOwnershipName");
        oleTypeOfOwnership.setSource("mockSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleTypeOfOwnership.setSourceDate(new java.sql.Date(st.getTime()));
        oleTypeOfOwnership.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleTypeOfOwnership> oleTypeOfOwnershipCollection = boService.findAll(OleTypeOfOwnership.class);
        for (OleTypeOfOwnership typeOfOwnership : oleTypeOfOwnershipCollection) {
            if (typeOfOwnership.getTypeOfOwnershipCode()
                               .equalsIgnoreCase(oleTypeOfOwnership.getTypeOfOwnershipCode())) {
                exists = true;
            }
        }

        if (!exists && oleTypeOfOwnership.getSourceDate() != null) {
            boService.save(oleTypeOfOwnership);
            OleTypeOfOwnership oleTypeOfOwnershipService = boService
                    .findBySinglePrimaryKey(OleTypeOfOwnership.class, oleTypeOfOwnership.getTypeOfOwnershipId());
            assertEquals("mockCd", oleTypeOfOwnershipService.getTypeOfOwnershipCode());
            assertEquals("mockTypeOfOwnershipName", oleTypeOfOwnershipService.getTypeOfOwnershipName());
        }

    }

    @Test
    @Transactional
    public void testSearch() {
        OleTypeOfOwnership oleTypeOfOwnership = new OleTypeOfOwnership();
        oleTypeOfOwnership.setTypeOfOwnershipCode("mockCd");
        oleTypeOfOwnership.setTypeOfOwnershipName("mockTypeOfOwnershipName");
        oleTypeOfOwnership.setSource("mockSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleTypeOfOwnership.setSourceDate(new java.sql.Date(st.getTime()));
        oleTypeOfOwnership.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleTypeOfOwnership> oleTypeOfOwnershipCollection = boService.findAll(OleTypeOfOwnership.class);
        for (OleTypeOfOwnership typeOfOwnership : oleTypeOfOwnershipCollection) {
            if (typeOfOwnership.getTypeOfOwnershipCode()
                               .equalsIgnoreCase(oleTypeOfOwnership.getTypeOfOwnershipCode())) {
                exists = true;
            }
        }

        if (!exists && oleTypeOfOwnership.getSourceDate() != null) {
            boService.save(oleTypeOfOwnership);
            OleTypeOfOwnership oleTypeOfOwnershipService = boService
                    .findBySinglePrimaryKey(OleTypeOfOwnership.class, oleTypeOfOwnership.getTypeOfOwnershipId());
            assertEquals("mockCd", oleTypeOfOwnershipService.getTypeOfOwnershipCode());
            assertEquals("mockTypeOfOwnershipName", oleTypeOfOwnershipService.getTypeOfOwnershipName());
        }
    }
}
