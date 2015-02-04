package org.kuali.ole.docstore.discovery.service;

import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.docstore.discovery.model.CallNumberBrowseParams;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.ole.docstore.model.bo.*;
import org.kuali.ole.pojo.OLESerialReceivingRecord;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tirumalesh.b
 * Date: 16/1/12 Time: 12:46 PM
 */
public interface QueryService {
    public List<OleDocument> queryForDocs(OleDocument queryDoc) throws Exception;

    public String queryField(SearchParams searchParams, String fieldName) throws Exception;

    public String buildQueryForDoc(OleDocument queryDoc);

    public List<String> getUUIDList(List<String> idList, String identifierType);

    public List<String> getTitleValues(String fieldValue);

    List<WorkBibDocument> getBibDocuments(SearchParams searchParams) throws Exception;

    List<WorkItemDocument> getItemDocuments(SearchParams searchParams) throws Exception;

    List<WorkHoldingsDocument> getHoldingDocuments(SearchParams searchParams) throws Exception;

    List<OLESerialReceivingRecord> getOleSerialReceivingRecords(SearchParams searchParams) throws Exception;

    List<String> getBibDetailsForPurchaseOrderSearch(Map<String, String> searchCriteria);

    public WorkBibDocument queryForBibTree(WorkBibDocument workBibDocument) throws Exception;

    public List<WorkBibDocument> queryForBibTree(List<WorkBibDocument> workBibDocumentList) throws SolrServerException, Exception;

    public WorkBibDocument buildBibDocumentInfo(WorkBibDocument bibDocument) throws SolrServerException, IOException;

    public WorkInstanceDocument queryForInstanceTree(WorkInstanceDocument instanceDocument) throws SolrServerException;

    public WorkItemDocument queryForItemTree(WorkItemDocument itemDocument) throws SolrServerException;

    public List<String> queryForBibs(String uuid) throws SolrServerException;

    public String queryForBib(String uuid) throws SolrServerException;

    public void initCallNumberBrowse(CallNumberBrowseParams callNumberBrowseParams) throws Exception;

    public List<WorkBibDocument> getWorkBibRecords(List<LinkedHashMap<String, String>> uuidsMapList) throws Exception;

    public List<WorkBibDocument> browseCallNumbers(CallNumberBrowseParams callNumberBrowseParams) throws Exception;

    boolean isFieldValueExists(String docType, String solrField, String fieldValue, String id) throws Exception;

    public List<String> queryForInstances(String uuid) throws SolrServerException;

    public List retriveResults(String queryString);

    public Map getItemDetails(String itemBarcode, String itemUUID) throws Exception;

    public Map getTitleAndAuthorfromBib(String bibUuid) throws Exception;

    public Map<String, String> getBibInformation(String bibIdentifier, Map<String, String> searchCriteria);

    public List retriveResults(String queryString, int rowSize);

    public boolean verifyFieldValue(String uuid, String fieldValue, List<String> fieldValueList) throws SolrServerException;

    public List<String> getItemIdsForInstanceIds(List<String> instanceIds) throws SolrServerException;

    public String buildQuery(SearchParams params);

    public List<WorkEHoldingsDocument> getEHoldingsDocuments(SearchParams searchParams) throws Exception;

    public WorkEInstanceDocument queryForEInstanceTree(WorkEInstanceDocument eInstanceDocument) throws SolrServerException;

    public List<String> getBibUuidsForBibMatchPoints(String code, String value) throws SolrServerException;

    public List<String> queryForItems(String bibId) throws SolrServerException;

    public List getDocuments(SearchParams searchParams) throws Exception;

}
