package org.kuali.ole.docstore.common.document.content.instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.krad.service.KRADServiceLocator;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for locationLevel complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="locationLevel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="level" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="locationLevel" type="{http://ole.kuali.org/standards/ole-instance}locationLevel" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "locationLevel", propOrder = {
        "name",
        "level",
        "code",
        "fullName",
        "locationLevel",

})
@XStreamAlias("locationLevel")
public class LocationLevel {
    @XmlElement
    protected String code;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String level;
    //@XmlElement(name = "locationLevel")
    protected LocationLevel locationLevel;
    @XmlElement(required = true)
    protected String fullName;

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
        //fullName = findOutFullName();
    }

    /**
     * Gets the value of the level property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLevel(String value) {
        this.level = value;
    }

    /**
     * Gets the value of the locationLevel property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.common.document.content.instance.LocationLevel }
     */
    public LocationLevel getLocationLevel() {
        return locationLevel;
    }

    /**
     * Sets the value of the locationLevel property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.common.document.content.instance.LocationLevel }
     */
    public void setLocationLevel(LocationLevel value) {
        this.locationLevel = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /**
     * 
     * Finds the full name for the location code
     *
     * @return The full name as string
     */
    private String findOutFullName(){
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("locationCode", name);
        List<OleLocation> oleLocation = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class, criteria);

        if (oleLocation.size() > 0){
            return oleLocation.get(0).getLocationName();
        }
        return null;
    }
}
