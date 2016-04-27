package org.kuali.ole.spring.batch.processor;

import org.apache.camel.*;
import org.apache.camel.processor.Splitter;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.reports.BatchBibFailureReportLogHandler;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;

/**
 * Created by SheikS on 4/10/2016.
 */
public class OleNGBatchCamelSplitter extends Splitter {
    private Object objectForCustomProcess;
    private BatchUtil batchUtil;

    public OleNGBatchCamelSplitter(Object objectForCustomProcess, CamelContext camelContext, Expression expression, Processor destination, AggregationStrategy aggregationStrategy,
                                   boolean parallelProcessing, ExecutorService executorService, boolean shutdownExecutorService,
                                   boolean streaming, boolean stopOnException, long timeout, Processor onPrepare, boolean useSubUnitOfWork,
                                   boolean parallelAggregate) {
        super(camelContext, expression, destination, aggregationStrategy,
                parallelProcessing, executorService, shutdownExecutorService, streaming, stopOnException,
                timeout, onPrepare, useSubUnitOfWork, parallelAggregate);
        this.objectForCustomProcess = objectForCustomProcess;
    }

    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        boolean process = super.process(exchange, callback);
        doCustomProcessAfterSplit(exchange);
        return process;
    }

    protected void doCustomProcessAfterSplit(Exchange exchange) {
        BatchProcessTxObject batchProcessTxObject = (BatchProcessTxObject) getObjectForCustomProcess();
        if(null != batchProcessTxObject) {
            BatchJobDetails batchJobDetails = batchProcessTxObject.getBatchJobDetails();
            if(null != batchJobDetails) {
                batchProcessTxObject.getOleStopWatch().end();
                String timeSpent = String.valueOf(batchProcessTxObject.getOleStopWatch().getTotalTime()) + "ms";
                batchJobDetails.setTimeSpent(timeSpent);
                batchJobDetails.setEndTime(new Timestamp(System.currentTimeMillis()));
                batchJobDetails.setTotalRecords(String.valueOf(batchProcessTxObject.getTotalNumberOfRecords()));
                batchJobDetails.setTotalFailureRecords(String.valueOf(batchProcessTxObject.getNumberOfFailurRecords()));
                if(batchProcessTxObject.isExceptionCaught()) {
                    batchJobDetails.setStatus(OleNGConstants.FAILED);
                    try {
                        BatchBibFailureReportLogHandler batchBibFailureReportLogHandler = BatchBibFailureReportLogHandler.getInstance();
                        batchBibFailureReportLogHandler.logMessage(batchProcessTxObject.getBatchProcessFailureResponses(),batchProcessTxObject.getReportDirectoryName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    batchJobDetails.setStatus(OleNGConstants.COMPLETED);
                }
                getBatchUtil().writeBatchRunningStatusToFile(batchProcessTxObject.getIncomingFileDirectoryPath(), batchJobDetails.getStatus(), timeSpent);
                if(batchJobDetails.getJobId() != 0 && batchJobDetails.getJobDetailId() != 0) {
                    KRADServiceLocator.getBusinessObjectService().save(batchJobDetails);
                }
            }
        }

    }
    public Object getObjectForCustomProcess() {
        return objectForCustomProcess;
    }

    public void setObjectForCustomProcess(Object objectForCustomProcess) {
        this.objectForCustomProcess = objectForCustomProcess;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }
}
