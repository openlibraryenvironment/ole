package org.kuali.ole.docstore.common.search;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for searchResult complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="searchResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="searchResultFields" type="{}searchResultField" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchResult", propOrder = {
        "searchResultFields"
})
public class SearchResult {

    @XmlElementWrapper(name = "searchResultFields")
    @XmlElement(name = "searchResultField")
    protected List<SearchResultField> searchResultFields;

    /**
     * Gets the value of the searchResultFields property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchResultFields property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchResultFields().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchResultField }
     *
     *
     */
    public List<SearchResultField> getSearchResultFields() {
        if (searchResultFields == null) {
            searchResultFields = new ArrayList<SearchResultField>();
        }
        return this.searchResultFields;
    }

}
