package org.kuali.ole.oleng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.Exchange;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.oleng.service.impl.OrderImportServiceImpl;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.spring.batch.BatchUtil;

import java.util.*;

/**
 * Created by SheikS on 3/10/2016.
 */
public class OleNGPOHelperUtil {
    private static final Logger LOG = Logger.getLogger(OleNGPOHelperUtil.class);
    private static  OrderImportService oleOrderImportService;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    private BatchUtil batchUtil;
    private OleNGPOValidationUtil oleNGPOValidationUtil;

    private static OleNGPOHelperUtil oleNGPOHelperUtil;

    private OleNGPOHelperUtil() {
    }

    public static OleNGPOHelperUtil getInstance() {
        if(null == oleNGPOHelperUtil) {
            oleNGPOHelperUtil = new OleNGPOHelperUtil();
        }
        return oleNGPOHelperUtil;
    }

    public Map<Integer, Set<Integer>> processReqAndPo(List<RecordDetails> recordDetailsList, BatchProcessProfile batchProcessProfile,
                                   CreateReqAndPOBaseServiceHandler createReqAndPOServiceHandler, Exchange exchange)  {
        Map<Integer, Set<Integer>> poIdsMap = new HashMap<>();
        List<OleOrderRecord> oleOrderRecords = new ArrayList<>();
        Integer purapId = null;
        for (Iterator<RecordDetails> iterator = recordDetailsList.iterator(); iterator.hasNext(); ) {
            RecordDetails recordDetails = iterator.next();
            Integer recordIndex = recordDetails.getIndex();
            String bibUUID = recordDetails.getBibUUID();
            if (StringUtils.isNotBlank(bibUUID)) {
                try {
                    OleTxRecord oleTxRecord = getOleOrderImportService().processDataMapping(recordDetails, batchProcessProfile);

                    final OleOrderRecord oleOrderRecord = new OleOrderRecord();
                    oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
                    oleTxRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
                    String orderType = batchProcessProfile.getOrderType();
                    String linkToOption = "";
                    if(StringUtils.isNotBlank(orderType) && orderType.equalsIgnoreCase(OleNGConstants.BatchProcess.ORDER_TYPE_EHOLDINGS)) {
                        linkToOption = OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC;
                    }

                    if(StringUtils.isBlank(linkToOption)) {
                        linkToOption = OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT;
                    }

                    oleOrderRecord.setOleTxRecord(oleTxRecord);

                    OleBibRecord oleBibRecord = new OleBibRecord();
                    Bib bib = getBibDetails(bibUUID);
                    oleBibRecord.setBibUUID(bibUUID);
                    oleBibRecord.setBib(bib);
                    oleOrderRecord.setOleBibRecord(oleBibRecord);

                    oleOrderRecord.setLinkToOrderOption(linkToOption);

                    String bibProfileName = batchProcessProfile.getBibImportProfileForOrderImport();
                    oleOrderRecord.setBibImportProfileName(bibProfileName);
                    oleOrderRecord.setRecordIndex(recordIndex);
                    boolean valid = getOleNGPOValidationUtil().validateOleOrderRecord(oleOrderRecord, exchange, recordIndex);
                    if(valid) {
                        oleOrderRecords.add(oleOrderRecord);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getBatchUtil().addOrderFaiureResponseToExchange(e, recordIndex, exchange);
                }
            }

        }
        try {
            if(CollectionUtils.isNotEmpty(oleOrderRecords)) {
                purapId = createReqAndPOServiceHandler.processOrder(oleOrderRecords,exchange);
                addPOIdToMap(purapId, oleOrderRecords, poIdsMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getBatchUtil().addOrderFaiureResponseToExchange(e, null, exchange);
        }
        return poIdsMap;
    }

    private void addPOIdToMap(Integer purapId, List<OleOrderRecord> oleOrderRecords, Map<Integer, Set<Integer>> poIdsMap) {
        if (null != purapId) {
            Set<Integer> recordIndexes = new HashSet<>();
            for (Iterator<OleOrderRecord> iterator = oleOrderRecords.iterator(); iterator.hasNext(); ) {
                OleOrderRecord oleOrderRecord = iterator.next();
                Integer recordIndex = oleOrderRecord.getRecordIndex();
                recordIndexes.add(recordIndex);
            }
            poIdsMap.put(purapId, recordIndexes);
        }
    }


    private Bib getBibDetails(String bibId) {
        Bib bib = new Bib();
        String query = "id:" + bibId;
        SolrDocumentList solrDocumentList = getSolrRequestReponseHandler().getSolrDocumentList(query);
        if (solrDocumentList.size() > 0) {
            SolrDocument solrDocument = solrDocumentList.get(0);

            List<String> authors = (List<String>) solrDocument.getFieldValue(DocstoreConstants.TITLE_DISPLAY);
            String author = (CollectionUtils.isNotEmpty(authors)) ? authors.get(0) : "";

            List<String> titles = (List<String>) solrDocument.getFieldValue(DocstoreConstants.TITLE_DISPLAY);
            String title = (CollectionUtils.isNotEmpty(titles)) ? titles.get(0) : "";

            List<String> isbns = (List<String>) solrDocument.getFieldValue(DocstoreConstants.ISBN_DISPLAY);
            String isbn = (CollectionUtils.isNotEmpty(isbns)) ? isbns.get(0) : "";

            List<String> publishers = (List<String>) solrDocument.getFieldValue(DocstoreConstants.PUBLISHER_DISPLAY);
            String publisher = (CollectionUtils.isNotEmpty(publishers)) ? publishers.get(0) : "";

            bib.setTitle(title);
            bib.setAuthor(author);
            bib.setPublisher(publisher);
            bib.setIsbn(isbn);
        }
        return bib;
    }

    public OrderImportService getOleOrderImportService() {
        if (null == oleOrderImportService) {
            oleOrderImportService = new OrderImportServiceImpl();
        }
        return oleOrderImportService;
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }

    public void setBatchUtil(BatchUtil batchUtil) {
        this.batchUtil = batchUtil;
    }

    public OleNGPOValidationUtil getOleNGPOValidationUtil() {
        if(null == oleNGPOValidationUtil) {
            oleNGPOValidationUtil = new OleNGPOValidationUtil();
        }
        return oleNGPOValidationUtil;
    }

    public void setOleNGPOValidationUtil(OleNGPOValidationUtil oleNGPOValidationUtil) {
        this.oleNGPOValidationUtil = oleNGPOValidationUtil;
    }
}
