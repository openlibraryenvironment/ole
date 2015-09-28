package org.kuali.ole.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.bo.OLECheckInItem;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/29/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckInItemConverter {
    final Logger LOG = Logger.getLogger(OLECheckInItemConverter.class);

    public String generateCheckInItemXml(OLECheckInItem oleCheckInItem){
        XStream xStream = new XStream();
        xStream.alias("checkInItem",OLECheckInItem.class);
        xStream.omitField(OLECheckInItem.class, "patronBarcode");
        xStream.omitField(OLECheckInItem.class, "itemLocation");
        return xStream.toXML(oleCheckInItem);
    }

    public String generateCheckInItemXmlForSIP2(OLECheckInItem oleCheckInItem){
         XStream xStream = new XStream();
         xStream.alias("checkInItem",OLECheckInItem.class);
         return xStream.toXML(oleCheckInItem);
     }

    public Object generateCheckInItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("checkInItem",OLECheckInItem.class);
        return xStream.fromXML(xml);
    }
    public String generateCheckInItemJson(String xml) {
        OLECheckInItem oleCheckedInItems = (OLECheckInItem)generateCheckInItemObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        try{
            return xmlContentHandler.marshalToJSON(oleCheckedInItems);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }

}
