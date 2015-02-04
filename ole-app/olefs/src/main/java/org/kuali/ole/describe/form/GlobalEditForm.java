package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.*;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.search.FacetResultField;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 2/21/14
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class GlobalEditForm extends OLESearchForm {

    private SearchResultDisplayFields searchResultDisplayFields;
    private List<SearchResultDisplayRow> searchResultDisplayRowList;
    private List<SearchResultDisplayRow> globalEditRecords;
    private Map<String, SearchResultDisplayRow> globalEditMap;
    private List<FacetResultField> facetResultFields;

    private boolean viewGlobalEditFlag;
    private String docType;
    private String searchType;
    private String fieldType;
    private MultipartFile file;
    private boolean searchFlag = true;
    private int start;
    private boolean showExport;
    private boolean showRequestXML;
    private String requestXMLTextArea;
    private String tokenId;
    private String message;
    private String successMessage;
    private boolean linkToERSFlag;
    private String linkExistingInstance;
    private String errorMessage;
    private String globalEditDefaultMessage;
    private String selectedFileName;
    private int matchedCount;
    private String unMatchedRecords;
    private int unMatchedCount;
    private int totalRecords;
    private boolean viewGlobalEditDispMessageFlag;
    private boolean selectAll;

    public boolean isViewGlobalEditDispMessageFlag() {
        return viewGlobalEditDispMessageFlag;
    }

    public void setViewGlobalEditDispMessageFlag(boolean viewGlobalEditDispMessageFlag) {
        this.viewGlobalEditDispMessageFlag = viewGlobalEditDispMessageFlag;
    }

    public String getSelectedFileName() {
        return selectedFileName;
    }

    public void setSelectedFileName(String selectedFileName) {
        this.selectedFileName = selectedFileName;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
    }

    public String getUnMatchedRecords() {
        return unMatchedRecords;
    }

    public void setUnMatchedRecords(String unMatchedRecords) {
        this.unMatchedRecords = unMatchedRecords;
    }

    public int getUnMatchedCount() {
        return unMatchedCount;
    }

    public void setUnMatchedCount(int unMatchedCount) {
        this.unMatchedCount = unMatchedCount;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getGlobalEditDefaultMessage() {
        return globalEditDefaultMessage;
    }

    public void setGlobalEditDefaultMessage(String globalEditDefaultMessage) {
        this.globalEditDefaultMessage = globalEditDefaultMessage;
    }

    public GlobalEditForm() {
        super();
    }

    public List<SearchResultDisplayRow> getSearchResultDisplayRowList() {
        return searchResultDisplayRowList;
    }

    public void setSearchResultDisplayRowList(List<SearchResultDisplayRow> searchResultDisplayRowList) {
        this.searchResultDisplayRowList = searchResultDisplayRowList;
    }

    public List<SearchResultDisplayRow> getGlobalEditRecords() {
        if (globalEditRecords == null) {
            globalEditRecords = new ArrayList<SearchResultDisplayRow>();
        }
        return globalEditRecords;
    }

    public void setGlobalEditRecords(List<SearchResultDisplayRow> globalEditRecords) {
        this.globalEditRecords = globalEditRecords;
    }

    public boolean isViewGlobalEditFlag() {
        return viewGlobalEditFlag;
    }

    public void setViewGlobalEditFlag(boolean viewGlobalEditFlag) {
        this.viewGlobalEditFlag = viewGlobalEditFlag;
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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
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

    public String getLinkExistingInstance() {
        return linkExistingInstance;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }


    public boolean isSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(boolean searchFlag) {
        this.searchFlag = searchFlag;
    }

    public Map<String, SearchResultDisplayRow> getGlobalEditMap() {
        if (globalEditMap == null) {
            globalEditMap = new HashMap<String, SearchResultDisplayRow>();
        }
        return globalEditMap;
    }

    public void setGlobalEditMap(Map<String, SearchResultDisplayRow> globalEditMap) {
        this.globalEditMap = globalEditMap;
    }

    public List<FacetResultField> getFacetResultFields() {
        return facetResultFields;
    }

    public void setFacetResultFields(List<FacetResultField> facetResultFields) {
        this.facetResultFields = facetResultFields;
    }

    public SearchResultDisplayFields getSearchResultDisplayFields() {
        return searchResultDisplayFields;
    }

    public void setSearchResultDisplayFields(SearchResultDisplayFields searchResultDisplayFields) {
        this.searchResultDisplayFields = searchResultDisplayFields;
    }


    private String browseField;
    private String browseText;
    private String location;
    private String classificationScheme;
    private String callNumberBrowseText;
    private List<Holdings> holdingsList;
    private List<Item> itemList;

    public List<Holdings> getHoldingsList() {
        return holdingsList;
    }

    public void setHoldingsList(List<Holdings> holdingsList) {
        this.holdingsList = holdingsList;
    }

    public List<Item> getItemList() {
        return itemList;
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

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
}
