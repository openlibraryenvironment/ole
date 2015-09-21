package org.kuali.ole.deliver.drools.checkin;

import org.drools.core.common.InternalAgendaGroup;
import org.drools.core.reteoo.RuleTerminalNodeLeftTuple;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.kuali.ole.deliver.drools.CustomAgendaFilter;
import org.kuali.ole.deliver.drools.DroolsBaseTestCase;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

import java.util.List;

/**
 * Created by pvsubrah on 7/17/15.
 */

public class InTransitCheck_IT extends DroolsKieBaseTestCase {
    @Test
    public void checkInTransitForAvailable() throws Exception {

        Mockito.when(mockCirculationDesk.getShelvingLagTimeInt()).thenReturn(0);
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("LOANED");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isCheckinLocationSameAsHomeLocation()).thenReturn(true);

        kieSession.insert(mockCirculationDesk);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockDroolsResponse);

        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.atLeast(1)).setItemStatusToBeUpdatedTo("AVAILABLE");
    }

    @Test
    public void checkInTransitForRecentlyReturned() throws Exception {

        Mockito.when(mockCirculationDesk.getShelvingLagTimeInt()).thenReturn(2);
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("LOANED");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isCheckinLocationSameAsHomeLocation()).thenReturn(true);
        kieSession.insert(mockDroolsResponse);

        kieSession.insert(mockCirculationDesk);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.atLeast(1)).setItemStatusToBeUpdatedTo("RECENTLY-RETURNED");
    }

    @Test
    public void checkInTransitForInTransit() throws Exception {
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("LOANED");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isCheckinLocationSameAsHomeLocation()).thenReturn(false);
        kieSession.insert(mockDroolsResponse);

        kieSession.insert(mockCirculationDesk);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.atLeast(1)).setItemStatusToBeUpdatedTo("INTRANSIT");
    }
}
