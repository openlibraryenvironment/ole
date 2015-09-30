package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.bo.OLECheckOutItem;

/**
 * Created by chenchulakshmig on 8/25/15.
 */
public class NonSip2CheckoutItemService extends CheckoutItemService {

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
}
