package org.kuali.ole.deliver.drools.checkout;

import org.junit.Test;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 8/24/15.
 */
public class ExistingRequests_IT extends DroolsKieBaseTestCase {

    @Test
    public void requestByAnotherPatron() throws Exception {
        Mockito.when(mockLoanDocument.getLoanId()).thenReturn("22");

        Mockito.when(mockOlePatronDocument.getBarcode()).thenReturn("323");

        OlePatronDocument requestPatron = new OlePatronDocument();
        requestPatron.setBarcode("452");
        Mockito.when(mockOleDeliverRequestBo.getOlePatron()).thenReturn(requestPatron);

        Mockito.when(mockItemStatusRecord.getCode()).thenReturn("AVAILABLE");
        Mockito.when(mockItemRecordForCirc.getItemStatusRecord()).thenReturn(mockItemStatusRecord);



        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockOleDeliverRequestBo);
        kieSession.insert(mockLoanDocument);


        kieSession.getAgenda().getAgendaGroup("request-or-existing-loan-checks").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Item has an existing request by the patron who is different than the current borrower.");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addOverridePermissions(DroolsConstants.GENERAL_BLOCK_PERMISSION);
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessageCode(DroolsConstants.REQUEST_EXITS_FOR_LOANED_ITEM);
    }


    @Test
    public void itemLoanedBySomeoneElse() throws Exception {
        Mockito.when(mockOlePatronDocument.getOlePatronId()).thenReturn("123");
        OlePatronDocument loanedPatron = new OlePatronDocument();
        loanedPatron.setOlePatronId("321");
        Mockito.when(mockLoanDocument.getOlePatron()).thenReturn(loanedPatron);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);


        kieSession.getAgenda().getAgendaGroup("request-or-existing-loan-checks").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Item currently loaned by some one else.");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addOverridePermissions(DroolsConstants.GENERAL_BLOCK_PERMISSION);
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessageCode(DroolsConstants.LOANED_BY_DIFFERENT_PATRON);
    }

    @Test
    public void itemLoanedBySamePatron() throws Exception {
        Mockito.when(mockOlePatronDocument.getOlePatronId()).thenReturn("123");
        Mockito.when(mockLoanDocument.getOlePatron()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockItemRecordForCirc.getOleDeliverRequestBo()).thenReturn(null);

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockOlePatronDocument);
        kieSession.insert(mockLoanDocument);
        kieSession.insert(mockItemRecordForCirc);


        kieSession.getAgenda().getAgendaGroup("request-or-existing-loan-checks").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessage("Item currently loaned by same patron.");
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addOverridePermissions(DroolsConstants.GENERAL_BLOCK_PERMISSION);
        Mockito.verify(mockDroolsResponse, Mockito.times(1)).addErrorMessageCode(DroolsConstants.CHECKED_OUT_BY_SAME_PATRON);
    }

}
