
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
 * <p>Java class for Annotation complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="Annotation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AnnotationType" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.editeur.org/onix-pl>AnnotationTypeCode">
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Authority" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AnnotationText">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataRevisionAttributes"/>
 *                 &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataFormatAttributes"/>
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
@XmlType(name = "Annotation", namespace = "http://www.editeur.org/onix-pl", propOrder = {
        "annotationType",
        "authority",
        "annotationText"
})
public class Annotation {

    @XmlElement(name = "AnnotationType", namespace = "http://www.editeur.org/onix-pl")
    protected Annotation.AnnotationType annotationType;
    @XmlElement(name = "Authority", namespace = "http://www.editeur.org/onix-pl")
    protected Annotation.Authority authority;
    @XmlElement(name = "AnnotationText", namespace = "http://www.editeur.org/onix-pl", required = true)
    protected Annotation.AnnotationText annotationText;
    @XmlAttribute
    protected String datestamp;
    @XmlAttribute
    protected String sourcetype;
    @XmlAttribute
    protected String sourcename;

    /**
     * Gets the value of the annotationType property.
     *
     * @return possible object is
     *         {@link Annotation.AnnotationType }
     */
    public Annotation.AnnotationType getAnnotationType() {
        return annotationType;
    }

    /**
     * Sets the value of the annotationType property.
     *
     * @param value allowed object is
     *              {@link Annotation.AnnotationType }
     */
    public void setAnnotationType(Annotation.AnnotationType value) {
        this.annotationType = value;
    }

    /**
     * Gets the value of the authority property.
     *
     * @return possible object is
     *         {@link Annotation.Authority }
     */
    public Annotation.Authority getAuthority() {
        return authority;
    }

    /**
     * Sets the value of the authority property.
     *
     * @param value allowed object is
     *              {@link Annotation.Authority }
     */
    public void setAuthority(Annotation.Authority value) {
        this.authority = value;
    }

    /**
     * Gets the value of the annotationText property.
     *
     * @return possible object is
     *         {@link Annotation.AnnotationText }
     */
    public Annotation.AnnotationText getAnnotationText() {
        return annotationText;
    }

    /**
     * Sets the value of the annotationText property.
     *
     * @param value allowed object is
     *              {@link Annotation.AnnotationText }
     */
    public void setAnnotationText(Annotation.AnnotationText value) {
        this.annotationText = value;
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
     *       &lt;attGroup ref="{http://www.editeur.org/onix-pl}dataFormatAttributes"/>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class AnnotationText {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String datestamp;
        @XmlAttribute
        protected String sourcetype;
        @XmlAttribute
        protected String sourcename;
        @XmlAttribute
        protected String language;

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

    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.editeur.org/onix-pl>AnnotationTypeCode">
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
    public static class AnnotationType {

        @XmlValue
        protected AnnotationTypeCode value;
        @XmlAttribute
        protected String datestamp;
        @XmlAttribute
        protected String sourcetype;
        @XmlAttribute
        protected String sourcename;

        /**
         * A controlled value specifying the type of an annotation.
         *
         * @return possible object is
         *         {@link AnnotationTypeCode }
         */
        public AnnotationTypeCode getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value allowed object is
         *              {@link AnnotationTypeCode }
         */
        public void setValue(AnnotationTypeCode value) {
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
    public static class Authority {

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
