package org.kuali.ole.indexer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.Constants;
import org.kuali.ole.common.DocumentUniqueIDPrefix;
import org.kuali.ole.common.enums.DocCategory;
import org.kuali.ole.common.enums.DocFormat;
import org.kuali.ole.common.enums.DocType;
import org.kuali.ole.common.exception.DocstoreIndexException;
import org.kuali.ole.common.util.CallNumberUtil;
import org.kuali.ole.model.jpa.*;
import org.kuali.ole.model.solr.RecordCountAndSolrDocumentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SheikS on 11/30/2015.
 */
public class HoldingIndexer extends OleDsNgIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingIndexer.class);

    @Override
    public void deleteDocument(String id) {

    }

    public RecordCountAndSolrDocumentMap getInputDocumentForHoldings(HoldingsRecord holdingsRecord, RecordCountAndSolrDocumentMap recordCountAndSolrDocumentMap) {
        if(null == recordCountAndSolrDocumentMap) {
            recordCountAndSolrDocumentMap = new RecordCountAndSolrDocumentMap();
        }
        Map<String, SolrInputDocument> solrInputDocumentMap = recordCountAndSolrDocumentMap.getSolrInputDocumentMap();
        try {
            SolrInputDocumentAndDocumentMap solrInputDocumentAndDocumentMap = buildSolrInputDocument(holdingsRecord, solrInputDocumentMap);
            SolrInputDocument holdingsSolrInputDocument = solrInputDocumentAndDocumentMap.getSolrInputDocument();
            solrInputDocumentMap = solrInputDocumentAndDocumentMap.getMap();

            Date date = new Date();
            holdingsSolrInputDocument.addField(UPDATED_BY, holdingsRecord.getUpdatedBy());
            holdingsSolrInputDocument.addField(DATE_UPDATED, date);
            Integer bibId = holdingsRecord.getBibRecord().getBibId();
            if(null != bibId){

                String bibIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, String.valueOf(bibId));

                SolrInputDocument bibSolrInputDocument = getSolrInputDocumentFromMap(solrInputDocumentMap,bibIdentifierWithPrefix);

                if(null == bibIdentifierWithPrefix) {
                    // Todo : Need to form solrInputDocument for Bib.
                }

                addBibInfoForHoldingsOrItems(holdingsSolrInputDocument,bibSolrInputDocument);
                holdingsSolrInputDocument.addField(BIB_IDENTIFIER, bibIdentifierWithPrefix);
                bibSolrInputDocument = addHoldingsDetailsToBib(holdingsSolrInputDocument,bibSolrInputDocument);

                addSolrInputDocumentToMap(solrInputDocumentMap,bibSolrInputDocument);
            }
            addSolrInputDocumentToMap(solrInputDocumentMap,holdingsSolrInputDocument);
            List<ItemRecord> itemRecords = holdingsRecord.getItemRecords();
            List<String> itemUUIds = new ArrayList<String>();

            recordCountAndSolrDocumentMap.setSolrInputDocumentMap(solrInputDocumentMap);

            if(CollectionUtils.isNotEmpty(itemRecords)){
                int processed = 0;
                for (Iterator<ItemRecord> iterator = itemRecords.iterator(); iterator.hasNext(); ) {
                    ItemRecord itemRecord = iterator.next();
                    ItemIndexer itemIndexer = new ItemIndexer();
                    itemIndexer.setOleMemorizeService(getOleMemorizeService());
                    itemIndexer.setReportUtil(getReportUtil());
                    //Todo : Need to do for Item.
                    recordCountAndSolrDocumentMap = itemIndexer.getInputDocumentForItem(itemRecord, recordCountAndSolrDocumentMap);
                    String itemIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML, String.valueOf(itemRecord.getItemId()));
                    itemUUIds.add(itemIdentifierWithPrefix);
                    processed++;
                }
                recordCountAndSolrDocumentMap.setNumberOfItemsFetched(recordCountAndSolrDocumentMap.getNumberOfItemsFetched() + itemRecords.size());
                recordCountAndSolrDocumentMap.setNumberOfItemsProcessed(recordCountAndSolrDocumentMap.getNumberOfItemsProcessed() + processed);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reportUtil.saveExceptionReportForHoldings(holdingsRecord, e);
        }

        return recordCountAndSolrDocumentMap;
    }

    @Override
    public SolrInputDocumentAndDocumentMap buildSolrInputDocument(Object object, Map parameterMap) {
        SolrInputDocument solrInputDocument = null;
        HoldingsRecord holdingsRecord = (HoldingsRecord) object;
        try {
            solrInputDocument = prepareSolrInputDocument(holdingsRecord);
            assignUUIDs(solrInputDocument);
        } catch (Exception e) {
            LOG.info("Exception :", e);
            e.printStackTrace();
            throw new DocstoreIndexException(e.getMessage());
        }

        return new SolrInputDocumentAndDocumentMap(solrInputDocument, parameterMap);
    }

    private SolrInputDocument prepareSolrInputDocument(HoldingsRecord object) throws Exception {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        HoldingsRecord holdingsRecord = object;

        String holdingsIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, String.valueOf(holdingsRecord.getHoldingsId()));


        if (null != holdingsRecord.getGokbIdentifier()) {
            solrInputDocument.addField(GOKB_IDENTIFIER, holdingsRecord.getGokbIdentifier());
        }

        setCommonFields(holdingsRecord, solrInputDocument);
        ReceiptStatusRecord receiptStatusRecord = getOleMemorizeService().getReceiptStatusRecordById(getStringValue(holdingsRecord.getReceiptStatusId()));
        if (null != receiptStatusRecord) {
            solrInputDocument.addField(RECEIPT_STATUS_SEARCH, receiptStatusRecord.getRcptStatCd());
            solrInputDocument.addField(RECEIPT_STATUS_DISPLAY, receiptStatusRecord.getRcptStatCd());
        }

        if (holdingsRecord.getCopyNumber() != null) {
            solrInputDocument.addField(COPY_NUMBER_SEARCH, holdingsRecord.getCopyNumber());
            solrInputDocument.addField(COPY_NUMBER_LABEL_SEARCH, holdingsRecord.getCopyNumber());
            solrInputDocument.addField(COPY_NUMBER_DISPLAY, holdingsRecord.getCopyNumber());
            solrInputDocument.addField(COPY_NUMBER_LABEL_DISPLAY, holdingsRecord.getCopyNumber());
        }
        if (holdingsRecord.getCallNumber() != null) {
            // solrInputDocument.addField(CALL_NUMBER_TYPE_SEARCH, holdingsRecord.getCallNumberTypeRecord().getCode()); // Todo : Need to verify (ANS : LCC)
            solrInputDocument.addField(CALL_NUMBER_SEARCH, holdingsRecord.getCallNumber());
            solrInputDocument.setField(CALL_NUMBER_SORT, holdingsRecord.getCallNumber());
            //            solrInputDocument.addField(ITEM_PART_SEARCH, holdingsRecord.getCallNumber().getItemPart()); // Todo : Need to verify
            solrInputDocument.addField(CALL_NUMBER_PREFIX_SEARCH, holdingsRecord.getCallNumberPrefix());
            //            solrInputDocument.addField(CLASSIFICATION_PART_SEARCH, holdingsRecord.getCallNumber().getClassificationPart());// Todo : Need to verify

            //            solrInputDocument.addField(ITEM_PART_DISPLAY, holdingsRecord.getCallNumber().getItemPart());// Todo : Need to verify
            //            solrInputDocument.addField(CALL_NUMBER_TYPE_DISPLAY, holdingsRecord.getCallNumber().getType());// Todo : Need to verify
            solrInputDocument.addField(CALL_NUMBER_DISPLAY, holdingsRecord.getCallNumber());
            solrInputDocument.addField(CALL_NUMBER_PREFIX_DISPLAY, holdingsRecord.getCallNumberPrefix());
            //            solrInputDocument.addField(CLASSIFICATION_PART_DISPLAY, holdingsRecord.getCallNumber().getClassificationPart());// Todo : Need to verify
            String shelvingSchemeCodeValue = "";
            CallNumberTypeRecord callNumberTypeRecord = getOleMemorizeService().getCallNumberTypeRecordById(getLongValue(holdingsRecord.getCallNumberTypeId()));
            if (callNumberTypeRecord != null) {
                shelvingSchemeCodeValue = callNumberTypeRecord.getShvlgSchmCd();
                String shelvingSchemaFullvalue = callNumberTypeRecord.getShvlgSchmNm();
                solrInputDocument.addField(SHELVING_SCHEME_VALUE_SEARCH, shelvingSchemaFullvalue);
                solrInputDocument.addField(SHELVING_SCHEME_CODE_SEARCH, shelvingSchemeCodeValue);
                solrInputDocument.addField(SHELVING_SCHEME_VALUE_DISPLAY, shelvingSchemaFullvalue);
                solrInputDocument.addField(SHELVING_SCHEME_CODE_DISPLAY, shelvingSchemeCodeValue);
            }
            String shelvingOrder = null;
            if (StringUtils.isNotEmpty(holdingsRecord.getShelvingOrder())) {
                shelvingOrder = holdingsRecord.getShelvingOrder();
            } else {
                if (StringUtils.isNotEmpty(holdingsRecord.getCallNumber())) {
                    shelvingOrder = new CallNumberUtil().buildSortableCallNumber(holdingsRecord.getCallNumber(), shelvingSchemeCodeValue);
                }
            }
            if (StringUtils.isNotEmpty(shelvingOrder)) {
                shelvingOrder = shelvingOrder.replaceAll(" ", "-");
                solrInputDocument.addField(SHELVING_ORDER_SORT, shelvingOrder + holdingsIdentifierWithPrefix);
                solrInputDocument.addField(SHELVING_ORDER_SEARCH, shelvingOrder);
                solrInputDocument.addField(SHELVING_ORDER_DISPLAY, shelvingOrder);
            }
        }
        StringBuffer loactionLevelStr = new StringBuffer(" ");
        if (holdingsRecord != null && holdingsRecord.getLocation() != null &&
                holdingsRecord.getLocationLevel() != null) {
            solrInputDocument.addField(LOCATION_LEVEL_SEARCH, holdingsRecord.getLocation());
            solrInputDocument.addField(LOCATION_LEVEL_NAME_SEARCH, holdingsRecord.getLocationLevel());
            solrInputDocument.addField(LOCATION_LEVEL_DISPLAY, holdingsRecord.getLocation());
            solrInputDocument.addField(LOCATION_LEVEL_NAME_DISPLAY, holdingsRecord.getLocationLevel());
            solrInputDocument.addField(LOCATION_LEVEL_SORT, holdingsRecord.getLocation());
            StringTokenizer locationTokenizer = new StringTokenizer(holdingsRecord.getLocation(), "/");
            StringTokenizer locationLevelTokenizer = new StringTokenizer(holdingsRecord.getLocationLevel(), "/");
            while (locationLevelTokenizer.hasMoreTokens()) {
                String locationLevel = locationLevelTokenizer.nextToken();
                String location = locationTokenizer.nextToken();
                addLocationLevelsToSolrInputodument(location, locationLevel, solrInputDocument, loactionLevelStr);
            }
        }
        if (null != holdingsRecord.getHoldingsType() && holdingsRecord.getHoldingsType().equalsIgnoreCase(Constants.PRINT)) {
            solrInputDocument.addField(DOC_TYPE, DocType.HOLDINGS.getCode());
            indexPHoldingsInformation(holdingsRecord, solrInputDocument);
        } else {
            solrInputDocument.addField(DOC_TYPE, DocType.EHOLDINGS.getCode());
            indexEHoldingsInfomation(holdingsRecord, solrInputDocument);
        }

        solrInputDocument.addField(ALL_TEXT, getAllTextValueForHoldings(holdingsRecord) + loactionLevelStr.toString());
        return solrInputDocument;
    }

    protected void setCommonFields(HoldingsRecord holdingsRecord, SolrInputDocument solrDocForHolding) {
        String holdingsIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, String.valueOf(holdingsRecord.getHoldingsId()));
        solrDocForHolding.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDocForHolding.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDocForHolding.addField(LOCALID_SEARCH, holdingsRecord.getHoldingsId());
        solrDocForHolding.addField(LOCALID_DISPLAY, holdingsRecord.getHoldingsId());
        solrDocForHolding.addField(ID, holdingsIdentifierWithPrefix);
        solrDocForHolding.addField(HOLDINGS_IDENTIFIER, holdingsIdentifierWithPrefix);
        solrDocForHolding.addField(UNIQUE_ID, holdingsIdentifierWithPrefix);
        solrDocForHolding.addField(STAFF_ONLY_FLAG, getBooleanValueYorN(holdingsRecord.getStaffOnly()));

    }

    private void indexPHoldingsInformation(HoldingsRecord holdingsRecord, SolrInputDocument solrInputDocument) {
        if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsNoteRecords())) {
            for (HoldingsNoteRecord holdingNote : holdingsRecord.getHoldingsNoteRecords()) {
                solrInputDocument.addField(HOLDING_NOTE_SEARCH, holdingNote.getNote());
                solrInputDocument.addField(HOLDING_NOTE_DISPLAY, holdingNote.getNote());
            }
        }
        if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsUriRecords())) {
            for (HoldingsUriRecord uri : holdingsRecord.getHoldingsUriRecords()) {
                solrInputDocument.addField(URI_SEARCH, uri.getUri());
                solrInputDocument.addField(URI_DISPLAY, uri.getUri());
            }
        }
        List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = holdingsRecord.getExtentOfOwnerShipRecords();
        if (CollectionUtils.isNotEmpty(extentOfOwnerShipRecords)) {
            for (Iterator<ExtentOfOwnerShipRecord> iterator = extentOfOwnerShipRecords.iterator(); iterator.hasNext(); ) {
                ExtentOfOwnerShipRecord extentOfOwnerShipRecord = iterator.next();
                List<ExtentNoteRecord> extentNoteRecords = extentOfOwnerShipRecord.getExtentNoteRecords();
                for (Iterator<ExtentNoteRecord> extentNoteRecordIterator = extentNoteRecords.iterator(); extentNoteRecordIterator.hasNext(); ) {
                    ExtentNoteRecord extentNoteRecord = extentNoteRecordIterator.next();
                    solrInputDocument.addField(EXTENT_OF_OWNERSHIP_NOTE_VALUE_DISPLAY, extentNoteRecord.getNote());
                    solrInputDocument.addField(EXTENT_OF_OWNERSHIP_NOTE_TYPE_DISPLAY, extentNoteRecord.getType());
                }
                ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecordById = getOleMemorizeService().getExtentOfOwnerShipTypeRecordById(getLongValue(extentOfOwnerShipRecord.getExtOwnershipTypeId()));
                if (null != extentOfOwnerShipTypeRecordById) {
                    solrInputDocument.addField(EXTENT_OF_OWNERSHIP_TYPE_DISPLAY,extentOfOwnerShipTypeRecordById.getTypeOwnershipCd());
                }
            }
        }
        AuthenticationTypeRecord authenticationTypeRecordById = getOleMemorizeService().getAuthenticationTypeRecordById(holdingsRecord.getAuthenticationTypeId());
        if (holdingsRecord.getHoldingsAccessLocations() != null && null != authenticationTypeRecordById) {
            solrInputDocument.addField(AUTHENTICATION_DISPLAY, authenticationTypeRecordById.getName());
            solrInputDocument.addField(PROXIED_DISPLAY, holdingsRecord.getProxiedResource());
            solrInputDocument.addField(NUMBER_OF_SIMULTANEOUS_USERS_DISPLAY, holdingsRecord.getNumberSimultUsers());
            if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsAccessLocations()) && null != getOleMemorizeService().getAccessLocationById(holdingsRecord.getHoldingsAccessLocations().get(0).getAccessLocationId())) {
                solrInputDocument.addField(ACCESS_LOCATION_DISPLAY, getOleMemorizeService().getAccessLocationById(holdingsRecord.getHoldingsAccessLocations().get(0).getAccessLocationId()).getCode());
            } else {
                solrInputDocument.addField(ACCESS_LOCATION_DISPLAY, null);
            }
        }
    }

    private void indexEHoldingsInfomation(HoldingsRecord holdingsRecord, SolrInputDocument solrInputDocument) {

        if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsNoteRecords())) {
            for (HoldingsNoteRecord holdingNote : holdingsRecord.getHoldingsNoteRecords()) {
                solrInputDocument.addField(HOLDING_NOTE_SEARCH, holdingNote.getNote());
                solrInputDocument.addField(HOLDING_NOTE_DISPLAY, holdingNote.getNote());
            }
        }
        solrInputDocument.addField(ACCESS_STATUS_DISPLAY, holdingsRecord.getAccessStatus());
        solrInputDocument.addField(ACCESS_STATUS_SEARCH, holdingsRecord.getAccessStatus());
        List<OLEHoldingsDonorRecord> oleHoldingsDonorRecords = holdingsRecord.getOLEHoldingsDonorRecords();
        if (CollectionUtils.isNotEmpty(oleHoldingsDonorRecords)) {
            for (OLEHoldingsDonorRecord donorInfo : oleHoldingsDonorRecords) {
                solrInputDocument.addField(DONOR_CODE_SEARCH, donorInfo.getDonorCode());
                solrInputDocument.addField(DONOR_CODE_DISPLAY, donorInfo.getDonorCode());
                solrInputDocument.addField(DONOR_PUBLIC_DISPLAY, donorInfo.getDonorDisplayNote());
                solrInputDocument.addField(DONOR_PUBLIC_SEARCH, donorInfo.getDonorDisplayNote());
                solrInputDocument.addField(DONOR_NOTE_DISPLAY, donorInfo.getDonorNote());
                solrInputDocument.addField(DONOR_NOTE_SEARCH, donorInfo.getDonorNote());
            }
        }
        List<HoldingsStatisticalSearchRecord> oleDsHoldingsStatSearchTs = holdingsRecord.getHoldingsStatisticalSearchRecords();


        if (CollectionUtils.isNotEmpty(oleDsHoldingsStatSearchTs)) {
            StatisticalSearchRecord statisticalSearchRecordById = getOleMemorizeService().getStatisticalSearchRecordById(getLongValue(oleDsHoldingsStatSearchTs.get(0).getStatSearchCodeId()));
            if(null != statisticalSearchRecordById) {
                solrInputDocument.addField(STATISTICAL_SEARCHING_CODE_VALUE_SEARCH, statisticalSearchRecordById.getStatSrchNm());
                solrInputDocument.addField(STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY, statisticalSearchRecordById.getStatSrchCd());
            }
        }

        solrInputDocument.addField(E_PUBLISHER_DISPLAY, holdingsRecord.getPublisher());
        solrInputDocument.addField(E_PUBLISHER_SEARCH, holdingsRecord.getPublisher());

        solrInputDocument.addField(IMPRINT_SEARCH, holdingsRecord.getImprint());
        solrInputDocument.addField(IMPRINT_DISPLAY, holdingsRecord.getImprint());

        if (holdingsRecord.getPlatform() != null) {
            solrInputDocument.addField(ADMIN_URL_DISPLAY, holdingsRecord.getAdminUrl());
            solrInputDocument.addField(ADMIN_URL_SEARCH, holdingsRecord.getAdminUrl());
            solrInputDocument.addField(PLATFORM_DISPLAY, holdingsRecord.getPlatform());
            solrInputDocument.addField(ADMIN_USERNAME_DISPLAY, holdingsRecord.getAdminUsername());
            solrInputDocument.addField(ADMIN_USERNAME_SEARCH, holdingsRecord.getAdminUsername());
            solrInputDocument.addField(ADMIN_PASSWORD_DISPLAY, holdingsRecord.getAdminPassword());
            solrInputDocument.addField(ADMIN_PASSWORD_SEARCH, holdingsRecord.getAdminPassword());
        }

        if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsUriRecords())) {
            for (HoldingsUriRecord uri : holdingsRecord.getHoldingsUriRecords()) {
                solrInputDocument.addField(URL_DISPLAY, uri.getUri());
                solrInputDocument.addField(URL_SEARCH, uri.getUri());
                solrInputDocument.addField(LINK_TEXT_DISPLAY, uri.getText());
                solrInputDocument.addField(LINK_TEXT_SEARCH, uri.getText());
            }
        }

        if (holdingsRecord.getSubscriptionStatus() != null) {
            solrInputDocument.addField(SUBSCRIPTION_DISPLAY, holdingsRecord.getSubscriptionStatus());
            solrInputDocument.addField(SUBSCRIPTION_SEARCH, holdingsRecord.getSubscriptionStatus());
        }

        solrInputDocument.addField(ACCESS_USERNAME_DISPLAY, holdingsRecord.getAccessUsername());
        solrInputDocument.addField(ACCESS_USERNAME_SEARCH, holdingsRecord.getAccessUsername());
        solrInputDocument.addField(ACCESS_PASSWORD_DISPLAY, holdingsRecord.getAccessPassword());
        solrInputDocument.addField(ACCESS_PASSWORD_SEARCH, holdingsRecord.getAccessPassword());
        AuthenticationTypeRecord authenticationTypeRecordById = getOleMemorizeService().getAuthenticationTypeRecordById(holdingsRecord.getAuthenticationTypeId());
        if (null != authenticationTypeRecordById) {
            solrInputDocument.addField(AUTHENTICATION_DISPLAY, authenticationTypeRecordById.getCode());
            solrInputDocument.addField(AUTHENTICATION_SEARCH, authenticationTypeRecordById.getCode());
        }
        solrInputDocument.addField(PROXIED_DISPLAY, holdingsRecord.getProxiedResource());
        solrInputDocument.addField(PROXIED_SEARCH, holdingsRecord.getProxiedResource());
        solrInputDocument.addField(NUMBER_OF_SIMULTANEOUS_USERS_DISPLAY, holdingsRecord.getNumberSimultUsers());
        solrInputDocument.addField(NUMBER_OF_SIMULTANEOUS_USERS_SEARCH, holdingsRecord.getNumberSimultUsers());
        if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsAccessLocations())) {
            AccessLocation accessLocationById = getOleMemorizeService().getAccessLocationById(holdingsRecord.getHoldingsAccessLocations().get(0).getAccessLocationCodeId());
            if(null != accessLocationById) {
                solrInputDocument.addField(ACCESS_LOCATION_DISPLAY, accessLocationById.getCode());
                solrInputDocument.addField(ACCESS_LOCATION_SEARCH, accessLocationById.getCode());

            }
        }
        solrInputDocument.addField(PERSIST_LINK_SEARCH, holdingsRecord.getLocalPersistentUri());
        solrInputDocument.addField(PERSIST_LINK_DISPLAY, holdingsRecord.getLocalPersistentUri());

        solrInputDocument.addField(ILL_DISPLAY, holdingsRecord.getAllowIll());
        solrInputDocument.addField(ILL_SEARCH, holdingsRecord.getAllowIll());

        List<EInstanceCoverageRecord> eInstanceCoverageRecords = holdingsRecord.getEInstanceCoverageRecords();
        if (CollectionUtils.isNotEmpty(eInstanceCoverageRecords)) {
            for (Iterator<EInstanceCoverageRecord> iterator = eInstanceCoverageRecords.iterator(); iterator.hasNext(); ) {
                EInstanceCoverageRecord eInstanceCoverageRecord = iterator.next();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(eInstanceCoverageRecord.getCoverageStartDate()) || org.apache.commons.lang3.StringUtils.isNotBlank(eInstanceCoverageRecord.getCoverageEndDate())) {
                    solrInputDocument.addField(E_INSTANCE_COVERAGE_DATE, eInstanceCoverageRecord.getCoverageStartDate() + "-" + eInstanceCoverageRecord.getCoverageEndDate());
                } else if (CollectionUtils.isNotEmpty(holdingsRecord.getEInstancePerpetualAccessRecords())) {
                    List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecords = holdingsRecord.getEInstancePerpetualAccessRecords();
                    for (Iterator<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecordIterator = eInstancePerpetualAccessRecords.iterator();
                         eInstancePerpetualAccessRecordIterator.hasNext(); ) {
                        EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = eInstancePerpetualAccessRecordIterator.next();
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(eInstancePerpetualAccessRecord.getPerpetualAccessStartDate()) || org.apache.commons.lang3.StringUtils.isNotBlank(eInstancePerpetualAccessRecord.getPerpetualAccessEndDate())) {
                            solrInputDocument.addField(E_INSTANCE_PERPETUAL_ACCESS_DATE, eInstancePerpetualAccessRecord.getPerpetualAccessStartDate() + "-" + eInstancePerpetualAccessRecord.getPerpetualAccessEndDate());
                        }
                    }
                }
            }
        }

    }

    public String getAllTextValueForHoldings(HoldingsRecord holdingsRecord) {
        StringBuffer sb = new StringBuffer();
        String holdingsIdentifier = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, String.valueOf(holdingsRecord.getHoldingsId()));
        String accessStatus = holdingsRecord.getAccessStatus();
        String copyNumber = holdingsRecord.getCopyNumber();
        String eResourceId = holdingsRecord.getEResourceId();
        String holdingsType = holdingsRecord.getHoldingsType();
        String imprint = holdingsRecord.getImprint();
        String localPersistentLink = holdingsRecord.getLocalPersistentUri();
        String publisher = holdingsRecord.getPublisher();
        ReceiptStatusRecord receiptStatusRecord = getOleMemorizeService().getReceiptStatusRecordById(getStringValue(holdingsRecord.getReceiptStatusId()));
        String receiptStatus = (receiptStatusRecord != null ? receiptStatusRecord.getRcptStatCd() : "");
        Date accessStatusDateUpdated = holdingsRecord.getAccessStatusDateUpdated();
        String statusDate = (null != accessStatusDateUpdated ? new SimpleDateFormat("dd-MM-yyyy").format(accessStatusDateUpdated) : ""); // Todo : Need to verify the date format
        String subscriptionStatus = holdingsRecord.getSubscriptionStatus();


        appendData(sb, holdingsIdentifier);
        appendData(sb, accessStatus);
        appendData(sb, copyNumber);
        appendData(sb, eResourceId);
        appendData(sb, holdingsType);
        appendData(sb, imprint);
        appendData(sb, localPersistentLink);
        appendData(sb, publisher);
        appendData(sb, receiptStatus);
        appendData(sb, statusDate);
        appendData(sb, subscriptionStatus);

        if (CollectionUtils.isNotEmpty(holdingsRecord.getExtentOfOwnerShipRecords())) {
            for (ExtentOfOwnerShipRecord extentOfOwnership : holdingsRecord.getExtentOfOwnerShipRecords()) {
                if (extentOfOwnership != null) {
                    String textualHoldings = extentOfOwnership.getText();
                    appendData(sb, textualHoldings);
                    ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecordById = getOleMemorizeService().getExtentOfOwnerShipTypeRecordById(getLongValue(extentOfOwnership.getExtOwnershipTypeId()));
                    if (null != extentOfOwnerShipTypeRecordById) {
                        String type = extentOfOwnerShipTypeRecordById.getTypeOwnershipCd();
                        appendData(sb, type);
                    }

                    List<EInstanceCoverageRecord> eInstanceCoverageRecords = holdingsRecord.getEInstanceCoverageRecords();
                    if (CollectionUtils.isNotEmpty(eInstanceCoverageRecords)) {
                        for (Iterator<EInstanceCoverageRecord> iterator = eInstanceCoverageRecords.iterator(); iterator.hasNext(); ) {
                            EInstanceCoverageRecord eInstanceCoverageRecord = iterator.next();
                            String coverageEndDate = eInstanceCoverageRecord.getCoverageEndDate();
                            String coverageEndIssue = eInstanceCoverageRecord.getCoverageEndIssue();
                            String coverageEndVolume = eInstanceCoverageRecord.getCoverageEndVolume();
                            String coverageStartDate = eInstanceCoverageRecord.getCoverageStartDate();
                            String coverageStartVolume = eInstanceCoverageRecord.getCoverageStartVolume();
                            String coverageStartIssue = eInstanceCoverageRecord.getCoverageStartIssue();
                            appendData(sb, coverageEndDate);
                            appendData(sb, coverageEndIssue);
                            appendData(sb, coverageEndVolume);
                            appendData(sb, coverageStartDate);
                            appendData(sb, coverageStartVolume);
                            appendData(sb, coverageStartIssue);
                        }
                    }

                    List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecords = holdingsRecord.getEInstancePerpetualAccessRecords();
                    if (CollectionUtils.isNotEmpty(eInstancePerpetualAccessRecords)) {
                        for (Iterator<EInstancePerpetualAccessRecord> iterator = eInstancePerpetualAccessRecords.iterator(); iterator.hasNext(); ) {
                            EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = iterator.next();
                            if (null != eInstancePerpetualAccessRecord) {
                                String perpetualAccessEndDate = eInstancePerpetualAccessRecord.getPerpetualAccessEndDate();
                                String perpetualAccessEndIssue = eInstancePerpetualAccessRecord.getPerpetualAccessEndIssue();
                                String perpetualAccessEndVolume = eInstancePerpetualAccessRecord.getPerpetualAccessEndVolume();
                                String perpetualAccessStartDate = eInstancePerpetualAccessRecord.getPerpetualAccessStartDate();
                                String perpetualAccessStartIssue = eInstancePerpetualAccessRecord.getPerpetualAccessStartIssue();
                                String perpetualAccessStartVolume = eInstancePerpetualAccessRecord.getPerpetualAccessStartVolume();
                                appendData(sb, perpetualAccessEndDate);
                                appendData(sb, perpetualAccessEndIssue);
                                appendData(sb, perpetualAccessEndVolume);
                                appendData(sb, perpetualAccessStartDate);
                                appendData(sb, perpetualAccessStartIssue);
                                appendData(sb, perpetualAccessStartVolume);
                            }
                        }
                    }

                    List<ExtentNoteRecord> extentNoteRecords = extentOfOwnership.getExtentNoteRecords();
                    if (CollectionUtils.isNotEmpty(extentNoteRecords)) {
                        for (Iterator<ExtentNoteRecord> iterator = extentNoteRecords.iterator(); iterator.hasNext(); ) {
                            ExtentNoteRecord extentNoteRecord = iterator.next();
                            String noteType = extentNoteRecord.getType();
                            String noteValue = extentNoteRecord.getNote();
                            appendData(sb, noteType);
                            appendData(sb, noteValue);
                        }
                    }
                }
            }
        }

        CallNumberTypeRecord callNumberTypeRecord = getOleMemorizeService().getCallNumberTypeRecordById(getLongValue(holdingsRecord.getCallNumberTypeId()));
        if (null != callNumberTypeRecord && StringUtils.isNotBlank(holdingsRecord.getCallNumber())) {
            String number = holdingsRecord.getCallNumber();
            String prefix = holdingsRecord.getCallNumberPrefix();
            appendData(sb, number);
            appendData(sb, prefix);
            String shelvingSchemeCodeValue = holdingsRecord.getShelvingOrder();
            String shelvingSchemeFullValue = holdingsRecord.getShelvingOrder();
            appendData(sb, shelvingSchemeCodeValue);
            appendData(sb, shelvingSchemeFullValue);
            String shelvingOrderCodeValue = holdingsRecord.getShelvingOrder();
            String shelvingOrderFullValue = holdingsRecord.getShelvingOrder();
            appendData(sb, shelvingOrderCodeValue);
            appendData(sb, shelvingOrderFullValue);
        }

        List<OLEHoldingsDonorRecord> donorList = holdingsRecord.getOLEHoldingsDonorRecords();
        if (CollectionUtils.isNotEmpty(donorList)) {
            for (Iterator<OLEHoldingsDonorRecord> iterator = donorList.iterator(); iterator.hasNext(); ) {
                OLEHoldingsDonorRecord oleHoldingsDonorRecord = iterator.next();
                if (null != oleHoldingsDonorRecord) {
                    String donorCode = oleHoldingsDonorRecord.getDonorCode();
                    String donorInfoNote = oleHoldingsDonorRecord.getDonorNote();
                    String donorInfoPublicDisplay = oleHoldingsDonorRecord.getDonorDisplayNote();
                    appendData(sb, donorCode);
                    appendData(sb, donorInfoNote);
                    appendData(sb, donorInfoPublicDisplay);

                }
            }
        }

        String accessPassword = holdingsRecord.getAccessPassword();
        String accessUsername = holdingsRecord.getAccessUsername();
        AuthenticationTypeRecord authenticationTypeRecord = getOleMemorizeService().getAuthenticationTypeRecordById(holdingsRecord.getAuthenticationTypeId());
        String authenticationType = (null != authenticationTypeRecord ? authenticationTypeRecord.getCode() : "");
        String numberOfSimultaneousUser = String.valueOf(holdingsRecord.getNumberSimultUsers());
        String proxiedResource = holdingsRecord.getProxiedResource();
        appendData(sb, accessPassword);
        appendData(sb, accessUsername);
        appendData(sb, authenticationType);
        appendData(sb, numberOfSimultaneousUser);
        appendData(sb, proxiedResource);

        List<HoldingsUriRecord> holdingsUriRecords = holdingsRecord.getHoldingsUriRecords();
        if (CollectionUtils.isNotEmpty(holdingsUriRecords)) {
            for (Iterator<HoldingsUriRecord> iterator = holdingsUriRecords.iterator(); iterator.hasNext(); ) {
                HoldingsUriRecord holdingsUriRecord = iterator.next();
                if (null != holdingsUriRecord) {
                    String text = holdingsUriRecord.getText();
                    String url = holdingsUriRecord.getUri();
                    appendData(sb, text);
                    appendData(sb, url);
                }
            }
        }

        List<HoldingsNoteRecord> holdingsNoteRecords = holdingsRecord.getHoldingsNoteRecords();
        if (CollectionUtils.isNotEmpty(holdingsNoteRecords)) {
            for (Iterator<HoldingsNoteRecord> iterator = holdingsNoteRecords.iterator(); iterator.hasNext(); ) {
                HoldingsNoteRecord holdingsNoteRecord = iterator.next();
                if (null != holdingsNoteRecord) {
                    String noteValue = holdingsNoteRecord.getNote();
                    String noteType = holdingsNoteRecord.getType();
                    appendData(sb, noteValue);
                    appendData(sb, noteType);
                }
            }
        }

        String adminPassword = holdingsRecord.getAdminPassword();
        String adminUrl = holdingsRecord.getAdminUrl();
        String adminUserName = holdingsRecord.getAdminUsername();
        String platformName = holdingsRecord.getPlatform();
        appendData(sb, adminPassword);
        appendData(sb, adminUrl);
        appendData(sb, adminUserName);
        appendData(sb, platformName);

        List<HoldingsStatisticalSearchRecord> oleDsHoldingsStatSearchTs = holdingsRecord.getHoldingsStatisticalSearchRecords();

        if (CollectionUtils.isNotEmpty(oleDsHoldingsStatSearchTs)) {
            StatisticalSearchRecord statisticalSearchRecordById = getOleMemorizeService().getStatisticalSearchRecordById(getLongValue(oleDsHoldingsStatSearchTs.get(0).getStatSearchCodeId()));
            if(null != statisticalSearchRecordById) {
                String codeValue = statisticalSearchRecordById.getStatSrchCd();
                appendData(sb, codeValue);
            }
        }
        appendData(sb, holdingsRecord.getLocation());
        appendData(sb, holdingsRecord.getLocationLevel());
        return sb.toString();
    }

    private SolrInputDocument addHoldingsDetailsToBib(SolrInputDocument sourceSolrInputDocument, SolrInputDocument destinationSolrInputDocument) {

        if(null != destinationSolrInputDocument) {
            addDetails(sourceSolrInputDocument, destinationSolrInputDocument, URI_SEARCH);
            addDetails(sourceSolrInputDocument, destinationSolrInputDocument, HOLDINGS_IDENTIFIER);
            return destinationSolrInputDocument;
        }
        return null;
    }
}
