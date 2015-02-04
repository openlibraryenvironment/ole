package org.kuali.ole.handler;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.bo.serachRetrieve.OleSRUDublin;
import org.kuali.ole.bo.serachRetrieve.OleSRUDublinRecord;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 7/6/13
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDublinRecordResponseHandler {

    public OleSRUDublinRecord fromXML(String fileContent) {
        XStream xStream = new XStream();
        xStream.alias("dublincore", OleSRUDublinRecord.class);
        xStream.alias("dublin", OleSRUDublin.class);
        Object object = xStream.fromXML(fileContent);
        return (OleSRUDublinRecord) object;
    }

    public String toXML(OleSRUDublinRecord oleSRUDublinRecord) {
        StringBuffer stringBuffer = new StringBuffer();
        XStream xStream = new XStream();
        xStream.alias("dublincore", OleSRUDublinRecord.class);
        xStream.alias("dublin", OleSRUDublin.class);
        String xml = xStream.toXML(oleSRUDublinRecord);
        xml = xml.replace("<dublincore>", "");
        xml = xml.replace("</dublincore>", "");
        xml = xml.replace("<dublin>", "");
        xml = xml.replace("</dublin>", "");
        stringBuffer.append(xml);
        return stringBuffer.toString();
    }
}
