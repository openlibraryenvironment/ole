package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLEPlaceRequest;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPlaceRequestConverter {
    final Logger LOG = Logger.getLogger(OLEPlaceRequestConverter.class);
    public String generatePlaceRequestXml(OLEPlaceRequest olePlaceRequest){
        XStream xStream = new XStream();
        xStream.alias("placeRequest",OLEPlaceRequest.class);
        //xStream.omitField(OLEPlaceRequest.class, "expirationDate");
       // xStream.omitField(OLEPlaceRequest.class, "blockOverride");
        return xStream.toXML(olePlaceRequest);
    }

    public String generatePlaceRequestXmlForSip2(OLEPlaceRequest olePlaceRequest){
        XStream xStream = new XStream();
        xStream.alias("placeRequest",OLEPlaceRequest.class);
        xStream.omitField(OLEPlaceRequest.class, "blockOverride");
        return xStream.toXML(olePlaceRequest);
    }
    public Object generatePlaceRequestObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("placeRequest",OLEPlaceRequest.class);
        return xStream.fromXML(xml);
    }
    public String generatePlaceRequestJson(String xml) {
        OLEPlaceRequest olePlaceRequest = (OLEPlaceRequest)generatePlaceRequestObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if(olePlaceRequest==null){
            olePlaceRequest=new OLEPlaceRequest();
        }
        try{
            return xmlContentHandler.marshalToJSON(olePlaceRequest);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
