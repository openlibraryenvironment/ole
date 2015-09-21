package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.bo.OLECheckOutItem;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/29/13
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckOutItemConverter {
    final Logger LOG = Logger.getLogger(OLECheckOutItemConverter.class);

    public String generateCheckOutItemXml(OLECheckOutItem oleCheckOutItem){
        XStream xStream = new XStream();
        xStream.alias("checkOutItem",OLECheckOutItem.class);
        xStream.omitField(OLECheckOutItem.class, "titleIdentifier");
        xStream.omitField(OLECheckOutItem.class, "feeAmount");
        xStream.omitField(OLECheckOutItem.class, "feeType");
        xStream.omitField(OLECheckOutItem.class, "transactionId");
        xStream.omitField(OLECheckOutItem.class, "itemProperties");
        return xStream.toXML(oleCheckOutItem);
    }
    
    public String generateCheckOutItemXmlForSIP2(OLECheckOutItem oleCheckOutItem){
        XStream xStream = new XStream();
        xStream.alias("checkOutItem",OLECheckOutItem.class);
        return xStream.toXML(oleCheckOutItem);
    }

    public Object generateCheckoutItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("checkOutItem",OLECheckOutItem.class);
        return xStream.fromXML(xml);
    }
    public String generateCheckOutItemJson(String xml) {
        OLECheckOutItem oleCheckOutItem = (OLECheckOutItem)generateCheckoutItemObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        try{
            return xmlContentHandler.marshalToJSON(oleCheckOutItem);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
