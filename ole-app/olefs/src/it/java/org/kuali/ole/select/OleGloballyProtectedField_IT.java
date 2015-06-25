package org.kuali.ole.select;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: JuliyaMonica.S
 * Date: 30/10/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */



public class OleGloballyProtectedField_IT extends KFSTestCaseBase{

    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleGloballyProtectedField oleGloballyProtectedField = new OleGloballyProtectedField();
        oleGloballyProtectedField.setTag("Mock Tag");
        oleGloballyProtectedField.setSubField("Mock SubField");
        oleGloballyProtectedField.setFirstIndicator("Mock First Indicator");
        oleGloballyProtectedField.setSecondIndicator("Mock Second Indicator");
        oleGloballyProtectedField.setActiveIndicator(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleGloballyProtectedField savedGloballyProtectedField = boService.save(oleGloballyProtectedField);
        assertNotNull(savedGloballyProtectedField);
        assertNotNull(savedGloballyProtectedField.getOleGloballyProtectedFieldId());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleGloballyProtectedField oleGloballyProtectedField = new OleGloballyProtectedField();
        oleGloballyProtectedField.setTag("Mock Tag");
        oleGloballyProtectedField.setSubField("Mock SubField");
        oleGloballyProtectedField.setFirstIndicator("Mock First Indicator");
        oleGloballyProtectedField.setSecondIndicator("Mock Second Indicator");
        oleGloballyProtectedField.setActiveIndicator(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleGloballyProtectedField savedGloballyProtectedField = boService.save(oleGloballyProtectedField);
        assertNotNull(savedGloballyProtectedField);
        OleGloballyProtectedField globallyProtectedField = boService.findBySinglePrimaryKey(OleGloballyProtectedField.class,
                savedGloballyProtectedField.getOleGloballyProtectedFieldId());

        assertEquals("Mock Tag", globallyProtectedField.getTag());

    }

    @Test
    @Transactional
    public void testDelete(){
        OleGloballyProtectedField oleGloballyProtectedField = new OleGloballyProtectedField();
        oleGloballyProtectedField.setTag("Mock Tag");
        oleGloballyProtectedField.setSubField("Mock SubField");
        oleGloballyProtectedField.setFirstIndicator("Mock First Indicator");
        oleGloballyProtectedField.setSecondIndicator("Mock Second Indicator");
        oleGloballyProtectedField.setActiveIndicator(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleGloballyProtectedField savedGloballyProtectedField = boService.save(oleGloballyProtectedField);
        assertNotNull(savedGloballyProtectedField);
        OleGloballyProtectedField globallyProtectedField = boService.findBySinglePrimaryKey(OleGloballyProtectedField.class,
                savedGloballyProtectedField.getOleGloballyProtectedFieldId());
        assertNotNull(globallyProtectedField);
        boService.delete(savedGloballyProtectedField);
        OleGloballyProtectedField deletedGloballyProtectedField = boService.findBySinglePrimaryKey(OleGloballyProtectedField.class,
                savedGloballyProtectedField.getOleGloballyProtectedFieldId());
        assertNull(deletedGloballyProtectedField);

    }

}
