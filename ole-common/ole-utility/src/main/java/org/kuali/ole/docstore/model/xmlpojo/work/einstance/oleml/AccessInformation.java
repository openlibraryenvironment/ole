package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accessInformation complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="accessInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numberOfSimultaneousUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="proxiedResource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authenticationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessUsername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accessInformation", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "numberOfSimultaneousUser",
        "proxiedResource",
        "accessLocation",
        "authenticationType",
        "accessUsername",
        "accessPassword"
})
@XStreamAlias("accessInformation")
public class AccessInformation {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String numberOfSimultaneousUser;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String proxiedResource;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String accessLocation;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String authenticationType;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String accessUsername;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String accessPassword;

    /**
     * Gets the value of the numberOfSimultaneousUser property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getNumberOfSimultaneousUser() {
        return numberOfSimultaneousUser;
    }

    /**
     * Sets the value of the numberOfSimultaneousUser property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumberOfSimultaneousUser(String value) {
        this.numberOfSimultaneousUser = value;
    }

    /**
     * Gets the value of the proxiedResource property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProxiedResource() {
        return proxiedResource;
    }

    /**
     * Sets the value of the proxiedResource property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProxiedResource(String value) {
        this.proxiedResource = value;
    }

    /**
     * Gets the value of the accessLocation property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAccessLocation() {
        return accessLocation;
    }

    /**
     * Sets the value of the accessLocation property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccessLocation(String value) {
        this.accessLocation = value;
    }

    /**
     * Gets the value of the authenticationType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAuthenticationType() {
        return authenticationType;
    }

    /**
     * Sets the value of the authenticationType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAuthenticationType(String value) {
        this.authenticationType = value;
    }

    /**
     * Gets the value of the accessUsername property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAccessUsername() {
        return accessUsername;
    }

    /**
     * Sets the value of the accessUsername property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccessUsername(String value) {
        this.accessUsername = value;
    }

    /**
     * Gets the value of the accessPassword property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAccessPassword() {
        return accessPassword;
    }

    /**
     * Sets the value of the accessPassword property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccessPassword(String value) {
        this.accessPassword = value;
    }

}
