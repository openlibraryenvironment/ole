package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.BibDocumentSearchResult;
import org.kuali.ole.describe.bo.ImportBibConfirmReplace;
import org.kuali.ole.describe.bo.ImportBibSearch;
import org.kuali.ole.describe.bo.ImportBibUserPreferences;
import org.kuali.ole.describe.service.ImportBibService;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pj7789
 * Date: 26/11/12
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBibForm
        extends UifFormBase {

    private List<BibMarcRecord> bibMarcRecordList = new ArrayList<BibMarcRecord>();
    private ImportBibUserPreferences importBibUserPreferences;
    private ImportBibSearch importBibSearch;
    private ImportBibConfirmReplace importBibConfirmReplace;
    private SearchParams searchParams;
    private BibMarcRecord newBibMarcRecord;
    private BibMarcRecord existingBibMarcRecord;
//    private WorkInstanceDocument neWorkInstanceOlemlRecord;
    private InstanceCollection newInstanceCollection;
    private String message;
    private String displayField;
    private String uuid;
    private List<BibDocumentSearchResult> bibUuidsList;
    private String requestString;
    private Request request;
    private SortedMap<BibDocumentSearchResult, BibMarcRecord> marcDocRecMap;


    public ImportBibForm() {
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

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public ImportBibUserPreferences getImportBibUserPreferences() {
        return importBibUserPreferences;
    }

    public void setImportBibUserPreferences(ImportBibUserPreferences importBibUserPreferences) {
        this.importBibUserPreferences = importBibUserPreferences;
    }

    public List<BibMarcRecord> getBibMarcRecordList() {
        return bibMarcRecordList;
    }

    public void setBibMarcRecordList(List<BibMarcRecord> bibMarcRecordList) {
        this.bibMarcRecordList = bibMarcRecordList;
    }

    public ImportBibSearch getImportBibSearch() {
        if (null == importBibSearch) {
            importBibSearch = new ImportBibSearch();
        }
        return importBibSearch;
    }

    public void setImportBibSearch(ImportBibSearch importBibSearch) {
        this.importBibSearch = importBibSearch;
    }

    public ImportBibConfirmReplace getImportBibConfirmReplace() {
        if (null == importBibConfirmReplace) {
            importBibConfirmReplace = new ImportBibConfirmReplace();
        }
        return importBibConfirmReplace;
    }

    public void setImportBibConfirmReplace(ImportBibConfirmReplace importBibConfirmReplace) {
        this.importBibConfirmReplace = importBibConfirmReplace;
    }

    public BibMarcRecord getNewBibMarcRecord() {
        return newBibMarcRecord;
    }

    public void setNewBibMarcRecord(BibMarcRecord newBibMarcRecord) {
        this.newBibMarcRecord = newBibMarcRecord;
    }

    public BibMarcRecord getExistingBibMarcRecord() {
        return existingBibMarcRecord;
    }

    public void setExistingBibMarcRecord(BibMarcRecord existingBibMarcRecord) {
        this.existingBibMarcRecord = existingBibMarcRecord;
    }

//    public WorkInstanceDocument getNeWorkInstanceOlemlRecord() {
//        return neWorkInstanceOlemlRecord;
//    }
//
//    public void setNeWorkInstanceOlemlRecord(WorkInstanceDocument neWorkInstanceOlemlRecord) {
//        this.neWorkInstanceOlemlRecord = neWorkInstanceOlemlRecord;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<BibDocumentSearchResult> getBibUuidsList() {
        if (null == bibUuidsList) {
            bibUuidsList = new ArrayList<BibDocumentSearchResult>();
        }
        return bibUuidsList;
    }

    public void setBibUuidsList(List<BibDocumentSearchResult> bibUuidsList) {
        this.bibUuidsList = bibUuidsList;
    }

    public String getRequestString() {
        return requestString;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public InstanceCollection getNewInstanceCollection() {
        return newInstanceCollection;
    }

    public void setNewInstanceCollection(InstanceCollection newInstanceCollection) {
        this.newInstanceCollection = newInstanceCollection;
    }

    public SortedMap<BibDocumentSearchResult, BibMarcRecord> getMarcDocRecMap() {
        if (null == marcDocRecMap) {
            marcDocRecMap = new TreeMap<BibDocumentSearchResult, BibMarcRecord>(new ImportBibService());
        }
        return marcDocRecMap;
    }

    public void setMarcDocRecMap(SortedMap<BibDocumentSearchResult, BibMarcRecord> marcDocRecMap) {
        this.marcDocRecMap = marcDocRecMap;
    }
}

