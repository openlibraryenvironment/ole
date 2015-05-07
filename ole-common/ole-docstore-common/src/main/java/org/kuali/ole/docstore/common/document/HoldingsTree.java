package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for holdingsTree complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="holdingsTree">
 *   &lt;complexContent>
 *     &lt;extension base="{}docstoreDocument">
 *       &lt;sequence>
 *         &lt;element name="items" type="{}item" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="holdings" type="{}holdings" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsTree", propOrder = {
        "items",
        "holdings"
})

@XmlRootElement(name = "holdingsDocTree")
public class HoldingsTree
        implements Comparable<HoldingsTree> {

    private static final Logger LOG = Logger.getLogger(HoldingsTree.class);
    @XmlElementWrapper(name = "itemsDocs")
    @XmlElement(name = "itemsDoc")
    protected List<Item> items;
    @XmlElement(name = "holdingsDoc")
    protected Holdings holdings;

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

    /**
     * Gets the value of the holdings property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.common.document.Holdings }
     */
    public Holdings getHoldings() {
        return holdings;
    }

    /**
     * Sets the value of the holdings property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.common.document.Holdings }
     */
    public void setHoldings(Holdings value) {
        this.holdings = value;
    }

    public String serialize(Object object) {
        String result = null;
        HoldingsTree holdingsTree = (HoldingsTree) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(HoldingsTree.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(holdingsTree, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    public Object deserialize(String holdingsTreeXml) {
        HoldingsTree holdingsTree = new HoldingsTree();
        try {
            ByteArrayInputStream bibTreeInputStream = new ByteArrayInputStream(holdingsTreeXml.getBytes());
            StreamSource streamSource = new StreamSource(bibTreeInputStream);
            XMLStreamReader xmlStreamReader = JAXBContextFactory.getInstance().getXmlInputFactory().createXMLStreamReader(streamSource);
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(HoldingsTree.class);
            synchronized (unmarshaller) {
                holdingsTree = unmarshaller.unmarshal(xmlStreamReader, HoldingsTree.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return holdingsTree;
    }

    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int compareTo(HoldingsTree o) {
        return this.getHoldings().compareTo(o.getHoldings());
    }
}
