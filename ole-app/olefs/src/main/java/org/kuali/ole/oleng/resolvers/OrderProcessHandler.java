package org.kuali.ole.oleng.resolvers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.callable.POCallable;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.marc4j.marc.Record;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by SheikS on 1/13/2016.
 */
public abstract class OrderProcessHandler {
    private  OleNGRequisitionService oleNGRequisitionService;

    private static final Logger LOG = Logger.getLogger(OrderProcessHandler.class);

    public abstract boolean isInterested(List<String> options, Boolean matchedRecords, Boolean unMatchedRecords);

    public abstract CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler();

    public List<Integer> processOrder(Map<String, Record> recordMap, BatchProcessProfile batchProcessProfile, CreateReqAndPOServiceHandler orderRequestHandler) throws Exception {
        List<Integer> poIds = new ArrayList<>();

        ExecutorService orderImportExecutorService;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("create-order-thread-%d").build();
        int numberOfThreadForOrder = 1;
        orderImportExecutorService = Executors.newFixedThreadPool(numberOfThreadForOrder, threadFactory);
        List<Future> futures = new ArrayList<>();

        Set<String> bibIds = recordMap.keySet();
        for (Iterator<String> iterator = bibIds.iterator(); iterator.hasNext(); ) {
            String bibId = iterator.next();
            Future future = orderImportExecutorService.submit(new POCallable(bibId, batchProcessProfile, getCreateReqOrPOServiceHandler()));
            futures.add(future);
        }

        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                String orderImportResponse = (String) future.get();
                poIds.add(Integer.valueOf(orderImportResponse));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        orderImportExecutorService.shutdown();
        return poIds;
    }


    public int getNumberOfThreadForOrderImport() {
        String maxNumberOfThreadFromParameter = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, OLEConstants.MAX_NO_OF_THREAD_FOR_ORDER_IMPORT);
        int maxNumberOfThread = 0;
        try {
            maxNumberOfThread = Integer.parseInt(maxNumberOfThreadFromParameter);
        } catch (Exception exception) {
            LOG.error("Invalid max number of thread for order import service from system parameter.");
        }
        return maxNumberOfThread > 0 ? maxNumberOfThread : 10;
    }

    public OleNGRequisitionService getOleNGRequisitionService() {
        return oleNGRequisitionService;
    }

    public void setOleNGRequisitionService(OleNGRequisitionService oleNGRequisitionService) {
        this.oleNGRequisitionService = oleNGRequisitionService;
    }
}
