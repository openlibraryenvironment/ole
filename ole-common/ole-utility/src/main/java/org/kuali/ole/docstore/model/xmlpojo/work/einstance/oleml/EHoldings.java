package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * This section can be used to record general holdings information if not using a specific
 * source holdings.
 * <p/>
 * <p/>
 * <p>Java class for eHoldings complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="eHoldings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="holdingsIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="relatedInstanceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="publisher" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imprint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localPersistentLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="link" type="{http://ole.kuali.org/standards/ole-eInstance}link" minOccurs="0"/>
 *         &lt;element name="interLibraryLoanAllowed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="staffOnlyFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="extentOfOwnership" type="{http://ole.kuali.org/standards/ole-eInstance}extentOfOwnership" minOccurs="0"/>
 *         &lt;element name="note" type="{http://ole.kuali.org/standards/ole-eInstance}note" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="donorPublicDisplay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="donorNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="vendor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="purchaseOrderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statisticalSearchingCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eResourceTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eResourceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callNumber" type="{http://ole.kuali.org/standards/ole-eInstance}callNumber"/>
 *         &lt;element name="invoice" type="{http://ole.kuali.org/standards/ole-eInstance}invoice"/>
 *         &lt;element name="accessInformation" type="{http://ole.kuali.org/standards/ole-eInstance}accessInformation"/>
 *         &lt;element name="platform" type="{http://ole.kuali.org/standards/ole-eInstance}platform"/>
 *         &lt;element name="subscriptionStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="location" type="{http://ole.kuali.org/standards/ole-eInstance}location" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="primary" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eHoldings", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "holdingsIdentifier",
        "relatedInstanceIdentifier",
        "publisher",
        "imprint",
        "localPersistentLink",
        "link",
        "interLibraryLoanAllowed",
        "staffOnlyFlag",
        "extentOfOwnership",
        "note",
        "donorPublicDisplay",
        "donorNote",
        "accessStatus",
        "statusDate",
        "vendor",
        "orderType",
        "orderFormat",
        "purchaseOrderId",
        "statisticalSearchingCode",
        "eResourceTitle",
        "eResourceId",
        "callNumber",
        "invoice",
        "accessInformation",
        "platform",
        "subscriptionStatus",
        "location"
})
@XStreamAlias("eHoldings")
public class EHoldings {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String holdingsIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String relatedInstanceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String publisher;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String imprint;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    @XmlSchemaType(name = "anyURI")
    protected String localPersistentLink;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected Link link;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected boolean interLibraryLoanAllowed;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected boolean staffOnlyFlag;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected ExtentOfOwnership extentOfOwnership;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    @XStreamImplicit(itemFieldName = "note")
    protected List<Note> note;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String donorPublicDisplay;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String donorNote;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String accessStatus;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String statusDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String vendor;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String orderType;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String orderFormat;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String purchaseOrderId;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String statisticalSearchingCode;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String eResourceTitle;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String eResourceId;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected CallNumber callNumber;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected Invoice invoice;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected AccessInformation accessInformation;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected Platform platform;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String subscriptionStatus;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected Location location;
    @XmlAttribute(name = "primary")
    protected String primary;

    /**
     * Gets the value of the holdingsIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    /**
     * Sets the value of the holdingsIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHoldingsIdentifier(String value) {
        this.holdingsIdentifier = value;
    }

    /**
     * Gets the value of the relatedInstanceIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRelatedInstanceIdentifier() {
        return relatedInstanceIdentifier;
    }

    /**
     * Sets the value of the relatedInstanceIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRelatedInstanceIdentifier(String value) {
        this.relatedInstanceIdentifier = value;
    }

    /**
     * Gets the value of the publisher property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the value of the publisher property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublisher(String value) {
        this.publisher = value;
    }

    /**
     * Gets the value of the imprint property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getImprint() {
        return imprint;
    }

    /**
     * Sets the value of the imprint property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setImprint(String value) {
        this.imprint = value;
    }

    /**
     * Gets the value of the localPersistentLink property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLocalPersistentLink() {
        return localPersistentLink;
    }

    /**
     * Sets the value of the localPersistentLink property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocalPersistentLink(String value) {
        this.localPersistentLink = value;
    }

    /**
     * Gets the value of the link property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Link }
     */
    public Link getLink() {
        return link;
    }

    /**
     * Sets the value of the link property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Link }
     */
    public void setLink(Link value) {
        this.link = value;
    }

    /**
     * Gets the value of the interLibraryLoanAllowed property.
     */
    public boolean isInterLibraryLoanAllowed() {
        return interLibraryLoanAllowed;
    }

    /**
     * Sets the value of the interLibraryLoanAllowed property.
     */
    public void setInterLibraryLoanAllowed(boolean value) {
        this.interLibraryLoanAllowed = value;
    }

    /**
     * Gets the value of the staffOnlyFlag property.
     */
    public boolean isStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    /**
     * Sets the value of the staffOnlyFlag property.
     */
    public void setStaffOnlyFlag(boolean value) {
        this.staffOnlyFlag = value;
    }

    /**
     * Gets the value of the extentOfOwnership property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.ExtentOfOwnership }
     */
    public ExtentOfOwnership getExtentOfOwnership() {
        return extentOfOwnership;
    }

    /**
     * Sets the value of the extentOfOwnership property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.ExtentOfOwnership }
     */
    public void setExtentOfOwnership(ExtentOfOwnership value) {
        this.extentOfOwnership = value;
    }

    /**
     * Gets the value of the note property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Note }
     */
    public List<Note> getNote() {
        if (note == null) {
            note = new ArrayList<Note>();
        }
        return this.note;
    }

    /**
     * Gets the value of the donorPublicDisplay property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDonorPublicDisplay() {
        return donorPublicDisplay;
    }

    /**
     * Sets the value of the donorPublicDisplay property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDonorPublicDisplay(String value) {
        this.donorPublicDisplay = value;
    }

    /**
     * Gets the value of the donorNote property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDonorNote() {
        return donorNote;
    }

    /**
     * Sets the value of the donorNote property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDonorNote(String value) {
        this.donorNote = value;
    }

    /**
     * Gets the value of the accessStatus property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAccessStatus() {
        return accessStatus;
    }

    /**
     * Sets the value of the accessStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccessStatus(String value) {
        this.accessStatus = value;
    }

    /**
     * Gets the value of the statusDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStatusDate() {
        return statusDate;
    }

    /**
     * Sets the value of the statusDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatusDate(String value) {
        this.statusDate = value;
    }

    /**
     * Gets the value of the vendor property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the value of the vendor property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVendor(String value) {
        this.vendor = value;
    }

    /**
     * Gets the value of the orderType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrderType(String value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the orderFormat property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getOrderFormat() {
        return orderFormat;
    }

    /**
     * Sets the value of the orderFormat property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrderFormat(String value) {
        this.orderFormat = value;
    }

    /**
     * Gets the value of the purchaseOrderId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    /**
     * Sets the value of the purchaseOrderId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPurchaseOrderId(String value) {
        this.purchaseOrderId = value;
    }

    /**
     * Gets the value of the statisticalSearchingCode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStatisticalSearchingCode() {
        return statisticalSearchingCode;
    }

    /**
     * Sets the value of the statisticalSearchingCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatisticalSearchingCode(String value) {
        this.statisticalSearchingCode = value;
    }

    /**
     * Gets the value of the eResourceTitle property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEResourceTitle() {
        return eResourceTitle;
    }

    /**
     * Sets the value of the eResourceTitle property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEResourceTitle(String value) {
        this.eResourceTitle = value;
    }

    /**
     * Gets the value of the eResourceId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEResourceId() {
        return eResourceId;
    }

    /**
     * Sets the value of the eResourceId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEResourceId(String value) {
        this.eResourceId = value;
    }

    /**
     * Gets the value of the callNumber property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumber }
     */
    public CallNumber getCallNumber() {
        return callNumber;
    }

    /**
     * Sets the value of the callNumber property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumber }
     */
    public void setCallNumber(CallNumber value) {
        this.callNumber = value;
    }

    /**
     * Gets the value of the invoice property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Invoice }
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Sets the value of the invoice property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Invoice }
     */
    public void setInvoice(Invoice value) {
        this.invoice = value;
    }

    /**
     * Gets the value of the accessInformation property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.AccessInformation }
     */
    public AccessInformation getAccessInformation() {
        return accessInformation;
    }

    /**
     * Sets the value of the accessInformation property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.AccessInformation }
     */
    public void setAccessInformation(AccessInformation value) {
        this.accessInformation = value;
    }

    /**
     * Gets the value of the platform property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Platform }
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Sets the value of the platform property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Platform }
     */
    public void setPlatform(Platform value) {
        this.platform = value;
    }

    /**
     * Gets the value of the subscriptionStatus property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    /**
     * Sets the value of the subscriptionStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSubscriptionStatus(String value) {
        this.subscriptionStatus = value;
    }

    /**
     * Gets the value of the location property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Location }
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Location }
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the primary property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrimary(String value) {
        this.primary = value;
    }

}
