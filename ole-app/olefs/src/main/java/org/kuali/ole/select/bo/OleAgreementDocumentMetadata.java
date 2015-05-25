package org.kuali.ole.select.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * OleAgreementDocumentMetadata is the business object class for Agreement.
 */
public class OleAgreementDocumentMetadata extends PersistableBusinessObjectBase {
    private Timestamp uploadedDate;
    private String uploadedBy;
    private String agreementType;
    private String agreementName;
    private String agreementFileName;
    private String agreementNotes;
    private String agreementVersion;
    private String agreementMimeType;
    private String agreementUUID;
    private String oleAgreementDocumentId;
    private String oleLicenseRequestId;
    private OleLicenseRequestBo oleLicenseRequestBo;
    private String selected;

    /**
     * Gets the uploadedDate attribute.
     *
     * @return Returns the uploadedDate
     */
    public Timestamp getUploadedDate() {
        return uploadedDate;
    }

    /**
     * Sets the uploadedDate attribute value.
     *
     * @param uploadedDate The uploadedDate to set.
     */
    public void setUploadedDate(Timestamp uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    /**
     * Gets the uploadedBy attribute.
     *
     * @return Returns the uploadedBy
     */
    public String getUploadedBy() {
        return uploadedBy;
    }

    /**
     * Sets the uploadedBy attribute value.
     *
     * @param uploadedBy The uploadedBy to set.
     */
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    /**
     * Gets the agreementType attribute.
     *
     * @return Returns the agreementType
     */
    public String getAgreementType() {
        return agreementType;
    }

    /**
     * Sets the agreementType attribute value.
     *
     * @param agreementType The agreementType to set.
     */
    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    /**
     * Gets the agreementName attribute.
     *
     * @return Returns the agreementName
     */
    public String getAgreementName() {
        return agreementName;
    }

    /**
     * Sets the agreementName attribute value.
     *
     * @param agreementName The agreementName to set.
     */
    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
    }

    /**
     * Gets the agreementFileName attribute.
     *
     * @return Returns the agreementFileName
     */
    public String getAgreementFileName() {
        return agreementFileName;
    }

    /**
     * Sets the agreementFileName attribute value.
     *
     * @param agreementFileName The agreementFileName to set.
     */
    public void setAgreementFileName(String agreementFileName) {
        this.agreementFileName = agreementFileName;
    }

    /**
     * Gets the agreementNotes attribute.
     *
     * @return Returns the agreementNotes
     */
    public String getAgreementNotes() {
        return agreementNotes;
    }

    /**
     * Sets the agreementNotes attribute value.
     *
     * @param agreementNotes The agreementNotes to set.
     */
    public void setAgreementNotes(String agreementNotes) {
        this.agreementNotes = agreementNotes;
    }

    /**
     * Gets the agreementVersion attribute.
     *
     * @return Returns the agreementVersion
     */
    public String getAgreementVersion() {
        return agreementVersion;
    }

    /**
     * Sets the agreementVersion attribute value.
     *
     * @param agreementVersion The agreementVersion to set.
     */
    public void setAgreementVersion(String agreementVersion) {
        this.agreementVersion = agreementVersion;
    }

    /**
     * Gets the agreementMimeType attribute.
     *
     * @return Returns the agreementMimeType
     */
    public String getAgreementMimeType() {
        return agreementMimeType;
    }

    /**
     * Sets the agreementMimeType attribute value.
     *
     * @param agreementMimeType The agreementMimeType to set.
     */
    public void setAgreementMimeType(String agreementMimeType) {
        this.agreementMimeType = agreementMimeType;
    }

    /**
     * Gets the agreementUUID attribute.
     *
     * @return Returns the agreementUUID
     */
    public String getAgreementUUID() {
        return agreementUUID;
    }

    /**
     * Sets the agreementUUID attribute value.
     *
     * @param agreementUUID The agreementUUID to set.
     */
    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    /**
     * Gets the oleAgreementDocumentId attribute.
     *
     * @return Returns the oleAgreementDocumentId
     */
    public String getOleAgreementDocumentId() {
        return oleAgreementDocumentId;
    }

    /**
     * Sets the oleAgreementDocumentId attribute value.
     *
     * @param oleAgreementDocumentId The oleAgreementDocumentId to set.
     */
    public void setOleAgreementDocumentId(String oleAgreementDocumentId) {
        this.oleAgreementDocumentId = oleAgreementDocumentId;
    }

    /**
     * Gets the oleLicenseRequestId attribute.
     *
     * @return Returns the oleLicenseRequestId
     */
    public String getOleLicenseRequestId() {
        return oleLicenseRequestId;
    }

    /**
     * Sets the oleLicenseRequestId attribute value.
     *
     * @param oleLicenseRequestId The oleLicenseRequestId to set.
     */
    public void setOleLicenseRequestId(String oleLicenseRequestId) {
        this.oleLicenseRequestId = oleLicenseRequestId;
    }

    /**
     * Gets the oleLicenseRequestBo attribute.
     *
     * @return Returns the oleLicenseRequestBo
     */
    public OleLicenseRequestBo getOleLicenseRequestBo() {
        return oleLicenseRequestBo;
    }

    /**
     * Sets the oleLicenseRequestBo attribute value.
     *
     * @param oleLicenseRequestBo The oleLicenseRequestBo to set.
     */
    public void setOleLicenseRequestBo(OleLicenseRequestBo oleLicenseRequestBo) {
        this.oleLicenseRequestBo = oleLicenseRequestBo;
    }

    /**
     * Gets the isComplete attribute.
     *
     * @return boolean
     */
    public boolean isComplete() {
        return StringUtils.isNotBlank(agreementFileName) && StringUtils.isNotBlank(agreementMimeType);
    }

    /**
     * Sets the setUploadedDate attribute value.
     */
    public void setCurrentTimeStamp() {
        final Timestamp now = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
        this.setUploadedDate(now);
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
