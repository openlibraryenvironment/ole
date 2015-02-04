package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.util.ParseXml;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * <p>Java class for holdingsTrees complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="holdingsTrees">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="holdingsTrees" type="{}holdingsTree" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsTrees", propOrder = {
        "holdingsTrees"
})

@XmlRootElement(name = "holdingsDocsTree")
public class HoldingsTrees {

    private static final Logger LOG = Logger.getLogger(HoldingsTrees.class);
    @XmlElement(name = "holdingsDocTree")
    protected List<HoldingsTree> holdingsTrees;

    /**
     * Gets the value of the holdingsTrees property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the holdingsTrees property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHoldingsTrees().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.kuali.ole.docstore.common.document.HoldingsTree }
     *
     *
     */
    public List<HoldingsTree> getHoldingsTrees() {
        if (holdingsTrees == null) {
            holdingsTrees = new ArrayList<HoldingsTree>();
        }
        return this.holdingsTrees;
    }


    public static String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        HoldingsTrees bibsTrees = (HoldingsTrees) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsTrees.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(bibsTrees, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    public static Object deserialize(String holdingsTreesXml) {

        HoldingsTrees holdingsTrees = new HoldingsTrees();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(holdingsTreesXml)));
            NodeList root = doc.getChildNodes();
            Node holdingsTreesNode = ParseXml.getNode("holdingsDocsTree", root);
            HoldingsTree holdingsTree = new HoldingsTree();
            NodeList nodeList = doc.getElementsByTagName("holdingsDocTree");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node bibTreeNode = ParseXml.getNodeXml(holdingsTreesNode.getChildNodes(),i);
                holdingsTrees.getHoldingsTrees().add((HoldingsTree) holdingsTree.deserialize(ParseXml.nodeToString(bibTreeNode)));
            }
        } catch (SAXException e) {
            LOG.error("Exception ", e);
        } catch (IOException e) {
            LOG.error("Exception ", e);
        } catch (ParserConfigurationException e) {
            LOG.error("Exception ", e);
        }
        return holdingsTrees;
    }

}
