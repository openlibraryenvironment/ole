package org.kuali.ole.select.form;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.document.OLEEResourceInstance;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/21/13
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceRecordForm extends TransactionalDocumentFormBase {

    private boolean selectFlag;
    private String statusDate;
    private String documentDescription;
    private String instanceId;
    private String bibId;
    private boolean linkInstance;
    private boolean createInstance;
    private String selectInstance;
    private boolean selectLinkInstance;
    private boolean selectCreateInstance;
    private boolean coverageFlag;
    private boolean perpetualAccessFlag;
    private boolean removeInstanceFlag;
    private boolean defaultDatesFlag;
    private String defaultCovStartDateErrorMessage;
    private String defaultCovEndDateErrorMessage;
    private String defaultPerAccStartDateErrorMessage;
    private String defaultPerAccEndDateErrorMessage;
    private boolean flagPop = false;
    private String selectedPOType;
    private List<OLECreatePO> instancePOs = new ArrayList<>();
    private List<OLECreatePO> eResourcePOs = new ArrayList<>();

    List<OLEEResourceInstance> oleeResourceInstances = new ArrayList<>();

    private String confirmationMessageForHoldings = OLEConstants.CONFIRMATION_MESSAGE_INSTANCE;
    private String confirmationMessageForEResource = OLEConstants.CONFIRMATION_MESSAGE_E_RESOURCE;

    private List<OLEEResourceRecordDocument> eresourceDocumentList = new ArrayList<OLEEResourceRecordDocument>();
    public String url;
    private boolean emailFlag;

    private int index;
    private String addLineForHoldingsOrEResource;
    private String purposeType;
    private String deletedInstanceInfo = "";
    private int deletedInstance =0;

    private String searchUrl;
    private OLESearchParams oleSearchParams = new OLESearchParams();
    private Date beginDate;
    private Date endDate;
    private List<String> status;
    private boolean eResStatusDate;
    private boolean selectEResFlag = true;
    private String message;
    private String poErrorMessage;
    private String poSuccessMessage;

    private String packageName;
    private String platformName;
    private String platformProvider;
    private List<String> platformProviderList;
    private String title;
    private String titleInstanceType;
    private String publisher;
    private boolean showTiipResults;
    private boolean showTippsWithMorePlatform;
    private boolean showTippResults;
    private boolean importPackageMetaDataOnly;
    private boolean showDeletedTipps;
    private String currentPackageName;
    private String currentPlatformName;
    private List<OLEPhoneNumber> phoneNos = new ArrayList<>();
    private String vendorNameForContacts;
    private List<OLEGOKbMappingValue> oleGOKbMappingValueList = new ArrayList<OLEGOKbMappingValue>();
    private boolean showMultiplePlatforms = false;
    private String packageId;
    private boolean access ;
    private String profileErrorMessage;
    public boolean canApprove;

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }
     public OLEEResourceRecordForm() {
        super();
        this.selectedPOType = OLEConstants.OLEEResourceRecord.ONE_PO_PER_TITLE;
        List<OLESearchCondition> searchConditions = new ArrayList<OLESearchCondition>();
        searchConditions = getOleSearchParams().getSearchFieldsList();
        searchConditions.add(new OLESearchCondition());
        searchConditions.add(new OLESearchCondition());
        searchConditions.add(new OLESearchCondition());
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_ERS_DOC";
    }

    public boolean isFlagPop() {
        return flagPop;
    }

    public void setFlagPop(boolean flagPop) {
        this.flagPop = flagPop;
    }

    private OLEEResourceRecordDocument oleeResourceRecordDocument;

    public OLEEResourceRecordDocument getOleeResourceRecordDocument() {
        return oleeResourceRecordDocument;
    }

    public void setOleeResourceRecordDocument(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        this.oleeResourceRecordDocument = oleeResourceRecordDocument;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public boolean isLinkInstance() {
        return linkInstance;
    }

    public void setLinkInstance(boolean linkInstance) {
        this.linkInstance = linkInstance;
    }

    public String getSelectInstance() {
        return selectInstance;
    }

    public void setSelectInstance(String selectInstance) {
        this.selectInstance = selectInstance;
    }

    public boolean isSelectLinkInstance() {
        return selectLinkInstance;
    }

    public void setSelectLinkInstance(boolean selectLinkInstance) {
        this.selectLinkInstance = selectLinkInstance;
    }

    public boolean isSelectCreateInstance() {
        return selectCreateInstance;
    }

    public void setSelectCreateInstance(boolean selectCreateInstance) {
        this.selectCreateInstance = selectCreateInstance;
    }

    public boolean isCreateInstance() {
        return createInstance;
    }

    public void setCreateInstance(boolean createInstance) {
        this.createInstance = createInstance;
    }

    public boolean isCoverageFlag() {
        return coverageFlag;
    }

    public void setCoverageFlag(boolean coverageFlag) {
        this.coverageFlag = coverageFlag;
    }

    public boolean isPerpetualAccessFlag() {
        return perpetualAccessFlag;
    }

    public void setPerpetualAccessFlag(boolean perpetualAccessFlag) {
        this.perpetualAccessFlag = perpetualAccessFlag;
    }

    public boolean isRemoveInstanceFlag() {
        return removeInstanceFlag;
    }

    public void setRemoveInstanceFlag(boolean removeInstanceFlag) {
        this.removeInstanceFlag = removeInstanceFlag;
    }

    public boolean isDefaultDatesFlag() {
        return defaultDatesFlag;
    }

    public void setDefaultDatesFlag(boolean defaultDatesFlag) {
        this.defaultDatesFlag = defaultDatesFlag;
    }

    public String getDefaultCovStartDateErrorMessage() {
        return defaultCovStartDateErrorMessage;
    }

    public void setDefaultCovStartDateErrorMessage(String defaultCovStartDateErrorMessage) {
        this.defaultCovStartDateErrorMessage = defaultCovStartDateErrorMessage;
    }

    public String getDefaultCovEndDateErrorMessage() {
        return defaultCovEndDateErrorMessage;
    }

    public void setDefaultCovEndDateErrorMessage(String defaultCovEndDateErrorMessage) {
        this.defaultCovEndDateErrorMessage = defaultCovEndDateErrorMessage;
    }

    public String getDefaultPerAccStartDateErrorMessage() {
        return defaultPerAccStartDateErrorMessage;
    }

    public void setDefaultPerAccStartDateErrorMessage(String defaultPerAccStartDateErrorMessage) {
        this.defaultPerAccStartDateErrorMessage = defaultPerAccStartDateErrorMessage;
    }

    public String getDefaultPerAccEndDateErrorMessage() {
        return defaultPerAccEndDateErrorMessage;
    }

    public void setDefaultPerAccEndDateErrorMessage(String defaultPerAccEndDateErrorMessage) {
        this.defaultPerAccEndDateErrorMessage = defaultPerAccEndDateErrorMessage;
    }

    public String getSelectedPOType() {
        return selectedPOType;
    }

    public void setSelectedPOType(String selectedPOType) {
        this.selectedPOType = selectedPOType;
    }

    public List<OLECreatePO> getInstancePOs() {
        return instancePOs;
    }

    public void setInstancePOs(List<OLECreatePO> instancePOs) {
        this.instancePOs = instancePOs;
    }

    public String getConfirmationMessageForHoldings() {
        return confirmationMessageForHoldings;
    }

    public void setConfirmationMessageForHoldings(String confirmationMessageForHoldings) {
        this.confirmationMessageForHoldings = confirmationMessageForHoldings;
    }

    public List<OLECreatePO> geteResourcePOs() {
        return eResourcePOs;
    }

    public void seteResourcePOs(List<OLECreatePO> eResourcePOs) {
        this.eResourcePOs = eResourcePOs;
    }

    public List<OLEEResourceRecordDocument> getEresourceDocumentList() {
        return eresourceDocumentList;
    }

    public void setEresourceDocumentList(List<OLEEResourceRecordDocument> eresourceDocumentList) {
        this.eresourceDocumentList = eresourceDocumentList;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEmailFlag() {
        return emailFlag;
    }

    public void setEmailFlag(boolean emailFlag) {
        this.emailFlag = emailFlag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getAddLineForHoldingsOrEResource() {
        return addLineForHoldingsOrEResource;
    }

    public void setAddLineForHoldingsOrEResource(String addLineForHoldingsOrEResource) {
        this.addLineForHoldingsOrEResource = addLineForHoldingsOrEResource;
    }

    public String getConfirmationMessageForEResource() {
        return confirmationMessageForEResource;
    }

    public void setConfirmationMessageForEResource(String confirmationMessageForEResource) {
        this.confirmationMessageForEResource = confirmationMessageForEResource;
    }

    public String getPurposeType() {
        return purposeType;
    }

    public void setPurposeType(String purposeType) {
        this.purposeType = purposeType;
    }

    public List<OLEEResourceInstance> getOleeResourceInstances() {
        return oleeResourceInstances;
    }

    public void setOleeResourceInstances(List<OLEEResourceInstance> oleeResourceInstances) {
        this.oleeResourceInstances = oleeResourceInstances;
    }

    public String getDeletedInstanceInfo() {
        return deletedInstanceInfo;
    }

    public void setDeletedInstanceInfo(String deletedInstanceInfo) {
        this.deletedInstanceInfo = deletedInstanceInfo;
    }

    public int getDeletedInstance() {
        return deletedInstance;
    }

    public void setDeletedInstance(int deletedInstance) {
        this.deletedInstance = deletedInstance;
    }

    public OLESearchParams getOleSearchParams() {
        return oleSearchParams;
    }

    public void setOleSearchParams(OLESearchParams oleSearchParams) {
        this.oleSearchParams = oleSearchParams;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public boolean iseResStatusDate() {
        return eResStatusDate;
    }

    public void seteResStatusDate(boolean eResStatusDate) {
        this.eResStatusDate = eResStatusDate;
    }

    public boolean isSelectEResFlag() {
        return selectEResFlag;
    }

    public void setSelectEResFlag(boolean selectEResFlag) {
        this.selectEResFlag = selectEResFlag;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformProvider() {
        return platformProvider;
    }

    public void setPlatformProvider(String platformProvider) {
        this.platformProvider = platformProvider;
    }

    public List<String> getPlatformProviderList() {
        return platformProviderList;
    }

    public void setPlatformProviderList(List<String> platformProviderList) {
        this.platformProviderList = platformProviderList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleInstanceType() {
        return titleInstanceType;
    }

    public void setTitleInstanceType(String titleInstanceType) {
        this.titleInstanceType = titleInstanceType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isShowTiipResults() {
        return showTiipResults;
    }

    public void setShowTiipResults(boolean showTiipResults) {
        this.showTiipResults = showTiipResults;
    }

    public boolean isShowTippsWithMorePlatform() {
        return showTippsWithMorePlatform;
    }

    public void setShowTippsWithMorePlatform(boolean showTippsWithMorePlatform) {
        this.showTippsWithMorePlatform = showTippsWithMorePlatform;
    }

    public boolean isShowTippResults() {
        return showTippResults;
    }

    public void setShowTippResults(boolean showTippResults) {
        this.showTippResults = showTippResults;
    }

    public boolean isImportPackageMetaDataOnly() {
        return importPackageMetaDataOnly;
    }

    public void setImportPackageMetaDataOnly(boolean importPackageMetaDataOnly) {
        this.importPackageMetaDataOnly = importPackageMetaDataOnly;
    }

    public boolean isShowDeletedTipps() {
        return showDeletedTipps;
    }

    public void setShowDeletedTipps(boolean showDeletedTipps) {
        this.showDeletedTipps = showDeletedTipps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPoErrorMessage() {
        return poErrorMessage;
    }

    public void setPoErrorMessage(String poErrorMessage) {
        this.poErrorMessage = poErrorMessage;
    }

    public String getPoSuccessMessage() {
        return poSuccessMessage;
    }

    public void setPoSuccessMessage(String poSuccessMessage) {
        this.poSuccessMessage = poSuccessMessage;
    }

    public String getCurrentPackageName() {
        return currentPackageName;
    }

    public void setCurrentPackageName(String currentPackageName) {
        this.currentPackageName = currentPackageName;
    }

    public String getCurrentPlatformName() {
        return currentPlatformName;
    }

    public void setCurrentPlatformName(String currentPlatformName) {
        this.currentPlatformName = currentPlatformName;
    }

    public List<OLEPhoneNumber> getPhoneNos() {
        return phoneNos;
    }

    public void setPhoneNos(List<OLEPhoneNumber> phoneNos) {
        this.phoneNos = phoneNos;
    }

    public String getVendorNameForContacts() {
        return vendorNameForContacts;
    }

    public void setVendorNameForContacts(String vendorNameForContacts) {
        this.vendorNameForContacts = vendorNameForContacts;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public boolean isShowMultiplePlatforms() {
        return showMultiplePlatforms;
    }

    public void setShowMultiplePlatforms(boolean showMultiplePlatforms) {
        this.showMultiplePlatforms = showMultiplePlatforms;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public List<OLEGOKbMappingValue> getOleGOKbMappingValueList() {
        return oleGOKbMappingValueList;
    }

    public void setOleGOKbMappingValueList(List<OLEGOKbMappingValue> oleGOKbMappingValueList) {
        this.oleGOKbMappingValueList = oleGOKbMappingValueList;
    }

    public String getProfileErrorMessage() {
        return profileErrorMessage;
    }

    public void setProfileErrorMessage(String profileErrorMessage) {
        this.profileErrorMessage = profileErrorMessage;
    }
}
