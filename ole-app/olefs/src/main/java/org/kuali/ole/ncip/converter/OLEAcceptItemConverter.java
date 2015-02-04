package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLEAcceptItem;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAcceptItemConverter {
    final Logger LOG = Logger.getLogger(OLEAcceptItemConverter.class);
    public String generateAcceptItemXml(OLEAcceptItem oleAcceptItem){
        XStream xStream = new XStream();
        xStream.alias("acceptItem",OLEAcceptItem.class);
        return xStream.toXML(oleAcceptItem);
    }
    public Object generateAcceptItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("acceptItem",OLEAcceptItem.class);
        return xStream.fromXML(xml);
    }
    public String generateAcceptItemJson(String xml) {
        OLEAcceptItem oleAcceptItem = (OLEAcceptItem)generateAcceptItemObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if(oleAcceptItem==null){
            oleAcceptItem=new OLEAcceptItem();
        }
        try{
            return xmlContentHandler.marshalToJSON(oleAcceptItem);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
