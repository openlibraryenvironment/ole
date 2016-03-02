package org.kuali.ole.deliver;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OLEDeliverService;
import org.kuali.ole.deliver.service.PatronBillResolver;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 5/18/15.
 */
public class OleLoanDocumentsFromSolrBuilder {
    private static final Logger LOG = Logger.getLogger(OleLoanDocumentsFromSolrBuilder.class);

    private DocstoreClientLocator docstoreClientLocator;
    private BusinessObjectService businessObjectService;
    private CircDeskLocationResolver circDeskLocationResolver;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private PatronBillResolver patronBillResolver;
    private static Map<String, String> locationName = new HashMap<>();


    private PatronBillResolver getPatronBillResolver() {
        if (patronBillResolver == null) {
            patronBillResolver = new PatronBillResolver();
        }
        return patronBillResolver;
    }

    public List<OleLoanDocument> getPatronLoanedItemBySolr(String patronId, String itemBarcode) throws Exception {

        Long b1 = System.currentTimeMillis();
        SearchResponse searchResponse = new SearchResponse();
        if (patronId != null) {
            String itemStatusParam = getParameter(OLEParameterConstants.ITEM_STATUS_FOR_RET_LOAN);
            String[] itemStatus = new String[0];
            if (itemStatusParam != null) {
                itemStatus = itemStatusParam.split("[|]");
            }
            SearchParams searchParams = new SearchParams();
            List<SearchCondition> searchConditions = new ArrayList<>();
            int count = itemStatus.length;
            for (int i = 0; i < count; i++) {
                if (i == (count - 1)) {
                    searchConditions.add(searchParams.buildSearchCondition("", searchParams.buildSearchField("item", "ItemStatus_search", itemStatus[i]), "AND"));
                } else {
                    searchConditions.add(searchParams.buildSearchCondition("", searchParams.buildSearchField("item", "ItemStatus_search", itemStatus[i]), "OR"));
                }
            }

            searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField("item", "currentBorrower", patronId), "AND"));
            if (null != itemBarcode) {
                searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org
                                .kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), ItemOleml.ITEM_BARCODE,
                        itemBarcode), "AND"));
            }
            searchParams.setPageSize(Integer.parseInt(OLEConstants.MAX_PAGE_SIZE_FOR_LOAN));
            SearchParmsBuilder.buildSearchParams(searchParams);
            searchParams.getSearchConditions().addAll(searchConditions);
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("patronId", patronId);
        List<OleLoanDocument> matching = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, map);

        List<OleLoanDocument> matchingLoan = getOleLoanDocumentsFromSearchResponse(searchResponse, matching);


        Long b2 = System.currentTimeMillis();
        Long total = b2 - b1;
        LOG.info("The time taken for Docstore call :" + total);
        return sortLoanDocumentByDueDate(matchingLoan);
    }

    public OleLoanDocument retrieveByPatronAndItem(String patronId, String itemBarcode) throws Exception {
        LOG.debug("Inside the retrieveByPatronAndItem method");
        Long b1 = System.currentTimeMillis();
        SearchResponse searchResponse = new SearchResponse();
        HashMap<String, Object> loanMap = new HashMap<>();
        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
        if (patronId != null) {
            String itemStatusParam = getParameter(OLEParameterConstants.ITEM_STATUS_FOR_RET_LOAN);
            String[] itemStatus = new String[0];
            if (itemStatusParam != null) {
                itemStatus = itemStatusParam.split("[|]");
            }
            SearchParams searchParams = new SearchParams();
            List<SearchCondition> searchConditions = new ArrayList<>();
            int count = itemStatus.length;
            for (int i = 0; i < count; i++) {
                if (i == (count - 1)) {
                    searchConditions.add(searchParams.buildSearchCondition("", searchParams.buildSearchField("item", "ItemStatus_search", itemStatus[i]), "OR"));
                } else {
                    searchConditions.add(searchParams.buildSearchCondition("", searchParams.buildSearchField("item", "ItemStatus_search", itemStatus[i]), "AND"));
                }
            }

            searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField("item", "currentBorrower", patronId), "AND"));
            searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, itemBarcode), "AND"));
            searchParams.setPageSize(Integer.parseInt(OLEConstants.MAX_PAGE_SIZE_FOR_LOAN));
            SearchParmsBuilder.buildSearchParams(searchParams);
            searchParams.getSearchConditions().addAll(searchConditions);
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        }
        List<OleLoanDocument> matchingLoan = getOleLoanDocumentsFromSearchResponse(searchResponse, null);
        Long b2 = System.currentTimeMillis();
        Long b3 = b2 - b1;
        LOG.info("The Time taken retrieveByPatronAndItem--" + b3);
        return matchingLoan != null && matchingLoan.size() > 0 ? matchingLoan.get(0) : null;
    }


    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if (parameter == null) {
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter != null ? parameter.getValue() : null;
    }

    public List<OleLoanDocument> getOleLoanDocumentsFromSearchResponse(SearchResponse searchResponse, List<OleLoanDocument> oleDBLoanDocuments) throws Exception {
        int count = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<OleLoanDocument> oleLoanDocuments = new ArrayList<>();
        if (searchResponse != null) {
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                OleLoanDocument oleLoanDocument = null;
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {

                    if (searchResultField.getFieldValue() != null) {

                        if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                            String itemBarcode = searchResultField.getFieldValue();
                            if (null == oleLoanDocument) {
                                oleLoanDocument = getLoanDocumentBasedOnBarcode(itemBarcode, oleDBLoanDocuments);
                            }
                            oleLoanDocument.setItemId(itemBarcode);
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                            String itemUuid = searchResultField.getFieldValue();
                            if (null == oleLoanDocument) {
                                oleLoanDocument = getLoanDocumentBasedOnItemUuid(itemUuid, oleDBLoanDocuments);
                            }
                            oleLoanDocument.setItemUuid(itemUuid);
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                            oleLoanDocument.setBibUuid(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                            oleLoanDocument.setTitle(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Author_display")) {
                            oleLoanDocument.setAuthor(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                            oleLoanDocument.setInstanceUuid(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("claimsReturnedNote")) {
                            oleLoanDocument.setClaimsReturnNote(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ClaimsReturnedFlag_display") && searchResultField.getFieldValue().equalsIgnoreCase("true")) {
                            oleLoanDocument.setClaimsReturnedIndicator(true);
                            count++;
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                            String location= searchResultField.getFieldValue().split("/")[searchResultField.getFieldValue().split("/").length - 1];
                            getLocationBySolr(location, oleLoanDocument);
                            oleLoanDocument.setItemFullLocation(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsLocation_display") &&
                                (oleLoanDocument.getItemLocation() == null || oleLoanDocument.getItemLocation().isEmpty())) {
                            String location= searchResultField.getFieldValue().split("/")[searchResultField.getFieldValue().split("/").length - 1];
                            getLocationBySolr(location, oleLoanDocument);
                            oleLoanDocument.setItemFullLocation(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("claimsReturnedFlagCreateDate")) {
                            String[] formatStrings = new String[]{"MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss"};
                            Date date = tryParse(formatStrings, searchResultField.getFieldValue());
                            oleLoanDocument.setClaimsReturnedDate(new Timestamp(date.getTime()));

                        } else if (searchResultField.getFieldName().equalsIgnoreCase("dueDateTime")) {
                            String[] formatStrings = new String[]{"MM/dd/yyyy hh:mm:ssa", "MM/dd/yyyy hh:mm:ss", "MM/dd/yyyy", "yyyy-MM-dd hh:mm:ss"};
                            Date date = tryParse(formatStrings, searchResultField.getFieldValue());
                            oleLoanDocument.setLoanDueDate(new Timestamp(date.getTime()));

                        } else if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                            oleLoanDocument.setItemCallNumber(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("CallNumberPrefix_display")) {
                            oleLoanDocument.setItemCallNumberPrefix(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCallNumberPrefix_display") &&
                                (oleLoanDocument.getItemCallNumberPrefix() == null || oleLoanDocument.getItemCallNumberPrefix().isEmpty())) {
                            oleLoanDocument.setItemCallNumberPrefix(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("TemporaryItemTypeFullValue_search")) {
                            oleLoanDocument.setItemType(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeFullValue_display") &&
                                (oleLoanDocument.getItemType() == null || oleLoanDocument.getItemType().isEmpty())) {
                            oleLoanDocument.setItemType(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("TemporaryItemTypeCodeValue_search")) {
                            oleLoanDocument.setItemTypeName(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeCodeValue_display") &&
                                (oleLoanDocument.getItemTypeName() == null || oleLoanDocument.getItemTypeName().isEmpty())) {
                            oleLoanDocument.setItemTypeName(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Enumeration_display")) {
                            oleLoanDocument.setEnumeration(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Chronology_display")) {
                            oleLoanDocument.setChronology(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemStatus_display")) {
                            oleLoanDocument.setItemStatus(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemDamagedStatus_display")) {
                            oleLoanDocument.setItemDamagedStatus(searchResultField.getFieldValue().equalsIgnoreCase("true"));
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("DamagedItemNote_search")) {
                            oleLoanDocument.setItemDamagedNote(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceFlagNote_search")) {
                            oleLoanDocument.setMissingPieceNote(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceFlag_display")) {
                            oleLoanDocument.setMissingPieceFlag(searchResultField.getFieldValue().equalsIgnoreCase("true"));
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("CopyNumber_search")) {
                            oleLoanDocument.setItemCopyNumber(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCopyNumber_search") &&
                                (oleLoanDocument.getItemCopyNumber() == null || oleLoanDocument.getItemCopyNumber().isEmpty())) {
                            oleLoanDocument.setItemCopyNumber(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCallNumber_search") &&
                                (oleLoanDocument.getItemCallNumber() == null || oleLoanDocument.getItemCallNumber().isEmpty())) {
                            oleLoanDocument.setItemCallNumber(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceCount_search")) {
                            oleLoanDocument.setMissingPiecesCount(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("NumberOfPieces_search")) {
                            oleLoanDocument.setItemNumberOfPieces(Integer.parseInt(searchResultField.getFieldValue()));
                            oleLoanDocument.setBackUpNoOfPieces(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("proxyBorrower")) {
                            oleLoanDocument.setProxyPatronId(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceFlag_display")) {
                            oleLoanDocument.setMissingPieceFlag(Boolean.parseBoolean(searchResultField.getFieldValue()));
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("NumberOfRenew_display")) {
                            oleLoanDocument.setNumberOfRenewals(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("checkOutDateTime")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                            Date date = formatter.parse(searchResultField.getFieldValue());
                            oleLoanDocument.setCreateDate(new Timestamp(date.getTime()));
                        }
                    }
                }
                if (oleLoanDocument.getOlePatron() != null) {
                    oleLoanDocument.getOlePatron().setNumberOfClaimsReturned(count);
                }
                if (oleLoanDocument.getItemCallNumber() != null && oleLoanDocument.getItemCallNumberPrefix() != null) {
                    oleLoanDocument.setItemCallNumber(oleLoanDocument.getItemCallNumberPrefix() + "-" + oleLoanDocument.getItemCallNumber());
                }
                oleLoanDocuments.add(oleLoanDocument);
            }
            if (!oleLoanDocuments.isEmpty()) {
                processRequestBoCheckForLoanDocuments(oleLoanDocuments);
            }
        }
        return oleLoanDocuments;
    }

    private OleLoanDocument getLoanDocumentBasedOnItemUuid(String itemUuid, List<OleLoanDocument> oleDBLoanDocuments) {
        for (Iterator<OleLoanDocument> iterator = oleDBLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument loanDocument = iterator.next();
            if(loanDocument.getItemUuid().equalsIgnoreCase(itemUuid)){
                return loanDocument;
            }
        }
        return new OleLoanDocument();
    }

    private OleLoanDocument getLoanDocumentBasedOnBarcode(String itemBarcode, List<OleLoanDocument> oleDBLoanDocuments) {
        for (Iterator<OleLoanDocument> iterator = oleDBLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument loanDocument = iterator.next();
            if(loanDocument.getItemId().equalsIgnoreCase(itemBarcode)){
                return loanDocument;
            }
        }
        return new OleLoanDocument();
    }

    private void processRequestBoCheckForLoanDocuments(List<OleLoanDocument> oleLoanDocuments) {
        List<String> itemIds = new ArrayList<>();
        Map<String, OleLoanDocument> map = new HashMap();
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        for (Iterator<OleLoanDocument> iterator = oleLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            String itemId = oleLoanDocument.getItemId();
            itemIds.add(itemId);
            map.put(itemId, oleLoanDocument);
            oleLoanDocument.setRequestFlag("");
        }
        List<OleDeliverRequestBo> deliverRequestBos = oleLoanDocumentDaoOjb.getDeliverRequestBos(itemIds);

        for (Iterator<OleDeliverRequestBo> iterator = deliverRequestBos.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            if (map.containsKey(oleDeliverRequestBo.getItemId())) {
                map.get(oleDeliverRequestBo.getItemId()).setRequestFlag(OLEConstants.VIEW_ALL_REQUESTS);
            }
        }
    }

    public void getLocationBySolr(String locationCode, OleLoanDocument oleLoanDocument) throws Exception {
        String levelFullName = getLocationFullName(locationCode);

        oleLoanDocument.setItemLocation(levelFullName);
        oleLoanDocument.setLocation(levelFullName);
    }

    public String getLocationFullName(String locationCode) throws Exception {
        String levelFullName = "";
        if (locationName.containsKey(locationCode)) {
            levelFullName = locationName.get(locationCode);
        } else {
            OleLocation oleLocation = getCircDeskLocationResolver().getLocationByLocationCode(locationCode);
            levelFullName = getCircDeskLocationResolver().getFullPathLocationByName(oleLocation);
            locationName.put(locationCode, levelFullName);
        }
        return levelFullName;
    }

    public Date tryParse(String[] formatStrings, String dateString) throws ParseException {
        StringBuffer exceptionMessage = new StringBuffer("Parse Exception'");
        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            } catch (ParseException e) {
                exceptionMessage.append(e.getMessage());
            }
        }
        throw new ParseException(exceptionMessage.toString(), 0);
    }

    public List<OleLoanDocument> sortLoanDocumentByDueDate(List<OleLoanDocument> loanDocuments) {
        List<OleLoanDocument> LoanDocumentList = new ArrayList<>();
        List<OleLoanDocument> indefiniteLoanDocumentList = new ArrayList<>();
        for (OleLoanDocument loanDoc : loanDocuments) {
            if (loanDoc.getLoanDueDate() != null && !(loanDoc.getLoanDueDate().toString().isEmpty())) {
                LoanDocumentList.add(loanDoc);
            } else {

                indefiniteLoanDocumentList.add(loanDoc);
            }
        }
        Collections.sort(LoanDocumentList, new Comparator<OleLoanDocument>() {
            public int compare(OleLoanDocument o1, OleLoanDocument o2) {
                return o1.getLoanDueDate().compareTo(o2.getLoanDueDate());
            }
        });
        LoanDocumentList.addAll(indefiniteLoanDocumentList);
        return LoanDocumentList;
    }


    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = SpringContext.getBean(ItemOlemlRecordProcessor.class);
        }
        return itemOlemlRecordProcessor;
    }


    /**
     * This method returns PatronTemporaryCirculationHistoryRecord using patronId
     *
     * @param patronId
     * @return List
     * @throws Exception
     */
    public List<OleTemporaryCirculationHistory> getPatronTemporaryCirculationHistoryRecords(String patronId) {
        LOG.debug("Inside the getPatronTemporaryCirculationHistoryRecords method");
        Map barMap = new HashMap();
        barMap.put("olePatronId", patronId);
        List<OleTemporaryCirculationHistory> matchingLoan = (List<OleTemporaryCirculationHistory>) getBusinessObjectService().findMatching(OleTemporaryCirculationHistory.class, barMap);
        for (int itemid = 0; itemid < matchingLoan.size(); itemid++) {
            String itemUuid = matchingLoan.get(itemid).getItemUuid();
            org.kuali.ole.docstore.common.document.Item item = null;
            try {
                item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUuid);

                org.kuali.ole.docstore.common.document.content.instance.Item itemContent = getItemOlemlRecordProcessor().fromXML(item.getContent());
                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(item.getHolding().getContent());
                OleTemporaryCirculationHistory oleTemporaryCirculationHistory = matchingLoan.get(itemid);
                if (oleTemporaryCirculationHistory.getItemUuid().equals(item.getId())) {
                    oleTemporaryCirculationHistory.setTitle(item.getHolding().getBib().getTitle());
                    oleTemporaryCirculationHistory.setAuthor(item.getHolding().getBib().getAuthor());
                    oleTemporaryCirculationHistory.setItemUuid(item.getId());
                    oleTemporaryCirculationHistory.setCallNumber(getItemCallNumber(itemContent.getCallNumber(), oleHoldings.getCallNumber()));
                    oleTemporaryCirculationHistory.setCopyNumber(itemContent.getCopyNumber());
                    oleTemporaryCirculationHistory.setVolumeNumber(itemContent.getVolumeNumber());
                    oleTemporaryCirculationHistory.setItemStatus(itemContent.getItemStatus().getFullValue());
                    oleTemporaryCirculationHistory.setItemType(itemContent.getItemType().getCodeValue());
                    oleTemporaryCirculationHistory.setEnumeration(itemContent.getEnumeration());
                    oleTemporaryCirculationHistory.setChronology(itemContent.getChronology());
                    if(oleTemporaryCirculationHistory.getOleProxyPatronId() != null){
                        Map<String, String> criteria = new HashMap<>();
                        criteria.put(OLEConstants.OlePatron.PATRON_ID,oleTemporaryCirculationHistory.getOleProxyPatronId());
                        OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, criteria);
                        if(olePatronDocument != null){
                            olePatronDocument = OLEDeliverService.populatePatronName(olePatronDocument);
                            oleTemporaryCirculationHistory.setProxyPatronBarcode(olePatronDocument.getBarcode());
                            oleTemporaryCirculationHistory.setProxyPatronName(olePatronDocument.getPatronName());
                            oleTemporaryCirculationHistory.setProxyPatronBarcodeUrl(OLEConstants.ASSIGN_INQUIRY_PATRON_ID + olePatronDocument.getOlePatronId() + OLEConstants.ASSIGN_PATRON_INQUIRY);
                        }
                    }

                    OleCirculationDesk val = getCircDeskLocationResolver().getOleCirculationDesk(oleTemporaryCirculationHistory.getCirculationLocationId());
                    if (val != null) {
                        oleTemporaryCirculationHistory.setCirculationLocationCode(val.getCirculationDeskCode());
                    }
                    if (oleTemporaryCirculationHistory.getCirculationLocationId() != null && !oleTemporaryCirculationHistory.getCirculationLocationId().equals("")) {
                        LocationValuesBuilder locationValuesBuilder = new LocationValuesBuilder();
                        locationValuesBuilder.getLocation(itemContent, oleTemporaryCirculationHistory, item.getHolding().getId());
                    }
                }
            } catch (Exception e) {
                LOG.error("Error while looking up temp circ history records.");
                LOG.error(e.getStackTrace());

            }

        }
        return matchingLoan;
    }

    public String getItemCallNumber(org.kuali.ole.docstore.common.document.content.instance.Item oleItem, String instanceUUID) throws Exception {
        OleHoldings oleHoldings = getOleHoldings(instanceUUID);
        return getItemCallNumber(oleItem.getCallNumber(), oleHoldings.getCallNumber());
    }

    /**
     * Retrieves Item call number.
     *
     * @param itemCallNumber,holdingCallNumber
     * @return
     * @throws Exception
     */
    public String getItemCallNumber(CallNumber itemCallNumber, CallNumber holdingCallNumber) throws Exception {
        LOG.debug("Inside the getItemCallNumber method");
        String callNumber = "";

        if (itemCallNumber != null && StringUtils.isNotBlank(itemCallNumber.getType())) {
            callNumber += itemCallNumber.getType() + OLEConstants.DELIMITER_DASH;
        } else if (holdingCallNumber != null && StringUtils.isNotBlank(holdingCallNumber.getType())) {
            callNumber += holdingCallNumber.getType() + OLEConstants.DELIMITER_DASH;
        }
        if (itemCallNumber != null && StringUtils.isNotBlank(itemCallNumber.getPrefix())) {
            callNumber += itemCallNumber.getPrefix() + OLEConstants.DELIMITER_DASH;
        } else if (holdingCallNumber != null && StringUtils.isNotBlank(holdingCallNumber.getPrefix())) {
            callNumber += holdingCallNumber.getPrefix() + OLEConstants.DELIMITER_DASH;
        }
        if (itemCallNumber != null && StringUtils.isNotBlank(itemCallNumber.getNumber())) {
            callNumber += itemCallNumber.getNumber();
        } else if (holdingCallNumber != null && StringUtils.isNotBlank(holdingCallNumber.getNumber())) {
            callNumber += holdingCallNumber.getNumber();
        }

        return callNumber;
    }

    /**
     * Retrieves Holding Object for given instance UUID.
     *
     * @param instanceUUID
     * @return
     * @throws Exception
     */
    public OleHoldings getOleHoldings(String instanceUUID) throws Exception {
        LOG.debug("--Inside getOleHoldings---");
        Holdings holdings = new Holdings();
        holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(instanceUUID);
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        return oleHoldings;
    }


}
