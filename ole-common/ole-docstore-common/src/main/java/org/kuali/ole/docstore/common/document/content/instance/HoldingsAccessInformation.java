package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for accessInformation complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="accessInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uri" type="{http://ole.kuali.org/standards/ole-instance}uri"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsAccessInformation", propOrder = {
        "numberOfSimultaneousUser",
        "proxiedResource",
        "accessLocation",
        "authenticationType",
        "accessUsername",
        "accessPassword",
        "materialsSpecified",
        "firstIndicator",
        "secondIndicator"
})
@XStreamAlias("holdingsAccessInformation")
public class HoldingsAccessInformation {

    protected String numberOfSimultaneousUser;
    protected String proxiedResource;
    protected String accessLocation;
    protected String authenticationType;
    protected String accessUsername;
    protected String accessPassword;
    protected String materialsSpecified;
    protected String firstIndicator;
    protected String secondIndicator;

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

    public String getMaterialsSpecified() {
        return materialsSpecified;
    }

    public void setMaterialsSpecified(String materialsSpecified) {
        this.materialsSpecified = materialsSpecified;
    }

    public String getFirstIndicator() {
        return firstIndicator;
    }

    public void setFirstIndicator(String firstIndicator) {
        this.firstIndicator = firstIndicator;
    }

    public String getSecondIndicator() {
        return secondIndicator;
    }

    public void setSecondIndicator(String secondIndicator) {
        this.secondIndicator = secondIndicator;
    }
}
