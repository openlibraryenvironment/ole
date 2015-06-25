package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleAccessMethod;
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
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleAccessMethod_IT extends OLETestCaseBase {

    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleAccessMethod oleAccessMethod = new OleAccessMethod();
        oleAccessMethod.setAccessMethodCode("mockCd0");
        oleAccessMethod.setAccessMethodName("mockAccessMethodName0");
        oleAccessMethod.setSource("mockSource0");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleAccessMethod.setSourceDate(new java.sql.Date(st.getTime()));
        oleAccessMethod.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleAccessMethod);
        OleAccessMethod oleAccessMethodService = boService.findBySinglePrimaryKey(OleAccessMethod.class,oleAccessMethod.getAccessMethodId());
        assertEquals("mockCd0",oleAccessMethodService.getAccessMethodCode());
        assertEquals("mockAccessMethodName0",oleAccessMethodService.getAccessMethodName());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleAccessMethod oleAccessMethod = new OleAccessMethod();
        oleAccessMethod.setAccessMethodCode("mockCd3");
        oleAccessMethod.setAccessMethodName("mockAccessMethodName3");
        oleAccessMethod.setSource("mockSource3");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleAccessMethod.setSourceDate(new java.sql.Date(st.getTime()));
        oleAccessMethod.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleAccessMethod);
        OleAccessMethod oleAccessMethodService = boService.findBySinglePrimaryKey(OleAccessMethod.class,oleAccessMethod.getAccessMethodId());
        assertEquals("mockCd3",oleAccessMethodService.getAccessMethodCode());
        assertEquals("mockAccessMethodName3",oleAccessMethodService.getAccessMethodName());
    }
}
