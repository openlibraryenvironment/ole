package org.kuali.ole.select.bo;

import org.junit.Ignore;
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
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class OleLicenseRequestWorkflowType_IT extends KFSTestCaseBase{

    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType = new OleLicenseRequestWorkflowType();
        oleLicenseRequestWorkflowType.setCode("Mock Code");
        oleLicenseRequestWorkflowType.setName("Mock Name");
        oleLicenseRequestWorkflowType.setDescription("Mock Description");
        oleLicenseRequestWorkflowType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleLicenseRequestWorkflowType);
        OleLicenseRequestWorkflowType savedLicenseRequestWorkflowType = boService.findBySinglePrimaryKey(OleLicenseRequestWorkflowType.class,oleLicenseRequestWorkflowType.getCode());
        assertNotNull(savedLicenseRequestWorkflowType);
        assertEquals("Mock Name",savedLicenseRequestWorkflowType.getName());
        assertEquals("Mock Description",savedLicenseRequestWorkflowType.getDescription());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType = new OleLicenseRequestWorkflowType();
        oleLicenseRequestWorkflowType.setCode("Mock Code1");
        oleLicenseRequestWorkflowType.setName("Mock Name1");
        oleLicenseRequestWorkflowType.setDescription("Mock Description1");
        oleLicenseRequestWorkflowType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleLicenseRequestWorkflowType);
        OleLicenseRequestWorkflowType savedLicenseRequestWorkflowType = boService.findBySinglePrimaryKey(OleLicenseRequestWorkflowType.class,oleLicenseRequestWorkflowType.getCode());
        assertNotNull(savedLicenseRequestWorkflowType);
        assertEquals("Mock Name1",savedLicenseRequestWorkflowType.getName());
        assertEquals("Mock Description1",savedLicenseRequestWorkflowType.getDescription());
    }
}
