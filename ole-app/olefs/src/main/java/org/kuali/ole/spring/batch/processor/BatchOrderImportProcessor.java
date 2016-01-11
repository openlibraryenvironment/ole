package org.kuali.ole.spring.batch.processor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.callable.POCallable;
import org.kuali.ole.oleng.handler.OrderRequestHandler;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.oleng.service.impl.OrderImportServiceImpl;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
        StringBuilder stringBuilder= new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        if(CollectionUtils.isNotEmpty(records)) {
            ExecutorService orderImportExecutorService;
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("create-order-thread-%d").build();
            int numberOfThreadForOrder = 10;
            orderImportExecutorService = Executors.newFixedThreadPool(numberOfThreadForOrder, threadFactory);
            List<Future> futures = new ArrayList<>();

            for(int index = 0; index < 15; index++){
                Future future = orderImportExecutorService.submit(new POCallable(batchProcessProfile,orderRequestHandler,getOleOrderImportService()));
                futures.add(future);
            }
            for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
                Future future = iterator.next();
                try {
                    String orderImportResponse = (String) future.get();
                    appendContentToStrinBuilder(stringBuilder,orderImportResponse);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            orderImportExecutorService.shutdown();
            response = stringBuilder.toString();
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

    @Override
    public String getReportingFilePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("batch.orderRecord.directory");
    }

    private void appendContentToStrinBuilder(StringBuilder stringBuilder, String content) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("\n").append(content);
        } else {
            stringBuilder.append(content);
        }
    }
}
