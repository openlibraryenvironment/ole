package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleFieldEncodingLevel;
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
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleFieldEncodingLevel_IT extends SpringBaseTestCase {

    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleFieldEncodingLevel fieldEncodingLevel = new OleFieldEncodingLevel();
        fieldEncodingLevel.setFieldEncodingLevelCode("testCode");
        fieldEncodingLevel.setFieldEncodingLevelName("testName");
        fieldEncodingLevel.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        fieldEncodingLevel.setSourceDate(new java.sql.Date(st.getTime()));
        fieldEncodingLevel.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleFieldEncodingLevel> oleFieldEncodingLevelCollection = boService
                .findAll(OleFieldEncodingLevel.class);
        for (OleFieldEncodingLevel oleFieldEncodingLevel : oleFieldEncodingLevelCollection) {
            if (oleFieldEncodingLevel.getFieldEncodingLevelCode()
                                     .equalsIgnoreCase(oleFieldEncodingLevel.getFieldEncodingLevelCode())) {
                exists = true;
            }
        }

        if (!exists && fieldEncodingLevel.getFieldEncodingLevelCode() != null) {
            boService.save(fieldEncodingLevel);
            OleFieldEncodingLevel oleFieldEncodingLevelService = boService
                    .findBySinglePrimaryKey(OleFieldEncodingLevel.class, fieldEncodingLevel.getFieldEncodingLevelId());
            assertEquals("testCode", oleFieldEncodingLevelService.getFieldEncodingLevelCode());
            assertEquals("testName", oleFieldEncodingLevelService.getFieldEncodingLevelName());
        }
    }

    @Test
    @Transactional
    public void testSearch() {
        OleFieldEncodingLevel fieldEncodingLevel = new OleFieldEncodingLevel();
        fieldEncodingLevel.setFieldEncodingLevelCode("testCode");
        fieldEncodingLevel.setFieldEncodingLevelName("testName");
        fieldEncodingLevel.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        fieldEncodingLevel.setSourceDate(new java.sql.Date(st.getTime()));
        fieldEncodingLevel.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleFieldEncodingLevel> oleFieldEncodingLevelCollection = boService
                .findAll(OleFieldEncodingLevel.class);
        for (OleFieldEncodingLevel oleFieldEncodingLevel : oleFieldEncodingLevelCollection) {
            if (oleFieldEncodingLevel.getFieldEncodingLevelCode()
                                     .equalsIgnoreCase(oleFieldEncodingLevel.getFieldEncodingLevelCode())) {
                exists = true;
            }
        }

        if (!exists && fieldEncodingLevel.getFieldEncodingLevelCode() != null) {
            boService.save(fieldEncodingLevel);
            OleFieldEncodingLevel oleFieldEncodingLevelService = boService
                    .findBySinglePrimaryKey(OleFieldEncodingLevel.class, fieldEncodingLevel.getFieldEncodingLevelId());
            assertEquals("testCode", oleFieldEncodingLevelService.getFieldEncodingLevelCode());
            assertEquals("testName", oleFieldEncodingLevelService.getFieldEncodingLevelName());
        }
    }
}

