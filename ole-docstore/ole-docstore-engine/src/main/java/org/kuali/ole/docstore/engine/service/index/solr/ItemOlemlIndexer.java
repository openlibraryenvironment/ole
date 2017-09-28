package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreIndexException;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.engine.service.storage.DocstoreRDBMSStorageService;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.utility.XMLUtility;
import org.kuali.ole.utility.Constants;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemOlemlIndexer extends DocstoreSolrIndexService implements DocstoreConstants {

    private static final Logger LOG = LoggerFactory.getLogger(ItemOlemlIndexer.class);
    private static ItemOlemlIndexer itemOlemlIndexer = null;

    public static ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    public static XMLUtility xmlUtility = new XMLUtility();

    public static ItemOlemlIndexer getInstance() {
        if(itemOlemlIndexer == null) {
            itemOlemlIndexer = new ItemOlemlIndexer();
        }
        return itemOlemlIndexer;
    }

    protected void updateRecordInSolr(Object object, List<SolrInputDocument> solrInputDocuments) {
        LOG.info("Class - ItemOlemlIndexer");
        LOG.info("Method - updateRecordInSolr");
        Item itemDocument = (Item)object;
        LOG.info("Incoming Item Document " + itemDocument.toString());
        String id = itemDocument.getId();
        if(!DocumentUniqueIDPrefix.hasPrefix(id)){
            id="wio-" +itemDocument.getId();
            itemDocument.setId(id);
        }
        SolrInputDocument solrInputDocument = getSolrInputFieldsForItem(itemDocument);

        SolrDocument solrDocument = null;
        try {
            solrDocument = getSolrDocumentByUUID(id);
        } catch (Exception e){
            BibMarcIndexer bibMarcIndexer = new BibMarcIndexer();
            DocstoreRDBMSStorageService rdbmsStorageService = new DocstoreRDBMSStorageService();
            bibMarcIndexer.createTree(rdbmsStorageService.retrieveBibTree(rdbmsStorageService.retrieveItem(id).getHolding().getBib().getId()));
            solrDocument = getSolrDocumentByUUID(id);
        }
        Object bibs=null;
        if (solrDocument != null && solrDocument.size() > 0) {
             bibs = solrDocument.getFieldValue(BIB_IDENTIFIER);
            Object holdingsIds = solrDocument.getFieldValue(HOLDINGS_IDENTIFIER);
            solrInputDocument.addField(HOLDINGS_IDENTIFIER, holdingsIds);
            solrInputDocument.addField(BIB_IDENTIFIER, bibs);
        }

        addBibInfoForHoldingsOrItems1(solrInputDocument, solrDocument);
        List<String> bibIds= new ArrayList<>();
        if (bibs instanceof String) {
            bibIds.add((String) bibs);
        } else if (bibs instanceof List) {
            bibIds = (List) bibs;
        }
        addItemDetailsToBib(solrInputDocuments, solrInputDocument, bibs, bibIds);
        solrInputDocuments.add(solrInputDocument);
        LOG.info("Solr Input Document " + solrInputDocument.toString());
    }

    protected void updateRecordInSolrForItem(Object object, List<SolrInputDocument> solrInputDocuments, SolrInputDocument holdingsSolrInputDocument) {
        LOG.info("Class - ItemOlemlIndexer");
        LOG.info("Method - updateRecordInSolrForItem");
        Item itemDocument = (Item) object;
        LOG.info("Incoming Item Document " + itemDocument.toString());
        String bibId = itemDocument.getHolding().getBib().getId();
        String holdingsId = itemDocument.getHolding().getId();
        SolrInputDocument solrInputDocument = getSolrInputFieldsForItem(itemDocument);
        if (itemDocument.isAnalytic()) {
            List<String> holdingsIds = new ArrayList<>();
            for (Holdings holdings : itemDocument.getHoldings()) {
                holdingsIds.add(holdings.getId());
            }
            solrInputDocument.addField(HOLDINGS_IDENTIFIER, holdingsIds);
        }
        solrInputDocument.addField(HOLDINGS_IDENTIFIER, holdingsId);
        solrInputDocument.addField(BIB_IDENTIFIER, bibId);
        addBibAndHoldingsInfoToItem(solrInputDocument, holdingsSolrInputDocument);
        solrInputDocuments.add(solrInputDocument);
        LOG.info("Solr Input Document " + solrInputDocument.toString());
    }

    protected void buildSolrInputDocument(Object object, List<SolrInputDocument> solrInputDocuments) {


        Item itemDocument = (Item) object;
        String holdingsId = itemDocument.getHolding().getId();

        SolrInputDocument solrInputDocument = getSolrInputFieldsForItem(itemDocument);

        if (holdingsId != null) {
            solrInputDocument.addField(HOLDINGS_IDENTIFIER, holdingsId);
        }
        SolrDocument holdingsSolrDoc = getSolrDocumentByUUID(holdingsId);
        Object bibs = holdingsSolrDoc.getFieldValue(BIB_IDENTIFIER);
        holdingsSolrDoc.addField(ITEM_IDENTIFIER, itemDocument.getId());
        SolrInputDocument holdingsSolrInput = buildSolrInputDocFromSolrDoc(holdingsSolrDoc);
        List<String> bibIds = new ArrayList<>();

        if (bibs instanceof String) {
            bibIds.add((String) bibs);
        } else if (bibs instanceof List) {
            bibIds = (List) bibs;
        }

        addBibInfoForHoldingsOrItems(solrInputDocument, holdingsSolrDoc);

        solrInputDocument.addField("bibIdentifier", bibIds);
        solrInputDocuments.add(solrInputDocument);
        solrInputDocuments.add(holdingsSolrInput);

        addItemDetailsToBib(solrInputDocuments, solrInputDocument, bibs, bibIds);
    }



    protected void buildSolrInputDocumentForBatchProcess(Object object, List<SolrInputDocument> solrInputDocuments, SolrInputDocument holdingsSolrInputs) {
        Item itemDocument = (Item) object;
        String holdingsId = itemDocument.getHolding().getId();

        SolrInputDocument solrInputDocument = getSolrInputFieldsForItem(itemDocument);

        if (holdingsId != null) {
            solrInputDocument.addField(HOLDINGS_IDENTIFIER, holdingsId);
        }
        SolrDocument holdingsSolrDoc = getSolrDocumentByUUID(holdingsId);
        Object bibs = holdingsSolrDoc.getFieldValue(BIB_IDENTIFIER);
        holdingsSolrDoc.addField(ITEM_IDENTIFIER, itemDocument.getId());
        if(holdingsSolrInputs == null ){
          SolrInputDocument holdingsSolrInput = buildSolrInputDocFromSolrDoc(holdingsSolrDoc);
          solrInputDocuments.add(holdingsSolrInput);
        }

        List<String> bibIds = new ArrayList<>();

        if (bibs instanceof String) {
            bibIds.add((String) bibs);
        } else if (bibs instanceof List) {
            bibIds = (List) bibs;
        }

        addBibInfoForHoldingsOrItems(solrInputDocument, holdingsSolrDoc);

        solrInputDocument.addField(BIB_IDENTIFIER, bibIds);
        solrInputDocuments.add(solrInputDocument);
        addItemDetailsToBib(solrInputDocuments, solrInputDocument, bibs, bibIds);

    }


    protected SolrInputDocument getSolrInputFieldsForItem(Item itemDocument) {
        org.kuali.ole.docstore.common.document.content.instance.Item item = null;
        if (StringUtils.isNotEmpty(itemDocument.getContent())) {
            item = itemOlemlRecordProcessor.fromXML(itemDocument.getContent());
        }
        else if(itemDocument.getContentObject() != null) {
            item = (org.kuali.ole.docstore.common.document.content.instance.Item) itemDocument.getContentObject();
        }

        SolrInputDocument solrInputDocument = new SolrInputDocument();

        solrInputDocument.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrInputDocument.addField(DOC_TYPE, DOC_TYPE_ITEM_VALUE);
        solrInputDocument.addField(DOC_FORMAT, DOC_FORMAT_INSTANCE_VALUE);
        solrInputDocument.addField(ID, itemDocument.getId());
        solrInputDocument.addField(ITEM_IDENTIFIER, itemDocument.getId());
        solrInputDocument.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(itemDocument.getId()));
        solrInputDocument.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(itemDocument.getId()));


        solrInputDocument.addField(CLMS_RET_FLAG, item.isClaimsReturnedFlag());
        solrInputDocument.addField(CLMS_RET_FLAG_CRE_DATE, item.getClaimsReturnedFlagCreateDate());
        solrInputDocument.addField(CLMS_RET_NOTE, item.getClaimsReturnedNote());
        solrInputDocument.addField(CURRENT_BORROWER, item.getCurrentBorrower());
        solrInputDocument.addField(PROXY_BORROWER, item.getProxyBorrower());
        solrInputDocument.addField(DUE_DATE_TIME, item.getDueDateTime());
        solrInputDocument.addField(ORG_DUE_DATE_TIME, item.getOriginalDueDate());
        solrInputDocument.addField(ITEM_STATUS_EFFECTIVE_DATE, item.getItemStatusEffectiveDate());
        solrInputDocument.addField(CHECK_OUT_DUE_DATE_TIME, item.getCheckOutDateTime());
        solrInputDocument.addField(STAFF_ONLY_FLAG, itemDocument.isStaffOnly());
        solrInputDocument.addField(IS_ANALYTIC, itemDocument.isAnalytic());
        solrInputDocument.addField(ITEM_IDENTIFIER_SEARCH, itemDocument.getId());
        solrInputDocument.addField(BARCODE_ARSL_SEARCH, item.getBarcodeARSL());
        solrInputDocument.addField(COPY_NUMBER_SEARCH, item.getCopyNumber());
        solrInputDocument.addField(COPY_NUMBER_LABEL_SEARCH, item.getCopyNumberLabel());
        solrInputDocument.addField(PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_SEARCH, item.getPurchaseOrderLineItemIdentifier());
        solrInputDocument.addField(VENDOR_LINE_ITEM_IDENTIFIER_SEARCH, item.getVendorLineItemIdentifier());
        solrInputDocument.addField(COPY_NUMBER_LABEL_SEARCH, item.getCopyNumberLabel());
        solrInputDocument.addField(VOLUME_NUMBER_LABEL_SEARCH, item.getVolumeNumberLabel());
        solrInputDocument.addField(VOLUME_NUMBER_SEARCH, item.getVolumeNumberLabel());
        solrInputDocument.addField(ENUMERATION_SEARCH, item.getEnumeration());
        solrInputDocument.addField(CHRONOLOGY_SEARCH, item.getChronology());
        solrInputDocument.addField(MISSING_PIECE_FLAG_NOTE_SEARCH, item.getMissingPieceFlagNote());
        solrInputDocument.addField(CLAIMS_RETURNED_NOTE_SEARCH, item.getClaimsReturnedNote());
        solrInputDocument.addField(DAMAGED_ITEM_NOTE_SEARCH, item.getDamagedItemNote());
        solrInputDocument.addField(MISSING_PIECE_FLAG_SEARCH, item.isMissingPieceFlag());
        solrInputDocument.addField(CLAIMS_RETURNED_FLAG_SEARCH, item.isClaimsReturnedFlag());
        solrInputDocument.addField(ITEM_DAMAGED_FLAG_SEARCH, item.isItemDamagedStatus());
        solrInputDocument.addField(MISSING_PIECE_COUNT_SEARCH,item.getMissingPiecesCount());
        solrInputDocument.addField(NUMBER_OF_PIECES_SEARCH,item.getNumberOfPieces());
        Date date = new Date();
        // Item call number should be indexed if it is available at item level or holdings level.
        String itemCallNumber = null;
      if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
            if (StringUtils.isNotEmpty(item.getCallNumber().getNumber())) {
                itemCallNumber = item.getCallNumber().getNumber();
                solrInputDocument.addField(CALL_NUMBER_SEARCH, item.getCallNumber().getNumber());
                solrInputDocument.addField(CALL_NUMBER_DISPLAY, item.getCallNumber().getNumber());
                solrInputDocument.setField(CALL_NUMBER_SORT, item.getCallNumber().getNumber());
            }
        }

        if (item.getCallNumber() != null) {
            solrInputDocument.addField(CALL_NUMBER_TYPE_SEARCH, item.getCallNumber().getType());
            solrInputDocument.addField(CALL_NUMBER_PREFIX_SEARCH, item.getCallNumber().getPrefix());
            solrInputDocument.addField(CLASSIFICATION_PART_SEARCH, item.getCallNumber().getClassificationPart());

            solrInputDocument.addField(CALL_NUMBER_TYPE_DISPLAY, item.getCallNumber().getType());
            solrInputDocument.addField(CALL_NUMBER_PREFIX_DISPLAY, item.getCallNumber().getPrefix());
            solrInputDocument.addField(CLASSIFICATION_PART_DISPLAY, item.getCallNumber().getClassificationPart());

            //Shelving scheme code should be indexed if it is available at holdings level
            String shelvingSchemeCode = "";
            String shelvingSchemeValue = "";

            if(item.getCallNumber() != null && item.getCallNumber().getShelvingScheme() != null && item.getCallNumber().getShelvingScheme().getCodeValue() != null) {
                shelvingSchemeCode = item.getCallNumber().getShelvingScheme().getCodeValue();
                shelvingSchemeValue = item.getCallNumber().getShelvingScheme().getFullValue();
                if (StringUtils.isNotEmpty(shelvingSchemeCode)) {
                    solrInputDocument.addField(SHELVING_SCHEME_CODE_SEARCH, shelvingSchemeCode);
                    solrInputDocument.addField(SHELVING_SCHEME_CODE_DISPLAY, shelvingSchemeCode);
                }
                if (StringUtils.isNotEmpty(shelvingSchemeValue)) {
                    solrInputDocument.addField(SHELVING_SCHEME_VALUE_SEARCH, shelvingSchemeValue);
//                    solrInputDocument.addField(SHELVING_SCHEME_CODE_SEARCH, item.getCallNumber().getShelvingScheme().getCodeValue());

                    solrInputDocument.addField(SHELVING_SCHEME_VALUE_DISPLAY, shelvingSchemeValue);
//                    solrInputDocument.addField(SHELVING_SCHEME_CODE_DISPLAY, item.getCallNumber().getShelvingScheme().getCodeValue());
                }
            }

            String shelvingOrder = null;
            //TODO:Shelving order not present at item level
            if (item.getCallNumber().getShelvingOrder() != null) {
                shelvingOrder = item.getCallNumber().getShelvingOrder().getFullValue();
            }
            if (StringUtils.isEmpty(shelvingOrder) && item.getCallNumber() != null) {
                try {
                    //Build sortable key for a valid call number
                    if (item.getCallNumber().getShelvingScheme() != null) {
//                        boolean isValid = validateCallNumber(itemCallNumber, item.getCallNumber().getShelvingScheme().getCodeValue());
//                        if (isValid) {
//                            shelvingOrder = buildSortableCallNumber(itemCallNumber, item.getCallNumber().getShelvingScheme().getCodeValue());
//                        } else {
//                            shelvingOrder = itemCallNumber;
//                        }
                        if(StringUtils.isNotEmpty(itemCallNumber) && itemCallNumber.trim().length() > 0) {
                            shelvingOrder = buildSortableCallNumber(itemCallNumber, item.getCallNumber().getShelvingScheme().getCodeValue());
                        }
                    }
                } catch (Exception e) {
                    LOG.info("Exception due to :" + e.getMessage(), e);
                    LOG.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
                }
                // shelvingOrder = oleInstance.getOleHoldings().getCallNumber().getShelvingOrder().getFullValue();
            }
            if (StringUtils.isNotEmpty(shelvingOrder)) {
                shelvingOrder = shelvingOrder.replaceAll(" ", "-");
                solrInputDocument.addField(SHELVING_ORDER_SORT, shelvingOrder + itemDocument.getId());
                solrInputDocument.addField(SHELVING_ORDER_SEARCH, shelvingOrder);
                solrInputDocument.addField(SHELVING_ORDER_DISPLAY, shelvingOrder);
            }
            if (item.getCallNumber() != null && item.getCallNumber().getPrefix() != null) {
                solrInputDocument.addField(CALLNUMBER_PREFIX_SORT, item.getCallNumber().getPrefix());
            }
            if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                solrInputDocument.setField(CALLNUMBER_SORT, item.getCallNumber().getNumber());
            }
            if (item.getEnumeration() != null) {
                String enumerationSort = getNormalizedEnumeration(item.getEnumeration());
                solrInputDocument.addField(ENUMERATION_SORT, enumerationSort);
            }
            if (item.getChronology() != null) {
                solrInputDocument.addField(CHRONOLOGY_SORT, item.getChronology());
            }
            if (item.getCopyNumber() != null) {
                String copyNumberSort = getNormalizedEnumeration(item.getCopyNumber());
                solrInputDocument.addField(COPYNUMBER_SORT, copyNumberSort);
            }
            if (item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                solrInputDocument.addField(ITEM_BARCODE_SORT, item.getAccessInformation().getBarcode());
            }
        }

        if (item.getItemStatus() != null) {
            solrInputDocument.addField(ITEM_STATUS_DISPLAY, item.getItemStatus().getCodeValue());
            solrInputDocument.addField(ITEM_STATUS_SEARCH, item.getItemStatus().getCodeValue());
            solrInputDocument.addField(ITEM_STATUS_SORT, item.getItemStatus().getCodeValue());
        }
        StringBuffer loactionLevelStr = new StringBuffer(" ");
        if (item.getLocation() != null &&
                item.getLocation().getLocationLevel() != null) {
            StringBuffer locationName = new StringBuffer();
            StringBuffer locationLevel = new StringBuffer();
            Location location = item.getLocation();
            buildLocationNameAndLocationLevel(location, locationName, locationLevel);
            buildLocationName(location, solrInputDocument,loactionLevelStr);
            solrInputDocument.addField(LOCATION_LEVEL_SEARCH, locationName.toString().replaceAll("-",""));
            solrInputDocument.addField(LOCATION_LEVEL_NAME_SEARCH, locationLevel.toString());
            solrInputDocument.addField(LOCATION_LEVEL_DISPLAY, locationName.toString());
            solrInputDocument.addField(LOCATION_LEVEL_NAME_DISPLAY, locationLevel.toString());
            solrInputDocument.addField(LOCATION_LEVEL_SORT, locationName.toString());
        }

        solrInputDocument.addField(ALL_TEXT, getAllTextValueForItem(item) + loactionLevelStr.toString());
        if (item.getItemType() != null) {
            solrInputDocument.addField(ITEM_TYPE_FULL_VALUE_SEARCH, item.getItemType().getFullValue());
            solrInputDocument.addField(ITEM_TYPE_CODE_VALUE_SEARCH, item.getItemType().getCodeValue());
            solrInputDocument.addField(ITEM_TYPE_FULL_VALUE_DISPLAY, item.getItemType().getFullValue());
            solrInputDocument.addField(ITEM_TYPE_CODE_VALUE_DISPLAY, item.getItemType().getCodeValue());
        }

        if (item.getTemporaryItemType() != null) {
            solrInputDocument.addField(TEMPORARY_ITEM_TYPE_FULL_VALUE_SEARCH, item.getTemporaryItemType().getFullValue());
            solrInputDocument.addField(TEMPORARY_ITEM_TYPE_CODE_VALUE_SEARCH, item.getTemporaryItemType().getCodeValue());
            solrInputDocument.addField(TEMPORARY_ITEM_TYPE_FULL_VALUE_DISPLAY, item.getTemporaryItemType().getFullValue());
            solrInputDocument.addField(TEMPORARY_ITEM_TYPE_CODE_VALUE_DISPLAY, item.getTemporaryItemType().getCodeValue());
        }

        if (item.getAccessInformation() != null) {
            if (item.getAccessInformation().getBarcode() != null) {
                solrInputDocument.addField(ITEM_BARCODE_SEARCH, item.getAccessInformation().getBarcode());
                solrInputDocument.addField(ITEM_BARCODE_DISPLAY, item.getAccessInformation().getBarcode());
            }
            if (item.getAccessInformation().getUri() != null) {
                solrInputDocument.addField(ITEM_URI_SEARCH, item.getAccessInformation().getUri().getValue());
                solrInputDocument.addField(ITEM_URI_DISPLAY, item.getAccessInformation().getUri().getValue());
            }
        }

        for (StatisticalSearchingCode searchingCode : item.getStatisticalSearchingCode()) {
            if (searchingCode != null) {
                solrInputDocument.addField(STATISTICAL_SEARCHING_CODE_VALUE_SEARCH, searchingCode.getCodeValue());
                solrInputDocument.addField(STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY, searchingCode.getCodeValue());
                solrInputDocument.addField(STATISTICAL_SEARCHING_FULL_VALUE_SEARCH, searchingCode.getFullValue());
                solrInputDocument.addField(STATISTICAL_SEARCHING_FULL_VALUE_DISPLAY, searchingCode.getFullValue());
            }
        }
        solrInputDocument.addField(ITEM_IDENTIFIER_DISPLAY, itemDocument.getId());
        solrInputDocument.addField(BARCODE_ARSL_DISPLAY, item.getBarcodeARSL());
        solrInputDocument.addField(COPY_NUMBER_DISPLAY, item.getCopyNumber());
        solrInputDocument.addField(COPY_NUMBER_LABEL_DISPLAY, item.getCopyNumberLabel());
        solrInputDocument.addField(PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_DISPLAY, item.getPurchaseOrderLineItemIdentifier());
        solrInputDocument.addField(VENDOR_LINE_ITEM_IDENTIFIER_DISPLAY, item.getVendorLineItemIdentifier());
        solrInputDocument.addField(COPY_NUMBER_LABEL_DISPLAY, item.getCopyNumberLabel());
        solrInputDocument.addField(VOLUME_NUMBER_LABEL_DISPLAY, item.getVolumeNumberLabel());
        solrInputDocument.addField(VOLUME_NUMBER_DISPLAY, item.getVolumeNumber());
        solrInputDocument.addField(ENUMERATION_DISPLAY, item.getEnumeration());
        solrInputDocument.addField(CHRONOLOGY_DISPLAY, item.getChronology());
        solrInputDocument.addField(MISSING_PIECE_FLAG_NOTE_DISPLAY, item.getMissingPieceFlagNote());
        solrInputDocument.addField(CLAIMS_RETURNED_NOTE_DISPLAY, item.getClaimsReturnedNote());
        solrInputDocument.addField(DAMAGED_ITEM_NOTE_DISPLAY, item.getDamagedItemNote());
        solrInputDocument.addField(MISSING_PIECE_FLAG_DISPLAY, item.isMissingPieceFlag());
        solrInputDocument.addField(CLAIMS_RETURNED_FLAG_DISPLAY, item.isClaimsReturnedFlag());
        solrInputDocument.addField(ITEM_DAMAGED_FLAG_DISPLAY, item.isItemDamagedStatus());
        solrInputDocument.addField(MISSING_PIECE_COUNT_DISPLAY,item.getMissingPiecesCount());
        solrInputDocument.addField(NUMBER_OF_PIECES_DISPLAY,item.getNumberOfPieces());
        solrInputDocument.addField(CREATED_BY,itemDocument.getCreatedBy());
        solrInputDocument.addField(UPDATED_BY,itemDocument.getUpdatedBy());
        Date createdDate = null;
        date = new Date();
        if (StringUtils.isNotBlank(itemDocument.getUpdatedOn())) {
            solrInputDocument.setField(DATE_UPDATED, getDate(itemDocument.getUpdatedOn()));
        } else {
            if (StringUtils.isNotBlank(itemDocument.getCreatedOn())) {
                solrInputDocument.setField(DATE_UPDATED, createdDate);
            } else {
                solrInputDocument.setField(DATE_UPDATED, date);
            }
        }
        if (StringUtils.isNotBlank(itemDocument.getCreatedOn())) {
            createdDate = getDate(itemDocument.getCreatedOn());
            solrInputDocument.setField(DATE_ENTERED, createdDate);
        } else {
            solrInputDocument.setField(DATE_ENTERED, date);
        }

        for (DonorInfo donorInfo : item.getDonorInfo()) {
            solrInputDocument.addField(DONOR_CODE_SEARCH, donorInfo.getDonorCode());
            solrInputDocument.addField(DONOR_CODE_DISPLAY, donorInfo.getDonorCode());
            solrInputDocument.addField(DONOR_PUBLIC_DISPLAY, donorInfo.getDonorPublicDisplay());
            solrInputDocument.addField(DONOR_NOTE_DISPLAY, donorInfo.getDonorNote());
        }
        if (item.getHighDensityStorage() != null) {
            solrInputDocument.addField(HIGHDENSITYSTORAGE_ROW_DISPLAY, item.getHighDensityStorage().getRow());
            solrInputDocument.addField(HIGHDENSITYSTORAGE_MODULE_DISPLAY, item.getHighDensityStorage().getModule());
            solrInputDocument.addField(HIGHDENSITYSTORAGE_SHELF_DISPLAY, item.getHighDensityStorage().getShelf());
            solrInputDocument.addField(HIGHDENSITYSTORAGE_TRAY_DISPLAY, item.getHighDensityStorage().getTray());
        }
        for (Note note : item.getNote()) {
            solrInputDocument.addField(ITEMNOTE_VALUE_DISPLAY, note.getValue());
            solrInputDocument.addField(ITEMNOTE_TYPE_DISPLAY, note.getType());
        }
        solrInputDocument.addField(NUMBER_OF_RENEW, item.getNumberOfRenew());
        solrInputDocument.addField(UNIQUE_ID, itemDocument.getId());
        return solrInputDocument;
    }

    private Date getDate(String dateStr) {
        DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            if (StringUtils.isNotEmpty(dateStr)) {
                return format.parse(dateStr);
            } else {
                return new Date();
            }

        } catch (ParseException e) {
            LOG.info("Exception : " + dateStr + " for format:: " + Constants.DATE_FORMAT, e);
            return new Date();
        }
    }

    protected boolean validateCallNumber(String callNumber, String codeValue) throws OleDocStoreException {
        boolean isValid = false;
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                isValid = callNumberObj.isValid();
            }
        }
        return isValid;
    }
    protected void processRecord(SolrServer solrServer, String id, List<SolrInputDocument> solrInputDocumentList) throws IOException, SolrServerException {
        boolean isDocumentExists = false;
        SolrInputDocument solrInputDocumentExists = null;
        for (SolrInputDocument solrInputDocument : solrInputDocumentList) {
            List<String> ids = new ArrayList<>();
            SolrInputField docType = solrInputDocument.get("DocType");
            if (docType.getValue().equals("holdings")) {
                SolrInputField itemIds = solrInputDocument.get("itemIdentifier");
                Object object = itemIds.getValue();
                if (object instanceof List) {
                    ids.addAll((List) object);
                } else {
                    ids.add((String) object);
                }
                for (Object itemId : ids) {
                    if (itemId.equals(id)) {
                        solrInputDocumentExists = solrInputDocument;
                        isDocumentExists = true;
                        break;
                    }
                }
            }
        }

        if (!isDocumentExists) {
            processDocument(solrServer, id, solrInputDocumentList);
        } else {
            solrInputDocumentExists.getFieldValues("itemIdentifier").remove(id);
        }
    }

    private void processDocument(SolrServer solrServer, String id, List<SolrInputDocument> solrInputDocumentList) throws SolrServerException {
        String query = "itemIdentifier:" + id + " AND DocType:holdings";
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        QueryResponse response = solrServer.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        for (SolrDocument instanceSolrDocument : solrDocumentList) {
            List<String> itemIdentifierList = new ArrayList<String>();
            Object itemIdentifier = instanceSolrDocument.getFieldValue(ITEM_IDENTIFIER);
            if (itemIdentifier instanceof List) {
                itemIdentifierList = (List<String>) itemIdentifier;
                if (itemIdentifierList.contains(id)) {
                    itemIdentifierList.remove(id);
                    instanceSolrDocument.setField(ITEM_IDENTIFIER, itemIdentifierList);
                }
            } else if (itemIdentifier instanceof String) {
                String itemId = (String) itemIdentifier;
                if (itemId.equalsIgnoreCase(id)) {
                    itemIdentifier = null;
                    instanceSolrDocument.setField(ITEM_IDENTIFIER, itemIdentifier);
                }
            }
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(instanceSolrDocument));
        }
    }


    protected void deleteRecordInSolr(SolrServer solrServer, String id) throws IOException, SolrServerException {
        UpdateResponse updateResponse = solrServer.deleteById(id);
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        processDocument(solrServer, id, solrInputDocumentList);
        indexSolrDocuments(solrInputDocumentList, true);
    }

    /*protected void indexSolrDocuments(List<SolrInputDocument> solrDocs, boolean isCommit) {
        SolrServer solr = null;
        try {
            solr = SolrServerManager.getInstance().getSolrServer();
            UpdateResponse response = solr.add(solrDocs);
            if (isCommit) {
                solr.commit(false, false);
            }
        } catch (SolrServerException e) {
            rollback(solr);
            throw new DocstoreIndexException(e.getMessage());
        } catch (IOException e) {
            LOG.info("Exception :", e);
            rollback(solr);
            throw new DocstoreIndexException(e.getMessage());
        }
    }*/

    protected void rollback(SolrServer solrServer) {
        try {
            solrServer.rollback();
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        } catch (IOException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        }
    }

    public String getNormalizedEnumeration(String enumation) {
        if (enumation.contains(".")) {
            StringBuffer resultBuf = new StringBuffer();
            String[] splitEnum = enumation.split("\\.");
            if (splitEnum.length > 1) {
                String enumerationNo = splitEnum[1];
                String enumBufAfterDot = null;
                String enumBufAfterSpecial = null;
                String normalizedEnum = null;
                if (enumerationNo != null && (enumerationNo.trim().length() > 0)) {
                    int pos = 0;
                    boolean numCheck = false;
                    for (int i = 0; i < enumerationNo.length(); i++) {
                        char c = enumerationNo.charAt(i);
                        String convertedEnum = String.valueOf(c);
                        if (convertedEnum.matches("[0-9]")) {
                            if (Character.isDigit(c)) {
                                pos = i;
                                numCheck = true;
                            } else {
                                break;
                            }
                        } else {
                            if (pos == 0 && numCheck == false) {
                                return enumation;
                            }
                            break;
                        }
                    }
                    enumBufAfterDot = enumerationNo.substring(0, pos + 1);
                    normalizedEnum = normalizeFloatForEnumeration(enumBufAfterDot, 5);
                    enumBufAfterSpecial = enumerationNo.substring(pos + 1);
                    splitEnum[1] = normalizedEnum + enumBufAfterSpecial;
                }
                for (int j = 0; j < splitEnum.length; j++) {
                    resultBuf.append(splitEnum[j]);
                    resultBuf.append(".");
                }

                return resultBuf.substring(0, resultBuf.length() - 1).toString();
            } else {
                return enumation;
            }
        } else {
            return enumation;
        }
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



    protected void modifySolrDocForDestination(String holdingsId, List<String> itemIds, List<SolrInputDocument> solrInputDocumentListFinal) {
        updateSolrDocForDestination(holdingsId, itemIds, solrInputDocumentListFinal);
        /*SolrDocument solrDocumentForDestinationInstance = getSolrDocumentByUUID(holdingsId);

        for (String item : itemIds) {
            SolrDocument solrDocumentForItem = getSolrDocumentByUUID(item);
                String sourceInstanceUUID = (String) solrDocumentForItem.getFieldValue("holdingsIdentifier");
                removeItemsInSourceInstance(sourceInstanceUUID, item,solrInputDocumentListFinal);
            solrDocumentForItem
                    .setField("bibIdentifier", solrDocumentForDestinationInstance.getFieldValue("bibIdentifier"));
            solrDocumentForItem
                    .setField("holdingsIdentifier", solrDocumentForDestinationInstance.getFieldValue("id"));

            SolrInputDocument solrInputDocument = new SolrInputDocument();
            buildSolrInputDocFromSolrDoc(solrDocumentForItem,solrInputDocument);
            solrInputDocumentListFinal.add(solrInputDocument);
        }*/
    }

    protected void updateSolrDocForDestination(String holdingsId, List<String> itemIds, List<SolrInputDocument> solrInputDocumentListFinal) {
        SolrDocument destinationHoldingsSolrDocument = getSolrDocumentByUUID(holdingsId);
        SolrDocument destinationBibSolrDocument = null;
        //Get the destination bib and add the items being transfered
        Object object = destinationHoldingsSolrDocument.getFieldValue(BIB_IDENTIFIER);
        List<String> bibIds = new ArrayList<>();
        if(object instanceof String){
            String bibId = (String) object;
            bibIds.add(bibId);
            SolrInputDocument destBibSolrInputDocument = new SolrInputDocument();
            destBibSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, bibId);
            destinationBibSolrDocument = getSolrDocumentByUUID(bibId);
            Object itemIdentifier = destinationBibSolrDocument.getFieldValue(ITEM_IDENTIFIER);
            List<String> existingItemIds = new ArrayList<String>();
            if(itemIdentifier instanceof String){
                String id = (String) itemIdentifier;
                existingItemIds.add(id);
            }else if(itemIdentifier instanceof List){
                List<String> existingItemIdentifierList = (List<String>) itemIdentifier;
                existingItemIds.addAll(existingItemIdentifierList);
            }
            Map<String, List<String>> itemIdMap = new HashMap<String, List<String>>();
            for(String id:itemIds){
                if(!existingItemIds.contains(id)){
                    existingItemIds.add(id);
                }
            }
            itemIdMap.put(AtomicUpdateConstants.SET, existingItemIds);
            destBibSolrInputDocument.setField(ITEM_IDENTIFIER, itemIdMap);
            solrInputDocumentListFinal.add(destBibSolrInputDocument);
        }else if(object instanceof List){
            // if holdings is bound -with
            List<String> bibIdentifierList = (List<String>) object;
            bibIds.addAll(bibIdentifierList);
            for(String bibId : bibIdentifierList){
                SolrInputDocument destBibSolrInputDocument = new SolrInputDocument();
                Object itemIdentifier = destinationBibSolrDocument.getFieldValue(ITEM_IDENTIFIER);
                List<String> existingItemIds = new ArrayList<String>();
                if(itemIdentifier instanceof String){
                    String id = (String) itemIdentifier;
                    existingItemIds.add(id);
                }else if(itemIdentifier instanceof List){
                    List<String> existingItemIdentifierList = (List<String>) itemIdentifier;
                    existingItemIds.addAll(existingItemIdentifierList);
                }
                for(String id:itemIds){
                    if(!existingItemIds.contains(id)){
                        existingItemIds.add(id);
                    }
                }
                destBibSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, bibId);
                Map<String, List<String>> itemIdMap = new HashMap<String, List<String>>();
                itemIdMap.put(AtomicUpdateConstants.ADD, existingItemIds);
                destBibSolrInputDocument.setField(ITEM_IDENTIFIER, itemIdMap);
                solrInputDocumentListFinal.add(destBibSolrInputDocument);
            }
        }

        //Get the destination holdings and add items being transfered
        SolrInputDocument destHoldingsSolrInputDocument = new SolrInputDocument();
        destHoldingsSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, holdingsId);
        Object existingItems = destinationHoldingsSolrDocument.getFieldValue(ITEM_IDENTIFIER);
        List<String> existingIds = new ArrayList<>();
        if(existingItems instanceof String){
            String id = (String) existingItems;
            existingIds.add(id);
        }else if(existingItems instanceof List){
            List<String> existingIdsList = (List<String>) existingItems;
            existingIds.addAll(existingIdsList);
        }
        Map<String, List<String>> itemIdMap = new HashMap<String, List<String>>();
        existingIds.addAll(itemIds);
        itemIdMap.put(AtomicUpdateConstants.SET, existingIds);
        destHoldingsSolrInputDocument.setField(ITEM_IDENTIFIER, itemIdMap);
        solrInputDocumentListFinal.add(destHoldingsSolrInputDocument);

        //Set the bib id and holdings id for items being transfered
        for (String item : itemIds) {
            SolrInputDocument itemSolrInputDocument = new SolrInputDocument();
            itemSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, item);
            Map<String, List<String>> bibIdMap = new HashMap<String, List<String>>();
            bibIdMap.put(AtomicUpdateConstants.SET, bibIds);
            itemSolrInputDocument.setField(BIB_IDENTIFIER, bibIdMap);
            bibIdMap.put(AtomicUpdateConstants.SET, bibIds);
            itemSolrInputDocument.setField(HOLDINGS_IDENTIFIER, holdingsId);
            setBibInfoForHoldingsOrItems(itemSolrInputDocument, destinationBibSolrDocument);
            solrInputDocumentListFinal.add(itemSolrInputDocument);
        }
    }



    private void removeItemsInSourceInstance(String sourceInstanceUuid, String itemId ,List<SolrInputDocument> solrInputDocumentListFinal) {

        List<String> itemIdentifierList = new ArrayList<>();
        SolrDocument solrDocumentForSourceInstance = getSolrDocumentByUUID(sourceInstanceUuid);
        Object field = solrDocumentForSourceInstance.getFieldValue("itemIdentifier");
        if (field instanceof String) {
            String instanceIdentifier = (String) solrDocumentForSourceInstance.getFieldValue("itemIdentifier");
            itemIdentifierList.add(instanceIdentifier);
        } else if (field instanceof List) {
            itemIdentifierList = (List) solrDocumentForSourceInstance.getFieldValue("itemIdentifier");
        }
        itemIdentifierList.remove(itemId);
        solrDocumentForSourceInstance.setField("itemIdentifier", itemIdentifierList);

        SolrInputDocument solrInputDocument = new SolrInputDocument();
        buildSolrInputDocFromSolrDoc(solrDocumentForSourceInstance, solrInputDocument);
        solrInputDocumentListFinal.add(solrInputDocument);
    }



    protected void modifySolrDocForSource(List<String> itemsIds, String holdingsId, List<SolrInputDocument> solrInputDocumentListFinal) {

        updateSolrDocForSource(itemsIds,holdingsId,solrInputDocumentListFinal);
        /*//Get the solr Document for holdings and add new Items to solr.
        SolrDocument solrDocumentForDestinationInstance = getSolrDocumentByUUID(holdingsId);
        solrDocumentForDestinationInstance.addField("itemIdentifier", itemsIds);

        SolrInputDocument destinationHoldingsDocument = new SolrInputDocument();
        buildSolrInputDocFromSolrDoc(solrDocumentForDestinationInstance, destinationHoldingsDocument);

        solrInputDocumentListFinal.add(destinationHoldingsDocument);*/
    }

    private void updateSolrDocForSource(List<String> itemsIds, String holdingsId, List<SolrInputDocument> solrInputDocumentListFinal){
        for(String itemId: itemsIds){
            SolrDocument itemSolrDocument = getSolrDocumentByUUID(itemId);

            //Get the bib id from item being transfered and remove the source bib id
            Object bibObject = itemSolrDocument.getFieldValue(BIB_IDENTIFIER);
            List<String> bibIds = new ArrayList<>();
            if (bibObject instanceof String) {
                String bibIdentifier = (String) bibObject;
                bibIds.add(bibIdentifier);
                SolrInputDocument sourceBibSolrInputDocument = new SolrInputDocument();
                sourceBibSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, bibIdentifier);
                Map<String, List<String>> itemMap= new HashMap<>();
                itemMap.put(AtomicUpdateConstants.REMOVE, itemsIds);
                sourceBibSolrInputDocument.setField(ITEM_IDENTIFIER, itemMap);
                solrInputDocumentListFinal.add(sourceBibSolrInputDocument);
            } else if (bibObject instanceof List) {
                List<String> bibIdentifierList = (List<String>) bibObject;
                for(String bibIdentifier : bibIdentifierList){
                    SolrInputDocument sourceBibSolrInputDocument = new SolrInputDocument();
                    sourceBibSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, bibIdentifier);
                    Map<String, List<String>> itemMap= new HashMap<>();
                    itemMap.put(AtomicUpdateConstants.REMOVE, itemsIds);
                    sourceBibSolrInputDocument.setField(ITEM_IDENTIFIER, itemMap);
                    solrInputDocumentListFinal.add(sourceBibSolrInputDocument);
                }
            }

            //Get the holdings id from item being transfered and remove the source bib id
            Object holdingsObject = itemSolrDocument.getFieldValue(HOLDINGS_IDENTIFIER);
            List<String> holdingsIds = new ArrayList<>();
            if (holdingsObject instanceof String) {
                String holdingsIdentifier = (String) holdingsObject;
                holdingsIds.add(holdingsIdentifier);
                SolrInputDocument sourceHoldingsSolrInputDocument = new SolrInputDocument();
                sourceHoldingsSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, holdingsIdentifier);
                Map<String, List<String>> itemMap= new HashMap<>();
                itemMap.put(AtomicUpdateConstants.REMOVE, itemsIds);
                sourceHoldingsSolrInputDocument.setField(ITEM_IDENTIFIER, itemMap);
                solrInputDocumentListFinal.add(sourceHoldingsSolrInputDocument);
            } else if (holdingsObject instanceof List) {
                List<String> bibIdentifierList = (List<String>) holdingsObject;
                for(String bibIdentifier : bibIdentifierList){
                    SolrInputDocument sourceBibSolrInputDocument = new SolrInputDocument();
                    sourceBibSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, bibIdentifier);
                    Map<String, List<String>> itemMap= new HashMap<>();
                    itemMap.put(AtomicUpdateConstants.REMOVE, itemsIds);
                    sourceBibSolrInputDocument.setField(ITEM_IDENTIFIER, itemMap);
                    solrInputDocumentListFinal.add(sourceBibSolrInputDocument);
                }
            }
        }
    }

    protected void addBibAndHoldingsInfoToItem(SolrInputDocument solrInputDocument, SolrInputDocument holdingsSolrInputDocument) {
        super.addBibInfoForHoldingsOrItems(solrInputDocument, holdingsSolrInputDocument);
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_SEARCH, holdingsSolrInputDocument.getFieldValue(CALL_NUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_LOCATION_SEARCH, holdingsSolrInputDocument.getFieldValue(LOCATION_LEVEL_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_DISPLAY, holdingsSolrInputDocument.getFieldValue(CALL_NUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_LOCATION_DISPLAY, holdingsSolrInputDocument.getFieldValue(LOCATION_LEVEL_DISPLAY));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_SEARCH,holdingsSolrInputDocument.getFieldValue(COPY_NUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_DISPLAY,holdingsSolrInputDocument.getFieldValue(COPY_NUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_SEARCH, holdingsSolrInputDocument.getFieldValue(CALL_NUMBER_PREFIX_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_DISPLAY, holdingsSolrInputDocument.getFieldValue(CALL_NUMBER_PREFIX_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_SEARCH, holdingsSolrInputDocument.getFieldValue(SHELVING_SCHEME_CODE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_DISPLAY, holdingsSolrInputDocument.getFieldValue(SHELVING_SCHEME_CODE_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_SEARCH, holdingsSolrInputDocument.getFieldValue(SHELVING_SCHEME_VALUE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_DISPLAY, holdingsSolrInputDocument.getFieldValue(SHELVING_SCHEME_VALUE_DISPLAY));
    }


    protected void addBibInfoForHoldingsOrItems(SolrInputDocument solrInputDocument, SolrDocument sourceDocument) {
        super.addBibInfoForHoldingsOrItems(solrInputDocument, sourceDocument);
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_SEARCH, sourceDocument.getFieldValue(CALL_NUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_LOCATION_SEARCH, sourceDocument.getFieldValue(LOCATION_LEVEL_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_DISPLAY, sourceDocument.getFieldValue(CALL_NUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_LOCATION_DISPLAY, sourceDocument.getFieldValue(LOCATION_LEVEL_DISPLAY));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_SEARCH,sourceDocument.getFieldValue(COPY_NUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_DISPLAY,sourceDocument.getFieldValue(COPY_NUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_SEARCH, sourceDocument.getFieldValue(CALL_NUMBER_PREFIX_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_DISPLAY, sourceDocument.getFieldValue(CALL_NUMBER_PREFIX_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_SEARCH, sourceDocument.getFieldValue(SHELVING_SCHEME_CODE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_DISPLAY, sourceDocument.getFieldValue(SHELVING_SCHEME_CODE_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_SEARCH, sourceDocument.getFieldValue(SHELVING_SCHEME_VALUE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_DISPLAY, sourceDocument.getFieldValue(SHELVING_SCHEME_VALUE_DISPLAY));
    }

    protected void addBibInfoForHoldingsOrItems1(SolrInputDocument solrInputDocument, SolrDocument sourceDocument) {
        super.addBibInfoForHoldingsOrItems(solrInputDocument, sourceDocument);
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_SEARCH, sourceDocument.getFieldValue(HOLDINGS_CALLNUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_LOCATION_SEARCH, sourceDocument.getFieldValue(HOLDINGS_LOCATION_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_DISPLAY, sourceDocument.getFieldValue(HOLDINGS_CALLNUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_LOCATION_DISPLAY, sourceDocument.getFieldValue(HOLDINGS_LOCATION_DISPLAY));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_SEARCH, sourceDocument.getFieldValue(HOLDINGS_COPYNUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_DISPLAY,sourceDocument.getFieldValue(HOLDINGS_COPYNUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_SEARCH, sourceDocument.getFieldValue(HOLDINGS_CALLNUMBER_PREFIX_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_DISPLAY, sourceDocument.getFieldValue(HOLDINGS_CALLNUMBER_PREFIX_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_SEARCH, sourceDocument.getFieldValue(HOLDINGS_SHELVING_SCHEME_CODE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_DISPLAY, sourceDocument.getFieldValue(HOLDINGS_SHELVING_SCHEME_CODE_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_SEARCH, sourceDocument.getFieldValue(HOLDINGS_SHELVING_SCHEME_VALUE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_DISPLAY, sourceDocument.getFieldValue(HOLDINGS_SHELVING_SCHEME_VALUE_DISPLAY));
    }


    public String getAllTextValueForItem(org.kuali.ole.docstore.common.document.content.instance.Item item) {
        StringBuffer sb = new StringBuffer();
        String itemIdentifier = item.getItemIdentifier();
        String copyNumber = item.getCopyNumber();
        String enumeration = item.getEnumeration();
        String analytic = item.getAnalytic();
        String barcodeARSL = item.getBarcodeARSL();
        String chronology = item.getChronology();
        String checkinNote = item.getCheckinNote();
        String claimsReturnedFlagCreateDate = item.getClaimsReturnedFlagCreateDate();
        String claimsReturnedNote = item.getClaimsReturnedNote();
        String copyNumberLabel = item.getCopyNumberLabel();
        String currentBorrower = item.getCurrentBorrower();
        String damagedItemNote = item.getDamagedItemNote();
        String dueDateTime = item.getDueDateTime();
        String fund = item.getFund();
        String itemStatusEffectiveDate = item.getItemStatusEffectiveDate();
        String missingPieceEffectiveDate = item.getMissingPieceEffectiveDate();
        String missingPieceFlagNote = item.getMissingPieceFlagNote();
        String missingPiecesCount = item.getMissingPiecesCount();
        String numberOfPieces = item.getNumberOfPieces();
        String price = item.getPrice();
        String proxyBorrower = item.getProxyBorrower();
        String purchaseOrderLineItemIdentifier = item.getPurchaseOrderLineItemIdentifier();
        String resourceIdentifier = item.getResourceIdentifier();
        String vendorLineItemIdentifier = item.getVendorLineItemIdentifier();
        String volumeNumber = item.getVolumeNumber();
        String volumeNumberLabel = item.getVolumeNumberLabel();

        appendData(sb, itemIdentifier);
        appendData(sb, copyNumber);
        appendData(sb, enumeration);
        appendData(sb, analytic);
        appendData(sb, chronology);
        appendData(sb, barcodeARSL);
        appendData(sb, checkinNote);
        appendData(sb, claimsReturnedFlagCreateDate);
        appendData(sb, claimsReturnedNote);
        appendData(sb, copyNumberLabel);
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
        appendData(sb, resourceIdentifier);
        appendData(sb, vendorLineItemIdentifier);
        appendData(sb, volumeNumber);
        appendData(sb, volumeNumberLabel);

        boolean staffOnlyFlag = item.isStaffOnlyFlag();
        boolean claimsReturnedFlag = item.isClaimsReturnedFlag();
        boolean fastAddFlag = item.isFastAddFlag();
        boolean itemDamagedStatus = item.isItemDamagedStatus();
        boolean missingPieceFlag = item.isMissingPieceFlag();

        appendData(sb, String.valueOf(staffOnlyFlag));
        appendData(sb, String.valueOf(claimsReturnedFlag));
        appendData(sb, String.valueOf(fastAddFlag));
        appendData(sb, String.valueOf(itemDamagedStatus));
        appendData(sb, String.valueOf(missingPieceFlag));

        AccessInformation accessInformation = item.getAccessInformation();
        if (accessInformation != null) {
            String barcode = accessInformation.getBarcode();
            appendData(sb, barcode);
            if (accessInformation.getUri() != null) {
                String resolvable = accessInformation.getUri().getResolvable();
                String value = accessInformation.getUri().getValue();
                appendData(sb, resolvable);
                appendData(sb, value);
            }
        }

        CallNumber callNumber = item.getCallNumber();
        if (callNumber !=null && StringUtils.isNotEmpty(callNumber.getNumber())) {
            String number = callNumber.getNumber();
            String prefix = callNumber.getPrefix();
            String classificationPart = callNumber.getClassificationPart();
            String itemPart = callNumber.getItemPart();
            String type = callNumber.getType();
            if (callNumber.getShelvingScheme() != null) {
                String shelvingSchemeCodeValue = callNumber.getShelvingScheme().getCodeValue();
                String shelvingSchemeFullValue = callNumber.getShelvingScheme().getFullValue();

                appendData(sb, shelvingSchemeCodeValue);
                appendData(sb, shelvingSchemeFullValue);
            }
            if (callNumber.getShelvingOrder() != null) {
                String shelvingOrderCodeValue = callNumber.getShelvingOrder().getCodeValue();
                String shelvingOrderFullValue = callNumber.getShelvingOrder().getFullValue();
                appendData(sb, shelvingOrderCodeValue);
                appendData(sb, shelvingOrderFullValue);
            }

            appendData(sb, number);
            appendData(sb, prefix);
            appendData(sb, classificationPart);
            appendData(sb, itemPart);
            appendData(sb, type);
        }


        for (DonorInfo donorInfo : item.getDonorInfo()) {
            if (donorInfo != null) {
                String donorCode = donorInfo.getDonorCode();
                String donorNote = donorInfo.getDonorNote();
                String donorPublicDisplay = donorInfo.getDonorPublicDisplay();
                appendData(sb, donorCode);
                appendData(sb, donorNote);
                appendData(sb, donorPublicDisplay);
            }
        }

        for (FormerIdentifier formerIdentifier : item.getFormerIdentifier()) {
            if (formerIdentifier.getIdentifierType() != null) {
                String identifierType = formerIdentifier.getIdentifierType();
                String identifierValue = formerIdentifier.getIdentifier().getIdentifierValue();
                String source = formerIdentifier.getIdentifier().getSource();
                appendData(sb, identifierType);
                appendData(sb, identifierValue);
                appendData(sb, source);
            }
        }

        HighDensityStorage highDensityStorage = item.getHighDensityStorage();
        if (highDensityStorage != null) {
            String module = highDensityStorage.getModule();
            String row = highDensityStorage.getRow();
            String shelf = highDensityStorage.getShelf();
            String tray = highDensityStorage.getTray();
            appendData(sb, module);
            appendData(sb, row);
            appendData(sb, shelf);
            appendData(sb, tray);
        }

        ItemStatus itemStatus = item.getItemStatus();
        if(itemStatus != null) {
            String itemStatusCodeValue = itemStatus.getCodeValue();
            String itemStatusFullValue = itemStatus.getFullValue();
            appendData(sb, itemStatusCodeValue);
            appendData(sb, itemStatusFullValue);
        }

        ItemType itemType = item.getItemType();
        if(itemType != null) {
            String itemTypeCodeValue = itemType.getCodeValue();
            String itemTypeFullValue = itemType.getFullValue();
            if (itemType.getTypeOrSource() != null) {
                String itemTypeText = itemType.getTypeOrSource().getText();
                String itemTypePointer = itemType.getTypeOrSource().getPointer();
                appendData(sb, itemTypeText);
                appendData(sb, itemTypePointer);
            }
            appendData(sb, itemTypeCodeValue);
            appendData(sb, itemTypeFullValue);
        }

        for (Note note : item.getNote()) {
            String itemNoteValue = note.getValue();
            String itemNoteType = note.getType();
            appendData(sb, itemNoteValue);
            appendData(sb, itemNoteType);
        }

        NumberOfCirculations numberOfCirculations = item.getNumberOfCirculations();
        if(numberOfCirculations != null) {
            for (CheckInLocation checkInLocation : numberOfCirculations.getCheckInLocation()) {

                if (checkInLocation != null) {
                    if (checkInLocation.getCount() != null) {
                        String checkInLocationCount = checkInLocation.getCount().toString();
                        appendData(sb, checkInLocationCount);
                    }
                    if (checkInLocation.getInHouseCount() != null) {
                        String checkInLocationInHouseCount = checkInLocation.getInHouseCount().toString();
                        appendData(sb, checkInLocationInHouseCount);
                    }
                    String checkInLocationName = checkInLocation.getName();
                    appendData(sb, checkInLocationName);
                }
            }
        }

        for (StatisticalSearchingCode statisticalSearchingCode : item.getStatisticalSearchingCode()) {
            if (statisticalSearchingCode != null) {
                String codeValue = statisticalSearchingCode.getCodeValue();
                appendData(sb, codeValue);
                String fullValue = statisticalSearchingCode.getFullValue();
                appendData(sb, fullValue);
            }
        }

        ItemType temporaryItemType = item.getTemporaryItemType();
        if (temporaryItemType != null) {
            String temporaryItemTypeCodeValue = temporaryItemType.getCodeValue();
            String temporaryItemTypeFullValue = temporaryItemType.getFullValue();
            appendData(sb, temporaryItemTypeCodeValue);
            appendData(sb, temporaryItemTypeFullValue);
        }
        buildLocationNameAndLocationLevel(item.getLocation(), sb, sb);
        return sb.toString();

    }

    private void addItemDetailsToBib(List<SolrInputDocument> solrInputDocuments, SolrInputDocument solrInputDocument, Object bibs, List<String> bibIds) {
        if (bibs != null && bibs instanceof List) {
            for (String bibId : bibIds) {
                SolrDocument solrBibDocument;
                try {
                    solrBibDocument = getSolrDocumentByUUID(bibId);
                } catch (Exception e){
                    BibMarcIndexer bibMarcIndexer = new BibMarcIndexer();
                    DocstoreRDBMSStorageService rdbmsStorageService = new DocstoreRDBMSStorageService();
                    bibMarcIndexer.createTree(rdbmsStorageService.retrieveBibTree(bibId));
                    solrBibDocument = getSolrDocumentByUUID(bibId);
                }
                SolrInputDocument existingInputDocumnet = getExistingSolrDoc(solrInputDocuments, bibId);
                if(existingInputDocumnet != null){
                    existingInputDocumnet.setField(ITEM_IDENTIFIER,solrInputDocument.getFieldValue(ITEM_IDENTIFIER));
                    existingInputDocumnet.setField(ITEM_BARCODE_SEARCH,solrInputDocument.getFieldValue(ITEM_BARCODE_SEARCH));
                }else{
                    addDetails(solrInputDocument, solrBibDocument, ITEM_BARCODE_SEARCH);
                    addDetails(solrInputDocument, solrBibDocument, ITEM_IDENTIFIER);
                    solrInputDocuments.add(buildSolrInputDocFromSolrDoc(solrBibDocument));
                }
            }
        } else {
            String bibId = (String) bibs;
            SolrDocument solrBibDocument;
            try {
                solrBibDocument = getSolrDocumentByUUID(bibId);
            } catch (Exception e){
                BibMarcIndexer bibMarcIndexer = new BibMarcIndexer();
                DocstoreRDBMSStorageService rdbmsStorageService = new DocstoreRDBMSStorageService();
                bibMarcIndexer.createTree(rdbmsStorageService.retrieveBibTree(bibId));
                solrBibDocument = getSolrDocumentByUUID(bibId);
            }
            SolrInputDocument existingInputDocumnet = getExistingSolrDoc(solrInputDocuments, bibId);
            if(existingInputDocumnet != null){
                existingInputDocumnet.addField(ITEM_IDENTIFIER,solrInputDocument.getFieldValue(ITEM_IDENTIFIER));
                existingInputDocumnet.addField(ITEM_BARCODE_SEARCH,solrInputDocument.getFieldValue(ITEM_BARCODE_SEARCH));
            }else{
                solrBibDocument.addField(ITEM_IDENTIFIER,solrInputDocument.getFieldValue(ITEM_IDENTIFIER));
                solrBibDocument.addField(ITEM_BARCODE_SEARCH,solrInputDocument.getFieldValue(ITEM_BARCODE_SEARCH));
                solrInputDocuments.add(buildSolrInputDocFromSolrDoc(solrBibDocument));
            }
        }
    }

    private SolrInputDocument getExistingSolrDoc(List<SolrInputDocument> solrInputDocuments, String bibId) {
        SolrInputDocument existingInputDocumnet = null;
        for(SolrInputDocument solrInputDocument1 : solrInputDocuments){
            String doctype = (String) solrInputDocument1.getField(DOC_TYPE).getFirstValue();
            if( DocType.BIB.getCode().equalsIgnoreCase(doctype)){
                String id= (String) solrInputDocument1.getField(ID).getFirstValue() ;
                if(bibId.equalsIgnoreCase(id)){
                    existingInputDocumnet = solrInputDocument1;
                    break;
                }
            }
        }
        return existingInputDocumnet;
    }

    private void addDetails(SolrInputDocument solrInputDocument, SolrDocument solrBibDocument, String docfiled) {
        Collection<Object> bibValues = solrBibDocument.getFieldValues(docfiled);
        Object holdigsValue = solrInputDocument.getFieldValue(docfiled);
        if (bibValues != null) {
            List<Object> valueObject = new ArrayList();
            valueObject.addAll(bibValues);
            for (Object bibValue : valueObject) {
                if (holdigsValue != null && !((String) bibValue).equalsIgnoreCase(((String) holdigsValue))) {
                    solrBibDocument.addField(docfiled, solrInputDocument.getFieldValue(docfiled));
                }
            }
        } else {
            solrBibDocument.addField(docfiled, solrInputDocument.getFieldValue(docfiled));
        }

    }
}



