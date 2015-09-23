package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.*;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 26/2/14
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESearchForm extends UifFormBase {
    public boolean closeBtnShowFlag;
    protected List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
    private SearchParams searchParams;
    private SearchResponse searchResponse;
    private String docType;
    private String searchType;
    private int pageSize = 10;
    private String browseField;
    private String browseText;
    private String location;
    private String classificationScheme;
    private String callNumberBrowseText;
    private List<Holdings> holdingsList;
    private List<Item> itemList;
    private String requestXMLTextArea;
    private int facetLimit;
    private String searchTypeField;
    private String showPageSize;
    private String showFieldSort;
    private boolean isMoreFacets = false;
    private int start;
    private String linkExistingInstance;
    private String sortOrder;
    private List<OleWorkItemDocument> workItemDocumentList;
    private String sortField;
    private String sortFlag;
    private boolean nextFlag;
    private boolean previousFlag;
    private String message;
    private String pageShowEntries;
    private SearchResultDisplayFields searchResultDisplayFields;
    private List<SearchResultDisplayRow> selectedFacet;
    private List<SearchResultDisplayRow> searchResultDisplayRowList;
    private List<FacetResultField> facetResultFields;
    private boolean showRequestXml;
    private String successMessage;
    private String errorMessage;
    private String tokenId;
    private String holdingsFlag="false";
    private String eHoldingsFlag="false";
    private String holdings;
    private List<SearchResultDisplayRow> bibSearchResultDisplayRowList;
    private List<SearchResultDisplayRow> holdingSearchResultDisplayRowList;
    private List<OleWorkBibDocument> workBibDocumentList;
    private List<OleWorkHoldingsDocument> workHoldingsDocumentList;
    private List<OleWorkEHoldingsDocument> workEHoldingsDocumentList;
    private String linkToOrderOption;
    
    private int totalRecordCount;
    private String pageNumber;

    private boolean showMoreFacetNext = false;
    private boolean showMoreFacetPrevious = false;
    private String facetPageEntries;
    private String totalTime;
    private String solrTime;
    private String serverTime;
    private boolean showTime = false;
    private boolean selectAllRecords= false;
    private String idsToBeOpened;
    private String collectionIndex ="0";

    public String getIdsToBeOpened() {
        return idsToBeOpened;
    }

    public void setIdsToBeOpened(String idsToBeOpened) {
        this.idsToBeOpened = idsToBeOpened;
    }

    public boolean isSelectAllRecords() {
        return selectAllRecords;
    }

    public void setSelectAllRecords(boolean selectAllRecords) {
        this.selectAllRecords = selectAllRecords;
    }

    public OLESearchForm() {
        searchResultDisplayFields = new SearchResultDisplayFields();
     }

    public List<SearchCondition> getSearchConditions() {
        if (searchConditions == null) {
            searchConditions = new ArrayList<SearchCondition>();
        }
        return this.searchConditions;
    }


    public String getHoldings() {
        return holdings;
    }

    public void setHoldings(String holdings) {
        this.holdings = holdings;
    }

    public List<OleWorkBibDocument> getWorkBibDocumentList() {
        return workBibDocumentList;
    }

    public void setWorkBibDocumentList(List<OleWorkBibDocument> workBibDocumentList) {
        this.workBibDocumentList = workBibDocumentList;
    }

    public List<OleWorkHoldingsDocument> getWorkHoldingsDocumentList() {
        return workHoldingsDocumentList;
    }

    public List<OleWorkItemDocument> getWorkItemDocumentList() {
        return workItemDocumentList;
    }

    public void setWorkItemDocumentList(List<OleWorkItemDocument> workItemDocumentList) {
        this.workItemDocumentList = workItemDocumentList;
    }

    public void setWorkHoldingsDocumentList(List<OleWorkHoldingsDocument> workHoldingsDocumentList) {
        this.workHoldingsDocumentList = workHoldingsDocumentList;
    }

    public List<OleWorkEHoldingsDocument> getWorkEHoldingsDocumentList() {
        return workEHoldingsDocumentList;
    }

    public void setWorkEHoldingsDocumentList(List<OleWorkEHoldingsDocument> workEHoldingsDocumentList) {
        this.workEHoldingsDocumentList = workEHoldingsDocumentList;
    }

    public String getHoldingsFlag() {
        return holdingsFlag;
    }

    public void setHoldingsFlag(String holdingsFlag) {
        this.holdingsFlag = holdingsFlag;
    }

    public String geteHoldingsFlag() {
        return eHoldingsFlag;
    }

    public void seteHoldingsFlag(String eHoldingsFlag) {
        this.eHoldingsFlag = eHoldingsFlag;
    }


    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getLinkExistingInstance() {
        return linkExistingInstance;
    }

    public void setLinkExistingInstance(String linkExistingInstance) {
        this.linkExistingInstance = linkExistingInstance;
    }

    public List<SearchResultDisplayRow> getHoldingSearchResultDisplayRowList() {
        return holdingSearchResultDisplayRowList;
    }

    public void setHoldingSearchResultDisplayRowList(List<SearchResultDisplayRow> holdingSearchResultDisplayRowList) {
        this.holdingSearchResultDisplayRowList = holdingSearchResultDisplayRowList;
    }

    public List<SearchResultDisplayRow> getBibSearchResultDisplayRowList() {
        return bibSearchResultDisplayRowList;
    }

    public void setBibSearchResultDisplayRowList(List<SearchResultDisplayRow> bibSearchResultDisplayRowList) {
        this.bibSearchResultDisplayRowList = bibSearchResultDisplayRowList;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public void setShowFieldSort(String showFieldSort) {
        this.showFieldSort = showFieldSort;
    }

    public List<Holdings> getHoldingsList() {
        return holdingsList;
    }

    public void setHoldingsList(List<Holdings> holdingsList) {
        this.holdingsList = holdingsList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public boolean isCloseBtnShowFlag() {
        return closeBtnShowFlag;
    }

    public void setCloseBtnShowFlag(boolean closeBtnShowFlag) {
        this.closeBtnShowFlag = closeBtnShowFlag;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public int getFacetLimit() {
        return facetLimit;
    }

    public void setFacetLimit(int facetLimit) {
        this.facetLimit = facetLimit;
    }

    public String getSearchTypeField() {
        return searchTypeField;
    }

    public void setSearchTypeField(String searchTypeField) {
        this.searchTypeField = searchTypeField;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public SearchResponse getSearchResponse() {
        return searchResponse;
    }

    public void setSearchResponse(SearchResponse searchResponse) {
        this.searchResponse = searchResponse;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }

    public boolean getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean getPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public String getPageShowEntries() {
        return pageShowEntries;
    }

    public void setPageShowEntries(String pageShowEntries) {
        this.pageShowEntries = pageShowEntries;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        if (docType != null && docType.contains(","))
            this.docType = docType.substring(0, docType.indexOf(","));
        else
            this.docType = docType;

    }
    
    public boolean isFilterBibSearch() {
    	return !"bibliographic".equals(getDocType());
    }

    public boolean isFilterHoldingsSearch() {
    	return !"holdings".equals(getDocType());
    }

    public boolean isFilterEHoldingsSearch() {
    	return !"eHoldings".equals(getDocType());
    }

    public boolean isFilterItemSearch() {
    	return !"item".equals(getDocType());
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public List<SearchResultDisplayRow> getSelectedFacet() {
        return selectedFacet;
    }

    public void setSelectedFacet(List<SearchResultDisplayRow> selectedFacet) {
        this.selectedFacet = selectedFacet;
    }

    public List<SearchResultDisplayRow> getSearchResultDisplayRowList() {
        return searchResultDisplayRowList;
    }

    public void setSearchResultDisplayRowList(List<SearchResultDisplayRow> searchResultDisplayRowList) {
        this.searchResultDisplayRowList = searchResultDisplayRowList;
    }

    public boolean isShowRequestXml() {
        return showRequestXml;
    }

    public void setShowRequestXml(boolean showRequestXml) {
        this.showRequestXml = showRequestXml;
    }

    public List<FacetResultField> getFacetResultFields() {
        return facetResultFields;
    }

    public void setFacetResultFields(List<FacetResultField> facetResultFields) {
        this.facetResultFields = facetResultFields;
    }

    public String getBrowseField() {
        return browseField;
    }

    public void setBrowseField(String browseField) {
        this.browseField = browseField;
    }

    public String getBrowseText() {
        return browseText;
    }

    public void setBrowseText(String browseText) {
        this.browseText = browseText;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClassificationScheme() {
        return classificationScheme;
    }

    public void setClassificationScheme(String classificationScheme) {
        this.classificationScheme = classificationScheme;
    }

    public String getCallNumberBrowseText() {
        return callNumberBrowseText;
    }

    public void setCallNumberBrowseText(String callNumberBrowseText) {
        this.callNumberBrowseText = callNumberBrowseText;
    }

    public SearchResultDisplayFields getSearchResultDisplayFields() {
        return searchResultDisplayFields;
    }

    public void setSearchResultDisplayFields(SearchResultDisplayFields searchResultDisplayFields) {
        this.searchResultDisplayFields = searchResultDisplayFields;
    }

    public String getRequestXMLTextArea() {
        return requestXMLTextArea;
    }

    public void setRequestXMLTextArea(String requestXMLTextArea) {
        this.requestXMLTextArea = requestXMLTextArea;
    }

    public String getShowPageSize() {
        return showPageSize;
    }

    public void setShowPageSize(String showPageSize) {
        this.showPageSize = showPageSize;
    }

    public boolean isMoreFacets() {
        return isMoreFacets;
    }

    public void setMoreFacets(boolean moreFacets) {
        isMoreFacets = moreFacets;
    }

    public String getLinkToOrderOption() {
        return linkToOrderOption;
    }

    public void setLinkToOrderOption(String linkToOrderOption) {
        this.linkToOrderOption = linkToOrderOption;
    }

    public boolean isShowMoreFacetNext() {
        return showMoreFacetNext;
    }

    public void setShowMoreFacetNext(boolean showMoreFacetNext) {
        this.showMoreFacetNext = showMoreFacetNext;
    }

    public boolean isShowMoreFacetPrevious() {
        return showMoreFacetPrevious;
    }

    public void setShowMoreFacetPrevious(boolean showMoreFacetPrevious) {
        this.showMoreFacetPrevious = showMoreFacetPrevious;
    }

    public String getFacetPageEntries() {
        return facetPageEntries;
    }

    public void setFacetPageEntries(String facetPageEntries) {
        this.facetPageEntries = facetPageEntries;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getSolrTime() {
        return solrTime;
    }

    public void setSolrTime(String solrTime) {
        this.solrTime = solrTime;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public String getCollectionIndex() {
        return collectionIndex;
    }

    public void setCollectionIndex(String collectionIndex) {
        this.collectionIndex = collectionIndex;
    }
}
