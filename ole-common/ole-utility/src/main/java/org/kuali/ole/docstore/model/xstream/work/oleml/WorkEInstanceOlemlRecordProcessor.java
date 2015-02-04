package org.kuali.ole.docstore.model.xstream.work.oleml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EHoldings;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 7/18/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEInstanceOlemlRecordProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkEInstanceOlemlRecordProcessor.class);

    private static XStream xStream = getXstream();
    private static XStream xStreamInstance = getXstreamEInstance();
    private static XStream xStreamHolding = getXstreamEHolding();


    /**
     * @param instanceCollection
     * @return
     */
    public String toXML(InstanceCollection instanceCollection) {
        String xml = xStreamInstance.toXML(instanceCollection);
        return xml;
    }

    public String toXML(EHoldings eHoldings) {
        String xml = xStreamHolding.toXML(eHoldings);
        return xml;
    }

    /**
     * @param instanceXML
     * @return
     */
    public InstanceCollection fromXML(String instanceXML) {
        Object instanceObject = xStream.fromXML(instanceXML);
        return (InstanceCollection) instanceObject;
    }

    private static XStream getXstream() {
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(new QName("uri", "ole"), EInstance.class);
        StaxDriver d = new StaxDriver(nsm);
        XStream xStream = new XStream(d);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(InstanceCollection.class);
        return xStream;
    }

    private static XStream getXstreamEInstance() {
        XStream xStreamInstance = new XStream();
        xStreamInstance.autodetectAnnotations(true);
        xStreamInstance.processAnnotations(InstanceCollection.class);
        return xStreamInstance;
    }

    private static XStream getXstreamEHolding() {
        XStream xStreamHolding = new XStream();
        xStreamHolding.autodetectAnnotations(true);
        xStreamHolding.processAnnotations(EHoldings.class);
        return xStreamHolding;
    }


}
