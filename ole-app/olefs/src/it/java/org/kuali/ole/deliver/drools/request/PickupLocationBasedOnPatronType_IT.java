package org.kuali.ole.deliver.drools.request;

import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by hemalathas on 12/24/15.
 */
public class PickupLocationBasedOnPatronType_IT extends DroolsKieBaseTestCase {

    @Test
    public void testPickUpLocations(){
        Mockito.when(mockOlePatronDocument.getBorrowerTypeCode()).thenReturn("UGRAD");
        Mockito.when(mockOlePlaceRequestForm.getOlePatronDocument()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockOlePlaceRequestForm.getItemType()).thenReturn("BOOK");
        Mockito.when(mockOlePlaceRequestForm.getItemLocation()).thenReturn("B-EDUC/BED-STACKS");
        Mockito.when(mockDroolsResponse.getDroolsExchange()).thenReturn(mockDroolsExchange);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockOlePlaceRequestForm);
        kieSession.insert(mockDroolsResponse);
        kieSession.getAgenda().getAgendaGroup("pickup-location").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockOlePlaceRequestForm, Mockito.times(1)).
                setPickUpLocation("BL_EDUC,BL_HPER");


    }

}
