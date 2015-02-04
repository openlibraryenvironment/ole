package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.util.ParseXml;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
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
        extends DocstoreDocument implements Comparable<HoldingsTree>{

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

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        HoldingsTree holdingsTree = (HoldingsTree) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsTree.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(holdingsTree, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String holdingsTreeXml) {
        JAXBElement<HoldingsTree> holdingsTreeElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsTree.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(holdingsTreeXml.getBytes("UTF-8"));
            holdingsTreeElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), HoldingsTree.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        HoldingsTree holdingsTrees = holdingsTreeElement.getValue();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(holdingsTreeXml)));
            NodeList root = doc.getChildNodes();
            Node holdingsTree = ParseXml.getNode("holdingsDocTree", root);
            Node holdings = ParseXml.getNode("holdingsDoc", holdingsTree.getChildNodes());
            NodeList nodes = holdings.getChildNodes();
            String holdingsXml = ParseXml.nodeToString(holdings);
            if (ParseXml.getNodeValue("holdingsType", nodes).equals("print")) {
                JAXBContext jc = JAXBContext.newInstance(PHoldings.class);
                Unmarshaller unmarshaller1 = jc.createUnmarshaller();
                StreamSource xmlSource = new StreamSource(new StringReader(holdingsXml));
                JAXBElement<PHoldings> je1 = unmarshaller1.unmarshal(xmlSource, PHoldings.class);
                PHoldings pHoldings = je1.getValue();
                holdingsTrees.setHoldings(pHoldings);
            } else {
                JAXBContext jc = JAXBContext.newInstance(EHoldings.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                StreamSource xmlSource1 = new StreamSource(new StringReader(holdingsXml));
                JAXBElement<EHoldings> je = unmarshaller.unmarshal(xmlSource1, EHoldings.class);
                EHoldings eHoldings = je.getValue();
                holdingsTrees.setHoldings(eHoldings);
            }
        } catch (SAXException e) {
            LOG.error("Exception ", e);
        } catch (IOException e) {
            LOG.error("Exception ", e);
        } catch (ParserConfigurationException e) {
            LOG.error("Exception ", e);
        } catch (JAXBException e) {
            LOG.error("Exception ", e);
        }

        return holdingsTrees;
    }

    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object deserializeContent(String content) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String serializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int compareTo(HoldingsTree o) {
        return this.getHoldings().compareTo(o.getHoldings());
    }
}
