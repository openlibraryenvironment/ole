package org.kuali.ole.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.alert.bo.ActionListAlertBo;
import org.kuali.ole.alert.service.impl.AlertServiceImpl;
import org.kuali.ole.batch.bo.OLEBatchProcessBibDataMappingNew;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.batch.ingest.OLEBatchGOKBImport;
import org.kuali.ole.batch.util.BatchBibImportUtil;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.instance.Coverage;
import org.kuali.ole.docstore.common.document.content.instance.Link;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.document.*;
import org.kuali.ole.select.form.OLEEResourceRecordForm;
import org.kuali.ole.select.gokb.*;
import org.kuali.ole.select.gokb.service.GokbLocalService;
import org.kuali.ole.select.gokb.service.GokbRdbmsService;
import org.kuali.ole.select.gokb.service.impl.GokbLocalServiceImpl;
import org.kuali.ole.select.gokb.service.impl.GokbRdbmsServiceImpl;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;
import org.kuali.ole.service.impl.OLEEResourceSearchServiceImpl;
import org.kuali.ole.service.impl.OleLicenseRequestServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.template.EresourceAlertContentFormatter;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.ole.vnd.document.service.impl.VendorServiceImpl;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.rules.rule.event.SaveDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Collection;
import java.util.Date;

import static org.kuali.ole.OLEConstants.*;

/**
 * Created by sambasivam on 24/10/14.
 */
public class OLEEResourceHelperService {

    private VendorServiceImpl vendorService = null;
    private OLEEResourceSearchServiceImpl oleeResourceSearchService = new OLEEResourceSearchServiceImpl();
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private DocstoreClientLocator docstoreClientLocator;
    private static final Logger LOG = Logger.getLogger(OLEEResourceHelperService.class);
    private BusinessObjectService businessObjectService;
    private DocumentService documentService;
    private DocumentDao documentDao;
    private AlertServiceImpl alertService = new AlertServiceImpl();

    private GokbRdbmsService gokbRdbmsService;
    private GokbLocalService gokbLocalService;
    private OlePatronHelperServiceImpl olePatronHelperService;
    private EresourceAlertContentFormatter eresourceAlertContentFormatter;
    private LoanProcessor loanProcessor;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.CREATED_DATE_FORMAT);
    public LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = (LoanProcessor)SpringContext.getService("loanProcessor");
        }
        return loanProcessor;
    }

    public EresourceAlertContentFormatter getEresourceAlertContentFormatter() {
        if(eresourceAlertContentFormatter ==null){
            eresourceAlertContentFormatter = new EresourceAlertContentFormatter();
        }
        return eresourceAlertContentFormatter;
    }

    public void setEresourceAlertContentFormatter(EresourceAlertContentFormatter eresourceAlertContentFormatter) {
        this.eresourceAlertContentFormatter = eresourceAlertContentFormatter;
    }

    public OlePatronHelperService getOlePatronHelperService() {
        if (olePatronHelperService == null)
            olePatronHelperService = new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public GokbRdbmsService getGokbRdbmsService() {
        if (null == gokbRdbmsService) {
            return new GokbRdbmsServiceImpl();
        }
        return gokbRdbmsService;
    }

    public GokbLocalService getGokbLocalService() {
        if (null == gokbLocalService) {
            return new GokbLocalServiceImpl();
        }
        return gokbLocalService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    public VendorServiceImpl getVendorService() {
        if (vendorService == null) {
            vendorService = new VendorServiceImpl();
        }

        return vendorService;
    }

    public DocumentService getDocumentService() {
        if (this.documentService == null) {
            this.documentService = KRADServiceLocatorWeb.getDocumentService();
        }
        return this.documentService;
    }

    public DocumentDao getDocumentDao(){
        if(documentDao == null){
            documentDao = (DocumentDao) SpringContext.getBean("documentDao");
        }
        return documentDao;
    }

    /**
     * This method is uses to update the vendor information in the contacts tab.
     *
     * @param oleeResourceRecordDocument
     */
    public void updateVendorInfo(OLEEResourceRecordDocument oleeResourceRecordDocument) {

        List<OLEEResourceContacts> oleEResourceContactsList = new ArrayList<>();
        Set<VendorDetail> vendorDetails = new HashSet<>();
        Set<String> vendorNumbers = new HashSet<>();

        //current vendor of the eResource
        vendorNumbers.add(oleeResourceRecordDocument.getVendorId());

        //put the vendors in vendor map for child eresources
        List<OLELinkedEresource> oleLinkedEresources = oleeResourceRecordDocument.getOleLinkedEresources();
        for (OLELinkedEresource oleLinkedEresource : oleLinkedEresources) {
            if (oleLinkedEresource.getRelationShipType() != null && oleLinkedEresource.getRelationShipType().equals("child")) {
                vendorNumbers.add(oleLinkedEresource.getOleeResourceRecordDocument().getVendorId());
            }
        }

        vendorDetails.addAll(getVendorDetails(vendorNumbers));

        //put the vendors in the vendor map for each po item
        List<OLEEResourcePO> oleEResourcePOs = oleeResourceRecordDocument.getOleERSPOItems();
        for (OLEEResourcePO oleeResourcePO : oleEResourcePOs) {
            vendorDetails.add(oleeResourcePO.getVendorDetail());
        }

        //put the vendors in the vendor map for each invoice
        List<OLEEResourceInvoices> oleEResourceInvoiceList = oleeResourceRecordDocument.getOleERSInvoices();
        for (OLEEResourceInvoices oleeResourceInvoices : oleEResourceInvoiceList) {
            vendorDetails.add(oleeResourceInvoices.getVendorDetail());
        }

        for (VendorDetail vendorDetail : vendorDetails) {
            if (vendorDetail.getVendorContacts() != null) {
                for (VendorContact vendorContact : vendorDetail.getVendorContacts()) {
                    if (vendorContact.isActive()) {
                        OLEEResourceContacts oleEResourceContacts = new OLEEResourceContacts();
                        if (vendorContact.getVendorDetail() != null) {
                            oleEResourceContacts.setOrganization(vendorContact.getVendorDetail().getVendorName());
                        }
                        oleEResourceContacts.setContact(vendorContact.getVendorContactName());
                        oleEResourceContacts.setRole(vendorContact.getVendorContactType().getVendorContactTypeDescription());
                        oleEResourceContacts.setEmail(vendorContact.getVendorContactEmailAddress());
                        oleEResourceContacts.setFormat(vendorContact.getFormat());
                        oleEResourceContacts.setNote(vendorContact.getVendorContactCommentText());
                        oleEResourceContacts.setVendorContactGeneratedIdentifier(vendorContact.getVendorContactGeneratedIdentifier());
                        oleEResourceContacts.setVendorHeaderGeneratedIdentifier(vendorContact.getVendorHeaderGeneratedIdentifier());
                        oleEResourceContacts.setVendorDetailAssignedIdentifier(vendorContact.getVendorDetailAssignedIdentifier());
                        if (CollectionUtils.isNotEmpty(vendorContact.getVendorContactPhoneNumbers())) {
                            VendorContactPhoneNumber vendorContactPhoneNumber = vendorContact.getVendorContactPhoneNumbers().get(0);
                            oleEResourceContacts.setPhone(vendorContactPhoneNumber.getVendorPhoneNumber());
                            if (vendorContact.getVendorContactPhoneNumbers().size()>1){
                                oleEResourceContacts.setHasMorePhoneNo(true);
                            }
                        }
                        oleEResourceContacts.setOleERSIdentifier(oleeResourceRecordDocument.getOleERSIdentifier());
                        oleEResourceContacts.setActiveVendor(vendorDetail.isActiveIndicator());
                        oleEResourceContactsList.add(oleEResourceContacts);
                    }
                }
            }
        }
        oleeResourceRecordDocument.setOleERSContacts(oleEResourceContactsList);
    }

    private Set<VendorDetail> getVendorDetails(Set<String> vendorNumbers) {
        Set<VendorDetail> vendorDetails = new HashSet<>();
        for (String vendorId : vendorNumbers) {
            VendorDetail vendorDetail = getVendorService().getByVendorNumber(vendorId);
            if (vendorDetail != null) {
                vendorDetails.add(vendorDetail);
            }
        }
        return vendorDetails;
    }

    private void addInvoiceVendors(Map<String, VendorDetail> vendorDetailMap, List<OLEEResourceInvoices> oleERSInvoices) {
        if (oleERSInvoices != null) {
            for (OLEEResourceInvoices oleeResourceInvoices : oleERSInvoices) {
                Map map = new HashMap();
                map.put(OLEConstants.PURAP_DOC_IDENTIFIER, oleeResourceInvoices.getInvoiceId());
                List<OleInvoiceDocument> oleInvoiceDocuments = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, map);
                if (oleInvoiceDocuments != null && oleInvoiceDocuments.size() > 0) {
                    Map fieldValues = new HashMap();
                    fieldValues.put(OleSelectConstant.VENDOR_HEADER_GENERATED_ID, oleInvoiceDocuments.get(0).getVendorId());
                    List<VendorDetail> vendorDetails = new ArrayList<>(KRADServiceLocator.getBusinessObjectService().findMatching(VendorDetail.class, fieldValues));
                    if (vendorDetails != null) {
                        for (VendorDetail vd : vendorDetails) {
                            vendorDetailMap.put(vd.getVendorNumber(), vd);
                        }
                    }
                }
            }
        }
    }

    private void addPOItemVendors(Map<String, VendorDetail> vendorDetailMap, List<OLEEResourcePO> oleeResourcePOs) {
        if (oleeResourcePOs != null) {
            for (OLEEResourcePO oleeResourcePO : oleeResourcePOs) {
                Map map = new HashMap();
                map.put(OLEConstants.PURAP_DOC_IDENTIFIER, oleeResourcePO.getOlePOItemId());
                List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, map);
                if (olePurchaseOrderDocuments != null && olePurchaseOrderDocuments.size() > 0) {
                    Map fieldValues = new HashMap();
                    fieldValues.put(OleSelectConstant.VENDOR_HEADER_GENERATED_ID, olePurchaseOrderDocuments.get(0).getVendorHeaderGeneratedIdentifier());
                    List<VendorDetail> vendorDetails = new ArrayList<>(KRADServiceLocator.getBusinessObjectService().findMatching(VendorDetail.class, fieldValues));
                    if (vendorDetails != null) {
                        for (VendorDetail vd : vendorDetails) {
                            vendorDetailMap.put(vd.getVendorNumber(), vd);
                        }
                    }
                }
            }
        }
    }

    public void updateEHoldingsInEResource(Holdings holdings) {

        try {
            holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdings.getId());
        } catch (Exception e) {
            LOG.error(e);
        }
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        String eResourceIdentifier = oleHoldings.getEResourceId();
        if (StringUtils.isEmpty(eResourceIdentifier)) {
            return;
        }
        OLEEResourceInstance oleeResourceInstance = new OLEEResourceInstance();
        Map ids = new HashMap();
        ids.put("instanceId", holdings.getId());
        List<OLEEResourceInstance> oleeResourceInstances = (List<OLEEResourceInstance>) getBusinessObjectService().findMatching(OLEEResourceInstance.class, ids);
        for (OLEEResourceInstance existingOleeResourceInstance : oleeResourceInstances) {
            oleeResourceInstance = existingOleeResourceInstance;
        }
        oleeResourceInstance.setInstanceTitle(holdings.getBib().getTitle());
        oleeResourceInstance.setOleERSIdentifier(eResourceIdentifier);
        oleeResourceSearchService.getHoldingsField(oleeResourceInstance, oleHoldings);
        oleeResourceInstance.setInstancePublisher(oleHoldings.getPublisher());
        if (oleHoldings.getPlatform() != null) {
            oleeResourceInstance.setPlatformId(oleHoldings.getPlatform().getPlatformName());
        }

        StringBuffer urls = new StringBuffer();
        for (Link link : oleHoldings.getLink()) {
            urls.append(link.getUrl());
            urls.append(",");
        }
        if (urls.toString().contains(",")) {
            String url = urls.substring(0, urls.lastIndexOf(","));
            oleeResourceInstance.setUrl(url);
        }
        if (oleHoldings.getGokbIdentifier() != null) {
            oleeResourceInstance.setGokbId(oleHoldings.getGokbIdentifier());
        }
        if(StringUtils.isNotEmpty(holdings.getBib().getIsbn())){
            oleeResourceInstance.setIsbn(holdings.getBib().getIsbn());
        }else{
            oleeResourceInstance.setIsbn(holdings.getBib().getIssn());
        }

        oleeResourceInstance.setStatus(oleHoldings.getAccessStatus());
        oleeResourceInstance.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
        oleeResourceInstance.setBibId(holdings.getBib().getId());
        oleeResourceInstance.setInstanceId(holdings.getId());
        oleeResourceInstance.setInstanceFlag("false");

        KRADServiceLocator.getBusinessObjectService().save(oleeResourceInstance);

        OleCopy oleCopy = new OleCopy();
        oleCopy.setBibId(holdings.getBib().getId());
        oleCopy.setOleERSIdentifier(eResourceIdentifier != null ? eResourceIdentifier : "");
        oleCopy.setInstanceId(holdings.getId());

        KRADServiceLocator.getBusinessObjectService().save(oleCopy);

        this.insertOrUpdateGokbDataMapping(oleHoldings, false);

    }


    public void deleteEHoldingsInEResource(Holdings holdings) {
        Map<String, String> criteriaMap = new HashMap<>();
        if (StringUtils.isNotEmpty(holdings.getId())) {
            criteriaMap.put(OLEConstants.INSTANCE_ID, holdings.getId());
            getBusinessObjectService().deleteMatching(OLEEResourceInstance.class, criteriaMap);
        }
    }


    /**
     * This method returns Bib Import Profile based on which we can create the bib.
     */
    public OLEBatchProcessProfileBo getGOKBImportProfile(String batchProcessProfileId) {
        Map<String, String> bibImportProfileMap = new HashMap<>();
        bibImportProfileMap.put("batchProcessProfileId", batchProcessProfileId);
        List<OLEBatchProcessProfileBo> oleBatchProcessProfileBoList = (List<OLEBatchProcessProfileBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEBatchProcessProfileBo.class, bibImportProfileMap);
        if (oleBatchProcessProfileBoList != null && oleBatchProcessProfileBoList.size() > 0) {
            return oleBatchProcessProfileBoList.get(0);
        }
        return null;
    }




    public List<OLEGOKbPackage> searchGokbForPackagess(List<OleGokbTipp> oleGokbTipps, OLEEResourceRecordForm oleEResourceRecordForm) {
        OLEEResourceRecordDocument oleeResourceRecordDocument = (OLEEResourceRecordDocument) oleEResourceRecordForm.getDocument();
        List<OLEGOKbPackage> olegoKbPackages = new ArrayList<>();
        OLEGOKbPackage olegoKbPackage;
       Map<Integer,OLEGOKbPackage> packageMaps = new HashMap<>();
       Map<Integer,Integer> platformMap = new HashMap<Integer,Integer>();
        Map<Integer,Integer> packageTippMap = new HashMap<Integer,Integer>();
        if(oleGokbTipps != null && oleGokbTipps.size() > 0 ){
            for(OleGokbTipp oleGokbTipp : oleGokbTipps){
                if(oleGokbTipp.getOleGokbPackage()!=null){
                    if(!packageMaps.containsKey(oleGokbTipp.getOleGokbPackage().getGokbPackageId())){
                        packageTippMap.put(oleGokbTipp.getOleGokbPackage().getGokbPackageId(),1);
                        olegoKbPackage = buildOLEGOKBPackage(oleGokbTipp.getOleGokbPackage());
                        olegoKbPackages.add(olegoKbPackage);
                        packageMaps.put(oleGokbTipp.getOleGokbPackage().getGokbPackageId(),olegoKbPackage);
                       if(oleGokbTipp.getOleGokbPlatform()!=null){
                        platformMap.put(oleGokbTipp.getOleGokbPackage().getGokbPackageId(), oleGokbTipp.getOleGokbPlatform().getGokbPlatformId());
                       }
                       }else{
                        packageTippMap.put(oleGokbTipp.getOleGokbPackage().getGokbPackageId(),packageTippMap.get(oleGokbTipp.getOleGokbPackage().getGokbPackageId())+1);
                       if(platformMap.containsKey(oleGokbTipp.getOleGokbPackage().getGokbPackageId())){
                           oleeResourceRecordDocument.setSinglePlatform(false);
                           packageMaps.get(oleGokbTipp.getOleGokbPackage().getGokbPackageId()).setMultiplePlatform(true);
                       }
                    }
                }
            }

        }

        for(int i=0;i<olegoKbPackages.size();i++){
            olegoKbPackages.get(i).setTiips(packageTippMap.get(olegoKbPackages.get(i).getPackageId()));
        }
        return olegoKbPackages;
    }


    private OLEGOKbPackage buildOLEGOKBPackage(OleGokbPackage oleGokbPackage){
        OLEGOKbPackage olegoKbPackage = new OLEGOKbPackage();
        if(oleGokbPackage.getDateCreated()!=null)
        olegoKbPackage.setDateCreated(oleGokbPackage.getDateCreated().toString());
        if(oleGokbPackage.getDateUpdated()!=null)
        olegoKbPackage.setDateEntered(oleGokbPackage.getDateUpdated().toString());
        olegoKbPackage.setGokbStatus(oleGokbPackage.getStatus());
       /* olegoKbPackage.setMultiplePlatform();*/
       /* olegoKbPackage.setOleStatus();*/
        olegoKbPackage.setPackageId(oleGokbPackage.getGokbPackageId());
        olegoKbPackage.setPackageName(oleGokbPackage.getPackageName());
/*        olegoKbPackage.setPrimaryPlatform();
        olegoKbPackage.setPrimaryPlatformProvider();*/
        olegoKbPackage.setMultiplePlatform(true);
        olegoKbPackage.setTiips(0);
        return olegoKbPackage;
    }



    public List<OLEGOKbPackage> searchGokbForPackages(List<OleGokbView> oleGokbViews, OLEEResourceRecordForm oleEResourceRecordForm) {
        OLEEResourceRecordDocument oleeResourceRecordDocument = (OLEEResourceRecordDocument) oleEResourceRecordForm.getDocument();
        List<OLEGOKbPackage> olegoKbPackages = new ArrayList<>();
        Map<Integer, OLEGOKbPackage> packageMap = new HashMap();
        Set<Integer> platformIds = new HashSet<>();

        for (OleGokbView oleGokbView : oleGokbViews) {
            Integer packageId = oleGokbView.getPackageId();

            if (packageMap.containsKey(packageId)) {
                OLEGOKbPackage olegoKbPackage = packageMap.get(packageId);
                olegoKbPackage.setTiips((olegoKbPackage.getTiips() + 1));

                platformIds.add(oleGokbView.getPlatformId());

                if (platformIds.size() > 1) {
                    olegoKbPackage.setMultiplePlatform(true);
                }


            } else {
                OLEGOKbPackage olegoKbPackage = new OLEGOKbPackage();
                Map map = new HashMap();
                map.put("gokbPackageId", packageId);
                OleGokbPackage oleGokbPackage = getBusinessObjectService().findByPrimaryKey(OleGokbPackage.class, map);
                olegoKbPackage.setPackageId(packageId);
                if(oleGokbPackage.getDateCreated() != null)
                olegoKbPackage.setDateCreated(oleGokbPackage.getDateCreated().toString());
                if(oleGokbPackage.getDateUpdated() != null)
                olegoKbPackage.setDateEntered(oleGokbPackage.getDateUpdated().toString());
                olegoKbPackage.setPackageName(oleGokbPackage.getPackageName());
                olegoKbPackage.setPrimaryPlatformProvider(oleGokbView.getOrgName());
                olegoKbPackage.setPrimaryPlatform(oleGokbView.getPlatformName());
                olegoKbPackage.setTiips(1);
                olegoKbPackage.setGokbStatus(oleGokbView.getPackageStatus());
                olegoKbPackages.add(olegoKbPackage);
                packageMap.put(packageId, olegoKbPackage);

                platformIds.clear();

                platformIds.add(oleGokbView.getPlatformId());
                olegoKbPackage.setMultiplePlatform(false);
                OLEGOKbPlatform olegoKbPlatform = new OLEGOKbPlatform();
                olegoKbPlatform.setGoKbTIPPList(getExceptedOrCurrentTippsByPlatform(oleGokbView.getPlatformId(), oleGokbView.getTitle(), oleGokbView.getPublisherId()));
                olegoKbPlatform.setPlatformName(oleGokbView.getPlatformName());
                olegoKbPlatform.setPlatformId(oleGokbView.getPlatformId());
                olegoKbPlatform.setPlatformProvider(oleGokbView.getOrgName());
                olegoKbPlatform.setStatus(oleGokbView.getPlatformStatus());
                olegoKbPlatform.setPlatformProviderId(oleGokbView.getOrgId());
                olegoKbPlatform.setSoftwarePlatform(oleGokbView.getSoftwarePlatform());
                List<OLEGOKbPlatform> olegoKbPlatforms = new ArrayList<>();
                olegoKbPlatforms.add(olegoKbPlatform);

                oleeResourceRecordDocument.setGoKbPlatformList(olegoKbPlatforms);

            }
        }

        return olegoKbPackages;
    }


    public List<OLEGOKbTIPP> getExceptedOrCurrentTippsByPlatform(Integer platformId, String title, Integer publisherId) {
        List<OLEGOKbTIPP> olegoKbTIPPs = new ArrayList<>();

        Map map = new HashMap();
        map.put("gokbPlatformId", platformId);

        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) getBusinessObjectService().findMatching(OleGokbTipp.class, map);

        for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
            if (StringUtils.isNotEmpty(oleGokbTipp.getStatus()) && (oleGokbTipp.getStatus().equals("Current") || oleGokbTipp.getStatus().equals("Excepted"))) {
                OLEGOKbTIPP olegoKbTIPP = new OLEGOKbTIPP();
                olegoKbTIPP.setTitle(title);
                boolean isTippExists = verifyTippExistsInOle(oleGokbTipp.getGokbTippId());
                olegoKbTIPP.setTippExists(isTippExists);
                olegoKbTIPP.setPublisherId(publisherId);
                if(oleGokbTipp.getDateCreated()!=null)
                olegoKbTIPP.setDateCreated(oleGokbTipp.getDateCreated().toString());
                if(oleGokbTipp.getDateUpdated() != null)
                olegoKbTIPP.setDateUpdated(oleGokbTipp.getDateUpdated().toString());
                olegoKbTIPP.setGokbStatus(oleGokbTipp.getStatus());
                olegoKbTIPP.setUrl(oleGokbTipp.getPlatformHostUrl());
                if (oleGokbTipp.getEndDate() != null) {
                    olegoKbTIPP.setEndDate(oleGokbTipp.getEndDate().toString());
                }
                if(olegoKbTIPP.getStartDate()!=null)
                olegoKbTIPP.setStartDate(oleGokbTipp.getStartdate().toString());
                olegoKbTIPP.setOleGokbTipp(oleGokbTipp);
                olegoKbTIPPs.add(olegoKbTIPP);
            }
        }

        return olegoKbTIPPs;

    }

    public List<OLEGOKbTIPP> getAllTippsByPlatform(Integer platformId, String title, Integer publisherId) {
        List<OLEGOKbTIPP> olegoKbTIPPs = new ArrayList<>();

        Map map = new HashMap();
        map.put("gokbPlatformId", platformId);

        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) getBusinessObjectService().findMatching(OleGokbTipp.class, map);

        for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
            OLEGOKbTIPP olegoKbTIPP = new OLEGOKbTIPP();
            olegoKbTIPP.setTitle(title);
            boolean isTippExists = verifyTippExistsInOle(oleGokbTipp.getGokbTippId());
            olegoKbTIPP.setTippExists(isTippExists);
            olegoKbTIPP.setPublisherId(publisherId);
            olegoKbTIPP.setDateCreated(oleGokbTipp.getDateCreated().toString());
            olegoKbTIPP.setDateUpdated(oleGokbTipp.getDateUpdated().toString());
            olegoKbTIPP.setGokbStatus(oleGokbTipp.getStatus());
            olegoKbTIPP.setUrl(oleGokbTipp.getPlatformHostUrl());
            olegoKbTIPP.setEndDate(oleGokbTipp.getEndDate().toString());
            olegoKbTIPP.setStartDate(oleGokbTipp.getStartdate().toString());
            olegoKbTIPP.setOleGokbTipp(oleGokbTipp);


            olegoKbTIPPs.add(olegoKbTIPP);
        }

        return olegoKbTIPPs;

    }

    public List<OLEGOKbTIPP> getTippsByPlatform(Integer platformId) {
        List<OLEGOKbTIPP> olegoKbTIPPs = new ArrayList<>();

        Map map = new HashMap();
        map.put("gokbPlatformId", platformId);

        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) getBusinessObjectService().findMatching(OleGokbTipp.class, map);

        for (OleGokbTipp gokbTipp : oleGokbTipps) {
            OLEGOKbTIPP olegoKbTIPP = new OLEGOKbTIPP();

            map.clear();
            map.put("gokbTitleId", gokbTipp.getGokbTitleId());

            OleGokbTitle oleGokbTitle = getBusinessObjectService().findByPrimaryKey(OleGokbTitle.class, map);
            boolean isTippExists = verifyTippExistsInOle(gokbTipp.getGokbTippId());
            olegoKbTIPP.setTippExists(isTippExists);
            if(oleGokbTitle!=null){
            olegoKbTIPP.setTitle(oleGokbTitle.getTitleName());
            olegoKbTIPP.setPublisherId(oleGokbTitle.getPublisherId());
            }
                if(gokbTipp.getDateCreated()!=null)
            olegoKbTIPP.setDateCreated(gokbTipp.getDateCreated().toString());
            if(gokbTipp.getDateUpdated()!=null)
            olegoKbTIPP.setDateUpdated(gokbTipp.getDateUpdated().toString());
            olegoKbTIPP.setGokbStatus(gokbTipp.getStatus());
            olegoKbTIPP.setUrl(gokbTipp.getPlatformHostUrl());
            if(gokbTipp.getEndDate()!=null)
            olegoKbTIPP.setEndDate(gokbTipp.getEndDate().toString());
            if(gokbTipp.getStartdate()!=null)
            olegoKbTIPP.setStartDate(gokbTipp.getStartdate().toString());
            olegoKbTIPP.setOleGokbTipp(gokbTipp);

            olegoKbTIPPs.add(olegoKbTIPP);
        }

        return olegoKbTIPPs;

    }


    public List<OLEGOKbTIPP> buildOLEGOKBTIPP(List<OleGokbTipp> oleGokbTippList) {
        List<OLEGOKbTIPP> olegoKbTIPPs = new ArrayList<>();
        OLEGOKbTIPP olegoKbTIPP;
        for (OleGokbTipp gokbTipp : oleGokbTippList) {
            olegoKbTIPP = new OLEGOKbTIPP();
            boolean isTippExists = verifyTippExistsInOle(gokbTipp.getGokbTippId());

            if (!isTippExists) {
                olegoKbTIPP.setTippExists(isTippExists);

                if (gokbTipp.getOleGokbTitle() != null) {
                    olegoKbTIPP.setTitle(gokbTipp.getOleGokbTitle().getTitleName());
                    olegoKbTIPP.setPublisherId(gokbTipp.getOleGokbTitle().getPublisherId());
                    if (StringUtils.isNotEmpty(gokbTipp.getOleGokbTitle().getIssnOnline())) {
                        olegoKbTIPP.setIssn(gokbTipp.getOleGokbTitle().getIssnOnline());
                    } else {
                        olegoKbTIPP.setIssn(gokbTipp.getOleGokbTitle().getIssnPrint());
                    }
                    if(StringUtils.isNotEmpty(gokbTipp.getOleGokbTitle().getMedium())){
                        olegoKbTIPP.setType(gokbTipp.getOleGokbTitle().getMedium());
                    }
                }

                if (gokbTipp.getDateCreated() != null)
                    olegoKbTIPP.setDateCreated(gokbTipp.getDateCreated().toString());
                if (gokbTipp.getDateUpdated() != null)
                    olegoKbTIPP.setDateUpdated(gokbTipp.getDateUpdated().toString());
                olegoKbTIPP.setGokbStatus(gokbTipp.getStatus());
                olegoKbTIPP.setUrl(gokbTipp.getPlatformHostUrl());
                if (gokbTipp.getEndDate() != null)
                    olegoKbTIPP.setEndDate(gokbTipp.getEndDate().toString());
                if (gokbTipp.getStartdate() != null)
                    olegoKbTIPP.setStartDate(gokbTipp.getStartdate().toString());
                olegoKbTIPP.setOleGokbTipp(gokbTipp);
                olegoKbTIPPs.add(olegoKbTIPP);
            }

        }


        return olegoKbTIPPs;
    }


    private boolean verifyTippExistsInOle(Integer gokbTippId) {
        Map map = new HashMap();
        map.put("gokbId", gokbTippId);
        List<OLEEResourceInstance> oleeResourceInstances = (List<OLEEResourceInstance>) getBusinessObjectService().findMatching(OLEEResourceInstance.class, map);
        if (oleeResourceInstances != null && oleeResourceInstances.size() > 0) {
            return true;
        }
        return false;
    }

    public List<OLEGOKbPlatform> getPlatformByPlackage(String packageId) {
        List<OLEGOKbPlatform> olegoKbPlatforms = new ArrayList<>();

        Map map = new HashMap();
        map.put("gokbPackageId", packageId);

        Map<Integer, OLEGOKbPlatform> platformMap = new HashMap();

        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) getBusinessObjectService().findMatching(OleGokbTipp.class, map);

        for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
            Integer platformId = oleGokbTipp.getGokbPlatformId();
            if (platformMap.containsKey(platformId)) {

                OLEGOKbPlatform olegoKbPlatform = platformMap.get(platformId);
                olegoKbPlatform.setNoOfTiips((olegoKbPlatform.getNoOfTiips() + 1));
            } else {

                map = new HashMap();
                map.put("gokbPlatformId", platformId);
                OleGokbPlatform gokbPlatform = getBusinessObjectService().findByPrimaryKey(OleGokbPlatform.class, map);

                OLEGOKbPlatform olegoKbPlatform = new OLEGOKbPlatform();
                olegoKbPlatform.setPlatformName(gokbPlatform.getPlatformName());
                map.clear();
                map.put("gokbOrganizationId", gokbPlatform.getPlatformProviderId());
                OleGokbOrganization oleGokbOrganization = getBusinessObjectService().findByPrimaryKey(OleGokbOrganization.class, map);
                if(oleGokbOrganization!=null){
                olegoKbPlatform.setPlatformProvider(oleGokbOrganization.getOrganizationName());
                }
                olegoKbPlatform.setPlatformProviderId(gokbPlatform.getPlatformProviderId());
                olegoKbPlatform.setSoftwarePlatform(gokbPlatform.getSoftwarePlatform());
                olegoKbPlatform.setNoOfTiips(1);
                olegoKbPlatform.setPlatformId(platformId);
                olegoKbPlatform.setStatus(gokbPlatform.getStatus());
                olegoKbPlatform.setPackageId(Integer.parseInt(packageId));
                olegoKbPlatforms.add(olegoKbPlatform);
                platformMap.put(platformId, olegoKbPlatform);
            }
        }
        return olegoKbPlatforms;
    }


    public List<BibMarcRecord> buildBibMarcRecords(List<OLEGOKbPlatform> olegoKbPlatforms, String eResourceId, OLEBatchProcessProfileBo oleBatchProcessProfile) {

        List<BibMarcRecord> bibMarcRecords = new ArrayList<>();

        Map<Integer, BibMarcRecord> bibMarcRecordMap = new HashMap<>();

        for (OLEGOKbPlatform olegoKbPlatform : olegoKbPlatforms) {
            String platformName = olegoKbPlatform.getPlatformName();

            String imprint = "";
            String publisher = "";
            List<OLEGOKbTIPP> olegoKbTIPPList = new ArrayList<>();
            List<OLEGOKbTIPP> slecetdOlegoKbTIPPList = new ArrayList<>();
            for (OLEGOKbTIPP olegoKbTIPP : olegoKbPlatform.getGoKbTIPPList()) {

                if (olegoKbTIPP.isSelect() && olegoKbTIPP.getOleGokbTipp() != null) {

                    Integer titleId = olegoKbTIPP.getOleGokbTipp().getGokbTitleId();

                    if (bibMarcRecordMap.containsKey(olegoKbTIPP.getOleGokbTipp().getGokbTitleId())) {
                        BibMarcRecord bibMarcRecord = bibMarcRecordMap.get(titleId);
                        DataField dataField = addEHoldingsFields(olegoKbTIPP, platformName, eResourceId, imprint, publisher,oleBatchProcessProfile.getOleBatchProcessProfileMappingOptionsList().get(0).getOleBatchProcessProfileDataMappingOptionsBoList());
                        bibMarcRecord.getDataFields().add(dataField);
                    } else {
                        OleGokbTitle oleGokbTitle = getOleGokbTitle(titleId);
                        if(oleGokbTitle !=null){
                        BibMarcRecord bibMarcRecord = buildBibMarcRecord(oleGokbTitle,oleBatchProcessProfile.getOleBatchProcessBibDataMappingNewList());
                        bibMarcRecords.add(bibMarcRecord);
                        imprint = oleGokbTitle.getImprint();
                        publisher = String.valueOf(oleGokbTitle.getPublisherId());

                        DataField dataField = addEHoldingsFields(olegoKbTIPP, platformName, eResourceId, imprint, publisher,oleBatchProcessProfile.getOleBatchProcessProfileMappingOptionsList().get(0).getOleBatchProcessProfileDataMappingOptionsBoList());
                        bibMarcRecord.getDataFields().add(dataField);
                        bibMarcRecordMap.put(titleId, bibMarcRecord);
                        }
                    }
                    slecetdOlegoKbTIPPList.add(olegoKbTIPP);
                }else{
                    olegoKbTIPPList.add(olegoKbTIPP);
                }

            }
            olegoKbPlatform.setSelectedGokbTippList(slecetdOlegoKbTIPPList);
            olegoKbPlatform.setGoKbTIPPList(olegoKbTIPPList);
        }
        return bibMarcRecords;
    }

    private DataField addEHoldingsFields(OLEGOKbTIPP olegoKbTIPP, String platformName, String eResourceId, String imprint, String publisher, List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList) {

        OleGokbTipp goKbTIPP = olegoKbTIPP.getOleGokbTipp();
        DataField dataField = getDataFieldForTipp(platformName, eResourceId, goKbTIPP, imprint, publisher, oleBatchProcessProfileDataMappingOptionsBoList);
        return dataField;

    }

    public DataField getDataFieldForTipp(String platformName, String eResourceId, OleGokbTipp goKbTIPP, String imprint, String publisher, List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList) {
        DataField dataField = new DataField();
        String docField = null;
        if (oleBatchProcessProfileDataMappingOptionsBoList.size() > 0) {
            for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBoList){
                docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
                if (EHoldings.DESTINATION_FIELD_LINK_URL.equalsIgnoreCase(docField)) {
                    dataField.setTag(oleBatchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3));
                    break;
                }
            }
        } else {
            dataField.setTag(OLEConstants.OLEBatchProcess.CONSTANT_DATAMAPPING_FOR_EHOLDINGS);
        }
        DataField tempDataField = null;
        List<SubField> subFields = new ArrayList<>();

        for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBoList) {
            tempDataField = BatchBibImportUtil.getDataField(oleBatchProcessProfileDataMappingOptionsBo.getSourceField());
            docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
            for (SubField subField : tempDataField.getSubFields()) {
                if (EHoldings.DESTINATION_FIELD_LINK_URL.equalsIgnoreCase(docField)) {
                    subFields.add(buildSubField(subField.getCode(), goKbTIPP.getPlatformHostUrl()));
                } else if (EHoldings.DESTINATION_FIELD_IMPRINT.equalsIgnoreCase(docField)) {
                    if (StringUtils.isNotBlank(imprint)) {
                        subFields.add(buildSubField(subField.getCode(), imprint));
                    }
                } else if (EHoldings.DESTINATION_FIELD_ERESOURCE_ID.equalsIgnoreCase(docField)) {
                    if (StringUtils.isNotEmpty(eResourceId)) {
                        subFields.add(buildSubField(subField.getCode(), eResourceId));
                    }
                } else if (EHoldings.DESTINATION_FIELD_PLATFORM.equalsIgnoreCase(docField)) {
                    if (StringUtils.isNotEmpty(platformName)) {
                        subFields.add(buildSubField(subField.getCode(), platformName));
                    }
                } else if (EHoldings.DESTINATION_FIELD_GOKB_ID.equalsIgnoreCase(docField)) {
                    if (goKbTIPP.getGokbTippId() != null) {
                        subFields.add(buildSubField(subField.getCode(), goKbTIPP.getGokbTippId().toString()));
                    }
                } else if (EHoldings.DESTINATION_FIELD_COVERAGE_END_DATE.equalsIgnoreCase(docField)) {
                    if (goKbTIPP.getEndDate() != null) {
                        subFields.add(buildSubField(subField.getCode(), goKbTIPP.getEndDate().toString()));
                    }
                } else if (EHoldings.DESTINATION_FIELD_COVERAGE_END_ISSUE.equalsIgnoreCase(docField)) {
                    if (goKbTIPP.getEndIssue() != null) {
                        subFields.add(buildSubField(subField.getCode(), goKbTIPP.getEndIssue().toString()));
                    }
                } else if (EHoldings.DESTINATION_FIELD_COVERAGE_END_VOLUME.equalsIgnoreCase(docField)) {
                    if (goKbTIPP.getEndVolume() != null) {
                        subFields.add(buildSubField(subField.getCode(), goKbTIPP.getEndVolume().toString()));
                    }
                } else if (EHoldings.DESTINATION_FIELD_COVERAGE_START_DATE.equalsIgnoreCase(docField)) {
                    if (goKbTIPP.getStartdate() != null) {
                        subFields.add(buildSubField(subField.getCode(), goKbTIPP.getStartdate().toString()));
                    }
                } else if (EHoldings.DESTINATION_FIELD_COVERAGE_END_ISSUE.equalsIgnoreCase(docField)) {
                    if (goKbTIPP.getStartIssue() != null) {
                        subFields.add(buildSubField(subField.getCode(), goKbTIPP.getStartIssue().toString()));
                    }
                } else if (EHoldings.DESTINATION_FIELD_COVERAGE_START_VOLUME.equalsIgnoreCase(docField)) {
                    if (goKbTIPP.getStartVolume() != null) {
                        subFields.add(buildSubField(subField.getCode(), goKbTIPP.getStartVolume().toString()));
                    }
                } else if (EHoldings.DESTINATION_FIELD_PUBLISHER.equalsIgnoreCase(docField)) {
                    if (publisher != null) {
                        subFields.add(buildSubField(subField.getCode(), publisher));
                    }
                }
            }
        }
        dataField.setSubFields(subFields);
        return dataField;
    }

    private SubField buildSubField(String code, String value) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue(value);
        return subField;
    }

    private BibMarcRecord buildBibMarcRecord(OleGokbTitle oleGokbTitle, List<OLEBatchProcessBibDataMappingNew> oleBatchProcessBibDataMappingNewList) {

        BibMarcRecord bibMarcRecord = buildBibMarcRecordFromTitle(oleGokbTitle, oleBatchProcessBibDataMappingNewList);

        return bibMarcRecord;
    }

    public BibMarcRecord buildBibMarcRecordFromTitle(OleGokbTitle oleGokbTitle, List<OLEBatchProcessBibDataMappingNew> oleBatchProcessBibDataMappingNewList) {
        BibMarcRecord bibMarcRecord = new BibMarcRecord();
        bibMarcRecord.setLeader("#####nam#a22######a#4500");

        for (OLEBatchProcessBibDataMappingNew oleBatchProcessBibDataMappingNew : oleBatchProcessBibDataMappingNewList) {
            String tag = oleBatchProcessBibDataMappingNew.getTag();
            String gokbFiled = oleBatchProcessBibDataMappingNew.getGokbFieldBib();

            if (gokbFiled.equalsIgnoreCase("TI Publisher ID") && oleGokbTitle.getPublisherId() > 0) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, "(PublisherID)" + oleGokbTitle.getPublisherId()));
            } else if (gokbFiled.equalsIgnoreCase("variantName") && StringUtils.isNotEmpty(oleGokbTitle.getVariantName())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, oleGokbTitle.getVariantName()));
            } else if (gokbFiled.equalsIgnoreCase("name") && StringUtils.isNotEmpty(oleGokbTitle.getTitleName())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, oleGokbTitle.getTitleName()));
            } else if (gokbFiled.equalsIgnoreCase("GOKb UID") && oleGokbTitle.getGokbTitleId() > 0) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, "(" + OLEConstants.GOKBID + ")" + oleGokbTitle.getGokbTitleId()));
            } else if (gokbFiled.equalsIgnoreCase("TI ISSN (Online)") && StringUtils.isNotEmpty(oleGokbTitle.getIssnOnline())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, oleGokbTitle.getIssnOnline()));
            } else if (gokbFiled.equalsIgnoreCase("TI ISSN (Print)") && StringUtils.isNotEmpty( oleGokbTitle.getIssnPrint())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, oleGokbTitle.getIssnPrint()));
            } else if (gokbFiled.equalsIgnoreCase("TI ISSN-L")  && StringUtils.isNotEmpty( oleGokbTitle.getIssnL())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, oleGokbTitle.getIssnL()));
            } else if (gokbFiled.equalsIgnoreCase("OCLC Number") && oleGokbTitle.getOclcNumber() >0) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, "(OCLC)" + oleGokbTitle.getOclcNumber()));
            } else if (gokbFiled.equalsIgnoreCase("TI DOI")  && StringUtils.isNotEmpty(oleGokbTitle.getDoi())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, "(DOI)" + oleGokbTitle.getDoi()));
            } else if (gokbFiled.equalsIgnoreCase("TI Proprietary ID") && oleGokbTitle.getProprietaryId() >0) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, "(ProprietaryID)" + oleGokbTitle.getProprietaryId()));
            } else if (gokbFiled.equalsIgnoreCase("TI SUNCAT") && StringUtils.isNotEmpty( oleGokbTitle.getSuncat())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, "(SUNCAT)" + oleGokbTitle.getSuncat()));
            } else if (gokbFiled.equalsIgnoreCase("TI LCCN") && StringUtils.isNotEmpty( oleGokbTitle.getLccn())) {
                bibMarcRecord.getDataFields().add(buildDataField(tag, oleGokbTitle.getLccn()));
            }
        }

        return bibMarcRecord;
    }

    private DataField buildDataField(String tag, String data) {
        DataField dataField = BatchBibImportUtil.getDataField(tag);
        for (SubField subField : dataField.getSubFields()) {
            subField.setValue(data);
        }
        return dataField;
    }


    public List<String> getPlatformProvidersForInstance(List<OLEEResourceInstance> oleERSInstances) {

        List<String> platformProviders = new ArrayList<>();
        Set<String> platformIds = new HashSet<>();

        for (OLEEResourceInstance oleeResourceInstance : oleERSInstances) {
            if(StringUtils.isNotBlank(oleeResourceInstance.getPlatformId())){
                platformIds.add(oleeResourceInstance.getPlatformId());
            }
        }

        for (String platformId : platformIds) {
            OLEPlatformRecordDocument olePlatformRecordDocument = getBusinessObjectService().findBySinglePrimaryKey(OLEPlatformRecordDocument.class, platformId);
            if (olePlatformRecordDocument != null) {
                if (olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier() != null && olePlatformRecordDocument.getVendorDetailAssignedIdentifier() != null) {
                    Map vendorMap = new HashMap();
                    vendorMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier());
                    vendorMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, olePlatformRecordDocument.getVendorDetailAssignedIdentifier());
                    VendorDetail vendor = getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorMap);
                    if (vendor != null && StringUtils.isNotEmpty(vendor.getVendorName()))
                        platformProviders.add(vendor.getVendorName());
                }
            }
        }

        return platformProviders;
    }


    public void importTipps(OLEBatchProcessProfileBo gokbImportProfile, List<BibMarcRecord> bibMarcRecords) {

        OLEBatchGOKBImport batchGOKBImport = new OLEBatchGOKBImport();
        batchGOKBImport.setOleBatchProcessProfileBo(gokbImportProfile);

        try {
            batchGOKBImport.processBatch(bibMarcRecords);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createOrUpdateVendorAndPlatform(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        for (OLEGOKbPlatform oleGoKbPlatform : oleeResourceRecordDocument.getGoKbPlatformList()) {
            //create/update vendor
            if (oleGoKbPlatform.getPlatformProviderId() != null) {
                createOrUpdateVendor(oleGoKbPlatform.getPlatformProviderId(), "");
            }
            Set<Integer> publisherIds = new HashSet<>();
            for (OLEGOKbTIPP oleGoKbTIPP : oleGoKbPlatform.getSelectedGokbTippList()) {
                if (oleGoKbTIPP.getPublisherId() != null && publisherIds.add(oleGoKbTIPP.getPublisherId())) {
                    createOrUpdateVendor(oleGoKbTIPP.getPublisherId(), "");
                }
            }
            //update publisher in E-Resource
            oleeResourceSearchService.updatePublisher(oleGoKbPlatform.getSelectedGokbTippList(), oleeResourceRecordDocument);

            //create/update platform
            if (oleGoKbPlatform.getPlatformId() != null) {
                Map gokbMap = new HashMap();
                gokbMap.put(OLEConstants.GOKB_ID, oleGoKbPlatform.getPlatformId());
                List<OLEPlatformRecordDocument> platformRecordDocumentList = (List<OLEPlatformRecordDocument>) getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, gokbMap);
                OLEPlatformRecordDocument olePlatformRecordDocument = null;
                if (platformRecordDocumentList != null && platformRecordDocumentList.size() > 0) {
                    olePlatformRecordDocument = platformRecordDocumentList.get(0);
                    oleeResourceSearchService.updatePlatform(olePlatformRecordDocument, oleGoKbPlatform.getPlatformName(), oleGoKbPlatform.getStatus(), oleGoKbPlatform.getSoftwarePlatform(), oleGoKbPlatform.getPlatformProviderId());
                } else {
                    olePlatformRecordDocument = oleeResourceSearchService.createPlatform(oleGoKbPlatform.getPlatformName(), oleGoKbPlatform.getPlatformId(), oleGoKbPlatform.getSoftwarePlatform(), oleGoKbPlatform.getStatus(), oleGoKbPlatform.getPlatformProviderId());
                }
                updatePlatformVendorAssociation(olePlatformRecordDocument);
            }
        }
    }

    public void createOrUpdateVendor(Integer vendorId, String logId) {
        OleGokbOrganization oleGokbOrganization = getBusinessObjectService().findBySinglePrimaryKey(OleGokbOrganization.class, vendorId);
        if (oleGokbOrganization != null && oleGokbOrganization.getGokbOrganizationId() != null) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.GOKB_ID, oleGokbOrganization.getGokbOrganizationId());
            List<VendorDetail> vendorDetails = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            if (vendorDetails != null && vendorDetails.size() > 0) {
                VendorDetail vendorDetail = vendorDetails.get(0);
                oleeResourceSearchService.updateVendor(vendorDetail, oleGokbOrganization.getOrganizationName());
                updateLog(logId, "", "vendorsUpdatedCount", 1);
            } else {
                oleeResourceSearchService.createVendor(oleGokbOrganization.getOrganizationName(), oleGokbOrganization.getGokbOrganizationId(), oleGokbOrganization.getVariantName());
                updateLog(logId, "", "vendorsAddedCount", 1);
            }
        }
    }

    public List<OLEPlatformRecordDocument> fetchPlatformDetailsForEResId(String eResId) {
        OLEEResourceRecordDocument oleeResourceRecordDocument = (OLEEResourceRecordDocument) getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, eResId);
        List<OLEPlatformRecordDocument> olePlatformRecordDocuments = new ArrayList<>();
        for (OLEEResourceInstance oleeResourceInstance : oleeResourceRecordDocument.getOleERSInstances()) {
            OLEPlatformRecordDocument olePlatformRecordDocument = getBusinessObjectService().findBySinglePrimaryKey(OLEPlatformRecordDocument.class, oleeResourceInstance.getPlatformId());
            olePlatformRecordDocuments.add(olePlatformRecordDocument);
        }
        return olePlatformRecordDocuments;
    }

    public void applyGokbChangesToPlatforms(String eResId, String logId) {
        List<OLEPlatformRecordDocument> olePlatformRecordDocuments = new ArrayList<>();
        if (eResId != null && StringUtils.isNotEmpty(eResId)) {
            olePlatformRecordDocuments = fetchPlatformDetailsForEResId(eResId);
        } else {
            List<OLEPlatformRecordDocument> olePlatformRecordDocumentList = (List<OLEPlatformRecordDocument>) getBusinessObjectService().findAll(OLEPlatformRecordDocument.class);
            for (OLEPlatformRecordDocument platformRecordDocument : olePlatformRecordDocumentList) {
                if (platformRecordDocument.getGokbId() != null) {
                    olePlatformRecordDocuments.add(platformRecordDocument);
                }
            }
        }
        for (OLEPlatformRecordDocument platformRecordDocument : olePlatformRecordDocuments) {
            OleGokbPlatform olegoKbPlatform = getBusinessObjectService().findBySinglePrimaryKey(OleGokbPlatform.class, platformRecordDocument.getGokbId());
            if (olegoKbPlatform != null) {
                if (platformRecordDocument.getGokbLastUpdatedDate() == null || olegoKbPlatform.getDateUpdated().after(platformRecordDocument.getGokbLastUpdatedDate())) {
                    Map hashmap = new HashMap();
                    hashmap.put("platformStatusId",platformRecordDocument.getStatusId());
                    List<OLEPlatformStatus> olePlatformStatusList = (List<OLEPlatformStatus>) getBusinessObjectService().findMatching(OLEPlatformStatus.class, hashmap);
                    if(olePlatformStatusList.size()>0){
                        if (olePlatformStatusList.get(0).getPlatformStatusName().equalsIgnoreCase(olegoKbPlatform.getStatus())) {
                            String details = "[" + olePlatformStatusList.get(0).getPlatformStatusName() + "]: GOKb status changed to[" + olegoKbPlatform.getStatus() + "]";
                            createToDo(eResId, "Change Platform Status", details, null);
                        }
                    }
                    else{
                        String details = " GOKb status changed to[" + olegoKbPlatform.getStatus() + "]";
                        createToDo(eResId, "Change Platform Status", details, null);
                    }
                    if (!platformRecordDocument.getName().equalsIgnoreCase(olegoKbPlatform.getPlatformName())) {
                        String detail = "[" + olegoKbPlatform.getPlatformName() + "] has changed form [" + platformRecordDocument.getName() + "] to [" + olegoKbPlatform.getPlatformName() + "]";
                        createChangeLog(eResId, "Metadata change: Platform name", detail);
                    }
                    // TODO :Add change log for platform provider name if not same
                    // TODO :Add change log for authentication if not same
                    if (!platformRecordDocument.getSoftware().equalsIgnoreCase(olegoKbPlatform.getSoftwarePlatform())) {
                        String details = "[" + platformRecordDocument.getName() + "]: Platform software has changed from [" + platformRecordDocument.getSoftware() + "] to [" + olegoKbPlatform.getStatus() + "]";
                        createChangeLog(eResId, "Metadata change: Platform software", details);
                    }
                    oleeResourceSearchService.updatePlatform(platformRecordDocument, olegoKbPlatform.getPlatformName(), olegoKbPlatform.getStatus(), olegoKbPlatform.getSoftwarePlatform(), olegoKbPlatform.getPlatformProviderId());
                    updatePlatformVendorAssociation(platformRecordDocument);
                    updateLog(logId, eResId, "platformsUpdatedCount", 1);
                }
            }
        }
    }

    public List<VendorDetail> fetchVendorDetailsForEResId(String eResId) {
        //List<String> vendorGokbList = new ArrayList<>();
        List<VendorDetail> vendorDetails = new ArrayList<>();
        Map eResMap = new HashMap();
        eResMap.put("oleERSIdentifier", eResId);
        OLEEResourceRecordDocument oleeResourceRecordDocument = (OLEEResourceRecordDocument) getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, eResId);
        if (StringUtils.isNotEmpty(oleeResourceRecordDocument.getVendorId())) {
            VendorDetail vendorDetail = getBusinessObjectService().findBySinglePrimaryKey(VendorDetail.class, oleeResourceRecordDocument.getVendorId());
            vendorDetails.add(vendorDetail);
        }
        //vendorGokbList.add(vendorDetail.getGokbId().toString());
        if (StringUtils.isNotEmpty(oleeResourceRecordDocument.getPublisherId())) {
            Map vendorMap = new HashMap();
            vendorMap.put("gokbId", oleeResourceRecordDocument.getPublisherId());
            List<VendorDetail> publishers = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            //vendorGokbList.add(publisher.getGokbId().toString());
            if (publishers.size() > 0) {
                vendorDetails.add(publishers.get(0));
            }
        }
        for (OLEEResourceInstance oleeResourceInstance : oleeResourceRecordDocument.getOleERSInstances()) {
            OLEPlatformRecordDocument olePlatformRecordDocument = getBusinessObjectService().findBySinglePrimaryKey(OLEPlatformRecordDocument.class, oleeResourceInstance.getPlatformId());
            Map vendorDetailMap = new HashMap();
            vendorDetailMap.put("gokbId", olePlatformRecordDocument.getGokbId());
            List<VendorDetail> platformProviderList = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorDetailMap);
            if (platformProviderList.size() > 0) {
                //vendorGokbList.add(olePlatformRecordDocument.getGokbId().toString());
                vendorDetails.add(platformProviderList.get(0));
            }
        }
        return vendorDetails;
    }


    public void applyGokbChangesToVendors(String eResId, String logId) {
        List<VendorDetail> vendorDetails = new ArrayList<>();
        if (eResId != null && StringUtils.isNotEmpty(eResId)) {
            vendorDetails = fetchVendorDetailsForEResId(eResId);
        } else {
            List<VendorDetail> vendorDetailList = (List<VendorDetail>) getBusinessObjectService().findAll(VendorDetail.class);
            for (VendorDetail vendorDetail : vendorDetailList) {
                if (vendorDetail.getGokbId() != null) {
                    vendorDetails.add(vendorDetail);
                }
            }
        }
        for (VendorDetail vendor : vendorDetails) {
            OleGokbOrganization oleGokbOrganization = getBusinessObjectService().findBySinglePrimaryKey(OleGokbOrganization.class, vendor.getGokbId());
            if (oleGokbOrganization != null) {
                if (vendor.getGokbLastUpdated() == null || oleGokbOrganization.getDateUpdated().after(vendor.getGokbLastUpdated())) {
                    // TODO createToDo Organization status not avaliable in gokb Organization
                    if (!vendor.getVendorName().equalsIgnoreCase(oleGokbOrganization.getOrganizationName())) {
                        String details = "[" + oleGokbOrganization.getOrganizationName() + "]" + " Organization name changed from [" + vendor.getVendorName() + "] to [" + oleGokbOrganization.getOrganizationName() + "]";
                        createChangeLog(eResId, "Change: Organization name", details);
                    }
                    List<String> vendorAlias = new ArrayList<>();
                    if (vendor.getVendorAliases() != null) {
                        for (VendorAlias vendoralias : vendor.getVendorAliases()) {
                            vendorAlias.add(vendoralias.getVendorAliasName());
                        }
                        if (!vendorAlias.contains(oleGokbOrganization.getVariantName())) {
                            String details = "[" + vendor.getVendorName() + "] " + "has new variant name ";
                            createChangeLog(eResId, "Metadata change: New organization variant name", details);
                        }
                    }
                    oleeResourceSearchService.updateVendor(vendor, oleGokbOrganization.getOrganizationName());
                    updateLog(logId, eResId, "vendorsUpdatedCount", 1);
                }
            }
        }
    }

    public void overwriteEresourceWithPackage(OLEEResourceRecordDocument oleeResourceRecordDocument, OleGokbPackage oleGokbPackage, String logId) {
        if (oleeResourceRecordDocument.getGokbPackageStatus() != null && !oleeResourceRecordDocument.getGokbPackageStatus().equalsIgnoreCase(oleGokbPackage.getStatus())) {
            String details = "[" + oleeResourceRecordDocument.getGokbPackageStatus() + "]: GOKb status changed to [" + oleGokbPackage.getStatus() + "]";
            createToDo(oleeResourceRecordDocument.getOleERSIdentifier(), "Change: Package Status", details, null);
        }

        GOKbDataElement eResourceNameDataElement = getDataElement(E_RESOURCE_NAME);
        GOKbDataElement variantDataElement = getDataElement(VARIANT_NAME);
        GOKbDataElement publisherDataElement = getDataElement(PUBLISHER);
        GOKbDataElement packageScopeDataElement = getDataElement(PACKAGE_SCOPE);
        GOKbDataElement breakableDataElement = getDataElement(BREAKABLE);
        GOKbDataElement fixedTitleDataElement = getDataElement(FIXED_TITLE_LIST);
        GOKbDataElement packageTypeDataElement = getDataElement(PACKAGE_TYPE);
        GOKbDataElement eResourceDataElement = getDataElement(E_RESOURCE_GOKBID);

        Map dataMapping = new HashMap();
        dataMapping.put("recordType", "E-Resource");
        dataMapping.put("recordId", oleeResourceRecordDocument.getOleERSIdentifier());
        List<OLEGOKbMappingValue> locallyModifiedElements = (List<OLEGOKbMappingValue>) getBusinessObjectService().findMatching(OLEGOKbMappingValue.class, dataMapping);
        if (locallyModifiedElements.size() == 0) {
            oleeResourceRecordDocument.setTitle(oleGokbPackage.getPackageName());
            String variantName = oleGokbPackage.getVariantName();
            if (StringUtils.isNotBlank(variantName)){
                OLEEResourceVariantTitle variantTitle = new OLEEResourceVariantTitle();
                variantTitle.setOleVariantTitle(variantName);
                oleeResourceRecordDocument.getOleEResourceVariantTitleList().add(variantTitle);
            }
            getPublisherIdFromPackageId(oleeResourceRecordDocument, oleGokbPackage.getGokbPackageId().toString());
            String packId = getOlePackageScopeId(oleGokbPackage.getPackageScope());
            if(packId != null) {
                oleeResourceRecordDocument.setPackageScopeId(packId);
            }
            oleeResourceRecordDocument.setBreakable(Boolean.valueOf(oleGokbPackage.getBreakable()));
            oleeResourceRecordDocument.setFixedTitleList(Boolean.valueOf(oleGokbPackage.getFixed()));
            oleeResourceRecordDocument.setGokbIdentifier(oleGokbPackage.getGokbPackageId());
        } else {
            for (OLEGOKbMappingValue localMappingValue : locallyModifiedElements) {

                if (localMappingValue.getDataElementId().equalsIgnoreCase(eResourceNameDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        oleeResourceRecordDocument.setTitle(oleGokbPackage.getPackageName());
                    }
                } else if (localMappingValue.getDataElementId().equalsIgnoreCase(variantDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        String variantName = oleGokbPackage.getVariantName();
                        if (StringUtils.isNotBlank(variantName)) {
                            OLEEResourceVariantTitle variantTitle = new OLEEResourceVariantTitle();
                            variantTitle.setOleVariantTitle(variantName);
                            oleeResourceRecordDocument.getOleEResourceVariantTitleList().add(variantTitle);
                        }
                    }
                } else if (localMappingValue.getDataElementId().equalsIgnoreCase(publisherDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        getPublisherIdFromPackageId(oleeResourceRecordDocument, oleGokbPackage.getGokbPackageId().toString());
                    }
                } else if (localMappingValue.getDataElementId().equalsIgnoreCase(packageScopeDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        String packId = getOlePackageScopeId(oleGokbPackage.getPackageScope());
                        if(packId != null) {
                            oleeResourceRecordDocument.setPackageScopeId(packId);
                        }
                    }
                } else if (localMappingValue.getDataElementId().equalsIgnoreCase(breakableDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        oleeResourceRecordDocument.setBreakable(Boolean.valueOf(oleGokbPackage.getBreakable()));
                    }
                } else if (localMappingValue.getDataElementId().equalsIgnoreCase(fixedTitleDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        oleeResourceRecordDocument.setFixedTitleList(Boolean.valueOf(oleGokbPackage.getFixed()));
                    }
                } else if (localMappingValue.getDataElementId().equalsIgnoreCase(packageTypeDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        // TODO : Add package type id to E-Resource if avalibale in GokbPackage
                        //oleeResourceRecordDocument.setPackageTypeId(oleGokbPackage.getPa);
                    }
                } else if (localMappingValue.getDataElementId().equalsIgnoreCase(eResourceDataElement.getDataElementId())) {
                    if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                        oleeResourceRecordDocument.setGokbIdentifier(oleGokbPackage.getGokbPackageId());
                    }
                }
            }
        }
        oleeResourceRecordDocument.setGokbLastUpdatedDate(oleGokbPackage.getDateUpdated());
        oleeResourceRecordDocument.setGokbPackageStatus(oleGokbPackage.getStatus());
        KRADServiceLocatorWeb.getDocumentService().updateDocument(oleeResourceRecordDocument);
        updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "eResUpdatedCount", 1);
        if (!oleeResourceRecordDocument.getTitle().equalsIgnoreCase(oleGokbPackage.getPackageName())) {
            String details = "[" + oleeResourceRecordDocument.getTitle() + "]: E-Resource name changed from [" + oleeResourceRecordDocument.getTitle() + "] to [" + oleGokbPackage.getPackageName() + "]";
            createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Resource name", details);
        }
        if (!oleeResourceRecordDocument.getPackageScopeId().equalsIgnoreCase(oleGokbPackage.getPackageScope())) {
            String details = "[" + oleeResourceRecordDocument.getTitle() + "]: E-Resource scope changed from [" + oleeResourceRecordDocument.getPackageScopeId() + "] to [" + oleGokbPackage.getPackageScope() + "]";
            createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Resource scope", details);
        }
        if (!oleGokbPackage.getBreakable().equalsIgnoreCase("Unknown")) {
            if (!String.valueOf(oleeResourceRecordDocument.isBreakable()).equalsIgnoreCase(oleGokbPackage.getBreakable())) {
                String details = "[" + oleeResourceRecordDocument.getTitle() + "]: E-Resource breakable changed from [" + oleeResourceRecordDocument.isBreakable() + "] to [" + oleGokbPackage.getBreakable() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Resource breakable", details);
            }
        }
        if (!oleGokbPackage.getBreakable().equalsIgnoreCase("Unknown")) {
            if (!String.valueOf(oleeResourceRecordDocument.isFixedTitleList()).equalsIgnoreCase(oleGokbPackage.getFixed())) {
                String details = "[" + oleeResourceRecordDocument.getTitle() + "]: E-Resource fixed changed from [" + oleeResourceRecordDocument.isFixedTitleList() + "] to [" + oleGokbPackage.getFixed() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Resource fixed", details);
            }
        }
    }

    private String getOlePackageScopeId(String packageScope) {

        Map map = new HashMap();
        map.put("olePackageScopeName", packageScope);

        List<OLEPackageScope> packageScopes = (List<OLEPackageScope>) getBusinessObjectService().findMatching(OLEPackageScope.class, map);
        if(packageScopes != null && packageScopes.size() > 0) {
            return packageScopes.get(0).getOlePackageScopeId();
        }
        return null;
    }

    public void getPublisherIdFromPackageId(OLEEResourceRecordDocument oleeResourceRecordDocument, String packageId) {
        Map map = new HashMap();
        map.put("gokbPackageId", packageId);
        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) getBusinessObjectService().findMatching(OleGokbTipp.class, map);
        OleGokbTitle oleGokbTitle = getBusinessObjectService().findBySinglePrimaryKey(OleGokbTitle.class, oleGokbTipps.get(0).getGokbTitleId());
        if (oleGokbTitle != null && oleGokbTitle.getPublisherId() != null) {
            oleeResourceRecordDocument.setPublisher(oleGokbTitle.getPublisherId().toString());
        }
    }

    public void applyGokbChangesToEresources(String eResId, String logId) {
        List<OLEEResourceRecordDocument> oleeResourceRecordDocuments = new ArrayList<>();
        if (StringUtils.isNotEmpty(eResId)) {
            OLEEResourceRecordDocument oleeResourceRecordDocument = getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, eResId);
            oleeResourceRecordDocuments.add(oleeResourceRecordDocument);
        } else {
            oleeResourceRecordDocuments = (List<OLEEResourceRecordDocument>) getBusinessObjectService().findAll(OLEEResourceRecordDocument.class);
        }
        for (OLEEResourceRecordDocument oleeResourceRecordDocument : oleeResourceRecordDocuments) {
            if (oleeResourceRecordDocument.getGokbIdentifier() != null) {
                Map packageMap = new HashMap();
                packageMap.put("gokbPackageId", oleeResourceRecordDocument.getGokbIdentifier());
                OleGokbPackage oleGokbPackage = (OleGokbPackage) getBusinessObjectService().findBySinglePrimaryKey(OleGokbPackage.class, oleeResourceRecordDocument.getGokbIdentifier());
                if (oleGokbPackage != null) {
                    //if (oleeResourceRecordDocument.getGokbLastUpdatedDate() == null || oleGokbPackage.getDateUpdated().after(oleeResourceRecordDocument.getGokbLastUpdatedDate())) {
                    overwriteEresourceWithPackage(oleeResourceRecordDocument, oleGokbPackage, logId);
                    //}
                }
                //List<String> gokbIds = new ArrayList<>();
                Map eholdingsMap = new HashMap();
                for (OLEEResourceInstance oleeResourceInstance : oleeResourceRecordDocument.getOleERSInstances()) {
                    //gokbIds.add(oleeResourceInstance.getGokbId().toString());
                    eholdingsMap.put(oleeResourceInstance.getGokbId(), oleeResourceInstance);
                }
                Map map = new HashMap();
                map.put("gokbPackageId", oleGokbPackage.getGokbPackageId());
                List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) getBusinessObjectService().findMatching(OleGokbTipp.class, map);
                List<OleGokbTipp> oleGokbTippTobeCreated = new ArrayList<>();
                for (OleGokbTipp tipp : oleGokbTipps) {
                    if (!eholdingsMap.containsKey(tipp.getGokbTippId())) {
                        oleGokbTippTobeCreated.add(tipp);
//                    addTippToEresource(oleeResourceRecordDocument, tipp);
                    } else {
                        if (tipp.getStatus().equalsIgnoreCase("Retired")) {
                            SearchParams searchParams = new SearchParams();
                            searchParams.buildSearchCondition("", searchParams.buildSearchField("Biblographic", "mdf_035a", "(GOKbUID)" + tipp.getGokbTitleId()), "AND");
                            SearchResponse searchResponse = null;
                            try {
                                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String bibId = "";
                            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                                        bibId = searchResultField.getFieldValue();
                                    }
                                }
                            }
                            Bib bib = null;
                            try {
                                bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Holdings eHoldings = null;
                            OLEEResourceInstance oleeResourceInstance = (OLEEResourceInstance) eholdingsMap.get(tipp.getGokbTippId());
                            try {
                                eHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleeResourceInstance.getInstanceId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            retireTippFromEresource(eHoldings, tipp, bib, oleeResourceRecordDocument, logId);
                        } else if (tipp.getStatus().equalsIgnoreCase("Deleted")) {
                            SearchParams searchParams = new SearchParams();
                            searchParams.buildSearchCondition("", searchParams.buildSearchField("Biblographic", "mdf_035a", "(GOKbUID)" + tipp.getGokbTitleId()), "AND");
                            SearchResponse searchResponse = null;
                            try {
                                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String bibId = "";
                            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                                        bibId = searchResultField.getFieldValue();
                                    }
                                }
                            }
                            Bib bib = null;
                            try {
                                bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Holdings eHoldings = null;
                            OLEEResourceInstance oleeResourceInstance = (OLEEResourceInstance) eholdingsMap.get(tipp.getGokbTippId());
                            try {
                                eHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleeResourceInstance.getInstanceId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            deleteTippFromEresource(eHoldings, tipp, bib, oleeResourceRecordDocument, logId);
                        } else {
                            SearchParams searchParams = new SearchParams();
                            searchParams.buildSearchCondition("", searchParams.buildSearchField("Biblographic", "mdf_035a", "(GOKbUID)" + tipp.getGokbTitleId()), "AND");
                            SearchResponse searchResponse = null;
                            try {
                                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        /*String bibId = "";
                        for (SearchResult searchResult : searchResponse.getSearchResults()) {
                            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                if(searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")){
                                    bibId = searchResultField.getFieldValue();
                                }
                            }
                        }
                        Bib bib = null;
                        try {
                            bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                            Holdings eHoldings = null;
                            OLEEResourceInstance oleeResourceInstance = (OLEEResourceInstance) eholdingsMap.get(tipp.getGokbTippId());
                            try {
                                eHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleeResourceInstance.getInstanceId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //if(tipp.getDateUpdated().after(eHoldings.))
                            overwriteEholdingsWithTipp(eHoldings, tipp, oleeResourceRecordDocument, logId);
                        }
                    }
                }
                if (oleGokbTippTobeCreated.size() > 0) {
                    addTippToEresource(oleeResourceRecordDocument, oleGokbTippTobeCreated, logId);
                }
            }
        }
    }

    public void addTippToEresource(OLEEResourceRecordDocument oleeResourceRecordDocument, List<OleGokbTipp> oleGokbTipps, String logId) {

        if (oleeResourceRecordDocument.getGokbconfig().equalsIgnoreCase("sync")) {
            List<BibMarcRecord> bibMarcRecords = new ArrayList<>();
            OLEBatchProcessProfileBo gokbImportProfile = getGOKBImportProfile(oleeResourceRecordDocument.getGokbProfile());
            for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
                OleGokbPlatform oleGokbPlatform = fetchPlatformForTipp(oleGokbTipp);
                createOrUpdateVendor(oleGokbPlatform.getPlatformProviderId(), logId);
                OleGokbTitle oleGokbTitle = getOleGokbTitle(oleGokbTipp.getGokbTitleId());
                if (oleGokbTitle != null) {
                    String details = "[" + oleGokbTitle.getTitleName() + "]: E-Holdings added to OLE";
                    createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "New E-Holdings", details);
                    createOrUpdateVendor(oleGokbTitle.getPublisherId(), logId);
                    oleeResourceSearchService.updatePublisherDetails(oleeResourceRecordDocument, oleGokbTitle.getPublisherId());
                }
                Map gokbMap = new HashMap();
                gokbMap.put(OLEConstants.GOKB_ID, oleGokbPlatform.getGokbPlatformId());
                List<OLEPlatformRecordDocument> platformRecordDocumentList = (List<OLEPlatformRecordDocument>) getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, gokbMap);
                OLEPlatformRecordDocument olePlatformRecordDocument = null;
                if (platformRecordDocumentList != null && platformRecordDocumentList.size() > 0) {
                    olePlatformRecordDocument = platformRecordDocumentList.get(0);
                    olePlatformRecordDocument.setGokbLastUpdatedDate(oleGokbPlatform.getDateUpdated());
                    oleeResourceSearchService.updatePlatform(olePlatformRecordDocument, oleGokbPlatform.getPlatformName(), oleGokbPlatform.getStatus(), oleGokbPlatform.getSoftwarePlatform(), oleGokbPlatform.getPlatformProviderId());
                } else {
                    olePlatformRecordDocument = oleeResourceSearchService.createPlatform(oleGokbPlatform.getPlatformName(), oleGokbPlatform.getGokbPlatformId(), oleGokbPlatform.getSoftwarePlatform(), oleGokbPlatform.getStatus(), oleGokbPlatform.getPlatformProviderId());
                }
                if (oleeResourceRecordDocument != null) {
                    updatePlatformVendorAssociation(olePlatformRecordDocument);
                }
                String platformName = oleGokbPlatform.getPlatformName();
                if (oleGokbTitle != null) {
                    BibMarcRecord bibMarcRecord = buildBibMarcRecordFromTitle(oleGokbTitle,gokbImportProfile.getOleBatchProcessBibDataMappingNewList());
                    String imprint = String.valueOf(oleGokbTitle.getImprint());
                    String publisher = String.valueOf(oleGokbTitle.getPublisherId());
                    DataField dataField = getDataFieldForTipp(platformName, oleeResourceRecordDocument.getOleERSIdentifier(), oleGokbTipp, imprint, publisher,gokbImportProfile.getOleBatchProcessProfileDataMappingOptionsBoList());
                    bibMarcRecord.getDataFields().add(dataField);
                    bibMarcRecords.add(bibMarcRecord);
                }
            }

            importTipps(gokbImportProfile, bibMarcRecords);
            updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "eHoldingsAddedCount", oleGokbTipps.size());
            updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "bibAddedCount", oleGokbTipps.size());
        } else {
            for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
                OleGokbTitle oleGokbTitle = getBusinessObjectService().findBySinglePrimaryKey(OleGokbTitle.class, oleGokbTipp.getGokbTitleId());
                String details = "[" + oleGokbTitle.getTitleName() + "]: TIPP added to GOKb package. E-Holdings not created. Review TIPP and approve to create associated E-Holdings";
                createToDo(oleeResourceRecordDocument.getOleERSIdentifier(), "New TIPP", details, oleGokbTipp.getGokbTippId());
            }
        }

    }

    private OleGokbTitle getOleGokbTitle(Integer titleId) {
        Map map = new HashMap();
        map.put("gokbTitleId", titleId);
        return getBusinessObjectService().findByPrimaryKey(OleGokbTitle.class, map);
    }

    public OleGokbPlatform fetchPlatformForTipp(OleGokbTipp tipp) {
        OleGokbPlatform oleGokbPlatform = getBusinessObjectService().findBySinglePrimaryKey(OleGokbPlatform.class, tipp.getGokbPlatformId());
        return oleGokbPlatform;
    }

    public void deleteTippFromEresource(Holdings eHolding, OleGokbTipp tipp, Bib bib, OLEEResourceRecordDocument oleeResourceRecordDocument, String logId) {
        if (oleeResourceRecordDocument.getGokbconfig().equalsIgnoreCase("sync")) {
            List<OLEEResourceInstance> oleERSInstances = oleeResourceRecordDocument.getOleERSInstances();
            for (OLEEResourceInstance oleeResourceInstance : oleERSInstances) {
                if (oleeResourceInstance.getInstanceId().equalsIgnoreCase(eHolding.getLocalId())) {
                    Map copyMap = new HashMap();
                    copyMap.put("instanceId", oleeResourceInstance.getInstanceId());
                    List<OleCopy> copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, copyMap);
                    if (copies.get(0).getPoItemId() != null) {
                        String details = "[" + eHolding.getBib().getTitle() + "] TIPP deleted from GOKb package. E-Holdings could not be deleted because of an attached purchase order";
                        createToDo(oleeResourceRecordDocument.getOleERSIdentifier(), "Deleted TIPP", details, null);
                    } else {
                        try {
                            getDocstoreClientLocator().getDocstoreClient().deleteHoldings(eHolding.getLocalId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String details = "[" + eHolding.getBib().getTitle() + "]: E-Holding deleted from OLE";
                        createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Deleted E-Holdings", details);
                    }
                    updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "eHoldingsDeletedCount", 1);
                }
            }
        } else {
            String details = "[" + eHolding.getBib().getTitle() + "] TIPP deleted from GOKb package. E-Holdings could not be deleted because of an attached purchase order";
            createToDo(oleeResourceRecordDocument.getOleERSIdentifier(), "Deleted TIPP", details, null);
        }
    }

    public void retireTippFromEresource(Holdings eHolding, OleGokbTipp tipp, Bib bib, OLEEResourceRecordDocument oleeResourceRecordDocument, String logId) {
        if (oleeResourceRecordDocument.getGokbconfig().equalsIgnoreCase("sync")) {
            List<OLEEResourceInstance> oleERSInstances = oleeResourceRecordDocument.getOleERSInstances();
            for (OLEEResourceInstance oleeResourceInstance : oleERSInstances) {
                if (oleeResourceInstance.getInstanceId().equalsIgnoreCase(eHolding.getLocalId())) {
                    Map copyMap = new HashMap();
                    copyMap.put("instanceId", oleeResourceInstance.getInstanceId());
                    List<OleCopy> copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, copyMap);
                    if (copies.get(0).getPoItemId() != null) {
                        String details = "[" + eHolding.getBib().getTitle() + "] TIPP retired from GOKb package. E-Holdings could not be deleted because of an attached purchase order";
                        createToDo(oleeResourceRecordDocument.getOleERSIdentifier(), "Retired TIPP", details, null);
                    } else {
                        try {
                            getDocstoreClientLocator().getDocstoreClient().deleteHoldings(eHolding.getLocalId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String details = "[" + eHolding.getBib().getTitle() + "]: E-Holding deleted from OLE";
                        createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Retired E-Holdings", details);
                    }
                    updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "eHoldingsRetiredCount", 1);
                }
            }

        } else {
            String details = "[<" + eHolding.getBib().getTitle() + ">]: TIPP retired from GOKb package. No changes to E-Holdings. Review E-Holdings to determine if action is needed";
            createToDo(oleeResourceRecordDocument.getOleERSIdentifier(), "Retired TIPP", details, null);
        }
    }

    public void overwriteEholdingsWithTipp(Holdings eHolding, OleGokbTipp tipp, OLEEResourceRecordDocument oleeResourceRecordDocument, String logId) {
        if (eHolding.getStatus() != null && !eHolding.getStatus().equalsIgnoreCase(tipp.getStatus())) {
            String details = "[" + eHolding.getStatus() + "]: GOKB status changed to [" + tipp.getStatus() + "]";
            createToDo(oleeResourceRecordDocument.getOleERSIdentifier(), "Change: TIPP Status", details, null);
        }
        OleHoldings oleHoldings = eHolding.getContentObject();
        if (oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().size()>0) {
            GOKbDataElement coverageStartDateElement = getDataElement(coverageStartDate);
            GOKbDataElement coverageStartIssueElement = getDataElement(coverageStartIssue);
            GOKbDataElement coverageStartVolumeeElement = getDataElement(coverageStartVolume);
            GOKbDataElement coverageEndDateElement = getDataElement(coverageEndDate);
            GOKbDataElement coverageEndIssueElement = getDataElement(coverageEndIssue);
            GOKbDataElement coverageEndVolumeElement = getDataElement(coverageEndVolume);
            Map dataMapping = new HashMap();
            dataMapping.put("recordType", "E-Instance");
            dataMapping.put("recordId", oleHoldings.getHoldingsIdentifier());
            List<OLEGOKbMappingValue> locallyModifiedElements = (List<OLEGOKbMappingValue>) getBusinessObjectService().findMatching(OLEGOKbMappingValue.class, dataMapping);
            Coverage coverage = oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0);
            if (locallyModifiedElements.size() == 0) {
                coverage.setCoverageStartDate(tipp.getStartdate().toString());
                coverage.setCoverageStartIssue(tipp.getStartIssue());
                coverage.setCoverageStartVolume(tipp.getStartVolume());
                coverage.setCoverageEndDate(tipp.getEndDate().toString());
                coverage.setCoverageEndIssue(tipp.getEndIssue());
                coverage.setCoverageEndVolume(tipp.getEndVolume());
            } else {
                for (OLEGOKbMappingValue localMappingValue : locallyModifiedElements) {

                    if (localMappingValue.getDataElementId().equalsIgnoreCase(coverageStartDateElement.getDataElementId())) {
                        if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                            coverage.setCoverageStartDate(tipp.getStartdate().toString());
                        }
                    } else if (localMappingValue.getDataElementId().equalsIgnoreCase(coverageStartIssueElement.getDataElementId())) {
                        if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                            coverage.setCoverageStartIssue(tipp.getStartIssue());
                        }
                    } else if (localMappingValue.getDataElementId().equalsIgnoreCase(coverageStartVolumeeElement.getDataElementId())) {
                        if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                            coverage.setCoverageStartVolume(tipp.getStartVolume());
                        }
                    } else if (localMappingValue.getDataElementId().equalsIgnoreCase(coverageEndDateElement.getDataElementId())) {
                        if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                            coverage.setCoverageEndDate(tipp.getEndDate().toString());
                        }
                    } else if (localMappingValue.getDataElementId().equalsIgnoreCase(coverageEndIssueElement.getDataElementId())) {
                        if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                            coverage.setCoverageEndIssue(tipp.getEndIssue());
                        }
                    } else if (localMappingValue.getDataElementId().equalsIgnoreCase(coverageEndVolumeElement.getDataElementId())) {
                        if (StringUtils.isEmpty(localMappingValue.getLocalValue())) {
                            coverage.setCoverageEndVolume(tipp.getEndVolume());
                        }
                    }
                }
            }
        }
        try {
            getDocstoreClientLocator().getDocstoreClient().updateHoldings(eHolding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "eHoldingsUpdatedCount", 1);
        OleGokbPlatform oleGokbPlatform = fetchPlatformForTipp(tipp);
        addorUpdatePlatform(oleGokbPlatform, oleeResourceRecordDocument, logId);
        OleGokbTitle oleGokbTitle = getOleGokbTitle(tipp.getGokbTitleId());
        oleeResourceSearchService.updatePublisherDetails(oleeResourceRecordDocument, oleGokbTitle.getPublisherId());
        updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "vendorsAddedCount", 1);
        if (oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().size()>0) {
            Coverage coverage = oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0);
            if (!coverage.getCoverageStartDate().equalsIgnoreCase(tipp.getStartdate().toString())) {
                String details = "[" + eHolding.getDisplayLabel() + "]]: E-Holdings start date has changed from [" + coverage.getCoverageStartDate() + "] to [" + tipp.getStartdate() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Holdings start date", details);
            }
            if (!coverage.getCoverageStartIssue().equalsIgnoreCase(tipp.getStartIssue())) {
                String details = "[" + eHolding.getDisplayLabel() + "]]: E-Holdings start issue has changed from [" + coverage.getCoverageStartIssue() + "] to [" + tipp.getStartIssue() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Holdings start issue", details);
            }
            if (!coverage.getCoverageStartVolume().equalsIgnoreCase(tipp.getStartVolume())) {
                String details = "[" + eHolding.getDisplayLabel() + "]]: E-Holdings start volume has changed from [" + coverage.getCoverageStartVolume() + "] to [" + tipp.getStartVolume() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Holdings start volume", details);
            }
            if (!coverage.getCoverageEndDate().equalsIgnoreCase(tipp.getEndDate().toString())) {
                String details = "[" + eHolding.getDisplayLabel() + "]]: E-Holdings end date has changed from [" + coverage.getCoverageEndDate() + "] to [" + tipp.getEndDate() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Holdings end date", details);
            }
            if (!coverage.getCoverageEndVolume().equalsIgnoreCase(tipp.getEndVolume())) {
                String details = "[" + eHolding.getDisplayLabel() + "]]: E-Holdings end volume has changed from [" + coverage.getCoverageEndIssue() + "] to [" + tipp.getEndVolume() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Holdings end volume", details);
            }
            if (!coverage.getCoverageEndIssue().equalsIgnoreCase(tipp.getEndIssue())) {
                String details = "[" + eHolding.getDisplayLabel() + "]]: E-Holdings end issue has changed from [" + coverage.getCoverageEndIssue() + "] to [" + tipp.getEndIssue() + "]";
                createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Holdings end issue", details);
            }
            if (oleHoldings.getLink() != null) {
                if (!oleHoldings.getLink().get(0).getUrl().equalsIgnoreCase(tipp.getPlatformHostUrl())) {
                    String details = "[" + eHolding.getDisplayLabel() + "]]: E-Holdings URL has changed from [" + oleHoldings.getLink().get(0).getUrl() + "] to [" + tipp.getPlatformHostUrl() + "]";
                    createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "Metadata change: E-Holdings URL", details);
                }
            }
        }
    }

    public void addorUpdatePlatform(OleGokbPlatform oleGokbPlatform, OLEEResourceRecordDocument oleeResourceRecordDocument, String logId) {

        Map map = new HashMap();
        map.put("gokbPlatformId", oleGokbPlatform.getGokbPlatformId());
        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) getBusinessObjectService().findMatching(OleGokbTipp.class, map);
        OleGokbTitle oleGokbTitle = getBusinessObjectService().findBySinglePrimaryKey(OleGokbTitle.class, oleGokbTipps.get(0));
        oleeResourceSearchService.updatePublisherDetails(oleeResourceRecordDocument, oleGokbTitle.getPublisherId());
        Map gokbMap = new HashMap();
        gokbMap.put(OLEConstants.GOKB_ID, oleGokbPlatform.getGokbPlatformId());
        List<OLEPlatformRecordDocument> platformRecordDocumentList = (List<OLEPlatformRecordDocument>) getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, gokbMap);
        OLEPlatformRecordDocument olePlatformRecordDocument = null;
        if (platformRecordDocumentList != null && platformRecordDocumentList.size() > 0) {
            olePlatformRecordDocument = platformRecordDocumentList.get(0);
            oleeResourceSearchService.updatePlatform(olePlatformRecordDocument, oleGokbPlatform.getPlatformName(), oleGokbPlatform.getStatus(), oleGokbPlatform.getSoftwarePlatform(), oleGokbPlatform.getPlatformProviderId());
            updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "platformsUpdatedCount", 1);
        } else {
            olePlatformRecordDocument = oleeResourceSearchService.createPlatform(oleGokbPlatform.getPlatformName(), oleGokbPlatform.getGokbPlatformId(), oleGokbPlatform.getSoftwarePlatform(), oleGokbPlatform.getStatus(), oleGokbPlatform.getPlatformProviderId());
            updateLog(logId, oleeResourceRecordDocument.getOleERSIdentifier(), "platformsAddedCount", 1);
        }
        if (olePlatformRecordDocument != null) {
            updatePlatformVendorAssociation(olePlatformRecordDocument);
        }
        createOrUpdateVendor(oleGokbPlatform.getPlatformProviderId(), logId);

    }

    public void createToDo(String eResId, String type, String details, Integer tippId) {
        OleGokbReview oleGokbReview = new OleGokbReview();
        oleGokbReview.setDetails(details);
        oleGokbReview.setType(type);
        oleGokbReview.setOleERSIdentifier(eResId);
        oleGokbReview.setReviewDate(new Timestamp(System.currentTimeMillis()));
        if (tippId != null) {
            oleGokbReview.setGokbTippId(tippId);
        }
        getBusinessObjectService().save(oleGokbReview);
    }

    public void createChangeLog(String eResId, String type, String details) {
        OleGokbChangeLog oleGokbChangeLog = new OleGokbChangeLog();
        oleGokbChangeLog.setType(type);
        oleGokbChangeLog.setDetails(details);
        oleGokbChangeLog.setOleERSIdentifier(eResId);
        oleGokbChangeLog.setChangeLogDate(new Timestamp(System.currentTimeMillis()));
        oleGokbChangeLog.setOrigin("System");
    }

    public void updatePlatformVendorAssociation(OLEPlatformRecordDocument olePlatformRecordDocument) {
        if (olePlatformRecordDocument != null) {
            Map roleMap = new HashMap();
            roleMap.put("associatedEntityId", olePlatformRecordDocument.getOlePlatformId());
            roleMap.put("vendorRoleId", "3");
            List<OLEVendorAssociation> vendorAssociations = (List<OLEVendorAssociation>) getBusinessObjectService().findMatching(OLEVendorAssociation.class, roleMap);
            if (!(vendorAssociations != null && vendorAssociations.size() > 0)) {
                if (olePlatformRecordDocument.getOlePlatformId() != null && olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier() != null && olePlatformRecordDocument.getVendorDetailAssignedIdentifier() != null) {
                    OLEVendorAssociation oleVendorAssociation = new OLEVendorAssociation();
                    oleVendorAssociation.setVendorHeaderGeneratedIdentifier(olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier());
                    oleVendorAssociation.setVendorDetailAssignedIdentifier(olePlatformRecordDocument.getVendorDetailAssignedIdentifier());
                    oleVendorAssociation.setVendorRoleId("3");
                    oleVendorAssociation.setAssociatedEntityId(olePlatformRecordDocument.getOlePlatformId());
                    getBusinessObjectService().save(oleVendorAssociation);
                }
            }
        }
    }


    public void insertOrUpdateGokbDataMapping(OleHoldings oleHoldings, boolean isUpdate) {

        String holdingsId = oleHoldings.getHoldingsIdentifier();

        if (StringUtils.isNotEmpty(oleHoldings.getEResourceId()) && StringUtils.isNotEmpty(holdingsId) && oleHoldings.getGokbIdentifier() != null) {

            GOKbDataElement urlElement = getDataElement(url);
            GOKbDataElement gokbIdElement = getDataElement(gokbId);

            if (StringUtils.isNotEmpty(gokbId)) {
                saveGokbMappingValue(holdingsId, gokbIdElement, String.valueOf(oleHoldings.getGokbIdentifier()), isUpdate, "E-Instance");
            }

            if (oleHoldings.getLink() != null && oleHoldings.getLink().size() > 0) {
                saveGokbMappingValue(holdingsId, urlElement, oleHoldings.getLink().get(0).getUrl(), isUpdate, "E-Instance");
            }


            if (oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().get(0) != null && oleHoldings.getExtentOfOwnership().get(0).getCoverages() != null && oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage() != null && oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0) != null) {
                Coverage coverage = oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0);

                GOKbDataElement coverageStartDateElement = getDataElement(coverageStartDate);
                GOKbDataElement coverageStartIssueElement = getDataElement(coverageStartIssue);
                GOKbDataElement coverageStartVolumeeElement = getDataElement(coverageStartVolume);
                GOKbDataElement coverageEndDateElement = getDataElement(coverageEndDate);
                GOKbDataElement coverageEndIssueElement = getDataElement(coverageEndIssue);
                GOKbDataElement coverageEndVolumeElement = getDataElement(coverageEndVolume);

                String startDate = coverage.getCoverageStartDate();
                String startIssue = coverage.getCoverageStartIssue();
                String startVolume = coverage.getCoverageStartVolume();
                String endDate = coverage.getCoverageEndDate();
                String endIssue = coverage.getCoverageEndIssue();
                String endVolume = coverage.getCoverageEndVolume();

                if (StringUtils.isNotEmpty(startDate)) {
                    saveGokbMappingValue(holdingsId, coverageStartDateElement, startDate, isUpdate, "E-Instance");
                }
                if (StringUtils.isNotEmpty(startDate)) {
                    saveGokbMappingValue(holdingsId, coverageStartIssueElement, startIssue, isUpdate, "E-Instance");
                }
                if (StringUtils.isNotEmpty(startDate)) {
                    saveGokbMappingValue(holdingsId, coverageStartVolumeeElement, startVolume, isUpdate, "E-Instance");
                }
                if (StringUtils.isNotEmpty(startDate)) {
                    saveGokbMappingValue(holdingsId, coverageEndDateElement, endDate, isUpdate, "E-Instance");
                }
                if (StringUtils.isNotEmpty(startDate)) {
                    saveGokbMappingValue(holdingsId, coverageEndIssueElement, endIssue, isUpdate, "E-Instance");
                }
                if (StringUtils.isNotEmpty(startDate)) {
                    saveGokbMappingValue(holdingsId, coverageEndVolumeElement, endVolume, isUpdate, "E-Instance");
                }
            }
        }
    }


    private void saveGokbMappingValue(String recordId, GOKbDataElement goKbDataElement, String value, boolean isUpdate, String recType) {
        OLEGOKbMappingValue olegoKbMappingValue = null;


        Map map = new HashMap();
        map.put("recordId", recordId);
        map.put("dataElementId", goKbDataElement.getDataElementId());
        List<OLEGOKbMappingValue> olegoKbMappingValues = (List<OLEGOKbMappingValue>) getBusinessObjectService().findMatching(OLEGOKbMappingValue.class, map);
        if (olegoKbMappingValues != null && olegoKbMappingValues.size() > 0) {
            olegoKbMappingValue = olegoKbMappingValues.get(0);
        } else {
            olegoKbMappingValue = new OLEGOKbMappingValue();
            olegoKbMappingValue.setGokbValue(value);
        }
        if (isUpdate) {
            olegoKbMappingValue.setLocalValue(value);
        }

        olegoKbMappingValue.setGoKbDataElement(goKbDataElement);
        olegoKbMappingValue.setRecordId(recordId);
        olegoKbMappingValue.setRecordType(recType);
        getBusinessObjectService().save(olegoKbMappingValue);
    }

    public GOKbDataElement getDataElement(String dataElementName) {
        Map dataElementMap = new HashMap();
        dataElementMap.put("dataElementName", dataElementName);
        List<GOKbDataElement> goKbDataElements = (List<GOKbDataElement>) getBusinessObjectService().findMatching(GOKbDataElement.class, dataElementMap);
        if (goKbDataElements != null && goKbDataElements.size() > 0) {
            return goKbDataElements.get(0);
        }
        return null;
    }

    public void insertOrUpdateGokbElementsForEResource(OLEEResourceRecordDocument oleeResourceRecordDocument, boolean isUpdate) {


        String eResourceId = oleeResourceRecordDocument.getOleERSIdentifier();

        GOKbDataElement eResourceNameDataElement = getDataElement(E_RESOURCE_NAME);
        GOKbDataElement variantDataElement = getDataElement(VARIANT_NAME);
        GOKbDataElement publisherDataElement = getDataElement(PUBLISHER);
        GOKbDataElement packageScopeDataElement = getDataElement(PACKAGE_SCOPE);
        GOKbDataElement breakableDataElement = getDataElement(BREAKABLE);
        GOKbDataElement fixedTitleDataElement = getDataElement(FIXED_TITLE_LIST);
        GOKbDataElement packageTypeDataElement = getDataElement(PACKAGE_TYPE);
        GOKbDataElement eResourceDataElement = getDataElement(E_RESOURCE_GOKBID);

        if (oleeResourceRecordDocument.getGokbIdentifier() != null) {

            String title = oleeResourceRecordDocument.getTitle();
            if (StringUtils.isNotEmpty(title)) {
                saveGokbMappingValue(eResourceId, eResourceNameDataElement, title, isUpdate, "E-Resource");
            }

            List<OLEEResourceVariantTitle> oleEResourceVariantTitleList = oleeResourceRecordDocument.getOleEResourceVariantTitleList();

            if (oleEResourceVariantTitleList != null && oleEResourceVariantTitleList.size() > 0) {
                saveGokbMappingValue(eResourceId, variantDataElement, oleEResourceVariantTitleList.get(0).getOleVariantTitle(), isUpdate, "E-Resource");
            }


            String publisher = oleeResourceRecordDocument.getPublisher();
            if (StringUtils.isNotEmpty(publisher)) {
                saveGokbMappingValue(eResourceId, publisherDataElement, publisher, isUpdate, "E-Resource");
            }

            String packageScope = oleeResourceRecordDocument.getPackageScopeId();
            if (StringUtils.isNotEmpty(packageScope)) {
                saveGokbMappingValue(eResourceId, packageScopeDataElement, packageScope, isUpdate, "E-Resource");
            }

            if (oleeResourceRecordDocument.isBreakable()) {
                saveGokbMappingValue(eResourceId, breakableDataElement, "true", isUpdate, "E-Resource");
            } else {
                saveGokbMappingValue(eResourceId, breakableDataElement, "false", isUpdate, "E-Resource");
            }

            if (oleeResourceRecordDocument.isFixedTitleList()) {
                saveGokbMappingValue(eResourceId, fixedTitleDataElement, "true", isUpdate, "E-Resource");
            } else {
                saveGokbMappingValue(eResourceId, fixedTitleDataElement, "false", isUpdate, "E-Resource");
            }

            String packageType = oleeResourceRecordDocument.getPackageTypeId();

            if (StringUtils.isNotEmpty(packageType)) {
                saveGokbMappingValue(eResourceId, packageTypeDataElement, packageType, isUpdate, "E-Resource");
            }

            if (oleeResourceRecordDocument.getGokbIdentifier() != null) {
                saveGokbMappingValue(eResourceId, eResourceDataElement, String.valueOf(oleeResourceRecordDocument.getGokbIdentifier()), isUpdate, "E-Resource");
            }
        }
    }

    public void applyGOKbChangesToOle(String eResId) {
        OLEEResourceSynchronizationGokbLog oleeResourceSynchronizationGokbLog = null;
        if (StringUtils.isNotEmpty(eResId)) {
            oleeResourceSynchronizationGokbLog = new OLEEResourceSynchronizationGokbLog();
            oleeResourceSynchronizationGokbLog.setOleERSIdentifier(eResId);
        } else {
            oleeResourceSynchronizationGokbLog = new OLEEResourceSynchronizationGokbLog();
        }
        oleeResourceSynchronizationGokbLog.setStartTime(new Timestamp(System.currentTimeMillis()));
        getBusinessObjectService().save(oleeResourceSynchronizationGokbLog);
        applyGokbChangesToVendors(eResId, oleeResourceSynchronizationGokbLog.geteResSynchronizationGokbLogId());
        applyGokbChangesToPlatforms(eResId, oleeResourceSynchronizationGokbLog.geteResSynchronizationGokbLogId());
        applyGokbChangesToEresources(eResId, oleeResourceSynchronizationGokbLog.geteResSynchronizationGokbLogId());
        OLEEResourceSynchronizationGokbLog oleeResourceSynchronization = getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceSynchronizationGokbLog.class, oleeResourceSynchronizationGokbLog.geteResSynchronizationGokbLogId());
        oleeResourceSynchronizationGokbLog.setEndTime(new Timestamp(System.currentTimeMillis()));
        oleeResourceSynchronizationGokbLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        getBusinessObjectService().save(oleeResourceSynchronization);
    }

    public void updateLog(String updateId, String eResId, String column, int value) {
        OLEEResourceSynchronizationGokbLog oleeResourceSynchronizationGokbLog = getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceSynchronizationGokbLog.class, updateId);
        if (oleeResourceSynchronizationGokbLog != null) {
            if (StringUtils.isNotEmpty(eResId)) {
                oleeResourceSynchronizationGokbLog.setOleERSIdentifier(eResId);
            }
            if (column.equalsIgnoreCase("eResUpdatedCount")) {
                if (oleeResourceSynchronizationGokbLog.geteHoldingsUpdatedCount() != null) {
                    oleeResourceSynchronizationGokbLog.seteResUpdatedCount(oleeResourceSynchronizationGokbLog.geteHoldingsUpdatedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.seteHoldingsUpdatedCount(value);
                }
            } else if (column.equalsIgnoreCase("eHoldingsAddedCount")) {
                if (oleeResourceSynchronizationGokbLog.geteHoldingsAddedCount() != null) {
                    oleeResourceSynchronizationGokbLog.seteHoldingsAddedCount(oleeResourceSynchronizationGokbLog.geteHoldingsAddedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.seteHoldingsAddedCount(value);
                }
            } else if (column.equalsIgnoreCase("eHoldingsUpdatedCount")) {
                if (oleeResourceSynchronizationGokbLog.getVendorsUpdatedCount() != null) {
                    oleeResourceSynchronizationGokbLog.seteHoldingsUpdatedCount(oleeResourceSynchronizationGokbLog.getVendorsUpdatedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.setVendorsUpdatedCount(value);
                }
            } else if (column.equalsIgnoreCase("eHoldingsRetiredCount")) {
                if (oleeResourceSynchronizationGokbLog.geteHoldingsRetiredCount() != null) {
                    oleeResourceSynchronizationGokbLog.seteHoldingsRetiredCount(oleeResourceSynchronizationGokbLog.geteHoldingsRetiredCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.seteHoldingsRetiredCount(value);
                }
            } else if (column.equalsIgnoreCase("eHoldingsDeletedCount")) {
                if (oleeResourceSynchronizationGokbLog.geteHoldingsDeletedCount() != null) {
                    oleeResourceSynchronizationGokbLog.seteHoldingsDeletedCount(oleeResourceSynchronizationGokbLog.geteHoldingsDeletedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.seteHoldingsDeletedCount(value);
                }
            } else if (column.equalsIgnoreCase("eResUpdatedCount")) {
                if (oleeResourceSynchronizationGokbLog.geteResUpdatedCount() != null) {
                    oleeResourceSynchronizationGokbLog.seteResUpdatedCount(oleeResourceSynchronizationGokbLog.geteResUpdatedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.seteResUpdatedCount(value);
                }
            } else if (column.equalsIgnoreCase("bibAddedCount")) {
                if (oleeResourceSynchronizationGokbLog.getBibAddedCount() != null) {
                    oleeResourceSynchronizationGokbLog.setBibAddedCount(oleeResourceSynchronizationGokbLog.getBibAddedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.setBibAddedCount(value);
                }
            } else if (column.equalsIgnoreCase("vendorsAddedCount")) {
                if (oleeResourceSynchronizationGokbLog.getVendorsAddedCount() != null) {
                    oleeResourceSynchronizationGokbLog.setVendorsAddedCount(oleeResourceSynchronizationGokbLog.getVendorsAddedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.setVendorsAddedCount(value);
                }
            } else if (column.equalsIgnoreCase("vendorsUpdatedCount")) {
                if (oleeResourceSynchronizationGokbLog.getVendorsUpdatedCount() != null) {
                    oleeResourceSynchronizationGokbLog.setVendorsUpdatedCount(oleeResourceSynchronizationGokbLog.getVendorsUpdatedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.setVendorsUpdatedCount(value);
                }
            } else if (column.equalsIgnoreCase("platformsAddedCount")) {
                if (oleeResourceSynchronizationGokbLog.getPlatformsAddedCount() != null) {
                    oleeResourceSynchronizationGokbLog.setPlatformsAddedCount(oleeResourceSynchronizationGokbLog.getPlatformsAddedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.setPlatformsAddedCount(value);
                }
            } else if (column.equalsIgnoreCase("platformsUpdatedCount")) {
                if (oleeResourceSynchronizationGokbLog.getPlatformsUpdatedCount() != null) {
                    oleeResourceSynchronizationGokbLog.setPlatformsUpdatedCount(oleeResourceSynchronizationGokbLog.getPlatformsUpdatedCount() + value);
                } else {
                    oleeResourceSynchronizationGokbLog.setPlatformsUpdatedCount(value);
                }
            }
            getBusinessObjectService().save(oleeResourceSynchronizationGokbLog);
        }
    }

    public void retrieveAndApplyGokbChanges(){
        retrieveGokbChangesFromRemote();
        applyGOKbChangesToOle("");
    }

    public void retrieveGokbChangesFromRemote(){
        Timestamp lastUpdatedTime = getGokbRdbmsService().getUpdatedDate();
        if(lastUpdatedTime == null) {
            getGokbLocalService().initLocalGokb();
        } else {
            String stringFromTimeStamp = OleGokbXmlUtil.getStringFromTimeStamp(lastUpdatedTime);
            getGokbLocalService().updateLocalGokb(stringFromTimeStamp+"Z");
        }
    }


    public String getParameter(String applicationId, String namespace, String componentId, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(applicationId, namespace, componentId,parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);

        return parameter!=null?parameter.getValue():null;
    }

    public void setWorkflowCompletedStatus(OLEEResourceAccessActivation oleeResourceAccess, MaintenanceDocument maintenanceDocument, boolean newDocument) throws Exception {
        OLEAccessActivationConfiguration oleAccessActivationConfiguration = getBusinessObjectService().findBySinglePrimaryKey(OLEAccessActivationConfiguration.class, oleeResourceAccess.getWorkflowId());
        if (oleAccessActivationConfiguration != null) {
            oleeResourceAccess.setAccessStatus(oleAccessActivationConfiguration.getWorkflowCompletionStatus());
        } else {
            oleeResourceAccess.setAccessStatus("Workflow Completed");
        }
        oleeResourceAccess.setWorkflowName(null);
        oleeResourceAccess.setWorkflowId(null);
        oleeResourceAccess.setWorkflowDescription(null);
        List<OLEEResourceAccessWorkflow> accessWorkflowList = oleeResourceAccess.getOleERSAccessWorkflows();
        OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = accessWorkflowList.get(accessWorkflowList.size() - 1);
        oleeResourceAccessWorkflow.setStatus("Workflow Completed");
        oleeResourceAccessWorkflow.setCurrentOwner(maintenanceDocument.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId());
        if (newDocument) {
            getDocumentService().saveDocument(maintenanceDocument);
        } else {
            getDocumentService().validateAndPersistDocument((Document) maintenanceDocument, new SaveDocumentEvent(maintenanceDocument));
        }
        DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(maintenanceDocument.getDocumentNumber());
        processNotification(documentBo,oleAccessActivationConfiguration,oleeResourceAccess.geteResourceTitle());
        deleteMaintenanceLock();
    }

    public void setWorkflowCompletedStatusAfterApproval(OLEEResourceAccessActivation oleeResourceAccess, MaintenanceDocument maintenanceDocument) throws Exception {
        ActionRequestService actionRequestService = KEWServiceLocator.getActionRequestService();
        DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(maintenanceDocument.getDocumentNumber());
        documentBo.setDocRouteStatus("S");
        KEWServiceLocator.getRouteHeaderService().saveRouteHeader(documentBo);
        String previousStatus = oleeResourceAccess.getAccessStatus();
        OLEAccessActivationConfiguration oleAccessActivationConfiguration = getBusinessObjectService().findBySinglePrimaryKey(OLEAccessActivationConfiguration.class, oleeResourceAccess.getWorkflowId());
        if (oleAccessActivationConfiguration != null) {
            oleeResourceAccess.setAccessStatus(oleAccessActivationConfiguration.getWorkflowCompletionStatus());
        } else {
            oleeResourceAccess.setAccessStatus("Workflow Completed");
        }
        oleeResourceAccess.setWorkflowName(null);
        oleeResourceAccess.setWorkflowId(null);
        oleeResourceAccess.setWorkflowDescription(null);
        List<OLEEResourceAccessWorkflow> accessWorkflowList = oleeResourceAccess.getOleERSAccessWorkflows();
        OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = accessWorkflowList.get(accessWorkflowList.size() - 1);
        oleeResourceAccessWorkflow.setStatus("Workflow Completed");
        oleeResourceAccessWorkflow.setCurrentOwner(maintenanceDocument.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId());
        getDocumentService().validateAndPersistDocument((Document) maintenanceDocument, new SaveDocumentEvent(maintenanceDocument));
        List<ActionRequestValue> actionRequestValueList = actionRequestService.findAllPendingRequests(maintenanceDocument.getDocumentNumber());
        KEWServiceLocator.getActionRequestService().deleteByDocumentId(maintenanceDocument.getDocumentNumber());
        KEWServiceLocator.getActionListService().deleteByDocumentId(maintenanceDocument.getDocumentNumber());
        ActionTakenValue actionTakenValue;
        for (ActionRequestValue actionRequestValue : actionRequestValueList) {
            if (actionRequestValue.getPrincipalId().equalsIgnoreCase(GlobalVariables.getUserSession().getPrincipalId())) {
                actionTakenValue = new ActionTakenValue();
                actionTakenValue.setAnnotation("Approved Status : " + previousStatus);
                actionTakenValue.setActionDate(new Timestamp(System.currentTimeMillis()));
                actionTakenValue.setActionTaken("A");
                actionTakenValue.setDocumentId(actionRequestValue.getDocumentId());
                actionTakenValue.setPrincipalId(actionRequestValue.getPrincipalId());
                actionTakenValue.setDocVersion(1);
                KEWServiceLocator.getActionTakenService().saveActionTaken(actionTakenValue);
            }
        }
        processNotification(documentBo,oleAccessActivationConfiguration,oleeResourceAccess.geteResourceTitle());
        deleteMaintenanceLock();
    }

    public void deleteMaintenanceLock() {
        List<MaintenanceLock> maintenanceLocks = (List<MaintenanceLock>) getBusinessObjectService().findAll(MaintenanceLock.class);
        List<MaintenanceLock> deleteMaintenanceLockList = new ArrayList<MaintenanceLock>();
        if (maintenanceLocks != null && maintenanceLocks.size() > 0) {
            for (MaintenanceLock maintenanceLock : maintenanceLocks) {
                if (maintenanceLock.getLockingRepresentation().contains("org.kuali.ole.select.bo.OLEEResourceAccessActivation")) {
                    deleteMaintenanceLockList.add(maintenanceLock);
                }
            }
        }
        if (deleteMaintenanceLockList.size() > 0) {
            getBusinessObjectService().delete(deleteMaintenanceLockList);
        }
    }

    public void setAccessInfo(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        if (StringUtils.isNotBlank(oleeResourceRecordDocument.getOleAccessActivationDocumentNumber())) {
           // DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
            try {
                boolean hasPermission = false;
                PermissionService service = KimApiServiceLocator.getPermissionService();
                if (GlobalVariables.getUserSession() != null && StringUtils.isNotBlank(GlobalVariables.getUserSession().getPrincipalId())) {
                    hasPermission = service.hasPermission(GlobalVariables.getUserSession().getPrincipalId(), OLEConstants.SELECT_NMSPC, "Edit E-Resource Record");
                }
                MaintenanceDocumentBase maintenanceDocumentBase = (MaintenanceDocumentBase) getDocumentService().getByDocumentHeaderId(oleeResourceRecordDocument.getOleAccessActivationDocumentNumber());
                String accessActivationDocumentStatus = maintenanceDocumentBase.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
                if (!(hasPermission && accessActivationDocumentStatus.equalsIgnoreCase("s"))) {
                    oleeResourceRecordDocument.setAccessReadOnly(true);
                }
                OLEEResourceAccessActivation oleeResourceAccessActivation = (OLEEResourceAccessActivation) maintenanceDocumentBase.getNewMaintainableObject().getDataObject();
                if (oleeResourceAccessActivation != null) {
                    oleeResourceRecordDocument.setAccessStatus(oleeResourceAccessActivation.getAccessStatus());
                    oleeResourceRecordDocument.setAuthenticationTypeId(oleeResourceAccessActivation.getAuthenticationTypeId());
                    oleeResourceRecordDocument.setAccessLocation(oleeResourceAccessActivation.getAccessLocation());
                    oleeResourceRecordDocument.setAccessTypeId(oleeResourceAccessActivation.getAccessTypeId());
                    oleeResourceRecordDocument.setNumOfSimultaneousUsers(oleeResourceAccessActivation.getNumOfSimultaneousUsers());
                    oleeResourceRecordDocument.setTechRequirements(oleeResourceAccessActivation.getTechRequirements());
                }
            } catch (WorkflowException e) {
                e.printStackTrace();
            }
        }
    }

    public void createOrUpdateAccessWorkflow(OLEEResourceRecordDocument oleeResourceRecordDocument, boolean titleChange) {
        if (StringUtils.isNotBlank(oleeResourceRecordDocument.getOleAccessActivationDocumentNumber())) {
            try {
                String titleDesc = OLEConstants.ACCESS_ACTIVATION_DESCRIPTION + " for E-Resource Name : "+oleeResourceRecordDocument.getTitle();
                MaintenanceDocumentBase maintenanceDocumentBase = (MaintenanceDocumentBase) getDocumentService().getByDocumentHeaderId(oleeResourceRecordDocument.getOleAccessActivationDocumentNumber());
                String accessActivationDocumentStatus = maintenanceDocumentBase.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
                if (accessActivationDocumentStatus.equalsIgnoreCase("s")) {
                    OLEEResourceAccessActivation oleeResourceAccessActivation = (OLEEResourceAccessActivation) maintenanceDocumentBase.getNewMaintainableObject().getDataObject();
                    if (oleeResourceAccessActivation != null) {
                        maintenanceDocumentBase.getDocumentHeader().setDocumentDescription(titleDesc);
                        oleeResourceAccessActivation.setAuthenticationTypeId(oleeResourceRecordDocument.getAuthenticationTypeId());
                        oleeResourceAccessActivation.setAccessLocation(oleeResourceRecordDocument.getAccessLocation());
                        oleeResourceAccessActivation.setAccessTypeId(oleeResourceRecordDocument.getAccessTypeId());
                        oleeResourceAccessActivation.setNumOfSimultaneousUsers(oleeResourceRecordDocument.getNumOfSimultaneousUsers());
                        oleeResourceAccessActivation.setTechRequirements(oleeResourceRecordDocument.getTechRequirements());
                        getDocumentService().validateAndPersistDocument((Document)maintenanceDocumentBase,new SaveDocumentEvent(maintenanceDocumentBase));
                    }
                } else if(titleChange){
                    OLEEResourceAccessActivation oleeResourceAccessActivation = (OLEEResourceAccessActivation) maintenanceDocumentBase.getNewMaintainableObject().getDataObject();
                    if (oleeResourceAccessActivation != null) {
                        maintenanceDocumentBase.getDocumentHeader().setDocumentDescription(titleDesc);
                        getDocumentService().validateAndPersistDocument((Document)maintenanceDocumentBase,new SaveDocumentEvent(maintenanceDocumentBase));
                        KEWServiceLocator.getActionListService().updateActionItemsForTitleChange(maintenanceDocumentBase.getDocumentNumber(),titleDesc);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (StringUtils.isNotBlank(oleeResourceRecordDocument.getAccessStatus()) || StringUtils.isNotBlank(oleeResourceRecordDocument.getAuthenticationTypeId())
                    || oleeResourceRecordDocument.getAccessLocation().size() > 0 || StringUtils.isNotBlank(oleeResourceRecordDocument.getAccessTypeId())
                    || StringUtils.isNotBlank(oleeResourceRecordDocument.getNumOfSimultaneousUsers()) || StringUtils.isNotBlank(oleeResourceRecordDocument.getTechRequirements())) {
                deleteMaintenanceLock();
                DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
                try {
                    MaintenanceDocument newDocument = (MaintenanceDocument) documentService.getNewDocument("OLE_ERES_ACCESS_MD");
                    newDocument.getDocumentHeader().setDocumentDescription(OLEConstants.ACCESS_ACTIVATION_DESCRIPTION + " for E-Resource Name : "+oleeResourceRecordDocument.getTitle());
                    OLEEResourceAccessActivation oleeResourceAccessActivation = (OLEEResourceAccessActivation) newDocument.getNewMaintainableObject().getDataObject();
                    oleeResourceAccessActivation.setAuthenticationTypeId(oleeResourceRecordDocument.getAuthenticationTypeId());
                    oleeResourceAccessActivation.setAccessLocation(oleeResourceRecordDocument.getAccessLocation());
                    oleeResourceAccessActivation.setAccessTypeId(oleeResourceRecordDocument.getAccessTypeId());
                    oleeResourceAccessActivation.setNumOfSimultaneousUsers(oleeResourceRecordDocument.getNumOfSimultaneousUsers());
                    oleeResourceAccessActivation.setTechRequirements(oleeResourceRecordDocument.getTechRequirements());
                    oleeResourceAccessActivation.setOleERSIdentifier(oleeResourceRecordDocument.getOleERSIdentifier());
                    getDocumentService().saveDocument(newDocument);
                    oleeResourceRecordDocument.setOleAccessActivationDocumentNumber(newDocument.getDocumentNumber());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void processNotification(DocumentRouteHeaderValue documentRouteHeaderValue,OLEAccessActivationConfiguration oleAccessActivationConfiguration,String eResourceName){
        List<Principal> principals = null;
        String mailId = null;
        boolean emailNotification = oleAccessActivationConfiguration.isMailNotification();
        if(oleAccessActivationConfiguration.getRecipientRoleId()==null && oleAccessActivationConfiguration.getRecipientUserId()==null && StringUtils.isEmpty(oleAccessActivationConfiguration.getMailId())){
            updateActionList(documentRouteHeaderValue.getDocumentId(),documentRouteHeaderValue.getDocumentType().getName(),documentRouteHeaderValue.getRoutedByPrincipalId(),oleAccessActivationConfiguration.getMailContent(),documentRouteHeaderValue.getRoutedByPrincipalId());
       if(emailNotification){
           mailId = getHomeEmail(documentRouteHeaderValue.getRoutedByPrincipal().getEntityId());
          sendMail(mailId,oleAccessActivationConfiguration.getMailContent(),eResourceName);
       }
        }else if(oleAccessActivationConfiguration.getRecipientRoleId()!=null){
            principals = getPrincipals(oleAccessActivationConfiguration.getRecipientRoleId());
            for(Principal principal : principals){
                if(emailNotification){
                    mailId= getHomeEmail(principal.getEntityId());
                    sendMail(mailId,oleAccessActivationConfiguration.getMailContent(),eResourceName);
                }
                updateActionList(documentRouteHeaderValue.getDocumentId(),documentRouteHeaderValue.getDocumentType().getName(),principal.getPrincipalId(),oleAccessActivationConfiguration.getMailContent(),GlobalVariables.getUserSession().getPrincipalId());

            }
        }else if(oleAccessActivationConfiguration.getRecipientUserId()!=null){
            Collection<String> principalIds = new ArrayList<String>();
            principalIds.add(oleAccessActivationConfiguration.getRecipientUserId());
            principals = getPrincipals(principalIds);
            for(Principal principal : principals){
                if(emailNotification){
                    mailId= getHomeEmail(principal.getEntityId());
                    sendMail(mailId,oleAccessActivationConfiguration.getMailContent(),eResourceName);
                }
                updateActionList(documentRouteHeaderValue.getDocumentId(),documentRouteHeaderValue.getDocumentType().getName(),principal.getPrincipalId(),oleAccessActivationConfiguration.getMailContent(),GlobalVariables.getUserSession().getPrincipalId());
            }
        }else if(StringUtils.isNotEmpty(oleAccessActivationConfiguration.getMailId())){
            if(emailNotification){
                sendMail(oleAccessActivationConfiguration.getMailId(),oleAccessActivationConfiguration.getMailContent(),eResourceName);
            }

        }

    }





    public void updateActionList(String documentId,String documentTypeName,String receivingUserId,String alertNote,String alertInitiatorId){
        ActionListAlertBo actionListAlertBo = new ActionListAlertBo();
        actionListAlertBo.setDocumentId(documentId);
        actionListAlertBo.setActive(true);
        actionListAlertBo.setAlertDate(new java.sql.Date(System.currentTimeMillis()));
        actionListAlertBo.setRecordType(documentTypeName);
        actionListAlertBo.setTitle(documentTypeName);
        actionListAlertBo.setNote(alertNote);
        actionListAlertBo.setAlertUserId(receivingUserId);
        actionListAlertBo.setAlertInitiatorId(alertInitiatorId);
        getBusinessObjectService().save(actionListAlertBo);
    }


    public void sendMail(String mailId,String mailContent,String eResourceName){
        List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBos.add(oleNoticeBo);
        oleNoticeBo.setNoticeTitle("Workflow Completion Alert");
        oleNoticeBo.setNoticeSpecificContent(mailContent);
        oleNoticeBo.setNoticeName(eResourceName);
        String processedMailContent = getEresourceAlertContentFormatter().generateHTML(oleNoticeBos, null);
        OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
        String fromAddress = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEParameterConstants.NOTICE_FROM_MAIL);
        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(mailId), new EmailSubject("Workflow Completion Alert"), new EmailBody(processedMailContent), true);

    }





    public List<Principal> getPrincipals(String roleId){
        List<Principal> principals = new ArrayList<Principal>();
        org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService) KimApiServiceLocator.getRoleService();
        Collection<String> principalIds = new ArrayList<>();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(roleId)) {
            Role role = roleService.getRole(roleId);
            principalIds = (Collection<String>) roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), new HashMap<String, String>());
        }
       principals = getPrincipals(principalIds);
        return principals;
    }



    public List<Principal> getPrincipals(Collection<String> principalIds){
        List<Principal> principals = new ArrayList<Principal>();
        List<String> principalList = new ArrayList<String>();
        IdentityService identityService = KimApiServiceLocator.getIdentityService();
        if(CollectionUtils.isNotEmpty(principalIds)) {
            principalList.addAll(principalIds);
        }
        principals = identityService.getPrincipals(principalList);
       return principals;
    }


    public String getHomeEmail(String entityId){
        String mailId = null;
        Map<String,String> entityMap =new HashMap<String,String>();
        entityMap.put("id",entityId);
        List<EntityBo> entityBos = (List<EntityBo>)getBusinessObjectService().findMatching(EntityBo.class,entityMap);
        if(entityBos.get(0)!=null && entityBos.get(0).getEntityTypeContactInfos()!=null && entityBos.get(0).getEntityTypeContactInfos().get(0)!=null){
        EntityTypeContactInfoBo entityTypeContactInfoBo = entityBos.get(0).getEntityTypeContactInfos().get(0);
       try{
        mailId= getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo);
       }catch(Exception e){}
        }
    return mailId;
    }


    public List<OLEEResourceEventLog> filterByReportedDate(Date beginDate, Date endDate, List<OLEEResourceEventLog> eventLogs){
        List<OLEEResourceEventLog> oleEResourceEventLogs = new ArrayList<>();
        try {
            String begin = null;
            if (beginDate != null) {
                begin = dateFormat.format(beginDate);
            }
            String end = null;
            if (endDate != null) {
                end = dateFormat.format(endDate);
            }
            boolean isValid = false;
            for (OLEEResourceEventLog eventLog : eventLogs) {
                Date eventReportedDate = eventLog.getEventDate();
                OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                isValid = oleLicenseRequestService.validateDate(eventReportedDate, begin, end);
                if (isValid) {
                    oleEResourceEventLogs.add(eventLog);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return oleEResourceEventLogs;

    }



    public List<OLEEResourceEventLog> filterByResolvedDate(Date beginDate, Date endDate, List<OLEEResourceEventLog> eventLogs) {
        List<OLEEResourceEventLog> oleeResourceEventLogs = new ArrayList<>();
        try {
            String begin = null;
            if (beginDate != null) {
                begin = dateFormat.format(beginDate);
            }
            String end = null;
            if (endDate != null) {
                end = dateFormat.format(endDate);
            }
            boolean isValid = false;
            for (OLEEResourceEventLog eventLog : eventLogs) {
                Date eventResolvedDate = eventLog.getEventResolvedDate();
                OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                isValid = oleLicenseRequestService.validateDate(eventResolvedDate, begin, end);
                if (isValid) {
                    oleeResourceEventLogs.add(eventLog);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return oleeResourceEventLogs;
    }


    public List<OLEEResourceEventLog> filterByStatus(List<OLEEResourceEventLog> eventLogs, String status) {
        List<OLEEResourceEventLog> oleeResourceEventLogs = new ArrayList<>();
        for (OLEEResourceEventLog oleeResourceEventLog : eventLogs) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(oleeResourceEventLog.getEventStatus()) && oleeResourceEventLog.getEventStatus().equals(status)) {
                oleeResourceEventLogs.add(oleeResourceEventLog);
            }
        }
        return oleeResourceEventLogs;
    }





    public List<OLEEResourceEventLog> filterByLogType(List<OLEEResourceEventLog> eventLogs, String logType) {
        List<OLEEResourceEventLog> oleeResourceEventLogs = new ArrayList<>();
        for (OLEEResourceEventLog oleeResourceEventLog : eventLogs) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(oleeResourceEventLog.getLogTypeId()) && oleeResourceEventLog.getLogTypeId().equals(logType)) {
                oleeResourceEventLogs.add(oleeResourceEventLog);
            }
        }
        return oleeResourceEventLogs;
    }


    public List<OLEEResourceEventLog> filterByEventType(List<OLEEResourceEventLog> eventLogs, String eventType) {
        List<OLEEResourceEventLog> oleeResourceEventLogs = new ArrayList<>();
        for (OLEEResourceEventLog oleeResourceEventLog : eventLogs) {
            if (oleeResourceEventLog.getEventTypeId() != null && oleeResourceEventLog.getEventTypeId().equals(eventType)) {
                oleeResourceEventLogs.add(oleeResourceEventLog);
            }
        }
        return oleeResourceEventLogs;
    }


    public List<OLEEResourceEventLog> filterByProblemType(List<OLEEResourceEventLog> eventLogs, String problemType) {
        List<OLEEResourceEventLog> oleeResourceEventLogs = new ArrayList<>();
        for (OLEEResourceEventLog oleeResourceEventLog : eventLogs) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(oleeResourceEventLog.getProblemTypeId()) && oleeResourceEventLog.getProblemTypeId().equals(problemType)) {
                oleeResourceEventLogs.add(oleeResourceEventLog);
            }
        }
        return oleeResourceEventLogs;
    }

    public void getLinkedPOs(List<OLEEResourcePO> oleeResourcePOList, OLEEResourceRecordDocument oleeResourceRecordDocument) {
        Map map = new HashMap();
        map.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER,oleeResourceRecordDocument.getOleERSIdentifier());
        List<OleCopy> copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, map);
        for (OleCopy copy : copies) {
            if (copy.getPoItemId() != null && StringUtils.isBlank(copy.getInstanceId())) {
                oleeResourceSearchService.getPOFromCopy(oleeResourceRecordDocument.getTitle(), copy, oleeResourcePOList);
            }
        }
    }

    public void setRenewalRecipient(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(oleeResourceRecordDocument.getRecipientId())) {
            oleeResourceRecordDocument.setRecipientSelector(OLEConstants.SELECTOR_PERSON);
            oleeResourceRecordDocument.setRecipientName(alertService.getName(oleeResourceRecordDocument.getRecipientId()));
        } else if(org.apache.commons.lang3.StringUtils.isNotBlank(oleeResourceRecordDocument.getRecipientRoleId())) {
            oleeResourceRecordDocument.setRecipientSelector(OLEConstants.SELECTOR_ROLE);
            oleeResourceRecordDocument.setRecipientRoleName(alertService.getRoleName(oleeResourceRecordDocument.getRecipientRoleId()));
        } else if(org.apache.commons.lang3.StringUtils.isNotBlank(oleeResourceRecordDocument.getRecipientGroupId())) {
            oleeResourceRecordDocument.setRecipientSelector(OLEConstants.SELECTOR_GROUP);
            oleeResourceRecordDocument.setRecipientGroupName(alertService.getGroupName(oleeResourceRecordDocument.getRecipientGroupId()));
        }
    }

    public boolean validateRole(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        if(StringUtils.isBlank(oleeResourceRecordDocument.getRecipientRoleName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.SUBSCRIPTION_RENEWAL, OLEConstants.ERROR_EMPTY_ROLE);
            return false;
        }
        oleeResourceRecordDocument.setRecipientRoleId(alertService.getRoleId((oleeResourceRecordDocument.getRecipientRoleName())));
        if(StringUtils.isBlank(oleeResourceRecordDocument.getRecipientRoleId())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.SUBSCRIPTION_RENEWAL,OLEConstants.ERROR_INVALID_NAME);
            return false;
        }
        return true;
    }

    public boolean validateGroup(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        if(StringUtils.isBlank(oleeResourceRecordDocument.getRecipientGroupName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.SUBSCRIPTION_RENEWAL, OLEConstants.ERROR_EMPTY_GROUP);
            return false;
        }
        oleeResourceRecordDocument.setRecipientGroupId(alertService.getGroupId(oleeResourceRecordDocument.getRecipientGroupName()));
        if(StringUtils.isBlank(oleeResourceRecordDocument.getRecipientGroupId())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.SUBSCRIPTION_RENEWAL, OLEConstants.ERROR_INVALID_GROUP_NAME);
            return false;
        }
        return true;
    }

    public boolean validatePerson(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        if(StringUtils.isBlank(oleeResourceRecordDocument.getRecipientName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.SUBSCRIPTION_RENEWAL, OLEConstants.ERROR_EMPTY_PERSON);
            return false;
        }
        oleeResourceRecordDocument.setRecipientId(alertService.getPersonId(oleeResourceRecordDocument.getRecipientName()));
        if(StringUtils.isBlank(oleeResourceRecordDocument.getRecipientId())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.SUBSCRIPTION_RENEWAL, OLEConstants.ERROR_INVALID_PERSON_NAME);
            return false;
        }
        return true;
    }

}
