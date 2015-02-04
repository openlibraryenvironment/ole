
package org.kuali.ole.docstore.model.xmlpojo.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Mapping complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="Mapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="include" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="exclude" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mapping", propOrder = {
        "include",
        "exclude"
})
public class Mapping {

    @XmlElement(required = true)
    protected String include;
    @XmlElement(required = true)
    protected String exclude;
    @XmlAttribute
    protected String type;

    /**
     * Gets the value of the include property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getInclude() {
        return include;
    }

    /**
     * Sets the value of the include property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInclude(String value) {
        this.include = value;
    }

    /**
     * Gets the value of the exclude property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExclude() {
        return exclude;
    }

    /**
     * Sets the value of the exclude property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExclude(String value) {
        this.exclude = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

}
