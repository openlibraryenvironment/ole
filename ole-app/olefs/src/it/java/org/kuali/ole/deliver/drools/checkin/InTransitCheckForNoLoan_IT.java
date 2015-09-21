package org.kuali.ole.deliver.drools.checkin;

import org.drools.core.common.InternalAgendaGroup;
import org.drools.core.reteoo.RuleTerminalNodeLeftTuple;
import org.junit.Test;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.kuali.ole.deliver.drools.CustomAgendaFilterForAgendaGroup;
import org.kuali.ole.deliver.drools.DroolsBaseTestCase;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 7/23/15.
 */
public class InTransitCheckForNoLoan_IT extends DroolsKieBaseTestCase {

    @Test
    public void inTransitCheckForItemWithNoLoan() throws Exception {
        Mockito.when(mockCirculationDesk.getShelvingLagTimeInt()).thenReturn(2);
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("INTRANSIT");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isCheckinLocationSameAsHomeLocation()).thenReturn(true);

        kieSession.insert(mockCirculationDesk);
        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockDroolsResponse);

        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-no-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.times(1)).setItemStatusToBeUpdatedTo("RECENTLY-RETURNED");
        Mockito.verify(mockItemRecordForCirc, Mockito.times(1)).updateInHouseCheckinCount();
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).setRuleMatched(true);

    }

}
