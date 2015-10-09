package org.kuali.ole.select.document;

import org.joda.time.DateTime;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.document.OleTransactionalDocumentBase;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.content.instance.Link;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.form.OLEEResourceRecordForm;
import org.kuali.ole.service.OLEEResourceHelperService;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/21/13
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceRecordDocument extends OleTransactionalDocumentBase {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEEResourceRecordDocument.class);
    private BusinessObjectService boService;
    private OLEEResourceSearchService oleEResourceSearchService = null;
    private OLEEResourceHelperService oleeResourceHelperService = null;
    private String accessDescription;
    private String oleERSIdentifier;
    private String title;
    private String oldTitle;
    private String description;
    private String publisher;
    private String publisherId;
    private Integer gokbIdentifier;
    private String isbn;
    private String ISBN;
    private String oclc;
    private String platformProvider;
    private String statusId;
    private String statusName;
    private String statusDate;
    private String fundCode;
    private String fundId;
    /*private Integer vendorDetailAssignedIdentifier;
    private Integer vendorHeaderGeneratedIdentifier;*/
    private String vendorName;
    private String vendorId;
    private String vendorLink;
    private String estimatedPrice;
    private BigDecimal orderTypeId;
    private String paymentTypeId;
    private String packageTypeId;
    private String packageScopeId;
    private boolean breakable;
    private boolean fixedTitleList;
    private String noteId;
    private String publicDisplayNote;
    private String reqSelComment;
    private String reqPriority;
    private String techRequirements;
    private String accessTypeId;
    private String accessType;
    private String accessStatus;
    private String numOfSimultaneousUsers;
    private String authenticationTypeId;
    private String authenticationType;
    private String accessLocationId;
    private List<String> accessLocation = new ArrayList<>();
    private List<String> accessLocationName = new ArrayList<>();
    private boolean trialNeeded;
    private String trialStatus;
    private boolean licenseNeeded;
    private String licenseReqStatus;
    private String orderPayStatus;
    private String activationStatus;
    private boolean selectFlag;
    private Integer statisticalSearchingCodeId;
    private String statisticalSearchingCode;
    private String selectInstance;
    private String defaultCoverage;
    private String defaultCoverageView;
    private String defaultPerpetualAccess;
    private String defaultPerpetualAccessView;
    private OLEEResourceInstance oleERSInstance;
    private boolean eInstanceFlag;
    private String dummyDefaultCoverage;
    private String dummyDefaultPerpetualAccess;
    private boolean isCovEdited = false;
    private boolean isPerAccEdited = false;
    private String covStartDate;
    private String covEndDate;
    private String perAccStartDate;
    private String perAccEndDate;
    private String gokbId ;
    private String subscriptionStatus;
    private Timestamp currentSubscriptionStartDate;
    private Timestamp currentSubscriptionEndDate;
    private Timestamp initialSubscriptionStartDate;
    private Timestamp cancellationDecisionDate;
    private Timestamp cancellationEffectiveDate;
    private String cancellationReason;
    private boolean cancellationCandidate;
    private boolean renewalAlertEnabled;
    private String renewalNoticePeriod;
    private String recipientId;
    private String eresourceWorkflowIndication;
    private String relationshipType;
    private int activeTitlesCount;

    private OLEAccessType oleAccessType;
    private OLEPackageScope olePackageScope;
    private OLEPackageType olePackageType;
    private OLEPaymentType olePaymentType;
    private OLERequestPriority oleRequestPriority;
    private OLEAuthenticationType oleAuthenticationType;
    private OleStatisticalSearchingCodes oleStatisticalCode;
    private OLEEResourceStatus oleeResourceStatus;
    private VendorDetail vendorDetail;
    private PurchaseOrderType orderType;

    private List<OLEMaterialTypeList> oleMaterialTypes = new ArrayList<OLEMaterialTypeList>();
    private List<OLEContentTypes> oleContentTypes = new ArrayList<OLEContentTypes>();
    private List<OLEFormatTypeList> oleFormatTypes = new ArrayList<OLEFormatTypeList>();
    private List<OLEEResourceNotes> eresNotes = new ArrayList<OLEEResourceNotes>();
    private List<OLEGOKbMappingValue> oleGOKbMappingValueList = new ArrayList<OLEGOKbMappingValue>();
    private List<OLEPlatformAdminUrl> olePlatformAdminUrlList = new ArrayList<OLEPlatformAdminUrl>();
    private List<OLEEResourceRequestor> requestors = new ArrayList<OLEEResourceRequestor>();
    private List<OLEEResourceSelector> selectors = new ArrayList<OLEEResourceSelector>();
    private List<OLEEResourceReqSelComments> reqSelComments = new ArrayList<OLEEResourceReqSelComments>();
    private List<OLEEResourceVariantTitle> oleEResourceVariantTitleList = new ArrayList<OLEEResourceVariantTitle>();
    private List<OLEEResourceEventLog> oleERSEventLogs = new ArrayList<OLEEResourceEventLog>();
    private List<OLEEResourceLicense> oleERSLicenseRequests = new ArrayList<OLEEResourceLicense>();
    private List<OLEEResourceInstance> oleERSInstances = new ArrayList<OLEEResourceInstance>();
    private List<OLEEResourceInstance> eRSInstances = new ArrayList<OLEEResourceInstance>();
    private List<OLEEResourceInstance> deletedInstances = new ArrayList<OLEEResourceInstance>();
    private List<OLEEResourceInstance> purchaseOrderInstances = new ArrayList<OLEEResourceInstance>();
    private List<OLEEResourceInvoices> oleERSInvoices = new ArrayList<>();
    private List<OLEEResourceInvoices> eRSInvoices = new ArrayList<>();
    private List<OLEEResourceContacts> oleERSContacts = new ArrayList<OLEEResourceContacts>();
    private List<OLEEResourceChangeDashBoard> oleEResourceChangeDashBoards=  new ArrayList<>();
    private List<OLELinkedEresource> oleLinkedEresources=  new ArrayList<>();
    private List<OleCopy> copyList = new ArrayList<>();
    private List<OLEEResourcePO> oleERSPOItems = new ArrayList<>();
    private List<OLEEResourcePO> linkedERSPOItems = new ArrayList<>();
    private List<OLEEResourcePO> eRSPOItems = new ArrayList<>();
    private List<OLEEResourceAccountingLine> accountingLines = new ArrayList<>();
    private OLEEResourceRecordForm form;
    private String status=null;
    private String gokbconfig;
    private String removeOrRelinkToParent;
    private DocstoreClientLocator docstoreClientLocator;
    private String phone;
    private double fiscalYearCost;
    private double yearPriceQuote;
    private double costIncrease;
    private double percentageIncrease;
    private String emailText;
    private Integer gokbPackageId;

    private List<OLEPhoneNumber> phoneNos = new ArrayList<>();
    public boolean selectEResFlag;
    private List<OLEStandardIdentifier> standardIdentifiers = new ArrayList<>();
    private List<OLEGOKbPackage> goKbPackageList = new ArrayList<>();
    private List<OLEGOKbPlatform> goKbPlatformList = new ArrayList<>();
    private List<OLEGOKbPlatform> goKbPlatforms = new ArrayList<>();
    private List<OLEGOKbTIPP> goKbTIPPList = new ArrayList<>();
    private String profile;
    private String gokbProfile;
    private String gokbPackageStatus = "";

    private Timestamp dateAccessConfirmed;
    private String accessUserName;
    private String accessPassword;
    private String proxiedURL;
    private boolean proxiedResource;
    private String mobileAccessId;
    private List<String> mobileAccess = new ArrayList<>();
    private String mobileAccessNote;
    private boolean brandingComplete;
    private boolean platformConfigComplete;
    private String marcRecordSourceTypeId;
    private Timestamp lastRecordLoadDate;
    private String marcRecordSource;
    private String marcRecordUpdateFreqId;
    private String marcRecordURL;
    private String marcRecordUserName;
    private String marcRecordPasword;
    private String marcRecordNote;
    private List<OLEEResourceAccessWorkflow> oleERSAccessWorkflows = new ArrayList<>();
    private DateTimeService dateTimeService;
    private String lineActions;
    private String bannerMessage;
    private Timestamp gokbLastUpdatedDate;
    private String workflowConfigurationId;
    private String oleAccessActivationDocumentNumber;
    private List<OLEGOKbPlatform> selectedGoKbPlatforms = new ArrayList<>();
    private boolean accessReadOnly = false;

    public List<OLEGOKbPlatform> getSelectedGoKbPlatforms() {
        return selectedGoKbPlatforms;
    }

    private boolean singlePlatform=true;
    private Integer singlePaltformId;
    private String workflowStatus;

    private String publisherLink;

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(String workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public boolean isSinglePlatform() {
        return singlePlatform;
    }

    public void setSinglePlatform(boolean singlePlatform) {
        this.singlePlatform = singlePlatform;
    }

    public Integer getSinglePaltformId() {
        return singlePaltformId;
    }

    public void setSinglePaltformId(Integer singlePaltformId) {
        this.singlePaltformId = singlePaltformId;
    }

    public void setSelectedGoKbPlatforms(List<OLEGOKbPlatform> selectedGoKbPlatforms) {
        this.selectedGoKbPlatforms = selectedGoKbPlatforms;
    }

    public BusinessObjectService getBoService() {
        if(null == boService){
            boService = KRADServiceLocator.getBusinessObjectService();
        }
        return boService;
    }

    public Integer getGokbPackageId() {
        return gokbPackageId;
    }

    public void setGokbPackageId(Integer gokbPackageId) {
        this.gokbPackageId = gokbPackageId;
    }

    public void setBoService(BusinessObjectService boService) {
        this.boService = boService;
    }

    public DateTimeService getDateTimeService() {
        return (DateTimeService)SpringContext.getService("dateTimeService");
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public OLEEResourceHelperService getOleeResourceHelperService() {
        if(oleeResourceHelperService == null) {
            oleeResourceHelperService = new OLEEResourceHelperService();
        }
        return oleeResourceHelperService;
    }


    public OLEEResourceRecordDocument() {
        getOleMaterialTypes().add(new OLEMaterialTypeList());
        getOleFormatTypes().add(new OLEFormatTypeList());
        getOleContentTypes().add(new OLEContentTypes());
        getRequestors().add(new OLEEResourceRequestor());
        getSelectors().add(new OLEEResourceSelector());
        getReqSelComments().add(new OLEEResourceReqSelComments());
        getOleEResourceVariantTitleList().add(new OLEEResourceVariantTitle());
        getEresNotes().add(new OLEEResourceNotes());
        getStandardIdentifiers().add(new OLEStandardIdentifier());
    }


//   private String getMask()
//    {
//        return "******";
//    }


    public List<OLEPlatformAdminUrl> getOlePlatformAdminUrlList() {

//        if (this.olePlatformAdminUrlList.isEmpty())
//        {
//        OLEPlatformAdminUrl olePlatformAdminUrl = new OLEPlatformAdminUrl(),
//        olePlatformAdminUrl1 = new OLEPlatformAdminUrl(),
//        olePlatformAdminUrl2 = new OLEPlatformAdminUrl(),
//        olePlatformAdminUrl3 = new OLEPlatformAdminUrl();
//        OLEPlatformRecordDocument olePlatformRecordDocument = new OLEPlatformRecordDocument();
//
//        olePlatformRecordDocument.setName("Science Direct");
//        olePlatformAdminUrl.setOlePlatformRecordDocument(olePlatformRecordDocument);
//        olePlatformAdminUrl.setUrl("http://www.sciencedirect.com/");
//        olePlatformAdminUrl.setNote("Elsevier B.V. except certain content provided by third parties. ScienceDirect® is a registered trademark of Elsevier B.V.");
//        olePlatformAdminUrl.setUserName("root");
//        olePlatformAdminUrl.setTypeId("ADMIN");
//            olePlatformAdminUrl.setUserName(olePlatformAdminUrl.getUserName());
//            if (getParameter(OLEConstants.OLEEResourceRecord.MASK_PASSWORD).equalsIgnoreCase("Y"))
//            {
//                olePlatformAdminUrl.setPassword(getMask());
//            }
//            else
//            {
//                olePlatformAdminUrl.setPassword("89432hd");
//            }
//
//        olePlatformRecordDocument.setName("Medicine & Dentistry");
//        olePlatformAdminUrl1.setOlePlatformRecordDocument(olePlatformRecordDocument);
//        olePlatformAdminUrl1.setUrl("http://www.Medicine&Dentistry.com/");
//        olePlatformAdminUrl1.setNote("The word platform simply describes all the ways you are visible and appealing to your future, potential, or actual readership.");
//        olePlatformAdminUrl1.setUserName("Hazel");
//        if (getParameter(OLEConstants.OLEEResourceRecord.MASK_PASSWORD).equalsIgnoreCase("Y"))
//            {
//                olePlatformAdminUrl1.setPassword(getMask());
//            }
//            else
//            {
//                olePlatformAdminUrl1.setPassword("BHJH&&*HJ");
//            }
//        olePlatformAdminUrl1.setTypeId("STATS");
//
//        olePlatformRecordDocument.setName("Book Designer");
//        olePlatformAdminUrl1.setOlePlatformRecordDocument(olePlatformRecordDocument);
//        olePlatformAdminUrl2.setUrl("http://www.thebookdesigner.com/2013/07/rowling-galbraith/");
//        olePlatformAdminUrl2.setNote("The literary world was shocked this past week by the revelation that J.K. Rowling.");
//        olePlatformAdminUrl2.setUserName("Kiara");
//        if (getParameter(OLEConstants.OLEEResourceRecord.MASK_PASSWORD).equalsIgnoreCase("Y"))
//        {
//          olePlatformAdminUrl1.setPassword(getMask());
//        }
//        else
//        {
//           olePlatformAdminUrl1.setPassword("BHJH&&*HJ");
//        }
//        olePlatformAdminUrl2.setTypeId("CONSORTIA");
//
//        olePlatformRecordDocument.setName("Sapien Commute");
//        olePlatformAdminUrl1.setOlePlatformRecordDocument(olePlatformRecordDocument);
//        olePlatformAdminUrl3.setUrl("http://www.sapiencommute.com/");
//        olePlatformAdminUrl3.setNote("Elsevier B.V. except certain content provided by third parties. ScienceDirect® is a registered trademark of Elsevier B.V.");
//        olePlatformAdminUrl3.setUserName("root");
//        if (getParameter(OLEConstants.OLEEResourceRecord.MASK_PASSWORD).equalsIgnoreCase("Y"))
//        {
//            olePlatformAdminUrl1.setPassword(getMask());
//        }
//        else
//        {
//            olePlatformAdminUrl3.setPassword("JHJK#YUI");
//        }
//        olePlatformAdminUrl3.setTypeId("ADMIN");
//
//        olePlatformAdminUrlList.add(olePlatformAdminUrl);
//        olePlatformAdminUrlList.add(olePlatformAdminUrl1);
//        olePlatformAdminUrlList.add(olePlatformAdminUrl2);
//        olePlatformAdminUrlList.add(olePlatformAdminUrl3);
//        }

        return olePlatformAdminUrlList;
    }

    public final String getParameter(String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    public List<OLEEResourceVariantTitle> getOleEResourceVariantTitleList() {
        return oleEResourceVariantTitleList;
    }

    public void setOleEResourceVariantTitleList(List<OLEEResourceVariantTitle> oleEResourceVariantTitleList) {
        this.oleEResourceVariantTitleList = oleEResourceVariantTitleList;
    }

    public void setOlePlatformAdminUrlList(List<OLEPlatformAdminUrl> olePlatformAdminUrlList) {
        this.olePlatformAdminUrlList = olePlatformAdminUrlList;
    }

    public String getEresourceWorkflowIndication() {
        return eresourceWorkflowIndication;
    }

    public void setEresourceWorkflowIndication(String eresourceWorkflowIndication) {
        this.eresourceWorkflowIndication = eresourceWorkflowIndication;
    }

    public String getStatusName() {
        if (getStatusId() != null) {
            oleeResourceStatus = getBoService().findBySinglePrimaryKey(OLEEResourceStatus.class, statusId);
            statusName = oleeResourceStatus.getOleEResourceStatusName();
        }
        return statusName;
    }

    public OLEEResourceSearchService getOleEResourceSearchService() {
        if (oleEResourceSearchService == null) {
            oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        }
        return oleEResourceSearchService;
    }



    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public Timestamp getCancellationDecisionDate() {
        return cancellationDecisionDate;
    }

    public void setCancellationDecisionDate(Timestamp cancellationDecisionDate) {
        this.cancellationDecisionDate = cancellationDecisionDate;
    }

    public Timestamp getCancellationEffectiveDate() {
        return cancellationEffectiveDate;
    }

    public void setCancellationEffectiveDate(Timestamp cancellationEffectiveDate) {
        this.cancellationEffectiveDate = cancellationEffectiveDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public boolean isCancellationCandidate() {
        return cancellationCandidate;
    }

    public void setCancellationCandidate(boolean cancellationCandidate) {
        this.cancellationCandidate = cancellationCandidate;
    }

    public boolean isRenewalAlertEnabled() {
        return renewalAlertEnabled;
    }

    public void setRenewalAlertEnabled(boolean renewalAlertEnabled) {
        this.renewalAlertEnabled = renewalAlertEnabled;
    }

    public String getRenewalNoticePeriod() {
        return renewalNoticePeriod;
    }

    public void setRenewalNoticePeriod(String renewalNoticePeriod) {
        this.renewalNoticePeriod = renewalNoticePeriod;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Timestamp getCurrentSubscriptionStartDate() {
        return currentSubscriptionStartDate;
    }

    public void setCurrentSubscriptionStartDate(Timestamp currentSubscriptionStartDate) {
        this.currentSubscriptionStartDate = currentSubscriptionStartDate;
    }

    public Timestamp getCurrentSubscriptionEndDate() {
        return currentSubscriptionEndDate;
    }

    public void setCurrentSubscriptionEndDate(Timestamp currentSubscriptionEndDate) {
        this.currentSubscriptionEndDate = currentSubscriptionEndDate;
    }

    public Timestamp getInitialSubscriptionStartDate() {
        return initialSubscriptionStartDate;
    }

    public void setInitialSubscriptionStartDate(Timestamp initialSubscriptionStartDate) {
        this.initialSubscriptionStartDate = initialSubscriptionStartDate;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public OLEEResourceRecordForm getForm() {
        return form;
    }

    public void setForm(OLEEResourceRecordForm form) {
        this.form = form;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getOclc() {
        return oclc;
    }

    public void setOclc(String oclc) {
        this.oclc = oclc;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorLink() {
        if (StringUtils.isNotBlank(this.getVendorId())){
            String[] vendorDetails = this.getVendorId().split("-");
            Integer vendorHeaderGeneratedIdentifier = vendorDetails.length > 0 ? Integer.parseInt(vendorDetails[0]) : 0;
            Integer vendorDetailAssignedIdentifier = vendorDetails.length > 1 ? Integer.parseInt(vendorDetails[1]) : 0;
            String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
            String url = oleurl + "/kr/inquiry.do?methodToCall=start&amp;businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&amp;vendorHeaderGeneratedIdentifier=" + vendorHeaderGeneratedIdentifier + "&amp;vendorDetailAssignedIdentifier="
                    + vendorDetailAssignedIdentifier;
            return url;
        }
        return vendorLink;
    }

    public void setVendorLink(String vendorLink) {
        this.vendorLink = vendorLink;
    }

    public String getStatisticalSearchingCode() {
        return statisticalSearchingCode;
    }

    public void setStatisticalSearchingCode(String statisticalSearchingCode) {
        this.statisticalSearchingCode = statisticalSearchingCode;
    }

    public Integer getStatisticalSearchingCodeId() {
        return statisticalSearchingCodeId;
    }

    public void setStatisticalSearchingCodeId(Integer statisticalSearchingCodeId) {
        this.statisticalSearchingCodeId = statisticalSearchingCodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        if (publisher==null && StringUtils.isNotBlank(publisherId)) {
            String[] publisherDetails = publisherId.split("-");
            Map vendorMap = new HashMap<>();
            int vendorHeaderGeneratedIdentifier = publisherDetails.length > 0 ? Integer.parseInt(publisherDetails[0]) : 0;
            int vendorDetailAssignedIdentifier = publisherDetails.length > 1 ? Integer.parseInt(publisherDetails[1]) : 0;
            vendorMap.put(OLEConstants.OLEEResourceRecord.VENDOR_HEADER_GEN_ID, vendorHeaderGeneratedIdentifier);
            vendorMap.put(OLEConstants.OLEEResourceRecord.VENDOR_DETAILED_ASSIGNED_ID, vendorDetailAssignedIdentifier);
            VendorDetail vendorDetailDoc = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorMap);
            if (vendorDetailDoc != null) {
                return vendorDetailDoc.getVendorName();
            }
        }
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getGokbIdentifier() {
        return gokbIdentifier;
    }

    public void setGokbIdentifier(Integer gokbIdentifier) {
        this.gokbIdentifier = gokbIdentifier;
    }

    public void setGokbIdentifier(String gokbIdentifier) {
        setGokbIdentifier(Integer.parseInt(gokbIdentifier));
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPlatformProvider() {
        return platformProvider;
    }

    public void setPlatformProvider(String platformProvider) {
        this.platformProvider = platformProvider;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusDate() {
        if (this.statusDate != null) {
            return statusDate.substring(0, 10);
        } else
            return new Date(System.currentTimeMillis()).toString().substring(0, 10);
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(String estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public BigDecimal getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(BigDecimal orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(String packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public String getPackageScopeId() {
        return packageScopeId;
    }

    public void setPackageScopeId(String packageScopeId) {
        this.packageScopeId = packageScopeId;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isFixedTitleList() {
        return fixedTitleList;
    }

    public void setFixedTitleList(boolean fixedTitleList) {
        this.fixedTitleList = fixedTitleList;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getPublicDisplayNote() {
        return publicDisplayNote;
    }

    public void setPublicDisplayNote(String publicDisplayNote) {
        this.publicDisplayNote = publicDisplayNote;
    }

    public String getReqSelComment() {
        return reqSelComment;
    }

    public void setReqSelComment(String reqSelComment) {
        this.reqSelComment = reqSelComment;
    }

    public String getReqPriority() {
        return reqPriority;
    }

    public void setReqPriority(String reqPriority) {
        this.reqPriority = reqPriority;
    }

    public String getTechRequirements() {
        return techRequirements;
    }

    public void setTechRequirements(String techRequirements) {
        this.techRequirements = techRequirements;
    }

    public String getAccessTypeId() {
        return accessTypeId;
    }

    public void setAccessTypeId(String accessTypeId) {
        this.accessTypeId = accessTypeId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getNumOfSimultaneousUsers() {
        return numOfSimultaneousUsers;
    }

    public void setNumOfSimultaneousUsers(String numOfSimultaneousUsers) {
        this.numOfSimultaneousUsers = numOfSimultaneousUsers;
    }

    public String getAuthenticationTypeId() {
        return authenticationTypeId;
    }

    public void setAuthenticationTypeId(String authenticationTypeId) {
        this.authenticationTypeId = authenticationTypeId;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getAccessLocationId() {
        return accessLocationId;
    }

    public void setAccessLocationId(String accessLocationId) {
        this.accessLocationId = accessLocationId;
    }

    public List<String> getAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(List<String> accessLocation) {
        this.accessLocation = accessLocation;
    }

    public List<String> getAccessLocationName() {
        return accessLocationName;
    }

    public void setAccessLocationName(List<String> accessLocationName) {
        this.accessLocationName = accessLocationName;
    }

    public boolean isTrialNeeded() {
        return trialNeeded;
    }

    public void setTrialNeeded(boolean trialNeeded) {
        this.trialNeeded = trialNeeded;
    }

    public String getTrialStatus() {
        return trialStatus;
    }

    public void setTrialStatus(String trialStatus) {
        this.trialStatus = trialStatus;
    }

    public boolean isLicenseNeeded() {
        return licenseNeeded;
    }

    public void setLicenseNeeded(boolean licenseNeeded) {
        this.licenseNeeded = licenseNeeded;
    }

    public String getLicenseReqStatus() {
        return licenseReqStatus;
    }

    public void setLicenseReqStatus(String licenseReqStatus) {
        this.licenseReqStatus = licenseReqStatus;
    }

    public String getOrderPayStatus() {
        return orderPayStatus;
    }

    public void setOrderPayStatus(String orderPayStatus) {
        this.orderPayStatus = orderPayStatus;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public OLEAccessType getOleAccessType() {
        return oleAccessType;
    }

    public void setOleAccessType(OLEAccessType oleAccessType) {
        this.oleAccessType = oleAccessType;
    }

    public OLEPackageScope getOlePackageScope() {
        return olePackageScope;
    }

    public void setOlePackageScope(OLEPackageScope olePackageScope) {
        this.olePackageScope = olePackageScope;
    }

    public OLEPackageType getOlePackageType() {
        return olePackageType;
    }

    public void setOlePackageType(OLEPackageType olePackageType) {
        this.olePackageType = olePackageType;
    }

    public OLEPaymentType getOlePaymentType() {
        return olePaymentType;
    }

    public void setOlePaymentType(OLEPaymentType olePaymentType) {
        this.olePaymentType = olePaymentType;
    }

    public OLERequestPriority getOleRequestPriority() {
        return oleRequestPriority;
    }

    public void setOleRequestPriority(OLERequestPriority oleRequestPriority) {
        this.oleRequestPriority = oleRequestPriority;
    }

    public OLEAuthenticationType getOleAuthenticationType() {
        return oleAuthenticationType;
    }

    public void setOleAuthenticationType(OLEAuthenticationType oleAuthenticationType) {
        this.oleAuthenticationType = oleAuthenticationType;
    }

    public OLEEResourceStatus getOleeResourceStatus() {
        return oleeResourceStatus;
    }

    public void setOleeResourceStatus(OLEEResourceStatus oleeResourceStatus) {
        this.oleeResourceStatus = oleeResourceStatus;
    }

    public List<OLEFormatTypeList> getOleFormatTypes() {
        return oleFormatTypes;
    }

    public void setOleFormatTypes(List<OLEFormatTypeList> oleFormatTypes) {
        this.oleFormatTypes = oleFormatTypes;
    }

    public List<OLEMaterialTypeList> getOleMaterialTypes() {
        return oleMaterialTypes;
    }

    public void setOleMaterialTypes(List<OLEMaterialTypeList> oleMaterialTypes) {
        this.oleMaterialTypes = oleMaterialTypes;
    }

    public List<OLEContentTypes> getOleContentTypes() {
        return oleContentTypes;
    }

    public void setOleContentTypes(List<OLEContentTypes> oleContentTypes) {
        this.oleContentTypes = oleContentTypes;
    }

    public List<OLEEResourceNotes> getEresNotes() {
        return eresNotes;
    }

    public void setEresNotes(List<OLEEResourceNotes> eresNotes) {
        this.eresNotes = eresNotes;
    }

    public List<OLEEResourceRequestor> getRequestors() {
        return requestors;
    }

    public void setRequestors(List<OLEEResourceRequestor> requestors) {
        this.requestors = requestors;
    }

    public List<OLEEResourceSelector> getSelectors() {
        return selectors;
    }

    public void setSelectors(List<OLEEResourceSelector> selectors) {
        this.selectors = selectors;
    }

    public List<OLEEResourceReqSelComments> getReqSelComments() {
        return reqSelComments;
    }

    public void setReqSelComments(List<OLEEResourceReqSelComments> reqSelComments) {
        this.reqSelComments = reqSelComments;
    }

    public List<OLEEResourceEventLog> getOleERSEventLogs() {
        return oleERSEventLogs;
    }

    public void setOleERSEventLogs(List<OLEEResourceEventLog> oleERSEventLogs) {
        this.oleERSEventLogs = oleERSEventLogs;
    }

    public List<OLEEResourceLicense> getOleERSLicenseRequests() {
        return oleERSLicenseRequests;
    }

    public void setOleERSLicenseRequests(List<OLEEResourceLicense> oleERSLicenseRequests) {
        this.oleERSLicenseRequests = oleERSLicenseRequests;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    public String getSelectInstance() {
        return selectInstance;
    }

    public void setSelectInstance(String selectInstance) {
        this.selectInstance = selectInstance;
    }

    public String getDefaultCoverage() {
        return defaultCoverage;
    }

    public void setDefaultCoverage(String defaultCoverage) {
        this.defaultCoverage = defaultCoverage;
    }

    public String getDefaultPerpetualAccess() {
        return defaultPerpetualAccess;
    }

    public void setDefaultPerpetualAccess(String defaultPerpetualAccess) {
        this.defaultPerpetualAccess = defaultPerpetualAccess;
    }

    public String getDefaultCoverageView() {
        return defaultCoverageView;
    }

    public void setDefaultCoverageView(String defaultCoverageView) {
        this.defaultCoverageView = defaultCoverageView;
    }

    public String getDefaultPerpetualAccessView() {
        return defaultPerpetualAccessView;
    }

    public void setDefaultPerpetualAccessView(String defaultPerpetualAccessView) {
        this.defaultPerpetualAccessView = defaultPerpetualAccessView;
    }

    public List<OLEEResourceInstance> getOleERSInstances() {
        return oleERSInstances;
    }

    public void setOleERSInstances(List<OLEEResourceInstance> oleERSInstances) {
        this.oleERSInstances = oleERSInstances;
    }

    public List<OleCopy> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<OleCopy> copyList) {
        this.copyList = copyList;
    }

    public List<OLEEResourcePO> getOleERSPOItems() {
        return oleERSPOItems;
    }

    public void setOleERSPOItems(List<OLEEResourcePO> oleERSPOItems) {
        this.oleERSPOItems = oleERSPOItems;
    }

    public VendorDetail getVendorDetail() {
        return vendorDetail;
    }

    public void setVendorDetail(VendorDetail vendorDetail) {
        this.vendorDetail = vendorDetail;
    }

    public PurchaseOrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(PurchaseOrderType orderType) {
        this.orderType = orderType;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public OleStatisticalSearchingCodes getOleStatisticalCode() {
        return oleStatisticalCode;
    }

    public void setOleStatisticalCode(OleStatisticalSearchingCodes oleStatisticalCode) {
        this.oleStatisticalCode = oleStatisticalCode;
    }

    public OLEEResourceInstance getOleERSInstance() {
        return oleERSInstance;
    }

    public void setOleERSInstance(OLEEResourceInstance oleERSInstance) {
        this.oleERSInstance = oleERSInstance;
    }

    public List<OLEEResourceInvoices> getOleERSInvoices() {
        return oleERSInvoices;
    }

    public void setOleERSInvoices(List<OLEEResourceInvoices> oleERSInvoices) {
        this.oleERSInvoices = oleERSInvoices;
    }

    public boolean iseInstanceFlag() {
        return eInstanceFlag;
    }

    public void seteInstanceFlag(boolean eInstanceFlag) {
        this.eInstanceFlag = eInstanceFlag;
    }

    public String getDummyDefaultCoverage() {
        return dummyDefaultCoverage;
    }

    public void setDummyDefaultCoverage(String dummyDefaultCoverage) {
        this.dummyDefaultCoverage = dummyDefaultCoverage;
    }

    public String getDummyDefaultPerpetualAccess() {
        return dummyDefaultPerpetualAccess;
    }

    public void setDummyDefaultPerpetualAccess(String dummyDefaultPerpetualAccess) {
        this.dummyDefaultPerpetualAccess = dummyDefaultPerpetualAccess;
    }

    public boolean isCovEdited() {
        return isCovEdited;
    }

    public void setCovEdited(boolean covEdited) {
        isCovEdited = covEdited;
    }

    public boolean isPerAccEdited() {
        return isPerAccEdited;
    }

    public void setPerAccEdited(boolean perAccEdited) {
        isPerAccEdited = perAccEdited;
    }

    public String getCovStartDate() {
        return covStartDate;
    }

    public void setCovStartDate(String covStartDate) {
        this.covStartDate = covStartDate;
    }

    public String getCovEndDate() {
        return covEndDate;
    }

    public void setCovEndDate(String covEndDate) {
        this.covEndDate = covEndDate;
    }

    public String getPerAccStartDate() {
        return perAccStartDate;
    }

    public void setPerAccStartDate(String perAccStartDate) {
        this.perAccStartDate = perAccStartDate;
    }

    public String getPerAccEndDate() {
        return perAccEndDate;
    }

    public void setPerAccEndDate(String perAccEndDate) {
        this.perAccEndDate = perAccEndDate;
    }

    public List<OLEEResourceInvoices> geteRSInvoices() {
        return eRSInvoices;
    }

    public void seteRSInvoices(List<OLEEResourceInvoices> eRSInvoices) {
        this.eRSInvoices = eRSInvoices;
    }

    public List<OLEEResourcePO> geteRSPOItems() {
        return eRSPOItems;
    }

    public void seteRSPOItems(List<OLEEResourcePO> eRSPOItems) {
        this.eRSPOItems = eRSPOItems;
    }

    public List<OLEEResourceAccountingLine> getAccountingLines() {
        return accountingLines;
    }

    public void setAccountingLines(List<OLEEResourceAccountingLine> accountingLines) {
        this.accountingLines = accountingLines;
    }

    public List<OLEEResourceContacts> getOleERSContacts() {
        return oleERSContacts;
    }

    public void setOleERSContacts(List<OLEEResourceContacts> oleERSContacts) {
        this.oleERSContacts = oleERSContacts;
    }

    public String getGokbconfig() {
        return gokbconfig;
    }

    public void setGokbconfig(String gokbconfig) {
        this.gokbconfig = gokbconfig;
    }

    public void setResultDetails(DocumentSearchResult searchResult, List<OLESearchCondition> oleSearchEresources) {
        List<DocumentAttribute> documentAttributes = searchResult.getDocumentAttributes();
        for (DocumentAttribute docAttribute : documentAttributes) {
            String name = docAttribute.getName();
            if (OLEConstants.OLEEResourceRecord.ERESOURCE_RESULT_FIELDS.contains(name)) {
                if (name.equals(OLEConstants.OLEEResourceRecord.ERESOURCE_TITLE)) {
                    name = OLEConstants.OLEEResourceRecord.ERESOURCE_TITLE;
                }
                Method getMethod;
                try {
                    getMethod = getSetMethod(OLEEResourceRecordDocument.class, name, new Class[]{String.class});
                    getMethod.invoke(this, docAttribute.getValue().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private Method getSetMethod(Class targetClass, String attr, Class[] objectAttributes) throws Exception {
        Method method = targetClass.getMethod("set" + StringUtils.capitalize(attr), objectAttributes);
        return method;
    }

    @Override
    public void prepareForSave(KualiDocumentEvent event) {
        // TODO Auto-generated method stub
        // first populate, then call super
        super.prepareForSave(event);
        try {
            LOG.debug("###########Inside OLEEResourceRecordDocument " + "prepareForSave###########");
            if (this.getStatisticalSearchingCode() != null && (!"".equals(this.getStatisticalSearchingCode().trim()))) {
                this.setStatisticalSearchingCodeId(Integer.parseInt(this.getStatisticalSearchingCode()));
                Map statisticalCodeMap = new HashMap<>();
                statisticalCodeMap.put(OLEConstants.OLEEResourceRecord.STATISTICAL_SEARCH_CD_ID, this.getStatisticalSearchingCodeId());
                OleStatisticalSearchingCodes oleStatisticalSearchingCodes = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleStatisticalSearchingCodes.class, statisticalCodeMap);
                if(oleStatisticalSearchingCodes != null) {
                    this.setOleStatisticalCode(oleStatisticalSearchingCodes);
                }
            }
            String vendorId = this.getVendorId();
            if (vendorId != null && !vendorId.isEmpty()) {
                String[] vendorDetails = vendorId.split("-");
                Integer vendorHeaderGeneratedIdentifier = vendorDetails.length > 0 ? Integer.parseInt(vendorDetails[0]) : 0;
                Integer vendorDetailAssignedIdentifier = vendorDetails.length > 1 ? Integer.parseInt(vendorDetails[1]) : 0;
                Map vendorMap = new HashMap<>();
                vendorMap.put(OLEConstants.OLEEResourceRecord.VENDOR_HEADER_GEN_ID, vendorHeaderGeneratedIdentifier);
                vendorMap.put(OLEConstants.OLEEResourceRecord.VENDOR_DETAILED_ASSIGNED_ID, vendorDetailAssignedIdentifier);
                VendorDetail vendorDetailDoc = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorMap);
                if (vendorDetailDoc != null) {
                    this.setVendorName(vendorDetailDoc.getVendorName());
                }
            }
            /*String accessId = "";
            if (this.getAccessLocation() != null  && this.getAccessLocation().size() > 0) {
                List<String> accessLocationId = this.getAccessLocation();
                if (accessLocationId.size() > 0) {
                    for (String accessLocation : accessLocationId) {
                        accessId += accessLocation;
                        accessId += OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR;
                    }
                    this.setAccessLocationId(accessId.substring(0, (accessId.lastIndexOf(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR))));
                }
            }*/
            String MobAccessId = "";
            if (this.getMobileAccess() != null  && this.getMobileAccess().size() > 0) {
                List<String> mobileAccessId = this.getMobileAccess();
                if (mobileAccessId.size() > 0) {
                    for (String mobileAccess : mobileAccessId) {
                        MobAccessId += mobileAccess;
                        MobAccessId += OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR;
                    }
                    this.setMobileAccessId(MobAccessId.substring(0, (MobAccessId.lastIndexOf(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR))));
                }
            }
            this.setLicenseReqStatus("");
            List<OLEEResourceLicense> oleERSLicenses = this.getOleERSLicenseRequests();
            List<DateTime> modifiedDateList = new ArrayList<>();
            DateTime lastModifiedDate = null;
            if (oleERSLicenses.size() > 0) {
                for (int i = oleERSLicenses.size()-1; i >= 0; i--) {
                    DateTime appStatus = oleERSLicenses.get(i).getDocumentRouteHeaderValue().getApplicationDocumentStatusDate();
                    if (!OLEConstants.OLEEResourceRecord.ERESOURCE_STATUSES.contains(appStatus) &&
                            (!oleERSLicenses.get(i).getDocumentRouteHeaderValue().getAppDocStatus().equalsIgnoreCase(
                                    OLEConstants.OLEEResourceRecord.LICENSE_FINAL_STATUS))) {
                        modifiedDateList.add(appStatus);
                    }
                    DocumentRouteHeaderValue documentRouteHeaderValue = oleERSLicenses.get(i).getDocumentRouteHeaderValue();
                    if(documentRouteHeaderValue != null) {
                        String licenceTitle = documentRouteHeaderValue.getDocTitle();
                        if(licenceTitle != null && !licenceTitle.isEmpty()) {
                            licenceTitle = licenceTitle.substring(26);
                        }
                        oleERSLicenses.get(i).setDocumentDescription(licenceTitle);
                    }
                }
                for (int modifiedDate = 0; modifiedDate<modifiedDateList.size(); modifiedDate++) {
                    DateTime dateTime = modifiedDateList.get(modifiedDate);
                    if (lastModifiedDate == null) {
                        lastModifiedDate = dateTime;
                    } else {
                        if (dateTime.isAfter(lastModifiedDate)) {
                            lastModifiedDate = dateTime;
                        }
                    }
                }
                for (int i = oleERSLicenses.size()-1; i >= 0; i--) {
                    if (lastModifiedDate != null && lastModifiedDate.equals(oleERSLicenses.get(i).getDocumentRouteHeaderValue().getApplicationDocumentStatusDate())) {
                        this.setLicenseReqStatus(oleERSLicenses.get(i).getDocumentRouteHeaderValue().getApplicationDocumentStatus());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during prepareForSave()", e);
            throw new RuntimeException(e);
        }
//      OLE-3615
        List<OLEEResourceInstance> oleERSInstances = this.getOleERSInstances();
        if (oleERSInstances.size() > 0) {
            for (OLEEResourceInstance oleERSInstance : oleERSInstances) {
                if (StringUtils.isBlank(oleERSInstance.getSubscriptionStatus()) && StringUtils.isNotBlank(this.getSubscriptionStatus())) {
                    oleERSInstance.setSubscriptionStatus(this.getSubscriptionStatus());
                }
            }
        }
        Map statusMap = new HashMap<>();
        statusMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, this.getOleERSIdentifier());
        OLEEResourceRecordDocument oleERSDoc = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, statusMap);
        if (oleERSDoc != null) {
            status = oleERSDoc.getStatusName();
        }
        OLEEResourceEventLog oleEResourceEventLog = new OLEEResourceEventLog();
        if (status == null){
            oleEResourceEventLog.setCurrentTimeStamp();
            oleEResourceEventLog.setEventUser(GlobalVariables.getUserSession().getPrincipalName());
            oleEResourceEventLog.setEventType(OLEConstants.OLEEResourceRecord.SYSTEM);
            oleEResourceEventLog.setEventNote(OLEConstants.OLEEResourceRecord.STATUS_IS+getStatusName());
            oleEResourceEventLog.setSaveFlag(true);
            this.getOleERSEventLogs().add(oleEResourceEventLog);
        }
        else if (!status.equals(getStatusName())){
            oleEResourceEventLog.setCurrentTimeStamp();
            oleEResourceEventLog.setEventUser(GlobalVariables.getUserSession().getPrincipalName());
            oleEResourceEventLog.setEventType(OLEConstants.OLEEResourceRecord.SYSTEM);
            oleEResourceEventLog.setEventNote(OLEConstants.OLEEResourceRecord.STATUS_FROM+ status +OLEConstants.OLEEResourceRecord.STATUS_TO+getStatusName());
            oleEResourceEventLog.setSaveFlag(true);
            this.getOleERSEventLogs().add(oleEResourceEventLog);
        }
        status=getStatusName();
        String defaultCov = this.getDummyDefaultCoverage();
        if(defaultCov != null && !defaultCov.isEmpty() && !this.isCovEdited()) {
            this.setCovEdited(true);
            getOleEResourceSearchService().getDefaultCovDatesToPopup(this,defaultCov);
        }
        String defaultPerAcc = this.getDummyDefaultPerpetualAccess();
        if(defaultPerAcc != null && !defaultPerAcc.isEmpty() && !this.isPerAccEdited()) {
            this.setPerAccEdited(true);
            getOleEResourceSearchService().getDefaultPerAccDatesToPopup(this,defaultPerAcc);
        }
        getOleEResourceSearchService().saveDefaultCoverageDate(this);
        getOleEResourceSearchService().saveDefaultPerpetualAccessDate(this);
        try {
            getOleEResourceSearchService().saveEResourceInstanceToDocstore(this);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void processAfterRetrieve() {
        super.processAfterRetrieve();
        if (this.getStatisticalSearchingCodeId() != null) {
            this.setStatisticalSearchingCode(this.getStatisticalSearchingCodeId().toString());
        }
        String mobileAccessId = this.getMobileAccessId();
        if (mobileAccessId != null && !mobileAccessId.isEmpty()) {
            String[] mobileAccess = mobileAccessId.split(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR);
            List<String> mobileAccesses = new ArrayList<>();
            for (String mobileAccId : mobileAccess) {
                mobileAccesses.add(mobileAccId);
            }
            this.setMobileAccess(mobileAccesses);
        }
        if (this.getOleAccessType() != null) {
            this.setAccessType(this.getOleAccessType().getOleAccessTypeName());
        } else if (this.getAccessTypeId() != null) {
            Map accessTpeMap = new HashMap<>();
            accessTpeMap.put("oleAccessTypeId", this.getAccessTypeId());
            oleAccessType = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEAccessType.class, accessTpeMap);
            if (oleAccessType != null) {
                this.setAccessType(oleAccessType.getOleAccessTypeName());
            }
        }
        if (this.getOleAuthenticationType() != null) {
            this.setAuthenticationType(this.getOleAuthenticationType().getOleAuthenticationTypeName());
        } else if (this.getAuthenticationTypeId() != null) {
            Map authenticationMap = new HashMap();
            authenticationMap.put("oleAuthenticationTypeId", this.getAuthenticationTypeId());
            oleAuthenticationType = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEAuthenticationType.class, authenticationMap);
            if (oleAuthenticationType != null) {
                this.setAuthenticationType(oleAuthenticationType.getOleAuthenticationTypeName());
            }
        }
        this.setLicenseReqStatus("");
        List<OLEEResourceLicense> oleERSLicenses = this.getOleERSLicenseRequests();
        List<DateTime> modifiedDateList = new ArrayList<>();
        DateTime lastModifiedDate = null;
        if (oleERSLicenses.size() > 0) {
            for (int i = oleERSLicenses.size()-1; i >= 0; i--) {
                DateTime appStatus = oleERSLicenses.get(i).getDocumentRouteHeaderValue().getApplicationDocumentStatusDate();
                if (!OLEConstants.OLEEResourceRecord.ERESOURCE_STATUSES.contains(appStatus) &&
                        (!oleERSLicenses.get(i).getDocumentRouteHeaderValue().getAppDocStatus().equalsIgnoreCase(
                                OLEConstants.OLEEResourceRecord.LICENSE_FINAL_STATUS))) {
                    modifiedDateList.add(appStatus);
                }
                DocumentRouteHeaderValue documentRouteHeaderValue = oleERSLicenses.get(i).getDocumentRouteHeaderValue();
                if(documentRouteHeaderValue != null) {
                    String licenceTitle = documentRouteHeaderValue.getDocTitle();
                    if(licenceTitle != null && !licenceTitle.isEmpty()) {
                        licenceTitle = licenceTitle.substring(26);
                    }
                    oleERSLicenses.get(i).setDocumentDescription(licenceTitle);
                }
            }
            for (int modifiedDate = 0; modifiedDate<modifiedDateList.size(); modifiedDate++) {
                DateTime dateTime = modifiedDateList.get(modifiedDate);
                if (lastModifiedDate == null) {
                    lastModifiedDate = dateTime;
                } else {
                    if (dateTime.isAfter(lastModifiedDate)) {
                        lastModifiedDate = dateTime;
                    }
                }
            }
            for (int i = oleERSLicenses.size()-1; i >= 0; i--) {
                if (lastModifiedDate!=null && lastModifiedDate.equals(oleERSLicenses.get(i).getDocumentRouteHeaderValue().getApplicationDocumentStatusDate())) {
                    this.setLicenseReqStatus(oleERSLicenses.get(i).getDocumentRouteHeaderValue().getApplicationDocumentStatus());
                }
            }
        }
        List<OLEEResourceInstance> oleERSInstances = this.getOleERSInstances();
        OLEEResourceInstance oleeResourceInstance = null;
        List<Holdings> holdingsList = new ArrayList<Holdings>();
        List<String> instanceId = new ArrayList<String>();
        if (oleERSInstances.size() > 0) {
            for (OLEEResourceInstance oleERSInstance : oleERSInstances) {
                    instanceId.add(oleERSInstance.getInstanceId());
            }
        }
        if (instanceId.size() > 0) {
            for(String id:instanceId){
                try {
                    holdingsList.add(getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(id));
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        for (Holdings holdings:holdingsList){
            if(holdings instanceof EHoldings){
                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor=new HoldingOlemlRecordProcessor();
                OleHoldings oleHoldings=holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                for (OLEEResourceInstance oleERSInstance : oleERSInstances) {
                    if (holdings.getId().equals(oleERSInstance.getInstanceId())) {
                        oleeResourceInstance = oleERSInstance;
                        StringBuffer urls = new StringBuffer();
                        for(Link link :oleHoldings.getLink()){
                            urls.append(link.getUrl());
                            urls.append(",");
                        }
                        if (urls != null && urls.length() > 0) {
                            String url = urls.substring(0, urls.lastIndexOf(","));
                            oleeResourceInstance.setUrl(url);
                        }
                    }
                }
            }

        }
        getOleEResourceSearchService().getDefaultCovergeDate(this);
        getOleEResourceSearchService().getDefaultPerpetualAccessDate(this);
        getOleEResourceSearchService().getBannerMessage(this);
        getOleeResourceHelperService().updateVendorInfo(this);
        getOleEResourceSearchService().updatePlatformProvider(this);
        getOleeResourceHelperService().setAccessInfo(this);

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OLEPhoneNumber> getPhoneNos() {
        return phoneNos;
    }

    public void setPhoneNos(List<OLEPhoneNumber> phoneNos) {
        this.phoneNos = phoneNos;
    }

    public List<OLEEResourceChangeDashBoard> getOleEResourceChangeDashBoards() {
        return oleEResourceChangeDashBoards;
    }

    public void setOleEResourceChangeDashBoards(List<OLEEResourceChangeDashBoard> oleEResourceChangeDashBoards) {
        this.oleEResourceChangeDashBoards = oleEResourceChangeDashBoards;
    }

    public String getGokbId() {
        return gokbId;
    }

    public void setGokbId(String gokbId) {
        this.gokbId = gokbId;
    }

    public List<OLELinkedEresource> getOleLinkedEresources() {
        return oleLinkedEresources;
    }

    public void setOleLinkedEresources(List<OLELinkedEresource> oleLinkedEresources) {
        this.oleLinkedEresources = oleLinkedEresources;
    }

    public boolean isSelectEResFlag() {
        return selectEResFlag;
    }

    public void setSelectEResFlag(boolean selectEResFlag) {
        this.selectEResFlag = selectEResFlag;
    }

    public String getRemoveOrRelinkToParent() {
        return removeOrRelinkToParent;
    }

    public void setRemoveOrRelinkToParent(String removeOrRelinkToParent) {
        this.removeOrRelinkToParent = removeOrRelinkToParent;
    }

    public double getFiscalYearCost() {
        return fiscalYearCost;
    }

    public void setFiscalYearCost(double fiscalYearCost) {
        this.fiscalYearCost = fiscalYearCost;
    }

    public double getYearPriceQuote() {
        return yearPriceQuote;
    }

    public void setYearPriceQuote(double yearPriceQuote) {
        this.yearPriceQuote = yearPriceQuote;
    }

    public double getCostIncrease() {
        return costIncrease;
    }

    public void setCostIncrease(double costIncrease) {
        this.costIncrease = costIncrease;
    }

    public double getPercentageIncrease() {
        return percentageIncrease;
    }

    public void setPercentageIncrease(double percentageIncrease) {
        this.percentageIncrease = percentageIncrease;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public List<OLEStandardIdentifier> getStandardIdentifiers() {
        return standardIdentifiers;
    }

    public void setStandardIdentifiers(List<OLEStandardIdentifier> standardIdentifiers) {
        this.standardIdentifiers = standardIdentifiers;
    }

    public List<OLEGOKbPackage> getGoKbPackageList() {
        return goKbPackageList;
    }

    public void setGoKbPackageList(List<OLEGOKbPackage> goKbPackageList) {
        this.goKbPackageList = goKbPackageList;
    }

    public List<OLEGOKbPlatform> getGoKbPlatformList() {
        return goKbPlatformList;
    }

    public void setGoKbPlatformList(List<OLEGOKbPlatform> goKbPlatformList) {
        this.goKbPlatformList = goKbPlatformList;
    }

    public List<OLEGOKbTIPP> getGoKbTIPPList() {
        return goKbTIPPList;
    }

    public void setGoKbTIPPList(List<OLEGOKbTIPP> goKbTIPPList) {
        this.goKbTIPPList = goKbTIPPList;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public int getActiveTitlesCount() {
        return activeTitlesCount;
    }

    public void setActiveTitlesCount(int activeTitlesCount) {
        this.activeTitlesCount = activeTitlesCount;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<OLEGOKbPlatform> getGoKbPlatforms() {
        return goKbPlatforms;
    }

    public void setGoKbPlatforms(List<OLEGOKbPlatform> goKbPlatforms) {
        this.goKbPlatforms = goKbPlatforms;
    }

    public List<OLEGOKbMappingValue> getOleGOKbMappingValueList() {
        return oleGOKbMappingValueList;
    }

    public void setOleGOKbMappingValueList(List<OLEGOKbMappingValue> oleGOKbMappingValueList) {
        this.oleGOKbMappingValueList = oleGOKbMappingValueList;
    }

    public Timestamp getDateAccessConfirmed() {
        return dateAccessConfirmed;
    }

    public void setDateAccessConfirmed(Timestamp dateAccessConfirmed) {
        this.dateAccessConfirmed = dateAccessConfirmed;
    }

    public String getAccessUserName() {
        return accessUserName;
    }

    public void setAccessUserName(String accessUserName) {
        this.accessUserName = accessUserName;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
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

    public String getMobileAccessId() {
        return mobileAccessId;
    }

    public void setMobileAccessId(String mobileAccessId) {
        this.mobileAccessId = mobileAccessId;
    }

    public List<String> getMobileAccess() {
        return mobileAccess;
    }

    public void setMobileAccess(List<String> mobileAccess) {
        this.mobileAccess = mobileAccess;
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

    public String getMarcRecordUpdateFreqId() {
        return marcRecordUpdateFreqId;
    }

    public void setMarcRecordUpdateFreqId(String marcRecordUpdateFreqId) {
        this.marcRecordUpdateFreqId = marcRecordUpdateFreqId;
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

    public String getMarcRecordPasword() {
        return marcRecordPasword;
    }

    public void setMarcRecordPasword(String marcRecordPasword) {
        this.marcRecordPasword = marcRecordPasword;
    }

    public String getMarcRecordNote() {
        return marcRecordNote;
    }

    public void setMarcRecordNote(String marcRecordNote) {
        this.marcRecordNote = marcRecordNote;
    }

    public List<OLEEResourceAccessWorkflow> getOleERSAccessWorkflows() {
        return oleERSAccessWorkflows;
    }

    public void setOleERSAccessWorkflows(List<OLEEResourceAccessWorkflow> oleERSAccessWorkflows) {
        this.oleERSAccessWorkflows = oleERSAccessWorkflows;
    }

    public String getLineActions() {
        return lineActions;
    }

    public void setLineActions(String lineActions) {
        this.lineActions = lineActions;
    }

    public List<OLEEResourceInstance> geteRSInstances() {
        return eRSInstances;
    }

    public void seteRSInstances(List<OLEEResourceInstance> eRSInstances) {
        this.eRSInstances = eRSInstances;
    }

    public List<OLEEResourcePO> getLinkedERSPOItems() {
        return linkedERSPOItems;
    }

    public void setLinkedERSPOItems(List<OLEEResourcePO> linkedERSPOItems) {
        this.linkedERSPOItems = linkedERSPOItems;
    }

    public String getBannerMessage() {
        return bannerMessage;
    }

    public void setBannerMessage(String bannerMessage) {
        this.bannerMessage = bannerMessage;
    }

    public Timestamp getGokbLastUpdatedDate() {
        return gokbLastUpdatedDate;
    }

    public void setGokbLastUpdatedDate(Timestamp gokbLastUpdatedDate) {
        this.gokbLastUpdatedDate = gokbLastUpdatedDate;
    }

    public String getAccessDescription() {
        return accessDescription;
    }

    public void setAccessDescription(String accessDescription) {
        this.accessDescription = accessDescription;
    }

    public String getWorkflowConfigurationId() {
        return workflowConfigurationId;
    }

    public void setWorkflowConfigurationId(String workflowConfigurationId) {
        this.workflowConfigurationId = workflowConfigurationId;
    }

    public String getOleAccessActivationDocumentNumber() {
        return oleAccessActivationDocumentNumber;
    }

    public void setOleAccessActivationDocumentNumber(String oleAccessActivationDocumentNumber) {
        this.oleAccessActivationDocumentNumber = oleAccessActivationDocumentNumber;
    }

    public List<OLEEResourceInstance> getDeletedInstances() {
        return deletedInstances;
    }

    public void setDeletedInstances(List<OLEEResourceInstance> deletedInstances) {
        this.deletedInstances = deletedInstances;
    }

    public List<OLEEResourceInstance> getPurchaseOrderInstances() {
        return purchaseOrderInstances;
    }

    public void setPurchaseOrderInstances(List<OLEEResourceInstance> purchaseOrderInstances) {
        this.purchaseOrderInstances = purchaseOrderInstances;
    }

    public String getGokbProfile() {
        return gokbProfile;
    }

    public void setGokbProfile(String gokbProfile) {
        this.gokbProfile = gokbProfile;
    }

    public String getGokbPackageStatus() {
        return gokbPackageStatus;
    }

    public void setGokbPackageStatus(String gokbPackageStatus) {
        this.gokbPackageStatus = gokbPackageStatus;
    }

    public boolean isAccessReadOnly() {
        return accessReadOnly;
    }

    public void setAccessReadOnly(boolean accessReadOnly) {
        this.accessReadOnly = accessReadOnly;
    }

    public String getOldTitle() {
        return oldTitle;
    }

    public void setOldTitle(String oldTitle) {
        this.oldTitle = oldTitle;
    }

    public String getPublisherLink() {
        if (StringUtils.isNotBlank(this.getPublisherId())){
            String[] vendorDetails = this.getPublisherId().split("-");
            Integer vendorHeaderGeneratedIdentifier = vendorDetails.length > 0 ? Integer.parseInt(vendorDetails[0]) : 0;
            Integer vendorDetailAssignedIdentifier = vendorDetails.length > 1 ? Integer.parseInt(vendorDetails[1]) : 0;
            String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
            String url = oleurl + "/kr/inquiry.do?methodToCall=start&amp;businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&amp;vendorHeaderGeneratedIdentifier=" + vendorHeaderGeneratedIdentifier + "&amp;vendorDetailAssignedIdentifier="
                    + vendorDetailAssignedIdentifier;
            return url;
        }
        return publisherLink;
    }

    public void setPublisherLink(String publisherLink) {
        this.publisherLink = publisherLink;
    }
}
