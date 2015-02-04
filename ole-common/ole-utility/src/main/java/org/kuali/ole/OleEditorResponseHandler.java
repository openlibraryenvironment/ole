package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.edi.EDIOrder;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/11/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEditorResponseHandler {
    public OleEditorResponse fromXML(String marcXMLContent) {
        XStream xStream = new XStream();
        xStream.alias("oleEditorResponse", OleEditorResponse.class);
        xStream.alias("oleBibRecord", OleBibRecord.class);
        xStream.aliasField("tokenId", OleEditorResponse.class, "tokenId");
        return (OleEditorResponse) xStream.fromXML(marcXMLContent);

    }


    public String toXML(OleEditorResponse oleEditorResponse) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XStream xStream = new XStream();
        xStream.alias("oleEditorResponse", OleEditorResponse.class);
        xStream.alias("oleBibRecord", OleBibRecord.class);
        xStream.aliasField("tokenId", OleEditorResponse.class, "tokenId");
        String xml = xStream.toXML(oleEditorResponse);
        stringBuffer.append(xml);
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }
}
