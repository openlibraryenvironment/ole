package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.utility.OleStopWatch;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by chenchulakshmig on 8/25/15.
 */
public class NonSip2CheckoutItemServiceImpl extends CheckoutItemServiceImpl {

    @Override
    public String prepareResponse() {
        switch (responseFormatType) {
            case ("XML"):
                response = getResponseHandler().marshalObjectToXml(getOleCheckOutItem());
                break;
            case ("JSON"):
                response = getResponseHandler().marshalObjectToJson(getOleCheckOutItem());
                break;
        }

        return response;
    }

    @Override
    public String getOperatorId(String operatorId) {
        return operatorId;
    }

    @Override
    protected String preProcess(Map checkoutParameters) {
        return null;
    }

    @Override
    protected String fireRules() {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ArrayList<Object> facts = new ArrayList<>();
        facts.add(getOlePatronDocument());
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);
        new CircUtilController().fireRules(facts, null, "general-checks");
        return droolsResponse.getErrorMessage().getErrorMessage();
    }
}
