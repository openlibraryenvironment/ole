package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
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
 * User: meenau
 * Date: 6/1/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class OleStatisticalSearchingCodes_IT extends SpringBaseTestCase {
    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleStatisticalSearchingCodes oleStatisticalSearchingCodes = new OleStatisticalSearchingCodes();
        oleStatisticalSearchingCodes.setStatisticalSearchingCode("mockCd1");
        oleStatisticalSearchingCodes.setStatisticalSearchingName("mockStatisticalSearchingName1");
        oleStatisticalSearchingCodes.setSource("mockSource1");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleStatisticalSearchingCodes.setSourceDate(new java.sql.Date(st.getTime()));
        oleStatisticalSearchingCodes.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleStatisticalSearchingCodes> oleStatisticalSearchingCodesCollection = boService
                .findAll(OleStatisticalSearchingCodes.class);
        for (OleStatisticalSearchingCodes statisticalSearchingCodes : oleStatisticalSearchingCodesCollection) {
            if (statisticalSearchingCodes.getStatisticalSearchingCode().equalsIgnoreCase(
                    oleStatisticalSearchingCodes.getStatisticalSearchingCode())) {
                exists = true;
            }
        }

        if (!exists && oleStatisticalSearchingCodes.getSourceDate() != null) {
            boService.save(oleStatisticalSearchingCodes);
            OleStatisticalSearchingCodes oleStatisticalSearchingCodesService = boService
                    .findBySinglePrimaryKey(OleStatisticalSearchingCodes.class,
                                            oleStatisticalSearchingCodes.getStatisticalSearchingCodeId());
            assertEquals("mockCd1", oleStatisticalSearchingCodesService.getStatisticalSearchingCode());
            assertEquals("mockStatisticalSearchingName1",
                         oleStatisticalSearchingCodesService.getStatisticalSearchingName());
        }


    }

    @Test
    @Transactional
    public void testSearch() {
        OleStatisticalSearchingCodes oleStatisticalSearchingCodes = new OleStatisticalSearchingCodes();
        oleStatisticalSearchingCodes.setStatisticalSearchingCode("mockCd1");
        oleStatisticalSearchingCodes.setStatisticalSearchingName("mockStatisticalSearchingName1");
        oleStatisticalSearchingCodes.setSource("mockSource1");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleStatisticalSearchingCodes.setSourceDate(new java.sql.Date(st.getTime()));
        oleStatisticalSearchingCodes.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleStatisticalSearchingCodes> oleStatisticalSearchingCodesCollection = boService
                .findAll(OleStatisticalSearchingCodes.class);
        for (OleStatisticalSearchingCodes statisticalSearchingCodes : oleStatisticalSearchingCodesCollection) {
            if (statisticalSearchingCodes.getStatisticalSearchingCode().equalsIgnoreCase(
                    oleStatisticalSearchingCodes.getStatisticalSearchingCode())) {
                exists = true;
            }
        }

        if (!exists && oleStatisticalSearchingCodes.getSourceDate() != null) {
            boService.save(oleStatisticalSearchingCodes);
            OleStatisticalSearchingCodes oleStatisticalSearchingCodesService = boService
                    .findBySinglePrimaryKey(OleStatisticalSearchingCodes.class,
                                            oleStatisticalSearchingCodes.getStatisticalSearchingCodeId());
            assertEquals("mockCd1", oleStatisticalSearchingCodesService.getStatisticalSearchingCode());
            assertEquals("mockStatisticalSearchingName1",
                         oleStatisticalSearchingCodesService.getStatisticalSearchingName());
        }
    }
}
