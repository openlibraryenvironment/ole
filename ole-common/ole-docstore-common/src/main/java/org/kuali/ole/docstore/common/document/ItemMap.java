package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;


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
 *         &lt;element name="itemMap" type="{}item" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemMap", propOrder = {
        "itemMap"
})
@XmlRootElement(name="itemsDocs")
public class ItemMap {

    private static final Logger LOG = Logger.getLogger(ItemMap.class);
    @XmlElement(name = "itemsDoc")
    protected HashMap<String, Item> itemMap;



    public static String serialize(Object object) {
        String result = null;
        ItemMap itemMap = (ItemMap) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(ItemMap.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(itemMap, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }


    public static Object deserialize(String content) {
        ItemMap itemMap = new ItemMap();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(ItemMap.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                itemMap = unmarshaller.unmarshal(new StreamSource(input), ItemMap.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return itemMap;
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
     * {@link Item }
     */
    public HashMap<String, Item> getItemMap() {
        if (itemMap == null) {
            itemMap = new HashMap<>();
        }
        return this.itemMap;
    }
}
