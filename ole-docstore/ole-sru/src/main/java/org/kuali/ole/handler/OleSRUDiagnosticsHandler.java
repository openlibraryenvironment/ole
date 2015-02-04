package org.kuali.ole.handler;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostic;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/16/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDiagnosticsHandler {

    /**
     * this method is used to convert OleSRUDiagnostic object to an XML
     *
     * @param oleSRUDiagnostic object
     * @return xml as a string
     */
    public String toXML(OleSRUDiagnostic oleSRUDiagnostic) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<diagnostics>");
        stringBuffer.append("\n");
        XStream xStream = new XStream();
        xStream.alias("diagnostic", OleSRUDiagnostic.class);
        String xml = xStream.toXML(oleSRUDiagnostic);
        xml = xml.replaceAll("<diagnostic>", "<diagnostic xmlns=\"http://www.loc.gov/zing/srw/diagnostic/\">");
        xml += "\n</diagnostics>";
        stringBuffer.append(xml);
        return stringBuffer.toString();
    }
}
