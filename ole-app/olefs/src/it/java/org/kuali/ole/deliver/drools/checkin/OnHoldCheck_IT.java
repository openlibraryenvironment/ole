package org.kuali.ole.deliver.drools.checkin;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsBaseTestCase;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 7/20/15.
 */

public class OnHoldCheck_IT extends DroolsKieBaseTestCase {

    @Test
    public void onHold() throws Exception {
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("Recall/Hold Request");
        Mockito.when(mockItemRecordForCirc.getOleDeliverRequestBo()).thenReturn(mockOleDeliverRequestBo);
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("LOANED");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isItemPickupLocationSameAsOperatorCircLoc()).thenReturn(false);

        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockDroolsResponse);

        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.times(1)).setItemStatusToBeUpdatedTo("INTRANSIT-FOR-HOLD");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).setRuleMatched(true);
        Mockito.verify(mockDroolsExchange, Mockito.times(1)).addToContext(DroolsConstants.PRINT_SLIP_FLAG, true);
    }

    @Test
    public void onHoldForPickup() throws Exception {
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("Recall/Hold Request");
        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("LOANED");
        Mockito.when(mockItemRecordForCirc.getOleDeliverRequestBo()).thenReturn(mockOleDeliverRequestBo);
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);
        Mockito.when(mockItemRecordForCirc.isItemPickupLocationSameAsOperatorCircLoc()).thenReturn(true);


        kieSession.insert(mockItemRecordForCirc);
        kieSession.insert(mockDroolsResponse);

        kieSession.getAgenda().getAgendaGroup("checkin-validation-for-loan").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockItemRecordForCirc, Mockito.times(1)).setItemStatusToBeUpdatedTo("ONHOLD");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).setRuleMatched(true);
    }


}
