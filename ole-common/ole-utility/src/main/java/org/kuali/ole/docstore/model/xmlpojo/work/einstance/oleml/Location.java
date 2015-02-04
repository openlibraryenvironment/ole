package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for location complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="location">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="locationLevel" type="{http://ole.kuali.org/standards/ole-eInstance}locationLevel"/>
 *       &lt;/sequence>
 *       &lt;attribute name="primary" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "location", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "locationLevel"
})
@XStreamAlias("location")
public class Location {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected LocationLevel locationLevel;
    @XmlAttribute(name = "primary")
    protected String primary;
    @XmlAttribute(name = "status")
    protected String status;

    /**
     * Gets the value of the locationLevel property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.LocationLevel }
     */
    public LocationLevel getLocationLevel() {
        return locationLevel;
    }

    /**
     * Sets the value of the locationLevel property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.LocationLevel }
     */
    public void setLocationLevel(LocationLevel value) {
        this.locationLevel = value;
    }

    /**
     * Gets the value of the primary property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrimary(String value) {
        this.primary = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

}
