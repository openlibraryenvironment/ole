package org.kuali.ole.ncip.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.bo.*;
import org.kuali.ole.ncip.bo.*;
import org.kuali.ole.converter.OLELookupUserConverter;
import org.kuali.ole.ncip.service.NCIPLookupUserResponseBuilder;
import org.kuali.ole.ncip.service.OLELookupUserService;
import org.kuali.ole.ncip.util.OLENCIPUtil;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 8/5/15.
 */
public class OLENCIPLookupUserServiceImpl extends NonSip2LookupUserServiceImpl implements OLELookupUserService {

    private static final Logger LOG = Logger.getLogger(OLENCIPLookupUserServiceImpl.class);

    private OleSelectDocumentService oleSelectDocumentService;
    private LookupUserInitiationData lookupUserInitiationData;
    private OLELookupUserConverter oleLookupUserConverter;

    public NCIPLookupUserResponseBuilder getNcipLookupUserResponseBuilder() {
        return new NCIPLookupUserResponseBuilder();
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

    public LookupUserInitiationData getLookupUserInitiationData() {
        return lookupUserInitiationData;
    }

    public void setLookupUserInitiationData(LookupUserInitiationData lookupUserInitiationData) {
        this.lookupUserInitiationData = lookupUserInitiationData;
    }

    public OLELookupUserConverter getOleLookupUserConverter() {
        if (null == oleLookupUserConverter) {
            oleLookupUserConverter = GlobalResourceLoader.getService(OLENCIPConstants.LOOKUP_USER_CONVERTER);
        }
        return oleLookupUserConverter;
    }

    public void setOleLookupUserConverter(OLELookupUserConverter oleLookupUserConverter) {
        this.oleLookupUserConverter = oleLookupUserConverter;
    }

    @Override
    public LookupUserResponseData performService(LookupUserInitiationData lookupUserInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();

        OLENCIPUtil oleNcipUtil = new OLENCIPUtil();
        setLookupUserInitiationData(lookupUserInitiationData);
        NCIPLookupUserResponseBuilder ncipLookupUserResponseBuilder = getNcipLookupUserResponseBuilder();
        LookupUserResponseData lookupUserResponseData = new LookupUserResponseData();

        AgencyId agencyId = oleNcipUtil.validateAgency(getLookupUserInitiationData().getInitiationHeader(), lookupUserResponseData);
        if (null == agencyId) return lookupUserResponseData;

        UserId userId = getLookupUserInitiationData().getUserId();
        boolean userValid = oleNcipUtil.validateUser(userId, lookupUserResponseData);
        if (!userValid) return lookupUserResponseData;

        String patronBarcode = userId.getUserIdentifierValue();
        if (StringUtils.isBlank(patronBarcode)) {
            oleNcipUtil.processProblems(lookupUserResponseData, "", ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.USER_IDENTIFIER_VALUE_DOES_NOT_EXIST), OLENCIPConstants.USER);
            return lookupUserResponseData;
        }
        String operatorId = oleNcipUtil.agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
        LOG.info("User Id : " + userId.getUserIdentifierValue() + " Operator Id : " + operatorId + " Agency Id " + agencyId.getValue());

        Map lookupUserParameters = new HashMap();
        lookupUserParameters.put("patronBarcode", patronBarcode);
        lookupUserParameters.put("operatorId", operatorId);
        String lookUpUserResponseXml = lookupUser(lookupUserParameters);
        OLELookupUser oleLookupUser = getOleLookupUserConverter().getLookupUser(lookUpUserResponseXml);
        if (oleLookupUser != null && StringUtils.isNotBlank(oleLookupUser.getMessage())) {
            if (oleLookupUser.getMessage().equalsIgnoreCase(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS))) {
                generateResponseDataFromLookupUser(ncipLookupUserResponseBuilder, lookupUserResponseData, agencyId, userId, oleLookupUser);
            } else if (oleLookupUser.getMessage().equalsIgnoreCase(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO))) {
                oleNcipUtil.processProblems(lookupUserResponseData, patronBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.USER_UN_AVAILABLE), OLENCIPConstants.USER);
                return lookupUserResponseData;
            } else if (oleLookupUser.getMessage().equalsIgnoreCase(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR))) {
                oleNcipUtil.processProblems(lookupUserResponseData, operatorId, oleLookupUser.getMessage(), OLENCIPConstants.OPERATOR);
                return lookupUserResponseData;
            }
        }

        oleStopWatch.end();
        LOG.info("Time taken to perform lookup user service : " + oleStopWatch.getTotalTime());
        return lookupUserResponseData;
    }

    private void generateResponseDataFromLookupUser(NCIPLookupUserResponseBuilder ncipLookupUserResponseBuilder, LookupUserResponseData lookupUserResponseData, AgencyId agencyId, UserId userId, OLELookupUser oleLookupUser) {
        UserOptionalFields userOptionalFields = new UserOptionalFields();

        setNameInformation(oleLookupUser.getPatronName(), userOptionalFields);

        setUserAddressInformation(oleLookupUser, userOptionalFields);

        setUserPrivileges(oleLookupUser, userOptionalFields, agencyId);

        List<LoanedItem> loanedItems = getLoanedItems(oleLookupUser.getOleCheckedOutItems(), agencyId);
        ncipLookupUserResponseBuilder.setLoanedItems(lookupUserResponseData, loanedItems);

        List<RequestedItem> requestedItems = getRequestedItems(oleLookupUser.getOleHolds(), agencyId);
        ncipLookupUserResponseBuilder.setRequestedItems(lookupUserResponseData, requestedItems);

        List<UserFiscalAccount> userFiscalAccounts = getUserFiscalAccounts(oleLookupUser.getOleItemFines(), agencyId);
        ncipLookupUserResponseBuilder.setUserFiscalAccounts(lookupUserResponseData, userFiscalAccounts);

        ncipLookupUserResponseBuilder.addOptionalFields(lookupUserResponseData, userOptionalFields);
        ncipLookupUserResponseBuilder.setUserId(lookupUserResponseData, userId);
    }

    private void setNameInformation(OlePatronNameBo olePatronNameBo, UserOptionalFields userOptionalFields) {
        try {
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

        } catch (Exception e) {
            LOG.error("Exception while getting user information" + e);
        }
    }

    private void setUserAddressInformation(OLELookupUser oleLookupUser, UserOptionalFields userOptionalFields) {
        try {
            ArrayList<UserAddressInformation> userAddressInformationList = new ArrayList<>();
            OlePatronAddressBo olePatronAddressBo = oleLookupUser.getPatronAddress();
            if (olePatronAddressBo != null) {
                userAddressInformationList.add(retrievePhysicalAddress(olePatronAddressBo));
            }
            OlePatronEmailBo olePatronEmailBo = oleLookupUser.getPatronEmail();
            if (olePatronEmailBo != null) {
                userAddressInformationList.add(retrieveElectronicAddress(olePatronEmailBo));
            }
            OlePatronPhoneBo olePatronPhoneBo = oleLookupUser.getPatronPhone();
            if (olePatronPhoneBo != null) {
                userAddressInformationList.add(retrieveTelephoneNumber(olePatronPhoneBo));
            }
            userOptionalFields.setUserAddressInformations(userAddressInformationList);

        } catch (Exception e) {
            LOG.error("Exception while getting address information" + e);
        }
    }

    private void setUserPrivileges(OLELookupUser oleLookupUser, UserOptionalFields userOptionalFields, AgencyId agencyId) {
        try {
            List<OLEUserPrivilege> oleUserPrivilegeList = oleLookupUser.getOleUserPrivileges();
            List<UserPrivilege> userPrivilegeList = new ArrayList<>();
            for (OLEUserPrivilege oleUserPrivilege : oleUserPrivilegeList) {
                UserPrivilege userPrivilege = getPrivilege(oleUserPrivilege.getUserPrivilegeStatus(), oleUserPrivilege.getUserPrivilegeType(), oleUserPrivilege.getUserPrivilegeDescription(), agencyId);
                userPrivilegeList.add(userPrivilege);
            }
            userOptionalFields.setUserPrivileges(userPrivilegeList);
        } catch (Exception e) {
            LOG.error("Exception while getting user privileges" + e);
        }
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

    private List<LoanedItem> getLoanedItems(OLECheckedOutItems oleCheckedOutItems, AgencyId agencyId) {
        List<LoanedItem> loanedItems = new ArrayList<>();
        try {
            if (oleCheckedOutItems != null && CollectionUtils.isNotEmpty(oleCheckedOutItems.getCheckedOutItems())) {
                for (OLECheckedOutItem oleCheckedOutItem : oleCheckedOutItems.getCheckedOutItems()) {
                    LoanedItem loanedItem = new LoanedItem();

                    ItemId itemId = new ItemId();
                    ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, oleCheckedOutItem.getItemType());
                    itemId.setAgencyId(agencyId);
                    itemId.setItemIdentifierType(itemIdentifierType);
                    itemId.setItemIdentifierValue(oleCheckedOutItem.getItemId());
                    loanedItem.setItemId(itemId);

                    loanedItem.setReminderLevel(new BigDecimal(oleCheckedOutItem.getNumberOfOverdueSent()));

                    Amount amount = new Amount();
                    CurrencyCode currencyCode = new CurrencyCode(OLENCIPConstants.USD, 1);
                    amount.setCurrencyCode(currencyCode);
                    amount.setMonetaryValue(new BigDecimal(0));
                    loanedItem.setAmount(amount);

                    List<BibliographicId> bibliographicIds = new ArrayList<BibliographicId>();
                    BibliographicId bibliographicId = new BibliographicId();
                    BibliographicItemId bibliographicItemId = new BibliographicItemId();
                    bibliographicItemId.setBibliographicItemIdentifier(oleCheckedOutItem.getItemId());
                    BibliographicRecordId bibliographicRecordId = new BibliographicRecordId();
                    bibliographicRecordId.setAgencyId(agencyId);
                    bibliographicRecordId.setBibliographicRecordIdentifier(oleCheckedOutItem.getCatalogueId());
                    bibliographicId.setBibliographicRecordId(bibliographicRecordId);
                    bibliographicId.setBibliographicItemId(bibliographicItemId);
                    bibliographicIds.add(bibliographicId);
                    loanedItem.setBibliographicIds(bibliographicIds);

                    loanedItem.setDateDue(getOleCirculationHelperService().getGregorianCalendarDate(oleCheckedOutItem.getDueDate()));
                    loanedItem.setTitle(oleCheckedOutItem.getTitle());
                    MediumType mediumType = new MediumType(OLENCIPConstants.MEDIUM_TYPE, oleCheckedOutItem.getItemType());
                    loanedItem.setMediumType(mediumType);
                    loanedItem.setDateCheckedOut(getOleCirculationHelperService().getGregorianCalendarDate(oleCheckedOutItem.getLoanDate()));
                    loanedItems.add(loanedItem);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while getting loaned items " + e);
        }
        return loanedItems;
    }

    private List<RequestedItem> getRequestedItems(OLEHolds oleHolds, AgencyId agencyId) {
        List<RequestedItem> requestedItems = new ArrayList<>();
        try {
            if (oleHolds != null && CollectionUtils.isNotEmpty(oleHolds.getOleHoldList())) {
                for (OLEHold oleHold : oleHolds.getOleHoldList()) {
                    RequestedItem requestedItem = new RequestedItem();
                    requestedItem.setHoldQueuePosition(new BigDecimal(oleHold.getPriority()));
                    ItemId itemId = new ItemId();
                    itemId.setItemIdentifierValue(oleHold.getItemId());
                    ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, oleHold.getItemType());
                    itemId.setItemIdentifierType(itemIdentifierType);
                    requestedItem.setItemId(itemId);
                    RequestId requestId = new RequestId();
                    requestId.setAgencyId(agencyId);
                    requestId.setRequestIdentifierValue(oleHold.getRequestId());
                    requestedItem.setRequestId(requestId);
                    RequestType requestType = new RequestType(OLENCIPConstants.REQUEST_TYPES, oleHold.getRequestType());
                    requestedItem.setRequestType(requestType);
                    RequestStatusType requestStatusType = new RequestStatusType("");
                    requestedItem.setRequestStatusType(requestStatusType);
                    requestedItem.setDatePlaced(getOleCirculationHelperService().getGregorianCalendarDate(oleHold.getCreateDate()));
                    requestedItem.setPickupDate(getOleCirculationHelperService().getGregorianCalendarDate(oleHold.getAvailableDate()));
                    PickupLocation pickupLocation = new PickupLocation(oleHold.getPickupLocation());
                    requestedItem.setPickupLocation(pickupLocation);
                    requestedItem.setTitle(oleHold.getTitle());
                    requestedItems.add(requestedItem);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while getting requested items" + e);
        }
        return requestedItems;
    }

    private List<UserFiscalAccount> getUserFiscalAccounts(OLEItemFines oleItemFines, AgencyId agencyId) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        List<UserFiscalAccount> userFiscalAccounts = new ArrayList<>();
        try {
            if (oleItemFines != null && CollectionUtils.isNotEmpty(oleItemFines.getOleItemFineList())) {
                for (OLEItemFine oleItemFine : oleItemFines.getOleItemFineList()) {
                    UserFiscalAccount userFiscalAccount = new UserFiscalAccount();
                    AccountBalance accountBalance = new AccountBalance();
                    CurrencyCode currencyCode = new CurrencyCode(OLENCIPConstants.USD, 1);
                    accountBalance.setCurrencyCode(currencyCode);
                    accountBalance.setMonetaryValue(oleItemFine.getBalance());
                    userFiscalAccount.setAccountBalance(accountBalance);
                    List<AccountDetails> accountDetailsList = new ArrayList<AccountDetails>();
                    AccountDetails accountDetails = new AccountDetails();
                    accountDetails.setAccrualDate(getOleCirculationHelperService().getGregorianCalendarDate(oleItemFine.getDateCharged()));

                    FiscalTransactionInformation fiscalTransactionInformation = new FiscalTransactionInformation();
                    Amount amount = new Amount();
                    amount.setCurrencyCode(currencyCode);
                    amount.setMonetaryValue(oleItemFine.getAmount());
                    fiscalTransactionInformation.setAmount(amount);
                    PaymentMethodType paymentMethodType = new PaymentMethodType(OLENCIPConstants.PAYMENT_METHOD_TYPE, OLENCIPConstants.CASH);
                    fiscalTransactionInformation.setPaymentMethodType(paymentMethodType);
                    FiscalActionType fiscalActionType = new FiscalActionType(OLENCIPConstants.FISCAL_ACTION_TYPE, OLENCIPConstants.FINES);
                    fiscalTransactionInformation.setFiscalActionType(fiscalActionType);
                    FiscalTransactionType fiscalTransactionType = new FiscalTransactionType(OLENCIPConstants.FISCAL_TRANSACTION_TYPE, oleItemFine.getReason());
                    fiscalTransactionInformation.setFiscalTransactionType(fiscalTransactionType);
                    ItemDetails itemDetails = new ItemDetails();
                    ItemId itemId = new ItemId();
                    if (oleItemFine.getItemId() != null)
                        itemId.setItemIdentifierValue(oleItemFine.getItemId());
                    else
                        itemId.setItemIdentifierValue("");
                    itemId.setAgencyId(agencyId);
                    ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, OLENCIPConstants.ITEM_BARCODES);
                    itemId.setItemIdentifierType(itemIdentifierType);
                    itemDetails.setItemId(itemId);
                    BibliographicDescription bibliographicDescription = new BibliographicDescription();
                    bibliographicDescription.setTitle(oleItemFine.getTitle());
                    bibliographicDescription.setAuthor(oleItemFine.getAuthor());
                    itemDetails.setBibliographicDescription(bibliographicDescription);
                    fiscalTransactionInformation.setItemDetails(itemDetails);
                    accountDetails.setFiscalTransactionInformation(fiscalTransactionInformation);
                    accountDetailsList.add(accountDetails);
                    userFiscalAccount.setAccountDetails(accountDetailsList);
                    userFiscalAccounts.add(userFiscalAccount);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while getting user physical accounts " + e);
        }
        oleStopWatch.end();
        LOG.info("For " + userFiscalAccounts.size() + " user fisical accounts, time taken : " + oleStopWatch.getTotalTime());
        return userFiscalAccounts;
    }

    @Override
    public void validatePatron() {

    }

    @Override
    public boolean isRenewalInfoNeeded() {
        return false;
    }

    @Override
    public OLEUserPrivilege getStatusPrivilege() {
        OLEUserPrivilege statusPriv = null;

        String errorMessage = new OLENCIPUtil().fireLookupUserRules(getOlePatronDocument());

        if (StringUtils.isBlank(errorMessage)) {
            statusPriv = getPrivilege(OLEConstants.OK, OLEConstants.STATUS, OLEConstants.STATUS_DESCRIPTION);
        } else {
            statusPriv = getPrivilege(OLEConstants.BLOCKED, OLEConstants.STATUS, OLEConstants.STATUS_DESCRIPTION);
        }

        return statusPriv;
    }

    @Override
    protected void processErrorResponseForOperator() {
        getOleLookupUser().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
    }

    @Override
    protected void processErrorResponseForPatron() {
        getOleLookupUser().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
    }

    @Override
    protected void processSuccessResponseForLookupUser() {
        getOleLookupUser().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
    }

    @Override
    protected void processSuccessResponseForItemFine(OLEItemFines oleItemFines) {
        oleItemFines.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
    }

    @Override
    protected void processInfoForItemFine(OLEItemFines oleItemFines) {
        oleItemFines.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_FINE));
    }

    @Override
    protected boolean nameInformationDesired() {
        return getLookupUserInitiationData().getNameInformationDesired();
    }

    @Override
    protected boolean userAddressInformationDesired() {
        return getLookupUserInitiationData().getUserAddressInformationDesired();
    }

    @Override
    protected boolean userPrivilegeDesired() {
        return getLookupUserInitiationData().getUserPrivilegeDesired();
    }

    @Override
    protected boolean loanedItemsDesired() {
        return getLookupUserInitiationData().getLoanedItemsDesired();
    }

    @Override
    protected boolean requestedItemsDesired() {
        return getLookupUserInitiationData().getRequestedItemsDesired();
    }

    @Override
    protected boolean userFiscalAccountDesired() {
        return getLookupUserInitiationData().getUserFiscalAccountDesired();
    }

    @Override
    protected void preProcess(Map lookupUserParameters) {

    }
}
