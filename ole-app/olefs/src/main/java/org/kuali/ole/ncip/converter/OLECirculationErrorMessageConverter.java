package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLECirculationErrorMessage;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/4/13
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECirculationErrorMessageConverter {
    final Logger LOG = Logger.getLogger(OLECirculationErrorMessageConverter.class);

    public String generateCirculationErrorXml(OLECirculationErrorMessage oleCirculationErrorMessage){
        XStream xstream = new XStream();
        xstream.alias("response",OLECirculationErrorMessage.class);
        String response  =xstream.toXML(oleCirculationErrorMessage);
        if(LOG.isInfoEnabled()){
            LOG.info("Circulation Error :"+response);
        }
        return  response;
    }



    public OLECirculationErrorMessage getCirculationErrorObject(String xml){
        XStream xstream = new XStream();
        xstream.alias("response",OLECirculationErrorMessage.class);
        OLECirculationErrorMessage oleCirculationErrorMessage   =(OLECirculationErrorMessage)xstream.fromXML(xml);
        return oleCirculationErrorMessage;
    }

    public String generateLookupUserJson(String xml) {
        OLECirculationErrorMessage oleCirculationErrorMessage = (OLECirculationErrorMessage)getCirculationErrorObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        try{
            return xmlContentHandler.marshalToJSON(oleCirculationErrorMessage);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }




}
