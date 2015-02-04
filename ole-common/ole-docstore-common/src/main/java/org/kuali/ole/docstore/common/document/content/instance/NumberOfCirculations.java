package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: vivekb
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for numberOfCirculations complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="numberOfCirculations">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="checkInLocation" type="{http://ole.kuali.org/standards/ole-instance}checkInLocation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "numberOfCirculations", propOrder = {"checkInLocation"})
@XStreamAlias("numberOfCirculations")
public class NumberOfCirculations {

    @XStreamImplicit(itemFieldName = "checkInLocation")
    protected List<CheckInLocation> checkInLocation;

    /**
     * Gets the value of the checkInLocation property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the checkInLocation property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCheckInLocation().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link CheckInLocation }
     */
    public List<CheckInLocation> getCheckInLocation() {
        if (checkInLocation == null) {
            checkInLocation = new ArrayList<CheckInLocation>();
        }
        return this.checkInLocation;
    }

    public void setCheckInLocation(List<CheckInLocation> checkInLocation) {
        this.checkInLocation = checkInLocation;
    }
}
