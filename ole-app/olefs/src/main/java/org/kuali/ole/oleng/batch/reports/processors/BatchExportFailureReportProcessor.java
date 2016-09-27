package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.ExportFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchExportResponse;

import java.util.List;

/**
 * Created by rajeshbabuk on 4/27/16.
 */
public class BatchExportFailureReportProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBatchExportResponse oleNGBatchExportResponse = (OleNGBatchExportResponse) object;
        List<ExportFailureResponse> exportFailureResponses = oleNGBatchExportResponse.getExportFailureResponses();
        if (CollectionUtils.isNotEmpty(exportFailureResponses)) {
            String exportFailureMessages = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(exportFailureResponses);
            logMessage(directoryToWrite, "BatchExport-FailureMessages", "txt", exportFailureMessages, false);
        }
    }
}
