package org.kuali.ole.deliver.service;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OverdueNoticesExecutorTest {

    @Mock
    private OleDeliverRequestDocumentHelperServiceImpl mockOleDeliverRequestDocumentHelperServiceImpl;

    @Mock
    private LoanProcessor mockLoanProcessor;
    @Mock
    private BusinessObjectService mockBusinessObjectService;
    @Mock
    private ParameterValueResolver mockParameterResolverInstance;
    @Mock
    private NoticeMailContentFormatter mockNoticeMailContentFormatter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateOverDueNoticeContent() throws Exception {

        Mockito.when(mockLoanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE)).thenReturn("TITLE");
        Mockito.when(mockLoanProcessor.getParameter(OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT))
                .thenReturn
                        ("CONTENT");

        List<OleLoanDocument> loanDocuments = new ArrayList<>();
        Map overdueMap = new HashMap();
        overdueMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, "Overdue");
        overdueMap.put(OLEConstants.LOAN_DOCUMENTS, loanDocuments);
        OverdueNoticesExecutor overdueNoticesExecutor = new OverdueNoticesExecutor(overdueMap);

        overdueNoticesExecutor.setNoticeMailContentFormatter(mockNoticeMailContentFormatter);

        OleLoanDocument oleLoanDocument = new OleLoanDocument();

        OlePatronDocument olePatron = new OlePatronDocument();
        olePatron.setBarcode("123125");
        EntityBo entity = new EntityBo();
        ArrayList<EntityNameBo> entityNameBos = new ArrayList<>();
        EntityNameBo entityNameBo = new EntityNameBo();
        entityNameBo.setFirstName("FirtName");
        entityNameBos.add(entityNameBo);
        entity.setNames(entityNameBos);
        ArrayList<EntityTypeContactInfoBo> entityTypeContactInfos = new ArrayList<>();
        entityTypeContactInfos.add(new EntityTypeContactInfoBo());
        entity.setEntityTypeContactInfos(entityTypeContactInfos);
        olePatron.setEntity(entity);

        oleLoanDocument.setOlePatron(olePatron);
    }

    @Test
    public void generateDateStringsForMySQL() {
        String generateDateStringsForMySQL = DateFormatHelper.getInstance().generateDateStringsForMySQL("3/3/2015");
        System.out.println(generateDateStringsForMySQL);
    }

    @Test
    public void generateDateStringsForOracle() {
        String generateDateStringsForOracle = DateFormatHelper.getInstance().generateDateStringsForOracle("3/3/2015");
        System.out.println(generateDateStringsForOracle);
    }

    @Test
    public void generateTimeStampBasedOnDateString() {
        String dateString = "4/7/2015";

        Timestamp timeStamp = new Timestamp(new Date(dateString).getTime());
        System.out.println(timeStamp.toString());

        Timestamp systemTimestamp = new Timestamp((System.currentTimeMillis()));
        System.out.println(systemTimestamp);

        System.out.println(systemTimestamp.compareTo(timeStamp) > 0);

    }


    @Test
    public void generateOverDueNotices() throws Exception {
        ArrayList<OleLoanDocument> loanDocuments = new ArrayList<>();
        Map overdueMap = new HashMap();
        overdueMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, "Overdue");
        overdueMap.put(OLEConstants.LOAN_DOCUMENTS, loanDocuments);
        OverdueNoticesExecutor overdueNoticesExecutor = new OverdueNoticesExecutor(overdueMap);
        overdueNoticesExecutor.setBusinessObjectService(mockBusinessObjectService);
//        deliverNoticesExecutor.setParameterResolverInstance(mockParameterResolverInstance);
        OleLoanDocument loanDocument = new OleLoanDocument();
//        List<OLEDeliverNotice> oleDeliverNotices = deliverNoticesExecutor.generateOverDueNotices(loanDocument);
//        assertNotNull(oleDeliverNotices);
//        assertTrue(!oleDeliverNotices.isEmpty());
    }

    @Test
    public void generateAndSendOverdueNoticesToPatron() throws Exception {
        ArrayList<OleLoanDocument> loanDocuments = new ArrayList<>();
        OleLoanDocument oleLoanDocument = new OleLoanDocument();

        OlePatronDocument olePatron = new OlePatronDocument();
        olePatron.setBarcode("123125");
        EntityBo entity = new EntityBo();
        ArrayList<EntityNameBo> entityNameBos = new ArrayList<>();
        EntityNameBo entityNameBo = new EntityNameBo();
        entityNameBo.setFirstName("FirtName");
        entityNameBos.add(entityNameBo);
        entity.setNames(entityNameBos);
        ArrayList<EntityTypeContactInfoBo> entityTypeContactInfos = new ArrayList<>();
        entityTypeContactInfos.add(new EntityTypeContactInfoBo());
        entity.setEntityTypeContactInfos(entityTypeContactInfos);
        olePatron.setEntity(entity);

        oleLoanDocument.setOlePatron(olePatron);

        loanDocuments.add(oleLoanDocument);

        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        ExecutorService overDueNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        Map overdueMap = new HashMap();
        overdueMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, "Overdue");
        overdueMap.put(OLEConstants.LOAN_DOCUMENTS, loanDocuments);
        Runnable deliverOverDueNoticesExecutor = new MockOverdueNoticesExecutor(overdueMap);
        overDueNoticesExecutorService.execute(deliverOverDueNoticesExecutor);
    }

    @Test
    public void generateNotices() throws Exception {
        ArrayList<OleLoanDocument> loanDocuments = new ArrayList<>();
        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        oleLoanDocument.setPatronId("12");

        OleLoanDocument oleLoanDocument1 = new OleLoanDocument();
        oleLoanDocument1.setPatronId("12");


        OleLoanDocument oleLoanDocument2 = new OleLoanDocument();
        oleLoanDocument2.setPatronId("1234");

        OleLoanDocument oleLoanDocument3 = new OleLoanDocument();
        oleLoanDocument3.setPatronId("12345");

        loanDocuments.add(oleLoanDocument);
        loanDocuments.add(oleLoanDocument1);
        loanDocuments.add(oleLoanDocument2);
        loanDocuments.add(oleLoanDocument3);
        Map overdueMap = new HashMap();
        overdueMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, "Overdue");
        overdueMap.put(OLEConstants.LOAN_DOCUMENTS, loanDocuments);
        OverdueNoticesExecutor overdueNoticesExecutor = new MockOverdueNoticesExecutor(overdueMap);
        //deliverNoticesExecutor.buildNoticesForDeletion(loanDocuments);


    }

    class MockOverdueNoticesExecutor extends OverdueNoticesExecutor {

        MockOverdueNoticesExecutor(Map overdueMap) {
            super(overdueMap);
        }

        @Override
        public BusinessObjectService getBusinessObjectService() {
            return mockBusinessObjectService;
        }

        @Override
        protected String getNoticeType() {
            return OLEConstants.OVERDUE_NOTICE;
        }

        public void generateAndSendOverdueNoticesToPatron(List<OleLoanDocument> oleLoanDocuments) {
            for (Iterator<OleLoanDocument> iterator = oleLoanDocuments.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();

                System.out.println("Mail sent to: " + oleLoanDocument.getPatronId());
            }
        }
    }
}