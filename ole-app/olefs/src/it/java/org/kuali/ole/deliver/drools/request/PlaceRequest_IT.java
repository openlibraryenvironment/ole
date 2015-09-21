package org.kuali.ole.deliver.drools.request;

import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Created by maheswarang on 8/19/15.
 */
public class PlaceRequest_IT extends DroolsKieBaseTestCase {
    @Mock
    private DroolsExchange mockDroolsExchange;
    @Test
    public void testPlaceRecallRequestRule(){
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");
        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("Recall/Delivery Request");
        Mockito.when(mockOleDeliverRequestBo.getItemType()).thenReturn("stks");
        Mockito.when(mockOleDeliverRequestBo.getItemLibrary()).thenReturn("JRL");
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockOleDeliverRequestBo);
        kieSession.insert(mockNoticeInfo);
        kieSession.insert(mockDroolsResponse);
        kieSession.getAgenda().getAgendaGroup("place-request").setFocus();
        kieSession.fireAllRules();
        Mockito.verify(mockDroolsResponse.getDroolsExchange(), Mockito.times(1)).
                addToContext("recallLoanPeriod", "7-D");
    }


    @Test
    public void testPlaceHoldRequestRule(){
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");
        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("Hold/Hold Request");
        Mockito.when(mockOleDeliverRequestBo.getItemType()).thenReturn("stks");
        Mockito.when(mockOleDeliverRequestBo.getItemLibrary()).thenReturn("JRL");

        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockOleDeliverRequestBo);
        kieSession.insert(mockNoticeInfo);
        kieSession.insert(mockDroolsResponse);
        kieSession.getAgenda().getAgendaGroup("place-request").setFocus();
        kieSession.fireAllRules();
        Mockito.verify(mockDroolsResponse.getDroolsExchange(), Mockito.times(1)).
                addToContext("requestExpirationDays", 45);

}



    @Test
    public void testPlaceCopyRequestRule(){
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");
        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("Copy Request");
        Mockito.when(mockOleDeliverRequestBo.getItemType()).thenReturn("stks");
        Mockito.when(mockOleDeliverRequestBo.getItemLibrary()).thenReturn("JRL");
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockOleDeliverRequestBo);
        kieSession.insert(mockNoticeInfo);
        kieSession.insert(mockDroolsResponse);
        kieSession.getAgenda().getAgendaGroup("place-request").setFocus();
        kieSession.fireAllRules();
        Mockito.verify(mockDroolsResponse.getDroolsExchange(), Mockito.times(1)).
                addToContext("requestExpirationDays", 45);

    }



    @Test
    public void testPlacePageRequestRule(){
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");
        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("Page/Delivery Request");
        Mockito.when(mockOleDeliverRequestBo.getItemType()).thenReturn("stks");
        Mockito.when(mockOleDeliverRequestBo.getItemLibrary()).thenReturn("JRL");
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockOleDeliverRequestBo);
        kieSession.insert(mockNoticeInfo);
        kieSession.insert(mockDroolsResponse);
        kieSession.getAgenda().getAgendaGroup("place-request").setFocus();
        kieSession.fireAllRules();
        Mockito.verify(mockDroolsResponse.getDroolsExchange(), Mockito.times(1)).
                addToContext("requestExpirationDays", 45);

    }

    @Test
    public void testPlaceASRRequestRule(){
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("QCOL");
        Mockito.when(mockOleDeliverRequestBo.getRequestTypeCode()).thenReturn("ASR Request");
        Mockito.when(mockOleDeliverRequestBo.getItemType()).thenReturn("stks");
        Mockito.when(mockOleDeliverRequestBo.getItemLibrary()).thenReturn("ASR");
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockOleDeliverRequestBo);
        kieSession.insert(mockNoticeInfo);
        kieSession.insert(mockDroolsResponse);
        kieSession.getAgenda().getAgendaGroup("place-request").setFocus();
        kieSession.fireAllRules();
        Mockito.verify(mockDroolsResponse.getDroolsExchange(), Mockito.times(1)).
                addToContext("requestExpirationDays", 45);

    }

}
