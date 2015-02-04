package org.kuali.ole.docstore.discovery.service;

import org.kuali.ole.docstore.model.bo.OleDocument;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 8/6/12
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SRUCQLQueryService {

    //SRU request parameter index

    public static final String SRU_QUERY_TITLE = "title";
    public static final String SRU_QUERY_AUTHOR = "author";
    public static final String SRU_QUERY_CREATOR = "creator";
    public static final String SRU_QUERY_PUBLICATION_DATE = "publicationDate";
    public static final String SRU_QUERY_DATE = "date";
    public static final String SRU_QUERY_ISBN = "isbn";
    public static final String SRU_QUERY_PUBLISHER = "publisher";
    public static final String SRU_QUERY_ISSN = "issn";
    public static final String SRU_QUERY_LOCAL_ID = "localId";
    public static final String SRU_QUERY_OCLC = "oclc";
    public static final String SRU_QUERY_SUBJECT="subject";
    public static final String SRU_QUERY_SERVER_CHOICE="cql.serverChoice";
    public static final String SRU_QUERY_CQL_KEYWORDS = "cql.keywords";

    // SRU   request parameter relations

    //public static final String SRU_QUERY_SRC_RELATION = "scr"; //Support for scr is removed as part upgradation of CQL 1.2
    public static final String SRU_QUERY_ANY_RELATION = "any";
    public static final String SRU_QUERY_ALL_RELATION = "all";
    public static final String SRU_QUERY_EXACT_RELATION = "==";
    public static final String SRU_QUERY_LESSERTHAN_RELATION = "<";
    public static final String SRU_QUERY_LESSERTHAN_EQUAL_RELATION = "<=";
    public static final String SRU_QUERY_GREATERTHAN_RELATION = ">";
    public static final String SRU_QUERY_GREATERTHAN_EQUAL_RELATION = ">=";
    public static final String SRU_QUERY_ADJ_RELATION = "adj";
    public static final String SRU_QUERY_WITHIN_RELATION = "within";
    public static final String SRU_QUERY_EQUALS_RELATION = "=";

    // SRU request parameter names

    public static final String START_RECORD = "startRecord";
    public static final String MAXIMUM_RECORDS = "maximumRecords";
    public static final String RECORD_SCHEMA = "recordSchema";
    public static final String SORTKEYS = "sortKeys";

    public static final String LOCAL_Id ="localId";
    public static final String REC_ID = "rec.id";
    public static final String DC_ID="dc.id";
    public static final String OLE_ID="ole.id";
    public static final String BATH_ID="bath.id";
    public static final String DC="dc.";
    public static final String OLE="ole.";
    public static final String BATH="bath.";
    public static final String REC="rec.";



    public String getQuery(String term, String relation, String index, boolean solrQueryFlag) throws Exception;

    public List<OleDocument> queryForBibDocs(Map reqParamMap, String solrQuery) throws Exception;
}
