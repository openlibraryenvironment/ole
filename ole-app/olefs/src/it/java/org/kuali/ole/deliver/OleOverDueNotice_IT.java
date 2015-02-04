package org.kuali.ole.deliver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.batch.OleOverDueNotice;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/14/12
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class OleOverDueNotice_IT extends SpringBaseTestCase{
    private OleOverDueNotice oleOverDueNoticeBean;
    private BusinessObjectService service;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        service = KRADServiceLocator.getBusinessObjectService();
        oleOverDueNoticeBean = GlobalResourceLoader.getService("oleOverDueNotice");
    }

    @Test
    @Transactional
    public void testGenerateNotices() throws Exception {
         oleOverDueNoticeBean.generateNotices();
    }
    @Test
    @Transactional
    public  void testGenerateHoldCourtesyNotice() throws Exception{
        oleOverDueNoticeBean.generateHoldCourtesyNotice();
    }
    @Test
    @Transactional
    public void testDeleteTemporaryHistoryRecord() throws Exception {
        oleOverDueNoticeBean.deleteTemporaryHistoryRecord();
    }
    @Test
    @Transactional
    public void testGenerateRequestExpirationNotice() throws Exception {
        oleOverDueNoticeBean.generateRequestExpirationNotice();
    }
    @Test
    @Transactional
    public void testDeletingExpiredRequests() throws Exception {
        oleOverDueNoticeBean.deletingExpiredRequests();
    }
    @Test
    @Transactional
    public void testGenerateOnHoldNotice() throws Exception {
        oleOverDueNoticeBean.generateOnHoldNotice();
    }
    @Test
    @Transactional
    public void testUpdateStatusIntoAvailableAfterReShelving() throws Exception {
        oleOverDueNoticeBean.updateStatusIntoAvailableAfterReShelving();
    }

    @After
    public void tearDown() throws Exception {
        service = null;
        oleOverDueNoticeBean = null;
    }
}
