package org.kuali.ole.docstore.engine.service.storage.rdbms;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.AccessInformation;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.CheckInLocation;
import org.kuali.ole.docstore.common.document.content.instance.FormerIdentifier;
import org.kuali.ole.docstore.common.document.content.instance.HighDensityStorage;
import org.kuali.ole.docstore.common.document.content.instance.Identifier;
import org.kuali.ole.docstore.common.document.content.instance.ItemStatus;
import org.kuali.ole.docstore.common.document.content.instance.ItemType;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.Note;
import org.kuali.ole.docstore.common.document.content.instance.NumberOfCirculations;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingOrder;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.common.document.content.instance.StatisticalSearchingCode;
import org.kuali.ole.docstore.common.document.content.instance.Uri;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.engine.service.search.DocstoreSearchService;
import org.kuali.ole.docstore.engine.service.search.DocstoreSolrSearchService;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsItemDocumentManager extends RdbmsHoldingsDocumentManager {

    private static RdbmsItemDocumentManager rdbmsItemDocumentManager = null;
    private static final Logger LOG = LoggerFactory.getLogger(RdbmsItemDocumentManager.class);
    private static final String leftPaddingSize = ConfigContext.getCurrentContextConfig().getProperty(DocStoreConstants.LEFT_PADDING_SIZE);

    private ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();

    public static RdbmsItemDocumentManager getInstance() {
        if(rdbmsItemDocumentManager == null) {
            rdbmsItemDocumentManager = new RdbmsItemDocumentManager();
        }
        return rdbmsItemDocumentManager;
    }

    @Override
    public void create(Object object) {

        ItemRecord itemRecord = new ItemRecord();
        Item itemDocument = (Item)object;
        String holdingsId = "";
        //TODO:Update shelving order form holdings

        if(itemDocument != null &&  itemDocument.getHolding() != null && StringUtils.isNotEmpty(itemDocument.getHolding().getId())) {
            holdingsId = itemDocument.getHolding().getId();
        } else {
            throw new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND,DocstoreResources.HOLDING_ID_NOT_FOUND);
        }
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item item = itemOlemlRecordProcessor.fromXML(itemDocument.getContent());

        processCallNumber(item);

        itemRecord.setStaffOnlyFlag(itemDocument.isStaffOnly());
        itemRecord.setHoldingsId(DocumentUniqueIDPrefix.getDocumentId(holdingsId));
        itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML);
        itemRecord.setCreatedBy(itemDocument.getCreatedBy());
        itemRecord.setCreatedDate(createdDate());
        setItemProperties(itemRecord, item);
        String content =  itemOlemlRecordProcessor.toXML(item);
        itemDocument.setContent(content);
        itemDocument.setId(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
    }


    protected void processCallNumber(org.kuali.ole.docstore.common.document.content.instance.Item itemDocument) {
        if (itemDocument != null && itemDocument.getCallNumber() != null) {
            validateCallNumber(itemDocument.getCallNumber());
            CallNumber cNum = itemDocument.getCallNumber();
            computeCallNumberType(cNum);
            if (cNum.getNumber() != null && cNum.getNumber().trim().length() > 0) {
                //Build sortable key if a valid call number exists
                String callNumber = cNum.getNumber();
                callNumber = appendItemInfoToCallNumber(itemDocument, callNumber);

//                boolean isValid = validateCallNumber(callNumber, cNum.getShelvingScheme().getCodeValue());
                String value = "";
//                if (isValid) {
//                    value = buildSortableCallNumber(callNumber, cNum.getShelvingScheme().getCodeValue());
//                } else {
//                    value = callNumber;
//                }
                value = buildSortableCallNumber(callNumber, cNum.getShelvingScheme().getCodeValue());
                if (cNum.getShelvingOrder() == null) {
                    cNum.setShelvingOrder(new ShelvingOrder());
                }
                cNum.getShelvingOrder().setFullValue(value);
            }
        }
    }

    private String appendItemInfoToCallNumber(org.kuali.ole.docstore.common.document.content.instance.Item item, String callNumber) {
        if (item.getEnumeration() != null && item.getEnumeration().trim().length() > 0) {
            callNumber = callNumber + " " + item.getEnumeration().trim();
        }
        if (item.getChronology() != null && item.getChronology().trim().length() > 0) {
            callNumber = callNumber + " " + item.getChronology().trim();

        }
        if (item.getCopyNumber() != null && item.getCopyNumber().trim().length() > 0) {
            callNumber = callNumber + " " + item.getCopyNumber().trim();
        }
        return callNumber;
    }

    @Override
    public void update(Object object) {
        Item itemDocument = (Item)object;
        validateMissingPieces(itemDocument);
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("itemId", DocumentUniqueIDPrefix.getDocumentId(itemDocument.getId()));
        ItemRecord itemRecord = getBusinessObjectService().findByPrimaryKey(ItemRecord.class, parentCriteria1);
        if(itemRecord ==  null) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ITEM_ID_NOT_FOUND, DocstoreResources.ITEM_ID_NOT_FOUND);
            docstoreException.addErrorParams("itemId", itemDocument.getId());
            throw docstoreException;
        }
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item item = itemOlemlRecordProcessor.fromXML(itemDocument.getContent());
        itemRecord.setUpdatedBy(itemDocument.getUpdatedBy());
        itemRecord.setUpdatedDate(createdDate());
        itemRecord.setStaffOnlyFlag(itemDocument.isStaffOnly());
        processCallNumber(item);
        setItemProperties(itemRecord, item);
        String content =  itemOlemlRecordProcessor.toXML(item);
        itemDocument.setContent(content);
    }

    @Override
    public Object retrieve(String itemId) {
        Item item = retrieveItem(itemId, null, null);
//        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, getItemMap(itemId));
//        if (itemRecords != null && itemRecords.size() > 0) {
//            ItemRecord itemRecord = itemRecords.get(0);
//            item = buildItemContent(itemRecord);
//        }
//        else {
//            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ITEM_ID_NOT_FOUND, DocstoreResources.ITEM_ID_NOT_FOUND);
//            docstoreException.addErrorParams("itemId", itemId);
//            throw docstoreException;
//        }
        return item;
    }

    @Override
    public List<Object> retrieve(List<String> ids) {
        List<Object>  items =new ArrayList<>();
        for(String id:ids){
            items.add(retrieve(id));
        }
        return items;
    }


    protected Item buildItemContent(ItemRecord itemRecord) {

        org.kuali.ole.docstore.common.document.content.instance.Item item = new org.kuali.ole.docstore.common.document.content.instance.Item();
        item.setItemIdentifier(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
        item.setPurchaseOrderLineItemIdentifier(itemRecord.getPurchaseOrderItemLineId());
        item.setVendorLineItemIdentifier(itemRecord.getVendorLineItemId());
        item.setPrice(itemRecord.getPrice());
        item.setFund(itemRecord.getFund());
        item.setBarcodeARSL(itemRecord.getBarCodeArsl());
        item.setCopyNumber(itemRecord.getCopyNumber());
        item.setEnumeration(itemRecord.getEnumeration());
        item.setChronology(itemRecord.getChronology());
        item.setNumberOfPieces(itemRecord.getNumberOfPieces());
        item.setCheckinNote(itemRecord.getCheckInNote());
        item.setLocation(getLocationDetails(itemRecord.getLocation(), itemRecord.getLocationLevel()));
        if (itemRecord.getFormerIdentifierRecords() != null) {
            List<FormerIdentifier> formerIdList = new ArrayList<FormerIdentifier>();
            for (FormerIdentifierRecord formerIdentifierRecord : itemRecord.getFormerIdentifierRecords()) {
                FormerIdentifier formerIdentifier = new FormerIdentifier();
               Identifier identifier = new Identifier();
                identifier.setIdentifierValue(formerIdentifierRecord.getValue());
                formerIdentifier.setIdentifier(identifier);
                formerIdList.add(formerIdentifier);
                item.setFormerIdentifier(formerIdList);
            }
        }

        AccessInformation accessInformation = new AccessInformation();
        accessInformation.setBarcode(itemRecord.getBarCode());
        Uri itemuri = new Uri();
        itemuri.setValue(itemRecord.getUri());
        accessInformation.setUri(itemuri);
        item.setAccessInformation(accessInformation);
       CallNumber itemCallNumber = new CallNumber();
        itemCallNumber.setNumber(itemRecord.getCallNumber());
        itemCallNumber.setPrefix(itemRecord.getCallNumberPrefix());
        ShelvingScheme itemShelvingScheme = new ShelvingScheme();
        if (itemRecord.getCallNumberTypeRecord() != null) {
            itemShelvingScheme.setCodeValue(itemRecord.getCallNumberTypeRecord().getCode());
            itemShelvingScheme.setFullValue(itemRecord.getCallNumberTypeRecord().getName());
            itemCallNumber.setShelvingScheme(itemShelvingScheme);
        }
       ShelvingOrder itemShelvingOrder = new ShelvingOrder();
        itemShelvingOrder.setCodeValue(itemRecord.getShelvingOrder());
        itemShelvingOrder.setFullValue(itemRecord.getShelvingOrder());
        itemCallNumber.setShelvingOrder(itemShelvingOrder);
        item.setCallNumber(itemCallNumber);

        List<Note> notes = new ArrayList<Note>();
        if (itemRecord.getItemNoteRecords() != null) {
            List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
            for (ItemNoteRecord itemNoteRecord : itemNoteRecords) {
                Note note = new Note();
                note.setType(itemNoteRecord.getType());
                note.setValue(itemNoteRecord.getNote());
                notes.add(note);
            }
            item.setNote(notes);
        }
        List<DonorInfo> donorInfoList = new ArrayList<DonorInfo>();
        if (itemRecord.getDonorList() != null) {
            for (OLEItemDonorRecord oleItemDonorRecord : itemRecord.getDonorList()) {
                DonorInfo donorInfo = new DonorInfo();
                donorInfo.setDonorCode(oleItemDonorRecord.getDonorCode());
                donorInfo.setDonorPublicDisplay(oleItemDonorRecord.getDonorPublicDisplay());
                donorInfo.setDonorNote(oleItemDonorRecord.getDonorNote());
                donorInfoList.add(donorInfo);
            }
            item.setDonorInfo(donorInfoList);
        }
        if (itemRecord.getItemStatusRecord() != null) {
            ItemStatus itemStatus = new ItemStatus();
            itemStatus.setCodeValue(itemRecord.getItemStatusRecord().getCode());
            itemStatus.setFullValue(itemRecord.getItemStatusRecord().getName());
            item.setItemStatus(itemStatus);
        }
        if (itemRecord.getItemTypeRecord() != null) {
            ItemType itemType = new ItemType();
            itemType.setCodeValue(itemRecord.getItemTypeRecord().getCode());
            itemType.setFullValue(itemRecord.getItemTypeRecord().getName());
            item.setItemType(itemType);
        }
        if (itemRecord.getItemTempTypeRecord() != null) {
            ItemType itemType = new ItemType();
            itemType.setCodeValue(itemRecord.getItemTempTypeRecord().getCode());
            itemType.setFullValue(itemRecord.getItemTempTypeRecord().getName());
            item.setTemporaryItemType(itemType);
        }
        List<StatisticalSearchingCode> statisticalSearchingCodes = new ArrayList<StatisticalSearchingCode>();
        StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
        if (itemRecord.getItemStatisticalSearchRecords() != null && itemRecord.getItemStatisticalSearchRecords().size() > 0) {
            ItemStatisticalSearchRecord itemStatisticalSearchRecord = itemRecord.getItemStatisticalSearchRecords().get(0);
            if(itemStatisticalSearchRecord != null && itemStatisticalSearchRecord.getStatisticalSearchRecord() != null) {
                statisticalSearchingCode.setCodeValue(itemStatisticalSearchRecord.getStatisticalSearchRecord().getCode());
                statisticalSearchingCode.setFullValue(itemStatisticalSearchRecord.getStatisticalSearchRecord().getName());
            }
        }
        statisticalSearchingCodes.add(statisticalSearchingCode);
        item.setStatisticalSearchingCode(statisticalSearchingCodes);
        HighDensityStorage highDensityStorage = new HighDensityStorage();

        if (itemRecord.getHighDensityStorageRecord() != null) {
            HighDensityStorageRecord highDensityStorageRecord = itemRecord.getHighDensityStorageRecord();
            highDensityStorage.setModule(highDensityStorageRecord.getModule());
            highDensityStorage.setRow(highDensityStorageRecord.getRow());
            highDensityStorage.setShelf(highDensityStorageRecord.getShelf());
            highDensityStorage.setTray(highDensityStorageRecord.getTray());
        }
        item.setHighDensityStorage(highDensityStorage);
        if (itemRecord.getEffectiveDate() != null) {
            //Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            //String effectiveDate = formatter.format(itemRecord.getEffectiveDate().toString());
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date effectiveDate = null;
            try {
                effectiveDate = format2.parse(itemRecord.getEffectiveDate().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setItemStatusEffectiveDate(format1.format(effectiveDate).toString());
        }
        if(itemRecord.getStaffOnlyFlag() != null) {
            item.setStaffOnlyFlag(itemRecord.getStaffOnlyFlag());
        }
        if (itemRecord.getFastAddFlag() != null) {
            item.setFastAddFlag(Boolean.valueOf(itemRecord.getFastAddFlag()));
        }
        List<LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
        if (locationsCheckinCountRecords != null && locationsCheckinCountRecords.size() > 0) {

            NumberOfCirculations numberOfCirculations = new NumberOfCirculations();
            List<CheckInLocation> checkInLocations = new ArrayList<CheckInLocation>();
            for (LocationsCheckinCountRecord locationsCheckinCountRecord : locationsCheckinCountRecords) {

              CheckInLocation checkInLocation = new CheckInLocation();
                checkInLocation.setCount(locationsCheckinCountRecord.getLocationCount());
                checkInLocation.setName(locationsCheckinCountRecord.getLocationName());
                checkInLocation.setInHouseCount(locationsCheckinCountRecord.getLocationInhouseCount());
                checkInLocations.add(checkInLocation);
            }
            numberOfCirculations.setCheckInLocation(checkInLocations);
            item.setNumberOfCirculations(numberOfCirculations);
        }
        if (itemRecord.getClaimsReturnedFlag() != null) {
            item.setClaimsReturnedFlag(Boolean.valueOf(itemRecord.getClaimsReturnedFlag()));
        }
        if (itemRecord.getClaimsReturnedFlagCreateDate() != null) {
            //try {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date claimReturnCreateDate = null;
            try {
                claimReturnCreateDate = format2.parse(itemRecord.getClaimsReturnedFlagCreateDate().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setClaimsReturnedFlagCreateDate(format1.format(claimReturnCreateDate).toString());
            //item.setClaimsReturnedFlagCreateDate(getGregorianCalendar(itemRecord.getClaimsReturnedFlagCreateDate()));
            /*} catch (DatatypeConfigurationException e) {
                LOG.error(" getGregorianCalendar", e);
            }*/
        }

        if (itemRecord.getDueDateTime() != null) {
            /*try {
                item.setDueDateTime(getGregorianCalendar(itemRecord.getDueDateTime()));
            } catch (DatatypeConfigurationException e) {
                LOG.error(" getGregorianCalendar", e);
            }*/
            //try {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dueDateTime = null;
            try {
                dueDateTime = format2.parse(itemRecord.getDueDateTime().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setDueDateTime(format1.format(dueDateTime).toString());
            /*item.setClaimsReturnedFlagCreateDate(getGregorianCalendar(itemRecord.getClaimsReturnedFlagCreateDate()));
            } catch (DatatypeConfigurationException e) {
                LOG.error(" getGregorianCalendar", e);
            }*/
        }
        if (itemRecord.getCheckOutDateTime() != null) {

            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date checkoutDateTime = null;
            try {
                checkoutDateTime = format2.parse(itemRecord.getCheckOutDateTime().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setCheckOutDateTime(format1.format(checkoutDateTime).toString());
        }
        if(itemRecord.isItemDamagedStatus()){
            item.setItemDamagedStatus(itemRecord.isItemDamagedStatus());
            item.setDamagedItemNote(itemRecord.getDamagedItemNote());
        } else {
            item.setItemDamagedStatus(false);
            item.setDamagedItemNote("");
        }
        if(itemRecord.getClaimsReturnedNote() != null){
            item.setClaimsReturnedNote(itemRecord.getClaimsReturnedNote());
        }
        if (itemRecord.isMissingPieceFlag()) {
            item.setMissingPieceFlagNote(itemRecord.getMissingPieceFlagNote());
            item.setMissingPieceFlag(itemRecord.isMissingPieceFlag());
            item.setMissingPiecesCount(itemRecord.getMissingPiecesCount());
        } else {
            item.setMissingPieceFlagNote(null);
            item.setMissingPieceFlag(false);
            item.setMissingPiecesCount(null);
        }
        if (itemRecord.getMissingPieceEffectiveDate() != null) {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date effectiveDate = null;
            try {
                effectiveDate = format2.parse(itemRecord.getMissingPieceEffectiveDate().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setMissingPieceEffectiveDate(format1.format(effectiveDate).toString());
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String date = df.format(new Date());
            item.setMissingPieceEffectiveDate(date);
        }
        item.setProxyBorrower(itemRecord.getProxyBorrower()!=null?itemRecord.getProxyBorrower():"");
        item.setCurrentBorrower(itemRecord.getCurrentBorrower()!=null?itemRecord.getCurrentBorrower():"");
        item.setNumberOfRenew(itemRecord.getNumberOfRenew());
        Item itemDoc = new ItemOleml();
        itemDoc.setId(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
        itemDoc.setContent(itemOlemlRecordProcessor.toXML(item));
//        itemDoc.setHolding((Holdings) super.retrieve(itemRecord.getHoldingsId()));
        itemDoc.setLocation(itemRecord.getLocation());
        itemDoc.setCreatedBy(itemRecord.getCreatedBy());
        if(itemRecord.getStaffOnlyFlag() != null) {
            itemDoc.setStaffOnly(itemRecord.getStaffOnlyFlag());
        }
        if(itemRecord.getCreatedDate()!=null) {
            itemDoc.setCreatedOn(itemRecord.getCreatedDate().toString());

        }
        if(itemRecord.getUpdatedDate()!=null) {
            itemDoc.setUpdatedOn(itemRecord.getUpdatedDate().toString());
        }
        itemDoc.setUpdatedBy(itemRecord.getUpdatedBy());

        StringBuilder sortedValue = new StringBuilder();
        if (StringUtils.isNotEmpty(itemRecord.getLocation())) {
            addDataToLabel(sortedValue, itemRecord.getLocation());
        }
        if (StringUtils.isNotEmpty(itemRecord.getCallNumberPrefix())) {
            addDataToLabel(sortedValue, itemRecord.getCallNumberPrefix());
        }
        if (StringUtils.isNotEmpty(itemRecord.getCallNumber())) {
            addDataToLabel(sortedValue, getNormalized(itemRecord.getCallNumber()));
        }
        if (StringUtils.isNotEmpty(itemRecord.getEnumeration())) {
            addDataToLabel(sortedValue, getNormalized(itemRecord.getEnumeration()));
        }
        if (StringUtils.isNotEmpty(itemRecord.getChronology())) {
            addDataToLabel(sortedValue, itemRecord.getChronology());
        }
        if (StringUtils.isNotEmpty(itemRecord.getCopyNumber())) {
            addDataToLabel(sortedValue, getNormalized(itemRecord.getCopyNumber()));
        }

        StringBuilder labelName = new StringBuilder();
        if (StringUtils.isNotEmpty(itemRecord.getLocation())) {
            addDataToLabel(labelName, itemRecord.getLocation());
        }
        if (StringUtils.isNotEmpty(itemRecord.getCallNumberPrefix())) {
            addDataToLabel(labelName, itemRecord.getCallNumberPrefix());
        }
        if (StringUtils.isNotEmpty(itemRecord.getCallNumber())) {
            addDataToLabel(labelName, itemRecord.getCallNumber());
        }
        if (StringUtils.isNotEmpty(itemRecord.getEnumeration())) {
            addDataToLabel(labelName, itemRecord.getEnumeration());
        }
        if (StringUtils.isNotEmpty(itemRecord.getChronology())) {
            addDataToLabel(labelName, itemRecord.getChronology());
        }
        if (StringUtils.isNotEmpty(itemRecord.getCopyNumber())) {
            addDataToLabel(labelName, itemRecord.getCopyNumber());
        }
        if (StringUtils.isNotEmpty(itemRecord.getBarCode())) {
            addDataToLabel(labelName, itemRecord.getBarCode());
        }
        itemDoc.setSortedValue(sortedValue.toString());
        LOG.debug("Sorted Value : " + itemDoc.getSortedValue());
        //itemDoc.setDisplayLabel(labelName.toString());
        itemDoc.setDisplayLabel(encodeString(labelName.toString()));
//        List<HoldingsItemRecord> holdingsItemRecords = (List<HoldingsItemRecord>) getBusinessObjectService().findMatching(HoldingsItemRecord.class, getItemMap(itemDoc.getId()));
//        if (!CollectionUtils.isEmpty(holdingsItemRecords)) {
//            List<Holdings> holdingsList = new ArrayList<>();
//            for (HoldingsItemRecord holdingsItemRecord : holdingsItemRecords) {
//                holdingsList.add((Holdings) super.retrieve(holdingsItemRecord.getHoldingsId()));
//            }
//            itemDoc.setHoldings(holdingsList);
//            itemDoc.setAnalytic(true);
//        }

        List<HoldingsItemRecord> holdingsItemRecords = itemRecord.getHoldingsItemRecords();
        if(holdingsItemRecords != null && holdingsItemRecords.size() > 0) {
            List<Holdings> holdingsList = new ArrayList<>();
            for(HoldingsItemRecord holdingsItemRecord : holdingsItemRecords) {
                holdingsList.add(retrieveHoldings(holdingsItemRecord.getHoldingsId(), null, null));
            }
            itemDoc.setHoldings(holdingsList);
            itemDoc.setAnalytic(true);
        }
        return itemDoc;
    }

    @Override
    public void delete(String id) {
        Map itemMap = getItemMap(id);
       // getBusinessObjectService().deleteMatching(ItemRecord.class, itemMap);
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
        if (itemRecords != null && itemRecords.size() > 0) {
            ItemRecord itemRecord = itemRecords.get(0);
            if (itemRecord.getFormerIdentifierRecords() != null && itemRecord.getFormerIdentifierRecords().size() > 0) {
                List<FormerIdentifierRecord> formerIdentifierRecords = itemRecord.getFormerIdentifierRecords();
               getBusinessObjectService().delete(formerIdentifierRecords);
            }
            if (itemRecord.getItemNoteRecords() != null && itemRecord.getItemNoteRecords().size() > 0) {
                List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
                getBusinessObjectService().delete(itemNoteRecords);
            }
           if (itemRecord.getLocationsCheckinCountRecords() != null && itemRecord.getLocationsCheckinCountRecords().size() > 0) {
                List<LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
                getBusinessObjectService().delete(locationsCheckinCountRecords);
           }
            itemRecord.setItemStatusId(null);
           itemRecord.setItemTypeId(null);
            itemRecord.setTempItemTypeId(null);
            itemRecord.setStatisticalSearchId(null);
           itemRecord.setHighDensityStorageId(null);
            getBusinessObjectService().deleteMatching(ItemStatisticalSearchRecord.class, getItemMap(id));
           getBusinessObjectService().delete(itemRecord);
        }

    }


    @Override
    public Object retrieveTree(String id) {

        return null;
    }

    @Override
    public void validate(Object object) {
        Item item = (Item)object;
        validateItem(item);
        validateMissingPieces(item);
    }

    private void setItemProperties(ItemRecord itemRecord, org.kuali.ole.docstore.common.document.content.instance.Item item)  {
        itemRecord.setBarCodeArsl(item.getBarcodeARSL());
        if (item.getCallNumber() != null) {

            CallNumber callNumber = item.getCallNumber();
            itemRecord.setCallNumberPrefix(callNumber.getPrefix());
            itemRecord.setCallNumber(callNumber.getNumber());
            if (callNumber.getShelvingOrder() != null) {
                itemRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            if (callNumber.getShelvingScheme() != null) {
                CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
                itemRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
            }
        }
        if (item.getAccessInformation() != null) {
            itemRecord.setBarCode(item.getAccessInformation().getBarcode());
        }
        if (item.getItemType() != null) {
            ItemTypeRecord itemTypeRecord = saveItemTypeRecord(item.getItemType());
            itemRecord.setItemTypeId(itemTypeRecord == null ? null : itemTypeRecord.getItemTypeId());
            itemRecord.setItemTypeRecord(itemTypeRecord);
        }
        if (item.getTemporaryItemType() != null) {
            ItemTypeRecord tempItemTypeRecord = saveItemTypeRecord(item.getTemporaryItemType());
            itemRecord.setTempItemTypeId(tempItemTypeRecord == null ? null : tempItemTypeRecord.getItemTypeId());
            itemRecord.setItemTempTypeRecord(tempItemTypeRecord);
        }
        itemRecord.setChronology(item.getChronology());
        itemRecord.setCopyNumber(item.getCopyNumber());
        itemRecord.setEnumeration(item.getEnumeration());

        itemRecord.setNumberOfPieces(item.getNumberOfPieces());
        itemRecord.setPurchaseOrderItemLineId(item.getPurchaseOrderLineItemIdentifier());
        itemRecord.setVendorLineItemId(item.getVendorLineItemIdentifier());
        itemRecord.setFund(item.getFund());
        if(StringUtils.isNotEmpty(item.getPrice())) {
            itemRecord.setPrice(item.getPrice());
        }
        itemRecord.setCheckInNote(item.getCheckinNote());
        itemRecord.setFastAddFlag(item.isFastAddFlag() ? Boolean.TRUE : Boolean.FALSE);

        itemRecord.setClaimsReturnedFlag(item.isClaimsReturnedFlag());


        String claimsReturnDate = item.getClaimsReturnedFlagCreateDate();
        if (claimsReturnDate != null) {
            String[] claimsReturnDateArray = claimsReturnDate.split(" ");
            if (claimsReturnDateArray.length == 1 && claimsReturnDateArray[0] != "") {
                claimsReturnDate = claimsReturnDate + " 00:00:00";
                claimsReturnsCreateDateItem(item, itemRecord, claimsReturnDate);
            } else if (claimsReturnDateArray.length > 1) {
                claimsReturnsCreateDateItem(item, itemRecord, claimsReturnDate);
            } else {
                itemRecord.setClaimsReturnedFlagCreateDate(null);
            }
        } else {
            itemRecord.setClaimsReturnedFlagCreateDate(null);
        }

        String dueDateItem = item.getDueDateTime();
        if (dueDateItem != null) {
            //Timestamp timestamp = new Timestamp(item.getDueDateTime().toGregorianCalendar().getTimeInMillis());
            //itemRecord.setDueDateTime(timestamp);
            String[] dueDateItemArray = dueDateItem.split(" ");
            if (dueDateItemArray.length == 1 && dueDateItemArray[0] != "") {
                dueDateItem = dueDateItem + " 00:00:00";
                dueDateTime(item, itemRecord, dueDateItem);
            } else if (dueDateItemArray.length > 1) {
                dueDateTime(item, itemRecord, dueDateItem);
            } else {
                itemRecord.setDueDateTime(null);
            }
        } else {
            itemRecord.setDueDateTime(null);
        }
        String checkOutDateItem = item.getCheckOutDateTime();
        if (checkOutDateItem != null) {
            String[] dueDateItemArray = checkOutDateItem.split(" ");
            if (dueDateItemArray.length == 1 && dueDateItemArray[0] != "") {
                checkOutDateItem = checkOutDateItem + " 00:00:00";
                itemRecord.setCheckOutDateTime(convertDateToTimeStamp(checkOutDateItem));
            } else if (dueDateItemArray.length > 1) {
                itemRecord.setCheckOutDateTime(convertDateToTimeStamp(checkOutDateItem));
            } else {
                itemRecord.setCheckOutDateTime(null);
            }
        } else {
            itemRecord.setCheckOutDateTime(null);
        }

        itemRecord.setClaimsReturnedNote(item.getClaimsReturnedNote());
        itemRecord.setProxyBorrower(item.getProxyBorrower());
        itemRecord.setCurrentBorrower(item.getCurrentBorrower());
        itemRecord.setDamagedItemNote(item.getDamagedItemNote());
        itemRecord.setItemDamagedStatus(item.isItemDamagedStatus());
        itemRecord.setFastAddFlag(item.isFastAddFlag());
        String effectiveDateForItem = item.getItemStatusEffectiveDate();
        if (effectiveDateForItem != null) {
            String[] effectiveDateForItemArray = effectiveDateForItem.split(" ");
            if (effectiveDateForItemArray.length == 1 && effectiveDateForItemArray[0] != "") {
                effectiveDateForItem = effectiveDateForItem + " 00:00:00";
                effectiveDateItem(item, itemRecord, effectiveDateForItem);
            } else if (effectiveDateForItemArray.length > 1) {
                effectiveDateItem(item, itemRecord, effectiveDateForItem);
            } else {
                itemRecord.setEffectiveDate(null);
            }
        } else {
            itemRecord.setEffectiveDate(null);
        }

//        String staffOnlyFlagForItem = null;
//        if (item.getExtension() != null && item.getExtension().getContent().size() > 0 && item.getExtension().getContent().get(0) != null) {
//            AdditionalAttributes additionalAttributes = (AdditionalAttributes) item.getExtension().getContent().get(0);
//            if (additionalAttributes.getAttributeMap() != null) {
//                staffOnlyFlagForItem = additionalAttributes.getAttributeMap().get("staffOnlyFlag");
//                if (staffOnlyFlagForItem != null) {
//                    itemRecord.setStaffOnlyFlag(item.isStaffOnlyFlag());
//            }
//            }
//        } else {
//            itemRecord.setStaffOnlyFlag(item.isStaffOnlyFlag());
//        }
        if (item.getItemStatus() != null) {
            ItemStatusRecord itemStatusRecord = saveItemStatusRecord(item.getItemStatus().getCodeValue());
            itemRecord.setItemStatusId(itemStatusRecord == null ? null : itemStatusRecord.getItemStatusId());
            itemRecord.setItemStatusRecord(itemStatusRecord);
        }
        if (item.getHighDensityStorage() != null) {
            HighDensityStorageRecord highDensityStorageRecord = saveHighDensityStorageRecord(item.getHighDensityStorage());
            itemRecord.setHighDensityStorageId(highDensityStorageRecord == null ? null : highDensityStorageRecord.getHighDensityStorageId());
        }
        StringBuffer locationLevel = new StringBuffer("");
        itemRecord.setLocation(getLocation(item.getLocation(), locationLevel));
        itemRecord.setLocationLevel(locationLevel.toString());
        //itemRecord.setFormerIdentifierRecords(null);
        itemRecord.setLocationsCheckinCountRecords(null);
        itemRecord.setItemNoteRecords(null);
        if (item.getAccessInformation() != null && item.getAccessInformation().getUri() != null) {
            itemRecord.setUri(item.getAccessInformation().getUri().getValue());
        }
        if(item.isMissingPieceFlag()){
            if (item.getMissingPieceEffectiveDate() != null && !item.getMissingPieceEffectiveDate().equalsIgnoreCase("")) {
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date parsedDate = null;
                try {
                    parsedDate = df.parse(item.getMissingPieceEffectiveDate());
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
                itemRecord.setMissingPieceEffectiveDate(timestamp);
            } else {
                Timestamp timestamp = new Timestamp((new Date()).getTime());
                itemRecord.setMissingPieceEffectiveDate(timestamp);
            }
            if(item.getMissingPiecesCount()!=null){
                itemRecord.setMissingPiecesCount(item.getMissingPiecesCount());
            }
            itemRecord.setMissingPieceFlagNote(item.getMissingPieceFlagNote());
        }
        itemRecord.setMissingPieceFlag(item.isMissingPieceFlag());
        getBusinessObjectService().save(itemRecord);

        if (item.getStatisticalSearchingCode() != null) {
            ItemStatisticalSearchRecord itemStatisticalSearchRecord = saveItemStatisticalSearchCode(item.getStatisticalSearchingCode() , itemRecord.getItemId());
            List<ItemStatisticalSearchRecord> statisticalSearchRecords = new ArrayList<>();
            statisticalSearchRecords.add(itemStatisticalSearchRecord);
            itemRecord.setItemStatisticalSearchRecords((statisticalSearchRecords));
        }
        if (item.getFormerIdentifier() != null && item.getFormerIdentifier().size() > 0 && item.getFormerIdentifier().get(0).getIdentifier() != null) {
            saveFormerIdentifierRecords(item.getFormerIdentifier(), itemRecord.getItemId());
        }
        if (item.getNote() != null && item.getNote().size() > 0) {
            itemRecord.setItemNoteRecords(saveItemNoteRecord(item.getNote(), itemRecord.getItemId()));
        }
        if (item.getNumberOfCirculations() != null && item.getNumberOfCirculations().getCheckInLocation() != null && item.getNumberOfCirculations().getCheckInLocation().size() > 0) {
            saveCheckInLocationRecord(item.getNumberOfCirculations().getCheckInLocation(), itemRecord.getItemId());
        }
        if (item.getDonorInfo() != null && item.getDonorInfo().size() >= 0) {
            saveDonorList(item.getDonorInfo(), itemRecord.getItemId());
        }
        itemRecord.setNumberOfRenew(item.getNumberOfRenew());
        getBusinessObjectService().save(itemRecord);
    }

    protected StatisticalSearchRecord saveStatisticalSearchRecord(List<StatisticalSearchingCode> statisticalSearchingCodes) {

        if (statisticalSearchingCodes.size() > 0) {
            Map map = new HashMap();
            map.put("code", statisticalSearchingCodes.get(0).getCodeValue());
            List<StatisticalSearchRecord> statisticalSearchRecords = (List<StatisticalSearchRecord>) getBusinessObjectService().findMatching(StatisticalSearchRecord.class, map);
            if (statisticalSearchRecords.size() == 0) {
                if (statisticalSearchingCodes.get(0).getCodeValue() != null && !"".equals(statisticalSearchingCodes.get(0).getCodeValue())) {
                    StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchRecord();
                    statisticalSearchRecord.setCode(statisticalSearchingCodes.get(0).getCodeValue());
                    statisticalSearchRecord.setName(statisticalSearchingCodes.get(0).getFullValue());
                    getBusinessObjectService().save(statisticalSearchRecord);
                    return statisticalSearchRecord;
                } else {
                    return null;
                }
            }
            return statisticalSearchRecords.get(0);
        }
        return null;
    }

    private ItemStatisticalSearchRecord saveItemStatisticalSearchCode(List<StatisticalSearchingCode> statisticalSearchingCodes, String itemId) {
        if (statisticalSearchingCodes != null && statisticalSearchingCodes.size() > 0) {
            StatisticalSearchRecord statisticalSearchRecord = saveStatisticalSearchRecord(statisticalSearchingCodes);
            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = (List<ItemStatisticalSearchRecord>) getBusinessObjectService().findMatching(ItemStatisticalSearchRecord.class, getItemMap(itemId));
            ItemStatisticalSearchRecord itemStatisticalSearchRecord = null;

            if(itemStatisticalSearchRecords != null && itemStatisticalSearchRecords.size() > 0) {
                itemStatisticalSearchRecord = itemStatisticalSearchRecords.get(0);
            } else {
                itemStatisticalSearchRecord = new ItemStatisticalSearchRecord();
                itemStatisticalSearchRecord.setItemId(itemId);
            }
            if(statisticalSearchRecord != null) {
                itemStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                itemStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
            }
            getBusinessObjectService().save(itemStatisticalSearchRecord);
            return itemStatisticalSearchRecord;
        }
        return null;
    }


    private void effectiveDateItem(org.kuali.ole.docstore.common.document.content.instance.Item item, ItemRecord itemRecord, String effectiveDateForItem) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Timestamp effectiveDate = null;
        try {
            if (!"".equals(item.getItemStatusEffectiveDate()) && item.getItemStatusEffectiveDate() != null) {
                effectiveDate = new Timestamp(df.parse(effectiveDateForItem).getTime());
                itemRecord.setEffectiveDate(effectiveDate);
            }

        } catch (Exception e) {
            LOG.error("Effective Date for Item" + e);
        }
    }

    private void claimsReturnsCreateDateItem(org.kuali.ole.docstore.common.document.content.instance.Item item, ItemRecord itemRecord, String claimReturnCreateDate) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Timestamp claimReturnCreateDate1 = null;
        try {
            if (!"".equals(item.getClaimsReturnedFlagCreateDate()) && item.getClaimsReturnedFlagCreateDate() != null) {
                claimReturnCreateDate1 = new Timestamp(df.parse(claimReturnCreateDate).getTime());
                itemRecord.setClaimsReturnedFlagCreateDate(claimReturnCreateDate1);
            }
        } catch (Exception e) {
            LOG.error("Effective Date for Item" + e);
        }
    }

    private void dueDateTime(org.kuali.ole.docstore.common.document.content.instance.Item item, ItemRecord itemRecord, String dueDateTime) {
        Timestamp dueDateTime1 = convertDateToTimeStamp(dueDateTime);
        itemRecord.setDueDateTime(dueDateTime1);
    }

    private Timestamp convertDateToTimeStamp(String dateString) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Timestamp dueDateTime1 = null;
        try {
            if (!"".equals(dateString) && dateString != null) {
                dueDateTime1 = new Timestamp(df.parse(dateString).getTime());
            }
        } catch (Exception e) {
            LOG.error("Effective Date for Item" + e);
        }
        return dueDateTime1;
    }

    protected CallNumberTypeRecord saveCallNumberTypeRecord(ShelvingScheme scheme) {

        Map callMap = new HashMap();
        callMap.put("code", scheme.getCodeValue());
        List<CallNumberTypeRecord> callNumberTypeRecords = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, callMap);
        if (callNumberTypeRecords.size() == 0) {
            if (scheme.getCodeValue() != null && !"".equals(scheme.getCodeValue())) {
                CallNumberTypeRecord callNumberTypeRecord = new CallNumberTypeRecord();
                callNumberTypeRecord.setCode(scheme.getCodeValue());
                callNumberTypeRecord.setName(scheme.getFullValue());
                getBusinessObjectService().save(callNumberTypeRecord);
                return callNumberTypeRecord;
            } else
                return null;
        }
        return callNumberTypeRecords.get(0);
    }

    protected String getLocation(Location location, StringBuffer locationLevel) {
        StringBuffer locationName = new StringBuffer("");
        //StringBuffer locationLevel = new StringBuffer("");
        if (location != null && location.getLocationLevel() != null) {
            locationName = locationName.append(location.getLocationLevel().getName());
            locationLevel = locationLevel.append(location.getLocationLevel().getLevel());

            if (location.getLocationLevel().getLocationLevel() != null) {
                locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getName());
                locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLevel());

                if (location.getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                    locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getName());
                    locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                    if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                        locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                        locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                        if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                            locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                            locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());
                        }
                    }
                }
            }
        }
        return locationName.toString();
    }

    protected HighDensityStorageRecord saveHighDensityStorageRecord(HighDensityStorage  highDensityStorage) {

        HighDensityStorageRecord highDensityStorageRecord = new HighDensityStorageRecord();
        highDensityStorageRecord.setRow(highDensityStorage.getRow());
        highDensityStorageRecord.setModule(highDensityStorage.getModule());
        highDensityStorageRecord.setShelf(highDensityStorage.getShelf());
        getBusinessObjectService().save(highDensityStorageRecord);
        return highDensityStorageRecord;
    }


    protected ItemStatusRecord saveItemStatusRecord(String itemStatus) {
        Map map = new HashMap();
        map.put("code", itemStatus);
        List<ItemStatusRecord> itemStatusRecords = (List<ItemStatusRecord>) getBusinessObjectService().findMatching(ItemStatusRecord.class, map);
        if (itemStatusRecords.size() == 0) {
            if (itemStatus != null && !"".equals(itemStatus)) {
                ItemStatusRecord itemStatusRecord = new ItemStatusRecord();
                itemStatusRecord.setCode(itemStatus);
                itemStatusRecord.setName(itemStatus);
                getBusinessObjectService().save(itemStatusRecord);
                return itemStatusRecord;
            } else {
                return null;
            }
        }
        return itemStatusRecords.get(0);
    }

    protected void saveFormerIdentifierRecords(List<FormerIdentifier> formerIdentifierList, String itemId) {
        Map map = new HashMap();
        map.put("itemId", itemId);
        List<FormerIdentifierRecord> formerIdentifierRecordList = (List<FormerIdentifierRecord>) getBusinessObjectService().findMatching(FormerIdentifierRecord.class, map);
        if (formerIdentifierList.size() > 0) {
            List<FormerIdentifierRecord> formerIdentifierRecords = new ArrayList<FormerIdentifierRecord>();
            for (int i = 0; i < formerIdentifierList.size(); i++) {
                FormerIdentifier formerIdentifier = formerIdentifierList.get(i);
                if (formerIdentifier.getIdentifier() != null && formerIdentifier.getIdentifier().getIdentifierValue() != null && !"".equals(formerIdentifier.getIdentifier().getIdentifierValue())) {

                    FormerIdentifierRecord formerIdentifierRecord = new FormerIdentifierRecord();
                    if (i < formerIdentifierRecordList.size()) {
                        formerIdentifierRecord = formerIdentifierRecordList.get(i);
                    }
                    formerIdentifierRecord.setType(formerIdentifier.getIdentifierType());
                    if (formerIdentifier.getIdentifier() != null)
                        formerIdentifierRecord.setValue(formerIdentifier.getIdentifier().getIdentifierValue());
                    formerIdentifierRecord.setItemId(itemId);
                    formerIdentifierRecords.add(formerIdentifierRecord);
                }
            }
            if (formerIdentifierRecords.size() > 0) {
                getBusinessObjectService().save(formerIdentifierRecords);
            }

            if (formerIdentifierRecordList.size() > formerIdentifierList.size()) {
                getBusinessObjectService().delete(formerIdentifierRecordList.subList(formerIdentifierList.size() - 1, formerIdentifierRecordList.size()));
            }
        }
    }

    protected List<ItemNoteRecord> saveItemNoteRecord(List<Note> noteList, String itemId) {

        Map map = new HashMap();
        map.put("itemId", itemId);
        List<ItemNoteRecord> itemNoteRecordList = (List<ItemNoteRecord>) getBusinessObjectService().findMatching(ItemNoteRecord.class, map);
        getBusinessObjectService().delete(itemNoteRecordList);
        itemNoteRecordList.clear();

        if (noteList.size() > 0) {
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                    ItemNoteRecord itemNoteRecord = new ItemNoteRecord();
                    itemNoteRecord.setType(note.getType());
                    itemNoteRecord.setNote(note.getValue());
                    itemNoteRecord.setItemId(itemId);
                    itemNoteRecordList.add(itemNoteRecord);
                }
            }
            if (itemNoteRecordList.size() > 0) {
                getBusinessObjectService().save(itemNoteRecordList);
            }

        }
        return itemNoteRecordList;
    }

    protected void saveCheckInLocationRecord(List<CheckInLocation> checkInLocationList, String itemId) {

        Map map = new HashMap();
        map.put("itemId", itemId);
        List<LocationsCheckinCountRecord> locationsCheckinCountRecordList = (List<LocationsCheckinCountRecord>) getBusinessObjectService().findMatching(LocationsCheckinCountRecord.class, map);
        if (locationsCheckinCountRecordList != null && locationsCheckinCountRecordList.size() > 0) {
            LocationsCheckinCountRecord locationsCheckinCountRecord = locationsCheckinCountRecordList.get(0);
            CheckInLocation checkInLocation = checkInLocationList.get(0);
            locationsCheckinCountRecord.setLocationCount(checkInLocation.getCount());
            locationsCheckinCountRecord.setLocationName(checkInLocation.getName());
            locationsCheckinCountRecord.setLocationInhouseCount(checkInLocation.getInHouseCount());
            getBusinessObjectService().save(locationsCheckinCountRecord);
        } else {

            CheckInLocation checkInLocation = checkInLocationList.get(0);
            LocationsCheckinCountRecord locationsCheckinCountRecord = new LocationsCheckinCountRecord();
            locationsCheckinCountRecord.setLocationCount(checkInLocation.getCount());
            locationsCheckinCountRecord.setLocationName(checkInLocation.getName());
            locationsCheckinCountRecord.setLocationInhouseCount(checkInLocation.getInHouseCount());
            locationsCheckinCountRecord.setItemId(itemId);
            getBusinessObjectService().save(locationsCheckinCountRecord);

        }

    }

    protected void saveDonorList(List<DonorInfo> donorslist, String itemId) {
        Map map = new HashMap();
        map.put("itemId", itemId);
        List<OLEItemDonorRecord> itemDonorRecordList = (List<OLEItemDonorRecord>) getBusinessObjectService().findMatching(OLEItemDonorRecord.class, map);
        if(itemDonorRecordList!=null && itemDonorRecordList.size() >= 0) {
            getBusinessObjectService().delete(itemDonorRecordList);
        }
        if (donorslist.size() > 0) {
            List<OLEItemDonorRecord> oleItemDonorRecords = new ArrayList<OLEItemDonorRecord>();
            for (int i = 0; i < donorslist.size(); i++) {
                DonorInfo donorinfo = donorslist.get(i);
                if (donorinfo.getDonorCode() != null ) {
                    OLEItemDonorRecord oleItemDonorRecord = new OLEItemDonorRecord();
                    oleItemDonorRecord.setDonorPublicDisplay(donorinfo.getDonorPublicDisplay());
                    oleItemDonorRecord.setDonorCode(donorinfo.getDonorCode());
                    oleItemDonorRecord.setDonorNote(donorinfo.getDonorNote());
                    oleItemDonorRecord.setItemId(itemId);
                    oleItemDonorRecords.add(oleItemDonorRecord);
                }
            }
            if (oleItemDonorRecords.size() > 0) {
                getBusinessObjectService().save(oleItemDonorRecords);
            }
        }
    }

    protected ItemTypeRecord saveItemTypeRecord(ItemType itemType) {
        Map map = new HashMap();
        map.put("code", itemType.getCodeValue());
        List<ItemTypeRecord> itemTypeRecords = (List<ItemTypeRecord>) getBusinessObjectService().findMatching(ItemTypeRecord.class, map);
        if (itemTypeRecords.size() == 0) {
            if (itemType.getCodeValue() != null && !"".equals(itemType.getCodeValue())) {
                ItemTypeRecord itemTypeRecord = new ItemTypeRecord();
                itemTypeRecord.setCode(itemType.getCodeValue());
                itemTypeRecord.setName(itemType.getFullValue());
                getBusinessObjectService().save(itemTypeRecord);
                return itemTypeRecord;
            } else {
                return null;
            }
        }
        return itemTypeRecords.get(0);

    }

    private Location getLocationDetails(String locationName, String locationLevelName) {
        Location location = new Location();
        //LocationLevel locationLevel = new LocationLevel();
        LocationLevel locationLevel = createLocationLevel(locationName, locationLevelName);
        location.setLocationLevel(locationLevel);
        return location;
    }

    public LocationLevel createLocationLevel(String locationName, String locationLevelName) {
        LocationLevel locationLevel = null;
        if (StringUtils.isNotEmpty(locationName ) && StringUtils.isNotEmpty(locationLevelName)) {
            String[] locations = locationName.split("/");
            String[] locationLevels = locationLevelName.split("/");
            String locName = "";
            String levelName = "";
            if (locations.length > 0 ) {
                locName = locations[0];
                levelName = locationLevels[0];
                if (locationName.contains("/")) {
                    locationName = locationName.replaceFirst(locations[0] + "/", "");
                } else {
                    locationName = locationName.replace(locations[0], "");
                }

                if (locationLevelName.contains("/")) {
                    locationLevelName = locationLevelName.replaceFirst(locationLevels[0] + "/", "");
                } else {
                    locationLevelName = locationLevelName.replace(locationLevels[0], "");
                }
                if (locName != null && locations.length != 0) {
                    locationLevel = new LocationLevel();
                    locationLevel.setLevel(levelName);
                    locationLevel.setName(locName);
                    locationLevel.setLocationLevel(createLocationLevel(locationName, locationLevelName));
                }
            }
        }
        return locationLevel;
    }

    public void transferItems(List<String> itemIds, String holdingsId) {
        String destInstanceIdentifier = holdingsId;
        Map itemMap = new HashMap();
        for (String itemId: itemIds) {
            itemMap.put("itemId", DocumentUniqueIDPrefix.getDocumentId(itemId));
            ItemRecord itemRecord = getBusinessObjectService().findByPrimaryKey(ItemRecord.class, itemMap);
            itemRecord.setHoldingsId(DocumentUniqueIDPrefix.getDocumentId(destInstanceIdentifier));
            getBusinessObjectService().save(itemRecord);
        }
    }

    private void validateItem(Item item) {
        itemBarcodeValidation(item);
        if (item.getCallNumber() != null) {
            Map itemMap = new HashMap();
            itemMap.put("itemId", DocumentUniqueIDPrefix.getDocumentId(item.getId()));
            List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
            if (itemRecords != null && itemRecords.size() > 0) {
                Map instanceMap = new HashMap();
                instanceMap.put("holdingsId", itemRecords.get(0).getHoldingsId());
                List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, instanceMap);
                HoldingsRecord holdingsRecord = null;
                if (holdingsRecords != null && holdingsRecords.size() > 0) {
                    holdingsRecord = holdingsRecords.get(0);
                    getHoldingsContentNValidateItem(holdingsRecord, item);
                }
            }
        }
    }

    public void itemBarcodeValidation(Item item) {
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item itemDocument = itemOlemlRecordProcessor.fromXML(item.getContent());
        Map itemMap = new HashMap();
        if (itemDocument != null && itemDocument.getAccessInformation() != null && StringUtils.isNotEmpty(itemDocument.getAccessInformation().getBarcode())) {
            itemMap.put("barCode", itemDocument.getAccessInformation().getBarcode());
            List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
            if (itemRecords.size() > 0) {
                if (itemRecords.size() == 1) {
                    ItemRecord itemRecord = itemRecords.get(0);
                    String documentId = DocumentUniqueIDPrefix.getDocumentId(itemDocument.getItemIdentifier());
                    String itemId = DocumentUniqueIDPrefix.getDocumentId(itemRecord.getItemId());
                    if (documentId != null && documentId.equals(itemId)) {
                        return;
                    }
                }
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BARCODE_EXISTS, DocstoreResources.BARCODE_EXISTS);
                docstoreException.addErrorParams("barcode", itemDocument.getAccessInformation().getBarcode());
                throw docstoreException;
            }
        }
    }

    private void getHoldingsContentNValidateItem(HoldingsRecord holdingsRecord, Item item){
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = new RdbmsHoldingsDocumentManager();
        Holdings holdings = rdbmsHoldingsDocumentManager.buildHoldingsFromHoldingsRecord(holdingsRecord);
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item itemDocument = itemOlemlRecordProcessor.fromXML(item.getContent());
        validateCallNumber(itemDocument.getCallNumber(), oleHoldings);
    }

    public void validateCallNumber(CallNumber itemCNum, OleHoldings holdings) {
        // item call number and type verification
        if ((itemCNum.getNumber() != null && itemCNum.getNumber().length() > 0)) {
            validateCNumNCNumType(itemCNum);
            validateShelvingOrderNCNum(itemCNum);
        }
        // if item call number is null consider holdings call number
        else if (holdings != null) {
            if (holdings.getCallNumber() != null) {
                CallNumber holCNum = holdings.getCallNumber();
                validateCNumNCNumType(holCNum);
                // consider item shelving order and holdings call number information.
                if (itemCNum.getShelvingOrder() != null && itemCNum.getShelvingOrder().getFullValue() != null &&
                        itemCNum.getShelvingOrder().getFullValue().trim().length() > 0) {
                    if (!(holCNum.getNumber() != null && holCNum.getNumber().length() > 0)) {
                        DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.CALL_NUMBER_INFO, DocstoreResources.CALL_NUMBER_INFO);
                        throw docstoreException;
//                        throw new OleDocStoreException("Shelving order value is available, Please enter call number information");
                    }
                }
            }
            // item shelving order is not null and holdings call number is null
            else if (itemCNum.getShelvingOrder() != null && itemCNum.getShelvingOrder().getFullValue() != null &&
                    itemCNum.getShelvingOrder().getFullValue().trim().length() > 0) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.CALL_NUMBER_INFO, DocstoreResources.CALL_NUMBER_INFO);
                throw docstoreException;
//                throw new OleDocStoreException("Shelving order value is available, Please enter call number information");
            }

        }
    }

    private void validateCNumNCNumType(CallNumber cNum) {
        String callNumber = "";
        String callNumberType = "";
        // Get callNumber and callNumberType
        if (cNum != null) {
            callNumber = cNum.getNumber();
            if (cNum.getShelvingScheme() != null) {
                callNumberType = cNum.getShelvingScheme().getCodeValue();
            }
        }
        // Check if CallNumber is present
        if (StringUtils.isNotEmpty(callNumber)) {
            // Check if callNumberType is empty or #
            if ((callNumberType == null) || (callNumberType.length() == 0) || (callNumberType.equals("NOINFO"))) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.CALL_NUMBER_TYPE_INFO, DocstoreResources.CALL_NUMBER_TYPE_INFO);
                throw docstoreException;
//                throw new OleDocStoreException("Please enter valid call number type value in call number information ");
            }
        }
    }

    private void validateShelvingOrderNCNum(CallNumber cNum) {
        if (cNum.getShelvingOrder() != null && cNum.getShelvingOrder().getFullValue() != null &&
                cNum.getShelvingOrder().getFullValue().trim().length() > 0) {
            if (!(cNum.getNumber() != null && cNum.getNumber().length() > 0)) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.CALL_NUMBER_TYPE_INFO, DocstoreResources.CALL_NUMBER_TYPE_INFO);
                throw docstoreException;
//                throw new OleDocStoreException("Shelving order value is available, so please enter call number information");
            }
        }
    }

    public void validateMissingPieces(Item itemDoc) {

        org.kuali.ole.docstore.common.document.content.instance.Item item = itemOlemlRecordProcessor.fromXML(itemDoc.getContent());
            if (item.isMissingPieceFlag()) {
                Boolean isValid=true;
                if(item.getNumberOfPieces() == null || (item.getNumberOfPieces() != null && item.getNumberOfPieces().equals(""))){
                    isValid=false;
                    DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.NO_PIECE_FLAG_BASED_ERROR_VALIDATION, DocstoreResources.NO_PIECE_FLAG_BASED_ERROR_VALIDATION);
                    throw docstoreException;
                }
                if(isValid && (!item.getNumberOfPieces().equals("")) ){
                    int noOfPieces=Integer.parseInt(item.getNumberOfPieces());
                    if(noOfPieces<1){
                        DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.NO_PIECE_FLAG_BASED_ERROR_GREATER, DocstoreResources.NO_PIECE_FLAG_BASED_ERROR_GREATER);
                        throw docstoreException;
                    }
                }
                if(item.getMissingPiecesCount() == null || (item.getMissingPiecesCount() != null && item.getMissingPiecesCount().equals(""))){
                    isValid=false;
                    DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.MISSING_PIECE_FLAG_BASED_ERROR_BLANK, DocstoreResources.MISSING_PIECE_FLAG_BASED_ERROR_BLANK);
                    throw docstoreException;
                }
                if(isValid && (!item.getMissingPiecesCount().equals("")) ){

                    int noOfMissingPieceCount=Integer.parseInt(item.getMissingPiecesCount());
                    if(noOfMissingPieceCount<1){
                        DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.MISSING_PIECE_FLAG_BASED_ERROR_GREATER, DocstoreResources.MISSING_PIECE_FLAG_BASED_ERROR_GREATER);
                        throw docstoreException;
                    }
                }
                if (isValid && item.getMissingPiecesCount() != null && item.getNumberOfPieces() != null && !item.getNumberOfPieces().equals("") && !item.getMissingPiecesCount().equals("")) {
                    int noOfPieces = Integer.parseInt(item.getNumberOfPieces());
                    int missingPieceCount = Integer.parseInt(item.getMissingPiecesCount());
                    if (missingPieceCount > noOfPieces) {
                        DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.MISSING_PIECE_FLAG_BASED_ERROR_COUNT, DocstoreResources.MISSING_PIECE_FLAG_BASED_ERROR_COUNT);
                        throw docstoreException;
                    }
                }
            }
    }

    public String getNormalized(String enumeration) {
        Pattern validPattern = Pattern.compile("[0-9]+");
        int leftPadSize = 0;
        if (leftPaddingSize != null) {
            leftPadSize = Integer.parseInt(leftPaddingSize);
        }
        Matcher matcher = validPattern.matcher(enumeration);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, StringUtils.leftPad(matcher.group(), leftPadSize, "0"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String normalizeFloatForEnumeration(String floatStr, int digitsB4) {
        String replacString = floatStr.replaceAll("[^a-zA-Z0-9]+", "");
        double value = Double.valueOf(replacString).doubleValue();
        String formatStr = getFormatString(digitsB4);
        DecimalFormat normFormat = new DecimalFormat(formatStr);
        String norm = normFormat.format(value);
        if (norm.endsWith("."))
            norm = norm.substring(0, norm.length() - 1);
        return norm;
    }

    private String getFormatString(int numDigits) {
        StringBuilder b4 = new StringBuilder();
        if (numDigits < 0)
            b4.append("############");
        else if (numDigits > 0) {
            for (int i = 0; i < numDigits; i++) {
                b4.append('0');
            }
        }
        return b4.toString();
    }

    public Item retrieveItemByBarcode(String barcode) {
        Item item = null;
        Map itemMap = new HashMap();
        itemMap.put("barCode", barcode);
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
        if (itemRecords != null && itemRecords.size() > 0) {
            ItemRecord itemRecord = itemRecords.get(0);
            //if(holdings == null) {
                HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(itemRecord.getHoldingsId()));
               Holdings holdings = retrieveHoldings(holdingsRecord.getHoldingsId(), null, holdingsRecord);
            //}
            item = buildItemContent(itemRecord);
            item.setHolding(holdings);
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BARCODE_NOT_EXISTS, DocstoreResources.BARCODE_NOT_EXISTS);
            docstoreException.addErrorParams("barCode", barcode);
            throw docstoreException;
        }
        return item;
    }


    protected Item retrieveItem(String id, Holdings holdings, ItemRecord itemRecord) {
        if(itemRecord == null) {
            itemRecord = getBusinessObjectService().findByPrimaryKey(ItemRecord.class, getItemMap(id));
            if(itemRecord == null) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ITEM_ID_NOT_FOUND, DocstoreResources.ITEM_ID_NOT_FOUND);
                docstoreException.addErrorParams("itemId", id);
                throw docstoreException;
            }
        }

        if(holdings == null) {
            HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(itemRecord.getHoldingsId()));
            holdings = retrieveHoldings(holdingsRecord.getHoldingsId(), null, holdingsRecord);
        }

        Item item = buildItemContent(itemRecord);
        item.setHolding(holdings);

        return item;
    }

    protected List<Holdings> retrieveHoldingsRecordsFromAnalytic(String id, Bib bib, HoldingsRecord holdingsRecord) {
        List<HoldingsItemRecord> holdingsItemRecords = (List<HoldingsItemRecord>) getBusinessObjectService().findMatching(HoldingsItemRecord.class, getItemMap(id));
        List<Holdings> holdingsList = new ArrayList<>();
        for(HoldingsItemRecord holdingsItemRecord : holdingsItemRecords) {
            holdingsList.add(retrieveHoldings(holdingsItemRecord.getHoldingsId(), bib, holdingsRecord));
        }
        return holdingsList;
    }

    /**
     * This method verifies the existence of linked documents to the item record. If exists throws exception with appropriate error message.
     * @param itemId
     */
    @Override
    public void deleteVerify(String itemId) {
        if (itemId != null) {
            Item item = retrieveItem(itemId, null, null);
            if (null != item && null != item.getId()) {
                if (item.isAnalytic()) {
                    DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ANALYTIC_DELETE_MESSAGE_ITEM, DocstoreResources.ANALYTIC_DELETE_MESSAGE_ITEM);
                    throw docstoreException;
                }
            }
            checkUuidsToDelete(itemId, 1);
        }
    }
}
