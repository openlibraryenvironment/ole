package org.kuali.ole.deliver.drools;

import org.junit.Before;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.io.ResourceFactory;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 7/24/15.
 */
public class DroolsKieBaseTestCase {

    protected KieBase kieBase;
    protected KieSession kieSession;
    protected KieContainer kieContainer;

    protected KnowledgeBase knowledgeBase;
    @Mock
    protected OleCirculationDesk mockCirculationDesk;
    @Mock
    protected LoanPeriodUtil mockLoanPeriodUtil;
    @Mock
    protected OlePatronDocument mockOlePatronDocument;
    @Mock
    protected OleItemRecordForCirc mockItemRecordForCirc;
    @Mock
    protected ItemStatusRecord mockItemStatusRecord;
    @Mock
    protected ItemRecord mockItemRecord;
    @Mock
    protected OleLoanDocument mockLoanDocument;
    @Mock
    protected DroolsExchange mockDroolsExchange;
    @Mock
    protected NoticeInfo mockNoticeInfo;
    @Mock
    protected OleDeliverRequestBo mockOleDeliverRequestBo;
    @Mock
    protected ItemFineRate mockItemFineRate;
    @Mock
    protected DroolsResponse mockDroolsResponse;
    @Mock
    protected OleCirculationDesk getMockCirculationDesk;

    @Before
    public void setUp() throws  Exception {

        MockitoAnnotations.initMocks(this);

        KieServices kieServices = KieServices.Factory.get();
        List<Resource> resourceList = new ArrayList();

        KieFileSystem kfs = kieServices.newKieFileSystem();
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/general_checks/general-checks.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/general_checks/existing-loan-or-requests.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/checkout/2-H.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/checkout/STKS-MAX25.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/checkout/STKS-Q.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/renewals/its_renewal.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/checkin/in-transit-check-for-no-loan.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/checkin/in-transit-check.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/checkin/on-hold-check.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/checkin/staff-request-check.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/fines/short-term.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/ncip/lookupuser.drl"));
        resourceList.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/chicago/requests/place-request.drl"));

        for (Iterator<Resource> iterator = resourceList.iterator(); iterator.hasNext(); ) {
            Resource resource = iterator.next();
            kfs.write(resource);


        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();

        if( results.hasMessages( Message.Level.ERROR ) ){
            System.out.println( results.getMessages() );
            throw new IllegalStateException( "### errors ###" );
        }

        kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

        long startTime = System.currentTimeMillis();
        kieBase = kieContainer.getKieBase();
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken to initiallize KieBase: " + (endTime-startTime) + "ms");

        long startTime1 = System.currentTimeMillis();
        kieSession = kieContainer.newKieSession();
        long endTime1 = System.currentTimeMillis();

        System.out.println("Time taken to initiallize KieSession: " + (endTime1-startTime1) + "ms");
    }

}
