package org.kuali.ole.select.bo;

import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * OleLicenseRequestBo is the business object class for License Request Maintenance Document.
 */
public class OleLicenseRequestBo extends PersistableBusinessObjectBase {

    private String oleLicenseRequestId;
    private String licenseRequestStatusCode;
    private String assignee;
    private String locationId;
    private String licenseRequestTypeId;
    private String eResourceDocNumber;
    private String agreementId;
    private String licenseRequestWorkflowTypeCode;
    private String documentNumber;

    private String agreementMethod;
    private List<OleAgreementDocumentMetadata> agreementDocumentMetadataList = new ArrayList<OleAgreementDocumentMetadata>();
    private List<OleEventLogBo> eventLogs = new ArrayList<OleEventLogBo>();
    private List<OleLicenseRequestItemTitle> oleLicenseRequestItemTitles = new ArrayList<OleLicenseRequestItemTitle>();
    private OleLicenseRequestStatus oleLicenseRequestStatus;
    private OleLicenseRequestLocation oleLicenseRequestLocation = new OleLicenseRequestLocation();
    private OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType = new OleLicenseRequestWorkflowType();

    private String eResourceName;
    private String linkedAgreement;
    private String agreementDate;
    private String agreementUser;
    private String agreementStatus;
    private String agreementType;
    private String agreementMd;
    private OleLicenseRequestType oleLicenseRequestType = new OleLicenseRequestType();
    private Timestamp createdDateFrom;
    private Timestamp createdDateTo;
    private Timestamp createdDate;

    private Timestamp lastModifiedDateFrom;
    private Timestamp lastModifiedDateTo;
    private boolean lastModifiedDateSearchType;

    private String licenseDocumentNumber;
    private DocumentRouteHeaderValue documentRouteHeaderValue = new DocumentRouteHeaderValue();

    private OleAgreementMethod oleAgreementMethod = new OleAgreementMethod();
    private String agreementMethodId;


    public String getAgreementMethodId() {
        return agreementMethodId;
    }

    public void setAgreementMethodId(String agreementMethodId) {
        this.agreementMethodId = agreementMethodId;
    }

    public OleAgreementMethod getOleAgreementMethod() {
        return oleAgreementMethod;
    }

    public void setOleAgreementMethod(OleAgreementMethod oleAgreementMethod) {
        this.oleAgreementMethod = oleAgreementMethod;
    }

    public void setOleLicenseRequestType(OleLicenseRequestType oleLicenseRequestType) {
        this.oleLicenseRequestType = oleLicenseRequestType;
    }

    public OleLicenseRequestType getOleLicenseRequestType() {

        return oleLicenseRequestType;
    }

    private boolean present;

    /**
     * Initiate oleLicenseRequestStatus.
     */
    public OleLicenseRequestBo() {
        /*if(oleLicenseRequestStatus==null){
            oleLicenseRequestStatus = new OleLicenseRequestStatus();
            oleLicenseRequestStatus.setName("License Needed1");
        }*/
    }

    public String geteResourceName() {
        return eResourceName;
    }

    public void seteResourceName(String eResourceName) {
        this.eResourceName = eResourceName;
    }

    /**
     * Gets the oleLicenseRequestStatus attribute.
     *
     * @return Returns the oleLicenseRequestStatus
     */
    public OleLicenseRequestStatus getOleLicenseRequestStatus() {
        return oleLicenseRequestStatus;
    }

    /**
     * Sets the oleLicenseRequestStatus attribute value.
     *
     * @param oleLicenseRequestStatus The oleLicenseRequestStatus to set.
     */
    public void setOleLicenseRequestStatus(OleLicenseRequestStatus oleLicenseRequestStatus) {
        this.oleLicenseRequestStatus = oleLicenseRequestStatus;
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
     * Gets the licenseRequestStatusCode attribute.
     *
     * @return Returns the licenseRequestStatusCode
     */
    public String getLicenseRequestStatusCode() {
        return licenseRequestStatusCode;
    }

    /**
     * Sets the licenseRequestStatusCode attribute value.
     *
     * @param licenseRequestStatusCode The licenseRequestStatusCode to set.
     */
    public void setLicenseRequestStatusCode(String licenseRequestStatusCode) {
        this.licenseRequestStatusCode = licenseRequestStatusCode;
    }

    /**
     * Gets the assignee attribute.
     *
     * @return Returns the assignee
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * Sets the assignee attribute value.
     *
     * @param assignee The assignee to set.
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    /**
     * Gets the locationId attribute.
     *
     * @return Returns the locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * Sets the locationId attribute value.
     *
     * @param locationId The locationId to set.
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * Gets the licenseRequestTypeId attribute.
     *
     * @return Returns the licenseRequestTypeId
     */
    public String getLicenseRequestTypeId() {
        return licenseRequestTypeId;
    }

    /**
     * Sets the licenseRequestTypeId attribute value.
     *
     * @param licenseRequestTypeId The licenseRequestTypeId to set.
     */
    public void setLicenseRequestTypeId(String licenseRequestTypeId) {
        this.licenseRequestTypeId = licenseRequestTypeId;
    }

    /**
     * Gets the eventLogs attribute.
     *
     * @return Returns the eventLogs
     */
    public List<OleEventLogBo> getEventLogs() {
        return eventLogs;
    }

    /**
     * Sets the eventLogs attribute value.
     *
     * @param eventLogs The eventLogs to set.
     */
    public void setEventLogs(List<OleEventLogBo> eventLogs) {
        this.eventLogs = eventLogs;
    }

    /**
     * Gets the requisitionDocNumber attribute.
     *
     * @return Returns the requisitionDocNumber
     */
    public String geteResourceDocNumber() {
        return eResourceDocNumber;
    }


    /**
     * Sets the requisitionDocNumber attribute value.
     *
     * @param eResourceDocNumber The requisitionDocNumber to set.
     */
    public void seteResourceDocNumber(String eResourceDocNumber) {
        this.eResourceDocNumber = eResourceDocNumber;
    }

    /**
     * Gets the agreementId attribute.
     *
     * @return Returns the agreementId
     */
    public String getAgreementId() {
        return agreementId;
    }

    /**
     * Sets the agreementId attribute value.
     *
     * @param agreementId The agreementId to set.
     */
    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * Gets the licenseRequestWorkflowTypeCode attribute.
     *
     * @return Returns the licenseRequestWorkflowTypeCode
     */
    public String getLicenseRequestWorkflowTypeCode() {
        return licenseRequestWorkflowTypeCode;
    }

    /**
     * Sets the licenseRequestWorkflowTypeCode attribute value.
     *
     * @param licenseRequestWorkflowTypeCode The licenseRequestWorkflowTypeCode to set.
     */
    public void setLicenseRequestWorkflowTypeCode(String licenseRequestWorkflowTypeCode) {
        this.licenseRequestWorkflowTypeCode = licenseRequestWorkflowTypeCode;
    }

    /**
     * Gets the agreementMethod attribute.
     *
     * @return Returns the agreementMethod
     */
    public String getAgreementMethod() {
        return agreementMethod;
    }

    /**
     * Sets the agreementMethod attribute value.
     *
     * @param agreementMethod The agreementMethod to set.
     */
    public void setAgreementMethod(String agreementMethod) {
        this.agreementMethod = agreementMethod;
    }

    /**
     * Gets the agreementDocumentMetadataList attribute.
     *
     * @return Returns the agreementDocumentMetadataList
     */
    public List<OleAgreementDocumentMetadata> getAgreementDocumentMetadataList() {
        return agreementDocumentMetadataList;
    }

    /**
     * Sets the agreementDocumentMetadataList attribute value.
     *
     * @param agreementDocumentMetadataList The agreementDocumentMetadataList to set.
     */
    public void setAgreementDocumentMetadataList(List<OleAgreementDocumentMetadata> agreementDocumentMetadataList) {
        this.agreementDocumentMetadataList = agreementDocumentMetadataList;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public OleLicenseRequestLocation getOleLicenseRequestLocation() {
        return oleLicenseRequestLocation;
    }

    public void setOleLicenseRequestLocation(OleLicenseRequestLocation oleLicenseRequestLocation) {
        this.oleLicenseRequestLocation = oleLicenseRequestLocation;
    }

    public OleLicenseRequestWorkflowType getOleLicenseRequestWorkflowType() {
        return oleLicenseRequestWorkflowType;
    }

    public void setOleLicenseRequestWorkflowType(OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType) {
        this.oleLicenseRequestWorkflowType = oleLicenseRequestWorkflowType;
    }

    public String getLinkedAgreement() {
        return linkedAgreement;
    }

    public void setLinkedAgreement(String linkedAgreement) {
        this.linkedAgreement = linkedAgreement;
    }

    public String getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(String agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getAgreementUser() {
        return agreementUser;
    }

    public void setAgreementUser(String agreementUser) {
        this.agreementUser = agreementUser;
    }

    public String getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(String agreementStatus) {
        this.agreementStatus = agreementStatus;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public String getAgreementMd() {
        return agreementMd;
    }

    public void setAgreementMd(String agreementMd) {
        this.agreementMd = agreementMd;
    }

    public boolean isPresent() {
        return (this.agreementDate != null || this.agreementUser != null || this.agreementStatus != null ||
                this.agreementType != null || this.agreementMd != null || licenseRequestStatusCode != null) ? true : false;
    }

    public List<OleLicenseRequestItemTitle> getOleLicenseRequestItemTitles() {
        return oleLicenseRequestItemTitles;
    }

    public void setOleLicenseRequestItemTitles(List<OleLicenseRequestItemTitle> oleLicenseRequestItemTitles) {
        this.oleLicenseRequestItemTitles = oleLicenseRequestItemTitles;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(Timestamp createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public Timestamp getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(Timestamp createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    /**
     * Gets the lastModifiedDateFrom attribute.
     *
     * @return Returns the lastModifiedDateFrom
     */
    public Timestamp getLastModifiedDateFrom() {
        return lastModifiedDateFrom;
    }

    /**
     * Sets the lastModifiedDateFrom attribute value.
     *
     * @param lastModifiedDateFrom
     */
    public void setLastModifiedDateFrom(Timestamp lastModifiedDateFrom) {
        this.lastModifiedDateFrom = lastModifiedDateFrom;
    }

    /**
     * Gets the lastModifiedDateTo attribute.
     *
     * @return Returns the lastModifiedDateTo
     */
    public Timestamp getLastModifiedDateTo() {
        return lastModifiedDateTo;
    }

    /**
     * Sets the lastModifiedDateTo attribute value.
     *
     * @param lastModifiedDateTo
     */
    public void setLastModifiedDateTo(Timestamp lastModifiedDateTo) {
        this.lastModifiedDateTo = lastModifiedDateTo;
    }

    /**
     * Gets the lastModifiedDateSearchType attribute.
     *
     * @return Returns the lastModifiedDateSearchType
     */
    public boolean isLastModifiedDateSearchType() {
        return lastModifiedDateSearchType;
    }

    /**
     * Sets the lastModifiedDateSearchType attribute value.
     *
     * @param lastModifiedDateSearchType
     */
    public void setLastModifiedDateSearchType(boolean lastModifiedDateSearchType) {
        this.lastModifiedDateSearchType = lastModifiedDateSearchType;
    }


    public String getLicenseDocumentNumber() {
        return licenseDocumentNumber;
    }

    public void setLicenseDocumentNumber(String licenseDocumentNumber) {
        this.licenseDocumentNumber = licenseDocumentNumber;
    }

    public DocumentRouteHeaderValue getDocumentRouteHeaderValue() {
        return documentRouteHeaderValue;
    }

    public void setDocumentRouteHeaderValue(DocumentRouteHeaderValue documentRouteHeaderValue) {
        this.documentRouteHeaderValue = documentRouteHeaderValue;
    }
}