package org.kuali.ole.deliver.drools.checkout;

import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 6/17/15.
 */
public class StksQ_IT extends DroolsKieBaseTestCase {
    @Test
    public void checkoutValidationSTKQDRL() throws Exception {
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QGRAD");
        Mockito.when(mockItemRecordForCirc.getItemLibraryLocation()).thenReturn("JRL");
        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("stks");
        Mockito.when(mockItemRecordForCirc.getItemLocation()).thenReturn("gen");


        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("checkout validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockLoanDocument, Mockito.times(1)).setCirculationPolicyId("Check out Circ Policy Set STKS/Q");
        Mockito.verify(mockLoanDocument, Mockito.times(1)).loanPeriod(OLEConstants.FIXED_DUE_DATE, "7-D");
    }
}
