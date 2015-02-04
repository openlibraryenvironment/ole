package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for callNumberType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="callNumberType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fullValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="typeOrSource" type="{http://ole.kuali.org/standards/ole-eInstance}typeOrSource"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "callNumberType", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "codeValue",
        "fullValue",
        "typeOrSource"
})
@XStreamAlias("callNumberType")
public class CallNumberType {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String codeValue;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String fullValue;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected TypeOrSource typeOrSource;

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

    /**
     * Gets the value of the typeOrSource property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.TypeOrSource }
     */
    public TypeOrSource getTypeOrSource() {
        return typeOrSource;
    }

    /**
     * Sets the value of the typeOrSource property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.TypeOrSource }
     */
    public void setTypeOrSource(TypeOrSource value) {
        this.typeOrSource = value;
    }

}
