package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for extentOfOwnership complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="extentOfOwnership">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="coverages" type="{http://ole.kuali.org/standards/ole-eInstance}coverages" minOccurs="0"/>
 *         &lt;element name="perpetualAccesses" type="{http://ole.kuali.org/standards/ole-eInstance}perpetualAccesses" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extentOfOwnership", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "coverages",
        "perpetualAccesses"
})
@XStreamAlias("extentOfOwnership")
public class ExtentOfOwnership {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected Coverages coverages;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected PerpetualAccesses perpetualAccesses;

    /**
     * Gets the value of the coverages property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Coverages }
     */
    public Coverages getCoverages() {
        return coverages;
    }

    /**
     * Sets the value of the coverages property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Coverages }
     */
    public void setCoverages(Coverages value) {
        this.coverages = value;
    }

    /**
     * Gets the value of the perpetualAccesses property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.PerpetualAccesses }
     */
    public PerpetualAccesses getPerpetualAccesses() {
        return perpetualAccesses;
    }

    /**
     * Sets the value of the perpetualAccesses property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.PerpetualAccesses }
     */
    public void setPerpetualAccesses(PerpetualAccesses value) {
        this.perpetualAccesses = value;
    }

}
