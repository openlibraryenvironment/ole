package org.kuali.ole.oleng.util;

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
import org.kuali.ole.docstore.common.response.OrderFailureResponse;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.oleng.service.impl.OrderImportServiceImpl;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.OleSelectConstant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by SheikS on 3/10/2016.
 */
public class OleNGPOHelperUtil {
    private static final Logger LOG = Logger.getLogger(OleNGPOHelperUtil.class);
    private static  OrderImportService oleOrderImportService;
    private SolrRequestReponseHandler solrRequestReponseHandler;

    private static OleNGPOHelperUtil oleNGPOHelperUtil;

    private OleNGPOHelperUtil() {
    }

    public static OleNGPOHelperUtil getInstance() {
        if(null == oleNGPOHelperUtil) {
            oleNGPOHelperUtil = new OleNGPOHelperUtil();
        }
        return oleNGPOHelperUtil;
    }

    public Integer processReqAndPo(Set<String> bibIds, BatchProcessProfile batchProcessProfile,
                                   CreateReqAndPOBaseServiceHandler createReqAndPOServiceHandler, Exchange exchange)  {
        List<OleOrderRecord> oleOrderRecords = new ArrayList<>();
        List<OrderFailureResponse> orderFailureResponses = new ArrayList<>();
        Integer purapId = null;
        for (Iterator<String> iterator = bibIds.iterator(); iterator.hasNext(); ) {
            try {
                String bibId = iterator.next();
                OleTxRecord oleTxRecord = getOleOrderImportService().processDataMapping(bibId, batchProcessProfile);

                final OleOrderRecord oleOrderRecord = new OleOrderRecord();
                oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
                oleTxRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
                oleOrderRecord.setOleTxRecord(oleTxRecord);

                OleBibRecord oleBibRecord = new OleBibRecord();
                Bib bib = getBibDetails(bibId);
                oleBibRecord.setBibUUID(bibId);
                oleBibRecord.setBib(bib);
                oleOrderRecord.setOleBibRecord(oleBibRecord);

                oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT);

                String bibProfileName = batchProcessProfile.getBibImportProfileForOrderImport();
                oleOrderRecord.setBibImportProfileName(bibProfileName);
                oleOrderRecords.add(oleOrderRecord);
            } catch (Exception e) {
                e.printStackTrace();
                OrderFailureResponse orderFailureResponse = new OrderFailureResponse();
                orderFailureResponse.setFailureMessage(e.getMessage());
                orderFailureResponses.add(orderFailureResponse);
            }

        }
        try {
            if(CollectionUtils.isNotEmpty(oleOrderRecords)) {
                purapId = createReqAndPOServiceHandler.processOrder(oleOrderRecords);
            }
        } catch (Exception e) {
            e.printStackTrace();
            OrderFailureResponse orderFailureResponse = new OrderFailureResponse();
            orderFailureResponse.setFailureMessage(e.getMessage());
            orderFailureResponses.add(orderFailureResponse);
        }
        exchange.add(OleNGConstants.FAILURE_RESPONSE,orderFailureResponses);
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

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }
}
