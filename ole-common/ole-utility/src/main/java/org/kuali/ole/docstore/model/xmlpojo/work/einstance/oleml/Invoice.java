package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invoice complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="invoice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fundCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentFYCost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invoice", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "fundCode",
        "currentFYCost",
        "paymentStatus"
})
@XStreamAlias("invoice")
public class Invoice {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String fundCode;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String currentFYCost;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String paymentStatus;

    /**
     * Gets the value of the fundCode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getFundCode() {
        return fundCode;
    }

    /**
     * Sets the value of the fundCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFundCode(String value) {
        this.fundCode = value;
    }

    /**
     * Gets the value of the currentFYCost property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCurrentFYCost() {
        return currentFYCost;
    }

    /**
     * Sets the value of the currentFYCost property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCurrentFYCost(String value) {
        this.currentFYCost = value;
    }

    /**
     * Gets the value of the paymentStatus property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets the value of the paymentStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPaymentStatus(String value) {
        this.paymentStatus = value;
    }

}
