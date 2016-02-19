
package org.kuali.ole.gobi.datobjects;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CustomerDetail">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BaseAccount" type="{}BaseAccount"/>
 *                   &lt;element name="SubAccount" type="{}SubAccount"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Order">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ListedElectronicMonograph">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}collection"/>
 *                             &lt;element name="OrderDetail">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
 *                                       &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
 *                                       &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
 *                                       &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
 *                                       &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
 *                                       &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
 *                                       &lt;element name="Location" type="{}Location" minOccurs="0"/>
 *                                       &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
 *                                       &lt;element name="ListPrice">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                                                 &lt;element name="Currency" type="{}Currency"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Description" type="{}LD-Description"/>
 *                                                 &lt;element name="Value" type="{}LD-Value"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="PurchaseOption">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
 *                                                 &lt;element name="Code" type="{}PO-Code"/>
 *                                                 &lt;element name="Description" type="{}PO-Description"/>
 *                                                 &lt;element name="VendorCode" type="{}PO-VendorCode"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ListedElectronicSerial">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}collection"/>
 *                             &lt;element name="OrderDetail">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
 *                                       &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
 *                                       &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
 *                                       &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
 *                                       &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
 *                                       &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
 *                                       &lt;element name="ListPrice">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                                                 &lt;element name="Currency" type="{}Currency"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="PurchaseOption">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
 *                                                 &lt;element name="Code" type="{}PO-Code"/>
 *                                                 &lt;element name="Description" type="{}PO-Description"/>
 *                                                 &lt;element name="VendorCode" type="{}PO-VendorCode"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="UnlistedElectronicMonograph">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}collection"/>
 *                             &lt;element name="OrderDetail">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
 *                                       &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
 *                                       &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
 *                                       &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
 *                                       &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
 *                                       &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
 *                                       &lt;element name="Location" type="{}Location" minOccurs="0"/>
 *                                       &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
 *                                       &lt;element name="ListPrice" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                                                 &lt;element name="Currency" type="{}Currency"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Description" type="{}LD-Description"/>
 *                                                 &lt;element name="Value" type="{}LD-Value"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="PurchaseOption" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
 *                                                 &lt;element name="Code" type="{}PO-Code"/>
 *                                                 &lt;element name="Description" type="{}PO-Description"/>
 *                                                 &lt;element name="VendorCode" type="{}PO-VendorCode"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ListedPrintMonograph">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}collection"/>
 *                             &lt;element name="OrderDetail">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
 *                                       &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
 *                                       &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
 *                                       &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
 *                                       &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
 *                                       &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
 *                                       &lt;element name="Location" type="{}Location" minOccurs="0"/>
 *                                       &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
 *                                       &lt;element name="ListPrice">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                                                 &lt;element name="Currency" type="{}Currency"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Description" type="{}LD-Description"/>
 *                                                 &lt;element name="Value" type="{}LD-Value"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ListedPrintSerial">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}collection"/>
 *                             &lt;element name="OrderDetail">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
 *                                       &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
 *                                       &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
 *                                       &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
 *                                       &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
 *                                       &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
 *                                       &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="UnlistedPrintMonograph">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}collection"/>
 *                             &lt;element name="OrderDetail">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
 *                                       &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
 *                                       &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
 *                                       &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
 *                                       &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
 *                                       &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
 *                                       &lt;element name="Location" type="{}Location" minOccurs="0"/>
 *                                       &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
 *                                       &lt;element name="ListPrice" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                                                 &lt;element name="Currency" type="{}Currency"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Description" type="{}LD-Description"/>
 *                                                 &lt;element name="Value" type="{}LD-Value"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="UnlistedPrintSerial">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}collection"/>
 *                             &lt;element name="OrderDetail">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
 *                                       &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
 *                                       &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
 *                                       &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
 *                                       &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
 *                                       &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                       &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
 *                                       &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "customerDetail",
        "order"
})
@XmlRootElement(name = "PurchaseOrder")
public class PurchaseOrder {

    @XmlElement(name = "CustomerDetail", required = true)
    protected PurchaseOrder.CustomerDetail customerDetail;
    @XmlElement(name = "Order", required = true)
    protected PurchaseOrder.Order order;

    /**
     * Gets the value of the customerDetail property.
     *
     * @return possible object is
     * {@link PurchaseOrder.CustomerDetail }
     */
    public PurchaseOrder.CustomerDetail getCustomerDetail() {
        return customerDetail;
    }

    /**
     * Sets the value of the customerDetail property.
     *
     * @param value allowed object is
     *              {@link PurchaseOrder.CustomerDetail }
     */
    public void setCustomerDetail(PurchaseOrder.CustomerDetail value) {
        this.customerDetail = value;
    }

    /**
     * Gets the value of the order property.
     *
     * @return possible object is
     * {@link PurchaseOrder.Order }
     */
    public PurchaseOrder.Order getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     *
     * @param value allowed object is
     *              {@link PurchaseOrder.Order }
     */
    public void setOrder(PurchaseOrder.Order value) {
        this.order = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BaseAccount" type="{}BaseAccount"/>
     *         &lt;element name="SubAccount" type="{}SubAccount"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "baseAccount",
            "subAccount"
    })
    public static class CustomerDetail {

        @XmlElement(name = "BaseAccount")
        protected String baseAccount;
        @XmlElement(name = "SubAccount")
        protected String subAccount;

        /**
         * Gets the value of the baseAccount property.
         */
        public String getBaseAccount() {
            return baseAccount;
        }

        /**
         * Sets the value of the baseAccount property.
         */
        public void setBaseAccount(String value) {
            this.baseAccount = value;
        }

        /**
         * Gets the value of the subAccount property.
         */
        public String getSubAccount() {
            return subAccount;
        }

        /**
         * Sets the value of the subAccount property.
         */
        public void setSubAccount(String value) {
            this.subAccount = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="ListedElectronicMonograph">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}collection"/>
     *                   &lt;element name="OrderDetail">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
     *                             &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
     *                             &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
     *                             &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
     *                             &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
     *                             &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
     *                             &lt;element name="Location" type="{}Location" minOccurs="0"/>
     *                             &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
     *                             &lt;element name="ListPrice">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                                       &lt;element name="Currency" type="{}Currency"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Description" type="{}LD-Description"/>
     *                                       &lt;element name="Value" type="{}LD-Value"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="PurchaseOption">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
     *                                       &lt;element name="Code" type="{}PO-Code"/>
     *                                       &lt;element name="Description" type="{}PO-Description"/>
     *                                       &lt;element name="VendorCode" type="{}PO-VendorCode"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ListedElectronicSerial">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}collection"/>
     *                   &lt;element name="OrderDetail">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
     *                             &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
     *                             &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
     *                             &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
     *                             &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
     *                             &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
     *                             &lt;element name="ListPrice">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                                       &lt;element name="Currency" type="{}Currency"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="PurchaseOption">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
     *                                       &lt;element name="Code" type="{}PO-Code"/>
     *                                       &lt;element name="Description" type="{}PO-Description"/>
     *                                       &lt;element name="VendorCode" type="{}PO-VendorCode"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="UnlistedElectronicMonograph">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}collection"/>
     *                   &lt;element name="OrderDetail">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
     *                             &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
     *                             &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
     *                             &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
     *                             &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
     *                             &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
     *                             &lt;element name="Location" type="{}Location" minOccurs="0"/>
     *                             &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
     *                             &lt;element name="ListPrice" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                                       &lt;element name="Currency" type="{}Currency"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Description" type="{}LD-Description"/>
     *                                       &lt;element name="Value" type="{}LD-Value"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="PurchaseOption" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
     *                                       &lt;element name="Code" type="{}PO-Code"/>
     *                                       &lt;element name="Description" type="{}PO-Description"/>
     *                                       &lt;element name="VendorCode" type="{}PO-VendorCode"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ListedPrintMonograph">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}collection"/>
     *                   &lt;element name="OrderDetail">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
     *                             &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
     *                             &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
     *                             &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
     *                             &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
     *                             &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
     *                             &lt;element name="Location" type="{}Location" minOccurs="0"/>
     *                             &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
     *                             &lt;element name="ListPrice">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                                       &lt;element name="Currency" type="{}Currency"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Description" type="{}LD-Description"/>
     *                                       &lt;element name="Value" type="{}LD-Value"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ListedPrintSerial">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}collection"/>
     *                   &lt;element name="OrderDetail">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
     *                             &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
     *                             &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
     *                             &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
     *                             &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
     *                             &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
     *                             &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="UnlistedPrintMonograph">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}collection"/>
     *                   &lt;element name="OrderDetail">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
     *                             &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
     *                             &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
     *                             &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
     *                             &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
     *                             &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
     *                             &lt;element name="Location" type="{}Location" minOccurs="0"/>
     *                             &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
     *                             &lt;element name="ListPrice" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                                       &lt;element name="Currency" type="{}Currency"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Description" type="{}LD-Description"/>
     *                                       &lt;element name="Value" type="{}LD-Value"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="UnlistedPrintSerial">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}collection"/>
     *                   &lt;element name="OrderDetail">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
     *                             &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
     *                             &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
     *                             &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
     *                             &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
     *                             &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                             &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
     *                             &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "listedElectronicMonograph",
            "listedElectronicSerial",
            "unlistedElectronicMonograph",
            "listedPrintMonograph",
            "listedPrintSerial",
            "unlistedPrintMonograph",
            "unlistedPrintSerial"
    })
    public static class Order {

        @XmlElement(name = "ListedElectronicMonograph")
        protected PurchaseOrder.Order.ListedElectronicMonograph listedElectronicMonograph;
        @XmlElement(name = "ListedElectronicSerial")
        protected PurchaseOrder.Order.ListedElectronicSerial listedElectronicSerial;
        @XmlElement(name = "UnlistedElectronicMonograph")
        protected PurchaseOrder.Order.UnlistedElectronicMonograph unlistedElectronicMonograph;
        @XmlElement(name = "ListedPrintMonograph")
        protected PurchaseOrder.Order.ListedPrintMonograph listedPrintMonograph;
        @XmlElement(name = "ListedPrintSerial")
        protected PurchaseOrder.Order.ListedPrintSerial listedPrintSerial;
        @XmlElement(name = "UnlistedPrintMonograph")
        protected PurchaseOrder.Order.UnlistedPrintMonograph unlistedPrintMonograph;
        @XmlElement(name = "UnlistedPrintSerial")
        protected PurchaseOrder.Order.UnlistedPrintSerial unlistedPrintSerial;

        /**
         * Gets the value of the listedElectronicMonograph property.
         *
         * @return possible object is
         * {@link PurchaseOrder.Order.ListedElectronicMonograph }
         */
        public PurchaseOrder.Order.ListedElectronicMonograph getListedElectronicMonograph() {
            return listedElectronicMonograph;
        }

        /**
         * Sets the value of the listedElectronicMonograph property.
         *
         * @param value allowed object is
         *              {@link PurchaseOrder.Order.ListedElectronicMonograph }
         */
        public void setListedElectronicMonograph(PurchaseOrder.Order.ListedElectronicMonograph value) {
            this.listedElectronicMonograph = value;
        }

        /**
         * Gets the value of the listedElectronicSerial property.
         *
         * @return possible object is
         * {@link PurchaseOrder.Order.ListedElectronicSerial }
         */
        public PurchaseOrder.Order.ListedElectronicSerial getListedElectronicSerial() {
            return listedElectronicSerial;
        }

        /**
         * Sets the value of the listedElectronicSerial property.
         *
         * @param value allowed object is
         *              {@link PurchaseOrder.Order.ListedElectronicSerial }
         */
        public void setListedElectronicSerial(PurchaseOrder.Order.ListedElectronicSerial value) {
            this.listedElectronicSerial = value;
        }

        /**
         * Gets the value of the unlistedElectronicMonograph property.
         *
         * @return possible object is
         * {@link PurchaseOrder.Order.UnlistedElectronicMonograph }
         */
        public PurchaseOrder.Order.UnlistedElectronicMonograph getUnlistedElectronicMonograph() {
            return unlistedElectronicMonograph;
        }

        /**
         * Sets the value of the unlistedElectronicMonograph property.
         *
         * @param value allowed object is
         *              {@link PurchaseOrder.Order.UnlistedElectronicMonograph }
         */
        public void setUnlistedElectronicMonograph(PurchaseOrder.Order.UnlistedElectronicMonograph value) {
            this.unlistedElectronicMonograph = value;
        }

        /**
         * Gets the value of the listedPrintMonograph property.
         *
         * @return possible object is
         * {@link PurchaseOrder.Order.ListedPrintMonograph }
         */
        public PurchaseOrder.Order.ListedPrintMonograph getListedPrintMonograph() {
            return listedPrintMonograph;
        }

        /**
         * Sets the value of the listedPrintMonograph property.
         *
         * @param value allowed object is
         *              {@link PurchaseOrder.Order.ListedPrintMonograph }
         */
        public void setListedPrintMonograph(PurchaseOrder.Order.ListedPrintMonograph value) {
            this.listedPrintMonograph = value;
        }

        /**
         * Gets the value of the listedPrintSerial property.
         *
         * @return possible object is
         * {@link PurchaseOrder.Order.ListedPrintSerial }
         */
        public PurchaseOrder.Order.ListedPrintSerial getListedPrintSerial() {
            return listedPrintSerial;
        }

        /**
         * Sets the value of the listedPrintSerial property.
         *
         * @param value allowed object is
         *              {@link PurchaseOrder.Order.ListedPrintSerial }
         */
        public void setListedPrintSerial(PurchaseOrder.Order.ListedPrintSerial value) {
            this.listedPrintSerial = value;
        }

        /**
         * Gets the value of the unlistedPrintMonograph property.
         *
         * @return possible object is
         * {@link PurchaseOrder.Order.UnlistedPrintMonograph }
         */
        public PurchaseOrder.Order.UnlistedPrintMonograph getUnlistedPrintMonograph() {
            return unlistedPrintMonograph;
        }

        /**
         * Sets the value of the unlistedPrintMonograph property.
         *
         * @param value allowed object is
         *              {@link PurchaseOrder.Order.UnlistedPrintMonograph }
         */
        public void setUnlistedPrintMonograph(PurchaseOrder.Order.UnlistedPrintMonograph value) {
            this.unlistedPrintMonograph = value;
        }

        /**
         * Gets the value of the unlistedPrintSerial property.
         *
         * @return possible object is
         * {@link PurchaseOrder.Order.UnlistedPrintSerial }
         */
        public PurchaseOrder.Order.UnlistedPrintSerial getUnlistedPrintSerial() {
            return unlistedPrintSerial;
        }

        /**
         * Sets the value of the unlistedPrintSerial property.
         *
         * @param value allowed object is
         *              {@link PurchaseOrder.Order.UnlistedPrintSerial }
         */
        public void setUnlistedPrintSerial(PurchaseOrder.Order.UnlistedPrintSerial value) {
            this.unlistedPrintSerial = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{}collection"/>
         *         &lt;element name="OrderDetail">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
         *                   &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
         *                   &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
         *                   &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
         *                   &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
         *                   &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
         *                   &lt;element name="Location" type="{}Location" minOccurs="0"/>
         *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
         *                   &lt;element name="ListPrice">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *                             &lt;element name="Currency" type="{}Currency"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Description" type="{}LD-Description"/>
         *                             &lt;element name="Value" type="{}LD-Value"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="PurchaseOption">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
         *                             &lt;element name="Code" type="{}PO-Code"/>
         *                             &lt;element name="Description" type="{}PO-Description"/>
         *                             &lt;element name="VendorCode" type="{}PO-VendorCode"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "collection",
                "orderDetail"
        })
        public static class ListedElectronicMonograph {

            @XmlElement(required = true, nillable = true)
            protected CollectionType collection;
            @XmlElement(name = "OrderDetail", required = true)
            protected PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail orderDetail;

            /**
             * This is the MARC record object container.
             *
             * @return possible object is
             * {@link CollectionType }
             */
            public CollectionType getCollection() {
                return collection;
            }

            /**
             * Sets the value of the collection property.
             *
             * @param value allowed object is
             *              {@link CollectionType }
             */
            public void setCollection(CollectionType value) {
                this.collection = value;
            }

            /**
             * Gets the value of the orderDetail property.
             *
             * @return possible object is
             * {@link PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail }
             */
            public PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail getOrderDetail() {
                return orderDetail;
            }

            /**
             * Sets the value of the orderDetail property.
             *
             * @param value allowed object is
             *              {@link PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail }
             */
            public void setOrderDetail(PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail value) {
                this.orderDetail = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
             *         &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
             *         &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
             *         &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
             *         &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
             *         &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
             *         &lt;element name="Location" type="{}Location" minOccurs="0"/>
             *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
             *         &lt;element name="ListPrice">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
             *                   &lt;element name="Currency" type="{}Currency"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Description" type="{}LD-Description"/>
             *                   &lt;element name="Value" type="{}LD-Value"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="PurchaseOption">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
             *                   &lt;element name="Code" type="{}PO-Code"/>
             *                   &lt;element name="Description" type="{}PO-Description"/>
             *                   &lt;element name="VendorCode" type="{}PO-VendorCode"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "batchPONumber",
                    "itemPONumber",
                    "fundCode",
                    "mappedFundCode",
                    "orderNotes",
                    "otherLocalId",
                    "location",
                    "quantity",
                    "ybpOrderKey",
                    "orderPlaced",
                    "initials",
                    "listPrice",
                    "localData",
                    "purchaseOption"
            })
            public static class OrderDetail {

                @XmlElement(name = "BatchPONumber")
                protected String batchPONumber;
                @XmlElement(name = "ItemPONumber")
                protected String itemPONumber;
                @XmlElement(name = "FundCode")
                protected String fundCode;
                @XmlElement(name = "MappedFundCode")
                protected String mappedFundCode;
                @XmlElement(name = "OrderNotes")
                protected String orderNotes;
                @XmlElement(name = "OtherLocalId")
                protected String otherLocalId;
                @XmlElement(name = "Location")
                protected String location;
                @XmlElement(name = "Quantity", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger quantity;
                @XmlElement(name = "YBPOrderKey", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger ybpOrderKey;
                @XmlElement(name = "OrderPlaced", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar orderPlaced;
                @XmlElement(name = "Initials")
                protected String initials;
                @XmlElement(name = "ListPrice", required = true)
                protected PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.ListPrice listPrice;
                @XmlElement(name = "LocalData")
                protected List<LocalData> localData;
                @XmlElement(name = "PurchaseOption", required = true)
                protected PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.PurchaseOption purchaseOption;

                /**
                 * Gets the value of the batchPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getBatchPONumber() {
                    return batchPONumber;
                }

                /**
                 * Sets the value of the batchPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setBatchPONumber(String value) {
                    this.batchPONumber = value;
                }

                /**
                 * Gets the value of the itemPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getItemPONumber() {
                    return itemPONumber;
                }

                /**
                 * Sets the value of the itemPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setItemPONumber(String value) {
                    this.itemPONumber = value;
                }

                /**
                 * Gets the value of the fundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getFundCode() {
                    return fundCode;
                }

                /**
                 * Sets the value of the fundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setFundCode(String value) {
                    this.fundCode = value;
                }

                /**
                 * Gets the value of the mappedFundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getMappedFundCode() {
                    return mappedFundCode;
                }

                /**
                 * Sets the value of the mappedFundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setMappedFundCode(String value) {
                    this.mappedFundCode = value;
                }

                /**
                 * Gets the value of the orderNotes property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOrderNotes() {
                    return orderNotes;
                }

                /**
                 * Sets the value of the orderNotes property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOrderNotes(String value) {
                    this.orderNotes = value;
                }

                /**
                 * Gets the value of the otherLocalId property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOtherLocalId() {
                    return otherLocalId;
                }

                /**
                 * Sets the value of the otherLocalId property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOtherLocalId(String value) {
                    this.otherLocalId = value;
                }

                /**
                 * Gets the value of the location property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getLocation() {
                    return location;
                }

                /**
                 * Sets the value of the location property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setLocation(String value) {
                    this.location = value;
                }

                /**
                 * Gets the value of the quantity property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getQuantity() {
                    return quantity;
                }

                /**
                 * Sets the value of the quantity property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setQuantity(BigInteger value) {
                    this.quantity = value;
                }

                /**
                 * Gets the value of the ybpOrderKey property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getYBPOrderKey() {
                    return ybpOrderKey;
                }

                /**
                 * Sets the value of the ybpOrderKey property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setYBPOrderKey(BigInteger value) {
                    this.ybpOrderKey = value;
                }

                /**
                 * Gets the value of the orderPlaced property.
                 *
                 * @return possible object is
                 * {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public XMLGregorianCalendar getOrderPlaced() {
                    return orderPlaced;
                }

                /**
                 * Sets the value of the orderPlaced property.
                 *
                 * @param value allowed object is
                 *              {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public void setOrderPlaced(XMLGregorianCalendar value) {
                    this.orderPlaced = value;
                }

                /**
                 * Gets the value of the initials property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getInitials() {
                    return initials;
                }

                /**
                 * Sets the value of the initials property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setInitials(String value) {
                    this.initials = value;
                }

                /**
                 * Gets the value of the listPrice property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.ListPrice }
                 */
                public PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.ListPrice getListPrice() {
                    return listPrice;
                }

                /**
                 * Sets the value of the listPrice property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.ListPrice }
                 */
                public void setListPrice(PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.ListPrice value) {
                    this.listPrice = value;
                }

                /**
                 * Gets the value of the localData property.
                 * <p/>
                 * <p/>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the localData property.
                 * <p/>
                 * <p/>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getLocalData().add(newItem);
                 * </pre>
                 * <p/>
                 * <p/>
                 * <p/>
                 * Objects of the following type(s) are allowed in the list
                 * {@link PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.LocalData }
                 */
                public List<LocalData> getLocalData() {
                    if (localData == null) {
                        localData = new ArrayList<LocalData>();
                    }
                    return this.localData;
                }

                /**
                 * Gets the value of the purchaseOption property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.PurchaseOption }
                 */
                public PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.PurchaseOption getPurchaseOption() {
                    return purchaseOption;
                }

                /**
                 * Sets the value of the purchaseOption property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.PurchaseOption }
                 */
                public void setPurchaseOption(PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.PurchaseOption value) {
                    this.purchaseOption = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
                 *         &lt;element name="Currency" type="{}Currency"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "amount",
                        "currency"
                })
                public static class ListPrice {

                    @XmlElement(name = "Amount", required = true)
                    protected BigDecimal amount;
                    @XmlElement(name = "Currency", required = true)
                    protected String currency;

                    /**
                     * Gets the value of the amount property.
                     *
                     * @return possible object is
                     * {@link java.math.BigDecimal }
                     */
                    public BigDecimal getAmount() {
                        return amount;
                    }

                    /**
                     * Sets the value of the amount property.
                     *
                     * @param value allowed object is
                     *              {@link java.math.BigDecimal }
                     */
                    public void setAmount(BigDecimal value) {
                        this.amount = value;
                    }

                    /**
                     * Gets the value of the currency property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCurrency() {
                        return currency;
                    }

                    /**
                     * Sets the value of the currency property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCurrency(String value) {
                        this.currency = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Description" type="{}LD-Description"/>
                 *         &lt;element name="Value" type="{}LD-Value"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "description",
                        "value"
                })
                public static class LocalData {

                    @XmlElement(name = "Description", required = true)
                    protected String description;
                    @XmlElement(name = "Value", required = true)
                    protected String value;

                    /**
                     * Gets the value of the description property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Sets the value of the description property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     * {@link String }
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

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
                 *         &lt;element name="Code" type="{}PO-Code"/>
                 *         &lt;element name="Description" type="{}PO-Description"/>
                 *         &lt;element name="VendorCode" type="{}PO-VendorCode"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "vendorPOCode",
                        "code",
                        "description",
                        "vendorCode"
                })
                public static class PurchaseOption {

                    @XmlElement(name = "VendorPOCode", required = true)
                    protected String vendorPOCode;
                    @XmlElement(name = "Code", required = true)
                    protected String code;
                    @XmlElement(name = "Description", required = true)
                    protected String description;
                    @XmlElement(name = "VendorCode", required = true)
                    protected String vendorCode;

                    /**
                     * Gets the value of the vendorPOCode property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getVendorPOCode() {
                        return vendorPOCode;
                    }

                    /**
                     * Sets the value of the vendorPOCode property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setVendorPOCode(String value) {
                        this.vendorPOCode = value;
                    }

                    /**
                     * Gets the value of the code property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCode() {
                        return code;
                    }

                    /**
                     * Sets the value of the code property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCode(String value) {
                        this.code = value;
                    }

                    /**
                     * Gets the value of the description property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Sets the value of the description property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    /**
                     * Gets the value of the vendorCode property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getVendorCode() {
                        return vendorCode;
                    }

                    /**
                     * Sets the value of the vendorCode property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setVendorCode(String value) {
                        this.vendorCode = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{}collection"/>
         *         &lt;element name="OrderDetail">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
         *                   &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
         *                   &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
         *                   &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
         *                   &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
         *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
         *                   &lt;element name="ListPrice">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *                             &lt;element name="Currency" type="{}Currency"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="PurchaseOption">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
         *                             &lt;element name="Code" type="{}PO-Code"/>
         *                             &lt;element name="Description" type="{}PO-Description"/>
         *                             &lt;element name="VendorCode" type="{}PO-VendorCode"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "collection",
                "orderDetail"
        })
        public static class ListedElectronicSerial {

            @XmlElement(required = true, nillable = true)
            protected CollectionType collection;
            @XmlElement(name = "OrderDetail", required = true)
            protected PurchaseOrder.Order.ListedElectronicSerial.OrderDetail orderDetail;

            /**
             * This is the MARC record object container.
             *
             * @return possible object is
             * {@link CollectionType }
             */
            public CollectionType getCollection() {
                return collection;
            }

            /**
             * Sets the value of the collection property.
             *
             * @param value allowed object is
             *              {@link CollectionType }
             */
            public void setCollection(CollectionType value) {
                this.collection = value;
            }

            /**
             * Gets the value of the orderDetail property.
             *
             * @return possible object is
             * {@link PurchaseOrder.Order.ListedElectronicSerial.OrderDetail }
             */
            public PurchaseOrder.Order.ListedElectronicSerial.OrderDetail getOrderDetail() {
                return orderDetail;
            }

            /**
             * Sets the value of the orderDetail property.
             *
             * @param value allowed object is
             *              {@link PurchaseOrder.Order.ListedElectronicSerial.OrderDetail }
             */
            public void setOrderDetail(PurchaseOrder.Order.ListedElectronicSerial.OrderDetail value) {
                this.orderDetail = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
             *         &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
             *         &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
             *         &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
             *         &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
             *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
             *         &lt;element name="ListPrice">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
             *                   &lt;element name="Currency" type="{}Currency"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="PurchaseOption">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
             *                   &lt;element name="Code" type="{}PO-Code"/>
             *                   &lt;element name="Description" type="{}PO-Description"/>
             *                   &lt;element name="VendorCode" type="{}PO-VendorCode"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "batchPONumber",
                    "itemPONumber",
                    "fundCode",
                    "mappedFundCode",
                    "orderNotes",
                    "quantity",
                    "ybpOrderKey",
                    "orderPlaced",
                    "initials",
                    "listPrice",
                    "purchaseOption"
            })
            public static class OrderDetail {

                @XmlElement(name = "BatchPONumber")
                protected String batchPONumber;
                @XmlElement(name = "ItemPONumber")
                protected String itemPONumber;
                @XmlElement(name = "FundCode")
                protected String fundCode;
                @XmlElement(name = "MappedFundCode")
                protected String mappedFundCode;
                @XmlElement(name = "OrderNotes")
                protected String orderNotes;
                @XmlElement(name = "Quantity", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger quantity;
                @XmlElement(name = "YBPOrderKey", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger ybpOrderKey;
                @XmlElement(name = "OrderPlaced", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar orderPlaced;
                @XmlElement(name = "Initials")
                protected String initials;
                @XmlElement(name = "ListPrice", required = true)
                protected PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.ListPrice listPrice;
                @XmlElement(name = "PurchaseOption", required = true)
                protected PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.PurchaseOption purchaseOption;

                /**
                 * Gets the value of the batchPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getBatchPONumber() {
                    return batchPONumber;
                }

                /**
                 * Sets the value of the batchPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setBatchPONumber(String value) {
                    this.batchPONumber = value;
                }

                /**
                 * Gets the value of the itemPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getItemPONumber() {
                    return itemPONumber;
                }

                /**
                 * Sets the value of the itemPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setItemPONumber(String value) {
                    this.itemPONumber = value;
                }

                /**
                 * Gets the value of the fundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getFundCode() {
                    return fundCode;
                }

                /**
                 * Sets the value of the fundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setFundCode(String value) {
                    this.fundCode = value;
                }

                /**
                 * Gets the value of the mappedFundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getMappedFundCode() {
                    return mappedFundCode;
                }

                /**
                 * Sets the value of the mappedFundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setMappedFundCode(String value) {
                    this.mappedFundCode = value;
                }

                /**
                 * Gets the value of the orderNotes property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOrderNotes() {
                    return orderNotes;
                }

                /**
                 * Sets the value of the orderNotes property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOrderNotes(String value) {
                    this.orderNotes = value;
                }

                /**
                 * Gets the value of the quantity property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getQuantity() {
                    return quantity;
                }

                /**
                 * Sets the value of the quantity property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setQuantity(BigInteger value) {
                    this.quantity = value;
                }

                /**
                 * Gets the value of the ybpOrderKey property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getYBPOrderKey() {
                    return ybpOrderKey;
                }

                /**
                 * Sets the value of the ybpOrderKey property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setYBPOrderKey(BigInteger value) {
                    this.ybpOrderKey = value;
                }

                /**
                 * Gets the value of the orderPlaced property.
                 *
                 * @return possible object is
                 * {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public XMLGregorianCalendar getOrderPlaced() {
                    return orderPlaced;
                }

                /**
                 * Sets the value of the orderPlaced property.
                 *
                 * @param value allowed object is
                 *              {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public void setOrderPlaced(XMLGregorianCalendar value) {
                    this.orderPlaced = value;
                }

                /**
                 * Gets the value of the initials property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getInitials() {
                    return initials;
                }

                /**
                 * Sets the value of the initials property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setInitials(String value) {
                    this.initials = value;
                }

                /**
                 * Gets the value of the listPrice property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.ListPrice }
                 */
                public PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.ListPrice getListPrice() {
                    return listPrice;
                }

                /**
                 * Sets the value of the listPrice property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.ListPrice }
                 */
                public void setListPrice(PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.ListPrice value) {
                    this.listPrice = value;
                }

                /**
                 * Gets the value of the purchaseOption property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.PurchaseOption }
                 */
                public PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.PurchaseOption getPurchaseOption() {
                    return purchaseOption;
                }

                /**
                 * Sets the value of the purchaseOption property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.PurchaseOption }
                 */
                public void setPurchaseOption(PurchaseOrder.Order.ListedElectronicSerial.OrderDetail.PurchaseOption value) {
                    this.purchaseOption = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
                 *         &lt;element name="Currency" type="{}Currency"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "amount",
                        "currency"
                })
                public static class ListPrice {

                    @XmlElement(name = "Amount", required = true)
                    protected BigDecimal amount;
                    @XmlElement(name = "Currency", required = true)
                    protected String currency;

                    /**
                     * Gets the value of the amount property.
                     *
                     * @return possible object is
                     * {@link java.math.BigDecimal }
                     */
                    public BigDecimal getAmount() {
                        return amount;
                    }

                    /**
                     * Sets the value of the amount property.
                     *
                     * @param value allowed object is
                     *              {@link java.math.BigDecimal }
                     */
                    public void setAmount(BigDecimal value) {
                        this.amount = value;
                    }

                    /**
                     * Gets the value of the currency property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCurrency() {
                        return currency;
                    }

                    /**
                     * Sets the value of the currency property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCurrency(String value) {
                        this.currency = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
                 *         &lt;element name="Code" type="{}PO-Code"/>
                 *         &lt;element name="Description" type="{}PO-Description"/>
                 *         &lt;element name="VendorCode" type="{}PO-VendorCode"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "vendorPOCode",
                        "code",
                        "description",
                        "vendorCode"
                })
                public static class PurchaseOption {

                    @XmlElement(name = "VendorPOCode", required = true)
                    protected String vendorPOCode;
                    @XmlElement(name = "Code", required = true)
                    protected String code;
                    @XmlElement(name = "Description", required = true)
                    protected String description;
                    @XmlElement(name = "VendorCode", required = true)
                    protected String vendorCode;

                    /**
                     * Gets the value of the vendorPOCode property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getVendorPOCode() {
                        return vendorPOCode;
                    }

                    /**
                     * Sets the value of the vendorPOCode property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setVendorPOCode(String value) {
                        this.vendorPOCode = value;
                    }

                    /**
                     * Gets the value of the code property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCode() {
                        return code;
                    }

                    /**
                     * Sets the value of the code property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCode(String value) {
                        this.code = value;
                    }

                    /**
                     * Gets the value of the description property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Sets the value of the description property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    /**
                     * Gets the value of the vendorCode property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getVendorCode() {
                        return vendorCode;
                    }

                    /**
                     * Sets the value of the vendorCode property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setVendorCode(String value) {
                        this.vendorCode = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{}collection"/>
         *         &lt;element name="OrderDetail">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
         *                   &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
         *                   &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
         *                   &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
         *                   &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
         *                   &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
         *                   &lt;element name="Location" type="{}Location" minOccurs="0"/>
         *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
         *                   &lt;element name="ListPrice">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *                             &lt;element name="Currency" type="{}Currency"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Description" type="{}LD-Description"/>
         *                             &lt;element name="Value" type="{}LD-Value"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "collection",
                "orderDetail"
        })
        public static class ListedPrintMonograph {

            @XmlElement(required = true, nillable = true)
            protected CollectionType collection;
            @XmlElement(name = "OrderDetail", required = true)
            protected PurchaseOrder.Order.ListedPrintMonograph.OrderDetail orderDetail;

            /**
             * This is the MARC record object container.
             *
             * @return possible object is
             * {@link CollectionType }
             */
            public CollectionType getCollection() {
                return collection;
            }

            /**
             * Sets the value of the collection property.
             *
             * @param value allowed object is
             *              {@link CollectionType }
             */
            public void setCollection(CollectionType value) {
                this.collection = value;
            }

            /**
             * Gets the value of the orderDetail property.
             *
             * @return possible object is
             * {@link PurchaseOrder.Order.ListedPrintMonograph.OrderDetail }
             */
            public PurchaseOrder.Order.ListedPrintMonograph.OrderDetail getOrderDetail() {
                return orderDetail;
            }

            /**
             * Sets the value of the orderDetail property.
             *
             * @param value allowed object is
             *              {@link PurchaseOrder.Order.ListedPrintMonograph.OrderDetail }
             */
            public void setOrderDetail(PurchaseOrder.Order.ListedPrintMonograph.OrderDetail value) {
                this.orderDetail = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
             *         &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
             *         &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
             *         &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
             *         &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
             *         &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
             *         &lt;element name="Location" type="{}Location" minOccurs="0"/>
             *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
             *         &lt;element name="ListPrice">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
             *                   &lt;element name="Currency" type="{}Currency"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Description" type="{}LD-Description"/>
             *                   &lt;element name="Value" type="{}LD-Value"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "batchPONumber",
                    "itemPONumber",
                    "fundCode",
                    "mappedFundCode",
                    "orderNotes",
                    "otherLocalId",
                    "location",
                    "quantity",
                    "ybpOrderKey",
                    "orderPlaced",
                    "initials",
                    "listPrice",
                    "localData"
            })
            public static class OrderDetail {

                @XmlElement(name = "BatchPONumber")
                protected String batchPONumber;
                @XmlElement(name = "ItemPONumber")
                protected String itemPONumber;
                @XmlElement(name = "FundCode")
                protected String fundCode;
                @XmlElement(name = "MappedFundCode")
                protected String mappedFundCode;
                @XmlElement(name = "OrderNotes")
                protected String orderNotes;
                @XmlElement(name = "OtherLocalId")
                protected String otherLocalId;
                @XmlElement(name = "Location")
                protected String location;
                @XmlElement(name = "Quantity", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger quantity;
                @XmlElement(name = "YBPOrderKey", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger ybpOrderKey;
                @XmlElement(name = "OrderPlaced", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar orderPlaced;
                @XmlElement(name = "Initials")
                protected String initials;
                @XmlElement(name = "ListPrice", required = true)
                protected PurchaseOrder.Order.ListedPrintMonograph.OrderDetail.ListPrice listPrice;
                @XmlElement(name = "LocalData")
                protected List<LocalData> localData;

                /**
                 * Gets the value of the batchPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getBatchPONumber() {
                    return batchPONumber;
                }

                /**
                 * Sets the value of the batchPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setBatchPONumber(String value) {
                    this.batchPONumber = value;
                }

                /**
                 * Gets the value of the itemPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getItemPONumber() {
                    return itemPONumber;
                }

                /**
                 * Sets the value of the itemPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setItemPONumber(String value) {
                    this.itemPONumber = value;
                }

                /**
                 * Gets the value of the fundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getFundCode() {
                    return fundCode;
                }

                /**
                 * Sets the value of the fundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setFundCode(String value) {
                    this.fundCode = value;
                }

                /**
                 * Gets the value of the mappedFundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getMappedFundCode() {
                    return mappedFundCode;
                }

                /**
                 * Sets the value of the mappedFundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setMappedFundCode(String value) {
                    this.mappedFundCode = value;
                }

                /**
                 * Gets the value of the orderNotes property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOrderNotes() {
                    return orderNotes;
                }

                /**
                 * Sets the value of the orderNotes property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOrderNotes(String value) {
                    this.orderNotes = value;
                }

                /**
                 * Gets the value of the otherLocalId property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOtherLocalId() {
                    return otherLocalId;
                }

                /**
                 * Sets the value of the otherLocalId property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOtherLocalId(String value) {
                    this.otherLocalId = value;
                }

                /**
                 * Gets the value of the location property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getLocation() {
                    return location;
                }

                /**
                 * Sets the value of the location property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setLocation(String value) {
                    this.location = value;
                }

                /**
                 * Gets the value of the quantity property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getQuantity() {
                    return quantity;
                }

                /**
                 * Sets the value of the quantity property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setQuantity(BigInteger value) {
                    this.quantity = value;
                }

                /**
                 * Gets the value of the ybpOrderKey property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getYBPOrderKey() {
                    return ybpOrderKey;
                }

                /**
                 * Sets the value of the ybpOrderKey property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setYBPOrderKey(BigInteger value) {
                    this.ybpOrderKey = value;
                }

                /**
                 * Gets the value of the orderPlaced property.
                 *
                 * @return possible object is
                 * {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public XMLGregorianCalendar getOrderPlaced() {
                    return orderPlaced;
                }

                /**
                 * Sets the value of the orderPlaced property.
                 *
                 * @param value allowed object is
                 *              {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public void setOrderPlaced(XMLGregorianCalendar value) {
                    this.orderPlaced = value;
                }

                /**
                 * Gets the value of the initials property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getInitials() {
                    return initials;
                }

                /**
                 * Sets the value of the initials property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setInitials(String value) {
                    this.initials = value;
                }

                /**
                 * Gets the value of the listPrice property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.ListedPrintMonograph.OrderDetail.ListPrice }
                 */
                public PurchaseOrder.Order.ListedPrintMonograph.OrderDetail.ListPrice getListPrice() {
                    return listPrice;
                }

                /**
                 * Sets the value of the listPrice property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.ListedPrintMonograph.OrderDetail.ListPrice }
                 */
                public void setListPrice(PurchaseOrder.Order.ListedPrintMonograph.OrderDetail.ListPrice value) {
                    this.listPrice = value;
                }

                /**
                 * Gets the value of the localData property.
                 * <p/>
                 * <p/>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the localData property.
                 * <p/>
                 * <p/>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getLocalData().add(newItem);
                 * </pre>
                 * <p/>
                 * <p/>
                 * <p/>
                 * Objects of the following type(s) are allowed in the list
                 * {@link PurchaseOrder.Order.ListedPrintMonograph.OrderDetail.LocalData }
                 */
                public List<LocalData> getLocalData() {
                    if (localData == null) {
                        localData = new ArrayList<LocalData>();
                    }
                    return this.localData;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
                 *         &lt;element name="Currency" type="{}Currency"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "amount",
                        "currency"
                })
                public static class ListPrice {

                    @XmlElement(name = "Amount", required = true)
                    protected BigDecimal amount;
                    @XmlElement(name = "Currency", required = true)
                    protected String currency;

                    /**
                     * Gets the value of the amount property.
                     *
                     * @return possible object is
                     * {@link java.math.BigDecimal }
                     */
                    public BigDecimal getAmount() {
                        return amount;
                    }

                    /**
                     * Sets the value of the amount property.
                     *
                     * @param value allowed object is
                     *              {@link java.math.BigDecimal }
                     */
                    public void setAmount(BigDecimal value) {
                        this.amount = value;
                    }

                    /**
                     * Gets the value of the currency property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCurrency() {
                        return currency;
                    }

                    /**
                     * Sets the value of the currency property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCurrency(String value) {
                        this.currency = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Description" type="{}LD-Description"/>
                 *         &lt;element name="Value" type="{}LD-Value"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "description",
                        "value"
                })
                public static class LocalData {

                    @XmlElement(name = "Description", required = true)
                    protected String description;
                    @XmlElement(name = "Value", required = true)
                    protected String value;

                    /**
                     * Gets the value of the description property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Sets the value of the description property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     * {@link String }
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

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{}collection"/>
         *         &lt;element name="OrderDetail">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
         *                   &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
         *                   &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
         *                   &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
         *                   &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
         *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
         *                   &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "collection",
                "orderDetail"
        })
        public static class ListedPrintSerial {

            @XmlElement(required = true, nillable = true)
            protected CollectionType collection;
            @XmlElement(name = "OrderDetail", required = true)
            protected PurchaseOrder.Order.ListedPrintSerial.OrderDetail orderDetail;

            /**
             * This is the MARC record object container.
             *
             * @return possible object is
             * {@link CollectionType }
             */
            public CollectionType getCollection() {
                return collection;
            }

            /**
             * Sets the value of the collection property.
             *
             * @param value allowed object is
             *              {@link CollectionType }
             */
            public void setCollection(CollectionType value) {
                this.collection = value;
            }

            /**
             * Gets the value of the orderDetail property.
             *
             * @return possible object is
             * {@link PurchaseOrder.Order.ListedPrintSerial.OrderDetail }
             */
            public PurchaseOrder.Order.ListedPrintSerial.OrderDetail getOrderDetail() {
                return orderDetail;
            }

            /**
             * Sets the value of the orderDetail property.
             *
             * @param value allowed object is
             *              {@link PurchaseOrder.Order.ListedPrintSerial.OrderDetail }
             */
            public void setOrderDetail(PurchaseOrder.Order.ListedPrintSerial.OrderDetail value) {
                this.orderDetail = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
             *         &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
             *         &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
             *         &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
             *         &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
             *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
             *         &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "batchPONumber",
                    "itemPONumber",
                    "fundCode",
                    "mappedFundCode",
                    "orderNotes",
                    "quantity",
                    "ybpOrderKey",
                    "orderPlaced",
                    "initials",
                    "startWithVolume"
            })
            public static class OrderDetail {

                @XmlElement(name = "BatchPONumber")
                protected String batchPONumber;
                @XmlElement(name = "ItemPONumber")
                protected String itemPONumber;
                @XmlElement(name = "FundCode")
                protected String fundCode;
                @XmlElement(name = "MappedFundCode")
                protected String mappedFundCode;
                @XmlElement(name = "OrderNotes")
                protected String orderNotes;
                @XmlElement(name = "Quantity", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger quantity;
                @XmlElement(name = "YBPOrderKey", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger ybpOrderKey;
                @XmlElement(name = "OrderPlaced", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar orderPlaced;
                @XmlElement(name = "Initials")
                protected String initials;
                @XmlElement(name = "StartWithVolume")
                protected String startWithVolume;

                /**
                 * Gets the value of the batchPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getBatchPONumber() {
                    return batchPONumber;
                }

                /**
                 * Sets the value of the batchPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setBatchPONumber(String value) {
                    this.batchPONumber = value;
                }

                /**
                 * Gets the value of the itemPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getItemPONumber() {
                    return itemPONumber;
                }

                /**
                 * Sets the value of the itemPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setItemPONumber(String value) {
                    this.itemPONumber = value;
                }

                /**
                 * Gets the value of the fundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getFundCode() {
                    return fundCode;
                }

                /**
                 * Sets the value of the fundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setFundCode(String value) {
                    this.fundCode = value;
                }

                /**
                 * Gets the value of the mappedFundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getMappedFundCode() {
                    return mappedFundCode;
                }

                /**
                 * Sets the value of the mappedFundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setMappedFundCode(String value) {
                    this.mappedFundCode = value;
                }

                /**
                 * Gets the value of the orderNotes property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOrderNotes() {
                    return orderNotes;
                }

                /**
                 * Sets the value of the orderNotes property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOrderNotes(String value) {
                    this.orderNotes = value;
                }

                /**
                 * Gets the value of the quantity property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getQuantity() {
                    return quantity;
                }

                /**
                 * Sets the value of the quantity property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setQuantity(BigInteger value) {
                    this.quantity = value;
                }

                /**
                 * Gets the value of the ybpOrderKey property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getYBPOrderKey() {
                    return ybpOrderKey;
                }

                /**
                 * Sets the value of the ybpOrderKey property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setYBPOrderKey(BigInteger value) {
                    this.ybpOrderKey = value;
                }

                /**
                 * Gets the value of the orderPlaced property.
                 *
                 * @return possible object is
                 * {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public XMLGregorianCalendar getOrderPlaced() {
                    return orderPlaced;
                }

                /**
                 * Sets the value of the orderPlaced property.
                 *
                 * @param value allowed object is
                 *              {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public void setOrderPlaced(XMLGregorianCalendar value) {
                    this.orderPlaced = value;
                }

                /**
                 * Gets the value of the initials property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getInitials() {
                    return initials;
                }

                /**
                 * Sets the value of the initials property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setInitials(String value) {
                    this.initials = value;
                }

                /**
                 * Gets the value of the startWithVolume property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getStartWithVolume() {
                    return startWithVolume;
                }

                /**
                 * Sets the value of the startWithVolume property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setStartWithVolume(String value) {
                    this.startWithVolume = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{}collection"/>
         *         &lt;element name="OrderDetail">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
         *                   &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
         *                   &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
         *                   &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
         *                   &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
         *                   &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
         *                   &lt;element name="Location" type="{}Location" minOccurs="0"/>
         *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
         *                   &lt;element name="ListPrice" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *                             &lt;element name="Currency" type="{}Currency"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Description" type="{}LD-Description"/>
         *                             &lt;element name="Value" type="{}LD-Value"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="PurchaseOption" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
         *                             &lt;element name="Code" type="{}PO-Code"/>
         *                             &lt;element name="Description" type="{}PO-Description"/>
         *                             &lt;element name="VendorCode" type="{}PO-VendorCode"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "collection",
                "orderDetail"
        })
        public static class UnlistedElectronicMonograph {

            @XmlElement(required = true, nillable = true)
            protected CollectionType collection;
            @XmlElement(name = "OrderDetail", required = true)
            protected PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail orderDetail;

            /**
             * This is the MARC record object container.
             *
             * @return possible object is
             * {@link CollectionType }
             */
            public CollectionType getCollection() {
                return collection;
            }

            /**
             * Sets the value of the collection property.
             *
             * @param value allowed object is
             *              {@link CollectionType }
             */
            public void setCollection(CollectionType value) {
                this.collection = value;
            }

            /**
             * Gets the value of the orderDetail property.
             *
             * @return possible object is
             * {@link PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail }
             */
            public PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail getOrderDetail() {
                return orderDetail;
            }

            /**
             * Sets the value of the orderDetail property.
             *
             * @param value allowed object is
             *              {@link PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail }
             */
            public void setOrderDetail(PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail value) {
                this.orderDetail = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
             *         &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
             *         &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
             *         &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
             *         &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
             *         &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
             *         &lt;element name="Location" type="{}Location" minOccurs="0"/>
             *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
             *         &lt;element name="ListPrice" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
             *                   &lt;element name="Currency" type="{}Currency"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Description" type="{}LD-Description"/>
             *                   &lt;element name="Value" type="{}LD-Value"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="PurchaseOption" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
             *                   &lt;element name="Code" type="{}PO-Code"/>
             *                   &lt;element name="Description" type="{}PO-Description"/>
             *                   &lt;element name="VendorCode" type="{}PO-VendorCode"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "batchPONumber",
                    "itemPONumber",
                    "fundCode",
                    "mappedFundCode",
                    "orderNotes",
                    "otherLocalId",
                    "location",
                    "quantity",
                    "ybpOrderKey",
                    "orderPlaced",
                    "initials",
                    "listPrice",
                    "localData",
                    "purchaseOption"
            })
            public static class OrderDetail {

                @XmlElement(name = "BatchPONumber")
                protected String batchPONumber;
                @XmlElement(name = "ItemPONumber")
                protected String itemPONumber;
                @XmlElement(name = "FundCode")
                protected String fundCode;
                @XmlElement(name = "MappedFundCode")
                protected String mappedFundCode;
                @XmlElement(name = "OrderNotes")
                protected String orderNotes;
                @XmlElement(name = "OtherLocalId")
                protected String otherLocalId;
                @XmlElement(name = "Location")
                protected String location;
                @XmlElement(name = "Quantity", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger quantity;
                @XmlElement(name = "YBPOrderKey", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger ybpOrderKey;
                @XmlElement(name = "OrderPlaced", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar orderPlaced;
                @XmlElement(name = "Initials")
                protected String initials;
                @XmlElement(name = "ListPrice")
                protected PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.ListPrice listPrice;
                @XmlElement(name = "LocalData")
                protected List<LocalData> localData;
                @XmlElement(name = "PurchaseOption")
                protected PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.PurchaseOption purchaseOption;

                /**
                 * Gets the value of the batchPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getBatchPONumber() {
                    return batchPONumber;
                }

                /**
                 * Sets the value of the batchPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setBatchPONumber(String value) {
                    this.batchPONumber = value;
                }

                /**
                 * Gets the value of the itemPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getItemPONumber() {
                    return itemPONumber;
                }

                /**
                 * Sets the value of the itemPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setItemPONumber(String value) {
                    this.itemPONumber = value;
                }

                /**
                 * Gets the value of the fundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getFundCode() {
                    return fundCode;
                }

                /**
                 * Sets the value of the fundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setFundCode(String value) {
                    this.fundCode = value;
                }

                /**
                 * Gets the value of the mappedFundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getMappedFundCode() {
                    return mappedFundCode;
                }

                /**
                 * Sets the value of the mappedFundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setMappedFundCode(String value) {
                    this.mappedFundCode = value;
                }

                /**
                 * Gets the value of the orderNotes property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOrderNotes() {
                    return orderNotes;
                }

                /**
                 * Sets the value of the orderNotes property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOrderNotes(String value) {
                    this.orderNotes = value;
                }

                /**
                 * Gets the value of the otherLocalId property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOtherLocalId() {
                    return otherLocalId;
                }

                /**
                 * Sets the value of the otherLocalId property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOtherLocalId(String value) {
                    this.otherLocalId = value;
                }

                /**
                 * Gets the value of the location property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getLocation() {
                    return location;
                }

                /**
                 * Sets the value of the location property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setLocation(String value) {
                    this.location = value;
                }

                /**
                 * Gets the value of the quantity property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getQuantity() {
                    return quantity;
                }

                /**
                 * Sets the value of the quantity property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setQuantity(BigInteger value) {
                    this.quantity = value;
                }

                /**
                 * Gets the value of the ybpOrderKey property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getYBPOrderKey() {
                    return ybpOrderKey;
                }

                /**
                 * Sets the value of the ybpOrderKey property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setYBPOrderKey(BigInteger value) {
                    this.ybpOrderKey = value;
                }

                /**
                 * Gets the value of the orderPlaced property.
                 *
                 * @return possible object is
                 * {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public XMLGregorianCalendar getOrderPlaced() {
                    return orderPlaced;
                }

                /**
                 * Sets the value of the orderPlaced property.
                 *
                 * @param value allowed object is
                 *              {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public void setOrderPlaced(XMLGregorianCalendar value) {
                    this.orderPlaced = value;
                }

                /**
                 * Gets the value of the initials property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getInitials() {
                    return initials;
                }

                /**
                 * Sets the value of the initials property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setInitials(String value) {
                    this.initials = value;
                }

                /**
                 * Gets the value of the listPrice property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.ListPrice }
                 */
                public PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.ListPrice getListPrice() {
                    return listPrice;
                }

                /**
                 * Sets the value of the listPrice property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.ListPrice }
                 */
                public void setListPrice(PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.ListPrice value) {
                    this.listPrice = value;
                }

                /**
                 * Gets the value of the localData property.
                 * <p/>
                 * <p/>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the localData property.
                 * <p/>
                 * <p/>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getLocalData().add(newItem);
                 * </pre>
                 * <p/>
                 * <p/>
                 * <p/>
                 * Objects of the following type(s) are allowed in the list
                 * {@link PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.LocalData }
                 */
                public List<LocalData> getLocalData() {
                    if (localData == null) {
                        localData = new ArrayList<LocalData>();
                    }
                    return this.localData;
                }

                /**
                 * Gets the value of the purchaseOption property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.PurchaseOption }
                 */
                public PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.PurchaseOption getPurchaseOption() {
                    return purchaseOption;
                }

                /**
                 * Sets the value of the purchaseOption property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.PurchaseOption }
                 */
                public void setPurchaseOption(PurchaseOrder.Order.UnlistedElectronicMonograph.OrderDetail.PurchaseOption value) {
                    this.purchaseOption = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
                 *         &lt;element name="Currency" type="{}Currency"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "amount",
                        "currency"
                })
                public static class ListPrice {

                    @XmlElement(name = "Amount", required = true)
                    protected BigDecimal amount;
                    @XmlElement(name = "Currency", required = true)
                    protected String currency;

                    /**
                     * Gets the value of the amount property.
                     *
                     * @return possible object is
                     * {@link java.math.BigDecimal }
                     */
                    public BigDecimal getAmount() {
                        return amount;
                    }

                    /**
                     * Sets the value of the amount property.
                     *
                     * @param value allowed object is
                     *              {@link java.math.BigDecimal }
                     */
                    public void setAmount(BigDecimal value) {
                        this.amount = value;
                    }

                    /**
                     * Gets the value of the currency property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCurrency() {
                        return currency;
                    }

                    /**
                     * Sets the value of the currency property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCurrency(String value) {
                        this.currency = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Description" type="{}LD-Description"/>
                 *         &lt;element name="Value" type="{}LD-Value"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "description",
                        "value"
                })
                public static class LocalData {

                    @XmlElement(name = "Description", required = true)
                    protected String description;
                    @XmlElement(name = "Value", required = true)
                    protected String value;

                    /**
                     * Gets the value of the description property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Sets the value of the description property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     * {@link String }
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

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="VendorPOCode" type="{}Vendor-PO-Code"/>
                 *         &lt;element name="Code" type="{}PO-Code"/>
                 *         &lt;element name="Description" type="{}PO-Description"/>
                 *         &lt;element name="VendorCode" type="{}PO-VendorCode"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "vendorPOCode",
                        "code",
                        "description",
                        "vendorCode"
                })
                public static class PurchaseOption {

                    @XmlElement(name = "VendorPOCode", required = true)
                    protected String vendorPOCode;
                    @XmlElement(name = "Code", required = true)
                    protected String code;
                    @XmlElement(name = "Description", required = true)
                    protected String description;
                    @XmlElement(name = "VendorCode", required = true)
                    protected String vendorCode;

                    /**
                     * Gets the value of the vendorPOCode property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getVendorPOCode() {
                        return vendorPOCode;
                    }

                    /**
                     * Sets the value of the vendorPOCode property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setVendorPOCode(String value) {
                        this.vendorPOCode = value;
                    }

                    /**
                     * Gets the value of the code property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCode() {
                        return code;
                    }

                    /**
                     * Sets the value of the code property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCode(String value) {
                        this.code = value;
                    }

                    /**
                     * Gets the value of the description property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Sets the value of the description property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    /**
                     * Gets the value of the vendorCode property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getVendorCode() {
                        return vendorCode;
                    }

                    /**
                     * Sets the value of the vendorCode property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setVendorCode(String value) {
                        this.vendorCode = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{}collection"/>
         *         &lt;element name="OrderDetail">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
         *                   &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
         *                   &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
         *                   &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
         *                   &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
         *                   &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
         *                   &lt;element name="Location" type="{}Location" minOccurs="0"/>
         *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
         *                   &lt;element name="ListPrice" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *                             &lt;element name="Currency" type="{}Currency"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Description" type="{}LD-Description"/>
         *                             &lt;element name="Value" type="{}LD-Value"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "collection",
                "orderDetail"
        })
        public static class UnlistedPrintMonograph {

            @XmlElement(required = true, nillable = true)
            protected CollectionType collection;
            @XmlElement(name = "OrderDetail", required = true)
            protected PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail orderDetail;

            /**
             * This is the MARC record object container.
             *
             * @return possible object is
             * {@link CollectionType }
             */
            public CollectionType getCollection() {
                return collection;
            }

            /**
             * Sets the value of the collection property.
             *
             * @param value allowed object is
             *              {@link CollectionType }
             */
            public void setCollection(CollectionType value) {
                this.collection = value;
            }

            /**
             * Gets the value of the orderDetail property.
             *
             * @return possible object is
             * {@link PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail }
             */
            public PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail getOrderDetail() {
                return orderDetail;
            }

            /**
             * Sets the value of the orderDetail property.
             *
             * @param value allowed object is
             *              {@link PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail }
             */
            public void setOrderDetail(PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail value) {
                this.orderDetail = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
             *         &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
             *         &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
             *         &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
             *         &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
             *         &lt;element name="OtherLocalId" type="{}OtherLocalId" minOccurs="0"/>
             *         &lt;element name="Location" type="{}Location" minOccurs="0"/>
             *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
             *         &lt;element name="ListPrice" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
             *                   &lt;element name="Currency" type="{}Currency"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="LocalData" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Description" type="{}LD-Description"/>
             *                   &lt;element name="Value" type="{}LD-Value"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "batchPONumber",
                    "itemPONumber",
                    "fundCode",
                    "mappedFundCode",
                    "orderNotes",
                    "otherLocalId",
                    "location",
                    "quantity",
                    "ybpOrderKey",
                    "orderPlaced",
                    "initials",
                    "listPrice",
                    "localData"
            })
            public static class OrderDetail {

                @XmlElement(name = "BatchPONumber")
                protected String batchPONumber;
                @XmlElement(name = "ItemPONumber")
                protected String itemPONumber;
                @XmlElement(name = "FundCode")
                protected String fundCode;
                @XmlElement(name = "MappedFundCode")
                protected String mappedFundCode;
                @XmlElement(name = "OrderNotes")
                protected String orderNotes;
                @XmlElement(name = "OtherLocalId")
                protected String otherLocalId;
                @XmlElement(name = "Location")
                protected String location;
                @XmlElement(name = "Quantity", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger quantity;
                @XmlElement(name = "YBPOrderKey", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger ybpOrderKey;
                @XmlElement(name = "OrderPlaced", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar orderPlaced;
                @XmlElement(name = "Initials")
                protected String initials;
                @XmlElement(name = "ListPrice")
                protected PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail.ListPrice listPrice;
                @XmlElement(name = "LocalData")
                protected List<LocalData> localData;

                /**
                 * Gets the value of the batchPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getBatchPONumber() {
                    return batchPONumber;
                }

                /**
                 * Sets the value of the batchPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setBatchPONumber(String value) {
                    this.batchPONumber = value;
                }

                /**
                 * Gets the value of the itemPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getItemPONumber() {
                    return itemPONumber;
                }

                /**
                 * Sets the value of the itemPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setItemPONumber(String value) {
                    this.itemPONumber = value;
                }

                /**
                 * Gets the value of the fundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getFundCode() {
                    return fundCode;
                }

                /**
                 * Sets the value of the fundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setFundCode(String value) {
                    this.fundCode = value;
                }

                /**
                 * Gets the value of the mappedFundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getMappedFundCode() {
                    return mappedFundCode;
                }

                /**
                 * Sets the value of the mappedFundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setMappedFundCode(String value) {
                    this.mappedFundCode = value;
                }

                /**
                 * Gets the value of the orderNotes property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOrderNotes() {
                    return orderNotes;
                }

                /**
                 * Sets the value of the orderNotes property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOrderNotes(String value) {
                    this.orderNotes = value;
                }

                /**
                 * Gets the value of the otherLocalId property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOtherLocalId() {
                    return otherLocalId;
                }

                /**
                 * Sets the value of the otherLocalId property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOtherLocalId(String value) {
                    this.otherLocalId = value;
                }

                /**
                 * Gets the value of the location property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getLocation() {
                    return location;
                }

                /**
                 * Sets the value of the location property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setLocation(String value) {
                    this.location = value;
                }

                /**
                 * Gets the value of the quantity property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getQuantity() {
                    return quantity;
                }

                /**
                 * Sets the value of the quantity property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setQuantity(BigInteger value) {
                    this.quantity = value;
                }

                /**
                 * Gets the value of the ybpOrderKey property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getYBPOrderKey() {
                    return ybpOrderKey;
                }

                /**
                 * Sets the value of the ybpOrderKey property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setYBPOrderKey(BigInteger value) {
                    this.ybpOrderKey = value;
                }

                /**
                 * Gets the value of the orderPlaced property.
                 *
                 * @return possible object is
                 * {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public XMLGregorianCalendar getOrderPlaced() {
                    return orderPlaced;
                }

                /**
                 * Sets the value of the orderPlaced property.
                 *
                 * @param value allowed object is
                 *              {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public void setOrderPlaced(XMLGregorianCalendar value) {
                    this.orderPlaced = value;
                }

                /**
                 * Gets the value of the initials property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getInitials() {
                    return initials;
                }

                /**
                 * Sets the value of the initials property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setInitials(String value) {
                    this.initials = value;
                }

                /**
                 * Gets the value of the listPrice property.
                 *
                 * @return possible object is
                 * {@link PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail.ListPrice }
                 */
                public PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail.ListPrice getListPrice() {
                    return listPrice;
                }

                /**
                 * Sets the value of the listPrice property.
                 *
                 * @param value allowed object is
                 *              {@link PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail.ListPrice }
                 */
                public void setListPrice(PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail.ListPrice value) {
                    this.listPrice = value;
                }

                /**
                 * Gets the value of the localData property.
                 * <p/>
                 * <p/>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the localData property.
                 * <p/>
                 * <p/>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getLocalData().add(newItem);
                 * </pre>
                 * <p/>
                 * <p/>
                 * <p/>
                 * Objects of the following type(s) are allowed in the list
                 * {@link PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail.LocalData }
                 */
                public List<LocalData> getLocalData() {
                    if (localData == null) {
                        localData = new ArrayList<LocalData>();
                    }
                    return this.localData;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
                 *         &lt;element name="Currency" type="{}Currency"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "amount",
                        "currency"
                })
                public static class ListPrice {

                    @XmlElement(name = "Amount", required = true)
                    protected BigDecimal amount;
                    @XmlElement(name = "Currency", required = true)
                    protected String currency;

                    /**
                     * Gets the value of the amount property.
                     *
                     * @return possible object is
                     * {@link java.math.BigDecimal }
                     */
                    public BigDecimal getAmount() {
                        return amount;
                    }

                    /**
                     * Sets the value of the amount property.
                     *
                     * @param value allowed object is
                     *              {@link java.math.BigDecimal }
                     */
                    public void setAmount(BigDecimal value) {
                        this.amount = value;
                    }

                    /**
                     * Gets the value of the currency property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getCurrency() {
                        return currency;
                    }

                    /**
                     * Sets the value of the currency property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setCurrency(String value) {
                        this.currency = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * <p/>
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * <p/>
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Description" type="{}LD-Description"/>
                 *         &lt;element name="Value" type="{}LD-Value"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "description",
                        "value"
                })
                public static class LocalData {

                    @XmlElement(name = "Description", required = true)
                    protected String description;
                    @XmlElement(name = "Value", required = true)
                    protected String value;

                    /**
                     * Gets the value of the description property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Sets the value of the description property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    /**
                     * Gets the value of the value property.
                     *
                     * @return possible object is
                     * {@link String }
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

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p/>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p/>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{}collection"/>
         *         &lt;element name="OrderDetail">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
         *                   &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
         *                   &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
         *                   &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
         *                   &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
         *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                   &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
         *                   &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "collection",
                "orderDetail"
        })
        public static class UnlistedPrintSerial {

            @XmlElement(required = true, nillable = true)
            protected CollectionType collection;
            @XmlElement(name = "OrderDetail", required = true)
            protected PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail orderDetail;

            /**
             * This is the MARC record object container.
             *
             * @return possible object is
             * {@link CollectionType }
             */
            public CollectionType getCollection() {
                return collection;
            }

            /**
             * Sets the value of the collection property.
             *
             * @param value allowed object is
             *              {@link CollectionType }
             */
            public void setCollection(CollectionType value) {
                this.collection = value;
            }

            /**
             * Gets the value of the orderDetail property.
             *
             * @return possible object is
             * {@link PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail }
             */
            public PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail getOrderDetail() {
                return orderDetail;
            }

            /**
             * Sets the value of the orderDetail property.
             *
             * @param value allowed object is
             *              {@link PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail }
             */
            public void setOrderDetail(PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail value) {
                this.orderDetail = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * <p/>
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p/>
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BatchPONumber" type="{}BatchPONumber" minOccurs="0"/>
             *         &lt;element name="ItemPONumber" type="{}ItemPONumber" minOccurs="0"/>
             *         &lt;element name="FundCode" type="{}FundCode" minOccurs="0"/>
             *         &lt;element name="MappedFundCode" type="{}MappedFundCode" minOccurs="0"/>
             *         &lt;element name="OrderNotes" type="{}OrderNotes" minOccurs="0"/>
             *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="YBPOrderKey" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
             *         &lt;element name="OrderPlaced" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         &lt;element name="Initials" type="{}Initials" minOccurs="0"/>
             *         &lt;element name="StartWithVolume" type="{}StartWithVolume" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "batchPONumber",
                    "itemPONumber",
                    "fundCode",
                    "mappedFundCode",
                    "orderNotes",
                    "quantity",
                    "ybpOrderKey",
                    "orderPlaced",
                    "initials",
                    "startWithVolume"
            })
            public static class OrderDetail {

                @XmlElement(name = "BatchPONumber")
                protected String batchPONumber;
                @XmlElement(name = "ItemPONumber")
                protected String itemPONumber;
                @XmlElement(name = "FundCode")
                protected String fundCode;
                @XmlElement(name = "MappedFundCode")
                protected String mappedFundCode;
                @XmlElement(name = "OrderNotes")
                protected String orderNotes;
                @XmlElement(name = "Quantity", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger quantity;
                @XmlElement(name = "YBPOrderKey", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger ybpOrderKey;
                @XmlElement(name = "OrderPlaced", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar orderPlaced;
                @XmlElement(name = "Initials")
                protected String initials;
                @XmlElement(name = "StartWithVolume")
                protected String startWithVolume;

                /**
                 * Gets the value of the batchPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getBatchPONumber() {
                    return batchPONumber;
                }

                /**
                 * Sets the value of the batchPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setBatchPONumber(String value) {
                    this.batchPONumber = value;
                }

                /**
                 * Gets the value of the itemPONumber property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getItemPONumber() {
                    return itemPONumber;
                }

                /**
                 * Sets the value of the itemPONumber property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setItemPONumber(String value) {
                    this.itemPONumber = value;
                }

                /**
                 * Gets the value of the fundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getFundCode() {
                    return fundCode;
                }

                /**
                 * Sets the value of the fundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setFundCode(String value) {
                    this.fundCode = value;
                }

                /**
                 * Gets the value of the mappedFundCode property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getMappedFundCode() {
                    return mappedFundCode;
                }

                /**
                 * Sets the value of the mappedFundCode property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setMappedFundCode(String value) {
                    this.mappedFundCode = value;
                }

                /**
                 * Gets the value of the orderNotes property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getOrderNotes() {
                    return orderNotes;
                }

                /**
                 * Sets the value of the orderNotes property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setOrderNotes(String value) {
                    this.orderNotes = value;
                }

                /**
                 * Gets the value of the quantity property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getQuantity() {
                    return quantity;
                }

                /**
                 * Sets the value of the quantity property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setQuantity(BigInteger value) {
                    this.quantity = value;
                }

                /**
                 * Gets the value of the ybpOrderKey property.
                 *
                 * @return possible object is
                 * {@link java.math.BigInteger }
                 */
                public BigInteger getYBPOrderKey() {
                    return ybpOrderKey;
                }

                /**
                 * Sets the value of the ybpOrderKey property.
                 *
                 * @param value allowed object is
                 *              {@link java.math.BigInteger }
                 */
                public void setYBPOrderKey(BigInteger value) {
                    this.ybpOrderKey = value;
                }

                /**
                 * Gets the value of the orderPlaced property.
                 *
                 * @return possible object is
                 * {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public XMLGregorianCalendar getOrderPlaced() {
                    return orderPlaced;
                }

                /**
                 * Sets the value of the orderPlaced property.
                 *
                 * @param value allowed object is
                 *              {@link javax.xml.datatype.XMLGregorianCalendar }
                 */
                public void setOrderPlaced(XMLGregorianCalendar value) {
                    this.orderPlaced = value;
                }

                /**
                 * Gets the value of the initials property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getInitials() {
                    return initials;
                }

                /**
                 * Sets the value of the initials property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setInitials(String value) {
                    this.initials = value;
                }

                /**
                 * Gets the value of the startWithVolume property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getStartWithVolume() {
                    return startWithVolume;
                }

                /**
                 * Sets the value of the startWithVolume property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setStartWithVolume(String value) {
                    this.startWithVolume = value;
                }

            }

        }

    }

}
