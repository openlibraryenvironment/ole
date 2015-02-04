package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.xml.bind.annotation.*;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for location complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="location">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="locationLevel" type="{http://ole.kuali.org/standards/ole-instance}locationLevel"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "location", propOrder = {"locationLevel"})
@XStreamAlias("location")
public class Location {

    @XmlElement(required = true)
    protected LocationLevel locationLevel;
    @XmlAttribute
    @XStreamAsAttribute
    protected String primary;
    @XmlAttribute
    @XStreamAsAttribute
    protected String status;

    /**
     * Gets the value of the locationLevel property.
     *
     * @return possible object is
     *         {@link LocationLevel }
     */
    public LocationLevel getLocationLevel() {
        return locationLevel;
    }

    /**
     * Sets the value of the locationLevel property.
     *
     * @param value allowed object is
     *              {@link LocationLevel }
     */
    public void setLocationLevel(LocationLevel value) {
        this.locationLevel = value;
    }

    /**
     * @return
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * @param primary
     */
    public void setPrimary(String primary) {
        this.primary = primary;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
