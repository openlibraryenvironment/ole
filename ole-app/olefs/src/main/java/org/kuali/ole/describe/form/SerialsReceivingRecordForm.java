package org.kuali.ole.describe.form;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.ole.pojo.OLESerialReceivingRecord;
import org.kuali.ole.service.impl.OLESerialReceivingService;
import org.kuali.ole.service.impl.OLESerialReceivingServiceImpl;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sundarr
 * Date: 7/2/13
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerialsReceivingRecordForm extends UifFormBase {


    private String errorAuthorisedUserMessage;
    private SearchParams searchParams;
    private List<OLESerialReceivingRecord> oleSerialReceivingRecordList;
    private List<OLESerialReceivingRecord> tempSerialReceivingRecordList;
    private String searchLimit;
    private String pageResultDisplay;
    private boolean nextFlag;
    private boolean previousFlag;
    private String paginationFlag;
    private String searchFlag;
    private String sortOrder = "asc";

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(String searchFlag) {
        this.searchFlag = searchFlag;
    }

    public String getPaginationFlag() {
        return paginationFlag;
    }

    public void setPaginationFlag(String paginationFlag) {
        this.paginationFlag = paginationFlag;
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

    public String getPageResultDisplay() {
        return pageResultDisplay;
    }

    public void setPageResultDisplay(String pageResultDisplay) {
        this.pageResultDisplay = pageResultDisplay;
    }

    public SerialsReceivingRecordForm() {
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        searchLimit = oleSerialReceivingService.getParameter(OLEConstants.SERIAL_SEARCH_LIMIT);
        List<SearchCondition> searchConditions = getSearchParams().getSearchFieldsList();
        if (null == searchConditions) {
            searchConditions = new ArrayList<SearchCondition>();
        }
        SearchCondition title = new SearchCondition();
        title.setDocField(OLEConstants.TITLE_SEARCH);
        SearchCondition issn = new SearchCondition();
        issn.setDocField(OLEConstants.ISSN_SEARCH);
        SearchCondition serialId = new SearchCondition();
        serialId.setDocField(OLEConstants.SERIAL_SEARCH);
        SearchCondition bibId = new SearchCondition();
        bibId.setDocField(OLEConstants.LOCALID_SEARCH);
        SearchCondition poId = new SearchCondition();
        poId.setDocField(OLEConstants.PO_SEARCH);
        SearchCondition journalTitle = new SearchCondition();
        journalTitle.setDocField(OLEConstants.JOURNAL_TITLE_SEARCH);
        searchConditions.add(title);
        searchConditions.add(issn);
        searchConditions.add(serialId);
        searchConditions.add(bibId);
        searchConditions.add(poId);
        searchConditions.add(journalTitle);
    }

    public SearchParams getSearchParams() {
        if (null == searchParams) {
            searchParams = new SearchParams();
        }
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public List<OLESerialReceivingRecord> getOleSerialReceivingRecordList() {
        return oleSerialReceivingRecordList;
    }

    public void setOleSerialReceivingRecordList(List<OLESerialReceivingRecord> oleSerialReceivingRecordList) {
        this.oleSerialReceivingRecordList = oleSerialReceivingRecordList;
    }

    public String getErrorAuthorisedUserMessage() {
        return errorAuthorisedUserMessage;
    }

    public void setErrorAuthorisedUserMessage(String errorAuthorisedUserMessage) {
        this.errorAuthorisedUserMessage = errorAuthorisedUserMessage;
    }

    public String getSearchLimit() {
        return searchLimit;
    }

    public void setSearchLimit(String searchLimit) {
        this.searchLimit = searchLimit;
    }

    public List<OLESerialReceivingRecord> getTempSerialReceivingRecordList() {
        return tempSerialReceivingRecordList;
    }

    public void setTempSerialReceivingRecordList(List<OLESerialReceivingRecord> tempSerialReceivingRecordList) {
        this.tempSerialReceivingRecordList = tempSerialReceivingRecordList;
    }
}
