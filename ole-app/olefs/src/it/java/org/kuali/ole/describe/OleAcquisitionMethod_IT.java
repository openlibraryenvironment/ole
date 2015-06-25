package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleAcquisitionMethod;
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
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleAcquisitionMethod_IT extends KFSTestCaseBase {

    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleAcquisitionMethod oleAcquistionMethod = new OleAcquisitionMethod();
        oleAcquistionMethod.setAcquisitionMethodCode("n");
        oleAcquistionMethod.setAcquisitionMethodName("Non-library purchase");
        oleAcquistionMethod.setSource("src");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleAcquistionMethod.setSourceDate(new java.sql.Date(st.getTime()));
        oleAcquistionMethod.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleAcquisitionMethod> oleAcquisitionMethodCollection = boService.findAll(OleAcquisitionMethod.class);
        for (OleAcquisitionMethod acquisitionMethod : oleAcquisitionMethodCollection) {
            if (acquisitionMethod.getAcquisitionMethodCode()
                                 .equalsIgnoreCase(oleAcquistionMethod.getAcquisitionMethodCode())) {
                exists = true;
            }
        }

        if (!exists && oleAcquistionMethod.getSourceDate() != null) {
            boService.save(oleAcquistionMethod);
            OleAcquisitionMethod oleAcquisitionMethodService = boService
                    .findBySinglePrimaryKey(OleAcquisitionMethod.class, oleAcquistionMethod.getAcquisitionMethodId());
            assertEquals("n", oleAcquisitionMethodService.getAcquisitionMethodCode());
            assertEquals("Non-library purchase", oleAcquisitionMethodService.getAcquisitionMethodName());
        }
    }

    @Test
    @Transactional
    public void testSearch() {
        OleAcquisitionMethod oleAcquistionMethod = new OleAcquisitionMethod();
        oleAcquistionMethod.setAcquisitionMethodCode("n");
        oleAcquistionMethod.setAcquisitionMethodName("Non-library purchase");
        oleAcquistionMethod.setSource("src");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleAcquistionMethod.setSourceDate(new java.sql.Date(st.getTime()));
        oleAcquistionMethod.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleAcquisitionMethod> oleAcquisitionMethodCollection = boService.findAll(OleAcquisitionMethod.class);
        for (OleAcquisitionMethod acquisitionMethod : oleAcquisitionMethodCollection) {
            if (acquisitionMethod.getAcquisitionMethodCode()
                                 .equalsIgnoreCase(oleAcquistionMethod.getAcquisitionMethodCode())) {
                exists = true;
            }
        }

        if (!exists && oleAcquistionMethod.getSourceDate() != null) {
            boService.save(oleAcquistionMethod);
            OleAcquisitionMethod oleAcquisitionMethodService = boService
                    .findBySinglePrimaryKey(OleAcquisitionMethod.class, oleAcquistionMethod.getAcquisitionMethodId());
            assertEquals("n", oleAcquisitionMethodService.getAcquisitionMethodCode());
            assertEquals("Non-library purchase", oleAcquisitionMethodService.getAcquisitionMethodName());
        }
    }
}
