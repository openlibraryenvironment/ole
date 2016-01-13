package org.kuali.ole.batch.impl;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.service.BatchProcessDeleteService;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/15/13
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessDeleteServiceImpl implements BatchProcessDeleteService {

    private static final Logger LOG = Logger.getLogger(BatchProcessDeleteServiceImpl.class);
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    /**
     * This method performs to delete the bib records by using list of bib UUid values
     * @param docBibIds
     * @param profileField
     * @return
     * @throws Exception
     */
    @Override
    public int performBatchDelete(List docBibIds, String profileField) throws Exception {
        int count = docBibIds.size();
        List<Response> responseList = null;
        try {
            // Remove duplicates from docBibIds
            List<String> docBibIdList = new ArrayList<>();
            if (docBibIds != null && docBibIds.size() > 0)
                for (int i = 0; i < docBibIds.size(); i++) {
                    String bibIdDelete = (String) docBibIds.get(i);
                    if (!docBibIdList.contains(bibIdDelete)) {
                        docBibIdList.add(bibIdDelete);
                    }
                }
            deleteBatch(docBibIdList);
        } catch (Exception e) {
            count = 0;
        }
        return count;
    }

    private void deleteBatch(List<String> docBibIdList) throws Exception {
        List<List<String>> tempBibIdLists = Lists.partition(docBibIdList, 300);
        List<Future> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (List<String> tempBibIdList : tempBibIdLists) {
            futures.add(executorService.submit(new BatchprocessDelete(tempBibIdList, getDocstoreClientLocator().getDocstoreClient())));
        }
        executorService.shutdown();
    }

    /**
     * This method return list of item UUids by using bib UUid value
     * @param bibId
     * @return
     * @throws Exception
     */
    public List getItemIdList(String bibId) throws Exception {
        List itemIdList = new ArrayList(0);
        List<Item> items = new ArrayList<>();
        //itemIdList = getDocstoreHelperService().getItemIdList(bibId);
        BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
        List<HoldingsTree> holdingsTreeList = bibTree.getHoldingsTrees();
        for(HoldingsTree holdingsTree : holdingsTreeList){
            items.addAll(holdingsTree.getItems());
        }
        for(Item item : items){
            itemIdList.add(item.getId());
        }
        return itemIdList;
    }

    /**
     * This method perform to fetch the record from docstore by using file data and profile field value and validate the bib record is using in any requisition and loan and boundwith
     *
     * @param searchMrcFieldData
     * @param profileFiled
     * @return
     * @throws Exception
     */
    public Map getBibIdsForBatchDelete(String searchMrcFieldData, String profileFiled) throws Exception {
        Map batchDeleteMap = new HashMap();
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        searchMrcFieldData = searchMrcFieldData.replace("\"", "'");
        Map batchMap = getBibIdsForBatchDeleteWithSearchData(searchMrcFieldData, profileFiled);
        String failureInfo = (String) batchMap.get(OLEConstants.OLEBatchProcess.FAILURE_INFO);
        if (failureInfo != null) {
            return batchMap;
        }
        List bibIdList = (List)batchMap.get(OLEConstants.BIB_SEARCH);
        if (bibIdList != null && bibIdList.size() == 0) {
            batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.BIB_REC_NOT_FOUND);
            return batchDeleteMap;
        } else if (bibIdList != null && bibIdList.size() == 1) {
            Map<String, String> map = new HashMap<>();
            map.put(OLEConstants.BIB_ID, (String) bibIdList.get(0));
            List<OleCopy> listOfValues = (List<OleCopy>) boService.findMatching(OleCopy.class, map);
            if (listOfValues.size() != 0) {
                batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.LINK_REQ_PO);
                return batchDeleteMap;
            } else {
                List<String> itemIdList = getItemIdList((String) bibIdList.get(0));
                for (String itemId : itemIdList) {
                    Map<String, String> itemMap = new HashMap<>();
                    itemMap.put("itemUuid", itemId);
                    List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) boService.findMatching(OleLoanDocument.class, itemMap);
                    if (oleLoanDocuments.size() != 0) {
                        batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.ITEM_LOANED);
                        return batchDeleteMap;
                    } else {
                        Map<String, String> reqMap = new HashMap<>();
                        reqMap.put("itemUuid", itemId);
                        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) boService.findMatching(OleDeliverRequestBo.class, reqMap);
                        if (oleDeliverRequestBos.size() != 0) {
                            batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.ITEM_ONHOLD);
                            return batchDeleteMap;
                    }
                }
            }
            }
        } else {
            batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.MORE_BIB_RECORDS);
            return batchDeleteMap;
        }

        batchDeleteMap.put(OLEConstants.BIB_SEARCH, bibIdList);
        return batchDeleteMap;
    }

    public Map getBibIdsForBatchDeleteWithSearchData(String searchData, String dataField) {

        Map batchDeleteMap = new HashMap();
        List bibIDsList = new ArrayList(0);
        SearchParams searchParams = new SearchParams();
        SearchCondition searchCondition = new SearchCondition();
        SearchField searchField = new SearchField();
        try {
            searchField.setDocType("bibliographic");
            if (OLEConstants.OLEBatchProcess.CONTROL_FIELD_001.equals(dataField)) {
                dataField = OLEConstants.OLEBatchProcess.CONTROL_FIELD_NAME_001;
            } else {
                dataField = OLEConstants.PREFIX_FOR_DATA_FIELD + dataField;
            }
            searchField.setFieldName(dataField);
            searchField.setFieldValue(searchData);
            searchCondition.setSearchField(searchField);
            searchCondition.setSearchScope("phrase");
            searchParams.getSearchConditions().add(searchCondition);
            //List solrHits = QueryServiceImpl.getInstance().retriveResults(solrQuery);
            SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            if(null != searchResponse && null != searchResponse.getSearchResults()){
                if(searchResponse.getSearchResults().size() == 0){
                    batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.BIB_REC_NOT_FOUND);
                    return batchDeleteMap;
                } else if(searchResponse.getSearchResults().size() > 1){
                    batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.MORE_BIB_RECORDS);
                    return batchDeleteMap;
                }
                String bibId = null;
                for(SearchResult searchResult : searchResponse.getSearchResults()){
                    List<String> holdingsIdentifierList = new ArrayList();
                    List<String> eHoldingsIdentifierList = new ArrayList();
                    List<String> holdingsIdentifierListForHoldings = new ArrayList();
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                            holdingsIdentifierList.add(searchResultField.getFieldValue());
                        }
                        if(searchResultField.getFieldName().equalsIgnoreCase("id")){
                            bibId = searchResultField.getFieldValue();
                        }
                    }
                    if(holdingsIdentifierList.size()>1){
                        //Modified code for jira 5641
                        for (Object holdingsObject : holdingsIdentifierList) {
                            String holdingsId = holdingsObject.toString();
                            Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsId);
                            if ("electronic".equalsIgnoreCase(holdings.getHoldingsType()) && (!holdings.isBoundWithBib())) {
                                eHoldingsIdentifierList.add(holdings.getId());
                            } else if("print".equalsIgnoreCase(holdings.getHoldingsType()) && (!holdings.isBoundWithBib())){
                                holdingsIdentifierListForHoldings.add(holdings.getId());
                            } else{
                            }

                        }
                    }
                    //Modified code for jira 5641
                    if (holdingsIdentifierListForHoldings.size() > 1) {
                        batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.MORE_HOLDINGS);
                        return batchDeleteMap;
                    }
                    if (eHoldingsIdentifierList.size() > 1) {
                        batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.MORE_EHOLDINGS);
                        return batchDeleteMap;
                    }
                    // Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsIdentifierList.get(0));
                    BibTree bibTree = new BibTree();
                    if (bibId != null) {
                        bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
                    }
                    List<HoldingsTree> hodingsTreeList = bibTree.getHoldingsTrees();
                    if (hodingsTreeList.size() > 0) {
                        for (HoldingsTree holdingsTree : hodingsTreeList) {
                            Boolean boundWithBibFlag = holdingsTree.getHoldings().isBoundWithBib();
                            if (boundWithBibFlag && holdingsTree.getHoldings().getBib() != null) {
                                batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.BIB_BOUNDS_WITH);
                                return batchDeleteMap;
                            }
                            /*batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.BIB_BOUNDS_WITH);
                            return batchDeleteMap;*/
                        }
                    }

                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if(searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")){
                            bibIDsList.add(searchResultField.getFieldValue());
                        }
                    }
                }
                batchDeleteMap.put(OLEConstants.BIB_SEARCH, bibIDsList);
            }

        } catch (Exception e) {
            //e.printStackTrace();
            LOG.error("getBibIdsForBatchDelete Exception:" + e);
        }
        return batchDeleteMap;
    }

}