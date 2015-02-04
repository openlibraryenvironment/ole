package org.kuali.ole.systemintegration.rest.bo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;

import javax.xml.bind.annotation.*;

/**
 * Created by sheiksalahudeenm on 7/1/14.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unboundLocation", propOrder = {"locationLevel"})
@XStreamAlias("unboundLocation")
public class UnboundLocation {

    @XmlElement(required = true)
    protected LocationLevel locationLevel;
    @XmlAttribute
    @XStreamAsAttribute
    protected String primary;
    @XmlAttribute
    @XStreamAsAttribute
    protected String status;

    /**
     * Gets the value of the locationLevel property.
     *
     * @return possible object is
     *         {@link LocationLevel }
     */
    public LocationLevel getLocationLevel() {
        return locationLevel;
    }

    /**
     * Sets the value of the locationLevel property.
     *
     * @param value allowed object is
     *              {@link LocationLevel }
     */
    public void setLocationLevel(LocationLevel value) {
        this.locationLevel = value;
    }

    /**
     * @return
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * @param primary
     */
    public void setPrimary(String primary) {
        this.primary = primary;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
