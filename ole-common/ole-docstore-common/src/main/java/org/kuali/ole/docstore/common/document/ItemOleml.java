package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;


/**
 * <p>Java class for itemOleml complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="itemOleml">
 *   &lt;complexContent>
 *     &lt;extension base="{}item">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemOleml")

@XmlRootElement(name = "itemDoc")
public class ItemOleml
        extends Item {
    private static final Logger LOG = Logger.getLogger(ItemOleml.class);
    public ItemOleml() {
        category=DocCategory.WORK.getCode();
        type=DocType.ITEM.getCode();
        format=DocFormat.OLEML.getCode();
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        ItemOleml itemOleml = (ItemOleml) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ItemOleml.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.marshal(itemOleml, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {

        JAXBElement<ItemOleml> ItemOlemlElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ItemOleml.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            ItemOlemlElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), ItemOleml.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return ItemOlemlElement.getValue();
    }

    @Override
    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object deserializeContent(String content) {
        ItemOlemlRecordProcessor recordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item item = recordProcessor.fromXML(content);
        return item;
    }

    @Override
    public String serializeContent(Object object) {
        org.kuali.ole.docstore.common.document.content.instance.Item item = (org.kuali.ole.docstore.common.document.content.instance.Item) object;
        ItemOlemlRecordProcessor recordProcessor = new ItemOlemlRecordProcessor();
        String content = recordProcessor.toXML(item);
        return content;
    }
}
