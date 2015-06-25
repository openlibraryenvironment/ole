package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleCompleteness;
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
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleCompleteness_IT extends KFSTestCaseBase {

    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleCompleteness completeness = new OleCompleteness();
        completeness.setCompletenessCode("testCode");
        completeness.setCompletenessName("testName");
        completeness.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        completeness.setSourceDate(new java.sql.Date(st.getTime()));
        completeness.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleCompleteness> oleCompletenessCollection = boService.findAll(OleCompleteness.class);
        for (OleCompleteness oleCompleteness : oleCompletenessCollection) {
            if (oleCompleteness.getCompletenessCode().equalsIgnoreCase(completeness.getCompletenessCode())) {
                exists = true;
            }
        }

        if (!exists && completeness.getSourceDate() != null) {
            boService.save(completeness);
            OleCompleteness oleCompletenessService = boService
                    .findBySinglePrimaryKey(OleCompleteness.class, completeness.getCompletenessId());
            assertEquals("testCode", oleCompletenessService.getCompletenessCode());
            assertEquals("testName", oleCompletenessService.getCompletenessName());
        }
    }

    @Test
    @Transactional
    public void testSearch() {
        OleCompleteness completeness = new OleCompleteness();
        completeness.setCompletenessCode("testCode");
        completeness.setCompletenessName("testName");
        completeness.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        completeness.setSourceDate(new java.sql.Date(st.getTime()));
        completeness.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleCompleteness> oleCompletenessCollection = boService.findAll(OleCompleteness.class);
        for (OleCompleteness oleCompleteness : oleCompletenessCollection) {
            if (oleCompleteness.getCompletenessCode().equalsIgnoreCase(completeness.getCompletenessCode())) {
                exists = true;
            }
        }

        if (!exists && completeness.getSourceDate() != null) {
            boService.save(completeness);
            OleCompleteness oleCompletenessService = boService
                    .findBySinglePrimaryKey(OleCompleteness.class, completeness.getCompletenessId());
            assertEquals("testCode", oleCompletenessService.getCompletenessCode());
            assertEquals("testName", oleCompletenessService.getCompletenessName());
        }
    }

}
