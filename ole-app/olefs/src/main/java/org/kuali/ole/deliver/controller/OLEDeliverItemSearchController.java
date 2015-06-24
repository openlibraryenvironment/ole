package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.form.OLEDeliverItemSearchForm;
import org.kuali.ole.deliver.service.OLEDeliverItemSearchService;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.engine.service.index.solr.BibConstants;
import org.kuali.ole.docstore.engine.service.index.solr.ItemConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by chenchulakshmig on 1/12/15.
 */
@Controller
@RequestMapping(value = "/deliverItemSearchController")
public class OLEDeliverItemSearchController extends UifControllerBase {

    String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEDeliverItemSearchController.class);
    private DocstoreClientLocator docstoreClientLocator;
    private OLEDeliverItemSearchService oleDeliverItemSearchService;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public OLEDeliverItemSearchService getOleDeliverItemSearchService() {
        if (oleDeliverItemSearchService == null) {
            oleDeliverItemSearchService = GlobalResourceLoader.getService(OLEConstants.DELIVER_ITEM__SEARCH_SERVICE);
        }
        return oleDeliverItemSearchService;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEDeliverItemSearchForm();
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverItemSearchForm oleDeliverItemSearchForm = (OLEDeliverItemSearchForm) form;
        oleDeliverItemSearchForm.setSingleItemFlag(false);
        oleDeliverItemSearchForm.setOleBibSearchResultDisplayRowList(new ArrayList<OLEBibSearchResultDisplayRow>());
        oleDeliverItemSearchForm.setOleSingleItemResultDisplayRow(new OLESingleItemResultDisplayRow());
        try {
            String title = oleDeliverItemSearchForm.getTitle();
            String author = oleDeliverItemSearchForm.getAuthor();
            String publisher = oleDeliverItemSearchForm.getPublisher();
            String itemBarcode = oleDeliverItemSearchForm.getItemBarCode();
            String callNumber = oleDeliverItemSearchForm.getItemCallNumber();
            String itemUUID = oleDeliverItemSearchForm.getItemUUID();
            String itemType = oleDeliverItemSearchForm.getItemType();
            String itemLocation = oleDeliverItemSearchForm.getItemLocation();
            SearchParams searchParams = new SearchParams();
            if (StringUtils.isNotBlank(title) || StringUtils.isNotBlank(author) || StringUtils.isNotBlank(publisher) || StringUtils.isNotBlank(itemBarcode)
                    || StringUtils.isNotBlank(callNumber) || StringUtils.isNotBlank(itemUUID) || StringUtils.isNotBlank(itemType) || StringUtils.isNotBlank(itemLocation)) {
                if (StringUtils.isNotBlank(title)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), Bib.TITLE, title), OLEConstants.AND_SEARCH_SCOPE));
                }
                if (StringUtils.isNotBlank(author)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), Bib.AUTHOR, author), OLEConstants.AND_SEARCH_SCOPE));
                }
                if (StringUtils.isNotBlank(publisher)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), Bib.PUBLISHER, publisher), OLEConstants.AND_SEARCH_SCOPE));
                }
                if (StringUtils.isNotBlank(itemBarcode)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), Item.ITEM_BARCODE, itemBarcode), OLEConstants.AND_SEARCH_SCOPE));
                }
                if (StringUtils.isNotBlank(callNumber)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), Item.CALL_NUMBER, callNumber),OLEConstants.OR_SEARCH_SCOPE));
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), ItemConstants.HOLDINGS_CALLNUMBER_SEARCH, callNumber), OLEConstants.AND_SEARCH_SCOPE));
                }
                if (StringUtils.isNotBlank(itemUUID)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), Item.ITEMIDENTIFIER, itemUUID), OLEConstants.AND_SEARCH_SCOPE));
                }
                if (StringUtils.isNotBlank(itemType)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.AND_SEARCH_SCOPE, searchParams.buildSearchField(DocType.ITEM.getCode(), Item.ITEM_TYPE, itemType), OLEConstants.AND_SEARCH_SCOPE));
                }
                if (StringUtils.isNotBlank(itemLocation)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.PHRASE, searchParams.buildSearchField(DocType.ITEM.getCode(), ItemConstants.LOCATION_LEVEL_SEARCH, itemLocation), OLEConstants.OR_SEARCH_SCOPE));
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.PHRASE, searchParams.buildSearchField(DocType.ITEM.getCode(),ItemConstants.HOLDINGS_LOCATION_SEARCH, itemLocation), OLEConstants.OR_SEARCH_SCOPE));
                }
                searchParams.setPageSize(100000);
                getSearchResultFields(searchParams);
                SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                getSearchResults(searchResponse, oleDeliverItemSearchForm);
                if(oleDeliverItemSearchForm.getOleBibSearchResultDisplayRowList()!=null && oleDeliverItemSearchForm.getOleBibSearchResultDisplayRowList().size()>0){
                    oleDeliverItemSearchForm.setMultipleItemFlag(true);
                }
            } else {
                oleDeliverItemSearchForm.setSingleItemFlag(false);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITM_BLANK_SEARCH_ERROR_MSG);
                return getUIFModelAndView(oleDeliverItemSearchForm,"OLEDeliverItemSearchPage");
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
        return getUIFModelAndView(oleDeliverItemSearchForm);
    }

    private void getSearchResultFields(SearchParams searchParams) {
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.BIB_IDENTIFIER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.TITLE_SORT));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Bib.AUTHOR));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Bib.PUBLICATIONPLACE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Bib.PUBLISHER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.HOLDINGS_LOCATION_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.LOCATION_LEVEL_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.CALL_NUMBER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.ITEM_STATUS));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.ITEM_TYPE_FULL_VALUE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.HOLDINGS_IDENTIFIER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.HOLDINGS_CALLNUMBER_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.ID));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.ITEM_BARCODE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.ENUMERATION_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.CHRONOLOGY_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.ITEM_STATUS_EFFECTIVE_DATE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.DUE_DATE_TIME));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.ORG_DUE_DATE_TIME));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.NUMBER_OF_PIECES_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.PUBLICATIONDATE_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.CURRENT_BORROWER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.PROXY_BORROWER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), "mdf_035a"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.ISBN_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.ITEMNOTE_VALUE_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.ITEMNOTE_TYPE_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.MISSING_PIECE_FLAG_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.MISSING_PIECE_COUNT_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.MISSING_PIECE_FLAG_NOTE_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.CLMS_RET_FLAG));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.CLMS_RET_NOTE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.CLMS_RET_FLAG_CRE_DATE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.ITEM_DAMAGED_FLAG_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.DAMAGED_ITEM_NOTE_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.CREATED_BY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.DATE_ENTERED));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.UPDATED_BY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.DATE_UPDATED));
        buildSortCondition(searchParams);
    }

    private void buildSortCondition(SearchParams searchParams) {
        SortCondition sortCondition = new SortCondition();
        sortCondition.setSortField(BibConstants.TITLE_SORT);
        sortCondition.setSortOrder("asc");
        searchParams.getSortConditions().add(sortCondition);
    }

    private void getSearchResults(SearchResponse searchResponse, OLEDeliverItemSearchForm oleDeliverItemSearchForm) {
        Map<String, OLEBibSearchResultDisplayRow> bibSearchResultDisplayRowMap = new TreeMap<>();
        Map<String, OLEHoldingsSearchResultDisplayRow> holdingsSearchResultDisplayRowMap = new HashMap<>();
        List<OLEBibSearchResultDisplayRow> bibSearchResultDisplayRowList = new ArrayList<>();
        OLESingleItemResultDisplayRow singleItemResultDisplayRow = new OLESingleItemResultDisplayRow();
        if (searchResponse != null && CollectionUtils.isNotEmpty(searchResponse.getSearchResults())) {
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                if (CollectionUtils.isNotEmpty(searchResult.getSearchResultFields())) {
                    OLEBibSearchResultDisplayRow bibSearchResultDisplayRow = new OLEBibSearchResultDisplayRow();
                    OLEHoldingsSearchResultDisplayRow holdingsSearchResultDisplayRow = new OLEHoldingsSearchResultDisplayRow();
                    singleItemResultDisplayRow = new OLESingleItemResultDisplayRow();
                    OLEItemSearchResultDisplayRow itemSearchResultDisplayRow = new OLEItemSearchResultDisplayRow();
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if (searchResultField.getFieldName().equalsIgnoreCase(Bib.BIBIDENTIFIER)) {
                            bibSearchResultDisplayRow.setId(searchResultField.getFieldValue());
                            holdingsSearchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                            singleItemResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase( BibConstants.TITLE_SORT)) {
                            singleItemResultDisplayRow.setTitle(searchResultField.getFieldValue());
                            bibSearchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Bib.AUTHOR)) {
                            singleItemResultDisplayRow.setAuthor(searchResultField.getFieldValue());
                            bibSearchResultDisplayRow.setAuthor(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.HOLDINGS_LOCATION_DISPLAY)) {
                            holdingsSearchResultDisplayRow.setLocation(searchResultField.getFieldValue());
                            singleItemResultDisplayRow.setHoldingsLocation(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.LOCATION_LEVEL_DISPLAY)) {
                            singleItemResultDisplayRow.setLocation(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setLocation(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.CALL_NUMBER)) {
                            singleItemResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.ITEM_STATUS)) {
                            singleItemResultDisplayRow.setItemStatus(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setItemStatus(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ITEM_TYPE_FULL_VALUE)) {
                            singleItemResultDisplayRow.setItemType(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setItemType(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.HOLDINGSIDENTIFIER)) {
                            holdingsSearchResultDisplayRow.setId(searchResultField.getFieldValue());
                            singleItemResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.HOLDINGS_CALLNUMBER_DISPLAY)) {
                            holdingsSearchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                            if(StringUtils.isEmpty(itemSearchResultDisplayRow.getCallNumber())){
                                singleItemResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                                itemSearchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                            }
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.ID)) {
                            singleItemResultDisplayRow.setId(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setId(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.ITEM_BARCODE)) {
                            singleItemResultDisplayRow.setItemBarCode(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setItemBarCode(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.ENUMERATION_DISPLAY)) {
                            singleItemResultDisplayRow.setEnumeration(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setEnumeration(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.CHRONOLOGY_DISPLAY)) {
                            singleItemResultDisplayRow.setChronology(searchResultField.getFieldValue());
                            itemSearchResultDisplayRow.setChronology(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.ITEM_STATUS_EFFECTIVE_DATE)) {
                            singleItemResultDisplayRow.setItemStatusDate(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.DUE_DATE_TIME)) {
                            singleItemResultDisplayRow.setDueDate(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.ORG_DUE_DATE_TIME)) {
                            singleItemResultDisplayRow.setOriginalDueDate(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.NUMBER_OF_PIECES_DISPLAY)) {
                            singleItemResultDisplayRow.setNoOfPieces(searchResultField.getFieldValue());
                        }else if (searchResultField.getFieldName().equalsIgnoreCase(Bib.PUBLICATIONPLACE)) {
                            singleItemResultDisplayRow.setPublication(searchResultField.getFieldValue());
                        }else if (searchResultField.getFieldName().equalsIgnoreCase(Bib.PUBLISHER)) {
                            if(StringUtils.isNotEmpty(singleItemResultDisplayRow.getPublication())){
                                singleItemResultDisplayRow.setPublication(singleItemResultDisplayRow.getPublication()+" "+searchResultField.getFieldValue());
                            }else{
                                singleItemResultDisplayRow.setPublication(searchResultField.getFieldValue());
                            }
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.PUBLICATIONDATE_DISPLAY)) {
                            bibSearchResultDisplayRow.setPublicationYear(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.CURRENT_BORROWER)) {
                            singleItemResultDisplayRow.setCurrentBorrowerId(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.PROXY_BORROWER)) {
                            singleItemResultDisplayRow.setProxyBorrowerId(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("mdf_035a")) {
                            singleItemResultDisplayRow.setOclcNumber(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.ISBN_DISPLAY)) {
                            singleItemResultDisplayRow.setIsbn(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.ITEMNOTE_VALUE_DISPLAY)) {
                            singleItemResultDisplayRow.setItemNoteValue(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.ITEMNOTE_TYPE_DISPLAY)) {
                            singleItemResultDisplayRow.setItemNoteType(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.MISSING_PIECE_FLAG_DISPLAY)) {
                            singleItemResultDisplayRow.setMissingPieceFlag(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.MISSING_PIECE_COUNT_DISPLAY)) {
                            singleItemResultDisplayRow.setMissingPieceCount(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.MISSING_PIECE_FLAG_NOTE_DISPLAY)) {
                            singleItemResultDisplayRow.setMissingPieceNote(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.CLMS_RET_FLAG)) {
                            singleItemResultDisplayRow.setClaimsReturnedFlag(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.CLMS_RET_NOTE)) {
                            singleItemResultDisplayRow.setClaimsReturnedNote(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.CLMS_RET_FLAG_CRE_DATE)) {
                            singleItemResultDisplayRow.setClaimsReturnedDate(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.ITEM_DAMAGED_FLAG_DISPLAY)) {
                            singleItemResultDisplayRow.setItemDamagedStatusFlag(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(ItemConstants.DAMAGED_ITEM_NOTE_DISPLAY)) {
                            singleItemResultDisplayRow.setItemDamagedNote(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.CREATED_BY)) {
                            singleItemResultDisplayRow.setCreatedBy(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.DATE_ENTERED)) {
                            singleItemResultDisplayRow.setCreatedDate(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.UPDATED_BY)) {
                            singleItemResultDisplayRow.setUpdatedBy(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.DATE_UPDATED)) {
                            singleItemResultDisplayRow.setUpdatedDate(searchResultField.getFieldValue());
                        }
                    }
                    if(StringUtils.isNotEmpty(singleItemResultDisplayRow.getPublication()) && StringUtils.isNotEmpty( bibSearchResultDisplayRow.getPublicationYear())){
                        singleItemResultDisplayRow.setPublication(singleItemResultDisplayRow.getPublication()+" "+bibSearchResultDisplayRow.getPublicationYear());
                    }else if(StringUtils.isEmpty(singleItemResultDisplayRow.getPublication())&& StringUtils.isNotEmpty( bibSearchResultDisplayRow.getPublicationYear())){
                        singleItemResultDisplayRow.setPublication(bibSearchResultDisplayRow.getPublicationYear());
                    }

                    if (searchResponse.getSearchResults().size() > 1) {
                        Map itemMap = new HashMap();
                        itemMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, itemSearchResultDisplayRow.getItemBarCode());
                        List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemMap);
                        if (CollectionUtils.isNotEmpty(deliverRequestBos)) {
                            itemSearchResultDisplayRow.setRequestExists(true);
                        }
                        if (!holdingsSearchResultDisplayRowMap.containsKey(holdingsSearchResultDisplayRow.getId())) {
                            List<OLEItemSearchResultDisplayRow> itemSearchResultDisplayRowList = new ArrayList<>();
                            itemSearchResultDisplayRowList.add(itemSearchResultDisplayRow);
                            holdingsSearchResultDisplayRow.setOleItemSearchResultDisplayRowList(itemSearchResultDisplayRowList);
                            holdingsSearchResultDisplayRowMap.put(holdingsSearchResultDisplayRow.getId(), holdingsSearchResultDisplayRow);
                        } else {
                            holdingsSearchResultDisplayRowMap.get(holdingsSearchResultDisplayRow.getId()).getOleItemSearchResultDisplayRowList().add(itemSearchResultDisplayRow);
                        }
                        if (!bibSearchResultDisplayRowMap.containsKey(bibSearchResultDisplayRow.getId())) {
                            List<OLEHoldingsSearchResultDisplayRow> holdingsSearchResultDisplayRowList = new ArrayList<>();
                            holdingsSearchResultDisplayRowList.add(holdingsSearchResultDisplayRow);
                            bibSearchResultDisplayRow.setOleHoldingsSearchResultDisplayRowList(holdingsSearchResultDisplayRowList);
                            bibSearchResultDisplayRowMap.put(bibSearchResultDisplayRow.getId(), bibSearchResultDisplayRow);
                        } else {
                            boolean flag = false;
                            for (OLEHoldingsSearchResultDisplayRow oleHoldingsSearchResultDisplayRow : bibSearchResultDisplayRowMap.get(bibSearchResultDisplayRow.getId()).getOleHoldingsSearchResultDisplayRowList()) {
                                if (oleHoldingsSearchResultDisplayRow.getId().equals(holdingsSearchResultDisplayRow.getId())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                bibSearchResultDisplayRowMap.get(bibSearchResultDisplayRow.getId()).getOleHoldingsSearchResultDisplayRowList().add(holdingsSearchResultDisplayRow);
                            }
                        }
                    }
                }
            }
            if (searchResponse.getSearchResults().size() > 1) {
                for (OLEBibSearchResultDisplayRow bibSearchResultDisplayRow : bibSearchResultDisplayRowMap.values()) {
                    if (bibSearchResultDisplayRow.getOleHoldingsSearchResultDisplayRowList().size() == 1) {
                        bibSearchResultDisplayRow.setHoldingsLocation(bibSearchResultDisplayRow.getOleHoldingsSearchResultDisplayRowList().get(0).getLocation());
                        if (bibSearchResultDisplayRow.getOleHoldingsSearchResultDisplayRowList().get(0).getOleItemSearchResultDisplayRowList().size() > 1) {
                            bibSearchResultDisplayRow.setItemLocation(OLEConstants.MULTIPLE);
                            bibSearchResultDisplayRow.setItemType(OLEConstants.MULTIPLE);
                            bibSearchResultDisplayRow.setItemStatus(OLEConstants.MULTIPLE);
                            bibSearchResultDisplayRow.setItemCallNumber(OLEConstants.MULTIPLE);
                        } else if (bibSearchResultDisplayRow.getOleHoldingsSearchResultDisplayRowList().get(0).getOleItemSearchResultDisplayRowList().size() == 1) {
                            OLEItemSearchResultDisplayRow oleItemSearchResultDisplayRow = bibSearchResultDisplayRow.getOleHoldingsSearchResultDisplayRowList().get(0).getOleItemSearchResultDisplayRowList().get(0);
                            bibSearchResultDisplayRow.setItemLocation(oleItemSearchResultDisplayRow.getLocation());
                            bibSearchResultDisplayRow.setItemType(oleItemSearchResultDisplayRow.getItemType());
                            bibSearchResultDisplayRow.setItemStatus(oleItemSearchResultDisplayRow.getItemStatus());
                            bibSearchResultDisplayRow.setItemCallNumber(oleItemSearchResultDisplayRow.getCallNumber());
                        }
                    } else if (bibSearchResultDisplayRow.getOleHoldingsSearchResultDisplayRowList().size() > 1) {
                        bibSearchResultDisplayRow.setHoldingsLocation(OLEConstants.MULTIPLE);
                        bibSearchResultDisplayRow.setItemLocation(OLEConstants.MULTIPLE);
                        bibSearchResultDisplayRow.setItemType(OLEConstants.MULTIPLE);
                        bibSearchResultDisplayRow.setItemStatus(OLEConstants.MULTIPLE);
                        bibSearchResultDisplayRow.setItemCallNumber(OLEConstants.MULTIPLE);
                    }
                    for (OLEHoldingsSearchResultDisplayRow holdingsSearchResultDisplayRow : bibSearchResultDisplayRow.getOleHoldingsSearchResultDisplayRowList()) {
                        if (holdingsSearchResultDisplayRow.getOleItemSearchResultDisplayRowList().size() == 1) {
                            OLEItemSearchResultDisplayRow oleItemSearchResultDisplayRow = holdingsSearchResultDisplayRow.getOleItemSearchResultDisplayRowList().get(0);
                            holdingsSearchResultDisplayRow.setItemLocation(oleItemSearchResultDisplayRow.getLocation());
                        } else if (holdingsSearchResultDisplayRow.getOleItemSearchResultDisplayRowList().size() > 1) {
                            holdingsSearchResultDisplayRow.setItemLocation(OLEConstants.MULTIPLE);
                        }
                    }
                    bibSearchResultDisplayRowList.add(bibSearchResultDisplayRow);
                }
                oleDeliverItemSearchForm.setOleBibSearchResultDisplayRowList(bibSearchResultDisplayRowList);
            } else if (searchResponse.getSearchResults().size() == 1) {
                Map itemIdMap = new HashMap();
                itemIdMap.put(OLEConstants.ITEM_UUID, singleItemResultDisplayRow.getId());
                if (singleItemResultDisplayRow.getItemStatusDate() != null) {
                    String[] itemStatusEffectiveDate = singleItemResultDisplayRow.getItemStatusDate().split(" ");
                    singleItemResultDisplayRow.setItemStatusDate(itemStatusEffectiveDate[0]);
                }
                //borrower
                getOleDeliverItemSearchService().setBorrowerInfo(singleItemResultDisplayRow);
                //request
                getOleDeliverItemSearchService().setDeliverRequestInfo(itemIdMap, singleItemResultDisplayRow);
                //outstanding fine
                getOleDeliverItemSearchService().setOutstandingFineInfo(itemIdMap, singleItemResultDisplayRow);
                //notes
                getOleDeliverItemSearchService().setNoteInfo(singleItemResultDisplayRow);
                //additional copies
                getOleDeliverItemSearchService().setAdditionalCopiesInfo(singleItemResultDisplayRow);
                //item history
                getOleDeliverItemSearchService().setInTransitHistoryInfo(singleItemResultDisplayRow);
                getOleDeliverItemSearchService().setMissingPieceItemInfo(singleItemResultDisplayRow);
                getOleDeliverItemSearchService().setClaimsReturnedInfo(singleItemResultDisplayRow);
                getOleDeliverItemSearchService().setDamagedInfo(singleItemResultDisplayRow);
                getOleDeliverItemSearchService().setRequestHistoryInfo(singleItemResultDisplayRow);
                oleDeliverItemSearchForm.setOleSingleItemResultDisplayRow(singleItemResultDisplayRow);
                oleDeliverItemSearchForm.setSingleItemFlag(true);
            }
        } else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
    }

    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverItemSearchForm oleDeliverItemSearchForm = (OLEDeliverItemSearchForm) form;
        oleDeliverItemSearchForm.setTitle(null);
        oleDeliverItemSearchForm.setAuthor(null);
        oleDeliverItemSearchForm.setPublisher(null);
        oleDeliverItemSearchForm.setItemBarCode(null);
        oleDeliverItemSearchForm.setItemCallNumber(null);
        oleDeliverItemSearchForm.setItemUUID(null);
        oleDeliverItemSearchForm.setItemType(null);
        oleDeliverItemSearchForm.setItemLocation(null);
        oleDeliverItemSearchForm.setOleBibSearchResultDisplayRowList(new ArrayList<OLEBibSearchResultDisplayRow>());
        oleDeliverItemSearchForm.setSingleItemFlag(false);
        oleDeliverItemSearchForm.setOleSingleItemResultDisplayRow(new OLESingleItemResultDisplayRow());
        return getUIFModelAndView(oleDeliverItemSearchForm);
    }


}
