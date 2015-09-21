package org.kuali.ole.deliver.drools.renew;

import org.junit.Test;
import org.kuali.ole.deliver.drools.DroolsBaseTestCase;
import org.kuali.ole.deliver.drools.DroolsKieBaseTestCase;
import org.mockito.Mockito;

/**
 * Created by pvsubrah on 6/24/15.
 */
public class RenewalPolicies_IT extends DroolsKieBaseTestCase {

    @Test
    public void renewITS() throws Exception {
        Mockito.when(mockItemRecordForCirc.getItemType()).thenReturn("its2adp");

        kieSession.insert(mockDroolsResponse);
        kieSession.insert(mockItemRecordForCirc);

        kieSession.getAgenda().getAgendaGroup("renewal validation").setFocus();
        kieSession.fireAllRules();

        Mockito.verify(mockDroolsResponse, Mockito.times(1)).
                addErrorMessage("Reserve and short-term loans may not be renewed");
    }
}
