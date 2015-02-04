
package org.kuali.ole.docstore.common.document.content.license.onixpl;

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
 * <p>Java class for LicenseDocumentReference complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="LicenseDocumentReference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocumentLabel">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SectionDesignation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SectionIdentifier" type="{http://www.editeur.org/onix-pl}SectionIdentifier" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LicenseDocumentReference", namespace = "http://www.editeur.org/onix-pl", propOrder = {
        "documentLabel",
        "sectionDesignation",
        "sectionIdentifier"
})
public class LicenseDocumentReference {

    @XmlElement(name = "DocumentLabel", namespace = "http://www.editeur.org/onix-pl", required = true)
    protected LicenseDocumentReference.DocumentLabel documentLabel;
    @XmlElement(name = "SectionDesignation", namespace = "http://www.editeur.org/onix-pl")
    protected LicenseDocumentReference.SectionDesignation sectionDesignation;
    @XmlElement(name = "SectionIdentifier", namespace = "http://www.editeur.org/onix-pl")
    protected SectionIdentifier sectionIdentifier;
    @XmlAttribute
    protected String datestamp;
    @XmlAttribute
    protected String sourcetype;
    @XmlAttribute
    protected String sourcename;

    /**
     * Gets the value of the documentLabel property.
     *
     * @return possible object is
     *         {@link LicenseDocumentReference.DocumentLabel }
     */
    public LicenseDocumentReference.DocumentLabel getDocumentLabel() {
        return documentLabel;
    }

    /**
     * Sets the value of the documentLabel property.
     *
     * @param value allowed object is
     *              {@link LicenseDocumentReference.DocumentLabel }
     */
    public void setDocumentLabel(LicenseDocumentReference.DocumentLabel value) {
        this.documentLabel = value;
    }

    /**
     * Gets the value of the sectionDesignation property.
     *
     * @return possible object is
     *         {@link LicenseDocumentReference.SectionDesignation }
     */
    public LicenseDocumentReference.SectionDesignation getSectionDesignation() {
        return sectionDesignation;
    }

    /**
     * Sets the value of the sectionDesignation property.
     *
     * @param value allowed object is
     *              {@link LicenseDocumentReference.SectionDesignation }
     */
    public void setSectionDesignation(LicenseDocumentReference.SectionDesignation value) {
        this.sectionDesignation = value;
    }

    /**
     * Gets the value of the sectionIdentifier property.
     *
     * @return possible object is
     *         {@link SectionIdentifier }
     */
    public SectionIdentifier getSectionIdentifier() {
        return sectionIdentifier;
    }

    /**
     * Sets the value of the sectionIdentifier property.
     *
     * @param value allowed object is
     *              {@link SectionIdentifier }
     */
    public void setSectionIdentifier(SectionIdentifier value) {
        this.sectionIdentifier = value;
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
    public static class DocumentLabel {

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
    public static class SectionDesignation {

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

}
