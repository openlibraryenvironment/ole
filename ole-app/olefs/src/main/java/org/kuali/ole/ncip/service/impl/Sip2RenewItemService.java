package org.kuali.ole.ncip.service.impl;

import org.kuali.asr.handler.RenewItemResponseHandler;
import org.kuali.ole.bo.OLERenewItemList;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.utility.OleStopWatch;

import java.util.ArrayList;

/**
 * Created by pvsubrah on 7/2/15.
 */
public class Sip2RenewItemService extends RenewItemsService {

    @Override
    public DroolsResponse validatePatron(OlePatronDocument olePatronDocument) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ArrayList<Object> facts = new ArrayList<>();
        facts.add(olePatronDocument);
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);
        new CircUtilController().fireRules(facts, null, "lookup-user-sip2");
        return droolsResponse;
    }

    @Override
    public String prepareResponse(OLERenewItemList oleRenewItemList) {
        switch (responseFormatType) {
            case ("XML"):
                response = ((RenewItemResponseHandler) getResponseHandler()).marshalObjectToSIP2Xml(oleRenewItemList);
                break;
            case ("JSON"):
                response = getResponseHandler().marshalObjectToJson(oleRenewItemList);
                break;
        }

        return response;
    }
}
