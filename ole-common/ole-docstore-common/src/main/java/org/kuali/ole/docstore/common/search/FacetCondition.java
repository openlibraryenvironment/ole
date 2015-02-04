package org.kuali.ole.docstore.common.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 2/26/14
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * <p>Java class for facetCondition complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="facetCondition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="facetField" type="{}facetField" minOccurs="0"/>
 *         &lt;element name="facetFieldPageSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facetCondition", propOrder = {
        "docType",
        "fieldName",
        "fieldValue",
        "shortValue"
})
public class FacetCondition {

    protected String docType;
    protected String fieldName;
    protected String fieldValue;
    protected String shortValue;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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

    public String getShortValue() {
        if(fieldValue.contains("/r/n!@#$")) {
            int index = fieldValue.indexOf("/r/n!@#$");
            shortValue = fieldValue.substring(index + 8, fieldValue.length());
        }
        else {
            shortValue = fieldValue;
        }
        return shortValue;
    }

    public void setShortValue(String shortValue) {
        this.shortValue = shortValue;
    }
}
