package org.kuali.ole.ncip.service.impl;

import org.kuali.asr.handler.CheckoutItemResponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.utility.OleStopWatch;

import java.util.ArrayList;

/**
 * Created by chenchulakshmig on 8/24/15.
 */
public class Sip2CheckoutItemServiceImpl extends CheckoutItemServiceImpl {

    @Override
    public String prepareResponse() {

        switch (responseFormatType) {
            case ("XML"):
                response = ((CheckoutItemResponseHandler) getResponseHandler()).marshalObjectToSIP2Xml(getOleCheckOutItem());
                break;
            case ("JSON"):
                response = getResponseHandler().marshalObjectToJson(getOleCheckOutItem());
                break;
        }

        return response;
    }

    @Override
    public String getOperatorId(String operatorId) {
        return ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, operatorId);
    }

    @Override
    protected String fireRules() {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ArrayList<Object> facts = new ArrayList<>();
        facts.add(getOlePatronDocument());
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);
        new CircUtilController().fireRules(facts, null, "lookup-user-sip2");
        return droolsResponse.getErrorMessage().getErrorMessage();
    }
}
