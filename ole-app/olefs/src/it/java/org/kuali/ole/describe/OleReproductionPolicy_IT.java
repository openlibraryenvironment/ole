package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleReproductionPolicy;
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
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleReproductionPolicy_IT extends KFSTestCaseBase {

    private BusinessObjectService boService ;

    @Test
    @Transactional
    public void testSave() {
        OleReproductionPolicy oleReproductionPolicy = new OleReproductionPolicy();
        oleReproductionPolicy.setReproductionPolicyCode("w1");
        oleReproductionPolicy.setReproductionPolicyName("Will reproduce");
        oleReproductionPolicy.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleReproductionPolicy.setSourceDate(new java.sql.Date(st.getTime()));
        oleReproductionPolicy.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleReproductionPolicy);
        OleReproductionPolicy oleReproductionPolicyService = boService.findBySinglePrimaryKey(OleReproductionPolicy.class,oleReproductionPolicy.getReproductionPolicyId());
        assertEquals("w1",oleReproductionPolicyService.getReproductionPolicyCode());
        assertEquals("Will reproduce",oleReproductionPolicyService.getReproductionPolicyName());
    }

    @Test
    @Transactional
    public void testSearch() {
        OleReproductionPolicy oleReproductionPolicy = new OleReproductionPolicy();
        oleReproductionPolicy.setReproductionPolicyCode("w2");
        oleReproductionPolicy.setReproductionPolicyName("Will reproduce");
        oleReproductionPolicy.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleReproductionPolicy.setSourceDate(new java.sql.Date(st.getTime()));
        oleReproductionPolicy.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleReproductionPolicy);
        OleReproductionPolicy oleReproductionPolicyService = boService.findBySinglePrimaryKey(OleReproductionPolicy.class,oleReproductionPolicy.getReproductionPolicyId());
        assertEquals("w2",oleReproductionPolicyService.getReproductionPolicyCode());
        assertEquals("Will reproduce",oleReproductionPolicyService.getReproductionPolicyName());
    }
}
