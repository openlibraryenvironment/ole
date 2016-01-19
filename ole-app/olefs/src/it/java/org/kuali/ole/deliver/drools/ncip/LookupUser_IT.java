package org.kuali.ole.deliver.drools.ncip;

import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenchulakshmig on 8/6/15.
 */
public class LookupUser_IT extends DroolsKieBaseTestCase {

    @Test
    public void testLookupUserNCIP() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(5, 5);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.getExpirationDate()).thenReturn(calendar.getTime());
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.getActivationDate()).thenReturn(new Date());
        Mockito.when(mockOlePatronDocument.getAllCharges()).thenReturn(new BigDecimal(10));
        Mockito.when(mockOlePatronDocument.getOverdueFineAmount()).thenReturn(new BigDecimal(10));
        Mockito.when(mockOlePatronDocument.getReplacementFineAmount()).thenReturn(new BigDecimal(0));

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);

        kieSession.getAgenda().getAgendaGroup("lookup-user-ncip").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron is blocked");
    }

    @Test
    public void testLookupUserSip2() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(5, 5);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.getExpirationDate()).thenReturn(calendar.getTime());
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.getActivationDate()).thenReturn(new Date());
        Mockito.when(mockOlePatronDocument.getAllCharges()).thenReturn(new BigDecimal(10));
        Mockito.when(mockOlePatronDocument.getOverdueFineAmount()).thenReturn(new BigDecimal(10));
        Mockito.when(mockOlePatronDocument.getReplacementFineAmount()).thenReturn(new BigDecimal(0));

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);

        kieSession.getAgenda().getAgendaGroup("lookup-user-sip2").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron is blocked");
    }

    @Test
    public void testLookupUserVufind() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(5, 5);
        Mockito.when(mockOlePatronDocument.isGeneralBlock()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.getExpirationDate()).thenReturn(calendar.getTime());
        Mockito.when(mockOlePatronDocument.isActiveIndicator()).thenReturn(true);
        Mockito.when(mockOlePatronDocument.getActivationDate()).thenReturn(new Date());
        Mockito.when(mockOlePatronDocument.getAllCharges()).thenReturn(new BigDecimal(10));
        Mockito.when(mockOlePatronDocument.getOverdueFineAmount()).thenReturn(new BigDecimal(10));
        Mockito.when(mockOlePatronDocument.getReplacementFineAmount()).thenReturn(new BigDecimal(0));

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);

        kieSession.getAgenda().getAgendaGroup("lookup-user-vufind").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Patron is blocked");
    }
}
