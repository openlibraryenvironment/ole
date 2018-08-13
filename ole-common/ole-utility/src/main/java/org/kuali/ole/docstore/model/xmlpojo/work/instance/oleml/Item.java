package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Item information recorded in this section. This will not be mapping to any specific format i.e. MFHD.
 * Institutions wanting to export true holdings information will need to map themselves.
 * <p/>
 * <p/>
 * <p>Java class for item complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="item">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itemIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="purchaseOrderLineItemIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="vendorLineItemIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="accessInformation" type="{http://ole.kuali.org/standards/ole-instance}accessInformation"/>
 *         &lt;element name="barcodeARSL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="formerIdentifier" type="{http://ole.kuali.org/standards/ole-instance}formerIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="statisticalSearchingCode" type="{http://ole.kuali.org/standards/ole-instance}statisticalSearchingCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="itemType" type="{http://ole.kuali.org/standards/ole-instance}itemType"/>
 *         &lt;element name="location" type="{http://ole.kuali.org/standards/ole-instance}location" minOccurs="0"/>
 *         &lt;element name="donorInfo" type="{http://ole.kuali.org/standards/ole-instance}donorInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="copyNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="copyNumberLabel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="volumeNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="volumeNumberLabel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="note" type="{http://ole.kuali.org/standards/ole-instance}note"/>
 *         &lt;element name="enumeration" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="chronology" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="temporaryItemType" type="{http://ole.kuali.org/standards/ole-instance}itemType"/>
 *         &lt;element name="fund" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="donorPublicDisplay" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="donorNote" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="callNumber" type="{http://ole.kuali.org/standards/ole-instance}callNumber"/>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numberOfPieces" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="itemStatus" type="{http://ole.kuali.org/standards/ole-instance}itemStatus"/>
 *         &lt;element name="itemStatusEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="checkinNote" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="staffOnlyFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="fastAddFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="extension" type="{http://ole.kuali.org/standards/ole-instance}extension"/>
 *         &lt;element name="numberOfCirculations" type="{http://ole.kuali.org/standards/ole-instance}numberOfCirculations"/> *
 *         &lt;element name="currentBorrower" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="proxyBorrower" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dueDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="claimsReturnedFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="claimsReturnedFlagCreateDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="claimsReturnedNote" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="itemDamagedStatus" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="damagedItemNote" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="missingPieceFlagNote" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="missingPieceFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="missingPiecesCount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="missingPieceEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="analytic" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="resourceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
    "itemIdentifier",
    "purchaseOrderLineItemIdentifier",
    "vendorLineItemIdentifier",
    "accessInformation",
    "barcodeARSL",
    "formerIdentifier",
    "statisticalSearchingCode",
    "itemType",
    "location",
    "copyNumber",
    "copyNumberLabel",
    "volumeNumber",
    "volumeNumberLabel",
    "note",
    "enumeration",
    "chronology",
    "temporaryItemType",
    "fund",
    "donorCode",
    "donorPublicDisplay",
    "donorNote",
    "callNumber",
    "price",
    "numberOfPieces",
    "descriptionOfPieces",
    "itemStatus",
    "itemStatusEffectiveDate",
    "checkinNote",
    "staffOnlyFlag",
    "fastAddFlag",
    "extension",
    "numberOfCirculations",
    "currentBorrower",
    "proxyBorrower",
    "dueDateTime",
    "claimsReturnedFlag",
    "claimsReturnedFlagCreateDate",
    "claimsReturnedNote",
    "itemDamagedStatus",
    "damagedItemNote",
    "missingPieceFlagNote",
    "missingPieceFlag",
    "missingPiecesCount",
    "missingPieceEffectiveDate"
})
@XStreamAlias("item")
@XmlRootElement(name = "item", namespace = "http://ole.kuali.org/standards/ole-instance")
public class Item {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String itemIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String purchaseOrderLineItemIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String vendorLineItemIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected AccessInformation accessInformation;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String barcodeARSL;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    @XStreamImplicit(itemFieldName = "formerIdentifier")
    protected List<FormerIdentifier> formerIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    @XStreamImplicit(itemFieldName = "statisticalSearchingCode")
    protected List<StatisticalSearchingCode> statisticalSearchingCode;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected ItemType itemType;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    protected Location location;
//    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
//    protected List<DonorInfo> donorInfo;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String copyNumber;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String copyNumberLabel;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String volumeNumber;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String volumeNumberLabel;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    @XStreamImplicit(itemFieldName = "note")
    protected List<Note> note;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String enumeration;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String chronology;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected ItemType temporaryItemType;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String fund;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String donorCode;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String donorPublicDisplay;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String donorNote;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected CallNumber callNumber;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String price;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String numberOfPieces;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String descriptionOfPieces;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected ItemStatus itemStatus;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String itemStatusEffectiveDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String checkinNote;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    protected boolean staffOnlyFlag;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    protected boolean fastAddFlag;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Extension extension;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String currentBorrower;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String proxyBorrower;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String dueDateTime;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    protected boolean claimsReturnedFlag;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String claimsReturnedFlagCreateDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String claimsReturnedNote;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    protected boolean itemDamagedStatus;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    protected boolean missingPieceFlag;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String damagedItemNote;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String missingPieceFlagNote;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String missingPiecesCount;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String missingPieceEffectiveDate;
    @XmlAttribute
    @XStreamAsAttribute
    protected String analytic;
    @XmlAttribute
    @XStreamAsAttribute
    protected String resourceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    protected NumberOfCirculations numberOfCirculations;

    /**
     * Gets the value of the itemIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getItemIdentifier() {
        return itemIdentifier;
    }

    /**
     * Sets the value of the itemIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setItemIdentifier(String value) {
        this.itemIdentifier = value;
    }

    /**
     * Gets the value of the purchaseOrderLineItemIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPurchaseOrderLineItemIdentifier() {
        return purchaseOrderLineItemIdentifier;
    }

    /**
     * Sets the value of the purchaseOrderLineItemIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPurchaseOrderLineItemIdentifier(String value) {
        this.purchaseOrderLineItemIdentifier = value;
    }

    /**
     * Gets the value of the vendorLineItemIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVendorLineItemIdentifier() {
        return vendorLineItemIdentifier;
    }

    /**
     * Sets the value of the vendorLineItemIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVendorLineItemIdentifier(String value) {
        this.vendorLineItemIdentifier = value;
    }

    /**
     * Gets the value of the accessInformation property.
     *
     * @return possible object is
     *         {@link AccessInformation }
     */
    public AccessInformation getAccessInformation() {
        return accessInformation;
    }

    /**
     * Sets the value of the accessInformation property.
     *
     * @param value allowed object is
     *              {@link AccessInformation }
     */
    public void setAccessInformation(AccessInformation value) {
        this.accessInformation = value;
    }

    /**
     * Gets the value of the barcodeARSL property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBarcodeARSL() {
        return barcodeARSL;
    }

    /**
     * Sets the value of the barcodeARSL property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBarcodeARSL(String value) {
        this.barcodeARSL = value;
    }

    /**
     * Gets the value of the formerIdentifier property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the formerIdentifier property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormerIdentifier().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link FormerIdentifier }
     */
    public List<FormerIdentifier> getFormerIdentifier() {
        if (formerIdentifier == null) {
            formerIdentifier = new ArrayList<FormerIdentifier>();
        }
        return this.formerIdentifier;
    }

    /**
     * @param formerIdentifier
     */
    public void setFormerIdentifier(List<FormerIdentifier> formerIdentifier) {
        this.formerIdentifier = formerIdentifier;
    }

    /**
     * Gets the value of the statisticalSearchingCode property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statisticalSearchingCode property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatisticalSearchingCode().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link StatisticalSearchingCode }
     */
    public List<StatisticalSearchingCode> getStatisticalSearchingCode() {
        if (statisticalSearchingCode == null) {
            statisticalSearchingCode = new ArrayList<StatisticalSearchingCode>();
        }
        return this.statisticalSearchingCode;
    }

    /**
     * @param statisticalSearchingCode
     */
    public void setStatisticalSearchingCode(List<StatisticalSearchingCode> statisticalSearchingCode) {
        this.statisticalSearchingCode = statisticalSearchingCode;
    }

    /**
     * Gets the value of the itemType property.
     *
     * @return possible object is
     *         {@link ItemType }
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Sets the value of the itemType property.
     *
     * @param value allowed object is
     *              {@link ItemType }
     */
    public void setItemType(ItemType value) {
        this.itemType = value;
    }

    /**
     * Gets the value of the location property.
     *
     * @return possible object is
     *         {@link Location }
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value allowed object is
     *              {@link Location }
     */
    public void setLocation(Location value) {
        this.location = value;
    }

//    /**
//     * Gets the value of the donorInfo property.
//     *
//     * <p>
//     * This accessor method returns a reference to the live list,
//     * not a snapshot. Therefore any modification you make to the
//     * returned list will be present inside the JAXB object.
//     * This is why there is not a <CODE>set</CODE> method for the donorInfo property.
//     *
//     * <p>
//     * For example, to add a new item, do as follows:
//     * <pre>
//     *    getDonorInfo().add(newItem);
//     * </pre>
//     *
//     *
//     * <p>
//     * Objects of the following type(s) are allowed in the list
//     * {@link DonorInfo }
//     *
//     *
//     */
//    public List<DonorInfo> getDonorInfo() {
//        if (donorInfo == null) {
//            donorInfo = new ArrayList<DonorInfo>();
//        }
//        return this.donorInfo;
//    }
//
//    public void setDonorInfo(List<DonorInfo> value){
//        this.donorInfo= value;
//    }

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
     * Gets the value of the copyNumberLabel property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCopyNumberLabel() {
        return copyNumberLabel;
    }

    /**
     * Sets the value of the copyNumberLabel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCopyNumberLabel(String value) {
        this.copyNumberLabel = value;
    }

    /**
     * Gets the value of the volumeNumber property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVolumeNumber() {
        return volumeNumber;
    }

    /**
     * Sets the value of the volumeNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVolumeNumber(String value) {
        this.volumeNumber = value;
    }

    /**
     * Gets the value of the volumeNumberLabel property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVolumeNumberLabel() {
        return volumeNumberLabel;
    }

    /**
     * Sets the value of the volumeNumberLabel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVolumeNumberLabel(String value) {
        this.volumeNumberLabel = value;
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
     * {@link Note }
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
     * Gets the value of the enumeration property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEnumeration() {
        return enumeration;
    }

    /**
     * Sets the value of the enumeration property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEnumeration(String value) {
        this.enumeration = value;
    }

    /**
     * Gets the value of the chronology property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getChronology() {
        return chronology;
    }

    /**
     * Sets the value of the chronology property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setChronology(String value) {
        this.chronology = value;
    }

    /**
     * Gets the value of the temporaryItemType property.
     *
     * @return possible object is
     *         {@link ItemType }
     */
    public ItemType getTemporaryItemType() {
        return temporaryItemType;
    }

    /**
     * Sets the value of the temporaryItemType property.
     *
     * @param value allowed object is
     *              {@link ItemType }
     */
    public void setTemporaryItemType(ItemType value) {
        this.temporaryItemType = value;
    }

    /**
     * Gets the value of the fund property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getFund() {
        return fund;
    }

    /**
     * Sets the value of the fund property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFund(String value) {
        this.fund = value;
    }

    /**
     * Gets the value of the donorCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDonorCode() {
        return donorCode;
    }

    /**
     * Sets the value of the donorCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDonorCode(String value) {
        this.donorCode = value;
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
     * Gets the value of the price property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrice(String value) {
        this.price = value;
    }

    /**
     * Gets the value of the numberOfPieces property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getNumberOfPieces() {
        return numberOfPieces;
    }

    /**
     * Sets the value of the numberOfPieces property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumberOfPieces(String value) {
        this.numberOfPieces = value;
    }

    public String getDescriptionOfPieces() {
        return descriptionOfPieces;
    }

    public void setDescriptionOfPieces(String descriptionOfPieces) {
        this.descriptionOfPieces = descriptionOfPieces;
    }

    /**
     * Gets the value of the itemStatus property.
     *
     * @return possible object is
     *         {@link ItemStatus }
     */
    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    /**
     * Sets the value of the itemStatus property.
     *
     * @param value allowed object is
     *              {@link ItemStatus }
     */
    public void setItemStatus(ItemStatus value) {
        this.itemStatus = value;
    }

    /**
     * Gets the value of the itemStatusEffectiveDate property.
     *
     * @return possible object is
     *         {@link ItemStatus }
     */
    public String getItemStatusEffectiveDate() {
        return itemStatusEffectiveDate;
    }

    /**
     * Sets the value of the itemStatusEffectiveDate property.
     *
     * @param value allowed object is
     *              {@link ItemStatus }
     */
    public void setItemStatusEffectiveDate(String value) {
        this.itemStatusEffectiveDate = value;
    }

    /**
     * Gets the value of the checkinNote property.
     *
     * @return possible object is
     *         {@link ItemStatus }
     */
    public String getCheckinNote() {
        return checkinNote;
    }

    /**
     * Sets the value of the checkinNote property.
     *
     * @param value allowed object is
     *              {@link ItemStatus }
     */
    public void setCheckinNote(String value) {
        this.checkinNote = value;
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
     * Gets the value of the fastAddFlag property.
     */
    public boolean isFastAddFlag() {
        return fastAddFlag;
    }

    /**
     * Sets the value of the fastAddFlag property.
     */
    public void setFastAddFlag(boolean value) {
        this.fastAddFlag = value;
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
     * Gets the value of the analytic property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAnalytic() {
        return analytic;
    }

    /**
     * Sets the value of the analytic property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAnalytic(String value) {
        this.analytic = value;
    }

    /**
     * Gets the value of the resourceIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    /**
     * Sets the value of the resourceIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResourceIdentifier(String value) {
        this.resourceIdentifier = value;
    }

    /**
     * Gets the value of the numberOfCirculations property.
     *
     * @return possible object is
     *         {@link String }
     */
    public NumberOfCirculations getNumberOfCirculations() {
        return numberOfCirculations;
    }

    /**
     * Sets the value of the resourceIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumberOfCirculations(NumberOfCirculations value) {
        this.numberOfCirculations = value;
    }

    /**
     * Gets the value of the currentBorrower property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCurrentBorrower() {
        return currentBorrower;
    }

    /**
     * Sets the value of the currentBorrower property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCurrentBorrower(String value) {
        this.currentBorrower = value;
    }

    /**
     * Gets the value of the proxyBorrower property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProxyBorrower() {
        return proxyBorrower;
    }

    /**
     * Sets the value of the proxyBorrower property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProxyBorrower(String value) {
        this.proxyBorrower = value;
    }

    /**
     * Gets the value of the dueDateTime property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDueDateTime() {
        return dueDateTime;
    }

    /**
     * Sets the value of the dueDateTime property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDueDateTime(String value) {
        this.dueDateTime = value;
    }

    /**
     * Gets the value of the claimsReturnedFlag property.
     *
     */
    public boolean isClaimsReturnedFlag() {
        return claimsReturnedFlag;
    }

    /**
     * Sets the value of the claimsReturnedFlag property.
     *
     */
    public void setClaimsReturnedFlag(boolean value) {
        this.claimsReturnedFlag = value;
    }

    /**
     * Gets the value of the claimsReturnedFlagCreateDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClaimsReturnedFlagCreateDate() {
        return claimsReturnedFlagCreateDate;
    }

    /**
     * Sets the value of the claimsReturnedFlagCreateDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClaimsReturnedFlagCreateDate(String value) {
        this.claimsReturnedFlagCreateDate = value;
    }

    /**
     * Gets the value of the claimsReturnedNote property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClaimsReturnedNote() {
        return claimsReturnedNote;
    }

    /**
     * Sets the value of the claimsReturnedNote property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClaimsReturnedNote(String value) {
        this.claimsReturnedNote = value;
    }

    /**
     * Gets the value of the itemDamagedStatus property.
     *
     */
    public boolean isItemDamagedStatus() {
        return itemDamagedStatus;
    }

    /**
     * Sets the value of the itemDamagedStatus property.
     *
     */
    public void setItemDamagedStatus(boolean value) {
        this.itemDamagedStatus = value;
    }

    /**
     * Gets the value of the missingPieceFlag property.
     *
     */
    public boolean isMissingPieceFlag() {
        return missingPieceFlag;
    }

    /**
     * Sets the value of the missingPieceFlag property.
     *
     */
    public void setMissingPieceFlag(boolean value) {
        this.missingPieceFlag = value;
    }

    /**
     * Gets the value of the missingPiecesCount property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMissingPiecesCount() {
        return missingPiecesCount;
    }

    /**
     * Sets the value of the missingPiecesCount property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMissingPiecesCount(String value) {
        this.missingPiecesCount = value;
    }

    /**
     * Gets the value of the missingPieceEffectiveDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMissingPieceEffectiveDate() {
        return missingPieceEffectiveDate;
    }

    /**
     * Sets the value of the missingPieceEffectiveDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMissingPieceEffectiveDate(String value) {
        this.missingPieceEffectiveDate = value;
    }

    /**
     * Gets the value of the damagedItemNote property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDamagedItemNote() {
        return damagedItemNote;
    }

    /**
     * Sets the value of the damagedItemNote property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDamagedItemNote(String value) {
        this.damagedItemNote = value;
    }

    /**
     * Gets the value of the missingPieceFlagNote property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMissingPieceFlagNote() {
        return missingPieceFlagNote;
    }

    /**
     * Sets the value of the missingPieceFlagNote property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMissingPieceFlagNote(String value) {
        this.missingPieceFlagNote = value;
    }

}
