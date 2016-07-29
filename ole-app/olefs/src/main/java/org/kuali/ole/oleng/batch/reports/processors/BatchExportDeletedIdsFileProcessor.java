package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BatchExportFileResponse;

/**
 * Created by rajeshbabuk on 4/26/16.
 */
public class BatchExportDeletedIdsFileProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        BatchExportFileResponse batchExportFileResponse = (BatchExportFileResponse) object;
        if (OleNGConstants.TXT.equalsIgnoreCase(batchExportFileResponse.getFileType())) {
            if (CollectionUtils.isNotEmpty(batchExportFileResponse.getDeletedBibIds())) {
                String bibIds = StringUtils.join(batchExportFileResponse.getDeletedBibIds(), "\n");
                logMessage(directoryToWrite, "BatchExport_" + batchExportFileResponse.getFileName() + "_DeletedBibIds", "txt", bibIds, false);
            }
        }
    }
}