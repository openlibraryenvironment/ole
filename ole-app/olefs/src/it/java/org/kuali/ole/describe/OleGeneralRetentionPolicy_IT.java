package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleGeneralRetentionPolicy;
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
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleGeneralRetentionPolicy_IT extends SpringBaseTestCase {
    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {

        OleGeneralRetentionPolicy generalRetentionPolicy = new OleGeneralRetentionPolicy();
        generalRetentionPolicy.setGeneralRetentionPolicyCode("testCode");
        generalRetentionPolicy.setGeneralRetentionPolicyName("testName");
        generalRetentionPolicy.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        generalRetentionPolicy.setSourceDate(new java.sql.Date(st.getTime()));
        generalRetentionPolicy.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleGeneralRetentionPolicy> oleRetentionPolicyCollection = boService
                .findAll(OleGeneralRetentionPolicy.class);
        for (OleGeneralRetentionPolicy oleGeneralRetentionPolicy : oleRetentionPolicyCollection) {
            if (oleGeneralRetentionPolicy.getGeneralRetentionPolicyCode()
                                         .equalsIgnoreCase(generalRetentionPolicy.getGeneralRetentionPolicyCode())) {
                exists = true;
            }
        }

        if (!exists && generalRetentionPolicy.getGeneralRetentionPolicyCode() != null) {
            boService.save(generalRetentionPolicy);
            OleGeneralRetentionPolicy oleGeneralRetentionPolicyService = boService
                    .findBySinglePrimaryKey(OleGeneralRetentionPolicy.class,
                                            generalRetentionPolicy.getGeneralRetentionPolicyId());
            assertEquals("testCode", oleGeneralRetentionPolicyService.getGeneralRetentionPolicyCode());
            assertEquals("testName", oleGeneralRetentionPolicyService.getGeneralRetentionPolicyName());
        }

    }

    @Test
    @Transactional
    public void testSearch() {
        OleGeneralRetentionPolicy generalRetentionPolicy = new OleGeneralRetentionPolicy();
        generalRetentionPolicy.setGeneralRetentionPolicyCode("testCode");
        generalRetentionPolicy.setGeneralRetentionPolicyName("testName");
        generalRetentionPolicy.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        generalRetentionPolicy.setSourceDate(new java.sql.Date(st.getTime()));
        generalRetentionPolicy.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleGeneralRetentionPolicy> oleRetentionPolicyCollection = boService
                .findAll(OleGeneralRetentionPolicy.class);
        for (OleGeneralRetentionPolicy oleGeneralRetentionPolicy : oleRetentionPolicyCollection) {
            if (oleGeneralRetentionPolicy.getGeneralRetentionPolicyCode()
                                         .equalsIgnoreCase(generalRetentionPolicy.getGeneralRetentionPolicyCode())) {
                exists = true;
            }
        }

        if (!exists && generalRetentionPolicy.getGeneralRetentionPolicyCode() != null) {
            boService.save(generalRetentionPolicy);
            OleGeneralRetentionPolicy oleGeneralRetentionPolicyService = boService
                    .findBySinglePrimaryKey(OleGeneralRetentionPolicy.class,
                                            generalRetentionPolicy.getGeneralRetentionPolicyId());
            assertEquals("testCode", oleGeneralRetentionPolicyService.getGeneralRetentionPolicyCode());
            assertEquals("testName", oleGeneralRetentionPolicyService.getGeneralRetentionPolicyName());
        }
    }

}
