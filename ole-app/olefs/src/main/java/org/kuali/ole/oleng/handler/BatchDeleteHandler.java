package org.kuali.ole.oleng.handler;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.pojo.DeleteRecordDetails;
import org.kuali.ole.docstore.common.response.BatchDeleteFailureResponse;
import org.kuali.ole.docstore.common.response.BatchDeleteSuccessResponse;
import org.kuali.ole.docstore.common.response.DeleteFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchDeleteResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.spring.batch.BatchDeleteCallable;
import org.kuali.ole.spring.batch.BatchUtil;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.impl.Verifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by rajeshbabuk on 4/11/16.
 */
@Service("batchDeleteHandler")
public class BatchDeleteHandler extends BatchUtil {

    public Set<String> processInputFileAndGetDeletableBibIds(String rawContent, String fileType, BatchProfileMatchPoint batchProfileMatchPoint, BatchJobDetails batchJobDetails, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        Exchange exchange = new Exchange();
        exchange.add(OleNGConstants.FAILURE_RESPONSE, new ArrayList<DeleteFailureResponse>());
        if (OleNGConstants.TXT.equalsIgnoreCase(fileType)) {
            return processTextFile(rawContent, batchProfileMatchPoint, batchJobDetails, exchange, oleNGBatchDeleteResponse);
        } else if (OleNGConstants.MARC.equalsIgnoreCase(fileType)) {
            return processMarcFile(rawContent, batchProfileMatchPoint, batchJobDetails, exchange, oleNGBatchDeleteResponse);
        }
        return new HashSet<>();
    }

    private Set<String> processTextFile(String rawContent, BatchProfileMatchPoint batchProfileMatchPoint, BatchJobDetails batchJobDetails, Exchange exchange, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        try {
            String matchPoint = getMatchPoint(batchProfileMatchPoint);
            List<String> matchPointValues = IOUtils.readLines(IOUtils.toInputStream(rawContent));
            oleNGBatchDeleteResponse.setTotalNumberOfRecords(matchPointValues.size());
            batchJobDetails.setTotalRecords(String.valueOf(matchPointValues.size()));
            updateBatchJob(batchJobDetails);
            removeDuplicates(matchPoint, matchPointValues, oleNGBatchDeleteResponse);
            return getBibIdsToBeDeletedForTextFile(matchPoint, matchPointValues, batchJobDetails, exchange, oleNGBatchDeleteResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    private Set<String> processMarcFile(String rawContent, BatchProfileMatchPoint batchProfileMatchPoint, BatchJobDetails batchJobDetails, Exchange exchange, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        List<Record> records = getMarcXMLConverter().convertRawMarchToMarc(rawContent);
        oleNGBatchDeleteResponse.setTotalNumberOfRecords(records.size());
        batchJobDetails.setTotalRecords(String.valueOf(records.size()));
        updateBatchJob(batchJobDetails);
        return getBibIdsToBeDeletedForMarcFile(batchProfileMatchPoint, records, batchJobDetails, exchange, oleNGBatchDeleteResponse);
    }

    public Set<String> getBibIdsToBeDeletedForTextFile(String matchPoint, List<String> matchPointValues, BatchJobDetails batchJobDetails, Exchange exchange, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        Set<String> deletableBibIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(matchPointValues)) {
            Map batchDeleteMap = null;
            for (String matchPointValue : matchPointValues) {
                String bibId = null;
                try {
                    batchDeleteMap = performMatchAndGetBibId(buildQueryFromMatchPointData(matchPoint, matchPointValue));
                    if (StringUtils.isNotBlank((String) batchDeleteMap.get(OleNGConstants.FAILURE))) {
                        String failureMessage = (String) batchDeleteMap.get(OleNGConstants.FAILURE);
                        bibId = (String) batchDeleteMap.get(OleNGConstants.FAILED_BIB_ID);
                        oleNGBatchDeleteResponse.addFailureRecord(matchPoint, matchPointValue, bibId, failureMessage);
                    } else {
                        bibId = (String) batchDeleteMap.get(OleNGConstants.SUCCESS);
                        deletableBibIds.add(bibId);
                        //oleNGBatchDeleteResponse.addSuccessRecord(matchPoint, matchPointValue, bibId, OleNGConstants.SUCCESS);
                        oleNGBatchDeleteResponse.addBibRecordToMap(null, matchPoint, matchPointValue, bibId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    oleNGBatchDeleteResponse.addFailureRecord(matchPoint, matchPointValue, bibId, e.getMessage());
                    addBatchDeleteFailureResponseToExchange(e, matchPointValue, bibId, exchange);
                }
            }
        }
        oleNGBatchDeleteResponse.getDeleteFailureResponseList().addAll((List<DeleteFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE));
        batchJobDetails.setTotalFailureRecords(String.valueOf(oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().size()));
        batchJobDetails.setTotalRecordsProcessed(String.valueOf(oleNGBatchDeleteResponse.getBatchDeleteSuccessResponseList().size() + oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().size()));
        updateBatchJob(batchJobDetails);
        return deletableBibIds;
    }

    public Set<String> getBibIdsToBeDeletedForMarcFile(BatchProfileMatchPoint batchProfileMatchPoint, List<Record> records, BatchJobDetails batchJobDetails, Exchange exchange, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        String matchPoint = getMatchPoint(batchProfileMatchPoint);
        String sourceMatchPoint = getSourceMatchPoint(batchProfileMatchPoint);
        if (StringUtils.isBlank(sourceMatchPoint)) {
            sourceMatchPoint = matchPoint;
        }
        Set<String> deletableBibIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(records)) {
            Map batchDeleteMap = null;
            for (Record record : records) {
                String matchPointValue = null;
                String bibId = null;
                try {
                    matchPointValue = getMatchPointValueFromMarc(sourceMatchPoint, record);
                    if (StringUtils.isBlank(matchPointValue)) {
                        oleNGBatchDeleteResponse.addFailureRecord(matchPoint, matchPointValue, bibId, OleNGConstants.ERR_NO_MATCH_POINT);
                        oleNGBatchDeleteResponse.getFailureMarcRecords().add(record);
                    }
                    batchDeleteMap = performMatchAndGetBibId(buildQueryFromMatchPointData(matchPoint, matchPointValue));
                    if (StringUtils.isNotBlank((String) batchDeleteMap.get(OleNGConstants.FAILURE))) {
                        String failureMessage = (String) batchDeleteMap.get(OleNGConstants.FAILURE);
                        bibId = (String) batchDeleteMap.get(OleNGConstants.FAILED_BIB_ID);
                        oleNGBatchDeleteResponse.addFailureRecord(matchPoint, matchPointValue, bibId, failureMessage);
                        oleNGBatchDeleteResponse.getFailureMarcRecords().add(record);
                    } else {
                        bibId = (String) batchDeleteMap.get(OleNGConstants.SUCCESS);
                        deletableBibIds.add(bibId);
                        //oleNGBatchDeleteResponse.addSuccessRecord(matchPoint, matchPointValue, bibId, OleNGConstants.SUCCESS);
                        //oleNGBatchDeleteResponse.getSuccessMarcRecords().add(record);
                        oleNGBatchDeleteResponse.addBibRecordToMap(record, matchPoint, matchPointValue, bibId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    oleNGBatchDeleteResponse.addFailureRecord(matchPoint, matchPointValue, bibId, e.toString());
                    oleNGBatchDeleteResponse.getFailureMarcRecords().add(record);
                    addBatchDeleteFailureResponseToExchange(e, matchPointValue, bibId, exchange);
                }
            }
        }
        oleNGBatchDeleteResponse.getDeleteFailureResponseList().addAll((List<DeleteFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE));
        batchJobDetails.setTotalFailureRecords(String.valueOf(oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().size()));
        batchJobDetails.setTotalRecordsProcessed(String.valueOf(oleNGBatchDeleteResponse.getBatchDeleteSuccessResponseList().size() + oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().size()));
        updateBatchJob(batchJobDetails);
        return deletableBibIds;
    }

    private Map performMatchAndGetBibId(String query) {
        Map batchDeleteMap = new HashMap();
        SolrDocumentList solrDocumentList = getSolrRequestReponseHandler().getSolrDocumentList(query);
        if (solrDocumentList.getNumFound() == 0) {
            batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_BIB_NOT_FOUND);
            return batchDeleteMap;
        } else if (solrDocumentList.getNumFound() > 1) {
            batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_MULTIPLE_BIBS_FOUND);
            return batchDeleteMap;
        } else if (solrDocumentList.getNumFound() == 1) {
            SolrDocument solrDocument = solrDocumentList.get(0);
            String bibId = (String) solrDocument.getFirstValue(OleNGConstants.ID);
            if (verifyIfDeletable(bibId, batchDeleteMap)) {
                batchDeleteMap.put(OleNGConstants.SUCCESS, bibId);
                return batchDeleteMap;
            } else {
                batchDeleteMap.put(OleNGConstants.FAILED_BIB_ID, bibId);
                return batchDeleteMap;
            }
        }
        return batchDeleteMap;
    }

    private String getMatchPointValueFromMarc(String matchPoint, Record record) {
        String matchPointValue = null;
        if (Verifier.isControlField(matchPoint)) {
            for (ControlField controlField : record.getControlFields()) {
                if (controlField.getTag().equalsIgnoreCase(matchPoint)) {
                    matchPointValue = controlField.getData();
                    break;
                }
            }
        } else {
            if (matchPoint.length() == 4) {
                char matchPointSubField = matchPoint.substring(3, 4).toCharArray()[0];
                for (DataField dataField : record.getDataFields()) {
                    if (dataField.getTag().equalsIgnoreCase(matchPoint.substring(0, 3))) {
                        matchPointValue = dataField.getSubfield(matchPointSubField).getData();
                        break;
                    }
                }
            }
        }
        return matchPointValue;
    }

    public void performDeletion(Set<String> deletableBibIds, BatchJobDetails batchJobDetails, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        int chunkSize = getBatchChunkSize();
        List<String> ids = new ArrayList<>(deletableBibIds);
        List<List<String>> tempBibIdLists = Lists.partition(ids, chunkSize);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (List<String> tempBibIdList : tempBibIdLists) {
            Future future = executorService.submit(new BatchDeleteCallable(buildJsonArray(tempBibIdList), getOleDsNgRestClient()));
            prepareBatchDeleteResponse(future, batchJobDetails, oleNGBatchDeleteResponse);
        }
        executorService.shutdown();
    }

    private void prepareBatchDeleteResponse(Future future, BatchJobDetails batchJobDetails, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        try {
            Object object = future.get();
            if (null != object && object instanceof String) {
                Map<String, DeleteRecordDetails> bibRecordsMap = oleNGBatchDeleteResponse.getBibRecordsMap();
                OleNGBatchDeleteResponse batchDeleteResponse = getObjectMapper().readValue((String) object, OleNGBatchDeleteResponse.class);
                updateBatchDeleteSuccessResponse(bibRecordsMap, batchDeleteResponse, oleNGBatchDeleteResponse);
                updateBatchDeleteFailureResponse(bibRecordsMap, batchDeleteResponse, oleNGBatchDeleteResponse);
                updateDeleteFailureMessages(bibRecordsMap, batchDeleteResponse, oleNGBatchDeleteResponse);
                batchJobDetails.setTotalFailureRecords(String.valueOf(oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().size()));
                batchJobDetails.setTotalRecordsProcessed(String.valueOf(oleNGBatchDeleteResponse.getBatchDeleteSuccessResponseList().size() + oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().size()));
                updateBatchJob(batchJobDetails);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDeleteFailureMessages(Map<String, DeleteRecordDetails> bibRecordsMap, OleNGBatchDeleteResponse batchDeleteResponse, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        List<DeleteFailureResponse> deleteFailureResponseList = batchDeleteResponse.getDeleteFailureResponseList();
        if (CollectionUtils.isNotEmpty(deleteFailureResponseList)) {
            for (DeleteFailureResponse deleteFailureResponse : deleteFailureResponseList) {
                DeleteRecordDetails deleteRecordDetails = bibRecordsMap.get(deleteFailureResponse.getFailedBibId());
                if (null != deleteRecordDetails) {
                    deleteFailureResponse.setFailedMatchPointData(deleteRecordDetails.getMatchPoint());
                }
                oleNGBatchDeleteResponse.getDeleteFailureResponseList().add(deleteFailureResponse);
            }
        }
    }

    private void updateBatchDeleteFailureResponse(Map<String, DeleteRecordDetails> bibRecordsMap, OleNGBatchDeleteResponse batchDeleteResponse, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        List<BatchDeleteFailureResponse> batchDeleteFailureResponses = batchDeleteResponse.getBatchDeleteFailureResponseList();
        if (CollectionUtils.isNotEmpty(batchDeleteFailureResponses)) {
            for (BatchDeleteFailureResponse deleteFailureResponse : batchDeleteFailureResponses) {
                DeleteRecordDetails deleteRecordDetails = bibRecordsMap.get(deleteFailureResponse.getBibId());
                if (null != deleteRecordDetails) {
                    deleteFailureResponse.setMatchPoint(deleteRecordDetails.getMatchPoint());
                    deleteFailureResponse.setMatchPointValue(deleteRecordDetails.getMatchPointValue());
                    if (null != deleteRecordDetails.getRecord()) {
                        oleNGBatchDeleteResponse.getFailureMarcRecords().add(deleteRecordDetails.getRecord());
                    }
                }
                oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().add(deleteFailureResponse);
            }
        }
    }

    private void updateBatchDeleteSuccessResponse(Map<String, DeleteRecordDetails> bibRecordsMap, OleNGBatchDeleteResponse batchDeleteResponse, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        List<BatchDeleteSuccessResponse> batchDeleteSuccessResponses = batchDeleteResponse.getBatchDeleteSuccessResponseList();
        if (CollectionUtils.isNotEmpty(batchDeleteSuccessResponses)) {
            for (BatchDeleteSuccessResponse deleteSuccessResponse : batchDeleteSuccessResponses) {
                DeleteRecordDetails deleteRecordDetails = bibRecordsMap.get(deleteSuccessResponse.getBibId());
                if (null != deleteRecordDetails) {
                    deleteSuccessResponse.setMatchPoint(deleteRecordDetails.getMatchPoint());
                    deleteSuccessResponse.setMatchPointValue(deleteRecordDetails.getMatchPointValue());
                    if (null != deleteRecordDetails.getRecord()) {
                        oleNGBatchDeleteResponse.getSuccessMarcRecords().add(deleteRecordDetails.getRecord());
                    }
                }
                oleNGBatchDeleteResponse.getBatchDeleteSuccessResponseList().add(deleteSuccessResponse);
            }
        }
    }

    private JSONArray buildJsonArray(List<String> tempBibIdList) {
        JSONArray jsonArray = new JSONArray();
        if (CollectionUtils.isNotEmpty(tempBibIdList)) {
            for (String bibId : tempBibIdList) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(OleNGConstants.ID, bibId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray;
    }

    private String buildQueryFromMatchPointData(String matchPoint, String matchPointValue) {
        if (OleNGConstants.TAG_001.equalsIgnoreCase(matchPoint)) {
            return OleNGConstants.CONTROL_FIELD_ + matchPoint + OleNGConstants.COLON + "(" + "\"" + matchPointValue + "\"" + ")";
        } else {
            return OleNGConstants.MDF_ + matchPoint + OleNGConstants.COLON + "(" + "\"" + matchPointValue + "\"" + ")";
        }
    }

    private boolean verifyIfDeletable(String bibId, Map batchDeleteMap) {
        SolrDocumentList holdingsSolrDocumentList = new SolrDocumentList();
        SolrDocumentList itemSolrDocumentList = new SolrDocumentList();
        if (checkForAcquisitionAndDeliverLinks(bibId, itemSolrDocumentList, batchDeleteMap)) {
            return false;
        }else if (checkForMultipleHoldings(bibId, holdingsSolrDocumentList, batchDeleteMap)) {
            return false;
        } else if (checkForBoundWithAndSeries(bibId, holdingsSolrDocumentList, batchDeleteMap)) {
            return false;
        } else if (checkForAnalyticItem(bibId, itemSolrDocumentList, batchDeleteMap)) {
            return false;
        }
        return true;
    }

    private boolean checkForMultipleHoldings(String bibId, SolrDocumentList holdingsSolrDocumentList, Map batchDeleteMap) {
        int holdingsCount = 0;
        int eHoldingsCount = 0;
        holdingsSolrDocumentList.addAll(getSolrDocsForBibIdByDocType(bibId, DocType.HOLDINGS.getCode()));
        if (holdingsSolrDocumentList.size() > 0) {
            for (SolrDocument holdingsSolrDocument : holdingsSolrDocumentList) {
                if (OleNGConstants.HOLDINGS.equalsIgnoreCase((String) holdingsSolrDocument.getFieldValue(OleNGConstants.DOCTYPE))) {
                    holdingsCount++;
                    if (holdingsCount > 1) {
                        batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_MULTIPLE_HOLDINGS_FOUND);
                        return true;
                    }
                }
            }
        }

        holdingsSolrDocumentList.addAll(getSolrDocsForBibIdByDocType(bibId, DocType.EHOLDINGS.getCode()));
        if (holdingsSolrDocumentList.size() > 0) {
            for (SolrDocument holdingsSolrDocument : holdingsSolrDocumentList) {
                if (OleNGConstants.E_HOLDINGS.equalsIgnoreCase((String) holdingsSolrDocument.getFieldValue(OleNGConstants.DOCTYPE))) {
                    eHoldingsCount++;
                    if (eHoldingsCount > 1) {
                        batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_MULTIPLE_EHOLDINGS_FOUND);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkForBoundWithAndSeries(String bibId, SolrDocumentList holdingsSolrDocumentList, Map batchDeleteMap) {
        if (holdingsSolrDocumentList.size() > 0) {
            for (SolrDocument holdingsSolrDocument : holdingsSolrDocumentList) {
                if (holdingsSolrDocument.containsKey(OleNGConstants.IS_BOUNDWITH)) {
                    boolean isBoundWith = (Boolean) holdingsSolrDocument.getFieldValue(OleNGConstants.IS_BOUNDWITH);
                    if (isBoundWith) {
                        batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_BIB_BOUND_WITH);
                        return true;
                    }
                }
                if (holdingsSolrDocument.containsKey(OleNGConstants.IS_SERIES)) {
                    boolean isSeries = (Boolean) holdingsSolrDocument.getFieldValue(OleNGConstants.IS_SERIES);
                    if (isSeries) {
                        batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_BIB_HAS_SERIES_HOLDING);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private SolrDocumentList getSolrDocsForBibIdByDocType(String solrBibId, String docType) {
        String query = "(DocType:" + docType + ")AND(bibIdentifier:" + DocumentUniqueIDPrefix.getPrefixedId(OleNGConstants.BIB_PREFIX, solrBibId) + ")";
        return getSolrRequestReponseHandler().getSolrDocumentList(query);
    }

    private boolean checkForAnalyticItem(String bibId, SolrDocumentList itemSolrDocumentList, Map batchDeleteMap) {
        itemSolrDocumentList.addAll(getSolrDocsForBibIdByDocType(bibId, DocType.ITEM.getCode()));
        if (itemSolrDocumentList.size() > 0) {
            for (SolrDocument itemSolrDocument : itemSolrDocumentList) {
                if (itemSolrDocument.containsKey(OleNGConstants.IS_ANALYTIC)) {
                    boolean isAnalytic = (Boolean) itemSolrDocument.getFieldValue(OleNGConstants.IS_ANALYTIC);
                    if (isAnalytic) {
                        batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_BIB_HAS_ANALYTIC_ITEM);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkForAcquisitionAndDeliverLinks(String bibId, SolrDocumentList itemSolrDocumentList, Map batchDeleteMap) {
        bibId = DocumentUniqueIDPrefix.getPrefixedId(OleNGConstants.BIB_PREFIX, bibId);
        Map<String, String> map = new HashMap<>();
        map.put(OLEConstants.BIB_ID, bibId);
        List<OleCopy> oleCopies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, map);
        if (oleCopies.size() > 0) {
            batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_BIB_HAS_REQ_OR_PO);
            return true;
        } else {
            List<String> itemIds = getItemIdsFromSolrDocs(itemSolrDocumentList);
            if (CollectionUtils.isNotEmpty(itemIds)) {
                for (String itemId : itemIds) {
                    map.clear();
                    map.put(OleNGConstants.ITEM_UUID, itemId);
                    List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, map);
                    if (oleLoanDocuments.size() > 0) {
                        batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_BIB_ITEM_LOANED);
                        return true;
                    } else {
                        map.clear();
                        map.put(OleNGConstants.ITEM_UUID, itemId);
                        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, map);
                        if (oleDeliverRequestBos.size() > 0) {
                            batchDeleteMap.put(OleNGConstants.FAILURE, OleNGConstants.ERR_BIB_ITEM_REQUESTED);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<String> getItemIdsFromSolrDocs(SolrDocumentList itemSolrDocumentList) {
        List<String> itemIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(itemSolrDocumentList)) {
            for (SolrDocument solrDocument : itemSolrDocumentList) {
                itemIds.addAll((List) solrDocument.getFieldValues(OleNGConstants.ITEM_IDENTIFIER));
            }
        }
        return itemIds;
    }
}
