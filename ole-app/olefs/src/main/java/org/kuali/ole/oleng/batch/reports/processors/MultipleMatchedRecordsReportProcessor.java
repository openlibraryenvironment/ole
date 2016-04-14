package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.utility.MarcRecordUtil;

/**
 * Created by angelind on 2/10/16.
 */
public class MultipleMatchedRecordsReportProcessor extends OleNGReportProcessor {

    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse)object;
        if(CollectionUtils.isNotEmpty(oleNGBibImportResponse.getMultipleMatchedRecords())) {
            String multipleMatchedMarcRawContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(oleNGBibImportResponse.getMultipleMatchedRecords());
            logMessage(directoryToWrite,  oleNGBibImportResponse.getBibImportProfileName() + "-MultipleMatched","mrc", multipleMatchedMarcRawContent, true);
        }
    }
}
