package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


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
        Bibs bibs = (Bibs) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(Bibs.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(bibs, sw);
               } 
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public static Object deserialize(String bibsXml) {
        Bibs bibs = new Bibs();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(Bibs.class);
            ByteArrayInputStream input = new ByteArrayInputStream(bibsXml.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                bibs = unmarshaller.unmarshal(new StreamSource(input), Bibs.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return bibs;
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