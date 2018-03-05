package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.PatronBillGenerator;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarWeek;
import org.kuali.ole.deliver.controller.drools.RuleExecutor;
import org.kuali.ole.deliver.controller.notices.*;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeFieldLabelMapping;
import org.kuali.ole.deliver.notice.executors.LoanNoticesExecutor;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.service.*;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.LocationsCheckinCountRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pvsubrah on 6/4/15.
 */
public class CircUtilController extends RuleExecutor {
    private static final Logger LOG = Logger.getLogger(CircUtilController.class);
    private BusinessObjectService businessObjectService;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private DocstoreClientLocator docstoreClientLocator;
    private SimpleDateFormat dateFormatForDocstoreDueDate;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;
    private ParameterValueResolver parameterValueResolver;

    public List<OLEDeliverNotice> processNotices(OleLoanDocument currentLoanDocument, ItemRecord itemRecord, NoticeInfo noticeInfo) {
        List<OLEDeliverNotice> deliverNotices = new ArrayList<>();

        List<NoticeDueDateProcessor> noticeProcessors = getNoticeProcessors();

        if(null == noticeInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("circPolicyId", currentLoanDocument.getCirculationPolicyId());
        List<OleNoticeTypeConfiguration> oleNoticeTypeConfigurations =
                (List<OleNoticeTypeConfiguration>) getBusinessObjectService().findMatching(OleNoticeTypeConfiguration.class, map);

        OleNoticeTypeConfiguration oleNoticeTypeConfiguration = null;
        if (CollectionUtils.isNotEmpty(oleNoticeTypeConfigurations)) {
            oleNoticeTypeConfiguration = oleNoticeTypeConfigurations.get(0);
                noticeInfo = new NoticeInfo();
            noticeInfo.setNoticeType(oleNoticeTypeConfiguration.getNoticeType());
            } else {
                LOG.error("No notice coniguration mapping was found for the circulation policy id: " + currentLoanDocument.getCirculationLocationId());
            }
        }
        if (null != noticeInfo && StringUtils.isNotBlank(noticeInfo.getNoticeType())) {
            itemRecord.setDueDateTime(currentLoanDocument.getLoanDueDate());
            ArrayList<Object> facts = new ArrayList<>();
            facts.add(noticeInfo);
            facts.add(itemRecord);
            fireRules(facts, null, "notice generation");


            Map<String, Map<String, Object>> noticeInfoForTypeMap = noticeInfo.getNoticeInfoForTypeMap();

            if (null != noticeInfoForTypeMap) {
                for (Iterator<String> iterator = noticeInfoForTypeMap.keySet().iterator(); iterator.hasNext(); ) {
                    String noticeType = iterator.next();
                    for (Iterator<NoticeDueDateProcessor> objectIterator = noticeProcessors.iterator(); objectIterator.hasNext(); ) {
                        NoticeDueDateProcessor noticeProcessor = objectIterator.next();
                        if (noticeProcessor.isInterested(noticeType)) {
                            List<OLEDeliverNotice> oleDeliverNotices = noticeProcessor.generateNotices(noticeInfo, currentLoanDocument);
                            if (CollectionUtils.isNotEmpty(oleDeliverNotices)) {
                                deliverNotices.addAll(oleDeliverNotices);
                            }
                        }
                    }
                }
            }

        } else {
            LOG.error("No notice coniguration mapping was found for the circulation policy id: " + currentLoanDocument.getCirculationLocationId());
        }

        return deliverNotices;
    }

    public ItemRecord getItemRecordByBarcode(String itemBarcode) {
        return ItemInfoUtil.getInstance().getItemRecordByBarcode(itemBarcode);
    }

    public String convertDateToString(Date date, String format) {
        LOG.info("Date Format : " + format + "Date : " + date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateValue = "";
        try {
            dateValue = simpleDateFormat.format(date);
        } catch (Exception e) {
            LOG.error(e, e);
        }
        LOG.info("Formatted Date : " + dateValue);
        return dateValue;
    }

    public String convertToString(Timestamp date) {
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date itemDate = null;
        try {
            itemDate = format2.parse(date.toString());
        } catch (ParseException e) {
            LOG.error("format string to Date " + e);
        }
        return format1.format(itemDate).toString();
    }

    public String getLoginUserId() {
        return GlobalVariables.getUserSession().getPrincipalId();
    }


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public CircDeskLocationResolver getCircDeskLocationResolver() {
        return new CircDeskLocationResolver();
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = SpringContext.getBean(ItemOlemlRecordProcessor.class);
        }
        return itemOlemlRecordProcessor;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void setDateFormatForDocstoreDueDate(SimpleDateFormat dateFormatForDocstoreDueDate) {
        this.dateFormatForDocstoreDueDate = dateFormatForDocstoreDueDate;
    }

    public SimpleDateFormat getDateFormatForDocstoreDueDate() {
        if (dateFormatForDocstoreDueDate == null) {
            dateFormatForDocstoreDueDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        }
        return dateFormatForDocstoreDueDate;
    }


    public void rollBackSavedLoanRecord(String barcode) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("itemId", barcode);
        List<OleLoanDocument> oleLoanDocument = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, criteria);
        if (oleLoanDocument.size() > 0) {
            KRADServiceLocator.getBusinessObjectService().delete(oleLoanDocument.get(0));
        }
    }

    public OleLoanDocument updateLoanDocumentWithItemInformation(ItemRecord itemRecord, OleLoanDocument oleLoanDocument) {

        OleItemSearch oleItemSearch = new DocstoreUtil().getOleItemSearchListFromLocalClient(itemRecord.getItemId());
        oleLoanDocument.setTitle(oleItemSearch.getTitle());
        oleLoanDocument.setAuthor(oleItemSearch.getAuthor());
        String location = oleItemSearch.getShelvingLocation().split("/")[oleItemSearch.getShelvingLocation().split("/").length - 1];
        try {
            getOleLoanDocumentsFromSolrBuilder().getLocationBySolr(location, oleLoanDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
        oleLoanDocument.setItemCallNumber(oleItemSearch.getCallNumber());
        oleLoanDocument.setItemCopyNumber(oleItemSearch.getCopyNumber());
        oleLoanDocument.setChronology(oleItemSearch.getChronology());
        oleLoanDocument.setEnumeration(oleItemSearch.getEnumeration());
        oleLoanDocument.setItemType(oleItemSearch.getItemType());
        oleLoanDocument.setItemStatus(OLEConstants.ITEM_STATUS_CHECKEDOUT);
        oleLoanDocument.setItemUuid(itemRecord.getUniqueIdPrefix() + "-" + oleItemSearch.getItemUUID());
        oleLoanDocument.setInstanceUuid(oleItemSearch.getInstanceUUID());
        oleLoanDocument.setBibUuid(oleItemSearch.getBibUUID());
        return oleLoanDocument;
    }


    /**
     * @param map
     * @param itemUUID
     * @param updateNulls - This indicates if null values should be updated for the corosponding parameters in SOLR.
     * @return If SOLR update was successfull.
     */
    public Boolean updateItemInfoInSolr(Map map, String itemUUID, boolean updateNulls) {
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = getExistingItemFromSolr(itemUUID);
        if (oleItem != null) {
            setItemInfoForUpdate(map, oleItem, updateNulls);
            try {
                updateItem(oleItem);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public Boolean deleteItemInfoInSolr(Map map, String itemUUID) {
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = getExistingItemFromSolr(itemUUID);
        if (oleItem != null) {
            setItemInfoForDelete(map, oleItem);
            try {
                updateItem(oleItem);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private void setItemInfoForDelete(Map map, org.kuali.ole.docstore.common.document.content.instance.Item oleItem) {
        if (map.keySet().contains("checkinNote"))
            oleItem.setCheckinNote("");
        if (map.containsKey("deleteClaimsReturn")) {
            oleItem.setClaimsReturnedFlag(false);
            oleItem.setClaimsReturnedNote(null);
            oleItem.setClaimsReturnedFlagCreateDate(null);
        }
        if (map.containsKey("deleteDamagedItem")) {
            oleItem.setItemDamagedStatus(false);
            oleItem.setDamagedItemNote(null);
        }
        if (map.containsKey("deleteMissingPieceItem")) {
            oleItem.setMissingPieceFlag(false);
            oleItem.setMissingPieceFlagNote(null);
            oleItem.setMissingPieceEffectiveDate(null);
            oleItem.setMissingPiecesCount(null);
        }
    }

    private void setItemInfoForUpdate(Map map, org.kuali.ole.docstore.common.document.content.instance.Item oleItem, boolean updateNulls) {
        String patronId = null == map.get("patronId") ? null : (String) map.get("patronId");
        String proxyPatronId = null == map.get("proxyPatronId") ? null : (String) map.get("proxyPatronId");
        Date itemCheckoutDateTime = null == map.get("itemCheckoutDateTime") ? null : (Date) map.get("itemCheckoutDateTime");
        Timestamp loanDueDate = null == map.get("loanDueDate") ? null : (Timestamp) map.get("loanDueDate");
        String numRenewals = null == map.get("numRenewals") ? null : (String) map.get("numRenewals");
        String itemStatus = null == map.get("itemStatus") ? null : (String) map.get("itemStatus");
        LocationsCheckinCountRecord locationsCheckinCountRecordToBeUpdated = null == map.get("locationsCheckinCountRecordToBeUpdated") ? null : (LocationsCheckinCountRecord) map.get("locationsCheckinCountRecordToBeUpdated");

        Boolean itemClaimsReturnedFlag = null == map.get("itemClaimsReturnedFlag") ? null : (Boolean) map.get("itemClaimsReturnedFlag");
        String claimsReturnNote = null == map.get("claimsReturnNote") ? null : (String) map.get("claimsReturnNote");
        String ClaimsReturnedDate = null == map.get("ClaimsReturnedDate") ? null : (String) map.get("ClaimsReturnedDate");

        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = null == map.get("itemClaimsReturnedRecords") ? null : (List<ItemClaimsReturnedRecord>) map.get("itemClaimsReturnedRecords");

        String damagedItemNote = null == map.get("damagedItemNote") ? null : (String) map.get("damagedItemNote");
        List<ItemDamagedRecord> itemDamagedRecords = null == map.get("itemDamagedRecords") ? null : (List<ItemDamagedRecord>) map.get("itemDamagedRecords");

        Boolean missingPieceItemFlag = null == map.get("missingPieceItemFlag") ? null : (Boolean) map.get("missingPieceItemFlag");
        String missingPieceItemNote = null == map.get("missingPieceItemNote") ? null : (String) map.get("missingPieceItemNote");
        String missingPieceItemCount = null == map.get("missingPieceItemCount") ? null : (String) map.get("missingPieceItemCount");
        String noOfmissingPiece = null == map.get("noOfmissingPiece") ? null : (String) map.get("noOfmissingPiece");
        String missingPieceItemDate = null == map.get("missingPieceItemDate") ? null : (String) map.get("missingPieceItemDate");
        List<MissingPieceItemRecord> itemMissingPieceItemRecords = null == map.get("itemMissingPieceItemRecords") ? null : (List<MissingPieceItemRecord>) map.get("itemMissingPieceItemRecords");

        if (updateNulls) {
            oleItem.setCurrentBorrower(patronId);
            oleItem.setProxyBorrower(proxyPatronId);
        }
        if (null != itemCheckoutDateTime) {
            oleItem.setCheckOutDateTime(convertDateToString(itemCheckoutDateTime, "MM/dd/yyyy HH:mm:ss"));
        } else if (updateNulls) {
            oleItem.setCheckOutDateTime(null);
        }

        if (loanDueDate != null) {
            oleItem.setDueDateTime(convertToString(loanDueDate));
        } else if (updateNulls) {
            oleItem.setDueDateTime("");
        }

        if (updateNulls) {
            oleItem.setNumberOfRenew(null == numRenewals ? 0 : Integer.parseInt(numRenewals));
        }

        if (null != locationsCheckinCountRecordToBeUpdated) {
            setNumberOfCirculationsCount(oleItem, locationsCheckinCountRecordToBeUpdated);
        }

        if (null != itemStatus) {
            ItemStatus itemSt = new ItemStatus();
            itemSt.setCodeValue(itemStatus);
            itemSt.setFullValue(itemStatus);
            oleItem.setItemStatus(itemSt);
        }

        if (CollectionUtils.isNotEmpty(itemClaimsReturnedRecords)) {
            oleItem.setClaimsReturnedNote(claimsReturnNote);
            oleItem.setClaimsReturnedFlagCreateDate(ClaimsReturnedDate);
            oleItem.setClaimsReturnedFlag(itemClaimsReturnedFlag);
            oleItem.setItemClaimsReturnedRecords(itemClaimsReturnedRecords);
        }

        if (CollectionUtils.isNotEmpty(itemDamagedRecords)) {
            oleItem.setDamagedItemNote(damagedItemNote);
            oleItem.setItemDamagedStatus(true);
            oleItem.setItemDamagedRecords(itemDamagedRecords);
        }

        if (CollectionUtils.isNotEmpty(itemMissingPieceItemRecords)) {
            oleItem.setMissingPieceFlagNote(missingPieceItemNote);
            oleItem.setMissingPieceFlag(missingPieceItemFlag);
            oleItem.setMissingPiecesCount(missingPieceItemCount);
            oleItem.setNumberOfPieces(noOfmissingPiece);
            oleItem.setMissingPieceEffectiveDate(missingPieceItemDate);
            oleItem.setMissingPieceItemRecordList(itemMissingPieceItemRecords);
        }
    }

    private void setNumberOfCirculationsCount(org.kuali.ole.docstore.common.document.content.instance.Item oleItem, LocationsCheckinCountRecord locationsCheckinCountRecordToBeUpdated) {
        //TODO: We should really use the LocationsCheckinCountRecord in DocStore rather than using some intermediary pojo like CheckinLocation and then a wrapper NumberOfCirculations;
        //TODO: Docstore needs to be fixed at some point.
        NumberOfCirculations numberOfCirculations = new NumberOfCirculations();
        ArrayList<CheckInLocation> checkInLocations = new ArrayList<>();
        CheckInLocation checkInLocation = new CheckInLocation();
        checkInLocation.setCount(locationsCheckinCountRecordToBeUpdated.getLocationCount());
        checkInLocation.setInHouseCount(locationsCheckinCountRecordToBeUpdated.getLocationInhouseCount());
        checkInLocation.setName(locationsCheckinCountRecordToBeUpdated.getLocationName());
        checkInLocations.add(checkInLocation);
        numberOfCirculations.setCheckInLocation(checkInLocations);
        oleItem.setNumberOfCirculations(numberOfCirculations);
    }


    protected org.kuali.ole.docstore.common.document.content.instance.Item getExistingItemFromSolr(String itemUUID) {
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        try {
            Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID);
            return itemOlemlRecordProcessor.fromXML(item.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateItem(org.kuali.ole.docstore.common.document.content.instance.Item oleItem) throws Exception {
        try {
            String itemUuid = oleItem.getItemIdentifier();
            String itemXmlContent = buildItemContent(oleItem);
            Item item = new ItemOleml();
            item.setId(itemUuid);
            item.setContent(itemXmlContent);
            item.setCategory(OLEConstants.WORK_CATEGORY);
            item.setType(DocType.ITEM.getCode());
            item.setFormat(OLEConstants.OLEML_FORMAT);
            item.setStaffOnly(oleItem.isStaffOnlyFlag());
            getDocstoreClientLocator().getDocstoreClient().updateItem(item);
        } catch (Exception e) {
            LOG.error(OLEConstants.ITM_STS_TO_DOC_FAIL + e, e);
            throw new Exception(OLEConstants.ITM_STS_TO_DOC_FAIL);
        }
    }


    public String buildItemContent(org.kuali.ole.docstore.common.document.content.instance.Item oleItem) throws Exception {
        oleItem.setItemStatusEffectiveDate(String.valueOf(new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE).format(new Date())));
        String itemContent = getItemOlemlRecordProcessor().toXML(oleItem);
        return itemContent;
    }


    public OleLoanDocument getLoanDocumentFromListBasedOnItemUuid(String itemUuid, List<OleLoanDocument> selectedLoanDocumentList) {
        for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            if (oleLoanDocument.getItemUuid().equalsIgnoreCase(itemUuid)) {
                return oleLoanDocument;
            }
        }
        return null;
    }

    public OleLoanDocument getLoanDocumentFromListBasedOnItemBarcode(String itemBarcode, List<OleLoanDocument> selectedLoanDocumentList) {
        for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            if (oleLoanDocument.getItemId().equalsIgnoreCase(itemBarcode)) {
                return oleLoanDocument;
            }
        }
        return null;
    }


    public Item getItemForUpdate(Map<String, Object> parameterMap) {
        if (parameterMap.size() > 0) {
            Item item = null;
            try {
                item = getDocstoreClientLocator().getDocstoreClient().retrieveItem((String) parameterMap.get("itemUUID"));
                for (Iterator<String> iterator = parameterMap.keySet().iterator(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    if (key.equalsIgnoreCase("loanDueDate")) {
                        Timestamp dueDate = (Timestamp) parameterMap.get(key);
                        if (null != dueDate) {
                            item.setField(Item.DUE_DATE_TIME, getDateFormatForDocstoreDueDate().format(dueDate));
                        } else {
                            item.setField(Item.DUE_DATE_TIME, null);
                        }
                    } else if (key.equalsIgnoreCase("numRenewals")) {
                        item.setField(Item.NO_OF_RENEWAL, (String) parameterMap.get(key));
                    } else if (key.equalsIgnoreCase("itemStatus")) {
                        item.setField(Item.DESTINATION_ITEM_STATUS, (String) parameterMap.get(key));
                    }
                }
                return item;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public Timestamp processDateAndTimeForAlterDueDate(Date loanDueDateToAlter, String loanDueDateTimeToAlter) throws Exception {
        boolean timeFlag;
        Timestamp timestamp;
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);

        if (org.apache.commons.lang3.StringUtils.isNotBlank(loanDueDateTimeToAlter)) {
            String[] str = loanDueDateTimeToAlter.split(":");
            timeFlag = validateTime(loanDueDateTimeToAlter);
            if (timeFlag) {
                if (str != null && str.length <= 2) {
                    loanDueDateTimeToAlter = loanDueDateTimeToAlter + OLEConstants.CHECK_IN_TIME_MS;
                }
                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(loanDueDateToAlter).concat(" ").concat(loanDueDateTimeToAlter));
            } else {
                throw new Exception("Invalid time format");
            }
        } else if (fmt.format(loanDueDateToAlter).compareTo(fmt.format(new Date())) == 0) {
            timestamp = new Timestamp(new Date().getTime());
        } else {
            timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(loanDueDateToAlter).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
        }
        return timestamp;
    }


    public boolean validateTime(String timeString) {
        if (StringUtils.isNotBlank(timeString)) {
            Pattern pattern;
            Matcher matcher;
            pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
            matcher = pattern.matcher(timeString);
            boolean validTime = matcher.matches();
            return validTime;
        } else {
            return true;
        }
    }


    private OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();
        }
        return oleLoanDocumentsFromSolrBuilder;
    }


    public void addToFormattedContent(StringBuilder stringBuilder, String content) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("<br/>").append(content);
        } else {
            stringBuilder.append(content);
        }
    }

    public void updateNoticesForLoanDocument(OleLoanDocument oleLoanDocument) {
        ItemRecord itemRecord = getItemRecordByBarcode(oleLoanDocument.getItemId());
        List<OLEDeliverNotice> oleDeliverNotices = processNotices(oleLoanDocument, itemRecord, null);
        oleLoanDocument.setDeliverNotices(oleDeliverNotices);
    }

    private List<NoticeDueDateProcessor> getNoticeProcessors() {
        List<NoticeDueDateProcessor> noticeProcessors = new ArrayList<>();
        noticeProcessors.add(new OverDueAndLostNoticeDueDateProcessor());
        noticeProcessors.add(new RecallOverDueAndLostNoticeDueDateProcessor());
        noticeProcessors.add(new CourtseyNoticeDueDateProcessor());
        noticeProcessors.add(new RecallCourtseyNoticeDueDateProcessor());
        return noticeProcessors;
    }

    private OlePaymentStatus getPaymentStatus(String paymentStatus) {
        LOG.debug("Inside the getPaymentStatus method");
        Map statusMap = new HashMap();
        statusMap.put("paymentStatusCode", paymentStatus);
        List<OlePaymentStatus> olePaymentStatusList = (List<OlePaymentStatus>) getBusinessObjectService().findMatching(OlePaymentStatus.class, statusMap);
        return olePaymentStatusList != null && olePaymentStatusList.size() > 0 ? olePaymentStatusList.get(0) : null;
    }

    public String generateBillPayment(String selectedCirculationDesk, OleLoanDocument loanDocument, Timestamp customDueDateMap, Timestamp dueDate,boolean isRenew) {
        String billPayment = null;
        OlePaymentStatus forgivePaymentStatus = new OlePaymentStatus();
        if(StringUtils.isNotBlank(loanDocument.getItemStatus()) && loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST_AND_PAID)){
            forgivePaymentStatus = getPaymentStatus(OLEConstants.REPLACED);
        } else{
            forgivePaymentStatus = getPaymentStatus(OLEConstants.FORGIVEN);
        }
        ItemFineRate itemFineRate = loanDocument.getItemFineRate();
        List<FeeType> olePatronFeeTypes=loanDocument.getOlePatron().getPatronFeeTypes();
        for(FeeType olePatronfeeType : olePatronFeeTypes) {
            if (olePatronfeeType.getOleFeeType().getFeeTypeCode().equalsIgnoreCase(OLEConstants.REPLACMENT_FEE) && loanDocument.getItemId().equals(olePatronfeeType.getItemBarcode())) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("billNumber", olePatronfeeType.getBillNumber());
                PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);
                if (olePatronfeeType.getPaymentStatusCode().equalsIgnoreCase("PAY_OUTSTN")) {
                    OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                    oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                    oleItemLevelBillPayment.setAmount(olePatronfeeType.getBalFeeAmount());
                    oleItemLevelBillPayment.setCreatedUser(loanDocument.getLoanOperatorId());
                    if(StringUtils.isNotBlank(loanDocument.getItemStatus()) && loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST_AND_PAID)){
                        oleItemLevelBillPayment.setPaymentMode("Replacement");
                        oleItemLevelBillPayment.setTransactionNote(loanDocument.getItemReplaceNote());
                        patronBillPayment.setPaymentMethod("Replacement");
                        patronBillPayment.setNote(loanDocument.getItemReplaceNote());
                    } else{
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.FORGIVE);
                        patronBillPayment.setPaymentMethod(OLEConstants.FORGIVE);
                    }
                    oleItemLevelBillPayment.setNote("Note" + loanDocument.getCheckInDate());
                    List<OleItemLevelBillPayment> oleItemLevelBillPayments = CollectionUtils.isNotEmpty(olePatronfeeType.getItemLevelBillPaymentList()) ? olePatronfeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                    oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                    olePatronfeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                    olePatronfeeType.setPaymentStatus(forgivePaymentStatus.getPaymentStatusId());
                    olePatronfeeType.setBalFeeAmount(new KualiDecimal(0));
                    patronBillPayment.setPaymentAmount(patronBillPayment.getUnPaidBalance());
                    patronBillPayment.setUnPaidBalance(KualiDecimal.ZERO);
                    patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                    getBusinessObjectService().save(olePatronfeeType);
                    getBusinessObjectService().save(patronBillPayment);
                    loanDocument.setReplacementBill(null);
                }
            }
        }
        if (null == itemFineRate.getFineRate() || null == itemFineRate.getMaxFine() || null == itemFineRate.getInterval()) {
            LOG.error("No fine rule found");
        } else {
            if (null != loanDocument.getReplacementBill() && loanDocument.getReplacementBill().compareTo(BigDecimal.ZERO) > 0) {
                billPayment = generateReplacementBill(loanDocument, dueDate,isRenew);
            } else {
                Double overdueFine = new FineDateTimeUtil().calculateOverdueFine(selectedCirculationDesk, dueDate, customDueDateMap, itemFineRate);
                overdueFine = overdueFine >= itemFineRate.getMaxFine() ? itemFineRate.getMaxFine() : overdueFine;
                if (null != overdueFine && overdueFine > 0) {
                    billPayment = generateOverdueBill(loanDocument, overdueFine, dueDate,isRenew);
                }
            }
        }
        return billPayment;
    }

    private String generateOverdueBill(OleLoanDocument loanDocument, Double overdueFine, Timestamp dueDate,boolean isRenew) {
        String billPayment = null;
        try {
            billPayment = getPatronBillGenerator().generatePatronBillPayment(loanDocument, OLEConstants.OVERDUE_FINE, overdueFine, dueDate,isRenew);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return billPayment;
    }

    public String generateProcessingBill(OleLoanDocument loanDocument, Double serviceFine, Timestamp dueDate, boolean isRenew) {
        String billPayment = null;
        try {
            billPayment = getPatronBillGenerator().generatePatronBillPayment(loanDocument, OLEConstants.LOST_ITEM_PROCESSING_FEE, serviceFine, dueDate,isRenew);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return billPayment;
    }

    private String generateReplacementBill(OleLoanDocument loanDocument, Timestamp dueDate,boolean isRenew) {
        String billPayment = null;
        try {
            billPayment = getPatronBillGenerator().generatePatronBillPayment(loanDocument, OLEConstants.REPLACEMENT_FEE, loanDocument.getReplacementBill().doubleValue(), dueDate,isRenew);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return billPayment;
    }

    private PatronBillGenerator getPatronBillGenerator() {
        return new PatronBillGenerator();
    }

    public ItemFineRate fireFineRules(OleLoanDocument loanDocument, OleItemRecordForCirc oleItemRecordForCirc, OlePatronDocument olePatronDocument) {
        ItemFineRate itemFineRate = new ItemFineRate();
        List<Object> facts = new ArrayList<>();
        facts.add(oleItemRecordForCirc);
        facts.add(itemFineRate);
        facts.add(olePatronDocument);
        facts.add(loanDocument);
        fireRules(facts, null, "fine validation");
        return itemFineRate;
    }

    public OleLoanDocument getLoanDocument(String itemBarcode) {
        HashMap<String, Object> criteriaMap = new HashMap<>();
        criteriaMap.put("itemId", itemBarcode);
        List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, criteriaMap);
        if (!CollectionUtils.isEmpty(oleLoanDocuments)) {
            return oleLoanDocuments.get(0);
        }
        return null;
    }

    public void sendClaimReturnedNotice(OleLoanDocument loanDocument, String noticeType, String noticeTitle, String noticeContent,boolean isClaimsReturned) {
        NoticeMailContentFormatter noticeMailContentFormatter = new ClaimsReturnedNoticeEmailContentFormatter();
        OLEDeliverNotice deliverNotice = null;
        for (OLEDeliverNotice oleDeliverNotice : loanDocument.getDeliverNotices()) {
            if (oleDeliverNotice != null && oleDeliverNotice.getNoticeType().equalsIgnoreCase(noticeType)) {
                deliverNotice =oleDeliverNotice;
                break;
            }
        }
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = getOleNoticeContentConfigurationBo(deliverNotice, noticeType, noticeTitle, noticeContent);
        List<OleLoanDocument> oleLoanDocuments = Arrays.asList(loanDocument);
        List<OleLoanDocument> oleLoanDocumentsWithItemInfo = new ArrayList<>();
        if(!isClaimsReturned){
            try {
                OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = SpringContext.getBean(OleDeliverRequestDocumentHelperServiceImpl.class);
                oleLoanDocumentsWithItemInfo = oleDeliverRequestDocumentHelperService.getLoanDocumentWithItemInfo(oleLoanDocuments, String.valueOf(isClaimsReturned));
            }catch (Exception e){
                LOG.info("Exception occured while setting the item info " + e.getMessage());
                LOG.error(e,e);
            }
        }

        if(oleLoanDocumentsWithItemInfo.size() == 0){
            oleLoanDocumentsWithItemInfo.addAll(oleLoanDocuments);
        }

        String mailContent = noticeMailContentFormatter.generateMailContentForPatron(oleLoanDocumentsWithItemInfo, oleNoticeContentConfigurationBo);

        Map claimMap = new HashMap();
        claimMap.put(OLEConstants.LOAN_DOCUMENTS, oleLoanDocumentsWithItemInfo);
        LoanNoticesExecutor claimsReturnedNoticesExecutor = new ClaimsReturnedNoticesExecutor(claimMap);
        if (StringUtils.isNotBlank(mailContent)) {
            claimsReturnedNoticesExecutor.setOleNoticeContentConfigurationBo(oleNoticeContentConfigurationBo);
            claimsReturnedNoticesExecutor.sendMail(mailContent);
            saveDeliverNoticeHistory(deliverNotice, mailContent, noticeType);
            claimsReturnedNoticesExecutor.getSolrRequestReponseHandler().updateSolr(org.kuali.common.util.CollectionUtils.singletonList(
                    claimsReturnedNoticesExecutor.getNoticeSolrInputDocumentGenerator().getSolrInputDocument(
                            claimsReturnedNoticesExecutor.buildMapForIndexToSolr(noticeType, mailContent, oleLoanDocumentsWithItemInfo))));
        }
    }

    private OleNoticeContentConfigurationBo getOleNoticeContentConfigurationBo(OLEDeliverNotice oleDeliverNotice, String noticeType, String noticeTitle, String noticeContent) {
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String, String> noticeConfigurationMap = new HashMap<>();
        if (oleDeliverNotice != null && StringUtils.isNotBlank(oleDeliverNotice.getNoticeContentConfigName())) {
            noticeConfigurationMap.put("noticeType", noticeType);
            noticeConfigurationMap.put("noticeName", oleDeliverNotice.getNoticeContentConfigName());
        }else {
            noticeConfigurationMap.put("noticeType", noticeType);
        }
        oleNoticeContentConfigurationBoList = (List<OleNoticeContentConfigurationBo>) getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class, noticeConfigurationMap);
        if (CollectionUtils.isNotEmpty(oleNoticeContentConfigurationBoList)) {
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        } else {
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeType(noticeType);
            oleNoticeContentConfigurationBo.setNoticeTitle(getParameterValueResolver().getParameter(OLEConstants.APPL_ID, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, noticeTitle));
            oleNoticeContentConfigurationBo.setNoticeBody(getParameterValueResolver().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, noticeContent));
            oleNoticeContentConfigurationBo.setNoticeSubjectLine(new OleNoticeServiceImpl().getNoticeSubjectForNoticeType(noticeType));
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            OleNoticeFieldLabelMapping oleNoticeFieldLabelMapping =new OleNoticeFieldLabelMapping();
            oleNoticeFieldLabelMapping.setFieldName("Claims Search Count");
            oleNoticeFieldLabelMapping.setFieldLabel("Claims Search Count");
            oleNoticeFieldLabelMapping.setBelongsTo("ITEM");
            oleNoticeFieldLabelMapping.setOleNoticeContentConfigurationBo(oleNoticeContentConfigurationBo);
            oleNoticeContentConfigurationBo.getOleNoticeItemFieldLabelMappings().add(oleNoticeFieldLabelMapping);

        }
        return oleNoticeContentConfigurationBo;
    }

    public void saveDeliverNoticeHistory(OLEDeliverNotice oleDeliverNotice, String mailContent, String noticeType) {
        if (oleDeliverNotice != null) {
            OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
            oleDeliverNoticeHistory.setLoanId(oleDeliverNotice.getLoanId());
            oleDeliverNoticeHistory.setNoticeType(oleDeliverNotice.getNoticeType());
            oleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(new Date().getTime()));
            oleDeliverNoticeHistory.setPatronId(oleDeliverNotice.getPatronId());
            oleDeliverNoticeHistory.setNoticeSendType(oleDeliverNotice.getNoticeSendType());
            oleDeliverNoticeHistory.setNoticeContent(mailContent.getBytes());
            getBusinessObjectService().save(oleDeliverNoticeHistory);
        }
    }

    public void updateFees(OleLoanDocument oleLoanDocument, String operatorId, List<String> forgiveFeeTypeCodes, String note, boolean isRenew) {
        List<FeeType> feeTypes = getFeeTypes(oleLoanDocument, Arrays.asList(OLEConstants.FEE_TYPE_CODE_REPL_FEE, OLEConstants.LOST_ITEM_PRCS_FEE));
        if (CollectionUtils.isNotEmpty(feeTypes)) {
            OlePaymentStatus paymentStatus = new PatronBillHelperService().getPaymentStatus(OLEConstants.REPLACED);
            OlePaymentStatus forgivePaymentStatus = new PatronBillHelperService().getPaymentStatus(OLEConstants.FORGIVEN);
            OlePaymentStatus outstandingPaymentStatus = new PatronBillHelperService().getPaymentStatus(OLEConstants.PAY_OUTSTANDING);
            for (FeeType feeType : feeTypes) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("billNumber", feeType.getBillNumber());
                PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);
                if (forgiveFeeTypeCodes.contains(feeType.getOleFeeType().getFeeTypeCode()) && paymentStatus != null && (oleLoanDocument.getItemStatus() !=null && oleLoanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST_AND_PAID))) {
                    if(!feeType.getBalFeeAmount().isZero()) {
                        patronBillPayment.setPaymentAmount(patronBillPayment.getUnPaidBalance());
                        patronBillPayment.setUnPaidBalance(KualiDecimal.ZERO);
                        patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                        patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                        patronBillPayment.setPaymentMethod("Replacement");
                        patronBillPayment.setNote(note);
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setAmount(feeType.getBalFeeAmount());
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setPaymentMode("Replacement");
                        oleItemLevelBillPayment.setTransactionNote(note);
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = CollectionUtils.isNotEmpty(feeType.getItemLevelBillPaymentList()) ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setPaymentStatus(paymentStatus.getPaymentStatusId());
                        feeType.setBalFeeAmount(new KualiDecimal(0));
                    }
                } else if(forgiveFeeTypeCodes.contains(feeType.getOleFeeType().getFeeTypeCode()) && paymentStatus != null){
                    if(!feeType.getBalFeeAmount().isZero()) {
                        patronBillPayment.setPaymentAmount(patronBillPayment.getUnPaidBalance());
                        patronBillPayment.setUnPaidBalance(KualiDecimal.ZERO);
                        patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                        patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                        patronBillPayment.setPaymentMethod(OLEConstants.FORGIVE);
                        patronBillPayment.setNote(note);
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setAmount(feeType.getBalFeeAmount());
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.FORGIVE);
                        if (isRenew) {
                            oleItemLevelBillPayment.setNote("Item renewed on " + new Timestamp(System.currentTimeMillis()));
                        } else {
                            oleItemLevelBillPayment.setNote(note + oleLoanDocument.getCheckInDate());
                        }
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = CollectionUtils.isNotEmpty(feeType.getItemLevelBillPaymentList()) ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setPaymentStatus(forgivePaymentStatus.getPaymentStatusId());
                        feeType.setBalFeeAmount(new KualiDecimal(0));
                    }
                } else if (feeType.getOlePaymentStatus().getPaymentStatusCode().equals(OLEConstants.SUSPENDED) && outstandingPaymentStatus != null) {
                    feeType.setPaymentStatus(outstandingPaymentStatus.getPaymentStatusId());
                }

                if (isRenew) {
                    feeType.setRenewalDate(new Timestamp(System.currentTimeMillis()));
                } else {
                    if (oleLoanDocument.isOverrideCheckInTime()) {
                        feeType.setOverrideCheckInDate(oleLoanDocument.getCheckInDate());
                        feeType.setCheckInDate(new Timestamp(System.currentTimeMillis()));
                    } else {
                        feeType.setCheckInDate(oleLoanDocument.getCheckInDate());
                    }
                }
                getBusinessObjectService().save(patronBillPayment);
            }
            getBusinessObjectService().save(feeTypes);
        }
    }

    public List<FeeType> getFeeTypes(OleLoanDocument oleLoanDocument, List<String> feeTypeCodes) {
        if (oleLoanDocument != null && oleLoanDocument.getOlePatron() != null && StringUtils.isNotBlank(oleLoanDocument.getOlePatron().getOlePatronId())
                && StringUtils.isNotBlank(oleLoanDocument.getRepaymentFeePatronBillId()) && StringUtils.isNotBlank(oleLoanDocument.getItemId())) {
            Map criteria = new HashMap<String, String>();
            criteria.put("patronBillPayment.billNumber", oleLoanDocument.getRepaymentFeePatronBillId());
            criteria.put("patronBillPayment.patronId", oleLoanDocument.getOlePatron().getOlePatronId());
            criteria.put("itemBarcode", oleLoanDocument.getItemId());
            criteria.put("oleFeeType.feeTypeCode", feeTypeCodes);
            return (List<FeeType>) getBusinessObjectService().findMatching(FeeType.class, criteria);
        }
        return null;
    }

    public boolean updatePaymentStatusToOutstanding(OleLoanDocument oleLoanDocument) {
        List<FeeType> feeTypes = getFeeTypes(oleLoanDocument, Arrays.asList(OLEConstants.FEE_TYPE_CODE_REPL_FEE, OLEConstants.LOST_ITEM_PRCS_FEE));
        if (CollectionUtils.isNotEmpty(feeTypes)) {
            OlePaymentStatus paymentStatus = new PatronBillHelperService().getPaymentStatus(OLEConstants.PAY_OUTSTANDING);
            if (paymentStatus != null) {
                for (FeeType feeType : feeTypes) {
                    OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                    oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                    oleItemLevelBillPayment.setAmount(feeType.getBalFeeAmount());
                    oleItemLevelBillPayment.setCreatedUser(GlobalVariables.getUserSession().getLoggedInUserPrincipalName());
                    oleItemLevelBillPayment.setPaymentMode(OLEConstants.FEE_UNSUSPENDED);
                    oleItemLevelBillPayment.setNote(OLEConstants.BILL_SUSPENDED_NOTE);
                    List<OleItemLevelBillPayment> oleItemLevelBillPayments = CollectionUtils.isNotEmpty(feeType.getItemLevelBillPaymentList()) ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                    oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                    feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                    if (feeType.getOlePaymentStatus().getPaymentStatusCode().equals(OLEConstants.SUSPENDED)) {
                        feeType.setPaymentStatus(paymentStatus.getPaymentStatusId());
                    }
                }
                getBusinessObjectService().save(feeTypes);
            }
            return true;
        }

        return false;
    }

    public void processLostItem(String operatorId, OleLoanDocument loanDocument, boolean isRenew) {
        String forgiveLostFees = getParameterValueResolver().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.FORGIVE_LOST_FEES);
        List<String> feeTypeCodes = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(forgiveLostFees)) {
            feeTypeCodes = Arrays.asList(forgiveLostFees.split(","));
        }
        if(StringUtils.isNotBlank(loanDocument.getItemStatus())&&loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST_AND_PAID)) {
            updateFees(loanDocument, operatorId, feeTypeCodes, loanDocument.getItemReplaceNote(), isRenew);
        } else{
            updateFees(loanDocument, operatorId, feeTypeCodes, "Item Returned on ", isRenew);
        }
    }

    public ParameterValueResolver getParameterValueResolver() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    public String getDefaultClosingTime(OleLoanDocument oleLoanDocument,Date dueDate){
        String closingTime = null;

        OleCirculationDesk circulationDesk = (oleLoanDocument.getCirculationLocationId() != null ? new CircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()):null);
        if(circulationDesk != null){
            LoanDateTimeUtil loanDateTimeUtil =new LoanDateTimeUtil();
            OleCalendar oleCalendar = loanDateTimeUtil.getActiveCalendar(dueDate,circulationDesk.getCalendarGroupId());
            if(oleCalendar != null){
                int day = dueDate.getDay();
                List<OleCalendarWeek> oleCalendarWeekList = oleCalendar.getOleCalendarWeekList();
                if(CollectionUtils.isNotEmpty(oleCalendarWeekList)){
                    for(Iterator<OleCalendarWeek> iterator = oleCalendarWeekList.iterator(); iterator.hasNext();){
                        OleCalendarWeek oleCalendarWeek =  iterator.next();
                        if(oleCalendarWeek.getStartDay().equalsIgnoreCase(String.valueOf(day))){
                            closingTime = oleCalendarWeek.getCloseTime();
                            break;
                        }else if ((day >= Integer.valueOf(oleCalendarWeek.getStartDay())) && day <= Integer.valueOf(oleCalendarWeek.getEndDay())){
                            closingTime = oleCalendarWeek.getCloseTime();
                            break;
                        }
                    }
                }
            }

        }
        return closingTime;
    }

}
