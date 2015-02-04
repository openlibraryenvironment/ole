
package org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 5/30/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for SectionIdentifier complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="SectionIdentifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SectionIDType">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.editeur.org/onix-pl>SectionIDTypeCode">
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IDValue">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SectionIdentifier", namespace = "http://www.editeur.org/onix-pl", propOrder = {
        "sectionIDType",
        "idValue"
})
public class SectionIdentifier {

    @XmlElement(name = "SectionIDType", namespace = "http://www.editeur.org/onix-pl", required = true)
    protected SectionIdentifier.SectionIDType sectionIDType;
    @XmlElement(name = "IDValue", namespace = "http://www.editeur.org/onix-pl", required = true)
    protected SectionIdentifier.IDValue idValue;
    @XmlAttribute
    protected String datestamp;
    @XmlAttribute
    protected String sourcetype;
    @XmlAttribute
    protected String sourcename;

    /**
     * Gets the value of the sectionIDType property.
     *
     * @return possible object is
     *         {@link SectionIdentifier.SectionIDType }
     */
    public SectionIdentifier.SectionIDType getSectionIDType() {
        return sectionIDType;
    }

    /**
     * Sets the value of the sectionIDType property.
     *
     * @param value allowed object is
     *              {@link SectionIdentifier.SectionIDType }
     */
    public void setSectionIDType(SectionIdentifier.SectionIDType value) {
        this.sectionIDType = value;
    }

    /**
     * Gets the value of the idValue property.
     *
     * @return possible object is
     *         {@link SectionIdentifier.IDValue }
     */
    public SectionIdentifier.IDValue getIDValue() {
        return idValue;
    }

    /**
     * Sets the value of the idValue property.
     *
     * @param value allowed object is
     *              {@link SectionIdentifier.IDValue }
     */
    public void setIDValue(SectionIdentifier.IDValue value) {
        this.idValue = value;
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


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class IDValue {

        @XmlValue
        protected String value;
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


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.editeur.org/onix-pl>SectionIDTypeCode">
     *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class SectionIDType {

        @XmlValue
        protected SectionIDTypeCode value;
        @XmlAttribute
        protected String datestamp;
        @XmlAttribute
        protected String sourcetype;
        @XmlAttribute
        protected String sourcename;

        /**
         * A controlled value specifying a scheme from which a document section identifier is taken.
         *
         * @return possible object is
         *         {@link SectionIDTypeCode }
         */
        public SectionIDTypeCode getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value allowed object is
         *              {@link SectionIDTypeCode }
         */
        public void setValue(SectionIDTypeCode value) {
            this.value = value;
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

}
