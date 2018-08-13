package org.kuali.ole.indexer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.common.DocumentUniqueIDPrefix;
import org.kuali.ole.common.enums.DocCategory;
import org.kuali.ole.common.exception.DocstoreIndexException;
import org.kuali.ole.common.util.CallNumberUtil;
import org.kuali.ole.common.util.EnumerationUtil;
import org.kuali.ole.model.jpa.*;
import org.kuali.ole.model.solr.RecordCountAndSolrDocumentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by SheikS on 11/30/2015.
 */
public class ItemIndexer extends OleDsNgIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(ItemIndexer.class);
    
    @Override
    public void deleteDocument(String id) {

    }

    public RecordCountAndSolrDocumentMap getInputDocumentForItem(ItemRecord itemRecord, RecordCountAndSolrDocumentMap recordCountAndSolrDocumentMap) {
        if(null == recordCountAndSolrDocumentMap) {
            recordCountAndSolrDocumentMap = new RecordCountAndSolrDocumentMap();
        }
        Map<String, SolrInputDocument> solrInputDocumentMap = recordCountAndSolrDocumentMap.getSolrInputDocumentMap();
        try {
            SolrInputDocumentAndDocumentMap solrInputDocumentAndDocumentMap = buildSolrInputDocument(itemRecord, solrInputDocumentMap);
            SolrInputDocument itemSolrInputDocument = solrInputDocumentAndDocumentMap.getSolrInputDocument();
            solrInputDocumentMap = solrInputDocumentAndDocumentMap.getMap();

            //***********************
            Integer holdingsId = itemRecord.getHoldingsRecord().getHoldingsId();
            String holdingsIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(
                    DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, String.valueOf(holdingsId));
            if (holdingsId != null) {
                itemSolrInputDocument.addField(HOLDINGS_IDENTIFIER, holdingsIdentifierWithPrefix);
            }

            SolrInputDocument holdingSolrInputDocuemnt = getSolrInputDocumentFromMap(solrInputDocumentMap, holdingsIdentifierWithPrefix);
            if(null == holdingSolrInputDocuemnt) {
                // Todo : Need to Build for Holdings
            }

            if(null != holdingSolrInputDocuemnt) {
                Object bibs = holdingSolrInputDocuemnt.getFieldValues(BIB_IDENTIFIER);
                addItemDetailsToHoldings(itemSolrInputDocument, holdingSolrInputDocuemnt);

                addBibInfoForHoldingsOrItems(itemSolrInputDocument, holdingSolrInputDocuemnt);
                addHoldingsInfoToItem(itemSolrInputDocument, holdingSolrInputDocuemnt);

                itemSolrInputDocument.addField("bibIdentifier", bibs);  // Todo Need to verify


                // Todo : Need to populate the holdings record
                // Todo : Need to do auto fetch (Lazy Loading)
                HoldingsRecord holdingsRecord = itemRecord.getHoldingsRecord();
                String bibId = "";
                if(null != holdingsRecord) {
                    bibId = String.valueOf(holdingsRecord.getBibRecord().getBibId());
                }
                String bibIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(
                        DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, String.valueOf(bibId));
                SolrInputDocument bibSolrInputDocuemnt = getSolrInputDocumentFromMap(solrInputDocumentMap, bibIdentifierWithPrefix);
                if(null == bibSolrInputDocuemnt) {
                    // Todo : Need to Build for bib
                }
                addSolrInputDocumentToMap(solrInputDocumentMap,itemSolrInputDocument);
                addSolrInputDocumentToMap(solrInputDocumentMap,holdingSolrInputDocuemnt);
                bibSolrInputDocuemnt = addItemDetailsToBib(itemSolrInputDocument, bibSolrInputDocuemnt);
                if(null != bibSolrInputDocuemnt) {
                    addSolrInputDocumentToMap(solrInputDocumentMap, bibSolrInputDocuemnt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            reportUtil.saveExceptionReportForItem(itemRecord, e);
        }

        recordCountAndSolrDocumentMap.setSolrInputDocumentMap(solrInputDocumentMap);

        return recordCountAndSolrDocumentMap;
    }

    @Override
    public SolrInputDocumentAndDocumentMap buildSolrInputDocument(Object object, Map<String, SolrInputDocument> parameterMap) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        ItemRecord itemRecord = (ItemRecord) object;
        try {
            String itemIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML, String.valueOf(itemRecord.getItemId()));

            solrInputDocument.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
            solrInputDocument.addField(DOC_TYPE, DOC_TYPE_ITEM_VALUE);
            solrInputDocument.addField(DOC_FORMAT, DOC_FORMAT_INSTANCE_VALUE);
            solrInputDocument.addField(ID, itemIdentifierWithPrefix);
            solrInputDocument.addField(ITEM_IDENTIFIER, itemIdentifierWithPrefix);
            solrInputDocument.addField(LOCALID_DISPLAY, itemRecord.getItemId());
            solrInputDocument.addField(LOCALID_SEARCH, itemRecord.getItemId());


            solrInputDocument.addField(CLMS_RET_FLAG, getBooleanValueYorN(itemRecord.getClaimsReturned()));
            Date claimsReturnedDateCreated = itemRecord.getClaimsReturnedDateCreated();
            solrInputDocument.addField(CLMS_RET_FLAG_CRE_DATE, convertDateToString(DOCSTORE_DATE_FORMAT, claimsReturnedDateCreated));
            solrInputDocument.addField(CLMS_RET_NOTE, itemRecord.getClaimsReturnedNote());
            solrInputDocument.addField(CURRENT_BORROWER, itemRecord.getCurrentBorrower());
            solrInputDocument.addField(PROXY_BORROWER, itemRecord.getProxyBorrower());
            String dueDateString = convertDateToString(DOCSTORE_DATE_FORMAT, itemRecord.getDueDateTime());
            solrInputDocument.addField(DUE_DATE_TIME, dueDateString);
            String originalDueDateString = convertDateToString(DOCSTORE_DATE_FORMAT, itemRecord.getOrgDueDateTime());
            solrInputDocument.addField(ORG_DUE_DATE_TIME, originalDueDateString);
            solrInputDocument.addField(ITEM_STATUS_EFFECTIVE_DATE, convertDateToString(DOCSTORE_DATE_FORMAT, itemRecord.getItemStatusDateUpdated()));
            solrInputDocument.addField(CHECK_OUT_DUE_DATE_TIME, convertDateToString(DOCSTORE_DATE_FORMAT, itemRecord.getCheckOutDateTime()));
            solrInputDocument.addField(STAFF_ONLY_FLAG, getBooleanValueYorN(itemRecord.getStaffOnly()));
//        solrInputDocument.addField(IS_ANALYTIC, itemRecord.isAnalytic()); // Todo : Need to verify (Ans : Need to verify with bib status) (HoldingsItemRecord - holdingsId and ItemId)
            solrInputDocument.addField(ITEM_IDENTIFIER_SEARCH, itemIdentifierWithPrefix);
            solrInputDocument.addField(BARCODE_ARSL_SEARCH, itemRecord.getBarcodeArsl());
            solrInputDocument.addField(COPY_NUMBER_SEARCH, itemRecord.getCopyNumber());
//        solrInputDocument.addField(COPY_NUMBER_LABEL_SEARCH, itemRecord.getCopyNumberLabel()); // Todo : Need to verify
            solrInputDocument.addField(PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_SEARCH, itemRecord.getPurchaseOrderLineItemId());
            solrInputDocument.addField(VENDOR_LINE_ITEM_IDENTIFIER_SEARCH, itemRecord.getVendorLineItemId());
//        solrInputDocument.addField(VOLUME_NUMBER_LABEL_SEARCH, itemRecord.getVolumeNumberLabel()); // Todo :Need to verify
//        solrInputDocument.addField(VOLUME_NUMBER_SEARCH, itemRecord.getVolumeNumberLabel());// Todo :Need to verify
            solrInputDocument.addField(ENUMERATION_SEARCH, itemRecord.getEnumeration());
            solrInputDocument.addField(CHRONOLOGY_SEARCH, itemRecord.getChronology());
            solrInputDocument.addField(MISSING_PIECE_FLAG_NOTE_SEARCH, itemRecord.getMissingPiecesNote());
            solrInputDocument.addField(CLAIMS_RETURNED_NOTE_SEARCH, itemRecord.getClaimsReturnedNote());
            solrInputDocument.addField(DAMAGED_ITEM_NOTE_SEARCH, itemRecord.getItemDamagedNote());
            solrInputDocument.addField(MISSING_PIECE_FLAG_SEARCH, getBooleanValueYorN(itemRecord.getMissingPieces()));
            solrInputDocument.addField(CLAIMS_RETURNED_FLAG_SEARCH, getBooleanValueYorN(itemRecord.getClaimsReturned()));
            solrInputDocument.addField(ITEM_DAMAGED_FLAG_SEARCH, getBooleanValueYorN(itemRecord.getItemDamagedStatus()));
            solrInputDocument.addField(MISSING_PIECE_COUNT_SEARCH,itemRecord.getMissingPiecesCount());
            solrInputDocument.addField(NUMBER_OF_PIECES_SEARCH,itemRecord.getNumPieces());

            Date date = new Date();
            // Item call number should be indexed if it is available at item level or holdings level.
            String itemCallNumber = null;
            if (StringUtils.isNotEmpty(itemRecord.getCallNumber())) {
                solrInputDocument.addField(CALL_NUMBER_SEARCH, itemRecord.getCallNumber());
                solrInputDocument.addField(CALL_NUMBER_DISPLAY, itemRecord.getCallNumber());
                solrInputDocument.setField(CALL_NUMBER_SORT, itemRecord.getCallNumber());
                solrInputDocument.addField(CALL_NUMBER_PREFIX_SEARCH, itemRecord.getCallNumberPrefix());
                solrInputDocument.addField(CALL_NUMBER_PREFIX_DISPLAY, itemRecord.getCallNumberPrefix());

                //Shelving scheme code should be indexed if it is available at holdings level
                String shelvingSchemeCode = "";
                String shelvingSchemeValue = "";

                CallNumberTypeRecord callNumberTypeRecord = getOleMemorizeService().getCallNumberTypeRecordById(getLongValue(itemRecord.getCallNumberTypeId()));
                if (callNumberTypeRecord != null) {
                    shelvingSchemeCode = callNumberTypeRecord.getShvlgSchmCd();
                    shelvingSchemeValue = callNumberTypeRecord.getShvlgSchmNm();
                    if (StringUtils.isNotEmpty(shelvingSchemeCode)) {
                        solrInputDocument.addField(SHELVING_SCHEME_CODE_SEARCH, shelvingSchemeCode);
                        solrInputDocument.addField(SHELVING_SCHEME_CODE_DISPLAY, shelvingSchemeCode);
                    }
                    if (StringUtils.isNotEmpty(shelvingSchemeValue)) {
                        solrInputDocument.addField(SHELVING_SCHEME_VALUE_SEARCH, shelvingSchemeValue);
                        solrInputDocument.addField(SHELVING_SCHEME_VALUE_DISPLAY, shelvingSchemeValue);
                    }
                }

                String shelvingOrder = null;
                //TODO:Shelving order not present at item level
                if (StringUtils.isNotBlank(itemRecord.getShelvingOrder())) {
                    shelvingOrder = itemRecord.getShelvingOrder();
                }else if (StringUtils.isEmpty(shelvingOrder) && itemRecord.getCallNumber() != null) {
                    try {
                        //Build sortable key for a valid call number
                        if (callNumberTypeRecord != null) {
                            if(StringUtils.isNotEmpty(itemCallNumber) && itemCallNumber.trim().length() > 0) {
                                shelvingOrder = new CallNumberUtil().buildSortableCallNumber(itemCallNumber, itemRecord.getShelvingOrder());
                            }
                        }
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
                if (StringUtils.isNotEmpty(shelvingOrder)) {
                    shelvingOrder = shelvingOrder.replaceAll(" ", "-");
                    solrInputDocument.addField(SHELVING_ORDER_SORT, shelvingOrder + itemIdentifierWithPrefix);
                    solrInputDocument.addField(SHELVING_ORDER_SEARCH, shelvingOrder);
                    solrInputDocument.addField(SHELVING_ORDER_DISPLAY, shelvingOrder);
                }
                if (itemRecord.getCallNumberPrefix() != null) {
                    solrInputDocument.addField(CALLNUMBER_PREFIX_SORT, itemRecord.getCallNumberPrefix());
                }
                if (itemRecord.getCallNumber() != null) {
                    solrInputDocument.setField(CALLNUMBER_SORT, itemRecord.getCallNumber());
                }
                if (itemRecord.getEnumeration() != null) {
                    String enumerationSort = new EnumerationUtil().getNormalizedEnumeration(itemRecord.getEnumeration());
                    solrInputDocument.addField(ENUMERATION_SORT, enumerationSort);
                }
                if (itemRecord.getChronology() != null) {
                    solrInputDocument.addField(CHRONOLOGY_SORT, itemRecord.getChronology());
                }
                if (itemRecord.getCopyNumber() != null) {
                    String copyNumberSort = new EnumerationUtil().getNormalizedEnumeration(itemRecord.getCopyNumber());
                    solrInputDocument.addField(COPYNUMBER_SORT, copyNumberSort);
                }
                if (null != itemRecord.getBarcode()) {
                    solrInputDocument.addField(ITEM_BARCODE_SORT, itemRecord.getBarcode());
                }
            }
            ItemStatusRecord itemStatusRecord = getOleMemorizeService().getItemStatusById(getStringValue(itemRecord.getItemStatusId()));
            if (itemStatusRecord != null) {
                solrInputDocument.addField(ITEM_STATUS_DISPLAY, itemStatusRecord.getItemAvailStatCd());
                solrInputDocument.addField(ITEM_STATUS_SEARCH, itemStatusRecord.getItemAvailStatNm());
                solrInputDocument.addField(ITEM_STATUS_SORT, itemStatusRecord.getItemAvailStatNm());
            }

            StringBuffer loactionLevelStr = new StringBuffer(" ");
            if (itemRecord.getLocation() != null &&
                    itemRecord.getLocationLevel() != null) {
                solrInputDocument.addField(LOCATION_LEVEL_SEARCH, itemRecord.getLocation());
                solrInputDocument.addField(LOCATION_LEVEL_NAME_SEARCH, itemRecord.getLocationLevel());
                solrInputDocument.addField(LOCATION_LEVEL_DISPLAY, itemRecord.getLocation());
                solrInputDocument.addField(LOCATION_LEVEL_NAME_DISPLAY, itemRecord.getLocationLevel());
                solrInputDocument.addField(LOCATION_LEVEL_SORT, itemRecord.getLocation());
                StringTokenizer locationTokenizer = new StringTokenizer(itemRecord.getLocation(),"/");
                StringTokenizer locationLevelTokenizer = new StringTokenizer(itemRecord.getLocationLevel(),"/");
                while(locationLevelTokenizer.hasMoreTokens()){
                    String locationLevel = locationLevelTokenizer.nextToken();
                    String location = locationTokenizer.nextToken();
                    addLocationLevelsToSolrInputodument(location,locationLevel,solrInputDocument,loactionLevelStr);
                }
            }

            solrInputDocument.addField(ALL_TEXT, getAllTextValueForItem(itemRecord) + loactionLevelStr.toString());

            ItemTypeRecord itemTypeRecord = getOleMemorizeService().getItemTypeById(getStringValue(itemRecord.getItemTypeId()));
            if (itemTypeRecord != null) {
                solrInputDocument.addField(ITEM_TYPE_FULL_VALUE_SEARCH, itemTypeRecord.getItmTypNm());
                solrInputDocument.addField(ITEM_TYPE_CODE_VALUE_SEARCH, itemTypeRecord.getItmTypCd());
                solrInputDocument.addField(ITEM_TYPE_FULL_VALUE_DISPLAY, itemTypeRecord.getItmTypNm());
                solrInputDocument.addField(ITEM_TYPE_CODE_VALUE_DISPLAY, itemTypeRecord.getItmTypCd());
            }

            ItemTypeRecord tempItemTypeRecord = getOleMemorizeService().getItemTypeById(getStringValue(itemRecord.getTempItemTypeId()));
            if (tempItemTypeRecord != null) {
                solrInputDocument.addField(TEMPORARY_ITEM_TYPE_FULL_VALUE_SEARCH, tempItemTypeRecord.getItmTypNm());
                solrInputDocument.addField(TEMPORARY_ITEM_TYPE_CODE_VALUE_SEARCH, tempItemTypeRecord.getItmTypCd());
                solrInputDocument.addField(TEMPORARY_ITEM_TYPE_FULL_VALUE_DISPLAY, tempItemTypeRecord.getItmTypNm());
                solrInputDocument.addField(TEMPORARY_ITEM_TYPE_CODE_VALUE_DISPLAY, tempItemTypeRecord.getItmTypCd());
            }

            solrInputDocument.addField(ITEM_BARCODE_SEARCH, itemRecord.getBarcode());
            solrInputDocument.addField(ITEM_BARCODE_DISPLAY, itemRecord.getBarcode());
            solrInputDocument.addField(ITEM_URI_SEARCH, itemRecord.getUri());
            solrInputDocument.addField(ITEM_URI_DISPLAY, itemRecord.getUri());

            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
            if (CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
                for (Iterator<ItemStatisticalSearchRecord> iterator = itemStatisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                    ItemStatisticalSearchRecord itemStatisticalSearchRecord = iterator.next();
                    if (null != itemStatisticalSearchRecord) {
                        StatisticalSearchRecord statisticalSearchRecord = getOleMemorizeService().getStatisticalSearchRecordById(getLongValue(itemStatisticalSearchRecord.getStatSearchCodeId()));
                        if(null != statisticalSearchRecord){
                            solrInputDocument.addField(STATISTICAL_SEARCHING_CODE_VALUE_SEARCH, statisticalSearchRecord.getStatSrchCd());
                            solrInputDocument.addField(STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY, statisticalSearchRecord.getStatSrchCd());
                            solrInputDocument.addField(STATISTICAL_SEARCHING_FULL_VALUE_SEARCH, statisticalSearchRecord.getStatSrchNm());
                            solrInputDocument.addField(STATISTICAL_SEARCHING_FULL_VALUE_DISPLAY, statisticalSearchRecord.getStatSrchNm());

                        }
                    }
                }
            }

            solrInputDocument.addField(ITEM_IDENTIFIER_DISPLAY, itemIdentifierWithPrefix);
            solrInputDocument.addField(BARCODE_ARSL_DISPLAY, itemRecord.getBarcodeArsl());
            solrInputDocument.addField(COPY_NUMBER_DISPLAY, itemRecord.getCopyNumber());
            solrInputDocument.addField(PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_DISPLAY, itemRecord.getPurchaseOrderLineItemId());
            solrInputDocument.addField(VENDOR_LINE_ITEM_IDENTIFIER_DISPLAY, itemRecord.getVendorLineItemId());
            solrInputDocument.addField(ENUMERATION_DISPLAY, itemRecord.getEnumeration());
            solrInputDocument.addField(CHRONOLOGY_DISPLAY, itemRecord.getChronology());
            solrInputDocument.addField(MISSING_PIECE_FLAG_NOTE_DISPLAY, itemRecord.getMissingPiecesNote());
            solrInputDocument.addField(CLAIMS_RETURNED_NOTE_DISPLAY, itemRecord.getClaimsReturnedNote());
            solrInputDocument.addField(DAMAGED_ITEM_NOTE_DISPLAY, itemRecord.getItemDamagedNote());
            solrInputDocument.addField(MISSING_PIECE_FLAG_DISPLAY, getBooleanValueYorN(itemRecord.getMissingPieces()));
            solrInputDocument.addField(CLAIMS_RETURNED_FLAG_DISPLAY, getBooleanValueYorN(itemRecord.getClaimsReturned()));
            solrInputDocument.addField(ITEM_DAMAGED_FLAG_DISPLAY, getBooleanValueYorN(itemRecord.getItemDamagedStatus()));
            solrInputDocument.addField(MISSING_PIECE_COUNT_DISPLAY,itemRecord.getMissingPiecesCount());
            solrInputDocument.addField(NUMBER_OF_PIECES_DISPLAY,itemRecord.getNumPieces());
            solrInputDocument.addField(CREATED_BY,itemRecord.getCreatedBy());
            solrInputDocument.addField(UPDATED_BY,itemRecord.getUpdatedBy());



            solrInputDocument.setField(DATE_UPDATED, itemRecord.getDateUpdated());
            solrInputDocument.setField(DATE_ENTERED, itemRecord.getDateCreated());

            List<OLEItemDonorRecord> itemDonorRecords = itemRecord.getOleItemDonorRecords();
            if (CollectionUtils.isNotEmpty(itemDonorRecords)) {
                for (Iterator<OLEItemDonorRecord> iterator = itemDonorRecords.iterator(); iterator.hasNext(); ) {
                    OLEItemDonorRecord itemDonorRecord = iterator.next();
                    solrInputDocument.addField(DONOR_CODE_SEARCH, itemDonorRecord.getDonorCode());
                    solrInputDocument.addField(DONOR_CODE_DISPLAY, itemDonorRecord.getDonorCode());
                    solrInputDocument.addField(DONOR_PUBLIC_DISPLAY, itemDonorRecord.getDonorDisplayNote());
                    solrInputDocument.addField(DONOR_NOTE_DISPLAY, itemDonorRecord.getDonorNote());
                }
            }
            List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
            if (CollectionUtils.isNotEmpty(itemNoteRecords)) {
                for (Iterator<ItemNoteRecord> iterator = itemNoteRecords.iterator(); iterator.hasNext(); ) {
                    ItemNoteRecord oleDsItemNitemNoteRecordteT = iterator.next();
                    solrInputDocument.addField(ITEMNOTE_VALUE_DISPLAY, oleDsItemNitemNoteRecordteT.getNote());
                    solrInputDocument.addField(ITEMNOTE_TYPE_DISPLAY, oleDsItemNitemNoteRecordteT.getType());
                }
            }
            solrInputDocument.addField(NUMBER_OF_RENEW, itemRecord.getNumOfRenew());
            solrInputDocument.addField(UNIQUE_ID, itemIdentifierWithPrefix);

            //Todo : Need to do the all text part

            assignUUIDs(solrInputDocument);
        } catch (Exception e) {
            LOG.info("Exception :", e);
            e.printStackTrace();
            throw new DocstoreIndexException(e.getMessage());
        }


        return new SolrInputDocumentAndDocumentMap(solrInputDocument, parameterMap);
    }

    private SolrInputDocument addItemDetailsToBib(SolrInputDocument solrInputDocument, SolrInputDocument destinationSolrInputDocument) {
        if(null != destinationSolrInputDocument) {
            addDetails(solrInputDocument, destinationSolrInputDocument, ITEM_BARCODE_SEARCH);
            addDetails(solrInputDocument, destinationSolrInputDocument, ITEM_IDENTIFIER);
            return destinationSolrInputDocument;
        }
        return null;
    }

    private SolrInputDocument addItemDetailsToHoldings(SolrInputDocument sourceSolrInputDocument, SolrInputDocument destinationSolrInputDocument) {
        if(null != destinationSolrInputDocument) {
            addDetails(sourceSolrInputDocument, destinationSolrInputDocument, ITEM_BARCODE_SEARCH);
            addDetails(sourceSolrInputDocument, destinationSolrInputDocument, ITEM_IDENTIFIER);
            return destinationSolrInputDocument;
        }
        return null;
    }

    public String getAllTextValueForItem(ItemRecord itemRecord) {
        StringBuffer sb = new StringBuffer();
        String itemIdentifier =  DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), String.valueOf(itemRecord.getItemId()));
        String copyNumber = itemRecord.getCopyNumber();
        String enumeration = itemRecord.getEnumeration();
        // String analytic = itemRecord.getAnalytic(); // Todo : Need to check
        String barcodeARSL = itemRecord.getBarcodeArsl();
        String chronology = itemRecord.getChronology();
        String checkinNote = itemRecord.getCheckInNote();
        Date claimsReturnedDateCreated = itemRecord.getClaimsReturnedDateCreated();
        String claimsReturnedFlagCreateDate = convertDateToString(DOCSTORE_DATE_FORMAT, claimsReturnedDateCreated);
        String claimsReturnedNote = itemRecord.getClaimsReturnedNote();
        //String copyNumberLabel = itemRecord.getCopyNumberLabel(); // TODO : Need to check
        String currentBorrower = itemRecord.getCurrentBorrower();
        String damagedItemNote = itemRecord.getItemDamagedNote();

        String dueDateTime = convertDateToString(DOCSTORE_DATE_FORMAT, itemRecord.getDueDateTime());

        String fund = itemRecord.getFund();

        Date itemStatusDateUpdated = itemRecord.getDateUpdated();
        String itemStatusEffectiveDate = convertDateToString(DOCSTORE_DATE_FORMAT, itemStatusDateUpdated);


        Date missingPiecesEffectiveDate = itemRecord.getMissingPiecesEffectiveDate();
        String missingPieceEffectiveDate = convertDateToString(DOCSTORE_DATE_FORMAT, missingPiecesEffectiveDate);

        String missingPieceFlagNote = itemRecord.getMissingPiecesNote();
        String missingPiecesCount = String.valueOf(itemRecord.getMissingPiecesCount());
        String numberOfPieces = itemRecord.getNumPieces();
        String price = String.valueOf(itemRecord.getPrice());
        String proxyBorrower = itemRecord.getProxyBorrower();
        String purchaseOrderLineItemIdentifier = itemRecord.getPurchaseOrderLineItemId();
        String vendorLineItemIdentifier = itemRecord.getVendorLineItemId();
        // String volumeNumber = itemRecord.getVolumeNumber();  // TODO : Need to check

        appendData(sb, itemIdentifier);
        appendData(sb, copyNumber);
        appendData(sb, enumeration);
        //appendData(sb, analytic);
        appendData(sb, chronology);
        appendData(sb, barcodeARSL);
        appendData(sb, checkinNote);
        appendData(sb, claimsReturnedFlagCreateDate);
        appendData(sb, claimsReturnedNote);
        //appendData(sb, copyNumberLabel);
        appendData(sb, currentBorrower);
        appendData(sb, damagedItemNote);
        appendData(sb, dueDateTime);
        appendData(sb, fund);
        appendData(sb, itemStatusEffectiveDate);
        appendData(sb, missingPieceEffectiveDate);
        appendData(sb, missingPieceFlagNote);
        appendData(sb, missingPiecesCount);
        appendData(sb, numberOfPieces);
        appendData(sb, price);
        appendData(sb, proxyBorrower);
        appendData(sb, purchaseOrderLineItemIdentifier);
        appendData(sb, vendorLineItemIdentifier);
        //appendData(sb, volumeNumber);

        boolean staffOnlyFlag = getBooleanValueYorN(itemRecord.getStaffOnly());
        boolean claimsReturnedFlag = getBooleanValueYorN(itemRecord.getClaimsReturned());
        boolean fastAddFlag = getBooleanValueYorN(itemRecord.getFastAdd());
        boolean itemDamagedStatus = getBooleanValueYorN(itemRecord.getItemDamagedStatus());
        boolean missingPieceFlag = getBooleanValueYorN(itemRecord.getMissingPieces());

        appendData(sb, String.valueOf(staffOnlyFlag));
        appendData(sb, String.valueOf(claimsReturnedFlag));
        appendData(sb, String.valueOf(fastAddFlag));
        appendData(sb, String.valueOf(itemDamagedStatus));
        appendData(sb, String.valueOf(missingPieceFlag));

        appendData(sb, itemRecord.getBarcode());
        appendData(sb, itemRecord.getUri());

        if (StringUtils.isNotEmpty(itemRecord.getCallNumber())) {
            String number = itemRecord.getCallNumber();
            String prefix = itemRecord.getCallNumberPrefix();
            CallNumberTypeRecord callNumberTypeRecord = getOleMemorizeService().getCallNumberTypeRecordById(getLongValue(itemRecord.getCallNumberTypeId()));
            if (callNumberTypeRecord != null) {
                String shelvingSchemeCodeValue = callNumberTypeRecord.getShvlgSchmCd();
                String shelvingSchemeFullValue = callNumberTypeRecord.getShvlgSchmNm();

                appendData(sb, shelvingSchemeCodeValue);
                appendData(sb, shelvingSchemeFullValue);
            }
            if (itemRecord.getShelvingOrder() != null) {
                String shelvingOrderCodeValue = itemRecord.getShelvingOrder();
                String shelvingOrderFullValue = itemRecord.getShelvingOrder();
                appendData(sb, shelvingOrderCodeValue);
                appendData(sb, shelvingOrderFullValue);
            }

            appendData(sb, number);
            appendData(sb, prefix);
        }


        List<OLEItemDonorRecord> donorList = itemRecord.getOleItemDonorRecords();
        if (CollectionUtils.isNotEmpty(donorList)) {
            for (Iterator<OLEItemDonorRecord> iterator = donorList.iterator(); iterator.hasNext(); ) {
                OLEItemDonorRecord oleItemDonorRecord = iterator.next();
                if(null != oleItemDonorRecord) {
                    String donorCode = oleItemDonorRecord.getDonorCode();
                    String donorNote = oleItemDonorRecord.getDonorNote();
                    String donorPublicDisplay = oleItemDonorRecord.getDonorDisplayNote();
                    appendData(sb, donorCode);
                    appendData(sb, donorNote);
                    appendData(sb, donorPublicDisplay);
                }
            }
        }


        List<FormerIdentifierRecord> formerIdentifierRecords = itemRecord.getFormerIdentifierRecords();
        if (CollectionUtils.isNotEmpty(formerIdentifierRecords)) {
            for (Iterator<FormerIdentifierRecord> iterator = formerIdentifierRecords.iterator(); iterator.hasNext(); ) {
                FormerIdentifierRecord formerIdentifierRecord = iterator.next();
                if(null != formerIdentifierRecord) {
                    String identifierType = formerIdentifierRecord.getType();
                    String identifierValue = formerIdentifierRecord.getValue();
                    appendData(sb, identifierType);
                    appendData(sb, identifierValue);
                }
            }
        }

        ItemStatusRecord itemStatusRecord = getOleMemorizeService().getItemStatusById(getStringValue(itemRecord.getItemStatusId()));
        if (itemStatusRecord != null) {
            String itemStatusCodeValue = itemStatusRecord.getItemAvailStatCd();
            String itemStatusFullValue = itemStatusRecord.getItemAvailStatNm();
            appendData(sb, itemStatusCodeValue);
            appendData(sb, itemStatusFullValue);
        }

        ItemTypeRecord itemTypeRecord = getOleMemorizeService().getItemTypeById(getStringValue(itemRecord.getItemTypeId()));
        if (itemTypeRecord != null) {
            String itemTypeCodeValue = itemTypeRecord.getItmTypCd();
            String itemTypeFullValue = itemTypeRecord.getItmTypNm();
            appendData(sb, itemTypeCodeValue);
            appendData(sb, itemTypeFullValue);
        }

        List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
        if (CollectionUtils.isNotEmpty(itemNoteRecords)) {
            for (Iterator<ItemNoteRecord> iterator = itemNoteRecords.iterator(); iterator.hasNext(); ) {
                ItemNoteRecord itemNoteRecord = iterator.next();
                String itemNoteValue = itemNoteRecord.getNote();
                String itemNoteType = itemNoteRecord.getType();
                appendData(sb, itemNoteValue);
                appendData(sb, itemNoteType);
            }
        }

        List<LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
        if (CollectionUtils.isNotEmpty(locationsCheckinCountRecords)) {
            for (Iterator<LocationsCheckinCountRecord> iterator = locationsCheckinCountRecords.iterator(); iterator.hasNext(); ) {
                LocationsCheckinCountRecord locationsCheckinCountRecord = iterator.next();
                if (null != locationsCheckinCountRecord) {
                    Integer locationCount = locationsCheckinCountRecord.getLocationCount();
                    String checkInLocationCount = (null != locationCount ? locationCount.toString() : "0");
                    appendData(sb, checkInLocationCount);
                    Integer locationInhouseCount = locationsCheckinCountRecord.getLocationInHouseCount();
                    String checkInLocationInHouseCount = (null != locationInhouseCount ? locationInhouseCount.toString() : "0");
                    appendData(sb, checkInLocationInHouseCount);
                    String checkInLocationName = locationsCheckinCountRecord.getLocationName();
                    appendData(sb, checkInLocationName);
                }
            }
        }

        List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
        if (CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
            for (Iterator<ItemStatisticalSearchRecord> iterator = itemStatisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                ItemStatisticalSearchRecord itemStatisticalSearchRecord = iterator.next();
                if (null != itemStatisticalSearchRecord) {
                    StatisticalSearchRecord statisticalSearchRecord = getOleMemorizeService().getStatisticalSearchRecordById(getLongValue(itemStatisticalSearchRecord.getStatSearchCodeId()));
                    if(null != statisticalSearchRecord){
                        String codeValue = statisticalSearchRecord.getStatSrchCd();
                        appendData(sb, codeValue);
                        String fullValue = statisticalSearchRecord.getStatSrchNm();
                        appendData(sb, fullValue);
                    }
                }
            }
        }

        ItemTypeRecord tempItemTypeRecord = getOleMemorizeService().getItemTypeById(getStringValue(itemRecord.getTempItemTypeId()));
        if (tempItemTypeRecord != null) {
            String temporaryItemTypeCodeValue = tempItemTypeRecord.getItmTypCd();
            String temporaryItemTypeFullValue = tempItemTypeRecord.getItmTypNm();
            appendData(sb, temporaryItemTypeCodeValue);
            appendData(sb, temporaryItemTypeFullValue);
        }
        appendData(sb,itemRecord.getLocation());
        appendData(sb,itemRecord.getLocationLevel());
        return sb.toString();

    }


}
