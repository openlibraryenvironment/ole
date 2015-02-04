package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLEItemFine;
import org.kuali.ole.ncip.bo.OLEItemFines;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/19/13
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEItemFineConverter {
    final Logger LOG = Logger.getLogger(OLEItemFineConverter.class);
    public String generateCheckOutItemXml(OLEItemFines oLECheckedOutItems){
        XStream xStream = new XStream();
        xStream.alias("fineItems",OLEItemFines.class);
        xStream.alias("fineItem",OLEItemFine.class);
        xStream.addImplicitCollection(OLEItemFines.class,"oleItemFineList");
        return xStream.toXML(oLECheckedOutItems);
    }

    public Object generateCheckoutItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("fineItems",OLEItemFines.class);
        xStream.alias("fineItem",OLEItemFine.class);
        xStream.addImplicitCollection(OLEItemFines.class,"oleItemFineList");
        return xStream.fromXML(xml);
    }
    public String generateFineJson(String xml) {
        OLEItemFines oleItemFines = (OLEItemFines)generateCheckoutItemObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if(oleItemFines==null){
            oleItemFines=new OLEItemFines();
        }
        try{
            return xmlContentHandler.marshalToJSON(oleItemFines);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
