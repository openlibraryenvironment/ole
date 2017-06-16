package org.kuali.ole.oleng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.Exchange;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibInfoRecord;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.oleng.service.impl.OrderImportServiceImpl;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.spring.batch.BatchUtil;

import java.util.*;

/**
 * Created by SheikS on 3/10/2016.
 */
public class OleNGPOHelperUtil extends BusinessObjectServiceHelperUtil {
    private static final Logger LOG = Logger.getLogger(OleNGPOHelperUtil.class);
    private OrderImportService oleOrderImportService;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    private BatchUtil batchUtil;
    private OleNGPOValidationUtil oleNGPOValidationUtil;

    public Map<Integer, Set<Integer>> processReqAndPo(List<RecordDetails> recordDetailsList, BatchProcessProfile batchProcessProfile,
                                                      CreateReqAndPOBaseServiceHandler createReqAndPOServiceHandler,
                                                      Exchange exchange)  {
        Map<Integer, Set<Integer>> poIdsMap = new HashMap<>();
        List<OleOrderRecord> oleOrderRecords = new ArrayList<>();
        Integer purapId = null;
        OrderImportService oleOrderImportService = getOleOrderImportService();
        oleOrderImportService.setOleNGMemorizeService(createReqAndPOServiceHandler.getOleNGMemorizeService());
        for (Iterator<RecordDetails> iterator = recordDetailsList.iterator(); iterator.hasNext(); ) {
            RecordDetails recordDetails = iterator.next();
            Integer recordIndex = recordDetails.getIndex();
            String bibUUID = recordDetails.getBibUUID();
            if (StringUtils.isNotBlank(bibUUID)) {
                try {
                    synchronized (this) {
                        OleOrderRecord oleOrderRecord = getOleOrderImportService().prepareOleOrderRecord(recordDetails,
                                batchProcessProfile, exchange);

                    OleBibRecord oleBibRecord = new OleBibRecord();
                    Bib bib = getBibDetails(bibUUID);
                    oleBibRecord.setBibUUID(bibUUID);
                    oleBibRecord.setBib(bib);
                    oleOrderRecord.setOleBibRecord(oleBibRecord);


                        String bibProfileName = batchProcessProfile.getBibImportProfileForOrderImport();
                        oleOrderRecord.setBibImportProfileName(bibProfileName);
                        oleOrderRecord.setRecordIndex(recordIndex);
                        getOleNGPOValidationUtil().setOleNGMemorizeService(createReqAndPOServiceHandler.getOleNGMemorizeService());
                        boolean valid = getOleNGPOValidationUtil().validateOleOrderRecord(oleOrderRecord, exchange, recordIndex);
                        if (valid) {
                            oleOrderRecords.add(oleOrderRecord);
                        }
                    }
                }catch(Exception e){
                        e.printStackTrace();
                        getBatchUtil().addOrderFaiureResponseToExchange(e, recordIndex, exchange);
                }
            }

        }

        try {
            synchronized (this) {
                if (CollectionUtils.isNotEmpty(oleOrderRecords)) {
                    purapId = createReqAndPOServiceHandler.processOrder(oleOrderRecords, exchange);
                    addPOIdToMap(purapId, oleOrderRecords, poIdsMap);
                }
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
        BibInfoRecord bySinglePrimaryKey = getBusinessObjectService().findBySinglePrimaryKey(BibInfoRecord.class, bibId);
        if(null != bySinglePrimaryKey) {
            Bib bib = new Bib();
            String author = bySinglePrimaryKey.getAuthor();
            String title = bySinglePrimaryKey.getTitle();
            String publisher = bySinglePrimaryKey.getPublisher();
            String isbn = bySinglePrimaryKey.getIsxn();
            bib.setTitle(StringUtils.isNotBlank(title)? title : "");
            bib.setAuthor(StringUtils.isNotBlank(author) ? author : "");
            bib.setPublisher(StringUtils.isNotBlank(publisher)? publisher : "");
            bib.setIsbn(StringUtils.isNotBlank(isbn)? isbn : "");
            return bib;
        }
        return null;
    }

    public OrderImportService getOleOrderImportService() {
        if (null == oleOrderImportService) {
            oleOrderImportService = new OrderImportServiceImpl();
        }
        return oleOrderImportService;
    }

    public void setOleOrderImportService(OrderImportService oleOrderImportService) {
        this.oleOrderImportService = oleOrderImportService;
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
