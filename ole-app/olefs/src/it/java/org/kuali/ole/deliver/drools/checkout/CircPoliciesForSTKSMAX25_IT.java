package org.kuali.ole.deliver.drools.checkout;

import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 6/6/15.
 */
public class CircPoliciesForSTKSMAX25_IT extends DroolsKieBaseTestCase {

    @Test
    public void validCheckedOutItems() throws Exception {
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QALUM25");
        Mockito.when(mockOlePatronDocument.getTotalLoanedItemsCount()).thenReturn(3);

        Mockito.when(mockItemRecordForCirc.getItemLibraryLocation()).thenReturn("DLL");
        Mockito.when(mockItemRecordForCirc.getItemLocation()).thenReturn("Gen");
        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("stks");

        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("checkout validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockLoanDocument, Mockito.atLeast(1)).loanPeriod(OLEConstants.FIXED_DUE_DATE, "7-D");
        Mockito.verify(mockLoanDocument, Mockito.atLeast(1)).setCirculationPolicyId("Check out Circ Policy Set STKS/MAX25");

    }

    @Test
    public void policySetSTKMAX25() throws Exception {
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QALUM25");

        Mockito.when(mockItemRecordForCirc.getItemLibraryLocation()).thenReturn("DLL");
        Mockito.when(mockItemRecordForCirc.getItemLocation()).thenReturn("Gen");
        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("stks");
        Mockito.when(mockOlePatronDocument.getTotalLoanedItemsCount()).thenReturn(25);

        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockDroolsResponse);

        kieSession.getAgenda().getAgendaGroup("checkout validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockLoanDocument, Mockito.atLeast(1)).loanPeriod(OLEConstants.FIXED_DUE_DATE, "7-D");
        Mockito.verify(mockDroolsResponse).addErrorMessage("Patron has 25 or more items checked out");
        Mockito.verify(mockLoanDocument, Mockito.atLeast(1)).setCirculationPolicyId("Check out Circ Policy Set STKS/MAX25");

    }
}
