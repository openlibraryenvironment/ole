package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for typeOrSource complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="typeOrSource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pointer">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="http://www.test.com/metuens/montis"/>
 *               &lt;enumeration value="http://www.your.com/tempestatesque/e"/>
 *               &lt;enumeration value="http://www.test.com/gero/nimborum"/>
 *               &lt;enumeration value="http://www.test.org/montis/flammato"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "typeOrSource", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "pointer",
        "text"
})
@XStreamAlias("typeOrSource")
public class TypeOrSource {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String pointer;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String text;

    /**
     * Gets the value of the pointer property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPointer() {
        return pointer;
    }

    /**
     * Sets the value of the pointer property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPointer(String value) {
        this.pointer = value;
    }

    /**
     * Gets the value of the text property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setText(String value) {
        this.text = value;
    }

}
