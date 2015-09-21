package org.kuali.ole.deliver.drools.fines;

import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsBaseTestCase;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 7/21/15.
 */
public class Fines_IT extends DroolsKieBaseTestCase {
    @Test
    public void recalledItemsHourlyFines() throws Exception {

        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("Recall/Delivery Request");
        Mockito.when(mockLoanDocument.getOleDeliverRequestBo()).thenReturn(mockOleDeliverRequestBo);

        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("res2");
        Mockito.when(mockItemRecordForCirc.getItemLibraryLocation()).thenReturn("JRL");

        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");

        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockItemFineRate);

        kieSession.getAgenda().getAgendaGroup("fine validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemFineRate, Mockito.times(1)).setFineRate(5.00);
        Mockito.verify(mockItemFineRate, Mockito.times(1)).setMaxFine(100.00);
        Mockito.verify(mockItemFineRate, Mockito.times(1)).setInterval("D");
    }

    @Test
    public void itemsHourlyFines() throws Exception {

        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("res2");
        Mockito.when(mockItemRecordForCirc.getItemLibraryLocation()).thenReturn("JRL");

        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");

        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockItemFineRate);

        kieSession.getAgenda().getAgendaGroup("fine validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemFineRate, Mockito.times(1)).setFineRate(3.00);
        Mockito.verify(mockItemFineRate, Mockito.times(1)).setMaxFine(100.00);
        Mockito.verify(mockItemFineRate, Mockito.times(1)).setInterval("D");
    }
}
