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
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class ASRRequestDetailBo {
    @XmlElement(name="holdId")
    @JsonProperty("holdId")
    private String requestId;
    @XmlElement(name = "patronBarcode")
    @JsonProperty("patronBarcode")
    private String patronId;
    @XmlElement
    @JsonProperty
    private boolean available;
    @XmlElement
    @JsonProperty
    private boolean recallStatus;
    @XmlElement
    @JsonProperty
    private String dateExpires;
    @XmlElement
    @JsonProperty
    private String dateCreated;
    @XmlElement
    @JsonProperty
    private String priority;
    @XmlElement
    @JsonProperty
    private String requestType;
    @XmlElement
    @JsonProperty
    private String pickupLocation;
    @XmlElement
    @JsonProperty
    private String dateRecalled;
    @XmlElement
    @JsonProperty
    private String dateAvailable;
    @XmlElement
    @JsonProperty
    private String dateAvailableExpires;
    @XmlElement
    @JsonProperty
    private String requestStatus;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isRecallStatus() {
        return recallStatus;
    }

    public void setRecallStatus(boolean recallStatus) {
        this.recallStatus = recallStatus;
    }

    public String getDateExpires() {
        return dateExpires;
    }

    public void setDateExpires(String dateExpires) {
        this.dateExpires = dateExpires;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDateRecalled() {
        return dateRecalled;
    }

    public void setDateRecalled(String dateRecalled) {
        this.dateRecalled = dateRecalled;
    }

    public String getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(String dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public String getDateAvailableExpires() {
        return dateAvailableExpires;
    }

    public void setDateAvailableExpires(String dateAvailableExpires) {
        this.dateAvailableExpires = dateAvailableExpires;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
