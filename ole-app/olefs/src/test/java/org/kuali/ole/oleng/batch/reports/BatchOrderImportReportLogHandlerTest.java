package org.kuali.ole.oleng.batch.reports;

import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGOrderImportResponse;
import org.kuali.ole.docstore.common.response.OrderData;
import org.kuali.ole.docstore.common.response.OrderResponse;
import org.kuali.ole.oleng.batch.reports.processors.BatchOrderImportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.OleNGReportProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by angelind on 2/16/16.
 */
public class BatchOrderImportReportLogHandlerTest{

    @Test
    public void testOrderImportReport() {
        OleNGOrderImportResponse oleNGOrderImportResponse = new OleNGOrderImportResponse();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProcessType(OleNGConstants.BatchProcess.CREATE_REQ_PO);
        OrderData orderData = new OrderData();
        orderData.setTitle("Test Title");
        orderData.setRecordNumber("1");
        orderData.setSuccessfulMatchPoints("Test MatchPoints");
        orderResponse.addOrderData(orderData);
        oleNGOrderImportResponse.addReqAndPOResponse(orderResponse);
        List<OleNGReportProcessor> processors = new ArrayList<>();
        processors.add(new MultipleBatchOrderImportProcessor());
        OrderImportReportLogHandler batchOrderImportReportLogHandler = OrderImportReportLogHandler.getInstance();
        batchOrderImportReportLogHandler.setProcessors(processors);
        batchOrderImportReportLogHandler.logMessage(oleNGOrderImportResponse,"4");
    }



    public class MultipleBatchOrderImportProcessor extends BatchOrderImportProcessor {
        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir") + File.separator + "reports";
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }
    }

}
