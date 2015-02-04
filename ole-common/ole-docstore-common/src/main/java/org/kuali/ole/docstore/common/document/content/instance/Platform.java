package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for platform complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="platform">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="adminUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="adminPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="adminUrl" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="platformName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "platform", propOrder = {
        "adminUserName",
        "adminPassword",
        "adminUrl",
        "platformName"
})
@XStreamAlias("platform")
public class Platform {

    protected String adminUserName;
    protected String adminPassword;
    @XmlSchemaType(name = "anyURI")
    protected String adminUrl;
    protected String platformName;

    /**
     * Gets the value of the adminUserName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAdminUserName() {
        return adminUserName;
    }

    /**
     * Sets the value of the adminUserName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAdminUserName(String value) {
        this.adminUserName = value;
    }

    /**
     * Gets the value of the adminPassword property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Sets the value of the adminPassword property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAdminPassword(String value) {
        this.adminPassword = value;
    }

    /**
     * Gets the value of the adminUrl property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAdminUrl() {
        return adminUrl;
    }

    /**
     * Sets the value of the adminUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAdminUrl(String value) {
        this.adminUrl = value;
    }

    /**
     * Gets the value of the platformName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * Sets the value of the platformName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPlatformName(String value) {
        this.platformName = value;
    }

}
