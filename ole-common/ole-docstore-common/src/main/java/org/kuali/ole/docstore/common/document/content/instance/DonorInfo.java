package org.kuali.ole.docstore.common.document.content.instance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for donorInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="donorInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="donorCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="donorPublicDisplay" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="donorNote" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "donorInfo", propOrder = {
    "donorCode",
    "donorPublicDisplay",
    "donorNote"
})
public class DonorInfo {

    @XmlElement(required = true)
    protected String donorCode;
    @XmlElement(required = true)
    protected String donorPublicDisplay;
    @XmlElement(required = true)
    protected String donorNote;

    /**
     * Gets the value of the donorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDonorCode() {
        return donorCode;
    }

    /**
     * Sets the value of the donorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDonorCode(String value) {
        this.donorCode = value;
    }

    /**
     * Gets the value of the donorPublicDisplay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDonorPublicDisplay() {
        return donorPublicDisplay;
    }

    /**
     * Sets the value of the donorPublicDisplay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDonorPublicDisplay(String value) {
        this.donorPublicDisplay = value;
    }

    /**
     * Gets the value of the donorNote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDonorNote() {
        return donorNote;
    }

    /**
     * Sets the value of the donorNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDonorNote(String value) {
        this.donorNote = value;
    }

}
