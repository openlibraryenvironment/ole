package org.kuali.ole.ncip.service.impl;

import org.kuali.asr.handler.RenewItemResponseHandler;
import org.kuali.ole.ncip.bo.OLERenewItemList;

/**
 * Created by pvsubrah on 7/2/15.
 */
public class Sip2RenewItemService extends RenewItemsService {
    @Override
    public String prepareResponse(OLERenewItemList oleRenewItemList) {
        switch (responseFormatType){
            case("XML"):
                response =  ((RenewItemResponseHandler)getResponseHandler()).marshalObjectToSIP2Xml(oleRenewItemList);
                break;
            case("JSON"):
                response = getResponseHandler().marshalObjectToJson(oleRenewItemList);
                break;
        }

        return response;
    }
}
