package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLECancelRequest;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECancelRequestConverter{
    final Logger LOG = Logger.getLogger(OLECancelRequestConverter.class);
    public String generateCancelRequestXml(OLECancelRequest oleCancelRequest){
        XStream xStream = new XStream();
        xStream.alias("cancelRequest",OLECancelRequest.class);
        return xStream.toXML(oleCancelRequest);
    }
    public Object generateCancelRequestObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("cancelRequest",OLECancelRequest.class);
        return xStream.fromXML(xml);
    }
    public String generateCancelRequestJson(String xml) {
        OLECancelRequest oleCancelRequest = (OLECancelRequest)generateCancelRequestObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if(oleCancelRequest==null){
            oleCancelRequest=new OLECancelRequest();
        }
        try{
            return xmlContentHandler.marshalToJSON(oleCancelRequest);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
