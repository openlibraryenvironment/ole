package org.kuali.ole.ncip.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.bo.*;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.deliver.util.NoticeInfo;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.ncip.service.LookupUserService;
import org.kuali.ole.ncip.util.OLENCIPUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenchulakshmig on 9/22/15.
 */
public abstract class LookupUserServiceImpl extends LookupUserServiceUtil implements LookupUserService {

    private static final Logger LOG = Logger.getLogger(LookupUserServiceImpl.class);

    protected String responseFormatType;
    protected String response;
    private OLELookupUser oleLookupUser;
    private OlePatronDocument olePatronDocument;

    public OLELookupUser getOleLookupUser() {
        return oleLookupUser;
    }

    public void setOleLookupUser(OLELookupUser oleLookupUser) {
        this.oleLookupUser = oleLookupUser;
    }

    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    /**
     * used for NCIP, VuFind, SIP2(3M)
     *
     * @param lookupUserParameters
     * @return
     */
    public String lookupUser(Map lookupUserParameters) {
        setResponseFormatType(lookupUserParameters);
        setOleLookupUser(new OLELookupUser());
        setOlePatronDocument(null);

        boolean isValid = validate(lookupUserParameters);
        if (!isValid) {
            return prepareResponse();
        }
        preProcess(lookupUserParameters);
        process();

        return prepareResponse();
    }

    private void setResponseFormatType(Map lookupUserParameters) {
        responseFormatType = (String) lookupUserParameters.get("responseFormatType");
        if (responseFormatType == null) {
            responseFormatType = "xml";
        }
        responseFormatType = responseFormatType.toUpperCase();
    }

    private boolean validate(Map lookupUserParameters) {
        String operatorId = getOperatorId((String) lookupUserParameters.get("operatorId"));
        String patronBarcode = (String) lookupUserParameters.get("patronBarcode");

        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCircDeskForOpertorId(operatorId);
        if (null == oleCirculationDesk) {
            processErrorResponseForOperator();
            return false;
        }
        try {
            OlePatronDocument patronDocument = getOlePatronRecordUtil().getPatronRecordByBarcode(patronBarcode);
            setOlePatronDocument(patronDocument);
            getOleLookupUser().setPatronId(getOlePatronDocument().getBarcode());
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
        if (null == getOlePatronDocument()) {
            processErrorResponseForPatron();
            return false;
        }
        return true;
    }

    private void process() {
        if (userAddressInformationDesired()) {
            processUserAddressInformation();
        }
        if (userPrivilegeDesired()) {
            processUserPriv();
        }
        if (nameInformationDesired()) {
            processNameInformation();
        }
        validatePatron();
        if (loanedItemsDesired()) {
            processLoanedItems();
        }
        if (requestedItemsDesired()) {
            processRequestedItems();
        }
        if (userFiscalAccountDesired()) {
            processFines();
        }
        processSuccessResponseForLookupUser();
    }

    private void processUserAddressInformation() {
        OlePatronEmailBo olePatronEmailBo = getDefaultEmailBo(getOlePatronDocument().getOlePatronId());
        if (olePatronEmailBo != null) {
            getOleLookupUser().setPatronEmail(olePatronEmailBo);
        }
        OlePatronAddressBo olePatronAddressBo = getDefaultAddressBo(getOlePatronDocument().getOlePatronId());
        if (olePatronAddressBo != null) {
            getOleLookupUser().setPatronAddress(olePatronAddressBo);
        }
        OlePatronPhoneBo olePatronPhoneBo = getDefaultPhoneBo(getOlePatronDocument().getOlePatronId());
        if (olePatronPhoneBo != null) {
            getOleLookupUser().setPatronPhone(olePatronPhoneBo);
        }
    }

    private OlePatronEmailBo getDefaultEmailBo(String entityId) {
        LOG.info("Inside getDefaultEmailBo : Entity Id : " + entityId);
        EntityBo entityBo = getEntity(entityId);
        OlePatronEmailBo olePatronEmailBo = null;
        if (entityBo != null) {
            if (entityBo.getEntityTypeContactInfos() != null && entityBo.getEntityTypeContactInfos().size() > 0)
                if (entityBo.getEntityTypeContactInfos().get(0).getEmailAddresses() != null && entityBo.getEntityTypeContactInfos().get(0).getEmailAddresses().size() > 0) {
                    for (EntityEmailBo entityEmailBo : entityBo.getEntityTypeContactInfos().get(0).getEmailAddresses()) {
                        if (entityEmailBo.getDefaultValue()) {
                            olePatronEmailBo = new OlePatronEmailBo();
                            olePatronEmailBo.setEmailTypeCode(entityEmailBo.getEmailTypeCode());
                            olePatronEmailBo.setEmailAddress(entityEmailBo.getEmailAddress());
                            return olePatronEmailBo;
                        }
                    }
                }
        }
        return null;
    }

    private OlePatronAddressBo getDefaultAddressBo(String entityId) {
        LOG.info("Inside getDefaultAddressBo : Entity Id : " + entityId);
        EntityBo entityBo = getEntity(entityId);
        EntityAddressBo entityAddressBo = null;
        OlePatronAddressBo olePatronAddressBo = null;
        if (entityBo != null) {
            if (entityBo.getEntityTypeContactInfos() != null && entityBo.getEntityTypeContactInfos().size() > 0)
                if (entityBo.getEntityTypeContactInfos().get(0).getDefaultAddress() != null) {
                    entityAddressBo = entityBo.getEntityTypeContactInfos().get(0).getDefaultAddress();
                    olePatronAddressBo = new OlePatronAddressBo();
                    olePatronAddressBo.setAddressTypeCode(entityAddressBo.getAddressTypeCode());
                    olePatronAddressBo.setCity(entityAddressBo.getCity());
                    olePatronAddressBo.setCountryCode(entityAddressBo.getCountryCode());
                    olePatronAddressBo.setLine1(entityAddressBo.getLine1());
                    olePatronAddressBo.setLine2(entityAddressBo.getLine2());
                    olePatronAddressBo.setLine3(entityAddressBo.getLine3());
                    olePatronAddressBo.setPostalCode(entityAddressBo.getPostalCode());
                    olePatronAddressBo.setStateProvinceCode(entityAddressBo.getStateProvinceCode());
                    return olePatronAddressBo;
                }
        }
        return null;
    }

    private void processUserPriv() {
        List<OLEUserPrivilege> userPrivilegeList = getPatronPrivileges();
        if (CollectionUtils.isNotEmpty(userPrivilegeList)) {
            getOleLookupUser().setOleUserPrivileges(userPrivilegeList);
        }
    }

    private void processNameInformation() {
        OlePatronNameBo olePatronNameBo = getEntityNameBo(getOlePatronDocument().getOlePatronId());
        if (olePatronNameBo != null) {
            getOleLookupUser().setPatronName(olePatronNameBo);
        }
    }

    private OlePatronNameBo getEntityNameBo(String entityId) {
        LOG.info("Inside getEntityNameBo : Entity Id : " + entityId);
        EntityBo entityBo = getEntity(entityId);
        EntityNameBo entityNameBo = null;
        OlePatronNameBo olePatronNameBo = null;
        if (entityBo != null) {
            if (entityBo.getNames() != null && entityBo.getNames().size() > 0) {
                entityNameBo = entityBo.getNames().get(0);
                olePatronNameBo = new OlePatronNameBo();
                olePatronNameBo.setFirstName(entityNameBo.getFirstName());
                olePatronNameBo.setMiddleName(entityNameBo.getMiddleName());
                olePatronNameBo.setLastName(entityNameBo.getLastName());
                return olePatronNameBo;
            }
        }
        return null;
    }

    private void processLoanedItems() {
        OLECheckedOutItems oleCheckedOutItems = getCheckedOutItems();
        getOleLookupUser().setOleCheckedOutItems(oleCheckedOutItems);
    }

    private void processRequestedItems() {
        List<OleDeliverRequestBo> oleDeliverRequestBoList = getOlePatronDocument().getOleDeliverRequestBos();
        OLEHolds oleHolds = getHoldsList(oleDeliverRequestBoList);
        getOleLookupUser().setOleHolds(oleHolds);
    }

    private void processFines() {
        OLEItemFines oleItemFines = getFines(getOlePatronDocument().getOlePatronId());
        getOleLookupUser().setOleItemFines(oleItemFines);
    }

    private OlePatronPhoneBo getDefaultPhoneBo(String entityId) {
        LOG.info("Inside getDefaultPhoneBo : Entity Id : " + entityId);
        EntityBo entityBo = getEntity(entityId);
        EntityPhoneBo entityPhoneBo = null;
        OlePatronPhoneBo olePatronPhoneBo = null;
        if (entityBo != null) {
            if (entityBo.getEntityTypeContactInfos().get(0) != null && entityBo.getEntityTypeContactInfos().size() > 0) {
                if (entityBo.getEntityTypeContactInfos().get(0).getDefaultPhoneNumber() != null) {
                    entityPhoneBo = entityBo.getEntityTypeContactInfos().get(0).getDefaultPhoneNumber();
                    olePatronPhoneBo = new OlePatronPhoneBo();
                    olePatronPhoneBo.setPhoneTypeCode(entityPhoneBo.getPhoneTypeCode());
                    olePatronPhoneBo.setPhoneNumber(entityPhoneBo.getPhoneNumber());
                    return olePatronPhoneBo;
                }
            }
        }
        return null;
    }

    private List<OLEUserPrivilege> getPatronPrivileges() {
        List<OLEUserPrivilege> userPrivilegeList = new ArrayList<>();
        try {
        OLEUserPrivilege courtesyNoticePriv = getPrivilege(String.valueOf(getOlePatronDocument().isCourtesyNotice()), OLEConstants.COURTESY_NOTICE, OLEConstants.COURTESY_DESCRIPTION);
        userPrivilegeList.add(courtesyNoticePriv);

        OLEUserPrivilege deliveryPriv = getPrivilege(String.valueOf(getOlePatronDocument().isDeliveryPrivilege()), OLEConstants.DELIVERY, OLEConstants.DELIVERY_DESCRIPTION);
        userPrivilegeList.add(deliveryPriv);

        OLEUserPrivilege pagingPriv = getPrivilege(String.valueOf(getOlePatronDocument().isPagingPrivilege()), OLEConstants.PAGING, OLEConstants.PAGING_DESCRIPTION);
        userPrivilegeList.add(pagingPriv);

        OLEUserPrivilege profilePriv = getPrivilege(getOlePatronDocument().getBorrowerTypeName(), OLEConstants.PROFILE, OLEConstants.PROFILE_DESCRIPTION);
        userPrivilegeList.add(profilePriv);

        OLEUserPrivilege statusPriv = getStatusPrivilege();
        if (statusPriv != null) {
            userPrivilegeList.add(statusPriv);
        }
        } catch (Exception e) {
            LOG.error("Exception while getting user privilages " + e);
        }
        return userPrivilegeList;
    }

    protected OLEUserPrivilege getPrivilege(String userPrivilegeStatus, String userPrivilegeType, String userPrivilegeDescription) {
        OLEUserPrivilege oleUserPrivilege = new OLEUserPrivilege();
        oleUserPrivilege.setUserPrivilegeType(userPrivilegeType);
        oleUserPrivilege.setUserPrivilegeDescription(userPrivilegeDescription);
        oleUserPrivilege.setUserPrivilegeStatus(userPrivilegeStatus);
        return oleUserPrivilege;
    }

    private EntityBo getEntity(String entityId) {
        LOG.info("Inside getEntity : Entity Id : " + entityId);
        Map<String, String> entityMap = new HashMap<>();
        entityMap.put(OLEConstants.ID, entityId);
        List<EntityBo> entityBoList = (List<EntityBo>) getBusinessObjectService().findMatching(EntityBo.class, entityMap);
        if (entityBoList.size() > 0)
            return entityBoList.get(0);
        return null;
    }

    private OLECheckedOutItems getCheckedOutItems() {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        List<OLECheckedOutItem> oleCheckedOutItemList = new ArrayList<>();
        List<OleLoanDocument> oleLoanDocumentList = new ArrayList<>();
        try {
            oleLoanDocumentList = getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr(getOlePatronDocument().getOlePatronId(), null);
        } catch (Exception e) {
            LOG.error("Exception while getting loaned items " + e);
        }
        if (CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
            SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
            boolean renewInfoNeeded = isRenewalInfoNeeded();
            for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
                OLECheckedOutItem oleCheckedOutItem = new OLECheckedOutItem();
                oleCheckedOutItem.setCopyNumber(oleLoanDocument.getItemCopyNumber());
                oleCheckedOutItem.setVolumeNumber(oleLoanDocument.getEnumeration());
                oleCheckedOutItem.setAcquiredFine("");
                oleCheckedOutItem.setDateRecalled("");
                oleCheckedOutItem.setTitle(oleLoanDocument.getTitle());
                oleCheckedOutItem.setAuthor(oleLoanDocument.getAuthor());
                oleCheckedOutItem.setCallNumber(oleLoanDocument.getItemCallNumber());
                oleCheckedOutItem.setCatalogueId(oleLoanDocument.getBibUuid());
                if (oleLoanDocument.getLoanDueDate() != null) {
                    oleCheckedOutItem.setDueDate(oleLoanDocument.getLoanDueDate().toString());
                    if ((fmt.format(oleLoanDocument.getLoanDueDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) > 0) {
                        oleCheckedOutItem.setOverDue(false);
                    } else {
                        oleCheckedOutItem.setOverDue(true);
                    }
                } else {
                    oleCheckedOutItem.setDueDate((new Timestamp(new Date(2025, 1, 1).getTime()).toString()));
                }
                if (oleLoanDocument.getRenewalLoanDueDate() != null) {
                    oleCheckedOutItem.setDateRenewed(oleLoanDocument.getRenewalLoanDueDate().toString());
                } else {
                    oleCheckedOutItem.setDateRenewed("");
                }
                oleCheckedOutItem.setItemType(oleLoanDocument.getItemType());
                if (null != oleLoanDocument.getCreateDate()) {
                    oleCheckedOutItem.setLoanDate(new Timestamp(oleLoanDocument.getCreateDate().getTime()).toString());
                }
                oleCheckedOutItem.setItemId(oleLoanDocument.getItemId());
                if (oleLoanDocument.getNoOfOverdueNoticesSentForBorrower() != null) {
                    oleCheckedOutItem.setNumberOfOverdueSent(oleLoanDocument.getNoOfOverdueNoticesSentForBorrower());
                } else {
                    oleCheckedOutItem.setNumberOfOverdueSent("1");
                }

                if (renewInfoNeeded) {
                    oleCheckedOutItem.setNumberOfRenewals(oleLoanDocument.getNumberOfRenewals());
                }
                oleCheckedOutItemList.add(oleCheckedOutItem);
            }
        }
        if (CollectionUtils.isNotEmpty(oleCheckedOutItemList)) {
            OLECheckedOutItems oleCheckedOutItems = new OLECheckedOutItems();
            oleCheckedOutItems.setCheckedOutItems(oleCheckedOutItemList);
            return oleCheckedOutItems;
        }
        oleStopWatch.end();
        LOG.info("Time taken to get  " + oleCheckedOutItemList.size() + " checked out items : " + oleStopWatch.getTotalTime());
        return null;
    }

    private OLEHolds getHoldsList(List<OleDeliverRequestBo> oleDeliverRequestBoList) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        List<OLEHold> oleHoldList = new ArrayList<>();
        try {
        if (CollectionUtils.isNotEmpty(oleDeliverRequestBoList)) {
            Map<String, OleCirculationDesk> oleCirculationDeskMap = getOleCirculationService().getAvailableCirculationDesks();
            for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
                OLEHold oleHold = new OLEHold();
                oleHold.setItemId(oleDeliverRequestBo.getItemId());
                Map<String, Object> detailMap = new OleDeliverRequestDocumentHelperServiceImpl().retrieveBIbItemHoldingData(oleDeliverRequestBo.getItemUuid());
                Bib bib = (Bib) detailMap.get(OLEConstants.BIB);
                Item item = (Item) detailMap.get(OLEConstants.ITEM);
                OleHoldings oleHoldings = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
                oleHold.setCatalogueId(bib.getId());
                oleHold.setRequestId(oleDeliverRequestBo.getRequestId());
                if (item.getItemStatus() != null) {
                    oleHold.setAvailableStatus(item.getItemStatus().getCodeValue());
                }
                String copyNumber = "";
                if (item.getCopyNumber() != null && !item.getCopyNumber().isEmpty()) {
                    copyNumber = item.getCopyNumber();
                } else {
                    copyNumber = oleHoldings.getCopyNumber() != null ? oleHoldings.getCopyNumber() : "";
                }
                oleHold.setCopyNumber(copyNumber);
                oleHold.setTitle(bib.getTitle());
                oleHold.setAuthor(bib.getAuthor());
                oleHold.setVolumeNumber(item.getEnumeration() != null ? item.getEnumeration() : "");
                try {
                    oleHold.setCallNumber(getOleLoanDocumentsFromSolrBuilder().getItemCallNumber(item.getCallNumber(), oleHoldings.getCallNumber()));
                } catch (Exception e) {
                    LOG.error("Exception while getting call number " + e);
                }
                if (item.getItemType() != null)
                    oleHold.setItemType(item.getItemType().getCodeValue());
                if (oleDeliverRequestBo.getRequestTypeId() != null && !oleDeliverRequestBo.getRequestTypeId().isEmpty()) {
                    if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2")) {
                        oleHold.setRecallStatus(OLEConstants.YES);
                    } else {
                        oleHold.setRecallStatus(OLEConstants.NO);
                    }
                }
                if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                    oleHold.setRequestExpiryDate(oleDeliverRequestBo.getRequestExpiryDate().toString());
                }
                if (oleDeliverRequestBo.getHoldExpirationDate() != null) {
                    oleHold.setHoldExpiryDate(oleDeliverRequestBo.getHoldExpirationDate().toString());
                }
                if (oleDeliverRequestBo.getCreateDate() != null) {
                    oleHold.setCreateDate(oleDeliverRequestBo.getCreateDate().toString());
                }
                if (oleDeliverRequestBo.getBorrowerQueuePosition() != null) {
                    oleHold.setPriority(oleDeliverRequestBo.getBorrowerQueuePosition().toString());
                }
                    if (oleDeliverRequestBo.getPickUpLocationId() != null) {
                oleHold.setPickupLocation(oleCirculationDeskMap.get(oleDeliverRequestBo.getPickUpLocationId()).getCirculationDeskCode());
                    }
                if (oleDeliverRequestBo.getRecallDueDate() != null) {
                    oleHold.setDateRecalled(oleDeliverRequestBo.getRecallDueDate().toString());
                }
                if (oleDeliverRequestBo.getRecallDueDate() != null) {
                    oleHold.setDateRecalled(oleDeliverRequestBo.getRecallDueDate().toString());
                }
                oleHold.setRequestType(oleDeliverRequestBo.getRequestTypeCode());
                Map<String, String> loanMap = new HashMap<>();
                loanMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, oleDeliverRequestBo.getItemId());
                List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
                if (CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
                    OleLoanDocument oleLoanDocument = oleLoanDocumentList.get(0);
                    if (oleLoanDocument.getLoanDueDate() != null) {
                        String[] availableDates = oleLoanDocument.getLoanDueDate().toString().split(" ");
                        if (availableDates != null && availableDates.length > 0) {
                            oleHold.setAvailableDate(availableDates[0]);
                        } else {
                            oleHold.setAvailableDate(oleLoanDocument.getLoanDueDate().toString());
                        }
                        if (oleDeliverRequestBo.getPickUpLocationId() != null) {
                            if (oleCirculationDeskMap.size() > 0 && oleCirculationDeskMap.get(oleDeliverRequestBo.getPickUpLocationId()) != null) {
                                oleHold.setDateAvailableExpires(addDate(new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime()), Integer.parseInt(oleCirculationDeskMap.get(oleDeliverRequestBo.getPickUpLocationId()).getOnHoldDays())).toString());
                            }
                        }
                    } else {
                        oleHold.setAvailableDate(OLEConstants.INDEFINITE);
                        oleHold.setDateAvailableExpires(OLEConstants.INDEFINITE);
                    }
                }
                if (oleDeliverRequestBo.getRequestTypeId().equals("2") || oleDeliverRequestBo.getRequestTypeId().equals("4") || oleDeliverRequestBo.getRequestTypeId().equals("6")) {
                    oleHold.setReserve(true);
                } else {
                    oleHold.setReserve(false);
                }
                oleHoldList.add(oleHold);
            }
            if (CollectionUtils.isNotEmpty(oleHoldList)) {
                OLEHolds oleHolds = new OLEHolds();
                oleHolds.setOleHoldList(oleHoldList);
                return oleHolds;
            }
        }
        } catch (Exception e) {
            LOG.error("Exception while processing requested records" + e);
        }
        oleStopWatch.end();
        LOG.info("For " + oleHoldList.size() + " hold items, time taken : " + oleStopWatch.getTotalTime());
        return null;
    }

    private java.sql.Date addDate(java.sql.Date in, int daysToAdd) {
        if (in == null) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(in);
        cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return new java.sql.Date(cal.getTime().getTime());
    }

    private OLEItemFines getFines(String olePatronId) {
        List<OLEItemFine> oleItemFineList = new ArrayList<>();
        OLEItemFines oleItemFines = new OLEItemFines();
        try {
        Map map = new HashMap();
        map.put(OLEConstants.PATRON_ID, olePatronId);
        List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) getBusinessObjectService().findMatching(PatronBillPayment.class, map);
        if (CollectionUtils.isNotEmpty(patronBillPaymentList)) {
            for (PatronBillPayment olePatronBillPayment : patronBillPaymentList) {
                List<FeeType> feeTypeList = olePatronBillPayment.getFeeType();
                for (FeeType feeType : feeTypeList) {
                    OLEItemFine oleItemFine = new OLEItemFine();
                    if (feeType.getItemBarcode() != null) {
                        OleItemSearch oleItemSearch = new OLENCIPUtil().getOleItemSearch(feeType.getItemBarcode());
                        oleItemFine.setItemId(feeType.getItemBarcode());
                        oleItemFine.setCatalogueId(oleItemSearch.getBibUUID());
                        oleItemFine.setTitle(oleItemSearch.getTitle());
                        oleItemFine.setAuthor(oleItemSearch.getAuthor());
                    }
                    oleItemFine.setPatronBillId(olePatronBillPayment.getBillNumber());
                    oleItemFine.setAmount((feeType.getFeeAmount() != null ? feeType.getFeeAmount().bigDecimalValue() : OLEConstants.BIGDECIMAL_DEF_VALUE));
                    oleItemFine.setBalance((feeType.getBalFeeAmount() != null ? feeType.getBalFeeAmount().bigDecimalValue() : OLEConstants.BIGDECIMAL_DEF_VALUE));
                    oleItemFine.setBillDate(feeType.getBillDate().toString());
                    int noOfPayment = feeType.getItemLevelBillPaymentList().size();
                    oleItemFine.setNoOfPayments(new Integer(noOfPayment).toString());
                    if (feeType.getOleFeeType() != null) {
                        oleItemFine.setReason(feeType.getOleFeeType().getFeeTypeName());
                        oleItemFine.setFeeType(feeType.getOleFeeType().getFeeTypeCode());
                    } else {
                        oleItemFine.setReason(feeType.getFeeType());
                        oleItemFine.setFeeType(feeType.getFeeType());
                    }
                    oleItemFine.setDateCharged(feeType.getBillDate().toString());
                    oleItemFineList.add(oleItemFine);
                }
            }
            oleItemFines.setOleItemFineList(oleItemFineList);
            processSuccessResponseForItemFine(oleItemFines);
        } else {
            processInfoForItemFine(oleItemFines);
            }
        } catch (Exception e) {
            LOG.error("Exception while proceesing fines" + e);
        }
        return oleItemFines;
    }

    protected abstract String prepareResponse();

    protected abstract String getOperatorId(String operatorId);

    protected abstract void validatePatron();

    protected abstract boolean isRenewalInfoNeeded();

    protected abstract OLEUserPrivilege getStatusPrivilege();

    protected abstract void processErrorResponseForOperator();

    protected abstract void processErrorResponseForPatron();

    protected abstract void processSuccessResponseForLookupUser();

    protected abstract void processSuccessResponseForItemFine(OLEItemFines oleItemFines);

    protected abstract void processInfoForItemFine(OLEItemFines oleItemFines);

    protected abstract boolean nameInformationDesired();

    protected abstract boolean userAddressInformationDesired();

    protected abstract boolean userPrivilegeDesired();

    protected abstract boolean loanedItemsDesired();

    protected abstract boolean requestedItemsDesired();

    protected abstract boolean userFiscalAccountDesired();

    protected abstract void preProcess(Map lookupUserParameters);

}
