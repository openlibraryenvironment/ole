package org.kuali.ole.docstore.common.document.content.instance.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 4/11/12
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemOlemlRecordProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ItemOlemlRecordProcessor.class);

    private static XStream xStream = getXstream();
    private static XStream xStreamItem = getXstreamItem();

    public Item fromXML(String itemXML) {
//        itemXML = itemXML.replaceAll("&", "&amp;");
//        itemXML = itemXML.replaceAll("< ", "&lt; ");
//        itemXML = itemXML.replaceAll("&amp;apos;","\'");
        Object itemObject = xStream.fromXML(itemXML);
        return (Item) itemObject;
    }


    public String toXML(Item oleItem) {

        String xml = xStreamItem.toXML(oleItem);
        return xml;
    }

   /* public String toXML(Item item) {

        String result = null;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(item, sw);
            result = sw.toString();
//            LOG.info("DocSearchConfig Xml is " + result);
        }
        catch (Exception e) {
            System.out.println("error in jaxb"+e.getMessage()+ e);

        }
        return result;
    }

     public Item fromXML(String itemXML) {

        JAXBElement<Item> item = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(OleHoldings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            itemXML=itemXML.replaceAll("&","&amp;");
            itemXML=itemXML.replaceAll("< ","&lt; ");
            ByteArrayInputStream input = new ByteArrayInputStream(itemXML.getBytes("UTF-8"));
            item = jaxbUnmarshaller.unmarshal(new StreamSource(input), Item.class);
        }
        catch (Exception e) {
           System.out.println("error in jaxb"+e.getMessage()+ e);
        }
        return item.getValue();

}*/

    private static XStream getXstream() {
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(new QName("uri", "ole"), Item.class);
        StaxDriver d = new StaxDriver(nsm);
        XStream xStream = new XStream(d);
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(Item.class);
        return xStream;
    }

    private static XStream getXstreamItem() {
        XStream xStreamItem = new XStream();
        xStreamItem.autodetectAnnotations(true);
        xStreamItem.processAnnotations(Item.class);
        return xStreamItem;
    }
}
