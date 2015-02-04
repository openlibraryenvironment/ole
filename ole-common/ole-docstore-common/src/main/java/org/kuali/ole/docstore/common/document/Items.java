package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;


/**
 * <p>Java class for items complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="items">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="items" type="{}item" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "items", propOrder = {
        "items"
})
@XmlRootElement(name="itemsDocs")
public class Items {

    private static final Logger LOG = Logger.getLogger(Items.class);
    @XmlElement(name = "itemsDoc")
    protected List<Item> items;

    public static String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        Items items = (Items) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Items.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(items, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    public static Object deserialize(String itemsXml) {

        JAXBElement<Items> itemsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Items.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(itemsXml.getBytes("UTF-8"));
            itemsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), Items.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return itemsElement.getValue();
    }

    /**
     * Gets the value of the items property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the items property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItems().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link org.kuali.ole.docstore.common.document.Item }
     */
    public List<Item> getItems() {
        if (items == null) {
            items = new ArrayList<Item>();
        }
        return this.items;
    }

}
