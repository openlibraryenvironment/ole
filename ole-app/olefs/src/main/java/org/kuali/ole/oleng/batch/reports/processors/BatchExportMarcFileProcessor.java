package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BatchExportFileResponse;
import org.kuali.ole.utility.MarcRecordUtil;

import java.util.List;

/**
 * Created by rajeshbabuk on 4/26/16.
 */
public class BatchExportMarcFileProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        BatchExportFileResponse batchExportFileResponse = (BatchExportFileResponse) object;
        if ("Marc".equalsIgnoreCase(batchExportFileResponse.getFileType())) {
            if (CollectionUtils.isNotEmpty(batchExportFileResponse.getMarcRecords())) {
                String marcContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(batchExportFileResponse.getMarcRecords());
                logMessage(directoryToWrite, batchExportFileResponse.getFileName() + "_Part" + batchExportFileResponse.getFileNumber(), OleNGConstants.MARC, marcContent, Boolean.TRUE);
            }
        }
    }
}