
package org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 5/30/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for LicenseDescription complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="LicenseDescription">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataFormatAttributes"/>
 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LicenseDescription", namespace = "http://www.editeur.org/onix-pl", propOrder = {
        "value"
})
public class LicenseDescription {

    @XmlValue
    protected String value;
    @XmlAttribute
    protected String language;
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
     * Gets the value of the language property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLanguage(String value) {
        this.language = value;
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
