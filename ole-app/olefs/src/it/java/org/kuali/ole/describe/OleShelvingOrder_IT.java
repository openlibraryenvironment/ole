package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleShelvingOrder;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/11/12
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleShelvingOrder_IT extends KFSTestCaseBase {

    private BusinessObjectService boService ;

    @Test
    @Transactional
    public void testSave() {
        OleShelvingOrder oleShelvingOrder = new OleShelvingOrder();
        oleShelvingOrder.setShelvingOrderCode("n1");
        oleShelvingOrder.setShelvingOrderName("No information provided");
        oleShelvingOrder.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleShelvingOrder.setSourceDate(new java.sql.Date(st.getTime()));
        oleShelvingOrder.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleShelvingOrder);
        OleShelvingOrder oleShelvingOrderService = boService.findBySinglePrimaryKey(OleShelvingOrder.class,oleShelvingOrder.getShelvingOrderId());
        assertEquals("n1",oleShelvingOrderService.getShelvingOrderCode());
        assertEquals("No information provided",oleShelvingOrderService.getShelvingOrderName());
    }

    @Test
    @Transactional
    public void testSearch() {
        OleShelvingOrder oleShelvingOrder = new OleShelvingOrder();
        oleShelvingOrder.setShelvingOrderCode("01");
        oleShelvingOrder.setShelvingOrderName("Not enumeration");
        oleShelvingOrder.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleShelvingOrder.setSourceDate(new java.sql.Date(st.getTime()));
        oleShelvingOrder.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleShelvingOrder);
        OleShelvingOrder oleShelvingOrderService = boService.findBySinglePrimaryKey(OleShelvingOrder.class,oleShelvingOrder.getShelvingOrderId());
        assertEquals("01",oleShelvingOrderService.getShelvingOrderCode());
        assertEquals("Not enumeration",oleShelvingOrderService.getShelvingOrderName());
    }
}
