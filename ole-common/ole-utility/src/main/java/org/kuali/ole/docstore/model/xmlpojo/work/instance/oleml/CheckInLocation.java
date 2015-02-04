
package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;

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
@XmlType(name = "checkInLocation", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
        "name",
        "count",
        "inHouseCount"
})
@XStreamAlias("checkInLocation")
public class CheckInLocation {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String name;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String count;

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String inHouseCount;

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
     * Gets the value of the inHouseCount property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getInHouseCount() {
        return inHouseCount;
    }

    /**
     * Sets the value of the inHouseCount property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInHouseCount(String value) {
        this.inHouseCount = value;
    }
}
