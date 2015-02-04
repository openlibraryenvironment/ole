package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.util.ParseXml;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for bibTrees complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="bibsTrees">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bibTrees" type="{}bibTree" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bibTrees", propOrder = {
        "bibTrees"
})
@XmlRootElement(name = "bibDocsTree")
public class BibTrees {

    private static final Logger LOG = Logger.getLogger(BibTrees.class);
    @XmlElement(name = "bibDocTree")
    protected List<BibTree> bibTrees;

    public static String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        BibTrees bibsTrees = (BibTrees) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibTrees.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(bibsTrees, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
       return result;
    }

    public static Object deserialize(String bibTreesXml) {

        BibTrees bibTrees = new BibTrees();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(bibTreesXml)));
            NodeList root = doc.getChildNodes();
            Node bibTreesNode = ParseXml.getNode("bibDocsTree", root);
            BibTree bibTree = new BibTree();
            NodeList nodeList = doc.getElementsByTagName("bibDocTree");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node bibTreeNode = ParseXml.getNodeXml(bibTreesNode.getChildNodes(), i);
                bibTrees.getBibTrees().add((BibTree) bibTree.deserialize(ParseXml.nodeToString(bibTreeNode)));
            }
        } catch (SAXException e) {
            LOG.error("Exception :", e);
        } catch (IOException e) {
            LOG.error("Exception :", e);
        } catch (ParserConfigurationException e) {
            LOG.error("Exception :", e);
        }


        return bibTrees;
    }

    /**
     * Gets the value of the bibTrees property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bibTrees property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBibTrees().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link BibTree }
     */
    public List<BibTree> getBibTrees() {
        if (bibTrees == null) {
            bibTrees = new ArrayList<BibTree>();
        }
        return this.bibTrees;
    }


}
