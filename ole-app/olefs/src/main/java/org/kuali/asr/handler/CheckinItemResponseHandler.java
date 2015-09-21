package org.kuali.asr.handler;

import org.kuali.ole.ncip.bo.OLECheckInItem;
import org.kuali.ole.ncip.converter.OLECheckInItemConverter;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public class CheckinItemResponseHandler extends ResponseHandler {

    private OLECheckInItemConverter oleCheckInItemConverter;

    public String marshalObjectToSIP2Xml(Object object) {
        String checkinItemXml = getOleCheckInItemConverter().generateCheckInItemXmlForSIP2((OLECheckInItem) object);
        return checkinItemXml;
    }

    @Override
    public String marshalObjectToXml(Object object) {
        String checkinItemXml = getOleCheckInItemConverter().generateCheckInItemXml((OLECheckInItem) object);
        return checkinItemXml;
    }

    public OLECheckInItemConverter getOleCheckInItemConverter() {
        if (null == oleCheckInItemConverter){
            oleCheckInItemConverter = new OLECheckInItemConverter();
        }
        return oleCheckInItemConverter;
    }

    public void setOleCheckInItemConverter(OLECheckInItemConverter oleCheckInItemConverter) {
        this.oleCheckInItemConverter = oleCheckInItemConverter;
    }
}
