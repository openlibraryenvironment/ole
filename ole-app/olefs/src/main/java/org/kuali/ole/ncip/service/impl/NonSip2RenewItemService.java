package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.bo.OLERenewItemList;

/**
 * Created by pvsubrah on 7/2/15.
 */
public class NonSip2RenewItemService extends RenewItemsService {
    @Override
    public String prepareResponse(OLERenewItemList oleRenewItemList) {
        switch (responseFormatType){
            case("XML"):
                response =  getResponseHandler().marshalObjectToXml(oleRenewItemList);
                break;
            case("JSON"):
                response = getResponseHandler().marshalObjectToJson(oleRenewItemList);
                break;
        }

        return response;
    }
}
