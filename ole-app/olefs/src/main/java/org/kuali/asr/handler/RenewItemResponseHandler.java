package org.kuali.asr.handler;

import org.kuali.ole.ncip.bo.OLERenewItem;
import org.kuali.ole.ncip.converter.OLERenewItemConverter;

/**
 * Created by pvsubrah on 6/26/15.
 */
public class RenewItemResponseHandler extends ResponseHandler {

    private OLERenewItemConverter oleRenewItemConverter;

    @Override
    public String marshalObjectToXml(Object object) {
        String renewItemXml = getOleRenewItemConverter().generateRenewItemXml((OLERenewItem) object);
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
