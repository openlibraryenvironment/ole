package org.kuali.ole.deliver.drools.ncip;

import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenchulakshmig on 8/6/15.
 */
public class LookupUser_IT extends DroolsKieBaseTestCase {

    @Test
    public void testLookupUser() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(5, 5);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(false);
        Mockito.when(mockOlePatronDocument.getExpirationDate()).thenReturn(calendar.getTime());
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.getActivationDate()).thenReturn(new Date());
        Mockito.when(mockOlePatronDocument.getAllCharges()).thenReturn(10);
        Mockito.when(mockOlePatronDocument.getOverdueFineAmount()).thenReturn(10);
        Mockito.when(mockOlePatronDocument.getReplacementFineAmount()).thenReturn(0);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);

        kieSession.getAgenda().getAgendaGroup("lookup-user").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(0)).addErrorMessage("Patron is blocked");
    }
}
