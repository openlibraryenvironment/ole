package org.kuali.ole.oleng.callable;

import org.apache.commons.collections.CollectionUtils;
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
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by SheikS on 1/10/2016.
 */
@Deprecated
public class POCallable implements Callable {

    private static final Logger LOG = Logger.getLogger(POCallable.class);
    private final List<RecordDetails> recordDetailsList;

    private BatchProcessProfile batchProcessProfile;
    private OrderImportService oleOrderImportService;
    private CreateReqAndPOBaseServiceHandler createReqAndPOServiceHandler;
    private PlatformTransactionManager transactionManager;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    private List<OleOrderRecord> oleOrderRecords;
    private BatchUtil batchUtil;
    private Exchange exchange;

    public POCallable(List<RecordDetails> recordDetailsList, BatchProcessProfile batchProcessProfile, CreateReqAndPOBaseServiceHandler createReqAndPOServiceHandler) {
        this.recordDetailsList = recordDetailsList;
        this.batchProcessProfile = batchProcessProfile;
        this.createReqAndPOServiceHandler = createReqAndPOServiceHandler;
        this.exchange = exchange;
    }

    @Override
    public Object call() throws Exception {
        oleOrderRecords = new ArrayList<>();
        Integer purapId = null;
        for (Iterator<RecordDetails> iterator = recordDetailsList.iterator(); iterator.hasNext(); ) {
            RecordDetails recordDetails = iterator.next();
            Integer recordIndex = recordDetails.getIndex();
            try {
                OleTxRecord oleTxRecord = getOleOrderImportService().processDataMapping(recordDetails, batchProcessProfile, exchange);

                final OleOrderRecord oleOrderRecord = new OleOrderRecord();
                oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
                oleTxRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
                oleOrderRecord.setOleTxRecord(oleTxRecord);

                OleBibRecord oleBibRecord = new OleBibRecord();
                String bibUUID = recordDetails.getBibUUID();
                Bib bib = getBibDetails(bibUUID);
                oleBibRecord.setBibUUID(bibUUID);
                oleBibRecord.setBib(bib);
                oleOrderRecord.setOleBibRecord(oleBibRecord);


                oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT);

                String bibProfileName = batchProcessProfile.getBibImportProfileForOrderImport();
                oleOrderRecord.setBibImportProfileName(bibProfileName);
                oleOrderRecord.setRecordIndex(recordIndex);
                oleOrderRecords.add(oleOrderRecord);
            } catch (Exception e) {
                e.printStackTrace();
                getBatchUtil().addOrderFaiureResponseToExchange(e, recordIndex, exchange);
            }

        }

        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());

        try {
            try {
                Exchange reponseObject = (Exchange) template.execute(new TransactionCallback<Object>() {
                    Integer response;
                    Exchange exchange = new Exchange();
                    @Override
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            response = createReqAndPOServiceHandler.processOrder(oleOrderRecords, exchange);
                            exchange.add("purapId",response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return exchange;
                    }
                });
                Map context = reponseObject.getContext();
                this.exchange.getContext().putAll(context);
            } catch (Exception ex) {
                throw ex;
            } finally {
                this.transactionManager = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return purapId;
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

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService(OleNGConstants.TRANSACTION_MANAGER);
        }
        return this.transactionManager;
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
}
