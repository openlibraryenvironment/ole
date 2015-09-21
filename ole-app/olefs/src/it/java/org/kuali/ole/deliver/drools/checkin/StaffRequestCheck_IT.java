package org.kuali.ole.deliver.drools.checkin;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsBaseTestCase;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 7/20/15.
 */

public class StaffRequestCheck_IT extends DroolsKieBaseTestCase {


    @Test
    public void staffRequestRecentlyReturned() throws Exception {

        Mockito.when(mockCirculationDesk.getShelvingLagTimeInt()).thenReturn(2);
        Mockito.when(mockLoanDocument.getOleCirculationDesk()).thenReturn(mockCirculationDesk);
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("INTRANSIT-PER-STAFF-REQUEST");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isCheckinLocationSameAsHomeLocation()).thenReturn(true);

        kieSession.insert(mockCirculationDesk);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.times(1)).setItemStatusToBeUpdatedTo("RECENTLY-RETURNED");
    }


    @Test
    public void staffRequestAvailable() throws Exception {

        Mockito.when(mockCirculationDesk.getShelvingLagTimeInt()).thenReturn(0);
        Mockito.when(mockLoanDocument.getOleCirculationDesk()).thenReturn(mockCirculationDesk);
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("INTRANSIT-PER-STAFF-REQUEST");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isCheckinLocationSameAsHomeLocation()).thenReturn(true);

        kieSession.insert(mockCirculationDesk);
        kieSession.insert(mockItemRecordForCirc);


        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.atLeast(1)).setItemStatusToBeUpdatedTo("AVAILABLE");
    }

}
