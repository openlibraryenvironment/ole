package org.kuali.ole.docstore.common.document;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.bind.annotation.*;


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for docstoreDocument complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="docstoreDocument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="category" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contentObject" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="createdBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="createdOn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fastAdd" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="format" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="public" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="staffOnly" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusUpdatedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusUpdatedOn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="updatedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="updatedOn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "docstoreDocument", propOrder = {
        "active",
        "category",
        "content",
        "createdBy",
        "createdOn",
        "fastAdd",
        "format",
        "id",
        "localId",
        "_public",
        "staffOnly",
        "status",
        "statusUpdatedBy",
        "statusUpdatedOn",
        "type",
        "updatedBy",
        "updatedOn",
        "lastUpdated",
        "displayLabel",
        "sortedValue",
        "operation",
        "result",
        "message"
})
@XmlSeeAlso({
        Bib.class
})
public abstract class DocstoreDocument {

    public static final String ID = "ID";
    public static final String CREATED_BY = "CREATEDBY";
    public static final String UPDATED_BY = "UPDATEDBY";
    public static final String DATE_ENTERED = "DATEENTERED";
    public static final String DATE_UPDATED = "DATEUPDATED";
    public static final String STATUS = "STATUS";
    public static final String STATUS_UPDATED_ON = "STATUSUPDATEDON";
    public static final String DESTINATION_FIELD_DONOR_CODE = "Donor Code";
    public static final String DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY = "Donor Public Display";
    public static final String DESTINATION_FIELD_DONOR_NOTE = "Donor Note";
    public static final String NO_INFO_CALL_NUMBER_TYPE_CODE = "NOINFO";

    public enum OperationType {CREATE, UPDATE, DELETE, NONE};
    public enum ResultType {SUCCESS, FAILURE};

    public OperationType operation;
    public ResultType result;
    public String message="";

    protected boolean active = false;
    protected String category;
    protected String content;
    @XmlTransient
    protected Object contentObject;
    protected String createdBy;
    protected String createdOn;
    protected boolean fastAdd = false;
    protected String format;
    protected String id;
    protected String localId;
    @XmlElement(name = "public")
    protected boolean _public = false;
    protected boolean staffOnly = false;
    protected String status;
    protected String statusUpdatedBy;
    protected String statusUpdatedOn;
    protected String type;
    protected String updatedBy;
    protected String updatedOn;
    protected String lastUpdated;
    protected String displayLabel;
    protected String sortedValue;

    /**
     * Gets the value of the active property.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the category property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the content property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the contentObject property.
     *
     * @return possible object is
     *         {@link Object }
     */
    public Object getContentObject() {
        return contentObject;
    }

    /**
     * Sets the value of the contentObject property.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setContentObject(Object value) {
        this.contentObject = value;
    }

    /**
     * Gets the value of the createdBy property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the createdOn property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the createdOn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCreatedOn(String value) {
        this.createdOn = value;
    }

    /**
     * Gets the value of the fastAdd property.
     */
    public boolean isFastAdd() {
        return fastAdd;
    }

    /**
     * Sets the value of the fastAdd property.
     */
    public void setFastAdd(boolean value) {
        this.fastAdd = value;
    }

    /**
     * Gets the value of the format property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the localId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLocalId() {
        return localId;
    }

    /**
     * Sets the value of the localId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocalId(String value) {
        this.localId = value;
    }

    /**
     * Gets the value of the public property.
     */
    public boolean isPublic() {
        return _public;
    }

    /**
     * Sets the value of the public property.
     */
    public void setPublic(boolean value) {
        this._public = value;
    }

    /**
     * Gets the value of the staffOnly property.
     */
    public boolean isStaffOnly() {
        return staffOnly;
    }

    /**
     * Sets the value of the staffOnly property.
     */
    public void setStaffOnly(boolean value) {
        this.staffOnly = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusUpdatedBy property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    /**
     * Sets the value of the statusUpdatedBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatusUpdatedBy(String value) {
        this.statusUpdatedBy = value;
    }

    /**
     * Gets the value of the statusUpdatedOn property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStatusUpdatedOn() {
        return statusUpdatedOn;
    }

    /**
     * Sets the value of the statusUpdatedOn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatusUpdatedOn(String value) {
        this.statusUpdatedOn = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the updatedBy property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the value of the updatedBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUpdatedBy(String value) {
        this.updatedBy = value;
    }

    /**
     * Gets the value of the updatedOn property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the value of the updatedOn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUpdatedOn(String value) {
        this.updatedOn = value;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getSortedValue() {
        return sortedValue;
    }

    public void setSortedValue(String sortedValue) {
        this.sortedValue = sortedValue;
    }

    public abstract String serialize(Object object);

    public abstract Object deserialize(String content);

    public abstract Object deserializeContent(Object object);

    public abstract Object deserializeContent(String content);

    public abstract String serializeContent(Object object);

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public ResultType getResult() {
        return result;
    }

    public void setResult(ResultType result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
