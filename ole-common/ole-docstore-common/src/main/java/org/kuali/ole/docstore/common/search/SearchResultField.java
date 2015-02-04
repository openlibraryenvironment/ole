package org.kuali.ole.docstore.common.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for searchResultField complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="searchResultField">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="docType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fieldValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchResultField", propOrder = {
        "docType",
        "fieldName",
        "fieldValue"
})
public class SearchResultField {

    protected String docType;
    protected String fieldName;
    protected String fieldValue;

    /**
     * Gets the value of the docType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDocType(String value) {
        this.docType = value;
    }

    /**
     * Gets the value of the fieldName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the value of the fieldName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    /**
     * Gets the value of the fieldValue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * Sets the value of the fieldValue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFieldValue(String value) {
        this.fieldValue = value;
    }
}
