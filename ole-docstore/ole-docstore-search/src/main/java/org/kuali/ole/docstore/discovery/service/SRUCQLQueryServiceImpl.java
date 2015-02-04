package org.kuali.ole.docstore.discovery.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.ole.docstore.model.bo.OleDocument;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkEInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 8/6/12
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class SRUCQLQueryServiceImpl implements SRUCQLQueryService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static SRUCQLQueryServiceImpl srucqlQueryService = null;

    private SRUCQLQueryServiceImpl() {
    }

    public static SRUCQLQueryServiceImpl getInstance() {
        if (srucqlQueryService == null)
            srucqlQueryService = new SRUCQLQueryServiceImpl();
        return srucqlQueryService;
    }

    /**
     * @param solrQuery
     * @return return list of bib id values
     * @throws Exception
     */
    @Override
    public List<OleDocument> queryForBibDocs(Map reqParamMap, String solrQuery) throws Exception {
        LOG.info("Inside queryForBibDocs method");
        List<OleDocument> oleDocuments;
        List<Map<String, Object>> solrHits;
        solrQuery = getSolrReqQuery(reqParamMap, solrQuery);
        solrHits = getSolrHitsForQuery(solrQuery, reqParamMap);
        oleDocuments = buildOleDocuments(solrHits);
        return oleDocuments;

    }


    /**
     * @param term
     * @param relation
     * @param index
     * @param solrQueryFlag
     * @return solr query
     * @throws Exception
     */
    @Override
    public String getQuery(String term, String relation, String index, boolean solrQueryFlag) throws Exception {

        if (term != null) {
            if (term.contains(":")) {
                solrQueryFlag = false;
                return null;
            }
        }

        if (index.contains(DC) || (index.contains(OLE)) || (index.contains(BATH)) || (index.contains(REC))) {
            if(index.contains(REC_ID) || (index.contains(OLE_ID)) || (index.contains(BATH_ID)) || (index.contains(DC_ID))){
                index = index.replaceAll(REC_ID,LOCAL_Id);
                index = index.replaceAll(OLE_ID,LOCAL_Id);
                index = index.replaceAll(BATH_ID,LOCAL_Id);
                index = index.replaceAll(DC_ID,LOCAL_Id);
            }
            else{
            index = index.replaceAll(REC,"");
            index = index.replaceAll(DC, "");
            index = index.replaceAll(OLE,"");
            index = index.replaceAll(BATH,"");
            }
           }

        if(SRU_QUERY_ADJ_RELATION.equals(relation) ) {
            StringBuffer query = new StringBuffer("");
            String[] terms = term.split(" ");
            query.append(getIndex(index) + "(" + "\"");
            for (int i = 0; i < terms.length ; i++){
                if(i == terms.length -1) {
                    query.append(terms[i].toLowerCase());
                }
                else{
                    query.append(terms[i].toLowerCase() + "+");
                }
            }
            query.append("\"" + "))");
            return query.toString();
        }

        if(SRU_QUERY_EQUALS_RELATION.equals(relation)) {
            StringBuffer query = new StringBuffer("");
            String[] terms = term.split(" ");
            query.append(getExactIndex(index) + "(" + "\"");
            for (int i = 0; i < terms.length ; i++){
                if(i == terms.length -1) {
                    query.append(terms[i].toLowerCase());
                }
                else{
                    query.append(terms[i].toLowerCase() + "+");
                }
            }
            query.append("\"" + "))");
            return query.toString();
        }

/*        if ( SRU_QUERY_SRC_RELATION.equals(relation)) {
            return "(all_text:(" + term + "))";
        }*/
        if (SRU_QUERY_ALL_RELATION.equals(relation)) {
            StringBuffer query = new StringBuffer("");
            String[] terms = term.split(" ");
            for (int i = 0; i < terms.length - 1; i++)
                query.append(getIndex(index) + "(" + terms[i].toLowerCase() + ")) AND ");
            query.append(getIndex(index) + "(" + terms[terms.length - 1].toLowerCase() + "))");
            return query.toString();
        }
        if (SRU_QUERY_EXACT_RELATION.equals(relation)) {
            return getExactIndex(index) + "(\"" + term + "\"))";
        }
        if (SRU_QUERY_WITHIN_RELATION.equals(relation)) {
            return getExactIndex(index) +getWithinQuery(term);
        }
        if (SRU_QUERY_ANY_RELATION.equals(relation)) {
            StringBuffer query = new StringBuffer("");
            String[] terms = term.split(" ");
            for (int i = 0; i < terms.length - 1; i++)
                query.append(getIndex(index) + "(" + terms[i].toLowerCase() + ")) OR ");
            query.append(getIndex(index) + "(" + terms[terms.length - 1].toLowerCase() + "))");
            return query.toString();
        }
        if (relation.contains(SRU_QUERY_LESSERTHAN_RELATION)) {
            StringBuffer query = new StringBuffer("");
            if (SRU_QUERY_LESSERTHAN_EQUAL_RELATION.equals(relation))
                query.append(getIndex(index) + "[0 TO " + term + "])");
            else {
                int pubDate = Integer.parseInt(term) - 1;
                query.append(getIndex(index) + "[0 TO " + pubDate + "])");
            }
            return query.toString();
        }

        if (relation.contains(SRU_QUERY_GREATERTHAN_RELATION)) {
            StringBuffer query = new StringBuffer("");
            if (SRU_QUERY_GREATERTHAN_EQUAL_RELATION.equals(relation))
                query.append(getIndex(index) + "[" + term + " TO NOW])");
            else {
                int pubDate = Integer.parseInt(term) + 1;
                query.append(getIndex(index) + "[" + pubDate + " TO NOW])");
            }
            return query.toString();
        }

        if (SRU_QUERY_TITLE.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_AUTHOR.equalsIgnoreCase(index) || SRU_QUERY_CREATOR.equals(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_PUBLICATION_DATE.equalsIgnoreCase(index) || SRU_QUERY_DATE.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_ISBN.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_PUBLISHER.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_ISSN.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_LOCAL_ID.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_OCLC.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if (SRU_QUERY_SUBJECT.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if(SRU_QUERY_SERVER_CHOICE.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        if(SRU_QUERY_CQL_KEYWORDS.equalsIgnoreCase(index))
            return getIndex(index) + "(" + term.toLowerCase() + "))";
        return "";

    }

    /**
     * @param index
     * @return query fields string
     */
    public String getIndex(String index) {

        if (SRU_QUERY_TITLE.equalsIgnoreCase(index))
            return "(Title_search:";
        if (SRU_QUERY_AUTHOR.equalsIgnoreCase(index) || SRU_QUERY_CREATOR.equals(index))
            return "(Author_search:";
        if (SRU_QUERY_PUBLICATION_DATE.equalsIgnoreCase(index) || SRU_QUERY_DATE.equalsIgnoreCase(index))
            return "(PublicationDate_search:";
        if (SRU_QUERY_ISBN.equalsIgnoreCase(index))
            return "(ISBN_search:";
        if (SRU_QUERY_PUBLISHER.equalsIgnoreCase(index))
            return "(Publisher_search:";
        if (SRU_QUERY_ISSN.equalsIgnoreCase(index))
            return "(ISSN_search:";
        if (SRU_QUERY_LOCAL_ID.equalsIgnoreCase(index))
            return "(LocalId_search:";
        if (SRU_QUERY_OCLC.equalsIgnoreCase(index))
            return "(035a:";
        if (SRU_QUERY_SUBJECT.equalsIgnoreCase(index))
            return "(Subject_search:";
        if(SRU_QUERY_SERVER_CHOICE.equalsIgnoreCase(index))
            return "(all_text:";
        if(SRU_QUERY_CQL_KEYWORDS.equalsIgnoreCase(index))
            return "(all_text:";
        return "";
    }

    /**
     * @param index
     * @return query fields string
     */
    public String getExactIndex(String index) {

        if (SRU_QUERY_TITLE.equalsIgnoreCase(index))
            return "(Title_display:";
        if (SRU_QUERY_AUTHOR.equalsIgnoreCase(index) || SRU_QUERY_CREATOR.equals(index))
            return "(Author_display:";
        if (SRU_QUERY_PUBLICATION_DATE.equalsIgnoreCase(index) || SRU_QUERY_DATE.equalsIgnoreCase(index))
            return "(PublicationDate_display:";
        if (SRU_QUERY_ISBN.equalsIgnoreCase(index))
            return "(ISBN_display:";
        if (SRU_QUERY_PUBLISHER.equalsIgnoreCase(index))
            return "(Publisher_display:";
        if (SRU_QUERY_ISSN.equalsIgnoreCase(index))
            return "(ISSN_display:";
        if (SRU_QUERY_LOCAL_ID.equalsIgnoreCase(index))
            return "(LocalId_display:";
        if (SRU_QUERY_OCLC.equalsIgnoreCase(index))
            return "(035a:";
        if (SRU_QUERY_SUBJECT.equalsIgnoreCase(index))
            return "(Subject_display:";
        return "";
    }

    /**
     * @param sortKey
     * @return
     */
    public String getSortKeyValue(String sortKey) {

        String sortKeys[] = sortKey.split(",");
        if (SRU_QUERY_TITLE.equalsIgnoreCase(sortKeys[0]))
            return "Title_sort asc";
        if (SRU_QUERY_AUTHOR.equalsIgnoreCase(sortKeys[0]))
            return "Author_sort asc";
        if (SRU_QUERY_PUBLICATION_DATE.equalsIgnoreCase(sortKeys[0]))
            return "PublicationDate_sort asc";
        if (SRU_QUERY_ISBN.equalsIgnoreCase(sortKeys[0]))
            return "ISBN_sort asc";
        if (SRU_QUERY_PUBLISHER.equalsIgnoreCase(sortKeys[0]))
            return "Publisher_sort asc";

        return "";
    }

    /**
     * @param hitsOnPage
     * @return List of OleDocuments
     */
    public List<OleDocument> buildOleDocuments(List<Map<String, Object>> hitsOnPage) {
        List<OleDocument> oleDocuments = new ArrayList<OleDocument>();
        for (Map<String, Object> hitsOnPageItr : hitsOnPage) {
            WorkBibDocument workBibDocument = new WorkBibDocument();
            Map map = hitsOnPageItr;
            Set keys = map.keySet();
            for (Object key : keys) {
                if ("id".equalsIgnoreCase(key.toString())) {
                    workBibDocument.setId((String) map.get(key));
                }
                if ("instanceIdentifier".equalsIgnoreCase(key.toString())) {
                    Object object = map.get(key);
                    List<WorkInstanceDocument> workInstanceDocuments = new ArrayList<WorkInstanceDocument>();
                    List<WorkEInstanceDocument> workEInstanceDocuments = new ArrayList<WorkEInstanceDocument>();
                    if (object instanceof String) {
                        String id = (String) object;
                        if(id.startsWith("wen")){
                            WorkEInstanceDocument workEInstanceDocument = new WorkEInstanceDocument();
                            workEInstanceDocument.setInstanceIdentifier(id);
                            workEInstanceDocuments.add(workEInstanceDocument);
                        }else{
                            WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                            workInstanceDocument.setInstanceIdentifier((String) object);
                            workInstanceDocuments.add(workInstanceDocument);
                        }
                    } else if (object instanceof List) {
                        List<String> instanceIds = (List<String>) object;
                        for (String id : instanceIds) {
                            if(id.startsWith("wen")){
                                WorkEInstanceDocument workEInstanceDocument = new WorkEInstanceDocument();
                                workEInstanceDocument.setInstanceIdentifier(id);
                                workEInstanceDocuments.add(workEInstanceDocument);
                            }else{
                                WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                                workInstanceDocument.setInstanceIdentifier(id);
                                workInstanceDocuments.add(workInstanceDocument);
                            }
                        }
                    }
                    workBibDocument.setWorkInstanceDocumentList(workInstanceDocuments);
                    workBibDocument.setWorkEInstanceDocumentList(workEInstanceDocuments);
                }
            }
            oleDocuments.add(workBibDocument);
        }
        return oleDocuments;
    }

    /**
     * @param inputQuery
     * @return hitsOnPage
     * @throws Exception Usage: Gets Solr response for input query and builds List of Maps holding Solr Doc Data
     */
    public List<Map<String, Object>> getSolrHitsForQuery(String inputQuery, Map reqParamMap) throws Exception {
        SolrServer server;
        List<Map<String, Object>> hitsOnPage = new ArrayList<Map<String, Object>>();
        server = SolrServerManager.getInstance().getSolrServer();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(inputQuery);
        solrQuery.setIncludeScore(true);
        solrQuery.set("fl", "id,instanceIdentifier");
        solrQuery.set("start", reqParamMap.get(START_RECORD).toString());
        solrQuery.set("rows", reqParamMap.get(MAXIMUM_RECORDS).toString());
        solrQuery.set("sort", getSortKeyValue(reqParamMap.get(SORTKEYS).toString()));
        QueryResponse queryResponse = server.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        reqParamMap.put("numberOfRecords", queryResponse.getResults().getNumFound());
        for (SolrDocument solrDocument : solrDocumentList) {
            hitsOnPage.add(solrDocument);
        }
        return hitsOnPage;
    }

    public String getSolrReqQuery(Map reqParamMap, String solrQuery) {
        StringBuffer query = new StringBuffer("");
        String recordSchema= (String)reqParamMap.get(RECORD_SCHEMA);
        if (recordSchema != null) {
            if (recordSchema.equalsIgnoreCase("OPAC")) {
                query.append("(DocFormat:marc AND staffOnlyFlag:false) AND DocType:bibliographic AND  ");
            } else {
                query.append("(DocFormat:" + reqParamMap.get(RECORD_SCHEMA).toString() + "AND staffOnlyFlag:false) AND DocType:bibliographic AND ");
            }
        }
        query.append(solrQuery);
        LOG.info("solr Query SRUCQLQueryServiceImpl"+query);
        query = new StringBuffer(query.toString().replace("+", " "));
        return query.toString();
    }
    public String getWithinQuery(String term){
        String range[]=term.split("[' ']");
        return "[" + range[0] +" TO "+range[1] + "])";
    }
}