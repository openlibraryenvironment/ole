package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.DeleteFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchDeleteResponse;

import java.util.List;

/**
 * Created by rajeshbabuk on 4/7/16.
 */
public class BatchDeleteFailureReportProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBatchDeleteResponse oleNGBatchDeleteResponse = (OleNGBatchDeleteResponse) object;
        List<DeleteFailureResponse> deleteFailureResponses = oleNGBatchDeleteResponse.getDeleteFailureResponseList();
        if (CollectionUtils.isNotEmpty(deleteFailureResponses)) {
            String deleteFailureMessages = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(deleteFailureResponses);
            logMessage(directoryToWrite, "BatchDelete-FailureMessages", "txt", deleteFailureMessages, false);
        }
    }
}
