package org.kuali.ole.oleng.callable;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.OrderRequestHandler;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

/**
 * Created by SheikS on 1/10/2016.
 */
public class POCallable implements Callable {

    private static final Logger LOG = Logger.getLogger(POCallable.class);

    private BatchProcessProfile batchProcessProfile;
    private OrderImportService oleOrderImportService;
    private OrderRequestHandler orderRequestHandler;

    public POCallable(BatchProcessProfile batchProcessProfile,OrderRequestHandler orderRequestHandler, OrderImportService oleOrderImportService) {
        this.batchProcessProfile = batchProcessProfile;
        this.orderRequestHandler = orderRequestHandler;
        this.oleOrderImportService = oleOrderImportService;
    }

    @Override
    public Object call() throws Exception {
        String response = "";
        JSONObject jsonObject = new JSONObject();
        OleTxRecord oleTxRecord = new OleTxRecord();
        oleTxRecord = oleOrderImportService.processDataMapping(oleTxRecord, batchProcessProfile);

        OleOrderRecord oleOrderRecord = new OleOrderRecord();
        oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
        oleOrderRecord.setOleTxRecord(oleTxRecord);

        Bib bib = new Bib();
        bib.setTitle("Test Bib For req");
        bib.setAuthor("Author");
        bib.setPublisher("Publisher");
        bib.setIsbn("1010101010");
        OleBibRecord oleBibRecord = new OleBibRecord();
        oleBibRecord.setBibUUID("wbm-10362693");
        oleBibRecord.setBib(bib);
        oleOrderRecord.setOleBibRecord(oleBibRecord);

        try {

            response = orderRequestHandler.processOrder(oleOrderRecord);

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("status", "failure");
            jsonObject.put("reason", e.getMessage());
            response = jsonObject.toString();
        }
        System.out.println("Response : \n" + response);

        return response;
    }
}
