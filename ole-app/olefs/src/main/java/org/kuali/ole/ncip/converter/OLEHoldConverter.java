package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.bo.OLEHold;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/20/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEHoldConverter {
    public String generateHoldXml(OLEHold oleHold){
        XStream xStream = new XStream();
        xStream.alias("hold",OLEHold.class);
        return xStream.toXML(oleHold);
    }

    public Object generateHoldObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("hold",OLEHold.class);
        return xStream.fromXML(xml);
    }
}
