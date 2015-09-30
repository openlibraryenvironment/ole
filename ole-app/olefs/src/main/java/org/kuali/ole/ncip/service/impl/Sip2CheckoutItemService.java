package org.kuali.ole.ncip.service.impl;

import org.kuali.asr.handler.CheckoutItemResponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.bo.OLECheckOutItem;
import org.kuali.ole.deliver.service.ParameterValueResolver;

/**
 * Created by chenchulakshmig on 8/24/15.
 */
public class Sip2CheckoutItemService extends CheckoutItemService {

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
}
