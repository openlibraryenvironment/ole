package org.kuali.ole.ncip.service.impl;

import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.ncip.bo.*;
import org.kuali.ole.ncip.converter.OLELookupUserConverter;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.OLELookupUserService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/31/13
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLELookupUserServiceImpl implements OLELookupUserService {
    private static final Logger LOG = Logger.getLogger(OLELookupUserServiceImpl.class);
    private OLECirculationService oleCirculationService = getOleCirculationService();
    private OLECirculationHelperServiceImpl oleCirculationHelperService= getOleCirculationHelperService();
    private LoanProcessor loanProcessor;
    private OLELookupUserConverter oleLookupUserConverter = getOleLookupUserConverter();
    private OleSelectDocumentService oleSelectDocumentService;


    public LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    public OLECirculationService getOleCirculationService() {
        if (null == oleCirculationService) {
            oleCirculationService = GlobalResourceLoader.getService(OLENCIPConstants.CIRCULATION_SERVICE);
        }
        return oleCirculationService;
    }

    public void setOleCirculationService(OLECirculationService oleCirculationService) {
        this.oleCirculationService = oleCirculationService;
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

    public OLELookupUserConverter getOleLookupUserConverter() {
        if(null == oleLookupUserConverter ){
            oleLookupUserConverter = GlobalResourceLoader.getService(OLENCIPConstants.LOOKUP_USER_CONVERTER);
        }
        return oleLookupUserConverter;
    }

    public void setOleLookupUserConverter(OLELookupUserConverter oleLookupUserConverter) {
        this.oleLookupUserConverter = oleLookupUserConverter;
    }

    @Override
    public LookupUserResponseData performService(LookupUserInitiationData initData,
                                                 ServiceContext serviceContext,
                                                 RemoteServiceManager serviceManager) {

        LookupUserResponseData responseData = new LookupUserResponseData();
        oleCirculationService = getOleCirculationService();
        oleCirculationHelperService = getOleCirculationHelperService();
        OLELookupUserConverter OLELookupUserConverter = new org.kuali.ole.ncip.converter.OLELookupUserConverter();
        //String lookUpUserResponseXml=OLECirculationService.lookupUser(initData.getUserId().getUserIdentifierValue(),"dev2");
        String lookUpUserResponseXml = null;
        OLELookupUser oleLookupUser = null;
        List<Problem> problems = new ArrayList<Problem>();
        AgencyId agencyId = null;
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        else
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));

        String operatorId, itemType = "";
        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        itemType = agencyPropertyMap.get(OLENCIPConstants.ITEM_TYPE);
        operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        if (agencyPropertyMap.size() > 0) {
            try {
                LOG.info("User Id : "+initData.getUserId().getUserIdentifierValue() + " Operator Id : " + operatorId + " Agency Id "+agencyId.getValue());
                lookUpUserResponseXml = oleCirculationService.lookupUserForNCIP(initData.getUserId().getUserIdentifierValue(), operatorId, agencyId.getValue());
                oleLookupUser = (OLELookupUser) oleLookupUserConverter.getLookupUser(lookUpUserResponseXml);
                if (oleLookupUser.getMessage() != null) {
                    if (oleLookupUser.getMessage().equalsIgnoreCase(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS))) {
                        responseData = generateResponseDataFromLookupUser(oleLookupUser, initData, agencyId);
                        responseData.setUserId(initData.getUserId());
                    } else if (oleLookupUser.getMessage().equalsIgnoreCase(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO))) {
                        problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.USER_UN_AVAILABLE));
                        problem.setProblemElement(OLENCIPConstants.USER);
                        problem.setProblemType(problemType);
                        problem.setProblemValue(initData.getUserId().getUserIdentifierValue());
                        problems.add(problem);
                        responseData.setProblems(problems);
                    } else if (oleLookupUser.getMessage().equalsIgnoreCase(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID))) {
                        problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.OPERATOR_UN_AVAILABLE));
                        problem.setProblemElement(OLENCIPConstants.OPERATOR);
                        problem.setProblemType(problemType);
                        problem.setProblemValue(initData.getUserId().getUserIdentifierValue());
                        problems.add(problem);
                        responseData.setProblems(problems);
                    }
                }

            } catch (Exception e) {
                LOG.error(e);
            }
        } else {
            problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.INVALID_AGENCY_ID));
            problem.setProblemElement(OLENCIPConstants.AGENCY_ID);
            problem.setProblemType(problemType);
            problem.setProblemValue(agencyId.getValue());
            problems.add(problem);
            responseData.setProblems(problems);
        }

        return responseData;
    }

    public LookupUserResponseData generateResponseDataFromLookupUser(OLELookupUser oleLookupUser, LookupUserInitiationData initData, AgencyId agencyId) {
        LookupUserResponseData lookupUserResponseData = new LookupUserResponseData();
        if (initData.getUserFiscalAccountDesired())
            lookupUserResponseData.setUserFiscalAccounts(getUserFiscalAccounts(oleLookupUser.getOleItemFines(), agencyId));
        if (initData.getLoanedItemsDesired())
            lookupUserResponseData.setLoanedItems(getLoanedItems(oleLookupUser.getOleCheckedOutItems(), agencyId));
        if (initData.getRequestedItemsDesired())
            lookupUserResponseData.setRequestedItems(getRequestedItems(oleLookupUser.getOleHolds(), agencyId));
        UserOptionalFields userOptionalFields = new UserOptionalFields();
        PersonalNameInformation pni = new PersonalNameInformation();
        String patronName = null;
        String firstName = null;
        String lastName = null;
        String middleName = null;
        if (oleLookupUser.getPatronName() != null) {
            if (oleLookupUser.getPatronName().getFirstName() != null) {
                firstName = oleLookupUser.getPatronName().getFirstName();
                patronName =  firstName;
            }
            if (oleLookupUser.getPatronName().getMiddleName() != null) {
                middleName = oleLookupUser.getPatronName().getMiddleName();
                if(patronName!=null){
                patronName = patronName + " " + middleName;
                }else{
                    patronName = middleName;
                }
            }
            if (oleLookupUser.getPatronName().getLastName() != null) {
                lastName = oleLookupUser.getPatronName().getLastName();
                if(patronName!=null){
                patronName = patronName + " " + lastName;
                }else{
                    patronName = lastName;
                }
            }
            StructuredPersonalUserName structuredPersonalUserName = new StructuredPersonalUserName();
            if (firstName != null) {
                structuredPersonalUserName.setGivenName(firstName);
            }
            if (lastName != null) {
                structuredPersonalUserName.setSurname(lastName);
            }
            pni.setStructuredPersonalUserName(structuredPersonalUserName);
            pni.setUnstructuredPersonalUserName(patronName);
        }

        NameInformation ni = new NameInformation();
        ni.setPersonalNameInformation(pni);
        //userOptionalFields.setNameInformation(ni);

        List<OLEUserPrivilege> oleUserPrivilegeList = oleLookupUser.getOleUserPrivileges();
        List<UserPrivilege> userPrivilegeList = new ArrayList<UserPrivilege>();
        UserPrivilege userPrivilege;
        UserPrivilegeStatus userPrivilegeStatus;
        UserPrivilegeStatusType userPrivilegeStatusType;
        for (OLEUserPrivilege oleUserPrivilege : oleUserPrivilegeList) {
            userPrivilege = new UserPrivilege();
            userPrivilegeStatus = new UserPrivilegeStatus();
            AgencyUserPrivilegeType agencyUserPrivilegeType = new AgencyUserPrivilegeType("", oleUserPrivilege.getUserPrivilegeType());
            userPrivilegeStatusType = new UserPrivilegeStatusType("", oleUserPrivilege.getUserPrivilegeStatus());
            userPrivilegeStatus.setUserPrivilegeStatusType(userPrivilegeStatusType);
            userPrivilege.setAgencyUserPrivilegeType(agencyUserPrivilegeType);
            userPrivilege.setUserPrivilegeDescription(oleUserPrivilege.getUserPrivilegeDescription());
            userPrivilege.setAgencyId(agencyId);
            userPrivilege.setUserPrivilegeStatus(userPrivilegeStatus);
            userPrivilegeList.add(userPrivilege);
        }
        if (lookupUserResponseData.getUserOptionalFields() == null) {
            lookupUserResponseData.setUserOptionalFields(new UserOptionalFields());
        }
        if (initData.getUserAddressInformationDesired()) {
            userOptionalFields.setUserAddressInformations(retrieveAddress(oleLookupUser));
        }
        if (initData.getUserPrivilegeDesired()) {
            userOptionalFields.setUserPrivileges(userPrivilegeList);
        }
        if (initData.getNameInformationDesired()) {
            userOptionalFields.setNameInformation(ni);
        }
        lookupUserResponseData.setUserOptionalFields(userOptionalFields);
        return lookupUserResponseData;
    }

    private ArrayList<UserAddressInformation> retrieveAddress(OLELookupUser oleLookupUser) {
        ArrayList<UserAddressInformation> userAddressInformationList = new ArrayList<UserAddressInformation>();
        if (oleLookupUser.getPatronAddress() != null) {
            userAddressInformationList.add(retrievePhysicalAddress(oleLookupUser));
        }
        if (oleLookupUser.getPatronEmail() != null) {
            userAddressInformationList.add(retrieveElectronicAddress(oleLookupUser));
        }
        if (oleLookupUser.getPatronPhone() != null) {
            userAddressInformationList.add(retrieveTelephoneNumber(oleLookupUser));
        }
        return userAddressInformationList;
    }

    private UserAddressInformation retrievePhysicalAddress(OLELookupUser oleLookupUser) {
        UserAddressInformation userAddressInformation = new UserAddressInformation();
        PhysicalAddress physicalAddress = new PhysicalAddress();
        StructuredAddress structuredAddress = new StructuredAddress();
        structuredAddress.setLine1(oleLookupUser.getPatronAddress().getLine1());
        structuredAddress.setLine2(oleLookupUser.getPatronAddress().getLine2());
        structuredAddress.setPostalCode(oleLookupUser.getPatronAddress().getPostalCode());
        structuredAddress.setCountry(oleLookupUser.getPatronAddress().getCountryCode());
        structuredAddress.setRegion(oleLookupUser.getPatronAddress().getStateProvinceCode());
        structuredAddress.setLocality(oleLookupUser.getPatronAddress().getCity());
        physicalAddress.setStructuredAddress(structuredAddress);
        UserAddressRoleType userAddressRoleType = new UserAddressRoleType(oleLookupUser.getPatronAddress().getAddressTypeCode());
        userAddressInformation.setUserAddressRoleType(userAddressRoleType);
        PhysicalAddressType physicalAddressType = new PhysicalAddressType(OLENCIPConstants.ADDRESS_TYPE_SCHEME, oleLookupUser.getPatronAddress().getAddressTypeCode());
        physicalAddress.setPhysicalAddressType(physicalAddressType);
        userAddressInformation.setPhysicalAddress(physicalAddress);
        return userAddressInformation;
    }

    private UserAddressInformation retrieveElectronicAddress(OLELookupUser oleLookupUser) {
        ElectronicAddress electronicAddress = new ElectronicAddress();
        electronicAddress.setElectronicAddressData(oleLookupUser.getPatronEmail().getEmailAddress());
        ElectronicAddressType electronicAddressType = new ElectronicAddressType(getOleSelectDocumentService().getSelectParameterValue(OLENCIPConstants.EMAIL));
        electronicAddress.setElectronicAddressType(electronicAddressType);
        UserAddressInformation userAddressInformation = new UserAddressInformation();
        UserAddressRoleType userAddressRoleType = new UserAddressRoleType(oleLookupUser.getPatronEmail().getEmailTypeCode());
        userAddressInformation.setUserAddressRoleType(userAddressRoleType);
        userAddressInformation.setElectronicAddress(electronicAddress);
        return userAddressInformation;
    }

    private UserAddressInformation retrieveTelephoneNumber(OLELookupUser oleLookupUser) {
        if (oleLookupUser.getPatronPhone() != null) {
            ElectronicAddress phone = new ElectronicAddress();
            phone.setElectronicAddressData(oleLookupUser.getPatronPhone().getPhoneNumber());
            phone.setElectronicAddressType(new ElectronicAddressType(OLENCIPConstants.TELEPHONE_CODE));
            UserAddressInformation uai = new UserAddressInformation();
            uai.setUserAddressRoleType(new UserAddressRoleType(oleLookupUser.getPatronPhone().getPhoneTypeCode()));
            uai.setElectronicAddress(phone);
            return uai;
        } else return null;
    }

    private List<LoanedItem> getLoanedItems(OLECheckedOutItems oleCheckedOutItems, AgencyId agencyId) {
        LoanedItem loanedItem;
        List<LoanedItem> loanedItems = new ArrayList<LoanedItem>();
        if (oleCheckedOutItems != null && oleCheckedOutItems.getCheckedOutItems() != null) {
            for (OLECheckedOutItem oleCheckedOutItems1 : oleCheckedOutItems.getCheckedOutItems()) {
                loanedItem = new LoanedItem();
                ItemId itemId = new ItemId();
                itemId.setAgencyId(agencyId);
                ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, oleCheckedOutItems1.getItemType());
                itemId.setItemIdentifierType(itemIdentifierType);
                itemId.setItemIdentifierValue(oleCheckedOutItems1.getItemId());
                loanedItem.setItemId(itemId);
                loanedItem.setReminderLevel(new BigDecimal(oleCheckedOutItems1.getNumberOfOverdueSent()));
                Amount amount = new Amount();
                CurrencyCode currencyCode = new CurrencyCode(OLENCIPConstants.USD, 1);
                amount.setCurrencyCode(currencyCode);
                amount.setMonetaryValue(new BigDecimal(0));
                loanedItem.setAmount(amount);
                List<BibliographicId> bibliographicIds = new ArrayList<BibliographicId>();
                BibliographicId bibliographicId = new BibliographicId();
                BibliographicItemId bibliographicItemId = new BibliographicItemId();
                bibliographicItemId.setBibliographicItemIdentifier(oleCheckedOutItems1.getItemId());
                BibliographicRecordId bibliographicRecordId = new BibliographicRecordId();
                bibliographicRecordId.setAgencyId(agencyId);
                bibliographicRecordId.setBibliographicRecordIdentifier(oleCheckedOutItems1.getCatalogueId());
                bibliographicId.setBibliographicRecordId(bibliographicRecordId);
                bibliographicId.setBibliographicItemId(bibliographicItemId);
                bibliographicIds.add(bibliographicId);
                loanedItem.setBibliographicIds(bibliographicIds);
                loanedItem.setDateDue(oleCirculationHelperService.getGregorianCalendarDate(oleCheckedOutItems1.getDueDate()));
                loanedItem.setTitle(oleCheckedOutItems1.getTitle());
                MediumType mediumType = new MediumType(OLENCIPConstants.MEDIUM_TYPE, oleCheckedOutItems1.getItemType());
                loanedItem.setMediumType(mediumType);
               // loanedItem.setRenewalCount(new BigDecimal(oleCheckedOutItems1.getNumberOfRenewals()));
                loanedItem.setDateCheckedOut(oleCirculationHelperService.getGregorianCalendarDate(oleCheckedOutItems1.getLoanDate()));
                loanedItems.add(loanedItem);
            }
        }
        return loanedItems;
    }


    private List<RequestedItem> getRequestedItems(OLEHolds oleHolds, AgencyId agencyId) {
        RequestedItem requestedItem;
        List<RequestedItem> requestedItems = new ArrayList<RequestedItem>();
        if (oleHolds != null && oleHolds.getOleHoldList() != null) {
            for (OLEHold oleHold : oleHolds.getOleHoldList()) {
                requestedItem = new RequestedItem();
                requestedItem.setHoldQueuePosition(new BigDecimal(oleHold.getPriority()));
                ItemId itemId = new ItemId();
                itemId.setItemIdentifierValue(oleHold.getItemId());
                ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.IDENTIFIER_TYPE, oleHold.getItemType());
                itemId.setItemIdentifierType(itemIdentifierType);
                requestedItem.setItemId(itemId);
                requestedItem.setItemId(itemId);
                RequestId requestId = new RequestId();
                requestId.setAgencyId(agencyId);
                requestId.setRequestIdentifierValue(oleHold.getRequestId());
                requestedItem.setRequestId(requestId);
                RequestType requestType = new RequestType(OLENCIPConstants.REQUEST_TYPES, oleHold.getRequestType());
                requestedItem.setRequestType(requestType);
                RequestStatusType requestStatusType = new RequestStatusType("");
                requestedItem.setRequestStatusType(requestStatusType);
                requestedItem.setDatePlaced(oleCirculationHelperService.getGregorianCalendarDate(oleHold.getCreateDate()));
                requestedItem.setPickupDate(oleCirculationHelperService.getGregorianCalendarDate(oleHold.getAvailableDate()));
                PickupLocation pickupLocation = new PickupLocation(oleHold.getPickupLocation());
                requestedItem.setPickupLocation(pickupLocation);
                requestedItem.setTitle(oleHold.getTitle());
                requestedItems.add(requestedItem);
            }

        }
        return requestedItems;
    }

    private List<UserFiscalAccount> getUserFiscalAccounts(OLEItemFines oleItemFines, AgencyId agencyId) {
        UserFiscalAccount userFiscalAccount;
        List<UserFiscalAccount> userFiscalAccounts = new ArrayList<UserFiscalAccount>();

        if (oleItemFines != null && oleItemFines.getOleItemFineList() != null) {
            for (OLEItemFine oleItemFine : oleItemFines.getOleItemFineList()) {
                userFiscalAccount = new UserFiscalAccount();
                AccountBalance accountBalance = new AccountBalance();
                CurrencyCode currencyCode = new CurrencyCode(OLENCIPConstants.USD, 1);
                accountBalance.setCurrencyCode(currencyCode);
                accountBalance.setMonetaryValue(new BigDecimal(oleItemFine.getBalance().intValue()));
                userFiscalAccount.setAccountBalance(accountBalance);
                List<AccountDetails> accountDetailsList = new ArrayList<AccountDetails>();
                AccountDetails accountDetails = new AccountDetails();
                accountDetails.setAccrualDate(oleCirculationHelperService.getGregorianCalendarDate(oleItemFine.getDateCharged()));
                FiscalTransactionInformation fiscalTransactionInformation = new FiscalTransactionInformation();
                Amount amount = new Amount();
                amount.setCurrencyCode(currencyCode);
                amount.setMonetaryValue(new BigDecimal(oleItemFine.getAmount().intValue()));
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
        return userFiscalAccounts;
    }


    public OleSelectDocumentService getOleSelectDocumentService() {
        if(oleSelectDocumentService == null){
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        this.oleSelectDocumentService = oleSelectDocumentService;
    }



}
