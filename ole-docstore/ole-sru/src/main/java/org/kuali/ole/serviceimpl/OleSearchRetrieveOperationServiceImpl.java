package org.kuali.ole.serviceimpl;

import org.codehaus.plexus.util.StringUtils;
import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.bo.serachRetrieve.OleSRUData;
import org.kuali.ole.bo.serachRetrieve.OleSRUSearchRetrieveResponse;
import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.utility.ISBNUtil;
import org.kuali.ole.handler.OleSRUOpacXMLResponseHandler;
import org.kuali.ole.service.OleCQLQueryParserService;
import org.kuali.ole.service.OleDiagnosticsService;
import org.kuali.ole.service.OleSRUDataService;
import org.kuali.ole.service.OleSearchRetrieveOperationService;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSearchRetrieveOperationServiceImpl implements OleSearchRetrieveOperationService, DocstoreConstants {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    public OleSRUDataService oleSRUDataService;
    public OleDiagnosticsService oleDiagnosticsService;
    public OleCQLQueryParserService oleCQLQueryParserService;
    private Config currentContextConfig;
    private SearchParams searchParams = new SearchParams();
    private SearchCondition searchCondition = null;
    private SearchField searchField = null;
    private DocstoreClient docstoreClient;
    private SearchResultField searchResultFields = null;

    public OleSearchRetrieveOperationServiceImpl() {
        oleSRUDataService = new OleSRUDataServiceImpl();
        oleDiagnosticsService = new OleDiagnosticsServiceImpl();
        oleCQLQueryParserService = new OleCQLQueryParserServiceImpl();
    }

    /**
     * it will return a OPAC response xml  ( overall xml )
     *
     * @param reqParamMap
     * @param cqlParseBO
     * @return OPAC xml response
     */
    public String getSearchRetriveResponse(Map reqParamMap, CQLResponseBO cqlParseBO) {

        SearchParams searchParams = createSearchParams(cqlParseBO, true);
        buildSearchConditions(searchParams);
        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        for (SearchCondition condition:searchParams.getSearchConditions()){
            if(condition.getSearchField().getFieldName().equals(OleSRUConstants.PUBLICATION_DATE_SEARCH) && StringUtils.isNotBlank(condition.getSearchScope())){
                if((condition.getSearchScope().equalsIgnoreCase("GREATER THAN EQUALS") || condition.getSearchScope().equalsIgnoreCase("LESS THAN EQUALS"))){
                    condition.setSearchScope("none");
                    condition.setOperator("AND");
                    searchConditions.add(condition);
                } else if(condition.getSearchScope().equalsIgnoreCase("LESS THAN") ||condition.getSearchScope().equalsIgnoreCase("GREATER THAN")) {
                    SearchCondition newCondition=new SearchCondition();
                    String textData=condition.getSearchField().getFieldValue();
                    if(StringUtils.isNotBlank(textData)&& textData.contains("[")&&textData.contains("]")){
                        textData=textData.replace("[","");
                        textData=textData.replace("]","");
                        textData=textData.replace("TO *","");
                        textData=textData.replace("* TO","");
                        textData=textData.trim();
                    }
                    SearchField newField=new SearchField();
                    newField.setDocType(condition.getSearchField().getDocType());
                    newField.setFieldName(condition.getSearchField().getFieldName());
                    newField.setFieldValue(textData);
                    newCondition.setSearchField(newField);
                    newCondition.setOperator("NOT");
                    newCondition.setSearchScope("none");
                    condition.setOperator("AND");
                    condition.setSearchScope("none");
                    searchConditions.add(condition);
                    searchConditions.add(newCondition);
                } else {
                    searchConditions.add(condition);
                }
            } else {
                searchConditions.add(condition);
            }

        }
        searchParams.getSearchConditions().clear();
        searchParams.getSearchConditions().addAll(searchConditions);
        /*SearchCondition condition = new SearchCondition();
        condition.setOperator("AND");
        SearchField field = new SearchField();
        field.setFieldName("DocFormat");
        String format = (String)reqParamMap.get(OleSRUConstants.RECORD_SCHEMA);
        if(format.equalsIgnoreCase("OPAC")){
            format = "marc";
        }
        field.setFieldValue(format);
        condition.setSearchField(field);
        searchParams.getSearchConditions().add(condition);*/
        searchParams.setStartIndex(Integer.parseInt(reqParamMap.get("startRecord").toString()));
        searchParams.setPageSize(Integer.parseInt(reqParamMap.get("maximumRecords").toString()));
        if (searchParams == null) {
            OleSRUDiagnostics oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(getCurrentContextConfig().getProperty(OleSRUConstants.INVALID_QUERY_DIAGNOSTIC_MSG));
            OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
            oleSRUSearchRetrieveResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
            oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
            OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
            return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse, (String) reqParamMap.get("recordSchema"));

        }
        // add q=staffOnlyFlag:false to search
        FacetCondition staffOnlyFacet = new FacetCondition();
        staffOnlyFacet.setFieldName(STAFF_ONLY_FLAG);
        staffOnlyFacet.setFieldValue(OleSRUConstants.SRU_STAFF_ONLY_FLAG);
        searchParams.getFacetConditions().add(staffOnlyFacet);

        // add fq=DocType:bibliographic to search
        FacetCondition docTypeFacet = new FacetCondition();
        docTypeFacet.setFieldName(DOC_TYPE);
        docTypeFacet.setFieldValue(OleSRUConstants.BIBLIOGRAPHIC);
        searchParams.getFacetConditions().add(docTypeFacet);

        OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
        oleSRUSearchRetrieveResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
        OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
        List oleSRUBibIdList = getBibIds(reqParamMap, searchParams);
        /*if(oleSRUBibIdList!=null && oleSRUBibIdList.size()>0 && oleSRUBibIdList.get(0) instanceof String){
            if(oleSRUBibIdList.get(0).equals("Invalid Local Id")){
                OleSRUDiagnostics oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse("Local Id Value Should be a Number");
                oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
                return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get("recordSchema"));
            }
            OleSRUDiagnostics oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(getCurrentContextConfig().getProperty(OleSRUConstants.INVALID_QUERY_DIAGNOSTIC_MSG));
            oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
            return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get("recordSchema"));
        }*/
        if (oleSRUBibIdList == null) {
            OleSRUDiagnostics oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(getCurrentContextConfig().getProperty(OleSRUConstants.SERVER_DIAGNOSTIC_MSG));
            oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
            return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse, (String) reqParamMap.get("recordSchema"));

        }

        String opacXML = oleSRUDataService.getOPACXMLSearchRetrieveResponse(oleSRUBibIdList, reqParamMap);
        String styleSheet = (String) reqParamMap.get(OleSRUConstants.STYLE_SHEET);
        if (styleSheet != null) {
            opacXML = opacXML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n <?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheet + "\"?>");
        }
        opacXML = opacXML.replace("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">", "");
        opacXML = opacXML.replace("<collection>","");
        opacXML = opacXML.replace("</collection>", "");
        LOG.info("opac xml response ---->  " + opacXML);
        return opacXML;
    }


    private void buildSearchConditions(SearchParams searchParams) {

        List<SearchCondition> searchConditions = searchParams.getSearchConditions();
        if (searchConditions.size() >1 && searchConditions.size() <= 2) {
            if(searchConditions.get(0)!=null && searchConditions.get(1)!=null){
                searchConditions.get(0).setOperator(searchConditions.get(1).getOperator());
            }
        } else if (searchConditions.size() > 2) {
            for (int i = 0; i < searchConditions.size() - 1; i++) {
                if(searchConditions.get(i)!=null && searchConditions.get(i)!=null){
                    SearchCondition sc1 = searchConditions.get(i);
                    SearchCondition sc2 = searchConditions.get(i + 1);
                    sc1.setOperator(sc2.getOperator());
                }
            }
        }
    }

    private Config getCurrentContextConfig() {
        if (null == currentContextConfig) {
            currentContextConfig = ConfigContext.getCurrentContextConfig();
        }
        return currentContextConfig;
    }

    public void setCurrentContextConfig(Config currentContextConfig) {
        this.currentContextConfig = currentContextConfig;
    }

    /**
     * .
     * this will get the proper query from the oleCQLQueryParserService
     *
     * @param cqlResponseBO object
     * @return query as a string
     */
    /*public String getSolrQueryFromCQLParseBO(CQLResponseBO cqlResponseBO) {
        String solrQuery = null;
        solrQuery = oleCQLQueryParserService.getSolrQueryFromCQLBO(cqlResponseBO);
        LOG.info("solr Query ------> " + solrQuery);
        return "(" + solrQuery + ")";
    }*/
    private SearchParams createSearchParams(CQLResponseBO cqlResponseBO, boolean flag) {
        searchResultFields = new SearchResultField();
        if (cqlResponseBO.getLeftOperand() != null) {
            CQLResponseBO leftOperand = cqlResponseBO.getLeftOperand();
            createSearchParams(leftOperand, false);
            searchField = setSearchField(cqlResponseBO.getLeftOperand());
            if (searchField != null) {
                searchCondition = setSearchCondition(cqlResponseBO.getLeftOperand(), searchField);
            }
            if (null != cqlResponseBO.getLeftOperand().getBooleanTagValue() && null != cqlResponseBO.getLeftOperand().getBooleanTagValue().getValue()) {
                searchCondition.setOperator(cqlResponseBO.getBooleanTagValue().getValue());
            }
            if (searchCondition != null && searchField != null) {
                searchParams.getSearchConditions().add(searchCondition);
            }
        }
        if (cqlResponseBO.getRightOperand() != null) {
            CQLResponseBO rightOperand = cqlResponseBO.getRightOperand();
            createSearchParams(rightOperand, false);
            searchField = setSearchField(cqlResponseBO.getRightOperand());
            if (searchField != null) {
                searchCondition = setSearchCondition(cqlResponseBO.getRightOperand(), searchField);
            }
            if (null != cqlResponseBO.getBooleanTagValue() && null != cqlResponseBO.getBooleanTagValue().getValue()) {
                searchCondition.setOperator(cqlResponseBO.getBooleanTagValue().getValue());
            }
            if (searchCondition != null && searchField != null) {
                searchParams.getSearchConditions().add(searchCondition);
            }
        }
        if (cqlResponseBO.getTriple() != null) {
            CQLResponseBO triple = cqlResponseBO.getTriple();
            if (triple != null) {
                createSearchParams(triple, false);
                searchField = setSearchField(triple);
                if (searchField != null)
                    searchCondition = setSearchCondition(triple, searchField);
            }
            if (null != cqlResponseBO.getBooleanTagValue() && null != triple.getBooleanTagValue().getValue()) {
                searchCondition.setOperator(triple.getBooleanTagValue().getValue());
            }
            if (searchCondition != null && searchField != null) {
                searchParams.getSearchConditions().add(searchCondition);
            }
        }
        if (cqlResponseBO.getSearchClauseTag() != null) {
            SearchField searchField = setSearchField(cqlResponseBO);
            SearchCondition searchCondition = setSearchCondition(cqlResponseBO, searchField);
            SearchParams searchParams = new SearchParams();
            searchResultFields.setDocType(OleSRUConstants.BIBLIOGRAPHIC);
            searchResultFields.setFieldName("id");
            searchParams.getSearchResultFields().add(searchResultFields);
            searchParams.getSearchConditions().add(searchCondition);
            return searchParams;
        }

        if (flag) {
            searchResultFields.setDocType(OleSRUConstants.BIBLIOGRAPHIC);
            searchResultFields.setFieldName("id");
            searchParams.getSearchResultFields().add(searchResultFields);
            return searchParams;
        }
        return null;
    }

    private List getBibIds(Map reqParamMap, SearchParams searchParams) {
        SearchResponse searchResponse = getDocstoreClient().search(searchParams);
        List bibIds = new ArrayList();
        List<OleSRUData> oleSRUDataList = new ArrayList<>();
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            OleSRUData oleSRUData ;
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {
                    if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                        bibIds.add(searchResultField.getFieldValue());
                        oleSRUData = new OleSRUData();
                        oleSRUData.setBibId(searchResultField.getFieldValue());
                        BibTree bib = getDocstoreClient().retrieveBibTree(searchResultField.getFieldValue());
                        List<HoldingsTree> holdingsTrees = bib.getHoldingsTrees();
                        List<String> ids = new ArrayList<>();
                        for(HoldingsTree holdingsTree:holdingsTrees){
                            ids.add(holdingsTree.getHoldings().getId());
                        }
                        /*ListIterator<HoldingsTree> holdingsTreeListIterator = holdingsTrees.listIterator();
                        List<String> ids = new ArrayList<>();
                        while(holdingsTreeListIterator.hasNext()){
                            ids.add(holdingsTreeListIterator.next().getId());
                        }*/
                        oleSRUData.setInstanceIds(ids);
                        oleSRUDataList.add(oleSRUData);
                    }
                }
            }
        }
        Integer size = searchResponse.getTotalRecordCount();
        reqParamMap.put("numberOfRecords", size.longValue());
        return oleSRUDataList;
    }

    private DocstoreClient getDocstoreClient() {

        if (docstoreClient == null) {
            docstoreClient = new DocstoreLocalClient();
        }
        return docstoreClient;
    }

    private SearchField setSearchField(CQLResponseBO cqlResponseBO) {
        searchField = null;
        if (cqlResponseBO != null) {
            if (cqlResponseBO.getSearchClauseTag() != null) {
                searchField = new SearchField();
                if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.TITLE) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_TITLE)) {
                    searchField.setFieldName(OleSRUConstants.TITLE_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.AUTHOR) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_AUTHOR)) {
                    searchField.setFieldName(OleSRUConstants.AUTHOR_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.PUBLISHER) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_PUBLISHER)) {
                    searchField.setFieldName(OleSRUConstants.PUBLISHER_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.PUBLICATION_DATE) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.PUB_DATE) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_PUBLICATION_DATE) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_PUB_DATE)) {
                    searchField.setFieldName(OleSRUConstants.PUBLICATION_DATE_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.ISBN) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_ISBN)) {
                    searchField.setFieldName(OleSRUConstants.ISBN_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.ISSN) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_ISSN)) {
                    searchField.setFieldName(OleSRUConstants.ISSN_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.LOCALID) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_LOCALID)) {
                    searchField.setFieldName(OleSRUConstants.LOCALID_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.SUBJECT) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_SUBJECT)) {
                    searchField.setFieldName(OleSRUConstants.SUBJECT_SEARCH);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CHOICE) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_CHOICE)) {
                    searchField.setFieldName(OleSRUConstants.ALL_TEXT);
                } else if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.OCLC) ||
                		cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.CS_OCLC)) {
                    searchField.setFieldName(OleSRUConstants.OCLC_SEARCH);
                }
                searchField.setDocType(OleSRUConstants.BIBLIOGRAPHIC);
            }
            if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getTerm() != null) {
                String fieldValue=cqlResponseBO.getSearchClauseTag().getTerm().replaceAll("-","");
                if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.ISBN)){
                    ISBNUtil isbnUtil = new ISBNUtil();
                    try {
                        fieldValue = isbnUtil.normalizeISBN(fieldValue);
                    } catch (OleException e) {
                        e.printStackTrace();
                    }
                    searchField.setFieldValue(fieldValue);
                } else if(cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.PUBLICATION_DATE)){
                    if(cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && (cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase("<=")||cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase("<"))){
                        searchField.setFieldValue("[ * TO "+cqlResponseBO.getSearchClauseTag().getTerm()+" ]");
                    } else if(cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && (cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase(">=")||cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase(">"))){
                        searchField.setFieldValue("[ "+cqlResponseBO.getSearchClauseTag().getTerm()+" TO * ]");
                    } else{
                        searchField.setFieldValue(cqlResponseBO.getSearchClauseTag().getTerm());
                    }
                } else{
                    searchField.setFieldValue(cqlResponseBO.getSearchClauseTag().getTerm());
                }
            }
        }
        return searchField;
    }

    private SearchCondition setSearchCondition(CQLResponseBO cqlResponseBO, SearchField searchField) {
        searchCondition = new SearchCondition();
        if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase("any")) {
            searchCondition.setSearchScope("or");
        } else if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase("all")) {
            searchCondition.setSearchScope("and");
        }else if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase("adj")) {
            searchCondition.setSearchScope("phrase");
        } else {
            searchCondition.setSearchScope("phrase");
        }
        if (null != searchField)
            searchCondition.setSearchField(searchField);
        if (null != cqlResponseBO && null != cqlResponseBO.getBooleanTagValue()) {
            searchCondition.setOperator(cqlResponseBO.getBooleanTagValue().getValue());
        } else {
            searchCondition.setOperator("AND");
        }
        if (cqlResponseBO.getSearchClauseTag().getIndex().equalsIgnoreCase(OleSRUConstants.PUBLICATION_DATE)) {
            if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && (cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase("<="))) {
                searchCondition.setSearchScope("LESS THAN EQUALS");
            } else if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && (cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase(">="))) {
                searchCondition.setSearchScope("GREATER THAN EQUALS");
            } else if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && (cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase("<"))) {
                searchCondition.setSearchScope("LESS THAN");
            } else if (cqlResponseBO.getSearchClauseTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag() != null && cqlResponseBO.getSearchClauseTag().getRelationTag().getValue() != null && (cqlResponseBO.getSearchClauseTag().getRelationTag().getValue().equalsIgnoreCase(">"))) {
                searchCondition.setSearchScope("GREATER THAN");
            }
        }
        return searchCondition;
    }
}
