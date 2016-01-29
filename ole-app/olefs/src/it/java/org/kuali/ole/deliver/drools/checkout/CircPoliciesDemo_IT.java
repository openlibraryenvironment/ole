package org.kuali.ole.deliver.drools.checkout;

import org.junit.Test;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kuali.ole.deliver.drools.DroolsKieDemoBaseTestCase;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by pvsubrah on 3/23/15.
 */
public class CircPoliciesDemo_IT extends DroolsKieDemoBaseTestCase {

    @Test
    public void fetchRuleSetFromDrools() throws Exception {
        for (Iterator iterator = kieContainer.getKieBase().getKiePackages().iterator(); iterator.hasNext(); ) {
            KiePackage kiePackage = (KiePackage) iterator.next();
            Collection<Rule> rules = kiePackage.getRules();
            for (Iterator<Rule> ruleIterator = rules.iterator(); ruleIterator.hasNext(); ) {
                Rule rule = ruleIterator.next();
                System.out.println(rule.getName());
            }
        }
    }


    @Test
    public void generalChecks() throws Exception {
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(Boolean.FALSE);
        Mockito.when(mockOlePatronDocument.isAddressVerified()).thenReturn(Boolean.FALSE);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(Boolean.TRUE);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockNoticeInfo);


        kieSession.getAgenda().getAgendaGroup("general-checks").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron is Inactive");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron is blocked");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Address is not verified");
    }

    @Test
    public void generalChecksForOverDueDays() throws Exception {
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(Boolean.TRUE);
        Mockito.when(mockOlePatronDocument.isAddressVerified()).thenReturn(Boolean.TRUE);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(Boolean.FALSE);

        Mockito.when(mockOlePatronDocument.isOverDueDays(3000)).thenReturn(true);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockNoticeInfo);


        kieSession.getAgenda().getAgendaGroup("general-checks").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron is Inactive");
        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron is blocked");
        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Address is not verified");
        Mockito.verify(mockDroolsResponse, Mockito.atLeast(1)).addErrorMessage("Patron Has Atleast One Item which is over the allowed due limit");
    }

    @Test
    public void generalChecksForRecallAndOverDueDays() throws Exception {
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(Boolean.TRUE);
        Mockito.when(mockOlePatronDocument.isAddressVerified()).thenReturn(Boolean.TRUE);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(Boolean.FALSE);

        Mockito.when(mockOlePatronDocument.isOverDueDays(3000)).thenReturn(true);
        Mockito.when(mockOlePatronDocument.isRecalledAndOverDue(3)).thenReturn(true);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockNoticeInfo);


        kieSession.getAgenda().getAgendaGroup("general-checks").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron is Inactive");
        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron is blocked");
        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Address is not verified");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron Has Atleast One Item which has been recalled and is overdue");
    }


    @Test
    public void generalChecksAndCharges() throws Exception {
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.isAddressVerified()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(false);
        Mockito.when(mockOlePatronDocument.getReplacementFineAmount()).thenReturn(new BigDecimal(250));
        Mockito.when(mockOlePatronDocument.getAllCharges()).thenReturn(new BigDecimal(50));
        Mockito.when(mockOlePatronDocument.getOverdueFineAmount()).thenReturn(new BigDecimal(50));

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockNoticeInfo);


        kieSession.getAgenda().getAgendaGroup("general-checks").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron is Inactive");
        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron is blocked");
        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Address is not verified");
        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron Has Atleast One Item which has been recalled and is overdue");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron has replacement fees of $150 or more");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron's overall charges are $50 or more");
    }
}
