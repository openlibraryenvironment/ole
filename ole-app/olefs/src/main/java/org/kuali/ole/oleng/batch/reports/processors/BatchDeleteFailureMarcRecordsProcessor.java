package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.response.OleNGBatchDeleteResponse;
import org.kuali.ole.utility.MarcRecordUtil;

/**
 * Created by rajeshbabuk on 4/12/16.
 */
public class BatchDeleteFailureMarcRecordsProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBatchDeleteResponse oleNGBatchDeleteResponse = (OleNGBatchDeleteResponse) object;
        if (CollectionUtils.isNotEmpty(oleNGBatchDeleteResponse.getFailureMarcRecords())) {
            String failureMarcRecordsContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(oleNGBatchDeleteResponse.getFailureMarcRecords());
            logMessage(directoryToWrite, oleNGBatchDeleteResponse.getProfileName() + "-FailedMarcRecords", "mrc", failureMarcRecordsContent, false);
        }
    }
}
