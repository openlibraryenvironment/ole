package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.utility.OleStopWatch;

import java.util.ArrayList;

/**
 * Created by chenchulakshmig on 10/1/15.
 */
public class VuFindCheckoutItemServiceImplImpl extends NonSip2CheckoutItemServiceImpl {

    @Override
    protected String fireRules() {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ArrayList<Object> facts = new ArrayList<>();
        facts.add(getOlePatronDocument());
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);
        new CircUtilController().fireRules(facts, null, "lookup-user-vufind");
        return droolsResponse.getErrorMessage().getErrorMessage();
    }
}
