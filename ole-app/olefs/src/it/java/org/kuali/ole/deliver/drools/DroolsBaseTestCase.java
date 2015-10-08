package org.kuali.ole.deliver.drools;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Before;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.ItemFineRate;
import org.kuali.ole.deliver.util.NoticeInfo;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pvsubrah on 6/6/15.
 */
public class DroolsBaseTestCase {
    protected KnowledgeBase knowledgeBase;
    @Mock
    protected OleCirculationDesk mockCirculationDesk;

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
    protected ErrorMessage mockErrorMessage;
    @Mock
    protected NoticeInfo mockNoticeInfo;
    @Mock
    protected OleDeliverRequestBo mockOleDeliverRequestBo;
    @Mock
    protected ItemFineRate mockItemFineRate;


    protected StatefulKnowledgeSession session;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        knowledgeBase = populateKnowledgeBase();

        // initialize a session for usage
        session = knowledgeBase.newStatefulKnowledgeSession();
        session.addEventListener(new CustomAgendaEventListener());

        assertNotNull(session);
    }

    protected KnowledgeBase populateKnowledgeBase() {
        // seed a builder with our rules file from classpath
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/general/general-checks.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/checkout/2-H.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/checkout/STKS-MAX25.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/checkout/STKS-Q.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/renewal/its_renewal.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/checkin/in-transit-check-for-no-loan.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/checkin/in-transit-check.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/checkin/on-hold-check.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/checkin/staff-request-check.drl"),
                ResourceType.DRL);
        builder.add(ResourceFactory.newClassPathResource("org/kuali/ole/deliver/rules/fines/short-term.drl"),
                ResourceType.DRL);
        if (builder.hasErrors()) {
            throw new RuntimeException(builder.getErrors().toString());
        }

        // create a knowledgeBase from our builder packages
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());

        return knowledgeBase;
    }


    @After
    public void tearDown() {
        Collection<AgendaEventListener> agendaEventListeners = session
                .getAgendaEventListeners();
        CustomAgendaEventListener agendaEventListener = (CustomAgendaEventListener) (agendaEventListeners.toArray
                ()[0]);


        List<String> rulesFiredList = agendaEventListener.getRulesFired();
        if (rulesFiredList.size() == 0) {
            System.out.println("No rules fired");
        } else {
            System.out.println("Rules Fired: ");
        }
        for (Iterator<String> stringIterator = rulesFiredList.iterator(); stringIterator.hasNext(); ) {
            String rulesFired = stringIterator.next();
            Log.info(rulesFired);
            System.out.println(rulesFired);
        }

        System.out.println("=====================================");
        session.dispose();
    }
}
