package org.kuali.ole.docstore.model.xstream.work.instance.oleml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.SourceHoldings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/31/12
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkSourceHoldingOlemlRecordProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(WorkSourceHoldingOlemlRecordProcessor.class);

    private static XStream xStream = getXstream();
    private static XStream xStreamSourceHolding = getXstreamSourceHolding();

    public SourceHoldings fromXML(String srHolXML) {
//        srHolXML = srHolXML.replaceAll("&", "&amp;");
//        srHolXML = srHolXML.replaceAll("< ", "&lt; ");
//        srHolXML = srHolXML.replaceAll("&amp;apos;", "\'");
        Object sourceHoldingObject = xStream.fromXML(srHolXML);
        return (SourceHoldings) sourceHoldingObject;
    }

    /**
     * @param sourceHoldings
     * @return
     */
    public String toXML(SourceHoldings sourceHoldings) {
        String xml = xStreamSourceHolding.toXML(sourceHoldings);
        return xml;
    }

    private static XStream getXstream() {
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(new QName("uri", "ole"), SourceHoldings.class);
        StaxDriver d = new StaxDriver(nsm);
        XStream xStream = new XStream(d);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(SourceHoldings.class);
        return xStream;
    }

    private static XStream getXstreamSourceHolding() {
        XStream xStreamSourceHolding = new XStream();
        xStreamSourceHolding.autodetectAnnotations(true);
        xStreamSourceHolding.processAnnotations(SourceHoldings.class);
        return xStreamSourceHolding;

    }
}
