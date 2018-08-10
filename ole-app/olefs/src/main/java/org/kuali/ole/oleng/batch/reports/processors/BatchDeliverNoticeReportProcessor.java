package org.kuali.ole.oleng.batch.reports.processors;

import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;

public class BatchDeliverNoticeReportProcessor  extends OleNGReportProcessor{

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBatchNoticeResponse oleNGBatchNoticeResponse = (OleNGBatchNoticeResponse) object;
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGBatchNoticeResponse);
        logMessage(directoryToWrite,  "BatchNotice","txt", message, false);
    }
}
