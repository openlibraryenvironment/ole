package org.kuali.ole.ncip.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.ncip.bo.*;
import org.kuali.ole.ncip.service.NCIPLookupUserResponseBuilder;
import org.kuali.ole.ncip.util.OLENCIPUtil;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 8/5/15.
 */
public class OLENCIPLookupUserServiceImpl extends OLENCIPUtil implements LookupUserService {

    private static final Logger LOG = Logger.getLogger(OLENCIPLookupUserServiceImpl.class);

    private BusinessObjectService businessObjectService;
    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private CircDeskLocationResolver circDeskLocationResolver;
    private OleSelectDocumentService oleSelectDocumentService;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;
    private DocstoreUtil docstoreUtil;
    private OlePatronRecordUtil olePatronRecordUtil;

    public NCIPLookupUserResponseBuilder getNcipLookupUserResponseBuilder() {
        return new NCIPLookupUserResponseBuilder();
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OLECirculationHelperServiceImpl getOleCirculationHelperService() {
        if (null == oleCirculationHelperService) {
            oleCirculationHelperService = GlobalResourceLoader.getService(OLENCIPConstants.CIRCULATION_HELPER_SERVICE);
        }
        return oleCirculationHelperService;
    }

    public void setOleCirculationHelperService(OLECirculationHelperServiceImpl oleCirculationHelperService) {
        this.oleCirculationHelperService = oleCirculationHelperService;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public OleSelectDocumentService getOleSelectDocumentService() {
        if (oleSelectDocumentService == null) {
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        this.oleSelectDocumentService = oleSelectDocumentService;
    }

    private OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();
        }
        return oleLoanDocumentsFromSolrBuilder;
    }

    public void setOleLoanDocumentsFromSolrBuilder(OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder) {
        this.oleLoanDocumentsFromSolrBuilder = oleLoanDocumentsFromSolrBuilder;
    }

    public DocstoreUtil getDocstoreUtil() {
        if (null == docstoreUtil) {
            docstoreUtil = new DocstoreUtil();
        }
        return docstoreUtil;
    }

    public void setDocstoreUtil(DocstoreUtil docstoreUtil) {
        this.docstoreUtil = docstoreUtil;
    }

    private OlePatronRecordUtil getOlePatronRecordUtil() {
        if (null == olePatronRecordUtil) {
            olePatronRecordUtil = (OlePatronRecordUtil) SpringContext.getBean("olePatronRecordUtil");
        }
        return olePatronRecordUtil;
    }

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
    }

    @Override
    public LookupUserResponseData performService(LookupUserInitiationData lookupUserInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();

        NCIPLookupUserResponseBuilder ncipLookupUserResponseBuilder = getNcipLookupUserResponseBuilder();
        LookupUserResponseData lookupUserResponseData = new LookupUserResponseData();

        AgencyId agencyId = validateAgency(lookupUserInitiationData.getInitiationHeader(), lookupUserResponseData);
        if (null == agencyId) return lookupUserResponseData;

        UserId userId = lookupUserInitiationData.getUserId();
        boolean userValid = validateUser(userId, lookupUserResponseData);
        if (!userValid) return lookupUserResponseData;

        String patronBarcode = userId.getUserIdentifierValue();
        if (StringUtils.isBlank(patronBarcode)) {
            processProblems(lookupUserResponseData, "", ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.USER_IDENTIFIER_VALUE_DOES_NOT_EXIST), OLENCIPConstants.USER);
            return lookupUserResponseData;
        }
        OlePatronDocument olePatronDocument = null;
        try {
            olePatronDocument = getOlePatronRecordUtil().getPatronRecordByBarcode(patronBarcode);
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
        if (olePatronDocument == null) {
            processProblems(lookupUserResponseData, patronBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO), OLENCIPConstants.USER);
            return lookupUserResponseData;
        }

        String operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
        OleCirculationDesk oleCirculationDesk = validOperator(lookupUserResponseData, operatorId);
        if (null == oleCirculationDesk) return lookupUserResponseData;

        LOG.info("User Id : " + userId.getUserIdentifierValue() + " Operator Id : " + operatorId + " Agency Id " + agencyId.getValue());

        UserOptionalFields userOptionalFields = new UserOptionalFields();
        String olePatronId = olePatronDocument.getOlePatronId();

        if (lookupUserInitiationData.getNameInformationDesired()) {
            setNameInformation(olePatronId, userOptionalFields);
        }
        if (lookupUserInitiationData.getUserAddressInformationDesired()) {
            setUserAddressInformation(olePatronId, userOptionalFields);
        }
        if (lookupUserInitiationData.getUserPrivilegeDesired()) {
            setUserPrivileges(olePatronDocument, userOptionalFields, agencyId);
        }
        if (lookupUserInitiationData.getLoanedItemsDesired()) {
            List<LoanedItem> loanedItems = getLoanedItems(agencyId, olePatronId);
            ncipLookupUserResponseBuilder.setLoanedItems(lookupUserResponseData, loanedItems);
        }
        if (lookupUserInitiationData.getRequestedItemsDesired()) {
            List<RequestedItem> requestedItems = getRequestedItems(agencyId, olePatronId);
            ncipLookupUserResponseBuilder.setRequestedItems(lookupUserResponseData, requestedItems);
        }
        if (lookupUserInitiationData.getUserFiscalAccountDesired()) {
            List<UserFiscalAccount> userFiscalAccounts = getUserFiscalAccounts(agencyId, olePatronId);
            ncipLookupUserResponseBuilder.setUserFiscalAccounts(lookupUserResponseData, userFiscalAccounts);
        }
        ncipLookupUserResponseBuilder.addOptionalFields(lookupUserResponseData, userOptionalFields);
        ncipLookupUserResponseBuilder.setUserId(lookupUserResponseData, userId);

        oleStopWatch.end();
        LOG.info("Time taken to perform lookup user service : " + oleStopWatch.getTotalTime());
        return lookupUserResponseData;
    }

    private void setNameInformation(String patronId, UserOptionalFields userOptionalFields) {
        try {
            if (StringUtils.isNotBlank(patronId)) {
                OlePatronNameBo olePatronNameBo = getOleCirculationHelperService().getEntityNameBo(patronId);

                if (olePatronNameBo != null) {
                    NameInformation ni = new NameInformation();
                    PersonalNameInformation pni = new PersonalNameInformation();
                    StructuredPersonalUserName structuredPersonalUserName = new StructuredPersonalUserName();

                    String firstName = olePatronNameBo.getFirstName();
                    String middleName = olePatronNameBo.getMiddleName();
                    String lastName = olePatronNameBo.getLastName();
                    StringBuffer patronName = new StringBuffer();

                    if (StringUtils.isNotBlank(firstName)) {
                        structuredPersonalUserName.setGivenName(firstName);
                        patronName.append(firstName);
                        patronName.append(OLEConstants.SPACE);
                    }
                    if (StringUtils.isNotBlank(middleName)) {
                        patronName.append(middleName);
                        patronName.append(OLEConstants.SPACE);
                    }
                    if (StringUtils.isNotBlank(lastName)) {
                        structuredPersonalUserName.setSurname(lastName);
                        patronName.append(lastName);
                        patronName.append(OLEConstants.SPACE);
                    }
                    pni.setStructuredPersonalUserName(structuredPersonalUserName);
                    if (patronName.length() > 0) {
                        patronName.deleteCharAt(patronName.length() - 1);
                        pni.setUnstructuredPersonalUserName(patronName.toString());
                    }
                    ni.setPersonalNameInformation(pni);
                    userOptionalFields.setNameInformation(ni);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while getting user information" + e);
        }
    }

    private void setUserAddressInformation(String patronId, UserOptionalFields userOptionalFields) {
        try {
            if (StringUtils.isNotBlank(patronId)) {
                ArrayList<UserAddressInformation> userAddressInformationList = new ArrayList<UserAddressInformation>();

                OlePatronAddressBo olePatronAddressBo = getOleCirculationHelperService().getDefaultAddressBo(patronId);
                if (olePatronAddressBo != null) {
                    userAddressInformationList.add(retrievePhysicalAddress(olePatronAddressBo));
                }
                OlePatronEmailBo olePatronEmailBo = getOleCirculationHelperService().getDefaultEmailBo(patronId);
                if (olePatronEmailBo != null) {
                    userAddressInformationList.add(retrieveElectronicAddress(olePatronEmailBo));
                }
                OlePatronPhoneBo olePatronPhoneBo = getOleCirculationHelperService().getDefaultPhoneBo(patronId);
                if (olePatronPhoneBo != null) {
                    userAddressInformationList.add(retrieveTelephoneNumber(olePatronPhoneBo));
                }
                userOptionalFields.setUserAddressInformations(userAddressInformationList);
            }
        } catch (Exception e) {
            LOG.error("Exception while getting address information" + e);
        }
    }

    private void setUserPrivileges(OlePatronDocument olePatronDocument, UserOptionalFields userOptionalFields, AgencyId agencyId) {
        try {
            List<UserPrivilege> userPrivilegeList = new ArrayList<>();

            UserPrivilege courtesyNoticePriv = getPrivilege(String.valueOf(olePatronDocument.isCourtesyNotice()), OLEConstants.COURTESY_NOTICE, OLEConstants.COURTESY_DESCRIPTION, agencyId);
            userPrivilegeList.add(courtesyNoticePriv);

            UserPrivilege deliveryPriv = getPrivilege(String.valueOf(olePatronDocument.isDeliveryPrivilege()), OLEConstants.DELIVERY, OLEConstants.DELIVERY_DESCRIPTION, agencyId);
            userPrivilegeList.add(deliveryPriv);

            UserPrivilege pagingPriv = getPrivilege(String.valueOf(olePatronDocument.isPagingPrivilege()), OLEConstants.PAGING, OLEConstants.PAGING_DESCRIPTION, agencyId);
            userPrivilegeList.add(pagingPriv);

            UserPrivilege profilePriv = getPrivilege(olePatronDocument.getBorrowerTypeName(), OLEConstants.PROFILE, OLEConstants.PROFILE_DESCRIPTION, agencyId);
            userPrivilegeList.add(profilePriv);

            UserPrivilege statusPriv = getStatusPrivilege(olePatronDocument, agencyId);
            userPrivilegeList.add(statusPriv);

            userOptionalFields.setUserPrivileges(userPrivilegeList);
        } catch (Exception e) {
            LOG.error("Exception while getting user privileges" + e);
        }
    }

    private UserPrivilege getStatusPrivilege(OlePatronDocument olePatronDocument, AgencyId agencyId) {
        UserPrivilege statusPriv = null;

        String errorMessage = fireLookupUserRules(olePatronDocument);

        if (StringUtils.isBlank(errorMessage)) {
            statusPriv = getPrivilege(OLEConstants.OK, OLEConstants.STATUS, OLEConstants.STATUS_DESCRIPTION, agencyId);
        } else {
            statusPriv = getPrivilege(OLEConstants.BLOCKED, OLEConstants.STATUS, OLEConstants.STATUS_DESCRIPTION, agencyId);
        }

        return statusPriv;
    }

    private UserAddressInformation retrievePhysicalAddress(OlePatronAddressBo olePatronAddressBo) {
        UserAddressInformation userAddressInformation = new UserAddressInformation();
        PhysicalAddress physicalAddress = new PhysicalAddress();
        StructuredAddress structuredAddress = new StructuredAddress();
        structuredAddress.setLine1(olePatronAddressBo.getLine1());
        structuredAddress.setLine2(olePatronAddressBo.getLine2());
        structuredAddress.setPostalCode(olePatronAddressBo.getPostalCode());
        structuredAddress.setCountry(olePatronAddressBo.getCountryCode());
        structuredAddress.setRegion(olePatronAddressBo.getStateProvinceCode());
        structuredAddress.setLocality(olePatronAddressBo.getCity());
        physicalAddress.setStructuredAddress(structuredAddress);
        UserAddressRoleType userAddressRoleType = new UserAddressRoleType(olePatronAddressBo.getAddressTypeCode());
        userAddressInformation.setUserAddressRoleType(userAddressRoleType);
        PhysicalAddressType physicalAddressType = new PhysicalAddressType(OLENCIPConstants.ADDRESS_TYPE_SCHEME, olePatronAddressBo.getAddressTypeCode());
        physicalAddress.setPhysicalAddressType(physicalAddressType);
        userAddressInformation.setPhysicalAddress(physicalAddress);
        return userAddressInformation;
    }

    private UserAddressInformation retrieveElectronicAddress(OlePatronEmailBo olePatronEmailBo) {
        ElectronicAddress electronicAddress = new ElectronicAddress();
        electronicAddress.setElectronicAddressData(olePatronEmailBo.getEmailAddress());
        ElectronicAddressType electronicAddressType = new ElectronicAddressType(getOleSelectDocumentService().getSelectParameterValue(OLENCIPConstants.EMAIL));
        electronicAddress.setElectronicAddressType(electronicAddressType);
        UserAddressInformation userAddressInformation = new UserAddressInformation();
        UserAddressRoleType userAddressRoleType = new UserAddressRoleType(olePatronEmailBo.getEmailTypeCode());
        userAddressInformation.setUserAddressRoleType(userAddressRoleType);
        userAddressInformation.setElectronicAddress(electronicAddress);
        return userAddressInformation;
    }

    private UserAddressInformation retrieveTelephoneNumber(OlePatronPhoneBo olePatronPhoneBo) {
        ElectronicAddress phone = new ElectronicAddress();
        phone.setElectronicAddressData(olePatronPhoneBo.getPhoneNumber());
        phone.setElectronicAddressType(new ElectronicAddressType(OLENCIPConstants.TELEPHONE_CODE));
        UserAddressInformation uai = new UserAddressInformation();
        uai.setUserAddressRoleType(new UserAddressRoleType(olePatronPhoneBo.getPhoneTypeCode()));
        uai.setElectronicAddress(phone);
        return uai;
    }

    private UserPrivilege getPrivilege(String privilegeStatus, String privilegeType, String privilegeDesc, AgencyId agencyId) {
        UserPrivilege userPrivilege = new UserPrivilege();
        UserPrivilegeStatus userPrivilegeStatus = new UserPrivilegeStatus();
        AgencyUserPrivilegeType agencyUserPrivilegeType = new AgencyUserPrivilegeType("", privilegeType);
        UserPrivilegeStatusType userPrivilegeStatusType = new UserPrivilegeStatusType("", privilegeStatus);
        userPrivilegeStatus.setUserPrivilegeStatusType(userPrivilegeStatusType);
        userPrivilege.setAgencyUserPrivilegeType(agencyUserPrivilegeType);
        userPrivilege.setUserPrivilegeDescription(privilegeDesc);
        userPrivilege.setAgencyId(agencyId);
        userPrivilege.setUserPrivilegeStatus(userPrivilegeStatus);
        return userPrivilege;
    }

    private List<LoanedItem> getLoanedItems(AgencyId agencyId, String olePatronId) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        List<LoanedItem> loanedItems = new ArrayList<LoanedItem>();

        try {
            List<OleLoanDocument> oleLoanDocumentList = getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr(olePatronId, null);
            if (CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
                setNumberOfOverdueNoticesSentForLoanDocuments(olePatronId, oleLoanDocumentList);
                for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
                    LoanedItem loanedItem = new LoanedItem();

                    ItemId itemId = new ItemId();
                    ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, oleLoanDocument.getItemType());
                    itemId.setAgencyId(agencyId);
                    itemId.setItemIdentifierType(itemIdentifierType);
                    itemId.setItemIdentifierValue(oleLoanDocument.getItemId());
                    loanedItem.setItemId(itemId);

                    String numberOfOverdueNoticesSent = oleLoanDocument.getNumberOfOverdueNoticesSent();
                    if (StringUtils.isNotBlank(numberOfOverdueNoticesSent)) {
                        loanedItem.setReminderLevel(new BigDecimal(numberOfOverdueNoticesSent));
                    } else {
                        loanedItem.setReminderLevel(BigDecimal.ZERO);
                    }

                    Amount amount = new Amount();
                    CurrencyCode currencyCode = new CurrencyCode(OLENCIPConstants.USD, 1);
                    amount.setCurrencyCode(currencyCode);
                    amount.setMonetaryValue(new BigDecimal(0));
                    loanedItem.setAmount(amount);

                    List<BibliographicId> bibliographicIds = new ArrayList<BibliographicId>();
                    BibliographicId bibliographicId = new BibliographicId();
                    BibliographicItemId bibliographicItemId = new BibliographicItemId();
                    bibliographicItemId.setBibliographicItemIdentifier(oleLoanDocument.getItemId());
                    BibliographicRecordId bibliographicRecordId = new BibliographicRecordId();
                    bibliographicRecordId.setAgencyId(agencyId);
                    bibliographicRecordId.setBibliographicRecordIdentifier(oleLoanDocument.getBibUuid());
                    bibliographicId.setBibliographicRecordId(bibliographicRecordId);
                    bibliographicId.setBibliographicItemId(bibliographicItemId);
                    bibliographicIds.add(bibliographicId);
                    loanedItem.setBibliographicIds(bibliographicIds);

                    if (oleLoanDocument.getLoanDueDate() != null) {
                        loanedItem.setDateDue(getOleCirculationHelperService().getGregorianCalendarDate(oleLoanDocument.getLoanDueDate().toString()));
                    } else {
                        loanedItem.setDateDue(getOleCirculationHelperService().getGregorianCalendarDate((new java.sql.Timestamp(new Date(2025, 1, 1).getTime()).toString())));
                    }
                    loanedItem.setTitle(oleLoanDocument.getTitle());

                    MediumType mediumType = new MediumType(OLENCIPConstants.MEDIUM_TYPE, oleLoanDocument.getItemType());
                    loanedItem.setMediumType(mediumType);

                    if (oleLoanDocument.getCreateDate() != null) {
                        loanedItem.setDateCheckedOut(getOleCirculationHelperService().getGregorianCalendarDate(new Timestamp(oleLoanDocument.getCreateDate().getTime()).toString()));
                    }
                    loanedItems.add(loanedItem);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while getting loaned items " + e);
        }
        oleStopWatch.end();
        LOG.info("For " + loanedItems.size() + " loaned items, time taken : " + oleStopWatch.getTotalTime());
        return loanedItems;
    }

    private void setNumberOfOverdueNoticesSentForLoanDocuments(String olePatronId, List<OleLoanDocument> oleLoanDocuments) {
        List<String> itemIds = new ArrayList<>();
        Map<String, OleLoanDocument> map = new HashMap();
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
            String itemId = oleLoanDocument.getItemUuid();
            itemIds.add(itemId);
            map.put(itemId, oleLoanDocument);
        }
        Collection collection = oleLoanDocumentDaoOjb.getLoanDocumentsUsingItemIdsAndPatronId(olePatronId, itemIds);
        List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) collection;

        for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
            if (map.containsKey(oleLoanDocument.getItemUuid())) {
                map.get(oleLoanDocument.getItemUuid()).setNumberOfOverdueNoticesSent(oleLoanDocument.getNumberOfOverdueNoticesSent());
            }
        }
    }

    private List<RequestedItem> getRequestedItems(AgencyId agencyId, String olePatronId) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        List<RequestedItem> requestedItems = new ArrayList<RequestedItem>();

        try {
            Map map = new HashMap();
            map.put(OLEConstants.OleDeliverRequest.BORROWER_ID, olePatronId);
            List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, map);
            if (CollectionUtils.isNotEmpty(oleDeliverRequestBoList)) {
                for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
                    RequestedItem requestedItem = new RequestedItem();

                    if (StringUtils.isNotBlank(oleDeliverRequestBo.getItemId())) {
                        OleItemSearch oleItemSearch = getOleItemSearch(oleDeliverRequestBo.getItemId());
                        oleDeliverRequestBo.setTitle(oleItemSearch.getTitle());
                        oleDeliverRequestBo.setItemType(oleItemSearch.getItemType());
                    }
                    requestedItem.setHoldQueuePosition(new BigDecimal(oleDeliverRequestBo.getBorrowerQueuePosition()));
                    ItemId itemId = new ItemId();
                    itemId.setItemIdentifierValue(oleDeliverRequestBo.getItemId());
                    ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, oleDeliverRequestBo.getItemType());
                    itemId.setItemIdentifierType(itemIdentifierType);
                    requestedItem.setItemId(itemId);
                    RequestId requestId = new RequestId();
                    requestId.setAgencyId(agencyId);
                    requestId.setRequestIdentifierValue(oleDeliverRequestBo.getRequestId());
                    requestedItem.setRequestId(requestId);
                    RequestType requestType = new RequestType(OLENCIPConstants.REQUEST_TYPES, oleDeliverRequestBo.getRequestTypeCode());
                    requestedItem.setRequestType(requestType);
                    RequestStatusType requestStatusType = new RequestStatusType("");
                    requestedItem.setRequestStatusType(requestStatusType);
                    if (oleDeliverRequestBo.getCreateDate() != null) {
                        requestedItem.setDatePlaced(getOleCirculationHelperService().getGregorianCalendarDate(new Timestamp(oleDeliverRequestBo.getCreateDate().getTime()).toString()));
                    }

                    setPickupDate(oleDeliverRequestBo, requestedItem);

                    PickupLocation pickupLocation = new PickupLocation(oleDeliverRequestBo.getPickUpLocationCode());
                    requestedItem.setPickupLocation(pickupLocation);
                    requestedItem.setTitle(oleDeliverRequestBo.getTitle());
                    requestedItems.add(requestedItem);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while getting requested items" + e);
        }
        oleStopWatch.end();
        LOG.info("For " + requestedItems.size() + " requested items, time taken : " + oleStopWatch.getTotalTime());
        return requestedItems;
    }

    private void setPickupDate(OleDeliverRequestBo oleDeliverRequestBo, RequestedItem requestedItem) {
        String availableDate = null;
        Map<String, String> loanMap = new HashMap<String, String>();
        loanMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, oleDeliverRequestBo.getItemId());
        List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
        if (CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
            OleLoanDocument oleLoanDocument = oleLoanDocumentList.get(0);
            if (oleLoanDocument.getLoanDueDate() != null) {
                String[] availableDates = oleLoanDocument.getLoanDueDate().toString().split(" ");
                if (availableDates != null && availableDates.length > 0) {
                    availableDate = availableDates[0];
                } else {
                    availableDate = oleLoanDocument.getLoanDueDate().toString();
                }
            } else {
                availableDate = OLEConstants.INDEFINITE;
            }
            if (StringUtils.isNotBlank(availableDate)) {
                requestedItem.setPickupDate(oleCirculationHelperService.getGregorianCalendarDate(availableDate));
            }
        }
    }

    private List<UserFiscalAccount> getUserFiscalAccounts(AgencyId agencyId, String olePatronId) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        List<UserFiscalAccount> userFiscalAccounts = new ArrayList<UserFiscalAccount>();
        try {
            Map map = new HashMap();
            map.put("patronId", olePatronId);
            List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) getBusinessObjectService().findMatching(PatronBillPayment.class, map);
            if (CollectionUtils.isNotEmpty(patronBillPaymentList)) {
                for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
                    List<FeeType> feeTypeList = patronBillPayment.getFeeType();
                    for (FeeType feeType : feeTypeList) {
                        if (StringUtils.isNotBlank(feeType.getItemBarcode())) {
                            OleItemSearch oleItemSearch = getOleItemSearch(feeType.getItemBarcode());
                            feeType.setItemUuid(oleItemSearch.getItemUUID());
                            feeType.setItemAuthor(oleItemSearch.getAuthor());
                            feeType.setItemTitle(oleItemSearch.getTitle());
                        }
                        UserFiscalAccount userFiscalAccount = new UserFiscalAccount();
                        AccountBalance accountBalance = new AccountBalance();
                        CurrencyCode currencyCode = new CurrencyCode(OLENCIPConstants.USD, 1);
                        accountBalance.setCurrencyCode(currencyCode);
                        accountBalance.setMonetaryValue(feeType.getBalFeeAmount().bigDecimalValue());
                        userFiscalAccount.setAccountBalance(accountBalance);
                        List<AccountDetails> accountDetailsList = new ArrayList<AccountDetails>();
                        AccountDetails accountDetails = new AccountDetails();
                        if (feeType.getBillDate() != null) {
                            accountDetails.setAccrualDate(getOleCirculationHelperService().getGregorianCalendarDate(feeType.getBillDate().toString()));
                        }
                        FiscalTransactionInformation fiscalTransactionInformation = new FiscalTransactionInformation();
                        Amount amount = new Amount();
                        amount.setCurrencyCode(currencyCode);
                        amount.setMonetaryValue(feeType.getFeeAmount() != null ? feeType.getFeeAmount().bigDecimalValue() : OLEConstants.BIGDECIMAL_DEF_VALUE);
                        fiscalTransactionInformation.setAmount(amount);
                        PaymentMethodType paymentMethodType = new PaymentMethodType(OLENCIPConstants.PAYMENT_METHOD_TYPE, OLENCIPConstants.CASH);
                        fiscalTransactionInformation.setPaymentMethodType(paymentMethodType);
                        FiscalActionType fiscalActionType = new FiscalActionType(OLENCIPConstants.FISCAL_ACTION_TYPE, OLENCIPConstants.FINES);
                        fiscalTransactionInformation.setFiscalActionType(fiscalActionType);

                        FiscalTransactionType fiscalTransactionType;
                        if (feeType.getOleFeeType() != null) {
                            fiscalTransactionType = new FiscalTransactionType(OLENCIPConstants.FISCAL_TRANSACTION_TYPE, feeType.getOleFeeType().getFeeTypeName());
                        } else {
                            fiscalTransactionType = new FiscalTransactionType(OLENCIPConstants.FISCAL_TRANSACTION_TYPE, feeType.getFeeType());
                        }
                        fiscalTransactionInformation.setFiscalTransactionType(fiscalTransactionType);
                        ItemDetails itemDetails = new ItemDetails();
                        ItemId itemId = new ItemId();
                        if (feeType.getItemBarcode() != null)
                            itemId.setItemIdentifierValue(feeType.getItemBarcode());
                        else
                            itemId.setItemIdentifierValue("");
                        itemId.setAgencyId(agencyId);
                        ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, OLENCIPConstants.ITEM_BARCODES);
                        itemId.setItemIdentifierType(itemIdentifierType);
                        itemDetails.setItemId(itemId);
                        BibliographicDescription bibliographicDescription = new BibliographicDescription();
                        bibliographicDescription.setTitle(feeType.getItemTitle());
                        bibliographicDescription.setAuthor(feeType.getItemAuthor());
                        itemDetails.setBibliographicDescription(bibliographicDescription);
                        fiscalTransactionInformation.setItemDetails(itemDetails);
                        accountDetails.setFiscalTransactionInformation(fiscalTransactionInformation);
                        accountDetailsList.add(accountDetails);
                        userFiscalAccount.setAccountDetails(accountDetailsList);
                        userFiscalAccounts.add(userFiscalAccount);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while getting user physical accounts " + e);
        }
        oleStopWatch.end();
        LOG.info("For " + userFiscalAccounts.size() + " user fisical accounts, time taken : " + oleStopWatch.getTotalTime());
        return userFiscalAccounts;
    }

    private OleItemSearch getOleItemSearch(String itemBarcode) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        OleItemSearch oleItemSearch = new OleItemSearch();
        try {
            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "ITEMBARCODE", itemBarcode), ""));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.ID));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.TITLE));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.AUTHOR));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.ITEM_TYPE));
            SearchResponse searchResponse = getDocstoreUtil().getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() > 0) {
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.TITLE)) {
                            oleItemSearch.setTitle(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.AUTHOR)) {
                            oleItemSearch.setAuthor(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ID)) {
                            oleItemSearch.setItemUUID(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ITEM_TYPE)) {
                            oleItemSearch.setItemType(searchResultField.getFieldValue());
                        }

                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
        oleStopWatch.end();
        LOG.info("Time taken to getOleItemSearch : " + oleStopWatch.getTotalTime());
        return oleItemSearch;
    }
}
