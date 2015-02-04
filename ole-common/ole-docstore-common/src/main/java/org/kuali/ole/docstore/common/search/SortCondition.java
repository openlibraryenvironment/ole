package org.kuali.ole.docstore.common.search;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sortCondition complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="sortCondition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sortField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sortOrder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sortCondition", propOrder = {
        "sortField",
        "sortOrder"
})
public class SortCondition {

    protected String sortField;
    protected String sortOrder;

    /**
     * Gets the value of the sortField property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * Sets the value of the sortField property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSortField(String value) {
        this.sortField = value;
    }

    /**
     * Gets the value of the sortOrder property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the value of the sortOrder property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSortOrder(String value) {
        this.sortOrder = value;
    }

}
