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
 * <p>Java class for facetField complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="facetField">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="docType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="filedNmae" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valueCounts" type="{}valueCount" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FacetResultField", propOrder = {
        "fieldName",
        "valueCounts",
        "fieldLabel",
        "totalCount"
})
public class FacetResultField {


    protected String fieldName;
    protected String fieldLabel;
    @XmlElementWrapper(name = "valueCounts")
    @XmlElement(name = "valueCount")
    protected List<ValueCount> valueCounts;
    protected int totalCount;



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
     * Sets the value of the filedNmae property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    public String getFieldLabel() {
        return fieldName.replace("_facet", "");
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    /**
     * Gets the value of the valueCounts property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueCounts property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueCounts().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueCount }
     */
    public List<ValueCount> getValueCounts() {
        if (valueCounts == null) {
            valueCounts = new ArrayList<ValueCount>();
        }
        return this.valueCounts;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
