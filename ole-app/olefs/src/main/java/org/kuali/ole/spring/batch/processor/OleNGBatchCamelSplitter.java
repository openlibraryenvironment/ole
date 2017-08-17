package org.kuali.ole.spring.batch.processor;

import org.apache.camel.*;
import org.apache.camel.processor.ProcessorExchangePair;
import org.apache.camel.processor.Splitter;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.reports.BatchBibFailureReportLogHandler;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
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
        if(exchange.getException()!=null){
            process = false;
        }
        return process;
    }

    @Override
    protected void updateNewExchange(Exchange exchange, int index, Iterable<ProcessorExchangePair> allPairs, Iterator<ProcessorExchangePair> it) {
        exchange.setUnitOfWork(null);

        exchange.setProperty(Exchange.SPLIT_INDEX, index);
        if (allPairs instanceof Collection) {
            // non streaming mode, so we know the total size already
            exchange.setProperty(Exchange.SPLIT_SIZE, ((Collection<?>) allPairs).size());
        }
        try {
            if (it.hasNext()) {
                exchange.setProperty(Exchange.SPLIT_COMPLETE, Boolean.FALSE);
            } else {
                exchange.setProperty(Exchange.SPLIT_COMPLETE, Boolean.TRUE);
                // streaming mode, so set total size when we are complete based on the index
                exchange.setProperty(Exchange.SPLIT_SIZE, index + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setProperty(Exchange.SPLIT_COMPLETE, Boolean.TRUE);
        }
    }

    protected void doCustomProcessAfterSplit(Exchange exchange) {
        BatchProcessTxObject batchProcessTxObject = (BatchProcessTxObject) getObjectForCustomProcess();
        if(null != batchProcessTxObject) {
            BatchJobDetails batchJobDetails = batchProcessTxObject.getBatchJobDetails();
            if(null != batchJobDetails) {
                try {
                    batchProcessTxObject.getOleStopWatch().end();
                    String timeSpent = String.valueOf(batchProcessTxObject.getOleStopWatch().getTotalTime()) + "ms";
                    batchJobDetails.setTimeSpent(timeSpent);
                    batchJobDetails.setEndTime(new Timestamp(System.currentTimeMillis()));
                    batchJobDetails.setTotalRecords(String.valueOf(batchProcessTxObject.getTotalNumberOfRecords()));
                    batchJobDetails.setTotalFailureRecords(String.valueOf(batchProcessTxObject.getNumberOfFailurRecords()));
                    if(exchange.getException()!=null){
                        BatchProcessFailureResponse batchProcessFailureResponse = new BatchProcessFailureResponse();
                        batchProcessFailureResponse.setBatchProcessProfileName(batchProcessTxObject.getBatchProcessProfile().getBatchProcessProfileName());
                        batchProcessFailureResponse.setFailureReason("Unable to parse the marc Record. Allowed format is UTF-8."+exchange.getException().getLocalizedMessage());
                        batchProcessFailureResponse.setDetailedMessage(getBatchUtil().getDetailedMessage(exchange.getException()));
                        batchProcessTxObject.getBatchProcessFailureResponses().add(batchProcessFailureResponse);
                        batchProcessTxObject.setExceptionCaught(true);
                    }
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
                    if(!batchProcessTxObject.isStopped() && batchJobDetails.getJobId() != 0 && batchJobDetails.getJobDetailId() != 0) {
                        KRADServiceLocator.getBusinessObjectService().save(batchJobDetails);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getBatchUtil().BATCH_JOB_EXECUTION_DETAILS_MAP.remove(batchJobDetails.getJobId() + "_" + batchJobDetails.getJobDetailId());
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
