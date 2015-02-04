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
 * <p>Java class for bibs complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="bibs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bibs" type="{}bib" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bibs", propOrder = {
        "bibs"
})
@XmlRootElement(name = "bibDocs")
public class Bibs {

    private static final Logger LOG = Logger.getLogger(Bibs.class);

    @XmlElement(name = "bibDoc")
    protected List<Bib> bibs;

    public static String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        Bibs bibs = (Bibs) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Bibs.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(bibs, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public static Object deserialize(String bibsXml) {

        JAXBElement<Bibs> bibsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Bibs.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(bibsXml.getBytes("UTF-8"));
            bibsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), Bibs.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return bibsElement.getValue();
    }

    /**
     * Gets the value of the bibs property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bibs property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBibs().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Bib }
     */
    public List<Bib> getBibs() {
        if (bibs == null) {
            bibs = new ArrayList<Bib>();
        }
        return this.bibs;
    }

}