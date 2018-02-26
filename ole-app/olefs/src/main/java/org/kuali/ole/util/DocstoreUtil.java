package org.kuali.ole.util;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 1/9/14
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreUtil {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocstoreUtil.class);
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");
        }
        return docstoreClientLocator;
    }

    public boolean isItemAvailableInDocStore(OleDeliverRequestBo oleDeliverRequestBo) {
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        LOG.info("Inside isItemAvailableInDocStore");
        boolean available = false;
        Map<String, String> itemMap = new HashMap<String, String>();
        LocationValuesBuilder locationValuesBuilder = new LocationValuesBuilder();
        String holdingsId = "";
        String bibTitle="";
        String bibAuthor="";
        try {
            try {
                org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                SearchResponse searchResponse = null;
                if(StringUtils.isNotBlank(oleDeliverRequestBo.getItemId())){
                    search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleDeliverRequestBo.getItemId()), ""));
                }else{
                    if(oleDeliverRequestBo.getItemUuid().contains("wio")){
                        search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ID, oleDeliverRequestBo.getItemUuid()), ""));
                    }else{
                        search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ID, "wio-"+oleDeliverRequestBo.getItemUuid()), ""));
                    }
                }
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "ItemBarcode_display"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "holdingsIdentifier"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Title_display"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Author_display"));
                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        String fieldName = searchResultField.getFieldName();
                        String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                        if (fieldName.equalsIgnoreCase("holdingsIdentifier") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                            holdingsId = fieldValue;
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Title_display") &&!fieldValue.isEmpty()) {
                            bibTitle = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Author_display") &&!fieldValue.isEmpty()) {
                            bibAuthor = searchResultField.getFieldValue();
                        } else  if (searchResultField.getFieldName().equalsIgnoreCase("id") &&!fieldValue.isEmpty()){
                            oleDeliverRequestBo.setItemUuid(fieldValue);
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display") &&!fieldValue.isEmpty()) {
                            oleDeliverRequestBo.setItemId(fieldValue);
                        }
                    }
                }
            } catch (Exception ex) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Item Exists");
                LOG.error(OLEConstants.ITEM_EXIST + ex);
            }
            OleItemSearch itemSearchList = getOleItemSearchList(oleDeliverRequestBo.getItemUuid());
            if (asrHelperService.isAnASRItem(itemSearchList.getShelvingLocation())) {
                oleDeliverRequestBo.setAsrFlag(true);
            } else {
                oleDeliverRequestBo.setAsrFlag(false);
            }
            if (itemSearchList != null) {
                oleDeliverRequestBo.setTitle(itemSearchList.getTitle());
                oleDeliverRequestBo.setAuthor(itemSearchList.getAuthor());
                oleDeliverRequestBo.setCallNumber(itemSearchList.getCallNumber());
                oleDeliverRequestBo.setItemType(itemSearchList.getItemType());
                oleDeliverRequestBo.setItemLocation(itemSearchList.getShelvingLocation());
            }
            if(StringUtils.isNotEmpty(bibTitle)){
                oleDeliverRequestBo.setTitle(bibTitle);
            }
            if(StringUtils.isNotEmpty(bibAuthor)){
                oleDeliverRequestBo.setAuthor(bibAuthor);
            }
            LoanProcessor loanProcessor = new LoanProcessor();
            String itemXml = loanProcessor.getItemXML(oleDeliverRequestBo.getItemUuid());
            Item oleItem = loanProcessor.getItemPojo(itemXml);
            oleDeliverRequestBo.setOleItem(oleItem);
            oleDeliverRequestBo.setCopyNumber(oleItem.getCopyNumber());
            oleDeliverRequestBo.setEnumeration(oleItem.getEnumeration());
            oleDeliverRequestBo.setChronology(oleItem.getChronology());
            oleDeliverRequestBo.setItemStatus(oleItem.getItemStatus().getCodeValue());
            oleDeliverRequestBo.setClaimsReturnedFlag(oleItem.isClaimsReturnedFlag());
            locationValuesBuilder.getLocation(oleItem, oleDeliverRequestBo, holdingsId);
            available = true;
        } catch (Exception e) {
            LOG.error(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC) + e);
        }
        return available;
    }

    public OleItemSearch getOleItemSearchList(String itemUuid) {
        OleItemSearch oleItemSearch = null;
        try {
            LoanProcessor loanProcessor = new LoanProcessor();
            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUuid);
            oleItemSearch = buildItemRecord(loanProcessor, item);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.info("Item does not Exist");
        }
        return oleItemSearch;
    }


    public OleItemSearch getOleItemSearchListFromLocalClient(String itemUuid) {
        OleItemSearch oleItemSearch = null;
        try {
            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreLocalClient().retrieveItem(itemUuid);
            LoanProcessor loanProcessor = new LoanProcessor();
            oleItemSearch = buildItemRecord(loanProcessor, item);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.info("Item does not Exist");
        }
        LoanProcessor loanProcessor = new LoanProcessor();
        return oleItemSearch;
    }

    private OleItemSearch buildItemRecord(LoanProcessor loanProcessor, org.kuali.ole.docstore.common.document.Item item) throws Exception {
        OleItemSearch oleItemSearch = new OleItemSearch();
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
        OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(item.getHolding().getContent());
        oleItemSearch.setTitle(item.getHolding().getBib().getTitle());
        oleItemSearch.setAuthor(item.getHolding().getBib().getAuthor());
        oleItemSearch.setVolumeNumber(itemContent.getVolumeNumber());
        oleItemSearch.setBibUUID(item.getHolding().getBib().getId());
        StringBuffer locationLevel = new StringBuffer("");
        String location = "";
        if (itemContent.getLocation().getLocationLevel() != null) {
            location = getLocation(itemContent.getLocation(), locationLevel);
            oleItemSearch.setShelvingLocation(location);
        } else {
            oleItemSearch.setShelvingLocation(getLocation(oleHoldings.getLocation(), locationLevel));
        }
           /* String callNumber;
            if(itemContent.getCallNumber()!=null && !StringUtils.isEmpty(itemContent.getCallNumber().getNumber())){
                callNumber = loanProcessor.getItemCallNumber(itemContent.getCallNumber());
            }else {
                callNumber = loanProcessor.getItemCallNumber(oleHoldings.getCallNumber());
            }*/
        String itemType;
        if (itemContent.getTemporaryItemType() != null && itemContent.getTemporaryItemType().getCodeValue() != null) {
            itemType = itemContent.getTemporaryItemType().getCodeValue();
        } else {
            itemType = itemContent.getItemType().getCodeValue();
        }
        oleItemSearch.setPublisher(item.getHolding().getBib().getPublisher());
        oleItemSearch.setCallNumber(loanProcessor.getItemCallNumber(itemContent.getCallNumber(), oleHoldings.getCallNumber()));
        oleItemSearch.setHoldingUUID(item.getHolding().getId());
        oleItemSearch.setInstanceUUID(item.getHolding().getId());
        oleItemSearch.setCopyNumber(itemContent.getCopyNumber());
        oleItemSearch.setItemBarCode(itemContent.getAccessInformation().getBarcode());
        oleItemSearch.setItemStatus(itemContent.getItemStatus().getFullValue());
        oleItemSearch.setItemType(itemType);
        oleItemSearch.setItemUUID(item.getId());
        oleItemSearch.setEnumeration(itemContent.getEnumeration());
        oleItemSearch.setChronology(itemContent.getChronology());

        return oleItemSearch;
    }


    public boolean isItemAvailableInDocStore(String itemBarcode) {
        LOG.info("Inside isItemAvailableInDocStore");
        boolean available = false;
        try {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
            SearchResponse searchResponse = null;
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, itemBarcode), ""));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.ID));
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() > 0) {
                available = true;
            }
        } catch (Exception ex) {

            LOG.error(OLEConstants.ITEM_EXIST + ex);
        }
        return available;
    }


    public void getSearchResultFields(SearchParams searchParams) {
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),OLEConstants.ID));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), org.kuali.ole.docstore.common.document.Item.ITEM_BARCODE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.HOLDINGS.getCode(), org.kuali.ole.docstore.common.document.Item.CALL_NUMBER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), org.kuali.ole.docstore.common.document.Item.ITEM_TYPE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),OLEConstants.TEMPITEMTYPE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), org.kuali.ole.docstore.common.document.Item.ITEM_STATUS));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), org.kuali.ole.docstore.common.document.Item.COPY_NUMBER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), Holdings.LOCATION_NAME));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.LOACTION_LEVEL_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), org.kuali.ole.docstore.common.document.Item.VOLUME_NUMBER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), org.kuali.ole.docstore.common.document.Item.CALL_NUMBER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), OLEConstants.ID));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), OLEConstants.ID));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.TITLE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.AUTHOR));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), OLEConstants.PUBLISHERDISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.CHRONOLOGY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.ENUMARATION));
    }

    public List<OleItemSearch> getSearchResults(SearchResponse searchResponse) {

        List<OleItemSearch> oleItemSearchList = new ArrayList<>();
        if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() > 0) {
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                OleItemSearch oleItemSearch = new OleItemSearch();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    String callNumber = "";

                    if (searchResultField.getFieldName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.Item.CALL_NUMBER) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode())) {
                        callNumber = searchResultField.getFieldValue();
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ID) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setItemUUID(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(Bib.TITLE) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode())) {
                        if (oleItemSearch.getTitle() == null) {
                            oleItemSearch.setTitle(searchResultField.getFieldValue());
                        }
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ID) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode())) {
                        oleItemSearch.setHoldingUUID(searchResultField.getFieldValue());
                        oleItemSearch.setInstanceUUID(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.Item.ITEM_BARCODE) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setItemBarCode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.Item.CALL_NUMBER) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setCallNumber(searchResultField.getFieldValue());
                    } else if (org.apache.commons.lang.StringUtils.isEmpty(oleItemSearch.getCallNumber())) {
                        oleItemSearch.setCallNumber(callNumber);
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.Item.COPY_NUMBER) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setCopyNumber(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase( OLEConstants.LOACTION_LEVEL_DISPLAY) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setShelvingLocation(searchResultField.getFieldValue());
                    }
                    else if (StringUtils.isEmpty(oleItemSearch.getShelvingLocation()) && searchResultField.getFieldName().equalsIgnoreCase(Holdings.LOCATION_NAME) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode())) {
                        oleItemSearch.setShelvingLocation(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.Item.ITEM_STATUS) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setItemStatus(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CHRONOLOGY) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setChronology(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ENUMARATION) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setEnumeration(searchResultField.getFieldValue());
                    }

                    if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.TEMPITEMTYPE) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setItemType(searchResultField.getFieldValue());
                    }
                    else if (StringUtils.isEmpty(oleItemSearch.getItemType()) && searchResultField.getFieldName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.Item.ITEM_TYPE) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setItemType(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.Item.VOLUME_NUMBER) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        oleItemSearch.setVolumeNumber(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(Bib.AUTHOR) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode())) {
                        if (oleItemSearch.getAuthor() == null) {
                            oleItemSearch.setAuthor(searchResultField.getFieldValue());
                        }
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.PUBLISHERDISPLAY) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode())) {
                        if (oleItemSearch.getPublisher() == null) {
                            oleItemSearch.setPublisher(searchResultField.getFieldValue());
                        }
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ID) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode())) {
                        if (oleItemSearch.getBibUUID() == null) {
                            oleItemSearch.setBibUUID(searchResultField.getFieldValue());
                        }

                    }
                }
                oleItemSearchList.add(oleItemSearch);
            }

        }
        return oleItemSearchList;
    }

    public String getLocation(Location location, StringBuffer locationLevel) {
        StringBuffer locationName = new StringBuffer("");
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

    public OleHoldings getOleHoldings(String instanceUUID) throws Exception {
        LOG.info("--Inside getOleHoldings---");
        Holdings holdings = new Holdings();
        holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(instanceUUID);
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        return oleHoldings;
    }

}
