package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.EDIOrder;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/9/12
 * Time: 7:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleOrderRecordHandler {

    public OleOrderRecords fromXML(String marcXMLContent) {
        XStream xStream = new XStream();
        xStream.alias("records", OleOrderRecords.class);
        xStream.alias("record", OleOrderRecord.class);
        xStream.alias("oleBibRecord", OleBibRecord.class);
        xStream.aliasField("bibRecord", OleOrderRecord.class, "oleBibRecord");
        xStream.aliasField("bibFileName", OleOrderRecord.class, "oleOriginalBibRecordFileName");
        xStream.aliasField("ediFileName", OleOrderRecord.class, "originalEDIFileName");
        xStream.alias("oleTxRecord", OleTxRecord.class);
        xStream.aliasField("transactionRecord", OleOrderRecord.class, "oleTxRecord");
        xStream.alias("originalRecord", BibMarcRecord.class);
        xStream.aliasField("bibMarcRecord", OleOrderRecord.class, "originalRecord");
        xStream.alias("originalEdi", EDIOrder.class);
        xStream.aliasField("ediOrder", OleOrderRecord.class, "originalEdi");
        xStream.addImplicitCollection(OleOrderRecords.class, "records");
        return (OleOrderRecords) xStream.fromXML(marcXMLContent);
    }


    public String toXML(OleOrderRecords oleOrderRecords) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuffer.append("\n");
        XStream xStream = new XStream();
        xStream.alias("records", OleOrderRecords.class);
        xStream.alias("record", OleOrderRecord.class);
        xStream.alias("oleBibRecord", OleBibRecord.class);
        xStream.aliasField("bibRecord", OleOrderRecord.class, "oleBibRecord");
        xStream.aliasField("bibFileName", OleOrderRecord.class, "oleOriginalBibRecordFileName");
        xStream.aliasField("ediFileName", OleOrderRecord.class, "originalEDIFileName");
        xStream.alias("oleTxRecord", OleTxRecord.class);
        xStream.aliasField("transactionRecord", OleOrderRecord.class, "oleTxRecord");
        xStream.alias("originalRecord", BibMarcRecord.class);
        xStream.aliasField("bibMarcRecord", OleOrderRecord.class, "originalRecord");
        xStream.alias("originalEdi", EDIOrder.class);
        xStream.aliasField("ediOrder", OleOrderRecord.class, "originalEdi");
        xStream.addImplicitCollection(OleOrderRecords.class, "records");
        String xml = xStream.toXML(oleOrderRecords);
        stringBuffer.append(xml);
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }
}
