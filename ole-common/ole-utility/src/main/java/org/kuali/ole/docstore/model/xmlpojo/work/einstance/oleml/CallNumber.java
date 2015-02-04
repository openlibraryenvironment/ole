package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for callNumber complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="callNumber">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="prefix" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="callNumberType" type="{http://ole.kuali.org/standards/ole-eInstance}callNumberType" minOccurs="0"/>
 *         &lt;element name="shelvingOrder" type="{http://ole.kuali.org/standards/ole-eInstance}shelvingOrder" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "callNumber", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "prefix",
        "number",
        "callNumberType",
        "shelvingOrder"
})
@XStreamAlias("callNumber")
public class CallNumber {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String prefix;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String number;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected CallNumberType callNumberType;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected ShelvingOrder shelvingOrder;

    /**
     * Gets the value of the prefix property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the value of the prefix property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrefix(String value) {
        this.prefix = value;
    }

    /**
     * Gets the value of the number property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the callNumberType property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumberType }
     */
    public CallNumberType getCallNumberType() {
        return callNumberType;
    }

    /**
     * Sets the value of the callNumberType property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumberType }
     */
    public void setCallNumberType(CallNumberType value) {
        this.callNumberType = value;
    }

    /**
     * Gets the value of the shelvingOrder property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.ShelvingOrder }
     */
    public ShelvingOrder getShelvingOrder() {
        return shelvingOrder;
    }

    /**
     * Sets the value of the shelvingOrder property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.ShelvingOrder }
     */
    public void setShelvingOrder(ShelvingOrder value) {
        this.shelvingOrder = value;
    }

}
