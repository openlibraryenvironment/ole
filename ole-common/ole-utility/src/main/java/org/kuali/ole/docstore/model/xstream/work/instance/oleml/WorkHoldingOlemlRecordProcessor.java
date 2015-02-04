package org.kuali.ole.docstore.model.xstream.work.instance.oleml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.OleHoldings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ   IDEA.
 * User: Pranitha
 * Date: 4/12/12
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkHoldingOlemlRecordProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(WorkHoldingOlemlRecordProcessor.class);

    private static XStream xStream = getXstream();
    private static XStream xStreamHolding = getXstreamHolding();

    public OleHoldings fromXML(String itemXML) {
//        itemXML = itemXML.replaceAll("&", "&amp;");
//        itemXML = itemXML.replaceAll("< ", "&lt; ");
//        itemXML = itemXML.replaceAll("&amp;apos;", "\'");
        Object holdingObject = xStream.fromXML(itemXML);
        return (OleHoldings) holdingObject;
    }

    public String toXML(OleHoldings oleHolding) {
        String xml = xStreamHolding.toXML(oleHolding);
        return xml;
    }

    /*  public String toXML(OleHoldings holding) {

          String result = null;
          StringWriter sw = new StringWriter();
          try {
              JAXBContext jaxbContext = JAXBContext.newInstance(OleHoldings.class);
              Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
              jaxbMarshaller.marshal(holding, sw);
              result = sw.toString();
  //            LOG.info("DocSearchConfig Xml is " + result);
          }
          catch (Exception e) {
              System.out.println("error in jaxb"+e.getMessage()+ e);

          }
          return result;
      }


       public OleHoldings fromXML(String holdingXml) {

          JAXBElement<OleHoldings> oleHoldings = null;
          try {
              JAXBContext jaxbContext = JAXBContext.newInstance(OleHoldings.class);
              Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
              holdingXml=holdingXml.replaceAll("&","&amp;");
              holdingXml=holdingXml.replaceAll("< ","&lt; ");
              ByteArrayInputStream input = new ByteArrayInputStream(holdingXml.getBytes("UTF-8"));
              oleHoldings = jaxbUnmarshaller.unmarshal(new StreamSource(input), OleHoldings.class);
          }
          catch (Exception e) {
             System.out.println("error in jaxb"+e.getMessage()+ e);
          }
          return oleHoldings.getValue();

    }*/

    private static XStream getXstream() {
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(new QName("uri", "ole"), OleHoldings.class);
        StaxDriver d = new StaxDriver(nsm);
        XStream xStream = new XStream(d);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(OleHoldings.class);
        return xStream;
    }

    private static XStream getXstreamHolding() {
        XStream xStreamHolding = new XStream();
        xStreamHolding.autodetectAnnotations(true);
        xStreamHolding.processAnnotations(OleHoldings.class);
        return xStreamHolding;
    }


}
