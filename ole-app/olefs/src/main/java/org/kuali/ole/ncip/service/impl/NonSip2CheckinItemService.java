package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.bo.OLECheckInItem;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public class NonSip2CheckinItemService extends CheckinItemService {

    @Override
    public String prepareResponse() {
        switch (responseFormatType) {
            case ("XML"):
                response = getResponseHandler().marshalObjectToXml(getOleCheckInItem());
                break;
            case ("JSON"):
                response = getResponseHandler().marshalObjectToJson(getOleCheckInItem());
                break;
        }

        return response;
    }

    @Override
    public String getOperatorId(String operatorId) {
        return operatorId;
    }
}
