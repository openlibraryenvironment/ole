package org.kuali.ole.deliver.drools.notices;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.assertNotNull;

/**
 * Created by pvsubrah on 11/3/15.
 */
public class NoticeGenerator_IT extends OLETestCaseBase {

    @Test
    public void generateNotices() {
        OleNoticeGeneratorPlatformAwareDao oleNoticeGeneratorPlatformAwareDao =
                (OleNoticeGeneratorPlatformAwareDao) SpringContext.getBean("oleNoticeGeneratorPlatformAwareDao");

        assertNotNull(oleNoticeGeneratorPlatformAwareDao);

//        List<Map<String, Object>> loanRecordsFromDBByDates = oleNoticeGeneratorPlatformAwareDao.getLoanRecordsFromDBByDates("10/17/2015", "11/04/2015");
        List<Map<String, Object>> loanRecordsFromDBByDates = oleNoticeGeneratorPlatformAwareDao.getLoanRecordsFromDBByDates(null, null);
        assertNotNull(loanRecordsFromDBByDates);

        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        List<Future> futures = new ArrayList<>();

        List<String> loanIds = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = loanRecordsFromDBByDates.iterator(); iterator.hasNext(); ) {
            Map<String, Object> objectMap = iterator.next();
            OleLoanDocument currentLoanDocument = new OleLoanDocument();
            ItemRecord itemRecord = new ItemRecord();

            String cir_policy_id = (String) objectMap.get("cir_policy_id");
            currentLoanDocument.setCirculationPolicyId(cir_policy_id);
            String loan_tran_id = (String) objectMap.get("loan_tran_id");
            if (null == cir_policy_id) {
                System.out.println("Circ policy id was null for loanId: " + loan_tran_id);
            }
            currentLoanDocument.setLoanId(loan_tran_id);
            String patronId = (String) objectMap.get("ole_ptrn_id");
            currentLoanDocument.setPatronId(patronId);
            Timestamp dueDateTime = (Timestamp) objectMap.get("curr_due_dt_time");
            currentLoanDocument.setLoanDueDate(dueDateTime);
            loanIds.add(loan_tran_id);

            itemRecord.setDueDateTime((Timestamp) objectMap.get("curr_due_dt_time"));

            Future future = executorService.submit(new NoticeEvaluvator(currentLoanDocument, itemRecord));
            futures.add(future);
        }

        long startTime = System.currentTimeMillis();
        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                oleDeliverNotices.addAll((Collection<? extends OLEDeliverNotice>) future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken to generate notices: " + (endTime-startTime) + " ms");

        assertNotNull(oleDeliverNotices);
        System.out.println("Num notices generated: " + oleDeliverNotices.size());

        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();


        List<Future> noticesTobePersisted = new ArrayList<>();

        List<List<OLEDeliverNotice>> partitionList = Lists.partition(oleDeliverNotices, 1000);
        for (Iterator<List<OLEDeliverNotice>> iterator = partitionList.iterator(); iterator.hasNext(); ) {
            List<OLEDeliverNotice> deliverNotices = iterator.next();
            noticesTobePersisted.add(executorService.submit(new NoticePersister(businessObjectService, deliverNotices)));
        }

        long startTime1 = System.currentTimeMillis();
        List<OLEDeliverNotice> savedNotices = new ArrayList<>();
        for (Iterator<Future> iterator = noticesTobePersisted.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                savedNotices.addAll((Collection<? extends OLEDeliverNotice>) future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        long endTime1 = System.currentTimeMillis();
        System.out.println("Time taken to generate notices: " + (endTime1-startTime1) + " ms");

        executorService.shutdown();
    }



    class NoticeEvaluvator implements Callable {

        private OleLoanDocument currentLoanDocument;
        private ItemRecord itemRecord;

        public NoticeEvaluvator(OleLoanDocument loanDocument, ItemRecord itemRecord) {
            this.currentLoanDocument = loanDocument;
            this.itemRecord = itemRecord;
        }

        @Override
        public Object call() throws Exception {
            List<OLEDeliverNotice> deliverNotices = new CircUtilController().processNotices(currentLoanDocument, itemRecord,null);
            return deliverNotices;
        }
    }



    class NoticePersister implements Callable {
        private final List<OLEDeliverNotice> notices;
        private final BusinessObjectService businessObjectService;

        public NoticePersister(BusinessObjectService businessObjectService, List<OLEDeliverNotice> notices) {
            this.businessObjectService = businessObjectService;
            this.notices = notices;
        }

        @Override
        public Object call() throws Exception {
            List<? extends PersistableBusinessObject> save = businessObjectService.save(notices);
            return save;
        }
    }
}
