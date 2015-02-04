package org.kuali.ole.deliver.lookup;

import org.jfree.util.Log;
import org.kuali.ole.deliver.bo.OLEFlaggedItems;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.web.form.LookupForm;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 1/13/14
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEFlaggedItemsLookupableImpl extends LookupableImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEFlaggedItemsLookupableImpl.class);
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        List<OLEFlaggedItems> oleFlaggedItemses = new ArrayList<>();
        SearchResponse searchResponse = new SearchResponse();
        SearchParams searchParams = new SearchParams();
        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
        // making field ready
        String barcode = StringUtils.isNotEmpty(searchCriteria.get("barcode")) ? searchCriteria.get("barcode") : "";
        String flagType = StringUtils.isNotEmpty(searchCriteria.get("flagType")) ? searchCriteria.get("flagType") : "";
        boolean isClaimsReturned = false;
        boolean isDamaged = false;
        boolean isMissingPieceEnabled = false;
        boolean defaultSearch = false;
        int maxRowSize=10000;
        if (StringUtils.isEmpty(barcode) && StringUtils.isEmpty(flagType)) {
            defaultSearch = true;
        }
        try {
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "", ""), ""));
            SearchResponse maxRowCountResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            maxRowSize=maxRowCountResponse.getTotalRecordCount();
        } catch (Exception e) {
            LOG.error("Exception occurred in retrieving the row doc count  "+e);
        }
        if (defaultSearch) {
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "ClaimsReturnedFlag_search", "true"), "OR"));
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "MissingPieceFlag_search", "true"), "OR"));
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "ItemDamagedStatus_search", "true"), "OR"));
        }
        if (StringUtils.isNotEmpty(barcode)) {
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), Item.ITEM_BARCODE, barcode), "AND"));
        }
        if (StringUtils.isNotEmpty(flagType)) {
            Boolean val = Boolean.valueOf("true");
            if (flagType.equalsIgnoreCase(OLEConstants.FLAG_TYP_ITM_DAMAGED)) {
                isDamaged = true;
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "ItemDamagedStatus_search", "true"), "AND"));
            }
            if (flagType.equalsIgnoreCase(OLEConstants.FLAG_TYP_ITM_MISSING)) {
                isMissingPieceEnabled = true;
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "MissingPieceFlag_search", "true"), "AND"));
            }
            if (flagType.equalsIgnoreCase(OLEConstants.FLAG_TYP_ITM_CLAIMS_RETURNED)) {
                isClaimsReturned = true;
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "ClaimsReturnedFlag_search", "true"), "AND"));
            }
        }
        searchParams.setPageSize(maxRowSize);
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "itembarcode"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "bibIdentifier"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "id"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "CallNumber_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CopyNumber_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceFlag_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ClaimsReturnedFlag_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemDamagedStatus_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceFlagNote_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "DamagedItemNote_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ClaimsReturnedNote_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "CallNumber_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "Location_display"));
       // LOG.info("Flagged Item Records Size isNull"+ (searchResponse.getSearchResults()==null?"true":"false"));

        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            if (LOG.isDebugEnabled()){
                if(searchResponse.getSearchResults()!=null && searchResponse.getSearchResults().size()>0){
                    LOG.debug("Flagged Item Records Size"+searchResponse.getSearchResults().size());
                } else {
                    LOG.debug("Flagged Item Records Size is Null");
                }
            }
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                OLEFlaggedItems flaggedItems = new OLEFlaggedItems();
                boolean isItemLevelCallNumberExist = true;
                boolean isItemLevelLocationExist = true;
                LOG.debug("DOCSTORE RECORD--------->");
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    String fieldName = searchResultField.getFieldName();
                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                    if (LOG.isDebugEnabled()){
                        LOG.debug("Field Name"+fieldName+"  :: " +fieldValue );
                    }
                    if (fieldName.equalsIgnoreCase("itemBarcode") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setBarcode(fieldValue);
                    } else if (fieldName.equalsIgnoreCase("MissingPieceFlag_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        if (Boolean.valueOf(fieldValue) || fieldValue.equalsIgnoreCase("true")) {
                            flaggedItems.setMissingPiece(true);
                        }
                    } else if (fieldName.equalsIgnoreCase("ClaimsReturnedFlag_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        if (Boolean.valueOf(fieldValue) || fieldValue.equalsIgnoreCase("true")) {
                            flaggedItems.setClaimsReturned(true);
                        }
                    } else if (fieldName.equalsIgnoreCase("ItemDamagedStatus_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        if (Boolean.valueOf(fieldValue) || fieldValue.equalsIgnoreCase("true")) {
                            flaggedItems.setDamaged(true);
                        }
                    } else if (fieldName.equalsIgnoreCase("title") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {
                        flaggedItems.setTitle(fieldValue);
                    } else if (fieldName.equalsIgnoreCase("CallNumber_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                        if (isItemLevelCallNumberExist) {
                            flaggedItems.setCallNumber(fieldValue);
                        }
                    } else if (fieldName.equalsIgnoreCase("CallNumber_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setCallNumber(fieldValue);
                        isItemLevelCallNumberExist = true;
                    } else if (fieldName.equalsIgnoreCase("CopyNumber_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setCopyNumber(fieldValue);
                    } else if (fieldName.equalsIgnoreCase("Location_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                        if (isItemLevelLocationExist) {
                            flaggedItems.setLocation(fieldValue);
                        }
                    } else if (fieldName.equalsIgnoreCase("Location_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setLocation(fieldValue);
                        isItemLevelLocationExist = true;
                    } else if (fieldName.equalsIgnoreCase("bibIdentifier") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setBibUuid(fieldValue);
                    } else if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setItemUuid(fieldValue);
                        flaggedItems.setInstanceUuid(fieldValue);
                    } else if (fieldName.equalsIgnoreCase("MissingPieceFlagNote_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setMissingPieceNote(fieldValue);
                    } else if (fieldName.equalsIgnoreCase("DamagedItemNote_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setDamagedNote(fieldValue);
                    } else if (fieldName.equalsIgnoreCase("ClaimsReturnedNote_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        flaggedItems.setClaimsReturnedNote(fieldValue);
                    }

                }
                if (LOG.isDebugEnabled()){
                    LOG.debug(flaggedItems.toString());
                }
                oleFlaggedItemses.add(flaggedItems);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buildResultForDisplay(oleFlaggedItemses, isClaimsReturned, isDamaged, isMissingPieceEnabled, "");
    }

    public List<OLEFlaggedItems> buildResultForDisplay(List<OLEFlaggedItems> docResults, boolean isClaimsReturned, boolean isDamaged, boolean isMissingPieceEnabled, String flagNote) {
        List<OLEFlaggedItems> result = new ArrayList<OLEFlaggedItems>();
        if (LOG.isDebugEnabled()){
            LOG.debug("Flagged Item Search : Before building the docItems size"+docResults.size());
        }
        boolean defaultSearch = false;
        if (!(isClaimsReturned || isDamaged || isMissingPieceEnabled)) {
            defaultSearch = true;
        }
        for (OLEFlaggedItems docResult : docResults) {
            if (defaultSearch) {
                if (docResult.isClaimsReturned()) {
                    OLEFlaggedItems flagItem = new OLEFlaggedItems();
                    flagItem.setBarcode(docResult.getBarcode());
                    flagItem.setTitle(docResult.getTitle());
                    flagItem.setCopyNumber(docResult.getCopyNumber());
                    flagItem.setCallNumber(docResult.getCallNumber());
                    flagItem.setFlagType("Claims Returned");
                    flagItem.setFlagNote(docResult.getClaimsReturnedNote());
                    flagItem.setLocation(docResult.getLocation());
                    flagItem.setBibUuid(docResult.getBibUuid());
                    flagItem.setInstanceUuid(docResult.getInstanceUuid());
                    flagItem.setItemUuid(docResult.getItemUuid());
                    result.add(flagItem);
                }
                if (docResult.isDamaged()) {
                    OLEFlaggedItems flagItem = new OLEFlaggedItems();
                    flagItem.setBarcode(docResult.getBarcode());
                    flagItem.setTitle(docResult.getTitle());
                    flagItem.setCopyNumber(docResult.getCopyNumber());
                    flagItem.setCallNumber(docResult.getCallNumber());
                    flagItem.setFlagType("Damaged");
                    flagItem.setFlagNote(docResult.getDamagedNote());
                    flagItem.setLocation(docResult.getLocation());
                    flagItem.setBibUuid(docResult.getBibUuid());
                    flagItem.setInstanceUuid(docResult.getInstanceUuid());
                    flagItem.setItemUuid(docResult.getItemUuid());
                    result.add(flagItem);
                }
                if (docResult.isMissingPiece()) {
                    OLEFlaggedItems flagItem = new OLEFlaggedItems();
                    flagItem.setBarcode(docResult.getBarcode());
                    flagItem.setTitle(docResult.getTitle());
                    flagItem.setCopyNumber(docResult.getCopyNumber());
                    flagItem.setCallNumber(docResult.getCallNumber());
                    flagItem.setFlagType("Missing Piece");
                    flagItem.setFlagNote(docResult.getMissingPieceNote());
                    flagItem.setLocation(docResult.getLocation());
                    flagItem.setBibUuid(docResult.getBibUuid());
                    flagItem.setInstanceUuid(docResult.getInstanceUuid());
                    flagItem.setItemUuid(docResult.getItemUuid());
                    result.add(flagItem);
                }
            }
            if (docResult.isClaimsReturned() && isClaimsReturned && !defaultSearch) {
                OLEFlaggedItems flagItem = new OLEFlaggedItems();
                flagItem.setBarcode(docResult.getBarcode());
                flagItem.setTitle(docResult.getTitle());
                flagItem.setCopyNumber(docResult.getCopyNumber());
                flagItem.setCallNumber(docResult.getCallNumber());
                flagItem.setFlagType("Claims Returned");
                flagItem.setFlagNote(docResult.getClaimsReturnedNote());
                flagItem.setLocation(docResult.getLocation());
                flagItem.setBibUuid(docResult.getBibUuid());
                flagItem.setInstanceUuid(docResult.getInstanceUuid());
                flagItem.setItemUuid(docResult.getItemUuid());
                result.add(flagItem);
            }
            if (docResult.isDamaged() && isDamaged && !defaultSearch) {
                OLEFlaggedItems flagItem = new OLEFlaggedItems();
                flagItem.setBarcode(docResult.getBarcode());
                flagItem.setTitle(docResult.getTitle());
                flagItem.setCopyNumber(docResult.getCopyNumber());
                flagItem.setCallNumber(docResult.getCallNumber());
                flagItem.setFlagType("Damaged");
                flagItem.setFlagNote(docResult.getDamagedNote());
                flagItem.setLocation(docResult.getLocation());
                flagItem.setBibUuid(docResult.getBibUuid());
                flagItem.setInstanceUuid(docResult.getInstanceUuid());
                flagItem.setItemUuid(docResult.getItemUuid());
                result.add(flagItem);
            }
            if (docResult.isMissingPiece() && isMissingPieceEnabled && !defaultSearch) {
                OLEFlaggedItems flagItem = new OLEFlaggedItems();
                flagItem.setBarcode(docResult.getBarcode());
                flagItem.setTitle(docResult.getTitle());
                flagItem.setCopyNumber(docResult.getCopyNumber());
                flagItem.setCallNumber(docResult.getCallNumber());
                flagItem.setFlagType("Missing Piece");
                flagItem.setFlagNote(docResult.getMissingPieceNote());
                flagItem.setLocation(docResult.getLocation());
                flagItem.setBibUuid(docResult.getBibUuid());
                flagItem.setInstanceUuid(docResult.getInstanceUuid());
                flagItem.setItemUuid(docResult.getItemUuid());
                result.add(flagItem);
            }
        }
        if (LOG.isDebugEnabled()){
            LOG.debug("Flagged Item Search : After building the docItems size"+result.size());
        }
        return result;
    }


}
