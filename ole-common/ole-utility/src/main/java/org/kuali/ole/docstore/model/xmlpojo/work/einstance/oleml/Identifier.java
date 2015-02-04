package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for identifier complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="identifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifierValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "identifier", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "identifierValue"
})
@XStreamAlias("identifier")
public class Identifier {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String identifierValue;
    @XmlAttribute(name = "source")
    protected String source;

    /**
     * Gets the value of the identifierValue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIdentifierValue() {
        return identifierValue;
    }

    /**
     * Sets the value of the identifierValue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIdentifierValue(String value) {
        this.identifierValue = value;
    }

    /**
     * Gets the value of the source property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSource(String value) {
        this.source = value;
    }

}
