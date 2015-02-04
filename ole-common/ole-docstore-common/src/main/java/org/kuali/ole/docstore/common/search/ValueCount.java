package org.kuali.ole.docstore.common.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 2/26/14
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * <p>Java class for valueCount complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="valueCount">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="count" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "valueCount", propOrder = {
        "count",
        "value",
        "fullValue",
        "facetLabel"
})
public class ValueCount {

    protected String count;
    protected String value;
    protected String fullValue;
    protected String facetLabel;

    public String getFullValue() {
        return URLEncoder.encode(fullValue);
    }

    public void setFullValue(String fullValue) {
        this.fullValue = fullValue;
    }



    /**
     * Gets the value of the count property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCount(String value) {
        this.count = value;
    }

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

    public String getFacetLabel() {
        return this.value + " (" + this.count + ")";
    }

    public void setFacetLabel(String facetLabel) {
        this.facetLabel = facetLabel;
    }
}
