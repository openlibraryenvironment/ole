package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * This section can be used to record general holdings information if not using a specific
 * source holdings.
 * <p/>
 * <p/>
 * <p>Java class for oleHoldings complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="oleHoldings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="holdingsIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="extentOfOwnership" type="{http://ole.kuali.org/standards/ole-instance}extentOfOwnership" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="receiptStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uri" type="{http://ole.kuali.org/standards/ole-instance}uri" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="note" type="{http://ole.kuali.org/standards/ole-instance}note" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="location" type="{http://ole.kuali.org/standards/ole-instance}location"/>
 *         &lt;element name="extension" type="{http://ole.kuali.org/standards/ole-instance}extension"/>
 *         &lt;element name="callNumber" type="{http://ole.kuali.org/standards/ole-instance}callNumber"/>
 *       &lt;/sequence>
 *       &lt;attribute name="primary" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "oleHoldings", propOrder = {
        "holdingsType",
        "bibIdentifier",
        "gokbIdentifier",
        "holdingsIdentifier",
        "extentOfOwnership",
        "receiptStatus",
        "uri",
        "note",
        "location",
        "extension",
        "callNumber",
        "copyNumber",
        "publisher",
        "imprint",
        "localPersistentLink",
        "link",
        "interLibraryLoanAllowed",
        "staffOnlyFlag",
        "donorPublicDisplay",
        "donorNote",
        "accessStatus",
        "statusDate",
        "statisticalSearchingCode",
        "eResourceId",
        "holdingsAccessInformation",
        "platform",
        "subscriptionStatus",
        "donorInfo",
        })
@XStreamAlias("oleHoldings")
@XmlRootElement(name = "oleHoldings")
public class OleHoldings {

    @XmlElement(required = true)
    protected String holdingsType;
    @XmlElement(required = true)
    protected String holdingsIdentifier;
    @XmlElement(name = "resourceIdentifier")
    protected String bibIdentifier;
    @XmlElement(name = "gokbIdentifier")
    protected Integer gokbIdentifier;
    @XStreamImplicit(itemFieldName = "extentOfOwnership")
    protected List<ExtentOfOwnership> extentOfOwnership;
    @XmlElement(required = true)
    protected String receiptStatus;
    @XStreamImplicit(itemFieldName = "uri")
    protected List<Uri> uri;
    @XStreamImplicit(itemFieldName = "note")
    protected List<Note> note;
    @XmlElement(required = true)
    protected Location location;
    @XmlElement(required = true)
    protected Extension extension;
    @XmlElement(required = true)
    protected CallNumber callNumber;
    @XmlElement(required = true)
    protected String copyNumber;
    @XmlAttribute
    @XStreamAsAttribute
    protected String primary;
    protected String publisher;
    protected String imprint;
    @XmlSchemaType(name = "anyURI")
    protected String localPersistentLink;
    protected List<Link> link;
    protected boolean interLibraryLoanAllowed;
    protected boolean staffOnlyFlag;
    protected String donorPublicDisplay;
    protected String donorNote;
    protected String accessStatus;
    protected String statusDate;
    protected StatisticalSearchingCode statisticalSearchingCode;
    protected String eResourceId;
    protected HoldingsAccessInformation holdingsAccessInformation;
    @XmlElement(required = true)
    protected Platform platform;
    protected String subscriptionStatus;
    protected List<DonorInfo> donorInfo;
    protected String currentSubscriptionStartDate;
    protected String currentSubscriptionEndDate;
    protected String initialSubscriptionStartDate;
    protected String cancellationDecisionDate;
    protected String cancellationEffectiveDate;
    protected String cancellationReason;
    protected Timestamp eResourceCurrentSubscriptionStartDate;
    protected Timestamp eResourceCurrentSubscriptionEndDate;
    protected Timestamp eResourceInitialSubscriptionStartDate;
    protected Timestamp eResourceCancellationDecisionDate;
    protected Timestamp eResourceCancellationEffectiveDate;
    protected String eResourceCancellationReason;
    protected String eResourceSubscriptionStatus;


//    @XmlElement(name = , required = true)
//    protected String createdBy;
//    @XmlElement(name = , required = false)
//    protected String updatedBy;
//    @XmlElement(name = , required = true)
//    protected Timestamp createdDate;
//    @XmlElement(name = , required = false)
//    protected Timestamp updatedDate;


//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public String getUpdatedBy() {
//        return updatedBy;
//    }
//
//    public void setUpdatedBy(String updatedBy) {
//        this.updatedBy = updatedBy;
//    }
//
//    public Timestamp getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Timestamp createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public Timestamp getUpdatedDate() {
//        return updatedDate;
//    }
//
//    public void setUpdatedDate(Timestamp updatedDate) {
//        this.updatedDate = updatedDate;
//    }

    public String getHoldingsType() {
        return holdingsType;
    }

    public void setHoldingsType(String holdingsType) {
        this.holdingsType = holdingsType;
    }

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
     * Gets the value of the extentOfOwnership property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extentOfOwnership property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtentOfOwnership().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtentOfOwnership }
     */
    public List<ExtentOfOwnership> getExtentOfOwnership() {
        if (extentOfOwnership == null) {
            extentOfOwnership = new ArrayList<ExtentOfOwnership>();
        }
        return this.extentOfOwnership;
    }

    /**
     * @param extentOfOwnership
     */
    public void setExtentOfOwnership(List<ExtentOfOwnership> extentOfOwnership) {
        this.extentOfOwnership = extentOfOwnership;
    }

    /**
     * Gets the value of the receiptStatus property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getReceiptStatus() {
        return receiptStatus;
    }

    /**
     * Sets the value of the receiptStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReceiptStatus(String value) {
        this.receiptStatus = value;
    }

    /**
     * Gets the value of the uri property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uri property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUri().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Uri }
     */
    public List<Uri> getUri() {
        if (uri == null) {
            uri = new ArrayList<Uri>();
        }
        return this.uri;
    }

    public String getCancellationDecisionDate() {
        return cancellationDecisionDate;
    }

    public void setCancellationDecisionDate(String cancellationDecisionDate) {
        this.cancellationDecisionDate = cancellationDecisionDate;
    }

    public String getCancellationEffectiveDate() {
        return cancellationEffectiveDate;
    }

    public void setCancellationEffectiveDate(String cancellationEffectiveDate) {
        this.cancellationEffectiveDate = cancellationEffectiveDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    /**
     * @param uri
     */
    public void setUri(List<Uri> uri) {
        this.uri = uri;
    }

    /**
     * Gets the value of the holdingsNote property.
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
     * {@link org.kuali.ole.docstore.common.document.content.instance.Note }
     */
    public List<Note> getNote() {
        if (note == null) {
            note = new ArrayList<Note>();
        }
        return this.note;
    }

    /**
     * @param note
     */
    public void setNote(List<Note> note) {
        this.note = note;
    }

    /**
     * Gets the value of the location property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.common.document.content.instance.Location }
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.common.document.content.instance.Location }
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the extension property.
     *
     * @return possible object is
     *         {@link Extension }
     */
    public Extension getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     *
     * @param value allowed object is
     *              {@link Extension }
     */
    public void setExtension(Extension value) {
        this.extension = value;
    }

    /**
     * Gets the value of the callNumber property.
     *
     * @return possible object is
     *         {@link CallNumber }
     */
    public CallNumber getCallNumber() {
        return callNumber;
    }

    /**
     * Sets the value of the callNumber property.
     *
     * @param value allowed object is
     *              {@link CallNumber }
     */
    public void setCallNumber(CallNumber value) {
        this.callNumber = value;
    }

    /**
     * Gets the value of the copyNumber property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCopyNumber() {
        return copyNumber;
    }

    /**
     * Sets the value of the copyNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCopyNumber(String value) {
        this.copyNumber = value;
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
     *         {@link org.kuali.ole.docstore.common.document.content.instance.Link }
     */
    public List<Link> getLink() {
        if (link == null){
            link = new ArrayList<Link>();
        }
        return this.link;
    }

    /**
     * Sets the value of the link property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.common.document.content.instance.Link }
     */
    public void setLink(List<Link> value) {
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

    public StatisticalSearchingCode getStatisticalSearchingCode() {
        return statisticalSearchingCode;
    }

    public void setStatisticalSearchingCode(StatisticalSearchingCode statisticalSearchingCode) {
        this.statisticalSearchingCode = statisticalSearchingCode;
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
     * Gets the value of the accessInformation property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.common.document.content.instance.AccessInformation }
     */
    public HoldingsAccessInformation getHoldingsAccessInformation() {
        return holdingsAccessInformation;
    }

    /**
     * Sets the value of the accessInformation property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.common.document.content.instance.AccessInformation }
     */
    public void setHoldingsAccessInformation(HoldingsAccessInformation value) {
        this.holdingsAccessInformation = value;
    }

    /**
     * Gets the value of the platform property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.common.document.content.instance.Platform }
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Sets the value of the platform property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.common.document.content.instance.Platform }
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
    public List<DonorInfo> getDonorInfo() {
        if (donorInfo == null) {
            donorInfo = new ArrayList<DonorInfo>();
        }
        return this.donorInfo;
    }

    public void setDonorInfo(List<DonorInfo> value){
        this.donorInfo= value;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public Integer getGokbIdentifier() {
        return gokbIdentifier;
    }

    public void setGokbIdentifier(Integer gokbIdentifier) {
        this.gokbIdentifier = gokbIdentifier;
    }

    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        OleHoldings oleHoldings = (OleHoldings) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(OleHoldings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(oleHoldings, sw);
            result = sw.toString();
        } catch (Exception e) {
            //LOG.error("Exception ", e);
        }
        return result;
    }

    public String getCurrentSubscriptionStartDate() {
        return currentSubscriptionStartDate;
    }

    public void setCurrentSubscriptionStartDate(String currentSubscriptionStartDate) {
        this.currentSubscriptionStartDate = currentSubscriptionStartDate;
    }

    public String getCurrentSubscriptionEndDate() {
        return currentSubscriptionEndDate;
    }

    public void setCurrentSubscriptionEndDate(String currentSubscriptionEndDate) {
        this.currentSubscriptionEndDate = currentSubscriptionEndDate;
    }

    public String getInitialSubscriptionStartDate() {
        return initialSubscriptionStartDate;
    }

    public void setInitialSubscriptionStartDate(String initialSubscriptionStartDate) {
        this.initialSubscriptionStartDate = initialSubscriptionStartDate;
    }

    public Timestamp geteResourceCurrentSubscriptionStartDate() {
        return eResourceCurrentSubscriptionStartDate;
    }

    public void seteResourceCurrentSubscriptionStartDate(Timestamp eResourceCurrentSubscriptionStartDate) {
        this.eResourceCurrentSubscriptionStartDate = eResourceCurrentSubscriptionStartDate;
    }

    public Timestamp geteResourceCurrentSubscriptionEndDate() {
        return eResourceCurrentSubscriptionEndDate;
    }

    public void seteResourceCurrentSubscriptionEndDate(Timestamp eResourceCurrentSubscriptionEndDate) {
        this.eResourceCurrentSubscriptionEndDate = eResourceCurrentSubscriptionEndDate;
    }

    public Timestamp geteResourceInitialSubscriptionStartDate() {
        return eResourceInitialSubscriptionStartDate;
    }

    public void seteResourceInitialSubscriptionStartDate(Timestamp eResourceInitialSubscriptionStartDate) {
        this.eResourceInitialSubscriptionStartDate = eResourceInitialSubscriptionStartDate;
    }

    public Timestamp geteResourceCancellationDecisionDate() {
        return eResourceCancellationDecisionDate;
    }

    public void seteResourceCancellationDecisionDate(Timestamp eResourceCancellationDecisionDate) {
        this.eResourceCancellationDecisionDate = eResourceCancellationDecisionDate;
    }

    public Timestamp geteResourceCancellationEffectiveDate() {
        return eResourceCancellationEffectiveDate;
    }

    public void seteResourceCancellationEffectiveDate(Timestamp eResourceCancellationEffectiveDate) {
        this.eResourceCancellationEffectiveDate = eResourceCancellationEffectiveDate;
    }

    public String geteResourceCancellationReason() {
        return eResourceCancellationReason;
    }

    public void seteResourceCancellationReason(String eResourceCancellationReason) {
        this.eResourceCancellationReason = eResourceCancellationReason;
    }

    public String geteResourceSubscriptionStatus() {
        return eResourceSubscriptionStatus;
    }

    public void seteResourceSubscriptionStatus(String eResourceSubscriptionStatus) {
        this.eResourceSubscriptionStatus = eResourceSubscriptionStatus;
    }
}
