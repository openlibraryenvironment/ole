package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.*;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

import org.kuali.ole.docstore.common.document.Item;

/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 11/21/12
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkbenchForm extends UifFormBase {

    private SearchParams searchParams;
    private SearchResponse searchResponse;
    private List<OleWorkBibDocument> workBibDocumentList;
    private List<SearchResultDisplayRow> searchResultDisplayRowList;
    private List<OleWorkHoldingsDocument> workHoldingsDocumentList;
    private List<OleWorkEHoldingsDocument> workEHoldingsDocumentList;
    private List<SearchResultDisplayRow> bibSearchResultDisplayRowList;
    private List<SearchResultDisplayRow> holdingSearchResultDisplayRowList;

    private List<Bib> bibList;
    private List<Holdings> holdingsList;
    private List<Item> itemList;
    // private List<WorkHoldingsDocument> workHoldingsDocumentList;
    //private List<WorkItemDocument> workItemDocumentList;
    private List<OleWorkItemDocument> workItemDocumentList;
    // private List<WorkEHoldingsDocument> workEHoldingsDocumentList;
    private boolean showExport;
    private boolean showRequestXML;
    private String requestXMLTextArea;
    private String tokenId;
    private String message;
    private String successMessage;
    private boolean linkToERSFlag;
    private String linkExistingInstance;
    private String holdings;
    private String errorMessage;
    private int pageSize = 10;
    private int start;
    public boolean nextFlag;
    public boolean previousFlag;
    public String pageShowEntries;
    private String sortOrder;
    private String sortField;
    private String sortFlag;
    private String holdingsFlag="false";
    private String eHoldingsFlag="false";
    private String docType;

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

    public List<SearchResultDisplayRow> getSearchResultDisplayRowList() {
        return searchResultDisplayRowList;
    }

    public void setSearchResultDisplayRowList(List<SearchResultDisplayRow> searchResultDisplayRowList) {
        this.searchResultDisplayRowList = searchResultDisplayRowList;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public List<OleWorkItemDocument> getWorkItemDocumentList() {
        return workItemDocumentList;
    }

    public void setWorkItemDocumentList(List<OleWorkItemDocument> workItemDocumentList) {
        this.workItemDocumentList = workItemDocumentList;
    }

    public WorkbenchForm() {
        List<SearchCondition> searchConditions = getSearchParams().getSearchConditions();
        if (null == searchConditions) {
            searchConditions = new ArrayList<SearchCondition>();
        }
        searchConditions.add(new SearchCondition());
        searchConditions.add(new SearchCondition());
    }

    public SearchParams getSearchParams() {
        if (null == searchParams) {
            searchParams = new SearchParams();
        }
        return searchParams;
    }

    public SearchResponse getSearchResponse() {
        return searchResponse;
    }

    public void setSearchResponse(SearchResponse searchResponse) {
        this.searchResponse = searchResponse;
    }

    public List<Bib> getBibList() {
        return bibList;
    }

    public void setBibList(List<Bib> bibList) {
        this.bibList = bibList;
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

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    /*public List<WorkItemDocument> getWorkItemDocumentList() {
        return workItemDocumentList;
    }

    public void setWorkItemDocumentList(List<WorkItemDocument> workItemDocumentList) {
        this.workItemDocumentList = workItemDocumentList;
    } */

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getMessage() {
        return message;
    }

    public String getLinkExistingInstance() {
        return linkExistingInstance;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getHoldings() {
        return holdings;
    }

    public void setHoldings(String holdings) {
        this.holdings = holdings;
    }

    public void setLinkExistingInstance(String linkExistingInstance) {
        this.linkExistingInstance = linkExistingInstance;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public List<OleWorkBibDocument> getWorkBibDocumentList() {
        return workBibDocumentList;
    }

    public void setWorkBibDocumentList(List<OleWorkBibDocument> workBibDocumentList) {
        this.workBibDocumentList = workBibDocumentList;
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

    /*  public List<WorkHoldingsDocument> getWorkHoldingsDocumentList() {
        return workHoldingsDocumentList;
    }

    public void setWorkHoldingsDocumentList(List<WorkHoldingsDocument> workHoldingsDocumentList) {
        this.workHoldingsDocumentList = workHoldingsDocumentList;
    }





    public void setWorkEHoldingsDocumentList(List<WorkEHoldingsDocument> workEHoldingsDocumentList) {
        this.workEHoldingsDocumentList = workEHoldingsDocumentList;
    }*/


    public List<OleWorkEHoldingsDocument> getWorkEHoldingsDocumentList() {
        return workEHoldingsDocumentList;
    }

    public void setWorkEHoldingsDocumentList(List<OleWorkEHoldingsDocument> workEHoldingsDocumentList) {
        this.workEHoldingsDocumentList = workEHoldingsDocumentList;
    }

    public List<SearchResultDisplayRow> getBibSearchResultDisplayRowList() {
        return bibSearchResultDisplayRowList;
    }

    public void setBibSearchResultDisplayRowList(List<SearchResultDisplayRow> bibSearchResultDisplayRowList) {
        this.bibSearchResultDisplayRowList = bibSearchResultDisplayRowList;
    }

    public List<SearchResultDisplayRow> getHoldingSearchResultDisplayRowList() {
        return holdingSearchResultDisplayRowList;
    }

    public void setHoldingSearchResultDisplayRowList(List<SearchResultDisplayRow> holdingSearchResultDisplayRowList) {
        this.holdingSearchResultDisplayRowList = holdingSearchResultDisplayRowList;
    }

    public List<OleWorkHoldingsDocument> getWorkHoldingsDocumentList() {
        return workHoldingsDocumentList;
    }

    public void setWorkHoldingsDocumentList(List<OleWorkHoldingsDocument> workHoldingsDocumentList) {
        this.workHoldingsDocumentList = workHoldingsDocumentList;
    }

    public boolean isShowExport() {
        return showExport;
    }

    public void setShowExport(boolean showExport) {
        this.showExport = showExport;
    }

    public boolean isShowRequestXML() {
        return showRequestXML;
    }

    public void setShowRequestXML(boolean showRequestXML) {
        this.showRequestXML = showRequestXML;
    }

    public String getRequestXMLTextArea() {
        return requestXMLTextArea;
    }

    public void setRequestXMLTextArea(String requestXMLTextArea) {
        this.requestXMLTextArea = requestXMLTextArea;
    }

    public boolean isLinkToERSFlag() {
        return linkToERSFlag;
    }

    public void setLinkToERSFlag(boolean linkToERSFlag) {
        this.linkToERSFlag = linkToERSFlag;
    }
}
