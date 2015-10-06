package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.utility.OleStopWatch;

import java.util.ArrayList;

/**
 * Created by chenchulakshmig on 10/5/15.
 */
public class VuFindRenewItemService extends NonSip2RenewItemService{

    @Override
    public DroolsResponse validatePatron(OlePatronDocument olePatronDocument) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ArrayList<Object> facts = new ArrayList<>();
        facts.add(olePatronDocument);
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);
        new CircUtilController().fireRules(facts, null, "lookup-user-vufind");
        return droolsResponse;
    }
}
