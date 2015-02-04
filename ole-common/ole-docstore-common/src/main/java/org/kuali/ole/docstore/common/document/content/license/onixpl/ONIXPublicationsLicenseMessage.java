
package org.kuali.ole.docstore.common.document.content.license.onixpl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Header">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Sender">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="SenderIdentifier">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="SenderIDType">
 *                                         &lt;complexType>
 *                                           &lt;simpleContent>
 *                                             &lt;extension base="&lt;http://www.editeur.org/onix-pl>SenderIDTypeCode">
 *                                               &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                             &lt;/extension>
 *                                           &lt;/simpleContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="IDTypeName" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;simpleContent>
 *                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                               &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                             &lt;/extension>
 *                                           &lt;/simpleContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="IDValue">
 *                                         &lt;complexType>
 *                                           &lt;simpleContent>
 *                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                               &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                             &lt;/extension>
 *                                           &lt;/simpleContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="SenderName" type="{http://www.editeur.org/onix-pl}PartyName" minOccurs="0"/>
 *                             &lt;element name="SenderContact" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="SenderEmail" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Addressee" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="AddresseeIdentifier">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="AddresseeIDType">
 *                                         &lt;complexType>
 *                                           &lt;simpleContent>
 *                                             &lt;extension base="&lt;http://www.editeur.org/onix-pl>AddresseeIDTypeCode">
 *                                               &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                             &lt;/extension>
 *                                           &lt;/simpleContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="IDTypeName" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;simpleContent>
 *                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                               &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                             &lt;/extension>
 *                                           &lt;/simpleContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="IDValue">
 *                                         &lt;complexType>
 *                                           &lt;simpleContent>
 *                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                               &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                             &lt;/extension>
 *                                           &lt;/simpleContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="AddresseeName" type="{http://www.editeur.org/onix-pl}PartyName" minOccurs="0"/>
 *                             &lt;element name="AddresseeContact" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="AddresseeEmail" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="MessageNumber" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="MessageRepeat" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>int">
 *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="SentDateTime">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.editeur.org/onix-pl>DateOrDateTime">
 *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="MessageNote" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PublicationsLicenseExpression" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;group ref="{http://www.editeur.org/onix-pl}PublicationsLicenseExpression-group"/>
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                 &lt;attribute name="version" type="{http://www.editeur.org/onix-pl}VersionList" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *       &lt;attribute name="version" use="required" type="{http://www.editeur.org/onix-pl}VersionList" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "header",
        "publicationsLicenseExpression"
})
@XmlRootElement(name = "ONIXPublicationsLicenseMessage", namespace = "http://www.editeur.org/onix-pl")
public class ONIXPublicationsLicenseMessage {

    @XmlElement(name = "Header", namespace = "http://www.editeur.org/onix-pl", required = true)
    protected ONIXPublicationsLicenseMessage.Header header;
    @XmlElement(name = "PublicationsLicenseExpression", namespace = "http://www.editeur.org/onix-pl", required = true)
    protected List<ONIXPublicationsLicenseMessage.PublicationsLicenseExpression> publicationsLicenseExpression;
    @XmlAttribute(required = true)
    protected String version;
    @XmlAttribute
    protected String datestamp;
    @XmlAttribute
    protected String sourcetype;
    @XmlAttribute
    protected String sourcename;

    /**
     * Gets the value of the header property.
     *
     * @return possible object is
     *         {@link ONIXPublicationsLicenseMessage.Header }
     */
    public ONIXPublicationsLicenseMessage.Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     *
     * @param value allowed object is
     *              {@link ONIXPublicationsLicenseMessage.Header }
     */
    public void setHeader(ONIXPublicationsLicenseMessage.Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the publicationsLicenseExpression property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publicationsLicenseExpression property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublicationsLicenseExpression().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ONIXPublicationsLicenseMessage.PublicationsLicenseExpression }
     */
    public List<ONIXPublicationsLicenseMessage.PublicationsLicenseExpression> getPublicationsLicenseExpression() {
        if (publicationsLicenseExpression == null) {
            publicationsLicenseExpression = new ArrayList<ONIXPublicationsLicenseMessage.PublicationsLicenseExpression>();
        }
        return this.publicationsLicenseExpression;
    }

    /**
     * Gets the value of the version property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the datestamp property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDatestamp() {
        return datestamp;
    }

    /**
     * Sets the value of the datestamp property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDatestamp(String value) {
        this.datestamp = value;
    }

    /**
     * Gets the value of the sourcetype property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSourcetype() {
        if (sourcetype == null) {
            return "00";
        } else {
            return sourcetype;
        }
    }

    /**
     * Sets the value of the sourcetype property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSourcetype(String value) {
        this.sourcetype = value;
    }

    /**
     * Gets the value of the sourcename property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSourcename() {
        return sourcename;
    }

    /**
     * Sets the value of the sourcename property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSourcename(String value) {
        this.sourcename = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Sender">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="SenderIdentifier">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="SenderIDType">
     *                               &lt;complexType>
     *                                 &lt;simpleContent>
     *                                   &lt;extension base="&lt;http://www.editeur.org/onix-pl>SenderIDTypeCode">
     *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                                   &lt;/extension>
     *                                 &lt;/simpleContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="IDTypeName" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;simpleContent>
     *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                                   &lt;/extension>
     *                                 &lt;/simpleContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="IDValue">
     *                               &lt;complexType>
     *                                 &lt;simpleContent>
     *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                                   &lt;/extension>
     *                                 &lt;/simpleContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="SenderName" type="{http://www.editeur.org/onix-pl}PartyName" minOccurs="0"/>
     *                   &lt;element name="SenderContact" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="SenderEmail" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Addressee" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="AddresseeIdentifier">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="AddresseeIDType">
     *                               &lt;complexType>
     *                                 &lt;simpleContent>
     *                                   &lt;extension base="&lt;http://www.editeur.org/onix-pl>AddresseeIDTypeCode">
     *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                                   &lt;/extension>
     *                                 &lt;/simpleContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="IDTypeName" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;simpleContent>
     *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                                   &lt;/extension>
     *                                 &lt;/simpleContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="IDValue">
     *                               &lt;complexType>
     *                                 &lt;simpleContent>
     *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                                     &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                                   &lt;/extension>
     *                                 &lt;/simpleContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="AddresseeName" type="{http://www.editeur.org/onix-pl}PartyName" minOccurs="0"/>
     *                   &lt;element name="AddresseeContact" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="AddresseeEmail" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="MessageNumber" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="MessageRepeat" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>int">
     *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="SentDateTime">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.editeur.org/onix-pl>DateOrDateTime">
     *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="MessageNote" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "sender",
            "addressee",
            "messageNumber",
            "messageRepeat",
            "sentDateTime",
            "messageNote"
    })
    public static class Header {

        @XmlElement(name = "Sender", namespace = "http://www.editeur.org/onix-pl", required = true)
        protected ONIXPublicationsLicenseMessage.Header.Sender sender;
        @XmlElement(name = "Addressee", namespace = "http://www.editeur.org/onix-pl")
        protected List<ONIXPublicationsLicenseMessage.Header.Addressee> addressee;
        @XmlElement(name = "MessageNumber", namespace = "http://www.editeur.org/onix-pl")
        protected ONIXPublicationsLicenseMessage.Header.MessageNumber messageNumber;
        @XmlElement(name = "MessageRepeat", namespace = "http://www.editeur.org/onix-pl")
        protected ONIXPublicationsLicenseMessage.Header.MessageRepeat messageRepeat;
        @XmlElement(name = "SentDateTime", namespace = "http://www.editeur.org/onix-pl", required = true)
        protected ONIXPublicationsLicenseMessage.Header.SentDateTime sentDateTime;
        @XmlElement(name = "MessageNote", namespace = "http://www.editeur.org/onix-pl")
        protected ONIXPublicationsLicenseMessage.Header.MessageNote messageNote;
        @XmlAttribute
        protected String datestamp;
        @XmlAttribute
        protected String sourcetype;
        @XmlAttribute
        protected String sourcename;

        /**
         * Gets the value of the sender property.
         *
         * @return possible object is
         *         {@link ONIXPublicationsLicenseMessage.Header.Sender }
         */
        public ONIXPublicationsLicenseMessage.Header.Sender getSender() {
            return sender;
        }

        /**
         * Sets the value of the sender property.
         *
         * @param value allowed object is
         *              {@link ONIXPublicationsLicenseMessage.Header.Sender }
         */
        public void setSender(ONIXPublicationsLicenseMessage.Header.Sender value) {
            this.sender = value;
        }

        /**
         * Gets the value of the addressee property.
         * <p/>
         * <p/>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the addressee property.
         * <p/>
         * <p/>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAddressee().add(newItem);
         * </pre>
         * <p/>
         * <p/>
         * <p/>
         * Objects of the following type(s) are allowed in the list
         * {@link ONIXPublicationsLicenseMessage.Header.Addressee }
         */
        public List<ONIXPublicationsLicenseMessage.Header.Addressee> getAddressee() {
            if (addressee == null) {
                addressee = new ArrayList<ONIXPublicationsLicenseMessage.Header.Addressee>();
            }
            return this.addressee;
        }

        /**
         * Gets the value of the messageNumber property.
         *
         * @return possible object is
         *         {@link ONIXPublicationsLicenseMessage.Header.MessageNumber }
         */
        public ONIXPublicationsLicenseMessage.Header.MessageNumber getMessageNumber() {
            return messageNumber;
        }

        /**
         * Sets the value of the messageNumber property.
         *
         * @param value allowed object is
         *              {@link ONIXPublicationsLicenseMessage.Header.MessageNumber }
         */
        public void setMessageNumber(ONIXPublicationsLicenseMessage.Header.MessageNumber value) {
            this.messageNumber = value;
        }

        /**
         * Gets the value of the messageRepeat property.
         *
         * @return possible object is
         *         {@link ONIXPublicationsLicenseMessage.Header.MessageRepeat }
         */
        public ONIXPublicationsLicenseMessage.Header.MessageRepeat getMessageRepeat() {
            return messageRepeat;
        }

        /**
         * Sets the value of the messageRepeat property.
         *
         * @param value allowed object is
         *              {@link ONIXPublicationsLicenseMessage.Header.MessageRepeat }
         */
        public void setMessageRepeat(ONIXPublicationsLicenseMessage.Header.MessageRepeat value) {
            this.messageRepeat = value;
        }

        /**
         * Gets the value of the sentDateTime property.
         *
         * @return possible object is
         *         {@link ONIXPublicationsLicenseMessage.Header.SentDateTime }
         */
        public ONIXPublicationsLicenseMessage.Header.SentDateTime getSentDateTime() {
            return sentDateTime;
        }

        /**
         * Sets the value of the sentDateTime property.
         *
         * @param value allowed object is
         *              {@link ONIXPublicationsLicenseMessage.Header.SentDateTime }
         */
        public void setSentDateTime(ONIXPublicationsLicenseMessage.Header.SentDateTime value) {
            this.sentDateTime = value;
        }

        /**
         * Gets the value of the messageNote property.
         *
         * @return possible object is
         *         {@link ONIXPublicationsLicenseMessage.Header.MessageNote }
         */
        public ONIXPublicationsLicenseMessage.Header.MessageNote getMessageNote() {
            return messageNote;
        }

        /**
         * Sets the value of the messageNote property.
         *
         * @param value allowed object is
         *              {@link ONIXPublicationsLicenseMessage.Header.MessageNote }
         */
        public void setMessageNote(ONIXPublicationsLicenseMessage.Header.MessageNote value) {
            this.messageNote = value;
        }

        /**
         * Gets the value of the datestamp property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getDatestamp() {
            return datestamp;
        }

        /**
         * Sets the value of the datestamp property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setDatestamp(String value) {
            this.datestamp = value;
        }

        /**
         * Gets the value of the sourcetype property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getSourcetype() {
            if (sourcetype == null) {
                return "00";
            } else {
                return sourcetype;
            }
        }

        /**
         * Sets the value of the sourcetype property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSourcetype(String value) {
            this.sourcetype = value;
        }

        /**
         * Gets the value of the sourcename property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getSourcename() {
            return sourcename;
        }

        /**
         * Sets the value of the sourcename property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSourcename(String value) {
            this.sourcename = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="AddresseeIdentifier">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="AddresseeIDType">
         *                     &lt;complexType>
         *                       &lt;simpleContent>
         *                         &lt;extension base="&lt;http://www.editeur.org/onix-pl>AddresseeIDTypeCode">
         *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *                         &lt;/extension>
         *                       &lt;/simpleContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="IDTypeName" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;simpleContent>
         *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *                         &lt;/extension>
         *                       &lt;/simpleContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="IDValue">
         *                     &lt;complexType>
         *                       &lt;simpleContent>
         *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *                         &lt;/extension>
         *                       &lt;/simpleContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="AddresseeName" type="{http://www.editeur.org/onix-pl}PartyName" minOccurs="0"/>
         *         &lt;element name="AddresseeContact" minOccurs="0">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="AddresseeEmail" minOccurs="0">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "addresseeIdentifier",
                "addresseeName",
                "addresseeContact",
                "addresseeEmail"
        })
        public static class Addressee {

            @XmlElement(name = "AddresseeIdentifier", namespace = "http://www.editeur.org/onix-pl", required = true)
            protected ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier addresseeIdentifier;
            @XmlElement(name = "AddresseeName", namespace = "http://www.editeur.org/onix-pl")
            protected PartyName addresseeName;
            @XmlElement(name = "AddresseeContact", namespace = "http://www.editeur.org/onix-pl")
            protected ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeContact addresseeContact;
            @XmlElement(name = "AddresseeEmail", namespace = "http://www.editeur.org/onix-pl")
            protected ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeEmail addresseeEmail;
            @XmlAttribute
            protected String datestamp;
            @XmlAttribute
            protected String sourcetype;
            @XmlAttribute
            protected String sourcename;

            /**
             * Gets the value of the addresseeIdentifier property.
             *
             * @return possible object is
             *         {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier }
             */
            public ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier getAddresseeIdentifier() {
                return addresseeIdentifier;
            }

            /**
             * Sets the value of the addresseeIdentifier property.
             *
             * @param value allowed object is
             *              {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier }
             */
            public void setAddresseeIdentifier(ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier value) {
                this.addresseeIdentifier = value;
            }

            /**
             * Gets the value of the addresseeName property.
             *
             * @return possible object is
             *         {@link PartyName }
             */
            public PartyName getAddresseeName() {
                return addresseeName;
            }

            /**
             * Sets the value of the addresseeName property.
             *
             * @param value allowed object is
             *              {@link PartyName }
             */
            public void setAddresseeName(PartyName value) {
                this.addresseeName = value;
            }

            /**
             * Gets the value of the addresseeContact property.
             *
             * @return possible object is
             *         {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeContact }
             */
            public ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeContact getAddresseeContact() {
                return addresseeContact;
            }

            /**
             * Sets the value of the addresseeContact property.
             *
             * @param value allowed object is
             *              {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeContact }
             */
            public void setAddresseeContact(ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeContact value) {
                this.addresseeContact = value;
            }

            /**
             * Gets the value of the addresseeEmail property.
             *
             * @return possible object is
             *         {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeEmail }
             */
            public ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeEmail getAddresseeEmail() {
                return addresseeEmail;
            }

            /**
             * Sets the value of the addresseeEmail property.
             *
             * @param value allowed object is
             *              {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeEmail }
             */
            public void setAddresseeEmail(ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeEmail value) {
                this.addresseeEmail = value;
            }

            /**
             * Gets the value of the datestamp property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getDatestamp() {
                return datestamp;
            }

            /**
             * Sets the value of the datestamp property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDatestamp(String value) {
                this.datestamp = value;
            }

            /**
             * Gets the value of the sourcetype property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcetype() {
                if (sourcetype == null) {
                    return "00";
                } else {
                    return sourcetype;
                }
            }

            /**
             * Sets the value of the sourcetype property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcetype(String value) {
                this.sourcetype = value;
            }

            /**
             * Gets the value of the sourcename property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcename() {
                return sourcename;
            }

            /**
             * Sets the value of the sourcename property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcename(String value) {
                this.sourcename = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;simpleContent>
             *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *     &lt;/extension>
             *   &lt;/simpleContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "value"
            })
            public static class AddresseeContact {

                @XmlValue
                protected String value;
                @XmlAttribute
                protected String datestamp;
                @XmlAttribute
                protected String sourcetype;
                @XmlAttribute
                protected String sourcename;

                /**
                 * Gets the value of the value property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the datestamp property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getDatestamp() {
                    return datestamp;
                }

                /**
                 * Sets the value of the datestamp property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setDatestamp(String value) {
                    this.datestamp = value;
                }

                /**
                 * Gets the value of the sourcetype property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcetype() {
                    if (sourcetype == null) {
                        return "00";
                    } else {
                        return sourcetype;
                    }
                }

                /**
                 * Sets the value of the sourcetype property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcetype(String value) {
                    this.sourcetype = value;
                }

                /**
                 * Gets the value of the sourcename property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcename() {
                    return sourcename;
                }

                /**
                 * Sets the value of the sourcename property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcename(String value) {
                    this.sourcename = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;simpleContent>
             *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *     &lt;/extension>
             *   &lt;/simpleContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "value"
            })
            public static class AddresseeEmail {

                @XmlValue
                protected String value;
                @XmlAttribute
                protected String datestamp;
                @XmlAttribute
                protected String sourcetype;
                @XmlAttribute
                protected String sourcename;

                /**
                 * Gets the value of the value property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the datestamp property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getDatestamp() {
                    return datestamp;
                }

                /**
                 * Sets the value of the datestamp property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setDatestamp(String value) {
                    this.datestamp = value;
                }

                /**
                 * Gets the value of the sourcetype property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcetype() {
                    if (sourcetype == null) {
                        return "00";
                    } else {
                        return sourcetype;
                    }
                }

                /**
                 * Sets the value of the sourcetype property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcetype(String value) {
                    this.sourcetype = value;
                }

                /**
                 * Gets the value of the sourcename property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcename() {
                    return sourcename;
                }

                /**
                 * Sets the value of the sourcename property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcename(String value) {
                    this.sourcename = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="AddresseeIDType">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.editeur.org/onix-pl>AddresseeIDTypeCode">
             *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="IDTypeName" minOccurs="0">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="IDValue">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "addresseeIDType",
                    "idTypeName",
                    "idValue"
            })
            public static class AddresseeIdentifier {

                @XmlElement(name = "AddresseeIDType", namespace = "http://www.editeur.org/onix-pl", required = true)
                protected ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.AddresseeIDType addresseeIDType;
                @XmlElement(name = "IDTypeName", namespace = "http://www.editeur.org/onix-pl")
                protected ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDTypeName idTypeName;
                @XmlElement(name = "IDValue", namespace = "http://www.editeur.org/onix-pl", required = true)
                protected ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDValue idValue;
                @XmlAttribute
                protected String datestamp;
                @XmlAttribute
                protected String sourcetype;
                @XmlAttribute
                protected String sourcename;

                /**
                 * Gets the value of the addresseeIDType property.
                 *
                 * @return possible object is
                 *         {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.AddresseeIDType }
                 */
                public ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.AddresseeIDType getAddresseeIDType() {
                    return addresseeIDType;
                }

                /**
                 * Sets the value of the addresseeIDType property.
                 *
                 * @param value allowed object is
                 *              {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.AddresseeIDType }
                 */
                public void setAddresseeIDType(ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.AddresseeIDType value) {
                    this.addresseeIDType = value;
                }

                /**
                 * Gets the value of the idTypeName property.
                 *
                 * @return possible object is
                 *         {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDTypeName }
                 */
                public ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDTypeName getIDTypeName() {
                    return idTypeName;
                }

                /**
                 * Sets the value of the idTypeName property.
                 *
                 * @param value allowed object is
                 *              {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDTypeName }
                 */
                public void setIDTypeName(ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDTypeName value) {
                    this.idTypeName = value;
                }

                /**
                 * Gets the value of the idValue property.
                 *
                 * @return possible object is
                 *         {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDValue }
                 */
                public ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDValue getIDValue() {
                    return idValue;
                }

                /**
                 * Sets the value of the idValue property.
                 *
                 * @param value allowed object is
                 *              {@link ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDValue }
                 */
                public void setIDValue(ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDValue value) {
                    this.idValue = value;
                }

                /**
                 * Gets the value of the datestamp property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getDatestamp() {
                    return datestamp;
                }

                /**
                 * Sets the value of the datestamp property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setDatestamp(String value) {
                    this.datestamp = value;
                }

                /**
                 * Gets the value of the sourcetype property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcetype() {
                    if (sourcetype == null) {
                        return "00";
                    } else {
                        return sourcetype;
                    }
                }

                /**
                 * Sets the value of the sourcetype property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcetype(String value) {
                    this.sourcetype = value;
                }

                /**
                 * Gets the value of the sourcename property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcename() {
                    return sourcename;
                }

                /**
                 * Sets the value of the sourcename property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcename(String value) {
                    this.sourcename = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.editeur.org/onix-pl>AddresseeIDTypeCode">
                 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
                 *     &lt;/extension>
                 *   &lt;/simpleContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "value"
                })
                public static class AddresseeIDType {

                    @XmlValue
                    protected String value;
                    @XmlAttribute
                    protected String datestamp;
                    @XmlAttribute
                    protected String sourcetype;
                    @XmlAttribute
                    protected String sourcename;

                    /**
                     * A controlled value identifying a scheme from which an addressee identifier is taken.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the datestamp property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getDatestamp() {
                        return datestamp;
                    }

                    /**
                     * Sets the value of the datestamp property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDatestamp(String value) {
                        this.datestamp = value;
                    }

                    /**
                     * Gets the value of the sourcetype property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcetype() {
                        if (sourcetype == null) {
                            return "00";
                        } else {
                            return sourcetype;
                        }
                    }

                    /**
                     * Sets the value of the sourcetype property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcetype(String value) {
                        this.sourcetype = value;
                    }

                    /**
                     * Gets the value of the sourcename property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcename() {
                        return sourcename;
                    }

                    /**
                     * Sets the value of the sourcename property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcename(String value) {
                        this.sourcename = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
                 *     &lt;/extension>
                 *   &lt;/simpleContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "value"
                })
                public static class IDTypeName {

                    @XmlValue
                    protected String value;
                    @XmlAttribute
                    protected String datestamp;
                    @XmlAttribute
                    protected String sourcetype;
                    @XmlAttribute
                    protected String sourcename;

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the datestamp property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getDatestamp() {
                        return datestamp;
                    }

                    /**
                     * Sets the value of the datestamp property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDatestamp(String value) {
                        this.datestamp = value;
                    }

                    /**
                     * Gets the value of the sourcetype property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcetype() {
                        if (sourcetype == null) {
                            return "00";
                        } else {
                            return sourcetype;
                        }
                    }

                    /**
                     * Sets the value of the sourcetype property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcetype(String value) {
                        this.sourcetype = value;
                    }

                    /**
                     * Gets the value of the sourcename property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcename() {
                        return sourcename;
                    }

                    /**
                     * Sets the value of the sourcename property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcename(String value) {
                        this.sourcename = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
                 *     &lt;/extension>
                 *   &lt;/simpleContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "value"
                })
                public static class IDValue {

                    @XmlValue
                    protected String value;
                    @XmlAttribute
                    protected String datestamp;
                    @XmlAttribute
                    protected String sourcetype;
                    @XmlAttribute
                    protected String sourcename;

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the datestamp property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getDatestamp() {
                        return datestamp;
                    }

                    /**
                     * Sets the value of the datestamp property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDatestamp(String value) {
                        this.datestamp = value;
                    }

                    /**
                     * Gets the value of the sourcetype property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcetype() {
                        if (sourcetype == null) {
                            return "00";
                        } else {
                            return sourcetype;
                        }
                    }

                    /**
                     * Sets the value of the sourcetype property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcetype(String value) {
                        this.sourcetype = value;
                    }

                    /**
                     * Gets the value of the sourcename property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcename() {
                        return sourcename;
                    }

                    /**
                     * Sets the value of the sourcename property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcename(String value) {
                        this.sourcename = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "value"
        })
        public static class MessageNote {

            @XmlValue
            protected String value;
            @XmlAttribute
            protected String datestamp;
            @XmlAttribute
            protected String sourcetype;
            @XmlAttribute
            protected String sourcename;

            /**
             * Gets the value of the value property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the datestamp property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getDatestamp() {
                return datestamp;
            }

            /**
             * Sets the value of the datestamp property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDatestamp(String value) {
                this.datestamp = value;
            }

            /**
             * Gets the value of the sourcetype property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcetype() {
                if (sourcetype == null) {
                    return "00";
                } else {
                    return sourcetype;
                }
            }

            /**
             * Sets the value of the sourcetype property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcetype(String value) {
                this.sourcetype = value;
            }

            /**
             * Gets the value of the sourcename property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcename() {
                return sourcename;
            }

            /**
             * Sets the value of the sourcename property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcename(String value) {
                this.sourcename = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "value"
        })
        public static class MessageNumber {

            @XmlValue
            protected String value;
            @XmlAttribute
            protected String datestamp;
            @XmlAttribute
            protected String sourcetype;
            @XmlAttribute
            protected String sourcename;

            /**
             * Gets the value of the value property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the datestamp property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getDatestamp() {
                return datestamp;
            }

            /**
             * Sets the value of the datestamp property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDatestamp(String value) {
                this.datestamp = value;
            }

            /**
             * Gets the value of the sourcetype property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcetype() {
                if (sourcetype == null) {
                    return "00";
                } else {
                    return sourcetype;
                }
            }

            /**
             * Sets the value of the sourcetype property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcetype(String value) {
                this.sourcetype = value;
            }

            /**
             * Gets the value of the sourcename property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcename() {
                return sourcename;
            }

            /**
             * Sets the value of the sourcename property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcename(String value) {
                this.sourcename = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>int">
         *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "value"
        })
        public static class MessageRepeat {

            @XmlValue
            protected int value;
            @XmlAttribute
            protected String datestamp;
            @XmlAttribute
            protected String sourcetype;
            @XmlAttribute
            protected String sourcename;

            /**
             * Gets the value of the value property.
             */
            public int getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             */
            public void setValue(int value) {
                this.value = value;
            }

            /**
             * Gets the value of the datestamp property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getDatestamp() {
                return datestamp;
            }

            /**
             * Sets the value of the datestamp property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDatestamp(String value) {
                this.datestamp = value;
            }

            /**
             * Gets the value of the sourcetype property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcetype() {
                if (sourcetype == null) {
                    return "00";
                } else {
                    return sourcetype;
                }
            }

            /**
             * Sets the value of the sourcetype property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcetype(String value) {
                this.sourcetype = value;
            }

            /**
             * Gets the value of the sourcename property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcename() {
                return sourcename;
            }

            /**
             * Sets the value of the sourcename property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcename(String value) {
                this.sourcename = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="SenderIdentifier">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="SenderIDType">
         *                     &lt;complexType>
         *                       &lt;simpleContent>
         *                         &lt;extension base="&lt;http://www.editeur.org/onix-pl>SenderIDTypeCode">
         *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *                         &lt;/extension>
         *                       &lt;/simpleContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="IDTypeName" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;simpleContent>
         *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *                         &lt;/extension>
         *                       &lt;/simpleContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="IDValue">
         *                     &lt;complexType>
         *                       &lt;simpleContent>
         *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                           &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *                         &lt;/extension>
         *                       &lt;/simpleContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="SenderName" type="{http://www.editeur.org/onix-pl}PartyName" minOccurs="0"/>
         *         &lt;element name="SenderContact" minOccurs="0">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="SenderEmail" minOccurs="0">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "senderIdentifier",
                "senderName",
                "senderContact",
                "senderEmail"
        })
        public static class Sender {

            @XmlElement(name = "SenderIdentifier", namespace = "http://www.editeur.org/onix-pl", required = true)
            protected ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier senderIdentifier;
            @XmlElement(name = "SenderName", namespace = "http://www.editeur.org/onix-pl")
            protected PartyName senderName;
            @XmlElement(name = "SenderContact", namespace = "http://www.editeur.org/onix-pl")
            protected ONIXPublicationsLicenseMessage.Header.Sender.SenderContact senderContact;
            @XmlElement(name = "SenderEmail", namespace = "http://www.editeur.org/onix-pl")
            protected ONIXPublicationsLicenseMessage.Header.Sender.SenderEmail senderEmail;
            @XmlAttribute
            protected String datestamp;
            @XmlAttribute
            protected String sourcetype;
            @XmlAttribute
            protected String sourcename;

            /**
             * Gets the value of the senderIdentifier property.
             *
             * @return possible object is
             *         {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier }
             */
            public ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier getSenderIdentifier() {
                return senderIdentifier;
            }

            /**
             * Sets the value of the senderIdentifier property.
             *
             * @param value allowed object is
             *              {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier }
             */
            public void setSenderIdentifier(ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier value) {
                this.senderIdentifier = value;
            }

            /**
             * Gets the value of the senderName property.
             *
             * @return possible object is
             *         {@link PartyName }
             */
            public PartyName getSenderName() {
                return senderName;
            }

            /**
             * Sets the value of the senderName property.
             *
             * @param value allowed object is
             *              {@link PartyName }
             */
            public void setSenderName(PartyName value) {
                this.senderName = value;
            }

            /**
             * Gets the value of the senderContact property.
             *
             * @return possible object is
             *         {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderContact }
             */
            public ONIXPublicationsLicenseMessage.Header.Sender.SenderContact getSenderContact() {
                return senderContact;
            }

            /**
             * Sets the value of the senderContact property.
             *
             * @param value allowed object is
             *              {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderContact }
             */
            public void setSenderContact(ONIXPublicationsLicenseMessage.Header.Sender.SenderContact value) {
                this.senderContact = value;
            }

            /**
             * Gets the value of the senderEmail property.
             *
             * @return possible object is
             *         {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderEmail }
             */
            public ONIXPublicationsLicenseMessage.Header.Sender.SenderEmail getSenderEmail() {
                return senderEmail;
            }

            /**
             * Sets the value of the senderEmail property.
             *
             * @param value allowed object is
             *              {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderEmail }
             */
            public void setSenderEmail(ONIXPublicationsLicenseMessage.Header.Sender.SenderEmail value) {
                this.senderEmail = value;
            }

            /**
             * Gets the value of the datestamp property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getDatestamp() {
                return datestamp;
            }

            /**
             * Sets the value of the datestamp property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDatestamp(String value) {
                this.datestamp = value;
            }

            /**
             * Gets the value of the sourcetype property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcetype() {
                if (sourcetype == null) {
                    return "00";
                } else {
                    return sourcetype;
                }
            }

            /**
             * Sets the value of the sourcetype property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcetype(String value) {
                this.sourcetype = value;
            }

            /**
             * Gets the value of the sourcename property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcename() {
                return sourcename;
            }

            /**
             * Sets the value of the sourcename property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcename(String value) {
                this.sourcename = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;simpleContent>
             *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *     &lt;/extension>
             *   &lt;/simpleContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "value"
            })
            public static class SenderContact {

                @XmlValue
                protected String value;
                @XmlAttribute
                protected String datestamp;
                @XmlAttribute
                protected String sourcetype;
                @XmlAttribute
                protected String sourcename;

                /**
                 * Gets the value of the value property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the datestamp property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getDatestamp() {
                    return datestamp;
                }

                /**
                 * Sets the value of the datestamp property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setDatestamp(String value) {
                    this.datestamp = value;
                }

                /**
                 * Gets the value of the sourcetype property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcetype() {
                    if (sourcetype == null) {
                        return "00";
                    } else {
                        return sourcetype;
                    }
                }

                /**
                 * Sets the value of the sourcetype property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcetype(String value) {
                    this.sourcetype = value;
                }

                /**
                 * Gets the value of the sourcename property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcename() {
                    return sourcename;
                }

                /**
                 * Sets the value of the sourcename property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcename(String value) {
                    this.sourcename = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;simpleContent>
             *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *     &lt;/extension>
             *   &lt;/simpleContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "value"
            })
            public static class SenderEmail {

                @XmlValue
                protected String value;
                @XmlAttribute
                protected String datestamp;
                @XmlAttribute
                protected String sourcetype;
                @XmlAttribute
                protected String sourcename;

                /**
                 * Gets the value of the value property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the datestamp property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getDatestamp() {
                    return datestamp;
                }

                /**
                 * Sets the value of the datestamp property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setDatestamp(String value) {
                    this.datestamp = value;
                }

                /**
                 * Gets the value of the sourcetype property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcetype() {
                    if (sourcetype == null) {
                        return "00";
                    } else {
                        return sourcetype;
                    }
                }

                /**
                 * Sets the value of the sourcetype property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcetype(String value) {
                    this.sourcetype = value;
                }

                /**
                 * Gets the value of the sourcename property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcename() {
                    return sourcename;
                }

                /**
                 * Sets the value of the sourcename property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcename(String value) {
                    this.sourcename = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="SenderIDType">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.editeur.org/onix-pl>SenderIDTypeCode">
             *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="IDTypeName" minOccurs="0">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="IDValue">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "senderIDType",
                    "idTypeName",
                    "idValue"
            })
            public static class SenderIdentifier {

                @XmlElement(name = "SenderIDType", namespace = "http://www.editeur.org/onix-pl", required = true)
                protected ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.SenderIDType senderIDType;
                @XmlElement(name = "IDTypeName", namespace = "http://www.editeur.org/onix-pl")
                protected ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDTypeName idTypeName;
                @XmlElement(name = "IDValue", namespace = "http://www.editeur.org/onix-pl", required = true)
                protected ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDValue idValue;
                @XmlAttribute
                protected String datestamp;
                @XmlAttribute
                protected String sourcetype;
                @XmlAttribute
                protected String sourcename;

                /**
                 * Gets the value of the senderIDType property.
                 *
                 * @return possible object is
                 *         {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.SenderIDType }
                 */
                public ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.SenderIDType getSenderIDType() {
                    return senderIDType;
                }

                /**
                 * Sets the value of the senderIDType property.
                 *
                 * @param value allowed object is
                 *              {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.SenderIDType }
                 */
                public void setSenderIDType(ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.SenderIDType value) {
                    this.senderIDType = value;
                }

                /**
                 * Gets the value of the idTypeName property.
                 *
                 * @return possible object is
                 *         {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDTypeName }
                 */
                public ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDTypeName getIDTypeName() {
                    return idTypeName;
                }

                /**
                 * Sets the value of the idTypeName property.
                 *
                 * @param value allowed object is
                 *              {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDTypeName }
                 */
                public void setIDTypeName(ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDTypeName value) {
                    this.idTypeName = value;
                }

                /**
                 * Gets the value of the idValue property.
                 *
                 * @return possible object is
                 *         {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDValue }
                 */
                public ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDValue getIDValue() {
                    return idValue;
                }

                /**
                 * Sets the value of the idValue property.
                 *
                 * @param value allowed object is
                 *              {@link ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDValue }
                 */
                public void setIDValue(ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDValue value) {
                    this.idValue = value;
                }

                /**
                 * Gets the value of the datestamp property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getDatestamp() {
                    return datestamp;
                }

                /**
                 * Sets the value of the datestamp property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setDatestamp(String value) {
                    this.datestamp = value;
                }

                /**
                 * Gets the value of the sourcetype property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcetype() {
                    if (sourcetype == null) {
                        return "00";
                    } else {
                        return sourcetype;
                    }
                }

                /**
                 * Sets the value of the sourcetype property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcetype(String value) {
                    this.sourcetype = value;
                }

                /**
                 * Gets the value of the sourcename property.
                 *
                 * @return possible object is
                 *         {@link String }
                 */
                public String getSourcename() {
                    return sourcename;
                }

                /**
                 * Sets the value of the sourcename property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setSourcename(String value) {
                    this.sourcename = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
                 *     &lt;/extension>
                 *   &lt;/simpleContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "value"
                })
                public static class IDTypeName {

                    @XmlValue
                    protected String value;
                    @XmlAttribute
                    protected String datestamp;
                    @XmlAttribute
                    protected String sourcetype;
                    @XmlAttribute
                    protected String sourcename;

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the datestamp property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getDatestamp() {
                        return datestamp;
                    }

                    /**
                     * Sets the value of the datestamp property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDatestamp(String value) {
                        this.datestamp = value;
                    }

                    /**
                     * Gets the value of the sourcetype property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcetype() {
                        if (sourcetype == null) {
                            return "00";
                        } else {
                            return sourcetype;
                        }
                    }

                    /**
                     * Sets the value of the sourcetype property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcetype(String value) {
                        this.sourcetype = value;
                    }

                    /**
                     * Gets the value of the sourcename property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcename() {
                        return sourcename;
                    }

                    /**
                     * Sets the value of the sourcename property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcename(String value) {
                        this.sourcename = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
                 *     &lt;/extension>
                 *   &lt;/simpleContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "value"
                })
                public static class IDValue {

                    @XmlValue
                    protected String value;
                    @XmlAttribute
                    protected String datestamp;
                    @XmlAttribute
                    protected String sourcetype;
                    @XmlAttribute
                    protected String sourcename;

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the datestamp property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getDatestamp() {
                        return datestamp;
                    }

                    /**
                     * Sets the value of the datestamp property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDatestamp(String value) {
                        this.datestamp = value;
                    }

                    /**
                     * Gets the value of the sourcetype property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcetype() {
                        if (sourcetype == null) {
                            return "00";
                        } else {
                            return sourcetype;
                        }
                    }

                    /**
                     * Sets the value of the sourcetype property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcetype(String value) {
                        this.sourcetype = value;
                    }

                    /**
                     * Gets the value of the sourcename property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcename() {
                        return sourcename;
                    }

                    /**
                     * Sets the value of the sourcename property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcename(String value) {
                        this.sourcename = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.editeur.org/onix-pl>SenderIDTypeCode">
                 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
                 *     &lt;/extension>
                 *   &lt;/simpleContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "value"
                })
                public static class SenderIDType {

                    @XmlValue
                    protected String value;
                    @XmlAttribute
                    protected String datestamp;
                    @XmlAttribute
                    protected String sourcetype;
                    @XmlAttribute
                    protected String sourcename;

                    /**
                     * A controlled value identifying a scheme from which a sender identifier is taken.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the datestamp property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getDatestamp() {
                        return datestamp;
                    }

                    /**
                     * Sets the value of the datestamp property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDatestamp(String value) {
                        this.datestamp = value;
                    }

                    /**
                     * Gets the value of the sourcetype property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcetype() {
                        if (sourcetype == null) {
                            return "00";
                        } else {
                            return sourcetype;
                        }
                    }

                    /**
                     * Sets the value of the sourcetype property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcetype(String value) {
                        this.sourcetype = value;
                    }

                    /**
                     * Gets the value of the sourcename property.
                     *
                     * @return possible object is
                     *         {@link String }
                     */
                    public String getSourcename() {
                        return sourcename;
                    }

                    /**
                     * Sets the value of the sourcename property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSourcename(String value) {
                        this.sourcename = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.editeur.org/onix-pl>DateOrDateTime">
         *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "value"
        })
        public static class SentDateTime {

            @XmlValue
            protected String value;
            @XmlAttribute
            protected String datestamp;
            @XmlAttribute
            protected String sourcetype;
            @XmlAttribute
            protected String sourcename;

            /**
             * Gets the value of the value property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the datestamp property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getDatestamp() {
                return datestamp;
            }

            /**
             * Sets the value of the datestamp property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDatestamp(String value) {
                this.datestamp = value;
            }

            /**
             * Gets the value of the sourcetype property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcetype() {
                if (sourcetype == null) {
                    return "00";
                } else {
                    return sourcetype;
                }
            }

            /**
             * Sets the value of the sourcetype property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcetype(String value) {
                this.sourcetype = value;
            }

            /**
             * Gets the value of the sourcename property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSourcename() {
                return sourcename;
            }

            /**
             * Sets the value of the sourcename property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourcename(String value) {
                this.sourcename = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;group ref="{http://www.editeur.org/onix-pl}PublicationsLicenseExpression-group"/>
     *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *       &lt;attribute name="version" type="{http://www.editeur.org/onix-pl}VersionList" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "expressionDetail",
            "licenseDetail",
            "definitions",
            "licenseGrant",
            "usageTerms",
            "supplyTerms",
            "continuingAccessTerms",
            "paymentTerms",
            "generalTerms",
            "licenseDocumentText"
    })
    public static class PublicationsLicenseExpression {

        @XmlElement(name = "ExpressionDetail", namespace = "http://www.editeur.org/onix-pl", required = true)
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ExpressionDetail expressionDetail;
        @XmlElement(name = "LicenseDetail", namespace = "http://www.editeur.org/onix-pl", required = true)
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDetail licenseDetail;
        @XmlElement(name = "Definitions", namespace = "http://www.editeur.org/onix-pl", required = true)
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.Definitions definitions;
        @XmlElement(name = "LicenseGrant", namespace = "http://www.editeur.org/onix-pl")
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseGrant licenseGrant;
        @XmlElement(name = "UsageTerms", namespace = "http://www.editeur.org/onix-pl", required = true)
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.UsageTerms usageTerms;
        @XmlElement(name = "SupplyTerms", namespace = "http://www.editeur.org/onix-pl")
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.SupplyTerms supplyTerms;
        @XmlElement(name = "ContinuingAccessTerms", namespace = "http://www.editeur.org/onix-pl")
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ContinuingAccessTerms continuingAccessTerms;
        @XmlElement(name = "PaymentTerms", namespace = "http://www.editeur.org/onix-pl")
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.PaymentTerms paymentTerms;
        @XmlElement(name = "GeneralTerms", namespace = "http://www.editeur.org/onix-pl")
        protected org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.GeneralTerms generalTerms;
        @XmlElement(name = "LicenseDocumentText", namespace = "http://www.editeur.org/onix-pl")
        protected List<org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDocumentText> licenseDocumentText;
        @XmlAttribute
        protected String version;
        @XmlAttribute
        protected String datestamp;
        @XmlAttribute
        protected String sourcetype;
        @XmlAttribute
        protected String sourcename;

        /**
         * Gets the value of the expressionDetail property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ExpressionDetail }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ExpressionDetail getExpressionDetail() {
            return expressionDetail;
        }

        /**
         * Sets the value of the expressionDetail property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ExpressionDetail }
         */
        public void setExpressionDetail(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ExpressionDetail value) {
            this.expressionDetail = value;
        }

        /**
         * Gets the value of the licenseDetail property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDetail }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDetail getLicenseDetail() {
            return licenseDetail;
        }

        /**
         * Sets the value of the licenseDetail property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDetail }
         */
        public void setLicenseDetail(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDetail value) {
            this.licenseDetail = value;
        }

        /**
         * Gets the value of the definitions property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.Definitions }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.Definitions getDefinitions() {
            return definitions;
        }

        /**
         * Sets the value of the definitions property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.Definitions }
         */
        public void setDefinitions(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.Definitions value) {
            this.definitions = value;
        }

        /**
         * Gets the value of the licenseGrant property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseGrant }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseGrant getLicenseGrant() {
            return licenseGrant;
        }

        /**
         * Sets the value of the licenseGrant property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseGrant }
         */
        public void setLicenseGrant(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseGrant value) {
            this.licenseGrant = value;
        }

        /**
         * Gets the value of the usageTerms property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.UsageTerms }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.UsageTerms getUsageTerms() {
            return usageTerms;
        }

        /**
         * Sets the value of the usageTerms property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.UsageTerms }
         */
        public void setUsageTerms(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.UsageTerms value) {
            this.usageTerms = value;
        }

        /**
         * Gets the value of the supplyTerms property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.SupplyTerms }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.SupplyTerms getSupplyTerms() {
            return supplyTerms;
        }

        /**
         * Sets the value of the supplyTerms property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.SupplyTerms }
         */
        public void setSupplyTerms(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.SupplyTerms value) {
            this.supplyTerms = value;
        }

        /**
         * Gets the value of the continuingAccessTerms property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ContinuingAccessTerms }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ContinuingAccessTerms getContinuingAccessTerms() {
            return continuingAccessTerms;
        }

        /**
         * Sets the value of the continuingAccessTerms property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ContinuingAccessTerms }
         */
        public void setContinuingAccessTerms(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.ContinuingAccessTerms value) {
            this.continuingAccessTerms = value;
        }

        /**
         * Gets the value of the paymentTerms property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.PaymentTerms }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.PaymentTerms getPaymentTerms() {
            return paymentTerms;
        }

        /**
         * Sets the value of the paymentTerms property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.PaymentTerms }
         */
        public void setPaymentTerms(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.PaymentTerms value) {
            this.paymentTerms = value;
        }

        /**
         * Gets the value of the generalTerms property.
         *
         * @return possible object is
         *         {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.GeneralTerms }
         */
        public org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.GeneralTerms getGeneralTerms() {
            return generalTerms;
        }

        /**
         * Sets the value of the generalTerms property.
         *
         * @param value allowed object is
         *              {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.GeneralTerms }
         */
        public void setGeneralTerms(org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.GeneralTerms value) {
            this.generalTerms = value;
        }

        /**
         * Gets the value of the licenseDocumentText property.
         * <p/>
         * <p/>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the licenseDocumentText property.
         * <p/>
         * <p/>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLicenseDocumentText().add(newItem);
         * </pre>
         * <p/>
         * <p/>
         * <p/>
         * Objects of the following type(s) are allowed in the list
         * {@link org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDocumentText }
         */
        public List<org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDocumentText> getLicenseDocumentText() {
            if (licenseDocumentText == null) {
                licenseDocumentText = new ArrayList<org.kuali.ole.docstore.common.document.content.license.onixpl.PublicationsLicenseExpression.LicenseDocumentText>();
            }
            return this.licenseDocumentText;
        }

        /**
         * Gets the value of the version property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getVersion() {
            return version;
        }

        /**
         * Sets the value of the version property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setVersion(String value) {
            this.version = value;
        }

        /**
         * Gets the value of the datestamp property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getDatestamp() {
            return datestamp;
        }

        /**
         * Sets the value of the datestamp property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setDatestamp(String value) {
            this.datestamp = value;
        }

        /**
         * Gets the value of the sourcetype property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getSourcetype() {
            if (sourcetype == null) {
                return "00";
            } else {
                return sourcetype;
            }
        }

        /**
         * Sets the value of the sourcetype property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSourcetype(String value) {
            this.sourcetype = value;
        }

        /**
         * Gets the value of the sourcename property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getSourcename() {
            return sourcename;
        }

        /**
         * Sets the value of the sourcename property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSourcename(String value) {
            this.sourcename = value;
        }

    }

}
