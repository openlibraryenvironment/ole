package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.bo.OLECheckedOutItem;
import org.kuali.ole.bo.OLECheckedOutItems;
import org.kuali.ole.converter.OleCirculationHandler;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/19/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckoutItemsConverter {
    final Logger LOG = Logger.getLogger(OLECheckoutItemsConverter.class);
    public String generateCheckOutItemXml(OLECheckedOutItems oLECheckedOutItems){
        XStream xStream = new XStream();
        xStream.alias("checkOutItems",OLECheckedOutItems.class);
        xStream.alias("checkOutItem",OLECheckedOutItem.class);
        xStream.addImplicitCollection(OLECheckedOutItems.class,"checkedOutItems");
        return xStream.toXML(oLECheckedOutItems);
    }

    public Object generateCheckoutItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("checkOutItems",OLECheckedOutItems.class);
        xStream.alias("checkOutItem",OLECheckedOutItem.class);
        xStream.addImplicitCollection(OLECheckedOutItems.class,"checkedOutItems");
        return xStream.fromXML(xml);
    }

    public String generateGetCheckedOutItemsJson(String xml) {
        OLECheckedOutItems oleCheckedOutItems = (OLECheckedOutItems)generateCheckoutItemObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if(oleCheckedOutItems==null){
            oleCheckedOutItems=new OLECheckedOutItems();
        }
        try{
            return xmlContentHandler.marshalToJSON(oleCheckedOutItems);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
