package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for locationLevel complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="locationLevel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="level" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="locationLevel" type="{http://ole.kuali.org/standards/ole-eInstance}locationLevel" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "locationLevel", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "name",
        "level",
        "locationLevel"
})
@XStreamAlias("locationLevel")
public class LocationLevel {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String name;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String level;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected LocationLevel locationLevel;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the level property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLevel(String value) {
        this.level = value;
    }

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

}
