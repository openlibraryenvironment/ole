package org.kuali.asr.handler;


import org.kuali.ole.ncip.bo.OLELookupUser;
import org.kuali.ole.ncip.converter.OLELookupUserConverter;

/**
 * Created by chenchulakshmig on 9/22/15.
 */
public class LookupUserResponseHandler extends ResponseHandler {

    private OLELookupUserConverter oleLookupUserConverter;

    public String marshalObjectToSIP2Xml(Object object) {
        String checkOutItemXml = getOleLookupUserConverter().generateLookupUserResponseXmlForSip2((OLELookupUser) object);
        return checkOutItemXml;
    }

    @Override
    public String marshalObjectToXml(Object object) {
        String checkOutItemXml = getOleLookupUserConverter().generateLookupUserResponseXml((OLELookupUser) object);
        return checkOutItemXml;
    }

    public OLELookupUserConverter getOleLookupUserConverter() {
        if (null == oleLookupUserConverter) {
            oleLookupUserConverter = new OLELookupUserConverter();
        }
        return oleLookupUserConverter;
    }

    public void setOleLookupUserConverter(OLELookupUserConverter oleLookupUserConverter) {
        this.oleLookupUserConverter = oleLookupUserConverter;
    }
}
