package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleSpecificRetentionPolicyType;
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
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/11/12
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleSpecificRetentionPolicyType_IT extends SpringBaseTestCase {

    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleSpecificRetentionPolicyType oleSpecificRetentionPolicyType = new OleSpecificRetentionPolicyType();
        oleSpecificRetentionPolicyType.setSpecificRetentionPolicyTypeCode("l");
        oleSpecificRetentionPolicyType.setSpecificRetentionPolicyTypeName("Latest");
        oleSpecificRetentionPolicyType.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleSpecificRetentionPolicyType.setSourceDate(new java.sql.Date(st.getTime()));
        oleSpecificRetentionPolicyType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleSpecificRetentionPolicyType> oleSpecificRetentionPolicyTypeCollection = boService
                .findAll(OleSpecificRetentionPolicyType.class);
        for (OleSpecificRetentionPolicyType specificRetentionPolicyType : oleSpecificRetentionPolicyTypeCollection) {
            if (specificRetentionPolicyType.getSpecificRetentionPolicyTypeCode().equalsIgnoreCase(
                    oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeCode())) {
                exists = true;
            }
        }

        if (!exists && oleSpecificRetentionPolicyType.getSourceDate() != null) {
            boService.save(oleSpecificRetentionPolicyType);
            OleSpecificRetentionPolicyType oleSpecificRetentionPolicyTypeService = boService
                    .findBySinglePrimaryKey(OleSpecificRetentionPolicyType.class,
                                            oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeId());
            assertEquals("l", oleSpecificRetentionPolicyTypeService.getSpecificRetentionPolicyTypeCode());
            assertEquals("Latest", oleSpecificRetentionPolicyTypeService.getSpecificRetentionPolicyTypeName());
        }

    }

    @Test
    @Transactional
    public void testSearch() {
        OleSpecificRetentionPolicyType oleSpecificRetentionPolicyType = new OleSpecificRetentionPolicyType();
        oleSpecificRetentionPolicyType.setSpecificRetentionPolicyTypeCode("l");
        oleSpecificRetentionPolicyType.setSpecificRetentionPolicyTypeName("Latest");
        oleSpecificRetentionPolicyType.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleSpecificRetentionPolicyType.setSourceDate(new java.sql.Date(st.getTime()));
        oleSpecificRetentionPolicyType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleSpecificRetentionPolicyType> oleSpecificRetentionPolicyTypeCollection = boService
                .findAll(OleSpecificRetentionPolicyType.class);
        for (OleSpecificRetentionPolicyType specificRetentionPolicyType : oleSpecificRetentionPolicyTypeCollection) {
            if (specificRetentionPolicyType.getSpecificRetentionPolicyTypeCode().equalsIgnoreCase(
                    oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeCode())) {
                exists = true;
            }
        }

        if (!exists && oleSpecificRetentionPolicyType.getSourceDate() != null) {
            boService.save(oleSpecificRetentionPolicyType);
            OleSpecificRetentionPolicyType oleSpecificRetentionPolicyTypeService = boService
                    .findBySinglePrimaryKey(OleSpecificRetentionPolicyType.class,
                                            oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeId());
            assertEquals("l", oleSpecificRetentionPolicyTypeService.getSpecificRetentionPolicyTypeCode());
            assertEquals("Latest", oleSpecificRetentionPolicyTypeService.getSpecificRetentionPolicyTypeName());
        }
    }
}
