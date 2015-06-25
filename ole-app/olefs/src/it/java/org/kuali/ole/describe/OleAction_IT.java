package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleAction;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: meenau
 * Date: 6/4/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleAction_IT extends KFSTestCaseBase {

    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleAction oleAction = new OleAction();
        oleAction.setActionCode("mockCde");
        oleAction.setActionName("mockActionName");
        oleAction.setActionDescription("mockActionDescription");
        oleAction.setSource("mockSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleAction.setSourceDate(new java.sql.Date(st.getTime()));
        oleAction.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleAction);
        OleAction oleActionService = boService.findBySinglePrimaryKey(OleAction.class,oleAction.getActionId());
        assertEquals("mockCde",oleActionService.getActionCode());
        assertEquals("mockActionName",oleActionService.getActionName());
        assertEquals("mockActionDescription",oleActionService.getActionDescription());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleAction oleAction = new OleAction();
        oleAction.setActionCode("mockCde0");
        oleAction.setActionName("mockActionName0");
        oleAction.setActionDescription("mockActionDescription0");
        oleAction.setSource("mockSource0");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleAction.setSourceDate(new java.sql.Date(st.getTime()));
        oleAction.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleAction);
        OleAction oleActionService = boService.findBySinglePrimaryKey(OleAction.class,oleAction.getActionId());
        assertEquals("mockCde0",oleActionService.getActionCode());
        assertEquals("mockActionName0",oleActionService.getActionName());
        assertEquals("mockActionDescription0",oleActionService.getActionDescription());
    }
}
