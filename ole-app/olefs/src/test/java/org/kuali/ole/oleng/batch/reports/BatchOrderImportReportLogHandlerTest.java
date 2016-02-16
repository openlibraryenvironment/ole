package org.kuali.ole.oleng.batch.reports;

import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGOrderImportResponse;
import org.kuali.ole.docstore.common.response.OrderData;
import org.kuali.ole.docstore.common.response.OrderResponse;


/**
 * Created by angelind on 2/16/16.
 */
public class BatchOrderImportReportLogHandlerTest extends OLETestCaseBase{

    @Test
    public void testOrderImportReport() {
        OleNGOrderImportResponse oleNGOrderImportResponse = new OleNGOrderImportResponse();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProcessType(OleNGConstants.BatchProcess.CREATE_REQ_PO);
        OrderData orderData = new OrderData();
        orderData.setTitle("Test Title");
        orderData.setRecordNumber("1");
        orderData.setSuccessfulMatchPoints("Test MatchPoints");
        orderData.setRequisitionNumber("1");
        orderResponse.addOrderData(orderData);
        oleNGOrderImportResponse.addReqAndPOResponse(orderResponse);
        BatchOrderImportReportLogHandler batchOrderImportReportLogHandler = BatchOrderImportReportLogHandler.getInstance();
        batchOrderImportReportLogHandler.logMessage(oleNGOrderImportResponse);
    }

}
