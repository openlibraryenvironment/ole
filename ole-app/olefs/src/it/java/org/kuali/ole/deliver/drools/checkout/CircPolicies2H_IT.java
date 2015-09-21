package org.kuali.ole.deliver.drools.checkout;

import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 6/17/15.
 */
public class CircPolicies2H_IT extends DroolsKieBaseTestCase {

    @Test
    public void twoHourLoanForMaxLoanedItemsLessThanThree() throws Exception {
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");

        Mockito.when(mockItemRecordForCirc.getItemLibraryLocation()).thenReturn("JRL");
        Mockito.when(mockItemRecordForCirc.getItemLocation()).thenReturn("Gen");
        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("res2");
        Mockito.when(mockOlePatronDocument.getLoanedItemsCountByItemType("res2")).thenReturn(2);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("checkout validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockLoanDocument, Mockito.times(1)).loanPeriod("2-H", null);
        Mockito.verify(mockLoanDocument, Mockito.times(1)).setCirculationPolicyId("Check out Circ Policy Set 2-H");
        Mockito.verify(mockLoanDocument, Mockito.times(0)).setErrorMessage("Patron has more than 3 items of 2-H reserve loan items type checked out");
    }

    @Test
    public void twoHourLoanForMaxLoanedItemsGreaterThanThree() throws Exception {
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");

        Mockito.when(mockItemRecordForCirc.getItemLibraryLocation()).thenReturn("JRL");
        Mockito.when(mockItemRecordForCirc.getItemLocation()).thenReturn("Gen");
        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("res2");
        Mockito.when(mockOlePatronDocument.getLoanedItemsCountByItemType("res2")).thenReturn(4);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("checkout validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockLoanDocument, Mockito.times(1)).loanPeriod("2-H", null);
        Mockito.verify(mockLoanDocument, Mockito.times(1)).setCirculationPolicyId("Check out Circ Policy Set 2-H");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron has 2 items of 2-H reserve loan items type checked out");
    }
}
