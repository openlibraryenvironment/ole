package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLEHold;
import org.kuali.ole.ncip.bo.OLEHolds;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/20/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEHoldsConverter {
    final Logger LOG = Logger.getLogger(OLEHoldsConverter.class);
    public String generateHoldsmXml(OLEHolds oleHolds){
        XStream xStream = new XStream();
        xStream.alias("holds",OLEHolds.class);
        xStream.alias("hold",OLEHold.class);
        xStream.addImplicitCollection(OLEHolds.class,"oleHoldList");
        return xStream.toXML(oleHolds);
    }

    public Object generateHoldsObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("holds",OLEHolds.class);
        xStream.alias("hold",OLEHold.class);
        xStream.addImplicitCollection(OLEHolds.class,"oleHoldList");
        return xStream.fromXML(xml);
    }
    public String generateHoldsJson(String xml) {
        OLEHolds oleHolds = (OLEHolds)generateHoldsObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if(oleHolds==null){
            oleHolds=new OLEHolds();
        }
        try{
            return xmlContentHandler.marshalToJSON(oleHolds);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
