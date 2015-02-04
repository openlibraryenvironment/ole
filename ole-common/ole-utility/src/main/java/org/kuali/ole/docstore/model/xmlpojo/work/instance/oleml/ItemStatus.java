
package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for itemStatus complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="itemStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fullValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemStatus", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
        "codeValue",
        "fullValue"
})
public class ItemStatus {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String codeValue;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String fullValue;

    /**
     * Gets the value of the codeValue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCodeValue() {
        return codeValue;
    }

    /**
     * Sets the value of the codeValue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodeValue(String value) {
        this.codeValue = value;
    }

    /**
     * Gets the value of the fullValue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getFullValue() {
        return fullValue;
    }

    /**
     * Sets the value of the fullValue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFullValue(String value) {
        this.fullValue = value;
    }

}
