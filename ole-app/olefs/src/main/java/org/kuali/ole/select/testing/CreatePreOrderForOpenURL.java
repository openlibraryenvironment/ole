
package org.kuali.ole.select.testing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createPreOrderForOpenURL complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="createPreOrderForOpenURL">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="openUrlString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="routeRequestorReceipt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsFirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsLastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsZipCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorsSMS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestorType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createPreOrderForOpenURL", propOrder = {
        "openUrlString",
        "routeRequestorReceipt",
        "requestorsNote",
/*
 * "requestorsFirstName", "requestorsLastName", "requestorsAddress1", "requestorsAddress2", "requestorsCity", "requestorsState",
 * "requestorsZipCode", "requestorsCountryCode", "requestorsPhone", "requestorsEmail", "requestorsSMS",
 */
        "requestorType", "requestorId"
})
public class CreatePreOrderForOpenURL {

    protected String openUrlString;
    protected String routeRequestorReceipt;
    protected String requestorsNote;
    /*
     * protected String requestorsFirstName; protected String requestorsLastName; protected String requestorsAddress1; protected
     * String requestorsAddress2; protected String requestorsCity; protected String requestorsState; protected String
     * requestorsZipCode; protected String requestorsCountryCode; protected String requestorsPhone; protected String
     * requestorsEmail; protected String requestorsSMS;
     */
    protected String requestorType;
    protected String requestorId;

    /**
     * Gets the value of the openUrlString property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getOpenUrlString() {
        return openUrlString;
    }

    /**
     * Sets the value of the openUrlString property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOpenUrlString(String openUrlString) {
        this.openUrlString = openUrlString;
    }

    /**
     * Gets the value of the routeRequestorReceipt property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRouteRequestorReceipt() {
        return routeRequestorReceipt;
    }

    /**
     * Sets the value of the routeRequestorReceipt property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRouteRequestorReceipt(String routeRequestorReceipt) {
        this.routeRequestorReceipt = routeRequestorReceipt;
    }

    /**
     * Gets the value of the requestorsNote property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRequestorsNote() {
        return requestorsNote;
    }

    /**
     * Sets the value of the requestorsNote property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestorsNote(String requestorsNote) {
        this.requestorsNote = requestorsNote;
    }

    /**
     * Gets the value of the requestorsFirstName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    /*
     * public String getRequestorsFirstName() { return requestorsFirstName; }
     *//**
     * Sets the value of the requestorsFirstName property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsFirstName(String requestorsFirstName) { this.requestorsFirstName = requestorsFirstName; }
     *//**
     * Gets the value of the requestorsLastName property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsLastName() { return requestorsLastName; }
     *//**
     * Sets the value of the requestorsLastName property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsLastName(String requestorsLastName) { this.requestorsLastName = requestorsLastName; }
     *//**
     * Gets the value of the requestorsAddress1 property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsAddress1() { return requestorsAddress1; }
     *//**
     * Sets the value of the requestorsAddress1 property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsAddress1(String requestorsAddress1) { this.requestorsAddress1 = requestorsAddress1; }
     *//**
     * Gets the value of the requestorsAddress2 property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsAddress2() { return requestorsAddress2; }
     *//**
     * Sets the value of the requestorsAddress2 property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsAddress2(String requestorsAddress2) { this.requestorsAddress2 = requestorsAddress2; }
     *//**
     * Gets the value of the requestorsCity property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsCity() { return requestorsCity; }
     *//**
     * Sets the value of the requestorsCity property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsCity(String requestorsCity) { this.requestorsCity = requestorsCity; }
     *//**
     * Gets the value of the requestorsState property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsState() { return requestorsState; }
     *//**
     * Sets the value of the requestorsState property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsState(String requestorsState) { this.requestorsState = requestorsState; }
     *//**
     * Gets the value of the requestorsZipCode property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsZipCode() { return requestorsZipCode; }
     *//**
     * Sets the value of the requestorsZipCode property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsZipCode(String requestorsZipCode) { this.requestorsZipCode = requestorsZipCode; }
     *//**
     * Gets the value of the requestorsCountryCode property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsCountryCode() { return requestorsCountryCode; }
     *//**
     * Sets the value of the requestorsCountryCode property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsCountryCode(String requestorsCountryCode) { this.requestorsCountryCode = requestorsCountryCode; }
     *//**
     * Gets the value of the requestorsPhone property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsPhone() { return requestorsPhone; }
     *//**
     * Sets the value of the requestorsPhone property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsPhone(String requestorsPhone) { this.requestorsPhone = requestorsPhone; }
     *//**
     * Gets the value of the requestorsEmail property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsEmail() { return requestorsEmail; }
     *//**
     * Sets the value of the requestorsEmail property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsEmail(String requestorsEmail) { this.requestorsEmail = requestorsEmail; }
     *//**
     * Gets the value of the requestorsSMS property.
     *
     * @return possible object is {@link String }
     */
    /*
     * public String getRequestorsSMS() { return requestorsSMS; }
     *//**
     * Sets the value of the requestorsSMS property.
     *
     * @param value allowed object is {@link String }
     */
    /*
     * public void setRequestorsSMS(String requestorsSMS) { this.requestorsSMS = requestorsSMS; }
     */

    /**
     * Gets the value of the requestorType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRequestorType() {
        return requestorType;
    }

    /**
     * Sets the value of the requestorType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestorType(String requestorType) {
        this.requestorType = requestorType;
    }

    /**
     * Gets the requestorId attribute.
     *
     * @return Returns the requestorId.
     */
    public String getRequestorId() {
        return requestorId;
    }

    /**
     * Sets the requestorId attribute value.
     *
     * @param requestorId The requestorId to set.
     */
    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

}
