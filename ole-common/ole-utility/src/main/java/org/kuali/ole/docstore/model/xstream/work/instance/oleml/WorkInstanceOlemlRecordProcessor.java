package org.kuali.ole.docstore.model.xstream.work.instance.oleml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.*;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.*;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.InstanceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/1/12
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkInstanceOlemlRecordProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkInstanceOlemlRecordProcessor.class);

    private static XStream xStream = getXstream();
    private static XStream xStreamInstance = getXstreamInstance();
    private static XStream xStreamHolding = getXstreamHolding();
    private static XStream xStreamSourceHolding = getXstreamSourceHolding();
    private static XStream xStreamItem = getXstreamItem();
    private static XStream eXStream = getExstream();

    /**
     * @param oleInstance
     * @return
     */
    public String toXML(InstanceCollection oleInstance) {
        String xml = xStreamInstance.toXML(oleInstance);
        return xml;
    }

    /* *
   *
   * Method to get XML out put for given Holdings Object.
   *
   * @param holding
   * @return*/
    public String toXML(OleHoldings holding) {
        String xml = xStreamHolding.toXML(holding);
        return xml;
    }

    /**
     * @param sourceHoldings
     * @return
     */
    public String toXML(SourceHoldings sourceHoldings) {
        String xml = xStreamSourceHolding.toXML(sourceHoldings);
        return xml;

    }

    /*
     *
     * Method to get XML out put for given Item Object.
     *
     * @param item
     * @return*/
    public String toXML(Item oleItem) {
        String xml = xStreamItem.toXML(oleItem);
        return xml;
    }


    /**
     * @param instanceXML
     * @return
     */
    public InstanceCollection fromXML(String instanceXML) {
        //        xs.registerConverter(new PhysicalLocationConverter());
//        instanceXML = instanceXML.replaceAll("&", "&amp;");
//        instanceXML = instanceXML.replaceAll("< ", "&lt; ");
//        instanceXML = instanceXML.replaceAll("&amp;apos;", "\'");
        Object instanceObject = xStream.fromXML(instanceXML);
        return (InstanceCollection) instanceObject;
    }

    private static XStream getXstream() {
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(new QName("uri", "ole"), Instance.class);
        StaxDriver d = new StaxDriver(nsm);
        XStream xStream = new XStream(d);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(InstanceCollection.class);
        return xStream;
    }

    private static XStream getXstreamInstance() {
        XStream xStreamInstance = new XStream();
        xStreamInstance.autodetectAnnotations(true);
        xStreamInstance.processAnnotations(InstanceCollection.class);
        return xStreamInstance;
    }

    private static XStream getXstreamHolding() {
        XStream xStreamHolding = new XStream();
        xStreamHolding.autodetectAnnotations(true);
        xStreamHolding.processAnnotations(OleHoldings.class);
        return xStreamHolding;
    }

    private static XStream getXstreamSourceHolding() {
        XStream xStreamSourceHolding = new XStream();
        xStreamSourceHolding.autodetectAnnotations(true);
        xStreamSourceHolding.processAnnotations(SourceHoldings.class);
        return xStreamSourceHolding;
    }

    private static XStream getXstreamItem() {
        XStream xStreamItem = new XStream();
        xStreamItem.autodetectAnnotations(true);
        xStreamItem.processAnnotations(Item.class);
        return xStreamItem;
    }

    public Object getInstance(String instanceXML) {
        try{
            return xStream.fromXML(instanceXML);
        }catch (Exception ex){
            return eXStream.fromXML(instanceXML);
        }
    }

    private static XStream getExstream() {
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(new QName("uri", "ole"), EInstance.class);
        StaxDriver d = new StaxDriver(nsm);
        XStream xStream = new XStream(d);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class);
        return xStream;
    }
}
