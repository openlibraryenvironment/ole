package org.kuali.ole.docstore.common.document;

import org.apache.commons.lang3.StringUtils;
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
 * <p>Java class for bibTree complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="bibTree">
 *   &lt;complexContent>
 *     &lt;extension base="{}docstoreDocument">
 *       &lt;sequence>
 *         &lt;element name="holdingsTrees" type="{}holdingsTree" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="bib" type="{}bib" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bibTree", propOrder = {
        "holdingsTrees",
        "bib"
})

@XmlRootElement(name = "bibDocTree")
public class BibTree
        extends DocstoreDocument {

    private static final Logger LOG = Logger.getLogger(BibTree.class);
    // XmLElementWrapper generates a wrapper element around XML representation
    @XmlElementWrapper(name = "holdingsDocsTree")
    // XmlElement sets the name of the entities
    @XmlElement(name = "holdingsDocTree")
    protected List<HoldingsTree> holdingsTrees;

    @XmlElement(name = "bibDoc")
    protected Bib bib;

    /**
     * Gets the value of the holdingsTrees property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the holdingsTrees property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHoldingsTrees().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link org.kuali.ole.docstore.common.document.HoldingsTree }
     */
    public List<HoldingsTree> getHoldingsTrees() {
        if (holdingsTrees == null) {
            holdingsTrees = new ArrayList<HoldingsTree>();
        }
        return this.holdingsTrees;
    }

    /**
     * Gets the value of the bib property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.common.document.Bib }
     */
    public Bib getBib() {
        return bib;
    }

    /**
     * Sets the value of the bib property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.common.document.Bib }
     */
    public void setBib(Bib value) {
        this.bib = value;
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        BibTree bibTree = (BibTree) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibTree.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(bibTree, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String bibTreeXml) {

        BibTree bibTree = new BibTree();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(bibTreeXml)));
            NodeList root = doc.getChildNodes();
            Node bibTreeNode = ParseXml.getNode("bibDocTree", root);
            Node bibNode = ParseXml.getNode("bibDoc", bibTreeNode.getChildNodes());
            String BibXml = ParseXml.nodeToString(bibNode);
            Bib bib = new Bib();
            bib = (Bib) bib.deserialize(BibXml);
            bibTree.setBib(bib);
            Node holdingsTreesNode = ParseXml.getNode("holdingsDocsTree", bibTreeNode.getChildNodes());
            String holdingsTreeXml = null;
            HoldingsTree holdingsTree = new HoldingsTree();
            NodeList nodeList = doc.getElementsByTagName("holdingsDocTree");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node holdingsTreeNode = ParseXml.getNode("holdingsDocTree", holdingsTreesNode.getChildNodes(), i);
                holdingsTreeXml = ParseXml.nodeToString(holdingsTreeNode);
                bibTree.getHoldingsTrees().add((HoldingsTree) holdingsTree.deserialize(holdingsTreeXml));
            }
        } catch (SAXException e) {
            LOG.error("Exception :", e);
        } catch (IOException e) {
            LOG.error("Exception :", e);
        } catch (ParserConfigurationException e) {
            LOG.error("Exception :", e);
        }


        return bibTree;
    }

    @Override
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
}
