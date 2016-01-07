package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONException;
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
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 1/6/2016.
 */
@Service("batchOrderImportProcessor")
public class BatchOrderImportProcessor extends BatchFileProcessor {

    private OrderImportService oleOrderImportService;

    @Autowired
    private OrderRequestHandler orderRequestHandler;

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        String response = "";
        JSONObject jsonObject = new JSONObject();
        if(CollectionUtils.isNotEmpty(records)) {
            for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
                Record record = iterator.next();

                OleTxRecord oleTxRecord = new OleTxRecord();
                oleTxRecord = getOleOrderImportService().processDataMapping(oleTxRecord, batchProcessProfile);

                OleOrderRecord oleOrderRecord = new OleOrderRecord();
                oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
                oleTxRecord.setOrgCode("LIB");
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
        } else {
            jsonObject.put("status", "failure");
            jsonObject.put("reason", "Invalid record.");
            response = jsonObject.toString();
        }
        return response;
    }

    public OrderImportService getOleOrderImportService() {
        if(null == oleOrderImportService) {
            oleOrderImportService = new OrderImportServiceImpl();
        }
        return oleOrderImportService;
    }

    public void setOleOrderImportService(OrderImportService oleOrderImportService) {
        this.oleOrderImportService = oleOrderImportService;
    }
}
