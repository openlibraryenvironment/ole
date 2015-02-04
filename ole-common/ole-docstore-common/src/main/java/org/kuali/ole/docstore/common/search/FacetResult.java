package org.kuali.ole.docstore.common.search;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 2/26/14
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * <p>Java class for facetResult complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="facetResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="facetFields" type="{}facetField" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facetResult", propOrder = {
        "facetResultFields"
})
public class FacetResult {

    @XmlElementWrapper(name = "facetFields")
    @XmlElement(name = "facetField")
    protected List<FacetResultField> facetResultFields;

    /**
     * Gets the value of the facetFields property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the facetFields property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFacetFields().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link FacetResultField }
     */
    public List<FacetResultField> getFacetResultFields() {
        if (facetResultFields == null) {
            facetResultFields = new ArrayList<FacetResultField>();
        }
        return this.facetResultFields;
    }

}
