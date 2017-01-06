package org.kuali.ole.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleReceiptStatus;
import org.kuali.ole.describe.form.SerialsReceivingRecordForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.pojo.OLESerialReceivingRecord;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.service.SerialReceivingSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 4/3/14
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerialReceivingSearchServiceImpl implements SerialReceivingSearchService {

    private static final Logger LOG = Logger.getLogger(SerialReceivingSearchService.class);
    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;

    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    private DocstoreUtil docstoreUtil;

    public DocstoreUtil getDocstoreUtil() {

        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);

        }
        return docstoreUtil;
    }

    public SearchResponse holdingSearch(int startIndex, int searchLimit,String sortingOrder) {
        SearchResponse searchResponse = new SearchResponse();
        org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
        search_Params.setStartIndex(startIndex);
        search_Params.setPageSize(searchLimit);
        search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), "", ""), ""));
        search_Params.getSortConditions().add(search_Params.buildSortCondition("Title_sort", sortingOrder));
        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(getSearchParams(search_Params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return searchResponse;
    }

    public SearchResponse searchDataFromDocstore(int startIndex, int searchLimit, Set<String> instance_Ids, HashMap<String,String> criteriaMap,String sortingOrder) {
        SearchResponse searchResponse = new SearchResponse();
        org.kuali.ole.docstore.common.search.SearchParams searchParam = new org.kuali.ole.docstore.common.search.SearchParams();
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(searchLimit);
        String title=criteriaMap.get(OLEConstants.TITLE);
        String issn=criteriaMap.get(OLEConstants.ISSN);
        String localIdentifier=criteriaMap.get(OLEConstants.LOCAL_IDENTIFIER);
        String journalTitle=criteriaMap.get(OLEConstants.JOURNAL_TITLE_SEARCH);
        if (!title.isEmpty()) {
            searchParam.getSearchConditions().add(searchParam.buildSearchCondition("AND", searchParam.buildSearchField(DocType.BIB.getCode(), Bib.TITLE, title), "AND"));
        }
        if (!issn.isEmpty()) {
            searchParam.getSearchConditions().add(searchParam.buildSearchCondition("AND", searchParam.buildSearchField(DocType.HOLDINGS.getCode(), Bib.ISSN, issn), "AND"));
        }
        if (!localIdentifier.isEmpty()) {
            searchParam.getSearchConditions().add(searchParam.buildSearchCondition("", searchParam.buildSearchField(DocType.BIB.getCode(), OLEConstants.LOCALID_SEARCH, localIdentifier), "AND"));

        }
        if (!journalTitle.isEmpty()) {
            searchParam.getSearchConditions().add(searchParam.buildSearchCondition("AND", searchParam.buildSearchField(DocType.BIB.getCode(), OLEConstants.JOURNAL_TITLE_SEARCH, journalTitle), "AND"));
        }
        if (instance_Ids != null && instance_Ids.size() > 0) {
            for (String instanceId : instance_Ids) {
                searchParam.getSearchConditions().add(searchParam.buildSearchCondition("AND", searchParam.buildSearchField(DocType.HOLDINGS.getCode(), "id", instanceId), "OR"));
            }
        }
        searchParam.getSortConditions().add(searchParam.buildSortCondition("Title_sort", sortingOrder));
        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(getSearchParams(searchParam));
            LOG.info(searchResponse.getTotalRecordCount());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return searchResponse;
    }

    private org.kuali.ole.docstore.common.search.SearchParams getSearchParams(org.kuali.ole.docstore.common.search.SearchParams search_Params) {
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), "id"));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), Holdings.CALL_NUMBER));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), Holdings.LOCATION_NAME));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "Title_sort"));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.ISSN));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "staffOnlyFlag"));
        return search_Params;
    }


    public OLESerialReceivingRecord getSerialRecord(SearchResult searchResult) {
        OLESerialReceivingRecord oleSerialReceivingRecord = new OLESerialReceivingRecord();
        Map instanceValue = new HashMap();
        if (searchResult.getSearchResultFields() != null && searchResult.getSearchResultFields().size() > 0) {
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                if (searchResultField.getDocType().equalsIgnoreCase("holdings") && searchResultField.getFieldName().equalsIgnoreCase("id") && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                    oleSerialReceivingRecord.setInstanceId(searchResultField.getFieldValue());

                    instanceValue.put(OLEConstants.INSTANCE_ID, searchResultField.getFieldValue());
                }

                if (searchResultField.getDocType().equalsIgnoreCase("holdings") && searchResultField.getFieldName().equalsIgnoreCase(Holdings.CALL_NUMBER) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                    oleSerialReceivingRecord.setCallNumber(searchResultField.getFieldValue());
                }
                if (searchResultField.getDocType().equalsIgnoreCase("holdings") && searchResultField.getFieldName().equalsIgnoreCase(Holdings.LOCATION_NAME) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                    oleSerialReceivingRecord.setBoundLocation(searchResultField.getFieldValue());
                }
                if (searchResultField.getDocType().equalsIgnoreCase("bibliographic") && searchResultField.getFieldName().equalsIgnoreCase("id") && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                    if(oleSerialReceivingRecord.getBibId()==null){
                    oleSerialReceivingRecord.setBibId(searchResultField.getFieldValue());
                    }
                }
                if (searchResultField.getDocType().equalsIgnoreCase("bibliographic") && searchResultField.getFieldName().equalsIgnoreCase("Title_sort") && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                    if(oleSerialReceivingRecord.getTitle()==null){
                    oleSerialReceivingRecord.setTitle(searchResultField.getFieldValue());
                    }
                }
                if (searchResultField.getDocType().equalsIgnoreCase("bibliographic") && searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag") && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getFieldValue().equalsIgnoreCase("true")) {
                    oleSerialReceivingRecord.setStaffOnlyFlag(OLEConstants.STAFF_ONLY_COLOR);
                    oleSerialReceivingRecord.setStaffOnlyFlagStyle(OLEConstants.STAFF_ONLY_STYLE);
                } else {
                    oleSerialReceivingRecord.setStaffOnlyFlag(OLEConstants.NON_STAFF_ONLY_COLOR);
                    oleSerialReceivingRecord.setStaffOnlyFlagStyle(OLEConstants.NON_STAFF_ONLY_STYLE);
                }
                if (searchResultField.getDocType().equalsIgnoreCase("bibliographic") && searchResultField.getFieldName().equalsIgnoreCase(Bib.ISSN) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                    if(oleSerialReceivingRecord.getIssn()==null){
                    oleSerialReceivingRecord.setIssn(searchResultField.getFieldValue());
                    }
                }
            }
        }
        List<OLESerialReceivingDocument> oleSerialReceivingDocument = new ArrayList<>();
        if(instanceValue.size()!=0){
            oleSerialReceivingDocument = (List<OLESerialReceivingDocument>) getBusinessObjectService().findMatching(OLESerialReceivingDocument.class, instanceValue);
        }
        if (oleSerialReceivingDocument.isEmpty()) {
            oleSerialReceivingRecord.setAction(OLEConstants.SERIAL_SEARCH_CREATE_NEW);
            oleSerialReceivingRecord.setHref("serialReceiving?viewId=OLESerialReceivingView&amp;methodToCall=docHandler&amp;command=initiate&amp;bibId=" + oleSerialReceivingRecord.getBibId() + "&amp;instanceId=" + oleSerialReceivingRecord.getInstanceId());
        } else {
            for (OLESerialReceivingDocument oleSerialReceiving_Document : oleSerialReceivingDocument) {
                if (!oleSerialReceiving_Document.isActive()) {
                    oleSerialReceivingRecord.setAction(OLEConstants.SERIAL_SEARCH_CREATE_NEW);
                    oleSerialReceivingRecord.setHref("serialReceiving?viewId=OLESerialReceivingView&amp;methodToCall=docHandler&amp;command=initiate&amp;bibId=" + oleSerialReceivingRecord.getBibId() + "&amp;instanceId=" + oleSerialReceivingRecord.getInstanceId());
                } else {
                    oleSerialReceivingRecord.setAction(OLEConstants.SERIAL_SEARCH_SHOW_RECORD);
                    oleSerialReceivingRecord.setHref("serialReceiving?viewId=OLESerialReceivingView&amp;methodToCall=docHandler&amp;docId=" + oleSerialReceiving_Document.getDocumentNumber() + "&amp;command=displayDocSearchView&amp;bibId=" + oleSerialReceiving_Document.getBibId() + "&amp;instanceId=" + oleSerialReceiving_Document.getInstanceId());
                    oleSerialReceivingRecord.setCheckInWorkUnit(oleSerialReceiving_Document.getSerialReceiptLocation());
                    oleSerialReceivingRecord.setUnboundLocation(oleSerialReceiving_Document.getUnboundLocation());
                    Map<String, String> subscriptionStatusMap = new HashMap<String, String>();
                    subscriptionStatusMap.put("receiptStatusCode", oleSerialReceiving_Document.getSubscriptionStatus());
                    List<OleReceiptStatus> oleReceiptStatuses = (List<OleReceiptStatus>) getBusinessObjectService().findMatching(OleReceiptStatus.class, subscriptionStatusMap);
                    oleSerialReceivingRecord.setSubscriptionStatus(oleReceiptStatuses != null && oleReceiptStatuses.size() > 0 ? oleReceiptStatuses.get(0).getReceiptStatusName() : "");
                    break;
                }
            }
        }

        return oleSerialReceivingRecord;

    }

    public Set<String> getInstanceIdList(String poId) {
        Set<String> instanceIds = new HashSet<>();
        Map parentCriterial = new HashMap();
        parentCriterial.put("purchaseOrder.purapDocumentIdentifier", poId);
        List<OlePurchaseOrderItem> purchaseOrderItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, parentCriterial);
        if (purchaseOrderItems != null && purchaseOrderItems.size() > 0) {
            for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
                String bibId = olePurchaseOrderItem.getItemTitleId();
                String poDocId = olePurchaseOrderItem.getPurapDocument().getDocumentNumber();
                OlePurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderItem.getPurapDocument();
                if (olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching()) {
                    parentCriterial = new HashMap();
                    parentCriterial.put(OLEConstants.BIB_ID, bibId);
                    parentCriterial.put("poDocNum", poDocId);
                    List<OleCopy> copyList = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, parentCriterial);
                    if (copyList != null && copyList.size() > 0) {
                        for (OleCopy copy : copyList) {
                            instanceIds.add(copy.getInstanceId());
                        }
                    }
                }
            }
        }
        return instanceIds;
    }

    public List<OLESerialReceivingRecord> getOleSerialReceivingList(List<OLESerialReceivingDocument> oleSerialReceivingDocuments) {
        List<OLESerialReceivingRecord> oleSerialReceivingRecords = new ArrayList<>();
        try {
            for (OLESerialReceivingDocument serialReceivingDocument : oleSerialReceivingDocuments) {
                Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(serialReceivingDocument.getBibId());
                Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(serialReceivingDocument.getInstanceId());
                OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
                OLESerialReceivingRecord oleSerialReceivingRecord = new OLESerialReceivingRecord();
                oleSerialReceivingRecord.setAction(OLEConstants.SERIAL_SEARCH_SHOW_RECORD);
                oleSerialReceivingRecord.setTitle(bib.getTitle());
                oleSerialReceivingRecord.setHref("serialReceiving?viewId=OLESerialReceivingView&amp;methodToCall=docHandler&amp;docId=" + serialReceivingDocument.getDocumentNumber() + "&amp;command=displayDocSearchView&amp;bibId=" + serialReceivingDocument.getBibId() + "&amp;instanceId=" + serialReceivingDocument.getInstanceId());
                oleSerialReceivingRecord.setCallNumber(serialReceivingDocument.getCallNumber());
                oleSerialReceivingRecord.setCheckInWorkUnit(serialReceivingDocument.getSerialReceiptLocation());
                oleSerialReceivingRecord.setBoundLocation(getDocstoreUtil().getLocation(oleHoldings.getLocation(), new StringBuffer("")));
                oleSerialReceivingRecord.setUnboundLocation(serialReceivingDocument.getUnboundLocation());
                oleSerialReceivingRecord.setIssn(bib.getIssn());
                oleSerialReceivingRecords.add(oleSerialReceivingRecord);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return oleSerialReceivingRecords;
    }

    public List<OLESerialReceivingDocument> getOleSerialReceivingDocuments(SerialsReceivingRecordForm serialsReceivingRecordForm) {
        Map serialMap = new HashMap();
        if (serialsReceivingRecordForm.getSearchParams().getSearchFieldsList() != null && serialsReceivingRecordForm.getSearchParams().getSearchFieldsList().size() > 0) {
            serialMap.put(OLEConstants.OleHoldings.ACTIVE, true);
            for (SearchCondition sc : serialsReceivingRecordForm.getSearchParams().getSearchFieldsList()) {
                if (sc.getDocField().equalsIgnoreCase(OLEConstants.LOCALID_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                    String holdingsPrefix = DocumentUniqueIDPrefix.getPrefix(DocCategory.WORK.getCode(), org.kuali.ole.docstore.model.enums.DocType.HOLDINGS.getCode(), DocFormat.OLEML.getCode());
                    if (!sc.getSearchText().contains(holdingsPrefix)) {
                        serialMap.put(OLEConstants.INSTANCE_ID, holdingsPrefix + "-" + sc.getSearchText());
                    } else {
                        serialMap.put(OLEConstants.INSTANCE_ID,holdingsPrefix);
                    }
                } else if (sc.getDocField().equalsIgnoreCase(OLEConstants.PO_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                    serialMap.put(OLEConstants.PO_ID, sc.getSearchText());
                } else if (sc.getDocField().equalsIgnoreCase(OLEConstants.SERIAL_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                    serialMap.put(OLEConstants.SERIAL_RECEIVING_RECORD, sc.getSearchText());
                }
            }
        }
        List<OLESerialReceivingDocument> oleSerialReceivingDocuments = (List<OLESerialReceivingDocument>) getBusinessObjectService().findMatching(OLESerialReceivingDocument.class, serialMap);
        return oleSerialReceivingDocuments;
    }

    public HoldingsRecord getHoldings(String holdingsIdenfier){
        Map holdingsValue = new HashMap();
        holdingsValue.put("holdingsId",holdingsIdenfier.replace("who-",""));
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord =  (HoldingsRecord)getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdingsValue);
        return holdingsRecord ;
    }

    public List<OLESerialReceivingRecord> getOleSerialReceivingListFromHoldings(SearchResult searchResult){
        List<OLESerialReceivingRecord> oleSerialReceivingRecordList = new ArrayList<>();
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        Map instanceValue = new HashMap();
        if (searchResult.getSearchResultFields() != null && searchResult.getSearchResultFields().size() > 0) {
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                OLESerialReceivingRecord oleSerialReceivingRecord = new OLESerialReceivingRecord();
                if (searchResultField.getDocType().equalsIgnoreCase("holdings") && searchResultField.getFieldName().equalsIgnoreCase("id") && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                    oleSerialReceivingRecord.setInstanceId(searchResultField.getFieldValue());
                    holdingsRecord = getHoldings(searchResultField.getFieldValue());
                    instanceValue.put(OLEConstants.INSTANCE_ID, searchResultField.getFieldValue());
                    if (StringUtils.isNotBlank(holdingsRecord.getCallNumber())) {
                        oleSerialReceivingRecord.setCallNumber(holdingsRecord.getCallNumber());
                    }
                    if (StringUtils.isNotBlank(holdingsRecord.getLocation())) {
                        oleSerialReceivingRecord.setBoundLocation(holdingsRecord.getLocation());
                    }
                    if (StringUtils.isNotBlank(holdingsRecord.getBibId())) {
                        oleSerialReceivingRecord.setBibId(holdingsRecord.getBibId());
                    }
                    for (SearchResultField searchResultField1 : searchResult.getSearchResultFields()) {
                        if (searchResultField1.getDocType().equalsIgnoreCase("bibliographic") && searchResultField1.getFieldName().equalsIgnoreCase("Title_sort") && searchResultField1.getFieldValue() != null && !searchResultField1.getFieldValue().isEmpty()) {
                            if(oleSerialReceivingRecord.getTitle()==null){
                                oleSerialReceivingRecord.setTitle(searchResultField1.getFieldValue());
                            }
                        }
                        if (searchResultField1.getDocType().equalsIgnoreCase("bibliographic") && searchResultField1.getFieldName().equalsIgnoreCase("staffOnlyFlag") && searchResultField1.getFieldValue() != null && !searchResultField1.getFieldValue().isEmpty() && searchResultField1.getFieldValue().equalsIgnoreCase("true")) {
                            oleSerialReceivingRecord.setStaffOnlyFlag(OLEConstants.STAFF_ONLY_COLOR);
                            oleSerialReceivingRecord.setStaffOnlyFlagStyle(OLEConstants.STAFF_ONLY_STYLE);
                        } else {
                            oleSerialReceivingRecord.setStaffOnlyFlag(OLEConstants.NON_STAFF_ONLY_COLOR);
                            oleSerialReceivingRecord.setStaffOnlyFlagStyle(OLEConstants.NON_STAFF_ONLY_STYLE);
                        }
                        if (searchResultField1.getDocType().equalsIgnoreCase("bibliographic") && searchResultField1.getFieldName().equalsIgnoreCase(Bib.ISSN) && searchResultField1.getFieldValue() != null && !searchResultField1.getFieldValue().isEmpty()) {
                            if(oleSerialReceivingRecord.getIssn()==null){
                                oleSerialReceivingRecord.setIssn(searchResultField1.getFieldValue());
                            }
                        }
                    }


                    if(instanceValue.size()!=0){
                        List<OLESerialReceivingDocument> oleSerialReceivingDocument = (List<OLESerialReceivingDocument>) getBusinessObjectService().findMatching(OLESerialReceivingDocument.class, instanceValue);

                        if (oleSerialReceivingDocument.isEmpty()) {
                            oleSerialReceivingRecord.setAction(OLEConstants.SERIAL_SEARCH_CREATE_NEW);
                            oleSerialReceivingRecord.setHref("serialReceiving?viewId=OLESerialReceivingView&amp;methodToCall=docHandler&amp;command=initiate&amp;bibId=" + oleSerialReceivingRecord.getBibId() + "&amp;instanceId=" + oleSerialReceivingRecord.getInstanceId());
                        } else {
                            for (OLESerialReceivingDocument oleSerialReceiving_Document : oleSerialReceivingDocument) {
                                if (!oleSerialReceiving_Document.isActive()) {
                                    oleSerialReceivingRecord.setAction(OLEConstants.SERIAL_SEARCH_CREATE_NEW);
                                    oleSerialReceivingRecord.setHref("serialReceiving?viewId=OLESerialReceivingView&amp;methodToCall=docHandler&amp;command=initiate&amp;bibId=" + oleSerialReceivingRecord.getBibId() + "&amp;instanceId=" + oleSerialReceivingRecord.getInstanceId());
                                } else {
                                    oleSerialReceivingRecord.setAction(OLEConstants.SERIAL_SEARCH_SHOW_RECORD);
                                    oleSerialReceivingRecord.setHref("serialReceiving?viewId=OLESerialReceivingView&amp;methodToCall=docHandler&amp;docId=" + oleSerialReceiving_Document.getDocumentNumber() + "&amp;command=displayDocSearchView&amp;bibId=" + oleSerialReceiving_Document.getBibId() + "&amp;instanceId=" + oleSerialReceiving_Document.getInstanceId());
                                    oleSerialReceivingRecord.setCheckInWorkUnit(oleSerialReceiving_Document.getSerialReceiptLocation());
                                    oleSerialReceivingRecord.setUnboundLocation(oleSerialReceiving_Document.getUnboundLocation());
                                    Map<String, String> subscriptionStatusMap = new HashMap<String, String>();
                                    subscriptionStatusMap.put("receiptStatusCode", oleSerialReceiving_Document.getSubscriptionStatus());
                                    List<OleReceiptStatus> oleReceiptStatuses = (List<OleReceiptStatus>) getBusinessObjectService().findMatching(OleReceiptStatus.class, subscriptionStatusMap);
                                    oleSerialReceivingRecord.setSubscriptionStatus(oleReceiptStatuses != null && oleReceiptStatuses.size() > 0 ? oleReceiptStatuses.get(0).getReceiptStatusName() : "");
                                    break;
                                }
                            }
                        }
                        oleSerialReceivingRecordList.add(oleSerialReceivingRecord);
                    }
                }

            }

        }
        return oleSerialReceivingRecordList;

    }

   }
