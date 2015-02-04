package org.kuali.ole.docstore.common.search;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for searchCondition complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="searchCondition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="searchField" type="{}searchField" minOccurs="0"/>
 *         &lt;element name="searchScope" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchCondition", propOrder = {
        "operator",
        "searchField",
        "searchScope"
})
public class SearchCondition {

    protected String operator;
    protected SearchField searchField;
    protected String searchScope;

    /**
     * Gets the value of the operator property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOperator(String value) {
        this.operator = value;
    }

    /**
     * Gets the value of the searchField property.
     *
     * @return
     *     possible object is
     *     {@link SearchField }
     *
     */
    public SearchField getSearchField() {
        return searchField;
    }

    /**
     * Sets the value of the searchField property.
     *
     * @param value
     *     allowed object is
     *     {@link SearchField }
     *
     */
    public void setSearchField(SearchField value) {
        this.searchField = value;
    }

    /**
     * Gets the value of the searchScope property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSearchScope() {
        return searchScope;
    }

    /**
     * Sets the value of the searchScope property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSearchScope(String value) {
        this.searchScope = value;
    }

}
