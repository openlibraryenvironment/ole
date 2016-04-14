package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;

import java.util.Iterator;
import java.util.List;

/**
 * Created by angelind on 2/11/16.
 */
public class FailedMarcContentProcessor extends OleNGReportProcessor {
    public void process(Object object, String directoryToWrite) throws Exception {
        List<BatchProcessFailureResponse> batchProcessFailureResponse = (List<BatchProcessFailureResponse>) object;
        StringBuilder stringBuilder = new StringBuilder();
        String profileName = "";
        if(CollectionUtils.isNotEmpty(batchProcessFailureResponse)) {
            profileName = batchProcessFailureResponse.get(0).getBatchProcessProfileName();
            for (Iterator<BatchProcessFailureResponse> iterator = batchProcessFailureResponse.iterator(); iterator.hasNext(); ) {
                BatchProcessFailureResponse processFailureResponse = iterator.next();
                String failedRawMarcContent = processFailureResponse.getFailedRawMarcContent();
                if(StringUtils.isNotBlank(failedRawMarcContent)) {
                    stringBuilder.append(failedRawMarcContent);
                }
            }
        }

        if(StringUtils.isNotBlank(stringBuilder.toString())) {
            logMessage(directoryToWrite, profileName + "-FailedInputData","mrc", stringBuilder.toString(), true);
        }
    }
}
