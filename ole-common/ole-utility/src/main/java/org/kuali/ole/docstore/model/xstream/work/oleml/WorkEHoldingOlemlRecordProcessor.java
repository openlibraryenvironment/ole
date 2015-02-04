package org.kuali.ole.docstore.model.xstream.work.oleml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EHoldings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 7/18/13
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEHoldingOlemlRecordProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkEHoldingOlemlRecordProcessor.class);

    private static XStream xStream = getXstream();
    private static XStream xStreamEHolding = getXstreamHolding();

    public EHoldings fromXML(String itemXML) {

        Object holdingObject = xStream.fromXML(itemXML);
        return (EHoldings) holdingObject;
    }

    public String toXML(EHoldings eHolding) {
        String xml = xStreamEHolding.toXML(eHolding);
        return xml;
    }

    private static XStream getXstream() {
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(new QName("uri", "ole"), EHoldings.class);
        StaxDriver d = new StaxDriver(nsm);
        XStream xStream = new XStream(d);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(EHoldings.class);
        return xStream;
    }

    private static XStream getXstreamHolding() {
        XStream xStreamHolding = new XStream();
        xStreamHolding.autodetectAnnotations(true);
        xStreamHolding.processAnnotations(EHoldings.class);
        return xStreamHolding;
    }

}
