package org.kuali.ole.select.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;


/**
 * OleCheckListBo is the business object class for CheckList Maintenance Document
 */
public class OleCheckListBo extends PersistableBusinessObjectBase {
    private Long oleCheckListId;
    private Timestamp lastModified;
    private String description;
    private String name;
    private String author;
    private boolean activeIndicator;
    private String fileName;
    private String remoteObjectIdentifier;
    private String mimeType;

    /**
     * Gets the oleCheckListId attribute.
     *
     * @return Returns the oleCheckListId
     */
    public Long getOleCheckListId() {
        return oleCheckListId;
    }

    /**
     * Sets the oleCheckListId attribute value.
     *
     * @param oleCheckListId The oleCheckListId to set.
     */
    public void setOleCheckListId(Long oleCheckListId) {
        this.oleCheckListId = oleCheckListId;
    }

    /**
     * Gets the description attribute.
     *
     * @return Returns the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description attribute value.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the lastModified attribute.
     *
     * @return Returns the lastModified
     */
    public Timestamp getLastModified() {
        return lastModified;
    }

    /**
     * Sets the lastModified attribute value.
     *
     * @param lastModified The lastModified to set.
     */
    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Gets the name attribute.
     *
     * @return Returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name attribute value.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the author attribute.
     *
     * @return Returns the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author attribute value.
     *
     * @param author The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the activeIndicator attribute.
     *
     * @return Returns the activeIndicator
     */
    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    /**
     * Sets the activeIndicator attribute value.
     *
     * @param activeIndicator The activeIndicator to set.
     */
    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    /**
     * Gets the fileName attribute.
     *
     * @return Returns the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the fileName attribute value.
     *
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the remoteObjectIdentifier attribute.
     *
     * @return Returns the remoteObjectIdentifier
     */
    public String getRemoteObjectIdentifier() {
        return remoteObjectIdentifier;
    }

    /**
     * Sets the remoteObjectIdentifier attribute value.
     *
     * @param remoteObjectIdentifier The remoteObjectIdentifier to set.
     */
    public void setRemoteObjectIdentifier(String remoteObjectIdentifier) {
        this.remoteObjectIdentifier = remoteObjectIdentifier;
    }

    /**
     * Gets the mimeType attribute.
     *
     * @return Returns the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the mimeType attribute value.
     *
     * @param mimeType The mimeType to set.
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Gets the isComplete attribute.
     *
     * @return boolean
     */
    public boolean isComplete() {
        return StringUtils.isNotBlank(fileName) && StringUtils.isNotBlank(mimeType);
    }
}
