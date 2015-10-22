package org.kuali.ole.select.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.document.OLEEResourceAccessWorkflow;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by syedk on 12/18/14.
 */
public class OLEEResourceAccessActivation extends PersistableBusinessObjectBase {

    private String oleERSAccessIdentifier;
    private String oleERSIdentifier;
    private String eResourceTitle;
    private String eResourceDocumentNumber;
    private String accessStatus;
    private Timestamp dateAccessConfirmed;
    private String accessTypeId;
    private String accessUserName;
    private String authenticationTypeId;
    private String accessPassword;
    private String numOfSimultaneousUsers;
    private String proxiedURL;
    private boolean proxiedResource;
    private OLEAccessType oleAccessType;
    private OLEAuthenticationType oleAuthenticationType;
    private String accessLocationId;
    private String mobileAccessId;
    private String mobileAccessNote;
    private boolean brandingComplete;
    private boolean platformConfigComplete;
    private String techRequirements;
    private String marcRecordSourceTypeId;
    private Timestamp lastRecordLoadDate;
    private String marcRecordSource;
    private Integer marcRecordUpdateFrequency;
    private String marcRecordUpdateRegularity;
    private String marcRecordURL;
    private String marcRecordUserName;
    private String marcRecordNote;
    private String marcRecordPassword;
    private List<String> accessLocation = new ArrayList<>();
    private List<String> mobileAccess = new ArrayList<>();
    private List<OLEEResourceAccessWorkflow> oleERSAccessWorkflows = new ArrayList<>();
    private List<OLEEResourceNotes> eresNotes = new ArrayList<OLEEResourceNotes>();
    private boolean workflowCompleted;
    private String workflowId;
    private String workflowName;
    private boolean workflowNameReadOnly = true;
    private String workflowDescription;
    private boolean adHocUserExists;
    private String adHocAccessStatus;
    private String adHocAccessStatusDummy;
    private List<OLEAdHocRoutingForAccessActivation> oleAdHocRoutingForAccessActivations = new ArrayList<>();
    public OLEEResourceAccessActivation(){
        getEresNotes().add(new OLEEResourceNotes());
    }

    public DateTimeService getDateTimeService() {
        return (DateTimeService) SpringContext.getService("dateTimeService");
    }

    public String getOleERSAccessIdentifier() {
        return oleERSAccessIdentifier;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String geteResourceTitle() {
        return eResourceTitle;
    }

    public void seteResourceTitle(String eResourceTitle) {
        this.eResourceTitle = eResourceTitle;
    }

    public String geteResourceDocumentNumber() {
        return eResourceDocumentNumber;
    }

    public void seteResourceDocumentNumber(String eResourceDocumentNumber) {
        this.eResourceDocumentNumber = eResourceDocumentNumber;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public void setOleERSAccessIdentifier(String oleERSAccessIdentifier) {
        this.oleERSAccessIdentifier = oleERSAccessIdentifier;
    }

    public Timestamp getDateAccessConfirmed() {
        return dateAccessConfirmed;
    }

    public void setDateAccessConfirmed(Timestamp dateAccessConfirmed) {
        this.dateAccessConfirmed = dateAccessConfirmed;
    }

    public String getAccessTypeId() {
        return accessTypeId;
    }

    public void setAccessTypeId(String accessTypeId) {
        this.accessTypeId = accessTypeId;
    }

    public String getAccessUserName() {
        return accessUserName;
    }

    public void setAccessUserName(String accessUserName) {
        this.accessUserName = accessUserName;
    }

    public String getAuthenticationTypeId() {
        return authenticationTypeId;
    }

    public void setAuthenticationTypeId(String authenticationTypeId) {
        this.authenticationTypeId = authenticationTypeId;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public String getNumOfSimultaneousUsers() {
        return numOfSimultaneousUsers;
    }

    public void setNumOfSimultaneousUsers(String numOfSimultaneousUsers) {
        this.numOfSimultaneousUsers = numOfSimultaneousUsers;
    }

    public String getProxiedURL() {
        return proxiedURL;
    }

    public void setProxiedURL(String proxiedURL) {
        this.proxiedURL = proxiedURL;
    }

    public boolean isProxiedResource() {
        return proxiedResource;
    }

    public void setProxiedResource(boolean proxiedResource) {
        this.proxiedResource = proxiedResource;
    }

    public OLEAccessType getOleAccessType() {
        return oleAccessType;
    }

    public void setOleAccessType(OLEAccessType oleAccessType) {
        this.oleAccessType = oleAccessType;
    }

    public OLEAuthenticationType getOleAuthenticationType() {
        return oleAuthenticationType;
    }

    public void setOleAuthenticationType(OLEAuthenticationType oleAuthenticationType) {
        this.oleAuthenticationType = oleAuthenticationType;
    }

    public String getAccessLocationId() {
        return accessLocationId;
    }

    public void setAccessLocationId(String accessLocationId) {
        this.accessLocationId = accessLocationId;
    }

    public String getMobileAccessId() {
        return mobileAccessId;
    }

    public void setMobileAccessId(String mobileAccessId) {
        this.mobileAccessId = mobileAccessId;
    }

    public String getMobileAccessNote() {
        return mobileAccessNote;
    }

    public void setMobileAccessNote(String mobileAccessNote) {
        this.mobileAccessNote = mobileAccessNote;
    }

    public boolean isBrandingComplete() {
        return brandingComplete;
    }

    public void setBrandingComplete(boolean brandingComplete) {
        this.brandingComplete = brandingComplete;
    }

    public boolean isPlatformConfigComplete() {
        return platformConfigComplete;
    }

    public void setPlatformConfigComplete(boolean platformConfigComplete) {
        this.platformConfigComplete = platformConfigComplete;
    }

    public String getTechRequirements() {
        return techRequirements;
    }

    public void setTechRequirements(String techRequirements) {
        this.techRequirements = techRequirements;
    }

    public String getMarcRecordSourceTypeId() {
        return marcRecordSourceTypeId;
    }

    public void setMarcRecordSourceTypeId(String marcRecordSourceTypeId) {
        this.marcRecordSourceTypeId = marcRecordSourceTypeId;
    }

    public Timestamp getLastRecordLoadDate() {
        return lastRecordLoadDate;
    }

    public void setLastRecordLoadDate(Timestamp lastRecordLoadDate) {
        this.lastRecordLoadDate = lastRecordLoadDate;
    }

    public String getMarcRecordSource() {
        return marcRecordSource;
    }

    public void setMarcRecordSource(String marcRecordSource) {
        this.marcRecordSource = marcRecordSource;
    }

    public Integer getMarcRecordUpdateFrequency() {
        return marcRecordUpdateFrequency;
    }

    public void setMarcRecordUpdateFrequency(Integer marcRecordUpdateFrequency) {
        this.marcRecordUpdateFrequency = marcRecordUpdateFrequency;
    }

    public String getMarcRecordURL() {
        return marcRecordURL;
    }

    public void setMarcRecordURL(String marcRecordURL) {
        this.marcRecordURL = marcRecordURL;
    }

    public String getMarcRecordUserName() {
        return marcRecordUserName;
    }

    public void setMarcRecordUserName(String marcRecordUserName) {
        this.marcRecordUserName = marcRecordUserName;
    }

    public String getMarcRecordNote() {
        return marcRecordNote;
    }

    public void setMarcRecordNote(String marcRecordNote) {
        this.marcRecordNote = marcRecordNote;
    }

    public String getMarcRecordPassword() {
        return marcRecordPassword;
    }

    public void setMarcRecordPassword(String marcRecordPassword) {
        this.marcRecordPassword = marcRecordPassword;
    }

    public List<String> getAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(List<String> accessLocation) {
        this.accessLocation = accessLocation;
    }

    public List<String> getMobileAccess() {
        return mobileAccess;
    }

    public void setMobileAccess(List<String> mobileAccess) {
        this.mobileAccess = mobileAccess;
    }

    public List<OLEEResourceAccessWorkflow> getOleERSAccessWorkflows() {
        return oleERSAccessWorkflows;
    }

    public void setOleERSAccessWorkflows(List<OLEEResourceAccessWorkflow> oleERSAccessWorkflows) {
        this.oleERSAccessWorkflows = oleERSAccessWorkflows;
    }

    public List<OLEEResourceNotes> getEresNotes() {
        return eresNotes;
    }

    public void setEresNotes(List<OLEEResourceNotes> eresNotes) {
        this.eresNotes = eresNotes;
    }

    public boolean isWorkflowCompleted() {
        return workflowCompleted;
    }

    public void setWorkflowCompleted(boolean workflowCompleted) {
        this.workflowCompleted = workflowCompleted;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public boolean isWorkflowNameReadOnly() {
        return workflowNameReadOnly;
    }

    public void setWorkflowNameReadOnly(boolean workflowNameReadOnly) {
        this.workflowNameReadOnly = workflowNameReadOnly;
    }

    public String getWorkflowDescription() {
        return workflowDescription;
    }

    public void setWorkflowDescription(String workflowDescription) {
        this.workflowDescription = workflowDescription;
    }

    public boolean isAdHocUserExists() {
        return adHocUserExists;
    }

    public void setAdHocUserExists(boolean adHocUserExists) {
        this.adHocUserExists = adHocUserExists;
    }

    public String getMarcRecordUpdateRegularity() {
        return marcRecordUpdateRegularity;
    }

    public void setMarcRecordUpdateRegularity(String marcRecordUpdateRegularity) {
        this.marcRecordUpdateRegularity = marcRecordUpdateRegularity;
    }

    public String getAdHocAccessStatus() {
        if(StringUtils.isNotBlank(getAdHocAccessStatusDummy())) {
            adHocAccessStatus = adHocAccessStatusDummy;
        }
        return adHocAccessStatus;
    }

    public void setAdHocAccessStatus(String adHocAccessStatus) {
        this.adHocAccessStatus = adHocAccessStatus;
    }

    public String getAdHocAccessStatusDummy() {
        return adHocAccessStatusDummy;
    }

    public void setAdHocAccessStatusDummy(String adHocAccessStatusDummy) {
        this.adHocAccessStatusDummy = adHocAccessStatusDummy;
    }

    public List<OLEAdHocRoutingForAccessActivation> getOleAdHocRoutingForAccessActivations() {
        return oleAdHocRoutingForAccessActivations;
    }

    public void setOleAdHocRoutingForAccessActivations(List<OLEAdHocRoutingForAccessActivation> oleAdHocRoutingForAccessActivations) {
        this.oleAdHocRoutingForAccessActivations = oleAdHocRoutingForAccessActivations;
    }
}
