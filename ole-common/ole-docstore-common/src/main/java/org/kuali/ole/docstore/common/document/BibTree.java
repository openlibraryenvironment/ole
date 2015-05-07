package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

import javax.xml.bind.JAXBContext;
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
public class BibTree {

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

    public String serialize(Object object) {
        String result = null;
        BibTree bibTree = (BibTree) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(BibTree.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(bibTree, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public Object deserialize(String bibTreeXml) {
        BibTree bibTree = new BibTree();
        try {
            ByteArrayInputStream bibTreeInputStream = new ByteArrayInputStream(bibTreeXml.getBytes());
            StreamSource streamSource = new StreamSource(bibTreeInputStream);
            XMLStreamReader xmlStreamReader = JAXBContextFactory.getInstance().getXmlInputFactory().createXMLStreamReader(streamSource);
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(BibTree.class);
            synchronized (unmarshaller) {
                bibTree = unmarshaller.unmarshal(xmlStreamReader, BibTree.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return bibTree;
    }

}
