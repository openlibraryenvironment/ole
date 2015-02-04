package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleEncodingLevel;
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
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleEncodingLevel_IT extends SpringBaseTestCase {
    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleEncodingLevel encodingLevel = new OleEncodingLevel();
        encodingLevel.setEncodingLevelCode("testCode");
        encodingLevel.setEncodingLevelName("testName");
        encodingLevel.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        encodingLevel.setSourceDate(new java.sql.Date(st.getTime()));
        encodingLevel.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleEncodingLevel> oleEncodingLevelCollection = boService.findAll(OleEncodingLevel.class);
        for (OleEncodingLevel oleEncodingLevel : oleEncodingLevelCollection) {
            if (oleEncodingLevel.getEncodingLevelCode().equalsIgnoreCase(encodingLevel.getEncodingLevelCode())) {
                exists = true;
            }
        }

        if (!exists && encodingLevel.getEncodingLevelCode() != null) {
            boService.save(encodingLevel);
            OleEncodingLevel oleEncodingLevelService = boService
                    .findBySinglePrimaryKey(OleEncodingLevel.class, encodingLevel.getEncodingLevelId());
            assertEquals("testCode", oleEncodingLevelService.getEncodingLevelCode());
            assertEquals("testName", oleEncodingLevelService.getEncodingLevelName());
        }
    }

    @Test
    @Transactional
    public void testSearch() {
        OleEncodingLevel encodingLevel = new OleEncodingLevel();
        encodingLevel.setEncodingLevelCode("testCode");
        encodingLevel.setEncodingLevelName("testName");
        encodingLevel.setSource("testSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        encodingLevel.setSourceDate(new java.sql.Date(st.getTime()));
        encodingLevel.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleEncodingLevel> oleEncodingLevelCollection = boService.findAll(OleEncodingLevel.class);
        for (OleEncodingLevel oleEncodingLevel : oleEncodingLevelCollection) {
            if (oleEncodingLevel.getEncodingLevelCode().equalsIgnoreCase(encodingLevel.getEncodingLevelCode())) {
                exists = true;
            }
        }

        if (!exists && encodingLevel.getEncodingLevelCode() != null) {
            boService.save(encodingLevel);
            OleEncodingLevel oleEncodingLevelService = boService
                    .findBySinglePrimaryKey(OleEncodingLevel.class, encodingLevel.getEncodingLevelId());
            assertEquals("testCode", oleEncodingLevelService.getEncodingLevelCode());
            assertEquals("testName", oleEncodingLevelService.getEncodingLevelName());
        }
    }
}
