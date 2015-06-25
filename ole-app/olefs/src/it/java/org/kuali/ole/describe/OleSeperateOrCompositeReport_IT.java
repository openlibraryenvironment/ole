package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleSeperateOrCompositeReport;
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
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleSeperateOrCompositeReport_IT extends KFSTestCaseBase {

    private BusinessObjectService boService ;

    @Test
    @Transactional
    public void testSave() {
        OleSeperateOrCompositeReport oleSeperateOrCompositeReport = new OleSeperateOrCompositeReport();
        oleSeperateOrCompositeReport.setSeperateOrCompositeReportCode("s1");
        oleSeperateOrCompositeReport.setSeperateOrCompositeReportName("Separate copy report");
        oleSeperateOrCompositeReport.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleSeperateOrCompositeReport.setSourceDate(new java.sql.Date(st.getTime()));
        oleSeperateOrCompositeReport.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleSeperateOrCompositeReport);
        OleSeperateOrCompositeReport oleSeperateOrCompositeReportService = boService.findBySinglePrimaryKey(OleSeperateOrCompositeReport.class,oleSeperateOrCompositeReport.getSeperateOrCompositeReportId());
        assertEquals("s1",oleSeperateOrCompositeReportService.getSeperateOrCompositeReportCode());
        assertEquals("Separate copy report",oleSeperateOrCompositeReportService.getSeperateOrCompositeReportName());
    }

    @Test
    @Transactional
    public void testSearch() {
        OleSeperateOrCompositeReport oleSeperateOrCompositeReport = new OleSeperateOrCompositeReport();
        oleSeperateOrCompositeReport.setSeperateOrCompositeReportCode("c1");
        oleSeperateOrCompositeReport.setSeperateOrCompositeReportName("Composite copy report");
        oleSeperateOrCompositeReport.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleSeperateOrCompositeReport.setSourceDate(new java.sql.Date(st.getTime()));
        oleSeperateOrCompositeReport.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleSeperateOrCompositeReport);
        OleSeperateOrCompositeReport oleSeperateOrCompositeReportService = boService.findBySinglePrimaryKey(OleSeperateOrCompositeReport.class,oleSeperateOrCompositeReport.getSeperateOrCompositeReportId());
        assertEquals("c1",oleSeperateOrCompositeReportService.getSeperateOrCompositeReportCode());
        assertEquals("Composite copy report",oleSeperateOrCompositeReportService.getSeperateOrCompositeReportName());
    }
}
