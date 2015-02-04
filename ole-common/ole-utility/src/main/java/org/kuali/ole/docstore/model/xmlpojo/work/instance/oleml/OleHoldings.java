package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.bind.annotation.*;
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
@XmlType(name = "oleHoldings", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
        "holdingsIdentifier",
        "extentOfOwnership",
        "receiptStatus",
        "uri",
        "note",
        "location",
        "extension",
        "callNumber",
        "copyNumber",
        "createdBy",
        "createdDate",
        "updatedBy",
        "updatedDate"
})
@XStreamAlias("oleHoldings")
@XmlRootElement(name = "oleHoldings", namespace = "http://ole.kuali.org/standards/ole-instance")
public class OleHoldings {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String holdingsIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    @XStreamImplicit(itemFieldName = "extentOfOwnership")
    protected List<ExtentOfOwnership> extentOfOwnership;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String receiptStatus;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    @XStreamImplicit(itemFieldName = "uri")
    protected List<Uri> uri;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    @XStreamImplicit(itemFieldName = "note")
    protected List<Note> note;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Location location;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Extension extension;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected CallNumber callNumber;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String copyNumber;
    @XmlAttribute
    @XStreamAsAttribute
    protected String primary;

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String createdBy;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = false)
    protected String updatedBy;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Timestamp createdDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = false)
    protected Timestamp updatedDate;


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
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

}
