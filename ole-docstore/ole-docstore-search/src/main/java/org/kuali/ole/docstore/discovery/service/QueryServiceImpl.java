package org.kuali.ole.docstore.discovery.service;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.ole.docstore.discovery.model.CallNumberBrowseParams;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.ole.docstore.discovery.solr.work.bib.WorkBibCommonFields;
import org.kuali.ole.docstore.model.bo.*;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.pojo.OLESerialReceivingRecord;
import org.kuali.ole.utility.callnumber.CallNumber;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.CharacterIterator;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: tirumalesh.b
 * Date: 16/1/12 Time: 12:49 PM
 */
public class QueryServiceImpl
        implements QueryService {
    private static final Logger LOG = LoggerFactory.getLogger(QueryServiceImpl.class);
    private static QueryService queryService = null;
    private static String docSearchUrl = null;
    private final String INSTANCE_IDENTIFIER = "instanceIdentifier";
    private final String BIBLIOGRAPHIC = "bibliographic";
    private final String DOC_TYPE = "DocType";
    private final String DOC_FORMAT = "DocFormat";
    private final String HOLDINGS_IDENTIFIER = "holdingsIdentifier";
    private final String ITEM_IDENTIFIER = "itemIdentifier";
    private final String INSTANCE = "instance";
    private final String OLEML = "oleml";
    private final String DELETE_WITH_LINKED_DOCS = "deleteWithLinkedDocs";
    private final String DELETE = "delete";

    private QueryServiceImpl() {
        init();
    }

    public static QueryService getInstance() {
        if (null == queryService) {
            queryService = new QueryServiceImpl();
        }
        return queryService;
    }

    protected void init() {
        LOG.debug("QueryServiceImpl init ");
        if(ConfigContext.getCurrentContextConfig()!=null){
            docSearchUrl = ConfigContext.getCurrentContextConfig().getProperty("docSearchURL");
            if ((null != docSearchUrl) && !docSearchUrl.endsWith("/")) {
                docSearchUrl = docSearchUrl + "/";
            }
        }

    }

    @Override
    public List<OleDocument> queryForDocs(OleDocument queryDoc) throws Exception {
        List<OleDocument> oleDocuments;
        List<Map<String, Object>> solrHits;
        String queryString = buildQueryForDoc(queryDoc);
        solrHits = getSolrHitsForQuery(queryString);
        oleDocuments = buildOleDocuments(solrHits);
        return oleDocuments;
    }

    private List<Map<String, Object>> getSolrHits(SearchParams searchParams) throws Exception {
        List<Map<String, Object>> solrHits;
        String queryString = buildQuery(searchParams);
        solrHits = getSolrHitsForQuery(queryString);
        return solrHits;
    }

    /**
     * To get the records from solr
     * @param searchParams
     * @return solrHits
     * @throws Exception
     */
    private List<Map<String, Object>> getSolrHitsWithParams(SearchParams searchParams) throws Exception {
        int totalRecords = 0;
        List<Map<String, Object>> solrHits;
        StringBuffer initialQuery = new StringBuffer();
        buildInitialQuery(initialQuery, searchParams);
        if(StringUtils.isNotEmpty(searchParams.getSortField()) && StringUtils.isNotEmpty(searchParams.getSortOrder())) {
            buildSortQuery(initialQuery , searchParams);
        }
        totalRecords = getSolrHitsForQueryParams(initialQuery.toString());
        LOG.info("Total Records count:" + totalRecords);
        searchParams.setTotalRecCount(totalRecords);
        searchParams.setSearchQuery(initialQuery.toString());
        solrHits = getSolrHitsForQueryParams(searchParams);
        return solrHits;
    }

    /**
     * prepare the sort solr query based on sortFiled and sortOrder type in search params
     * @param solrQuery , searchParams
     * @return
     */
    private void buildSortQuery(StringBuffer solrQuery , SearchParams searchParams) {
        String sortField = getSortField(searchParams.getSortField());
        String sortOrder = searchParams.getSortOrder();
        if(StringUtils.isNotEmpty(sortField))  {
            solrQuery.append("&sort="+sortField+" "+sortOrder);
        }
    }

    /**
     * return the sort filed according to docstore
     * @param sortField
     * @return
     */
    private String getSortField(String sortField) {

        if("Title".equals(sortField)) {
            return "Title_sort";
        }else  if("Author".equals(sortField)) {
            return "Author_sort";
        }else  if("Publication Date".equals(sortField)) {
            return "PublicationDate_sort";
        } else  if("Location".equals(sortField)) {
            return "Location_sort";
        }else  if("Call Number".equals(sortField)) {
            return "CallNumber_sort";
        }
        return null;

    }

    @Override
    public List<WorkBibDocument> getBibDocuments(SearchParams searchParams) throws Exception {
        List<WorkBibDocument> workBibDocuments;
        List<Map<String, Object>> solrHits = getSolrHitsWithParams(searchParams);
        workBibDocuments = buildWorkBibDocuments(solrHits);
        return workBibDocuments;
    }

    @Override
    public List<WorkHoldingsDocument> getHoldingDocuments(SearchParams searchParams) throws Exception {
        List<WorkHoldingsDocument> workHoldingsDocuments;
        List<Map<String, Object>> solrHits = getSolrHitsWithParams(searchParams);
        workHoldingsDocuments = buildWorkHoldingDocuments(solrHits);
        // Set bibTitle and showBibMessage for HoldingsDocument
        for (WorkHoldingsDocument workHoldingsDocument : workHoldingsDocuments) {
            if(workHoldingsDocument.getBibIdentifier() != null){
                WorkBibDocument workBibDocument = new WorkBibDocument();
                workBibDocument.setId(workHoldingsDocument.getBibIdentifier());
                workBibDocument = buildBibDocumentInfo(workBibDocument);
                workHoldingsDocument.setBibUUIDList(queryService.queryForBibs(workHoldingsDocument.getInstanceIdentifier()));
                String showBibMessage = getLinkedBibCount(workHoldingsDocument.getBibUUIDList());
                workHoldingsDocument.setBibTitle(workBibDocument.getTitle());
                workHoldingsDocument.setLinkedBibCount(showBibMessage);
            }

        }
        return workHoldingsDocuments;
    }

    @Override
    public List<OLESerialReceivingRecord> getOleSerialReceivingRecords(SearchParams searchParams) throws Exception {
        List<OLESerialReceivingRecord> oleSerialReceivingRecordList;
        List<Map<String, Object>> solrHits = getSolrHits(searchParams);
        oleSerialReceivingRecordList = buildOleSerialReceivingRecords(solrHits);
        return oleSerialReceivingRecordList;
    }

    @Override
    public List<WorkItemDocument> getItemDocuments(SearchParams searchParams) throws Exception {
        List<WorkItemDocument> workItemDocuments;
        List<Map<String, Object>> solrHits = getSolrHitsWithParams(searchParams);
        workItemDocuments = buildWorkItemDocuments(solrHits);
        // Set bibTitle and showBibMessage for ItemDocument
        for (WorkItemDocument workItemDocument : workItemDocuments) {
            if(workItemDocument.getBibIdentifier() != null) {
                WorkBibDocument workBibDocument = new WorkBibDocument();
                workBibDocument.setId(workItemDocument.getBibIdentifier());
                workBibDocument = buildBibDocumentInfo(workBibDocument);
                workItemDocument.setBibUUIDList(queryService.queryForBibs(workItemDocument.getInstanceIdentifier()));
                String linkedBibCount = getLinkedBibCount(workItemDocument.getBibUUIDList());
                workItemDocument.setBibTitle(workBibDocument.getTitle());
                workItemDocument.setLinkedBibCount(linkedBibCount);
            }
        }
        return workItemDocuments;
    }


    /**
     *  Get List of records from solr after click next or previous
     * @param searchParams
     * @return List Of Documents
     * @throws Exception
     * */
    @Override
    public List getDocuments(SearchParams searchParams) {
        List<WorkItemDocument> workItemDocumentList = null;
        List<WorkHoldingsDocument> workHoldingsDocumentList = null;
        List<WorkBibDocument> workBibDocumentList = null;
        List<WorkEHoldingsDocument> workEHoldingsDocumentList = null;

        try {
            if (searchParams.getDocType().equalsIgnoreCase("item")) {
                workItemDocumentList = getItemDocuments(searchParams);
                return workItemDocumentList;
            } else if (searchParams.getDocType().equalsIgnoreCase("holdings")) {
                workHoldingsDocumentList = getHoldingDocuments(searchParams);
                return workHoldingsDocumentList;
            } else if (searchParams.getDocType().equalsIgnoreCase("bibliographic")) {
                workBibDocumentList = getBibDocuments(searchParams);
                return workBibDocumentList;
            } else if (searchParams.getDocType().equalsIgnoreCase("eholdings")) {
                workEHoldingsDocumentList = getEHoldingsDocuments(searchParams);
                return workEHoldingsDocumentList;
            }
        } catch (Exception e) {
            LOG.info("Exception in DocumentSearch " + e);
        }
        return null;

    }


    @Override
    public String queryField(SearchParams searchParams, String fieldName) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        String query = ServiceLocator.getDiscoveryService().buildQuery(searchParams);
        int i = query.indexOf("=");
        String query1 = query.substring(i + 1);
        solrQuery.setQuery(query1);
        solrQuery.setStart(1);
        solrQuery.setRows(100);
        QueryResponse response = server.query(solrQuery);
        return getFieldValue(response, fieldName);
    }

    public String getFieldValue(QueryResponse response, String fieldName) {
        String result = null;
        SolrDocumentList docs = response.getResults();
        if (docs != null) {
            SolrDocument doc = docs.get(0);
            result = (String) doc.getFieldValue(fieldName);
        }
        return result;
    }

    /**
     * @param hitsOnPage
     * @return List of OleDocuments
     */
    public List<OLESerialReceivingRecord> buildOleSerialReceivingRecords(List<Map<String, Object>> hitsOnPage) throws SolrServerException {
        List<OLESerialReceivingRecord> oleSerialReceivingRecordList = new ArrayList<OLESerialReceivingRecord>();
        for (Map<String, Object> hitsOnPageItr : hitsOnPage) {
            OLESerialReceivingRecord oleSerialReceivingRecord = new OLESerialReceivingRecord();
            Map map = hitsOnPageItr;
            Set keys = map.keySet();
            for (Object key : keys) {
                if (key.toString().equalsIgnoreCase("Title_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        oleSerialReceivingRecord.setTitle((String) value);
                    }
                }
                if (key.toString().equalsIgnoreCase("CallNumber_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        oleSerialReceivingRecord.setCallNumber((String) value);
                    }
                }
                if (key.toString().equalsIgnoreCase("ISSN_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        oleSerialReceivingRecord.setIssn((String) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("id")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        oleSerialReceivingRecord.setBibId((String) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("instanceIdentifier")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                        workInstanceDocument.setId(value.toString());
                        workInstanceDocument =  queryForInstanceTree(workInstanceDocument);
                        String callNumber = workInstanceDocument!=null && workInstanceDocument.getHoldingsDocument()!=null ?
                                workInstanceDocument.getHoldingsDocument().getCallNumber():null;
                        String location = workInstanceDocument!=null && workInstanceDocument.getHoldingsDocument()!=null ?
                                workInstanceDocument.getHoldingsDocument().getLocationName():null;
                        oleSerialReceivingRecord.setInstanceId((String) value);
                        oleSerialReceivingRecord.setCallNumber(callNumber);
                        oleSerialReceivingRecord.setBoundLocation(location);
                        oleSerialReceivingRecordList.add(oleSerialReceivingRecord);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        for (String str : list) {
                            WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                            workInstanceDocument.setId(str);
                            workInstanceDocument =  queryForInstanceTree(workInstanceDocument);
                            String callNumber = workInstanceDocument!=null && workInstanceDocument.getHoldingsDocument()!=null ?
                                    workInstanceDocument.getHoldingsDocument().getCallNumber():null;
                            String location = workInstanceDocument!=null && workInstanceDocument.getHoldingsDocument()!=null ?
                                    workInstanceDocument.getHoldingsDocument().getLocationName():null;
                            OLESerialReceivingRecord oleSerialReceiving_Record = new OLESerialReceivingRecord();
                            oleSerialReceiving_Record.setBibId(oleSerialReceivingRecord.getBibId());
                            oleSerialReceiving_Record.setTitle(oleSerialReceivingRecord.getTitle());
                            oleSerialReceiving_Record.setCallNumber(callNumber);
                            oleSerialReceiving_Record.setIssn(oleSerialReceivingRecord.getIssn());
                            oleSerialReceiving_Record.setInstanceId(str);
                            oleSerialReceiving_Record.setBoundLocation(location);
                            oleSerialReceivingRecordList.add(oleSerialReceiving_Record);
                        }

                    }
                }


            }

        }
        return oleSerialReceivingRecordList;
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
                if (key.toString().equalsIgnoreCase("Title_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setTitle((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setTitles((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("Author_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setAuthor((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setAuthors((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("PublicationDate_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setPublicationDate((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setPublicationDates((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("ISBN_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setIsbn((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setIsbns((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("Publisher_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setPublisher((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setPublishers((List<String>) value);
                    }
                }
                if (key.toString().equalsIgnoreCase("id")) {
                    workBibDocument.setId((String) map.get(key));
                }
                if (key.toString().equalsIgnoreCase("bibIdentifier")) {
                    workBibDocument.setId((String) map.get(key));
                }

            }
            oleDocuments.add(workBibDocument);
        }
        return oleDocuments;
    }

    /**
     * @param hitsOnPage
     * @return List of workBibDocuments
     */
    public List<WorkBibDocument> buildWorkBibDocuments(List<Map<String, Object>> hitsOnPage) {
        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
        for (Map<String, Object> hitsOnPageItr : hitsOnPage) {
            WorkBibDocument workBibDocument = new WorkBibDocument();
            Map map = hitsOnPageItr;
            Set keys = map.keySet();
            for (Object key : keys) {

                if (key.toString().equalsIgnoreCase("LocalId_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setLocalIds((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setLocalId((List<String>) value);
                    }
                }
                if (key.toString().equalsIgnoreCase("Title_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setTitle((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setTitles((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("Author_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setAuthor((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setAuthors((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("PublicationDate_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setPublicationDate((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setPublicationDates((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("ISBN_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setIsbn((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setIsbns((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("Publisher_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setPublisher((String) value);
                    } else if (value instanceof List) {
                        workBibDocument.setPublishers((List<String>) value);
                    }
                }

                if (key.toString().equalsIgnoreCase("instanceIdentifier")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setInstanceIdentifier((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workBibDocument.setInstanceIdentifier(list.get(0));
                    }
                }

                if (key.toString().equalsIgnoreCase("DocFormat")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setDocFormat((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workBibDocument.setDocFormat(list.get(0));
                    }
                }

                if (key.toString().equalsIgnoreCase("staffOnlyFlag")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workBibDocument.setStaffOnlyFlag((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workBibDocument.setStaffOnlyFlag(list.get(0));
                    }
                }

                if (key.toString().equalsIgnoreCase("id")) {
                    workBibDocument.setId((String) map.get(key));
                }

            }
            workBibDocuments.add(workBibDocument);
        }
        return workBibDocuments;
    }

    /**
     * @param hitsOnPage
     * @return List of workHoldingsDocuments
     */
    public List<WorkHoldingsDocument> buildWorkHoldingDocuments(List<Map<String, Object>> hitsOnPage) {
        List<WorkHoldingsDocument> workHoldingsDocuments = new ArrayList<WorkHoldingsDocument>();
        for (Map<String, Object> hitsOnPageItr : hitsOnPage) {
            WorkHoldingsDocument workHoldingsDocument = new WorkHoldingsDocument();
            Map map = hitsOnPageItr;
            if (map.get("DocType").equals("holdings")) {
                Set keys = map.keySet();
                for (Object key : keys) {
                    if (key.toString().equalsIgnoreCase("LocalId_display")) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            workHoldingsDocument.setLocalId((String) value);
                        }
                    }
                    if (key.toString().equalsIgnoreCase("Location_display")) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            workHoldingsDocument.setLocationName((String) value);
                        }
                    }
                    if (key.toString().equalsIgnoreCase("CallNumber_display")) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            workHoldingsDocument.setCallNumber((String) value);
                        }
                    }
                    if (key.toString().equalsIgnoreCase("id")) {
                        workHoldingsDocument.setHoldingsIdentifier((String) map.get(key));
                    }
                    if (key.toString().equalsIgnoreCase("bibIdentifier")) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            workHoldingsDocument.setBibIdentifier((String) value);
                        } else if (value instanceof List) {
                            ArrayList<String> list = (ArrayList<String>) value;
                            workHoldingsDocument.setBibIdentifier(list.get(0));
                        }
                    }
                    if (key.toString().equalsIgnoreCase("instanceIdentifier")) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            workHoldingsDocument.setInstanceIdentifier((String) value);
                        } else if (value instanceof List) {
                            ArrayList<String> list = (ArrayList<String>) value;
                            workHoldingsDocument.setInstanceIdentifier(list.get(0));
                        }
                    }
                    if (key.toString().equalsIgnoreCase("staffOnlyFlag")) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            workHoldingsDocument.setStaffOnlyFlag((String) value);
                        } else if (value instanceof List) {
                            ArrayList<String> list = (ArrayList<String>) value;
                            workHoldingsDocument.setStaffOnlyFlag(list.get(0));

                        }
                    }
                }
                workHoldingsDocuments.add(workHoldingsDocument);
            }
        }
        return workHoldingsDocuments;
    }

    /**
     * @param hitsOnPage
     * @return List of workItemDocuments
     */
    public List<WorkItemDocument> buildWorkItemDocuments(List<Map<String, Object>> hitsOnPage) {
        List<WorkItemDocument> workItemDocuments = new ArrayList<WorkItemDocument>();
        for (Map<String, Object> hitsOnPageItr : hitsOnPage) {
            WorkItemDocument workItemDocument = new WorkItemDocument();
            Map map = hitsOnPageItr;
            Set keys = map.keySet();
            for (Object key : keys) {
                if (key.toString().equalsIgnoreCase("LocalId_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setLocalId((String) value);
                    }
                }
                if (key.toString().equalsIgnoreCase("Location_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setLocation((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setLocation(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("CallNumber_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setCallNumber((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setCallNumber(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("ItemBarcode_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setBarcode((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setBarcode(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("ShelvingOrder_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setShelvingOrder((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setShelvingOrder(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("Enumeration_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setEnumeration((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setEnumeration(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("Chronology_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setChronology((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setChronology(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("CopyNumber_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setCopyNumber((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setCopyNumber(list.get(0));
                    }
                }

                if (key.toString().equalsIgnoreCase("bibIdentifier")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setBibIdentifier((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setBibIdentifier(list.get(0));
                    }
                }

                if (key.toString().equalsIgnoreCase("id")) {
                    workItemDocument.setId((String) map.get(key));
                }

                if (key.toString().equalsIgnoreCase("id")) {
                    workItemDocument.setItemIdentifier((String) map.get(key));
                }

                if (key.toString().equalsIgnoreCase("instanceIdentifier")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setInstanceIdentifier((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setInstanceIdentifier(list.get(0));
                    }
                }

                if (key.toString().equalsIgnoreCase("staffOnlyFlag")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workItemDocument.setStaffOnlyFlag((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workItemDocument.setStaffOnlyFlag(list.get(0));
                    }
                }

            }
            workItemDocuments.add(workItemDocument);
        }
        return workItemDocuments;
    }


    /**
     * @param queryDoc
     * @return
     * @throws Exception
     */
    @Override
    public WorkBibDocument queryForBibTree(WorkBibDocument queryDoc) throws Exception {
        String id = queryDoc.getId();
        LOG.info("id-->" + id);
        List<WorkInstanceDocument> workInstanceDocumentList = new ArrayList<WorkInstanceDocument>();
        List<WorkItemDocument> workItemDocumentList = new ArrayList<WorkItemDocument>();
        WorkInstanceDocument workInstanceDocument = null;
        WorkHoldingsDocument workHoldingsDocument;
        WorkItemDocument workItemDocument;
        List<WorkEInstanceDocument> workEInstanceDocumentList = new ArrayList<>();
        queryDoc = buildBibDocumentInfo(queryDoc);
        String queryString = buildQueryForBibTree(queryDoc);
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery(queryString);
        solrQuery.addSortField("Location_sort", SolrQuery.ORDER.asc);
        solrQuery.addSortField("ShelvingOrder_sort", SolrQuery.ORDER.asc);
        solrQuery.setRows(50000); // There can bib can have more items.
        LOG.info("solr query-->" + solrQuery);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        for (SolrDocument solrDocument : solrDocumentList) {

            //build instance document
            String docType = (String) solrDocument.getFieldValue("DocType");
            if (docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                workInstanceDocument = new WorkInstanceDocument();
                String instanceIdentifier = (String) solrDocument.getFieldValue("instanceIdentifier");
                String holdingsIdentifier = (String) solrDocument.getFieldValue("id");
                workItemDocumentList = new ArrayList<WorkItemDocument>();
                workHoldingsDocument = new WorkHoldingsDocument();
                workHoldingsDocument.setHoldingsIdentifier(holdingsIdentifier);
                workHoldingsDocument.setInstanceIdentifier(instanceIdentifier);
                workHoldingsDocument.setBibUUIDList(queryService.queryForBibs(workHoldingsDocument.getInstanceIdentifier()));
                String locationName = (String) solrDocument.getFieldValue("Location_display");
                workHoldingsDocument.setLocationName(locationName);
                workHoldingsDocument.setCallNumberType(getFieldVal(solrDocument, "CallNumberType_display"));
                workHoldingsDocument.setCallNumberPrefix(getFieldVal(solrDocument, "CallNumberPrefix_display"));
                workHoldingsDocument.setCallNumber(getFieldVal(solrDocument, "CallNumber_display"));
                workHoldingsDocument.setCopyNumber(getFieldVal(solrDocument, "CopyNumber_display"));
                workHoldingsDocument.setDateEntered(getFieldDateVal(solrDocument, "dateEntered"));
                workHoldingsDocument.setDateUpdated(getFieldDateVal(solrDocument, "dateUpdated"));
                workHoldingsDocument.setCreatedBy(getFieldVal(solrDocument, "createdBy"));
                workHoldingsDocument.setUpdatedBy(getFieldVal(solrDocument, "updatedBy"));
                workHoldingsDocument.setLocalId(getFieldVal(solrDocument, "LocalId_display"));
                workHoldingsDocument.setBibIdentifier(getFieldVal(solrDocument, "bibIdentifier"));
                workInstanceDocument.setHoldingsDocument(workHoldingsDocument);

                for (SolrDocument itemSolrDocument : solrDocumentList) {
                    String itemDocType = (String) itemSolrDocument.getFieldValue("DocType");
                    if ((itemDocType.equalsIgnoreCase(DocType.ITEM.getCode())) && (itemSolrDocument.getFieldValue("holdingsIdentifier").equals(holdingsIdentifier))) {

                        workItemDocument = new WorkItemDocument();
                        String itemIdentifier = (String) itemSolrDocument.getFieldValue("id");
                        workItemDocument.setItemIdentifier(itemIdentifier);
                        String callNumberType = (String) itemSolrDocument.getFieldValue("CallNumberType_display");
                        String callNumberPrefix = (String) itemSolrDocument.getFieldValue("CallNumberPrefix_display");
                        String callNumber = (String) itemSolrDocument.getFieldValue("CallNumber_display");
                        workItemDocument.setLocation(getFieldVal(itemSolrDocument, "Location_display"));
                        workItemDocument.setCopyNumber(getFieldVal(itemSolrDocument, "CopyNumber_display"));
                        workItemDocument.setEnumeration(getFieldVal(itemSolrDocument, "Enumeration_display"));
                        workItemDocument.setChronology(getFieldVal(itemSolrDocument, "Chronology_display"));
                        workItemDocument.setItemStatus(getFieldVal(itemSolrDocument, "ItemStatus_display"));
                        workItemDocument.setBarcode(getFieldVal(itemSolrDocument, "ItemBarcode_display"));
                        workItemDocument.setVolumeNumber(getFieldVal(itemSolrDocument, "VolumeNumber_display"));
                        workItemDocument.setLocalId(getFieldVal(itemSolrDocument, "LocalId_display"));
                        workItemDocument.setCallNumberType(callNumberType);
                        workItemDocument.setCallNumberPrefix(callNumberPrefix);
                        workItemDocument.setItemType(getFieldVal(itemSolrDocument, "ItemTypeCodeValue_display"));
                        workItemDocument.setCallNumber(callNumber);
                        String staffOnlyFlag = (String) itemSolrDocument.getFieldValue("staffOnlyFlag");
                        workItemDocument.setStaffOnlyFlag(staffOnlyFlag);
                        workItemDocument.setDateEntered(getFieldDateVal(itemSolrDocument, "dateEntered"));
                        workItemDocument.setDateUpdated(getFieldDateVal(itemSolrDocument, "dateUpdated"));
                        workItemDocument.setCreatedBy(getFieldVal(itemSolrDocument, "createdBy"));
                        workItemDocument.setUpdatedBy(getFieldVal(itemSolrDocument, "updatedBy"));
                        workItemDocumentList.add(workItemDocument);
                        workInstanceDocument.setItemDocumentList(workItemDocumentList);

                        LOG.debug("workItemDocumentList size-->" + workItemDocumentList.size());
                    }
                }
                workInstanceDocument.setInstanceIdentifier(instanceIdentifier);
                workInstanceDocumentList.add(workInstanceDocument);
            }

            if (docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                WorkEInstanceDocument workEInstanceDocument = new WorkEInstanceDocument();
                WorkEHoldingsDocument workEHoldingsDocument = new WorkEHoldingsDocument();
                workEHoldingsDocument.setHoldingsIdentifier((String) solrDocument.getFieldValue("id"));
                workEHoldingsDocument.setLocalId((String) solrDocument.getFieldValue("LocalId_display"));
                workEHoldingsDocument.setImprint((String) solrDocument.getFieldValue("Imprint_display"));
                workEHoldingsDocument.setInstanceIdentifier((String) solrDocument.getFieldValue("instanceIdentifier"));
                workEHoldingsDocument.setAccessStatus(getFieldVal(solrDocument, "AccessStatus_display"));
                workEHoldingsDocument.setSubscriptionStatus(getFieldVal(solrDocument, "SubscriptionStatus_display"));
                workEHoldingsDocument.setPlatForm(getFieldVal(solrDocument, "Platform_display"));
                workEHoldingsDocument.setLocation(getFieldVal(solrDocument, "Location_display"));
                workEHoldingsDocument.setUrl(getFieldVal(solrDocument, "Url_display"));
                workEHoldingsDocument.setStaffOnly(getFieldVal(solrDocument, "staffOnlyFlag"));
                workEHoldingsDocument.seteResourceName(getFieldVal(solrDocument, "EResource_name_display"));
                workEInstanceDocument.setInstanceIdentifier((String) solrDocument.getFieldValue("instanceIdentifier"));
                workEHoldingsDocument.setCoverageDates(getFieldValues(solrDocument,"CoverageDate_display" ));
                workEInstanceDocument.setWorkEHoldingsDocument(workEHoldingsDocument);
                workEInstanceDocument.setPublisher(getFieldVal(solrDocument, "E_Publisher_display"));
                workEInstanceDocument.setPublicDisplayNote(getFieldVal(solrDocument, "Public_Note_display"));
                workEInstanceDocumentList.add(workEInstanceDocument);
            }

            queryDoc.setInstanceDocument(workInstanceDocument);
            queryDoc.setWorkInstanceDocumentList(workInstanceDocumentList);
            queryDoc.setWorkEInstanceDocumentList(workEInstanceDocumentList);
        }

        return queryDoc;
    }

    private String getFieldVal(SolrDocument solrDocument, String field) {
        String fieldValue = null;
        if (solrDocument.getFieldValue(field) instanceof String) {
            fieldValue = (String) solrDocument.getFieldValue(field);
        } else if (solrDocument.getFieldValue(field) instanceof List) {
            List<String> locList = (List<String>) solrDocument.getFieldValue(field);
            fieldValue = locList.get(0);
        }
        return fieldValue;
    }

    private List<String> getFieldValues(SolrDocument solrDocument, String field) {
        List<String> list = new ArrayList<>();
        if (solrDocument.getFieldValue(field) instanceof String) {
            list.add((String) solrDocument.getFieldValue(field));
        } else if (solrDocument.getFieldValue(field) instanceof List) {
            list.addAll((List<String>) solrDocument.getFieldValue(field));
        }
        return list;
    }

    private String getFieldDateVal(SolrDocument solrDocument, String field) {
        String fieldValue = null;

        if (solrDocument.getFieldValue(field) instanceof Date) {
            Format formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
            fieldValue = formatter.format(solrDocument.getFieldValue(field));
        }
        return fieldValue;
    }
    public WorkBibDocument buildBibDocumentInfo(WorkBibDocument bibDocument) throws SolrServerException, IOException {
        String queryString = buildQueryForBibInfo(bibDocument);
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery(queryString);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();

        for (SolrDocument solrDocument : solrDocumentList) {
            StringBuffer titleBuffer = new StringBuffer();
            String title = (String) solrDocument.getFieldValue("Title_display");
            String format = (String) solrDocument.getFieldValue("DocFormat");
            String staffOnlyFlag = (String) solrDocument.getFieldValue("staffOnlyFlag");
            /* if("true".equals(staffOnlyFlag))  {
                titleBuffer.append("<font color='red'>");
                titleBuffer.append(title);
                titleBuffer.append("</font>");
            bibDocument.setTitle(titleBuffer.toString());
            }else{
                bibDocument.setTitle(title);
            }*/
            bibDocument.setTitle(title);
            bibDocument.setAuthor(getFieldVal(solrDocument, "Author_display"));
            bibDocument.setPublicationDate(getFieldVal(solrDocument, "PublicationDate_display"));
            bibDocument.setPublisher(getFieldVal(solrDocument, "Publisher_display"));
            bibDocument.setIssn(getFieldVal(solrDocument, "ISSN_display"));
            bibDocument.setIsbn(getFieldVal(solrDocument, "ISBN_display"));
            bibDocument.setEdition(getFieldVal(solrDocument, "Edition_display"));
            bibDocument.setStaffOnlyFlag(staffOnlyFlag);
            bibDocument.setDocFormat(format);
        }

        return bibDocument;
    }


    /**
     * @param workBibDocumentList
     * @return
     * @throws Exception
     */
    @Override
    public List<WorkBibDocument> queryForBibTree(List<WorkBibDocument> workBibDocumentList) throws Exception {
        for (WorkBibDocument bibDocument : workBibDocumentList) {
            bibDocument = queryForBibTree(bibDocument);
            workBibDocumentList.add(bibDocument);
        }
        return workBibDocumentList;
    }


    /**
     * This method returns the Bib ids for the selected bound-with instance.
     *
     * @param instancedId
     * @return
     * @throws SolrServerException
     */
    @Override
    public List<String> queryForBibs(String instancedId) throws SolrServerException {
        List<String> bibUuidList = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("instanceIdentifier:" + instancedId);
        solrQuery.setRows(500);
        LOG.info("solr query-->" + solrQuery);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        for (SolrDocument solrDocument : solrDocumentList) {
            String docType = (String) solrDocument.getFieldValue("DocType");
            if (docType.equalsIgnoreCase(DocType.BIB.getDescription())) {
                bibUuidList.add((String) solrDocument.getFieldValue("id"));
            }

        }
        return bibUuidList;
    }

    public List<String> queryForItems(String bibId) throws SolrServerException {
        List<String> itemIdList = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("(DocType:item)AND(bibIdentifier:" + bibId + ")");
        solrQuery.setRows(500);
        LOG.info("solr query-->" + solrQuery);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        for (SolrDocument solrDocument : solrDocumentList) {
            Object itemId = solrDocument.getFieldValue("id");
            if (itemId instanceof String) {
                itemIdList.add((String) itemId);
            }
            if (itemId instanceof List) {
                itemIdList.addAll((ArrayList) itemId);
            }
        }


        return itemIdList;
    }

    @Override
    public String queryForBib(String itemId) throws SolrServerException {
        List<String> bibUuidList = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("itemIdentifier:" + itemId);
        solrQuery.setRows(500);
        LOG.info("solr query-->" + solrQuery);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        for (SolrDocument solrDocument : solrDocumentList) {
            String docType = (String) solrDocument.getFieldValue("DocType");
            if (docType.equalsIgnoreCase(DocType.INSTANCE.getDescription())) {
                return getFieldVal(solrDocument, "bibIdentifier");
            }
        }
        return "";
    }

    /**
     * This method used to get the instances for the selected bibs.
     *
     * @param uuid
     * @return
     * @throws SolrServerException
     */
    @Override
    public List<String> queryForInstances(String uuid) throws SolrServerException {
        List<String> instanceIdList = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("id:" + uuid);
        LOG.info("solr query-->" + solrQuery);
        solrQuery.setRows(500);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        for (SolrDocument solrDocument : solrDocumentList) {
            String docType = (String) solrDocument.getFieldValue("DocType");
            if (docType.equalsIgnoreCase(DocType.BIB.getDescription())) {
                Object instanceId = solrDocument.getFieldValue("instanceIdentifier");
                if (instanceId instanceof String) {
                    instanceIdList.add((String) instanceId);
                }
                if (instanceId instanceof List) {
                    instanceIdList = (List<String>) instanceId;
                }
            }

        }
        return instanceIdList;
    }

    @Override
    public WorkInstanceDocument queryForInstanceTree(WorkInstanceDocument instanceDocument) throws SolrServerException {

        String id = instanceDocument.getId();
        LOG.info("id-->" + id);
        List<WorkInstanceDocument> workInstanceDocumentList = new ArrayList<WorkInstanceDocument>();
        List<WorkHoldingsDocument> workHoldingsDocumentList = new ArrayList<WorkHoldingsDocument>();
        List<WorkItemDocument> workItemDocumentList = new ArrayList<WorkItemDocument>();
        WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
        WorkHoldingsDocument workHoldingsDocument;
        WorkItemDocument workItemDocument;
        //String queryString = buildQueryForInstanceTree(instanceDocument);
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("DocType:holdings AND instanceIdentifier:" + id);
        LOG.info("solr query-->" + solrQuery);
        solrQuery.addSortField("Location_sort", SolrQuery.ORDER.asc);
        solrQuery.addSortField("ShelvingOrder_sort", SolrQuery.ORDER.asc);
        solrQuery.setRows(500);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> holdingsSolrDocumentList = response.getResults();
        String docType;
        workItemDocumentList = new ArrayList<WorkItemDocument>();
        workHoldingsDocumentList = new ArrayList<WorkHoldingsDocument>();//
        for (SolrDocument holdingSolrDocument : holdingsSolrDocumentList) {
            docType = (String) holdingSolrDocument.getFieldValue("DocType");
            if ((docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) && (holdingSolrDocument.getFieldValue("instanceIdentifier").equals(id))) {
                workHoldingsDocument = new WorkHoldingsDocument();
                String holdingsIdentifier = (String) holdingSolrDocument.getFieldValue("id");
                String bibIdentifier = getFieldVal(holdingSolrDocument, "bibIdentifier");
                workHoldingsDocument.setHoldingsIdentifier(holdingsIdentifier);
                String locationName = getFieldVal(holdingSolrDocument, "Location_display");
                String instanceIdentifier = getFieldVal(holdingSolrDocument, "instanceIdentifier");
                workHoldingsDocument.setInstanceIdentifier(instanceIdentifier);
                workHoldingsDocument.setBibUUIDList(queryService.queryForBibs(instanceIdentifier));
                workHoldingsDocument.setLocationName(locationName);
                workHoldingsDocument.setCallNumberType(getFieldVal(holdingSolrDocument, "CallNumberType_display"));
                workHoldingsDocument.setCallNumberPrefix(getFieldVal(holdingSolrDocument, "CallNumberPrefix_display"));
                workHoldingsDocument.setCallNumber(getFieldVal(holdingSolrDocument, "CallNumber_display"));
                workHoldingsDocument.setCopyNumber(getFieldVal(holdingSolrDocument, "CopyNumber_display"));
                String staffOnlyFlag = (String) holdingSolrDocument.getFieldValue("staffOnlyFlag");
                workHoldingsDocument.setStaffOnlyFlag(staffOnlyFlag);
                workHoldingsDocumentList.add(workHoldingsDocument);
                workInstanceDocument.setBibIdentifier(bibIdentifier);
                workInstanceDocument.setHoldingsDocument(workHoldingsDocument);
            }
        }
        SolrQuery solrQueryForItem = new SolrQuery();
        solrQueryForItem.setQuery("DocType:item AND instanceIdentifier:" + id);
        LOG.info("solr query-->" + solrQueryForItem);
        solrQueryForItem.addSortField("Location_sort", SolrQuery.ORDER.asc);
        solrQueryForItem.addSortField("CallNumberPrefix_sort", SolrQuery.ORDER.asc);
        solrQueryForItem.addSortField("CallNumber_sort", SolrQuery.ORDER.asc);
        solrQueryForItem.addSortField("Enumeration_sort", SolrQuery.ORDER.asc);
        solrQueryForItem.addSortField("Chronology_sort", SolrQuery.ORDER.asc);
        solrQueryForItem.addSortField("CopyNumber_sort", SolrQuery.ORDER.asc);
        solrQueryForItem.addSortField("ItemBarcode_sort", SolrQuery.ORDER.asc);
        solrQueryForItem.setRows(5000);
        QueryResponse responseItem = server.query(solrQueryForItem);
        List<SolrDocument> itemSolrDocumentList = responseItem.getResults();
        for (SolrDocument itemSolrDocument : itemSolrDocumentList) {
            docType = (String) itemSolrDocument.getFieldValue("DocType");
            if ((docType.equalsIgnoreCase(DocType.ITEM.getCode()))) {
                workItemDocument = new WorkItemDocument();
                String itemIdentifier = getFieldVal(itemSolrDocument, "id");
                String callNumberType = getFieldVal(itemSolrDocument, "CallNumberType_display");
                String callNumberPrefix = getFieldVal(itemSolrDocument, "CallNumberPrefix_display");
                String bibIdentifier = getFieldVal(itemSolrDocument, "bibIdentifier");
                String callNumber = getFieldVal(itemSolrDocument, "CallNumber_display");
                workItemDocument.setLocation(getFieldVal(itemSolrDocument, "Location_display"));
                workItemDocument.setBarcode(getFieldVal(itemSolrDocument, "ItemBarcode_display"));
                workItemDocument.setItemType(getFieldVal(itemSolrDocument, "ItemTypeCodeValue_display"));
                workItemDocument.setLocationName(getFieldVal(itemSolrDocument, "LocationLevelName_display"));
                workItemDocument.setItemStatus(getFieldVal(itemSolrDocument, "ItemStatus_display"));
                workItemDocument.setCopyNumber(getFieldVal(itemSolrDocument, "CopyNumber_display"));
                workItemDocument.setEnumeration(getFieldVal(itemSolrDocument, "Enumeration_display"));
                workItemDocument.setChronology(getFieldVal(itemSolrDocument, "Chronology_display"));
                workItemDocument.setCallNumberType(callNumberType);
                String staffOnlyFlag = (String) itemSolrDocument.getFieldValue("staffOnlyFlag");
                workItemDocument.setStaffOnlyFlag(staffOnlyFlag);
                workItemDocument.setCallNumberPrefix(callNumberPrefix);
                workItemDocument.setCallNumber(callNumber);
                workItemDocument.setItemIdentifier(itemIdentifier);
                workItemDocumentList.add(workItemDocument);
                workInstanceDocument.setBibIdentifier(bibIdentifier);
            }
        }
        workInstanceDocument.setItemDocumentList(workItemDocumentList);
        workInstanceDocument.setInstanceIdentifier(id);
        workInstanceDocumentList.add(workInstanceDocument);
        return workInstanceDocument;
    }

    @Override
    public WorkItemDocument queryForItemTree(WorkItemDocument itemDocument) throws SolrServerException {

        String id = itemDocument.getId();
        LOG.info("id-->" + id);
        List<WorkHoldingsDocument> workHoldingsDocumentList = new ArrayList<WorkHoldingsDocument>();
        List<WorkItemDocument> workItemDocumentList = new ArrayList<WorkItemDocument>();
        WorkItemDocument workItemDocument;
        //String queryString = buildQueryForItemTree(itemDocument);
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("id:" + id);
        solrQuery.setRows(500);
        LOG.info("solr query-->" + solrQuery);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        String docType;
        workItemDocument = new WorkItemDocument();
        workItemDocumentList = new ArrayList<WorkItemDocument>();

        for (SolrDocument itemSolrDocument : solrDocumentList) {
            docType = (String) itemSolrDocument.getFieldValue("DocType");
            if ((docType.equalsIgnoreCase(DocType.ITEM.getCode())) && (itemSolrDocument.getFieldValue("id").equals(id))) {


                String itemIdentifier = (String) itemSolrDocument.getFieldValue("id");
//                String instanceIdentifier = (String) itemSolrDocument.getFieldValue("instanceIdentifier");
//                String bibIdentifier = (String) itemSolrDocument.getFieldValue("bibIdentifier");
//                String callNumberType = (String) itemSolrDocument.getFieldValue("CallNumberType_display");
//                String callNumberPrefix = (String) itemSolrDocument.getFieldValue("CallNumberPrefix_display");
//                String callNumber = (String) itemSolrDocument.getFieldValue("CallNumber_display");

                String instanceIdentifier = getFieldVal(itemSolrDocument, "instanceIdentifier");
                String bibIdentifier = getFieldVal(itemSolrDocument, "bibIdentifier");
                String callNumberType = getFieldVal(itemSolrDocument, "CallNumberType_display");
                String callNumberPrefix = getFieldVal(itemSolrDocument, "CallNumberPrefix_display");
                String callNumber = getFieldVal(itemSolrDocument, "CallNumber_display");

                workItemDocument.setLocation(getFieldVal(itemSolrDocument, "Location_display"));
                workItemDocument.setBarcode(getFieldVal(itemSolrDocument, "ItemBarcode_display"));
                workItemDocument.setItemType(getFieldVal(itemSolrDocument, "ItemTypeCodeValue_display"));
                workItemDocument.setLocationName(getFieldVal(itemSolrDocument, "LocationLevelName_display"));
                workItemDocument.setCopyNumber(getFieldVal(itemSolrDocument, "CopyNumber_display"));
                workItemDocument.setVolumeNumber(getFieldVal(itemSolrDocument, "VolumeNumber_display"));
                workItemDocument.setItemStatus(getFieldVal(itemSolrDocument, "ItemStatus_display"));
                workItemDocument.setCallNumber(callNumber);
                workItemDocument.setCallNumberType(callNumberType);
                workItemDocument.setCallNumberPrefix(callNumberPrefix);
                workItemDocument.setItemIdentifier(itemIdentifier);
                workItemDocument.setInstanceIdentifier(instanceIdentifier);
                workItemDocument.setBibIdentifier(bibIdentifier);
                workItemDocumentList.add(workItemDocument);
                LOG.debug("workItemDocumentList size-->" + workItemDocumentList.size());
            }
        }


        return workItemDocument;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WorkEInstanceDocument queryForEInstanceTree(WorkEInstanceDocument eInstanceDocument) throws SolrServerException {

        String id = eInstanceDocument.getId();
        LOG.info("id-->" + id);
        WorkEInstanceDocument workEInstanceDocument = new WorkEInstanceDocument();
        WorkEHoldingsDocument workEHoldingsDocument;
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("instanceIdentifier:" + id);
        LOG.info("solr query-->" + solrQuery);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        String docType;

        for (SolrDocument holdingSolrDocument : solrDocumentList) {
            docType = (String) holdingSolrDocument.getFieldValue("DocType");

            if ((docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) && (holdingSolrDocument.getFieldValue("instanceIdentifier").equals(id))) {
                workEHoldingsDocument = new WorkEHoldingsDocument();
                String holdingsIdentifier = (String) holdingSolrDocument.getFieldValue("id");
                String bibIdentifier = getFieldVal(holdingSolrDocument, "bibIdentifier");
                workEHoldingsDocument.setHoldingsIdentifier(holdingsIdentifier);
//                String locationName = (String) holdingSolrDocument.getFieldValue("LocationLevel_display");
                String locationName = getFieldVal(holdingSolrDocument, "Location_display");
                String instanceIdentifier = getFieldVal(holdingSolrDocument, "instanceIdentifier");
                workEHoldingsDocument.setInstanceIdentifier(instanceIdentifier);
                workEHoldingsDocument.setAccessStatus(getFieldVal(holdingSolrDocument, "AccessStatus_display"));
                workEHoldingsDocument.setSubscriptionStatus(getFieldVal(holdingSolrDocument, "SubscriptionStatus_display"));
                workEHoldingsDocument.setImprint(getFieldVal(holdingSolrDocument, "Imprint_display"));
                workEHoldingsDocument.setPlatForm(getFieldVal(holdingSolrDocument, "Platform_display"));
                workEHoldingsDocument.setLocation(getFieldVal(holdingSolrDocument, "Location_display"));
                workEHoldingsDocument.seteResourceName(getFieldVal(holdingSolrDocument, "EResource_name_display"));
                workEHoldingsDocument.setCoverageDates(getFieldValues(holdingSolrDocument,"CoverageDate_display" ));
                workEHoldingsDocument.setStaffOnly(getFieldVal(holdingSolrDocument, "staffOnlyFlag"));
                workEHoldingsDocument.setBibIdentifier(bibIdentifier);
                workEInstanceDocument.setBibIdentifier(bibIdentifier);
                workEInstanceDocument.setInstanceIdentifier(instanceIdentifier);
                workEInstanceDocument.setWorkEHoldingsDocument(workEHoldingsDocument);
            }
        }

        workEInstanceDocument.setInstanceIdentifier(id);

        return workEInstanceDocument;
    }


    private String buildQueryForBibTree(OleDocument queryDoc) {
        StringBuilder query = new StringBuilder();

        if (((WorkBibDocument) queryDoc).getId() != null && query.length() > 0) {
            query.append("(bibIdentifier :").append((((WorkBibDocument) queryDoc).getId())).append(")");
        } else if (((WorkBibDocument) queryDoc).getId() != null) {
            query.append("(bibIdentifier :").append((((WorkBibDocument) queryDoc).getId())).append(")");


        }
        return query.toString();
    }

    private String buildQueryForInstanceTree(OleDocument queryDoc) {
        StringBuilder query = new StringBuilder();

        if (((WorkInstanceDocument) queryDoc).getId() != null && query.length() > 0) {
            query.append("(instanceIdentifier :").append((((WorkInstanceDocument) queryDoc).getId())).append(")");
        } else if (((WorkInstanceDocument) queryDoc).getId() != null) {
            query.append("(instanceIdentifier :").append((((WorkInstanceDocument) queryDoc).getId())).append(")");


        }
        return query.toString();
    }

    private String buildQueryForItemTree(OleDocument queryDoc) {
        StringBuilder query = new StringBuilder();

        if (((WorkItemDocument) queryDoc).getId() != null && query.length() > 0) {
            query.append("(id :").append((((WorkItemDocument) queryDoc).getId())).append(")");
        } else if (((WorkItemDocument) queryDoc).getId() != null) {
            query.append("(id :").append((((WorkItemDocument) queryDoc).getId())).append(")");


        }
        return query.toString();
    }

    private String buildQueryForBibInfo(OleDocument queryDoc) {
        StringBuilder query = new StringBuilder();

        if (((WorkBibDocument) queryDoc).getId() != null && query.length() > 0) {
            query.append("(id :").append((((WorkBibDocument) queryDoc).getId())).append(" AND DocType:bibliographic").append(")");
        } else if (((WorkBibDocument) queryDoc).getId() != null) {
            query.append("(id :").append((((WorkBibDocument) queryDoc).getId())).append(" AND DocType:bibliographic").append(")");


        }
        return query.toString();
    }

    /**
     * @param queryDoc
     * @return query
     *         Usage: Builds query string using OleDocument Data
     */
    public String buildQueryForDoc(OleDocument queryDoc) {
        StringBuilder query = new StringBuilder();
        if ((((WorkBibDocument) queryDoc).getInstanceDocument() == null)
                || ((WorkBibDocument) queryDoc).getInstanceDocument().getInstanceIdentifier() == null) {
            if (((WorkBibDocument) queryDoc).getTitle() != null) {
                query.append("(Title_display:").append(((WorkBibDocument) queryDoc).getTitle()).append(")");
            }
            if (((WorkBibDocument) queryDoc).getAuthor() != null && query.length() > 0) {
                query.append("AND (Author_display:").append(((WorkBibDocument) queryDoc).getAuthor()).append(")");
            } else if (((WorkBibDocument) queryDoc).getAuthor() != null) {
                query.append("(Author_display:").append(((WorkBibDocument) queryDoc).getAuthor()).append(")");
            }
            if (((WorkBibDocument) queryDoc).getPublicationDate() != null && query.length() > 0) {
                query.append("AND (PublicationDate_display:").append(((WorkBibDocument) queryDoc).getPublicationDate()).append(")");
            } else if (((WorkBibDocument) queryDoc).getPublicationDate() != null) {
                query.append("(PublicationDate_display:").append(((WorkBibDocument) queryDoc).getPublicationDate()).append(")");
            }
            if (((WorkBibDocument) queryDoc).getIsbn() != null && query.length() > 0) {
                query.append("AND (ISBN_display:").append(((WorkBibDocument) queryDoc).getIsbn()).append(")");
            } else if (((WorkBibDocument) queryDoc).getIsbn() != null) {
                query.append("(ISBN_display:").append(((WorkBibDocument) queryDoc).getIsbn()).append(")");
            }
            if (((WorkBibDocument) queryDoc).getPublisher() != null && query.length() > 0) {
                query.append("AND (Publisher_display:").append(((WorkBibDocument) queryDoc).getPublisher()).append(")");
            } else if (((WorkBibDocument) queryDoc).getPublisher() != null) {
                query.append("(Publisher_display:").append(((WorkBibDocument) queryDoc).getPublisher()).append(")");
            }
            if (((WorkBibDocument) queryDoc).getId() != null && query.length() > 0) {
                query.append("(id:").append((((WorkBibDocument) queryDoc).getId())).append(")");
            } else if (((WorkBibDocument) queryDoc).getId() != null) {
                query.append("(id:").append((((WorkBibDocument) queryDoc).getId())).append(")");

            }
        } else {
            query.append("(" + DOC_TYPE + ":" + queryDoc.getDocType().getDescription() + ")");
            if (((WorkBibDocument) queryDoc).getInstanceDocument() != null) {
                query.append(" AND ");
                String instanseIdentifier = ((WorkBibDocument) queryDoc).getInstanceDocument().getInstanceIdentifier();
                query.append("(" + INSTANCE_IDENTIFIER + ":" + instanseIdentifier + ")");
            }
        }
        query.append(
                "&fl=id,instanceIdentifier,Title_display,Author_display,PublicationDate_display,ISBN_display,Publisher_display");
        return query.toString();
    }


    /**
     * @param inputQuery
     * @return hitsOnPage
     * @throws Exception Usage: Gets Solr response for input query and builds List of Maps holding Solr Doc Data
     */
    public List<Map<String, Object>> getSolrHitsForQuery(String inputQuery) throws Exception {
        SolrServer server;
        List<Map<String, Object>> hitsOnPage = new ArrayList<Map<String, Object>>();
        server = SolrServerManager.getInstance().getSolrServer();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(inputQuery);
        solrQuery.setSortField("Title_sort", SolrQuery.ORDER.asc);
        solrQuery.setIncludeScore(true);
        solrQuery.setRows(20000);
        LOG.info("solr Query -->" + solrQuery.getQuery());
        QueryResponse queryResponse = server.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();

        for (SolrDocument solrDocument : solrDocumentList) {
            hitsOnPage.add(solrDocument);
        }
        return hitsOnPage;
    }


    /**
     * Gets Solr response for search Params
     * @param searchParams
     * @return hitsOnPage
     * @throws Exception
     */
    public List<Map<String, Object>> getSolrHitsForQueryParams(SearchParams searchParams) throws Exception {
        SolrServer server;
        List<Map<String, Object>> hitsOnPage = new ArrayList<Map<String, Object>>();
        server = SolrServerManager.getInstance().getSolrServer();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(searchParams.getSearchQuery());
        if(StringUtils.isNotEmpty(searchParams.getSortField()) && StringUtils.isNotEmpty(searchParams.getSortOrder())) {
            String solrField = getSortField(searchParams.getSortField()).toString();
            if("asc".equals(searchParams.getSortOrder())) {
                solrQuery.setSortField(solrField ,SolrQuery.ORDER.asc);
            }
            else if("desc".equals(searchParams.getSortOrder())) {
                solrQuery.setSortField(solrField ,SolrQuery.ORDER.desc);
            }
        }
        else {
            solrQuery.setSortField("Title_sort", SolrQuery.ORDER.asc);
        }
        solrQuery.setIncludeScore(true);
        solrQuery.setRows(searchParams.getRows());
        solrQuery.setStart(searchParams.getStart());
        LOG.info("solr Query -->" + solrQuery.getQuery());
        QueryResponse queryResponse = server.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();

        for (SolrDocument solrDocument : solrDocumentList) {
            hitsOnPage.add(solrDocument);
        }
        return hitsOnPage;
    }


    /**
     * Gets total number of records from solr
     * @param inputQuery
     * @return numFound
     * @throws Exception
     */
    public int getSolrHitsForQueryParams(String inputQuery) throws Exception {
        SolrServer server;
        List<Map<String, Object>> hitsOnPage = new ArrayList<Map<String, Object>>();
        server = SolrServerManager.getInstance().getSolrServer();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(inputQuery);
        solrQuery.setIncludeScore(true);
        solrQuery.setRows(0);

        LOG.info("solr Query -->" + solrQuery.getQuery());
        QueryResponse queryResponse = server.query(solrQuery);
        int numFound = (int) queryResponse.getResults().getNumFound();
        return numFound;
    }


    /**
     * @param idList
     * @param identifierType
     * @return
     */
    @Override
    public List<String> getUUIDList(List<String> idList, String identifierType) {
        SolrServer solrServer;
        SolrQuery solrQuery = new SolrQuery();
        SolrDocumentList solrDocumentList;
        String id;
        StringBuilder builder = new StringBuilder();
        List<String> uuidList = new ArrayList<String>();
        String searchField = null;
        if (identifierType.equalsIgnoreCase("SCN")) {
            searchField = WorkBibCommonFields.SYSTEM_CONTROL_NUMBER + ":";
        } else if (identifierType.equalsIgnoreCase("ISBN")) {
            searchField = WorkBibCommonFields.ISBN_SEARCH + ":";
        }
        try {
            solrServer = SolrServerManager.getInstance().getSolrServer();
            for (String ssn : idList) {
                if (builder.length() > 0) {
                    builder.append(" OR ");
                }
                builder.append("(");
                builder.append(searchField);
                builder.append(ssn);
                builder.append(")");
            }

            LOG.debug("query-->" + builder.toString());
            solrQuery.setQuery(builder.toString());
            LOG.debug("solr query-->" + solrQuery);
            QueryResponse response = solrServer.query(solrQuery);
            solrDocumentList = response.getResults();
            for (SolrDocument solrDocument : solrDocumentList) {
                id = (String) solrDocument.getFieldValue("id");
                LOG.debug("id-->" + id);
                uuidList.add(id);
            }

        } catch (SolrServerException e) {

        }
        return uuidList;
    }

    /**
     * This method returns the list of titles based on the given search value.
     *
     * @param fieldValue
     * @return
     */
    @Override
    public List<String> getTitleValues(String fieldValue) {
        SolrServer solrServer;
        SolrQuery solrQuery = new SolrQuery();
        SolrDocumentList solrDocumentList;
        Object title;
        StringBuilder builder = new StringBuilder();
        List<String> titleList = new ArrayList<String>();
        String searchField = null;
        searchField = WorkBibCommonFields.TITLE_SEARCH + ":";
        try {
            solrServer = SolrServerManager.getInstance().getSolrServer();
            builder.append("(");
            builder.append("DocType");
            builder.append(":");
            builder.append("bibliographic");
            builder.append(")");
            builder.append("AND");
            builder.append("(");
            builder.append("(");
            builder.append(searchField);
            builder.append("(");
            builder.append(fieldValue);
            builder.append(")");
            builder.append(")");
            builder.append(")");
            LOG.debug("query-->" + builder.toString());
            solrQuery.setQuery(builder.toString());
            LOG.info("solr query-->" + solrQuery);
            QueryResponse response = solrServer.query(solrQuery);
            solrDocumentList = response.getResults();
            for (SolrDocument solrDocument : solrDocumentList) {
                title = solrDocument.getFieldValue("Title_search");
                LOG.debug("title-->" + title);
                if (title instanceof List) {
                    titleList.addAll((Collection<? extends String>) title);
                } else {
                    titleList.add(title.toString());
                }
            }

        } catch (SolrServerException e) {

        }
        return titleList;

    }

    public String buildQuery(SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        buildInitialQuery(query, searchParams);
        if (searchParams.getResultPageSize() != null) {
            query.append("&rows=" + searchParams.getResultPageSize());
        }
        if (searchParams.getResultFromIndex() != null) {
            query.append("&start=" + searchParams.getResultFromIndex());
        }
        query.append(buildQueryWithSortFields(searchParams.getSortField(), searchParams.getSortOrder()));
        query.append(buildQueryWithFieldListParameters(searchParams.getFieldList()));
        return query.toString();
    }

    /**
     * @param query
     * @param searchParams Usage: This method builds initial SOLR query with DocType and DocFormat as SolrParams
     */
    private void buildInitialQuery(StringBuffer query, SearchParams searchParams) {

        if (searchParams.getDocType() == null && searchParams.getSearchFieldsList().isEmpty())
            return;
        if (searchParams.getDocType() != null && searchParams.getSearchFieldsList().isEmpty()) {
            query.append("(DocType:" + searchParams.getDocType() + ")");
        } else if (searchParams.getDocType() != null && !searchParams.getSearchFieldsList().isEmpty()) {
            query.append("(DocType:" + searchParams.getDocType() + ")");
            query.append("AND");
        }

        /*  if (searchParams.getDocFormat().equalsIgnoreCase("marc")) {
            query.append("((DocType:" + searchParams.getDocType() + ")" + "OR(DocType:item))");
        }
        else {
            query.append("(DocType:" + searchParams.getDocType() + ")");
        }*/
        if (searchParams.getSearchFieldsList().isEmpty() && searchParams.getDocFormat() != null && !searchParams.getDocFormat().equalsIgnoreCase("all")) {
            query.append("AND");
            query.append("(DocFormat:" + searchParams.getDocFormat() + ")");
        } else if (searchParams.getDocFormat() != null && !searchParams.getDocFormat().equalsIgnoreCase("all")) {
            query.append("(DocFormat:" + searchParams.getDocFormat() + ")");
            query.append("AND");
        }

        if (searchParams.getSearchFieldsList().size() > 0) {
            query.append(buildQueryWithSearchParameters(searchParams.getSearchFieldsList()));
        }
    }

    public String buildQueryWithSearchParameters(List<SearchCondition> searchFieldsList) {
        SearchCondition docSearchFieldsDTO = null;
        StringBuffer queryStringbuffer = new StringBuffer();
        StringBuffer highlightBuffer = new StringBuffer("&hl.fl=");
        if (searchFieldsList != null && searchFieldsList.size() > 0) {
            queryStringbuffer.append("(");
            for (int i = 0; i < searchFieldsList.size(); i++) {
                int searchScopeAddLimit = i;
                docSearchFieldsDTO = searchFieldsList.get(i);
                if (docSearchFieldsDTO.getOperator() != null) {
                    // queryStringbuffer.append(docSearchFieldsDTO.getOperator());
                }
                queryStringbuffer.append("(");
                if (docSearchFieldsDTO.getDocField().equalsIgnoreCase("all") || (
                        docSearchFieldsDTO.getDocField() == null || docSearchFieldsDTO.getDocField().length() == 0)) {
                    queryStringbuffer.append("all_text");
                    highlightBuffer.append("*");

                } else {
                    queryStringbuffer.append(docSearchFieldsDTO.getDocField());
                    highlightBuffer.append(docSearchFieldsDTO.getDocField());

                    if (i != searchFieldsList.size() - 1) {
                        highlightBuffer.append(",");
                    }
                }
                queryStringbuffer.append(":");
                String searchScope = docSearchFieldsDTO.getSearchScope();
                String searchText = docSearchFieldsDTO.getSearchText();
                String operator = docSearchFieldsDTO.getOperator();
                if (docSearchFieldsDTO.getFieldType() != null && docSearchFieldsDTO.getFieldType().equals("range")) {
                    //do nothing to searchText
                } else {
                    if (searchText.equalsIgnoreCase("")) {
                        searchText = "(*:*)";
                    } else {
                        //commented line for jira-4802
                        //searchText = searchText.toLowerCase();
                        //searchText = searchText.replaceAll("[~!(){}\\[\\]':-]+", " ");
                        searchText=getModifiedText(searchText);

                        searchText.replaceAll(" ", "+");
                    }
                }
                LOG.debug("searchText-->" + searchText);
                String searchTextVal = null;
                if (searchText.length() > 0) {
                    queryStringbuffer.append("(");
                    if (searchScope.equalsIgnoreCase("AND")) {
                        searchText = searchText.replaceAll("\\s+", " ");
                        searchTextVal = searchText.trim().replace(" ", " AND ");
                    } else if (searchScope.equalsIgnoreCase("OR")) {
                        searchText = searchText.replaceAll("\\s+", " ");
                        searchTextVal = searchText.trim().replace(" ", " OR ");
                    } else if (searchScope.equalsIgnoreCase("phrase")) {
                        searchTextVal = "\"" + searchText + "\"";
                    } else if (searchScope.equalsIgnoreCase("none")) {
                        //do nothing to the search text
                        searchTextVal = searchText;
                    }
                    /* try {
                        searchTextVal = URLEncoder.encode(searchTextVal, "UTF-8");
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }*/
                    queryStringbuffer.append(searchTextVal);
                    LOG.debug("searchTextVal............" + searchTextVal + "........" + queryStringbuffer.toString());
                    queryStringbuffer.append(")");
                }
                queryStringbuffer.append(")");
                ++searchScopeAddLimit;
                if (operator == null) {
                    break;
                }
                if (searchScopeAddLimit != searchFieldsList.size()) {
                    queryStringbuffer.append(operator);
                }
            }
            queryStringbuffer.append(")");
            queryStringbuffer.append(highlightBuffer.toString());

            queryStringbuffer.append("&hl=true");
        }
        return queryStringbuffer.toString();
    }

    public String buildQueryWithSortFields(String sortField, String sortOrder) {
        StringBuffer sortFieldsQuery = new StringBuffer();
        if (null != sortField) {
            sortFieldsQuery.append("&");
            sortFieldsQuery.append("sort=");
            sortFieldsQuery.append(sortField);
            if (null != sortOrder) {
                sortFieldsQuery.append(" ");
                sortFieldsQuery.append(sortOrder);
            }
        }
        return sortFieldsQuery.toString();
    }

    public String buildQueryWithFieldListParameters(List<String> fieldsList) {
        String queryWithFieldListParameters = "";
        if (fieldsList != null) {
            StringBuffer fieldsListQueryStringbuffer = new StringBuffer();
            fieldsListQueryStringbuffer.append("&");
            fieldsListQueryStringbuffer.append("fl=");
            for (int i = 0; i < fieldsList.size(); i++) {
                fieldsListQueryStringbuffer.append(fieldsList.get(i));
                fieldsListQueryStringbuffer.append(",");
            }
            queryWithFieldListParameters = fieldsListQueryStringbuffer
                    .substring(0, fieldsListQueryStringbuffer.length() - 1);
        }
        return queryWithFieldListParameters;
    }

    /**
     * Prepares the solr query based on user input values location, classification and call number browse text
     * Calculates totalCallNumberCount and totalForwardCallNumberCount based on solr query results
     * Evaluates matchIndex based on totalCallNumberCount and totalForwardCallNumberCount
     *
     * @param callNumberBrowseParams contains required parameters for call number browse functionality
     * @throws Exception
     */
    public void initCallNumberBrowse(CallNumberBrowseParams callNumberBrowseParams) throws Exception {
        int matchIndex = 0;
        int totalCallNumberCount = 0;
        int totalForwardCallNumberCount = 0;
        String location = callNumberBrowseParams.getLocation();
        String classificationScheme = callNumberBrowseParams.getClassificationScheme();
        String callNumberBrowseText = callNumberBrowseParams.getCallNumberBrowseText();
        String docType = callNumberBrowseParams.getDocTye();
        if (StringUtils.isNotEmpty(classificationScheme)) {
            String queryString = buildQueryForTotalCallNumberCount(location, classificationScheme, docType);
            List<Map<String, Object>> solrHits;
            totalCallNumberCount = getSolrHitsForCallNumberBrowse(queryString);
            LOG.info("Total Call Number count:" + totalCallNumberCount);

            if (StringUtils.isNotEmpty(callNumberBrowseText)) {
                CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(classificationScheme);
                String normalizedCallNumberBrowseText = callNumber.getSortableKey(callNumberBrowseText);
                normalizedCallNumberBrowseText = normalizedCallNumberBrowseText.replaceAll(" ", "-");

                queryString = buildQueryForTotalForwardCallNumberCount(location, classificationScheme, normalizedCallNumberBrowseText, docType);
                totalForwardCallNumberCount = getSolrHitsForCallNumberBrowse(queryString);
            } else
                totalForwardCallNumberCount = totalCallNumberCount;
        }

        LOG.info("Total Forward Call Number Count:" + totalForwardCallNumberCount);
        matchIndex = (totalCallNumberCount - totalForwardCallNumberCount) + 1;
        callNumberBrowseParams.setMatchIndex(matchIndex);
        callNumberBrowseParams.setTotalCallNumberCount(totalCallNumberCount);
        callNumberBrowseParams.setTotalForwardCallNumberCount(totalForwardCallNumberCount);
    }

    /**
     * Used to display closest record based on input fields classification, rows and start index
     *
     * @param callNumberBrowseParams contains required parameters for call number browse functionality
     * @return
     * @throws Exception
     */
    public List<WorkBibDocument> browseCallNumbers(CallNumberBrowseParams callNumberBrowseParams) throws Exception {
        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
        if (StringUtils.isNotEmpty(callNumberBrowseParams.getClassificationScheme())) {
            int startIndex = callNumberBrowseParams.getStartIndex();
            if (startIndex > 0) {
                startIndex = startIndex - 1;
            }
            String queryString = buildQueryForBrowseCallNumbers(callNumberBrowseParams.getLocation(), callNumberBrowseParams.getClassificationScheme(), callNumberBrowseParams.getNumRows(), startIndex, callNumberBrowseParams.getDocTye());
            List<Map<String, Object>> solrHits;
            solrHits = getSolrHitsForCallNumberBrowse(queryString, startIndex, callNumberBrowseParams.getNumRows());
            List<WorkItemDocument> workItemDocuments = buildWorkItemDocuments(solrHits);
            buildItemCallNumber(workItemDocuments);
            workBibDocuments = getWorkBibDocuments(workItemDocuments);
            LOG.info("Before Merging");
            LOG.info("Item list size:" + workItemDocuments.size());
            LOG.info("Bib list size:" + workBibDocuments.size());
            workBibDocuments = mergeBibAndItemDocuments(workItemDocuments, workBibDocuments);
            LOG.info("After Merging");
            LOG.info("Item list size:" + workItemDocuments.size());
            LOG.info("Bib list size:" + workBibDocuments.size());
        }
        return workBibDocuments;
    }

    /**
     * Merges the item documents with corresponding Bib document.
     * The main intention is to make equal count for both Item and Bib documents.
     *
     * @param workItemDocumentList contains list of Item records
     * @param workBibDocumentList  contains list of Bib records.
     * @return
     */
    public List<WorkBibDocument> mergeBibAndItemDocuments(List<WorkItemDocument> workItemDocumentList, List<WorkBibDocument> workBibDocumentList) {

        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
        for (WorkItemDocument workItemDocument : workItemDocumentList) {
//            WorkBibDocument workBibDocument=new WorkBibDocument();
            for (WorkBibDocument workBibDoc : workBibDocumentList) {
                WorkBibDocument workBibDocument = new WorkBibDocument();
                if (workBibDoc.getId().equals(workItemDocument.getBibIdentifier())) {
                    //workBibDocument = workBibDoc;
                    workBibDocument.setAuthor(workBibDoc.getAuthor());
                    workBibDocument.setTitle(workBibDoc.getTitle());
                    workBibDocument.setId(workBibDoc.getId());
                    workBibDocument.setDocFormat(workBibDoc.getDocFormat());
                    workBibDocument.setInstanceIdentifier(workBibDoc.getInstanceIdentifier());
                    List<WorkInstanceDocument> workInstanceDocuments = new ArrayList<WorkInstanceDocument>();
                    WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                    List<WorkItemDocument> workitemDocuments = new ArrayList<WorkItemDocument>();
                    workitemDocuments.add(workItemDocument);
                    workInstanceDocument.setItemDocumentList(workitemDocuments);
                    workInstanceDocuments.add(workInstanceDocument);
                    workBibDocument.setWorkInstanceDocumentList(workInstanceDocuments);
                    workBibDocuments.add(workBibDocument);
                    break;
                }
            }

        }
        return workBibDocuments;
    }

    /**
     * Prepares a list of Bib records based on available Item records.
     *
     * @param workItemDocuments contains list of Item documents
     * @return
     * @throws Exception
     */
    public List<WorkBibDocument> getWorkBibDocuments(List<WorkItemDocument> workItemDocuments) throws Exception {
        List<WorkBibDocument> workBibDocumentList = new ArrayList<WorkBibDocument>();
        if (workItemDocuments.size() <= 0)
            return workBibDocumentList;
        String queryString = buildQueryForBib(workItemDocuments);
        List<Map<String, Object>> solrHits = getSolrHitsForQuery(queryString);
        workBibDocumentList = buildWorkBibDocuments(solrHits);
        return workBibDocumentList;
    }

    /**
     * Building solr query with list of Item records
     * The solr query is used to get Bib records.
     *
     * @param workItemDocumentList contains list of Item records
     * @return the solr query for Bib records
     */
    public String buildQueryForBib(List<WorkItemDocument> workItemDocumentList) {
        if (workItemDocumentList != null && workItemDocumentList.size() > 0) {
            StringBuilder query = new StringBuilder();
            StringBuilder idQuery = new StringBuilder();
            query.append("(DocType:bibliographic) AND");
            int i = 0;
            for (; i < workItemDocumentList.size() - 1; i++) {
                idQuery.append(("(id:" + workItemDocumentList.get(i).getBibIdentifier()) + ") OR ");
            }
            idQuery.append("(id:" + workItemDocumentList.get(i).getBibIdentifier() + ")");
            if (idQuery.length() > 0)
                query.append("(" + idQuery + ")");
            return query.toString();
        }
        return null;
    }


    /**
     * Preparing solr query based on user input fields location and classification scheme.
     *
     * @param location             contains user input location
     * @param classificationScheme user input classification
     * @return solr query for total call number count
     */
    public String buildQueryForTotalCallNumberCount(String location, String classificationScheme, String docType) {
        StringBuffer query = new StringBuffer();
        query.append("(DocCategory:work)AND(DocType:" + docType + ")AND(DocFormat:oleml)");
        if (StringUtils.isNotEmpty(location))
            query.append("AND (Location_search:" + location + ")");
        if (StringUtils.isNotEmpty(classificationScheme))
            query.append("AND(ShelvingSchemeCode_search:" + classificationScheme + ")");
        query.append("AND (ShelvingOrder_sort:{* TO *})");
        query.append("&sort=ShelvingOrder_sort asc");
        return query.toString();
    }

    /**
     * Building solr query for getting Item/Holdings records with call number
     *
     * @param location             the Item/Holdings location
     * @param classificationScheme the Item/Holdings classification
     * @param numRows              the number of rows for displaying results
     * @param startIndex           index used to specify the
     * @return the solr query for browse call numbers.
     */
    public String buildQueryForBrowseCallNumbers(String location, String classificationScheme, int numRows, int startIndex, String docType) {
        StringBuffer query = new StringBuffer();
        String queryString = buildCommonQueryForForwardCallNumber(location, classificationScheme, "", docType);
        query.append(queryString);
        query.append("&fl=");
        // query.append("CallNumber_display,ShelvingSchemeCode_display,bibIdentifier,id");
        query.append("CallNumber_display,bibIdentifier,id");
        query.append("&rows=" + numRows + "&start=" + startIndex);
        query.append("&sort=ShelvingOrder_sort asc");
        return query.toString();
    }

    /**
     * @param location             the Item/Holdings location.
     * @param classificationScheme the Item/Holdings classification.
     * @param callNumberBrowseText the user input field callNumberBrowseText.
     * @return the solr query for getting forward call number count.
     */
    public String buildQueryForTotalForwardCallNumberCount(String location, String classificationScheme, String callNumberBrowseText, String docType) {
        StringBuffer query = new StringBuffer();
        String queryString = buildCommonQueryForForwardCallNumber(location, classificationScheme, callNumberBrowseText, docType);
        query.append(queryString);
        return query.toString();
    }

    /**
     * @param location             the Item/Holdings location
     * @param classificationScheme the Item/Holdings classification
     * @param callNumberBrowseText the user input field callNumberBrowseText.
     * @return solr query
     */
    public String buildCommonQueryForForwardCallNumber(String location, String classificationScheme, String callNumberBrowseText, String docType) {
        StringBuffer query = new StringBuffer();
        //TODO: Supporting other DocType
        query.append("(DocCategory:work)AND(DocType:" + docType + ")AND(DocFormat:oleml)");
        StringBuffer locations = new StringBuffer();
        if (StringUtils.isNotEmpty(location)) {
            query.append("AND((Location_display:" + location + "*)");
            if (location.contains("-")) {
                locations.append(location.split("-")[0]);
                locations.append("*");
                locations.append(location.split("-")[1]);
                location = locations.substring(0,locations.length());
            }
            query.append("OR(Location_search:" + "" + location.toLowerCase() + "*" + "))");
        }
        if (StringUtils.isNotEmpty(classificationScheme))
            query.append("AND(ShelvingSchemeCode_search:" + classificationScheme + ")");
        if (StringUtils.isNotEmpty(callNumberBrowseText))
            query.append("AND (ShelvingOrder_sort:{\"" + callNumberBrowseText + "*\" TO *})");
        else
            query.append("AND (ShelvingOrder_sort:{* TO *})");
        return query.toString();
    }

    public List<WorkBibDocument> getWorkBibRecords(List<LinkedHashMap<String, String>> uuidsMapList) throws Exception {
        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
        for (LinkedHashMap<String, String> uuidsMap : uuidsMapList) {
            WorkBibDocument workBibDocument = new WorkBibDocument();
            if (uuidsMap.containsKey(DocType.BIB.getDescription())) {
                String bibId = uuidsMap.get(DocType.BIB.getDescription());
                if (LOG.isInfoEnabled()) {
                    LOG.info(" bibId ---------------> " + bibId);
                }
                workBibDocument.setId(bibId.toString());
                workBibDocument = queryForBibTree(workBibDocument);
            } else if (uuidsMap.containsKey(DocType.INSTANCE.getDescription())) {
                WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                List<WorkInstanceDocument> workInstanceDocuments = new ArrayList<WorkInstanceDocument>();
                String instanceId = uuidsMap.get(DocType.INSTANCE.getDescription());
                if (LOG.isInfoEnabled()) {
                    LOG.info(" instanceId ---------------> " + instanceId);
                }
                workInstanceDocument.setId(instanceId.toString());
                workInstanceDocument.setInstanceIdentifier(instanceId.toString());
                workInstanceDocument = queryForInstanceTree(workInstanceDocument);
                workInstanceDocuments.add(workInstanceDocument);
                workBibDocument.setId(workInstanceDocument.getBibIdentifier());
                workBibDocument.setWorkInstanceDocumentList(workInstanceDocuments);
                workBibDocument = queryForBibTree(workBibDocument);
            } else if (uuidsMap.containsKey(DocType.ITEM.getDescription())) {
                WorkItemDocument workItemDocument = new WorkItemDocument();
                List<WorkInstanceDocument> workInstanceDocuments = new ArrayList<WorkInstanceDocument>();
                List<WorkItemDocument> workItemDocumentList = new ArrayList<WorkItemDocument>();
                WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                String itemId = uuidsMap.get(DocType.ITEM.getDescription());
                if (LOG.isInfoEnabled()) {
                    LOG.info(" itemId ---------------> " + itemId);
                }
                workItemDocument.setId(itemId.toString());
                workItemDocument = queryForItemTree(workItemDocument);
                workItemDocumentList.add(workItemDocument);
                workInstanceDocument.setItemDocumentList(workItemDocumentList);
                workInstanceDocument.setId(workItemDocument.getInstanceIdentifier());
                //workInstanceDocument = queryForInstanceTree(workInstanceDocument);
                workInstanceDocuments.add(workInstanceDocument);
                workBibDocument.setId(workItemDocument.getBibIdentifier());
                workBibDocument.setWorkInstanceDocumentList(workInstanceDocuments);
                workBibDocument = queryForBibTree(workBibDocument);
            } else if (uuidsMap.containsKey(DocType.HOLDINGS.getDescription())) {
                WorkHoldingsDocument workHoldingsDocument = new WorkHoldingsDocument();
                List<WorkInstanceDocument> workInstanceDocuments = new ArrayList<WorkInstanceDocument>();
                WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
                String holdingsId = uuidsMap.get(DocType.HOLDINGS.getDescription());
                if (LOG.isInfoEnabled()) {
                    LOG.info(" holdingsId ---------------> " + holdingsId);
                }
                workHoldingsDocument.setHoldingsIdentifier(holdingsId);
                workInstanceDocument.setHoldingsDocument(workHoldingsDocument);
                workInstanceDocument = queryForInstanceTree(workInstanceDocument);
                workInstanceDocuments.add(workInstanceDocument);
                workBibDocument.setId(workInstanceDocument.getBibIdentifier());
                workBibDocument.setWorkInstanceDocumentList(workInstanceDocuments);
                workBibDocument = queryForBibTree(workBibDocument);
            } else if (uuidsMap.containsKey(DocType.EINSTANCE.getDescription())) {
                WorkEInstanceDocument workEInstanceDocument = new WorkEInstanceDocument();
                List<WorkEInstanceDocument> workEInstanceDocuments = new ArrayList<WorkEInstanceDocument>();
                String instanceId = uuidsMap.get(DocType.EINSTANCE.getDescription());
                if (LOG.isInfoEnabled()) {
                    LOG.info(" eInstanceId ---------------> " + instanceId);
                }
                workEInstanceDocument.setId(instanceId.toString());
                workEInstanceDocument.setInstanceIdentifier(instanceId.toString());
                workEInstanceDocument = queryForEInstanceTree(workEInstanceDocument);
                workEInstanceDocuments.add(workEInstanceDocument);
                workBibDocument.setId(workEInstanceDocument.getBibIdentifier());
                workBibDocument.setWorkEInstanceDocumentList(workEInstanceDocuments);
                workBibDocument = queryForBibTree(workBibDocument);
            }
            workBibDocuments.add(workBibDocument);
        }
        return workBibDocuments;
    }

    /**
     * @param inputQuery input query
     * @param start      start index
     * @param numRows    the number of rows for displaying results
     * @return the list of solr hits for call number browse
     * @throws Exception
     */
    public List<Map<String, Object>> getSolrHitsForCallNumberBrowse(String inputQuery, int start, int numRows) throws Exception {
        SolrServer server;
        List<Map<String, Object>> hitsOnPage = new ArrayList<Map<String, Object>>();
        server = SolrServerManager.getInstance().getSolrServer();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(inputQuery);
        solrQuery.setIncludeScore(true);
        solrQuery.setRows(numRows);
        solrQuery.setStart(start);
        solrQuery.setSortField("ShelvingOrder_sort", SolrQuery.ORDER.asc);
        LOG.info("solr Query -->" + solrQuery.getQuery());
        QueryResponse queryResponse = server.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();

        for (SolrDocument solrDocument : solrDocumentList) {
            hitsOnPage.add(solrDocument);
        }
        return hitsOnPage;
    }

    public int getSolrHitsForCallNumberBrowse(String inputQuery) throws Exception {
        SolrServer server;
        List<Map<String, Object>> hitsOnPage = new ArrayList<Map<String, Object>>();
        server = SolrServerManager.getInstance().getSolrServer();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(inputQuery);
        solrQuery.setIncludeScore(true);
        solrQuery.setRows(0);
        solrQuery.setSortField("ShelvingOrder_sort", SolrQuery.ORDER.asc);
        LOG.info("solr Query -->" + solrQuery.getQuery());
        QueryResponse queryResponse = server.query(solrQuery);
        int numFound = (int) queryResponse.getResults().getNumFound();
        return numFound;
    }

    /**
     * Verify solr for the field and return true if exists (Ingest)
     * Verify solr for the field other updating record (Update)
     *
     * @param docType
     * @param field
     * @param value
     * @param id
     * @return
     * @throws Exception
     */
    public boolean isFieldValueExists(String docType, String field, String value, String id) throws Exception {
        String queryString = "(DocType:" + docType + ")AND(" + field + ":" + value + ")";
        QueryResponse queryResponse = getSolrResponse(queryString, field);
        long i = queryResponse.getResults().getNumFound();
        // verify barcode while updating an item
        if (id != null) {
            if (i == 1) {      // verify the result record id with given id.
                SolrDocumentList solrDocumentList = queryResponse.getResults();
                for (SolrDocument solrDocument : solrDocumentList) {
                    if (!id.equalsIgnoreCase(solrDocument.getFieldValue("id").toString())) {
                        return true;   // other record is having barcode value
                    }
                }
            } else if (i > 1) {
                return true;   // other records having same barcode value.
            }
        } else if (i > 0) {    // verify barcode while ingesting an item.
            return true;
        }
        return false;
    }

    private QueryResponse getSolrResponse(String queryString, String field) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery(queryString);
        solrQuery.setRows(500);
        solrQuery.setFields("id");
        return server.query(solrQuery);
    }


    /**
     * Append 'Enumeration', 'Chronology' and 'Copy Number' to item call number based on their availability.
     *
     * @param itemDocuments
     */
    public void buildItemCallNumber(List<WorkItemDocument> itemDocuments) {
        String itemCallNumber = "";
        for (WorkItemDocument workItemDocument : itemDocuments) {
            itemCallNumber = workItemDocument.getCallNumber();
            if (StringUtils.isNotEmpty(itemCallNumber)) {
                itemCallNumber = itemCallNumber + " " + StringUtils.trimToEmpty(workItemDocument.getEnumeration()) + " "
                        + StringUtils.trimToEmpty(workItemDocument.getChronology()) + " " + StringUtils
                        .trimToEmpty(workItemDocument.getCopyNumber());

            }
            workItemDocument.setCallNumber(itemCallNumber);
        }
    }


    public List retriveResults(String queryString) {
        HttpSolrServer server = null;
        ArrayList<HashMap<String, Object>> hitsOnPage = new ArrayList<HashMap<String, Object>>();
        try {
            String serverUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.url.base") + "/bib";
            server = new HttpSolrServer(serverUrl);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        query.setIncludeScore(true);
        try {
            QueryResponse qr = server.query(query);

            SolrDocumentList sdl = qr.getResults();


            for (SolrDocument d : sdl) {
                HashMap<String, Object> values = new HashMap<String, Object>();

                for (Iterator<Map.Entry<String, Object>> i = d.iterator(); i.hasNext(); ) {
                    Map.Entry<String, Object> e2 = i.next();

                    values.put(e2.getKey(), e2.getValue());
                }

                hitsOnPage.add(values);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return hitsOnPage;
    }


    /**
     * Retrieve instance,item and bib uuid using item barcode or item uuid.
     *
     * @param itemBarcode
     * @return Map
     */
    public Map getItemDetails(String itemBarcode, String itemUUID) throws Exception {
        LOG.debug("Inside the getItemDetails method");

        HashMap itemAndTitleDetails = new HashMap();
        HashMap<String, Object> itemvalues = new HashMap<String, Object>();
        try {
            if (itemBarcode != null && !"".equals(itemBarcode)) {
                List<HashMap<String, Object>> documentList = retriveResults("ItemBarcode_display:" + itemBarcode);
                if(documentList!=null && documentList.size()>0){
                    itemvalues = documentList.get(0);
                    if (itemvalues.get("instanceIdentifier") != null) {
                        Object object = itemvalues.get("instanceIdentifier");
                        if(object instanceof List) {
                            List<String> list = (List<String>) itemvalues.get("instanceIdentifier");
                            if(list.size() > 0)
                                itemAndTitleDetails.put("instanceUuid", list.get(0));
                        }
                        else
                            itemAndTitleDetails.put("instanceUuid", (String) itemvalues.get("instanceIdentifier"));
                    }

                    if (itemvalues.get("ItemIdentifier_display") != null) {
                        Object object = itemvalues.get("ItemIdentifier_display");
                        if(object instanceof List) {
                            List<String> list = (List<String>) itemvalues.get("ItemIdentifier_display");
                            if(list.size() > 0)
                                itemAndTitleDetails.put("itemUuid", list.get(0));
                        }
                        else
                            itemAndTitleDetails.put("itemUuid", (String) itemvalues.get("ItemIdentifier_display"));
                    }

                    if (itemvalues.get("bibIdentifier") != null) {
                        Object object = itemvalues.get("bibIdentifier");
                        if(object instanceof List) {
                            List<String> list = (List<String>) itemvalues.get("bibIdentifier");
                            if(list.size() > 0)
                                itemAndTitleDetails.put("bibUuid", list.get(0));
                        }
                        else
                            itemAndTitleDetails.put("bibUuid", (String) itemvalues.get("bibIdentifier"));
                    }
                }
            } else {
                List<HashMap<String, Object>> documentList = retriveResults("id:" + itemUUID);
                if(documentList!=null && documentList.size()>0){
                    itemvalues = documentList.get(0);
                    if (itemvalues.get("instanceIdentifier") != null) {
                        Object object = itemvalues.get("instanceIdentifier");
                        if(object instanceof List) {
                            List<String> list = (List<String>) itemvalues.get("instanceIdentifier");
                            if(list.size() > 0)
                                itemAndTitleDetails.put("instanceUuid", list.get(0));
                        }
                        else
                            itemAndTitleDetails.put("instanceUuid", (String) itemvalues.get("instanceIdentifier"));
                    }

                    if (itemvalues.get("ItemIdentifier_display") != null) {
                        Object object = itemvalues.get("ItemIdentifier_display");
                        if(object instanceof List) {
                            List<String> list = (List<String>) itemvalues.get("ItemIdentifier_display");
                            if(list.size() > 0)
                                itemAndTitleDetails.put("itemUuid", list.get(0));
                        }
                        else
                            itemAndTitleDetails.put("itemUuid", (String) itemvalues.get("ItemIdentifier_display"));
                    }

                    if (itemvalues.get("bibIdentifier") != null) {
                        Object object = itemvalues.get("bibIdentifier");
                        if(object instanceof List) {
                            List<String> list = (List<String>) itemvalues.get("bibIdentifier");
                            if(list.size() > 0)
                                itemAndTitleDetails.put("bibUuid", list.get(0));
                        }
                        else
                            itemAndTitleDetails.put("bibUuid", (String) itemvalues.get("bibIdentifier"));
                    }
                }
            }
            return itemAndTitleDetails;
        } catch (Exception e) {
            LOG.error("Item barcode does not exist.", e);
            throw new Exception("Item barcode does not exist.");
        }
    }

    /**
     * Retrieve title and author from docstore using solr query.
     *
     * @param bibUuid
     * @return
     * @throws Exception
     */
    public Map getTitleAndAuthorfromBib(String bibUuid) throws Exception {
        LOG.debug("Inside the getTitleAndAuthorfromBib method");
        Map<String, String> bibInformationMap = new HashMap<String, String>();
        try {
            List<HashMap<String, Object>> bibDocumentList = QueryServiceImpl.getInstance().retriveResults("id:" + bibUuid);
            HashMap<String, Object> bibvalues = bibDocumentList.get(0);
            bibInformationMap.put("title", (String) ((ArrayList) bibvalues.get("245a")).get(0));
            if (bibvalues.get("100a") != null)
                bibInformationMap.put("author", (String) ((ArrayList) bibvalues.get("100a")).get(0));
            else
                bibInformationMap.put("author", "No Author");
            return bibInformationMap;
        } catch (Exception e) {
            LOG.error("Title does not exist.", e);
            throw new Exception("Title does not exist.");
        }
    }


    public Map<String, String> getBibInformation(String bibIdentifier, Map<String, String> searchCriteria) {

        HashMap bibDetails = new HashMap();
        String title = (String) searchCriteria.get("title");
        String author = (String) searchCriteria.get("author");
        String publisher = (String) searchCriteria.get("publisher");
        String isbn = (String) searchCriteria.get("ISBN");

        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append("(id:" + bibIdentifier + ") AND ");

        if (title != null && !title.equals(""))
            solrQuery.append("(Title_search:" + title.toLowerCase() + "*) AND ");
        if (author != null && !author.equals(""))
            solrQuery.append("(Author_search:" + author.toLowerCase() + "*) AND ");
        if (publisher != null && !publisher.equals(""))
            solrQuery.append("(Publisher_search:" + publisher.toLowerCase() + "*) AND");
        if (isbn != null && !isbn.equals(""))
            solrQuery.append("(ISBN_display:" + isbn.toLowerCase() + ") AND");

        String query = solrQuery.substring(0, solrQuery.lastIndexOf("AND"));

        List<HashMap<String, Object>> documentList = retriveResults("(DocType:bibliographic) AND (" + query + ")");
        if (documentList.size() > 0) {
            HashMap<String, Object> itemvalues = documentList.get(0);
            if (itemvalues.get("Title_display") != null)
                bibDetails.put("Title", (String) ((ArrayList) itemvalues.get("Title_display")).get(0));
            else if (itemvalues.get("Title_search") != null)
                bibDetails.put("Title", (String) ((ArrayList) itemvalues.get("Title_search")).get(0));
            if (itemvalues.get("Author_display") != null)
                bibDetails.put("Author", (String) ((ArrayList) itemvalues.get("Author_display")).get(0));
            else if (itemvalues.get("Author_search") != null)
                bibDetails.put("Author", (String) ((ArrayList) itemvalues.get("Author_search")).get(0));
            if (itemvalues.get("Publisher_display") != null)
                bibDetails.put("Publisher", (String) ((ArrayList) itemvalues.get("Publisher_display")).get(0));
            else if (itemvalues.get("Publisher_search") != null)
                bibDetails.put("Publisher", (String) ((ArrayList) itemvalues.get("Publisher_search")).get(0));
            if (itemvalues.get("ISBN_display") != null)
                bibDetails.put("ISBN", (String) ((ArrayList) itemvalues.get("ISBN_display")).get(0));
        }

        if ((title != null && !title.equals("")) || (author != null && !author.equals("")) || (publisher != null && !publisher.equals("")))
            if (bibDetails != null && bibDetails.isEmpty())
                return null;

        return bibDetails;

    }

    /**
     * This method is for item search in deliver
     *
     * @param queryString
     * @param rowSize
     * @return
     */
    public List retriveResults(String queryString, int rowSize) {
        HttpSolrServer server = null;
        ArrayList<HashMap<String, Object>> hitsOnPage = new ArrayList<HashMap<String, Object>>();
        try {
            String serverUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.url.base") + "/bib";
            server = new HttpSolrServer(serverUrl);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        query.setIncludeScore(true);
        query.setRows(rowSize);
        try {
            QueryResponse qr = server.query(query);

            SolrDocumentList sdl = qr.getResults();


            for (SolrDocument d : sdl) {
                HashMap<String, Object> values = new HashMap<String, Object>();

                for (Iterator<Map.Entry<String, Object>> i = d.iterator(); i.hasNext(); ) {
                    Map.Entry<String, Object> e2 = i.next();

                    values.put(e2.getKey(), e2.getValue());
                }

                hitsOnPage.add(values);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return hitsOnPage;
    }


    /**
     * Verifies whether the given field for the given document id has a value from the given value list.
     * Returns true if the field has a value.
     *
     * @param uuid
     * @param fieldName
     * @param fieldValueList - "," separated values
     * @return
     * @throws SolrServerException
     */
    @Override
    public boolean verifyFieldValue(String uuid, String fieldName, List<String> fieldValueList) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("id:" + uuid);
        solrQuery.setFields(fieldName);
        QueryResponse queryResponse = server.query(solrQuery);
        SolrDocumentList solrDocuments = queryResponse.getResults();

        for (String fieldValue : fieldValueList) {
            for (SolrDocument solrDocument : solrDocuments) {
                if (solrDocument.getFieldValue(fieldName) instanceof String) {
                    String fieldVal = "";
                    fieldVal = (String) solrDocument.getFieldValue(fieldName);
                    if ((fieldVal != null) && (fieldVal.equalsIgnoreCase(fieldValue))) {
                        return true;
                    }
                } else if (solrDocument.getFieldValue(fieldName) instanceof List) {
                    List<String> fieldValuesFromSolr = (List<String>) solrDocument.getFieldValue(fieldName);
                    for (String fieldVal : fieldValuesFromSolr) {
                        if (fieldVal.equalsIgnoreCase(fieldValue)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param instanceIds
     * @return
     * @throws SolrServerException
     */
    @Override
    public List<String> getItemIdsForInstanceIds(List<String> instanceIds) throws SolrServerException {

        List<String> itemIds = new ArrayList<String>();

        for (String uuid : instanceIds) {
            SolrQuery solrQuery = new SolrQuery();
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            solrQuery.setQuery("id:" + uuid);
            solrQuery.setFields("itemIdentifier");
            QueryResponse queryResponse = server.query(solrQuery);
            SolrDocumentList solrDocuments = queryResponse.getResults();
            for (SolrDocument solrDocument : solrDocuments) {
                if (solrDocument.getFieldValue("itemIdentifier") instanceof String) {
                    String fieldVal = (String) solrDocument.getFieldValue("itemIdentifier");
                    itemIds.add(fieldVal);
                } else if (solrDocument.getFieldValue("itemIdentifier") instanceof List) {
                    List<String> fieldValuesFromSolr = (List<String>) solrDocument.getFieldValue("itemIdentifier");
                    itemIds.addAll(fieldValuesFromSolr);
                }
            }
        }
        return itemIds;
    }

    @Override
    public List<WorkEHoldingsDocument> getEHoldingsDocuments(SearchParams searchParams) throws Exception {
        List<WorkEHoldingsDocument> workEHoldingsDocuments;
        List<Map<String, Object>> solrHits = getSolrHitsWithParams(searchParams);
        workEHoldingsDocuments = buildWorkEHoldingDocuments(solrHits);
        return workEHoldingsDocuments;
    }

    private List<WorkEHoldingsDocument> buildWorkEHoldingDocuments(List<Map<String, Object>> solrHits) {
        List<WorkEHoldingsDocument> workEHoldingsDocuments = new ArrayList<>();
        for (Map<String, Object> hitsOnPageItr : solrHits) {
            WorkEHoldingsDocument workEHoldingsDocument = new WorkEHoldingsDocument();
            Map map = hitsOnPageItr;
            Set keys = map.keySet();
            for (Object key : keys) {
                if (key.toString().equalsIgnoreCase("id")) {
                    workEHoldingsDocument.setHoldingsIdentifier((String) map.get(key));
                }
                if (key.toString().equalsIgnoreCase("LocalId_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setLocalId((String) value);
                    }
                }
                if (key.toString().equalsIgnoreCase("bibIdentifier")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setBibIdentifier((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setBibIdentifier(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("instanceIdentifier")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setInstanceIdentifier((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setInstanceIdentifier(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("Imprint_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setImprint((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setImprint(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("AccessStatus_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setAccessStatus((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setAccessStatus(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("Platform_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setPlatForm((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setPlatForm(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("StatisticalSearchingFullValue_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setStatisticalCode((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setStatisticalCode(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("SubscriptionStatus_display")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setSubscriptionStatus((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setSubscriptionStatus(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("bibIdentifier")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setBibIdentifier((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setBibIdentifier(list.get(0));
                    }
                }
                if (key.toString().equalsIgnoreCase("staffOnlyFlag")) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        workEHoldingsDocument.setStaffOnly((String) value);
                    } else if (value instanceof List) {
                        ArrayList<String> list = (ArrayList<String>) value;
                        workEHoldingsDocument.setStaffOnly(list.get(0));
                    }
                }


            }
            workEHoldingsDocuments.add(workEHoldingsDocument);
        }
        return workEHoldingsDocuments;
    }

    /**
     * Show additional linked Bib count based on input Bib UUID list
     *
     * @param bibUUIDList
     * @return
     */
    private String getLinkedBibCount(List<String> bibUUIDList) {
        String showBibMessage = "";
        if (bibUUIDList != null) {
            if ((bibUUIDList.size() > 1)) {
                // showBibMessage = (bibUUIDList.size() - 1) + " more Bib(s)";
                showBibMessage = "Bound with Bibs(" + (bibUUIDList.size()) + ")";
            } else if ((bibUUIDList.size() == 1)) {
                //  showBibMessage = "No more Bibs";
            }
        }
        return showBibMessage;
    }

    public List<String> getBibUuidsForBibMatchPoints(String code, String value) throws SolrServerException {
        List<String> uuids = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        String query = null;
        if ("001".equals(code)) {
            query = "id" + ":\"wbm-" + value + "\"";
        } else {
            query = code + ":\"" + value + "\"";
        }
        solrQuery.setQuery(query);
        solrQuery.setFields("id");
        QueryResponse queryResponse = server.query(solrQuery);
        SolrDocumentList solrDocuments = queryResponse.getResults();
        if (solrDocuments.size() > 0) {
            for (SolrDocument solrDoc : solrDocuments) {
                uuids.add((String) solrDoc.getFieldValue("id"));
            }
        }
        return uuids;
    }

    private String getModifiedText(String searchText) {
        StringBuffer modifiedText = new StringBuffer();
        StringCharacterIterator stringCharacterIterator = new StringCharacterIterator(searchText);
        char character = stringCharacterIterator.current();
        while (character != CharacterIterator.DONE) {

            if (character == '\\') {
                modifiedText.append("\\\\");
            } else if (character == '?') {
                modifiedText.append("\\?");
            } else if (character == '*' && StringUtils.isEmpty(modifiedText.toString())) {
                modifiedText.append("\\*");
            } else if (character == '+') {
                modifiedText.append("\\+");
            } else if (character == ':') {
                modifiedText.append("\\:");
            } else if (character == '{') {
                modifiedText.append("\\{");
            } else if (character == '}') {
                modifiedText.append("\\}");
            } else if (character == '[') {
                modifiedText.append("\\[");
            } else if (character == ']') {
                modifiedText.append("\\]");
            } else if (character == '(') {
                modifiedText.append("\\(");
            } else if (character == ')') {
                modifiedText.append("\\)");
            } else if (character == '^') {
                modifiedText.append("\\^");
            } else if (character == '~') {
                modifiedText.append("\\~");
            } else if (character == '-') {
                modifiedText.append("\\-");
            } else if (character == '!') {
                modifiedText.append("\\!");
            } else if (character == '\'') {
                modifiedText.append("\\'");
            } else if (character == '@') {
                modifiedText.append("\\@");
            } else if (character == '#') {
                modifiedText.append("\\#");
            } else if (character == '$') {
                modifiedText.append("\\$");
            } else if (character == '%') {
                modifiedText.append("\\%");
            } else {
                //the char is not a special one
                //add it to the result as is
                modifiedText.append(character);
            }
            character = stringCharacterIterator.next();
        }

        return modifiedText.toString().toLowerCase();
    }
    public List<String>  getBibDetailsForPurchaseOrderSearch(Map<String, String> searchCriteria) {

        List<String> finallist = new ArrayList<>();
        String solrQuery;

        StringBuffer query = new StringBuffer("");
        String title = ((String) searchCriteria.get("title"))!=null?searchCriteria.get("title") :"";
        String author = ((String) searchCriteria.get("author"))!=null?searchCriteria.get("author") :"";
        String issn = ((String) searchCriteria.get("issn"))!=null?searchCriteria.get("issn") :"";
        if (title.equalsIgnoreCase("")) {
            String searchText;

            searchText = "(*:*)";
            query.append("(Title_search:"+searchText+")AND");
        } else {
            String searchText;
            searchText=getModifiedText(title);
            searchText.replaceAll(" ", "+");
            String searchTextVal=null;
            if (searchText.length() > 0) {
                searchTextVal = searchText.trim().replace(" ", " AND ");
            }

            query.append("(Title_search:("+searchTextVal+"))AND");
        }
        if (author.equalsIgnoreCase("")) {
            String searchText;

            searchText = "(*:*)";
            query.append("(Author_search:"+searchText+")AND");
        } else {
            String searchText;
            searchText=getModifiedText(author);
            searchText.replaceAll(" ", "+");
            String searchTextVal=null;
            if (searchText.length() > 0) {
                searchTextVal = searchText.trim().replace(" ", " AND ");
            }
            query.append("(Author_search:("+searchTextVal+"))AND");
        }
        if (issn.equalsIgnoreCase("")) {
            String searchText;
            searchText = "(*:*)";
            query.append("(ISSN_search:"+searchText+")AND");
        } else {
            String searchText;
            searchText=getModifiedText(issn);
            String searchTextVal=null;
            searchText.replaceAll(" ", "+");
            if (searchText.length() > 0) {
                searchTextVal = searchText.trim().replace(" ", " AND ");
            }
            query.append("(ISSN_search:("+searchTextVal+"))AND");
        }


        if (!query.toString().equals("")) {
            query = new StringBuffer(query.substring(0, query.lastIndexOf("AND")));
            solrQuery=query.toString();
            int rowSize=10;
            if (searchCriteria.get("rowSize") != null)
                rowSize = Integer.parseInt(searchCriteria.get("rowSize"));
            try {
                List<HashMap<String, Object>> documentList = QueryServiceImpl.getInstance().retriveResults("(DocType:bibliographic) AND (" + solrQuery + ")", rowSize);

                for (int i = 0; i < documentList.size(); i++) {
                    HashMap<String, Object> itemvalues = documentList.get(i);
                    if (itemvalues.get("id") != null) {
                        finallist.add((String) itemvalues.get("id"));
                    }
                }

            } catch (Exception e) {

                LOG.info("Exception ------> " + e);

            }

        }
        return finallist;

    }


}