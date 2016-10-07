
package org.kuali.ole.gobi.response;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="PoLineNumber" type="{}PoLineNumber"/>
 *         &lt;element name="Error" type="{}ResponseError"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "poLineNumber",
    "error"
})
@XmlRootElement(name = "Response")
public class Response {

    @XmlElement(name = "PoLineNumber")
    protected String poLineNumber;
    @XmlElement(name = "Error")
    protected ResponseError error;

    /**
     * Gets the value of the poLineNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoLineNumber() {
        return poLineNumber;
    }

    /**
     * Sets the value of the poLineNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoLineNumber(String value) {
        this.poLineNumber = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseError }
     *     
     */
    public ResponseError getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseError }
     *     
     */
    public void setError(ResponseError value) {
        this.error = value;
    }

}
