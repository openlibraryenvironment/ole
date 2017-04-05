package org.kuali.ole.indexer;

import org.kuali.ole.common.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.model.jpa.BibRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiks on 01/11/16.
 */
public class BibIndexingTxObject implements Serializable {
    private List<BibRecord> bibRecords;
    private Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP = new HashMap<String, String>();
    private Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP = new HashMap<String, String>();
    private BibMarcRecordProcessor bibMarcRecordProcessor;
    private int pageNum;
    private int docsPerPage;
    private int noOfBibProcessThreads;

    public List<BibRecord> getBibRecords() {
        if(null == bibRecords) {
            bibRecords = new ArrayList<>();
        }
        return bibRecords;
    }

    public void setBibRecords(List<BibRecord> bibRecords) {
        this.bibRecords = bibRecords;
    }

    public Map<String, String> getFIELDS_TO_TAGS_2_INCLUDE_MAP() {
        return FIELDS_TO_TAGS_2_INCLUDE_MAP;
    }

    public void setFIELDS_TO_TAGS_2_INCLUDE_MAP(Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP) {
        this.FIELDS_TO_TAGS_2_INCLUDE_MAP = FIELDS_TO_TAGS_2_INCLUDE_MAP;
    }

    public Map<String, String> getFIELDS_TO_TAGS_2_EXCLUDE_MAP() {
        return FIELDS_TO_TAGS_2_EXCLUDE_MAP;
    }

    public void setFIELDS_TO_TAGS_2_EXCLUDE_MAP(Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP) {
        this.FIELDS_TO_TAGS_2_EXCLUDE_MAP = FIELDS_TO_TAGS_2_EXCLUDE_MAP;
    }

    public BibMarcRecordProcessor getBibMarcRecordProcessor() {
        return bibMarcRecordProcessor;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getDocsPerPage() {
        return docsPerPage;
    }

    public void setDocsPerPage(int docsPerPage) {
        this.docsPerPage = docsPerPage;
    }

    public void setBibMarcRecordProcessor(BibMarcRecordProcessor bibMarcRecordProcessor) {
        this.bibMarcRecordProcessor = bibMarcRecordProcessor;
    }
}
