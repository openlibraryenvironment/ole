package org.kuali.ole.oleng.callable;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.OrderRequestHandler;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.oleng.service.impl.OrderImportServiceImpl;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Callable;

/**
 * Created by SheikS on 1/10/2016.
 */
public class POCallable implements Callable {

    private static final Logger LOG = Logger.getLogger(POCallable.class);
    private final String bibId;

    private BatchProcessProfile batchProcessProfile;
    private OrderImportService oleOrderImportService;
    private OrderRequestHandler orderRequestHandler;
    private PlatformTransactionManager transactionManager;

    public POCallable(String bibId, BatchProcessProfile batchProcessProfile, OrderRequestHandler orderRequestHandler) {
        this.bibId = bibId;
        this.batchProcessProfile = batchProcessProfile;
        this.orderRequestHandler = orderRequestHandler;
    }

    @Override
    public Object call() throws Exception {
        String finalResponse = "";
        final JSONObject jsonObject = new JSONObject();
        OleTxRecord oleTxRecord = new OleTxRecord();
        oleTxRecord = getOleOrderImportService().processDataMapping(oleTxRecord, batchProcessProfile);

        final OleOrderRecord oleOrderRecord = new OleOrderRecord();
        oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
        oleOrderRecord.setOleTxRecord(oleTxRecord);

        Bib bib = new Bib();
        bib.setTitle("Test Bib For req");
        bib.setAuthor("Author");
        bib.setPublisher("Publisher");
        bib.setIsbn("1010101010");
        OleBibRecord oleBibRecord = new OleBibRecord();
        oleBibRecord.setBibUUID(bibId);
        oleBibRecord.setBib(bib);
        oleOrderRecord.setOleBibRecord(oleBibRecord);

        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());

        try {
            try {
                finalResponse = (String) template.execute(new TransactionCallback<Object>() {
                    String response;

                    @Override
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            response = orderRequestHandler.processOrder(oleOrderRecord);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return response;
                    }
                });
            } catch (Exception ex) {
                throw ex;
            } finally {
                this.transactionManager = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("status", "failure");
            jsonObject.put("reason", e.getMessage());
            finalResponse = jsonObject.toString();

        }
        System.out.println("Response : \n" + finalResponse);

        return finalResponse;
    }

    public OrderImportService getOleOrderImportService() {
        if (null == oleOrderImportService) {
            oleOrderImportService = new OrderImportServiceImpl();
        }
        return oleOrderImportService;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}
