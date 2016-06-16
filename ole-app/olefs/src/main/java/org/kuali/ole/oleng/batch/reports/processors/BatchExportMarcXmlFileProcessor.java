package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BatchExportFileResponse;
import org.kuali.ole.utility.MarcRecordUtil;

/**
 * Created by rajeshbabuk on 4/26/16.
 */
public class BatchExportMarcXmlFileProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        BatchExportFileResponse batchExportFileResponse = (BatchExportFileResponse) object;
        if ("MarcXml".equalsIgnoreCase(batchExportFileResponse.getFileType())) {
            if (CollectionUtils.isNotEmpty(batchExportFileResponse.getMarcRecords())) {
                String marcContent = new MarcRecordUtil().convertMarcRecordListToMarcXmlContent(batchExportFileResponse.getMarcRecords());
                logMessage(directoryToWrite, batchExportFileResponse.getFileName() + "_Part" + batchExportFileResponse.getFileNumber(), OleNGConstants.XML, marcContent, Boolean.TRUE);
            }
        }
    }
}