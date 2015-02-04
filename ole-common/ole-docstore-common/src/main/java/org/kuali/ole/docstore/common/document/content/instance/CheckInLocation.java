package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Created by IntelliJ IDEA.
 * User: vivekb
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for checkInLocation complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="checkInLocation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="count" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkInLocation", propOrder = {
        "name",
        "count",
        "inHouseCount"
})
@XStreamAlias("checkInLocation")
public class CheckInLocation {

    @XmlElement(name = "name", required = true)
    protected String name;
    @XmlElement(name = "count", required = true)
    protected Integer count;

    @XmlElement(name = "inHouseCount", required = true)
    protected Integer inHouseCount;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the count property.
     *
     * @return possible object is
     *         {@link String }
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCount(Integer value) {
        this.count = value;
    }

    /**
     * Gets the value of the inHouseCount property.
     *
     * @return possible object is
     *         {@link String }
     */
    public Integer getInHouseCount() {
        return inHouseCount;
    }

    /**
     * Sets the value of the inHouseCount property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInHouseCount(Integer value) {
        this.inHouseCount = value;
    }
}
