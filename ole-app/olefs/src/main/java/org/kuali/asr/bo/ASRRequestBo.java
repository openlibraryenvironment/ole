package org.kuali.asr.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 1/6/14
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "asrRequestBo")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class ASRRequestBo {
    @XmlElement(name = "itemNumber")
    @JsonProperty("itemNumber")
    private String itemBarcode;
    @XmlElement(name = "pickupLocation")
    @JsonProperty("pickupLocation")
    private String pickupLocation;
    @XmlElement()
    @JsonProperty()
    private String patronBarcode;
    @XmlElement(name = "patronName")
    @JsonProperty("patronName")
    private String patronName;
    @XmlElement(name = "requestNote")
    @JsonProperty("requestNote")
    private String requestNote;
    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public String getRequestNote() {
        return requestNote;
    }

    public void setRequestNote(String requestNote) {
        this.requestNote = requestNote;
    }
}
