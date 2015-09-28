package org.kuali.asr.handler;

import org.kuali.ole.bo.OLECheckOutItem;
import org.kuali.ole.converter.OLECheckOutItemConverter;

/**
 * Created by chenchulakshmig on 8/25/15.
 */
public class CheckoutItemResponseHandler extends ResponseHandler {

    private OLECheckOutItemConverter oleCheckOutItemConverter;

    public String marshalObjectToSIP2Xml(Object object) {
        String checkOutItemXml = getOleCheckOutItemConverter().generateCheckOutItemXmlForSIP2((OLECheckOutItem) object);
        return checkOutItemXml;
    }

    @Override
    public String marshalObjectToXml(Object object) {
        String checkOutItemXml = getOleCheckOutItemConverter().generateCheckOutItemXml((OLECheckOutItem) object);
        return checkOutItemXml;
    }

    public OLECheckOutItemConverter getOleCheckOutItemConverter() {
        if (null == oleCheckOutItemConverter){
            oleCheckOutItemConverter = new OLECheckOutItemConverter();
        }
        return oleCheckOutItemConverter;
    }

    public void setOleCheckOutItemConverter(OLECheckOutItemConverter oleCheckOutItemConverter) {
        this.oleCheckOutItemConverter = oleCheckOutItemConverter;
    }
}
