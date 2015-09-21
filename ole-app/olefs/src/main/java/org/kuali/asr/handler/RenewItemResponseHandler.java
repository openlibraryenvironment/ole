package org.kuali.asr.handler;

import org.kuali.ole.ncip.bo.OLERenewItemList;
import org.kuali.ole.ncip.converter.OLERenewItemConverter;

/**
 * Created by pvsubrah on 6/26/15.
 */
public class RenewItemResponseHandler extends ResponseHandler {

    private OLERenewItemConverter oleRenewItemConverter;

    public String marshalObjectToSIP2Xml(Object object) {
        String renewItemXml = getOleRenewItemConverter().generateRenewItemListXmlForSip2((OLERenewItemList) object);
        return renewItemXml;
    }

    @Override
    public String marshalObjectToXml(Object object) {
        String renewItemXml = getOleRenewItemConverter().generateRenewItemListXml((OLERenewItemList) object);
        return renewItemXml;
    }

    @Override
    public String marshalObjectToJson(Object object) {
        return super.marshalObjectToJson(object);
    }

    private OLERenewItemConverter getOleRenewItemConverter() {
        if (null == oleRenewItemConverter) {
            oleRenewItemConverter = new OLERenewItemConverter();
        }
        return oleRenewItemConverter;
    }
}
