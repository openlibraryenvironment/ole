package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String licenseId;
    private String createdBy;
    private Timestamp dateCreated;
    private String updatedBy;
    private Timestamp dateUpdated;
    private byte[] content;
    private String uniqueIdPrefix;
    private String fileName;
    private String documentTitle;
    private String documentMimeType;
    private String documentNote;
    private String agreementType;
    private String agreementNote;

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getUniqueIdPrefix() {
        return uniqueIdPrefix;
    }

    public void setUniqueIdPrefix(String uniqueIdPrefix) {
        this.uniqueIdPrefix = uniqueIdPrefix;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentMimeType() {
        return documentMimeType;
    }

    public void setDocumentMimeType(String documentMimeType) {
        this.documentMimeType = documentMimeType;
    }

    public String getDocumentNote() {
        return documentNote;
    }

    public void setDocumentNote(String documentNote) {
        this.documentNote = documentNote;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public String getAgreementNote() {
        return agreementNote;
    }

    public void setAgreementNote(String agreementNote) {
        this.agreementNote = agreementNote;
    }
}
