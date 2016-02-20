package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGOrderImportResponse;
import org.kuali.ole.docstore.common.response.OrderData;
import org.kuali.ole.docstore.common.response.OrderResponse;

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
        OrderImportReportLogHandler batchOrderImportReportLogHandler = new MockOrderImportReportLogHandler("4","OrderImport");
        batchOrderImportReportLogHandler.logMessage(oleNGOrderImportResponse);
    }

    public class MockOrderImportReportLogHandler extends OrderImportReportLogHandler {

        public MockOrderImportReportLogHandler(String directoryName, String profileName) {
            super(directoryName, profileName);
        }

        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir");
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }
    }

}
