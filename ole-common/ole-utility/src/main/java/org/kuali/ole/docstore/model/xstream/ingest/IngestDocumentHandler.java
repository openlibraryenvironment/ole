package org.kuali.ole.docstore.model.xstream.ingest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/28/12
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class IngestDocumentHandler {
    private static final Logger LOG = LoggerFactory.getLogger(IngestDocumentHandler.class);
    private static XStream xStream = getXstream();

    private static XStream getXstream() {
        XStream xStream = new XStream();
        xStream.registerConverter(new IngestDocumentConverter());
        xStream.alias("ingestDocument", RequestDocument.class);
        xStream.alias("linkedIngestDocument", RequestDocument.class);
        xStream.alias("content", Content.class);
        xStream.alias("additionalAttributes", AdditionalAttributes.class);
        return xStream;
    }

    public RequestDocument toObject(String ingestDocXml) {
        RequestDocument request = (RequestDocument) xStream.fromXML(ingestDocXml);
        return request;
    }

    public String toXML(Request request) {
        XStream xStream = new XStream(new XppDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    protected void writeText(QuickWriter writer, String text) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }
                };
            }
        });
        xStream.registerConverter(new RequestDocumentConverter());
        xStream.registerConverter(new LinkInfoConverter());
        xStream.alias("ingestDocument", RequestDocument.class);
        String xml = xStream.toXML(request);
        return xml;
    }
}
