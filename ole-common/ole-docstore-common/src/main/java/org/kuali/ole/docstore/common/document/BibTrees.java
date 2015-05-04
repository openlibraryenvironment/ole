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
        BibTrees bibTrees = (BibTrees) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(BibTrees.class);
            synchronized (jaxbMarshaller) {
            jaxbMarshaller.marshal(bibTrees, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
       return result;
    }

    public static Object deserialize(String bibTreesXml) {

        BibTrees bibTrees = new BibTrees();
        try {
            ByteArrayInputStream bibTreeInputStream = new ByteArrayInputStream(bibTreesXml.getBytes());
            StreamSource streamSource = new StreamSource(bibTreeInputStream);
            XMLStreamReader xmlStreamReader = JAXBContextFactory.getInstance().getXmlInputFactory().createXMLStreamReader(streamSource);
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(BibTrees.class);
            synchronized (unmarshaller) {
            bibTrees = unmarshaller.unmarshal(xmlStreamReader, BibTrees.class).getValue();
            }
        } catch (Exception e) {
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
